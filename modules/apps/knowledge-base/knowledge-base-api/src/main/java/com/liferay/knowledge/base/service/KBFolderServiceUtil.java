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

package com.liferay.knowledge.base.service;

import aQute.bnd.annotation.ProviderType;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for KBFolder. This utility wraps
 * {@link com.liferay.knowledge.base.service.impl.KBFolderServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on a remote server. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see KBFolderService
 * @see com.liferay.knowledge.base.service.base.KBFolderServiceBaseImpl
 * @see com.liferay.knowledge.base.service.impl.KBFolderServiceImpl
 * @generated
 */
@ProviderType
public class KBFolderServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.knowledge.base.service.impl.KBFolderServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.knowledge.base.model.KBFolder addKBFolder(
		long groupId, long parentResourceClassNameId,
		long parentResourcePrimKey, java.lang.String name,
		java.lang.String description,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addKBFolder(groupId, parentResourceClassNameId,
			parentResourcePrimKey, name, description, serviceContext);
	}

	public static com.liferay.knowledge.base.model.KBFolder deleteKBFolder(
		long kbFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().deleteKBFolder(kbFolderId);
	}

	public static com.liferay.knowledge.base.model.KBFolder fetchFirstChildKBFolder(
		long groupId, long kbFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().fetchFirstChildKBFolder(groupId, kbFolderId);
	}

	public static com.liferay.knowledge.base.model.KBFolder fetchFirstChildKBFolder(
		long groupId, long kbFolderId,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.knowledge.base.model.KBFolder> obc)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().fetchFirstChildKBFolder(groupId, kbFolderId, obc);
	}

	public static com.liferay.knowledge.base.model.KBFolder fetchKBFolder(
		long kbFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().fetchKBFolder(kbFolderId);
	}

	public static com.liferay.knowledge.base.model.KBFolder fetchKBFolderByUrlTitle(
		long groupId, long parentKbFolderId, java.lang.String urlTitle)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .fetchKBFolderByUrlTitle(groupId, parentKbFolderId, urlTitle);
	}

	public static com.liferay.knowledge.base.model.KBFolder getKBFolder(
		long kbFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getKBFolder(kbFolderId);
	}

	public static com.liferay.knowledge.base.model.KBFolder getKBFolderByUrlTitle(
		long groupId, long parentKbFolderId, java.lang.String urlTitle)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .getKBFolderByUrlTitle(groupId, parentKbFolderId, urlTitle);
	}

	public static java.util.List<com.liferay.knowledge.base.model.KBFolder> getKBFolders(
		long groupId, long parentKBFolderId, int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getKBFolders(groupId, parentKBFolderId, start, end);
	}

	public static java.util.List<java.lang.Object> getKBFoldersAndKBArticles(
		long groupId, long parentResourcePrimKey, int status, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<?> orderByComparator) {
		return getService()
				   .getKBFoldersAndKBArticles(groupId, parentResourcePrimKey,
			status, start, end, orderByComparator);
	}

	public static int getKBFoldersAndKBArticlesCount(long groupId,
		long parentResourcePrimKey, int status) {
		return getService()
				   .getKBFoldersAndKBArticlesCount(groupId,
			parentResourcePrimKey, status);
	}

	public static int getKBFoldersCount(long groupId, long parentKBFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getKBFoldersCount(groupId, parentKBFolderId);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static java.lang.String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static void moveKBFolder(long kbFolderId, long parentKBFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService().moveKBFolder(kbFolderId, parentKBFolderId);
	}

	/**
	* @deprecated As of 1.1.0, replaced by {@link #updateKBFolder(long, long,
	long, String, String, ServiceContext)}
	*/
	@Deprecated
	public static com.liferay.knowledge.base.model.KBFolder updateKBFolder(
		long parentResourceClassNameId, long parentResourcePrimKey,
		long kbFolderId, java.lang.String name, java.lang.String description)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .updateKBFolder(parentResourceClassNameId,
			parentResourcePrimKey, kbFolderId, name, description);
	}

	public static com.liferay.knowledge.base.model.KBFolder updateKBFolder(
		long parentResourceClassNameId, long parentResourcePrimKey,
		long kbFolderId, java.lang.String name, java.lang.String description,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .updateKBFolder(parentResourceClassNameId,
			parentResourcePrimKey, kbFolderId, name, description, serviceContext);
	}

	public static KBFolderService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<KBFolderService, KBFolderService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(KBFolderService.class);

		ServiceTracker<KBFolderService, KBFolderService> serviceTracker = new ServiceTracker<KBFolderService, KBFolderService>(bundle.getBundleContext(),
				KBFolderService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
}