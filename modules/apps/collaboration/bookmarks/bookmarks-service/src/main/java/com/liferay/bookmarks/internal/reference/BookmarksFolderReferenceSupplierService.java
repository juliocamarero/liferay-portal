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

package com.liferay.bookmarks.internal.reference;

import com.liferay.bookmarks.model.BookmarksFolder;
import com.liferay.bookmarks.service.BookmarksEntryLocalService;
import com.liferay.bookmarks.service.BookmarksFolderLocalService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.reference.MultiReferenceSupplier;
import com.liferay.portal.reference.ReferenceSupplierService;
import com.liferay.portal.reference.ReferenceSuppliers;

import java.util.Objects;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Mate Thurzo
 */
@Component(immediate = true, service = ReferenceSupplierService.class)
public class BookmarksFolderReferenceSupplierService
	implements ReferenceSupplierService<BookmarksFolder> {

	@Override
	public DynamicQuery getDynamicQuery() {
		return _bookmarksFolderLocalService.dynamicQuery();
	}

	@Override
	public BaseLocalService getLocalService() {
		return _bookmarksFolderLocalService;
	}

	@Override
	public Class<BookmarksFolder> getProcessingClass() {
		return BookmarksFolder.class;
	}

	@Override
	public ReferenceSuppliers<BookmarksFolder> getReferenceSuppliers() {
		MultiReferenceSupplier<BookmarksFolder> folderEntriesSupplier =
			(folder) ->
				_bookmarksEntryLocalService.getEntries(
					folder.getGroupId(), folder.getFolderId(), -1, -1).
						stream().filter(Objects::nonNull).collect(
							Collectors.toList());

		return ReferenceSuppliers.create().withOutbound(folderEntriesSupplier);
	}

	@Reference
	private BookmarksEntryLocalService _bookmarksEntryLocalService;

	@Reference
	private BookmarksFolderLocalService _bookmarksFolderLocalService;

}