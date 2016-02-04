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

<%
long groupId = layoutsAdminDisplayContext.getGroupId();
long liveGroupId = layoutsAdminDisplayContext.getLiveGroupId();
boolean privateLayout = layoutsAdminDisplayContext.isPrivateLayout();
LayoutSet layoutSet = layoutsAdminDisplayContext.getSelLayoutSet();

Theme selTheme = layoutSet.getTheme();
ColorScheme selColorScheme = layoutSet.getColorScheme();
%>

<liferay-ui:error-marker key="<%= WebKeys.ERROR_SECTION %>" value="look-and-feel" />

<aui:model-context bean="<%= layoutSet %>" model="<%= Layout.class %>" />

<aui:input name="devices" type="hidden" value="regular" />

<%@ include file="/layout_set/look_and_feel_regular_browser.jspf" %>