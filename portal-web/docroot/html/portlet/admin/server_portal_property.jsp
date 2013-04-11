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

<%@ include file="/html/portlet/admin/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

Map.Entry entry = (Map.Entry)row.getObject();

String property = (String)entry.getKey();
String value = (String)entry.getValue();

PortletPreferences companyPrefs = PrefsPropsUtil.getPreferences(company.getCompanyId());
%>

<c:choose>
	<c:when test="<%= companyPrefs.getMap().containsKey(property) %>">
		 <%= StringUtil.shorten(companyPrefs.getValue(property, StringPool.BLANK), 80) %>

		 <liferay-ui:icon-help message='<%= LanguageUtil.format(pageContext, "this-property-was-stored-in-portal-preferences-using-control-panel-and-its-original-value-x-from-portal-ext-properties-was-overridden-by-this-value", HtmlUtil.escape(value)) %>' />
	</c:when>
	<c:otherwise>
		 <%= StringUtil.shorten(value, 80) %>
	</c:otherwise>
</c:choose>