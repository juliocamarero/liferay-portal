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

import com.liferay.portal.kernel.lar.BasePortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.documentlibrary.model.DLFileEntryType;
import com.liferay.portlet.documentlibrary.model.DLFileShortcut;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryTypeServiceUtil;
import com.liferay.portlet.documentlibrary.service.persistence.DLFileShortcutUtil;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;

/**
 * @author Bruno Farache
 * @author Raymond Augé
 * @author Sampsa Sohlman
 */
public class DLPortletDataHandler extends BasePortletDataHandler {

	public static final String NAMESPACE = "document_library";

	public DLPortletDataHandler() {
		setAlwaysExportable(true);
		setDataLocalized(true);
		setDataPortletPreferences("rootFolderId");
		setExportControls(
			new PortletDataHandlerBoolean(
				NAMESPACE, "folders-and-documents", true, true),
			new PortletDataHandlerBoolean(NAMESPACE, "shortcuts"),
			new PortletDataHandlerBoolean(NAMESPACE, "previews-and-thumbnails"),
			new PortletDataHandlerBoolean(NAMESPACE, "ranks"));
		setExportMetadataControls(
			new PortletDataHandlerBoolean(
				NAMESPACE, "folders-and-documents", true,
				new PortletDataHandlerControl[] {
					new PortletDataHandlerBoolean(NAMESPACE, "categories"),
					new PortletDataHandlerBoolean(NAMESPACE, "comments"),
					new PortletDataHandlerBoolean(NAMESPACE, "ratings"),
					new PortletDataHandlerBoolean(NAMESPACE, "tags")
				}));
		setPublishToLiveByDefault(PropsValues.DL_PUBLISH_TO_LIVE_BY_DEFAULT);
	}

	@Override
	protected PortletPreferences doDeleteData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		if (portletDataContext.addPrimaryKey(
				DLPortletDataHandler.class, "deleteData")) {

			return portletPreferences;
		}

		DLAppLocalServiceUtil.deleteAll(portletDataContext.getScopeGroupId());

