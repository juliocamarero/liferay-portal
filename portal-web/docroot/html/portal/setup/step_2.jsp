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

<div class="wrapper last">
	<table>
		<tr>
			<td>
				<aui:input type="text" label="Admin Username" name="setup_wizard.admin.username" id="setup_wizard.admin.username" 
					value="<%= adminInputs.get_username() %>"/>
			</td>
		</tr>		
		<tr>
			<td>
				<aui:input type="password" label="Admin Password" name="setup_wizard.admin.password" id="setup_wizard.admin.password"
					value="<%= adminInputs.get_password() %>"/>
			</td>
		</tr>
		<tr>
			<td>
				<aui:input type="text" label="E-mail" name="setup_wizard.admin.email" id="setup_wizard.admin.email" 
					value="<%= adminInputs.get_email() %>"/>
			</td>
		</tr>
	</table>
</div>