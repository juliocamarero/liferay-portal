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

package com.liferay.portlet.documentlibrary.lar;

import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Repository;
import com.liferay.portal.model.RepositoryEntry;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFileEntryType;
import com.liferay.portlet.documentlibrary.model.DLFileRank;
import com.liferay.portlet.documentlibrary.model.DLFileShortcut;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;

/**
 * @author Raymond Augé
 */
public class DLDisplayPortletDataHandler extends DLPortletDataHandler {

	public DLDisplayPortletDataHandler() {
		setAlwaysExportable(false);
	}

	@Override
	protected PortletPreferences doDeleteData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		if (portletPreferences == null) {
			return portletPreferences;
		}

		portletPreferences.setValue("rootFolderId", StringPool.BLANK);
		portletPreferences.setValue("showFoldersSearch", StringPool.BLANK);
		portletPreferences.setValue("showSubfolders", StringPool.BLANK);
		portletPreferences.setValue("foldersPerPage", StringPool.BLANK);
		portletPreferences.setValue("folderColumns", StringPool.BLANK);
		portletPreferences.setValue("fileEntriesPerPage", StringPool.BLANK);
		portletPreferences.setValue("fileEntryColumns", StringPool.BLANK);
		portletPreferences.setValue("enable-comment-ratings", StringPool.BLANK);

		return portletPreferences;
	}

	@Override
	protected void doExportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		portletDataContext.addPermissions(
			"com.liferay.portlet.documentlibrary",
			portletDataContext.getScopeGroupId());

		Element rootElement = portletDataContext.getRootElement();

		rootElement.addAttribute(
			"group-id", String.valueOf(portletDataContext.getScopeGroupId()));

		Element fileEntryTypesElement = rootElement.addElement(
			DLFileEntryType.class.getSimpleName());
		Element foldersElement = rootElement.addElement(
			DLFolder.class.getSimpleName());
		Element fileEntriesElement = rootElement.addElement(
			DLFileEntry.class.getSimpleName());
		Element fileShortcutsElement = rootElement.addElement(
			DLFileShortcut.class.getSimpleName());
		Element fileRanksElement = rootElement.addElement(
			DLFileRank.class.getSimpleName());
		Element repositoriesElement = rootElement.addElement(
			Repository.class.getSimpleName());
		Element repositoryEntriesElement = rootElement.addElement(
			RepositoryEntry.class.getSimpleName());

		long rootFolderId = GetterUtil.getLong(
			portletPreferences.getValue("rootFolderId", null));

		if (rootFolderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			List<Folder> folders = FolderUtil.findByRepositoryId(
				portletDataContext.getScopeGroupId());

			for (Folder folder : folders) {
				DLPortletDataHandler.exportFolder(
					portletDataContext, fileEntryTypesElement, foldersElement,
					fileEntriesElement, fileShortcutsElement, fileRanksElement,
					repositoriesElement, repositoryEntriesElement, folder,
					false);
			}

			List<FileEntry> fileEntries = FileEntryUtil.findByR_F(
				portletDataContext.getScopeGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID);

			for (FileEntry fileEntry : fileEntries) {
				DLPortletDataHandler.exportFileEntry(
					portletDataContext, fileEntryTypesElement, foldersElement,
					fileEntriesElement, fileRanksElement, repositoriesElement,
					repositoryEntriesElement, fileEntry, true);
			}
		}
		else {
			Folder folder = DLAppLocalServiceUtil.getFolder(rootFolderId);

			rootElement.addAttribute(
				"root-folder-id", String.valueOf(folder.getFolderId()));
			rootElement.addAttribute(
				"default-repository",
				String.valueOf(folder.isDefaultRepository()));

			DLPortletDataHandler.exportFolder(
				portletDataContext, fileEntryTypesElement, foldersElement,
				fileEntriesElement, fileShortcutsElement, fileRanksElement,
				repositoriesElement, repositoryEntriesElement, folder, true);
		}
	}

	@Override
	protected PortletPreferences doImportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		portletDataContext.importPermissions(
			"com.liferay.portlet.documentlibrary",
			portletDataContext.getSourceGroupId(),
			portletDataContext.getScopeGroupId());

		Element rootElement = portletDataContext.getRootElement();

		Element fileEntryTypesElement = rootElement.element(
			DLFileEntryType.class.getSimpleName());

		List<Element> fileEntryTypeElements = fileEntryTypesElement.elements(
			"file-entry-type");

		for (Element fileEntryTypeElement : fileEntryTypeElements) {
			DLPortletDataHandler.importFileEntryType(
				portletDataContext, fileEntryTypeElement);
		}

		Element foldersElement = rootElement.element(
			DLFolder.class.getSimpleName());

		List<Element> folderElements = foldersElement.elements("folder");

		for (Element folderElement : folderElements) {
			DLPortletDataHandler.importFolder(
				portletDataContext, folderElement);
		}

		Element fileEntriesElement = rootElement.element(
			DLFileEntry.class.getSimpleName());

		List<Element> fileEntryElements = fileEntriesElement.elements(
			"file-entry");

		for (Element fileEntryElement : fileEntryElements) {
			DLPortletDataHandler.importFileEntry(
				portletDataContext, fileEntryElement);
		}

		if (portletDataContext.getBooleanParameter(
				DLPortletDataHandler.NAMESPACE, "shortcuts")) {

			Element fileShortcutsElement = rootElement.element(
				DLFileShortcut.class.getSimpleName());

			List<Element> fileShortcutElements = fileShortcutsElement.elements(
				"file-shortcut");

			for (Element fileShortcutElement : fileShortcutElements) {
				DLPortletDataHandler.importFileShortcut(
					portletDataContext, fileShortcutElement);
			}
		}

		if (portletDataContext.getBooleanParameter(
				DLPortletDataHandler.NAMESPACE, "ranks")) {

			Element fileRanksElement = rootElement.element(
				DLFileRank.class.getSimpleName());

			List<Element> fileRankElements = fileRanksElement.elements(
				"file-rank");

			for (Element fileRankElement : fileRankElements) {
				DLPortletDataHandler.importFileRank(
					portletDataContext, fileRankElement);
			}
		}

		long rootFolderId = GetterUtil.getLong(
			rootElement.attributeValue("root-folder-id"));
		boolean defaultRepository = GetterUtil.getBoolean(
			rootElement.attributeValue("default-repository"), true);

		if (rootFolderId > 0) {
			Map<Long, Long> folderIds = null;

			if (defaultRepository) {
				folderIds =
					(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
						DLFolder.class);
			}
			else {
				folderIds =
					(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
						RepositoryEntry.class);
			}

			rootFolderId = MapUtil.getLong(
				folderIds, rootFolderId, rootFolderId);

			portletPreferences.setValue(
				"rootFolderId", String.valueOf(rootFolderId));
		}

		return portletPreferences;
	}

}