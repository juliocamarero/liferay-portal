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

<%@ include file="/html/taglib/ui/categorization_filter/init.jsp" %>

<portlet:renderURL var="viewURLWithoutCategory">
	<portlet:param name="categoryId" value="0" />
</portlet:renderURL>
<portlet:renderURL var="viewURLWithoutTag">
	<portlet:param name="tag" value="" />
</portlet:renderURL>

<%
String assetType = (String)request.getAttribute("liferay-ui:categorization-filter:assetType");
PortletURL portletURL = (PortletURL)request.getAttribute("liferay-ui:categorization-filter:portletURL");

if (Validator.isNull(assetType)) {
	assetType = "content";
}

if (Validator.isNull(portletURL)) {
	portletURL = renderResponse.createRenderURL();
}

long assetCategoryId = ParamUtil.getLong(request, "categoryId");

String assetCategoryTitle = null;
String assetVocabularyTitle = null;

if (assetCategoryId != 0) {
	AssetCategory assetCategory = AssetCategoryLocalServiceUtil.getAssetCategory(assetCategoryId);

	assetCategory = assetCategory.toEscapedModel();

	assetCategoryTitle = assetCategory.getTitle(locale);

	AssetVocabulary assetVocabulary = AssetVocabularyLocalServiceUtil.getAssetVocabulary(assetCategory.getVocabularyId());

	assetVocabulary = assetVocabulary.toEscapedModel();

	assetVocabularyTitle = assetVocabulary.getTitle(locale);
}

boolean isCategory = Validator.isNotNull(assetCategoryTitle);

String assetTagName = ParamUtil.getString(request, "tag");

boolean isTag = Validator.isNotNull(assetTagName);

String openSpanTag = "<span class=\"aui-textboxlistentry\">";
String openATag = "<a href=\"";
String closeTag = "\"><span class=\"aui-icon aui-icon-close aui-textboxlistentry-close\"></span></a></span>";

String assetTypeKey = "";
String message = "";

String removeCategoryHtml = "";

if (isCategory) {
	if (!isTag) {
		assetTypeKey = assetType.concat("-with-x-x");
		
		AssetUtil.addPortletBreadcrumbEntries(assetCategoryId, request, portletURL);
	}
	
	PortalUtil.addPageKeywords(assetCategoryTitle, request);
	
	StringBundler removeCategoryHtmlSb = new StringBundler(5);
	removeCategoryHtmlSb.append(openSpanTag);
	removeCategoryHtmlSb.append(assetCategoryTitle);
	removeCategoryHtmlSb.append(openATag);
	removeCategoryHtmlSb.append(viewURLWithoutCategory);
	removeCategoryHtmlSb.append(closeTag);
	
	removeCategoryHtml = removeCategoryHtmlSb.toString();
}

String removeTagHtml = "";

if (isTag) {
	if (!isCategory) {
		assetTypeKey = assetType.concat("-with-tag-x");
		
		PortalUtil.addPortletBreadcrumbEntry(request, assetTagName, currentURL);
	}
	
	PortalUtil.addPageKeywords(assetTagName, request);
	
	StringBundler removeTagHtmlSb = new StringBundler(5);
	removeTagHtmlSb.append(openSpanTag);
	removeTagHtmlSb.append(assetTagName);
	removeTagHtmlSb.append(openATag);
	removeTagHtmlSb.append(viewURLWithoutTag);
	removeTagHtmlSb.append(closeTag);
	
	removeTagHtml = removeTagHtmlSb.toString();
}

if (isCategory && isTag) {
	assetTypeKey = assetType.concat("-with-x-x-and-tag-x");
	
	AssetUtil.addPortletBreadcrumbEntries(assetCategoryId, request, portletURL);

	PortalUtil.addPortletBreadcrumbEntry(request, assetTagName, currentURL);
}

%>

<c:choose>
	<c:when test="<%= isCategory && isTag %>">
		<h1 class="entry-title">
			<liferay-ui:message arguments="<%= new String[] {assetVocabularyTitle, removeCategoryHtml, removeTagHtml.toString()} %>" key="<%=assetTypeKey %>" />		
		</h1>
	</c:when>
	<c:otherwise>
		<c:if test="<%= isCategory  %>">	
			<h1 class="entry-title">		
				<liferay-ui:message arguments="<%= new String[] {assetVocabularyTitle, removeCategoryHtml} %>" key="<%=assetTypeKey %>" />			
			</h1>		
		</c:if>
		<c:if test="<%= isTag %>">		
			<h1 class="entry-title">
				<liferay-ui:message arguments="<%= removeTagHtml %>" key="<%=assetTypeKey %>" />
			</h1>
		</c:if>
	</c:otherwise>
</c:choose>