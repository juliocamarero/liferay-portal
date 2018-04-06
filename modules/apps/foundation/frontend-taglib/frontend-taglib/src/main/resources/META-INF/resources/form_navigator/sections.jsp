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

<%@ include file="/form_navigator/init.jsp" %>

<%
List<FormNavigatorEntry<Object>> formNavigatorEntries = (List<FormNavigatorEntry<Object>>)request.getAttribute(FormNavigatorWebKeys.FORM_NAVIGATOR_ENTRIES);
%>

<liferay-frontend:fieldset-group>

	<%
	final FormNavigatorEntry formNavigatorEntry = formNavigatorEntries.get(0);

	String sectionId = namespace + _getSectionId(formNavigatorEntry.getKey());

	String errorSection = null;
	%>

	<!-- Begin fragment <%= sectionId %> -->

	<liferay-frontend:fieldset
		id="<%= _getSectionId(formNavigatorEntry.getKey()) %>"
	>

		<%
		PortalIncludeUtil.include(
			pageContext,
			new PortalIncludeUtil.HTMLRenderer() {

				public void renderHTML(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
					formNavigatorEntry.include(request, response);
				}

			});

		errorSection = (String)request.getAttribute(WebKeys.ERROR_SECTION);

		if (Objects.equals(formNavigatorEntry.getKey(), errorSection)) {
			request.setAttribute(WebKeys.ERROR_SECTION, null);
		}
		%>

	</liferay-frontend:fieldset>

	<!-- End fragment <%= sectionId %> -->

	<%
	for (int i = 1; i < formNavigatorEntries.size(); i++) {
		final FormNavigatorEntry curFormNavigatorEntry = formNavigatorEntries.get(i);

		sectionId = namespace + _getSectionId(curFormNavigatorEntry.getKey());
	%>

		<!-- Begin fragment <%= sectionId %> -->

		<liferay-frontend:fieldset
			collapsed="<%= true %>"
			collapsible="<%= true %>"
			id="<%= _getSectionId(curFormNavigatorEntry.getKey()) %>"
			label="<%= curFormNavigatorEntry.getLabel(locale) %>"
		>

			<%
			PortalIncludeUtil.include(
				pageContext,
				new PortalIncludeUtil.HTMLRenderer() {

					public void renderHTML(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
						curFormNavigatorEntry.include(request, response);
					}

				});
			%>

		</liferay-frontend:fieldset>

		<!-- End fragment <%= sectionId %> -->

	<%
		String curErrorSection = (String)request.getAttribute(WebKeys.ERROR_SECTION);

		if (Objects.equals(_getSectionId(curFormNavigatorEntry.getKey()), _getSectionId(curErrorSection))) {
			errorSection = curErrorSection;

			request.setAttribute(WebKeys.ERROR_SECTION, null);
		}
	}
	%>

	<%
	if (Validator.isNotNull(errorSection)) {
		String currentTab = (String)request.getAttribute(FormNavigatorWebKeys.CURRENT_TAB);

		request.setAttribute(FormNavigatorWebKeys.ERROR_TAB, currentTab);
	%>

		<aui:script sandbox="<%= true %>">
			var focusField;

			var sectionContent = $('#<%= _getSectionId(errorSection) %>Content');

			<%
			String focusField = (String)request.getAttribute("liferay-ui:error:focusField");
			%>

			<c:choose>
				<c:when test="<%= Validator.isNotNull(focusField) %>">
					focusField = sectionContent.find('#<portlet:namespace /><%= focusField %>');
				</c:when>
				<c:otherwise>
					focusField = sectionContent.find('input:not([type="hidden"]).field').first();
				</c:otherwise>
			</c:choose>

			Liferay.once(
				'<portlet:namespace />formReady',
				function(event) {
					var hasFocusField = focusField.length;

					if (!sectionContent.hasClass('in')) {
						if (hasFocusField) {
							sectionContent.one(
								'shown.bs.collapse',
								function() {
									Liferay.Util.focusFormField(focusField);
								}
							);
						}

						sectionContent.collapse('show');
					}
					else if (hasFocusField) {
						Liferay.Util.focusFormField(focusField);
					}
				}
			);
		</aui:script>

	<%
	}
	%>

</liferay-frontend:fieldset-group>