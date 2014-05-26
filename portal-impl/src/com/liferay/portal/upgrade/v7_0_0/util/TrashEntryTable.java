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

package com.liferay.portal.upgrade.v7_0_0.util;

import java.sql.Types;

/**
 * @author	  Brian Wing Shun Chan
 * @generated
 */
public class TrashEntryTable {

	public static final String TABLE_NAME = "TrashEntry";

	public static final Object[][] TABLE_COLUMNS = {
		{"entryId", Types.BIGINT},
		{"groupId", Types.BIGINT},
		{"companyId", Types.BIGINT},
		{"userId", Types.BIGINT},
		{"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP},
		{"classNameId", Types.BIGINT},
		{"classPK", Types.BIGINT},
		{"systemEventSetKey", Types.BIGINT},
		{"typeSettings", Types.CLOB},
		{"status", Types.INTEGER},
		{"createTimestamp", Types.INTEGER}
	};

	public static final String TABLE_SQL_CREATE = "create table TrashEntry (entryId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,classNameId LONG,classPK LONG,systemEventSetKey LONG,typeSettings TEXT null,status INTEGER,createTimestamp INTEGER)";

	public static final String TABLE_SQL_DROP = "drop table TrashEntry";

	public static final String[] TABLE_SQL_ADD_INDEXES = {
		"create unique index IX_B35F73D5 on TrashEntry (classNameId, classPK)",
		"create index IX_2674F2A8 on TrashEntry (companyId)",
		"create index IX_FC4EEA64 on TrashEntry (groupId, classNameId)",
		"create index IX_6CAAE2E8 on TrashEntry (groupId, createDate)"
	};

}