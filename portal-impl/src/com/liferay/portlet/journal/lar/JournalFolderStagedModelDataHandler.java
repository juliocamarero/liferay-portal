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

import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalFolder;
import com.liferay.portlet.journal.model.JournalFolderConstants;
import com.liferay.portlet.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.portlet.journal.service.persistence.JournalArticleUtil;
import com.liferay.portlet.journal.service.persistence.JournalFolderUtil;

import java.util.List;
import java.util.Map;

/**
 * @author Daniel Kocsis
 */
public class JournalFolderStagedModelDataHandler
	extends BaseStagedModelDataHandler<JournalFolder> {

	@Override
	public String getClassName() {
		return JournalFolder.class.getName();
	}

	protected static void exportParentFolder(
			PortletDataContext portletDataContext, Element foldersElement,
			long folderId)
		throws Exception {

		if (folderId == JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			return;
		}

		JournalFolder folder = JournalFolderUtil.findByPrimaryKey(folderId);

		exportParentFolder(
			portletDataContext, foldersElement, folder.getParentFolderId());

		String path = getFolderPath(portletDataContext, folder);

		if (portletDataContext.isPathNotProcessed(path)) {
			Element folderElement = foldersElement.addElement("folder");

			portletDataContext.addClassedModel(
				folderElement, path, folder, NAMESPACE);
		}
	}

	protected static void importFolder(
			PortletDataContext portletDataContext, String folderPath,
			JournalFolder folder)
		throws Exception {

		long userId = portletDataContext.getUserId(folder.getUserUuid());

		Map<Long, Long> folderIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				JournalFolder.class);

		long parentFolderId = MapUtil.getLong(
			folderIds, folder.getParentFolderId(), folder.getParentFolderId());

		if ((parentFolderId !=
				JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID) &&
			(parentFolderId == folder.getParentFolderId())) {

			String path = getImportFolderPath(
				portletDataContext, parentFolderId);

			JournalFolder parentFolder =
				(JournalFolder)portletDataContext.getZipEntryAsObject(path);

			importFolder(portletDataContext, path, parentFolder);

			parentFolderId = MapUtil.getLong(
				folderIds, folder.getParentFolderId(),
				folder.getParentFolderId());
		}

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			folderPath, folder, NAMESPACE);

		JournalFolder importedFolder = null;

		long groupId = portletDataContext.getScopeGroupId();

		if (portletDataContext.isDataStrategyMirror()) {
			JournalFolder existingFolder = JournalFolderUtil.fetchByUUID_G(
				folder.getUuid(), groupId);

			if (existingFolder == null) {
				serviceContext.setUuid(folder.getUuid());

				importedFolder = JournalFolderLocalServiceUtil.addFolder(
					userId, groupId, parentFolderId, folder.getName(),
					folder.getDescription(), serviceContext);
			}
			else {
				importedFolder = JournalFolderLocalServiceUtil.updateFolder(
					userId, existingFolder.getFolderId(), parentFolderId,
					folder.getName(), folder.getDescription(), false,
					serviceContext);
			}
		}
		else {
			importedFolder = JournalFolderLocalServiceUtil.addFolder(
				userId, groupId, parentFolderId, folder.getName(),
				folder.getDescription(), serviceContext);
		}

		portletDataContext.importClassedModel(
			folder, importedFolder, NAMESPACE);
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, JournalFolder folder)
		throws Exception {

		if (checkDateRange &&
			!portletDataContext.isWithinDateRange(folder.getModifiedDate())) {

			return;
		}

		exportParentFolder(
			portletDataContext, foldersElement, folder.getParentFolderId());

		String path = getFolderPath(portletDataContext, folder);

		if (portletDataContext.isPathNotProcessed(path)) {
			Element folderElement = foldersElement.addElement("folder");

			portletDataContext.addClassedModel(
				folderElement, path, folder, NAMESPACE);
		}

		List<JournalArticle> articles = JournalArticleUtil.findByG_F(
			folder.getGroupId(), folder.getFolderId());

		for (JournalArticle article : articles) {
			exportArticle(
				portletDataContext, articlesElement, ddmStructuresElement,
				ddmTemplatesElement, dlFileEntryTypesElement, dlFoldersElement,
				dlFileEntriesElement, dlFileRanksElement, dlRepositoriesElement,
				dlRepositoryEntriesElement, article, true);
		}
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, JournalFolder folder)
		throws Exception {

		String path = folderElement.attributeValue("path");

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		JournalFolder folder =
			(JournalFolder)portletDataContext.getZipEntryAsObject(path);

		importFolder(portletDataContext, path, folder);
	}

}