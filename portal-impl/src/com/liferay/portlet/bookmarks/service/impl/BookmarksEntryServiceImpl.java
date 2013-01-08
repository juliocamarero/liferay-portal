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

package com.liferay.portlet.bookmarks.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.service.base.BookmarksEntryServiceBaseImpl;
import com.liferay.portlet.bookmarks.service.permission.BookmarksEntryPermission;
import com.liferay.portlet.bookmarks.service.permission.BookmarksFolderPermission;
import com.liferay.portlet.bookmarks.util.comparator.EntryModifiedDateComparator;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @author Levente Hudák
 */
public class BookmarksEntryServiceImpl extends BookmarksEntryServiceBaseImpl {

	public BookmarksEntry addEntry(
			long groupId, long folderId, String name, String url,
			String description, ServiceContext serviceContext)
		throws PortalException, SystemException {

		BookmarksFolderPermission.check(
			getPermissionChecker(), groupId, folderId, ActionKeys.ADD_ENTRY);

		return bookmarksEntryLocalService.addEntry(
			getUserId(), groupId, folderId, name, url, description,
			serviceContext);
	}

	public void deleteEntry(long entryId)
		throws PortalException, SystemException {

		BookmarksEntryPermission.check(
			getPermissionChecker(), entryId, ActionKeys.DELETE);

		bookmarksEntryLocalService.deleteEntry(entryId);
	}

	public List<BookmarksEntry> getEntries(
			long groupId, long folderId, int start, int end)
		throws SystemException {

		return bookmarksEntryPersistence.filterFindByG_F_S(
			groupId, folderId, WorkflowConstants.STATUS_APPROVED, start, end);
	}

	public List<BookmarksEntry> getEntries(
			long groupId, long folderId, int start, int end,
			OrderByComparator orderByComparator)
		throws SystemException {

		return bookmarksEntryPersistence.filterFindByG_F_S(
			groupId, folderId, WorkflowConstants.STATUS_APPROVED, start, end,
			orderByComparator);
	}

	public int getEntriesCount(long groupId, long folderId)
		throws SystemException {

		return bookmarksEntryPersistence.filterCountByG_F_S(
			groupId, folderId, WorkflowConstants.STATUS_APPROVED);
	}

	public BookmarksEntry getEntry(long entryId)
		throws PortalException, SystemException {

		BookmarksEntryPermission.check(
			getPermissionChecker(), entryId, ActionKeys.VIEW);

		return bookmarksEntryLocalService.getEntry(entryId);
	}

	public int getFoldersEntriesCount(long groupId, List<Long> folderIds)
		throws SystemException {

		return bookmarksEntryPersistence.filterCountByG_F_S(
			groupId,
			ArrayUtil.toArray(folderIds.toArray(new Long[folderIds.size()])),
			WorkflowConstants.STATUS_APPROVED);
	}

	public List<BookmarksEntry> getGroupEntries(
			long groupId, int start, int end)
		throws SystemException {

		return bookmarksEntryPersistence.filterFindByG_S(
			groupId, WorkflowConstants.STATUS_APPROVED, start, end);
	}

	public List<BookmarksEntry> getGroupEntries(
			long groupId, long userId, int start, int end)
		throws SystemException {

		OrderByComparator orderByComparator = new EntryModifiedDateComparator();

		if (userId <= 0) {
			return bookmarksEntryPersistence.filterFindByG_S(
				groupId, WorkflowConstants.STATUS_APPROVED, start, end,
				orderByComparator);
		}
		else {
			return bookmarksEntryPersistence.filterFindByG_U_S(
				groupId, userId, WorkflowConstants.STATUS_APPROVED, start, end,
				orderByComparator);
		}
	}

	public int getGroupEntriesCount(long groupId) throws SystemException {
		return bookmarksEntryPersistence.filterCountByG_S(
			groupId, WorkflowConstants.STATUS_APPROVED);
	}

	public int getGroupEntriesCount(long groupId, long userId)
		throws SystemException {

		if (userId <= 0) {
			return getGroupEntriesCount(groupId);
		}
		else {
			return bookmarksEntryPersistence.filterCountByG_U_S(
				groupId, userId, WorkflowConstants.STATUS_APPROVED);
		}
	}

	public BookmarksEntry moveEntry(long entryId, long parentFolderId)
		throws PortalException, SystemException {

		BookmarksEntryPermission.check(
			getPermissionChecker(), entryId, ActionKeys.UPDATE);

		return bookmarksEntryLocalService.moveEntry(entryId, parentFolderId);
	}

	public BookmarksEntry moveEntryFromTrash(long entryId, long parentFolderId)
		throws PortalException, SystemException {

		BookmarksEntryPermission.check(
			getPermissionChecker(), entryId, ActionKeys.UPDATE);

		return bookmarksEntryLocalService.moveEntryFromTrash(
			getUserId(), entryId, parentFolderId);
	}

	public void moveEntryToTrash(long entryId)
		throws PortalException, SystemException {

		BookmarksEntryPermission.check(
			getPermissionChecker(), entryId, ActionKeys.DELETE);

		bookmarksEntryLocalService.moveEntryToTrash(getUserId(), entryId);
	}

	public BookmarksEntry openEntry(long entryId)
		throws PortalException, SystemException {

		BookmarksEntryPermission.check(
			getPermissionChecker(), entryId, ActionKeys.VIEW);

		return bookmarksEntryLocalService.openEntry(
			getGuestOrUserId(), entryId);
	}

	public void restoreEntryFromTrash(long entryId)
		throws PortalException, SystemException {

		BookmarksEntryPermission.check(
			getPermissionChecker(), entryId, ActionKeys.UPDATE);

		bookmarksEntryLocalService.restoreEntryFromTrash(getUserId(), entryId);
	}

	public void subscribeEntry(long entryId)
		throws PortalException, SystemException {

		BookmarksEntryPermission.check(
			getPermissionChecker(), entryId, ActionKeys.SUBSCRIBE);

		bookmarksEntryLocalService.subscribeEntry(getUserId(), entryId);
	}

	public void unsubscribeEntry(long entryId)
		throws PortalException, SystemException {

		BookmarksEntryPermission.check(
			getPermissionChecker(), entryId, ActionKeys.SUBSCRIBE);

		bookmarksEntryLocalService.unsubscribeEntry(getUserId(), entryId);
	}

	public BookmarksEntry updateEntry(
			long entryId, long groupId, long folderId, String name, String url,
			String description, ServiceContext serviceContext)
		throws PortalException, SystemException {

		BookmarksEntryPermission.check(
			getPermissionChecker(), entryId, ActionKeys.UPDATE);

		return bookmarksEntryLocalService.updateEntry(
			getUserId(), entryId, groupId, folderId, name, url, description,
			serviceContext);
	}

}