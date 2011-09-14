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

public class SetupPortalInputs {
	
	public SetupPortalInputs(String portalname) {
		_portalname = portalname;
	}
	
	public String get_portalname() {
		return _portalname;
	}

	public void set_portalname(String portalname) {
		_portalname = portalname;
	}

	public String toProperties()
	{
		String basicPortalExt = "";
		
		// portal properties
		
		basicPortalExt += PropsKeys.COMPANY_DEFAULT_WEB_ID + "=" + 
			this.get_portalname() + "\n";
		
		return basicPortalExt;
	}
	
	private String _portalname;
	
}
