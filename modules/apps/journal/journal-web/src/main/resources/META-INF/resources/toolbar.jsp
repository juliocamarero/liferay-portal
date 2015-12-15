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

<%@ include file="/init.jsp" %>

<%
String searchContainerId = ParamUtil.getString(request, "searchContainerId");
%>

<liferay-frontend:management-bar
	includeCheckBox="<%= !user.isDefaultUser() && journalDisplayContext.isShowEditActions() %>"
	searchContainerId="<%= searchContainerId %>"
>
	<liferay-frontend:management-bar-buttons>
		<c:if test="<%= journalDisplayContext.isShowInfoPanel() %>">
			<liferay-frontend:management-bar-button cssClass="infoPanelToggler" href="javascript:;" icon="info-circle" label="info" />
		</c:if>

		<c:if test="<%= !journalDisplayContext.isSearch() %>">
			<liferay-frontend:management-bar-display-buttons
				displayViews="<%= journalDisplayContext.getDisplayViews() %>"
				portletURL="<%= journalDisplayContext.getPortletURL() %>"
				selectedDisplayStyle="<%= journalDisplayContext.getDisplayStyle() %>"
			/>
		</c:if>
	</liferay-frontend:management-bar-buttons>

	<%
	String navigation = ParamUtil.getString(request, "navigation");

	String label = null;

	if (navigation.equals("structure")) {
		label = LanguageUtil.get(request, "structure") + StringPool.COLON + StringPool.SPACE + journalDisplayContext.getDdmStructureName();
	}
	%>

	<liferay-frontend:management-bar-filters>
		<liferay-frontend:management-bar-navigation
			label="<%= label %>"
		>

			<%
			PortletURL navigationAllURL = journalDisplayContext.getPortletURL();

			navigationAllURL.setParameter("navigation", "all");
			%>

			<liferay-frontend:management-bar-navigation-item active='<%= navigation.equals("all") %>' label="all" url="<%= navigationAllURL.toString() %>" />

			<%
			PortletURL navigationRecentURL = journalDisplayContext.getPortletURL();

			navigationRecentURL.setParameter("navigation", "recent");
			%>

			<liferay-frontend:management-bar-navigation-item active='<%= navigation.equals("recent") %>' label="recent" url="<%= navigationRecentURL.toString() %>" />

			<%
			PortletURL navigationMineURL = journalDisplayContext.getPortletURL();

			navigationMineURL.setParameter("navigation", "mine");
			%>

			<liferay-frontend:management-bar-navigation-item active='<%= navigation.equals("mine") %>' label="mine" url="<%= navigationMineURL.toString() %>" />

			<liferay-frontend:management-bar-navigation-item id="structures" label="structures" url="javascript:;" />
		</liferay-frontend:management-bar-navigation>

		<liferay-frontend:management-bar-filter
			label="status"
			managementBarFilterItems="<%= journalDisplayContext.getManagementBarStatusFilterItems() %>"
			value="<%= journalDisplayContext.getManagementBarStatusFilterValue() %>"
		/>

		<liferay-frontend:management-bar-sort
			orderByCol="<%= journalDisplayContext.getOrderByCol() %>"
			orderByType="<%= journalDisplayContext.getOrderByType() %>"
			orderColumns='<%= new String[] {"display-date", "modified-date"} %>'
			portletURL="<%= journalDisplayContext.getPortletURL() %>"
		/>
	</liferay-frontend:management-bar-filters>

	<liferay-frontend:management-bar-action-buttons>
		<c:if test="<%= journalDisplayContext.isShowInfoPanel() %>">
			<liferay-frontend:management-bar-button cssClass="infoPanelToggler" href="javascript:;" icon="info-circle" label="info" />
		</c:if>

		<%
		String taglibURL = "javascript:" + renderResponse.getNamespace() + "deleteEntries();";
		%>

		<liferay-frontend:management-bar-button href="<%= taglibURL %>" icon='<%= TrashUtil.isTrashEnabled(scopeGroupId) ? "trash" : "times" %>' label='<%= TrashUtil.isTrashEnabled(scopeGroupId) ? "recycle-bin" : "delete" %>' />

		<%
		taglibURL = "javascript:Liferay.fire('" + renderResponse.getNamespace() + "editEntry', {action: 'expireEntries'}); void(0);";
		%>

		<liferay-frontend:management-bar-button href="<%= taglibURL %>" icon="time" label="expire" />

		<%
		taglibURL = "javascript:Liferay.fire('" + renderResponse.getNamespace() + "editEntry', {action: 'moveEntries'}); void(0);";
		%>

		<liferay-frontend:management-bar-button href="<%= taglibURL %>" icon="change" label="move" />
	</liferay-frontend:management-bar-action-buttons>
</liferay-frontend:management-bar>

<aui:script>
	function <portlet:namespace />deleteEntries() {
		if (<%= TrashUtil.isTrashEnabled(scopeGroupId) %> || confirm(' <%= UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-delete-the-selected-entries") %>')) {
			Liferay.fire(
				'<%= renderResponse.getNamespace() %>editEntry',
				{
					action: '<%= TrashUtil.isTrashEnabled(scopeGroupId) ? "moveEntriesToTrash" : "deleteEntries" %>'
				}
			);
		}
	}
</aui:script>

<aui:script use="liferay-item-selector-dialog">
	var form = $(document.<portlet:namespace />fm);

	<portlet:renderURL var="viewDDMStructureArticlesURL">
		<portlet:param name="browseBy" value="structure" />
		<portlet:param name="navigation" value="structure" />
		<portlet:param name="folderId" value="<%= String.valueOf(JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID) %>" />
		<portlet:param name="showEditActions" value="<%= String.valueOf(journalDisplayContext.isShowEditActions()) %>" />
	</portlet:renderURL>

	$('#<portlet:namespace />structures').on(
		'click',
		function(event) {
			event.preventDefault();

			var itemSelectorDialog = new A.LiferayItemSelectorDialog(
				{
					eventName: '<portlet:namespace />selectStructure',
					on: {
						selectedItemChange: function(event) {
							var selectedItem = event.newVal;

							if (selectedItem) {
								var uri = '<%= viewDDMStructureArticlesURL %>';

								uri = Liferay.Util.addParams('<portlet:namespace />ddmStructureKey=' + selectedItem, uri);

								location.href = uri;
							}
						}
					},
					'strings.add': '<liferay-ui:message key="done" />',
					title: '<liferay-ui:message key="select-structure" />',
					url: '<portlet:renderURL windowState="<%= LiferayWindowState.POP_UP.toString() %>"><portlet:param name="mvcPath" value="/select_structure.jsp" /><portlet:param name="ddmStructureKey" value="<%= journalDisplayContext.getDDMStructureKey() %>" /></portlet:renderURL>'
				}
			);

			itemSelectorDialog.open();
		}
	);
</aui:script>