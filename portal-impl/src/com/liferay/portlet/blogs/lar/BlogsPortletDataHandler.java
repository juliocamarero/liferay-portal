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

package com.liferay.portlet.blogs.lar;

import com.liferay.portal.kernel.lar.BasePortletDataHandler;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.blogs.service.BlogsStatsUserLocalServiceUtil;
import com.liferay.portlet.blogs.service.persistence.BlogsEntryUtil;
import com.liferay.portlet.journal.lar.JournalPortletDataHandler;

import java.util.List;

import javax.portlet.PortletPreferences;

/**
 * @author Bruno Farache
 * @author Raymond Augé
 * @author Juan Fernández
 */
public class BlogsPortletDataHandler extends BasePortletDataHandler {

	public static final String NAMESPACE = "blogs";

	public BlogsPortletDataHandler() {
		setAlwaysExportable(true);
		setExportControls(
			new PortletDataHandlerBoolean(NAMESPACE, "entries", true, true));
		setExportMetadataControls(
			new PortletDataHandlerBoolean(
				NAMESPACE, "blog-entries", true,
				new PortletDataHandlerControl[] {
					new PortletDataHandlerBoolean(NAMESPACE, "categories"),
					new PortletDataHandlerBoolean(NAMESPACE, "comments"),
					new PortletDataHandlerBoolean(NAMESPACE, "ratings"),
					new PortletDataHandlerBoolean(NAMESPACE, "tags")
				}));
		setImportMetadataControls(
			getExportMetadataControls()[0],
			new PortletDataHandlerBoolean(NAMESPACE, "wordpress"));
		setPublishToLiveByDefault(PropsValues.BLOGS_PUBLISH_TO_LIVE_BY_DEFAULT);
	}

	@Override
	protected PortletPreferences doDeleteData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		if (portletDataContext.addPrimaryKey(
				BlogsPortletDataHandler.class, "deleteData")) {

			return portletPreferences;
		}

		BlogsEntryLocalServiceUtil.deleteEntries(
			portletDataContext.getScopeGroupId());

		BlogsStatsUserLocalServiceUtil.deleteStatsUserByGroupId(
			portletDataContext.getScopeGroupId());

		return portletPreferences;
	}

	@Override
	protected String doExportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		portletDataContext.addPermissions(
			"com.liferay.portlet.blogs", portletDataContext.getScopeGroupId());

		Element rootElement = addExportDataRootElement(portletDataContext);

		rootElement.addAttribute(
			"group-id", String.valueOf(portletDataContext.getScopeGroupId()));

		Element entriesElement = rootElement.addElement("entries");

		Element dlFileEntryTypesElement = entriesElement.addElement(
			"dl-file-entry-types");
		Element dlFoldersElement = entriesElement.addElement("dl-folders");
		Element dlFileEntriesElement = entriesElement.addElement(
			"dl-file-entries");
		Element dlFileRanksElement = entriesElement.addElement("dl-file-ranks");
		Element dlRepositoriesElement = entriesElement.addElement(
			"dl-repositories");
		Element dlRepositoryEntriesElement = entriesElement.addElement(
			"dl-repository-entries");

		List<BlogsEntry> entries = BlogsEntryUtil.findByGroupId(
			portletDataContext.getScopeGroupId());

		for (BlogsEntry entry : entries) {
			exportEntry(
				portletDataContext, entriesElement, dlFileEntryTypesElement,
				dlFoldersElement, dlFileEntriesElement, dlFileRanksElement,
				dlRepositoriesElement, dlRepositoryEntriesElement, entry);
		}

		return getExportDataRootElementString(rootElement);
	}

	@Override
	protected PortletPreferences doImportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, String data)
		throws Exception {

		portletDataContext.importPermissions(
			"com.liferay.portlet.blogs", portletDataContext.getSourceGroupId(),
			portletDataContext.getScopeGroupId());

		Element rootElement = portletDataContext.getImportDataRootElement();

		Element entriesElement = rootElement.element("entries");

		if (entriesElement != null) {
			JournalPortletDataHandler.importReferenceData(
				portletDataContext, entriesElement);
		}
		else {
			entriesElement = rootElement;
		}

		for (Element entryElement : entriesElement.elements("entry")) {
			String path = entryElement.attributeValue("path");

			if (!portletDataContext.isPathNotProcessed(path)) {
				continue;
			}

			BlogsEntry entry =
				(BlogsEntry)portletDataContext.getZipEntryAsObject(path);

			importEntry(portletDataContext, entryElement, entry);
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "wordpress")) {
			WordPressImporter.importData(portletDataContext);
		}

		return null;
	}

	protected void exportEntry(
			PortletDataContext portletDataContext, Element entriesElement,
			Element dlFileEntryTypesElement, Element dlFoldersElement,
			Element dlFileEntriesElement, Element dlFileRanksElement,
			Element dlRepositoriesElement, Element dlRepositoryEntriesElement,
			BlogsEntry entry)
		throws Exception {
	}

	protected String getEntryImagePath(
			PortletDataContext portletDataContext, BlogsEntry entry)
		throws Exception {

		StringBundler sb = new StringBundler(4);

		sb.append(
			ExportImportPathUtil.getPortletPath(
				portletDataContext, PortletKeys.BLOGS));
		sb.append("/entry/");
		sb.append(entry.getUuid());
		sb.append(StringPool.SLASH);

		return sb.toString();
	}

	protected String getEntryPath(
		PortletDataContext portletDataContext, BlogsEntry entry) {

		StringBundler sb = new StringBundler(4);

		sb.append(
			ExportImportPathUtil.getPortletPath(
				portletDataContext, PortletKeys.BLOGS));
		sb.append("/entries/");
		sb.append(entry.getEntryId());
		sb.append(".xml");

		return sb.toString();
	}

	protected String getEntrySmallImagePath(
			PortletDataContext portletDataContext, BlogsEntry entry)
		throws Exception {

		StringBundler sb = new StringBundler(6);

		sb.append(
			ExportImportPathUtil.getPortletPath(
				portletDataContext, PortletKeys.BLOGS));
		sb.append("/entries/");
		sb.append(entry.getUuid());
		sb.append("/thumbnail");
		sb.append(StringPool.PERIOD);
		sb.append(entry.getSmallImageType());

		return sb.toString();
	}

	protected void importEntry(
			PortletDataContext portletDataContext, Element entryElement,
			BlogsEntry entry)
		throws Exception {
	}

}