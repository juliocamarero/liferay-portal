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

package com.liferay.portlet.trash.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link TrashEntryService}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       TrashEntryService
 * @generated
 */
public class TrashEntryServiceWrapper implements TrashEntryService,
	ServiceWrapper<TrashEntryService> {
	public TrashEntryServiceWrapper(TrashEntryService trashEntryService) {
		_trashEntryService = trashEntryService;
	}

	public java.lang.Object[] getEntries(long groupId)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.security.auth.PrincipalException {
		return _trashEntryService.getEntries(groupId);
	}

	public java.lang.Object[] getEntries(long groupId, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.security.auth.PrincipalException {
		return _trashEntryService.getEntries(groupId, start, end);
	}

	/**
	 * @deprecated Renamed to {@link #getWrappedService}
	 */
	public TrashEntryService getWrappedTrashEntryService() {
		return _trashEntryService;
	}

	/**
	 * @deprecated Renamed to {@link #setWrappedService}
	 */
	public void setWrappedTrashEntryService(TrashEntryService trashEntryService) {
		_trashEntryService = trashEntryService;
	}

	public TrashEntryService getWrappedService() {
		return _trashEntryService;
	}

	public void setWrappedService(TrashEntryService trashEntryService) {
		_trashEntryService = trashEntryService;
	}

	private TrashEntryService _trashEntryService;
}