		return portletPreferences;
	}

	@Override
	protected String doExportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		portletDataContext.addPermissions(
			"com.liferay.portlet.documentlibrary",
			portletDataContext.getScopeGroupId());

		Element rootElement = addExportDataRootElement(portletDataContext);

		rootElement.addAttribute(
			"group-id", String.valueOf(portletDataContext.getScopeGroupId()));

		long rootFolderId = GetterUtil.getLong(
			portletPreferences.getValue("rootFolderId", null));

		if (rootFolderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			rootElement.addAttribute(
				"root-folder-id", String.valueOf(rootFolderId));
		}

		Element fileEntryTypesElement = rootElement.addElement(
			"file-entry-types");
		Element foldersElement = rootElement.addElement("folders");
		Element fileEntriesElement = rootElement.addElement("file-entries");
		Element fileShortcutsElement = rootElement.addElement("file-shortcuts");
		Element fileRanksElement = rootElement.addElement("file-ranks");
		Element repositoriesElement = rootElement.addElement("repositories");
		Element repositoryEntriesElement = rootElement.addElement(
			"repository-entries");

		Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(
			portletDataContext.getCompanyId());

		List<DLFileEntryType> dlFileEntryTypes =
			DLFileEntryTypeServiceUtil.getFileEntryTypes(
				new long[] {
					portletDataContext.getScopeGroupId(),
					companyGroup.getGroupId()
				});

		for (DLFileEntryType dlFileEntryType : dlFileEntryTypes) {
			if (!dlFileEntryType.isExportable()) {
				continue;
			}

			exportFileEntryType(
				portletDataContext, fileEntryTypesElement, dlFileEntryType);
		}

		List<Folder> folders = FolderUtil.findByRepositoryId(
			portletDataContext.getScopeGroupId());

		for (Folder folder : folders) {
			exportFolder(
				portletDataContext, fileEntryTypesElement, foldersElement,
				fileEntriesElement, fileShortcutsElement, fileRanksElement,
				repositoriesElement, repositoryEntriesElement, folder, false);
		}

		List<FileEntry> fileEntries = FileEntryUtil.findByR_F(
			portletDataContext.getScopeGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		for (FileEntry fileEntry : fileEntries) {
			exportFileEntry(
				portletDataContext, fileEntryTypesElement, foldersElement,
				fileEntriesElement, fileRanksElement, repositoriesElement,
				repositoryEntriesElement, fileEntry, true);
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "shortcuts")) {
			List<DLFileShortcut> fileShortcuts = DLFileShortcutUtil.findByG_F_A(
				portletDataContext.getScopeGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, true);

			for (DLFileShortcut fileShortcut : fileShortcuts) {
				exportFileShortcut(
					portletDataContext, fileEntryTypesElement, foldersElement,
					fileShortcutsElement, repositoriesElement,
					repositoryEntriesElement, fileShortcut);
			}
		}

		return getExportDataRootElementString(rootElement);
	}

	@Override
	protected PortletPreferences doImportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, String data)
		throws Exception {

		portletDataContext.importPermissions(
			"com.liferay.portlet.documentlibrary",
			portletDataContext.getSourceGroupId(),
			portletDataContext.getScopeGroupId());

		Element rootElement = portletDataContext.getImportDataRootElement();

		Element repositoriesElement = rootElement.element("repositories");

		if (repositoriesElement != null) {
			List<Element> repositoryElements = repositoriesElement.elements(
				"repository");

			for (Element repositoryElement : repositoryElements) {
				importRepository(portletDataContext, repositoryElement);
			}
		}

		Element repositoryEntriesElement = rootElement.element(
			"repository-entries");

		List<Element> repositoryEntryElements =
			repositoryEntriesElement.elements("repository-entry");

		for (Element repositoryEntryElement : repositoryEntryElements) {
			importRepositoryEntry(portletDataContext, repositoryEntryElement);
		}

		Element fileEntryTypesElement = rootElement.element("file-entry-types");

		List<Element> fileEntryTypeElements = fileEntryTypesElement.elements(
			"file-entry-type");

		for (Element fileEntryTypeElement : fileEntryTypeElements) {
			importFileEntryType(portletDataContext, fileEntryTypeElement);
		}

		Element foldersElement = rootElement.element("folders");

		List<Element> folderElements = foldersElement.elements("folder");

		for (Element folderElement : folderElements) {
			importFolder(portletDataContext, folderElement);
		}

		Element fileEntriesElement = rootElement.element("file-entries");

		List<Element> fileEntryElements = fileEntriesElement.elements(
			"file-entry");

		for (Element fileEntryElement : fileEntryElements) {
			importFileEntry(portletDataContext, fileEntryElement);
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "shortcuts")) {
			List<Element> fileShortcutElements = rootElement.element(
				"file-shortcuts").elements("file-shortcut");

			for (Element fileShortcutElement : fileShortcutElements) {
				importFileShortcut(portletDataContext, fileShortcutElement);
			}
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "ranks")) {
			Element fileRanksElement = rootElement.element("file-ranks");

			List<Element> fileRankElements = fileRanksElement.elements(
				"file-rank");

			for (Element fileRankElement : fileRankElements) {
				importFileRank(portletDataContext, fileRankElement);
			}
		}

		long rootFolderId = GetterUtil.getLong(
			rootElement.attributeValue("root-folder-id"));

		if (rootFolderId > 0) {
			Map<Long, Long> folderIds =
				(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
					DLFolder.class);

			rootFolderId = MapUtil.getLong(
				folderIds, rootFolderId, rootFolderId);

			portletPreferences.setValue(
				"rootFolderId", String.valueOf(rootFolderId));
		}

		return portletPreferences;
	}

	private static Log _log = LogFactoryUtil.getLog(DLPortletDataHandler.class);

}