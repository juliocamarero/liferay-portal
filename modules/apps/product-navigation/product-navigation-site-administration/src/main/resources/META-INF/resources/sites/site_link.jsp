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

<%@ include file="/sites/init.jsp" %>

<%
boolean privateLayout = (Boolean)request.getAttribute("my_sites.jsp-privateLayout");
boolean selectedSite = (Boolean)request.getAttribute("my_sites.jsp-selectedSite");
Group siteGroup = (Group)request.getAttribute("my_sites.jsp-siteGroup");
String siteName = GetterUtil.getString(request.getAttribute("my_sites.jsp-siteGroup"), siteGroup.getDescriptiveName(locale));
boolean showPrivateLabel = (Boolean)request.getAttribute("my_sites.jsp-showPrivateLabel");
boolean showStagingLabel = (Boolean)request.getAttribute("my_sites.jsp-showStagingLabel");

String groupDisplayURL = siteGroup.getDisplayURL(themeDisplay, privateLayout);

if (Validator.isNull(groupDisplayURL)) {
	PortletURL groupAdministrationURL = null;

	PanelCategoryHelper panelCategoryHelper = (PanelCategoryHelper)request.getAttribute(ApplicationListWebKeys.PANEL_CATEGORY_HELPER);

	String portletId = panelCategoryHelper.getFirstPortletId(PanelCategoryKeys.SITE_ADMINISTRATION, permissionChecker, siteGroup);

	if (Validator.isNotNull(portletId)) {
		groupAdministrationURL = PortalUtil.getControlPanelPortletURL(request, siteGroup, portletId, 0, 0, PortletRequest.RENDER_PHASE);

		if (groupAdministrationURL != null) {
			groupDisplayURL = groupAdministrationURL.toString();
		}
	}
}
%>

<li class="list-group-item selectable-site">
	<aui:a cssClass='<%= "site-link" + (selectedSite ? " selected-site" : StringPool.BLANK) %>' href="<%= groupDisplayURL %>">
		<div class="list-group-item-field site-logo-container">
			<c:choose>
				<c:when test="<%= selectedSite %>">
					<div class="site-logo">
						<aui:icon image="check" markupView="lexicon" />
					</div>
				</c:when>
				<c:when test="<%= Validator.isNotNull(siteGroup.getLogoURL(themeDisplay, false)) %>">
					<img alt="" class="site-image" src="<%= siteGroup.getLogoURL(themeDisplay, false) %>" />
				</c:when>
				<c:otherwise>
					<div class="site-logo">
						<aui:icon image="sites" markupView="lexicon" />
					</div>
				</c:otherwise>
			</c:choose>
		</div>

		<div class="list-group-item-content">
			<h5><%= HtmlUtil.escape(siteName) %></h5>

			<c:if test="<%= showStagingLabel %>">
				<small><liferay-ui:message key="staging" /></small>
			</c:if>

			<c:if test="<%= showPrivateLabel %>">
				<small><liferay-ui:message key='<%= privateLayout ? "private" : "public" %>' /></small>
			</c:if>
		</div>
	</aui:a>
</li>