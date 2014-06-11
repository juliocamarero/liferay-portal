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

<%@ include file="/html/portlet/admin/init.jsp" %>

<%
Runtime runtime = Runtime.getRuntime();

numberFormat = NumberFormat.getInstance(locale);

long totalMemory = runtime.totalMemory();
long usedMemory = totalMemory - runtime.freeMemory();
%>

<div>
	<portlet:resourceURL var="totalMemoryChartURL">
		<portlet:param name="struts_action" value="/admin_server/view_chart" />
		<portlet:param name="type" value="total" />
		<portlet:param name="totalMemory" value="<%= String.valueOf(totalMemory) %>" />
		<portlet:param name="usedMemory" value="<%= String.valueOf(usedMemory) %>" />
	</portlet:resourceURL>

	<img alt="<liferay-ui:message key="memory-used-vs-total-memory" />" src="<%= totalMemoryChartURL %>" />

	<portlet:resourceURL var="maxMemoryChartURL">
		<portlet:param name="struts_action" value="/admin_server/view_chart" />
		<portlet:param name="type" value="max" />
		<portlet:param name="maxMemory" value="<%= String.valueOf(runtime.maxMemory()) %>" />
		<portlet:param name="usedMemory" value="<%= String.valueOf(usedMemory) %>" />
	</portlet:resourceURL>

	<img alt="<liferay-ui:message key="memory-used-vs-max-memory" />" src="<%= maxMemoryChartURL %>" />
</div>

<br />

<table class="lfr-table">
<tr>
	<td>
		<liferay-ui:message key="used-memory" />:
	</td>
	<td>
		<%= numberFormat.format(usedMemory) %> <liferay-ui:message key="bytes" />
	</td>
</tr>
<tr>
	<td>
		<liferay-ui:message key="total-memory" />:
	</td>
	<td>
		<%= numberFormat.format(runtime.totalMemory()) %> <liferay-ui:message key="bytes" />
	</td>
</tr>
<tr>
	<td>
		<liferay-ui:message key="maximum-memory" />:
	</td>
	<td>
		<%= numberFormat.format(runtime.maxMemory()) %> <liferay-ui:message key="bytes" />
	</td>
</tr>
</table>

<br />

<liferay-ui:panel-container extended="<%= true %>" id="adminServerAdministrationActionsPanelContainer" persistState="<%= true %>">
	<liferay-ui:panel collapsible="<%= true %>" extended="<%= true %>" id="adminServerAdministrationActionsPanel" persistState="<%= true %>" title="actions">
		<table class="table table-condensed table-hover">
		<tr>
			<td>
				<liferay-ui:message key="run-the-garbage-collector-to-free-up-memory" />
			</td>
			<td>

				<%
				String taglibGC = renderResponse.getNamespace() + "saveServer('gc');";
				%>

				<aui:button onClick="<%= taglibGC %>" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="clear-content-cached-by-this-vm" />
			</td>
			<td>

				<%
				String taglibCacheSingle = renderResponse.getNamespace() + "saveServer('cacheSingle');";
				%>

				<aui:button onClick="<%= taglibCacheSingle %>" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="clear-content-cached-across-the-cluster" />
			</td>
			<td>

				<%
				String taglibCacheMulti = renderResponse.getNamespace() + "saveServer('cacheMulti');";
				%>

				<aui:button onClick="<%= taglibCacheMulti %>" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="clear-the-database-cache" />
			</td>
			<td>

				<%
				String taglibCacheDb = renderResponse.getNamespace() + "saveServer('cacheDb');";
				%>

				<aui:button onClick="<%= taglibCacheDb %>" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="clear-the-direct-servlet-cache" />
			</td>
			<td>

				<%
				String taglibCacheServlet = renderResponse.getNamespace() + "saveServer('cacheServlet');";
				%>

				<aui:button onClick="<%= taglibCacheServlet %>" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="reindex-all-search-indexes" />
			</td>
			<td>

				<%
				String taglibReindex = renderResponse.getNamespace() + "saveServer('reindex');";
				%>

				<aui:button onClick="<%= taglibReindex %>" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="reindex-all-spell-check-indexes" />
			</td>
			<td>

				<%
				String taglibReindexDictionaries = renderResponse.getNamespace() + "saveServer('reindexDictionaries');";
				%>

				<aui:button onClick="<%= taglibReindexDictionaries %>" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="reset-preview-and-thumbnail-files-for-documents-and-media-portlet" />
			</td>
			<td>

				<%
				String taglibDLPreviews = renderResponse.getNamespace() + "saveServer('dlPreviews');";
				%>

				<aui:button onClick="<%= taglibDLPreviews %>" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="generate-thread-dump" />
			</td>
			<td>

				<%
				String taglibThreadDump = renderResponse.getNamespace() + "saveServer('threadDump');";
				%>

				<aui:button onClick="<%= taglibThreadDump %>" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="verify-database-tables-of-all-plugins" />
			</td>
			<td>

				<%
				String taglibVerifyPluginTables = renderResponse.getNamespace() + "saveServer('verifyPluginTables');";
				%>

				<aui:button onClick="<%= taglibVerifyPluginTables %>" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="verify-membership-policies" />
			</td>
			<td>

				<%
				String taglibVerifyMembershipPolicies = renderResponse.getNamespace() + "saveServer('verifyMembershipPolicies');";
				%>

				<aui:button onClick="<%= taglibVerifyMembershipPolicies %>" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="clean-up-permissions" /> <liferay-ui:icon-help message="clean-up-permissions-help" />
			</td>
			<td>

				<%
				String taglibCleanUpPermissions = renderResponse.getNamespace() + "saveServer('cleanUpPermissions');";
				%>

				<aui:button onClick="<%= taglibCleanUpPermissions %>" value="execute" />
			</td>
		</tr>
		</table>
	</liferay-ui:panel>
</liferay-ui:panel-container>