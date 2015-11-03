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
							%>

								<portlet:renderURL var="layoutSetBranchURL">
									<portlet:param name="mvcPath" value="/view.jsp" />
									<portlet:param name="redirect" value="<%= String.valueOf(layoutsAdminDisplayContext.getRedirectURL()) %>" />
									<portlet:param name="groupId" value="<%= String.valueOf(curLayoutSetBranch.getGroupId()) %>" />
									<portlet:param name="privateLayout" value="<%= String.valueOf(layoutsAdminDisplayContext.isPrivateLayout()) %>" />
									<portlet:param name="layoutSetBranchId" value="<%= String.valueOf(curLayoutSetBranch.getLayoutSetBranchId()) %>" />
								</portlet:renderURL>

								<aui:nav-item cssClass='<%= selected ? "disabled" : StringPool.BLANK %>' href="<%= selected ? null : layoutSetBranchURL %>" label="<%= HtmlUtil.escape(curLayoutSetBranch.getName()) %>" />

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
		<liferay-portlet:renderURL varImpl="editPublicLayoutURL">
			<portlet:param name="privateLayout" value="<%= Boolean.FALSE.toString() %>" />
			<portlet:param name="redirect" value="<%= layoutsAdminDisplayContext.getRedirect() %>" />
			<portlet:param name="groupId" value="<%= String.valueOf(layoutsAdminDisplayContext.getLiveGroupId()) %>" />
			<portlet:param name="viewLayout" value="<%= Boolean.TRUE.toString() %>" />
		</liferay-portlet:renderURL>

		<liferay-ui:layouts-tree
			groupId="<%= selGroup.getGroupId() %>"
			portletURL="<%= editPublicLayoutURL %>"
			privateLayout="<%= false %>"
			rootNodeName="<%= liveGroup.getLayoutRootNodeName(false, themeDisplay.getLocale()) %>"
			selectedLayoutIds="<%= selectedLayoutIds %>"
			selPlid="<%= layoutsAdminDisplayContext.getSelPlid() %>"
			treeId="publicLayoutsTree"
		/>
	</c:if>

	<liferay-portlet:renderURL varImpl="editPrivateLayoutURL">
		<portlet:param name="privateLayout" value="<%= Boolean.TRUE.toString() %>" />
		<portlet:param name="redirect" value="<%= layoutsAdminDisplayContext.getRedirect() %>" />
		<portlet:param name="groupId" value="<%= String.valueOf(layoutsAdminDisplayContext.getLiveGroupId()) %>" />
		<portlet:param name="viewLayout" value="<%= Boolean.TRUE.toString() %>" />
	</liferay-portlet:renderURL>

	<liferay-ui:layouts-tree
		groupId="<%= selGroup.getGroupId() %>"
		portletURL="<%= editPrivateLayoutURL %>"
		privateLayout="<%= true %>"
		rootNodeName="<%= liveGroup.getLayoutRootNodeName(true, themeDisplay.getLocale()) %>"
		selectedLayoutIds="<%= selectedLayoutIds %>"
		selPlid="<%= layoutsAdminDisplayContext.getSelPlid() %>"
		treeId="privateLayoutsTree"
	/>
</c:if>