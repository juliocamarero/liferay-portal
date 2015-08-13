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
String cssClass = (String)request.getAttribute("liferay-ui:list-group-element-item-checkbox:cssClass");
String id = (String)request.getAttribute("liferay-ui:list-group-element-item-checkbox:id");
String name = (String)request.getAttribute("liferay-ui:list-group-element-item-checkbox:name");
String title = (String)request.getAttribute("liferay-ui:list-group-element-item-checkbox:title");
String value = (String)request.getAttribute("liferay-ui:list-group-element-item-checkbox:value");
%>

<liferay-ui:list-group-element-item cssClass="hidden-sm hidden-xs" view="lexicon">
	<aui:input cssClass="<%= cssClass %>" id="<%= id %>" label="" name="<%= name %>" title="<%= title %>" type="checkbox" value="<%= value %>" wrapperCssClass="checkbox-default" />
</liferay-ui:list-group-element-item>