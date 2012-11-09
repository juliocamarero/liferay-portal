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

import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.StagedModelDataHandler;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerImpl;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerRegistryUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.model.BookmarksFolderConstants;
import com.liferay.portlet.bookmarks.service.BookmarksFolderLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksEntryUtil;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksFolderUtil;

import java.util.List;
import java.util.Map;

/**
 * @author Mate Thurzo
 * @author Daniel Kocsis
 */
public class BookmarksFolderDataHandlerImpl
	extends StagedModelDataHandlerImpl<BookmarksFolder>
	implements StagedModelDataHandler<BookmarksFolder> {

	public void export(
			BookmarksFolder folder, PortletDataContext portletDataContext,
			Element... elements)
		throws Exception {

		String path = ExportImportPathUtil.getEntityPath(folder);

		if (!portletDataContext.isPathNotProcessed(path) ||
			!portletDataContext.isWithinDateRange(folder.getModifiedDate())) {

			return;
		}

		if (elements.length != 2) {
			return;
		}

		Element foldersElement = elements[0];
		Element entriesElement = elements[1];

		List<BookmarksFolder> children = BookmarksFolderUtil.findByG_P(
			folder.getGroupId(), folder.getFolderId());

		if (children != null) {
			for (BookmarksFolder childFolder : children) {
				export(
					childFolder, portletDataContext, foldersElement,
					entriesElement);
			}
		}

		List<BookmarksEntry> childEntries = BookmarksEntryUtil.findByG_F(
			folder.getGroupId(), folder.getFolderId());

		StagedModelDataHandler bookmarksEntryDataHandler =
			StagedModelDataHandlerRegistryUtil.getStagedModelDataHandler(
				BookmarksEntry.class.getName());

		if (childEntries != null) {
			for (BookmarksEntry childEntry : childEntries) {
				bookmarksEntryDataHandler.export(
					childEntry, portletDataContext, entriesElement);
			}
		}

		Element folderElement = foldersElement.addElement("folder");

		portletDataContext.addClassedModel(
			folderElement, path, folder,
			BookmarksPortletDataHandlerImpl.NAMESPACE);
	}

	@Override
	public void importData(
			BookmarksFolder folder, String path,
			PortletDataContext portletDataContext)
		throws Exception {

		long userId = portletDataContext.getUserId(folder.getUserUuid());

		Map<Long, Long> folderIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				BookmarksFolder.class);

		long parentFolderId = MapUtil.getLong(
			folderIds, folder.getParentFolderId(), folder.getParentFolderId());

		if ((parentFolderId !=
				BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID) &&
			(parentFolderId == folder.getParentFolderId())) {

			String parentFolderPath = ExportImportPathUtil.getEntityPath(
				parentFolderId, BookmarksFolder.class, portletDataContext);

			BookmarksFolder parentFolder =
				(BookmarksFolder)portletDataContext.getZipEntryAsObject(
					parentFolderPath);

			importData(parentFolder, parentFolderPath, portletDataContext);

			parentFolderId = MapUtil.getLong(
				folderIds, folder.getParentFolderId(),
				folder.getParentFolderId());
		}

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			path, folder, BookmarksPortletDataHandlerImpl.NAMESPACE);

		BookmarksFolder importedFolder = null;

		if (portletDataContext.isDataStrategyMirror()) {
			BookmarksFolder existingFolder = BookmarksFolderUtil.fetchByUUID_G(
				folder.getUuid(), portletDataContext.getScopeGroupId());

			if (existingFolder == null) {
				serviceContext.setUuid(folder.getUuid());

				importedFolder = BookmarksFolderLocalServiceUtil.addFolder(
					userId, parentFolderId, folder.getName(),
					folder.getDescription(), serviceContext);
			}
			else {
				importedFolder = BookmarksFolderLocalServiceUtil.updateFolder(
					existingFolder.getFolderId(), parentFolderId,
					folder.getName(), folder.getDescription(), false,
					serviceContext);
			}
		}
		else {
			importedFolder = BookmarksFolderLocalServiceUtil.addFolder(
				userId, parentFolderId, folder.getName(),
				folder.getDescription(), serviceContext);
		}

		portletDataContext.importClassedModel(
			folder, importedFolder, BookmarksPortletDataHandlerImpl.NAMESPACE);
	}

}