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
	List<String> drivers = (List<String>)session.getAttribute(SetupConstants.LIFERAY_SETUP_DRIVERS);
%>
<div class="wrapper last">
	<table>
		<tr>
			<td>
				<aui:select label="Driver" name="setup_wizard.db.driver" id="setup_wizard.db.driver">
				<% for (String driver : drivers) {
						String value = driver.toLowerCase().replaceAll(" ","");
						boolean selected = (databaseInputs.get_driver().equals(value));
				%>
					<option value="<%= value %>" <%= selected ? "selected='true'" : "" %>><%= driver %></option>
				<% } %>
				</aui:select>
				<span class="tooltip">If your driver is not supported directly by Liferay, you'll have to install it in your App Server.</span>
			</td>
		</tr>
		<tr>
			<td>
				<aui:input type="text" label="User Name" name="setup_wizard.db.username"
					id="setup_wizard.db.username" value="<%= databaseInputs.get_username() %>"/>
			</td>
		</tr>
		<tr>
			<td>
				<aui:input type="text" label="Password" name="setup_wizard.db.password" 
					id="setup_wizard.db.password" value="<%= databaseInputs.get_password() %>"/>
			</td>
		</tr>
		<tr>
			<td>
				<aui:input type="text" label="Database Name" name="setup_wizard.db.database-name" 
					id="setup_wizard.db.database-name" value="<%= databaseInputs.get_database() %>"/>
			</td>
		</tr>
	</table>
</div>