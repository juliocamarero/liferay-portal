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

package com.liferay.journal.web.context.util;

import com.liferay.journal.constants.JournalConstants;
import com.liferay.journal.settings.JournalGroupServiceSettings;
import com.liferay.journal.web.util.JournalWebComponentProvider;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.settings.ParameterMapSettingsLocator;
import com.liferay.portal.kernel.settings.SettingsFactory;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortletKeys;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Juergen Kappler
 */
public class JournalWebRequestHelper {

	public JournalWebRequestHelper(HttpServletRequest request) {
		_request = request;
	}

	public JournalGroupServiceSettings getJournalGroupServiceSettings() {
		try {
			if (_journalGroupServiceSettings == null) {
				ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
					WebKeys.THEME_DISPLAY);

				PortletDisplay portletDisplay =
					themeDisplay.getPortletDisplay();

				String portletId = portletDisplay.getId();

				long siteGroupId = themeDisplay.getSiteGroupId();

				JournalWebComponentProvider journalWebComponentProvider =
					JournalWebComponentProvider.
						getJournalWebComponentProvider();

				SettingsFactory settingsFactory =
					journalWebComponentProvider.getSettingsFactory();

				if (portletId.equals(PortletKeys.PORTLET_CONFIGURATION)) {
					_journalGroupServiceSettings = settingsFactory.getSettings(
						JournalGroupServiceSettings.class,
						new ParameterMapSettingsLocator(
							_request.getParameterMap(),
							new GroupServiceSettingsLocator(
								siteGroupId, JournalConstants.SERVICE_NAME)));
				}
				else {
					_journalGroupServiceSettings = settingsFactory.getSettings(
						JournalGroupServiceSettings.class,
						new GroupServiceSettingsLocator(
							siteGroupId, JournalConstants.SERVICE_NAME));
				}
			}

			return _journalGroupServiceSettings;
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}
	}

	private JournalGroupServiceSettings _journalGroupServiceSettings;
	private final HttpServletRequest _request;

}