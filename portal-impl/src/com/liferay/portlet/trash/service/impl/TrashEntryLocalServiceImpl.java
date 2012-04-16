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

package com.liferay.portlet.trash.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.trash.model.TrashEntry;
import com.liferay.portlet.trash.model.impl.TrashEntryImpl;
import com.liferay.portlet.trash.service.base.TrashEntryLocalServiceBaseImpl;

import java.util.Date;
import java.util.List;

/**
 * @author Zsolt Berentey
 */
public class TrashEntryLocalServiceImpl extends TrashEntryLocalServiceBaseImpl {

	public TrashEntry addTrashEntry(
			long companyId, long groupId, String className, long classPK,
			int status, UnicodeProperties typeSettingsProperties)
		throws SystemException {

		long classNameId = PortalUtil.getClassNameId(className);

		long entryId = counterLocalService.increment();

		TrashEntryImpl trashEntry =
			(TrashEntryImpl)trashEntryPersistence.create(entryId);

		trashEntry.setCompanyId(companyId);
		trashEntry.setGroupId(groupId);
		trashEntry.setClassNameId(classNameId);
		trashEntry.setClassPK(classPK);
		trashEntry.setStatus(status);
		trashEntry.setTrashedDate(new Date());

		if (typeSettingsProperties != null) {
			trashEntry.setTypeSettingsProperties(typeSettingsProperties);
		}

		trashEntryPersistence.update(trashEntry, false);

		return trashEntry;
	}

	public void deleteEntry(String className, long classPK)
		throws PortalException, SystemException {

		long classNameId = PortalUtil.getClassNameId(className);

		trashEntryPersistence.removeByC_C(classNameId, classPK);
	}

	public TrashEntry fetchEntry(long entryId) throws SystemException {
		return trashEntryPersistence.fetchByPrimaryKey(entryId);
	}

	public TrashEntry fetchEntry(String className, long classPK)
		throws SystemException {

		long classNameId = PortalUtil.getClassNameId(className);

		return trashEntryPersistence.fetchByC_C(classNameId, classPK);
	}

	public List<TrashEntry> getEntries(long groupId) throws SystemException {
		return trashEntryPersistence.findByGroupId(groupId);
	}

	public List<TrashEntry> getEntries(long groupId, int start, int end)
		throws SystemException {

		return trashEntryPersistence.findByGroupId(groupId, start, end);
	}

	public int getEntriesCount(long groupId) throws SystemException {
		return trashEntryPersistence.countByGroupId(groupId);
	}

	public TrashEntry getEntry(long entryId)
		throws PortalException, SystemException {

		return trashEntryPersistence.findByPrimaryKey(entryId);
	}

	public TrashEntry getEntry(String className, long classPK)
		throws PortalException, SystemException {

		long classNameId = PortalUtil.getClassNameId(className);

		return trashEntryPersistence.findByC_C(classNameId, classPK);
	}

}