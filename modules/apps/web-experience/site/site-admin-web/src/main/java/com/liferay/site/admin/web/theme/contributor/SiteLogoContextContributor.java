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

package com.liferay.site.admin.web.theme.contributor;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.template.TemplateContextContributor;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Julio Camarero
 */
@Component(
	immediate = true,
	property = {"type=" + TemplateContextContributor.TYPE_THEME},
	service = TemplateContextContributor.class
)
public class SiteLogoContextContributor
	implements TemplateContextContributor {

	@Override
	public void prepare(
		Map<String, Object> contextObjects, HttpServletRequest request) {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Company company = themeDisplay.getCompany();

		LayoutSet layoutSet = themeDisplay.getLayoutSet();

		String logoCssClass = "logo";

		if ((company.getLogoId() == 0) && !layoutSet.isLogo()) {
			logoCssClass += "default-logo ";
		}
		else {
			logoCssClass += "custom-logo ";
		}

		contextObjects.put("logo_css_class", logoCssClass);

		contextObjects.put("use_company_logo", !layoutSet.isLogo());

		String siteLogo = HtmlUtil.escape(themeDisplay.getCompanyLogo());

		contextObjects.put("company_logo", siteLogo);
		contextObjects.put(
			"company_logo_height", themeDisplay.getCompanyLogoHeight());
		contextObjects.put(
			"company_logo_width", themeDisplay.getCompanyLogoWidth());

		contextObjects.put("site_logo", siteLogo);
		contextObjects.put(
			"site_logo_height", themeDisplay.getCompanyLogoHeight());
		contextObjects.put(
			"site_logo_width", themeDisplay.getCompanyLogoWidth());

		boolean showSiteNameSupported = GetterUtil.getBoolean(
			themeDisplay.getThemeSetting("show-site-name-supported"), true);
		boolean showSiteNameDefault = GetterUtil.getBoolean(
			themeDisplay.getThemeSetting("show-site-name-default"),
			showSiteNameSupported);
		boolean showSiteName= GetterUtil.getBoolean(
			layoutSet.getSettingsProperty("showSiteName"), showSiteNameDefault);

		contextObjects.put("show_site_name", showSiteName);

		String logoDescription = StringPool.BLANK;

		if (!showSiteName) {
			try {
				logoDescription = HtmlUtil.escape(
					themeDisplay.getScopeGroupName());
			}
			catch (PortalException e) {
				_log.error(e);
			}
		}

		contextObjects.put("logo_description", logoDescription);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SiteLogoContextContributor.class);

}