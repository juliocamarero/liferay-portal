<%--
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
--%>

<%@ include file="/html/portlet/rss/init.jsp" %>

<%
long portletDisplayDDMTemplateId = PortletDisplayTemplateUtil.getPortletDisplayTemplateDDMTemplateId(rssDisplayContext.getDisplayStyleGroupId(), rssDisplayContext.getDisplayStyle());

List<RSSFeed> rssFeeds = rssDisplayContext.getRSSFeeds();
%>

<c:choose>
	<c:when test="<%= portletDisplayDDMTemplateId > 0 %>">
		<%= PortletDisplayTemplateUtil.renderDDMTemplate(request, response, portletDisplayDDMTemplateId, rssFeeds) %>
	</c:when>
	<c:otherwise>

		<%
		for (int i = 0; i < rssFeeds.size(); i++) {
			RSSFeed rssFeed = rssFeeds.get(i);

			boolean last = false;

			if (i == (rssFeeds.size() - 1)) {
				last = true;
			}

			SyndFeed syndFeed = rssFeed.getSyndFeed();
		%>

			<%@ include file="/html/portlet/rss/feed.jspf" %>

		<%
		}
		%>

	</c:otherwise>
</c:choose>