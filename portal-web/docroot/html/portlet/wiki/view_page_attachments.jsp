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

<%@ include file="/html/portlet/wiki/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

boolean viewTrashAttachments = ParamUtil.getBoolean(request, "viewTrashAttachments");

if (!TrashUtil.isTrashEnabled(scopeGroupId)) {
	viewTrashAttachments = false;
}

WikiNode node = (WikiNode)request.getAttribute(WebKeys.WIKI_NODE);
WikiPage wikiPage = (WikiPage)request.getAttribute(WebKeys.WIKI_PAGE);

PortletURL portletURL = renderResponse.createActionURL();

portletURL.setParameter("nodeId", String.valueOf(node.getNodeId()));
portletURL.setParameter("title", wikiPage.getTitle());

portletURL.setParameter("struts_action", "/wiki/view");

PortalUtil.addPortletBreadcrumbEntry(request, wikiPage.getTitle(), portletURL.toString());

portletURL.setParameter("struts_action", "/wiki/view_page_attachments");

PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, "attachments"), portletURL.toString());
%>

<c:if test="<%= !viewTrashAttachments %>">
	<portlet:actionURL var="undoTrashURL">
		<portlet:param name="struts_action" value="/wiki/edit_page_attachment" />
		<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.RESTORE %>" />
	</portlet:actionURL>

	<liferay-ui:trash-undo portletURL="<%= undoTrashURL %>" />
</c:if>

<liferay-util:include page="/html/portlet/wiki/top_links.jsp" />

<liferay-util:include page="/html/portlet/wiki/page_tabs.jsp">
	<liferay-util:param name="tabs1" value="attachments" />
</liferay-util:include>

<c:if test="<%= viewTrashAttachments %>">
	<liferay-ui:header
		backURL="<%= redirect %>"
		title="removed-attachments"
	/>
</c:if>

<%
String emptyResultsMessage = null;

if (viewTrashAttachments) {
	emptyResultsMessage = "this-page-does-not-have-file-attachments-in-the-recycle-bin";
}
else {
	emptyResultsMessage = "this-page-does-not-have-file-attachments";
}

int status = WorkflowConstants.STATUS_APPROVED;

if (viewTrashAttachments) {
	status = WorkflowConstants.STATUS_IN_TRASH;
}

PortletURL iteratorURL = renderResponse.createRenderURL();

iteratorURL.setParameter("struts_action", "/wiki/view_page_attachments");
iteratorURL.setParameter("redirect", currentURL);
iteratorURL.setParameter("nodeId", String.valueOf(node.getNodeId()));
iteratorURL.setParameter("viewTrashAttachments", String.valueOf(viewTrashAttachments));
%>

<c:if test="<%= WikiNodePermission.contains(permissionChecker, node.getNodeId(), ActionKeys.ADD_ATTACHMENT) %>">
	<c:choose>
		<c:when test="<%= viewTrashAttachments %>">
			<portlet:actionURL var="emptyTrashURL">
				<portlet:param name="struts_action" value="/wiki/edit_page_attachment" />
				<portlet:param name="nodeId" value="<%= String.valueOf(node.getPrimaryKey()) %>" />
				<portlet:param name="title" value="<%= wikiPage.getTitle() %>" />
			</portlet:actionURL>

			<liferay-ui:trash-empty
				confirmMessage="are-you-sure-you-want-to-remove-the-attachments-for-this-page"
				emptyMessage="remove-the-attachments-for-this-page"
				infoMessage="attachments-that-have-been-removed-for-more-than-x-days-will-be-automatically-deleted"
				portletURL="<%= emptyTrashURL.toString() %>"
				totalEntries="<%= wikiPage.getDeletedAttachmentsFileEntriesCount() %>"
			/>
		</c:when>
		<c:otherwise>

			<%
			int deletedAttachmentsSize = wikiPage.getDeletedAttachmentsFileEntriesCount();
			%>

			<c:if test="<%= TrashUtil.isTrashEnabled(scopeGroupId) && (deletedAttachmentsSize > 0) %>">
				<portlet:renderURL var="viewTrashAttachmentsURL">
					<portlet:param name="struts_action" value="/wiki/view_page_attachments" />
					<portlet:param name="tabs1" value="attachments" />
					<portlet:param name="redirect" value="<%= currentURL %>" />
					<portlet:param name="nodeId" value="<%= String.valueOf(node.getNodeId()) %>" />
					<portlet:param name="title" value="<%= wikiPage.getTitle() %>" />
					<portlet:param name="viewTrashAttachments" value="<%= Boolean.TRUE.toString() %>" />
				</portlet:renderURL>

				<liferay-ui:icon
					cssClass="trash-attachments"
					image="delete"
					label="<%= true %>"
					message='<%= LanguageUtil.format(pageContext, (deletedAttachmentsSize == 1) ? "x-recently-removed-attachment" : "x-recently-removed-attachments", deletedAttachmentsSize) %>'
					url="<%= viewTrashAttachmentsURL %>"
				/>
			</c:if>

			<div>
				<input type="button" value="<liferay-ui:message key="add-attachments" />" onClick="location.href = '<portlet:renderURL><portlet:param name="struts_action" value="/wiki/edit_page_attachment" /><portlet:param name="nodeId" value="<%= String.valueOf(node.getNodeId()) %>" /><portlet:param name="title" value="<%= wikiPage.getTitle() %>" /><portlet:param name="redirect" value="<%= currentURL %>" /></portlet:renderURL>';" />
			</div>
		</c:otherwise>
	</c:choose>

	<br />
