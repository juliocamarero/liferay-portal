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

package com.liferay.asset.publisher.web.portlet.toolbar.contributor;

import com.liferay.asset.publisher.web.constants.AssetPublisherPortletKeys;
import com.liferay.asset.publisher.web.display.context.AssetPublisherDisplayContext;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.toolbar.contributor.BasePortletToolbarContributor;
import com.liferay.portal.kernel.portlet.toolbar.contributor.PortletToolbarContributor;
import com.liferay.portal.kernel.servlet.taglib.ui.MenuItem;
import com.liferay.portal.kernel.servlet.taglib.ui.URLMenuItem;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.asset.util.AssetUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eduardo Garcia
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + AssetPublisherPortletKeys.ASSET_PUBLISHER,
		"mvc.path=-", "mvc.path=/view.jsp"
	},
	service = {
		AssetPublisherPortletToolbarContributor.class,
		PortletToolbarContributor.class
	}
)
public class AssetPublisherPortletToolbarContributor
	extends BasePortletToolbarContributor {

	protected void addPortletTitleAddAssetEntryMenuItems(
			List<MenuItem> menuItems, PortletRequest portletRequest,
			PortletResponse portletResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String redirect = _getAddAssetEntryRedirectURL(
			themeDisplay, portletRequest);

		AssetPublisherDisplayContext assetPublisherDisplayContext =
			new AssetPublisherDisplayContext(
				PortalUtil.getHttpServletRequest(portletRequest),
				portletRequest.getPreferences());

		Map<String, PortletURL> addPortletURLs = AssetUtil.getAddPortletURLs(
			(LiferayPortletRequest)portletRequest,
			(LiferayPortletResponse)portletResponse,
			themeDisplay.getScopeGroupId(),
			assetPublisherDisplayContext.getClassNameIds(),
			assetPublisherDisplayContext.getClassTypeIds(),
			assetPublisherDisplayContext.getAllAssetCategoryIds(),
			assetPublisherDisplayContext.getAllAssetTagNames(), redirect);

		if (MapUtil.isEmpty(addPortletURLs)) {
			return;
		}

		for (Map.Entry<String, PortletURL> entry : addPortletURLs.entrySet()) {
			AssetRendererFactory<?> assetRendererFactory =
				AssetRendererFactoryRegistryUtil.
					getAssetRendererFactoryByClassName(
						_getClassName(entry.getKey()));

			URLMenuItem urlMenuItem = new URLMenuItem();

			String message = _getMessage(
				entry.getKey(), addPortletURLs, themeDisplay.getLocale());

			long curGroupId = groupId;

			Group group = _groupLocalService.fetchGroup(groupId);

			if (!group.isStagedPortlet(assetRendererFactory.getPortletId()) &&
				!group.isStagedRemotely()) {

				curGroupId = group.getLiveGroupId();
			}

			String url = _getURL(
				curGroupId, plid, entry.getValue(),
				assetRendererFactory.getPortletId(), message,
				addDisplayPageParameter, layout, portletResponse);

			urlMenuItem.setLabel(HtmlUtil.escape(message));
			urlMenuItem.setURL(url);
			urlMenuItem.setUseDialog(true);

			menuItems.add(urlMenuItem);
		}
	}

	@Override
	protected List<MenuItem> getPortletTitleMenuItems(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		List<MenuItem> menuItems = new ArrayList<>();

		try {
			addPortletTitleAddAssetEntryMenuItems(
				menuItems, portletRequest, portletResponse);
		}
		catch (Exception e) {
			_log.error("Unable to add folder menu item", e);
		}

		return menuItems;
	}

	@Reference(unbind = "-")
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	private String _getAddAssetEntryRedirectURL(
			ThemeDisplay themeDisplay, PortletRequest portletRequest)
		throws Exception {

		PortletURL redirectURL = PortletURLFactoryUtil.create(
			portletRequest, AssetPublisherPortletKeys.ASSET_PUBLISHER,
			themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

		redirectURL.setParameter(
			"hideDefaultSuccessMessage", Boolean.TRUE.toString());
		redirectURL.setParameter("mvcPath", "/add_asset_redirect.jsp");
		redirectURL.setParameter("redirect", themeDisplay.getURLCurrent());
		redirectURL.setWindowState(LiferayWindowState.POP_UP);

		return redirectURL.toString();
	}

	private String _getClassName(String className) {
		int pos = className.indexOf(AssetUtil.CLASSNAME_SEPARATOR);

		if (pos != -1) {
			className = className.substring(0, pos);
		}

		return className;
	}

	private String _getMessage(
		String className, Map<String, PortletURL> addPortletURLs,
		Locale locale) {

		String message = null;

		int pos = className.indexOf(AssetUtil.CLASSNAME_SEPARATOR);

		if (pos != -1) {
			message = className.substring(
				pos + AssetUtil.CLASSNAME_SEPARATOR.length());

			className = className.substring(0, pos);
		}

		AssetRendererFactory<?> assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				className);

		if (pos == -1) {
			message = assetRendererFactory.getTypeName(locale);
		}

		return message;
	}

	private String _getURL(
		long groupId, long plid, PortletURL addPortletURL, String portletId,
		String message, boolean addDisplayPageParameter, Layout layout,
		PortletResponse portletResponse) {

		addPortletURL.setParameter(
			"hideDefaultSuccessMessage", Boolean.TRUE.toString());
		addPortletURL.setParameter("groupId", String.valueOf(groupId));
		addPortletURL.setParameter("showHeader", Boolean.FALSE.toString());

		String addPortletURLString = addPortletURL.toString();

		addPortletURLString = HttpUtil.addParameter(
			addPortletURLString, "refererPlid", plid);

		String namespace = PortalUtil.getPortletNamespace(portletId);

		if (addDisplayPageParameter) {
			addPortletURLString = HttpUtil.addParameter(
				addPortletURLString, namespace + "layoutUuid",
				layout.getUuid());
		}

		return addPortletURLString;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AssetPublisherPortletToolbarContributor.class);

	private GroupLocalService _groupLocalService;

}