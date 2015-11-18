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
Group group = themeDisplay.getSiteGroup();
%>

<div class="site-administration-toolbar toolbar">
	<div class="toolbar-group-content">
		<aui:a cssClass="site-administration-title" href="<%= group.getDisplayURL(themeDisplay) %>">
			<%= HtmlUtil.escape(group.getDescriptiveName(locale)) %>

			<c:if test="<%= themeDisplay.isShowStagingIcon() %>">
				<c:choose>
					<c:when test="<%= group.isStagingGroup() %>">
						(<liferay-ui:message key="staging" />)
					</c:when>
					<c:when test="<%= group.hasStagingGroup() %>">
						(<liferay-ui:message key="live" />)
					</c:when>
				</c:choose>
			</c:if>
		</aui:a>
	</div>

	<c:if test="<%= themeDisplay.isShowStagingIcon() %>">

		<%
		String stagingGroupURL = null;

		if (!group.isStagedRemotely() && group.hasStagingGroup()) {
			Group stagingGroup = StagingUtil.getStagingGroup(group.getGroupId());

			if (stagingGroup != null) {
				stagingGroupURL = stagingGroup.getDisplayURL(themeDisplay, layout.isPrivateLayout());

				if (Validator.isNull(stagingGroupURL)) {
					PortletURL groupAdministrationURL = null;

					PanelCategoryHelper panelCategoryHelper = (PanelCategoryHelper)request.getAttribute(ApplicationListWebKeys.PANEL_CATEGORY_HELPER);

					String portletId = panelCategoryHelper.getFirstPortletId(PanelCategoryKeys.SITE_ADMINISTRATION, permissionChecker, stagingGroup);

					if (Validator.isNotNull(portletId)) {
						groupAdministrationURL = PortalUtil.getControlPanelPortletURL(request, stagingGroup, portletId, 0, 0, PortletRequest.RENDER_PHASE);

						if (groupAdministrationURL != null) {
							stagingGroupURL = groupAdministrationURL.toString();
						}
					}
				}
			}
		}
		%>

		<div class="<%= stagingGroupURL == null ? "active" : StringPool.BLANK %> toolbar-group-field">
			<aui:a cssClass="icon-fb-radio icon-monospaced" href="<%= stagingGroupURL %>" title="staging" />
		</div>

		<%
		String liveGroupURL = null;

		if (group.isStagingGroup()) {
			if (group.isStagedRemotely()) {
				liveGroupURL = StagingUtil.buildRemoteURL(group.getTypeSettingsProperties());
			}
			else {
				Group liveGroup = StagingUtil.getLiveGroup(group.getGroupId());

				if (liveGroup != null) {
					liveGroupURL = liveGroup.getDisplayURL(themeDisplay, layout.isPrivateLayout());

					if (Validator.isNull(liveGroupURL)) {
						PortletURL groupAdministrationURL = null;

						PanelCategoryHelper panelCategoryHelper = (PanelCategoryHelper)request.getAttribute(ApplicationListWebKeys.PANEL_CATEGORY_HELPER);

						String portletId = panelCategoryHelper.getFirstPortletId(PanelCategoryKeys.SITE_ADMINISTRATION, permissionChecker, liveGroup);

						if (Validator.isNotNull(portletId)) {
							groupAdministrationURL = PortalUtil.getControlPanelPortletURL(request, liveGroup, portletId, 0, 0, PortletRequest.RENDER_PHASE);

							if (groupAdministrationURL != null) {
								liveGroupURL = groupAdministrationURL.toString();
							}
						}
					}
				}
			}
		}
		%>

		<div class="<%= liveGroupURL == null ? "active" : StringPool.BLANK %> toolbar-group-field">
			<aui:a cssClass="icon-circle-blank icon-monospaced" href="<%= liveGroupURL %>" title="live" />
		</div>
	</c:if>
</div>

<aui:a href="javascript:;" id="manageSitesLink" title="go-to-other-site">
	<aui:icon image="sites" markupView="lexicon" />
</aui:a>

<div class="hide">
	<div id="<portlet:namespace/>siteSelectorContent">
		<liferay-util:include page="/sites/my_sites.jsp" servletContext="<%= application %>" />

		<%
		String portletId = PortletProviderUtil.getPortletId(Group.class.getName(), PortletProvider.Action.MANAGE);
		%>

		<c:if test="<%= Validator.isNotNull(portletId) && PortletPermissionUtil.hasControlPanelAccessPermission(permissionChecker, scopeGroupId, portletId) %>">

			<%
			PortletURL portletURL = PortletProviderUtil.getPortletURL(request, Group.class.getName(), PortletProvider.Action.MANAGE);

			ResourceBundle resourceBundle = ResourceBundleUtil.getBundle("content.Language", locale, getClass());
			%>

			<div class="manage-sites-link">
				<aui:icon image="sites" label='<%= LanguageUtil.get(resourceBundle, "manage-sites") %>' markupView="lexicon" url="<%= portletURL.toString() %>" />
			</div>
		</c:if>
	</div>
</div>

<aui:script use="aui-popover">
	var trigger = A.one('#<portlet:namespace/>manageSitesLink');

	var popOver = new A.Popover(
		{
			align: {
				node: trigger,
				points:[A.WidgetPositionAlign.LC, A.WidgetPositionAlign.RC]
			},
			bodyContent: A.one('#<portlet:namespace/>siteSelectorContent'),
			cssClass: 'product-menu',
			position: 'left',
			visible: false,
			width: 300,
			zIndex: Liferay.zIndex.TOOLTIP
		}
	).render();

	trigger.on(
		'click',
		function() {
			popOver.set('visible', !popOver.get('visible'));
		}
	);
</aui:script>

<%
PanelCategory panelCategory = (PanelCategory)request.getAttribute(ApplicationListWebKeys.PANEL_CATEGORY);
%>

<liferay-application-list:panel panelCategory="<%= panelCategory %>" />