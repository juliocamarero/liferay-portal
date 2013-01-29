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
	<liferay-util:param name="tabs1" value="activities" />
</liferay-util:include>

<%
WikiNode node = (WikiNode)request.getAttribute(WebKeys.WIKI_NODE);
WikiPage wikiPage = (WikiPage)request.getAttribute(WebKeys.WIKI_PAGE);

PortletURL portletURL = renderResponse.createActionURL();

portletURL.setParameter("nodeId", String.valueOf(node.getNodeId()));
portletURL.setParameter("title", wikiPage.getTitle());

PortalUtil.addPortletBreadcrumbEntry(request, wikiPage.getTitle(), portletURL.toString());

portletURL.setParameter("struts_action", "/wiki/view_page_activities");
portletURL.setParameter("redirect", currentURL);
portletURL.setParameter("nodeId", String.valueOf(node.getNodeId()));
portletURL.setParameter("title", wikiPage.getTitle());

PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, "activities"), portletURL.toString());

String emptyResultsMessage = "this-page-does-not-have-any-activity";

PortletURL iteratorURL = renderResponse.createRenderURL();

iteratorURL.setParameter("struts_action", "/wiki/view_page_activities");
iteratorURL.setParameter("redirect", currentURL);
iteratorURL.setParameter("nodeId", String.valueOf(node.getNodeId()));

Map<Long,Integer> attachmentLastActivityMap = new HashMap<Long,Integer>();
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
		double version = 0;
		Date createDate = null;
		WikiPage activityWikiPage = null;

		if (Validator.isNotNull(activity.getExtraData())) {
			JSONObject extraDataJSONObject = JSONFactoryUtil.createJSONObject(HtmlUtil.unescape(activity.getExtraData()));

			if (isAttachmentActivity(activity.getType())){
				fileEntry = PortletFileRepositoryUtil.getPortletFileEntry(extraDataJSONObject.getLong("fileEntryId"));
				title = extraDataJSONObject.getString("title");

				if (TrashUtil.isInTrash(DLFileEntry.class.getName(), fileEntry.getFileEntryId())) {
					status = WorkflowConstants.STATUS_IN_TRASH;
				}
				
				Integer attachmentLastActivity = attachmentLastActivityMap.get(fileEntry.getFileEntryId());
				
				if (Validator.isNull(attachmentLastActivity)) {
					attachmentLastActivityMap.put(fileEntry.getFileEntryId(), activity.getType());
				}
								
			}
			else if (isPageActivity(activity.getType())) {
				version = extraDataJSONObject.getDouble("version");
				
				activityWikiPage = WikiPageLocalServiceUtil.getPage(node.getNodeId(), wikiPage.getTitle(), version);
			}
			
			createDate = new Date(activity.getCreateDate());
		}
		
		User activityUser = UserLocalServiceUtil.getUserById(activity.getUserId());
		
		Object[] titleArguments = new Object[] {activityUser.getFullName()};

		%>

		<c:choose>
			<c:when test="<%= isAttachmentActivity(activity.getType()) %>">
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
						<c:when test="<%= activity.getType() == SocialActivityConstants.TYPE_ADD_ATTACHMENT %>">
							<liferay-ui:icon
								image="add_article"
								label="<%= true %>"
								message='<%= themeDisplay.translate("activity-wiki-add-the-attachment", titleArguments) + ", " %>'
							/>
						</c:when>
						<c:when test="<%= activity.getType() == SocialActivityConstants.TYPE_MOVE_ATTACHMENT_TO_TRASH %>">
							<liferay-ui:icon
								image="trash"
								label="<%= true %>"
								message='<%= themeDisplay.translate("activity-wiki-remove-the-attachment", titleArguments) + ", " %>'
							/>
						</c:when>
						<c:otherwise>
							<liferay-ui:icon
								image="undo"
								label="<%= true %>"
								message='<%= themeDisplay.translate("activity-wiki-restore-the-attachment", titleArguments) + ", " %>'
							/>
						</c:otherwise>
					</c:choose>

					<aui:a href="<%= rowURL.toString() %>"><%= title %></aui:a>
				</liferay-ui:search-container-column-text>
			</c:when>

			<c:when test="<%= isPageActivity(activity.getType()) %>">
				<liferay-ui:search-container-column-text
					name="activity"
				>
					<portlet:renderURL var="rowURL">
						<portlet:param name="struts_action" value="/wiki/view" />
						<portlet:param name="nodeName" value="<%= node.getName() %>" />
						<portlet:param name="title" value="<%= activityWikiPage.getTitle() %>" />
						<portlet:param name="version" value="<%= String.valueOf(version) %>" />				
					</portlet:renderURL>

					<c:choose>
						<c:when test="<%= activity.getType() == WikiActivityKeys.ADD_PAGE %>">
							<liferay-ui:icon
								image="edit"
								message="edit"
							/>

							<%= themeDisplay.translate("activity-wiki-add-the-page", titleArguments) + ", " %>
							
							<aui:a href="<%= rowURL.toString() %>"><%= activityWikiPage.getTitle() %></aui:a>
						</c:when >
						<c:otherwise>
							<liferay-ui:icon
								image="edit"
								message="edit"
							/>

							<%= themeDisplay.translate("activity-wiki-update-the-page-to-version", titleArguments) + " " %>
							
							<aui:a href="<%= rowURL.toString() %>"><%= version %></aui:a>
							<c:if test="<%= Validator.isNotNull(activityWikiPage.getSummary()) %>">
								<br/>
								
								<%= LanguageUtil.get(pageContext, "summary") + ": " + activityWikiPage.getSummary() %>
								
							</c:if>
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
			<c:when test="<%= isAttachmentActivity(activity.getType()) && isAttachmentLastActivity(activity.getType(), fileEntry.getFileEntryId(), attachmentLastActivityMap) %>">

				<liferay-ui:search-container-column-jsp
					align="right"
					path="/html/portlet/wiki/page_activity_action_attachments.jsp"
				/>
			</c:when>
			<c:when test="<%= isPageActivity(activity.getType()) && !isPageLastActivity(activityWikiPage.getVersion(), wikiPage.getVersion()) %>">
				<liferay-ui:search-container-column-jsp
					align="right"
					path="/html/portlet/wiki/page_activity_action_pages.jsp"
				/>
			
			</c:when>
			<c:otherwise>
				<liferay-ui:search-container-column-text
					name="" value=" "
				/>
			</c:otherwise>
		</c:choose>
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator />
</liferay-ui:search-container>

