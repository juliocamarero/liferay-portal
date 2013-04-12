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
import com.liferay.portal.kernel.repository.model.Folder;

/**
 * @author Mate Thurzo
 */
public class FolderStagedModelDataHandler
	extends BaseStagedModelDataHandler<Folder> {

	public static final String[] CLASS_NAMES = {Folder.class.getName()};

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	protected static void exportFolderFileEntryTypes(
			PortletDataContext portletDataContext, Folder folder,
			Element fileEntryTypesElement, Element folderElement)
		throws Exception {

		List<DLFileEntryType> dlFileEntryTypes =
			DLFileEntryTypeLocalServiceUtil.getFolderFileEntryTypes(
				new long[] {portletDataContext.getScopeGroupId()},
				folder.getFolderId(), false);

		String[] fileEntryTypeUuids = new String[dlFileEntryTypes.size()];

		long defaultFileEntryTypeId =
			DLFileEntryTypeLocalServiceUtil.getDefaultFileEntryTypeId(
				folder.getFolderId());

		String defaultFileEntryTypeUuid = StringPool.BLANK;

		for (int i = 0; i < dlFileEntryTypes.size(); i++) {
			DLFileEntryType dlFileEntryType = dlFileEntryTypes.get(i);

			if (dlFileEntryType.getFileEntryTypeId() ==
					DLFileEntryTypeConstants.
						FILE_ENTRY_TYPE_ID_BASIC_DOCUMENT) {

				fileEntryTypeUuids[i] = "@basic_document@";
			}
			else {
				fileEntryTypeUuids[i] = dlFileEntryType.getUuid();
			}

			if (defaultFileEntryTypeId ==
					dlFileEntryType.getFileEntryTypeId()) {

				defaultFileEntryTypeUuid = dlFileEntryType.getUuid();
			}

			if (dlFileEntryType.isExportable()) {
				exportFileEntryType(
					portletDataContext, fileEntryTypesElement, dlFileEntryType);
			}
		}

		folderElement.addAttribute(
			"fileEntryTypeUuids", StringUtil.merge(fileEntryTypeUuids));
		folderElement.addAttribute(
			"defaultFileEntryTypeUuid", defaultFileEntryTypeUuid);
	}

	/**
	 * @see com.liferay.portal.lar.PortletImporter#getAssetCategoryName(String,
	 *      long, long, String, int)
	 * @see com.liferay.portal.lar.PortletImporter#getAssetVocabularyName(
	 *      String, long, String, int)
	 */
	protected static String getFolderName(
			String uuid, long groupId, long parentFolderId, String name,
			int count)
		throws Exception {

		Folder folder = FolderUtil.fetchByR_P_N(groupId, parentFolderId, name);

		if (folder == null) {
			return name;
		}

		if (Validator.isNotNull(uuid) && uuid.equals(folder.getUuid())) {
			return name;
		}

		name = StringUtil.appendParentheticalSuffix(name, count);

		return getFolderName(uuid, groupId, parentFolderId, name, ++count);
	}

	protected static void importFolderFileEntryTypes(
			PortletDataContext portletDataContext, Element folderElement,
			Folder folder, ServiceContext serviceContext)
		throws Exception {

		String[] fileEntryTypeUuids = StringUtil.split(
			folderElement.attributeValue("fileEntryTypeUuids"));

		List<Long> fileEntryTypeIds = new ArrayList<Long>();

		String defaultFileEntryTypeUuid = GetterUtil.getString(
			folderElement.attributeValue("defaultFileEntryTypeUuid"));

		long defaultFileEntryTypeId = 0;

		for (String fileEntryTypeUuid : fileEntryTypeUuids) {
			DLFileEntryType dlFileEntryType = DLFileEntryTypeUtil.fetchByUUID_G(
				fileEntryTypeUuid, portletDataContext.getScopeGroupId());

			if (dlFileEntryType == null) {
				Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(
					portletDataContext.getCompanyId());

				dlFileEntryType = DLFileEntryTypeUtil.fetchByUUID_G(
					fileEntryTypeUuid, companyGroup.getGroupId());
			}

			if ((dlFileEntryType == null) &&
				fileEntryTypeUuid.equals("@basic_document@")) {

				dlFileEntryType =
					DLFileEntryTypeLocalServiceUtil.fetchDLFileEntryType(0);
			}

			if (dlFileEntryType == null) {
				continue;
			}

			fileEntryTypeIds.add(dlFileEntryType.getFileEntryTypeId());

			if (defaultFileEntryTypeUuid.equals(dlFileEntryType.getUuid())) {
				defaultFileEntryTypeId = dlFileEntryType.getFileEntryTypeId();
			}
		}

		if (!fileEntryTypeIds.isEmpty()) {
			DLFolder dlFolder = (DLFolder)folder.getModel();

			dlFolder.setDefaultFileEntryTypeId(defaultFileEntryTypeId);
			dlFolder.setOverrideFileEntryTypes(true);

			DLFolderLocalServiceUtil.updateDLFolder(dlFolder);

			DLFileEntryTypeLocalServiceUtil.updateFolderFileEntryTypes(
				dlFolder, fileEntryTypeIds, defaultFileEntryTypeId,
				serviceContext);
		}
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Folder folder)
		throws Exception {

		if (!portletDataContext.isWithinDateRange(folder.getLastPostDate())) {
			return;
		}

		if (folder.isMountPoint()) {
			Repository repository = RepositoryUtil.findByPrimaryKey(
				folder.getRepositoryId());

			exportRepository(
				portletDataContext, repositoriesElement,
				repositoryEntriesElement, repository);

			return;
		}
		else if (!folder.isDefaultRepository()) {

			// No need to export non-Liferay repository items since they would
			// be exported as part of repository export

			return;
		}

		exportParentFolder(
			portletDataContext, fileEntryTypesElement, foldersElement,
			repositoriesElement, repositoryEntriesElement,
			folder.getParentFolderId());

		String path = getFolderPath(portletDataContext, folder);

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		Element folderElement = foldersElement.addElement("folder");

		exportFolderFileEntryTypes(
			portletDataContext, folder, fileEntryTypesElement, folderElement);

		portletDataContext.addClassedModel(
			folderElement, path, folder, NAMESPACE);

		if (recurse) {
			List<Folder> folders = FolderUtil.findByR_P(
				folder.getRepositoryId(), folder.getFolderId());

			for (Folder curFolder : folders) {
				exportFolder(
					portletDataContext, fileEntryTypesElement, foldersElement,
					fileEntriesElement, fileShortcutsElement, fileRanksElement,
					repositoriesElement, repositoryEntriesElement, curFolder,
					recurse);
			}
		}

		List<FileEntry> fileEntries = FileEntryUtil.findByR_F(
			folder.getRepositoryId(), folder.getFolderId());

		for (FileEntry fileEntry : fileEntries) {
			exportFileEntry(
				portletDataContext, fileEntryTypesElement, foldersElement,
				fileEntriesElement, fileRanksElement, repositoriesElement,
				repositoryEntriesElement, fileEntry, true);
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "shortcuts")) {
			List<DLFileShortcut> fileShortcuts = DLFileShortcutUtil.findByG_F_A(
				folder.getRepositoryId(), folder.getFolderId(), true);

			for (DLFileShortcut fileShortcut : fileShortcuts) {
				exportFileShortcut(
					portletDataContext, fileEntryTypesElement, foldersElement,
					fileShortcutsElement, repositoriesElement,
					repositoryEntriesElement, fileShortcut);
			}
		}
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, Folder folder)
		throws Exception {

		long userId = portletDataContext.getUserId(folder.getUserUuid());

		Map<Long, Long> folderIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DLFolder.class);

		long parentFolderId = MapUtil.getLong(
			folderIds, folder.getParentFolderId(), folder.getParentFolderId());

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			folderPath, folder, NAMESPACE);

		if ((parentFolderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) &&
			(parentFolderId == folder.getParentFolderId())) {

			String path = getImportFolderPath(
				portletDataContext, parentFolderId);

			Folder parentFolder =
				(Folder)portletDataContext.getZipEntryAsObject(path);

			importFolder(portletDataContext, path, folderElement, parentFolder);

			parentFolderId = MapUtil.getLong(
				folderIds, folder.getParentFolderId(),
				folder.getParentFolderId());
		}

		Folder importedFolder = null;

		if (portletDataContext.isDataStrategyMirror()) {
			Folder existingFolder = FolderUtil.fetchByUUID_R(
				folder.getUuid(), portletDataContext.getScopeGroupId());

			if (existingFolder == null) {
				String name = getFolderName(
					null, portletDataContext.getScopeGroupId(), parentFolderId,
					folder.getName(), 2);

				serviceContext.setUuid(folder.getUuid());

				importedFolder = DLAppLocalServiceUtil.addFolder(
					userId, portletDataContext.getScopeGroupId(),
					parentFolderId, name, folder.getDescription(),
					serviceContext);
			}
			else {
				String name = getFolderName(
					folder.getUuid(), portletDataContext.getScopeGroupId(),
					parentFolderId, folder.getName(), 2);

				importedFolder = DLAppLocalServiceUtil.updateFolder(
					existingFolder.getFolderId(), parentFolderId, name,
					folder.getDescription(), serviceContext);
			}
		}
		else {
			String name = getFolderName(
				null, portletDataContext.getScopeGroupId(), parentFolderId,
				folder.getName(), 2);

			importedFolder = DLAppLocalServiceUtil.addFolder(
				userId, portletDataContext.getScopeGroupId(), parentFolderId,
				name, folder.getDescription(), serviceContext);
		}

		folderIds.put(folder.getFolderId(), importedFolder.getFolderId());

		importFolderFileEntryTypes(
			portletDataContext, folderElement, importedFolder, serviceContext);

		portletDataContext.importClassedModel(
			folder, importedFolder, NAMESPACE);
	}

}