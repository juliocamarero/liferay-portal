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

package com.liferay.application.list.util;

import com.liferay.application.list.PanelApp;
import com.liferay.application.list.PanelCategory;
import com.liferay.application.list.PanelCategoryRegistry;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class PanelAppURLBuilder {

	public PanelAppURLBuilder(
		PanelApp panelApp, PanelCategoryRegistry panelCategoryRegistry) {

		_panelApp = panelApp;
		_panelCategoryRegistry = panelCategoryRegistry;
	}

	public PortletURL getPortletURL(HttpServletRequest request)
		throws PortalException {

		URLBuilder urlBuilder = new URLBuilder(request);

		String panelCategoryKey = _panelApp.getParentCategoryKey();

		if (Validator.isNull(panelCategoryKey) ||
			panelCategoryKey.equals(PanelCategoryKeys.ROOT)) {

			return _panelApp.getPortletURL(request, urlBuilder);
		}

		while (!panelCategoryKey.equals(PanelCategoryKeys.ROOT)) {
			PanelCategory panelCategory =
				_panelCategoryRegistry.getPanelCategory(panelCategoryKey);

			panelCategory.buildURL(request, urlBuilder);

			panelCategoryKey = panelCategory.getParentCategoryKey();
		}

		return _panelApp.getPortletURL(request, urlBuilder);
	}

	private final PanelApp _panelApp;
	private final PanelCategoryRegistry _panelCategoryRegistry;

}