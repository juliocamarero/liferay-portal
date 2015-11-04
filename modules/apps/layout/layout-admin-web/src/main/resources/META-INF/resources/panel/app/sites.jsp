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
Group group = layoutsAdminDisplayContext.getGroup();
%>

<c:if test="<%= !group.isLayoutPrototype() %>">

	<%
	Group stagingGroup = layoutsAdminDisplayContext.getStagingGroup();
	%>

	<c:if test="<%= stagingGroup.isStaged() %>">

		<%
		long layoutSetBranchId = ParamUtil.getLong(request, "layoutSetBranchId");

		if (layoutSetBranchId <= 0) {
			LayoutSet selLayoutSet = layoutsAdminDisplayContext.getSelLayoutSet();

			layoutSetBranchId = StagingUtil.getRecentLayoutSetBranchId(user, selLayoutSet.getLayoutSetId());
		}

		LayoutSetBranch layoutSetBranch = null;

		if (layoutSetBranchId > 0) {
			layoutSetBranch = LayoutSetBranchLocalServiceUtil.fetchLayoutSetBranch(layoutSetBranchId);
		}

		if (layoutSetBranch == null) {
			try {
				layoutSetBranch = LayoutSetBranchLocalServiceUtil.getMasterLayoutSetBranch(layoutsAdminDisplayContext.getStagingGroupId(), layoutsAdminDisplayContext.isPrivateLayout());
			}
			catch (NoSuchLayoutSetBranchException nslsbe) {
			}
		}

		List<LayoutSetBranch> layoutSetBranches = LayoutSetBranchLocalServiceUtil.getLayoutSetBranches(layoutsAdminDisplayContext.getStagingGroupId(), layoutsAdminDisplayContext.isPrivateLayout());
		%>

		<c:choose>
			<c:when test="<%= layoutSetBranches.size() > 1 %>">
				<aui:nav-bar>
					<aui:nav cssClass="navbar-nav">
						<aui:nav-item dropdown="<%= true %>" label="<%= HtmlUtil.escape(layoutSetBranch.getName()) %>">

							<%
							for (int i = 0; i < layoutSetBranches.size(); i++) {
								LayoutSetBranch curLayoutSetBranch = layoutSetBranches.get(i);

								boolean selected = (curLayoutSetBranch.getLayoutSetBranchId() == layoutSetBranch.getLayoutSetBranchId());

								PortletURL layoutSetBranchURL = PortalUtil.getControlPanelPortletURL(request, LayoutAdminPortletKeys.GROUP_PAGES, 0, PortletRequest.RENDER_PHASE);

								layoutSetBranchURL.setParameter("mvcPath", "/view.jsp");
								layoutSetBranchURL.setParameter("redirect", String.valueOf(layoutsAdminDisplayContext.getRedirectURL()));
								layoutSetBranchURL.setParameter("groupId", String.valueOf(curLayoutSetBranch.getGroupId()));
								layoutSetBranchURL.setParameter("privateLayout", String.valueOf(layoutsAdminDisplayContext.isPrivateLayout()));
								layoutSetBranchURL.setParameter("layoutSetBranchId", String.valueOf(curLayoutSetBranch.getLayoutSetBranchId()));
							%>

								<aui:nav-item cssClass='<%= selected ? "disabled" : StringPool.BLANK %>' href="<%= selected ? null : layoutSetBranchURL.toString() %>" label="<%= HtmlUtil.escape(curLayoutSetBranch.getName()) %>" />

							<%
							}
							%>

						</aui:nav-item>
					</aui:nav>
				</aui:nav-bar>
			</c:when>
		</c:choose>

		<%
		request.setAttribute(WebKeys.PRIVATE_LAYOUT, layoutsAdminDisplayContext.isPrivateLayout());
		%>

		<liferay-staging:menu cssClass="manage-pages-branch-menu" extended="<%= true %>" icon="/common/tool.png" message="" selPlid="<%= layoutsAdminDisplayContext.getSelPlid() %>" showManageBranches="<%= true %>"  />
	</c:if>

	<%
	String selectedLayoutIds = ParamUtil.getString(request, "selectedLayoutIds");

	Group liveGroup = layoutsAdminDisplayContext.getLiveGroup();

	Group selGroup = layoutsAdminDisplayContext.getSelGroup();
	%>

	<c:if test="<%= !selGroup.isLayoutSetPrototype() %>">

		<%
		PortletURL editPublicLayoutURL = PortalUtil.getControlPanelPortletURL(request, LayoutAdminPortletKeys.GROUP_PAGES, 0, PortletRequest.RENDER_PHASE);

		editPublicLayoutURL.setParameter("privateLayout", Boolean.FALSE.toString());
		editPublicLayoutURL.setParameter("redirect", layoutsAdminDisplayContext.getRedirect());
		editPublicLayoutURL.setParameter("groupId", String.valueOf(layoutsAdminDisplayContext.getLiveGroupId()));
		editPublicLayoutURL.setParameter("viewLayout", Boolean.TRUE.toString());
		%>

		<liferay-ui:layouts-tree
			groupId="<%= selGroup.getGroupId() %>"
			portletNamespace="<%= StringPool.UNDERLINE + LayoutAdminPortletKeys.GROUP_PAGES + StringPool.UNDERLINE %>"
			portletURL="<%= editPublicLayoutURL %>"
			privateLayout="<%= false %>"
			rootNodeName="<%= liveGroup.getLayoutRootNodeName(false, themeDisplay.getLocale()) %>"
			selectedLayoutIds="<%= selectedLayoutIds %>"
			selPlid="<%= layoutsAdminDisplayContext.getSelPlid() %>"
			treeId="publicLayoutsTree"
		/>
	</c:if>

	<%
	PortletURL editPrivateLayoutURL = PortalUtil.getControlPanelPortletURL(request, LayoutAdminPortletKeys.GROUP_PAGES, 0, PortletRequest.RENDER_PHASE);

	editPrivateLayoutURL.setParameter("privateLayout", Boolean.TRUE.toString());
	editPrivateLayoutURL.setParameter("redirect", layoutsAdminDisplayContext.getRedirect());
	editPrivateLayoutURL.setParameter("groupId", String.valueOf(layoutsAdminDisplayContext.getLiveGroupId()));
	editPrivateLayoutURL.setParameter("viewLayout", Boolean.TRUE.toString());
	%>

	<liferay-ui:layouts-tree
		groupId="<%= selGroup.getGroupId() %>"
		portletNamespace="<%= StringPool.UNDERLINE + LayoutAdminPortletKeys.GROUP_PAGES + StringPool.UNDERLINE %>"
		portletURL="<%= editPrivateLayoutURL %>"
		privateLayout="<%= true %>"
		rootNodeName="<%= liveGroup.getLayoutRootNodeName(true, themeDisplay.getLocale()) %>"
		selectedLayoutIds="<%= selectedLayoutIds %>"
		selPlid="<%= layoutsAdminDisplayContext.getSelPlid() %>"
		treeId="privateLayoutsTree"
	/>

	<%
	Layout selLayout = layoutsAdminDisplayContext.getSelLayout();

	boolean privateLayout = layoutsAdminDisplayContext.isPrivateLayout();

	if (selGroup.isLayoutSetPrototype()) {
		privateLayout = true;
	}
	%>

	<c:if test="<%= ((selLayout == null) && GroupPermissionUtil.contains(permissionChecker, selGroup, ActionKeys.ADD_LAYOUT)) || ((selLayout != null) && LayoutPermissionUtil.contains(permissionChecker, selLayout, ActionKeys.ADD_LAYOUT)) %>">

		<%
		PortletURL addPagesURL = PortalUtil.getControlPanelPortletURL(request, LayoutAdminPortletKeys.GROUP_PAGES, 0, PortletRequest.RENDER_PHASE);

		addPagesURL.setParameter("mvcPath", "/add_layout.jsp");
		addPagesURL.setParameter("groupId", String.valueOf(selGroup.getGroupId()));
		addPagesURL.setParameter("selPlid", (selLayout != null) ? String.valueOf(selLayout.getPlid()) : StringPool.BLANK);
		addPagesURL.setParameter("privateLayout", String.valueOf(privateLayout));
		%>

		<aui:button-row>
			<aui:button cssClass="btn-block btn-primary" href="<%= addPagesURL.toString() %>" value="add-page" />
		</aui:button-row>
	</c:if>
</c:if>