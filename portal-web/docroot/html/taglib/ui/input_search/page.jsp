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
String buttonLabel = GetterUtil.getString((String)request.getAttribute("liferay-ui:input-search:buttonLabel"), LanguageUtil.get(pageContext, "search"));
String cssClass = GetterUtil.getString((String)request.getAttribute("liferay-ui:input-search:cssClass"), "input-append");
DisplayTerms displayTerms = (DisplayTerms)GetterUtil.getObject(request.getAttribute("liferay-ui:input-search:displayTerms"), new DisplayTerms(portletRequest));
String id = (String)request.getAttribute("liferay-ui:input-search:id");
String name = GetterUtil.getString((String)request.getAttribute("liferay-ui:input-search:name"), DisplayTerms.KEYWORDS);
boolean showButton = GetterUtil.getBoolean(request.getAttribute("liferay-ui:input-search:showButton"), true);
%>

<div class="<%= cssClass %>" <%= Validator.isNotNull(id) ? "id=\"" + namespace + id + "simple\"" : StringPool.BLANK %> >
	<input class="search-query span9" id="<%= Validator.isNotNull(id) ? id : namespace + DisplayTerms.KEYWORDS %>" name="<%= namespace + name %>" placeholder="<liferay-ui:message key="keywords" />" type="text" value="<%= HtmlUtil.escapeAttribute(displayTerms.getKeywords()) %>" />

	<c:if test="<%= showButton %>">
		<button class="btn" type="submit">
			<%= buttonLabel %>
		</button>
	</c:if>
</div>