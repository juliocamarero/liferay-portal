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

package com.liferay.portal.upgrade.v7_0_0;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.upgrade.v7_0_0.util.GroupTable;
import com.liferay.portal.util.PropsValues;

	import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Sampsa Sohlman
 */
public class UpgradeGroup extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {

		try {
			runSQL("alter table Group_ add mvccVersion LONG default 0");
			runSQL("alter table Group_ add trashEntriesMaxAge INTEGER");
		}
		catch (SQLException sqle) {
			upgradeTable(
				GroupTable.TABLE_NAME, GroupTable.TABLE_COLUMNS,
				GroupTable.TABLE_SQL_CREATE, GroupTable.TABLE_SQL_ADD_INDEXES);
		}

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"SELECT companyId, groupId, typeSettings FROM Group_");

			rs = ps.executeQuery();

			while (rs.next()) {
				long companyId = rs.getLong("companyId");
				long groupId = rs.getLong("groupId");
				String typeSettings = rs.getString("typeSettings");

				upgradeGroup(companyId, groupId, typeSettings);
			}
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	protected int getTrashEntriesMaxAge(
			long companyId, long groupId, String typeSettings) {

		UnicodeProperties typeSettingsProperties = new UnicodeProperties(true);

		try {
			typeSettingsProperties.load(typeSettings);
		}
		catch (IOException ioe) {
			_log.error("With groupId : " + groupId, ioe);
			return -1;
		}

		String trashEntriesMaxAgeString = typeSettingsProperties.get(
			"trashEntriesMaxAge");

		boolean isTrashEnabled = PropsValues.TRASH_ENABLED;

		String trashEntriesMaxAgeDefault = String.valueOf(
			PropsValues.TRASH_ENTRIES_MAX_AGE);

		if (isTrashEnabled &&
			(trashEntriesMaxAgeString != null) &&
			!trashEntriesMaxAgeString.equals(trashEntriesMaxAgeDefault)) {

			return Integer.valueOf(trashEntriesMaxAgeString);
		}
		else {
			return -1;
		}
	}

	protected void upgradeGroup(
			long companyId, long groupId, String typeSettings)
		throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"UPDATE Group_" +
				" SET trashEntriesMaxAge = ? WHERE groupId = ?");

			int trashEntriesMaxAge = getTrashEntriesMaxAge(
				companyId, groupId, typeSettings);

			ps.setInt(1, trashEntriesMaxAge);
			ps.setLong(2, groupId);

			ps.executeUpdate();
			con.commit();
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(UpgradeGroup.class);

}