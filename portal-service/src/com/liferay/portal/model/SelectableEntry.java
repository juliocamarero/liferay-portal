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

import java.io.IOException;

import javax.portlet.PortletPreferences;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public interface SelectableEntry {

	public String getIcon();

	public String getKey();

	public String getLabel();

	public Double getWeight();

	public void include(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException;

	public boolean isEnabled();

	public boolean isSelected(
		HttpServletRequest request, PortletPreferences portletPreferences);

}