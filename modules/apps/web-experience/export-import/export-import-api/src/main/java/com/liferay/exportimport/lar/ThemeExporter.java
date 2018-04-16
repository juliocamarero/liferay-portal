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

package com.liferay.exportimport.lar;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.LayoutSetBranch;

/**
 * @author Matthew Tambara
 */
public interface ThemeExporter {

	public void exportTheme(
			PortletDataContext portletDataContext, LayoutSet layoutSet)
		throws Exception;

	public void exportTheme(
			PortletDataContext portletDataContext,
			LayoutSetBranch layoutSetBranch)
		throws Exception;

}