/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.documentlibrary.service;

/**
 * <p>
 * This class is a wrapper for {@link DLSyncService}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       DLSyncService
 * @generated
 */
public class DLSyncServiceWrapper implements DLSyncService {
	public DLSyncServiceWrapper(DLSyncService dlSyncService) {
		_dlSyncService = dlSyncService;
	}

	public java.io.InputStream getFileDeltaAsStream(long fileEntryId,
		java.lang.String sourceVersion, java.lang.String destinationVersion)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _dlSyncService.getFileDeltaAsStream(fileEntryId, sourceVersion,
			destinationVersion);
	}

	public com.liferay.portal.kernel.repository.model.FileEntry updateFileEntry(
		long fileEntryId, java.lang.String sourceFileName,
		java.lang.String mimeType, java.lang.String title,
		java.lang.String description, java.lang.String changeLog,
		boolean majorVersion, java.io.InputStream deltaInputStream, long size,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _dlSyncService.updateFileEntry(fileEntryId, sourceFileName,
			mimeType, title, description, changeLog, majorVersion,
			deltaInputStream, size, serviceContext);
	}

	public DLSyncService getWrappedDLSyncService() {
		return _dlSyncService;
	}

	public void setWrappedDLSyncService(DLSyncService dlSyncService) {
		_dlSyncService = dlSyncService;
	}

	private DLSyncService _dlSyncService;
}