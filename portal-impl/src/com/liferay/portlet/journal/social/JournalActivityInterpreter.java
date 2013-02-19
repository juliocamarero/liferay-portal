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
import com.liferay.portlet.journal.service.permission.JournalArticlePermission;
import com.liferay.portlet.journal.service.permission.JournalPermission;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityConstants;
import com.liferay.portlet.social.model.SocialActivityFeedEntry;
import com.liferay.portlet.trash.util.TrashUtil;

import java.util.Locale;

/**
 * @author Roberto Diaz
 * @author Zsolt Berentey
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

		int activityType = activity.getType();

		JournalArticle article =
			JournalArticleLocalServiceUtil.getJournalArticle(
				activity.getClassPK());

		if ((activityType == JournalActivityKeys.ADD_ARTICLE) &&
			!JournalPermission.contains(
				permissionChecker, activity.getGroupId(),
				ActionKeys.ADD_ARTICLE)) {

			return null;
		}
		else if ((activityType == JournalActivityKeys.UPDATE_ARTICLE) &&
				 !JournalArticlePermission.contains(
					 permissionChecker, article, ActionKeys.UPDATE)) {

			return null;
		}

		String groupName = StringPool.BLANK;

		if (activity.getGroupId() != themeDisplay.getScopeGroupId()) {
			groupName = getGroupName(activity.getGroupId(), themeDisplay);
		}

		String creatorUserName = getUserName(
			activity.getUserId(), themeDisplay);

		// Link

		String link = getLink(article, themeDisplay);

		// Title

		Object[] titleArguments = null;

		if (Validator.isNotNull(link)) {
			titleArguments = new Object[] {
				groupName, creatorUserName,
				wrapLink(link, getTitle(article, themeDisplay.getLocale()))
			};
		}
		else {
			titleArguments = new Object[] {
				groupName, creatorUserName,
				getTitle(article, themeDisplay.getLocale()),
			};
		}

		String title = themeDisplay.translate(
			getTitlePattern(groupName, activityType), titleArguments);

		// Body

		String body = StringPool.BLANK;

		return new SocialActivityFeedEntry(link, title, body);
	}

	protected String getLink(JournalArticle article, ThemeDisplay themeDisplay)
		throws Exception {

		if (TrashUtil.isInTrash(
				JournalArticle.class.getName(), article.getResourcePrimKey())) {

			return TrashUtil.getViewContentURL(
				JournalArticle.class.getName(), article.getResourcePrimKey(),
				themeDisplay);
		}

		String link = null;

		JournalArticle lastestArticle =
			JournalArticleLocalServiceUtil.getLatestArticle(
				article.getGroupId(), article.getArticleId());

		if (Validator.isNotNull(lastestArticle.getLayoutUuid()) &&
			!article.isInTrash()) {

			String groupFriendlyURL = PortalUtil.getGroupFriendlyURL(
				themeDisplay.getScopeGroup(), false, themeDisplay);

			link =
				groupFriendlyURL.concat(
					JournalArticleConstants.CANONICAL_URL_SEPARATOR).concat(
						lastestArticle.getUrlTitle());
		}

		return link;
	}

	protected String getTitle(
		JournalArticle article, Locale locale) throws Exception {

		if (article.isInTrash()) {
			return TrashUtil.getOriginalTitle(article.getTitle(), locale);
		}
		else {
			return article.getTitle(locale);
		}
	}

	protected String getTitlePattern(String groupName, int activityType) {
		if (activityType == JournalActivityKeys.ADD_ARTICLE) {
			if (Validator.isNull(groupName)) {
				return "activity-journal-add-article";
			}
			else {
				return "activity-journal-add-article-in";
			}
		}
		else if (activityType == JournalActivityKeys.UPDATE_ARTICLE) {
			if (Validator.isNull(groupName)) {
				return "activity-journal-update-article";
			}
			else {
				return "activity-journal-update-article-in";
			}
		}
		else if (activityType == SocialActivityConstants.TYPE_MOVE_TO_TRASH) {
			if (Validator.isNull(groupName)) {
				return "activity-journal-move-to-trash";
			}
			else {
				return "activity-journal-move-to-trash-in";
			}
		}
		else if (activityType ==
					SocialActivityConstants.TYPE_RESTORE_FROM_TRASH) {

			if (Validator.isNull(groupName)) {
				return "activity-journal-restore-from-trash";
			}
			else {
				return "activity-journal-restore-from-trash-in";
			}
		}

		return null;
	}

	private static final String[] _CLASS_NAMES = new String[] {
		JournalArticle.class.getName()
	};

}