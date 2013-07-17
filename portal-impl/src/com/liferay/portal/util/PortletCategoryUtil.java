/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.util;

import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.PortletCategory;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.permission.PortletPermissionUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eudaldo Alonso
 */
public class PortletCategoryUtil {

	public static PortletCategory getRelevantPortletCategory(
			PermissionChecker permissionChecker,
			PortletCategory portletCategory, Set panelSelectedPortlets,
			LayoutTypePortlet layoutTypePortlet, Layout layout, User user)
		throws Exception {

		PortletCategory relevantPortletCategory = new PortletCategory(
			portletCategory.getName(), portletCategory.getPortletIds());

		for (PortletCategory curPortletCategory :
				portletCategory.getCategories()) {

			Set<String> portletIds = new HashSet<String>();

			if (curPortletCategory.isHidden()) {
				continue;
			}

			for (String portletId : curPortletCategory.getPortletIds()) {
				Portlet portlet = PortletLocalServiceUtil.getPortletById(
					user.getCompanyId(), portletId);

				if (portlet != null) {
					if (portlet.isSystem()) {
					}
					else if (!portlet.isActive() ||
							 portlet.isUndeployedPortlet()) {
					}
					else if (layout.isTypePanel() &&
							 panelSelectedPortlets.contains(
									portlet.getRootPortletId())) {

						portletIds.add(portlet.getPortletId());
					}
					else if (layout.isTypePanel() &&
							 !panelSelectedPortlets.contains(
									portlet.getRootPortletId())) {
					}
					else if (!PortletPermissionUtil.contains(
								permissionChecker, layout, portlet,
								ActionKeys.ADD_TO_PAGE)) {
					}
					else if (!portlet.isInstanceable() &&
							 layoutTypePortlet.hasPortletId(
									portlet.getPortletId())) {

						portletIds.add(portlet.getPortletId());
					}
					else {
						portletIds.add(portlet.getPortletId());
					}
				}
			}

			PortletCategory curRelevantPortletCategory =
				getRelevantPortletCategory(
					permissionChecker, curPortletCategory,
					panelSelectedPortlets, layoutTypePortlet, layout, user);

			curRelevantPortletCategory.setPortletIds(portletIds);

			if (!curRelevantPortletCategory.getCategories().isEmpty() ||
				!portletIds.isEmpty()) {

				relevantPortletCategory.addCategory(curRelevantPortletCategory);
			}
		}

		return relevantPortletCategory;
	}

}