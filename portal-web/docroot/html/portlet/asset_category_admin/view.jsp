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

<%@ include file="/html/portlet/asset_category_admin/init.jsp" %>

<%
long vocabularyId = ParamUtil.getLong(request, "vocabularyId");
%>

<c:choose>
	<c:when test="<%= vocabularyId > 0 %>">
		<liferay-util:include page="/html/portlet/asset_category_admin/view_categories.jsp" />
	</c:when>
	<c:otherwise>
		<liferay-util:include page="/html/portlet/asset_category_admin/view_vocabularies.jsp" />
	</c:otherwise>
</c:choose>