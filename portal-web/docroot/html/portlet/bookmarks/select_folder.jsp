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

<%@ include file="/html/portlet/bookmarks/init.jsp" %>

<%
BookmarksFolder folder = (BookmarksFolder)request.getAttribute(WebKeys.BOOKMARKS_FOLDER);

long folderId = BeanParamUtil.getLong(folder, request, "folderId", BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID);

String folderName = LanguageUtil.get(pageContext, "home");

if (folder != null) {
	folderName = folder.getName();

	BookmarksUtil.addPortletBreadcrumbEntries(folder, request, renderResponse);
}

String eventName = ParamUtil.getString(request, "eventName", "selectFolder");
%>

<aui:form method="post" name="selectFolderFm">
	<liferay-ui:header
		title="home"
	/>

	<liferay-ui:breadcrumb showGuestGroup="<%= false %>" showLayout="<%= false %>" showParentGroups="<%= false %>" />

	<%
	boolean showAddFolderButton = BookmarksFolderPermission.contains(permissionChecker, scopeGroupId, folderId, ActionKeys.ADD_FOLDER);
	%>

	<aui:button-row>
		<c:if test="<%= showAddFolderButton %>">
			<portlet:renderURL var="editFolderURL">
				<portlet:param name="struts_action" value="/bookmarks/edit_folder" />
				<portlet:param name="redirect" value="<%= currentURL %>" />
				<portlet:param name="parentFolderId" value="<%= String.valueOf(folderId) %>" />
			</portlet:renderURL>

			<aui:button href="<%= editFolderURL %>" value='<%= (folder == null) ? "add-folder" : "add-subfolder" %>' />
		</c:if>

		<%
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("folderid", folderId);
		data.put("name", HtmlUtil.escape(folderName));
		%>

		<aui:button cssClass="selector-button"  data="<%= data %>" value="choose-this-folder" />
	</aui:button-row>

	<%
	PortletURL portletURL = renderResponse.createRenderURL();

	portletURL.setParameter("struts_action", "/bookmarks/select_folder");
	portletURL.setParameter("folderId", String.valueOf(folderId));
	portletURL.setParameter("eventName", eventName);

	int bookmarksFoldersCount = BookmarksFolderServiceUtil.getFoldersCount(scopeGroupId, folderId);
	%>

	<c:if test="<%= bookmarksFoldersCount > 0 %>">
		<br />

		<liferay-ui:search-container iteratorURL="<%= portletURL %>">
			<liferay-ui:search-container-results
				results="<%= BookmarksFolderServiceUtil.getFolders(scopeGroupId, folderId, searchContainer.getStart(), searchContainer.getEnd()) %>"
				total="<%= bookmarksFoldersCount %>"
			/>

			<liferay-ui:search-container-row className="com.liferay.portlet.bookmarks.model.BookmarksFolder" modelVar="curFolder">
				<portlet:renderURL var="viewFolderURL">
					<portlet:param name="struts_action" value="/bookmarks/select_folder" />
					<portlet:param name="folderId" value="<%= String.valueOf(curFolder.getFolderId()) %>" />
				</portlet:renderURL>

				<%
				List subfolderIds = new ArrayList();

				subfolderIds.add(new Long(curFolder.getFolderId()));

				BookmarksFolderServiceUtil.getSubfolderIds(subfolderIds, scopeGroupId, curFolder.getFolderId());

				int foldersCount = subfolderIds.size() - 1;
				int entriesCount = BookmarksEntryServiceUtil.getFoldersEntriesCount(scopeGroupId, subfolderIds);
				%>

				<liferay-ui:search-container-column-text href="<%= viewFolderURL %>" name="folder" value="<%= curFolder.getName() %>" />

				<liferay-ui:search-container-column-text href="<%= viewFolderURL %>" name="num-of-folders" value="<%= String.valueOf(foldersCount) %>" />

				<liferay-ui:search-container-column-text href="<%= viewFolderURL %>" name="num-of-entries" value="<%= String.valueOf(entriesCount) %>" />

				<liferay-ui:search-container-column-text>

					<%
					Map<String, Object> data = new HashMap<String, Object>();

					data.put("folderid", curFolder.getFolderId());
					data.put("name", HtmlUtil.escape(curFolder.getName()));
					%>

					<aui:button cssClass="selector-button right" data="<%= data %>" value="choose" />
				</liferay-ui:search-container-column-text>

			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator />
		</liferay-ui:search-container>
	</c:if>

</aui:form>
<aui:script use="aui-base">
	var Util = Liferay.Util;

	A.one('#<portlet:namespace />selectFolderFm').delegate(
		'click',
		function(event) {
			var result = Util.getAttributes(event.currentTarget, 'data-');

			Util.getOpener().Liferay.fire('<portlet:namespace /><%= eventName %>', result);

			Util.getWindow().close();
		},
		'.selector-button input'
	);
</aui:script>