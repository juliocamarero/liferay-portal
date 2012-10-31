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

package com.liferay.portlet.bookmarks.trash;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.trash.BaseTrashRenderer;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;

import java.util.Locale;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Levente Hudák
 */
public class BookmarksEntryTrashRenderer extends BaseTrashRenderer {

	public static final String TYPE = "bookmarks_entry";

	public BookmarksEntryTrashRenderer(BookmarksEntry entry)
		throws PortalException, SystemException {

		_entry = entry;
	}

	public String getIconPath(ThemeDisplay themeDisplay) {
		return themeDisplay.getPathThemeImages() + "/common/bookmarks.png";
	}

	public String getPortletId() {
		return PortletKeys.BOOKMARKS;
	}

	public String getSummary(Locale locale) {
		return HtmlUtil.stripHtml(_entry.getDescription());
	}

	public String getTitle(Locale locale) {
		return _entry.getName();
	}

	public String getType() {
		return TYPE;
	}

	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse,
		String template)
		throws Exception {

		if (template.equals(AssetRenderer.TEMPLATE_FULL_CONTENT)) {

			renderRequest.setAttribute(WebKeys.BOOKMARKS_ENTRY, _entry);

			return "/html/portlet/bookmarks/asset/" + template + ".jsp";
		}

		return null;
	}

	private BookmarksEntry _entry;

}