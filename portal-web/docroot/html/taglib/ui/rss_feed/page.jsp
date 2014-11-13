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

<%@ include file="/html/taglib/ui/rss_feed/init.jsp" %>

<%
int entriesPerFeed = GetterUtil.getInteger(request.getAttribute("liferay-ui:rss-feed:entriesPerFeed"));
int expandedEntriesPerFeed = GetterUtil.getInteger(request.getAttribute("liferay-ui:rss-feed:expandedEntriesPerFeed"));
boolean last = GetterUtil.getBoolean(request.getAttribute("liferay-ui:rss-feed:last"));
com.liferay.portlet.rss.RSSFeed rssFeed = (com.liferay.portlet.rss.RSSFeed)request.getAttribute("liferay-ui:rss-feed:rssFeed");
boolean showFeedDescription = GetterUtil.getBoolean(request.getAttribute("liferay-ui:rss-feed:showFeedDescription"));
boolean showFeedImage = GetterUtil.getBoolean(request.getAttribute("liferay-ui:rss-feed:showFeedImage"));
boolean showFeedItemAuthor = GetterUtil.getBoolean(request.getAttribute("liferay-ui:rss-feed:showFeedItemAuthor"));
boolean showFeedPublishedDate = GetterUtil.getBoolean(request.getAttribute("liferay-ui:rss-feed:showFeedPublishedDate"));
boolean showFeedTitle = GetterUtil.getBoolean(request.getAttribute("liferay-ui:rss-feed:showFeedTitle"));
%>

<div class="feed <%= last ? "last" : StringPool.BLANK %>">

	<%
	SyndFeed feed = rssFeed.getFeed();

	SyndImage feedImage = feed.getImage();
	%>

	<c:if test="<%= (feedImage != null) && showFeedImage %>">
		<div class="feed-image">
			<img alt="<%= HtmlUtil.escapeAttribute(feedImage.getDescription()) %>" src="<%= HtmlUtil.escapeHREF(rssFeed.getFeedImageURL()) %>" />
		</div>
	</c:if>

	<c:if test="<%= showFeedTitle %>">
		<div class="feed-title">
			<aui:a href="<%= RSSUtil.escapeJavaScriptLink(rssFeed.getFeedLink()) %>" target="_blank"><%= HtmlUtil.escape(rssFeed.getTitle()) %></aui:a>
		</div>
	</c:if>

	<c:if test="<%= (feed.getPublishedDate() != null) && showFeedPublishedDate %>">
		<div class="feed-date feed-published-date">
			<liferay-ui:icon
				iconCssClass="icon-calendar"
				label="<%= true %>"
				message="<%= dateFormatDateTime.format(feed.getPublishedDate()) %>"
			/>
		</div>
	</c:if>

	<c:if test="<%= Validator.isNotNull(feed.getDescription()) && showFeedDescription %>">
		<div class="feed-description">
			<%= HtmlUtil.escape(feed.getDescription()) %>
		</div>
	</c:if>

	<%
	List entries = feed.getEntries();
	%>

	<c:if test="<%= !entries.isEmpty() %>">
		<liferay-ui:panel-container cssClass="feed-entries" extended="<%= false %>" id="rssFeedsPanelContainer" persistState="<%= false %>">

			<%
			for (int j = 0; (j < entries.size()) && (j < entriesPerFeed); j++) {
				SyndEntry entry = (SyndEntry)entries.get(j);

				RSSFeedEntryDisplayContext rssFeedEntryDisplayContext = new RSSFeedEntryDisplayContext(entry, request, rssFeed);
			%>

				<liferay-ui:panel cssClass="feed-entry" extended="<%= (themeDisplay.isStateMaximized() || (j < expandedEntriesPerFeed)) %>" title="<%= HtmlUtil.escape(entry.getTitle()) %>">
					<div class="feed-entry-content">
						<c:if test="<%= showFeedItemAuthor && Validator.isNotNull(entry.getAuthor()) %>">
							<div class="feed-entry-author">
								<%= HtmlUtil.escape(entry.getAuthor()) %>
							</div>
						</c:if>

						<c:if test="<%= entry.getPublishedDate() != null %>">
							<div class="feed-date">
								<liferay-ui:icon
									iconCssClass="icon-calendar"
									label="<%= true %>"
									message="<%= dateFormatDateTime.format(entry.getPublishedDate()) %>"
								/>
							</div>
						</c:if>

						<c:if test="<%= Validator.isNotNull(rssFeedEntryDisplayContext.getEnclosureLink()) %>">
							<div class="feed-entry-enclosure">
								<aui:a href="<%= RSSUtil.escapeJavaScriptLink(rssFeedEntryDisplayContext.getEnclosureLink()) %>" target="_blank"><%= HtmlUtil.escape(rssFeedEntryDisplayContext.getEnclosureLinkTitle()) %></aui:a>
							</div>
						</c:if>

						<%= rssFeedEntryDisplayContext.getSanitizedContent() %>

						<br />

						<aui:a href="<%= RSSUtil.escapeJavaScriptLink(rssFeedEntryDisplayContext.getEntryLink()) %>"><liferay-ui:message key="read-more" /></aui:a>
					</div>
				</liferay-ui:panel>

			<%
			}
			%>

		</liferay-ui:panel-container>
	</c:if>
</div>