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
boolean aproximate = false;
%>

<portlet:actionURL var="editTrashEntryURL">
	<portlet:param name="struts_action" value="/trash/edit_entry" />
</portlet:actionURL>

<aui:form action='<%= editTrashEntryURL %>' method="post" name="fm" onSubmit="event.preventDefault();">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="" />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="deleteEntryIds" type="hidden" />
	<aui:input name="restoreEntryIds" type="hidden" />

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

		<c:if test="<%= total > 0 %>">
			<aui:button-row>
				<aui:button name="deleteButton" onClick='<%= renderResponse.getNamespace() + "deleteEntries();" %>' value="delete" />

				<aui:button name="restoreButton" onClick='<%= renderResponse.getNamespace() + "restoreEntries();" %>' value="restore" />

				<aui:button name="emptyTrashButton" onClick='<%= renderResponse.getNamespace() + "emptyTrash();" %>' value="empty-recycle-bin" />
			</aui:button-row>

			<div class="separator"><!-- --></div>
		</c:if>

		<liferay-ui:search-iterator type='<%= aproximate ? "more" : "regular" %>' />
	</liferay-ui:search-container>
</aui:form>

<aui:script use="aui-base">
	Liferay.provide(
		window,
		'<portlet:namespace />deleteEntries',
		function() {
			var deleteEntryIds = Liferay.Util.listCheckedExcept(document.<portlet:namespace />fm, "<portlet:namespace />allRowIds");

			if (confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-delete-the-selected-entries") %>')) {
				document.<portlet:namespace />fm.method = "post";
				document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = "<%= Constants.DELETE %>";
				document.<portlet:namespace />fm.<portlet:namespace />deleteEntryIds.value = Liferay.Util.listCheckedExcept(document.<portlet:namespace />fm, "<portlet:namespace />allRowIds");
				submitForm(document.<portlet:namespace />fm);
			}
		},
		['liferay-util-list-fields']
	);

	Liferay.provide(
		window,
		'<portlet:namespace />emptyTrash',
		function() {
			if (confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-empty-the-recycle-bin") %>')) {
				document.<portlet:namespace />fm.method = "post";
				document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = "<%= Constants.EMPTY_TRASH %>";
				submitForm(document.<portlet:namespace />fm);
			}
		}
	);

	Liferay.provide(
		window,
		'<portlet:namespace />restoreEntries',
		function() {
			var restoreEntryIds = Liferay.Util.listCheckedExcept(document.<portlet:namespace />fm, "<portlet:namespace />allRowIds");

			if (confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-restore-the-selected-entries") %>')) {
				document.<portlet:namespace />fm.method = "post";
				document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = "<%= Constants.RESTORE %>";
				document.<portlet:namespace />fm.<portlet:namespace />restoreEntryIds.value = Liferay.Util.listCheckedExcept(document.<portlet:namespace />fm, "<portlet:namespace />allRowIds");
				submitForm(document.<portlet:namespace />fm);
			}
		},
		['liferay-util-list-fields']
	);
</aui:script>