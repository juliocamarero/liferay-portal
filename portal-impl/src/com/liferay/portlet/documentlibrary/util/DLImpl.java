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

package com.liferay.portlet.documentlibrary.util;

import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PrefsParamUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.WorkflowDefinitionLinkLocalServiceUtil;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntryType;
import com.liferay.portlet.documentlibrary.model.DLFileEntryTypeConstants;
import com.liferay.portlet.documentlibrary.model.DLFileShortcut;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;
import com.liferay.portlet.documentlibrary.util.comparator.RepositoryModelCreateDateComparator;
import com.liferay.portlet.documentlibrary.util.comparator.RepositoryModelModifiedDateComparator;
import com.liferay.portlet.documentlibrary.util.comparator.RepositoryModelNameComparator;
import com.liferay.portlet.documentlibrary.util.comparator.RepositoryModelReadCountComparator;
import com.liferay.portlet.documentlibrary.util.comparator.RepositoryModelSizeComparator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 * @author Julio Camarero
 */
public class DLImpl implements DL {

	public void addPortletBreadcrumbEntries(
			DLFileShortcut dlFileShortcut, HttpServletRequest request,
			RenderResponse renderResponse)
		throws Exception {

		Folder folder = dlFileShortcut.getFolder();

		if (folder.getFolderId() !=
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {

			addPortletBreadcrumbEntries(folder, request, renderResponse);
		}

		DLFileShortcut unescapedDLFileShortcut =
			dlFileShortcut.toUnescapedModel();

		PortletURL portletURL = renderResponse.createRenderURL();

		portletURL.setParameter(
			"struts_action", "/document_library/view_file_entry");
		portletURL.setParameter(
			"fileEntryId", String.valueOf(dlFileShortcut.getToFileEntryId()));

		PortalUtil.addPortletBreadcrumbEntry(
			request, unescapedDLFileShortcut.getToTitle(),
			portletURL.toString());
	}

	public void addPortletBreadcrumbEntries(
			FileEntry fileEntry, HttpServletRequest request,
			RenderResponse renderResponse)
		throws Exception {

		Folder folder = fileEntry.getFolder();

		if (folder.getFolderId() !=
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {

			addPortletBreadcrumbEntries(folder, request, renderResponse);
		}

		PortletURL portletURL = renderResponse.createRenderURL();

		FileEntry unescapedFileEntry = fileEntry.toUnescapedModel();

		portletURL.setParameter(
			"struts_action", "/document_library/view_file_entry");
		portletURL.setParameter(
			"fileEntryId", String.valueOf(fileEntry.getFileEntryId()));

		PortalUtil.addPortletBreadcrumbEntry(
			request, unescapedFileEntry.getTitle(), portletURL.toString());
	}

	public void addPortletBreadcrumbEntries(
			Folder folder, HttpServletRequest request,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		portletURL.setParameter("struts_action", "/document_library/view");

		Map<String, Object> data = new HashMap<String, Object>();

		data.put("direction-right", Boolean.TRUE.toString());
		data.put("folder-id", getDefaultFolderId(request));

		PortalUtil.addPortletBreadcrumbEntry(
			request, themeDisplay.translate("home"), portletURL.toString(),
			data);

		addPortletBreadcrumbEntries(folder, request, portletURL);
	}

	public void addPortletBreadcrumbEntries(
			Folder folder, HttpServletRequest request, PortletURL portletURL)
		throws Exception {

		long defaultFolderId = getDefaultFolderId(request);

		List<Folder> ancestorFolders = Collections.emptyList();

		if ((folder != null) && (folder.getFolderId() != defaultFolderId)) {
			ancestorFolders = folder.getAncestors();

			int indexOfRootFolder = -1;

			for (int i = 0; i < ancestorFolders.size(); i++) {
				Folder ancestorFolder = ancestorFolders.get(i);

				if (defaultFolderId == ancestorFolder.getFolderId()) {
					indexOfRootFolder = i;
				}
			}

			if (indexOfRootFolder > -1) {
				ancestorFolders = ancestorFolders.subList(0, indexOfRootFolder);
			}
		}

		Collections.reverse(ancestorFolders);

		for (Folder ancestorFolder : ancestorFolders) {
			portletURL.setParameter(
				"folderId", String.valueOf(ancestorFolder.getFolderId()));

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("direction-right", Boolean.TRUE.toString());
			data.put("folder-id", ancestorFolder.getFolderId());

			PortalUtil.addPortletBreadcrumbEntry(
				request, ancestorFolder.getName(), portletURL.toString(), data);
		}

		long folderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;

		if (folder != null) {
			folderId = folder.getFolderId();
		}

		portletURL.setParameter("folderId", String.valueOf(folderId));

		if ((folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) &&
			(folderId != defaultFolderId)) {

			Folder unescapedFolder = folder.toUnescapedModel();

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("direction-right", Boolean.TRUE.toString());
			data.put("folder-id", folderId);

			PortalUtil.addPortletBreadcrumbEntry(
				request, unescapedFolder.getName(), portletURL.toString(),
				data);
		}
	}

	public void addPortletBreadcrumbEntries(
			Folder folder, HttpServletRequest request,
			RenderResponse renderResponse)
		throws Exception {

		String strutsAction = ParamUtil.getString(request, "struts_action");

		long groupId = ParamUtil.getLong(request, "groupId");

		PortletURL portletURL = renderResponse.createRenderURL();

		if (strutsAction.equals("/journal/select_document_library") ||
			strutsAction.equals("/document_library/select_file_entry") ||
			strutsAction.equals("/document_library/select_folder") ||
			strutsAction.equals("/document_library_display/select_folder") ||
			strutsAction.equals("/image_gallery_display/select_folder")) {

			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			portletURL.setWindowState(LiferayWindowState.POP_UP);

			portletURL.setParameter("struts_action", strutsAction);
			portletURL.setParameter("groupId", String.valueOf(groupId));

			PortalUtil.addPortletBreadcrumbEntry(
				request, themeDisplay.translate("home"), portletURL.toString());
		}
		else {
			portletURL.setParameter("struts_action", "/document_library/view");
		}

		addPortletBreadcrumbEntries(folder, request, portletURL);
	}

	public void addPortletBreadcrumbEntries(
			long folderId, HttpServletRequest request,
			RenderResponse renderResponse)
		throws Exception {

		if (folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			Folder folder = DLAppLocalServiceUtil.getFolder(folderId);

			if (folder.getFolderId() !=
					DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {

				addPortletBreadcrumbEntries(folder, request, renderResponse);
			}
		}
	}

	public int compareVersions(String version1, String version2) {
		int[] splitVersion1 = StringUtil.split(version1, StringPool.PERIOD, 0);
		int[] splitVersion2 = StringUtil.split(version2, StringPool.PERIOD, 0);

		if ((splitVersion1.length != 2) && (splitVersion2.length != 2)) {
			return 0;
		}
		else if (splitVersion1.length != 2) {
			return -1;
		}
		else if (splitVersion2.length != 2) {
			return 1;
		}

		if (splitVersion1[0] > splitVersion2[0]) {
			return 1;
		}
		else if (splitVersion1[0] < splitVersion2[0]) {
			return -1;
		}
		else if (splitVersion1[1] > splitVersion2[1]) {
			return 1;
		}
		else if (splitVersion1[1] < splitVersion2[1]) {
			return -1;
		}

		return 0;
	}

	public String getAbsolutePath(PortletRequest portletRequest, long folderId)
		throws PortalException, SystemException {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (folderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			return themeDisplay.translate("home");
		}

		DLFolder dlFolder = DLFolderLocalServiceUtil.getFolder(folderId);

		List<DLFolder> dlFolders = dlFolder.getAncestors();

		Collections.reverse(dlFolders);

		StringBundler sb = new StringBundler((dlFolders.size() * 3) + 6);

		sb.append(themeDisplay.translate("home"));
		sb.append(StringPool.SPACE);

		for (DLFolder curDLFolder : dlFolders) {
			sb.append(StringPool.RAQUO);
			sb.append(StringPool.SPACE);
			sb.append(curDLFolder.getName());
		}

		sb.append(StringPool.RAQUO);
		sb.append(StringPool.SPACE);
		sb.append(dlFolder.getName());

		return sb.toString();
	}

	public Set<String> getAllMediaGalleryMimeTypes() {
		return _allMediaGalleryMimeTypes;
	}

	public String getDDMStructureKey(DLFileEntryType dlFileEntryType) {
		return getDDMStructureKey(dlFileEntryType.getUuid());
	}

	public String getDDMStructureKey(String fileEntryTypeUuid) {
		return "auto_" + fileEntryTypeUuid;
	}

	public String getDeprecatedDDMStructureKey(
		DLFileEntryType dlFileEntryType) {

		return getDeprecatedDDMStructureKey(
			dlFileEntryType.getFileEntryTypeId());
	}

	public String getDeprecatedDDMStructureKey(long fileEntryTypeId) {
		return "auto_" + fileEntryTypeId;
	}

	public String getDividedPath(long id) {
		StringBundler sb = new StringBundler(16);

		long dividend = id;

		while ((dividend / _DIVISOR) != 0) {
			sb.append(StringPool.SLASH);
			sb.append(dividend % _DIVISOR);

			dividend = dividend / _DIVISOR;
		}

		sb.append(StringPool.SLASH);
		sb.append(id);

		return sb.toString();
	}

	public String getDLControlPanelLink(
			PortletRequest portletRequest, long folderId)
		throws PortalException, SystemException {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletURL portletURL = PortletURLFactoryUtil.create(
			portletRequest, PortletKeys.DOCUMENT_LIBRARY,
			PortalUtil.getControlPanelPlid(themeDisplay.getCompanyId()),
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter("struts_action", "/document_library/view");
		portletURL.setParameter("folderId", String.valueOf(folderId));

		return portletURL.toString();
	}

	public String getFileEntryImage(
		FileEntry fileEntry, ThemeDisplay themeDisplay) {

		StringBundler sb = new StringBundler(5);

		sb.append("<img style=\"border-width: 0; text-align: left;\" src=\"");
		sb.append(themeDisplay.getPathThemeImages());
		sb.append("/file_system/small/");
		sb.append(fileEntry.getIcon());
		sb.append(".png\">");

		return sb.toString();
	}

	public String getFileIcon(String extension) {
		if (!_fileIcons.contains(extension)) {
			extension = _DEFAULT_FILE_ICON;
		}

		return extension;
	}

	public String getGenericName(String extension) {
		String genericName = _genericNames.get(extension);

		if (genericName == null) {
			genericName = _DEFAULT_GENERIC_NAME;
		}

		return genericName;
	}

	public String[] getMediaGalleryMimeTypes(
		PortletPreferences portletPreferences, PortletRequest portletRequest) {

		String mimeTypes = PrefsParamUtil.getString(
			portletPreferences, portletRequest, "mimeTypes",
			_allMediaGalleryMimeTypesString);

		String[] mimeTypesArray = StringUtil.split(mimeTypes);

		Arrays.sort(mimeTypesArray);

		return mimeTypesArray;
	}

	public String getPreviewURL(
		FileEntry fileEntry, FileVersion fileVersion, ThemeDisplay themeDisplay,
		String queryString) {

		return getPreviewURL(
			fileEntry, fileVersion, themeDisplay, queryString, true, true);
	}

	/**
	 * @deprecated {@link #getPreviewURL(FileEntry, FileVersion, ThemeDisplay,
	 *             String, boolean, boolean)}
	 */
	public String getPreviewURL(
		FileEntry fileEntry, FileVersion fileVersion, ThemeDisplay themeDisplay,
		String queryString, boolean appendToken) {

		return getPreviewURL(
			fileEntry, fileVersion, themeDisplay, queryString, true, true);
	}

	public String getPreviewURL(
		FileEntry fileEntry, FileVersion fileVersion, ThemeDisplay themeDisplay,
		String queryString, boolean appendVersion, boolean absoluteURL) {

		StringBundler sb = new StringBundler(17);

		if (themeDisplay != null) {
			if (absoluteURL) {
				sb.append(themeDisplay.getPortalURL());
			}
		}

		sb.append(PortalUtil.getPathContext());
		sb.append("/documents/");
		sb.append(fileEntry.getRepositoryId());
		sb.append(StringPool.SLASH);
		sb.append(fileEntry.getFolderId());
		sb.append(StringPool.SLASH);
		sb.append(HttpUtil.encodeURL(HtmlUtil.unescape(fileEntry.getTitle())));
		sb.append(StringPool.SLASH);
		sb.append(fileEntry.getUuid());

		if (appendVersion) {
			sb.append("?version=");
			sb.append(fileVersion.getVersion());
		}

		if (ImageProcessorUtil.isImageSupported(fileVersion)) {
			if (appendVersion) {
				sb.append("&t=");
			}
			else {
				sb.append("?t=");
			}

			Date modifiedDate = fileVersion.getModifiedDate();

			sb.append(modifiedDate.getTime());
		}

		sb.append(queryString);

		if (themeDisplay != null) {
			PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

			if (portletDisplay != null) {
				String portletId = portletDisplay.getId();

				if (portletId.equals(PortletKeys.TRASH)) {
					sb.append("&status=");
					sb.append(WorkflowConstants.STATUS_IN_TRASH);
				}
			}
		}

		String previewURL = sb.toString();

		if ((themeDisplay != null) && themeDisplay.isAddSessionIdToURL()) {
			return PortalUtil.getURLWithSessionId(
				previewURL, themeDisplay.getSessionId());
		}

		return previewURL;
	}

	public OrderByComparator getRepositoryModelOrderByComparator(
		String orderByCol, String orderByType) {

		boolean orderByAsc = true;

		if (orderByType.equals("desc")) {
			orderByAsc = false;
		}

		OrderByComparator orderByComparator = null;

		if (orderByCol.equals("creationDate")) {
			orderByComparator = new RepositoryModelCreateDateComparator(
				orderByAsc);
		}
		else if (orderByCol.equals("downloads")) {
			orderByComparator = new RepositoryModelReadCountComparator(
				orderByAsc);
		}
		else if (orderByCol.equals("modifiedDate")) {
			orderByComparator = new RepositoryModelModifiedDateComparator(
				orderByAsc);
		}
		else if (orderByCol.equals("size")) {
			orderByComparator = new RepositoryModelSizeComparator(orderByAsc);
		}
		else {
			orderByComparator = new RepositoryModelNameComparator(orderByAsc);
		}

		return orderByComparator;
	}

	public String getTempFileId(long id, String version) {
		return getTempFileId(id, version, null);
	}

	public String getTempFileId(long id, String version, String languageId) {
		if (Validator.isNull(languageId)) {
			return String.valueOf(id).concat(StringPool.PERIOD).concat(version);
		}

		StringBundler sb = new StringBundler(5);

		sb.append(id);
		sb.append(StringPool.PERIOD);
		sb.append(version);
		sb.append(StringPool.PERIOD);
		sb.append(languageId);

		return sb.toString();
	}

	public String getThumbnailSrc(
			FileEntry fileEntry, DLFileShortcut dlFileShortcut,
			ThemeDisplay themeDisplay)
		throws Exception {

		return getThumbnailSrc(
			fileEntry, fileEntry.getFileVersion(), dlFileShortcut,
			themeDisplay);
	}

	public String getThumbnailSrc(
			FileEntry fileEntry, FileVersion fileVersion,
			DLFileShortcut dlFileShortcut, ThemeDisplay themeDisplay)
		throws Exception {

		StringBundler sb = new StringBundler(4);

		sb.append(themeDisplay.getPathThemeImages());
		sb.append("/file_system/large/");
		sb.append(getGenericName(fileEntry.getExtension()));
		sb.append(".png");

		String thumbnailSrc = sb.toString();

		String thumbnailQueryString = null;

		if (GetterUtil.getBoolean(
				PropsUtil.get(PropsKeys.DL_FILE_ENTRY_THUMBNAIL_ENABLED))) {

			if (ImageProcessorUtil.hasImages(fileVersion)) {
				thumbnailQueryString = "&imageThumbnail=1";
			}
			else if (PDFProcessorUtil.hasImages(fileVersion)) {
				thumbnailQueryString = "&documentThumbnail=1";
			}
			else if (VideoProcessorUtil.hasVideo(fileVersion)) {
				thumbnailQueryString = "&videoThumbnail=1";
			}
		}

		if (Validator.isNotNull(thumbnailQueryString)) {
			thumbnailSrc = getPreviewURL(
				fileEntry, fileVersion, themeDisplay, thumbnailQueryString,
				true, true);
		}

		return thumbnailSrc;
	}

	public String getThumbnailStyle() throws Exception {
		return getThumbnailStyle(true, 0);
	}

	public String getThumbnailStyle(boolean max, int margin) throws Exception {
		StringBundler sb = new StringBundler(5);

		if (max) {
			sb.append("max-height: ");
		}
		else {
			sb.append("height: ");
		}

		sb.append(
			PrefsPropsUtil.getLong(
				PropsKeys.DL_FILE_ENTRY_THUMBNAIL_MAX_HEIGHT) + 2 * margin);

		if (max) {
			sb.append("px; max-width: ");
		}
		else {
			sb.append("px; width: ");
		}

		sb.append(
			PrefsPropsUtil.getLong(
				PropsKeys.DL_FILE_ENTRY_THUMBNAIL_MAX_WIDTH) + 2 * margin);
		sb.append("px;");

		return sb.toString();
	}

	public String getTitleWithExtension(FileEntry fileEntry) {
		String title = fileEntry.getTitle();
		String extension = fileEntry.getExtension();

		return getTitleWithExtension(title, extension);
	}

	public String getTitleWithExtension(String title, String extension) {
		if (Validator.isNotNull(extension)) {
			String periodAndExtension = StringPool.PERIOD + extension;

			if (!title.endsWith(periodAndExtension)) {
				title += periodAndExtension;
			}
		}

		return title;
	}

	public String getWebDavURL(
			ThemeDisplay themeDisplay, Folder folder, FileEntry fileEntry)
		throws PortalException, SystemException {

		return getWebDavURL(themeDisplay, folder, fileEntry, false);
	}

	public String getWebDavURL(
			ThemeDisplay themeDisplay, Folder folder, FileEntry fileEntry,
			boolean manualCheckInRequired)
		throws PortalException, SystemException {

		return getWebDavURL(
			themeDisplay, folder, fileEntry, manualCheckInRequired, false);
	}

	public String getWebDavURL(
			ThemeDisplay themeDisplay, Folder folder, FileEntry fileEntry,
			boolean manualCheckInRequired, boolean openDocumentUrl)
		throws PortalException, SystemException {

		StringBundler webDavURL = new StringBundler(8);

		boolean secure = false;

		if (themeDisplay.isSecure() ||
			PropsValues.WEBDAV_SERVLET_HTTPS_REQUIRED) {

			secure = true;
		}

		String portalURL = PortalUtil.getPortalURL(
			themeDisplay.getServerName(), themeDisplay.getServerPort(), secure);

		webDavURL.append(portalURL);

		webDavURL.append(themeDisplay.getPathContext());
		webDavURL.append("/webdav");

		if (manualCheckInRequired) {
			webDavURL.append(DLUtil.MANUAL_CHECK_IN_REQUIRED_PATH);
		}

		String fileEntryTitle = null;

		if (fileEntry != null) {
			String extension = fileEntry.getExtension();

			fileEntryTitle = fileEntry.getTitle();

			if (openDocumentUrl && DLUtil.isOfficeExtension(extension) &&
				!fileEntryTitle.endsWith(StringPool.PERIOD + extension)) {

				webDavURL.append(DLUtil.OFFICE_EXTENSION_PATH);

				fileEntryTitle += StringPool.PERIOD + extension;
			}
		}

		Group group = themeDisplay.getScopeGroup();

		webDavURL.append(group.getFriendlyURL());
		webDavURL.append("/document_library");

		StringBuilder sb = new StringBuilder();

		if (folder != null) {
			Folder curFolder = folder;

			while (true) {
				sb.insert(0, HttpUtil.encodeURL(curFolder.getName(), true));
				sb.insert(0, StringPool.SLASH);

				if (curFolder.getParentFolderId() ==
						DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {

					break;
				}
				else {
					curFolder = DLAppLocalServiceUtil.getFolder(
						curFolder.getParentFolderId());
				}
			}
		}

		if (fileEntry != null) {
			sb.append(StringPool.SLASH);
			sb.append(HttpUtil.encodeURL(fileEntryTitle, true));
		}

		webDavURL.append(sb.toString());

		return webDavURL.toString();
	}

	public boolean hasWorkflowDefinitionLink(
			long companyId, long groupId, long folderId, long fileEntryTypeId)
		throws Exception {

		while (folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			DLFolder dlFolder = DLFolderLocalServiceUtil.fetchDLFolder(
				folderId);

			if (dlFolder == null) {
				return false;
			}

			if (dlFolder.isOverrideFileEntryTypes()) {
				break;
			}

			folderId = dlFolder.getParentFolderId();
		}

		if (WorkflowDefinitionLinkLocalServiceUtil.hasWorkflowDefinitionLink(
				companyId, groupId, DLFolderConstants.getClassName(), folderId,
				fileEntryTypeId) ||
			WorkflowDefinitionLinkLocalServiceUtil.hasWorkflowDefinitionLink(
				companyId, groupId, DLFolderConstants.getClassName(), folderId,
				DLFileEntryTypeConstants.FILE_ENTRY_TYPE_ID_ALL)) {

			return true;
		}

		return false;
	}

	public boolean isAutoGeneratedDLFileEntryTypeDDMStructureKey(
		String ddmStructureKey) {

		if (ddmStructureKey.startsWith("auto_")) {
			return true;
		}

		return false;
	}

	public boolean isOfficeExtension(String extension) {
		if (extension.equalsIgnoreCase("doc") ||
			extension.equalsIgnoreCase("docx") ||
			extension.equalsIgnoreCase("dot") ||
			extension.equalsIgnoreCase("ppt") ||
			extension.equalsIgnoreCase("pptx") ||
			extension.equalsIgnoreCase("xls") ||
			extension.equalsIgnoreCase("xlsx")) {

			return true;
		}

		return false;
	}

	protected long getDefaultFolderId(HttpServletRequest request)
		throws Exception {

		PortletPreferences portletPreferences =
			PortletPreferencesFactoryUtil.getPortletPreferences(
				request, PortalUtil.getPortletId(request));

		return GetterUtil.getLong(
			portletPreferences.getValue(
				"rootFolderId",
				String.valueOf(DLFolderConstants.DEFAULT_PARENT_FOLDER_ID)));
	}

	private static void _populateGenericNamesMap(String genericName) {
		String[] extensions = PropsUtil.getArray(
			PropsKeys.DL_FILE_GENERIC_EXTENSIONS, new Filter(genericName));

		for (String extension : extensions) {
			_genericNames.put(extension, genericName);
		}
	}

	private static final String _DEFAULT_FILE_ICON = "page";

	private static final String _DEFAULT_GENERIC_NAME = "default";

	private static final long _DIVISOR = 256;

	private static Log _log = LogFactoryUtil.getLog(DLImpl.class);

	private static Set<String> _allMediaGalleryMimeTypes =
		new TreeSet<String>();
	private static String _allMediaGalleryMimeTypesString;
	private static Set<String> _fileIcons = new HashSet<String>();
	private static Map<String, String> _genericNames =
		new HashMap<String, String>();

	static {
		_allMediaGalleryMimeTypes.addAll(
			SetUtil.fromArray(
				PropsUtil.getArray(
					PropsKeys.DL_FILE_ENTRY_PREVIEW_AUDIO_MIME_TYPES)));
		_allMediaGalleryMimeTypes.addAll(
			SetUtil.fromArray(
				PropsUtil.getArray(
					PropsKeys.DL_FILE_ENTRY_PREVIEW_VIDEO_MIME_TYPES)));
		_allMediaGalleryMimeTypes.addAll(
			SetUtil.fromArray(
				PropsUtil.getArray(
					PropsKeys.DL_FILE_ENTRY_PREVIEW_IMAGE_MIME_TYPES)));

		_allMediaGalleryMimeTypesString = StringUtil.merge(
			_allMediaGalleryMimeTypes);

		String[] fileIcons = null;

		try {
			fileIcons = PropsUtil.getArray(PropsKeys.DL_FILE_ICONS);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}

			fileIcons = new String[] {StringPool.BLANK};
		}

		for (int i = 0; i < fileIcons.length; i++) {

			// Only process non wildcard extensions

			if (!StringPool.STAR.equals(fileIcons[i])) {

				// Strip starting period

				String extension = fileIcons[i];

				if (extension.length() > 0) {
					extension = extension.substring(1);
				}

				_fileIcons.add(extension);
			}
		}

		String[] genericNames = PropsUtil.getArray(
				PropsKeys.DL_FILE_GENERIC_NAMES);

		for (String genericName : genericNames) {
			_populateGenericNamesMap(genericName);
		}
	}

}