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

<%@ include file="/html/portlet/document_selector/init.jsp" %>

<%
long groupId = ParamUtil.getLong(request, "groupId");

String eventName = ParamUtil.getString(request, "eventName");

Group group = GroupLocalServiceUtil.fetchGroup(groupId);

request.setAttribute(WebKeys.GROUP, group);

String tabNames = "";

if (group.getPublicLayoutsPageCount() > 0) {
	tabNames += "public-pages,";
}

if (group.getPrivateLayoutsPageCount() > 0) {
	tabNames += "private-pages";
}

boolean showGroupsSelector = ParamUtil.getBoolean(request, "showGroupsSelector");
%>

<c:if test="<%= showGroupsSelector %>">
	<liferay-util:include page="/html/portlet/document_selector/group_selector.jsp" />
</c:if>

<liferay-ui:tabs names="<%= tabNames %>" refresh="false">
	<c:if test="<%= group.getPublicLayoutsPageCount() > 0 %>">
		<liferay-ui:section>
			<div>
				<liferay-util:include page="/html/portlet/layouts_admin/tree_js.jsp">
					<liferay-util:param name="draggableTree" value="<%= Boolean.FALSE.toString() %>" />
					<liferay-util:param name="expandFirstNode" value="<%= Boolean.TRUE.toString() %>" />
					<liferay-util:param name="groupId" value="<%= String.valueOf(groupId) %>" />
					<liferay-util:param name="saveState" value="<%= Boolean.FALSE.toString() %>" />
					<liferay-util:param name="treeId" value="treeContainerPublicPages" />
				</liferay-util:include>
			</div>
		</liferay-ui:section>
	</c:if>

	<c:if test="<%= group.getPrivateLayoutsPageCount() > 0 %>">
		<liferay-ui:section>
			<div>
				<liferay-util:include page="/html/portlet/layouts_admin/tree_js.jsp">
					<liferay-util:param name="draggableTree" value="<%= Boolean.FALSE.toString() %>" />
					<liferay-util:param name="expandFirstNode" value="<%= Boolean.TRUE.toString() %>" />
					<liferay-util:param name="groupId" value="<%= String.valueOf(groupId) %>" />
					<liferay-util:param name="saveState" value="<%= Boolean.FALSE.toString() %>" />
					<liferay-util:param name="tabs1" value="private-pages" />
					<liferay-util:param name="treeId" value="treeContainerPrivatePages" />
				</liferay-util:include>
			</div>
		</liferay-ui:section>
	</c:if>
</liferay-ui:tabs>

<div class="alert" id="<portlet:namespace />selectPageMessage">

	<%
	String ckEditorFuncNum = DocumentSelectorUtil.getCKEditorFuncNum(request);

	Map<String, String> params = new HashMap<String, String>();

	params.put("ckeditorfuncnum", ckEditorFuncNum);
	%>

	<aui:button cssClass="selector-button" data="<%= params %>" disabled="<%= true %>" value="choose" />

	<span class="selected-page-message">
		<liferay-ui:message key="there-is-no-selected-page" />
	</span>
</div>

<aui:script use="aui-base">
	var Util = Liferay.Util;

	var bindTreeUI = function(containerId) {
		var container = A.one('#<portlet:namespace />' + containerId);

		if (container) {
			container.swallowEvent('click', true);

			var tree = container.getData('treeInstance');

			tree.after('lastSelectedChange', setSelectedPage);
		}
	};

	var getChosenPagePath = function(node) {
		var buffer = [];

		if (A.instanceOf(node, A.TreeNode)) {
			var labelText = Util.escapeHTML(node.get('labelEl').text());

			buffer.push(labelText);

			node.eachParent(
				function(treeNode) {
					var labelEl = treeNode.get('labelEl');

					if (labelEl) {
						labelText = Util.escapeHTML(labelEl.text());

						buffer.unshift(labelText);
					}
				}
			);
		}

		return buffer.join(' > ');
	};

	var setSelectedPage = function(event) {
		var disabled = true;

		var messageText = '<%= UnicodeLanguageUtil.get(pageContext, "there-is-no-selected-page") %>';

		var messageType = 'alert';

		var lastSelectedNode = event.newVal;

		var labelEl = lastSelectedNode.get('labelEl');

		var link = labelEl.one('a');

		var url = link.attr('data-url');

		var selectPageMessage = A.one('#<portlet:namespace />selectPageMessage');

		var button = selectPageMessage.one('.selector-button');

		if (link && url) {
			disabled = false;

			messageText = getChosenPagePath(lastSelectedNode);

			messageType = 'info';

			button.attr('data-url', url);
		}

		Liferay.Util.toggleDisabled(button, disabled);

		selectPageMessage.one('.selected-page-message').html(messageText);

		selectPageMessage.attr('className', 'alert alert-' + messageType);
	};

	<c:if test="<%= group.getPublicLayoutsPageCount() > 0 %>">
		bindTreeUI('treeContainerPublicPagesOutput');
	</c:if>

	<c:if test="<%= group.getPrivateLayoutsPageCount() > 0 %>">
		bindTreeUI('treeContainerPrivatePagesOutput');
	</c:if>

	Liferay.Util.selectEntityHandler('#<portlet:namespace />selectPageMessage', '<%= HtmlUtil.escapeJS(eventName) %>');
</aui:script>