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

package com.liferay.portlet.trash.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.liferay.portlet.trash.service.TrashEntryServiceUtil;

import java.rmi.RemoteException;

/**
 * <p>
 * This class provides a SOAP utility for the
 * {@link com.liferay.portlet.trash.service.TrashEntryServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it is difficult for SOAP to
 * support certain types.
 * </p>
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a {@link java.util.List}, that
 * is translated to an array of {@link com.liferay.portlet.trash.model.TrashEntrySoap}.
 * If the method in the service utility returns a
 * {@link com.liferay.portlet.trash.model.TrashEntry}, that is translated to a
 * {@link com.liferay.portlet.trash.model.TrashEntrySoap}. Methods that SOAP cannot
 * safely wire are skipped.
 * </p>
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform
 * compatible. SOAP allows different languages like Java, .NET, C++, PHP, and
 * even Perl, to call the generated services. One drawback of SOAP is that it is
 * slow because it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at http://localhost:8080/api/axis. Set the
 * property <b>axis.servlet.hosts.allowed</b> in portal.properties to configure
 * security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       TrashEntryServiceHttp
 * @see       com.liferay.portlet.trash.model.TrashEntrySoap
 * @see       com.liferay.portlet.trash.service.TrashEntryServiceUtil
 * @generated
 */
public class TrashEntryServiceSoap {
	/**
	* Deletes the trash entries with the matching group ID considering
	* permissions.
	*
	* @param groupId the primary key of the group
	* @throws PrincipalException if a principal exception occurred
	* @throws SystemException if a system exception occurred
	*/
	public static void deleteEntries(long groupId) throws RemoteException {
		try {
			TrashEntryServiceUtil.deleteEntries(groupId);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	/**
	* Returns the trash entries with the matching group ID.
	*
	* @param groupId the primary key of the group
	* @return the matching trash entries
	* @throws PrincipalException if a principal exception occurred
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portlet.trash.model.TrashEntryList getEntries(
		long groupId) throws RemoteException {
		try {
			com.liferay.portlet.trash.model.TrashEntryList returnValue = TrashEntryServiceUtil.getEntries(groupId);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
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
	public static com.liferay.portlet.trash.model.TrashEntryList getEntries(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator obc)
		throws RemoteException {
		try {
			com.liferay.portlet.trash.model.TrashEntryList returnValue = TrashEntryServiceUtil.getEntries(groupId,
					start, end, obc);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
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
	public static void moveEntry(long groupId, long entryId,
		long destinationContainerModelId,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			TrashEntryServiceUtil.moveEntry(groupId, entryId,
				destinationContainerModelId, serviceContext);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	private static Log _log = LogFactoryUtil.getLog(TrashEntryServiceSoap.class);
}