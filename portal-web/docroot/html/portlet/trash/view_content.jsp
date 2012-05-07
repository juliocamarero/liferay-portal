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

<div class="asset-content">

		<%
		long assetEntryId = ParamUtil.getLong(request, "assetEntryId");

		AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(assetEntryId);

		TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(assetEntry.getClassName());

		TrashRenderer trashRenderer = trashHandler.getTrashRenderer(assetEntry.getClassPK());

		String path = trashRenderer.render(renderRequest, renderResponse, "default");
		String redirect = ParamUtil.getString(request, "redirect");
		String title = trashRenderer.getTitle(locale);
		%>

		<liferay-ui:header
			backURL="<%= redirect %>"
			localizeTitle="<%= false %>"
			title="<%= title %>"
		/>

		<liferay-util:include page="<%= path %>" portletId="<%= trashRenderer.getPortletId() %>" />

</div>