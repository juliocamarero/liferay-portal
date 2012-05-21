<%--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

<%@ include file="/html/portlet/layouts_admin/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

String redirectWindowState = ParamUtil.getString(request, "redirectWindowState");

String cmd = ParamUtil.getString(request, Constants.CMD, Constants.EXPORT);

Group group = (Group)request.getAttribute(WebKeys.GROUP);

long groupId = ParamUtil.getLong(request, "groupId");

Group liveGroup = group;

if (group.isStagingGroup()) {
	liveGroup = group.getLiveGroup();
}

long liveGroupId = ParamUtil.getLong(request, "liveGroupId");

boolean privateLayout = ParamUtil.getBoolean(request, "privateLayout");

String rootNodeName = ParamUtil.getString(request, "rootNodeName");

boolean layoutPrototypeExportImport = ParamUtil.getBoolean(request, "layoutPrototypeExportImport");
long layoutPrototypeId = ParamUtil.getLong(request, "layoutPrototypeId");

List<Portlet> portletsList = new ArrayList<Portlet>();
Set<String> portletIdsSet = new HashSet<String>();

for (Layout curLayout : LayoutLocalServiceUtil.getLayouts(liveGroupId, privateLayout)) {
	if (curLayout.isTypePortlet()) {
		LayoutTypePortlet curLayoutTypePortlet = (LayoutTypePortlet)curLayout.getLayoutType();

		for (String portletId : curLayoutTypePortlet.getPortletIds()) {
			Portlet portlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), portletId);

			if (portlet == null) {
				continue;
			}

			PortletDataHandler portletDataHandler = portlet.getPortletDataHandlerInstance();

			if ((portletDataHandler != null) && !portletIdsSet.contains(portlet.getRootPortletId())) {
				portletIdsSet.add(portlet.getRootPortletId());

				portletsList.add(portlet);
			}
		}
	}
}

List<Portlet> alwaysExportablePortlets = LayoutExporter.getAlwaysExportablePortlets(company.getCompanyId());

for (Portlet alwaysExportablePortlet : alwaysExportablePortlets) {
	if (!portletIdsSet.contains(alwaysExportablePortlet.getRootPortletId())) {
		portletIdsSet.add(alwaysExportablePortlet.getRootPortletId());

		portletsList.add(alwaysExportablePortlet);
	}
}

portletsList = ListUtil.sort(portletsList, new PortletTitleComparator(application, locale));
%>

<aui:form cssClass="lfr-export-dialog" method="post" name="fm1">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= cmd %>" />

	<c:choose>
		<c:when test="<%= cmd.equals(Constants.EXPORT) %>">
			<%@ include file="/html/portlet/layouts_admin/export_import_options.jspf" %>

			<aui:button-row>
				<aui:button type="submit" value="export" />
			</aui:button-row>
		</c:when>
		<c:when test="<%= cmd.equals(Constants.IMPORT) %>">
			<liferay-ui:error exception="<%= LARFileException.class %>" message="please-specify-a-lar-file-to-import" />
			<liferay-ui:error exception="<%= LARTypeException.class %>" message="please-import-a-lar-file-of-the-correct-type" />
			<liferay-ui:error exception="<%= LayoutImportException.class %>" message="an-unexpected-error-occurred-while-importing-your-file" />

			<liferay-ui:error exception="<%= LayoutPrototypeException.class %>">

				<%
				LayoutPrototypeException lpe = (LayoutPrototypeException)errorException;
				%>

				<liferay-ui:message key="the-lar-file-could-not-be-imported-because-it-requires-page-templates-or-site-templates-that-could-not-be-found.-please-import-the-following-templates-manually" />

				<ul>

					<%
					List<Tuple> missingLayoutPrototypes = lpe.getMissingLayoutPrototypes();

					for (Tuple missingLayoutPrototype : missingLayoutPrototypes) {
						String layoutPrototypeClassName = (String)missingLayoutPrototype.getObject(0);
						String layoutPrototypeUuid = (String)missingLayoutPrototype.getObject(1);
						String layoutPrototypeName = (String)missingLayoutPrototype.getObject(2);
					%>

						<li>
							<%= ResourceActionsUtil.getModelResource(locale, layoutPrototypeClassName) %>: <strong><%= layoutPrototypeName %></strong> (<%= layoutPrototypeUuid %>)
						</li>

					<%
					}
					%>

				</ul>
			</liferay-ui:error>

			<c:choose>
				<c:when test="<%= (layout.getGroupId() != groupId) || (layout.isPrivateLayout() != privateLayout) %>">
					<%@ include file="/html/portlet/layouts_admin/export_import_options.jspf" %>

					<aui:button-row>
						<aui:button type="submit" value="import" />
					</aui:button-row>
				</c:when>
				<c:otherwise>
					<liferay-ui:message key="import-from-within-the-target-site-can-cause-conflicts" />
				</c:otherwise>
			</c:choose>
		</c:when>
	</c:choose>
