/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.StagedModel;

/**
 * @author Mate Thurzo
 * @author Daniel Kocsis
 */
public class ExportImportPathUtil {

	public static final String ROOT_PATH_GROUPS = "/groups/";

	public static final String ROOT_PATH_PORTLETS = "/portlets/";

	public static String getEntityPath(
		long primaryKey, Class clazz, PortletDataContext context) {

		StringBundler sb = new StringBundler(6);

		sb.append(ROOT_PATH_GROUPS);
		sb.append(context.getSourceGroupId());
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(clazz.getName());
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(primaryKey + ".xml");

		return sb.toString();
	}

	public static String getEntityPath(Object object) {
		if (!(object instanceof StagedModel)) {
			return StringPool.BLANK;
		}

		StagedModel stagedModel = (StagedModel)object;

		StringBundler sb = new StringBundler(6);

		sb.append(ROOT_PATH_GROUPS);
		sb.append(stagedModel.getGroupId());
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(stagedModel.getModelClassName());
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(stagedModel.getPrimaryKeyObj() + ".xml");

		return sb.toString();
	}

	public static String getSourcePortletPath(long groupid, String portletId) {
		return getSourceRootPath(groupid) + ROOT_PATH_PORTLETS + portletId;
	}

	public static String getSourceRootPath(long groupId) {
		return ROOT_PATH_GROUPS + groupId;
	}

}