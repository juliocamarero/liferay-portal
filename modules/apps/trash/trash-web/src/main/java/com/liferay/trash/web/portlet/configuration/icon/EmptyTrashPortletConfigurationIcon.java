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

package com.liferay.trash.web.portlet.configuration.icon;

import com.liferay.portal.kernel.portlet.configuration.icon.BaseJSPPortletConfigurationIcon;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.PortletRequest;

import javax.servlet.ServletContext;

/**
 * @author Eudaldo Alonso
 */
public class EmptyTrashPortletConfigurationIcon
	extends BaseJSPPortletConfigurationIcon {

	public EmptyTrashPortletConfigurationIcon(
		ServletContext servletContext, String jspPath,
		PortletRequest portletRequest) {

		super(servletContext, jspPath, portletRequest);
	}

	@Override
	public boolean isShow(PortletRequest portletRequest) {
		String keywords = ParamUtil.getString(portletRequest, "keywords");

		if (Validator.isNotNull(keywords)) {
			return false;
		}

		return true;
	}

}