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
import com.liferay.portal.kernel.trash.BaseTrashRenderer;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.permission.JournalArticlePermission;

import java.util.Locale;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Levente Hudák
 */
public class JournalArticleTrashRenderer extends BaseTrashRenderer {

	public static final String TYPE = "article";

	public JournalArticleTrashRenderer(JournalArticle article)
		throws PortalException, SystemException {

		_article = article;
	}

	@Override
	public String getIconPath(ThemeDisplay themeDisplay) {
		return themeDisplay.getPathThemeImages() +
			"/common/journal_articles.png";
	}

	public String getPortletId() {
		return PortletKeys.JOURNAL;
	}

	public String getSummary(Locale locale) {
		return HtmlUtil.stripHtml(_article.getDescription());
	}

	public String getTitle(Locale locale) {
		return HtmlUtil.stripHtml(_article.getTitle());
	}

	public String getType() {
		return TYPE;
	}

	public boolean hasDeletePermission(PermissionChecker permissionChecker) {
		return JournalArticlePermission.contains(
			permissionChecker, _article, ActionKeys.DELETE);
	}

	public boolean hasViewPermission(PermissionChecker permissionChecker) {
		return JournalArticlePermission.contains(
			permissionChecker, _article, ActionKeys.VIEW);
	}

	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse,
			String template)
		throws Exception {

		if (template.equals(AssetRenderer.TEMPLATE_ABSTRACT) ||
			template.equals(AssetRenderer.TEMPLATE_FULL_CONTENT)) {

			renderRequest.setAttribute(WebKeys.JOURNAL_ARTICLE, _article);

			return "/html/portlet/journal/asset/" + template + ".jsp";
		}

		return null;
	}

	private JournalArticle _article;

}