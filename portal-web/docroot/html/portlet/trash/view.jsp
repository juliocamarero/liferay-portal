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

<%@ include file="/html/portlet/trash/init.jsp" %>

<%
int trashEntriesCount = TrashEntryLocalServiceUtil.getTrashEntriesCount(themeDisplay.getScopeGroupId());
%>

<aui:layout>
	<c:if test="<%= trashEntriesCount > 0 %>">
		<aui:button-row>
			<aui:button name="emptyTrashButton" value="empty-trash" />
		</aui:button-row>
	</c:if>

	<liferay-ui:search-container
		emptyResultsMessage="the-recycle-bin-is-empty"
		headerNames="primaryKey, className, entryName, type, status, actions"
		id="trashEntriesSearchContainer"
		rowChecker="<%= new RowChecker(renderResponse) %>"
	>
		<liferay-ui:search-container-results
			results="<%= TrashEntryLocalServiceUtil.getEntries(themeDisplay.getScopeGroupId(), searchContainer.getStart(), searchContainer.getEnd()) %>"
			total="<%= trashEntriesCount %>"
		/>

		<liferay-ui:search-container-row
			className="com.liferay.portlet.trash.model.TrashEntry"
			keyProperty="entryId"
			modelVar="trashEntry"
		>

			<%
			String className = trashEntry.getClassName();

			AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(className);
			AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(className, trashEntry.getClassPK());
			AssetRenderer assetRenderer = assetRendererFactory.getAssetRenderer(trashEntry.getClassPK());
			%>

			<liferay-ui:search-container-column-text
				name="primaryKey"
				property="primaryKey"
			/>

			<liferay-ui:search-container-column-text
				name="type"
			>
				<liferay-ui:icon src="<%= assetRenderer.getIconPath(renderRequest) %>" />
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				name="className"
				property="className"
				orderable="true"
			/>

			<liferay-ui:search-container-column-text
				name="entryName"
				value="<%= assetEntry.getTitle() %>"
			/>

			<liferay-ui:search-container-column-text
				name="status"
				value="status"
				orderable="true"
			/>

			<liferay-ui:search-container-column-jsp
				align="right"
				path="/html/portlet/trash/trash_entry_actions.jsp"
			/>

		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator paginate="<%= false %>" />
	</liferay-ui:search-container>
</aui:layout>