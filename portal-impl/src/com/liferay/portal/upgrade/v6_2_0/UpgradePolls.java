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

package com.liferay.portal.upgrade.v6_2_0;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.upgrade.v6_2_0.util.PollsVoteTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Daniel Kocsis
 */
public class UpgradePolls extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {

		// PollsVote

		try {
			runSQL("alter table PollsVote add groupId LONG");
		}
		catch (SQLException sqle) {
			upgradeTable(
				PollsVoteTable.TABLE_NAME, PollsVoteTable.TABLE_COLUMNS,
				PollsVoteTable.TABLE_SQL_CREATE,
				PollsVoteTable.TABLE_SQL_ADD_INDEXES);
		}

		updatePollsVotes();
	}

	protected long getPollsQuestionsGroupId(long questionId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"select groupId from PollsQuestion where questionId = ?");

			ps.setLong(1, questionId);

			rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getLong("groupId");
			}

			return 0;
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	protected void updatePollsVote(long voteId, long groupId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"update PollsVote set groupId = ? where voteId = ?");

			ps.setLong(1, groupId);
			ps.setLong(2, voteId);

			ps.executeUpdate();
		}
		finally {
			DataAccess.cleanUp(con, ps);
		}
	}

	protected void updatePollsVotes() throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"select voteId, questionId from PollsVote");

			rs = ps.executeQuery();

			while (rs.next()) {
				long voteId = rs.getLong("voteId");
				long questionId = rs.getLong("questionId");

				long groupId = getPollsQuestionsGroupId(questionId);

				updatePollsVote(voteId, groupId);
			}
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

}