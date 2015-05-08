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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.iframe.web.configuration.IFrameConfiguration" %><%@
page import="com.liferay.iframe.web.configuration.IFramePortletInstanceConfiguration" %><%@
page import="com.liferay.iframe.web.constants.IFrameWebKeys" %><%@
page import="com.liferay.iframe.web.display.context.IFrameDisplayContext" %><%@
page import="com.liferay.iframe.web.util.IFrameUtil" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %><%@
page import="com.liferay.portal.kernel.util.CharPool" %><%@
page import="com.liferay.portal.kernel.util.Constants" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.ListUtil" %><%@
page import="com.liferay.portal.kernel.util.StringPool" %><%@
page import="com.liferay.portal.kernel.util.StringUtil" %><%@
page import="com.liferay.portal.kernel.util.Validator" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %>

<%@ page import="java.util.ArrayList" %><%@
page import="java.util.Enumeration" %><%@
page import="java.util.List" %>

<%@ page import="javax.portlet.WindowState" %>

<liferay-theme:defineObjects />
<portlet:defineObjects />

<%
WindowState windowState = liferayPortletRequest.getWindowState();

IFrameConfiguration iFrameConfiguration = (IFrameConfiguration)renderRequest.getAttribute(IFrameConfiguration.class.getName());
IFramePortletInstanceConfiguration iFramePortletInstanceConfiguration = portletDisplay.getPortletInstanceConfiguration(IFramePortletInstanceConfiguration.class);

IFrameDisplayContext iFrameDisplayContext = new IFrameDisplayContext(iFrameConfiguration, iFramePortletInstanceConfiguration, request);

String src = iFrameDisplayContext.getSrc();

boolean relative = iFrameDisplayContext.isRelative();

boolean auth = iFrameDisplayContext.isAuth();
String authType = iFrameDisplayContext.getAuthType();
String formMethod = iFrameDisplayContext.getFormMethod();
String userNameField = iFrameDisplayContext.getUserNameField();
String passwordField = iFrameDisplayContext.getPasswordField();

String userName = null;
String password = null;

if (authType.equals("basic")) {
	userName = iFrameDisplayContext.getBasicUserName();
	password = iFrameDisplayContext.getBasicPassword();
}
else {
	userName = iFrameDisplayContext.getFormUserName();
	password = iFrameDisplayContext.getFormPassword();
}

String hiddenVariables = iFrameDisplayContext.getHiddenVariables();
boolean resizeAutomatically = iFrameDisplayContext.isResizeAutomatically();
String heightMaximized = iFrameDisplayContext.getHeightMaximized();
String heightNormal = iFrameDisplayContext.getHeightNormal();
String width = iFrameDisplayContext.getWidth();

String alt = iFrameDisplayContext.getAlt();
String border = iFrameDisplayContext.getBorder();
String bordercolor = iFrameDisplayContext.getBordercolor();
String frameborder = iFrameDisplayContext.getFrameborder();
String hspace = iFrameDisplayContext.getHspace();
String longdesc = iFrameDisplayContext.getLongdesc();
String scrolling = iFrameDisplayContext.getScrolling();
String title = iFrameDisplayContext.getTitle();
String vspace = iFrameDisplayContext.getVspace();

List<String> iframeVariables = new ArrayList<String>();

Enumeration<String> enu = request.getParameterNames();

while (enu.hasMoreElements()) {
	String name = enu.nextElement();

	if (name.startsWith(_IFRAME_PREFIX)) {
		iframeVariables.add(name.substring(_IFRAME_PREFIX.length()).concat(StringPool.EQUAL).concat(request.getParameter(name)));
	}
}
%>

<%@ include file="/init-ext.jsp" %>

<%!
private static final String _IFRAME_PREFIX = "iframe_";
%>