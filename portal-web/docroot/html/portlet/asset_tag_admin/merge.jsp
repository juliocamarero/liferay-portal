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

<%@ include file="/html/portlet/asset_tag_admin/init.jsp" %>

<%
String redirect = ParamUtil.getString(renderRequest, "redirect");

long[] mergeTagIds = StringUtil.split(ParamUtil.getString(renderRequest, "mergeTagIds"), 0L);
%>

<liferay-ui:header
	title="merge-tags"
/>

<portlet:actionURL var="mergeURL">
	<portlet:param name="struts_action" value="/asset_tag_admin/edit_tag" />
	<portlet:param name="redirect" value="<%= redirect %>" />
	<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.MERGE %>" />
	<portlet:param name="mergeTagIds" value="<%= StringUtil.merge(mergeTagIds) %>" />
</portlet:actionURL>

<aui:form action="<%= mergeURL %>">
	<aui:select label="target-tag" name="targetTagId">

		<%
		for (long mergeTagId : mergeTagIds) {
			AssetTag tag = AssetTagLocalServiceUtil.getTag(mergeTagId);
		%>

			<aui:option label="<%= tag.getName() %>" value="<%= tag.getTagId() %>" />

		<%
		}
		%>

	</aui:select>

	<c:if test="<%= PropsValues.ASSET_TAG_PROPERTIES_ENABLED %>">
		<aui:input name="overrideTagsProperties" type="checkbox" />
	</c:if>

	<aui:button-row>
		<aui:button type="submit" />

		<aui:button href="<%= redirect %>" type="cancel" />
	</aui:button-row>
</aui:form>