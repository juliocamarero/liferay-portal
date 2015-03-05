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

<%@ include file="/html/taglib/init.jsp" %>

<%
PortletURL checkEntryURL = (PortletURL)request.getAttribute("liferay-ui:restore-entry:checkEntryURL");
PortletURL duplicateEntryURL = (PortletURL)request.getAttribute("liferay-ui:restore-entry:duplicateEntryURL");
String overrideMessage = (String)request.getAttribute("liferay-ui:restore-entry:overrideMessage");
String renameMessage = (String)request.getAttribute("liferay-ui:restore-entry:renameMessage");
%>

<aui:script use="liferay-restore-entry">
	<c:if test="<%= Validator.isNull(checkEntryURL) %>">
		<liferay-portlet:resourceURL id="checkEntry" plid="<%= PortalUtil.getControlPanelPlid(company.getCompanyId()) %>" portletName="<%= PortletKeys.TRASH %>" varImpl="portletURL" />

		<%
		checkEntryURL = portletURL;
		%>

	</c:if>

	<c:if test="<%= Validator.isNull(duplicateEntryURL) %>">
		<liferay-portlet:renderURL plid="<%= PortalUtil.getControlPanelPlid(company.getCompanyId()) %>" portletName="<%= PortletKeys.TRASH %>" varImpl="portletURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
			<portlet:param name="mvcPath" value="/html/portlet/trash/restore_entry.jsp" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
		</liferay-portlet:renderURL>

		<%
		duplicateEntryURL = portletURL;
		%>

	</c:if>

	new Liferay.RestoreEntry(
		{
			checkEntryURL: '<%= checkEntryURL.toString() %>',
			duplicateEntryURL: '<%= duplicateEntryURL.toString() %>',
			namespace: '<portlet:namespace />',
			overrideMessage: '<%= overrideMessage %>',
			renameMessage: '<%= renameMessage %>'
		}
	);
</aui:script>