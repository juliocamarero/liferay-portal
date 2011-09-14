<%--
/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

<%@page import="com.liferay.portal.setup.SetupConfiguration"%>
<%@page import="com.liferay.portal.setup.SetupConstants"%>
<%@page import="com.liferay.portal.setup.SetupAdminInputs"%>
<%@page import="com.liferay.portal.setup.SetupDatabaseInputs"%>
<%@page import="com.liferay.portal.setup.SetupPortalInputs"%>

<%@ include file="/html/common/init.jsp" %>

<%
	SetupConfiguration setupConfiguration = (SetupConfiguration)session.getAttribute(SetupConstants.LIFERAY_SETUP_CONFIG);

	SetupDatabaseInputs databaseInputs = null;
	SetupAdminInputs adminInputs = null;
	SetupPortalInputs portalInputs = null;

	if (setupConfiguration != null) {
		databaseInputs = setupConfiguration.get_databaseSetup();
		adminInputs = setupConfiguration.get_adminSetup();
		portalInputs = setupConfiguration.get_portalSetup();
	}
%>