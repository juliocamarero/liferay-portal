/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.setup;

/**
 * @author Manuel de la Peña
 * @author Brian Wing Shun Chan
 */
public class SetupConstants {

	public static final String LIFERAY_SETUP_CONFIG = "LIFERAY_SETUP_CONFIG";
	
	public static final String LIFERAY_SETUP_DRIVERS = "LIFERAY_SETUP_DRIVERS";
	
	public static final String LIFERAY_SETUP_ENABLED = "LIFERAY_SETUP_ENABLED";
	
	public static final String LIFERAY_STARTUP_FINISHED = 
		"LIFERAY_STARTUP_FINISHED";
	
	public static final String SETUP_WIZARD_CMD = "SETUP_WIZARD_CMD";
	
	private static final String SETUP_WIZARD_PAGE_BASE = "/html/portal/setup/";
	
	public static final String SETUP_WIZARD_PAGE_0 = 
		SETUP_WIZARD_PAGE_BASE + "step_0.jsp";
	
	public static final String SETUP_WIZARD_URL = "/c/portal/setup_wizard";
	
	public static final int SETUP_WIZARD_STEP_0 = 0;
	
	public static final int SETUP_WIZARD_STEP_1 = 10;
	
	public static final int SETUP_WIZARD_STEP_2 = 20;
	
	public static final int SETUP_WIZARD_STEP_3 = 30;
	
}