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

package com.liferay.portal.kernel.view;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutServiceUtil;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.registry.collections.ServiceTrackerCollections;
import com.liferay.registry.collections.ServiceTrackerMap;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class ViewPortletProviderUtil {

	public static String getPortletId(String className) {
		ViewPortletProvider displayPortletProvider =
			_serviceTrackerMap.getService(className);

		if (displayPortletProvider == null) {
			return StringPool.BLANK;
		}

		return displayPortletProvider.getPortletId();
	}

	private static final ServiceTrackerMap<String, ViewPortletProvider>
		_serviceTrackerMap = ServiceTrackerCollections.singleValueMap(
			ViewPortletProvider.class, "model.class.name");

	static {
		_serviceTrackerMap.open();
	}

}