</aui:form>

<c:if test='<%= cmd.equals(Constants.IMPORT) && SessionMessages.contains(renderRequest, "request_processed") %>'>
	<aui:script>
		var opener = Liferay.Util.getOpener();

		if (opener.<portlet:namespace />saveLayoutset) {
			Liferay.fire(
				'closeWindow',
				{
					id: '<portlet:namespace />importDialog'
				}
			);

			opener.<portlet:namespace />saveLayoutset('view');
		}
	</aui:script>
</c:if>

<aui:script use="aui-base,aui-loading-mask,json-stringify">
	var form = A.one('#<portlet:namespace />fm1');

	form.on(
		'submit',
		function(event) {
			event.preventDefault();

			<c:choose>
				<c:when test="<%= cmd.equals(Constants.EXPORT) %>">
					var layoutIds = [];

					<c:choose>
						<c:when test="<%= layoutPrototypeExportImport %>">
							layoutIds.push(
								{
									includeChildren: false,
									layoutId: <%= layoutPrototypeId %>
								}
							);
						</c:when>
						<c:otherwise>
							var layoutsExportTreeOutput = A.one('#<portlet:namespace />layoutsExportTreeOutput');

							if (layoutsExportTreeOutput) {
								var treeView = layoutsExportTreeOutput.getData('treeInstance');

								var regexLayoutId = /layoutId_(\d+)/;

								treeView.eachChildren(
									function(item, index, collection) {
										if (item.isChecked()) {
											var match = regexLayoutId.exec(item.get('id'));

											if (match) {
												layoutIds.push(
													{
														includeChildren: !item.hasChildNodes(),
														layoutId: match[1]
													}
												);
											}
										}
									},
									true
								);
							}
						</c:otherwise>
					</c:choose>

					var layoutIdsInput = A.one('#<portlet:namespace />layoutIds');

					if (layoutIdsInput) {
						layoutIdsInput.val(A.JSON.stringify(layoutIds));
					}

					<portlet:actionURL var="exportPagesURL">
						<portlet:param name="struts_action" value="/layouts_admin/export_layouts" />
						<portlet:param name="groupId" value="<%= (layoutPrototypeExportImport) ? String.valueOf(groupId) : String.valueOf(liveGroupId) %>" />
						<portlet:param name="privateLayout" value="<%= String.valueOf(privateLayout) %>" />
						<portlet:param name="exportLAR" value="<%= Boolean.TRUE.toString() %>" />
						<portlet:param name="layoutPrototypeId" value="<%= String.valueOf(layoutPrototypeId) %>" />
						<portlet:param name="layoutPrototypeExportImport" value="<%= String.valueOf(layoutPrototypeExportImport) %>" />
					</portlet:actionURL>

					submitForm(form, '<%= exportPagesURL + "&etag=0" %>', false);
				</c:when>
				<c:otherwise>
					<portlet:actionURL var="importPagesURL">
						<portlet:param name="struts_action" value="/layouts_admin/import_layouts" />
						<portlet:param name="groupId" value="<%= String.valueOf(groupId) %>" />
						<portlet:param name="privateLayout" value="<%= String.valueOf(privateLayout) %>" />
						<portlet:param name="layoutPrototypeId" value="<%= String.valueOf(layoutPrototypeId) %>" />
						<portlet:param name="layoutPrototypeExportImport" value="<%= String.valueOf(layoutPrototypeExportImport) %>" />
					</portlet:actionURL>

					form.attr('encoding', 'multipart/form-data');

					submitForm(form, '<%= importPagesURL %>');
				</c:otherwise>
			</c:choose>
		}
	);

	var toggleHandlerControl = function(item, index, collection) {
		var container = item.ancestor('.handler-control').one('ul');

		if (container) {
			var checked = item.get('checked');

			container.toggle(checked);

			container.all(':checkbox').attr('checked', checked);
		}
	};

	var checkboxes = A.all('.handler-control :checkbox');

	checkboxes.filter(':not(:checked)').each(toggleHandlerControl);

	checkboxes.detach('click');

	checkboxes.on(
		'click',
		function(event) {
			toggleHandlerControl(event.currentTarget);
		}
	);
</aui:script>