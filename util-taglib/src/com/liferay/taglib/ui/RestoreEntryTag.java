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

package com.liferay.taglib.ui;

import com.liferay.taglib.util.IncludeTag;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Roberto Diaz
 */
public class RestoreEntryTag extends IncludeTag {

	public void setCheckEntryURL(PortletURL checkEntryURL) {
		_checkEntryURL = checkEntryURL;
	}

	public void setDuplicateEntryURL(PortletURL duplicateEntryURL) {
		_duplicateEntryURL = duplicateEntryURL;
	}

	public void setOverrideMessage(String overrideMessage) {
		_overrideMessage = overrideMessage;
	}

	public void setRenameMessage(String renameMessage) {
		_renameMessage = renameMessage;
	}

	@Override
	protected void cleanUp() {
		_checkEntryURL = null;
		_duplicateEntryURL = null;
		_overrideMessage = _OVERRIDE_MESSAGE;
		_renameMessage = _RENAME_MESSAGE;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected boolean isCleanUpSetAttributes() {
		return _CLEAN_UP_SET_ATTRIBUTES;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-ui:restore-entry:checkEntryURL", _checkEntryURL);
		request.setAttribute(
			"liferay-ui:restore-entry:duplicateEntryURL", _duplicateEntryURL);
		request.setAttribute(
			"liferay-ui:restore-entry:overrideMessage", _overrideMessage);
		request.setAttribute(
			"liferay-ui:restore-entry:renameMessage", _renameMessage);
	}

	private static final boolean _CLEAN_UP_SET_ATTRIBUTES = true;

	private static final String _OVERRIDE_MESSAGE =
		"overwrite-the-existing-entry-with-the-one-from-the-recycle-bin";

	private static final String _PAGE =
		"/html/taglib/ui/restore_entry/page.jsp";

	private static final String _RENAME_MESSAGE =
		"keep-both-entries-and-rename-the-entry-from-the-recycle-bin-as";

	private PortletURL _checkEntryURL = null;
	private PortletURL _duplicateEntryURL = null;
	private String _overrideMessage = _OVERRIDE_MESSAGE;
	private String _renameMessage = _RENAME_MESSAGE;

}