</c:if>

<liferay-ui:search-container
	emptyResultsMessage="<%= emptyResultsMessage %>"
	iteratorURL="<%= iteratorURL %>"
>
	<liferay-ui:search-container-results
		results="<%= PortletFileRepositoryUtil.getPortletFileEntries(themeDisplay.getScopeGroupId(), wikiPage.getAttachmentsFolderId(), status, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator()) %>"
		total="<%= PortletFileRepositoryUtil.getPortletFileEntriesCount(themeDisplay.getScopeGroupId(), wikiPage.getAttachmentsFolderId(), status) %>"
	/>

	<liferay-ui:search-container-row
		className="com.liferay.portal.kernel.repository.model.FileEntry"
		escapedModel="<%= true %>"
		keyProperty="fileEntryId"
		modelVar="fileEntry"
		rowVar="row"
	>

		<%
		DLFileEntry dlFileEntry = (DLFileEntry)fileEntry.getModel();
		%>

		<liferay-portlet:actionURL varImpl="rowURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
			<portlet:param name="struts_action" value="/wiki/get_page_attachment" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="nodeId" value="<%= String.valueOf(node.getNodeId()) %>" />
			<portlet:param name="title" value="<%= wikiPage.getTitle() %>" />
			<portlet:param name="fileName" value="<%= dlFileEntry.getTitle() %>" />
			<portlet:param name="status" value="<%= String.valueOf(status) %>" />
		</liferay-portlet:actionURL>

		<%
		String fileName = dlFileEntry.getTitle();

		if (viewTrashAttachments) {
			fileName = TrashUtil.stripTrashNamespace(fileName);
		}
		%>

		<liferay-ui:search-container-column-text
			href="<%= rowURL %>"
			name="file-name"
		>
			<img align="left" border="0" src="<%= themeDisplay.getPathThemeImages() %>/file_system/small/<%= DLUtil.getFileIcon(dlFileEntry.getExtension()) %>.png"> <%= fileName %>
		</liferay-ui:search-container-column-text>

		<liferay-ui:search-container-column-text
			href="<%= rowURL %>"
			name="size"
			value="<%= TextFormatter.formatStorageSize(dlFileEntry.getSize(), locale) %>"
		/>

		<liferay-ui:search-container-column-jsp
			align="right"
			path="/html/portlet/wiki/page_attachment_action.jsp"
		/>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator />
</liferay-ui:search-container>

<aui:script use="liferay-restore-entry">
	<portlet:actionURL var="restoreEntryURL">
		<portlet:param name="struts_action" value="/wiki/restore_page_attachment" />
		<portlet:param name="redirect" value="<%= redirect %>" />
	</portlet:actionURL>

	new Liferay.RestoreEntry(
		{
			checkEntryURL: '<portlet:actionURL><portlet:param name="<%= Constants.CMD %>" value="<%= Constants.CHECK %>" /><portlet:param name="struts_action" value="/wiki/restore_page_attachment" /></portlet:actionURL>',
			namespace: '<portlet:namespace />',
			restoreEntryURL: '<portlet:renderURL windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>"><portlet:param name="struts_action" value="/wiki/restore_entry" /><portlet:param name="redirect" value="<%= currentURL %>" /><portlet:param name="restoreEntryURL" value="<%= restoreEntryURL %>" /></portlet:renderURL>'
		}
	);
</aui:script>