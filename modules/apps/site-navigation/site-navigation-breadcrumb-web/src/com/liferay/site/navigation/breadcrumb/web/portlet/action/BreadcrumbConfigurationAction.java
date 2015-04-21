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

package com.liferay.site.navigation.breadcrumb.web.portlet.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.settings.ParameterMapSettingsLocator;
import com.liferay.portal.kernel.settings.PortletInstanceSettingsLocator;
import com.liferay.portal.kernel.settings.SettingsFactory;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.site.navigation.breadcrumb.web.configuration.BreadcrumbPortletInstanceConfiguration;
import com.liferay.site.navigation.breadcrumb.web.constants.BreadcrumbPortletKeys;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	immediate = true,
	property = {"javax.portlet.name=" + BreadcrumbPortletKeys.BREADCRUMB},
	service = ConfigurationAction.class
)
public class BreadcrumbConfigurationAction extends DefaultConfigurationAction {

	@Override
	public String render(
			PortletConfig portletConfig, RenderRequest renderRequest,
			RenderResponse renderResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		try {
			BreadcrumbPortletInstanceConfiguration
				breadcrumbPortletInstanceConfiguration =
					_settingsFactory.getSettings(
						BreadcrumbPortletInstanceConfiguration.class,
						new ParameterMapSettingsLocator(
							renderRequest.getParameterMap(),
							new PortletInstanceSettingsLocator(
								themeDisplay.getLayout(),
								portletDisplay.getPortletResource())));

			renderRequest.setAttribute(
					BreadcrumbPortletInstanceConfiguration.class.getName(),
					breadcrumbPortletInstanceConfiguration);
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}

		return super.render(portletConfig, renderRequest, renderResponse);
	}

	@Reference(unbind = "-")
	protected void setSettingsFactory(SettingsFactory settingsFactory) {
		_settingsFactory = settingsFactory;
	}

	private SettingsFactory _settingsFactory;

}