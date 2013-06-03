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

<%@ include file="/html/portlet/dockbar/init.jsp" %>

<%@ page import="com.liferay.portal.plugin.PluginUtil" %>
<%@ page import="com.liferay.portal.util.LayoutLister" %>
<%@ page import="com.liferay.portal.util.LayoutView" %>

<%
Group group = null;

if (layout != null) {
	group = layout.getGroup();
}

LayoutLister layoutLister = new LayoutLister();

String pagesName = null;

long groupId = group.getGroupId();

boolean privateLayout = layout.isPrivateLayout();

String rootNodeName = LanguageUtil.get(pageContext, pagesName);

LayoutView layoutView = layoutLister.getLayoutView(groupId, privateLayout, rootNodeName, locale);

List layoutList = layoutView.getList();

long layoutId = layout.getLayoutId();

String layoutTemplateId = StringPool.BLANK;

request.setAttribute(WebKeys.LAYOUT_LISTER_LIST, layoutList);

Theme selTheme = layout.getTheme();

List<LayoutTemplate> layoutTemplates = LayoutTemplateLocalServiceUtil.getLayoutTemplates(selTheme.getThemeId());

int columnsCount = 2;
%>

<aui:model-context model="<%= Layout.class %>" />

<portlet:actionURL var="editPageURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
	<portlet:param name="struts_action" value="/dockbar/edit_layouts" />
</portlet:actionURL>

<portlet:renderURL var="redirectURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
	<portlet:param name="struts_action" value="/dockbar/edit_layouts" />
</portlet:renderURL>

<aui:form action="<%= editPageURL %>" enctype="multipart/form-data" method="post" name="fm2" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveEntry();" %>'>
	<aui:input id="cmd" name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.ADD %>" />
	<aui:input id="explicitCreation" name="explicitCreation" type="hidden" value="<%= true %>" />
	<aui:input id="groupId" name="groupId" type="hidden" value="<%= groupId %>" />
	<aui:input id="layoutPrototypeId" name="layoutPrototypeId" type="hidden" value="" />
	<aui:input id="privateLayout" name="privateLayout" type="hidden" value="<%= privateLayout %>" />
	<aui:input id="parentPlid" name="parentPlid" type="hidden" value="<%= layout.getParentPlid() %>" />
	<aui:input id="parentLayoutId" name="parentLayoutId" type="hidden" value="<%= layout.getParentLayoutId() %>" />
	<aui:input id="redirect" name="redirect" type="hidden" value="<%= redirectURL.toString() %>" />
	<aui:input id="type" name="type" type="hidden" value="portlet" />

	<div class="row-fluid">

		<span class="span12">
			<aui:input id="addLayoutName" name="name" />
		</span>

		<span class="span12">
			<aui:input id="addLayoutHidden" label="hidden" name="hidden" type="checkbox" />
		</span>

		<span class="span12" id="addPagePanelContainer">
			<liferay-util:include page="/html/portlet/dockbar/search_templates.jsp" />

			<aui:nav cssClass="nav-list no-margin-nav-list" id="templateList">
				<aui:nav-item cssClass="lfr-page-template" data-search="blank">
					<div class="active toggler-header toggler-header-collapsed" data-type="portlet">
						<h5 class="title">Blank (default)</h5>
					</div>

					<div class="toggler-content toggler-content-collapsed">
						<br />
						<%@ include file="/html/portlet/layouts_admin/layout/layout_templates.jspf" %>
					</div>
				</aui:nav-item>

				<%
				List<LayoutPrototype> layoutPrototypes = LayoutPrototypeServiceUtil.search(company.getCompanyId(), Boolean.TRUE, null);
				for (LayoutPrototype layoutPrototype : layoutPrototypes) {

					String name = HtmlUtil.escape(layoutPrototype.getName(user.getLanguageId()));
				%>

					<aui:nav-item cssClass="lfr-page-template" data-search="<%= name %>">
						<div class="toggler-header toggler-header-collapsed" data-prototype-id="<%= layoutPrototype.getLayoutPrototypeId() %>">
							<h5 class="title"><%= name %></h5>
							<%= HtmlUtil.escape(layoutPrototype.getDescription()) %>
						</div>

						<div class="toggler-content toggler-content-collapsed">
							<br />
							<aui:input label='<%= LanguageUtil.get(pageContext, "automatically-apply-changes-done-to-the-page-template") %>' name="layoutPrototypeLinkEnabled" type="checkbox" />
						</div>
					</aui:nav-item>

				<%
				}
				%>

				<%
				for (int i = 0; i < PropsValues.LAYOUT_TYPES.length; i++) {
					String layoutType = LanguageUtil.get(pageContext, "layout.types." + PropsValues.LAYOUT_TYPES[i]);
				%>

					<aui:nav-item cssClass="lfr-page-template" data-search="<%= layoutType %>">
						<div class="toggler-header toggler-header-collapsed" data-type="<%= PropsValues.LAYOUT_TYPES[i] %>">
							<h5 class="title"><%= layoutType %></h5>
						</div>

						<div class="toggler-content toggler-content-collapsed">
							<liferay-util:include page="<%= StrutsUtil.TEXT_HTML_DIR + PortalUtil.getLayoutEditPage(PropsValues.LAYOUT_TYPES[i]) %>" />
						</div>
					</aui:nav-item>

				<%
				}
				%>

			</aui:nav>
		</span>
	</div>

	<div class="pull-right">
		<button class="btn hide">OK</button>
		<button class="btn btn-primary btn-submit" type="submit">Add Page</button>
		<button class="btn" id="<portlet:namespace />cancelAction">Cancel</button>
	</div>
