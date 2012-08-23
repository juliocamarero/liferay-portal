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

<%@ include file="/html/portlet/asset_publisher/init.jsp" %>

<%
boolean showIconLabel = ((Boolean)request.getAttribute("view.jsp-showIconLabel")).booleanValue();

boolean showEditURL = ParamUtil.getBoolean(request, "showEditURL", true);

AssetRenderer assetRenderer = (AssetRenderer)request.getAttribute("view.jsp-assetRenderer");

PortletURL redirectURL = renderResponse.createRenderURL();

redirectURL.setWindowState(LiferayWindowState.POP_UP);
redirectURL.setParameter("struts_action", "/asset_publisher/add_asset_redirect");

PortletURL editPortletURL = assetRenderer.getURLEdit(liferayPortletRequest, liferayPortletResponse, true, redirectURL, LiferayWindowState.POP_UP);
%>

<c:if test="<%= showEditURL && editPortletURL != null %>">
	<div class="lfr-meta-actions asset-actions">

		<%
		String taglibEditURL = "javascript:Liferay.Util.openWindow({dialog: {width: 960}, id: '" + renderResponse.getNamespace() + "editAsset', title: '" + LanguageUtil.format(pageContext, "edit-x", HtmlUtil.escape(assetRenderer.getTitle(locale))) + "', uri:'" + HtmlUtil.escapeURL(editPortletURL.toString()) + "'});";
		%>

		<liferay-ui:icon
			image="edit"
			label="<%= showIconLabel %>"
			message='<%= showIconLabel ? LanguageUtil.format(pageContext, "edit-x-x", new Object[] {"aui-helper-hidden-accessible", HtmlUtil.escape(assetRenderer.getTitle(locale))}) : LanguageUtil.format(pageContext, "edit-x", HtmlUtil.escape(assetRenderer.getTitle(locale))) %>'
			url="<%= taglibEditURL %>"
		/>
	</div>
</c:if>