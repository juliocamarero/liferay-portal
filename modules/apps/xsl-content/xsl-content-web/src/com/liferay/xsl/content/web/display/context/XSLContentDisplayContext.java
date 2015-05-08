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

package com.liferay.xsl.content.web.display.context;

import com.liferay.portal.kernel.settings.SettingsException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.xsl.content.web.configuration.XSLContentPortletInstanceConfiguration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Juergen Kappler
 */
public class XSLContentDisplayContext {

	public XSLContentDisplayContext(HttpServletRequest request)
		throws SettingsException {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		_xslContentPortletInstanceConfiguration =
			portletDisplay.getPortletInstanceConfiguration(
				XSLContentPortletInstanceConfiguration.class);
	}

	public String getXMLUrl() {
		if (_xmlUrl != null) {
			return _xmlUrl;
		}

		_xmlUrl = _xslContentPortletInstanceConfiguration.xmlUrl();

		return _xmlUrl;
	}

	public String getXSLUrl() {
		if (_xslUrl != null) {
			return _xslUrl;
		}

		_xslUrl = _xslContentPortletInstanceConfiguration.xslUrl();

		return _xslUrl;
	}

	private String _xmlUrl;
	private final XSLContentPortletInstanceConfiguration
		_xslContentPortletInstanceConfiguration;
	private String _xslUrl;

}