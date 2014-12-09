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

<%@ include file="/html/portlet/users_admin/init.jsp" %>

<%
User selUser = (User)request.getAttribute("user.selUser");

List<LayoutSetPrototype> layoutSetPrototypes = LayoutSetPrototypeServiceUtil.search(company.getCompanyId(), Boolean.TRUE, null);

LayoutSet layoutSet = null;
LayoutSetPrototype layoutSetPrototype = null;
boolean layoutSetPrototypeLinkEnabled = true;

boolean hasGroupUpdatePermission = true;

if (selUser != null) {
	Group userGroup = selUser.getGroup();

	hasGroupUpdatePermission = GroupPermissionUtil.contains(themeDisplay.getPermissionChecker(), userGroup.getGroupId(), ActionKeys.UPDATE);

	if (userGroup != null) {
		try {
			LayoutLocalServiceUtil.getLayouts(userGroup.getGroupId(), true, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

			layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(userGroup.getGroupId(), false);

			layoutSetPrototypeLinkEnabled = layoutSet.isLayoutSetPrototypeLinkEnabled();

			String layoutSetPrototypeUuid = layoutSet.getLayoutSetPrototypeUuid();

			if (Validator.isNotNull(layoutSetPrototypeUuid)) {
				layoutSetPrototype = LayoutSetPrototypeLocalServiceUtil.getLayoutSetPrototypeByUuidAndCompanyId(layoutSetPrototypeUuid, company.getCompanyId());
			}
		}
		catch (Exception e) {
		}
	}
}
%>

<h3><liferay-ui:message key="personal-site" /></h3>

<aui:fieldset>

	<%
	boolean hasUnlinkLayoutSetPrototypePermission = PortalPermissionUtil.contains(permissionChecker, ActionKeys.UNLINK_LAYOUT_SET_PROTOTYPE);
	%>

	<c:choose>
		<c:when test="<%= PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_ENABLED && hasGroupUpdatePermission && ((selUser == null) || ((layoutSetPrototype == null) && (selUser.getPublicLayoutsPageCount() == 0))) && !layoutSetPrototypes.isEmpty() %>">
			<aui:select label="pages" name="publicLayoutSetPrototypeId">
				<aui:option label="none" selected="<%= true %>" value="" />

				<%
				for (LayoutSetPrototype curLayoutSetPrototype : layoutSetPrototypes) {
				%>

					<aui:option label="<%= HtmlUtil.escape(curLayoutSetPrototype.getName(locale)) %>" value="<%= curLayoutSetPrototype.getLayoutSetPrototypeId() %>" />

				<%
				}
				%>

			</aui:select>

			<c:choose>
				<c:when test="<%= hasUnlinkLayoutSetPrototypePermission %>">
					<div class="hide" id="<portlet:namespace />layoutSetPrototypeIdOptions">
						<aui:input helpMessage="enable-propagation-of-changes-from-the-site-template-help" label="enable-propagation-of-changes-from-the-site-template" name="publicLayoutSetPrototypeLinkEnabled" type="checkbox" value="<%= layoutSetPrototypeLinkEnabled %>" />
					</div>
				</c:when>
				<c:otherwise>
					<aui:input name="publicLayoutSetPrototypeLinkEnabled" type="hidden" value="<%= true %>" />
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<aui:field-wrapper label="pages">
				<c:choose>
					<c:when test="<%= selUser != null %>">
						<liferay-portlet:actionURL portletName="<%= PortletKeys.SITE_REDIRECTOR %>" var="pagesURL">
							<portlet:param name="struts_action" value="/my_sites/view" />
							<portlet:param name="groupId" value="<%= String.valueOf(selUser.getGroup().getGroupId()) %>" />
							<portlet:param name="privateLayout" value="<%= Boolean.FALSE.toString() %>" />
						</liferay-portlet:actionURL>

						<c:choose>
							<c:when test="<%= selUser.getPublicLayoutsPageCount() > 0 %>">
								<liferay-ui:icon
									iconCssClass="icon-search"
									label="<%= true %>"
									message="open-pages"
									method="get"
									target="_blank"
									url="<%= pagesURL.toString() %>"
								/>
							</c:when>
							<c:otherwise>
								<liferay-ui:message key="this-user-does-not-have-any-pages" />
							</c:otherwise>
						</c:choose>

						<c:if test="<%= PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_ENABLED %>">
							<c:choose>
								<c:when test="<%= (layoutSetPrototype != null) && hasUnlinkLayoutSetPrototypePermission %>">
									<aui:input label='<%= LanguageUtil.format(request, "enable-propagation-of-changes-from-the-site-template-x", HtmlUtil.escape(layoutSetPrototype.getName(locale)), false) %>' name="publicLayoutSetPrototypeLinkEnabled" type="checkbox" value="<%= layoutSetPrototypeLinkEnabled %>" />
								</c:when>
								<c:when test="<%= layoutSetPrototype != null %>">
									<liferay-ui:message arguments="<%= new Object[] {HtmlUtil.escape(layoutSetPrototype.getName(locale))} %>" key="these-pages-are-linked-to-site-template-x" translateArguments="<%= false %>" />

									<aui:input name="layoutSetPrototypeLinkEnabled" type="hidden" value="<%= true %>" />
								</c:when>
							</c:choose>
						</c:if>
					</c:when>
				</c:choose>
			</aui:field-wrapper>
		</c:otherwise>
	</c:choose>
</aui:fieldset>

<%
if ((selUser == null) && layoutSetPrototypes.isEmpty()) {
	request.setAttribute(WebKeys.FORM_NAVIGATOR_SECTION_SHOW + "pages", Boolean.FALSE);
}
%>

<aui:script>
	function <portlet:namespace />isVisible(currentValue, value) {
		return currentValue != '';
	}

	Liferay.Util.toggleSelectBox('<portlet:namespace />publicLayoutSetPrototypeId', <portlet:namespace />isVisible, '<portlet:namespace />layoutSetPrototypeIdOptions');
</aui:script>