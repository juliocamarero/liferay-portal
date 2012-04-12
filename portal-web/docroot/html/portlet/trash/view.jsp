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

Object trashEntries[] = TrashEntryServiceUtil.getEntries(scopeGroupId);

int[] affectedEntriesCount = (int[])renderRequest.getAttribute(WebKeys.RECYCLE_BIN_RESULTS);
int trashEntriesCount = (Integer)trashEntries[1];
%>

<aui:layout>
	<c:if test="<%= Validator.isNotNull(affectedEntriesCount) && (affectedEntriesCount.length > 0) %>">
		<liferay-ui:success key="successDeleteEntry" message="the-entry-has-been-deleted-sucessfully" />
		<liferay-ui:success key="successDeleteEntries" message='<%= LanguageUtil.format(pageContext, "x-entries-have-been-deleted-sucessfully", affectedEntriesCount[0]) %>'/>
		<liferay-ui:success key="successRestoreEntry" message="the-entry-has-been-restored-sucessfully" />
		<liferay-ui:success key="successRestoreEntries" message='<%= LanguageUtil.format(pageContext, "x-entries-have-been-restored-sucessfully", affectedEntriesCount[0]) %>'/>

		<liferay-ui:error key="errorDeleteEntry" message="the-entry-has-not-been-deleted" />
		<liferay-ui:error key="errorDeleteEntries" message='<%= LanguageUtil.format(pageContext, "x-entries-have-not-been-deleted", affectedEntriesCount[1]) %>'/>
		<liferay-ui:error key="errorRestoreEntry" message="the-entry-has-not-been-restored" />
		<liferay-ui:error key="errorRestoreEntries" message='<%= LanguageUtil.format(pageContext, "x-entries-have-not-been-restored", affectedEntriesCount[1]) %>'/>
	</c:if>

	<portlet:actionURL var="editTrashEntryURL">
		<portlet:param name="struts_action" value="/trash/edit_entry" />
	</portlet:actionURL>

	<aui:form action='<%= editTrashEntryURL %>' method="post" name="fm" onSubmit="event.preventDefault();">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="" />
		<aui:input name="affectedEntryIds" type="hidden" />

		<c:if test="<%= trashEntriesCount > 0 %>">
			<aui:button-row>
				<aui:button name="deleteButton" onClick='<%= renderResponse.getNamespace() + "deleteEntries();" %>' value="delete" />

				<aui:button name="restoreButton" onClick='<%= renderResponse.getNamespace() + "restoreEntries();" %>' value="restore" />
			</aui:button-row>
		</c:if>

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

				AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(className);
				AssetRenderer assetRenderer = assetRendererFactory.getAssetRenderer(trashEntry.getClassPK());
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
	</aui:form>
</aui:layout>

<aui:script use="aui-base">
	var emptyTrashButton = A.one('#<portlet:namespace/>deleteButton');
	var restoreTrashButton = A.one('#<portlet:namespace/>restoreButton');
	var trashForm = A.one('#<portlet:namespace/>fm');
	var cmd = A.one('#<portlet:namespace/><%= Constants.CMD %>');
	var affectedEntryIds = A.one('#<portlet:namespace/>affectedEntryIds');

	Liferay.provide(
		window,
		'<portlet:namespace />deleteEntries',
		function() {
			var deleteEntryIds = Liferay.Util.listCheckedExcept(document.<portlet:namespace />fm, "<portlet:namespace />allRowIds");

			if (confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-delete-the-selected-entries") %>')) {
				trashForm.attr("method","post");
				cmd.val('<%= Constants.DELETE %>');
				affectedEntryIds.val(deleteEntryIds);
				submitForm(trashForm, "<portlet:actionURL><portlet:param name="struts_action" value="/trash/edit_entry" /></portlet:actionURL>");
			}
		},
		['liferay-util-list-fields']
	);

	Liferay.provide(
		window,
		'<portlet:namespace />restoreEntries',
		function() {
			var restoreEntryIds = Liferay.Util.listCheckedExcept(document.<portlet:namespace />fm, "<portlet:namespace />allRowIds");

			if (confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-restore-the-selected-entries") %>')) {
				trashForm.attr("method","post");
				cmd.val('<%= Constants.RESTORE %>');
				affectedEntryIds.val(Liferay.Util.listCheckedExcept(document.<portlet:namespace />fm, "<portlet:namespace />allRowIds"));
				submitForm(trashForm, "<portlet:actionURL><portlet:param name="struts_action" value="/trash/edit_entry" /></portlet:actionURL>");
			}
		},
		['liferay-util-list-fields']
	);
</aui:script>