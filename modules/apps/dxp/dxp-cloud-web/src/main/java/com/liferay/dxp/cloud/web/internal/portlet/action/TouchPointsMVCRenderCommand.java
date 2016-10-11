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

package com.liferay.dxp.cloud.web.internal.portlet.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Bruno Basto
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=dxp_cloud_portlet", "mvc.command.name=TouchPointsHome"
	},
	service = MVCRenderCommand.class
)
public class TouchPointsMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		Template template = (Template)renderRequest.getAttribute(
			WebKeys.TEMPLATE);

		Map<String, Object> sidebarItem1 = new HashMap();
		sidebarItem1.put("href", "/web/guest/home/-/dxp/home");
		sidebarItem1.put("icon", "/o/dxp-cloud-web/images/dxp.png");

		Map<String, Object> sidebarItem2 = new HashMap();
		sidebarItem2.put("href", "/web/guest/home/-/dxp/assets");
		sidebarItem2.put("icon", "/o/dxp-cloud-web/images/Assets.png");

		Map<String, Object> sidebarItem3 = new HashMap();
		sidebarItem3.put("href", "/web/guest/home/-/dxp/campaigns");
		sidebarItem3.put("icon", "/o/dxp-cloud-web/images/Campaigns.png");

		Map<String, Object> sidebarItem4 = new HashMap();
		sidebarItem4.put("href", "/web/guest/home/-/dxp/contacts");
		sidebarItem4.put("icon", "/o/dxp-cloud-web/images/Contacts.png");

		Map<String, Object> sidebarItem5 = new HashMap();
		sidebarItem5.put("href", "/web/guest/home/-/dxp/lcs");
		sidebarItem5.put("icon", "/o/dxp-cloud-web/images/LCS.png");

		Map<String, Object> sidebarItem6 = new HashMap();
		sidebarItem6.put("href", "/web/guest/home/-/dxp/overview");
		sidebarItem6.put("icon", "/o/dxp-cloud-web/images/Overview.png");

		Map<String, Object> sidebarItem7 = new HashMap();
		sidebarItem7.put("href", "/web/guest/home/-/dxp/settings");
		sidebarItem7.put("icon", "/o/dxp-cloud-web/images/Settings.png");

		Map<String, Object> sidebarItem8 = new HashMap();
		sidebarItem8.put("href", "/web/guest/home/-/dxp/touchpoints");
		sidebarItem8.put("icon", "/o/dxp-cloud-web/images/Touchpoints.png");

		List<Map<String, Object>> sidebarItems = new ArrayList<>();
		sidebarItems.add(sidebarItem1);
		sidebarItems.add(sidebarItem2);
		sidebarItems.add(sidebarItem3);
		sidebarItems.add(sidebarItem4);
		sidebarItems.add(sidebarItem5);
		sidebarItems.add(sidebarItem6);
		sidebarItems.add(sidebarItem7);
		sidebarItems.add(sidebarItem8);

		template.put("sidebarItems", sidebarItems);

		return "TouchPointsHome";
	}

}