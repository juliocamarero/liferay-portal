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

package com.liferay.portlet.documentlibrary.trash;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.trash.TrashRenderer;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.documentlibrary.model.DLFileShortcut;
import com.liferay.portlet.documentlibrary.service.permission.DLFileShortcutPermission;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Zsolt Berentey
 */
public class DLFileShortcutTrashRenderer implements TrashRenderer {

	public static final String TYPE = "shortcut";

	public DLFileShortcutTrashRenderer(DLFileShortcut fileShortcut) {
		_fileShortcut = fileShortcut;
	}

	public String getIconPath(PortletRequest portletRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return themeDisplay.getPathThemeImages() + "/document_library/page.png";
	}

	public String getPortletId() {
		return PortletKeys.DOCUMENT_LIBRARY;
	}

	public String getTitle(Locale locale) {
		return _fileShortcut.getToTitle();
	}

	public String getType() {
		return TYPE;
	}

	public boolean hasDeletePermission(PermissionChecker permissionChecker)
		throws PortalException, SystemException {

		return DLFileShortcutPermission.contains(
			permissionChecker, _fileShortcut, ActionKeys.DELETE);
	}

	public boolean hasViewPermission(PermissionChecker permissionChecker)
		throws PortalException, SystemException {

		return DLFileShortcutPermission.contains(
			permissionChecker, _fileShortcut, ActionKeys.VIEW);
	}

	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse,
			String template)
		throws Exception {

		return "/html/portlet/document_library/trash/" + template + ".jsp";
	}

	private DLFileShortcut _fileShortcut;

}