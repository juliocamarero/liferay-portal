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

<%@ include file="/html/portlet/asset_categories_navigation/init.jsp" %>

<%
PortletDisplayTemplateHandler portletDisplayTemplateHandler = PortletDisplayTemplateHandlerRegistryUtil.getPortletDisplayTemplateHandler(AssetCategory.class.getName());

long portletDisplayTemplateHandlerClassNameId = PortalUtil.getClassNameId(portletDisplayTemplateHandler.getClassName());

String redirect = ParamUtil.getString(request, "redirect");
%>

<liferay-portlet:actionURL portletConfiguration="true" var="configurationURL" />

<aui:form action="<%= configurationURL %>" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveConfiguration();" %>'>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />

	<aui:fieldset>
		<aui:select label="vocabularies" name="preferences--allAssetVocabularies--">
			<aui:option label="all" selected="<%= allAssetVocabularies %>" value="<%= true %>" />
			<aui:option label="filter[action]" selected="<%= !allAssetVocabularies %>" value="<%= false %>" />
		</aui:select>

		<aui:input name="preferences--assetVocabularyIds--" type="hidden" />

		<%
		Set<Long> availableAssetVocabularyIdsSet = SetUtil.fromArray(availableAssetVocabularyIds);

		// Left list

		List<KeyValuePair> typesLeftList = new ArrayList<KeyValuePair>();

		for (long vocabularyId : assetVocabularyIds) {
			try {
				AssetVocabulary vocabulary = AssetVocabularyLocalServiceUtil.getVocabulary(vocabularyId);

				vocabulary = vocabulary.toEscapedModel();

				typesLeftList.add(new KeyValuePair(String.valueOf(vocabularyId), _getTitle(vocabulary, themeDisplay)));
			}
			catch (NoSuchVocabularyException nsve) {
			}
		}

		// Right list

		List<KeyValuePair> typesRightList = new ArrayList<KeyValuePair>();

		Arrays.sort(assetVocabularyIds);

		for (long vocabularyId : availableAssetVocabularyIdsSet) {
			if (Arrays.binarySearch(assetVocabularyIds, vocabularyId) < 0) {
				AssetVocabulary vocabulary = AssetVocabularyLocalServiceUtil.getVocabulary(vocabularyId);

				vocabulary = vocabulary.toEscapedModel();

				typesRightList.add(new KeyValuePair(String.valueOf(vocabularyId), _getTitle(vocabulary, themeDisplay)));
			}
		}

		typesRightList = ListUtil.sort(typesRightList, new KeyValuePairComparator(false, true));
		%>

		<div class="<%= allAssetVocabularies ? "aui-helper-hidden" : "" %>" id="<portlet:namespace />assetVocabulariesBoxes">
			<liferay-ui:input-move-boxes
				leftBoxName="currentAssetVocabularyIds"
				leftList="<%= typesLeftList %>"
				leftReorder="true"
				leftTitle="current"
				rightBoxName="availableAssetVocabularyIds"
				rightList="<%= typesRightList %>"
				rightTitle="available"
			/>
		</div>

		<div class="display-template">
			<aui:select label="display-template" name="preferences--displayTemplate--">

			<aui:option label='<%= LanguageUtil.get(pageContext, "default") %>' selected="<%= Validator.isNull(displayTemplate) %>" />
				<optgroup label="<liferay-ui:message key="global" />">

				<%
				List<DDMTemplate> companyAssetPublisherDDMTemplates = DDMTemplateLocalServiceUtil.getTemplates(themeDisplay.getCompanyGroupId(), portletDisplayTemplateHandlerClassNameId, 0);

				for (DDMTemplate companyAssetPublisherDDMTemplate : companyAssetPublisherDDMTemplates) {
					if (!DDMTemplatePermission.contains(permissionChecker, companyAssetPublisherDDMTemplate, ActionKeys.VIEW)) {
						continue;
				}
				%>

					<aui:option label="<%= HtmlUtil.escape(companyAssetPublisherDDMTemplate.getName(locale)) %>" selected="<%= (portletDisplayDDMTemplate != null) && (companyAssetPublisherDDMTemplate.getTemplateId() == portletDisplayDDMTemplate.getTemplateId()) %>" value='<%= "ddmTemplate_" + companyAssetPublisherDDMTemplate.getUuid() %>' />

				<%
				}
				%>

				</optgroup>
				<optgroup label="<%= themeDisplay.getScopeGroupName() %>">

				<%
				List<DDMTemplate> groupAssetPublisherDDMTemplates = DDMTemplateLocalServiceUtil.getTemplates(portletDisplayDDMTemplateGroupId, portletDisplayTemplateHandlerClassNameId, 0);

				for (DDMTemplate groupAssetPublisherDDMTemplate : groupAssetPublisherDDMTemplates) {
					if (!DDMTemplatePermission.contains(permissionChecker, groupAssetPublisherDDMTemplate, ActionKeys.VIEW)) {
						continue;
				}
				%>

					<aui:option label="<%= HtmlUtil.escape(groupAssetPublisherDDMTemplate.getName(locale)) %>" selected="<%= (portletDisplayDDMTemplate != null) && (groupAssetPublisherDDMTemplate.getTemplateId() == portletDisplayDDMTemplate.getTemplateId()) %>" value='<%= "ddmTemplate_" + groupAssetPublisherDDMTemplate.getUuid() %>' />

				<%
				}
				%>

				</optgroup>
			</aui:select>

			<liferay-ui:ddm-template-selector
				classNameId="<%= portletDisplayTemplateHandlerClassNameId %>"
				message='<%= LanguageUtil.format(pageContext, "manage-display-templates-for-x", themeDisplay.getScopeGroupName(), false) %>'
				refreshURL="<%= configurationURL%>"
			/>
		</div>
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>

<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />saveConfiguration',
		function() {
			if (document.<portlet:namespace />fm.<portlet:namespace />assetVocabularyIds) {
				document.<portlet:namespace />fm.<portlet:namespace />assetVocabularyIds.value = Liferay.Util.listSelect(document.<portlet:namespace />fm.<portlet:namespace />currentAssetVocabularyIds);
			}

			submitForm(document.<portlet:namespace />fm);
		},
		['liferay-util-list-fields']
	);

	Liferay.Util.toggleSelectBox('<portlet:namespace />allAssetVocabularies', 'false', '<portlet:namespace />assetVocabulariesBoxes');
</aui:script>

<%!
private String _getTitle(AssetVocabulary vocabulary, ThemeDisplay themeDisplay) {
	String title = vocabulary.getTitle(themeDisplay.getLanguageId());

	if (vocabulary.getGroupId() == themeDisplay.getCompanyGroupId()) {
		title += " (" + LanguageUtil.get(themeDisplay.getLocale(), "global") + ")";
	}

	return title;
}
%>