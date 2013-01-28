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

<liferay-util:include page="/html/portlet/wiki/top_links.jsp" />

<liferay-util:include page="/html/portlet/wiki/page_tabs.jsp">
	<liferay-util:param name="tabs1" value="history" />
</liferay-util:include>

<%
WikiNode node = (WikiNode)request.getAttribute(WebKeys.WIKI_NODE);
WikiPage wikiPage = (WikiPage)request.getAttribute(WebKeys.WIKI_PAGE);

PortletURL portletURL = renderResponse.createActionURL();

portletURL.setParameter("nodeId", String.valueOf(node.getNodeId()));
portletURL.setParameter("title", wikiPage.getTitle());

PortalUtil.addPortletBreadcrumbEntry(request, wikiPage.getTitle(), portletURL.toString());

portletURL.setParameter("struts_action", "/wiki/view_page_history");
portletURL.setParameter("redirect", currentURL);
portletURL.setParameter("nodeId", String.valueOf(node.getNodeId()));
portletURL.setParameter("title", wikiPage.getTitle());

PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, "history"), portletURL.toString());

String emptyResultsMessage = "this-page-does-not-have-any-activity";

PortletURL iteratorURL = renderResponse.createRenderURL();

iteratorURL.setParameter("struts_action", "/wiki/view_page_history");
iteratorURL.setParameter("redirect", currentURL);
iteratorURL.setParameter("nodeId", String.valueOf(node.getNodeId()));

Map<Long,Integer> lastFileStatusMap = new HashMap<Long,Integer>();
%>

<liferay-ui:search-container
	emptyResultsMessage="<%= emptyResultsMessage %>"
	iteratorURL="<%= iteratorURL %>"
