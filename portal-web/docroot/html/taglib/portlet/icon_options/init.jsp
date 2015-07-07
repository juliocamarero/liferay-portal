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

<%@ page import="com.liferay.portal.kernel.portlet.configuration.PortletConfigurationIcon" %><%@
page import="com.liferay.portal.kernel.portlet.configuration.PortletConfigurationIconFactory" %><%@
page import="com.liferay.portal.kernel.portlet.configuration.PortletConfigurationIconTracker" %><%@
page import="com.liferay.util.PropertyComparator" %><%@
page import="com.liferay.portal.kernel.util.Validator" %>

<%
String view = (String)request.getAttribute("liferay-ui:icon:view");
String direction = (String)request.getAttribute("liferay-ui:icon:direction");
Boolean showArrow = (Boolean)request.getAttribute("liferay-ui:icon:showArrow");
%>