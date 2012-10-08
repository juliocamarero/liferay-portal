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

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public java.lang.String getBeanIdentifier() {
		return _trashEntryService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_trashEntryService.setBeanIdentifier(beanIdentifier);
	}

	/**
	* Deletes the trash entries with the matching group ID considering
	* permissions.
	*
	* @param groupId the primary key of the group
	* @throws PrincipalException if a principal exception occurred
	* @throws SystemException if a system exception occurred
	*/
	public void deleteEntries(long groupId)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.security.auth.PrincipalException {
		_trashEntryService.deleteEntries(groupId);
	}

	/**
	* Returns the trash entries with the matching group ID.
	*
	* @param groupId the primary key of the group
	* @return the matching trash entries
	* @throws PrincipalException if a principal exception occurred
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.trash.model.TrashEntryList getEntries(
		long groupId)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.security.auth.PrincipalException {
		return _trashEntryService.getEntries(groupId);
	}

	/**
	* Returns a range of all the trash entries matching the group ID.
	*
	* @param groupId the primary key of the group
	* @param start the lower bound of the range of trash entries to return
	* @param end the upper bound of the range of trash entries to return (not
	inclusive)
	* @param obc the comparator to order the trash entries (optionally
	<code>null</code>)
	* @return the range of matching trash entries ordered by comparator
	<code>obc</code>
	* @throws PrincipalException if a system exception occurred
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.trash.model.TrashEntryList getEntries(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator obc)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.security.auth.PrincipalException {
		return _trashEntryService.getEntries(groupId, start, end, obc);
	}

	/**
	* Restores the trash entry with the primary key by moving it to a new
	* location identified by destination container model ID.
	*
	* <p>
	* This method throws a PrincipalException if the user didn't have the
	* permissions to perform one of the necessary operations. The exception is
	* created with different messages for different operations:
	* </p>
	*
	* <ul>
	* <li>
	* trash.move.error - if the permission to add the item to the new
	* destination was missing
	* </li>
	* <li>
	* trash.restore.error - if the permission to restore the item from trash
	* was missing
	* </li>
	* </ul>
	*
	* @param groupId the primary key of the group
	* @param entryId the primary key of the trash entry
	* @param destinationContainerModelId the primary key of the new location
	* @param serviceContext the service context (optionally <code>null</code>)
	* @throws PortalException if the user didn't have permission to add the
	entry to its new location or to restore it from the trash in
	general
	* @throws SystemException if a system exception occurred
	*/
	public void moveEntry(long groupId, long entryId,
		long destinationContainerModelId,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		_trashEntryService.moveEntry(groupId, entryId,
			destinationContainerModelId, serviceContext);
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