>

	<liferay-ui:search-container-results
		results="<%= SocialActivityLocalServiceUtil.getActivities(0, WikiPage.class.getName(), wikiPage.getResourcePrimKey(), searchContainer.getStart(), searchContainer.getEnd()) %>"
		total="<%= SocialActivityLocalServiceUtil.getActivitiesCount(0, WikiPage.class.getName(), wikiPage.getResourcePrimKey()) %>"
	/>

	<liferay-ui:search-container-row
		className="com.liferay.portlet.social.model.SocialActivity"
		escapedModel="<%= true %>"
		keyProperty="activityId"
		modelVar="activity"
		rowVar="row"
	>

		<%
		String title = null;
		int status = WorkflowConstants.STATUS_APPROVED;
		FileEntry fileEntry = null;
		TrashEntry trashEntry = null;
		double version = 0;	
		Date createDate = null;

		if(Validator.isNotNull(activity.getExtraData())) {

			JSONObject extraDataJSONObject = JSONFactoryUtil.createJSONObject(HtmlUtil.unescape(activity.getExtraData()));

			if (activity.getType() == SocialActivityConstants.TYPE_ADD_ATTACHMENT || activity.getType() == SocialActivityConstants.TYPE_MOVE_TO_TRASH || activity.getType() == SocialActivityConstants.TYPE_RESTORE_FROM_TRASH) {

				long fileEntryId = extraDataJSONObject.getLong("fileEntryId");
				title = extraDataJSONObject.getString("title");

				fileEntry = PortletFileRepositoryUtil.getPortletFileEntry(fileEntryId);

				if(TrashUtil.isInTrash("com.liferay.portlet.documentlibrary.model.DLFileEntry", fileEntry.getFileEntryId())){
					trashEntry = TrashEntryLocalServiceUtil.getEntry(DLFileEntry.class.getName(), fileEntry.getFileEntryId());
					status = WorkflowConstants.STATUS_IN_TRASH;
				}
				
				Integer lastFileStatus = lastFileStatusMap.get(fileEntryId);
				
				if (Validator.isNull(lastFileStatus)) {
					lastFileStatusMap.put(fileEntryId, activity.getType());
				}
								
			}
			else if (activity.getType() == WikiActivityKeys.UPDATE_PAGE || activity.getType() == WikiActivityKeys.ADD_PAGE) {
				version = extraDataJSONObject.getDouble("version");
				
				wikiPage = WikiPageLocalServiceUtil.getPage(node.getNodeId(), wikiPage.getTitle(), version);
			}
			
			createDate = new Date(activity.getCreateDate());
		}

		SocialActivityFeedEntry feedEntry = SocialActivityInterpreterLocalServiceUtil.interpret(activity, themeDisplay);
		%>
		<c:if test="<%= Validator.isNotNull(feedEntry) %>">
			<c:choose>
				<c:when test="<%= activity.getType() == SocialActivityConstants.TYPE_MOVE_TO_TRASH || activity.getType() == SocialActivityConstants.TYPE_ADD_ATTACHMENT || activity.getType() == SocialActivityConstants.TYPE_RESTORE_FROM_TRASH %>">
	
					<liferay-portlet:actionURL varImpl="rowURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
						<portlet:param name="struts_action" value="/wiki/get_page_attachment" />
						<portlet:param name="redirect" value="<%= currentURL %>" />
						<portlet:param name="nodeId" value="<%= String.valueOf(node.getNodeId()) %>" />
						<portlet:param name="title" value="<%= wikiPage.getTitle() %>" />
						<portlet:param name="fileName" value="<%= fileEntry.getTitle() %>" />
						<portlet:param name="status" value="<%= String.valueOf(status) %>" />
					</liferay-portlet:actionURL>
	
					<liferay-ui:search-container-column-text
						name="activity"
					>
						<c:choose>
							<c:when test="<%= activity.getType() == SocialActivityConstants.TYPE_MOVE_TO_TRASH %>">
								<liferay-ui:icon
									image="trash"
									message="trash"
								/>
							</c:when>
							<c:when test="<%= activity.getType() == SocialActivityConstants.TYPE_RESTORE_FROM_TRASH %>">
								<liferay-ui:icon
									image="undo"
									message="undo"
								/>
							</c:when>
							<c:otherwise>
								<liferay-ui:icon
									image="add_article"
									message="add_article"
								/>
							</c:otherwise>
						</c:choose>
						<%= feedEntry.getTitle() %>
						<aui:a href="<%= rowURL.toString() %>"><%= title %></aui:a>
	
					</liferay-ui:search-container-column-text>
	
				</c:when>
				<c:when test="<%= activity.getType() == WikiActivityKeys.UPDATE_PAGE || activity.getType() == WikiActivityKeys.ADD_PAGE %>">
					<liferay-ui:search-container-column-text
						name="activity"
					>
						<portlet:renderURL var="rowURL">
							<portlet:param name="struts_action" value="/wiki/view" />
							<portlet:param name="nodeName" value="<%= node.getName() %>" />
							<portlet:param name="title" value="<%= wikiPage.getTitle() %>" />
							<portlet:param name="version" value="<%= String.valueOf(version) %>" />				
						</portlet:renderURL>
	
						<c:choose>
							<c:when test="<%= activity.getType() == WikiActivityKeys.UPDATE_PAGE %>">
								<liferay-ui:icon
									image="edit"
									message="edit"
								/>
	
								<%= feedEntry.getTitle() %>
								<aui:a href="<%= rowURL.toString() %>"><%= version %></aui:a>
								<c:if test="<%= Validator.isNotNull(wikiPage.getSummary()) %>">
									<br/>
									<%= LanguageUtil.get(pageContext, "summary") + ": " + wikiPage.getSummary() %>
								</c:if>
							</c:when>
							<c:otherwise>
								<liferay-ui:icon
									image="edit"
									message="edit"
								/>
	
								<%= feedEntry.getTitle() %>
								<aui:a href="<%= rowURL.toString() %>"><%= wikiPage.getTitle() %></aui:a>
							</c:otherwise>
						</c:choose>
					</liferay-ui:search-container-column-text>
				</c:when>
			</c:choose>
	
			<liferay-ui:search-container-column-text
				name="date"
			>
				<liferay-ui:message arguments="<%= LanguageUtil.getTimeDescription(pageContext, System.currentTimeMillis() - createDate.getTime(), true) %>" key="x-ago" />
			
			</liferay-ui:search-container-column-text>
			<c:choose>
				<c:when test="<%= (activity.getType() == SocialActivityConstants.TYPE_MOVE_TO_TRASH || activity.getType() == SocialActivityConstants.TYPE_ADD_ATTACHMENT || activity.getType() == SocialActivityConstants.TYPE_RESTORE_FROM_TRASH) && lastFileStatusMap.get(fileEntry.getFileEntryId()) == activity.getType() %>">

					<%
					lastFileStatusMap.put(fileEntry.getFileEntryId(),-1);			
					%>

					<liferay-ui:search-container-column-jsp
						align="right"
						path="/html/portlet/wiki/page_history_action_attachments.jsp"
					/>
				</c:when>
				<c:when test="<%= (activity.getType() == WikiActivityKeys.UPDATE_PAGE || activity.getType() == WikiActivityKeys.ADD_PAGE) && wikiPage.getVersion() == version %>">
					<liferay-ui:search-container-column-jsp
						align="right"
						path="/html/portlet/wiki/page_history_action_pages.jsp"
					/>
				
				</c:when>
				<c:otherwise>
					<liferay-ui:search-container-column-text
						name="" value=" "
					/>
				</c:otherwise>
			</c:choose>
		</c:if>

	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator />
</liferay-ui:search-container>

<liferay-ui:restore-entry
	duplicateEntryAction="/wiki/restore_entry"
	overrideMessage="overwrite-the-existing-attachment-with-the-removed-one"
	renameMessage="keep-both-attachments-and-rename-the-removed-attachment-as"
	restoreEntryAction="/wiki/restore_page_attachment"
/>