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

package com.liferay.site.navigation.breadcrumb.web.context;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.portletdisplaytemplate.util.PortletDisplayTemplateUtil;
import com.liferay.site.navigation.breadcrumb.web.configuration.BreadcrumbPortletInstanceConfiguration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Julio Camarero
 */
public class BreadcrumbDisplayContext {

	public BreadcrumbDisplayContext(
		HttpServletRequest request, BreadcrumbPortletInstanceConfiguration
			breadcrumbPortletInstanceSettings) {

		_request = request;
		_breadcrumbPortletInstanceSettings = breadcrumbPortletInstanceSettings;
	}

	public String getDDMTemplateKey() {
		String displayStyle = getDisplayStyle();

		if (displayStyle != null) {
			return PortletDisplayTemplateUtil.getDDMTemplateKey(displayStyle);
		}

		return StringPool.BLANK;
	}

	public String getDisplayStyle() {
		return ParamUtil.getString(
			_request, "displayStyle",
			_breadcrumbPortletInstanceSettings.displayStyle());
	}

	public long getDisplayStyleGroupId() {
		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		return ParamUtil.getLong(
			_request, "displayStyleGroupId",
			_breadcrumbPortletInstanceSettings.displayStyleGroupId(
				themeDisplay.getSiteGroupId()));
	}

	public String getPortletResource() {
		if (_portletResource != null) {
			return _portletResource;
		}

		_portletResource = ParamUtil.getString(_request, "portletResource");

		return _portletResource;
	}

	public boolean isShowCurrentGroup() {
		return ParamUtil.getBoolean(
			_request, "showCurrentGroup",
			_breadcrumbPortletInstanceSettings.showCurrentGroup());
	}

	public boolean isShowGuestGroup() {
		return ParamUtil.getBoolean(
			_request, "showGuestGroup",
			_breadcrumbPortletInstanceSettings.showGuestGroup());
	}

	public boolean isShowLayout() {
		return ParamUtil.getBoolean(
			_request, "showLayout",
			_breadcrumbPortletInstanceSettings.showLayout());
	}

	public boolean isShowParentGroups() {
		return ParamUtil.getBoolean(
			_request, "showParentGroups",
			_breadcrumbPortletInstanceSettings.showParentGroups());
	}

	public boolean isShowPortletBreadcrumb() {
		return ParamUtil.getBoolean(
			_request, "showPortletBreadcrumb",
			_breadcrumbPortletInstanceSettings.showPortletBreadcrumb());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BreadcrumbDisplayContext.class);

	private final BreadcrumbPortletInstanceConfiguration
		_breadcrumbPortletInstanceSettings;
	private String _portletResource;
	private final HttpServletRequest _request;

}