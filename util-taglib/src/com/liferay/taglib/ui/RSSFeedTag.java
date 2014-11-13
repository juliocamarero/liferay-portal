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

package com.liferay.taglib.ui;

import com.liferay.portlet.rss.context.RSSFeed;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class RSSFeedTag extends IncludeTag {

	public void setEntriesPerFeed(int entriesPerFeed) {
		_entriesPerFeed = entriesPerFeed;
	}

	public void setExpandedEntriesPerFeed(int expandedEntriesPerFeed) {
		_expandedEntriesPerFeed = expandedEntriesPerFeed;
	}

	public void setLast(boolean last) {
		_last = last;
	}

	public void setRssFeed(RSSFeed rssFeed) {
		_rssFeed = rssFeed;
	}

	public void setShowFeedDescription(boolean showFeedDescription) {
		_showFeedDescription = showFeedDescription;
	}

	public void setShowFeedImage(boolean showFeedImage) {
		_showFeedImage = showFeedImage;
	}

	public void setShowFeedItemAuthor(boolean showFeedItemAuthor) {
		_showFeedItemAuthor = showFeedItemAuthor;
	}

	public void setShowFeedPublishedDate(boolean showFeedPublishedDate) {
		_showFeedPublishedDate = showFeedPublishedDate;
	}

	public void setShowFeedTitle(boolean showFeedTitle) {
		_showFeedTitle = showFeedTitle;
	}

	@Override
	protected void cleanUp() {
		_entriesPerFeed = 8;
		_expandedEntriesPerFeed = 8;
		_last = false;
		_rssFeed = null;
		_showFeedDescription = false;
		_showFeedImage = false;
		_showFeedItemAuthor = false;
		_showFeedPublishedDate = false;
		_showFeedTitle = false;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected boolean isCleanUpSetAttributes() {
		return _CLEAN_UP_SET_ATTRIBUTES;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-ui:rss-feed:entriesPerFeed",
			String.valueOf(_entriesPerFeed));
		request.setAttribute(
			"liferay-ui:rss-feed:expandedEntriesPerFeed",
			String.valueOf(_expandedEntriesPerFeed));
		request.setAttribute("liferay-ui:rss-feed:last",String.valueOf(_last));
		request.setAttribute("liferay-ui:rss-feed:rssFeed", _rssFeed);
		request.setAttribute(
			"liferay-ui:rss-feed:showFeedDescription",
			String.valueOf(_showFeedDescription));
		request.setAttribute(
			"liferay-ui:rss-feed:showFeedImage",
			String.valueOf(_showFeedImage));
		request.setAttribute(
			"liferay-ui:rss-feed:showFeedItemAuthor",
			String.valueOf(_showFeedItemAuthor));
		request.setAttribute(
			"liferay-ui:rss-feed:showFeedPublishedDate",
			String.valueOf(_showFeedPublishedDate));
		request.setAttribute(
			"liferay-ui:rss-feed:showFeedTitle",
			String.valueOf(_showFeedTitle));
	}

	private static final boolean _CLEAN_UP_SET_ATTRIBUTES = true;

	private static final String _PAGE = "/html/taglib/ui/rss_feed/page.jsp";

	private int _entriesPerFeed;
	private int _expandedEntriesPerFeed;
	private boolean _last;
	private RSSFeed _rssFeed;
	private boolean _showFeedDescription;
	private boolean _showFeedImage;
	private boolean _showFeedItemAuthor;
	private boolean _showFeedPublishedDate;
	private boolean _showFeedTitle;

}