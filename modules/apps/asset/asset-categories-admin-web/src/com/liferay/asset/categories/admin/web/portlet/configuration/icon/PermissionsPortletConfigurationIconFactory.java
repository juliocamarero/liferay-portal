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

package com.liferay.asset.categories.admin.web.portlet.configuration.icon;

import com.liferay.portal.kernel.portlet.configuration.icon.BaseJSPPortletConfigurationIconFactory;
import com.liferay.portal.kernel.portlet.configuration.icon.PortletConfigurationIcon;
import com.liferay.portal.kernel.portlet.configuration.icon.PortletConfigurationIconFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(immediate = true, service = PortletConfigurationIconFactory.class)
public class PermissionsPortletConfigurationIconFactory
	extends BaseJSPPortletConfigurationIconFactory {

	@Override
	public PortletConfigurationIcon create(HttpServletRequest request) {
		return new PermissionsPortletConfigurationIcon(request);
	}

	@Override
	public String getJspPath() {
		return "/configuration/icon/permissions.jsp";
	}

	@Override
	public double getWeight() {
		return 101;
	}

	@Override
	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.asset.categories.admin.web)", unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

}