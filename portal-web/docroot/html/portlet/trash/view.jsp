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

<liferay-ui:search-container
	emptyResultsMessage="the-recycle-bin-is-empty"
	headerNames="name,type,removed-date"
	rowChecker="<%= new RowChecker(renderResponse) %>"
>
	<liferay-ui:search-container-results>

		<%
		Object[] entries = TrashEntryServiceUtil.getEntries(themeDisplay.getScopeGroupId(), searchContainer.getStart(), searchContainer.getEnd());

		pageContext.setAttribute("results", entries[0]);
		pageContext.setAttribute("total", entries[1]);
		aproximate = (Boolean)entries[2];
		%>

	</liferay-ui:search-container-results>

	<liferay-ui:search-container-row
		className="com.liferay.portlet.trash.model.TrashEntry"
		keyProperty="entryId"
		modelVar="trashEntry"
	>

		<%
		String className = trashEntry.getClassName();

		TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(className);

		AssetRendererFactory assetRendererFactory = trashHandler.getAssetRendererFactory();
		AssetRenderer assetRenderer = trashHandler.getAssetRenderer(trashEntry.getClassPK());
		AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(className, trashEntry.getClassPK());

		PortletURL viewFullContentURL = renderResponse.createRenderURL();

		viewFullContentURL.setParameter("struts_action", "/trash/view_content");
		viewFullContentURL.setParameter("redirect", currentURL);

		if (assetEntry != null) {
			viewFullContentURL.setParameter("assetEntryId", String.valueOf(assetEntry.getEntryId()));
		}

		if (assetRendererFactory != null) {
			viewFullContentURL.setParameter("type", assetRendererFactory.getType());
		}

		viewFullContentURL.setParameter("showEditURL", String.valueOf(Boolean.FALSE));
		%>

		<liferay-ui:search-container-column-text
			href="<%= viewFullContentURL.toString() %>"
			name="name"
		>
			<liferay-ui:icon label="<%= true %>" message="<%= assetEntry.getTitle(locale) %>" src="<%= assetRenderer.getIconPath(renderRequest) %>" />
		</liferay-ui:search-container-column-text>

		<liferay-ui:search-container-column-text
			name="type"
			value="<%= LanguageUtil.get(pageContext, assetRendererFactory.getType()) %>"
		/>

		<liferay-ui:search-container-column-text
			name="removed-date"
			value="<%= dateFormatDateTime.format(trashEntry.getTrashedDate()) %>"
		/>

		<liferay-ui:search-container-column-jsp
			align="right"
			path="/html/portlet/trash/trash_entry_actions.jsp"
		/>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator type='<%= aproximate ? "more" : "regular" %>' />
</liferay-ui:search-container>
