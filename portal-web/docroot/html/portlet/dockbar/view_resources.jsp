<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

<%@ include file="/html/portlet/dockbar/init.jsp" %>

<%
boolean viewEntries = ParamUtil.getBoolean(request, "viewEntries");
boolean viewPreview = ParamUtil.getBoolean(request, "viewPreview");
%>

<c:if test="<%= viewEntries %>">

	<%
	int delta = ParamUtil.getInteger(request, "delta", 10);
	String displayStyle = ParamUtil.getString(request, "displayStyle", "list");
	String keywords = ParamUtil.getString(request, "keywords");
	%>

	<div id="<portlet:namespace />entries">
		<liferay-ui:panel collapsible="<%= false %>" cssClass="lfr-component panel-page-category recent" extended="<%= true %>" id="panel-manage-recent" persistState="<%= true %>" title="recent">

			<%
			long[] groupIds = new long[]{scopeGroupId};

			long[] availableClassNameIds = AssetRendererFactoryRegistryUtil.getClassNameIds();

			for (long classNameId : availableClassNameIds) {
				AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(PortalUtil.getClassName(classNameId));

				if (!assetRendererFactory.isSelectable()) {
					availableClassNameIds = ArrayUtil.remove(availableClassNameIds, classNameId);
				}
			}

			AssetEntryQuery assetEntryQuery = new AssetEntryQuery();

			assetEntryQuery.setClassNameIds(availableClassNameIds);
			assetEntryQuery.setEnd(delta);
			assetEntryQuery.setGroupIds(groupIds);
			assetEntryQuery.setKeywords(keywords);
			assetEntryQuery.setOrderByCol1("modifiedDate");
			assetEntryQuery.setOrderByCol2("title");
			assetEntryQuery.setOrderByType1("DESC");
			assetEntryQuery.setOrderByType2("ASC");
			assetEntryQuery.setStart(0);

			int total = 0;
			List<AssetEntry> results = null;

			if (PropsValues.ASSET_PUBLISHER_SEARCH_WITH_INDEX) {
				Hits hits = AssetUtil.search(request, assetEntryQuery, 0, delta);

				total = hits.getLength();

				results = AssetUtil.getAssetEntries(hits);
			}
			else {
				total = AssetEntryServiceUtil.getEntriesCount(assetEntryQuery);

				results = AssetEntryServiceUtil.getEntries(assetEntryQuery);
			}
			%>

			<c:if test='<%= displayStyle.equals("list") %>'>
				<ul>
			</c:if>

			<%
			int j = 0;

			for (AssetEntry assetEntry : results) {
				String className = PortalUtil.getClassName(assetEntry.getClassNameId());
				long classPK = assetEntry.getClassPK();

				AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(className);

				if (assetRendererFactory == null) {
					continue;
				}

				AssetRenderer assetRenderer = null;

				try {
					assetRenderer = assetRendererFactory.getAssetRenderer(classPK);
				}
				catch (Exception e) {
				}

				if ((assetRenderer == null) || !assetRenderer.isDisplayable()) {
					continue;
				}

				Map<String, Object> data = new HashMap<String, Object>();

				data.put("class-name", assetEntry.getClassName());
				data.put("class-pk", assetEntry.getClassPK());
				data.put("portlet-id", assetRenderer.getAddContentPortletId());
			%>

				<c:choose>
					<c:when test='<%= !displayStyle.equals("list") %>'>
						<liferay-ui:app-view-entry
							cssClass='<%= displayStyle.equals("descriptive") ? "has-preview content-shortcut" : "content-shortcut" %>'
							data="<%= data %>"
							description="<%= assetRenderer.getSummary(locale) %>"
							displayStyle="<%= displayStyle %>"
							showCheckbox="<%= false %>"
							showLinkTitle="<%= false %>"
							thumbnailSrc="<%= assetRenderer.getThumbnailPath(liferayPortletRequest) %>"
							title="<%= HtmlUtil.escape(assetRenderer.getTitle(locale)) %>"
							url="javascript:;"
						/>
					</c:when>
					<c:otherwise>
						<li class='<%= (j == 0) ? "first" : "" %>'>
							<a class="has-preview content-shortcut" <%= AUIUtil.buildData(data) %> href="javascript:;">
								<liferay-ui:icon
									label="<%= true %>"
									message="<%= HtmlUtil.escape(assetRenderer.getTitle(locale)) %>"
									src="<%= assetRenderer.getIconPath(liferayPortletRequest) %>"
								/>
							</a>
						</li>
					</c:otherwise>
				</c:choose>

			<%
				j++;
			}
			%>

			<c:if test='<%= displayStyle.equals("list") %>'>
				</ul>
			</c:if>
		</liferay-ui:panel>
	</div>
</c:if>

<c:if test="<%= viewPreview %>">

	<%
	long classPK = ParamUtil.getLong(request, "classPK");
	String className = ParamUtil.getString(request, "className");
	%>

	<c:if test="<%= (classPK > 0) && Validator.isNotNull(className) %>">

		<%
		AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(className, classPK);
		AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(className);
		AssetRenderer assetRenderer = assetRendererFactory.getAssetRenderer(classPK);

		request.setAttribute("view_add_content_application.jsp-assetEntry", assetEntry);
		request.setAttribute("view_add_content_application.jsp-assetRendererFactory", assetRendererFactory);
		request.setAttribute("view_add_content_application.jsp-assetRenderer", assetRenderer);
		%>

		<div id="<portlet:namespace />preview">
			<liferay-util:include page="<%= assetRenderer.getPreviewPath(liferayPortletRequest, liferayPortletResponse) %>" portletId="<%= assetRendererFactory.getPortletId() %>" />
		</div>
	</c:if>
</c:if>