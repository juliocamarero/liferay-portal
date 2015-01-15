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

package com.liferay.portlet.assetpublisher;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

/**
 * @author Eudaldo Alonso
 */
public class AssetPublisherPortlet extends MVCPortlet {

	@Override
	public void processAction(
			ActionMapping actionMapping, ActionForm actionForm,
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.SUBSCRIBE)) {
				subscribe((LiferayPortletConfig)portletConfig, actionRequest);
			}
			else if (cmd.equals(Constants.UNSUBSCRIBE)) {
				unsubscribe((LiferayPortletConfig)portletConfig, actionRequest);
			}

			sendRedirect(actionRequest, actionResponse);
		}
		catch (Exception e) {
			if (e instanceof PrincipalException) {
				SessionErrors.add(actionRequest, e.getClass());

				setForward(actionRequest, "portlet.asset_publisher.error");
			}
			else {
				throw e;
			}
		}
	}

	private void subscribe(
			LiferayPortletConfig liferayPortletConfig,
			ActionRequest actionRequest)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		AssetPublisherUtil.subscribe(
			themeDisplay.getPermissionChecker(), themeDisplay.getScopeGroupId(),
			themeDisplay.getPlid(), liferayPortletConfig.getPortletId());
	}

	private void unsubscribe(
			LiferayPortletConfig liferayPortletConfig,
			ActionRequest actionRequest)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		AssetPublisherUtil.unsubscribe(
			themeDisplay.getPermissionChecker(), themeDisplay.getPlid(),
			liferayPortletConfig.getPortletId());
	}

}