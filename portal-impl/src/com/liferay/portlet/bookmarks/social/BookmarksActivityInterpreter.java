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

package com.liferay.portlet.bookmarks.social;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.service.BookmarksEntryLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.BookmarksFolderLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.permission.BookmarksEntryPermission;
import com.liferay.portlet.bookmarks.service.permission.BookmarksFolderPermission;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityConstants;
import com.liferay.portlet.social.model.SocialActivityFeedEntry;
import com.liferay.portlet.trash.util.TrashUtil;

/**
 * @author Juan Fernández
 * @author Zsolt Berentey
 */
public class BookmarksActivityInterpreter
	extends BaseSocialActivityInterpreter {

	public String[] getClassNames() {
		return _CLASS_NAMES;
	}

	@Override
	protected SocialActivityFeedEntry doInterpret(
			SocialActivity activity, ThemeDisplay themeDisplay)
		throws Exception {

		if (activity.isClassName(BookmarksEntry.class.getName())) {
			return doInterpretBookmarksEntry(activity, themeDisplay);
		}
		else if (activity.isClassName(BookmarksFolder.class.getName())) {
			return doInterpretBookmarksFolder(activity, themeDisplay);
		}

		return null;
	}

	protected SocialActivityFeedEntry doInterpretBookmarksEntry(
			SocialActivity activity, ThemeDisplay themeDisplay)
		throws Exception {

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		if (!BookmarksEntryPermission.contains(
				permissionChecker, activity.getClassPK(), ActionKeys.VIEW)) {

			return null;
		}

		String groupName = StringPool.BLANK;

		if (activity.getGroupId() != themeDisplay.getScopeGroupId()) {
			groupName = getGroupName(activity.getGroupId(), themeDisplay);
		}

		String creatorUserName = getUserName(
			activity.getUserId(), themeDisplay);
		String receiverUserName = getUserName(
			activity.getReceiverUserId(), themeDisplay);

		BookmarksEntry entry = BookmarksEntryLocalServiceUtil.getEntry(
			activity.getClassPK());

		// Link

		String link = getLink(
			BookmarksEntry.class.getName(), entry.getEntryId(),
			"/bookmarks/find_entry?entryId=", themeDisplay);

		// Title

		Object[] titleArguments = new Object[] {
			groupName, creatorUserName, receiverUserName,
			wrapLink(link, getTitle(entry))
		};

		String title = themeDisplay.translate(
			getTitlePattern(groupName, activity), titleArguments);

		// Body

		String body = StringPool.BLANK;

		return new SocialActivityFeedEntry(link, title, body);
	}

	protected SocialActivityFeedEntry doInterpretBookmarksFolder(
			SocialActivity activity, ThemeDisplay themeDisplay)
		throws Exception {

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		if (!BookmarksFolderPermission.contains(
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
		String receiverUserName = getUserName(
			activity.getReceiverUserId(), themeDisplay);

		BookmarksFolder folder = BookmarksFolderLocalServiceUtil.getFolder(
			activity.getClassPK());

		// Link

		String link = getLink(
			BookmarksFolder.class.getName(), folder.getFolderId(),
			"/bookmarks/find_folder?folderId=", themeDisplay);

		// Title

		Object[] titleArguments = new Object[] {
			groupName, creatorUserName, receiverUserName,
			wrapLink(link, getTitle(folder))
		};

		String title = themeDisplay.translate(
			getTitlePattern(groupName, activity), titleArguments);

		// Body

		String body = StringPool.BLANK;

		return new SocialActivityFeedEntry(link, title, body);
	}

	protected String getTitle(BookmarksEntry entry) throws Exception {
		if (TrashUtil.isInTrash(
				BookmarksEntry.class.getName(), entry.getEntryId())) {

			return TrashUtil.getOriginalTitle(entry.getName());
		}
		else {
			return entry.getName();
		}
	}

	protected String getTitle(BookmarksFolder folder) throws Exception {
		if (TrashUtil.isInTrash(
				BookmarksFolder.class.getName(), folder.getFolderId())) {

			return TrashUtil.getOriginalTitle(folder.getName());
		}
		else {
			return folder.getName();
		}
	}

	protected String getTitlePattern(
		String groupName, SocialActivity activity) {

		int activityType = activity.getType();

		if (activityType == BookmarksActivityKeys.ADD_ENTRY) {
			if (Validator.isNull(groupName)) {
				return "activity-bookmarks-add-entry";
			}
			else {
				return "activity-bookmarks-add-entry-in";
			}
		}
		else if (activityType == BookmarksActivityKeys.UPDATE_ENTRY) {
			if (Validator.isNull(groupName)) {
				return "activity-bookmarks-update-entry";
			}
			else {
				return "activity-bookmarks-update-entry-in";
			}
		}
		else if (activityType == SocialActivityConstants.TYPE_MOVE_TO_TRASH) {
			if (activity.isClassName(BookmarksEntry.class.getName())) {
				if (Validator.isNull(groupName)) {
					return "activity-bookmarks-entry-move-to-trash";
				}
				else {
					return "activity-bookmarks-entry-move-to-trash-in";
				}
			}
			else if (activity.isClassName(BookmarksFolder.class.getName())) {
				if (Validator.isNull(groupName)) {
					return "activity-bookmarks-folder-move-to-trash";
				}
				else {
					return "activity-bookmarks-folder-move-to-trash-in";
				}
			}
		}
		else if (activityType ==
					SocialActivityConstants.TYPE_RESTORE_FROM_TRASH) {

			if (activity.isClassName(BookmarksEntry.class.getName())) {
				if (Validator.isNull(groupName)) {
					return "activity-bookmarks-entry-restore-from-trash";
				}
				else {
					return "activity-bookmarks-entry-restore-from-trash-in";
				}
			}
			else if (activity.isClassName(BookmarksFolder.class.getName())) {
				if (Validator.isNull(groupName)) {
					return "activity-bookmarks-folder-restore-from-trash";
				}
				else {
					return "activity-bookmarks-folder-restore-from-trash-in";
				}
			}
		}

		return null;
	}

	private static final String[] _CLASS_NAMES = new String[] {
		BookmarksEntry.class.getName(), BookmarksFolder.class.getName()
	};

}