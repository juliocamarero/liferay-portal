/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.dynamicdatalists.lar;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.lar.BasePortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetLocalServiceUtil;
import com.liferay.portlet.dynamicdatalists.service.persistence.DDLRecordSetActionableDynamicQuery;
import com.liferay.portlet.dynamicdatamapping.lar.DDMPortletDataHandler;

import java.util.List;

import javax.portlet.PortletPreferences;

/**
 * @author Michael C. Han
 * @author Mate Thurzo
 */
public class DDLPortletDataHandler extends BasePortletDataHandler {

	public static final String NAMESPACE = "ddl";

	public DDLPortletDataHandler() {
		setAlwaysExportable(true);
		setDataLocalized(true);
	}

	@Override
	protected PortletPreferences doDeleteData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		if (!portletDataContext.addPrimaryKey(
				DDLPortletDataHandler.class, "deleteData")) {

			DDLRecordSetLocalServiceUtil.deleteRecordSets(
				portletDataContext.getScopeGroupId());
		}

		return portletPreferences;
	}

	@Override
	protected String doExportData(
			final PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		portletDataContext.addPermissions(
			"com.liferay.portlet.dynamicdatalist",
			portletDataContext.getScopeGroupId());

		Document document = SAXReaderUtil.createDocument();

		Element rootElement = document.addElement("ddl-data");

		final Element recordSetsElement = rootElement.addElement("record-sets");
		final Element ddmStructuresElement = rootElement.addElement(
			"ddm-structures");
		final Element ddmTemplatesElement = rootElement.addElement(
			"ddm-templates");

		ActionableDynamicQuery recordSetsActionableDynamicQuery =
			new DDLRecordSetActionableDynamicQuery() {

			@Override
			protected void addCriteria(DynamicQuery dynamicQuery) {
				portletDataContext.addDateRangeCriteria(
					dynamicQuery, "modifiedDate");
			}

			@Override
			protected void performAction(Object object) throws PortalException {
				DDLRecordSet recordSet = (DDLRecordSet)object;

				StagedModelDataHandlerUtil.exportStagedModel(
					portletDataContext,
					new Element[] {
						recordSetsElement, ddmStructuresElement,
						ddmTemplatesElement
					},
					recordSet);
			}

		};

		recordSetsActionableDynamicQuery.setGroupId(
			portletDataContext.getScopeGroupId());

		recordSetsActionableDynamicQuery.performActions();

		return document.formattedString();
	}

	@Override
	protected PortletPreferences doImportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, String data)
		throws Exception {

		portletDataContext.importPermissions(
			"com.liferay.portlet.dynamicdatalist",
			portletDataContext.getSourceGroupId(),
			portletDataContext.getScopeGroupId());

		Document document = SAXReaderUtil.read(data);

		Element rootElement = document.getRootElement();

		// DDM Structures

		Element ddmStructuresElement = rootElement.element("ddm-structures");

		importDDMStructures(portletDataContext, ddmStructuresElement);

		// DDM Templates

		Element ddmTemplatesElement = rootElement.element("ddm-templates");

		importDDMTemplates(portletDataContext, ddmTemplatesElement);

		// DDL Record Sets

		Element recordSetsElement = rootElement.element("record-sets");

		List<Element> recordSetElements = recordSetsElement.elements(
			"record-set");

		for (Element recordSetElement : recordSetElements) {
			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, recordSetElement);
		}

		return portletPreferences;
	}

	protected String getRecordSetPath(
		PortletDataContext portletDataContext, DDLRecordSet recordSet) {

		StringBundler sb = new StringBundler(4);

		sb.append(
			portletDataContext.getPortletPath(PortletKeys.DYNAMIC_DATA_LISTS));
		sb.append("/record-sets/");
		sb.append(recordSet.getRecordSetId());
		sb.append(".xml");

		return sb.toString();
	}

	protected void importDDMStructures(
			PortletDataContext portletDataContext,
			Element ddmStructureReferencesElement)
		throws Exception {

		List<Element> ddmStructureElements =
			ddmStructureReferencesElement.elements("structure");

		for (Element ddmStructureElement : ddmStructureElements) {
			DDMPortletDataHandler.importStructure(
				portletDataContext, ddmStructureElement);
		}
	}

	protected void importDDMTemplates(
			PortletDataContext portletDataContext,
			Element ddmTemplateReferencesElement)
		throws Exception {

		List<Element> ddmTemplateElements =
			ddmTemplateReferencesElement.elements("template");

		for (Element ddmTemplateElement : ddmTemplateElements) {
			DDMPortletDataHandler.importTemplate(
				portletDataContext, ddmTemplateElement);
		}
	}

}