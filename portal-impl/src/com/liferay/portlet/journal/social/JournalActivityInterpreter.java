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

package com.liferay.portlet.journal.social;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleConstants;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.permission.JournalPermission;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityFeedEntry;

/**
 * @author Roberto Diaz
 */
public class JournalActivityInterpreter extends BaseSocialActivityInterpreter {

	public String[] getClassNames() {
		return _CLASS_NAMES;
	}

	@Override
	protected SocialActivityFeedEntry doInterpret(
			SocialActivity activity, ThemeDisplay themeDisplay)
		throws Exception {

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		JournalArticle article =
			JournalArticleLocalServiceUtil.getJournalArticle(
				activity.getClassPK());

		if (!JournalPermission.contains(
				permissionChecker, activity.getGroupId(), ActionKeys.VIEW)) {
			return null;
		}

		String groupName = StringPool.BLANK;

		if (activity.getGroupId() != themeDisplay.getScopeGroupId()) {
			groupName = getGroupName(activity.getGroupId(), themeDisplay);
		}

		String creatorUserName = getUserName(
			activity.getUserId(), themeDisplay);

		int activityType = activity.getType();

		// Link

		JournalArticle lastestArticle =
			JournalArticleLocalServiceUtil.getLatestArticle(
				article.getGroupId(), article.getArticleId());

		String groupFriendlyURL = PortalUtil.getGroupFriendlyURL(
				themeDisplay.getScopeGroup(), false, themeDisplay);

		String link =
				groupFriendlyURL.concat(
					JournalArticleConstants.CANONICAL_URL_SEPARATOR).concat(
						lastestArticle.getUrlTitle());

		// Title

		String titlePattern = null;

		if (activityType == JournalActivityKeys.ADD_JOURNAL_ARTICLE) {
			if (Validator.isNull(groupName)) {
				titlePattern = "activity-journal-add-article";
			}
			else {
				titlePattern = "activity-journal-add-article-in";
			}
		}
		else if (activityType == JournalActivityKeys.UPDATE_JOURNAL_ARTICLE) {
			if (Validator.isNull(groupName)) {
				titlePattern = "activity-journal-update-article";
			}
			else {
				titlePattern = "activity-journal-update-article-in";
			}
		}

		String eventTitle = getValue(
			activity.getExtraData(), "title", article.getTitle());

		Object[] titleArguments = new Object[] {
				groupName, creatorUserName, wrapLink(link, eventTitle)
			};

		String title = themeDisplay.translate(titlePattern, titleArguments);

		// Body

		return new SocialActivityFeedEntry(link, title, StringPool.BLANK);
	}

	private static final String[] _CLASS_NAMES = new String[] {
		JournalArticle.class.getName()
	};

}