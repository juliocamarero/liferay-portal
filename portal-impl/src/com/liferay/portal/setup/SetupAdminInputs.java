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

public class SetupAdminInputs {
	
	public SetupAdminInputs(String email, String password, String username) {
		_email = email;
		_password = password;
		_username = username;
	}
	
	public String get_email() {
		return _email;
	}
	
	public void set_email(String email) {
		_email = email;
	}
	
	public String get_password() {
		return _password;
	}
	
	public void set_password(String password) {
		_password = password;
	}
	
	public String get_username() {
		return _username;
	}
	
	public void set_username(String username) {
		_username = username;
	}
	
	public String toProperties()
	{
		String basicPortalExt = "";
		
		//default admin

		basicPortalExt += PropsKeys.DEFAULT_ADMIN_PASSWORD + "=" + 
			this.get_password() + "\n";
		basicPortalExt += PropsKeys.DEFAULT_ADMIN_SCREEN_NAME + "=" + 
			this.get_username() + "\n";
		
		//properties for portlets 
		
		basicPortalExt += PropsKeys.ADMIN_EMAIL_FROM_NAME + "=" + 
			this.get_username() + "\n";
		basicPortalExt += PropsKeys.ADMIN_EMAIL_FROM_ADDRESS + "=" + 
			this.get_email() + "\n";
		basicPortalExt += PropsKeys.ANNOUNCEMENTS_EMAIL_FROM_NAME + "=" + 
			this.get_username() + "\n";
		basicPortalExt += PropsKeys.ANNOUNCEMENTS_EMAIL_FROM_ADDRESS + "=" + 
			this.get_email() + "\n";
		basicPortalExt += PropsKeys.BLOGS_EMAIL_FROM_NAME + "=" + 
			this.get_username() + "\n";
		basicPortalExt += PropsKeys.BLOGS_EMAIL_FROM_ADDRESS + "=" + 
			this.get_email() + "\n";
		basicPortalExt += PropsKeys.CALENDAR_EMAIL_FROM_NAME + "=" + 
			this.get_username() + "\n";
		basicPortalExt += PropsKeys.CALENDAR_EMAIL_FROM_ADDRESS + "=" + 
			this.get_email() + "\n";
		basicPortalExt += PropsKeys.FLAGS_EMAIL_FROM_NAME + "=" + 
			this.get_username() + "\n";
		basicPortalExt += PropsKeys.FLAGS_EMAIL_FROM_ADDRESS + "=" + 
			this.get_email() + "\n";
		basicPortalExt += PropsKeys.JOURNAL_EMAIL_FROM_NAME + "=" + 
			this.get_username() + "\n";
		basicPortalExt += PropsKeys.JOURNAL_EMAIL_FROM_ADDRESS + "=" + 
			this.get_email() + "\n";
		basicPortalExt += PropsKeys.LOGIN_EMAIL_FROM_NAME + "=" + 
			this.get_username() + "\n";
		basicPortalExt += PropsKeys.LOGIN_EMAIL_FROM_ADDRESS + "=" + 
			this.get_email() + "\n";
		basicPortalExt += PropsKeys.MESSAGE_BOARDS_EMAIL_FROM_NAME + "=" + 
			this.get_username() + "\n";
		basicPortalExt += PropsKeys.MESSAGE_BOARDS_EMAIL_FROM_ADDRESS + "=" + 
			this.get_email() + "\n";
		basicPortalExt += PropsKeys.SITES_EMAIL_FROM_NAME + "=" + 
			this.get_username() + "\n";
		basicPortalExt += PropsKeys.SITES_EMAIL_FROM_ADDRESS + "=" + 
			this.get_email() + "\n";
		basicPortalExt += PropsKeys.SHOPPING_EMAIL_FROM_NAME + "=" + 
			this.get_username() + "\n";
		basicPortalExt += PropsKeys.SHOPPING_EMAIL_FROM_ADDRESS + "=" + 
			this.get_email() + "\n";
		basicPortalExt += PropsKeys.WIKI_EMAIL_FROM_NAME + "=" + 
			this.get_username() + "\n";
		basicPortalExt += PropsKeys.WIKI_EMAIL_FROM_ADDRESS + "=" + 
			this.get_email() + "\n";
		
		return basicPortalExt;
	}
	
	private String _email;
	private String _password;
	private String _username;
	
}