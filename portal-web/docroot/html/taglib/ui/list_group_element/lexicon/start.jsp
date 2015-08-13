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
String cssClass = (String)request.getAttribute("liferay-ui:list-group-element:cssClass");
String extraData = (String)request.getAttribute("liferay-ui:list-group-element:extraData");
String state = (String)request.getAttribute("liferay-ui:list-group-element:state");
%>

<li class="<%= cssClass %> list-group-item list-group-item-<%= state %>" <%= extraData %> >