<liferay-ui:restore-entry
	duplicateEntryAction="/wiki/restore_entry"
	overrideMessage="overwrite-the-existing-attachment-with-the-removed-one"
	renameMessage="keep-both-attachments-and-rename-the-removed-attachment-as"
	restoreEntryAction="/wiki/restore_page_attachment"
/>

<%!
public boolean isAttachmentActivity(int curActivityType) {
	if (curActivityType == SocialActivityConstants.TYPE_ADD_ATTACHMENT || 
		curActivityType == SocialActivityConstants.TYPE_MOVE_ATTACHMENT_TO_TRASH || 
		curActivityType == SocialActivityConstants.TYPE_RESTORE_ATTACHMENT_FROM_TRASH) {
		
		return true;
	}
	
	return false;
}

public boolean isPageActivity(int curActivityType) {
	if (curActivityType == WikiActivityKeys.UPDATE_PAGE || 
		curActivityType == WikiActivityKeys.ADD_PAGE) {
		
		return true;
	}
	
	return false;
}

public boolean isAttachmentLastActivity(int curActivityType, long fileEntryId, Map<Long,Integer> attachmentLastActivityMap) {
	if (curActivityType == attachmentLastActivityMap.get(fileEntryId)) {
		attachmentLastActivityMap.put(fileEntryId,-1);	
		return true;
	}
	
	return false;
}

public boolean isPageLastActivity(double curActivityVersion, double lastPageVersion) {
	if (curActivityVersion == lastPageVersion) {
		return true;
	}
	
	return false;
}

%>