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

<%@ include file="/taglib/ui/panel_app/init.jsp" %>

<%
PanelApp panelApp = (PanelApp)request.getAttribute("application-list-ui:panel-app:panelApp");

String ppid = themeDisplay.getPpid();

PortletURL portletURL = PortletURLFactoryUtil.create(request, panelApp.getPortletId(), themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

portletURL.setWindowState(WindowState.MAXIMIZED);

Portlet portlet = PortletLocalServiceUtil.getPortletById(themeDisplay.getCompanyId(), panelApp.getPortletId());
%>

<li class="<%= panelApp.getPortletId().equals(ppid) ? "active" : StringPool.BLANK %>">
	<c:choose>
		<c:when test="<%= portletURL != null %>">
			<a href="<%= portletURL.toString() %>">
				<span><%= PortalUtil.getPortletTitle(portlet, pageContext.getServletContext(), themeDisplay.getLocale()) %></span>
			</a>
		</c:when>
		<c:otherwise>
			<span><%= PortalUtil.getPortletTitle(portlet, pageContext.getServletContext(), themeDisplay.getLocale()) %></span>
		</c:otherwise>
	</c:choose>
</li>