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

package com.liferay.dxp.cloud.sidebar.internal.context.contributor;

import com.liferay.portal.kernel.template.TemplateContextContributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 *
 */
@Component(
	immediate = true,
	property = {"type=" + TemplateContextContributor.TYPE_GLOBAL},
	service = TemplateContextContributor.class
)
public class SidebarItemsContextContributor
	implements TemplateContextContributor {

	@Override
	public void prepare(
		Map<String, Object> contextObjects, HttpServletRequest request) {

		Map<String, Object> sidebarItem1 = new HashMap();
		sidebarItem1.put("href", "/web/guest/home/-/dxp/home");
		sidebarItem1.put("icon", "/o/dxp-cloud-sidebar/img/dxp.png");

		Map<String, Object> sidebarItem2 = new HashMap();
		sidebarItem2.put("href", "/web/guest/home/-/dxp/assets");
		sidebarItem2.put("icon", "/o/dxp-cloud-sidebar/img/Assets.png");

		Map<String, Object> sidebarItem3 = new HashMap();
		sidebarItem3.put("href", "/web/guest/home/-/dxp/campaigns");
		sidebarItem3.put("icon", "/o/dxp-cloud-sidebar/img/Campaigns.png");

		Map<String, Object> sidebarItem4 = new HashMap();
		sidebarItem4.put("href", "/web/guest/home/-/dxp/contacts");
		sidebarItem4.put("icon", "/o/dxp-cloud-sidebar/img/Contacts.png");

		Map<String, Object> sidebarItem5 = new HashMap();
		sidebarItem5.put("href", "/web/guest/home/-/dxp/lcs");
		sidebarItem5.put("icon", "/o/dxp-cloud-sidebar/img/LCS.png");

		Map<String, Object> sidebarItem6 = new HashMap();
		sidebarItem6.put("href", "/web/guest/home/-/dxp/overview");
		sidebarItem6.put("icon", "/o/dxp-cloud-sidebar/img/Overview.png");

		Map<String, Object> sidebarItem7 = new HashMap();
		sidebarItem7.put("href", "/web/guest/home/-/dxp/settings");
		sidebarItem7.put("icon", "/o/dxp-cloud-sidebar/img/Settings.png");

		Map<String, Object> sidebarItem8 = new HashMap();
		sidebarItem8.put("href", "/web/guest/home/-/dxp/touchpoints");
		sidebarItem8.put("icon", "/o/dxp-cloud-sidebar/img/Touchpoints.png");

		List<Map<String, Object>> sidebarItems = new ArrayList<>();
		sidebarItems.add(sidebarItem1);
		sidebarItems.add(sidebarItem2);
		sidebarItems.add(sidebarItem3);
		sidebarItems.add(sidebarItem4);
		sidebarItems.add(sidebarItem5);
		sidebarItems.add(sidebarItem6);
		sidebarItems.add(sidebarItem7);
		sidebarItems.add(sidebarItem8);

		Map<String, Object> sidebar = new HashMap();
		sidebar.put("items", sidebarItems);

		contextObjects.put("sidebar", sidebar);
	}
}