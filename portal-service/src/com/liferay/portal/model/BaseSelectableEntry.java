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

package com.liferay.portal.model;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PrefsParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import javax.portlet.PortletPreferences;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public abstract class BaseSelectableEntry implements SelectableEntry {

	@Override
	public String getIcon() {
		return _DEFAUTL_ICON;
	}

	public String getJSPPath() {
		return null;
	}

	@Override
	public String getKey() {
		return getClass().getSimpleName();
	}

	@Override
	public Double getWeight() {
		return 10.0;
	}

	@Override
	public void include(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		request.setAttribute(WebKeys.SELECTABLE_ENTRY, this);

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher(getJSPPath());

		try {
			requestDispatcher.include(request, response);
		}
		catch (ServletException se) {
			if (_log.isErrorEnabled()) {
				_log.error("Unable to include JSP", se);
			}

			throw new IOException("Unable to include JSP", se);
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isSelected(
		HttpServletRequest request, PortletPreferences portletPreferences) {

		if ((request != null) && (portletPreferences != null)) {
			return PrefsParamUtil.getBoolean(
				portletPreferences, request, getKey());
		}

		if (portletPreferences != null) {
			return GetterUtil.getBoolean(
				portletPreferences.getValue(getKey(), null));
		}

		if (request != null) {
			return ParamUtil.getBoolean(request, getKey());
		}

		return false;
	}

	public void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	private static final String _DEFAUTL_ICON = "circle-blank";

	private static final Log _log = LogFactoryUtil.getLog(
		BaseSelectableEntry.class);

	private ServletContext _servletContext;

}