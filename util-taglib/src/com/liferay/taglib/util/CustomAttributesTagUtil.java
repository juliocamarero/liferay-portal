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

package com.liferay.taglib.util;

import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.List;

/**
 * @author Olaf Kock
 */
public class CustomAttributesTagUtil {

	public static List<String> filterAttributes(
		List<String> attributeNames, String ignoreAttributeNames) {

		List<String> filteredAttributes = ListUtil.copy(attributeNames);

		List<String> ignoredAttributeNamesList = ListUtil.toList(
			StringUtil.split(ignoreAttributeNames));

		for (String ignoreAttributeName : ignoredAttributeNamesList) {
			filteredAttributes.remove(ignoreAttributeName);
		}

		return filteredAttributes;
	}

}