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

package com.liferay.portlet.search.context;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.service.LayoutServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.provider.PortletProvider;
import com.liferay.portlet.asset.provider.PortletProviderUtil;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class SearchDisplayContext {

	public SearchDisplayContext(
		HttpServletRequest request, PortletPreferences portletPreferences) {

		_request = request;
		_portletPreferences = portletPreferences;
	}

	public String buildAssetCategoryPath(
			AssetCategory assetCategory, Locale locale)
		throws Exception {

		List<AssetCategory> assetCategories = assetCategory.getAncestors();

		if (assetCategories.isEmpty()) {
			return HtmlUtil.escape(assetCategory.getTitle(locale));
		}

		Collections.reverse(assetCategories);

		StringBundler sb = new StringBundler(assetCategories.size() * 2 + 1);

		for (AssetCategory curAssetCategory : assetCategories) {
			sb.append(HtmlUtil.escape(curAssetCategory.getTitle(locale)));
			sb.append(" &raquo; ");
		}

		sb.append(HtmlUtil.escape(assetCategory.getTitle(locale)));

		return sb.toString();
	}

	public String checkViewURL(
		String viewURL, String currentURL, boolean inheritRedirect) {

		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (Validator.isNotNull(viewURL) &&
			viewURL.startsWith(themeDisplay.getURLPortal())) {

			viewURL = HttpUtil.setParameter(
				viewURL, "inheritRedirect", inheritRedirect);

			if (!inheritRedirect) {
				viewURL = HttpUtil.setParameter(
					viewURL, "redirect", currentURL);
			}
		}

		return viewURL;
	}

	public PortletURL getViewFullContentURL(String className, Document document)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		String portletId = PortletProviderUtil.getPortletId(
			className, PortletProvider.Action.VIEW);

		long groupId = GetterUtil.getLong(document.get(Field.GROUP_ID));

		if (groupId == 0) {
			Layout layout = themeDisplay.getLayout();

			groupId = layout.getGroupId();
		}

		long scopeGroupId = GetterUtil.getLong(
			document.get(Field.SCOPE_GROUP_ID));

		if (scopeGroupId == 0) {
			scopeGroupId = themeDisplay.getScopeGroupId();
		}

		long plid = LayoutConstants.DEFAULT_PLID;

		Layout layout = (Layout)_request.getAttribute(WebKeys.LAYOUT);

		if (layout != null) {
			plid = layout.getPlid();
		}

		if (plid == 0) {
			plid = LayoutServiceUtil.getDefaultPlid(
				groupId, scopeGroupId, portletId);
		}

		PortletURL portletURL = PortletURLFactoryUtil.create(
			_request, portletId, plid, PortletRequest.RENDER_PHASE);

		portletURL.setPortletMode(PortletMode.VIEW);
		portletURL.setWindowState(WindowState.MAXIMIZED);

		return portletURL;
	}

	private final PortletPreferences _portletPreferences;
	private final HttpServletRequest _request;

}