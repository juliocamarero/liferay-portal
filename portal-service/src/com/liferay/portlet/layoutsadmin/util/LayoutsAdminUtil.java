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
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.model.Layout;
import com.liferay.portal.security.permission.PermissionChecker;

import java.util.List;

/**
 * @author Sergio González
 */
public class LayoutsAdminUtil {

	public static List<Layout> filterLayouts(
			PermissionChecker permissionChecker, List<Layout> layouts)
		throws PortalException, SystemException {

		return getLayoutsAdmin().filterLayouts(permissionChecker, layouts);
	}

	public static LayoutsAdmin getLayoutsAdmin() {
		PortalRuntimePermission.checkGetBeanProperty(LayoutsAdminUtil.class);

		return _layoutsAdmin;
	}

	public void setLayoutsAdmin(LayoutsAdmin layoutsAdmin) {
		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_layoutsAdmin = layoutsAdmin;
	}

	private static LayoutsAdmin _layoutsAdmin;

}