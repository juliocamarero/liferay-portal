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

<%@ include file="/html/portlet/message_boards/init.jsp" %>

<link href="<%= PortalUtil.getStaticResourceURL(request, "/html/portlet/message_boards/css/main.css") %>" rel="stylesheet" type="text/css" />

<div class="portlet-message-boards" style="margin-top: 10px; padding-top: 30px;">
	<liferay-util:include page="/html/portlet/message_boards/view_thread_tree.jsp" />
</div>