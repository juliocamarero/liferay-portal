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

package com.liferay.portal.upgrade.v6_2_0.util;

import java.sql.Types;

/**
 * @author	  Daniel Kocsis
 * @generated
 */
public class PollsVoteTable {

	public static final String TABLE_NAME = "PollsVote";

	public static final Object[][] TABLE_COLUMNS = {
		{"uuid_", Types.VARCHAR},
		{"voteId", Types.BIGINT},
		{"groupId", Types.BIGINT},
		{"companyId", Types.BIGINT},
		{"userId", Types.BIGINT},
		{"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP},
		{"modifiedDate", Types.TIMESTAMP},
		{"questionId", Types.BIGINT},
		{"choiceId", Types.BIGINT},
		{"voteDate", Types.TIMESTAMP}
	};

	public static final String TABLE_SQL_CREATE = "create table PollsVote (uuid_ VARCHAR(75) null,voteId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,questionId LONG,choiceId LONG,voteDate DATE null)";

	public static final String TABLE_SQL_DROP = "drop table PollsVote";

	public static final String[] TABLE_SQL_ADD_INDEXES = {
		"create index IX_D5DF7B54 on PollsVote (choiceId)",
		"create index IX_12112599 on PollsVote (questionId)",
		"create unique index IX_1BBFD4D3 on PollsVote (questionId, userId)",
		"create index IX_FD32EB70 on PollsVote (uuid_)",
		"create index IX_7D8E92B8 on PollsVote (uuid_, companyId)",
		"create unique index IX_A88C673A on PollsVote (uuid_, groupId)"
	};

}