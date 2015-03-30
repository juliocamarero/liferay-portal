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

<%@ include file="/html/taglib/init.jsp" %>

<portlet:defineObjects />

<%
List<SelectableEntry> elements = (List<SelectableEntry>)request.getAttribute("liferay-ui:element-selector:elements");
String hiddenInput = (String)request.getAttribute("liferay-ui:element-selector:hiddenInput");
String id = GetterUtil.getString((String)request.getAttribute("liferay-ui:element-selector:id"));
%>

<div class="lfr-element-selector" id="<%= namespace + id %>elementSelector">
	<aui:input name="<%= hiddenInput %>" type="hidden" />

	<ul class="list-inline list-unstyled selected-items">
		<% for (SelectableEntry entry : elements) {
			if (entry.getSelected()) {
		%>

		<li class="list-entry">
			<span><%= LanguageUtil.get(request, entry.getLabel()) %></span>
			<aui:button cssClass="remove-button" icon="icon-remove" value="" />
		</li>

		<%
			}
		}
		%>
	</ul>

	<aui:button cssClass="select-button" name='<%= id + "selectButton" %>' value="select" />
</div>

<aui:script sandbox="<%= true %>">
	var elementsData = [];

	<% for (SelectableEntry entry : elements) {
	%>

	elementsData.push({
		icon: '<%= entry.getIcon() %>',
		key: '<%= entry.getKey() %>',
		label: '<%= LanguageUtil.get(request, entry.getLabel()) %>',
		selected: <%= entry.getSelected() %>
	});

	<%
	}
	%>

	var renderDialogBody = function(dialog) {
		var bodyNode = $(dialog.bodyNode);

		var bodyContent = '<div class="lfr-element-selector row"><ul class="list-unstyled select-list">'

		$.each(
			elementsData,
			function(index, item) {
				bodyContent += '<li class="col-md-3 list-entry">';
				bodyContent += '<div class="content">',
				bodyContent += '<label style="width: 100%; height: 100%;">';
				bodyContent += '<input class="switch" type="checkbox"' + (item.selected ? 'checked ' : '') + ' />';
				bodyContent += '<span aria-hidden="true" class="switch-bar">';
				bodyContent += '<span class="switch-toggle" data-label-on="' + item.label + '" data-label-off="' + item.label + '">';
				bodyContent += '<span class="switch-icon-on switch-icon icon-ok-circle"></span>';
				bodyContent += '<span class="switch-icon-off switch-icon icon-' + item.icon + '"></span>';
				bodyContent += '</span>';
				bodyContent += '</span>';
				bodyContent += '</label>';
				bodyContent += '</div>';
				bodyContent += '</li>';
			}
		);

		bodyContent += '</ul></div>';

		dialog.bodyNode.empty();
		dialog.bodyNode.append(bodyContent);

		dialog.footerNode.addClass('lfr-element-selector');
	};

	var selectorConfig = {
		dialog: {
			'toolbars.footer': [
				{
					cssClass: 'btn-primary done-button',
					label: '<%= UnicodeLanguageUtil.get(request, "done") %>',
					on: {
						click: function() {
							var dialog = Liferay.Util.getWindow('<%= namespace + id %>ElementSelector').hide();

							var selectedElements = $(dialog.bodyNode._node).find('input:checked');
						}
					}
				},
				{
					cssClass: 'cancel-button',
					label: '<%= UnicodeLanguageUtil.get(request, "cancel") %>',
					on: {
						click: function() {
							Liferay.Util.getWindow('<%= namespace + id %>ElementSelector').hide();
						}
					}
				}
			],
			width: 580
		},
		id: '<%= namespace + id %>ElementSelector',
		title: 'Select Elements'
		//title: '<%= UnicodeLanguageUtil.get(request, "select-elements") %>'
	};

	$('#<portlet:namespace /><%= id %>elementSelector').on(
		'click',
		'.remove-button',
		function(event) {
			event.currentTarget.parentNode.remove();
		}
	);

	$('#<portlet:namespace /><%= id %>selectButton').on(
		'click',
		function() {
			Liferay.Util.openWindow(selectorConfig, renderDialogBody);
		}
	);
</aui:script>