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

package com.liferay.product.navigation.control.menu.theme.contributor;

import com.liferay.layout.admin.web.control.menu.CustomizationSettingsProductNavigationControlMenuEntry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.template.TemplateContextContributor;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Julio Camarero
 */
@Component(
	immediate = true,
	property = {"type=" + TemplateContextContributor.TYPE_THEME},
	service = TemplateContextContributor.class
)
public class ProductNavigationControlMenuTemplateContextContributor
	implements TemplateContextContributor {

	@Override
	public void prepare(
		Map<String, Object> contextObjects, HttpServletRequest request) {

		if (!isShowControlMenu(request)) {
			return;
		}

		StringBuilder sb = new StringBuilder();

		sb.append(GetterUtil.getString(contextObjects.get("bodyCssClass")));
		sb.append(StringPool.SPACE);
		sb.append("has-control-menu");

		try {
			if (_customizationSettingsProductNavigationControlMenuEntry.isShow(
					request)) {

				sb.append(StringPool.SPACE);
				sb.append("has-customization-menu");
			}
		}
		catch (PortalException pe) {
			pe.printStackTrace();
		}

		contextObjects.put("bodyCssClass", sb.toString());
	}

	protected boolean isShowControlMenu(HttpServletRequest request) {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (themeDisplay.isImpersonated()) {
			return true;
		}

		if (!themeDisplay.isSignedIn()) {
			return false;
		}

		User user = themeDisplay.getUser();

		if (!user.isSetupComplete()) {
			return false;
		}

		return true;
	}

	@Reference(unbind = "-")
	protected void setCustomizationSettingsProductNavigationControlMenuEntry(
		CustomizationSettingsProductNavigationControlMenuEntry
			customizationSettingsProductNavigationControlMenuEntry) {

		this._customizationSettingsProductNavigationControlMenuEntry =
			customizationSettingsProductNavigationControlMenuEntry;
	}

	private CustomizationSettingsProductNavigationControlMenuEntry
		_customizationSettingsProductNavigationControlMenuEntry;

}