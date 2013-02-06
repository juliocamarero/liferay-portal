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
WikiPage wikiPage = (WikiPage)request.getAttribute(WebKeys.WIKI_PAGE);

double sourceVersion = GetterUtil.getDouble((String) request.getAttribute(WebKeys.WIKI_PAGE_SOURCE_VERSION));

%>
<liferay-ui:search-container
	id="wikiPageVersionSearchContainer"
>
	<liferay-ui:search-container-results
		results="<%= WikiPageLocalServiceUtil.getPages(wikiPage.getNodeId(), wikiPage.getTitle(), searchContainer.getStart(), searchContainer.getEnd(), new PageVersionComparator()) %>"
		total="<%= WikiPageLocalServiceUtil.getPagesCount(wikiPage.getNodeId(), wikiPage.getTitle()) %>"
	/>
	
	<liferay-ui:search-container-row
		className="com.liferay.portlet.wiki.model.WikiPage"
		modelVar="curWikiPage"
	>
		<liferay-ui:search-container-column-text
			name="version"
			value="<%= String.valueOf(curWikiPage.getVersion()) %>"
		/>
		
		<liferay-ui:search-container-column-text
			name="date"
			value='<%= LanguageUtil.format(pageContext, "x-ago", LanguageUtil.getTimeDescription(pageContext, System.currentTimeMillis() - curWikiPage.getModifiedDate().getTime(), true)) %>'
		/>
	
		<c:choose>
			<c:when test="<%= sourceVersion != curWikiPage.getVersion() %>">
				<liferay-ui:search-container-column-text
					name=""
				>
					<aui:button data-sourceversion="<%= sourceVersion %>" data-targetversion="<%= curWikiPage.getVersion() %>" href="javascript:;" value="choose" cssClass="select-wiki-page-version"/>
 				</liferay-ui:search-container-column-text>
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

<aui:script use="liferay-portlet-url,liferay-search-container">
var searchContainer = Liferay.SearchContainer.get('<portlet:namespace />wikiPageVersionSearchContainer');

searchContainer.get('contentBox').delegate(
	'click',
	function(event) {
		var link = event.currentTarget;
		
		var sourceVersion = parseFloat(link.getAttribute('data-sourceversion'));
		var targetVersion = parseFloat(link.getAttribute('data-targetversion'));
		
		if(targetVersion < sourceVersion) {
			var tempVersion = targetVersion;
			
			targetVersion = sourceVersion;
			sourceVersion = tempVersion;
		}
	
		var redirect = Liferay.PortletURL.createRenderURL();
		
		redirect.setPortletId(36);
		
		redirect.setParameter('struts_action', '/wiki/view_page_activities');
		redirect.setParameter('nodeId', '<%= wikiPage.getNode().getNodeId() %>');
		redirect.setParameter("title", '<%= wikiPage.getTitle() %>');

		var portletURL = Liferay.PortletURL.createRenderURL();

		portletURL.setPortletId(36);

		portletURL.setParameter('struts_action', '/wiki/compare_versions');
		portletURL.setParameter('redirect', redirect);
		portletURL.setParameter('nodeId', '<%= wikiPage.getNode().getNodeId() %>');
		portletURL.setParameter('title', '<%= wikiPage.getTitle() %>');
		portletURL.setParameter('sourceVersion', sourceVersion);
		portletURL.setParameter('targetVersion', targetVersion);
		portletURL.setParameter('type', 'html');
		
		Liferay.Util.getOpener().location.href = portletURL.toString();
	},
	'.select-wiki-page-version .aui-button-input'
);

</aui:script>
