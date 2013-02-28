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

<%@ include file="/html/portlet/portal_settings/init.jsp" %>

<%
int trashEnabled = PrefsPropsUtil.getInteger(company.getCompanyId(), PropsKeys.TRASH_ENABLED);
%>

<h3><liferay-ui:message key="recycle-bin" /></h3>

<aui:fieldset>
	<aui:input checked="<%= trashEnabled == TrashUtil.TRASH_ENABLED %>" helpMessage="enable-it-by-default-while-allowing-site-administrators-to-disable-it-per-site" label="enabled" name='<%= "settings--" + PropsKeys.TRASH_ENABLED + "--" %>' type="radio" value="<%= TrashUtil.TRASH_ENABLED %>" />

	<aui:input checked="<%= trashEnabled == TrashUtil.TRASH_DISABLED %>" label="disabled" name='<%= "settings--" + PropsKeys.TRASH_ENABLED + "--" %>' type="radio" value="<%= TrashUtil.TRASH_DISABLED %>" />
</aui:fieldset>