/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.journal.lar;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.BasePortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.persistence.DDMStructureActionableDynamicQuery;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalFeed;
import com.liferay.portlet.journal.model.JournalFolder;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.persistence.JournalArticleActionableDynamicQuery;
import com.liferay.portlet.journal.service.persistence.JournalFeedActionableDynamicQuery;
import com.liferay.portlet.journal.service.persistence.JournalFolderActionableDynamicQuery;

import java.util.Collections;
import java.util.List;

import javax.portlet.PortletPreferences;

/**
 * <p>
 * Provides the Journal portlet export and import functionality, which is to
 * clone all articles, structures, and templates associated with the layout's
 * group. Upon import, new instances of the corresponding articles, structures,
 * and templates are created or updated according to the DATA_MIRROW strategy
 * The author of the newly created objects are determined by the
 * JournalCreationStrategy class defined in <i>portal.properties</i>. That
 * strategy also allows the text of the journal article to be modified prior to
 * import.
 * </p>
 *
 * <p>
 * This <code>PortletDataHandler</code> differs from
 * <code>JournalContentPortletDataHandlerImpl</code> in that it exports all
 * articles owned by the group whether or not they are actually displayed in a
 * portlet in the layout set.
 * </p>
 *
 * @author Raymond Augé
 * @author Joel Kozikowski
 * @author Brian Wing Shun Chan
 * @author Bruno Farache
 * @author Karthik Sudarshan
 * @author Wesley Gong
 * @author Hugo Huijser
 * @author Daniel Kocsis
 * @see    com.liferay.portal.kernel.lar.PortletDataHandler
 * @see    com.liferay.portlet.journal.lar.JournalContentPortletDataHandler
 * @see    com.liferay.portlet.journal.lar.JournalCreationStrategy
 */
public class JournalPortletDataHandler extends BasePortletDataHandler {

	public static final String NAMESPACE = "journal";

	public JournalPortletDataHandler() {
		setAlwaysExportable(true);
		setDataLocalized(true);
		setExportControls(
			new PortletDataHandlerBoolean(NAMESPACE, "web-content"),
			new PortletDataHandlerBoolean(
				NAMESPACE, "structures-templates-and-feeds", true, true),
			new PortletDataHandlerBoolean(NAMESPACE, "embedded-assets"),
			new PortletDataHandlerBoolean(
				NAMESPACE, "version-history",
				PropsValues.JOURNAL_PUBLISH_VERSION_HISTORY_BY_DEFAULT));
		setExportMetadataControls(
			new PortletDataHandlerBoolean(
				NAMESPACE, "web-content", true,
				new PortletDataHandlerControl[] {
					new PortletDataHandlerBoolean(NAMESPACE, "images"),
					new PortletDataHandlerBoolean(NAMESPACE, "categories"),
					new PortletDataHandlerBoolean(NAMESPACE, "comments"),
					new PortletDataHandlerBoolean(NAMESPACE, "ratings"),
					new PortletDataHandlerBoolean(NAMESPACE, "tags")
				}));
		setImportControls(getExportControls()[0], getExportControls()[1]);
		setPublishToLiveByDefault(
			PropsValues.JOURNAL_PUBLISH_TO_LIVE_BY_DEFAULT);
	}

	@Override
	protected PortletPreferences doDeleteData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		if (portletDataContext.addPrimaryKey(
				JournalPortletDataHandler.class, "deleteData")) {

			return portletPreferences;
		}

		JournalArticleLocalServiceUtil.deleteArticles(
			portletDataContext.getScopeGroupId());

		DDMTemplateLocalServiceUtil.deleteTemplates(
			portletDataContext.getScopeGroupId());

		DDMStructureLocalServiceUtil.deleteStructures(
			portletDataContext.getScopeGroupId());

