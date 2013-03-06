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

package com.liferay.portlet.documentlibrary.social;

import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.permission.DLFileEntryPermission;
import com.liferay.portlet.documentlibrary.service.permission.DLFolderPermission;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityConstants;
import com.liferay.portlet.social.model.SocialActivityFeedEntry;
import com.liferay.portlet.trash.util.TrashUtil;

/**
 * @author Ryan Park
 * @author Zsolt Berentey
 */
public class DLActivityInterpreter extends BaseSocialActivityInterpreter {

	public String[] getClassNames() {
		return _CLASS_NAMES;
	}

	@Override
	protected SocialActivityFeedEntry doInterpret(
			SocialActivity activity, ThemeDisplay themeDisplay)
		throws Exception {

		if (activity.isClassName(DLFileEntry.class.getName())) {
			return doInterpretFileEntry(activity, themeDisplay);
		}
		else if (activity.isClassName(DLFolder.class.getName())) {
			return doInterpretFolder(activity, themeDisplay);
		}

		return null;
	}

	protected SocialActivityFeedEntry doInterpretFileEntry(
			SocialActivity activity, ThemeDisplay themeDisplay)
		throws Exception {

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		if (!DLFileEntryPermission.contains(
				permissionChecker, activity.getClassPK(), ActionKeys.VIEW)) {

			return null;
		}

		String groupName = StringPool.BLANK;

		if (activity.getGroupId() != themeDisplay.getScopeGroupId()) {
			groupName = getGroupName(activity.getGroupId(), themeDisplay);
		}

		String creatorUserName = getUserName(
			activity.getUserId(), themeDisplay);

		FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(
			activity.getClassPK());

		// Link

		String link = getLink(
			DLFileEntry.class.getName(), fileEntry.getFileEntryId(),
			"/document_library/find_file_entry?fileEntryId=", themeDisplay);

		// Title

		Object[] titleArguments = new Object[] {
			groupName, creatorUserName, wrapLink(link, getTitle(fileEntry))
		};

		String title = themeDisplay.translate(
			getTitlePattern(groupName, activity), titleArguments);

		// Body

		String body = getBody(fileEntry, themeDisplay);

		return new SocialActivityFeedEntry(link, title, body);
	}

	protected SocialActivityFeedEntry doInterpretFolder(
			SocialActivity activity, ThemeDisplay themeDisplay)
		throws Exception {

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		if (!DLFolderPermission.contains(
				permissionChecker, activity.getGroupId(), activity.getClassPK(),
				ActionKeys.VIEW)) {

			return null;
		}

		String groupName = StringPool.BLANK;

		if (activity.getGroupId() != themeDisplay.getScopeGroupId()) {
			groupName = getGroupName(activity.getGroupId(), themeDisplay);
		}

		String creatorUserName = getUserName(
			activity.getUserId(), themeDisplay);

		Folder folder = DLAppLocalServiceUtil.getFolder(activity.getClassPK());

		// Link

		String link = getLink(
			DLFolder.class.getName(), folder.getFolderId(),
			"/document_library/find_folder?folderId=", themeDisplay);

		// Title

		Object[] titleArguments = new Object[] {
			groupName, creatorUserName, wrapLink(link, getTitle(folder))
		};

		String title = themeDisplay.translate(
			getTitlePattern(groupName, activity), titleArguments);

		// Body

		String body = StringPool.BLANK;

		return new SocialActivityFeedEntry(link, title, body);
	}

	protected String getBody(FileEntry fileEntry, ThemeDisplay themeDisplay)
		throws Exception {

		if (TrashUtil.isInTrash(
				DLFileEntry.class.getName(), fileEntry.getFileEntryId())) {

			return StringPool.BLANK;
		}

		AssetRendererFactory assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				DLFileEntry.class.getName());

		AssetRenderer assetRenderer = assetRendererFactory.getAssetRenderer(
			fileEntry.getFileEntryId());

		String fileEntryLink = assetRenderer.getURLDownload(themeDisplay);

		StringBundler sb = new StringBundler(3);

		sb.append(wrapLink(fileEntryLink, "download-file", themeDisplay));

		sb.append(StringPool.SPACE);

		String folderLink = getFolderLink(fileEntry, themeDisplay);

		sb.append(wrapLink(folderLink, "go-to-folder", themeDisplay));

		return sb.toString();
	}

	protected String getFolderLink(
		FileEntry fileEntry, ThemeDisplay themeDisplay) {

		StringBundler sb = new StringBundler(6);

		sb.append(themeDisplay.getPortalURL());
		sb.append(themeDisplay.getPathMain());
		sb.append("/document_library/find_folder?groupId=");
		sb.append(fileEntry.getRepositoryId());
		sb.append("&folderId=");
		sb.append(fileEntry.getFolderId());

		return sb.toString();
	}

	protected String getTitle(FileEntry fileEntry) throws Exception {
		if (TrashUtil.isInTrash(
				DLFileEntry.class.getName(), fileEntry.getFileEntryId())) {

			return TrashUtil.getOriginalTitle(fileEntry.getTitle());
		}
		else {
			return fileEntry.getTitle();
		}
	}

	protected String getTitle(Folder folder) throws Exception {
		if (TrashUtil.isInTrash(
				DLFolder.class.getName(), folder.getFolderId())) {

			return TrashUtil.getOriginalTitle(folder.getName());
		}
		else {
			return folder.getName();
		}
	}

	protected String getTitlePattern(
		String groupName, SocialActivity activity) {

		int activityType = activity.getType();

		if (activityType == DLActivityKeys.ADD_FILE_ENTRY) {
			if (Validator.isNull(groupName)) {
				return "activity-document-library-add-file";
			}
			else {
				return "activity-document-library-add-file-in";
			}
		}
		else if (activityType == DLActivityKeys.UPDATE_FILE_ENTRY) {
			if (Validator.isNull(groupName)) {
				return "activity-document-library-update-file";
			}
			else {
				return "activity-document-library-update-file-in";
			}
		}
		else if (activityType == SocialActivityConstants.TYPE_MOVE_TO_TRASH) {
			if (activity.isClassName(DLFileEntry.class.getName())) {
				if (Validator.isNull(groupName)) {
					return "activity-document-library-file-move-to-trash";
				}
				else {
					return "activity-document-library-file-move-to-trash-in";
				}
			}
			else if (activity.isClassName(DLFolder.class.getName())) {
				if (Validator.isNull(groupName)) {
					return "activity-document-library-folder-move-to-trash";
				}
				else {
					return "activity-document-library-folder-move-to-trash-in";
				}
			}
		}
		else if (activityType ==
					SocialActivityConstants.TYPE_RESTORE_FROM_TRASH) {

			if (activity.isClassName(DLFileEntry.class.getName())) {
				if (Validator.isNull(groupName)) {
					return "activity-document-library-file-restore-from-trash";
				}
				else {
					return
						"activity-document-library-file-restore-from-trash-in";
				}
			}
			else if (activity.isClassName(DLFolder.class.getName())) {
				if (Validator.isNull(groupName)) {
					return
						"activity-document-library-folder-restore-from-trash";
				}
				else {
					return
						"activity-document-library-folder-restore-from-trash-" +
							"in";
				}
			}
		}

		return null;
	}

	private static final String[] _CLASS_NAMES = new String[] {
		DLFileEntry.class.getName(), DLFolder.class.getName()
	};

}