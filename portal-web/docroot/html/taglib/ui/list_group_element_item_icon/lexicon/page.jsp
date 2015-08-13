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
String cssClass = (String)request.getAttribute("liferay-ui:list-group-element-item-icon:cssClass");
String iconCssClass = (String)request.getAttribute("liferay-ui:list-group-element-item-icon:iconCssClass");
boolean isImage = GetterUtil.getBoolean(request.getAttribute("liferay-ui:list-group-element-item-icon:isImage"));
String icon = (String)request.getAttribute("liferay-ui:list-group-element-item-icon:icon");
%>

<liferay-ui:list-group-element-item view="lexicon">
	<div class="<%= cssClass %> click-selector detailed-list-view-icon">
		<c:choose>
			<c:when test="<%= isImage %>">
				<div class="user-icon user-icon-lg">
					<img alt="thumbnail" class="img-responsive" src="<%= HtmlUtil.escapeAttribute(icon) %>" />
				</div>
			</c:when>
			<c:otherwise>
				<aui:icon cssClass="icon-xl text-default" image="<%= icon %>" view="lexicon" />
			</c:otherwise>
		</c:choose>
	</div>
</liferay-ui:list-group-element-item>