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

package com.liferay.portlet.configuration.icons.web;

import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.theme.PortletDisplay;

/**
 * @author Eudaldo Alonso
 */
@OSGiBeanProperties
public class ClosePortletConfigurationIcon
	extends BasePortletConfigurationIcon {

	@Override
	public String getCssClass() {
		return "item-remove portlet-close portlet-close-icon";
	}

	@Override
	public String getImage() {
		return "../aui/remove";
	}

	@Override
	public String getMessage() {
		return "remove";
	}

	@Override
	public String getOnClick() {
		PortletDisplay portletDisplay = _themeDisplay.getPortletDisplay();

		return "Liferay.Portlet.close('#p_p_id_".concat(
			portletDisplay.getId()).concat("_'); return false;");
	}

	@Override
	public String getURL() {
		PortletDisplay portletDisplay = _themeDisplay.getPortletDisplay();

		return portletDisplay.getURLClose();
	}

	@Override
	public boolean isShow() {
		PortletDisplay portletDisplay = _themeDisplay.getPortletDisplay();

		return portletDisplay.isShowCloseIcon();
	}

	@Override
	public boolean showToolTip() {
		return false;
	}

}