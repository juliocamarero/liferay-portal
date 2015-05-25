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

import aQute.bnd.annotation.metatype.Configurable;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.site.navigation.breadcrumb.web.configuration.BreadcrumbWebConfiguration;
import com.liferay.site.navigation.breadcrumb.web.constants.BreadcrumbPortletKeys;
import com.liferay.site.navigation.breadcrumb.web.upgrade.BreadcrumbWebUpgrade;

import java.util.Map;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	configurationPid = "com.liferay.site.navigation.breadcrumb.web.configuration.BreadcrumbWebConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
	property = {"javax.portlet.name=" + BreadcrumbPortletKeys.BREADCRUMB},
	service = ConfigurationAction.class
)
public class BreadcrumbConfigurationAction extends DefaultConfigurationAction {

	@Override
	public String render(
			PortletConfig portletConfig, RenderRequest renderRequest,
			RenderResponse renderResponse)
		throws Exception {

		renderRequest.setAttribute(
			BreadcrumbWebConfiguration.class.getName(),
			_breadcrumbWebConfiguration);

		return super.render(portletConfig, renderRequest, renderResponse);
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_breadcrumbWebConfiguration = Configurable.createConfigurable(
			BreadcrumbWebConfiguration.class, properties);
	}

	@Reference(unbind = "-")
	protected void setBreadcrumbWebUpgrade(
		BreadcrumbWebUpgrade breadcrumbWebUpgrade) {
	}

	private volatile BreadcrumbWebConfiguration _breadcrumbWebConfiguration;

}