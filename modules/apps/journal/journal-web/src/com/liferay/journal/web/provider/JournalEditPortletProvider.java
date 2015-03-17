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

package com.liferay.journal.web.provider;

import com.liferay.journal.web.constants.JournalPortletKeys;
import com.liferay.portal.kernel.provider.EditPortletProvider;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eduardo Garcia
 */
@Component(
	immediate = true,
	property = {
		"model.class.name=com.liferay.portlet.journal.model.JournalArticle"
	},
	service = EditPortletProvider.class
)
public class JournalEditPortletProvider implements EditPortletProvider {

	@Override
	public String getPortletId() {
		return JournalPortletKeys.JOURNAL;
	}

}