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

package com.liferay.portal.kernel.portlet.configuration.icon;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;

/**
 * @author Eduardo Garcia
 */
public class PortletConfigurationIconMenu {

	public List<PortletConfigurationIconFactory>
		getPortletConfigurationIcons(
			String portletId, PortletRequest portletRequest) {

		List<PortletConfigurationIconFactory>
			portletConfigurationIconFactories =
				PortletConfigurationIconTracker.getPortletConfigurationIcons(
					portletId, portletRequest);

		List<PortletConfigurationIconFactory>
			filteredPortletConfigurationIconFactories = new ArrayList();

		for (PortletConfigurationIconFactory portletConfigurationIconFactory :
				portletConfigurationIconFactories) {

			PortletConfigurationIcon portletConfigurationIcon =
				portletConfigurationIconFactory.create(portletRequest);

			if ((portletConfigurationIcon != null) &&
				portletConfigurationIcon.isShow()) {

				filteredPortletConfigurationIconFactories.add(
					portletConfigurationIconFactory);
			}
		}

		return filteredPortletConfigurationIconFactories;
	}

}