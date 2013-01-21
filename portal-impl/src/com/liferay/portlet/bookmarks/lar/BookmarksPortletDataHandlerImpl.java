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

package com.liferay.portlet.bookmarks.lar;

import com.liferay.portal.kernel.lar.BasePortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.model.BookmarksFolderConstants;
import com.liferay.portlet.bookmarks.service.BookmarksEntryLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.BookmarksFolderLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksEntryUtil;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksFolderUtil;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;

/**
 * @author Jorge Ferrer
 * @author Bruno Farache
 * @author Raymond Augé
 * @author Juan Fernández
 */
public class BookmarksPortletDataHandlerImpl extends BasePortletDataHandler {

	public static final String NAMESPACE = "bookmarks";

	@Override
	public PortletDataHandlerControl[] getExportControls() {
		return new PortletDataHandlerControl[] {
			_foldersAndEntries
		};
	}

	@Override
	public PortletDataHandlerControl[] getExportMetadataControls() {
		return new PortletDataHandlerControl[] {
			new PortletDataHandlerBoolean(
				NAMESPACE, "bookmarks", true, _metadataControls)
		};
	}

	@Override
	public PortletDataHandlerControl[] getImportControls() {
		return new PortletDataHandlerControl[] {
			_foldersAndEntries
		};
	}

	@Override
	public PortletDataHandlerControl[] getImportMetadataControls() {
		return new PortletDataHandlerControl[] {
			new PortletDataHandlerBoolean(
				NAMESPACE, "bookmarks", true, _metadataControls)
		};
	}

	@Override
	public boolean isAlwaysExportable() {
		return _ALWAYS_EXPORTABLE;
	}

	@Override
	public boolean isPublishToLiveByDefault() {
		return _PUBLISH_TO_LIVE_BY_DEFAULT;
	}

	@Override
	protected PortletPreferences doDeleteData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		if (!portletDataContext.addPrimaryKey(
				BookmarksPortletDataHandlerImpl.class, "deleteData")) {

			BookmarksFolderLocalServiceUtil.deleteFolders(
				portletDataContext.getScopeGroupId());

			BookmarksEntryLocalServiceUtil.deleteEntries(
				portletDataContext.getScopeGroupId(),
				BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID);
		}

		return null;
	}

	@Override
	protected String doExportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		portletDataContext.addPermissions(
			"com.liferay.portlet.bookmarks",
			portletDataContext.getScopeGroupId());

		Document document = SAXReaderUtil.createDocument();

		Element rootElement = document.addElement("bookmarks-data");

		rootElement.addAttribute(
			"group-id", String.valueOf(portletDataContext.getScopeGroupId()));

		Element foldersElement = rootElement.addElement("folders");
		Element entriesElement = rootElement.addElement("entries");

		List<BookmarksFolder> folders = BookmarksFolderUtil.findByGroupId(
			portletDataContext.getScopeGroupId());

		for (BookmarksFolder folder : folders) {
			exportFolder(
				portletDataContext, foldersElement, entriesElement, folder);
		}

		List<BookmarksEntry> entries = BookmarksEntryUtil.findByG_F(
			portletDataContext.getScopeGroupId(),
			BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		for (BookmarksEntry entry : entries) {
			exportEntry(portletDataContext, null, entriesElement, entry);
		}

		return document.formattedString();
	}

	@Override
	protected PortletPreferences doImportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, String data)
		throws Exception {

		portletDataContext.importPermissions(
			"com.liferay.portlet.bookmarks",
			portletDataContext.getSourceGroupId(),
			portletDataContext.getScopeGroupId());

		Document document = SAXReaderUtil.read(data);

		Element rootElement = document.getRootElement();

		Element foldersElement = rootElement.element("folders");

		for (Element folderElement : foldersElement.elements("folder")) {
			String path = folderElement.attributeValue("path");

			if (!portletDataContext.isPathNotProcessed(path)) {
				continue;
			}

			BookmarksFolder folder =
				(BookmarksFolder)portletDataContext.getZipEntryAsObject(path);

			importFolder(portletDataContext, path, folder);
		}

		Element entriesElement = rootElement.element("entries");

		for (Element entryElement : entriesElement.elements("entry")) {
			String path = entryElement.attributeValue("path");

			if (!portletDataContext.isPathNotProcessed(path)) {
				continue;
			}

			BookmarksEntry entry =
				(BookmarksEntry)portletDataContext.getZipEntryAsObject(path);

			importEntry(portletDataContext, entryElement, entry);
		}

		return null;
	}

	private static final boolean _ALWAYS_EXPORTABLE = true;

	private static final boolean _PUBLISH_TO_LIVE_BY_DEFAULT = true;

	private static PortletDataHandlerBoolean _foldersAndEntries =
		new PortletDataHandlerBoolean(
			NAMESPACE, "folders-and-entries", true, true);

	private static PortletDataHandlerControl[] _metadataControls =
		new PortletDataHandlerControl[] {
			new PortletDataHandlerBoolean(NAMESPACE, "categories"),
			new PortletDataHandlerBoolean(NAMESPACE, "ratings"),
			new PortletDataHandlerBoolean(NAMESPACE, "tags")
		};

}