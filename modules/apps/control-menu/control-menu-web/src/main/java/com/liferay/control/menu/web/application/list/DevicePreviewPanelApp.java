/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p/>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.control.menu.web.application.list;

import com.liferay.application.list.BaseJSPPanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.control.menu.application.list.SimulationPanelCategory;
import org.osgi.service.component.annotations.Component;

/**
 *
 * @author Eduardo Garcia
 */

@Component(
	immediate = true,
	property = {
		"panel.category.key=" + SimulationPanelCategory.SIMULATION,
		"service.ranking:Integer=100"
	},
	service = PanelApp.class
)
public class DevicePreviewPanelApp extends BaseJSPPanelApp {

	@Override
	public String getJspPath() {
		return null;
	}

	@Override
	public String getPortletId() {
		return null;
	}

}