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

<%@ include file="/html/portlet/sites_admin/init.jsp" %>

<%
Group group = (Group)request.getAttribute("site.group");
Group liveGroup = (Group)request.getAttribute("site.liveGroup");
LayoutSetPrototype layoutSetPrototype = (LayoutSetPrototype)request.getAttribute("site.layoutSetPrototype");
boolean showPrototypes = GetterUtil.getBoolean(request.getAttribute("site.showPrototypes"));

long parentGroupId = ParamUtil.getLong(request, "parentGroupSearchContainerPrimaryKeys", (group != null) ? group.getParentGroupId() : GroupConstants.DEFAULT_PARENT_GROUP_ID);

if (parentGroupId <= 0) {
	parentGroupId = GroupConstants.DEFAULT_PARENT_GROUP_ID;

	if (liveGroup != null) {
		parentGroupId = liveGroup.getParentGroupId();
	}
}

Group parentGroup = null;

if ((group == null) && (parentGroupId == GroupConstants.DEFAULT_PARENT_GROUP_ID) && !permissionChecker.isCompanyAdmin()) {
	List<Group> manageableGroups = new ArrayList<Group>();

	for (Group curGroup : user.getGroups()) {
		if (GroupPermissionUtil.contains(permissionChecker, curGroup, ActionKeys.MANAGE_SUBGROUPS)) {
			manageableGroups.add(curGroup);
		}
	}

	if (manageableGroups.size() == 1) {
		Group manageableGroup = manageableGroups.get(0);

		parentGroupId = manageableGroup.getGroupId();
	}
}

if (parentGroupId != GroupConstants.DEFAULT_PARENT_GROUP_ID) {
	parentGroup = GroupLocalServiceUtil.fetchGroup(parentGroupId);
}

List<LayoutSetPrototype> layoutSetPrototypes = LayoutSetPrototypeServiceUtil.search(company.getCompanyId(), Boolean.TRUE, null);

LayoutSet layoutSet = null;
boolean layoutSetPrototypeLinkEnabled = true;

if (showPrototypes && (group != null)) {
	try {
		layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(group.getGroupId());

		layoutSetPrototypeLinkEnabled = layoutSet.isLayoutSetPrototypeLinkEnabled();

		String layoutSetPrototypeUuid = layoutSet.getLayoutSetPrototypeUuid();

		if (Validator.isNotNull(layoutSetPrototypeUuid)) {
			layoutSetPrototype = LayoutSetPrototypeLocalServiceUtil.getLayoutSetPrototypeByUuidAndCompanyId(layoutSetPrototypeUuid, company.getCompanyId());
		}
	}
	catch (Exception e) {
	}
}

UnicodeProperties typeSettingsProperties = null;

if (liveGroup != null) {
	typeSettingsProperties = liveGroup.getTypeSettingsProperties();
}
else if (group != null) {
	typeSettingsProperties = group.getTypeSettingsProperties();
}
%>

<liferay-ui:error-marker key="errorSection" value="details" />

<h3><liferay-ui:message key="details" /></h3>

<aui:model-context bean="<%= liveGroup %>" model="<%= Group.class %>" />

<liferay-ui:error exception="<%= DuplicateGroupException.class %>" message="please-enter-a-unique-name" />
<liferay-ui:error exception="<%= GroupInheritContentException.class %>" message="this-site-cannot-inherit-content-from-its-parent-site" />
<liferay-ui:error exception="<%= GroupKeyException.class %>" message="please-enter-a-valid-name" />
<liferay-ui:error exception="<%= GroupParentException.MustNotBeOwnParent.class %>" message="the-site-cannot-be-its-own-parent-site" />
<liferay-ui:error exception="<%= GroupParentException.MustNotHaveChildParent.class %>" message="the-site-cannot-have-a-child-as-its-parent-site" />
<liferay-ui:error exception="<%= GroupParentException.MustNotHaveStagingParent.class %>" message="the-site-cannot-have-a-staging-site-as-its-parent-site" />
<liferay-ui:error exception="<%= PendingBackgroundTaskException.class %>" message="the-site-cannot-be-deleted-because-it-has-background-tasks-in-progress" />
<liferay-ui:error exception="<%= RequiredGroupException.MustNotDeleteCurrentGroup.class %>" message="the-site-cannot-be-deleted-or-deactivated-because-you-are-accessing-the-site" />
<liferay-ui:error exception="<%= RequiredGroupException.MustNotDeleteGroupThatHasChild.class %>" message="you-cannot-delete-sites-that-have-subsites" />
<liferay-ui:error exception="<%= RequiredGroupException.MustNotDeleteSystemGroup.class %>" message="the-site-cannot-be-deleted-or-deactivated-because-it-is-a-required-system-site" />

