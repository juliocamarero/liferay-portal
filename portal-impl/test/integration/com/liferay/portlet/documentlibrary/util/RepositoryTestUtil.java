/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.documentlibrary.util;

import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.Repository;
import com.liferay.portal.service.RepositoryLocalServiceUtil;
import com.liferay.portal.service.RepositoryServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;

/**
 * @author Roberto Díaz
 */
public class RepositoryTestUtil {

	public static Repository addRepository(
			long userId, long classNameId, ServiceContext serviceContext,
			boolean secure)
		throws Exception {

		if (secure) {
			return RepositoryServiceUtil.addRepository(
				serviceContext.getScopeGroupId(), classNameId,
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				ServiceTestUtil.randomString(),
				ServiceTestUtil.randomString(50), PortletKeys.DOCUMENT_LIBRARY,
				new UnicodeProperties(), serviceContext);
		}
		else {
			return RepositoryLocalServiceUtil.addRepository(
				userId, serviceContext.getScopeGroupId(), classNameId,
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				ServiceTestUtil.randomString(),
				ServiceTestUtil.randomString(50), PortletKeys.DOCUMENT_LIBRARY,
				new UnicodeProperties(), false, serviceContext);
		}
	}

}