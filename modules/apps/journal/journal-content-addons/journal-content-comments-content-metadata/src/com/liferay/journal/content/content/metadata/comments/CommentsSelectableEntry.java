/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.journal.content.content.metadata.comments;

import com.liferay.journal.content.web.util.ContentMetadataEntry;
import com.liferay.portal.model.BaseSelectableEntry;
import com.liferay.portal.util.PropsValues;

import javax.portlet.PortletPreferences;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
@Component(
	immediate = true, service = ContentMetadataEntry.class
)
public class CommentsSelectableEntry
	extends BaseSelectableEntry implements ContentMetadataEntry {

	@Override
	public String getIcon() {
		return "comments";
	}

	@Override
	public String getJSPPath() {
		return _JSP_PATH;
	}

	@Override
	public String getKey() {
		return "enableComments";
	}

	@Override
	public String getLabel() {
		return "comments";
	}

	@Override
	public Double getWeight() {
		return 3.0;
	}

	public boolean isCommentsRatingsSelected(
		HttpServletRequest request, PortletPreferences portletPreferences) {

		if (_commentRatingsSelectableEntry != null) {
			return _commentRatingsSelectableEntry.isSelected(
				request, portletPreferences);
		}

		return false;
	}

	@Override
	public boolean isEnabled() {
		if (!PropsValues.JOURNAL_ARTICLE_COMMENTS_ENABLED) {
			return false;
		}

		return super.isEnabled();
	}

	@Reference
	public void setCommentRatingsSelectableEntry(
		CommentRatingsSelectableEntry commentRatingsSelectableEntry) {

		_commentRatingsSelectableEntry = commentRatingsSelectableEntry;
	}

	@Override
	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.journal.content.content.metadata.comments)"
	)
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

	private static final String _JSP_PATH = "/META-INF/resources/comments.jsp";

	private CommentRatingsSelectableEntry _commentRatingsSelectableEntry;

}