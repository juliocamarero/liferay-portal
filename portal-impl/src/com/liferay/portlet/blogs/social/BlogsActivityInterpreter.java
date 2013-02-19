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

package com.liferay.portlet.blogs.social;

import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.blogs.service.permission.BlogsEntryPermission;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityConstants;
import com.liferay.portlet.social.model.SocialActivityFeedEntry;
import com.liferay.portlet.trash.util.TrashUtil;

import java.text.Format;

/**
 * @author Brian Wing Shun Chan
 * @author Ryan Park
 * @author Zsolt Berentey
 */
public class BlogsActivityInterpreter extends BaseSocialActivityInterpreter {

	public String[] getClassNames() {
		return _CLASS_NAMES;
	}

	@Override
	protected SocialActivityFeedEntry doInterpret(
			SocialActivity activity, ThemeDisplay themeDisplay)
		throws Exception {

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		if (!BlogsEntryPermission.contains(
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

		int activityType = activity.getType();

		BlogsEntry entry = BlogsEntryLocalServiceUtil.getEntry(
			activity.getClassPK());

		// Link

		String link = getLink(entry, themeDisplay);

		// Title

		String displayTitle;
		String displayDate;

		if ((activityType == BlogsActivityKeys.ADD_ENTRY) &&
			(entry.getStatus() == WorkflowConstants.STATUS_SCHEDULED)) {

			displayTitle = getTitle(entry);

			Format dateFormatDate =
				FastDateFormatFactoryUtil.getSimpleDateFormat(
					"MMMM d", themeDisplay.getLocale(),
					themeDisplay.getTimeZone());

			displayDate = dateFormatDate.format(entry.getDisplayDate());
		}
		else {
			displayTitle = wrapLink(link, getTitle(entry));
			displayDate = StringPool.BLANK;
		}

		Object[] titleArguments = new Object[] {
			groupName, creatorUserName, receiverUserName, displayTitle,
			displayDate
		};

		String title = themeDisplay.translate(
			getTitlePattern(groupName, entry, activityType), titleArguments);

		// Body

		String body = StringPool.BLANK;

		return new SocialActivityFeedEntry(link, title, body);
	}

	protected String getLink(BlogsEntry entry, ThemeDisplay themeDisplay)
		throws Exception {

		if (entry.isInTrash()) {
			return TrashUtil.getViewContentURL(
				BlogsEntry.class.getName(), entry.getEntryId(), themeDisplay);
		}

		StringBundler sb = new StringBundler(4);

		sb.append(themeDisplay.getPortalURL());
		sb.append(themeDisplay.getPathMain());
		sb.append("/blogs/find_entry?entryId=");
		sb.append(entry.getEntryId());

		return sb.toString();
	}

	protected String getTitle(BlogsEntry entry) throws Exception {
		if (entry.isInTrash()) {
			return TrashUtil.getOriginalTitle(entry.getTitle());
		}
		else {
			return entry.getTitle();
		}
	}

	protected String getTitlePattern(
		String groupName, BlogsEntry entry, int activityType) {

		if ((activityType == BlogsActivityKeys.ADD_COMMENT) ||
			(activityType == SocialActivityConstants.TYPE_ADD_COMMENT)) {

			if (Validator.isNull(groupName)) {
				return "activity-blogs-add-comment";
			}
			else {
				return "activity-blogs-add-comment-in";
			}
		}
		else if (activityType == BlogsActivityKeys.ADD_ENTRY) {
			if (entry.getStatus() == WorkflowConstants.STATUS_SCHEDULED) {
				if (Validator.isNull(groupName)) {
					return "activity-blogs-scheduled-entry";
				}
				else {
					return "activity-blogs-scheduled-entry-in";
				}
			}
			else {
				if (Validator.isNull(groupName)) {
					return "activity-blogs-add-entry";
				}
				else {
					return "activity-blogs-add-entry-in";
				}
			}
		}
		else if (activityType == SocialActivityConstants.TYPE_MOVE_TO_TRASH) {
			if (Validator.isNull(groupName)) {
				return "activity-blogs-move-to-trash";
			}
			else {
				return "activity-blogs-move-to-trash-in";
			}
		}
		else if (activityType ==
					SocialActivityConstants.TYPE_RESTORE_FROM_TRASH) {

			if (Validator.isNull(groupName)) {
				return "activity-blogs-restore-from-trash";
			}
			else {
				return "activity-blogs-restore-from-trash-in";
			}
		}
		else if (activityType == BlogsActivityKeys.UPDATE_ENTRY) {
			if (Validator.isNull(groupName)) {
				return "activity-blogs-update-entry";
			}
			else {
				return "activity-blogs-update-entry-in";
			}
		}

		return null;
	}

	private static final String[] _CLASS_NAMES = new String[] {
		BlogsEntry.class.getName()
	};

}