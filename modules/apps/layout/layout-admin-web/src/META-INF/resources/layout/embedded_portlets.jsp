<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

<%@ include file="/init.jsp" %>

<%
Group selGroup = (Group)request.getAttribute(WebKeys.GROUP);

Layout selLayout = layoutsAdminDisplayContext.getSelLayout();

List<Portlet> embeddedPortlets = new ArrayList<Portlet>();

if (selLayout.isSupportsEmbeddedPortlets()) {
	LayoutTypePortlet selLayoutTypePortlet = (LayoutTypePortlet)selLayout.getLayoutType();

	List<String> portletIds = selLayoutTypePortlet.getPortletIds();

	for (Portlet portlet : selLayoutTypePortlet.getAllPortlets(false)) {
		if (!portletIds.contains(portlet.getPortletId())) {
			embeddedPortlets.add(portlet);
		}
	}
}

RowChecker rowChecker = new RowChecker(liferayPortletResponse);

rowChecker.setRowIds("removeEmbeddedPortletIds");

boolean showDeleteButton = !embeddedPortlets.isEmpty() && selGroup.hasLocalOrRemoteStagingGroup() && GroupPermissionUtil.contains(permissionChecker, layoutsAdminDisplayContext.getStagingGroup(), ActionKeys.UPDATE);
%>

<div id="<portlet:namespace />embeddedPortlets">
	<h3><liferay-ui:message key="embedded-portlets" /></h3>

	<c:choose>
		<c:when test="<%= selLayout.isLayoutPrototypeLinkActive() %>">

			<%
			rowChecker = null;
			%>

			<div class="alert alert-info">
				<liferay-ui:message key="layout-inherits-from-a-prototype-portlets-cannot-be-manipulated" />
			</div>
		</c:when>
		<c:otherwise>
			<div class="alert alert-warning">
				<liferay-ui:message key="warning-preferences-of-selected-portlets-will-be-reset-or-deleted" />
			</div>
		</c:otherwise>
	</c:choose>

	<liferay-ui:search-container
		deltaConfigurable="<%= false %>"
		emptyResultsMessage="there-are-no-embedded-portlets-in-this-page"
		rowChecker="<%= rowChecker %>"
	>
		<liferay-ui:search-container-results results="<%= embeddedPortlets %>" />

		<liferay-ui:search-container-row
			className="com.liferay.portal.model.Portlet"
			escapedModel="<%= true %>"
			keyProperty="portletId"
			modelVar="portlet"
		>
			<liferay-ui:search-container-column-text
				name="portlet-id"
				property="portletId"
			/>

			<liferay-ui:search-container-column-text
				name="title"
			>
				<%= PortalUtil.getPortletTitle(portlet, application, locale) %>
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				name="status"
			>
				<c:choose>
					<c:when test="<%= !portlet.isActive() %>">
						<liferay-ui:message key="inactive" />
					</c:when>
					<c:when test="<%= !portlet.isReady() %>">
						<liferay-ui:message arguments="portlet" key="is-not-ready" />
					</c:when>
					<c:when test="<%= portlet.isUndeployedPortlet() %>">
						<liferay-ui:message key="undeployed" />
					</c:when>
					<c:otherwise>
						<liferay-ui:message key="active" />
					</c:otherwise>
				</c:choose>
			</liferay-ui:search-container-column-text>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator type="none" />
	</liferay-ui:search-container>

	<c:if test="<%= showDeleteButton %>">
		<aui:button data-actionname="deleteEmbeddedPortlets" name="delete" onClick='<%= renderResponse.getNamespace() + "deleteEmbeddedPortlets();" %>' value="delete" />

		<portlet:actionURL name="deleteEmbeddedPortlets" var="deleteEmbeddedPortletsURL">
			<portlet:param name="mvcPath" value="/layout/embedded_portlets.jsp" />
		</portlet:actionURL>

		<aui:script use="aui-io-request,aui-parse-content">
			function <portlet:namespace />deleteEmbeddedPortlets() {

				var form = A.one('#<portlet:namespace />fm');

				var embeddedPortletsNode = form.get('#<portlet:namespace />embeddedPortlets');

				embeddedPortletsNode.plug(A.Plugin.ParseContent);

				form.on(
					'submit',
					function(event) {
						A.io.request(
							'<%=deleteEmbeddedPortletsURL%>',
							{
								form: {
									id: form
								},
								on: {
									success: function(event, id, obj) {
										var responseData = this.get('responseData');

										embeddedPortletsNode.setContent(responseData);
									}
								}
							}
						);

						event.halt();
					}
				);
			}
		</aui:script>
	</c:if>
</div>