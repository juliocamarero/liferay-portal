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

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

public class SetupTasks {

	public static void configAdministrator(HttpServletRequest request) {
		
		//read parameters
		
		String email = ParamUtil.getString(
			request, "setup_wizard.admin.email");
		String password = ParamUtil.getString(
			request, "setup_wizard.admin.password");
		String username = ParamUtil.getString(
			request, "setup_wizard.admin.username");
		
		SetupAdminInputs inputs = new SetupAdminInputs(email, 
			password, username);
		_setup.set_adminSetup(inputs);
		
		//process admin task
	}

	public static void configDatabase(HttpServletRequest request) {
		
		//read parameters

		String database = ParamUtil.getString(
			request, "setup_wizard.db.database-name");
		String driver = ParamUtil.getString(
			request, "setup_wizard.db.driver");
		String password = ParamUtil.getString(
			request, "setup_wizard.db.password");
		String username = ParamUtil.getString(
			request, "setup_wizard.db.username");
		
		SetupDatabaseInputs inputs = new SetupDatabaseInputs(database, 
				driver, password, username);
		
		_setup.set_databaseSetup(inputs);
		
		//process database task
	}

	public static void configPortal(HttpServletRequest request) {

		//read parameters
		
		String portalname = ParamUtil.getString(
			request, "setup_wizard.portal.name");
		
		SetupPortalInputs inputs = new SetupPortalInputs(portalname);
		_setup.set_portalSetup(inputs);
		
		//process portal task
	}
	
	public static void end(HttpServletRequest request) {
		_setup = null;
		
		request.getSession().removeAttribute(
				SetupConstants.LIFERAY_SETUP_CONFIG);
		request.getSession().removeAttribute(
				SetupConstants.LIFERAY_SETUP_DRIVERS);
	}

	public static void init(HttpServletRequest request) {
		if (_setup == null) {
			_setup = new SetupConfiguration();
		}
		
		request.getSession().setAttribute( 
			SetupConstants.LIFERAY_SETUP_CONFIG, _setup );
		
		List<String> drivers = 
			Arrays.asList(
				PropsUtil.getArray(PropsKeys.SETUP_DATABASE_DRIVERS_LIST));
		request.getSession().setAttribute( 
			SetupConstants.LIFERAY_SETUP_DRIVERS, drivers );
	}

	public static void writeSetupWizardFile()
	{
		String path = PropsValues.LIFERAY_WEB_PORTAL_DIR+"WEB-INF/classes/" +
			SetupConstants.LIFERAY_SETUP_PROPS_FILE;
		
		FileWriter fileWriter = null;
		BufferedWriter out = null;

		try {
			fileWriter = new FileWriter(path);
			out = new BufferedWriter(fileWriter);
			
			out.write(_setup.toProperties());
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static SetupConfiguration _setup;
}
