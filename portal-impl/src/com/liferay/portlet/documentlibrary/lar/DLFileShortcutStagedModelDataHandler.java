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

package com.liferay.portlet.documentlibrary.lar;

import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portlet.documentlibrary.model.DLFileShortcut;

/**
 * @author Mate Thurzo
 */
public class DLFileShortcutStagedModelDataHandler
	extends BaseStagedModelDataHandler<DLFileShortcut> {

	public static final String[] CLASS_NAMES = {DLFileShortcut.class.getName()};

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, DLFileShortcut fileShortcut)
		throws Exception {

		if (!portletDataContext.isWithinDateRange(
				fileShortcut.getModifiedDate())) {

			return;
		}

		exportParentFolder(
			portletDataContext, fileEntryTypesElement, foldersElement,
			repositoriesElement, repositoryEntriesElement,
			fileShortcut.getFolderId());

		String path = getFileShortcutPath(portletDataContext, fileShortcut);

		if (portletDataContext.isPathNotProcessed(path)) {
			Element fileShortcutElement = fileShortcutsElement.addElement(
				"file-shortcut");

			FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(
				fileShortcut.getToFileEntryId());

			String fileEntryUuid = fileEntry.getUuid();

			fileShortcutElement.addAttribute("file-entry-uuid", fileEntryUuid);

			portletDataContext.addClassedModel(
				fileShortcutElement, path, fileShortcut, NAMESPACE);
		}
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, DLFileShortcut fileShortcut)
		throws Exception {

		long userId = portletDataContext.getUserId(fileShortcut.getUserUuid());

		Map<Long, Long> folderIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DLFolder.class);

		long folderId = MapUtil.getLong(
			folderIds, fileShortcut.getFolderId(), fileShortcut.getFolderId());

		long groupId = portletDataContext.getScopeGroupId();

		if (folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			Folder folder = FolderUtil.findByPrimaryKey(folderId);

			groupId = folder.getRepositoryId();
		}

		String fileEntryUuid = fileShortcutElement.attributeValue(
			"file-entry-uuid");

		FileEntry fileEntry = FileEntryUtil.fetchByUUID_R(
			fileEntryUuid, groupId);

		if (fileEntry == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to fetch file entry {uuid=" + fileEntryUuid +
						", groupId=" + groupId + "}");
			}

			return;
		}

		long fileEntryId = fileEntry.getFileEntryId();

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			fileShortcutElement, fileShortcut, NAMESPACE);

		DLFileShortcut importedFileShortcut = null;

		if (portletDataContext.isDataStrategyMirror()) {
			DLFileShortcut existingFileShortcut =
				DLFileShortcutUtil.fetchByUUID_G(
					fileShortcut.getUuid(),
					portletDataContext.getScopeGroupId());

			if (existingFileShortcut == null) {
				serviceContext.setUuid(fileShortcut.getUuid());

				importedFileShortcut = DLAppLocalServiceUtil.addFileShortcut(
					userId, groupId, folderId, fileEntryId, serviceContext);
			}
			else {
				importedFileShortcut = DLAppLocalServiceUtil.updateFileShortcut(
					userId, existingFileShortcut.getFileShortcutId(), folderId,
					fileEntryId, serviceContext);
			}
		}
		else {
			importedFileShortcut = DLAppLocalServiceUtil.addFileShortcut(
				userId, groupId, folderId, fileEntryId, serviceContext);
		}

		portletDataContext.importClassedModel(
			fileShortcut, importedFileShortcut, NAMESPACE);
	}

}