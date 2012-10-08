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

<%
long containerModelId = 0;
long trashEntryId = ParamUtil.getLong(request, "trashEntryId");
String className = ParamUtil.getString(request, "entryClassName");

TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(className);

ContainerModel containerModel = (ContainerModel)request.getAttribute(WebKeys.TRASH_DESTINATION_CONTAINER_MODEL);

if (containerModel != null) {
	containerModelId = containerModel.getContainerModelId();
}

PortletURL containerURL = renderResponse.createRenderURL();

containerURL.setParameter("struts_action", "/trash/select_container");
containerURL.setParameter("trashEntryId", String.valueOf(trashEntryId));
containerURL.setParameter("entryClassName", className);

TrashUtil.addContainerBreadcrumbEntries(request, trashHandler, containerURL);

String containerModelName = LanguageUtil.get(pageContext, trashHandler.getContainerModelName());
String subContainerModelNamePlural = LanguageUtil.get(pageContext, trashHandler.getSubContainerModelName() + "s");

String header = LanguageUtil.format(pageContext, "select-destination-x", containerModelName);
String headerName2 = LanguageUtil.format(pageContext, "num-of-x", subContainerModelNamePlural);
%>

<aui:form method="post" name="fm">
	<liferay-ui:header
		title="<%= header %>"
	/>

	<liferay-ui:breadcrumb showGuestGroup="<%= false %>" showLayout="<%= false %>" showParentGroups="<%= false %>" />

	<%
	containerURL.setParameter("containerModelId", String.valueOf(containerModelId));
	%>

	<liferay-ui:search-container
		searchContainer="<%= new SearchContainer(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, containerURL, null, null) %>"
	>

		<liferay-ui:search-container-results>

			<%
			pageContext.setAttribute("results", trashHandler.getContainerModels(trashEntryId, containerModelId, searchContainer.getStart(), searchContainer.getEnd()));
			pageContext.setAttribute("total", trashHandler.getContainerModelsCount(trashEntryId, containerModelId));
			%>

		</liferay-ui:search-container-results>

		<liferay-ui:search-container-row
			className="com.liferay.portal.model.ContainerModel"
			keyProperty="containerModelId"
			modelVar="curContainerModel"
		>

			<%
			containerURL.setParameter("containerModelId", String.valueOf(curContainerModel.getContainerModelId()));
			%>

			<liferay-ui:search-container-column-text
				name="<%= containerModelName %>"
			>
				<c:choose>
					<c:when test="<%= curContainerModel.getContainerModelId() > 0 %>">

						<%
						TrashHandler containerTrashHandler = TrashHandlerRegistryUtil.getTrashHandler(((BaseModel)curContainerModel).getModelClassName());

						TrashRenderer containerTrashRenderer = containerTrashHandler.getTrashRenderer(curContainerModel.getContainerModelId());
						%>

						<liferay-ui:icon label="<%= true %>" message="<%= curContainerModel.getContainerModelName() %>" src="<%= containerTrashRenderer.getIconPath(renderRequest) %>" url="<%= containerURL.toString() %>" />
					</c:when>
					<c:otherwise>
						<%= curContainerModel.getContainerModelName() %>
					</c:otherwise>
				</c:choose>
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				name="<%= headerName2 %>"
				value="<%= String.valueOf(trashHandler.getContainerModelsCount(trashEntryId, curContainerModel.getContainerModelId())) %>"
			/>

			<c:choose>
				<c:when test="<%= containerURL != null %>">

					<%
					StringBuilder sb = new StringBuilder();

					sb.append("opener.");
					sb.append(renderResponse.getNamespace());
					sb.append("selectContainer(");
					sb.append(trashEntryId);
					sb.append(", ");
					sb.append(curContainerModel.getContainerModelId());
					sb.append("); window.close();");
					%>

					<liferay-ui:search-container-column-button
						align="right"
						href="<%= sb.toString() %>"
						name="choose"
					/>
				</c:when>
				<c:otherwise>
					<liferay-ui:search-container-column-text> </liferay-ui:search-container-column-text>
				</c:otherwise>
			</c:choose>
		</liferay-ui:search-container-row>

		<aui:button-row>

			<%
			String taglibSelectOnClick = "opener." + renderResponse.getNamespace() + "selectContainer("+ trashEntryId + ", " + containerModelId + "); window.close();";

			String taglibSelectName = LanguageUtil.format(pageContext, "choose-this-x", containerModelName);
			%>

			<aui:button onClick="<%= taglibSelectOnClick %>" value="<%=taglibSelectName %>" />
		</aui:button-row>

		<div class="separator"><!-- --></div>

		<liferay-ui:search-iterator />
	</liferay-ui:search-container>
</aui:form>