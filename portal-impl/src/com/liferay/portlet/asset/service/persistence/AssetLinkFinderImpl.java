/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.asset.service.persistence;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portlet.asset.NoSuchLinkException;
import com.liferay.portlet.asset.model.AssetLink;
import com.liferay.portlet.asset.model.impl.AssetLinkImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

import java.util.List;

/**
 * @author Julio Camarero
 */
public class AssetLinkFinderImpl
	extends BasePersistenceImpl<AssetLink> implements AssetLinkFinder {

	public static final String FIND_BY_E1_V =
		AssetLinkFinder.class.getName() + ".findByE1_V";

	public static final String FIND_BY_E1_T_V =
		AssetLinkFinder.class.getName() + ".findByE1_T_V";

	public static final String FIND_BY_G_E1UUID_E2UUID_T =
			AssetLinkFinder.class.getName() + ".findByG_E1UUID_E2UUID_T";

	public List<AssetLink> findByE1_V(long entryId1, boolean visible)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_BY_E1_V);

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity("AssetLink", AssetLinkImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(entryId1);
			qPos.add(visible);

			return (List<AssetLink>)QueryUtil.list(
				q, getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List<AssetLink> findByE1_T_V(
			long entryId1, int type, boolean visible)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_BY_E1_T_V);

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity("AssetLink", AssetLinkImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(entryId1);
			qPos.add(type);
			qPos.add(visible);

			return (List<AssetLink>)QueryUtil.list(
				q, getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public AssetLink findByG_E1_E2_T(
			long groupId, String entry1Uuid, String entry2Uuid, int type)
		throws NoSuchLinkException, SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_BY_G_E1UUID_E2UUID_T);

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity("AssetLink", AssetLinkImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(entry1Uuid);
			qPos.add(entry2Uuid);
			qPos.add(groupId);

			qPos.add(type);

			List<AssetLink> links = (List<AssetLink>)QueryUtil.list(
					q, getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);

			if (!links.isEmpty()) {
				return links.get(0);
			}
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}

		StringBundler sb = new StringBundler(6);

		sb.append("No AssetLink exists with the key: ");
		sb.append("{groupId=");
		sb.append(groupId);
		sb.append(", entry1Uuid=");
		sb.append(entry1Uuid);
		sb.append(", entry2Uuid=");
		sb.append(entry2Uuid);
		sb.append(", type=");
		sb.append(type);
		sb.append("}");

		throw new NoSuchLinkException(sb.toString());
	}

}