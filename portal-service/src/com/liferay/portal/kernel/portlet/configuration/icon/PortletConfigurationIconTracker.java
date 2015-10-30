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

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.registry.collections.ServiceTrackerCollections;
import com.liferay.registry.collections.ServiceTrackerMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eudaldo Alonso
 */
public class PortletConfigurationIconTracker {

	public static List<PortletConfigurationIconFactory>
		getPortletConfigurationIcons(String portletId) {

		List<PortletConfigurationIconFactory>
			portletConfigurationIconFactories = new ArrayList<>();

		List<PortletConfigurationIconFactory>
			portletPortletConfigurationIconFactories =
				_serviceTrackerMap.getService(StringPool.STAR);

		if (portletPortletConfigurationIconFactories != null) {
			portletConfigurationIconFactories.addAll(
				portletPortletConfigurationIconFactories);
		}

		portletPortletConfigurationIconFactories =
			_serviceTrackerMap.getService(portletId);

		if (portletPortletConfigurationIconFactories != null) {
			portletConfigurationIconFactories.addAll(
				portletPortletConfigurationIconFactories);
		}

		return portletConfigurationIconFactories;
	}

	private PortletConfigurationIconTracker() {
		_serviceTrackerMap.open();
	}

	private static final ServiceTrackerMap
			<String, List<PortletConfigurationIconFactory>>
		_serviceTrackerMap = ServiceTrackerCollections.multiValueMap(
			PortletConfigurationIconFactory.class, null,
			new PortletConfigurationIconServiceReferenceMapper());

}