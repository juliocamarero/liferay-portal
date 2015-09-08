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

<c:if test="<%= AssetPermission.contains(permissionChecker, themeDisplay.getSiteGroupId(), ActionKeys.PERMISSIONS) && GroupPermissionUtil.contains(permissionChecker, themeDisplay.getSiteGroupId(), ActionKeys.PERMISSIONS) %>">
</c:if>

<liferay-security:permissionsURL
	modelResource="com.liferay.portlet.asset"
	modelResourceDescription="<%= themeDisplay.getScopeGroupName() %>"
	resourcePrimKey="<%= String.valueOf(themeDisplay.getSiteGroupId()) %>"
	var="permissionsURL"
	windowState="<%= LiferayWindowState.POP_UP.toString() %>"
/>

<liferay-ui:icon
	iconCssClass="icon-lock"
	message="permissions"
	url="<%= permissionsURL %>"
	useDialog="<%= true %>"
/>