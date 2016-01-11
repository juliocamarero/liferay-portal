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
String productMenuState = SessionClicks.get(request, "com.liferay.control.menu.web_productMenuState", "closed");

PortletURL portletURL = PortletURLFactoryUtil.create(request, ProductNavigationProductMenuPortletKeys.PRODUCT_NAVIGATION_PRODUCT_MENU, plid, RenderRequest.RENDER_PHASE);

portletURL.setWindowState(LiferayWindowState.EXCLUSIVE);
%>

<div class="toolbar-group-content <%= Validator.equals(productMenuState, "open") ? "active" : StringPool.BLANK %>">
	<a class="control-menu-icon sidenav-toggler" data-content="body" data-panelurl="<%= portletURL.toString() %>" data-qa-id="productMenu" data-target="#sidenavSliderId,#wrapper" data-title="<%= HtmlUtil.escape(LanguageUtil.get(request, "menu")) %>" data-toggle="sidenav" data-type="fixed-push" data-type-mobile="fixed" href="#sidenavSliderId" id="sidenavToggleId" onmouseover="Liferay.Portal.ToolTip.show(this, '<%= HtmlUtil.escapeJS(LanguageUtil.get(request, "menu")) %>')">
		<div class="toast-animation">
			<div class="pm"></div>

			<div class="cn"></div>
		</div>
	</a>
</div>

<aui:script use="liferay-store">
	var sidenavToggle = $('#sidenavToggleId');

	sidenavToggle.sideNavigation();

	var sidenavSlider = $('#sidenavSliderId');

	sidenavSlider.off('closed.lexicon.sidenav');
	sidenavSlider.off('open.lexicon.sidenav');

	sidenavSlider.on(
		'closed.lexicon.sidenav',
		function(event) {
			Liferay.Store('com.liferay.control.menu.web_productMenuState', 'closed');
		}
	);

	sidenavSlider.on(
		'open.lexicon.sidenav',
		function(event) {
			var productMenuSidebar = A.one('#productMenuSidebar');

			if (productMenuSidebar && !productMenuSidebar.hasClass('content-loaded')) {
				<portlet:namespace />getProductMenuSidebar();
			}

			Liferay.Store('com.liferay.control.menu.web_productMenuState', 'open');
		}
	);

	Liferay.on(
		'ProductMenu:openUserMenu',
		function(event) {
			var userCollapse = $('#<portlet:namespace /><%= AUIUtil.normalizeId(PanelCategoryKeys.USER) %>Collapse');

			if ($('body').hasClass('open')) {
				if (userCollapse.hasClass('in')) {
					userCollapse.collapse('hide');

					sidenavToggle.sideNavigation('hide');
				}
				else {
					userCollapse.collapse('show');
				}
			}
			else {
				sidenavToggle.sideNavigation('show');

				userCollapse.collapse('show');
			}
		}
	);
</aui:script>

<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />getProductMenuSidebar',
		function() {
			var A = AUI();

			var sidenavToggle = A.one('#sidenavToggleId');

			if (sidenavToggle) {
				var uri = sidenavToggle.attr('data-panelurl');

				A.io.request(
					uri,
					{
						after: {
							success: function(event, id, obj) {
								var response = this.get('responseData');

								var productMenuSidebar = A.one('#productMenuSidebar');

								productMenuSidebar.plug(A.Plugin.ParseContent);

								productMenuSidebar.setContent(response);
								productMenuSidebar.addClass('content-loaded');
							}
						}
					}
				);
			}
		},
		['aui-io-request', 'aui-parse-content', 'event-outside']
	);
</aui:script>