		return portletPreferences;
	}

	@Override
	protected String doExportData(
			final PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		portletDataContext.addPermissions(
			RESOURCE_NAME, portletDataContext.getScopeGroupId());

		Element rootElement = addExportDataRootElement(portletDataContext);

		rootElement.addAttribute(
			"group-id", String.valueOf(portletDataContext.getScopeGroupId()));

		ActionableDynamicQuery structureActionableDynamicQuery =
			new DDMStructureActionableDynamicQuery() {

			@Override
			protected void addCriteria(DynamicQuery dynamicQuery) {
				portletDataContext.addDateRangeCriteria(
					dynamicQuery, "modifiedDate");

				Property classNameIdProperty = PropertyFactoryUtil.forName(
					"classNameId");

				long classNameId = PortalUtil.getClassNameId(
					JournalArticle.class);

				dynamicQuery.add(classNameIdProperty.eq(classNameId));
			}

			@Override
			protected void performAction(Object object) throws PortalException {
				DDMStructure ddmStructure = (DDMStructure)object;

				StagedModelDataHandlerUtil.exportStagedModel(
					portletDataContext, ddmStructure);

				List<DDMTemplate> ddmTemplates = Collections.emptyList();

				try {
					ddmTemplates = ddmStructure.getTemplates();
				}
				catch (SystemException se) {
				}

				for (DDMTemplate ddmTemplate : ddmTemplates) {
					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, ddmTemplate);
				}
			}

		};

		structureActionableDynamicQuery.setGroupId(
			portletDataContext.getScopeGroupId());

		structureActionableDynamicQuery.performActions();

		ActionableDynamicQuery feedActionableDynamicQuery =
			new JournalFeedActionableDynamicQuery() {

			@Override
			protected void addCriteria(DynamicQuery dynamicQuery) {
				portletDataContext.addDateRangeCriteria(
					dynamicQuery, "modifiedDate");
			}

			@Override
			protected void performAction(Object object) throws PortalException {
				JournalFeed feed = (JournalFeed)object;

				StagedModelDataHandlerUtil.exportStagedModel(
					portletDataContext, feed);
			}

		};

		feedActionableDynamicQuery.setGroupId(
			portletDataContext.getScopeGroupId());

		feedActionableDynamicQuery.performActions();

		if (!portletDataContext.getBooleanParameter(NAMESPACE, "web-content")) {
			getExportDataRootElementString(rootElement);
		}

		ActionableDynamicQuery folderActionableDynamicQuery =
			new JournalFolderActionableDynamicQuery() {

			@Override
			protected void addCriteria(DynamicQuery dynamicQuery) {
				portletDataContext.addDateRangeCriteria(
					dynamicQuery, "modifiedDate");

				Property statusProperty = PropertyFactoryUtil.forName("status");

				dynamicQuery.add(
					statusProperty.ne(WorkflowConstants.STATUS_IN_TRASH));
			}

			@Override
			protected void performAction(Object object) throws PortalException {
				JournalFolder folder = (JournalFolder)object;

				StagedModelDataHandlerUtil.exportStagedModel(
					portletDataContext, folder);
			}

		};

		folderActionableDynamicQuery.setGroupId(
			portletDataContext.getScopeGroupId());

		folderActionableDynamicQuery.performActions();

		ActionableDynamicQuery articleActionableDynamicQuery =
			new JournalArticleActionableDynamicQuery() {

			@Override
			protected void addCriteria(DynamicQuery dynamicQuery) {
				portletDataContext.addDateRangeCriteria(
					dynamicQuery, "modifiedDate");

				Property statusProperty = PropertyFactoryUtil.forName("status");

				dynamicQuery.add(
					statusProperty.in(
						new Integer[] {
							WorkflowConstants.STATUS_APPROVED,
							WorkflowConstants.STATUS_EXPIRED
						}));
			}

			@Override
			protected void performAction(Object object) throws PortalException {
				JournalArticle article = (JournalArticle)object;

				boolean latestVersion = false;

				try {
					latestVersion =
						JournalArticleLocalServiceUtil.isLatestVersion(
							article.getGroupId(), article.getArticleId(),
							article.getVersion(),
							WorkflowConstants.STATUS_APPROVED);
				}
				catch (Exception e) {
				}

				if (portletDataContext.getBooleanParameter(
						NAMESPACE, "version-history") || latestVersion) {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, article);
				}
			}

		};

		articleActionableDynamicQuery.setGroupId(
			portletDataContext.getScopeGroupId());

		articleActionableDynamicQuery.performActions();

		return getExportDataRootElementString(rootElement);
	}

	@Override
	protected PortletPreferences doImportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, String data)
		throws Exception {

		portletDataContext.importPermissions(
			RESOURCE_NAME, portletDataContext.getSourceGroupId(),
			portletDataContext.getScopeGroupId());

		Element ddmStructuresElement =
			portletDataContext.getImportDataGroupElement(DDMStructure.class);

		List<Element> ddmStructureElements = ddmStructuresElement.elements();

		for (Element ddmStructureElement : ddmStructureElements) {
			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, ddmStructureElement);
		}

		Element ddmTemplatesElement =
			portletDataContext.getImportDataGroupElement(DDMTemplate.class);

		List<Element> ddmTemplateElements = ddmTemplatesElement.elements();

		for (Element ddmTemplateElement : ddmTemplateElements) {
			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, ddmTemplateElement);
		}

		Element feedsElement = portletDataContext.getImportDataGroupElement(
			JournalFeed.class);

		List<Element> feedElements = feedsElement.elements();

		for (Element feedElement : feedElements) {
			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, feedElement);
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "web-content")) {
			Element foldersElement =
				portletDataContext.getImportDataGroupElement(
					JournalFolder.class);

			List<Element> folderElements = foldersElement.elements();

			for (Element folderElement : folderElements) {
				StagedModelDataHandlerUtil.importStagedModel(
					portletDataContext, folderElement);
			}

			Element articlesElement =
				portletDataContext.getImportDataGroupElement(
					JournalArticle.class);

			List<Element> articleElements = articlesElement.elements();

			for (Element articleElement : articleElements) {
				StagedModelDataHandlerUtil.importStagedModel(
					portletDataContext, articleElement);
			}
		}

		return portletPreferences;
	}

	protected static final String RESOURCE_NAME = "com.liferay.portlet.journal";

}