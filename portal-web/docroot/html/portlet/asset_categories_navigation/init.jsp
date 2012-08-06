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

<%@ include file="/html/portlet/init.jsp" %>

<%@ page import="com.liferay.portal.kernel.template.PortletDisplayTemplateHandler" %><%@
page import="com.liferay.portal.kernel.template.PortletDisplayTemplateHandlerRegistryUtil" %><%@
page import="com.liferay.portlet.asset.NoSuchVocabularyException" %><%@
page import="com.liferay.portlet.asset.model.AssetCategory" %><%@
page import="com.liferay.portlet.asset.model.AssetVocabulary" %><%@
page import="com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil" %><%@
page import="com.liferay.portlet.asset.service.AssetVocabularyServiceUtil" %><%@
page import="com.liferay.portlet.dynamicdatamapping.model.DDMTemplate" %><%@
page import="com.liferay.portlet.portletdisplaytemplates.util.PortletDisplayTemplatesUtil" %>

<%
PortletPreferences preferences = renderRequest.getPreferences();

String portletResource = ParamUtil.getString(request, "portletResource");

if (Validator.isNotNull(portletResource)) {
	preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
}

List<AssetVocabulary> vocabularies = AssetVocabularyServiceUtil.getGroupsVocabularies(new long[] {scopeGroupId, themeDisplay.getCompanyGroupId()});

long[] availableAssetVocabularyIds = new long[vocabularies.size()];

for (int i = 0; i < vocabularies.size(); i++) {
	AssetVocabulary vocabulary = vocabularies.get(i);

	availableAssetVocabularyIds[i] = vocabulary.getVocabularyId();
}

boolean allAssetVocabularies = GetterUtil.getBoolean(preferences.getValue("allAssetVocabularies", Boolean.TRUE.toString()));

long[] assetVocabularyIds = availableAssetVocabularyIds;

if (!allAssetVocabularies && (preferences.getValues("assetVocabularyIds", null) != null)) {
	assetVocabularyIds = StringUtil.split(preferences.getValue("assetVocabularyIds", null), 0L);
}

String displayTemplate = preferences.getValue("displayTemplate", StringPool.BLANK);

DDMTemplate portletDisplayDDMTemplate = null;
long portletDisplayDDMTemplateId = 0;
long portletDisplayDDMTemplateGroupId = PortletDisplayTemplatesUtil.getDDMTemplateGroupId(themeDisplay);

List<AssetVocabulary> templateVocabularies = new ArrayList<AssetVocabulary>();

if (displayTemplate.startsWith("ddmTemplate_")) {
	portletDisplayDDMTemplate = PortletDisplayTemplatesUtil.fetchDDMTemplate(portletDisplayDDMTemplateGroupId, displayTemplate);

	if (portletDisplayDDMTemplate != null) {
		portletDisplayDDMTemplateId = portletDisplayDDMTemplate.getTemplateId();
	}
}

if (portletDisplayDDMTemplateId > 0) {
	if (allAssetVocabularies) {
		templateVocabularies = vocabularies;
	}
	else {
		for (long vocabularyId : assetVocabularyIds) {
			try {
				templateVocabularies.add(AssetVocabularyServiceUtil.getVocabulary(vocabularyId));
			}
			catch (NoSuchVocabularyException nsve) {
			}
		}
	}
}
%>

<%@ include file="/html/portlet/asset_categories_navigation/init-ext.jsp" %>