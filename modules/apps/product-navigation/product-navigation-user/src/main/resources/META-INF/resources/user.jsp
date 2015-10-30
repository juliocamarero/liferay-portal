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

<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="com.liferay.application.list.PanelCategory" %>
<%@ page import="java.util.List" %>
<%@ page import="com.liferay.application.list.constants.ApplicationListWebKeys" %>

<liferay-theme:defineObjects />

<%
List<PanelCategory> panelCategories = (List<PanelCategory>)request.getAttribute(ApplicationListWebKeys.PANEL_CATEGORIES);
%>

<c:choose>
	<c:when test="<%= panelCategories.size() > 1 %>">
		<div class="product-menu-tab-icon user-tab">
			<div class="icon-monospaced">
				<div class="user-icon">
					<img alt="<%= HtmlUtil.escapeAttribute(user.getFullName()) %>" src="<%= HtmlUtil.escape(user.getPortraitURL(themeDisplay)) %>" />
				</div>
			</div>
		</div>

		<div class="product-menu-tab-text">
			<%= user.getFirstName() %>
		</div>
	</c:when>
	<c:otherwise>
		<div class="nameplate">
			<div class="nameplate-field">
				<div class="user-icon user-icon-lg">
					<img alt="<%= HtmlUtil.escapeAttribute(user.getFullName()) %>" src="<%= HtmlUtil.escape(user.getPortraitURL(themeDisplay)) %>" />
				</div>
			</div>

			<div class="nameplate-content">
				<div class="user-heading">
					<%= HtmlUtil.escape(user.getFullName()) %>
				</div>
			</div>
		</div>
	</c:otherwise>
</c:choose>
