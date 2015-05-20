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

package com.liferay.portlet.messageboards.asset;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.asset.model.BaseAssetRenderer;
import com.liferay.portlet.messageboards.model.MBCategory;
import com.liferay.portlet.messageboards.service.permission.MBCategoryPermission;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

/**
 * @author Julio Camarero
 * @author Juan Fernández
 * @author Sergio González
 * @author Jonathan Lee
 */
public class MBCategoryAssetRenderer extends BaseAssetRenderer {

	public MBCategoryAssetRenderer(MBCategory category) {
		_category = category;
	}

	@Override
	public String getClassName() {
		return MBCategory.class.getName();
	}

	@Override
	public long getClassPK() {
		return _category.getCategoryId();
	}

	@Override
	public long getGroupId() {
		return _category.getGroupId();
	}

	@Override
	public String getStatusLabel() {
		return WorkflowConstants.getStatusLabel(_category.getStatus());
	}

	@Override
	public String getSummary(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		return _category.getDescription();
	}

	@Override
	public String getTitle(Locale locale) {
		return _category.getName();
	}

	@Override
	public PortletURL getURLEdit(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
			getControlPanelPlid(liferayPortletRequest),
			PortletKeys.MESSAGE_BOARDS, PortletRequest.RENDER_PHASE);

		portletURL.setParameter(
			"struts_action", "/message_boards/edit_category");
		portletURL.setParameter(
			"mbCategoryId", String.valueOf(_category.getCategoryId()));

		return portletURL;
	}

	@Override
	public PortletURL getURLView(
			LiferayPortletResponse liferayPortletResponse,
			WindowState windowState)
		throws Exception {

		AssetRendererFactory assetRendererFactory = getAssetRendererFactory();

		PortletURL portletURL = assetRendererFactory.getURLView(
			liferayPortletResponse, windowState);

		portletURL.setParameter("struts_action", "/message_boards/view");
		portletURL.setParameter(
			"mbCategoryId", String.valueOf(_category.getCategoryId()));
		portletURL.setWindowState(windowState);

		return portletURL;
	}

	@Override
	public String getURLViewInContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		String noSuchEntryRedirect) {

		return getURLViewInContext(
			liferayPortletRequest, noSuchEntryRedirect,
			"/message_boards/find_category", "mbCategoryId",
			_category.getCategoryId());
	}

	@Override
	public long getUserId() {
		return _category.getUserId();
	}

	@Override
	public String getUserName() {
		return _category.getUserName();
	}

	@Override
	public String getUuid() {
		return _category.getUuid();
	}

	@Override
	public boolean hasEditPermission(PermissionChecker permissionChecker)
		throws PortalException {

		return MBCategoryPermission.contains(
			permissionChecker, _category, ActionKeys.UPDATE);
	}

	@Override
	public boolean hasViewPermission(PermissionChecker permissionChecker)
		throws PortalException {

		return MBCategoryPermission.contains(
			permissionChecker, _category, ActionKeys.VIEW);
	}

	@Override
	public String render(
			PortletRequest portletRequest, PortletResponse portletResponse,
			String template)
		throws Exception {

		if (template.equals(TEMPLATE_ABSTRACT) ||
			template.equals(TEMPLATE_FULL_CONTENT)) {

			portletRequest.setAttribute(
				WebKeys.MESSAGE_BOARDS_CATEGORY, _category);

			return "/html/portlet/message_boards/asset/" + template + ".jsp";
		}
		else {
			return null;
		}
	}

	@Override
	protected String getIconPath(ThemeDisplay themeDisplay) {
		return themeDisplay.getPathThemeImages() + "/common/conversation.png";
	}

	private final MBCategory _category;

}