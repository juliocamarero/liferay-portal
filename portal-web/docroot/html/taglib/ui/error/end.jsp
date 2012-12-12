<%--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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
String key = (String)request.getAttribute("liferay-ui:error:key");
String message = (String)request.getAttribute("liferay-ui:error:message");
boolean translateMessage = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:error:translateMessage"));
String rowBreak = (String)request.getAttribute("liferay-ui:error:rowBreak");
%>

<c:choose>
	<c:when test="<%= (key != null) && Validator.isNull(message) %>">
		<c:if test="<%= SessionErrors.contains(portletRequest, key) %>">
			</div>

			<%= rowBreak %>
		</c:if>
	</c:when>
	<c:when test="<%= key == null %>">
		<c:choose>
			<c:when test="<%= SessionErrors.contains(portletRequest, SanitizerException.class) %>">
				<div class="portlet-msg-error">
					<liferay-ui:message key="your-request-failed-to-complete-because-some-of-the-data-entered-could-not-be-sanitized" />
				</div>

				<%= rowBreak %>
			</c:when>
			<c:when test="<%= !SessionErrors.isEmpty(portletRequest) %>">
				<div class="portlet-msg-error">
					<liferay-ui:message key="your-request-failed-to-complete" />
				</div>

				<%= rowBreak %>
			</c:when>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:if test="<%= SessionErrors.contains(portletRequest, key) %>">
			<div class="portlet-msg-error">

			<c:choose>
				<c:when test="<%= translateMessage %>">
					<%= LanguageUtil.get(pageContext, message) %>
				</c:when>
				<c:otherwise>
					<%= message %>
				</c:otherwise>
			</c:choose>

			</div>

			<%= rowBreak %>
		</c:if>
	</c:otherwise>
</c:choose>