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

package com.liferay.portlet.css.web.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portlet.css.web.upgrade.PortletCSSWebUpgrade;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.icon=/icons/portlet_css.png",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.system=true",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=Portlet CSS",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.resource-bundle=content.Language"
	},
	service = Portlet.class
)
public class PortletCSSPortlet extends MVCPortlet {

	@Override
	public String getJSON(
			ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		String portletId = ParamUtil.getString(request, "portletId");

		if (!PortletPermissionUtil.contains(
				permissionChecker, layout, portletId,
				ActionKeys.CONFIGURATION)) {

			return null;
		}

		PortletPreferences portletSetup =
			PortletPreferencesFactoryUtil.getStrictLayoutPortletSetup(
				layout, portletId);

		String css = ParamUtil.getString(request, "css");

		if (_log.isDebugEnabled()) {
			_log.debug("Updating css " + css);
		}

		JSONObject jsonObj = JSONFactoryUtil.createJSONObject(css);

		JSONObject portletData = jsonObj.getJSONObject("portletData");

		jsonObj.remove("portletData");

		css = jsonObj.toString();

		boolean useCustomTitle = portletData.getBoolean("useCustomTitle");
		String showBorders = portletData.getString("showBorders");
		String linkToLayoutUuid = GetterUtil.getString(
			portletData.getString("portletLinksTarget"));

		JSONObject titles = portletData.getJSONObject("titles");

		for (Locale locale : LanguageUtil.getAvailableLocales(
				themeDisplay.getSiteGroupId())) {

			String languageId = LocaleUtil.toLanguageId(locale);

			String title = null;

			if (titles.has(languageId)) {
				title = GetterUtil.getString(titles.getString(languageId));
			}

			String rootPortletId = PortletConstants.getRootPortletId(portletId);

			String defaultPortletTitle = PortalUtil.getPortletTitle(
				rootPortletId, languageId);

			if ((title != null) &&
				!Validator.equals(defaultPortletTitle, title)) {

				portletSetup.setValue("portletSetupTitle_" + languageId, title);
			}
			else {
				portletSetup.reset("portletSetupTitle_" + languageId);
			}
		}

		portletSetup.setValue(
			"portletSetupUseCustomTitle", String.valueOf(useCustomTitle));

		if (Validator.isNotNull(showBorders)) {
			boolean showBordersBoolean = portletData.getBoolean("showBorders");

			portletSetup.setValue(
				"portletSetupShowBorders", String.valueOf(showBordersBoolean));
		}
		else {
			portletSetup.reset("portletSetupShowBorders");
		}

		if (Validator.isNotNull(linkToLayoutUuid)) {
			portletSetup.setValue(
				"portletSetupLinkToLayoutUuid", linkToLayoutUuid);
		}
		else {
			portletSetup.reset("portletSetupLinkToLayoutUuid");
		}

		portletSetup.setValue("portletSetupCss", css);

		if (PropsValues.MOBILE_DEVICE_STYLING_WAP_ENABLED) {
			JSONObject wapData = jsonObj.getJSONObject("wapData");

			String wapInitialWindowState = wapData.getString(
				"initialWindowState");
			String wapTitle = wapData.getString("title");

			portletSetup.setValue(
				"lfrWapInitialWindowState", wapInitialWindowState);
			portletSetup.setValue("lfrWapTitle", wapTitle);
		}
		else {
			portletSetup.reset("lfrWapInitialWindowState");
			portletSetup.reset("lfrWapTitle");
		}

		portletSetup.store();

		InvokerPortletImpl.clearResponse(
			session, layout.getPrimaryKey(), portletId,
			LanguageUtil.getLanguageId(request));

		return null;
	}

	@Reference(unbind = "-")
	protected void setPortletCSSWebUpgrade(
		PortletCSSWebUpgrade portletCSSWebUpgrade) {
	}

}