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

package com.liferay.portlet.layoutsadmin.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.permission.GroupPermissionUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.service.permission.LayoutPrototypePermissionUtil;
import com.liferay.portal.service.permission.LayoutSetPrototypePermissionUtil;
import com.liferay.portal.service.permission.UserPermissionUtil;

/**
 * @author Eudaldo Alonso
 */
public class LayoutsPermissionUtil {

	public static void checkPermission(
			PermissionChecker permissionChecker, Group group, Layout layout,
			long selPlid)
		throws PortalException {

		if (selPlid > 0) {
			LayoutPermissionUtil.check(
				permissionChecker, layout, ActionKeys.VIEW);
		}
		else {
			GroupPermissionUtil.check(
				permissionChecker, group, ActionKeys.VIEW);
		}
	}

	public static void checkPermissions(
			PermissionChecker permissionChecker, Group group, Layout layout,
			long selPlid, long parentPlid, String cmd)
		throws Exception {

		if (group == null) {
			throw new PrincipalException();
		}

		if (selPlid > 0) {
			layout = LayoutLocalServiceUtil.getLayout(selPlid);
		}

		if (cmd.equals(Constants.ADD)) {
			if (parentPlid == LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {
				if (!GroupPermissionUtil.contains(
						permissionChecker, group, ActionKeys.ADD_LAYOUT)) {

					throw new PrincipalException();
				}
			}
			else {
				layout = LayoutLocalServiceUtil.getLayout(parentPlid);

				if (!LayoutPermissionUtil.contains(
						permissionChecker, layout, ActionKeys.ADD_LAYOUT)) {

					throw new PrincipalException();
				}
			}
		}
		else if (cmd.equals(Constants.DELETE)) {
			if (!LayoutPermissionUtil.contains(
					permissionChecker, layout, ActionKeys.DELETE)) {

				throw new PrincipalException();
			}
		}
		else if (cmd.equals(Constants.PUBLISH_TO_LIVE) ||
				 cmd.equals(Constants.PUBLISH_TO_REMOTE)) {

			boolean hasUpdateLayoutPermission = false;

			if (layout != null) {
				hasUpdateLayoutPermission = LayoutPermissionUtil.contains(
					permissionChecker, layout, ActionKeys.UPDATE);
			}

			if (group.isCompany() || group.isSite()) {
				boolean publishToLive = GroupPermissionUtil.contains(
					permissionChecker, group, ActionKeys.PUBLISH_STAGING);

				if (!hasUpdateLayoutPermission && !publishToLive) {
					throw new PrincipalException();
				}
			}
			else {
				checkPermission(permissionChecker, group, layout, selPlid);
			}
		}
		else if (cmd.equals(Constants.UPDATE)) {
			if (group.isCompany()) {
				if (!permissionChecker.isCompanyAdmin()) {
					throw new PrincipalException();
				}
			}
			else if (group.isLayoutPrototype()) {
				LayoutPrototypePermissionUtil.check(
					permissionChecker, group.getClassPK(), ActionKeys.UPDATE);
			}
			else if (group.isLayoutSetPrototype()) {
				LayoutSetPrototypePermissionUtil.check(
					permissionChecker, group.getClassPK(), ActionKeys.UPDATE);
			}
			else if (group.isUser()) {
				long groupUserId = group.getClassPK();

				User groupUser = UserLocalServiceUtil.getUserById(groupUserId);

				long[] organizationIds = groupUser.getOrganizationIds();

				UserPermissionUtil.check(
					permissionChecker, groupUserId, organizationIds,
					ActionKeys.UPDATE);
			}
			else {
				checkPermission(permissionChecker, group, layout, selPlid);
			}
		}
		else if (cmd.equals("reset_customized_view")) {
			if (!LayoutPermissionUtil.contains(
					permissionChecker, layout, ActionKeys.CUSTOMIZE)) {

				throw new PrincipalException();
			}
		}
		else {
			checkPermission(permissionChecker, group, layout, selPlid);
		}
	}

	public static void checkPermissions(
			PermissionChecker permissionChecker, Group group, Layout layout,
			long selPlid, String cmd)
		throws Exception {

		checkPermissions(
			permissionChecker, group, layout, selPlid,
			LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, cmd);
	}

}