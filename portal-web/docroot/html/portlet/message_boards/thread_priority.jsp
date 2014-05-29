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

<%@ include file="/html/portlet/message_boards/init.jsp" %>

<%
MBMessage message = (MBMessage)request.getAttribute("thread_priority.jsp-message");
MBThread thread = (MBThread)request.getAttribute("thread_priority.jsp-thread");

String[] threadPriority = MBUtil.getThreadPriority(mbSettings, themeDisplay.getLanguageId(), thread.getPriority(), themeDisplay);
%>

<c:if test="<%= (threadPriority != null) && (thread.getPriority() > 0) %>">
	<img alt="<%= threadPriority[0] %>" class="thread-priority" src="<%= threadPriority[1] %>" title="<%= threadPriority[0] %>" />
</c:if>

<c:if test="<%= thread.isLocked() %>">
	<i class="icon-lock" title="<%= LanguageUtil.get(pageContext, "thread-locked") %>"></i>
</c:if>

<%= message.getSubject() %>