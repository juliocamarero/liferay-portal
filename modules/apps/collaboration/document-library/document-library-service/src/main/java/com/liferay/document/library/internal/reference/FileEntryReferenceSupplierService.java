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

package com.liferay.document.library.internal.reference;

import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Repository;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.service.RepositoryLocalService;
import com.liferay.portal.reference.ReferenceSupplier;
import com.liferay.portal.reference.ReferenceSupplierService;
import com.liferay.portal.reference.ReferenceSuppliers;

import java.util.Optional;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Mate Thurzo
 */
public class FileEntryReferenceSupplierService
	implements ReferenceSupplierService<FileEntry> {

	@Override
	public DynamicQuery getDynamicQuery() {
		return _dlFileEntryLocalService.dynamicQuery();
	}

	@Override
	public BaseLocalService getLocalService() {
		return _dlFileEntryLocalService;
	}

	@Override
	public Class<FileEntry> getProcessingClass() {
		return FileEntry.class;
	}

	@Override
	public ReferenceSuppliers<FileEntry> getReferenceSuppliers() {
		ReferenceSupplier<FileEntry, Folder> folderReferenceSupplier =
			(fileEntry) ->
				Optional.of(fileEntry).
					filter(
						(fe) ->
							fe.getFolderId() !=
								DLFolderConstants.DEFAULT_PARENT_FOLDER_ID).
					flatMap(
						(filteredEntry) ->
							Optional.of(filteredEntry.getFolder()));

		ReferenceSupplier<FileEntry, Repository> repositoryReferenceSupplier =
			(fileEntry) ->
				Optional.of(fileEntry).
					filter(
						(fe) ->
							!fileEntry.isDefaultRepository()).
					flatMap((filteredEntry) -> _getRepository(filteredEntry));

		return ReferenceSuppliers.create().withOutbound(folderReferenceSupplier).
			withOutbound(repositoryReferenceSupplier);
	}

	private Optional<Repository> _getRepository(FileEntry fileEntry) {
		try {
			return Optional.of(
				_repositoryLocalService.getRepository(
					fileEntry.getRepositoryId()));
		}
		catch (PortalException pe) {
			return Optional.of(null);
		}
	}

	@Reference
	private DLFileEntryLocalService _dlFileEntryLocalService;

	@Reference
	private RepositoryLocalService _repositoryLocalService;

}