</aui:form>

<%
Layout addedLayout = (Layout)SessionMessages.get(renderRequest, portletDisplay.getId() + "PAGE_ADDED");
%>

<c:if test="<%= addedLayout != null && !addedLayout.isHidden() %>">
	<aui:script use="aui-base">
		var TPL_TAB_LINK = '<li class="lfr-nav-item lfr-nav-deletable lfr-nav-sortable lfr-nav-updateable yui3-dd-drop" aria-selected="true"> <a class="" href="{url}" tabindex="-1"><span> {pageTitle} </span> </a> </li>';

		var tabHtml = A.Lang.sub(
			TPL_TAB_LINK,
			{
				url: '<%= addedLayout.getFriendlyURL() %>',
				pageTitle: A.Lang.String.escapeHTML('<%= addedLayout.getName(locale) %>')
			}
		);

		var nav = A.one('#banner .nav');
		nav.append(tabHtml);
	</aui:script>
</c:if>

<aui:script use="aui-base,aui-parse-content">
	window.<portlet:namespace />saveEntry = function() {
		var title =
		A.io.request(
			document.<portlet:namespace />fm2.action,
			{
				dataType: 'json',
				form: {
					id: document.<portlet:namespace />fm2
				},
				after: {
					success: function(event, id, obj) {
						var response = this.get('responseData');

						var panel = A.one('#<portlet:namespace />addPanelSidebar');

						panel.plug(A.Plugin.ParseContent);

						panel.setContent(response);
					}
				}
			}
		);
	}
</aui:script>

<aui:script use="aui-toggler-delegate">
	new A.TogglerDelegate(
		{
			animated: true,
			closeAllOnExpand: true,
			container: '#addPagePanelContainer',
			content: '.toggler-content',
			expanded: false,
			header: '.toggler-header',
			on: {
				'toggler:expandedChange': function(event) {
					if (event.newVal === true) {
						var header = event.target.get('header');

						var selectedType = header.attr('data-type');

						var selectedPrototypeId = header.attr('data-prototype-id');

						var templateList = A.one('#<portlet:namespace />templateList');

						templateList.all('.active').removeClass('active');

						header.addClass('active');

						A.one('#<portlet:namespace />type').set('value', selectedType);

						A.one('#<portlet:namespace />layoutPrototypeId').set('value', selectedPrototypeId);

						toggleLayoutTypeFields();
					}
				}
			},
			transition: {
				duration: 0.2
			}
		}
	);

	function toggleLayoutTypeFields() {
		A.all('.lfr-page-template').each(
			function(item, index, collection) {
				var header = item.one('.toggler-header');

				var active = header.hasClass('active');

				var disabled = !active;

				item.all('input, select, textarea').set('disabled', disabled);
			}
		);
	}

	A.one('#<portlet:namespace />cancelAction').on(
		'click',
		function(event) {
			var Dockbar = Liferay.Dockbar;

			Dockbar.loadPanel();

			event.preventDefault();
		}
	);

	toggleLayoutTypeFields();
</aui:script>