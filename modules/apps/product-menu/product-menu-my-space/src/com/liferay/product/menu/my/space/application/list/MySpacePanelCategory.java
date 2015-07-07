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

package com.liferay.product.menu.my.space.application.list;

import com.liferay.application.list.BasePanelCategory;
import com.liferay.application.list.PanelCategory;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.application.list.util.URLBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.service.LayoutLocalService;
import com.liferay.portal.theme.ThemeDisplay;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	immediate = true,
	property = {
		"panel.category.key=" + PanelCategoryKeys.ROOT,
		"service.ranking:Integer=200"
	},
	service = PanelCategory.class
)
public class MySpacePanelCategory extends BasePanelCategory {

	@Override
	public void buildURL(HttpServletRequest request, URLBuilder urlBuilder)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Group userPersonalPanelGroup = _groupLocalService.getGroup(
			themeDisplay.getCompanyId(), GroupConstants.USER_PERSONAL_PANEL);

		long plid = _layoutLocalService.getDefaultPlid(
			userPersonalPanelGroup.getGroupId(), true);

		urlBuilder.setPlid(plid);
	}

	@Override
	public String getIconCssClass() {
		return "icon-user";
	}

	@Override
	public String getKey() {
		return PanelCategoryKeys.MY_SPACE;
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(
			"content.Language", locale);

		return LanguageUtil.get(resourceBundle, "my-space");
	}

	@Override
	public String getParentCategoryKey() {
		return PanelCategoryKeys.ROOT;
	}

	@Reference(unbind = "-")
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	@Reference(unbind = "-")
	protected void setLayoutLocalService(
		LayoutLocalService layoutLocalService) {

		_layoutLocalService = layoutLocalService;
	}

	private GroupLocalService _groupLocalService;
	private LayoutLocalService _layoutLocalService;

}