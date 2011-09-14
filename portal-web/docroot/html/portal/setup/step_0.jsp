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
<%@ include file="/html/portal/setup/init.jsp" %>
<%
	int cmd = ParamUtil.getInteger(request,SetupConstants.SETUP_WIZARD_CMD);
	
	String clazz = "setup-settings-done";
	
%>
<html>
	<head>
		<title></title>
		<style>
			<liferay-util:include page="/html/themes/classic/css/custom.css" />
			<liferay-util:include page="/html/themes/classic/css/custom_common.css" />
			<liferay-util:include page="/html/themes/classic/css/application.css" />
			<liferay-util:include page="/html/portal/css.jsp" />
			<liferay-util:include page="/html/portal/setup/css/main.jsp" />
		</style>
		<script type="text/javascript">
			function processStep(step) {
				var f = document.forms[0];
				document.getElementById("<%=SetupConstants.SETUP_WIZARD_CMD%>").value = step;
				f.submit();
			}
		</script>
	</head>
	<body>
		<div id="wrapper" style="padding-left: 2%; padding-right: 2%">
			<header id="banner" role="banner">
				<hgroup id="heading">
					<h1 class="company-title">
						<img src="/html/portal/setup/images/company_logo.png" title="Liferay" alt="Liferay"/>
					</h1>
				</hgroup>
			</header>
		
			<div id="content">
				<form action="/c/portal/setup_wizard" id="fm" name="fm" method="post">
					<aui:input type="hidden" name="<%= SetupConstants.SETUP_WIZARD_CMD %>" value="<%= cmd %>"/>
					<c:choose>
						<c:when test="<%= cmd == SetupConstants.SETUP_WIZARD_STEP_0 %>">
							<h1>Welcome to Liferay Simple Setup</h1>
							<div style="display:table">
								<div style="float:left; width: 49%">
									From here, you can edit basic configuration in Liferay Portal, such as Database configuration, Credentials for Administrator or default Site Name.
									<div id="wrapper-button">
										Please click button to start Setup Wizard to customize your Liferay Portal. Enjoy!
										<%
											String onclickInit = "javascript:processStep('" + SetupConstants.SETUP_WIZARD_STEP_1 + "')";
										%>
									</div>
									<div id="wrapper-button" style="text-align: center">
										<aui:button name="btn_init_setup" 
											onClick="<%= onclickInit %>"
											value="Start Setup Wizard"/>
									</div>
								</div>
								<div style="float:right; width: 49%">
									<img id="wrapper-image" src="/html/themes/classic/_diffs/images/screenshot.png" 
										style="width:75%;%" alt="Liferay Portal Example" title="Liferay Portal Example"/>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<div id="setup-wrapper">
								<c:choose>
									<c:when test="<%= (cmd == SetupConstants.SETUP_WIZARD_STEP_1) %>">
										<h2 class="fieldset-header first">
											Database Config
										</h2>
										<aui:fieldset>
											<liferay-util:include page="/html/portal/setup/step_1.jsp" />
										</aui:fieldset>
										<h2 class="fieldset-header first">
											Administrator Config
										</h2>
										<aui:fieldset>
											<liferay-util:include page="/html/portal/setup/step_2.jsp" />
										</aui:fieldset>
										<h2 class="fieldset-header first">
											Portal Config
										</h2>
										<aui:fieldset>
											<liferay-util:include page="/html/portal/setup/step_3.jsp" />
										</aui:fieldset>
										<div id="wrapper-button">
										<%
											String onclickSave = "javascript:processStep('" + SetupConstants.SETUP_WIZARD_STEP_2 + "')";
										%>
											<aui:button name="btn_save_portal"
												onClick="<%= onclickSave %>"
												value="Start Configuration"/>
										</div>
									</c:when>
									<c:otherwise>
										<liferay-util:include page="/html/portal/setup/step_4.jsp" />
									</c:otherwise>
								</c:choose>
							</div>
						</c:otherwise>
					</c:choose>
				</form>
			</div>
		</div>
	</body>
</html>