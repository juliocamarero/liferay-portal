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

import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.util.PropsValues;

public class SetupConfiguration {
	
	public SetupConfiguration()
	{
		_adminSetup = new SetupAdminInputs(
			PropsValues.SETUP_ADMIN_EMAIL, PropsValues.SETUP_ADMIN_PASSWORD,
			PropsValues.SETUP_ADMIN_USERNAME);
		_databaseSetup = new SetupDatabaseInputs(
			PropsValues.SETUP_DATABASE_NAME, PropsValues.SETUP_DATABASE_DRIVER,
			PropsValues.SETUP_DATABASE_PASSWORD, 
			PropsValues.SETUP_DATABASE_USERNAME);
		_portalSetup = new SetupPortalInputs(PropsValues.SETUP_PORTAL_SITENAME);
	}
	
	public SetupAdminInputs get_adminSetup() {
		return _adminSetup;
	}
	
	public void set_adminSetup(SetupAdminInputs adminSetup) {
		_adminSetup = adminSetup;
	}
	
	public SetupDatabaseInputs get_databaseSetup() {
		return _databaseSetup;
	}
	
	public void set_databaseSetup(SetupDatabaseInputs databaseSetup) {
		_databaseSetup = databaseSetup;
	}
	
	public SetupPortalInputs get_portalSetup() {
		return _portalSetup;
	}
	
	public void set_portalSetup(SetupPortalInputs portalSetup) {
		_portalSetup = portalSetup;
	}

	public String toProperties()
	{
		String basicPortalExt = "";
		
		//database properties
		
		basicPortalExt += _databaseSetup.toProperties();
		
		//admin user properties
		
		basicPortalExt += _adminSetup.toProperties();
		
		// portal properties
		
		basicPortalExt += _portalSetup.toProperties();
		
		//disable setup
		
		basicPortalExt += PropsKeys.SETUP_WIZARD_ENABLED + "=false\n";
		
		return basicPortalExt;
	}
	
	private SetupDatabaseInputs _databaseSetup;
	private SetupAdminInputs _adminSetup;
	private SetupPortalInputs _portalSetup;

}