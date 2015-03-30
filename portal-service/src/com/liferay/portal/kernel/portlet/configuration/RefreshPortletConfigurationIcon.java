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

package com.liferay.portal.kernel.portlet.configuration;

import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.theme.PortletDisplay;

/**
 * @author Eudaldo Alonso
 */
@OSGiBeanProperties
public class RefreshPortletConfigurationIcon
	extends BasePortletConfigurationIcon {

	@Override
	public String getCssClass() {
		return "portlet-refresh portlet-refresh-icon";
	}

	@Override
	public String getImage() {
		return "../aui/refresh";
	}

	@Override
	public String getMessage() {
		return "refresh";
	}

	@Override
	public String getOnClick() {
		PortletDisplay portletDisplay = _themeDisplay.getPortletDisplay();

		return "Liferay.Portlet.refresh('#p_p_id_".concat(
			portletDisplay.getId()).concat("_'); return false;");
	}

	@Override
	public String getURL() {
		PortletDisplay portletDisplay = _themeDisplay.getPortletDisplay();

		return portletDisplay.getURLRefresh();
	}

	@Override
	public boolean isShow() {
		PortletDisplay portletDisplay = _themeDisplay.getPortletDisplay();

		return portletDisplay.isShowRefreshIcon();
	}

	@Override
	public boolean showToolTip() {
		return false;
	}

}