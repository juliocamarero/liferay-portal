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

<div id="<portlet:namespace />feedsContainer">

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

				SyndFeed feed = rssFeed.getFeed();
			%>

				<c:choose>
					<c:when test="<%= Validator.isNotNull(rssFeed.getUrl()) && (feed != null) %>">
						<liferay-ui:rss-feed
							entriesPerFeed="<%= rssDisplayContext.getEntriesPerFeed() %>"
							expandedEntriesPerFeed="<%= rssDisplayContext.getExpandedEntriesPerFeed() %>"
							last="<%= last %>"
							rssFeed="<%= rssFeed %>"
							showFeedDescription="<%= rssDisplayContext.isShowFeedDescription() %>"
							showFeedImage="<%= rssDisplayContext.isShowFeedImage() %>"
							showFeedItemAuthor="<%= rssDisplayContext.isShowFeedItemAuthor() %>"
							showFeedPublishedDate="<%= rssDisplayContext.isShowFeedPublishedDate() %>"
							showFeedTitle="<%= rssDisplayContext.isShowFeedTitle() %>"
						/>
					</c:when>
					<c:otherwise>
						<div class="alert alert-danger">
							<liferay-ui:message arguments="<%= HtmlUtil.escape(rssFeed.getUrl()) %>" key="cannot-be-found" translateArguments="false" />
						</div>
					</c:otherwise>
				</c:choose>

			<%
			}
			%>

		</c:otherwise>
	</c:choose>
</div>

<aui:script use="aui-base">
	var feedsContainer = A.one('#<portlet:namespace />feedsContainer');

	feedsContainer.delegate(
		'click',
		function(event) {
			var expander = event.currentTarget;
			var feedContent = expander.get('parentNode').get('parentNode').one('.feed-entry-content');

			if (feedContent) {
				if (expander.hasClass('icon-collapse-alt')) {
					expander.addClass('icon-expand-alt');
					expander.removeClass('icon-collapse-alt');
				}
				else {
					expander.addClass('icon-collapse-alt');
					expander.removeClass('icon-expand-alt');
				}

				feedContent.toggle();
			}
		},
		'.entry-expander'
	);
</aui:script>