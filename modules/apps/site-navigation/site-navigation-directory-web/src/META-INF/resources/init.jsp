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

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.portal.kernel.util.Constants" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.site.navigation.directory.web.configuration.SitesDirectoryPortletInstanceConfiguration" %><%@
page import="com.liferay.taglib.ui.SitesDirectoryTag" %>

<liferay-theme:defineObjects />
<portlet:defineObjects />

<%
String portletResource = ParamUtil.getString(request, "portletResource");

SitesDirectoryPortletInstanceConfiguration sitesDirectoryPortletInstanceConfiguration = portletDisplay.getPortletInstanceConfiguration(SitesDirectoryPortletInstanceConfiguration.class);

String displayStyle = ParamUtil.getString(renderRequest, "displayStyle", sitesDirectoryPortletInstanceConfiguration.displayStyle());
String sites = ParamUtil.getString(renderRequest, "sites", sitesDirectoryPortletInstanceConfiguration.sites());
%>

<%@ include file="/init-ext.jsp" %>