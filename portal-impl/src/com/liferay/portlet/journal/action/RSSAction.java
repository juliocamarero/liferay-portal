/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.journal.action;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.journal.model.JournalFeed;
import com.liferay.portlet.journal.service.JournalFeedLocalServiceUtil;
import com.liferay.portlet.rss.RSSRenderer;
import com.liferay.portlet.rss.action.DefaultRSSAction;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

/**
 * @author Raymond Augé
 */
public class RSSAction extends DefaultRSSAction {

	@Override
	protected RSSRenderer getRSSRenderer(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		JournalFeed feed = (JournalFeed)resourceRequest.getAttribute(
			"rssJournalFeed");

		if (feed != null) {
			return new JournalRSSRenderer(
				feed, resourceRequest, resourceResponse);
		}

		long id = ParamUtil.getLong(resourceRequest, "id");

		long groupId = ParamUtil.getLong(resourceRequest, "groupId");
		String feedId = ParamUtil.getString(resourceRequest, "feedId");

		if (id > 0) {
			feed = JournalFeedLocalServiceUtil.getFeed(id);
		}
		else {
			feed = JournalFeedLocalServiceUtil.getFeed(groupId, feedId);
		}

		resourceRequest.setAttribute("rssJournalFeed", feed);

		return new JournalRSSRenderer(feed, resourceRequest, resourceResponse);
	}

}