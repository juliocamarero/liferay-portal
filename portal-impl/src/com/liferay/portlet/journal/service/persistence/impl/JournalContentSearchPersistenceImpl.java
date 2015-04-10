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

package com.liferay.portlet.journal.service.persistence.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import com.liferay.portlet.journal.NoSuchContentSearchException;
import com.liferay.portlet.journal.model.JournalContentSearch;
import com.liferay.portlet.journal.model.impl.JournalContentSearchImpl;
import com.liferay.portlet.journal.model.impl.JournalContentSearchModelImpl;
import com.liferay.portlet.journal.service.persistence.JournalContentSearchPersistence;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence implementation for the journal content search service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see JournalContentSearchPersistence
 * @see com.liferay.portlet.journal.service.persistence.JournalContentSearchUtil
 * @generated
 */
@ProviderType
public class JournalContentSearchPersistenceImpl extends BasePersistenceImpl<JournalContentSearch>
	implements JournalContentSearchPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link JournalContentSearchUtil} to access the journal content search persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = JournalContentSearchImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPID = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroupId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID =
		new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupId",
			new String[] { Long.class.getName() },
			JournalContentSearchModelImpl.GROUPID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_GROUPID = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the journal content searchs where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByGroupId(long groupId) {
		return findByGroupId(groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the journal content searchs where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @return the range of matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByGroupId(long groupId, int start,
		int end) {
		return findByGroupId(groupId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the journal content searchs where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByGroupId(long groupId, int start,
		int end, OrderByComparator<JournalContentSearch> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID;
			finderArgs = new Object[] { groupId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPID;
			finderArgs = new Object[] { groupId, start, end, orderByComparator };
		}

		List<JournalContentSearch> list = (List<JournalContentSearch>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (JournalContentSearch journalContentSearch : list) {
				if ((groupId != journalContentSearch.getGroupId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE);

			query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(JournalContentSearchModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (!pagination) {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first journal content search in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal content search
	 * @throws NoSuchContentSearchException if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch findByGroupId_First(long groupId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByGroupId_First(groupId,
				orderByComparator);

		if (journalContentSearch != null) {
			return journalContentSearch;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchContentSearchException(msg.toString());
	}

	/**
	 * Returns the first journal content search in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByGroupId_First(long groupId,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		List<JournalContentSearch> list = findByGroupId(groupId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last journal content search in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal content search
	 * @throws NoSuchContentSearchException if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch findByGroupId_Last(long groupId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByGroupId_Last(groupId,
				orderByComparator);

		if (journalContentSearch != null) {
			return journalContentSearch;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchContentSearchException(msg.toString());
	}

	/**
	 * Returns the last journal content search in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByGroupId_Last(long groupId,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		int count = countByGroupId(groupId);

		if (count == 0) {
			return null;
		}

		List<JournalContentSearch> list = findByGroupId(groupId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the journal content searchs before and after the current journal content search in the ordered set where groupId = &#63;.
	 *
	 * @param contentSearchId the primary key of the current journal content search
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next journal content search
	 * @throws NoSuchContentSearchException if a journal content search with the primary key could not be found
	 */
	@Override
	public JournalContentSearch[] findByGroupId_PrevAndNext(
		long contentSearchId, long groupId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = findByPrimaryKey(contentSearchId);

		Session session = null;

		try {
			session = openSession();

			JournalContentSearch[] array = new JournalContentSearchImpl[3];

			array[0] = getByGroupId_PrevAndNext(session, journalContentSearch,
					groupId, orderByComparator, true);

			array[1] = journalContentSearch;

			array[2] = getByGroupId_PrevAndNext(session, journalContentSearch,
					groupId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected JournalContentSearch getByGroupId_PrevAndNext(Session session,
		JournalContentSearch journalContentSearch, long groupId,
		OrderByComparator<JournalContentSearch> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE);

		query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(JournalContentSearchModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(journalContentSearch);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<JournalContentSearch> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the journal content searchs where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 */
	@Override
	public void removeByGroupId(long groupId) {
		for (JournalContentSearch journalContentSearch : findByGroupId(
				groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(journalContentSearch);
		}
	}

	/**
	 * Returns the number of journal content searchs where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching journal content searchs
	 */
	@Override
	public int countByGroupId(long groupId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_GROUPID;

		Object[] finderArgs = new Object[] { groupId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_JOURNALCONTENTSEARCH_WHERE);

			query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_GROUPID_GROUPID_2 = "journalContentSearch.groupId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_PORTLETID =
		new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByPortletId",
			new String[] {
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_PORTLETID =
		new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByPortletId",
			new String[] { String.class.getName() },
			JournalContentSearchModelImpl.PORTLETID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_PORTLETID = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByPortletId",
			new String[] { String.class.getName() });

	/**
	 * Returns all the journal content searchs where portletId = &#63;.
	 *
	 * @param portletId the portlet ID
	 * @return the matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByPortletId(String portletId) {
		return findByPortletId(portletId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the journal content searchs where portletId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param portletId the portlet ID
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @return the range of matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByPortletId(String portletId,
		int start, int end) {
		return findByPortletId(portletId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the journal content searchs where portletId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param portletId the portlet ID
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByPortletId(String portletId,
		int start, int end,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_PORTLETID;
			finderArgs = new Object[] { portletId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_PORTLETID;
			finderArgs = new Object[] { portletId, start, end, orderByComparator };
		}

		List<JournalContentSearch> list = (List<JournalContentSearch>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (JournalContentSearch journalContentSearch : list) {
				if (!Validator.equals(portletId,
							journalContentSearch.getPortletId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE);

			boolean bindPortletId = false;

			if (portletId == null) {
				query.append(_FINDER_COLUMN_PORTLETID_PORTLETID_1);
			}
			else if (portletId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_PORTLETID_PORTLETID_3);
			}
			else {
				bindPortletId = true;

				query.append(_FINDER_COLUMN_PORTLETID_PORTLETID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(JournalContentSearchModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindPortletId) {
					qPos.add(portletId);
				}

				if (!pagination) {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first journal content search in the ordered set where portletId = &#63;.
	 *
	 * @param portletId the portlet ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal content search
	 * @throws NoSuchContentSearchException if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch findByPortletId_First(String portletId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByPortletId_First(portletId,
				orderByComparator);

		if (journalContentSearch != null) {
			return journalContentSearch;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("portletId=");
		msg.append(portletId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchContentSearchException(msg.toString());
	}

	/**
	 * Returns the first journal content search in the ordered set where portletId = &#63;.
	 *
	 * @param portletId the portlet ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByPortletId_First(String portletId,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		List<JournalContentSearch> list = findByPortletId(portletId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last journal content search in the ordered set where portletId = &#63;.
	 *
	 * @param portletId the portlet ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal content search
	 * @throws NoSuchContentSearchException if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch findByPortletId_Last(String portletId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByPortletId_Last(portletId,
				orderByComparator);

		if (journalContentSearch != null) {
			return journalContentSearch;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("portletId=");
		msg.append(portletId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchContentSearchException(msg.toString());
	}

	/**
	 * Returns the last journal content search in the ordered set where portletId = &#63;.
	 *
	 * @param portletId the portlet ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByPortletId_Last(String portletId,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		int count = countByPortletId(portletId);

		if (count == 0) {
			return null;
		}

		List<JournalContentSearch> list = findByPortletId(portletId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the journal content searchs before and after the current journal content search in the ordered set where portletId = &#63;.
	 *
	 * @param contentSearchId the primary key of the current journal content search
	 * @param portletId the portlet ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next journal content search
	 * @throws NoSuchContentSearchException if a journal content search with the primary key could not be found
	 */
	@Override
	public JournalContentSearch[] findByPortletId_PrevAndNext(
		long contentSearchId, String portletId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = findByPrimaryKey(contentSearchId);

		Session session = null;

		try {
			session = openSession();

			JournalContentSearch[] array = new JournalContentSearchImpl[3];

			array[0] = getByPortletId_PrevAndNext(session,
					journalContentSearch, portletId, orderByComparator, true);

			array[1] = journalContentSearch;

			array[2] = getByPortletId_PrevAndNext(session,
					journalContentSearch, portletId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected JournalContentSearch getByPortletId_PrevAndNext(Session session,
		JournalContentSearch journalContentSearch, String portletId,
		OrderByComparator<JournalContentSearch> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE);

		boolean bindPortletId = false;

		if (portletId == null) {
			query.append(_FINDER_COLUMN_PORTLETID_PORTLETID_1);
		}
		else if (portletId.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_PORTLETID_PORTLETID_3);
		}
		else {
			bindPortletId = true;

			query.append(_FINDER_COLUMN_PORTLETID_PORTLETID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(JournalContentSearchModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindPortletId) {
			qPos.add(portletId);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(journalContentSearch);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<JournalContentSearch> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the journal content searchs where portletId = &#63; from the database.
	 *
	 * @param portletId the portlet ID
	 */
	@Override
	public void removeByPortletId(String portletId) {
		for (JournalContentSearch journalContentSearch : findByPortletId(
				portletId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(journalContentSearch);
		}
	}

	/**
	 * Returns the number of journal content searchs where portletId = &#63;.
	 *
	 * @param portletId the portlet ID
	 * @return the number of matching journal content searchs
	 */
	@Override
	public int countByPortletId(String portletId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_PORTLETID;

		Object[] finderArgs = new Object[] { portletId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_JOURNALCONTENTSEARCH_WHERE);

			boolean bindPortletId = false;

			if (portletId == null) {
				query.append(_FINDER_COLUMN_PORTLETID_PORTLETID_1);
			}
			else if (portletId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_PORTLETID_PORTLETID_3);
			}
			else {
				bindPortletId = true;

				query.append(_FINDER_COLUMN_PORTLETID_PORTLETID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindPortletId) {
					qPos.add(portletId);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_PORTLETID_PORTLETID_1 = "journalContentSearch.portletId IS NULL";
	private static final String _FINDER_COLUMN_PORTLETID_PORTLETID_2 = "journalContentSearch.portletId = ?";
	private static final String _FINDER_COLUMN_PORTLETID_PORTLETID_3 = "(journalContentSearch.portletId IS NULL OR journalContentSearch.portletId = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ARTICLEID =
		new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByArticleId",
			new String[] {
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ARTICLEID =
		new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByArticleId",
			new String[] { String.class.getName() },
			JournalContentSearchModelImpl.ARTICLEID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_ARTICLEID = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByArticleId",
			new String[] { String.class.getName() });

	/**
	 * Returns all the journal content searchs where articleId = &#63;.
	 *
	 * @param articleId the article ID
	 * @return the matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByArticleId(String articleId) {
		return findByArticleId(articleId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the journal content searchs where articleId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param articleId the article ID
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @return the range of matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByArticleId(String articleId,
		int start, int end) {
		return findByArticleId(articleId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the journal content searchs where articleId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param articleId the article ID
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByArticleId(String articleId,
		int start, int end,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ARTICLEID;
			finderArgs = new Object[] { articleId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ARTICLEID;
			finderArgs = new Object[] { articleId, start, end, orderByComparator };
		}

		List<JournalContentSearch> list = (List<JournalContentSearch>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (JournalContentSearch journalContentSearch : list) {
				if (!Validator.equals(articleId,
							journalContentSearch.getArticleId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE);

			boolean bindArticleId = false;

			if (articleId == null) {
				query.append(_FINDER_COLUMN_ARTICLEID_ARTICLEID_1);
			}
			else if (articleId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_ARTICLEID_ARTICLEID_3);
			}
			else {
				bindArticleId = true;

				query.append(_FINDER_COLUMN_ARTICLEID_ARTICLEID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(JournalContentSearchModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindArticleId) {
					qPos.add(articleId);
				}

				if (!pagination) {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first journal content search in the ordered set where articleId = &#63;.
	 *
	 * @param articleId the article ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal content search
	 * @throws NoSuchContentSearchException if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch findByArticleId_First(String articleId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByArticleId_First(articleId,
				orderByComparator);

		if (journalContentSearch != null) {
			return journalContentSearch;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("articleId=");
		msg.append(articleId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchContentSearchException(msg.toString());
	}

	/**
	 * Returns the first journal content search in the ordered set where articleId = &#63;.
	 *
	 * @param articleId the article ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByArticleId_First(String articleId,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		List<JournalContentSearch> list = findByArticleId(articleId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last journal content search in the ordered set where articleId = &#63;.
	 *
	 * @param articleId the article ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal content search
	 * @throws NoSuchContentSearchException if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch findByArticleId_Last(String articleId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByArticleId_Last(articleId,
				orderByComparator);

		if (journalContentSearch != null) {
			return journalContentSearch;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("articleId=");
		msg.append(articleId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchContentSearchException(msg.toString());
	}

	/**
	 * Returns the last journal content search in the ordered set where articleId = &#63;.
	 *
	 * @param articleId the article ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByArticleId_Last(String articleId,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		int count = countByArticleId(articleId);

		if (count == 0) {
			return null;
		}

		List<JournalContentSearch> list = findByArticleId(articleId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the journal content searchs before and after the current journal content search in the ordered set where articleId = &#63;.
	 *
	 * @param contentSearchId the primary key of the current journal content search
	 * @param articleId the article ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next journal content search
	 * @throws NoSuchContentSearchException if a journal content search with the primary key could not be found
	 */
	@Override
	public JournalContentSearch[] findByArticleId_PrevAndNext(
		long contentSearchId, String articleId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = findByPrimaryKey(contentSearchId);

		Session session = null;

		try {
			session = openSession();

			JournalContentSearch[] array = new JournalContentSearchImpl[3];

			array[0] = getByArticleId_PrevAndNext(session,
					journalContentSearch, articleId, orderByComparator, true);

			array[1] = journalContentSearch;

			array[2] = getByArticleId_PrevAndNext(session,
					journalContentSearch, articleId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected JournalContentSearch getByArticleId_PrevAndNext(Session session,
		JournalContentSearch journalContentSearch, String articleId,
		OrderByComparator<JournalContentSearch> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE);

		boolean bindArticleId = false;

		if (articleId == null) {
			query.append(_FINDER_COLUMN_ARTICLEID_ARTICLEID_1);
		}
		else if (articleId.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_ARTICLEID_ARTICLEID_3);
		}
		else {
			bindArticleId = true;

			query.append(_FINDER_COLUMN_ARTICLEID_ARTICLEID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(JournalContentSearchModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindArticleId) {
			qPos.add(articleId);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(journalContentSearch);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<JournalContentSearch> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the journal content searchs where articleId = &#63; from the database.
	 *
	 * @param articleId the article ID
	 */
	@Override
	public void removeByArticleId(String articleId) {
		for (JournalContentSearch journalContentSearch : findByArticleId(
				articleId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(journalContentSearch);
		}
	}

	/**
	 * Returns the number of journal content searchs where articleId = &#63;.
	 *
	 * @param articleId the article ID
	 * @return the number of matching journal content searchs
	 */
	@Override
	public int countByArticleId(String articleId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_ARTICLEID;

		Object[] finderArgs = new Object[] { articleId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_JOURNALCONTENTSEARCH_WHERE);

			boolean bindArticleId = false;

			if (articleId == null) {
				query.append(_FINDER_COLUMN_ARTICLEID_ARTICLEID_1);
			}
			else if (articleId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_ARTICLEID_ARTICLEID_3);
			}
			else {
				bindArticleId = true;

				query.append(_FINDER_COLUMN_ARTICLEID_ARTICLEID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindArticleId) {
					qPos.add(articleId);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_ARTICLEID_ARTICLEID_1 = "journalContentSearch.articleId IS NULL";
	private static final String _FINDER_COLUMN_ARTICLEID_ARTICLEID_2 = "journalContentSearch.articleId = ?";
	private static final String _FINDER_COLUMN_ARTICLEID_ARTICLEID_3 = "(journalContentSearch.articleId IS NULL OR journalContentSearch.articleId = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_L = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_L",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_L = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_L",
			new String[] { Long.class.getName(), Long.class.getName() },
			JournalContentSearchModelImpl.GROUPID_COLUMN_BITMASK |
			JournalContentSearchModelImpl.LAYOUTID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_L = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_L",
			new String[] { Long.class.getName(), Long.class.getName() });

	/**
	 * Returns all the journal content searchs where groupId = &#63; and layoutId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @return the matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByG_L(long groupId, long layoutId) {
		return findByG_L(groupId, layoutId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the journal content searchs where groupId = &#63; and layoutId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @return the range of matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByG_L(long groupId, long layoutId,
		int start, int end) {
		return findByG_L(groupId, layoutId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the journal content searchs where groupId = &#63; and layoutId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByG_L(long groupId, long layoutId,
		int start, int end,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_L;
			finderArgs = new Object[] { groupId, layoutId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_L;
			finderArgs = new Object[] {
					groupId, layoutId,
					
					start, end, orderByComparator
				};
		}

		List<JournalContentSearch> list = (List<JournalContentSearch>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (JournalContentSearch journalContentSearch : list) {
				if ((groupId != journalContentSearch.getGroupId()) ||
						(layoutId != journalContentSearch.getLayoutId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE);

			query.append(_FINDER_COLUMN_G_L_GROUPID_2);

			query.append(_FINDER_COLUMN_G_L_LAYOUTID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(JournalContentSearchModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(layoutId);

				if (!pagination) {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first journal content search in the ordered set where groupId = &#63; and layoutId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal content search
	 * @throws NoSuchContentSearchException if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch findByG_L_First(long groupId, long layoutId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByG_L_First(groupId,
				layoutId, orderByComparator);

		if (journalContentSearch != null) {
			return journalContentSearch;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", layoutId=");
		msg.append(layoutId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchContentSearchException(msg.toString());
	}

	/**
	 * Returns the first journal content search in the ordered set where groupId = &#63; and layoutId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByG_L_First(long groupId, long layoutId,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		List<JournalContentSearch> list = findByG_L(groupId, layoutId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last journal content search in the ordered set where groupId = &#63; and layoutId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal content search
	 * @throws NoSuchContentSearchException if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch findByG_L_Last(long groupId, long layoutId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByG_L_Last(groupId,
				layoutId, orderByComparator);

		if (journalContentSearch != null) {
			return journalContentSearch;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", layoutId=");
		msg.append(layoutId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchContentSearchException(msg.toString());
	}

	/**
	 * Returns the last journal content search in the ordered set where groupId = &#63; and layoutId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByG_L_Last(long groupId, long layoutId,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		int count = countByG_L(groupId, layoutId);

		if (count == 0) {
			return null;
		}

		List<JournalContentSearch> list = findByG_L(groupId, layoutId,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the journal content searchs before and after the current journal content search in the ordered set where groupId = &#63; and layoutId = &#63;.
	 *
	 * @param contentSearchId the primary key of the current journal content search
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next journal content search
	 * @throws NoSuchContentSearchException if a journal content search with the primary key could not be found
	 */
	@Override
	public JournalContentSearch[] findByG_L_PrevAndNext(long contentSearchId,
		long groupId, long layoutId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = findByPrimaryKey(contentSearchId);

		Session session = null;

		try {
			session = openSession();

			JournalContentSearch[] array = new JournalContentSearchImpl[3];

			array[0] = getByG_L_PrevAndNext(session, journalContentSearch,
					groupId, layoutId, orderByComparator, true);

			array[1] = journalContentSearch;

			array[2] = getByG_L_PrevAndNext(session, journalContentSearch,
					groupId, layoutId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected JournalContentSearch getByG_L_PrevAndNext(Session session,
		JournalContentSearch journalContentSearch, long groupId, long layoutId,
		OrderByComparator<JournalContentSearch> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE);

		query.append(_FINDER_COLUMN_G_L_GROUPID_2);

		query.append(_FINDER_COLUMN_G_L_LAYOUTID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(JournalContentSearchModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		qPos.add(layoutId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(journalContentSearch);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<JournalContentSearch> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the journal content searchs where groupId = &#63; and layoutId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 */
	@Override
	public void removeByG_L(long groupId, long layoutId) {
		for (JournalContentSearch journalContentSearch : findByG_L(groupId,
				layoutId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(journalContentSearch);
		}
	}

	/**
	 * Returns the number of journal content searchs where groupId = &#63; and layoutId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @return the number of matching journal content searchs
	 */
	@Override
	public int countByG_L(long groupId, long layoutId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_L;

		Object[] finderArgs = new Object[] { groupId, layoutId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_JOURNALCONTENTSEARCH_WHERE);

			query.append(_FINDER_COLUMN_G_L_GROUPID_2);

			query.append(_FINDER_COLUMN_G_L_LAYOUTID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(layoutId);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_L_GROUPID_2 = "journalContentSearch.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_L_LAYOUTID_2 = "journalContentSearch.layoutId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_A = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_A",
			new String[] {
				Long.class.getName(), String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_A = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_A",
			new String[] { Long.class.getName(), String.class.getName() },
			JournalContentSearchModelImpl.GROUPID_COLUMN_BITMASK |
			JournalContentSearchModelImpl.ARTICLEID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_A = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_A",
			new String[] { Long.class.getName(), String.class.getName() });

	/**
	 * Returns all the journal content searchs where groupId = &#63; and articleId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @return the matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByG_A(long groupId, String articleId) {
		return findByG_A(groupId, articleId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the journal content searchs where groupId = &#63; and articleId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @return the range of matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByG_A(long groupId, String articleId,
		int start, int end) {
		return findByG_A(groupId, articleId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the journal content searchs where groupId = &#63; and articleId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByG_A(long groupId, String articleId,
		int start, int end,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_A;
			finderArgs = new Object[] { groupId, articleId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_A;
			finderArgs = new Object[] {
					groupId, articleId,
					
					start, end, orderByComparator
				};
		}

		List<JournalContentSearch> list = (List<JournalContentSearch>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (JournalContentSearch journalContentSearch : list) {
				if ((groupId != journalContentSearch.getGroupId()) ||
						!Validator.equals(articleId,
							journalContentSearch.getArticleId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE);

			query.append(_FINDER_COLUMN_G_A_GROUPID_2);

			boolean bindArticleId = false;

			if (articleId == null) {
				query.append(_FINDER_COLUMN_G_A_ARTICLEID_1);
			}
			else if (articleId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_A_ARTICLEID_3);
			}
			else {
				bindArticleId = true;

				query.append(_FINDER_COLUMN_G_A_ARTICLEID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(JournalContentSearchModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindArticleId) {
					qPos.add(articleId);
				}

				if (!pagination) {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first journal content search in the ordered set where groupId = &#63; and articleId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal content search
	 * @throws NoSuchContentSearchException if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch findByG_A_First(long groupId, String articleId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByG_A_First(groupId,
				articleId, orderByComparator);

		if (journalContentSearch != null) {
			return journalContentSearch;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", articleId=");
		msg.append(articleId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchContentSearchException(msg.toString());
	}

	/**
	 * Returns the first journal content search in the ordered set where groupId = &#63; and articleId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByG_A_First(long groupId,
		String articleId,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		List<JournalContentSearch> list = findByG_A(groupId, articleId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last journal content search in the ordered set where groupId = &#63; and articleId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal content search
	 * @throws NoSuchContentSearchException if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch findByG_A_Last(long groupId, String articleId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByG_A_Last(groupId,
				articleId, orderByComparator);

		if (journalContentSearch != null) {
			return journalContentSearch;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", articleId=");
		msg.append(articleId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchContentSearchException(msg.toString());
	}

	/**
	 * Returns the last journal content search in the ordered set where groupId = &#63; and articleId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByG_A_Last(long groupId, String articleId,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		int count = countByG_A(groupId, articleId);

		if (count == 0) {
			return null;
		}

		List<JournalContentSearch> list = findByG_A(groupId, articleId,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the journal content searchs before and after the current journal content search in the ordered set where groupId = &#63; and articleId = &#63;.
	 *
	 * @param contentSearchId the primary key of the current journal content search
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next journal content search
	 * @throws NoSuchContentSearchException if a journal content search with the primary key could not be found
	 */
	@Override
	public JournalContentSearch[] findByG_A_PrevAndNext(long contentSearchId,
		long groupId, String articleId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = findByPrimaryKey(contentSearchId);

		Session session = null;

		try {
			session = openSession();

			JournalContentSearch[] array = new JournalContentSearchImpl[3];

			array[0] = getByG_A_PrevAndNext(session, journalContentSearch,
					groupId, articleId, orderByComparator, true);

			array[1] = journalContentSearch;

			array[2] = getByG_A_PrevAndNext(session, journalContentSearch,
					groupId, articleId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected JournalContentSearch getByG_A_PrevAndNext(Session session,
		JournalContentSearch journalContentSearch, long groupId,
		String articleId,
		OrderByComparator<JournalContentSearch> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE);

		query.append(_FINDER_COLUMN_G_A_GROUPID_2);

		boolean bindArticleId = false;

		if (articleId == null) {
			query.append(_FINDER_COLUMN_G_A_ARTICLEID_1);
		}
		else if (articleId.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_G_A_ARTICLEID_3);
		}
		else {
			bindArticleId = true;

			query.append(_FINDER_COLUMN_G_A_ARTICLEID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(JournalContentSearchModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		if (bindArticleId) {
			qPos.add(articleId);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(journalContentSearch);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<JournalContentSearch> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the journal content searchs where groupId = &#63; and articleId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 */
	@Override
	public void removeByG_A(long groupId, String articleId) {
		for (JournalContentSearch journalContentSearch : findByG_A(groupId,
				articleId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(journalContentSearch);
		}
	}

	/**
	 * Returns the number of journal content searchs where groupId = &#63; and articleId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @return the number of matching journal content searchs
	 */
	@Override
	public int countByG_A(long groupId, String articleId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_A;

		Object[] finderArgs = new Object[] { groupId, articleId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_JOURNALCONTENTSEARCH_WHERE);

			query.append(_FINDER_COLUMN_G_A_GROUPID_2);

			boolean bindArticleId = false;

			if (articleId == null) {
				query.append(_FINDER_COLUMN_G_A_ARTICLEID_1);
			}
			else if (articleId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_A_ARTICLEID_3);
			}
			else {
				bindArticleId = true;

				query.append(_FINDER_COLUMN_G_A_ARTICLEID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindArticleId) {
					qPos.add(articleId);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_A_GROUPID_2 = "journalContentSearch.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_A_ARTICLEID_1 = "journalContentSearch.articleId IS NULL";
	private static final String _FINDER_COLUMN_G_A_ARTICLEID_2 = "journalContentSearch.articleId = ?";
	private static final String _FINDER_COLUMN_G_A_ARTICLEID_3 = "(journalContentSearch.articleId IS NULL OR journalContentSearch.articleId = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_L_P = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_L_P",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_L_P = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_L_P",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName()
			},
			JournalContentSearchModelImpl.GROUPID_COLUMN_BITMASK |
			JournalContentSearchModelImpl.LAYOUTID_COLUMN_BITMASK |
			JournalContentSearchModelImpl.PORTLETID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_L_P = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_L_P",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName()
			});

	/**
	 * Returns all the journal content searchs where groupId = &#63; and layoutId = &#63; and portletId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @return the matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByG_L_P(long groupId, long layoutId,
		String portletId) {
		return findByG_L_P(groupId, layoutId, portletId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the journal content searchs where groupId = &#63; and layoutId = &#63; and portletId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @return the range of matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByG_L_P(long groupId, long layoutId,
		String portletId, int start, int end) {
		return findByG_L_P(groupId, layoutId, portletId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the journal content searchs where groupId = &#63; and layoutId = &#63; and portletId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findByG_L_P(long groupId, long layoutId,
		String portletId, int start, int end,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_L_P;
			finderArgs = new Object[] { groupId, layoutId, portletId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_L_P;
			finderArgs = new Object[] {
					groupId, layoutId, portletId,
					
					start, end, orderByComparator
				};
		}

		List<JournalContentSearch> list = (List<JournalContentSearch>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (JournalContentSearch journalContentSearch : list) {
				if ((groupId != journalContentSearch.getGroupId()) ||
						(layoutId != journalContentSearch.getLayoutId()) ||
						!Validator.equals(portletId,
							journalContentSearch.getPortletId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(5 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE);

			query.append(_FINDER_COLUMN_G_L_P_GROUPID_2);

			query.append(_FINDER_COLUMN_G_L_P_LAYOUTID_2);

			boolean bindPortletId = false;

			if (portletId == null) {
				query.append(_FINDER_COLUMN_G_L_P_PORTLETID_1);
			}
			else if (portletId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_L_P_PORTLETID_3);
			}
			else {
				bindPortletId = true;

				query.append(_FINDER_COLUMN_G_L_P_PORTLETID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(JournalContentSearchModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(layoutId);

				if (bindPortletId) {
					qPos.add(portletId);
				}

				if (!pagination) {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first journal content search in the ordered set where groupId = &#63; and layoutId = &#63; and portletId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal content search
	 * @throws NoSuchContentSearchException if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch findByG_L_P_First(long groupId, long layoutId,
		String portletId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByG_L_P_First(groupId,
				layoutId, portletId, orderByComparator);

		if (journalContentSearch != null) {
			return journalContentSearch;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", layoutId=");
		msg.append(layoutId);

		msg.append(", portletId=");
		msg.append(portletId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchContentSearchException(msg.toString());
	}

	/**
	 * Returns the first journal content search in the ordered set where groupId = &#63; and layoutId = &#63; and portletId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByG_L_P_First(long groupId, long layoutId,
		String portletId,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		List<JournalContentSearch> list = findByG_L_P(groupId, layoutId,
				portletId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last journal content search in the ordered set where groupId = &#63; and layoutId = &#63; and portletId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal content search
	 * @throws NoSuchContentSearchException if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch findByG_L_P_Last(long groupId, long layoutId,
		String portletId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByG_L_P_Last(groupId,
				layoutId, portletId, orderByComparator);

		if (journalContentSearch != null) {
			return journalContentSearch;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", layoutId=");
		msg.append(layoutId);

		msg.append(", portletId=");
		msg.append(portletId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchContentSearchException(msg.toString());
	}

	/**
	 * Returns the last journal content search in the ordered set where groupId = &#63; and layoutId = &#63; and portletId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByG_L_P_Last(long groupId, long layoutId,
		String portletId,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		int count = countByG_L_P(groupId, layoutId, portletId);

		if (count == 0) {
			return null;
		}

		List<JournalContentSearch> list = findByG_L_P(groupId, layoutId,
				portletId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the journal content searchs before and after the current journal content search in the ordered set where groupId = &#63; and layoutId = &#63; and portletId = &#63;.
	 *
	 * @param contentSearchId the primary key of the current journal content search
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next journal content search
	 * @throws NoSuchContentSearchException if a journal content search with the primary key could not be found
	 */
	@Override
	public JournalContentSearch[] findByG_L_P_PrevAndNext(
		long contentSearchId, long groupId, long layoutId, String portletId,
		OrderByComparator<JournalContentSearch> orderByComparator)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = findByPrimaryKey(contentSearchId);

		Session session = null;

		try {
			session = openSession();

			JournalContentSearch[] array = new JournalContentSearchImpl[3];

			array[0] = getByG_L_P_PrevAndNext(session, journalContentSearch,
					groupId, layoutId, portletId, orderByComparator, true);

			array[1] = journalContentSearch;

			array[2] = getByG_L_P_PrevAndNext(session, journalContentSearch,
					groupId, layoutId, portletId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected JournalContentSearch getByG_L_P_PrevAndNext(Session session,
		JournalContentSearch journalContentSearch, long groupId, long layoutId,
		String portletId,
		OrderByComparator<JournalContentSearch> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE);

		query.append(_FINDER_COLUMN_G_L_P_GROUPID_2);

		query.append(_FINDER_COLUMN_G_L_P_LAYOUTID_2);

		boolean bindPortletId = false;

		if (portletId == null) {
			query.append(_FINDER_COLUMN_G_L_P_PORTLETID_1);
		}
		else if (portletId.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_G_L_P_PORTLETID_3);
		}
		else {
			bindPortletId = true;

			query.append(_FINDER_COLUMN_G_L_P_PORTLETID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(JournalContentSearchModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		qPos.add(layoutId);

		if (bindPortletId) {
			qPos.add(portletId);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(journalContentSearch);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<JournalContentSearch> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the journal content searchs where groupId = &#63; and layoutId = &#63; and portletId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 */
	@Override
	public void removeByG_L_P(long groupId, long layoutId, String portletId) {
		for (JournalContentSearch journalContentSearch : findByG_L_P(groupId,
				layoutId, portletId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(journalContentSearch);
		}
	}

	/**
	 * Returns the number of journal content searchs where groupId = &#63; and layoutId = &#63; and portletId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @return the number of matching journal content searchs
	 */
	@Override
	public int countByG_L_P(long groupId, long layoutId, String portletId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_L_P;

		Object[] finderArgs = new Object[] { groupId, layoutId, portletId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_JOURNALCONTENTSEARCH_WHERE);

			query.append(_FINDER_COLUMN_G_L_P_GROUPID_2);

			query.append(_FINDER_COLUMN_G_L_P_LAYOUTID_2);

			boolean bindPortletId = false;

			if (portletId == null) {
				query.append(_FINDER_COLUMN_G_L_P_PORTLETID_1);
			}
			else if (portletId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_L_P_PORTLETID_3);
			}
			else {
				bindPortletId = true;

				query.append(_FINDER_COLUMN_G_L_P_PORTLETID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(layoutId);

				if (bindPortletId) {
					qPos.add(portletId);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_L_P_GROUPID_2 = "journalContentSearch.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_L_P_LAYOUTID_2 = "journalContentSearch.layoutId = ? AND ";
	private static final String _FINDER_COLUMN_G_L_P_PORTLETID_1 = "journalContentSearch.portletId IS NULL";
	private static final String _FINDER_COLUMN_G_L_P_PORTLETID_2 = "journalContentSearch.portletId = ?";
	private static final String _FINDER_COLUMN_G_L_P_PORTLETID_3 = "(journalContentSearch.portletId IS NULL OR journalContentSearch.portletId = '')";
	public static final FinderPath FINDER_PATH_FETCH_BY_G_L_P_A = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED,
			JournalContentSearchImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByG_L_P_A",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName(), String.class.getName()
			},
			JournalContentSearchModelImpl.GROUPID_COLUMN_BITMASK |
			JournalContentSearchModelImpl.LAYOUTID_COLUMN_BITMASK |
			JournalContentSearchModelImpl.PORTLETID_COLUMN_BITMASK |
			JournalContentSearchModelImpl.ARTICLEID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_L_P_A = new FinderPath(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_L_P_A",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName(), String.class.getName()
			});

	/**
	 * Returns the journal content search where groupId = &#63; and layoutId = &#63; and portletId = &#63; and articleId = &#63; or throws a {@link NoSuchContentSearchException} if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @param articleId the article ID
	 * @return the matching journal content search
	 * @throws NoSuchContentSearchException if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch findByG_L_P_A(long groupId, long layoutId,
		String portletId, String articleId) throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByG_L_P_A(groupId,
				layoutId, portletId, articleId);

		if (journalContentSearch == null) {
			StringBundler msg = new StringBundler(10);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("groupId=");
			msg.append(groupId);

			msg.append(", layoutId=");
			msg.append(layoutId);

			msg.append(", portletId=");
			msg.append(portletId);

			msg.append(", articleId=");
			msg.append(articleId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchContentSearchException(msg.toString());
		}

		return journalContentSearch;
	}

	/**
	 * Returns the journal content search where groupId = &#63; and layoutId = &#63; and portletId = &#63; and articleId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @param articleId the article ID
	 * @return the matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByG_L_P_A(long groupId, long layoutId,
		String portletId, String articleId) {
		return fetchByG_L_P_A(groupId, layoutId, portletId, articleId, true);
	}

	/**
	 * Returns the journal content search where groupId = &#63; and layoutId = &#63; and portletId = &#63; and articleId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @param articleId the article ID
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching journal content search, or <code>null</code> if a matching journal content search could not be found
	 */
	@Override
	public JournalContentSearch fetchByG_L_P_A(long groupId, long layoutId,
		String portletId, String articleId, boolean retrieveFromCache) {
		Object[] finderArgs = new Object[] {
				groupId, layoutId, portletId, articleId
			};

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_G_L_P_A,
					finderArgs, this);
		}

		if (result instanceof JournalContentSearch) {
			JournalContentSearch journalContentSearch = (JournalContentSearch)result;

			if ((groupId != journalContentSearch.getGroupId()) ||
					(layoutId != journalContentSearch.getLayoutId()) ||
					!Validator.equals(portletId,
						journalContentSearch.getPortletId()) ||
					!Validator.equals(articleId,
						journalContentSearch.getArticleId())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(6);

			query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE);

			query.append(_FINDER_COLUMN_G_L_P_A_GROUPID_2);

			query.append(_FINDER_COLUMN_G_L_P_A_LAYOUTID_2);

			boolean bindPortletId = false;

			if (portletId == null) {
				query.append(_FINDER_COLUMN_G_L_P_A_PORTLETID_1);
			}
			else if (portletId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_L_P_A_PORTLETID_3);
			}
			else {
				bindPortletId = true;

				query.append(_FINDER_COLUMN_G_L_P_A_PORTLETID_2);
			}

			boolean bindArticleId = false;

			if (articleId == null) {
				query.append(_FINDER_COLUMN_G_L_P_A_ARTICLEID_1);
			}
			else if (articleId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_L_P_A_ARTICLEID_3);
			}
			else {
				bindArticleId = true;

				query.append(_FINDER_COLUMN_G_L_P_A_ARTICLEID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(layoutId);

				if (bindPortletId) {
					qPos.add(portletId);
				}

				if (bindArticleId) {
					qPos.add(articleId);
				}

				List<JournalContentSearch> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_L_P_A,
						finderArgs, list);
				}
				else {
					JournalContentSearch journalContentSearch = list.get(0);

					result = journalContentSearch;

					cacheResult(journalContentSearch);

					if ((journalContentSearch.getGroupId() != groupId) ||
							(journalContentSearch.getLayoutId() != layoutId) ||
							(journalContentSearch.getPortletId() == null) ||
							!journalContentSearch.getPortletId()
													 .equals(portletId) ||
							(journalContentSearch.getArticleId() == null) ||
							!journalContentSearch.getArticleId()
													 .equals(articleId)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_L_P_A,
							finderArgs, journalContentSearch);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_L_P_A,
					finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (JournalContentSearch)result;
		}
	}

	/**
	 * Removes the journal content search where groupId = &#63; and layoutId = &#63; and portletId = &#63; and articleId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @param articleId the article ID
	 * @return the journal content search that was removed
	 */
	@Override
	public JournalContentSearch removeByG_L_P_A(long groupId, long layoutId,
		String portletId, String articleId) throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = findByG_L_P_A(groupId,
				layoutId, portletId, articleId);

		return remove(journalContentSearch);
	}

	/**
	 * Returns the number of journal content searchs where groupId = &#63; and layoutId = &#63; and portletId = &#63; and articleId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param layoutId the layout ID
	 * @param portletId the portlet ID
	 * @param articleId the article ID
	 * @return the number of matching journal content searchs
	 */
	@Override
	public int countByG_L_P_A(long groupId, long layoutId, String portletId,
		String articleId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_L_P_A;

		Object[] finderArgs = new Object[] {
				groupId, layoutId, portletId, articleId
			};

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(5);

			query.append(_SQL_COUNT_JOURNALCONTENTSEARCH_WHERE);

			query.append(_FINDER_COLUMN_G_L_P_A_GROUPID_2);

			query.append(_FINDER_COLUMN_G_L_P_A_LAYOUTID_2);

			boolean bindPortletId = false;

			if (portletId == null) {
				query.append(_FINDER_COLUMN_G_L_P_A_PORTLETID_1);
			}
			else if (portletId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_L_P_A_PORTLETID_3);
			}
			else {
				bindPortletId = true;

				query.append(_FINDER_COLUMN_G_L_P_A_PORTLETID_2);
			}

			boolean bindArticleId = false;

			if (articleId == null) {
				query.append(_FINDER_COLUMN_G_L_P_A_ARTICLEID_1);
			}
			else if (articleId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_L_P_A_ARTICLEID_3);
			}
			else {
				bindArticleId = true;

				query.append(_FINDER_COLUMN_G_L_P_A_ARTICLEID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(layoutId);

				if (bindPortletId) {
					qPos.add(portletId);
				}

				if (bindArticleId) {
					qPos.add(articleId);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_L_P_A_GROUPID_2 = "journalContentSearch.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_L_P_A_LAYOUTID_2 = "journalContentSearch.layoutId = ? AND ";
	private static final String _FINDER_COLUMN_G_L_P_A_PORTLETID_1 = "journalContentSearch.portletId IS NULL AND ";
	private static final String _FINDER_COLUMN_G_L_P_A_PORTLETID_2 = "journalContentSearch.portletId = ? AND ";
	private static final String _FINDER_COLUMN_G_L_P_A_PORTLETID_3 = "(journalContentSearch.portletId IS NULL OR journalContentSearch.portletId = '') AND ";
	private static final String _FINDER_COLUMN_G_L_P_A_ARTICLEID_1 = "journalContentSearch.articleId IS NULL";
	private static final String _FINDER_COLUMN_G_L_P_A_ARTICLEID_2 = "journalContentSearch.articleId = ?";
	private static final String _FINDER_COLUMN_G_L_P_A_ARTICLEID_3 = "(journalContentSearch.articleId IS NULL OR journalContentSearch.articleId = '')";

	public JournalContentSearchPersistenceImpl() {
		setModelClass(JournalContentSearch.class);
	}

	/**
	 * Caches the journal content search in the entity cache if it is enabled.
	 *
	 * @param journalContentSearch the journal content search
	 */
	@Override
	public void cacheResult(JournalContentSearch journalContentSearch) {
		EntityCacheUtil.putResult(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			journalContentSearch.getPrimaryKey(), journalContentSearch);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_L_P_A,
			new Object[] {
				journalContentSearch.getGroupId(),
				journalContentSearch.getLayoutId(),
				journalContentSearch.getPortletId(),
				journalContentSearch.getArticleId()
			}, journalContentSearch);

		journalContentSearch.resetOriginalValues();
	}

	/**
	 * Caches the journal content searchs in the entity cache if it is enabled.
	 *
	 * @param journalContentSearchs the journal content searchs
	 */
	@Override
	public void cacheResult(List<JournalContentSearch> journalContentSearchs) {
		for (JournalContentSearch journalContentSearch : journalContentSearchs) {
			if (EntityCacheUtil.getResult(
						JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
						JournalContentSearchImpl.class,
						journalContentSearch.getPrimaryKey()) == null) {
				cacheResult(journalContentSearch);
			}
			else {
				journalContentSearch.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all journal content searchs.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(JournalContentSearchImpl.class.getName());
		}

		EntityCacheUtil.clearCache(JournalContentSearchImpl.class);

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the journal content search.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(JournalContentSearch journalContentSearch) {
		EntityCacheUtil.removeResult(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchImpl.class, journalContentSearch.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(journalContentSearch);
	}

	@Override
	public void clearCache(List<JournalContentSearch> journalContentSearchs) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (JournalContentSearch journalContentSearch : journalContentSearchs) {
			EntityCacheUtil.removeResult(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
				JournalContentSearchImpl.class,
				journalContentSearch.getPrimaryKey());

			clearUniqueFindersCache(journalContentSearch);
		}
	}

	protected void cacheUniqueFindersCache(
		JournalContentSearch journalContentSearch) {
		if (journalContentSearch.isNew()) {
			Object[] args = new Object[] {
					journalContentSearch.getGroupId(),
					journalContentSearch.getLayoutId(),
					journalContentSearch.getPortletId(),
					journalContentSearch.getArticleId()
				};

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_G_L_P_A, args,
				Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_L_P_A, args,
				journalContentSearch);
		}
		else {
			JournalContentSearchModelImpl journalContentSearchModelImpl = (JournalContentSearchModelImpl)journalContentSearch;

			if ((journalContentSearchModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_G_L_P_A.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						journalContentSearch.getGroupId(),
						journalContentSearch.getLayoutId(),
						journalContentSearch.getPortletId(),
						journalContentSearch.getArticleId()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_G_L_P_A, args,
					Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_L_P_A, args,
					journalContentSearch);
			}
		}
	}

	protected void clearUniqueFindersCache(
		JournalContentSearch journalContentSearch) {
		JournalContentSearchModelImpl journalContentSearchModelImpl = (JournalContentSearchModelImpl)journalContentSearch;

		Object[] args = new Object[] {
				journalContentSearch.getGroupId(),
				journalContentSearch.getLayoutId(),
				journalContentSearch.getPortletId(),
				journalContentSearch.getArticleId()
			};

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_L_P_A, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_L_P_A, args);

		if ((journalContentSearchModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_G_L_P_A.getColumnBitmask()) != 0) {
			args = new Object[] {
					journalContentSearchModelImpl.getOriginalGroupId(),
					journalContentSearchModelImpl.getOriginalLayoutId(),
					journalContentSearchModelImpl.getOriginalPortletId(),
					journalContentSearchModelImpl.getOriginalArticleId()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_L_P_A, args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_L_P_A, args);
		}
	}

	/**
	 * Creates a new journal content search with the primary key. Does not add the journal content search to the database.
	 *
	 * @param contentSearchId the primary key for the new journal content search
	 * @return the new journal content search
	 */
	@Override
	public JournalContentSearch create(long contentSearchId) {
		JournalContentSearch journalContentSearch = new JournalContentSearchImpl();

		journalContentSearch.setNew(true);
		journalContentSearch.setPrimaryKey(contentSearchId);

		return journalContentSearch;
	}

	/**
	 * Removes the journal content search with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param contentSearchId the primary key of the journal content search
	 * @return the journal content search that was removed
	 * @throws NoSuchContentSearchException if a journal content search with the primary key could not be found
	 */
	@Override
	public JournalContentSearch remove(long contentSearchId)
		throws NoSuchContentSearchException {
		return remove((Serializable)contentSearchId);
	}

	/**
	 * Removes the journal content search with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the journal content search
	 * @return the journal content search that was removed
	 * @throws NoSuchContentSearchException if a journal content search with the primary key could not be found
	 */
	@Override
	public JournalContentSearch remove(Serializable primaryKey)
		throws NoSuchContentSearchException {
		Session session = null;

		try {
			session = openSession();

			JournalContentSearch journalContentSearch = (JournalContentSearch)session.get(JournalContentSearchImpl.class,
					primaryKey);

			if (journalContentSearch == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchContentSearchException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(journalContentSearch);
		}
		catch (NoSuchContentSearchException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected JournalContentSearch removeImpl(
		JournalContentSearch journalContentSearch) {
		journalContentSearch = toUnwrappedModel(journalContentSearch);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(journalContentSearch)) {
				journalContentSearch = (JournalContentSearch)session.get(JournalContentSearchImpl.class,
						journalContentSearch.getPrimaryKeyObj());
			}

			if (journalContentSearch != null) {
				session.delete(journalContentSearch);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (journalContentSearch != null) {
			clearCache(journalContentSearch);
		}

		return journalContentSearch;
	}

	@Override
	public JournalContentSearch updateImpl(
		JournalContentSearch journalContentSearch) {
		journalContentSearch = toUnwrappedModel(journalContentSearch);

		boolean isNew = journalContentSearch.isNew();

		JournalContentSearchModelImpl journalContentSearchModelImpl = (JournalContentSearchModelImpl)journalContentSearch;

		Session session = null;

		try {
			session = openSession();

			if (journalContentSearch.isNew()) {
				session.save(journalContentSearch);

				journalContentSearch.setNew(false);
			}
			else {
				session.merge(journalContentSearch);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !JournalContentSearchModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((journalContentSearchModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						journalContentSearchModelImpl.getOriginalGroupId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
					args);

				args = new Object[] { journalContentSearchModelImpl.getGroupId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
					args);
			}

			if ((journalContentSearchModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_PORTLETID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						journalContentSearchModelImpl.getOriginalPortletId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_PORTLETID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_PORTLETID,
					args);

				args = new Object[] { journalContentSearchModelImpl.getPortletId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_PORTLETID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_PORTLETID,
					args);
			}

			if ((journalContentSearchModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ARTICLEID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						journalContentSearchModelImpl.getOriginalArticleId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ARTICLEID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ARTICLEID,
					args);

				args = new Object[] { journalContentSearchModelImpl.getArticleId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ARTICLEID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ARTICLEID,
					args);
			}

			if ((journalContentSearchModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_L.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						journalContentSearchModelImpl.getOriginalGroupId(),
						journalContentSearchModelImpl.getOriginalLayoutId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_L, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_L,
					args);

				args = new Object[] {
						journalContentSearchModelImpl.getGroupId(),
						journalContentSearchModelImpl.getLayoutId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_L, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_L,
					args);
			}

			if ((journalContentSearchModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_A.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						journalContentSearchModelImpl.getOriginalGroupId(),
						journalContentSearchModelImpl.getOriginalArticleId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_A, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_A,
					args);

				args = new Object[] {
						journalContentSearchModelImpl.getGroupId(),
						journalContentSearchModelImpl.getArticleId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_A, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_A,
					args);
			}

			if ((journalContentSearchModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_L_P.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						journalContentSearchModelImpl.getOriginalGroupId(),
						journalContentSearchModelImpl.getOriginalLayoutId(),
						journalContentSearchModelImpl.getOriginalPortletId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_L_P, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_L_P,
					args);

				args = new Object[] {
						journalContentSearchModelImpl.getGroupId(),
						journalContentSearchModelImpl.getLayoutId(),
						journalContentSearchModelImpl.getPortletId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_L_P, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_L_P,
					args);
			}
		}

		EntityCacheUtil.putResult(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
			JournalContentSearchImpl.class,
			journalContentSearch.getPrimaryKey(), journalContentSearch, false);

		clearUniqueFindersCache(journalContentSearch);
		cacheUniqueFindersCache(journalContentSearch);

		journalContentSearch.resetOriginalValues();

		return journalContentSearch;
	}

	protected JournalContentSearch toUnwrappedModel(
		JournalContentSearch journalContentSearch) {
		if (journalContentSearch instanceof JournalContentSearchImpl) {
			return journalContentSearch;
		}

		JournalContentSearchImpl journalContentSearchImpl = new JournalContentSearchImpl();

		journalContentSearchImpl.setNew(journalContentSearch.isNew());
		journalContentSearchImpl.setPrimaryKey(journalContentSearch.getPrimaryKey());

		journalContentSearchImpl.setContentSearchId(journalContentSearch.getContentSearchId());
		journalContentSearchImpl.setGroupId(journalContentSearch.getGroupId());
		journalContentSearchImpl.setCompanyId(journalContentSearch.getCompanyId());
		journalContentSearchImpl.setLayoutId(journalContentSearch.getLayoutId());
		journalContentSearchImpl.setPortletId(journalContentSearch.getPortletId());
		journalContentSearchImpl.setArticleId(journalContentSearch.getArticleId());

		return journalContentSearchImpl;
	}

	/**
	 * Returns the journal content search with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the journal content search
	 * @return the journal content search
	 * @throws NoSuchContentSearchException if a journal content search with the primary key could not be found
	 */
	@Override
	public JournalContentSearch findByPrimaryKey(Serializable primaryKey)
		throws NoSuchContentSearchException {
		JournalContentSearch journalContentSearch = fetchByPrimaryKey(primaryKey);

		if (journalContentSearch == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchContentSearchException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return journalContentSearch;
	}

	/**
	 * Returns the journal content search with the primary key or throws a {@link NoSuchContentSearchException} if it could not be found.
	 *
	 * @param contentSearchId the primary key of the journal content search
	 * @return the journal content search
	 * @throws NoSuchContentSearchException if a journal content search with the primary key could not be found
	 */
	@Override
	public JournalContentSearch findByPrimaryKey(long contentSearchId)
		throws NoSuchContentSearchException {
		return findByPrimaryKey((Serializable)contentSearchId);
	}

	/**
	 * Returns the journal content search with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the journal content search
	 * @return the journal content search, or <code>null</code> if a journal content search with the primary key could not be found
	 */
	@Override
	public JournalContentSearch fetchByPrimaryKey(Serializable primaryKey) {
		JournalContentSearch journalContentSearch = (JournalContentSearch)EntityCacheUtil.getResult(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
				JournalContentSearchImpl.class, primaryKey);

		if (journalContentSearch == _nullJournalContentSearch) {
			return null;
		}

		if (journalContentSearch == null) {
			Session session = null;

			try {
				session = openSession();

				journalContentSearch = (JournalContentSearch)session.get(JournalContentSearchImpl.class,
						primaryKey);

				if (journalContentSearch != null) {
					cacheResult(journalContentSearch);
				}
				else {
					EntityCacheUtil.putResult(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
						JournalContentSearchImpl.class, primaryKey,
						_nullJournalContentSearch);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
					JournalContentSearchImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return journalContentSearch;
	}

	/**
	 * Returns the journal content search with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param contentSearchId the primary key of the journal content search
	 * @return the journal content search, or <code>null</code> if a journal content search with the primary key could not be found
	 */
	@Override
	public JournalContentSearch fetchByPrimaryKey(long contentSearchId) {
		return fetchByPrimaryKey((Serializable)contentSearchId);
	}

	@Override
	public Map<Serializable, JournalContentSearch> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, JournalContentSearch> map = new HashMap<Serializable, JournalContentSearch>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			JournalContentSearch journalContentSearch = fetchByPrimaryKey(primaryKey);

			if (journalContentSearch != null) {
				map.put(primaryKey, journalContentSearch);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			JournalContentSearch journalContentSearch = (JournalContentSearch)EntityCacheUtil.getResult(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
					JournalContentSearchImpl.class, primaryKey);

			if (journalContentSearch == null) {
				if (uncachedPrimaryKeys == null) {
					uncachedPrimaryKeys = new HashSet<Serializable>();
				}

				uncachedPrimaryKeys.add(primaryKey);
			}
			else {
				map.put(primaryKey, journalContentSearch);
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_JOURNALCONTENTSEARCH_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			query.append(String.valueOf(primaryKey));

			query.append(StringPool.COMMA);
		}

		query.setIndex(query.index() - 1);

		query.append(StringPool.CLOSE_PARENTHESIS);

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			for (JournalContentSearch journalContentSearch : (List<JournalContentSearch>)q.list()) {
				map.put(journalContentSearch.getPrimaryKeyObj(),
					journalContentSearch);

				cacheResult(journalContentSearch);

				uncachedPrimaryKeys.remove(journalContentSearch.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				EntityCacheUtil.putResult(JournalContentSearchModelImpl.ENTITY_CACHE_ENABLED,
					JournalContentSearchImpl.class, primaryKey,
					_nullJournalContentSearch);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the journal content searchs.
	 *
	 * @return the journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the journal content searchs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @return the range of journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the journal content searchs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link JournalContentSearchModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of journal content searchs
	 * @param end the upper bound of the range of journal content searchs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of journal content searchs
	 */
	@Override
	public List<JournalContentSearch> findAll(int start, int end,
		OrderByComparator<JournalContentSearch> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<JournalContentSearch> list = (List<JournalContentSearch>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_JOURNALCONTENTSEARCH);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_JOURNALCONTENTSEARCH;

				if (pagination) {
					sql = sql.concat(JournalContentSearchModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<JournalContentSearch>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the journal content searchs from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (JournalContentSearch journalContentSearch : findAll()) {
			remove(journalContentSearch);
		}
	}

	/**
	 * Returns the number of journal content searchs.
	 *
	 * @return the number of journal content searchs
	 */
	@Override
	public int countAll() {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_JOURNALCONTENTSEARCH);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Initializes the journal content search persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		EntityCacheUtil.removeCache(JournalContentSearchImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_JOURNALCONTENTSEARCH = "SELECT journalContentSearch FROM JournalContentSearch journalContentSearch";
	private static final String _SQL_SELECT_JOURNALCONTENTSEARCH_WHERE_PKS_IN = "SELECT journalContentSearch FROM JournalContentSearch journalContentSearch WHERE contentSearchId IN (";
	private static final String _SQL_SELECT_JOURNALCONTENTSEARCH_WHERE = "SELECT journalContentSearch FROM JournalContentSearch journalContentSearch WHERE ";
	private static final String _SQL_COUNT_JOURNALCONTENTSEARCH = "SELECT COUNT(journalContentSearch) FROM JournalContentSearch journalContentSearch";
	private static final String _SQL_COUNT_JOURNALCONTENTSEARCH_WHERE = "SELECT COUNT(journalContentSearch) FROM JournalContentSearch journalContentSearch WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "journalContentSearch.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No JournalContentSearch exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No JournalContentSearch exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = com.liferay.portal.util.PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE;
	private static final Log _log = LogFactoryUtil.getLog(JournalContentSearchPersistenceImpl.class);
	private static final JournalContentSearch _nullJournalContentSearch = new JournalContentSearchImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<JournalContentSearch> toCacheModel() {
				return _nullJournalContentSearchCacheModel;
			}
		};

	private static final CacheModel<JournalContentSearch> _nullJournalContentSearchCacheModel =
		new CacheModel<JournalContentSearch>() {
			@Override
			public JournalContentSearch toEntityModel() {
				return _nullJournalContentSearch;
			}
		};
}