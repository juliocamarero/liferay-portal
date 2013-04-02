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
import com.liferay.portal.kernel.lar.PortletDataException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.util.DLProcessorThreadLocal;

/**
 * @author Mate Thurzo
 */
public class DLFileEntryStagedModelDataHandler
	extends BaseStagedModelDataHandler<DLFileEntry> {

	@Override
	public String getClassName() {
		return DLFileEntry.class.getName();
	}

	@Override
	public void importStagedModel(
			PortletDataContext portletDataContext, DLFileEntry fileEntry)
		throws PortletDataException {

		boolean dlProcessorEnabled = DLProcessorThreadLocal.isEnabled();

		try {
			DLProcessorThreadLocal.setEnabled(false);

			super.importStagedModel(portletDataContext, fileEntry);
		}
		finally {
			DLProcessorThreadLocal.setEnabled(dlProcessorEnabled);
		}
	}

	protected static void exportMetaData(
			PortletDataContext portletDataContext,
			Element fileEntryTypesElement, Element fileEntryElement,
			FileEntry fileEntry)
		throws Exception {

		if (!(fileEntry instanceof LiferayFileEntry)) {
			return;
		}

		LiferayFileEntry liferayFileEntry = (LiferayFileEntry)fileEntry;

		DLFileEntry dlFileEntry = liferayFileEntry.getDLFileEntry();

		long fileEntryTypeId = dlFileEntry.getFileEntryTypeId();

		DLFileEntryType dlFileEntryType =
			DLFileEntryTypeLocalServiceUtil.fetchFileEntryType(fileEntryTypeId);

		if (dlFileEntryType == null) {
			return;
		}

		fileEntryElement.addAttribute(
			"fileEntryTypeUuid", dlFileEntryType.getUuid());

		if (!dlFileEntryType.isExportable()) {
			return;
		}

		exportFileEntryType(
			portletDataContext, fileEntryTypesElement, dlFileEntryType);

		List<DDMStructure> ddmStructures = dlFileEntryType.getDDMStructures();

		for (DDMStructure ddmStructure : ddmStructures) {
			Element structureFields = fileEntryElement.addElement(
				"structure-fields");

			String path = getFileEntryFileEntryTypeStructureFieldsPath(
				portletDataContext, fileEntry, dlFileEntryType.getUuid(),
				ddmStructure.getStructureId());

			structureFields.addAttribute("path", path);

			structureFields.addAttribute(
				"structureUuid", ddmStructure.getUuid());

			FileVersion fileVersion = fileEntry.getFileVersion();

			DLFileEntryMetadata dlFileEntryMetadata =
				DLFileEntryMetadataLocalServiceUtil.getFileEntryMetadata(
					ddmStructure.getStructureId(),
					fileVersion.getFileVersionId());

			Fields fields = StorageEngineUtil.getFields(
				dlFileEntryMetadata.getDDMStorageId());

			portletDataContext.addZipEntry(path, fields);
		}
	}

	protected static void importMetaData(
			PortletDataContext portletDataContext, Element fileEntryElement,
			ServiceContext serviceContext)
		throws Exception {

		String fileEntryTypeUuid = fileEntryElement.attributeValue(
			"fileEntryTypeUuid");

		if (Validator.isNull(fileEntryTypeUuid)) {
			return;
		}

		DLFileEntryType dlFileEntryType = DLFileEntryTypeUtil.fetchByUUID_G(
			fileEntryTypeUuid, portletDataContext.getScopeGroupId());

		if (dlFileEntryType == null) {
			Group group = GroupLocalServiceUtil.getCompanyGroup(
				portletDataContext.getCompanyId());

			dlFileEntryType = DLFileEntryTypeUtil.fetchByUUID_G(
				fileEntryTypeUuid, group.getGroupId());

			if (dlFileEntryType == null) {
				serviceContext.setAttribute("fileEntryTypeId", -1);

				return;
			}
		}

		serviceContext.setAttribute(
			"fileEntryTypeId", dlFileEntryType.getFileEntryTypeId());

		List<DDMStructure> ddmStructures = dlFileEntryType.getDDMStructures();

		for (DDMStructure ddmStructure : ddmStructures) {
			Element structureFieldsElement =
				(Element)fileEntryElement.selectSingleNode(
					"structure-fields[@structureUuid='".concat(
							ddmStructure.getUuid()).concat("']"));

			if (structureFieldsElement == null) {
				continue;
			}

			String path = structureFieldsElement.attributeValue("path");

			Fields fields = (Fields)portletDataContext.getZipEntryAsObject(
				path);

			serviceContext.setAttribute(
				Fields.class.getName() + ddmStructure.getStructureId(), fields);
		}
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, DLFileEntry fileEntry)
		throws Exception {

		if (checkDateRange &&
				!portletDataContext.isWithinDateRange(
					fileEntry.getModifiedDate())) {

			return;
		}

		if (!fileEntry.isDefaultRepository()) {
			Repository repository = RepositoryUtil.findByPrimaryKey(
				fileEntry.getRepositoryId());

			exportRepository(
				portletDataContext, repositoriesElement,
				repositoryEntriesElement, repository);

			return;
		}

		FileVersion fileVersion = fileEntry.getFileVersion();

		if (!fileVersion.isApproved() && !fileVersion.isInTrash()) {
			return;
		}

		String path = getFileEntryPath(portletDataContext, fileEntry);

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		LiferayFileEntry liferayFileEntry = (LiferayFileEntry)fileEntry;

		liferayFileEntry.setCachedFileVersion(fileVersion);

		Element fileEntryElement = fileEntriesElement.addElement("file-entry");

		if (foldersElement != null) {
			exportParentFolder(
				portletDataContext, fileEntryTypesElement, foldersElement,
				repositoriesElement, repositoryEntriesElement,
				fileEntry.getFolderId());
		}

		if (!portletDataContext.isPerformDirectBinaryImport()) {
			InputStream is = null;

			try {
				is = FileEntryUtil.getContentStream(fileEntry);
			}
			catch (NoSuchFileException nsfe) {
			}

			if (is == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"No file found for file entry " +
							fileEntry.getFileEntryId());
				}

				fileEntryElement.detach();

				return;
			}

			try {
				String binPath = getFileEntryBinPath(
					portletDataContext, fileEntry);

				portletDataContext.addZipEntry(binPath, is);

				fileEntryElement.addAttribute("bin-path", binPath);
			}
			finally {
				try {
					is.close();
				}
				catch (IOException ioe) {
					_log.error(ioe, ioe);
				}
			}
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "ranks")) {
			List<DLFileRank> fileRanks = DLFileRankUtil.findByFileEntryId(
				fileEntry.getFileEntryId());

			for (DLFileRank fileRank : fileRanks) {
				exportFileRank(portletDataContext, fileRanksElement, fileRank);
			}
		}

		if (portletDataContext.getBooleanParameter(
				NAMESPACE, "previews-and-thumbnails")) {

			DLProcessorRegistryUtil.exportGeneratedFiles(
				portletDataContext, fileEntry, fileEntryElement);
		}

		exportMetaData(
			portletDataContext, fileEntryTypesElement, fileEntryElement,
			fileEntry);

		portletDataContext.addClassedModel(
			fileEntryElement, path, fileEntry, NAMESPACE);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, DLFileEntry fileEntrys)
		throws Exception {

		FileEntry fileEntry = (FileEntry)portletDataContext.getZipEntryAsObject(
			path);

		long userId = portletDataContext.getUserId(fileEntry.getUserUuid());

		Map<Long, Long> folderIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DLFolder.class);

		long folderId = MapUtil.getLong(
			folderIds, fileEntry.getFolderId(), fileEntry.getFolderId());

		long[] assetCategoryIds = null;
		String[] assetTagNames = null;

		if (portletDataContext.getBooleanParameter(NAMESPACE, "categories")) {
			assetCategoryIds = portletDataContext.getAssetCategoryIds(
				DLFileEntry.class, fileEntry.getFileEntryId());
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "tags")) {
			assetTagNames = portletDataContext.getAssetTagNames(
				DLFileEntry.class, fileEntry.getFileEntryId());
		}

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			fileEntryElement, fileEntry, NAMESPACE);

		serviceContext.setAttribute(
			"sourceFileName", "A." + fileEntry.getExtension());
		serviceContext.setUserId(userId);

		String binPath = fileEntryElement.attributeValue("bin-path");

		InputStream is = null;

		if (Validator.isNull(binPath) &&
				portletDataContext.isPerformDirectBinaryImport()) {

			try {
				is = FileEntryUtil.getContentStream(fileEntry);
			}
			catch (NoSuchFileException nsfe) {
			}
		}
		else {
			is = portletDataContext.getZipEntryAsInputStream(binPath);
		}

		if (is == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"No file found for file entry " +
						fileEntry.getFileEntryId());
			}

			return;
		}

		if ((folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) &&
			(folderId == fileEntry.getFolderId())) {

			String folderPath = getImportFolderPath(
				portletDataContext, folderId);

			Folder folder = (Folder)portletDataContext.getZipEntryAsObject(
				folderPath);

			Document document = fileEntryElement.getDocument();

			Element rootElement = document.getRootElement();

			Element folderElement = (Element)rootElement.selectSingleNode(
				"//folder[@path='".concat(folderPath).concat("']"));

			importFolder(portletDataContext, folderPath, folderElement, folder);

			folderId = MapUtil.getLong(
				folderIds, fileEntry.getFolderId(), fileEntry.getFolderId());
		}

		importMetaData(portletDataContext, fileEntryElement, serviceContext);

		FileEntry importedFileEntry = null;

		String titleWithExtension = DLUtil.getTitleWithExtension(fileEntry);
		String extension = fileEntry.getExtension();

		String dotExtension = StringPool.PERIOD + extension;

		if (portletDataContext.isDataStrategyMirror()) {
			FileEntry existingFileEntry = FileEntryUtil.fetchByUUID_R(
				fileEntry.getUuid(), portletDataContext.getScopeGroupId());

			FileVersion fileVersion = fileEntry.getFileVersion();

			if (existingFileEntry == null) {
				String fileEntryTitle = fileEntry.getTitle();

				FileEntry existingTitleFileEntry = FileEntryUtil.fetchByR_F_T(
					portletDataContext.getScopeGroupId(), folderId,
					fileEntryTitle);

				if (existingTitleFileEntry != null) {
					if ((fileEntry.getGroupId() ==
							portletDataContext.getSourceGroupId()) &&
							portletDataContext.
								isDataStrategyMirrorWithOverwriting()) {

						DLAppLocalServiceUtil.deleteFileEntry(
							existingTitleFileEntry.getFileEntryId());
					}
					else {
						boolean titleHasExtension = false;

						if (fileEntryTitle.endsWith(dotExtension)) {
							fileEntryTitle = FileUtil.stripExtension(
								fileEntryTitle);

							titleHasExtension = true;
						}

						for (int i = 1;; i++) {
							fileEntryTitle += StringPool.SPACE + i;

							titleWithExtension = fileEntryTitle + dotExtension;

							existingTitleFileEntry = FileEntryUtil.fetchByR_F_T(
								portletDataContext.getScopeGroupId(), folderId,
								titleWithExtension);

							if (existingTitleFileEntry == null) {
								if (titleHasExtension) {
									fileEntryTitle += dotExtension;
								}

								break;
							}
						}
					}
				}

				serviceContext.setAttribute(
					"fileVersionUuid", fileVersion.getUuid());
				serviceContext.setUuid(fileEntry.getUuid());

				importedFileEntry = DLAppLocalServiceUtil.addFileEntry(
					userId, portletDataContext.getScopeGroupId(), folderId,
					titleWithExtension, fileEntry.getMimeType(), fileEntryTitle,
					fileEntry.getDescription(), null, is, fileEntry.getSize(),
					serviceContext);

				if (fileVersion.isInTrash()) {
					importedFileEntry = DLAppServiceUtil.moveFileEntryToTrash(
						importedFileEntry.getFileEntryId());
				}
			}
			else {
				FileVersion latestExistingFileVersion =
					existingFileEntry.getLatestFileVersion();

				boolean indexEnabled = serviceContext.isIndexingEnabled();

				try {
					serviceContext.setIndexingEnabled(false);

					if (!fileVersion.getUuid().equals(
							latestExistingFileVersion.getUuid())) {

						DLFileVersion alreadyExistingFileVersion =
							DLFileVersionLocalServiceUtil.
								getFileVersionByUuidAndGroupId(
									fileVersion.getUuid(),
									existingFileEntry.getGroupId());

						if (alreadyExistingFileVersion != null) {
							serviceContext.setAttribute(
								"existingDLFileVersionId",
								alreadyExistingFileVersion.getFileVersionId());
						}

						serviceContext.setUuid(fileVersion.getUuid());

						importedFileEntry =
							DLAppLocalServiceUtil.updateFileEntry(
								userId, existingFileEntry.getFileEntryId(),
								fileEntry.getTitle(), fileEntry.getMimeType(),
								fileEntry.getTitle(),
								fileEntry.getDescription(), null, false, is,
								fileEntry.getSize(), serviceContext);
					}
					else {
						DLAppLocalServiceUtil.updateAsset(
							userId, existingFileEntry,
							latestExistingFileVersion, assetCategoryIds,
							assetTagNames, null);

						importedFileEntry = existingFileEntry;
					}

					if (importedFileEntry.getFolderId() != folderId) {
						importedFileEntry = DLAppLocalServiceUtil.moveFileEntry(
							userId, importedFileEntry.getFileEntryId(),
							folderId, serviceContext);
					}

					if (importedFileEntry instanceof LiferayFileEntry) {
						LiferayFileEntry liferayFileEntry =
							(LiferayFileEntry)importedFileEntry;

						Indexer indexer = IndexerRegistryUtil.getIndexer(
							DLFileEntry.class);

						indexer.reindex(liferayFileEntry.getModel());
					}
				}
				finally {
					serviceContext.setIndexingEnabled(indexEnabled);
				}
			}
		}
		else {
			try {
				importedFileEntry = DLAppLocalServiceUtil.addFileEntry(
					userId, portletDataContext.getScopeGroupId(), folderId,
					titleWithExtension, fileEntry.getMimeType(),
					fileEntry.getTitle(), fileEntry.getDescription(), null, is,
					fileEntry.getSize(), serviceContext);
			}
			catch (DuplicateFileException dfe) {
				String title = fileEntry.getTitle();

				String[] titleParts = title.split("\\.", 2);

				title = titleParts[0] + PwdGenerator.getPassword();

				if (titleParts.length > 1) {
					title += StringPool.PERIOD + titleParts[1];
				}

				if (!title.endsWith(dotExtension)) {
					title += dotExtension;
				}

				importedFileEntry = DLAppLocalServiceUtil.addFileEntry(
					userId, portletDataContext.getScopeGroupId(), folderId,
					title, fileEntry.getMimeType(), title,
					fileEntry.getDescription(), null, is, fileEntry.getSize(),
					serviceContext);
			}
		}

		if (portletDataContext.getBooleanParameter(
				NAMESPACE, "previews-and-thumbnails")) {

			DLProcessorRegistryUtil.importGeneratedFiles(
				portletDataContext, fileEntry, importedFileEntry,
				fileEntryElement);
		}

		Map<String, String> fileEntryTitles =
			(Map<String, String>)portletDataContext.getNewPrimaryKeysMap(
				DLFileEntry.class.getName() + ".title");

		fileEntryTitles.put(fileEntry.getTitle(), importedFileEntry.getTitle());

		portletDataContext.importClassedModel(
			fileEntry, importedFileEntry, NAMESPACE);
	}


}