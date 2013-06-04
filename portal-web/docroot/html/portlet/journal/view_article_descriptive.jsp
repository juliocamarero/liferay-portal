<%--
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
--%>

<%@ include file="/html/portlet/journal/init.jsp" %>

<%
JournalArticle article = (JournalArticle)request.getAttribute("view_entries.jsp-article");

PortletURL tempRowURL = (PortletURL)request.getAttribute("view_entries.jsp-tempRowURL");

JournalArticle latestArticleVersion = null;

Date createDate = article.getCreateDate();

if (article.getVersion() > 1.0) {
	JournalArticle firstArticleVersion = JournalArticleLocalServiceUtil.getArticle(article.getGroupId(), article.getArticleId(), 1.0);

	createDate = firstArticleVersion.getCreateDate();

	latestArticleVersion = JournalArticleLocalServiceUtil.getLatestArticle(article.getGroupId(), article.getArticleId(), WorkflowConstants.STATUS_APPROVED);
}
%>

<liferay-ui:app-view-entry
	actionJsp="/html/portlet/journal/article_action.jsp"
	assetCategoryClassName="<%= JournalArticle.class.getName() %>"
	assetCategoryClassPK="<%= article.getClassPK() %>"
	assetTagClassName="<%= JournalArticle.class.getName() %>"
	assetTagClassPK="<%= article.getClassPK() %>"
	author="<%= article.getUserName() %>"
	createDate="<%= createDate %>"
	description="<%= article.getDescription(locale) %>"
	displayDate="<%= article.getDisplayDate() %>"
	displayStyle="descriptive"
	expirationDate="<%= article.getExpirationDate() %>"
	latestApprovedVersion="<%= Validator.isNotNull(latestArticleVersion) ? String.valueOf(latestArticleVersion.getVersion()) : null %>"
	latestApprovedVersionAuthor="<%= Validator.isNotNull(latestArticleVersion) ? String.valueOf(latestArticleVersion.getUserName()) : null %>"
	modelClassName="<%= article.getModelClassName() %>"
	modifiedDate="<%= article.getModifiedDate() %>"
	reviewDate="<%= article.getReviewDate() %>"
	rowCheckerId="<%= String.valueOf(article.getArticleId()) %>"
	rowCheckerName="<%= JournalArticle.class.getSimpleName() %>"
	showCheckbox="<%= JournalArticlePermission.contains(permissionChecker, article, ActionKeys.DELETE) || JournalArticlePermission.contains(permissionChecker, article, ActionKeys.UPDATE) %>"
	status="<%= article.getStatus() %>"
	thumbnailDivStyle="height: 136px; width: 136px;"
	thumbnailSrc='<%= themeDisplay.getPathThemeImages() + "/file_system/large/article.png" %>'
	thumbnailStyle="max-height: 128px; max-width: 128px;"
	title="<%= article.getTitle(locale) %>"
	url="<%= tempRowURL.toString() %>"
	version="<%= String.valueOf(article.getVersion()) %>"
/>