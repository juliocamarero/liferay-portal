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

package com.liferay.portlet.messageboards.social;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadLocalServiceUtil;
import com.liferay.portlet.messageboards.service.permission.MBMessagePermission;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityConstants;
import com.liferay.portlet.social.model.SocialActivityFeedEntry;
import com.liferay.portlet.trash.util.TrashUtil;

/**
 * @author Brian Wing Shun Chan
 * @author Ryan Park
 * @author Zsolt Berentey
 */
public class MBActivityInterpreter extends BaseSocialActivityInterpreter {

	public String[] getClassNames() {
		return _CLASS_NAMES;
	}

	@Override
	protected SocialActivityFeedEntry doInterpret(
			SocialActivity activity, ThemeDisplay themeDisplay)
		throws Exception {

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		int activityType = activity.getType();

		MBThread thread;
		MBMessage message;

		if (activity.isClassName(MBThread.class.getName())) {
			thread = MBThreadLocalServiceUtil.getThread(activity.getClassPK());

			message = MBMessageLocalServiceUtil.getMessage(
				thread.getRootMessageId());
		}
		else {
			message = MBMessageLocalServiceUtil.getMessage(
				activity.getClassPK());

			thread = message.getThread();
		}

		if (!MBMessagePermission.contains(
				permissionChecker, message.getMessageId(), ActionKeys.VIEW)) {

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

		// Link

		String link = getLink(message, themeDisplay);

		// Title

		Object[] titleArguments = new Object[] {
			groupName, creatorUserName, receiverUserName,
			wrapLink(link, getTitle(message))
		};

		String title = themeDisplay.translate(
			getTitlePattern(groupName, activityType,
			activity.getReceiverUserId()), titleArguments);

		// Body

		String body = StringPool.BLANK;

		if (message.getCategoryId() > 0) {
			StringBundler sb = new StringBundler(4);

			sb.append(themeDisplay.getPortalURL());
			sb.append(themeDisplay.getPathMain());
			sb.append("/message_boards/find_category?mbCategoryId=");
			sb.append(message.getCategoryId());

			String categoryLink = sb.toString();

			body = wrapLink(categoryLink, "go-to-category", themeDisplay);
		}

		return new SocialActivityFeedEntry(link, title, body);
	}

	protected String getLink(MBMessage message, ThemeDisplay themeDisplay)
		throws Exception {

		MBThread thread = message.getThread();

		if (thread.isInTrash()) {
			return TrashUtil.getViewContentURL(
				MBThread.class.getName(), thread.getThreadId(), themeDisplay);
		}

		StringBundler sb = new StringBundler(4);

		sb.append(themeDisplay.getPortalURL());
		sb.append(themeDisplay.getPathMain());
		sb.append("/message_boards/find_message?messageId=");
		sb.append(message.getMessageId());

		return sb.toString();
	}

	protected String getTitle(MBMessage message) throws Exception {
		if (message.isInTrash()) {
			return TrashUtil.getOriginalTitle(message.getSubject());
		}
		else {
			return message.getSubject();
		}
	}

	protected String getTitlePattern(
		String groupName, int activityType, long receiverUserId) {

		if (activityType == MBActivityKeys.ADD_MESSAGE) {
			if (receiverUserId == 0) {
				if (Validator.isNull(groupName)) {
					return "activity-message-boards-add-message";
				}
				else {
					return "activity-message-boards-add-message-in";
				}
			}
			else {
				if (Validator.isNull(groupName)) {
					return "activity-message-boards-reply-message";
				}
				else {
					return "activity-message-boards-reply-message-in";
				}
			}
		}
		else if ((activityType == MBActivityKeys.REPLY_MESSAGE) &&
				 (receiverUserId > 0)) {

			if (Validator.isNull(groupName)) {
				return "activity-message-boards-reply-message";
			}
			else {
				return "activity-message-boards-reply-message-in";
			}
		}
		else if (activityType == SocialActivityConstants.TYPE_MOVE_TO_TRASH) {
			if (Validator.isNull(groupName)) {
				return "activity-message-boards-move-to-trash";
			}
			else {
				return "activity-message-boards-move-to-trash-in";
			}
		}
		else if (activityType ==
					SocialActivityConstants.TYPE_RESTORE_FROM_TRASH) {

			if (Validator.isNull(groupName)) {
				return "activity-message-boards-restore-from-trash";
			}
			else {
				return "activity-message-boards-restore-from-trash-in";
			}
		}

		return null;
	}

	private static final String[] _CLASS_NAMES = new String[] {
		MBMessage.class.getName(), MBThread.class.getName()
	};

}