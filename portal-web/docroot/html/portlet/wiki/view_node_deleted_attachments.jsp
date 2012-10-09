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

WikiNode node = (WikiNode)request.getAttribute(WebKeys.WIKI_NODE);

List<DLFileEntry> attachments = node.getDeletedAttachmentsFiles();

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/wiki/view_all_pages");
portletURL.setParameter("redirect", currentURL);
portletURL.setParameter("nodeId", String.valueOf(node.getNodeId()));

PortalUtil.addPortletBreadcrumbEntry(request, node.getName(), portletURL.toString());

portletURL.setParameter("struts_action", "/wiki/view_node_deleted_attachments");

PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, "attachments-recycle-bin"), portletURL.toString());

PortletURL iteratorURL = renderResponse.createRenderURL();

iteratorURL.setParameter("struts_action", "/wiki/view_node_deleted_attachments");
iteratorURL.setParameter("redirect", currentURL);
iteratorURL.setParameter("nodeId", String.valueOf(node.getNodeId()));
iteratorURL.setParameter("viewTrashAttachments", Boolean.TRUE.toString());
%>

<liferay-ui:header
	backURL="<%= redirect %>"
	title="removed-attachments"
/>

<portlet:actionURL var="emptyTrashURL">
	<portlet:param name="struts_action" value="/wiki/edit_node_attachment" />
	<portlet:param name="nodeId" value="<%= String.valueOf(node.getPrimaryKey()) %>" />
</portlet:actionURL>

<liferay-ui:trash-empty
	confirmMessage="are-you-sure-you-want-to-remove-the-attachments-for-this-wiki-node"
	emptyMessage="remove-the-attachments-for-this-wiki-node"
	infoMessage="attachments-that-have-been-removed-for-more-than-x-days-will-be-automatically-deleted"
	portletURL="<%= emptyTrashURL.toString() %>"
	totalEntries="<%= attachments.size() %>"
/>

<liferay-ui:search-container
	emptyResultsMessage="this-wiki-node-does-not-have-file-attachments-in-the-recycle-bin"
	iteratorURL="<%= iteratorURL %>"
>
	<liferay-ui:search-container-results
		results="<%= ListUtil.subList(attachments, searchContainer.getStart(), searchContainer.getEnd()) %>"
		total="<%= attachments.size() %>"
	/>

	<liferay-ui:search-container-row
		className="com.liferay.portlet.documentlibrary.model.DLFileEntry"
		modelVar="dlFileEntry"
	>

		<%
		DLFolder dlFolder = DLFolderLocalServiceUtil.getDLFolder(dlFileEntry.getFolderId());

		long resourcePrimKey = GetterUtil.getLong(dlFolder.getName());

		WikiPage wikiPage = WikiPageLocalServiceUtil.getPage(resourcePrimKey);
		%>

		<liferay-portlet:actionURL varImpl="rowURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
			<portlet:param name="struts_action" value="/wiki/get_page_attachment" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="nodeId" value="<%= String.valueOf(node.getNodeId()) %>" />
			<portlet:param name="title" value="<%= wikiPage.getTitle() %>" />
			<portlet:param name="fileName" value="<%= dlFileEntry.getTitle() %>" />
		</liferay-portlet:actionURL>

		<liferay-ui:search-container-column-text
			href="<%= rowURL %>"
			name="file-name"
		>
			<img align="left" border="0" src="<%= themeDisplay.getPathThemeImages() %>/file_system/small/<%= DLUtil.getFileIcon(dlFileEntry.getExtension()) %>.png"> <%= TrashUtil.stripTrashNamespace(dlFileEntry.getTitle()) %>
		</liferay-ui:search-container-column-text>

		<liferay-ui:search-container-column-text
			href="<%= rowURL %>"
			name="page"
			value="<%= wikiPage.getTitle() %>"
		/>

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