<liferay-ui:error key="resetMergeFailCountAndMerge" message="unable-to-reset-the-failure-counter-and-propagate-the-changes" />

<aui:fieldset>
	<c:choose>
		<c:when test="<%= (liveGroup != null) && liveGroup.isOrganization() %>">
			<aui:input helpMessage="the-name-of-this-site-cannot-be-edited-because-it-belongs-to-an-organization" name="name" type="resource" value="<%= liveGroup.getDescriptiveName(locale) %>" />
		</c:when>
		<c:when test="<%= (liveGroup == null) || (!liveGroup.isCompany() && !PortalUtil.isSystemGroup(liveGroup.getGroupKey())) %>">
			<aui:input autoFocus="<%= windowState.equals(WindowState.MAXIMIZED) %>" name="name" />
		</c:when>
	</c:choose>

	<aui:input name="description" />

	<c:if test="<%= liveGroup != null %>">
		<aui:input name="siteId" type="resource" value="<%= String.valueOf(liveGroup.getGroupId()) %>" />
	</c:if>

	<c:if test="<%= (group == null) || !group.isCompany() %>">
		<aui:input name="active" value="<%= true %>" />
	</c:if>

	<c:if test="<%= ((parentGroupId != GroupConstants.DEFAULT_PARENT_GROUP_ID) && PropsValues.SITES_SHOW_INHERIT_CONTENT_SCOPE_FROM_PARENT_SITE) %>">

		<%
		boolean disabled = false;

		if ((parentGroup != null) && parentGroup.isInheritContent()) {
			disabled = true;
		}
		%>

		<aui:input disabled="<%= disabled %>" helpMessage='<%= disabled ? "this-site-cannot-inherit-the-content-from-its-parent-site-since-the-parent-site-is-already-inheriting-the-content-from-its-parent" : StringPool.BLANK %>' name="inheritContent" value="<%= false %>" />
	</c:if>

	<h3><liferay-ui:message key="membership-options" /></h3>

	<c:if test="<%= (group == null) || !group.isCompany() %>">
		<aui:select label="membership-type" name="type">
			<aui:option label="open" value="<%= GroupConstants.TYPE_SITE_OPEN %>" />
			<aui:option label="restricted" value="<%= GroupConstants.TYPE_SITE_RESTRICTED %>" />
			<aui:option label="private" value="<%= GroupConstants.TYPE_SITE_PRIVATE %>" />
		</aui:select>

		<%
		boolean manualMembership = true;

		if (liveGroup != null) {
			manualMembership = GetterUtil.getBoolean(liveGroup.isManualMembership(), true);
		}
		%>

		<aui:input label="allow-manual-membership-management" name="manualMembership" value="<%= manualMembership %>" />
	</c:if>
</aui:fieldset>

<%
boolean disableLayoutSetPrototypeInput = false;

if ((group != null) && !LanguageUtil.isInheritLocales(group.getGroupId())) {
	disableLayoutSetPrototypeInput = true;
}

boolean hasUnlinkLayoutSetPrototypePermission = PortalPermissionUtil.contains(permissionChecker, ActionKeys.UNLINK_LAYOUT_SET_PROTOTYPE);
%>

