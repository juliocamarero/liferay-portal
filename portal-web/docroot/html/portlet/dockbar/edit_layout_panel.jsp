<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

<%@ include file="/html/portlet/layouts_admin/init.jsp" %>

<%@ include file="/html/portlet/layouts_admin/init_attributes.jspf" %>

<div id="<portlet:namespace />editLayoutContainer">
	<c:choose>
		<c:when test='<%= SessionMessages.contains(renderRequest, "requestProcessed") || ((selPlid == 0) && Validator.isNotNull(closeRedirect)) %>'>

			<%
			String refreshURL = null;

			if ((selPlid == 0) && Validator.isNotNull(closeRedirect)) {
				refreshURL = closeRedirect;
			}
			else {
				refreshURL = (String)SessionMessages.get(renderRequest, portletDisplay.getId() + SessionMessages.KEY_SUFFIX_CLOSE_REDIRECT);
			}

			if (Validator.isNull(refreshURL)) {
				refreshURL = PortalUtil.getLayoutURL(layout, themeDisplay);
			}
			%>

			<aui:script>
				window.location.href = '<%= HtmlUtil.escapeJS(refreshURL) %>';
			</aui:script>
		</c:when>
		<c:otherwise>
			<aui:button cssClass="close pull-right" name="closePanelEdit" value="&times;" />

			<h1><liferay-ui:message key="edit-page" /></h1>

			<c:if test="<%= selPlid > 0 %>">
				<liferay-util:include page="/html/portlet/layouts_admin/edit_layout.jsp">
					<liferay-util:param name="displayStyle" value="panel" />
					<liferay-util:param name="showAddAction" value="<%= Boolean.FALSE.toString() %>" />
				</liferay-util:include>

				<c:if test="<%= themeDisplay.isShowSiteAdministrationIcon() %>">
					<liferay-portlet:renderURL plid="<%= PortalUtil.getControlPanelPlid(company.getCompanyId()) %>" portletName="<%= PortletKeys.GROUP_PAGES %>" varImpl="siteAdministrationURL" windowState="<%= WindowState.NORMAL.toString() %>">
						<portlet:param name="struts_action" value="/group_pages/edit_layouts" />
						<portlet:param name="tabs1" value="public-pages" />
						<portlet:param name="groupId" value="<%= String.valueOf(liveGroupId) %>" />
						<portlet:param name="selPlid" value="<%= String.valueOf(selPlid) %>" />
						<portlet:param name="treeId" value="layoutsTree" />
						<portlet:param name="viewLayout" value="true" />
					</liferay-portlet:renderURL>

					<%
					String siteAdministrationURLString = HttpUtil.setParameter(siteAdministrationURL.toString(), "controlPanelCategory", "current_site");

					siteAdministrationURLString = HttpUtil.setParameter(siteAdministrationURLString, "doAsGroupId", String.valueOf(liveGroupId));
					siteAdministrationURLString = HttpUtil.setParameter(siteAdministrationURLString, "refererPlid", String.valueOf(selPlid));
					%>

					<aui:a cssClass="site-admin-link" href="<%= siteAdministrationURLString %>" label="site-administration" />
				</c:if>
			</c:if>
		</c:otherwise>
	</c:choose>
</div>