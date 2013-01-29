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
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

com.liferay.portlet.social.model.SocialActivity activity = (com.liferay.portlet.social.model.SocialActivity)row.getObject();
		 
JSONObject extraDataJSONObject = JSONFactoryUtil.createJSONObject(HtmlUtil.unescape(activity.getExtraData()));
		 
double version = extraDataJSONObject.getDouble("version");

WikiPage wikiPage = (WikiPage)request.getAttribute(WebKeys.WIKI_PAGE);
%>

<liferay-ui:icon-menu>
	<c:if test="<%= (wikiPage.getStatus() == WorkflowConstants.STATUS_APPROVED) && WikiPagePermission.contains(permissionChecker, wikiPage, ActionKeys.UPDATE) %>">
		<portlet:actionURL var="revertURL">
			<portlet:param name="struts_action" value="/wiki/edit_page" />
			<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.REVERT %>" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="nodeId" value="<%= String.valueOf(wikiPage.getNodeId()) %>" />
			<portlet:param name="title" value="<%= HtmlUtil.unescape(wikiPage.getTitle()) %>" />
			<portlet:param name="version" value="<%= String.valueOf(version) %>" />
		</portlet:actionURL>

		<liferay-ui:icon
			image="undo"
			message='<%= LanguageUtil.get(pageContext, "restore-version") + " " + String.valueOf(version) %>'
			url="<%= revertURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>