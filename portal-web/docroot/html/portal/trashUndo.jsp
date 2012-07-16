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

<%
if (SessionMessages.contains(renderRequest, portletDisplay.getId() + SessionMessages.KEY_SUFFIX_DELETE_SUCCESS)) {

	Map<String, String> data = (HashMap<String,String>)SessionMessages.get(renderRequest, WebKeys.TRASHED_ENTRIES);

	if (data != null) {
		String[] keys = (String[])data.keySet().toArray(new String[data.keySet().size()]);

		String struts_action = StringPool.BLANK;

		String undoType = (String)SessionMessages.get(renderRequest, WebKeys.UNDO_TYPE);

		boolean plural = false;

		for (String key : keys) {
			if (data.get(key).contains(StringPool.COMMA)) {
				plural = true;
				break;
			}
		}

		if (undoType == PortletKeys.DOCUMENT_LIBRARY) {
			struts_action = "/document_library/edit_entry";
		}
		else if (undoType.equals(PortletKeys.BLOGS)) {
			if (portletDisplay.getPortletName().equals(PortletKeys.BLOGS_ADMIN)) {
				struts_action = "/blogs_admin/edit_entry";
			}
			else if(portletDisplay.getPortletName().equals(PortletKeys.BLOGS)) {
				struts_action = "/blogs/edit_entry";
			}
		}
		%>

		<div class="portlet-msg-notifier">
			<c:choose>
				<c:when test='<%= plural %>'>
					<liferay-ui:message arguments='<%= new String[] {"javascript:" + renderResponse.getNamespace() + "undoEntries();"} %>' key="the-selected-items-have-been-moved-to-the-recycle-bin.-undo" translateArguments="false" />
				</c:when>
				<c:otherwise>
					<liferay-ui:message arguments='<%= new String[] {"javascript:" + renderResponse.getNamespace() + "undoEntries();"} %>' key="the-selected-item-has-been-moved-to-the-recycle-bin.-undo" translateArguments="false" />
				</c:otherwise>
			</c:choose>
		</div>

		<liferay-portlet:renderURL varImpl="trashUndoURL">
			<portlet:param name="struts_action" value="<%= struts_action %>" />
		</liferay-portlet:renderURL>

		<aui:form action="<%= trashUndoURL.toString() %>" method="get" name="undoForm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" />
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

		<%
		for (String key : keys) {
			%>

			<aui:input name="<%= key %>" type="hidden" />

			<%
		}
		%>

		</aui:form>

		<aui:script>
			Liferay.provide(
				window,
				'<portlet:namespace />undoEntries',
				function() {
					if (confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-undo-your-last-changes") %>')) {
						document.<portlet:namespace />undoForm.method = "post";
						document.<portlet:namespace />undoForm.<portlet:namespace /><%= Constants.CMD %>.value = "<%= Constants.UNDO %>";

						<%
						for (String key : keys) {
							if (key == WebKeys.UNDO_TYPE) {
							continue;
						}
						%>

						document.<portlet:namespace />undoForm.<portlet:namespace /><%= key %>.value = "<%= data.get(key) %>";

						<%
						}
						%>

						submitForm(document.<portlet:namespace />undoForm, "<portlet:actionURL><portlet:param name="struts_action" value="<%= struts_action %>" /></portlet:actionURL>");
					}
				},
				['liferay-util-list-fields']
			);
		</aui:script>

	<%
	}
}
%>