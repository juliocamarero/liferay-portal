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
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.upgrade.v7_0_0.util.TrashEntryTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Sampsa Sohlman
 */
public class UpgradeTrashEntry extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {

		try {
			runSQL("alter table TrashEntry add createTimestamp INTEGER");
		}
		catch (SQLException sqle) {
			upgradeTable(
				TrashEntryTable.TABLE_NAME, TrashEntryTable.TABLE_COLUMNS,
				TrashEntryTable.TABLE_SQL_CREATE,
				TrashEntryTable.TABLE_SQL_ADD_INDEXES);
		}

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"SELECT entryId, createDate FROM TrashEntry");

			rs = ps.executeQuery();

			while (rs.next()) {
				long entryId = rs.getLong("entryId");
				Timestamp createDate = rs.getTimestamp("createDate");

				upgradeTrashEntry(entryId, createDate);
			}
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	protected void upgradeTrashEntry(long entryId, Timestamp createDate)
		throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"update TrashEntry" +
				" set createTimestamp = ? where entryId = ?");

			int timestampInMinutes = (int)(createDate.getTime() / 60000);

			ps.setInt(1, timestampInMinutes);
			ps.setLong(2, entryId);

			ps.executeUpdate();
			con.commit();
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

}