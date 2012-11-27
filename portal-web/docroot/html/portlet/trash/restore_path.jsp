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

<%@ include file="/html/portlet/trash/init.jsp" %>

<c:if test="<%= SessionMessages.contains(renderRequest, portletDisplay.getId() + SessionMessages.KEY_SUFFIX_DELETE_SUCCESS_DATA) %>">
	<div class="portlet-msg-success">

		<%
		Map<String, List<String>> data = (HashMap<String, List<String>>)SessionMessages.get(renderRequest, portletDisplay.getId() + SessionMessages.KEY_SUFFIX_DELETE_SUCCESS_DATA);

		List<String> restoreLinks = data.get("restoreLinks");
		List<String> restoreMessages = data.get("restoreMessages");
		%>

		<c:choose>
			<c:when test="<%= (data != null) && (restoreLinks != null) && (restoreMessages != null) && (restoreLinks.size() > 0) && (restoreMessages.size() > 0) %>">

				<%
				StringBundler sb = new StringBundler(5 * restoreMessages.size());

				for (int i = 0; i < restoreLinks.size(); i++) {
					sb.append("<a href=\"");
					sb.append(restoreLinks.get(i));
					sb.append("\">");
					sb.append(restoreMessages.get(i));
					sb.append("</a> ");
				}
				%>

				<liferay-ui:message arguments="<%= sb.toString() %>" key="the-item-has-been-restored-to-x" />
			</c:when>
			<c:otherwise>
				<liferay-ui:message key="the-item-has-been-restored" />
			</c:otherwise>
		</c:choose>
	</div>
</c:if>