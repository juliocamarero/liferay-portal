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

package com.liferay.portlet.trash.service.persistence.impl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.model.impl.GroupImpl;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portlet.trash.model.TrashEntry;
import com.liferay.portlet.trash.model.impl.TrashEntryImpl;
import com.liferay.portlet.trash.service.persistence.TrashEntryFinder;
import com.liferay.util.dao.orm.CustomSQLUtil;

import java.util.List;

/**
 * @author Sampsa Sohlman
 */
public class TrashEntryFinderImpl extends BasePersistenceImpl<TrashEntry>
		implements TrashEntryFinder {

	public static final String FIND_CLEANABLE_ENTRIES =
		TrashEntryFinder.class.getName() + ".findCleanableEntries";

	public static final String FIND_TRASHENTRY_WITH_GROUP =
		TrashEntryFinder.class.getName() + ".findTrashEntryWithGroup";

	public List<TrashEntry> findCleanableEntries(
			long trashEntryId, long companyId, int start, int end)
		throws PortalException, SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_CLEANABLE_ENTRIES);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addEntity("te", TrashEntryImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			int currentTimeInMinutes =
				(int) (System.currentTimeMillis() / 60000L);

			int companyTrashEntriesMaxAge = PrefsPropsUtil.getInteger(
				companyId, PropsKeys.TRASH_ENTRIES_MAX_AGE);

			qPos.add(currentTimeInMinutes);
			qPos.add(companyTrashEntriesMaxAge);
			qPos.add(currentTimeInMinutes);

			qPos.add(currentTimeInMinutes);
			qPos.add(companyTrashEntriesMaxAge);
			qPos.add(currentTimeInMinutes);

			qPos.add(companyId);
			qPos.add(trashEntryId);

			return (List<TrashEntry>)QueryUtil.list(
				q, getDialect(), start, end);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List<Object[]> findTrashEntryWithGroup(
			long trashEntryId, int start, int end)
		throws PortalException, SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_TRASHENTRY_WITH_GROUP);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addEntity("te", TrashEntryImpl.class);
			q.addEntity("g", GroupImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(trashEntryId);

			return (List<Object[]>)QueryUtil.list(q, getDialect(), start, end);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

}