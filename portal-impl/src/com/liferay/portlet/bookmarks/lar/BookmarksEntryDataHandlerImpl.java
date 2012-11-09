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

import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.StagedModelDataHandler;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerRegistryUtil;
import com.liferay.portal.kernel.lar.StagedModelPathUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.model.BookmarksFolderConstants;
import com.liferay.portlet.bookmarks.service.BookmarksEntryLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksEntryUtil;

import java.util.Map;

/**
 * @author Mate Thurzo
 * @author Daniel Kocsis
 */
public class BookmarksEntryDataHandlerImpl
	extends BaseStagedModelDataHandler<BookmarksEntry>
	implements StagedModelDataHandler<BookmarksEntry> {

	public static final String CLASS_NAME = BookmarksEntry.class.getName();

	@Override
	public String getClassName() {
		return CLASS_NAME;
	}

	@Override
	protected void doExportModelData(
			BookmarksEntry entry, PortletDataContext portletDataContext,
			Element... elements)
		throws Exception {

		if (!portletDataContext.isWithinDateRange(entry.getModifiedDate())) {
			return;
		}

		if (elements.length != 1) {
			return;
		}

		Element entriesElement = elements[0];

		Element entryElement = entriesElement.addElement("entry");

		portletDataContext.addClassedModel(
			entryElement, StagedModelPathUtil.getPath(entry), entry,
			BookmarksPortletDataHandlerImpl.NAMESPACE);
	}

	@Override
	protected void doImportModelData(
			BookmarksEntry entry, String path,
			PortletDataContext portletDataContext)
		throws Exception {

		long userId = portletDataContext.getUserId(entry.getUserUuid());

		Map<Long, Long> folderIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				BookmarksFolder.class);

		long folderId = MapUtil.getLong(
			folderIds, entry.getFolderId(), entry.getFolderId());

		if ((folderId != BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID) &&
			(folderId == entry.getFolderId())) {

			String parentFolderPath = StagedModelPathUtil.getPath(
				portletDataContext, BookmarksFolder.class.getName(), folderId);

			BookmarksFolder parentFolder =
				(BookmarksFolder)portletDataContext.getZipEntryAsObject(
					parentFolderPath);

			StagedModelDataHandler<BookmarksFolder> bookmarksFolderDataHandler =
				(StagedModelDataHandler<BookmarksFolder>)
					StagedModelDataHandlerRegistryUtil.
						getStagedModelDataHandler(
							BookmarksFolder.class.getName());

			bookmarksFolderDataHandler.importModelData(
				parentFolder, parentFolderPath, portletDataContext);

			folderId = MapUtil.getLong(
				folderIds, entry.getFolderId(), entry.getFolderId());
		}

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			path, entry, BookmarksPortletDataHandlerImpl.NAMESPACE);

		BookmarksEntry importedEntry = null;

		if (portletDataContext.isDataStrategyMirror()) {
			BookmarksEntry existingEntry = BookmarksEntryUtil.fetchByUUID_G(
				entry.getUuid(), portletDataContext.getScopeGroupId());

			if (existingEntry == null) {
				serviceContext.setUuid(entry.getUuid());

				importedEntry = BookmarksEntryLocalServiceUtil.addEntry(
					userId, portletDataContext.getScopeGroupId(), folderId,
					entry.getName(), entry.getUrl(), entry.getDescription(),
					serviceContext);
			}
			else {
				importedEntry = BookmarksEntryLocalServiceUtil.updateEntry(
					userId, existingEntry.getEntryId(),
					portletDataContext.getScopeGroupId(), folderId,
					entry.getName(), entry.getUrl(), entry.getDescription(),
					serviceContext);
			}
		}
		else {
			importedEntry = BookmarksEntryLocalServiceUtil.addEntry(
				userId, portletDataContext.getScopeGroupId(), folderId,
				entry.getName(), entry.getUrl(), entry.getDescription(),
				serviceContext);
		}

		portletDataContext.importClassedModel(
			entry, importedEntry, BookmarksPortletDataHandlerImpl.NAMESPACE);
	}

}