<c:if test="<%= (group == null) || !group.isCompany() %>">
	<aui:fieldset>
		<c:choose>
			<c:when test="<%= showPrototypes && ((group != null) || (!layoutSetPrototypes.isEmpty() && (layoutSetPrototype == null))) %>">
				<h3><liferay-ui:message key="pages" /></h3>

				<liferay-ui:panel-container extended="<%= false %>">
					<liferay-ui:panel collapsible="<%= true %>" defaultState='<%= ((group != null) && group.hasLayouts()) ? "open" : "closed" %>' title="pages">
						<c:choose>
							<c:when test="<%= ((group == null) || ((layoutSetPrototype == null) && !group.hasLayouts())) && !layoutSetPrototypes.isEmpty() %>">
								<c:if test="<%= disableLayoutSetPrototypeInput %>">
									<div class="alert alert-info">
										<liferay-ui:message key="you-cannot-apply-a-site-template-because-you-modified-the-display-settings-of-this-site" />
									</div>
								</c:if>

								<aui:select disabled="<%= disableLayoutSetPrototypeInput %>" helpMessage="site-templates-with-an-incompatible-application-adapter-are-disabled" label="site-template" name="publicLayoutSetPrototypeId">
									<aui:option label="none" selected="<%= true %>" value="" />

									<%
									for (LayoutSetPrototype curLayoutSetPrototype : layoutSetPrototypes) {
										UnicodeProperties settingsProperties = curLayoutSetPrototype.getSettingsProperties();

										String servletContextName = settingsProperties.getProperty("customJspServletContextName", StringPool.BLANK);
									%>

										<aui:option data-servletContextName="<%= servletContextName %>" value="<%= curLayoutSetPrototype.getLayoutSetPrototypeId() %>"><%= HtmlUtil.escape(curLayoutSetPrototype.getName(locale)) %></aui:option>

									<%
									}
									%>

								</aui:select>

								<c:choose>
									<c:when test="<%= (group == null) || !group.isStaged() %>">
										<c:choose>
											<c:when test="<%= hasUnlinkLayoutSetPrototypePermission %>">
												<div class="hide" id="<portlet:namespace />layoutSetPrototypeIdOptions">
													<c:if test="<%= disableLayoutSetPrototypeInput %>">
														<div class="alert alert-info">
															<liferay-ui:message key="you-cannot-enable-the-propagation-of-changes-because-you-modified-the-display-settings-of-this-site" />
														</div>
													</c:if>

													<aui:input disabled="<%= disableLayoutSetPrototypeInput %>" helpMessage="enable-propagation-of-changes-from-the-site-template-help" label="enable-propagation-of-changes-from-the-site-template" name="publicLayoutSetPrototypeLinkEnabled" type="checkbox" value="<%= layoutSetPrototypeLinkEnabled %>" />
												</div>
											</c:when>
											<c:otherwise>
												<aui:input name="publicLayoutSetPrototypeLinkEnabled" type="hidden" value="<%= true %>" />
											</c:otherwise>
										</c:choose>
									</c:when>
								</c:choose>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="<%= group != null %>">
										<c:choose>
											<c:when test="<%= group.hasLayouts() %>">
												<liferay-ui:icon
													iconCssClass="icon-search"
													label="<%= true %>"
													message="open-pages"
													method="get"
													target="_blank"
													url="<%= group.getDisplayURL(themeDisplay) %>"
												/>
											</c:when>
											<c:otherwise>
												<liferay-ui:message key="this-site-does-not-have-any-pages" />
											</c:otherwise>
										</c:choose>

										<c:choose>
											<c:when test="<%= (layoutSetPrototype != null) && !group.isStaged() && hasUnlinkLayoutSetPrototypePermission %>">
												<c:if test="<%= disableLayoutSetPrototypeInput %>">
													<div class="alert alert-info">
														<liferay-ui:message key="you-cannot-enable-the-propagation-of-changes-because-you-modified-the-display-settings-of-this-site" />
													</div>
												</c:if>

												<aui:input disabled="<%= disableLayoutSetPrototypeInput %>" label='<%= LanguageUtil.format(request, "enable-propagation-of-changes-from-the-site-template-x", HtmlUtil.escape(layoutSetPrototype.getName(locale)), false) %>' name="publicLayoutSetPrototypeLinkEnabled" type="checkbox" value="<%= layoutSetPrototypeLinkEnabled %>" />

												<div class='<%= layoutSetPrototypeLinkEnabled ? "" : "hide" %>' id="<portlet:namespace/>layoutSetPrototypeMergeAlert">

													<%
													request.setAttribute("edit_layout_set_prototype.jsp-groupId", String.valueOf(group.getGroupId()));
													request.setAttribute("edit_layout_set_prototype.jsp-layoutSet", layoutSet);
													request.setAttribute("edit_layout_set_prototype.jsp-layoutSetPrototype", layoutSetPrototype);
													request.setAttribute("edit_layout_set_prototype.jsp-redirect", currentURL);
													%>

													<liferay-util:include page="/html/portlet/layouts_admin/layout_set_merge_alert.jsp" />
												</div>
											</c:when>
											<c:when test="<%= layoutSetPrototype != null %>">
												<liferay-ui:message arguments="<%= new Object[] {HtmlUtil.escape(layoutSetPrototype.getName(locale))} %>" key="these-pages-are-linked-to-site-template-x" translateArguments="<%= false %>" />

												<aui:input name="publicLayoutSetPrototypeLinkEnabled" type="hidden" value="<%= layoutSetPrototypeLinkEnabled %>" />
											</c:when>
										</c:choose>
									</c:when>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</liferay-ui:panel>
				</liferay-ui:panel-container>

				<%
				Set<String> servletContextNames = CustomJspRegistryUtil.getServletContextNames();
				%>

				<c:if test="<%= servletContextNames.size() > 0 %>">
					<aui:fieldset label="configuration">

						<%
						String customJspServletContextName = StringPool.BLANK;

						if (typeSettingsProperties != null) {
							customJspServletContextName = GetterUtil.getString(typeSettingsProperties.get("customJspServletContextName"));
						}
						%>

						<aui:select helpMessage='<%= LanguageUtil.format(request, "application-adapter-help", "http://www.liferay.com/community/wiki/-/wiki/Main/Application+Adapters", false) %>' label="application-adapter" name="customJspServletContextName">
							<aui:option label="none" value="" />

							<%
							for (String servletContextName : servletContextNames) {
							%>

								<aui:option selected="<%= customJspServletContextName.equals(servletContextName) %>" value="<%= servletContextName %>"><%= CustomJspRegistryUtil.getDisplayName(servletContextName) %></aui:option>

							<%
							}
							%>

						</aui:select>
					</aui:fieldset>
				</c:if>
			</c:when>
			<c:when test="<%= layoutSetPrototype != null %>">
				<aui:fieldset label="pages">
					<aui:input name="layoutSetPrototypeId" type="hidden" value="<%= layoutSetPrototype.getLayoutSetPrototypeId() %>" />

					<aui:field-wrapper label="copy-as">
						<aui:input checked="<%= true %>" helpMessage='<%= LanguageUtil.format(request, "select-this-to-copy-the-pages-of-the-site-template-x-as-pages-for-this-site", HtmlUtil.escape(layoutSetPrototype.getName(locale)), false) %>' label="pages" name="layoutSetVisibility" type="radio" value="0" />
					</aui:field-wrapper>

					<c:choose>
						<c:when test="<%= hasUnlinkLayoutSetPrototypePermission %>">
							<aui:input helpMessage="enable-propagation-of-changes-from-the-site-template-help" label="enable-propagation-of-changes-from-the-site-template" name="layoutSetPrototypeLinkEnabled" type="checkbox" value="<%= true %>" />
						</c:when>
						<c:otherwise>
							<aui:input name="layoutSetPrototypeLinkEnabled" type="hidden" value="<%= true %>" />
						</c:otherwise>
					</c:choose>
				</aui:fieldset>
			</c:when>
		</c:choose>
	</aui:fieldset>

	<%
	List<Group> parentGroups = new ArrayList<Group>();

	if (parentGroup != null) {
		parentGroups.add(parentGroup);
	}
	%>

	<liferay-util:buffer var="removeGroupIcon">
		<liferay-ui:icon
			iconCssClass="icon-unlink"
			label="<%= true %>"
			message="remove"
		/>
	</liferay-util:buffer>

	<h3><liferay-ui:message key="parent-site" /></h3>

	<liferay-ui:search-container
		headerNames="name,type,null"
		id="parentGroupSearchContainer"
	>
		<liferay-ui:search-container-results
			results="<%= parentGroups %>"
			total="<%= parentGroups.size() %>"
		/>

		<liferay-ui:search-container-row
			className="com.liferay.portal.model.Group"
			escapedModel="<%= true %>"
			keyProperty="groupId"
			modelVar="curGroup"
		>
			<portlet:renderURL var="rowURL">
				<portlet:param name="struts_action" value="/sites_admin/edit_site" />
				<portlet:param name="redirect" value="<%= currentURL %>" />
				<portlet:param name="groupId" value="<%= String.valueOf(curGroup.getGroupId()) %>" />
			</portlet:renderURL>

			<liferay-ui:search-container-column-text
				href="<%= rowURL %>"
				name="name"
				value="<%= HtmlUtil.escape(curGroup.getDescriptiveName(locale)) %>"
			/>

			<liferay-ui:search-container-column-text
				name="type"
				value="<%= LanguageUtil.get(request, curGroup.getTypeLabel()) %>"
			/>

			<liferay-ui:search-container-column-text>
				<a class="modify-link" data-rowId="<%= curGroup.getGroupId() %>" href="javascript:;"><%= removeGroupIcon %></a>
			</liferay-ui:search-container-column-text>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator paginate="<%= false %>" />
	</liferay-ui:search-container>

	<liferay-ui:icon
		cssClass="modify-link"
		iconCssClass="icon-search"
		id="selectParentSiteLink"
		label="<%= true %>"
		linkCssClass="btn btn-default"
		message="select"
		url="javascript:;"
	/>

	<br />

	<div class="<%= parentGroups.isEmpty() ? "membership-restriction-container hide" : "membership-restriction-container" %>" id="<portlet:namespace />membershipRestrictionContainer">

		<%
		boolean membershipRestriction = false;

		if ((liveGroup != null) && (liveGroup.getMembershipRestriction() == GroupConstants.MEMBERSHIP_RESTRICTION_TO_PARENT_SITE_MEMBERS)) {
			membershipRestriction = true;
		}
		%>

		<aui:input label="limit-membership-to-members-of-the-parent-site" name="membershipRestriction" type="checkbox" value="<%= membershipRestriction %>" />

		<%
		boolean breadcrumbShowParentGroups = true;

		if (typeSettingsProperties != null) {
			breadcrumbShowParentGroups = PropertiesParamUtil.getBoolean(typeSettingsProperties, request, "breadcrumbShowParentGroups", breadcrumbShowParentGroups);
		}
		%>

		<aui:input label="show-parent-sites-in-the-breadcrumb" name="TypeSettingsProperties--breadcrumbShowParentGroups--" type="checkbox" value="<%= breadcrumbShowParentGroups %>" />
	</div>

	<aui:script>
		function <portlet:namespace />isVisible(currentValue, value) {
			return currentValue != '';
		}

		Liferay.Util.toggleSelectBox('<portlet:namespace />layoutSetPrototypeId', <portlet:namespace />isVisible, '<portlet:namespace />layoutSetPrototypeIdOptions');

		Liferay.Util.toggleBoxes('<portlet:namespace />publicLayoutSetPrototypeLinkEnabled','<portlet:namespace />publicLayoutSetPrototypeMergeAlert');
	</aui:script>

	<aui:script use="liferay-search-container">
		var createURL = function(href, value, onclick) {
			return '<a href="' + href + '"' + (onclick ? ' onclick="' + onclick + '" ' : '') + '>' + value + '</a>';
		};

		A.one('#<portlet:namespace />selectParentSiteLink').on(
			'click',
			function(event) {
				Liferay.Util.selectEntity(
					{
						dialog: {
							constrain: true,
							modal: true
						},
						id: '<portlet:namespace />selectGroup',
						title: '<liferay-ui:message arguments="site" key="select-x" />',

						<portlet:renderURL var="groupSelectorURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
							<portlet:param name="struts_action" value="/sites_admin/select_site" />
							<portlet:param name="groupId" value='<%= (group != null) ? String.valueOf(group.getGroupId()) : "0" %>' />
						</portlet:renderURL>

						uri: '<%= groupSelectorURL.toString() %>'
					},
					function(event) {
						var searchContainer = Liferay.SearchContainer.get('<portlet:namespace />parentGroupSearchContainer');

						var rowColumns = [];

						var href = '<portlet:renderURL><portlet:param name="struts_action" value="/sites_admin/edit_site" /><portlet:param name="redirect" value="<%= currentURL %>" /></portlet:renderURL>&<portlet:namespace />groupId=' + event.groupid;

						rowColumns.push(createURL(href, event.groupdescriptivename));
						rowColumns.push(event.grouptype);
						rowColumns.push('<a class="modify-link" data-rowId="' + event.groupid + '" href="javascript:;"><%= UnicodeFormatter.toString(removeGroupIcon) %></a>');

						searchContainer.deleteRow(1, searchContainer.getData());
						searchContainer.addRow(rowColumns, event.groupid);
						searchContainer.updateDataStore(event.groupid);

						var membershipRestrictionContainer = A.one('#<portlet:namespace />membershipRestrictionContainer');

						membershipRestrictionContainer.show();
					}
				);
			}
		);

		var searchContainer = Liferay.SearchContainer.get('<portlet:namespace />parentGroupSearchContainer');

		searchContainer.get('contentBox').delegate(
			'click',
			function(event) {
				var link = event.currentTarget;

				var tr = link.ancestor('tr');

				searchContainer.deleteRow(tr, link.getAttribute('data-rowId'));

				var membershipRestrictionContainer = A.one('#<portlet:namespace />membershipRestrictionContainer');

				membershipRestrictionContainer.hide();
			},
			'.modify-link'
		);
	</aui:script>
</c:if>