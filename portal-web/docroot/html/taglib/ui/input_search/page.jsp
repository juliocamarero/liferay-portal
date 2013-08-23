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
String buttonLabel = GetterUtil.getString((String) request.getAttribute("liferay-ui:input-search:buttonLabel"), UnicodeLanguageUtil.get(pageContext, "search"));
DisplayTerms displayTerms = (DisplayTerms)GetterUtil.getObject(request.getAttribute("liferay-ui:input-search:displayTerms"), new DisplayTerms(portletRequest));
String id = (String)request.getAttribute("liferay-ui:input-search:id");
boolean showButton = GetterUtil.getBoolean(request.getAttribute("liferay-ui:input-search:showButton"), true);
%>

	<div class="input-append" <%= Validator.isNotNull(id) ? "id=\"" + id + "simple\"" : StringPool.BLANK %> >
		<input class="search-query span9" id="<%= Validator.isNotNull(id) ? id + DisplayTerms.KEYWORDS : portletResponse.getNamespace() + DisplayTerms.KEYWORDS %>" name="<portlet:namespace /><%= DisplayTerms.KEYWORDS %>" placeholder="<liferay-ui:message key="keywords" />" type="text" value="<%= HtmlUtil.escapeAttribute(displayTerms.getKeywords()) %>" />

		<c:if test="<%= showButton %>">
			<button class="btn" type="submit">
				<%= buttonLabel %>
			</button>
		</c:if>
	</div>