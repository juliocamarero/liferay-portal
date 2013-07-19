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

<html>

<head>
	<liferay-util:include page="/html/common/themes/top_head.jsp" />
</head>

<body>

<%
JournalArticle article = (JournalArticle)request.getAttribute(WebKeys.JOURNAL_ARTICLE);
%>

<%= JournalArticleLocalServiceUtil.getArticleContent(article, article.getTemplateId(), null, themeDisplay.getLanguageId(), themeDisplay) %>

<liferay-util:include page="/html/common/themes/bottom.jsp" />

</body>

</html>