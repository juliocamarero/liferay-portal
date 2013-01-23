<%--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

<%@ include file="/html/portlet/layouts_admin/init.jsp" %>

<%@ include file="/html/portlet/layouts_admin/init_attributes.jspf" %>

<%
boolean viewTree = ParamUtil.getBoolean(request, "viewTree");
boolean viewLayout = ParamUtil.getBoolean(request, "viewLayout");

boolean hasLayoutUpdatePermission = (selPlid > 0) && LayoutPermissionUtil.contains(permissionChecker, selPlid, ActionKeys.UPDATE);
boolean hasGroupUpdatePermission = (liveGroupId > 0) && GroupPermissionUtil.contains(permissionChecker, liveGroupId, ActionKeys.UPDATE);

SitesUtil.addPortletBreadcrumbEntries(group, pagesName, redirectURL, request, renderResponse);
%>

<c:if test="<%= viewTree %>">
	<div id="<portlet:namespace />viewTree">
		<liferay-util:include page="/html/portlet/layouts_admin/tree_js.jsp">
			<liferay-util:param name="treeId" value="layoutsTree" />
		</liferay-util:include>
	</div>
</c:if>

<c:if test="<%= viewLayout %>">
	<div id="<portlet:namespace />viewLayout">
		<c:choose>
			<c:when test="<%= selPlid > 0 %>">
				<c:if test="<%= hasLayoutUpdatePermission %>">
					<liferay-util:include page="/html/portlet/layouts_admin/edit_layout.jsp" />
				</c:if>
			</c:when>
			<c:otherwise>
				<c:if test="<%= hasGroupUpdatePermission %>">
					<liferay-util:include page="/html/portlet/layouts_admin/edit_layout_set.jsp" />
				</c:if>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>