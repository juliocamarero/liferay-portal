<%--
/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

<%@ include file="/html/portlet/staging_bar/init.jsp" %>

<%
long layoutSetBranchId = ParamUtil.getLong(request, "layoutSetBranchId");

List<LayoutRevision> layoutRevisions = LayoutRevisionLocalServiceUtil.getChildLayoutRevisions(layoutSetBranchId, LayoutRevisionConstants.DEFAULT_PARENT_LAYOUT_REVISION_ID, plid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, new LayoutRevisionCreateDateComparator(true));

long layoutRevisionId = StagingUtil.getRecentLayoutRevisionId(request, layoutSetBranchId, plid);

LayoutRevision currentLayoutRevision = null;

if (layoutRevisionId <= 0) {
	LayoutBranch layoutBranch =	LayoutBranchLocalServiceUtil.getMasterLayoutBranch(layoutSetBranchId, plid);

	currentLayoutRevision =	LayoutRevisionLocalServiceUtil.getLayoutRevision(layoutSetBranchId, layoutBranch.getLayoutBranchId(), plid);

	layoutRevisionId = currentLayoutRevision.getLayoutRevisionId();
}
else {
	currentLayoutRevision = LayoutRevisionLocalServiceUtil.getLayoutRevision(layoutRevisionId);
}

request.setAttribute("view_layout_branches.jsp-currenttLayoutBranchId", String.valueOf(currentLayoutRevision.getLayoutBranchId()));
%>

<liferay-ui:error key="<%= LayoutBranchNameException.class.getName() + LayoutBranchNameException.DUPLICATE %>" message="a-page-variation-with-that-name-already-exists" />
<liferay-ui:error key="<%= LayoutBranchNameException.class.getName() + LayoutBranchNameException.TOO_LONG %>" message='<%= LanguageUtil.format(pageContext, "please-enter-a-value-between-x-and-x-characters-long", new Object[] {4, 100}) %>' />
<liferay-ui:error key="<%= LayoutBranchNameException.class.getName() + LayoutBranchNameException.TOO_SHORT %>" message='<%= LanguageUtil.format(pageContext, "please-enter-a-value-between-x-and-x-characters-long", new Object[] {4, 100}) %>' />

<div class="portlet-msg-info">
	<liferay-ui:message key="page-variations-help" />
</div>

<c:if test="<%= GroupPermissionUtil.contains(permissionChecker, stagingGroup.getGroupId(), ActionKeys.ADD_LAYOUT_BRANCH) %>">
	<liferay-util:html-top>
		<liferay-util:include page="/html/portlet/staging_bar/edit_layout_branch.jsp">
			<liferay-util:param name="layoutRevisionId" value="<%= String.valueOf(layoutRevisionId) %>" />
			<liferay-util:param name="redirect" value="<%= currentURL %>" />
		</liferay-util:include>
	</liferay-util:html-top>

	<%
	String taglibOnClick = "javascript:Liferay.StagingBar.addBranch('" + LanguageUtil.get(pageContext, "add-page-variation") + "');";
	%>

	<aui:button-row>
		<aui:button name="addRootLayoutBranch" onClick="<%= taglibOnClick %>" value="add-page-variation" />
	</aui:button-row>
</c:if>

<div class="branch-results">
	<liferay-ui:search-container>
		<liferay-ui:search-container-results
			results="<%= layoutRevisions %>"
			total="<%= layoutRevisions.size() %>"
		/>

		<liferay-ui:search-container-row
			className="com.liferay.portal.model.LayoutRevision"
			escapedModel="<%= true %>"
			keyProperty="layoutRevisionId"
			modelVar="layoutRevision"
		>

			<%
			LayoutBranch layoutBranch = layoutRevision.getLayoutBranch();
			%>

			<liferay-ui:search-container-column-text
				buffer="buffer"
				name="name"
			>

				<%
				String layoutBranchName = layoutBranch.getName();

				if (layoutRevision.getLayoutBranchId() == currentLayoutRevision.getLayoutBranchId()) {
					buffer.append("<strong>");
				}

				buffer.append(LanguageUtil.get(pageContext, layoutBranchName));

				if (layoutBranch.isMaster()) {
					buffer.append(" (*)");
				}

				if (layoutRevision.getLayoutBranchId() == currentLayoutRevision.getLayoutBranchId()) {
					buffer.append("</strong>");
				}
				%>

			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				name="description"
				value="<%= layoutBranch.getDescription() %>"
			/>

			<liferay-ui:search-container-column-jsp
				path="/html/portlet/staging_bar/layout_branch_action.jsp"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator searchContainer="<%= searchContainer %>" paginate="<%= false %>" />
	</liferay-ui:search-container>
</div>

<aui:script position="inline" use="liferay-staging-branch">
	Liferay.StagingBar.init(
		{
			namespace: '<portlet:namespace />'
		}
	);

	<c:if test='<%= themeDisplay.isStatePopUp() && SessionMessages.contains(renderRequest, portletName + ".doConfigure") %>'>
		var data = null;

		<c:if test='<%= SessionMessages.contains(renderRequest, portletName + ".notAjaxable") %>'>
			data = {
				portletAjaxable: false
			};
		</c:if>

		Liferay.Util.getOpener().Liferay.Portlet.refresh('#p_p_id_<%= PortletKeys.STAGING_BAR %>_', data);
	</c:if>
</aui:script>