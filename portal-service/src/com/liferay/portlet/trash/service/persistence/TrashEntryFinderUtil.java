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

package com.liferay.portlet.trash.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;

/**
 * @author Brian Wing Shun Chan
 */
@ProviderType
public class TrashEntryFinderUtil {
	public static java.util.List<java.lang.Object[]> findTrashEntryWithGroup(
		long trashEntryId, int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getFinder().findTrashEntryWithGroup(trashEntryId, start, end);
	}

	public static TrashEntryFinder getFinder() {
		if (_finder == null) {
			_finder = (TrashEntryFinder)PortalBeanLocatorUtil.locate(TrashEntryFinder.class.getName());

			ReferenceRegistry.registerReference(TrashEntryFinderUtil.class,
				"_finder");
		}

		return _finder;
	}

	public void setFinder(TrashEntryFinder finder) {
		_finder = finder;

		ReferenceRegistry.registerReference(TrashEntryFinderUtil.class,
			"_finder");
	}

	private static TrashEntryFinder _finder;
}