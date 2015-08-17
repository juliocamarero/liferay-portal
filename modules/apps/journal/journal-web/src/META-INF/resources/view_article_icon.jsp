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

<%@ include file="/init.jsp" %>

<%
JournalArticle article = (JournalArticle)request.getAttribute("view_entries.jsp-article");

PortletURL tempRowURL = (PortletURL)request.getAttribute("view_entries.jsp-tempRowURL");

String articleImageURL = article.getArticleImageURL(themeDisplay);
%>

<liferay-ui:app-view-entry
	actionJsp="/article_action.jsp"
	actionJspServletContext="<%= application %>"
	author="<%= article.getUserName() %>"
	authorId="<%= article.getUserId() %>"
	description="<%= HtmlUtil.escape(article.getDescription(locale)) %>"
	displayStyle="icon"
	groupId="<%= article.getGroupId() %>"
	modifiedDate="<%= article.getModifiedDate() %>"
	rowCheckerId="<%= HtmlUtil.escape(article.getArticleId()) %>"
	rowCheckerName="<%= JournalArticle.class.getSimpleName() %>"
	showCheckbox="<%= JournalArticlePermission.contains(permissionChecker, article, ActionKeys.DELETE) || JournalArticlePermission.contains(permissionChecker, article, ActionKeys.EXPIRE) || JournalArticlePermission.contains(permissionChecker, article, ActionKeys.UPDATE) %>"
	status="<%= article.getStatus() %>"
	thumbnailDivStyle="height: 146px; width: 146px;"
	thumbnailSrc='<%= Validator.isNotNull(articleImageURL) ? articleImageURL : themeDisplay.getPathThemeImages() + "/file_system/large/article.png" %>'
	title="<%= HtmlUtil.escape(article.getTitle(locale)) %>"
	url="<%= tempRowURL.toString() %>"
	view="lexicon"
/>