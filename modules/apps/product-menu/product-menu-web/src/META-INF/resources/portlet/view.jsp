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

<%@ include file="/portlet/init.jsp" %>

<%
PanelCategoryRegistry panelCategoryRegistry = (PanelCategoryRegistry)request.getAttribute(ApplicationListWebKeys.PANEL_CATEGORY_REGISTRY);
PanelAppRegistry panelAppRegistry = (PanelAppRegistry)request.getAttribute(ApplicationListWebKeys.PANEL_APP_REGISTRY);

String portletId = themeDisplay.getPpid();

PanelCategory firstChildPanelCategory = panelCategoryRegistry.getFirstChildPanelCategory(PanelCategoryKeys.ROOT);

String rootPanelCategoryKey = firstChildPanelCategory.getKey();

if (Validator.isNotNull(portletId)) {
	for (PanelCategory panelCategory : panelCategoryRegistry.getChildPanelCategories(PanelCategoryKeys.ROOT)) {
		for (PanelCategory subPanelCategory : panelCategoryRegistry.getChildPanelCategories(panelCategory)) {
			for (PanelApp panelApp : panelAppRegistry.getPanelApps(subPanelCategory)) {
				if (portletId.equals(panelApp.getPortletId())) {
					rootPanelCategoryKey = panelCategory.getKey();

					break;
				}
			}
		}
	}
}
%>

<div class="<%= Validator.isNotNull(portletId) ? "open" : "close" %> product-menu sidebar sidebar-fixed sidebar-inverse" id="productMenu">
	<div class="sidebar-header">
		<h4>
			<span>
				<span class="icon-monospaced"><img alt="" height="25px" src="<%= themeDisplay.getCompanyLogo() %>" width="25px" /></span>
				<span><%= company.getName() %></span>
			</span>

			<span class="icon-monospaced icon-remove sidenav-close" id="<portlet:namespace />closeButton"></span>
		</h4>
	</div>

	<div class="sidebar-body">
		<ul class="nav nav-tabs product-menu-tabs">

			<%
			for (PanelCategory curPanelCategory : panelCategoryRegistry.getChildPanelCategories(PanelCategoryKeys.ROOT)) {
			%>

				<li class="col-xs-4 <%= rootPanelCategoryKey.equals(curPanelCategory.getKey()) ? "active" : StringPool.BLANK %>">
					<a aria-expanded="true" data-toggle="tab" href="#<%= curPanelCategory.getKey() %>">
						<div class="product-menu-tab-icon"><span class="<%= curPanelCategory.getIconCssClass() %> icon-monospaced"></span></div>
						<div class="product-menu-tab-text"><%= curPanelCategory.getLabel(locale) %></div>
					</a>
				</li>

			<%
			}
			%>

		</ul>

		<div class="tab-content">

			<%
			for (PanelCategory curPanelCategory : panelCategoryRegistry.getChildPanelCategories(PanelCategoryKeys.ROOT)) {
			%>

				<div class="fade tab-pane <%= rootPanelCategoryKey.equals(curPanelCategory.getKey()) ? "active" : StringPool.BLANK %> in" id="<%= curPanelCategory.getKey() %>">
					<application-list-ui:panel-category-content panelCategory="<%= curPanelCategory %>" />
				</div>

			<%
			}
			%>

		</div>
	</div>

	<div class="sidebar-footer">
		<div class="nameplate">
			<div class="nameplate-field">
				<div class="user-icon user-icon-lg user-icon-warning">
					<img alt="<%= HtmlUtil.escapeAttribute(user.getFullName()) %>" src="<%= HtmlUtil.escape(user.getPortraitURL(themeDisplay)) %>" />
				</div>
			</div>

			<div class="nameplate-content">
				<h4 class="user-heading"><%= HtmlUtil.escape(user.getFullName()) %><small class="user-subheading"></small></h4>
			</div>

			<c:if test="<%= themeDisplay.isShowSignOutIcon() %>">
				<div class="nameplate-field">
					<a class="icon-monospaced icon-signout user-signout" href="<%= themeDisplay.getURLSignOut() %>"></a>
				</div>
			</c:if>
		</div>
	</div>
</div>

<aui:script>
	$('#<portlet:namespace />closeButton').on(
		'click',
		function(event) {
			var productMenu = $('#productMenu');

			productMenu.addClass('close');

			productMenu.removeClass('open');
		}
	);
</aui:script>