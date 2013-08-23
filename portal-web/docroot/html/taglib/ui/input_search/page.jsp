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

<%@ include file="/html/taglib/init.jsp" %>

<%
DisplayTerms displayTerms =  new DisplayTerms(portletRequest);
%>

<div class="input-append">
	<input class="search-query span9" id="<portlet:namespace/><%= DisplayTerms.KEYWORDS %>" name="<portlet:namespace/><%= DisplayTerms.KEYWORDS %>" placeholder="<liferay-ui:message key="<%= DisplayTerms.KEYWORDS %>" />" type="text" value="<%= displayTerms.getKeywords() %>" />

	<aui:button primary="<%= false %>" type="submit" value="<%= UnicodeLanguageUtil.get(locale, "search") %>" />
</div>