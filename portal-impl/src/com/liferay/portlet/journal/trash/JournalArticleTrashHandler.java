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

package com.liferay.portlet.journal.trash;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.trash.BaseTrashHandler;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleServiceUtil;
import com.liferay.portlet.journal.service.permission.JournalArticlePermission;
import com.liferay.portlet.journal.util.JournalUtil;

import javax.portlet.PortletRequest;

/**
 * Represents the trash handler for the journal article entity.
 *
 * @author Levente Hudák
 */
public class JournalArticleTrashHandler extends BaseTrashHandler {

	public static final String CLASS_NAME = JournalArticle.class.getName();

	public void deleteTrashEntries(long[] classPKs, boolean checkPermission)
		throws PortalException, SystemException {

		for (long classPK : classPKs) {
			JournalArticle article = getJournalArticle(classPK);

			if (checkPermission) {
				JournalArticleServiceUtil.deleteArticle(
					article.getGroupId(), article.getArticleId(),
					article.getUrlTitle(), null);
			}
			else {
				JournalArticleLocalServiceUtil.deleteArticle(article);
			}
		}
	}

	public String getClassName() {
		return CLASS_NAME;
	}

	@Override
	public String getDeleteMessage() {
		return "found-in-deleted-folder-x";
	}

	@Override
	public String getRestoreLink(PortletRequest portletRequest, long classPK)
		throws PortalException, SystemException {

		JournalArticle article = getJournalArticle(classPK);

		return JournalUtil.getJournalControlPanelLink(
			portletRequest, article.getFolderId());
	}

	@Override
	public String getRestoreMessage(PortletRequest portletRequest, long classPK)
		throws PortalException, SystemException {

		JournalArticle article = getJournalArticle(classPK);

		return JournalUtil.getAbsolutePath(
			portletRequest, article.getFolderId());
	}

	public boolean isInTrash(long classPK)
		throws PortalException, SystemException {

		JournalArticle article = getJournalArticle(classPK);

		if (article.isInTrash()) {
			return true;
		}

		return false;
	}

	public void restoreTrashEntries(long[] classPKs)
		throws PortalException, SystemException {

		for (long classPK : classPKs) {
			JournalArticle article = getJournalArticle(classPK);

			JournalArticleServiceUtil.restoreArticleFromTrash(
				article.getGroupId(), article.getArticleId());
		}
	}

	protected JournalArticle getJournalArticle(long classPK)
		throws PortalException, SystemException {

		JournalArticle article = JournalArticleServiceUtil.getLatestArticle(
			classPK);

		return article;
	}

	@Override
	protected boolean hasPermission(
			PermissionChecker permissionChecker, long classPK, String actionId)
		throws PortalException, SystemException {

		return JournalArticlePermission.contains(
			permissionChecker, classPK, actionId);
	}

}