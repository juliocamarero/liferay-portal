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

<%@ include file="/html/portlet/sites_admin/init.jsp" %>

<%
String tabs1 = ParamUtil.getString(request, "tabs1", "current");

int cur = ParamUtil.getInteger(request, SearchContainer.DEFAULT_CUR_PARAM);

String redirect = ParamUtil.getString(request, "redirect");

String backURL = ParamUtil.getString(request, "backURL", redirect);

Group group = (Group)request.getAttribute(WebKeys.GROUP);

if (group != null) {
	group = StagingUtil.getLiveGroup(group.getGroupId());
}

String groupDescriptiveName = group.getDescriptiveName(locale);

Role role = (Role)request.getAttribute(WebKeys.ROLE);

long roleId = BeanParamUtil.getLong(role, request, "roleId");

int roleType = ParamUtil.getInteger(request, "roleType", RoleConstants.TYPE_SITE);

Organization organization = null;

if (group.isOrganization()) {
	organization = OrganizationLocalServiceUtil.getOrganization(group.getClassPK());
}

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/sites_admin/edit_user_roles");
portletURL.setParameter("tabs1", tabs1);
portletURL.setParameter("redirect", redirect);
portletURL.setParameter("groupId", String.valueOf(group.getGroupId()));

// Breadcrumbs

if (organization != null) {
	UsersAdminUtil.addPortletBreadcrumbEntries(organization, request, renderResponse);
}
else if (group != null) {
	PortalUtil.addPortletBreadcrumbEntry(request, groupDescriptiveName, null);
}

PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(request, "assign-user-roles"), portletURL.toString());

if (role != null) {
	portletURL.setParameter("roleId", String.valueOf(roleId));

	PortalUtil.addPortletBreadcrumbEntry(request, role.getTitle(locale), currentURL);
}

request.setAttribute("edit_user_roles.jsp-tabs1", tabs1);

request.setAttribute("edit_user_roles.jsp-cur", cur);

request.setAttribute("edit_user_roles.jsp-redirect", redirect);

request.setAttribute("edit_user_roles.jsp-group", group);
request.setAttribute("edit_user_roles.jsp-groupDescriptiveName", groupDescriptiveName);
request.setAttribute("edit_user_roles.jsp-role", role);
request.setAttribute("edit_user_roles.jsp-roleId", roleId);
request.setAttribute("edit_user_roles.jsp-roleType", roleType);
request.setAttribute("edit_user_roles.jsp-organization", organization);

request.setAttribute("edit_user_roles.jsp-portletURL", portletURL);
%>

<liferay-ui:header
	backURL="<%= redirect %>"
	escapeXml="<%= false %>"
	localizeTitle="<%= false %>"
	title='<%= LanguageUtil.get(request, "add-site-roles-to") + ": " + LanguageUtil.get(request, "users") %>'
/>

<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" />
	<aui:input name="tabs1" type="hidden" value="<%= tabs1 %>" />
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	<aui:input name="groupId" type="hidden" value="<%= String.valueOf(group.getGroupId()) %>" />
	<aui:input name="roleId" type="hidden" value="<%= roleId %>" />

	<c:choose>
		<c:when test="<%= role == null %>">
			<liferay-util:include page="/html/portlet/sites_admin/edit_user_roles_role.jsp" />
		</c:when>
		<c:otherwise>
			<liferay-util:include page="/html/portlet/sites_admin/edit_user_roles_users.jsp" />
		</c:otherwise>
	</c:choose>
</aui:form>

<aui:script>
	function <portlet:namespace />updateUserGroupRoleUsers(redirect) {
		var Util = Liferay.Util;

		var form = AUI.$(document.<portlet:namespace />fm);

		form.fm('<%= Constants.CMD %>').val('user_group_role_users');
		form.fm('redirect').val(redirect);
		form.fm('addUserIds').val(Util.listCheckedExcept(form, '<portlet:namespace />allRowIds'));
		form.fm('removeUserIds').val(Util.listUncheckedExcept(form, '<portlet:namespace />allRowIds'));

		submitForm(form, '<portlet:actionURL><portlet:param name="struts_action" value="/sites_admin/edit_user_roles" /></portlet:actionURL>');
	}
</aui:script>