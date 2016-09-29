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

package com.liferay.document.library.validator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.asset.categories.validator.CardinalityAssetEntryValidatorHelper;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFileEntryConstants;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;

/**
 * @author Eduardo Perez Garcia
 */
@Component(
	immediate = true,
	property = {"model.class.name=com.liferay.blogs.kernel.model.BlogsEntry"},
	service = CardinalityAssetEntryValidatorHelper.class
)
public class DLFileEntryCardinalityValidatorHelper 
	implements CardinalityAssetEntryValidatorHelper{

	@Override
	public boolean isValidable(long groupId, String className, long classPK, long classTypePK, long[] categoryIds,
			String[] entryNames) {

		if (className.equals(DLFileEntryConstants.getClassName())) {
			DLFileEntry dlFileEntry = _dlFileEntryLocalService.fetchDLFileEntry(
				classTypePK);

			if ((dlFileEntry == null) ||
				(dlFileEntry.getRepositoryId() != groupId)) {

				return false;
			}
		}

		return true;
	}

	@Reference(unbind = "-")
	protected void setDLFileEntryLocalService(
		DLFileEntryLocalService dlFileEntryLocalService) {

		_dlFileEntryLocalService = dlFileEntryLocalService;
	}

	private DLFileEntryLocalService _dlFileEntryLocalService;

}
