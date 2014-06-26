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

<%@ include file="/html/portlet/trash/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

String restoreEntryAction = ParamUtil.getString(request, "restoreEntryAction", "/trash/edit_entry");

long trashEntryId = ParamUtil.getLong(request, "trashEntryId");

String className = ParamUtil.getString(request, "className");
long classPK = ParamUtil.getLong(request, "classPK");

TrashEntry entry = null;

if (trashEntryId > 0) {
	entry = TrashEntryLocalServiceUtil.getEntry(trashEntryId);
}
else if (Validator.isNotNull(className) && (classPK > 0)) {
	entry = TrashEntryLocalServiceUtil.fetchEntry(className, classPK);
}

if (entry != null) {
	className = entry.getClassName();
	classPK = entry.getClassPK();
}

boolean multipleConflicts = ParamUtil.getBoolean(request, "multipleConflicts");
String overrideMessage = ParamUtil.getString(request, "overrideMessage");
String renameMessage = ParamUtil.getString(request, "renameMessage");
%>

<portlet:actionURL var="restoreActionURL">
	<portlet:param name="struts_action" value='<%= restoreEntryAction %>' />
</portlet:actionURL>

<aui:form action="<%= restoreActionURL %>" enctype="multipart/form-data" method="post" name="restoreTrashEntryFm" onSubmit="event.preventDefault();">
	<c:choose>
		<c:when test="<%= multipleConflicts %>">
			<%@ include file="/html/portlet/trash/multiple_conflict_resolutor.jspf" %>
		</c:when>
		<c:otherwise>
			<%@ include file="/html/portlet/trash/single_conflict_resolutor.jspf" %>
		</c:otherwise>
	</c:choose>
</aui:form>