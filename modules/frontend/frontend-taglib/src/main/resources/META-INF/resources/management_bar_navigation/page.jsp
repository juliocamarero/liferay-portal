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

<%@ include file="/management_bar_navigation/init.jsp" %>

<%
List<ManagementBarFilterItem> managementBarFilterItems = (List<ManagementBarFilterItem>)request.getAttribute("liferay-frontend:management-bar-navigation:managementBarFilterItems");
String label = (String)request.getAttribute("liferay-frontend:management-bar-navigation:label");
%>

<c:if test="<%= ListUtil.isNotEmpty(managementBarFilterItems) %>">
	<li class="dropdown">
		<a aria-expanded="true" class="dropdown-toggle" data-qa-id="filter" data-toggle="dropdown" href="javascript:;">
			<span class="management-bar-item-title"><liferay-ui:message key="<%= label %>" /></span>
			<span class="icon-sort"></span>
		</a>

		<ul class="dropdown-menu" data-qa-id="filterValues">

			<%
			for (ManagementBarFilterItem managementBarFilterItem : managementBarFilterItems) {
			%>

				<li class="<%= managementBarFilterItem.isActive() ? "active" : StringPool.BLANK %>">
					<aui:a href="<%= managementBarFilterItem.getUrl() %>" id="<%= Validator.isNotNull(managementBarFilterItem.getId()) ? managementBarFilterItem.getId() : StringPool.BLANK %>" label="<%= managementBarFilterItem.getLabel() %>" />
				</li>

			<%
			}
			%>

		</ul>
	</li>
</c:if>