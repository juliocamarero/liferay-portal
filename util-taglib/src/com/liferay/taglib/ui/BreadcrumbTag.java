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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.BreadcrumbEntry;
import com.liferay.portal.kernel.servlet.taglib.ui.BreadcrumbUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.portlet.portletdisplaytemplate.util.PortletDisplayTemplate;
import com.liferay.taglib.util.IncludeTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 */
public class BreadcrumbTag extends IncludeTag {

	public long getDisplayStyleGroupId() {
		return _displayStyleGroupId;
	}

	public void setDisplayStyle(String displayStyle) {
		_displayStyle = displayStyle;
	}

	public void setDisplayStyleGroupId(long displayStyleGroupId) {
		_displayStyleGroupId = displayStyleGroupId;
	}

	public void setShowCurrentGroup(boolean showCurrentGroup) {
		_showCurrentGroup = showCurrentGroup;
	}

	public void setShowGuestGroup(boolean showGuestGroup) {
		_showGuestGroup = showGuestGroup;
	}

	public void setShowLayout(boolean showLayout) {
		_showLayout = showLayout;
	}

	public void setShowParentGroups(boolean showParentGroups) {
		_showParentGroups = showParentGroups;
	}

	public void setShowPortletBreadcrumb(boolean showPortletBreadcrumb) {
		_showPortletBreadcrumb = showPortletBreadcrumb;
	}

	@Override
	protected void cleanUp() {
		_displayStyle = null;
		_showCurrentGroup = true;
		_showGuestGroup = _SHOW_GUEST_GROUP;
		_showLayout = true;
		_showParentGroups = null;
		_showPortletBreadcrumb = true;
	}

	protected List<BreadcrumbEntry> getBreadcrumbEntries(
		HttpServletRequest request) {

		List<Integer> breadcrumbEntryTypes = new ArrayList<>();

		if (_showCurrentGroup) {
			breadcrumbEntryTypes.add(BreadcrumbUtil.ENTRY_TYPE_CURRENT_GROUP);
		}

		if (_showGuestGroup) {
			breadcrumbEntryTypes.add(BreadcrumbUtil.ENTRY_TYPE_GUEST_GROUP);
		}

		if (_showLayout) {
			breadcrumbEntryTypes.add(BreadcrumbUtil.ENTRY_TYPE_LAYOUT);
		}

		if (_showParentGroups) {
			breadcrumbEntryTypes.add(BreadcrumbUtil.ENTRY_TYPE_PARENT_GROUP);
		}

		if (_showPortletBreadcrumb) {
			breadcrumbEntryTypes.add(BreadcrumbUtil.ENTRY_TYPE_PORTLET);
		}

		List<BreadcrumbEntry> breadcrumbEntries = Collections.EMPTY_LIST;

		try {
			breadcrumbEntries = BreadcrumbUtil.getBreadcrumbEntries(
				request, ArrayUtil.toIntArray(breadcrumbEntryTypes));
		}
		catch (Exception e) {
		}

		return breadcrumbEntries;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	protected void initShowParentGroups(HttpServletRequest request) {
		if (_showParentGroups != null) {
			return;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		try {
			Layout layout = themeDisplay.getLayout();

			Group group = layout.getGroup();

			UnicodeProperties typeSettingsProperties =
				group.getTypeSettingsProperties();

			_showParentGroups = GetterUtil.getBoolean(
				typeSettingsProperties.getProperty(
					"breadcrumbShowParentGroups"),
				_SHOW_PARENT_GROUPS);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		initShowParentGroups(request);

		request.setAttribute(
			"liferay-ui:breadcrumb:breadcrumbEntries",
			getBreadcrumbEntries(request));

		long displayStyleGroupId = _displayStyleGroupId;

		if (displayStyleGroupId <= 0) {
			displayStyleGroupId = themeDisplay.getScopeGroupId();
		}

		String displayStyle = _displayStyle;

		if (Validator.isNull(displayStyle)) {
			try {
				DDMTemplate ddmTemplate =
					DDMTemplateLocalServiceUtil.getTemplate(
						displayStyleGroupId,
						PortalUtil.getClassNameId(Layout.class), _DISPLAY_STYLE,
						true);

				displayStyle = PortletDisplayTemplate.DISPLAY_STYLE_PREFIX +
					ddmTemplate.getUuid();
			}
			catch (PortalException e) {
			}
		}

		request.setAttribute(
			"liferay-ui:breadcrumb:displayStyle", displayStyle);
		request.setAttribute(
			"liferay-ui:breadcrumb:displayStyleGroupId", displayStyleGroupId);
	}

	private static final String _DISPLAY_STYLE = GetterUtil.getString(
		PropsUtil.get(PropsKeys.BREADCRUMB_DISPLAY_STYLE_DEFAULT));

	private static final String _PAGE = "/html/taglib/ui/breadcrumb/page.jsp";

	private static final boolean _SHOW_GUEST_GROUP = GetterUtil.getBoolean(
		PropsUtil.get(PropsKeys.BREADCRUMB_SHOW_GUEST_GROUP));

	private static final boolean _SHOW_PARENT_GROUPS = GetterUtil.getBoolean(
		PropsUtil.get(PropsKeys.BREADCRUMB_SHOW_PARENT_GROUPS));

	private static final Log _log = LogFactoryUtil.getLog(BreadcrumbTag.class);

	private String _displayStyle;
	private long _displayStyleGroupId;
	private boolean _showCurrentGroup = true;
	private boolean _showGuestGroup = _SHOW_GUEST_GROUP;
	private boolean _showLayout = true;
	private Boolean _showParentGroups = null;
	private boolean _showPortletBreadcrumb = true;

}