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

<%@ taglib uri="http://liferay.com/tld/frontend" prefix="frontend" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

JournalArticle article = null;

if (row != null) {
	article = (JournalArticle)row.getObject();
}
else {
	article = (JournalArticle)request.getAttribute("view_entries.jsp-article");
}

String articleImageURL = article.getArticleImageURL(themeDisplay);

String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");

Map<String, Object> data = new HashMap<String, Object>();

data.put("draggable", true);
data.put("folder", false);
data.put("title", HtmlUtil.escape(article.getTitle(locale)));

User userDisplay = UserLocalServiceUtil.fetchUserById(article.getUserId());

String userImage;

if (userDisplay != null) {
	userImage = userDisplay.getPortraitURL(themeDisplay);
}
else {
	userImage = UserConstants.getPortraitURL(themeDisplay.getPathImage(), true, 0, null);
}
%>

<liferay-portlet:renderURL varImpl="rowURL">
	<portlet:param name="mvcPath" value="/edit_article.jsp" />
	<portlet:param name="redirect" value="<%= currentURL %>" />
	<portlet:param name="backURL" value="<%= currentURL %>" />
	<portlet:param name="referringPortletResource" value="<%= referringPortletResource %>" />
	<portlet:param name="groupId" value="<%= String.valueOf(article.getGroupId()) %>" />
	<portlet:param name="folderId" value="<%= String.valueOf(article.getFolderId()) %>" />
	<portlet:param name="articleId" value="<%= article.getArticleId() %>" />
	<portlet:param name="version" value="<%= String.valueOf(article.getVersion()) %>" />
</liferay-portlet:renderURL>

<liferay-util:buffer var="statusText">
	<aui:workflow-status showIcon="<%= false %>" showLabel="<%= false %>" status="<%= article.getStatus() %>" view="lexicon" />
</liferay-util:buffer>

<liferay-util:buffer var="headerText">
	<liferay-ui:message arguments="<%= new String[] {LanguageUtil.getTimeDescription(locale, System.currentTimeMillis() - article.getModifiedDate().getTime(), true), HtmlUtil.escape(article.getUserName())} %>" key="x-ago-by-x" translateArguments="<%= false %>" />
</liferay-util:buffer>

<frontend:card
	actionJsp="/article_action.jsp"
	actionJspServletContext="<%= application %>"
	checkboxCSSClass="entry-selector"
	checkboxId="<%= HtmlUtil.escape(article.getArticleId()) %>"
	checkboxName="<%= RowChecker.ROW_IDS %>"
	data="<%= data %>"
	footer="<%= statusText %>"
	header="<%= headerText %>"
	image="<%= Validator.isNotNull(articleImageURL) ? articleImageURL : themeDisplay.getPathThemeImages() + "/file_system/large/article.png" %>"
	showCheckbox="<%= JournalArticlePermission.contains(permissionChecker, article, ActionKeys.DELETE) || JournalArticlePermission.contains(permissionChecker, article, ActionKeys.EXPIRE) || JournalArticlePermission.contains(permissionChecker, article, ActionKeys.UPDATE) %>"
	smallImageCSSClass="user-icon user-icon-lg"
	smallImageUrl="<%= userImage %>"
	title="<%= HtmlUtil.escape(article.getTitle(locale)) %>"
	url="<%= rowURL != null ? rowURL.toString() : null %>"
/>