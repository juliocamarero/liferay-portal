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

package com.liferay.blogs.validator;

import com.liferay.asset.categories.validator.CardinalityAssetEntryValidatorHelper;
import com.liferay.blogs.kernel.model.BlogsEntry;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eduardo Perez Garcia
 */
@Component(
	immediate = true,
	property = {"model.class.name=com.liferay.blogs.kernel.model.BlogsEntry"},
	service = CardinalityAssetEntryValidatorHelper.class
)
public class BlogEntryCardinalityValidatorHelper
	implements CardinalityAssetEntryValidatorHelper {

	@Override
	public boolean isValidable(
		long groupId, String className, long classPK, long classTypePK,
		long[] categoryIds, String[] entryNames) {

		if (className.equals(BlogsEntry.class.getName()) &&
			(groupId == classPK)) {

			return false;
		}

		return true;
	}

}