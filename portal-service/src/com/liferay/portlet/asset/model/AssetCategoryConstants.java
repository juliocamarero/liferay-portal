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

package com.liferay.portlet.asset.model;

import com.liferay.portal.kernel.util.StringPool;

/**
 * @author Jorge Ferrer
 */
public class AssetCategoryConstants {

	public static final String ALL_CLASS_NAMES_AND_TYPES_ID =
		AssetCategoryConstants.ALL_CLASS_NAMES_ID + StringPool.COLON +
			AssetCategoryConstants.ALL_CLASS_TYPES_ID;

	public static final long ALL_CLASS_NAMES_ID = 0;

	public static final long ALL_CLASS_TYPES_ID = -1;

	public static final long DEFAULT_PARENT_CATEGORY_ID = 0;

	public static final String PROPERTY_KEY_VALUE_SEPARATOR = "_KEY_VALUE_";

}