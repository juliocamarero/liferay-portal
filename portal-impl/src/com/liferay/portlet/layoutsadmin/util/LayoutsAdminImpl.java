/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.layoutsadmin.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Layout;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.permission.LayoutPermissionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergio González
 */
public class LayoutsAdminImpl implements LayoutsAdmin {

	public List<Layout> filterLayouts(
			PermissionChecker permissionChecker, List<Layout> layouts)
		throws PortalException, SystemException {

		List<Layout> filteredLayouts = new ArrayList<Layout>();

		for (Layout layout : layouts) {
			if (LayoutPermissionUtil.contains(
					permissionChecker, layout.getPlid(), ActionKeys.VIEW)) {

				filteredLayouts.add(layout);
			}
		}

		return filteredLayouts;
	}

}