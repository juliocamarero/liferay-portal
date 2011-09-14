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

<div class="msg-success">
	<span class="bigger">Congratulations!!</span> Your configuration has finished successly.
</div>
<div class="msg-alert">
	A <%= SetupConstants.LIFERAY_SETUP_PROPS_FILE %></span> file has been created for you into /WEB-INF/classes with the following lines.
	<br/>
	The application Server <span class="bigger">MUST BE RESTARTED</span> to apply changes.
</div>
<div id="wrapper-button" style="text-align: center; padding-bottom: 15px">
	<%
		String onclickPortal = "javascript:processStep('" + SetupConstants.SETUP_WIZARD_STEP_3 + "')";
		String buttonValue = "Go to " + portalInputs.get_portalname() + " !!";
	%>
	<aui:button name="btn_go_to_portal" 
		onClick="<%= onclickPortal %>"
		value="<%= buttonValue %>"/>
</div>
<div id="setup-summary">
	<h2 class="fieldset-header first">
		Your custom portal-wizard.properties
	</h2>
	<aui:fieldset>
		<div class="wrapper last">
			<aui:input type="textarea" name="portal-wizard" id="portal-wizard" value="<%= setupConfiguration.toProperties() %>"
				style='<%= "height: " + ModelHintsConstants.TEXTAREA_DISPLAY_HEIGHT + "px; width: 90%;" %>' 
				wrap="soft" />
		</div>
	</aui:fieldset>
	
	<h2 class="fieldset-header first">
		Database Config
	</h2>
	<aui:fieldset>
		<div class="wrapper last">
			<ul>
				<li>Driver: <%= databaseInputs.get_driver() %></li>
				<li>Username: <%= databaseInputs.get_username() %></li>
				<li>Password: ******</li>
				<li>Database Name: <%= databaseInputs.get_database() %></li>
			</ul>
		</div>
	</aui:fieldset>
	
	<h2 class="fieldset-header first">
		Administrator Config
	</h2>
	<aui:fieldset>
		<div class="wrapper last">
			<ul>
				<li>Username: <%= adminInputs.get_username() %></li>
				<li>Password: ******</li>
				<li>E-mail: <%= adminInputs.get_email() %></li>
			</ul>
		</div>
	</aui:fieldset>
	
	<h2 class="fieldset-header first">
		Portal Config
	</h2>
	<aui:fieldset>
		<div class="wrapper last">
			<ul>
				<li>Portal Name: <%= portalInputs.get_portalname() %></li>
			</ul>
		</div>
	</aui:fieldset>
</div>