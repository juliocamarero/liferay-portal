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
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.StagedModel;

/**
 * @author Mate Thurzo
 * @author Daniel Kocsis
 */
public class ExportImportPathUtil {

	public static final String ROOT_PATH_GROUPS = "/groups/";

	public static String getEntityPath(
		long primaryKey, Class clazz, PortletDataContext portletDataContext) {

		StringBundler sb = new StringBundler(6);

		sb.append(ROOT_PATH_GROUPS);
		sb.append(portletDataContext.getSourceGroupId());
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(clazz.getName());
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(primaryKey + ".xml");

		return sb.toString();
	}

	public static String getEntityPath(Object entity) {
		if (!(entity instanceof StagedModel)) {
			return StringPool.BLANK;
		}

		long groupId = ((StagedModel)entity).getGroupId();

		ClassedModel classedModel = (ClassedModel)entity;

		StringBundler sb = new StringBundler(6);

		sb.append(ROOT_PATH_GROUPS);
		sb.append(groupId);
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(classedModel.getModelClassName());
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(classedModel.getPrimaryKeyObj() + ".xml");

		return sb.toString();
	}

}