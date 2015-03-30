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

<%@ include file="/html/taglib/init.jsp" %>

<%
List<SelectableEntry> entries = (List<SelectableEntry>)request.getAttribute("liferay-ui:entry-selector:entries");
String hiddenInput = (String)request.getAttribute("liferay-ui:entry-selector:hiddenInput");
String id = GetterUtil.getString((String)request.getAttribute("liferay-ui:entry-selector:id"));
PortletPreferences portletPreferences = (PortletPreferences)request.getAttribute("liferay-ui:entry-selector:portletPreferences");
String title = GetterUtil.getString((String)request.getAttribute("liferay-ui:entry-selector:title"));
%>

<div class="lfr-entry-selector" id="<%= namespace + id %>entrySelector">
	<aui:input name="<%= hiddenInput %>" type="hidden" />

	<ul class="list-inline list-unstyled selected-items">

		<%
		for (SelectableEntry entry : entries) {
			if (entry.isSelected(request, portletPreferences)) {
		%>
			<li class="list-entry" data-key="<%= entry.getKey() %>">
				<span><%= LanguageUtil.get(request, entry.getLabel()) %></span>

				<aui:button cssClass="remove-button" icon="icon-remove" />
			</li>
		<%
			}
		}
		%>

	</ul>

	<aui:button cssClass="select-button" name='<%= id + "selectButton" %>' value="select" />
</div>

<aui:script use="liferay-entry-selector">
	var entries = [];

	<%
	for (SelectableEntry entry : entries) {
		String entryLabel = LanguageUtil.get(request, entry.getLabel());
		String entryKey = entry.getKey();
	%>

		entries.push(
			{
				icon: '<%= entry.getIcon() %>',
				key: '<%= entry.getKey() %>',
				label: '<%= LanguageUtil.get(request, entry.getLabel()) %>',
				selected: <%= entry.isSelected(request, portletPreferences) %>
			}
		);

	<%
	}
	%>

	var selector = new Liferay.EntrySelector(
		{
			entries: entries,
			rootNode: '#<%= namespace + id %>entrySelector'
		}
	);
</aui:script>