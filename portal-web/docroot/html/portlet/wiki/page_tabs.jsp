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
WikiPage wikiPage = (WikiPage)request.getAttribute(WebKeys.WIKI_PAGE);

String title = wikiPage.getTitle();

PortletURL viewPageURL = renderResponse.createRenderURL();

viewPageURL.setParameter("struts_action", "/wiki/view");
viewPageURL.setParameter("nodeName", node.getName());
viewPageURL.setParameter("title", wikiPage.getTitle());

PortletURL editPageURL = renderResponse.createRenderURL();

editPageURL.setParameter("struts_action", "/wiki/edit_page");
editPageURL.setParameter("redirect", viewPageURL.toString());
editPageURL.setParameter("nodeId", String.valueOf(node.getNodeId()));
editPageURL.setParameter("title", title);

PortletURL viewPageDetailsURL = renderResponse.createRenderURL();

viewPageDetailsURL.setParameter("struts_action", "/wiki/view_page_details");
viewPageDetailsURL.setParameter("redirect", viewPageURL.toString());
viewPageDetailsURL.setParameter("nodeId", String.valueOf(node.getNodeId()));
viewPageDetailsURL.setParameter("title", wikiPage.getTitle());

PortletURL viewPageVersionsURL = PortletURLUtil.clone(viewPageDetailsURL, renderResponse);

viewPageVersionsURL.setParameter("struts_action", "/wiki/view_page_versions");

PortletURL viewPageHistoryURL = PortletURLUtil.clone(viewPageDetailsURL, renderResponse);

viewPageHistoryURL.setParameter("struts_action", "/wiki/view_page_history");

PortletURL viewPageIncomingLinksURL = PortletURLUtil.clone(viewPageDetailsURL, renderResponse);

viewPageIncomingLinksURL.setParameter("struts_action", "/wiki/view_page_incoming_links");

PortletURL viewPageOutgoingLinksURL = PortletURLUtil.clone(viewPageDetailsURL, renderResponse);

viewPageOutgoingLinksURL.setParameter("struts_action", "/wiki/view_page_outgoing_links");

PortletURL attachmentsURL = PortletURLUtil.clone(viewPageDetailsURL, renderResponse);

attachmentsURL.setParameter("struts_action", "/wiki/view_page_attachments");

%>

<%@ include file="/html/portlet/wiki/page_name.jspf" %>

<c:choose>
	<c:when test="<%= WikiPagePermission.contains(permissionChecker, wikiPage, ActionKeys.UPDATE) %>">
		<liferay-ui:tabs
			names="content,details,versions,history,incoming-links,outgoing-links,attachments"
			url0="<%= editPageURL.toString() %>"
			url1="<%= viewPageDetailsURL.toString() %>"
			url2="<%= viewPageVersionsURL.toString() %>"
			url3="<%= viewPageHistoryURL.toString() %>"
			url4="<%= viewPageIncomingLinksURL.toString() %>"
			url5="<%= viewPageOutgoingLinksURL.toString() %>"
			url6="<%= attachmentsURL.toString() %>"
		/>
	</c:when>
	<c:otherwise>
		<liferay-ui:tabs
			names="details,versions,history,incoming-links,outgoing-links"
			url0="<%= viewPageDetailsURL.toString() %>"
			url1="<%= viewPageVersionsURL.toString() %>"
			url2="<%= viewPageHistoryURL.toString() %>"
			url3="<%= viewPageIncomingLinksURL.toString() %>"
			url4="<%= viewPageOutgoingLinksURL.toString() %>"
			url5="<%= attachmentsURL.toString() %>"
		/>
	</c:otherwise>
</c:choose>