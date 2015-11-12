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

package com.liferay.site.navigation.taglib.servlet.taglib;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portletdisplaytemplate.PortletDisplayTemplateManagerUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.theme.NavItem;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.site.navigation.taglib.servlet.ServletContextUtil;
import com.liferay.taglib.util.IncludeTag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author Brian Wing Shun Chan
 * @author Jorge Ferrer
 * @author Tibor Lipusz
 */
public class NavigationTag extends IncludeTag {

	public void setBulletStyle(String bulletStyle) {
		_bulletStyle = bulletStyle;
	}

	public void setDdmTemplateGroupId(long ddmTemplateGroupId) {
		_ddmTemplateGroupId = ddmTemplateGroupId;
	}

	public void setDdmTemplateKey(String ddmTemplateKey) {
		_ddmTemplateKey = ddmTemplateKey;
	}

	public void setHeaderType(String headerType) {
		_headerType = headerType;
	}

	public void setIncludedLayouts(String includedLayouts) {
		_includedLayouts = includedLayouts;
	}

	public void setNestedChildren(boolean nestedChildren) {
		_nestedChildren = nestedChildren;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		servletContext = ServletContextUtil.getServletContext();
	}

	public void setPreview(boolean preview) {
		_preview = preview;
	}

	public void setRootLayoutLevel(int rootLayoutLevel) {
		_rootLayoutLevel = rootLayoutLevel;
	}

	public void setRootLayoutType(String rootLayoutType) {
		_rootLayoutType = rootLayoutType;
	}

	@Override
	protected void cleanUp() {
		_bulletStyle = "1";
		_ddmTemplateGroupId = 0;
		_ddmTemplateKey = null;
		_headerType = "none";
		_includedLayouts = "auto";
		_nestedChildren = true;
		_preview = false;
		_rootLayoutLevel = 1;
		_rootLayoutType = "absolute";
	}

	protected String getDisplayStyle() {
		if (Validator.isNotNull(_ddmTemplateKey)) {
			return PortletDisplayTemplateManagerUtil.getDisplayStyle(
				_ddmTemplateKey);
		}

		return null;
	}

	protected long getDisplayStyleGroupId() {
		if (_ddmTemplateGroupId > 0) {
			return _ddmTemplateGroupId;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		return themeDisplay.getScopeGroupId();
	}

	protected List<NavItem> getNavItems(HttpServletRequest request)
		throws PortalException {

		List<NavItem> navItems = new ArrayList<>();

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		NavItem navItem = new NavItem(request, layout, null);

		navItems.add(navItem);

		for (Layout ancestorLayout : layout.getAncestors()) {
			navItems.add(0, new NavItem(request, ancestorLayout, null));
		}

		return navItems;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-site-navigation:navigation:bulletStyle", _bulletStyle);
		request.setAttribute(
			"liferay-site-navigation:navigation:displayStyle",
			getDisplayStyle());
		request.setAttribute(
			"liferay-site-navigation:navigation:displayStyleGroupId",
			String.valueOf(getDisplayStyleGroupId()));
		request.setAttribute(
			"liferay-site-navigation:navigation:headerType", _headerType);
		request.setAttribute(
			"liferay-site-navigation:navigation:includedLayouts",
			_includedLayouts);

		try {
			List<NavItem> navItems = getNavItems(request);

			request.setAttribute(
				"liferay-site-navigation:navigation:navItems", navItems);
		}
		catch (PortalException pe) {
			_log.error(pe);
		}

		request.setAttribute(
			"liferay-site-navigation:navigation:nestedChildren",
			String.valueOf(_nestedChildren));
		request.setAttribute(
			"liferay-site-navigation:navigation:preview",
			String.valueOf(_preview));
		request.setAttribute(
			"liferay-site-navigation:navigation:rootLayoutLevel",
			String.valueOf(_rootLayoutLevel));
		request.setAttribute(
			"liferay-site-navigation:navigation:rootLayoutType",
			_rootLayoutType);
	}

	private static final String _PAGE = "/navigation/page.jsp";

	private static final Log _log = LogFactoryUtil.getLog(NavigationTag.class);

	private String _bulletStyle = "1";
	private long _ddmTemplateGroupId;
	private String _ddmTemplateKey;
	private String _headerType = "none";
	private String _includedLayouts = "auto";
	private boolean _nestedChildren = true;
	private boolean _preview;
	private int _rootLayoutLevel = 1;
	private String _rootLayoutType = "absolute";

}