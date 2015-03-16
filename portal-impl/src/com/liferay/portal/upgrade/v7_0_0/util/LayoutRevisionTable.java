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
public class LayoutRevisionTable {

	public static final String TABLE_NAME = "LayoutRevision";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT},
		{"layoutRevisionId", Types.BIGINT},
		{"groupId", Types.BIGINT},
		{"companyId", Types.BIGINT},
		{"userId", Types.BIGINT},
		{"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP},
		{"modifiedDate", Types.TIMESTAMP},
		{"layoutSetBranchId", Types.BIGINT},
		{"layoutBranchId", Types.BIGINT},
		{"parentLayoutRevisionId", Types.BIGINT},
		{"head", Types.BOOLEAN},
		{"major", Types.BOOLEAN},
		{"plid", Types.BIGINT},
		{"privateLayout", Types.BOOLEAN},
		{"name", Types.VARCHAR},
		{"title", Types.VARCHAR},
		{"description", Types.CLOB},
		{"keywords", Types.CLOB},
		{"robots", Types.CLOB},
		{"typeSettings", Types.CLOB},
		{"iconImageId", Types.BIGINT},
		{"themeId", Types.VARCHAR},
		{"colorSchemeId", Types.VARCHAR},
		{"wapThemeId", Types.VARCHAR},
		{"wapColorSchemeId", Types.VARCHAR},
		{"css", Types.CLOB},
		{"status", Types.INTEGER},
		{"statusByUserId", Types.BIGINT},
		{"statusByUserName", Types.VARCHAR},
		{"statusDate", Types.TIMESTAMP}
	};

	public static final String TABLE_SQL_CREATE = "create table LayoutRevision (mvccVersion LONG default 0,layoutRevisionId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,layoutSetBranchId LONG,layoutBranchId LONG,parentLayoutRevisionId LONG,head BOOLEAN,major BOOLEAN,plid LONG,privateLayout BOOLEAN,name STRING null,title STRING null,description TEXT null,keywords TEXT null,robots TEXT null,typeSettings TEXT null,iconImageId LONG,themeId VARCHAR(75) null,colorSchemeId VARCHAR(75) null,wapThemeId VARCHAR(75) null,wapColorSchemeId VARCHAR(75) null,css TEXT null,status INTEGER,statusByUserId LONG,statusByUserName VARCHAR(75) null,statusDate DATE null)";

	public static final String TABLE_SQL_DROP = "drop table LayoutRevision";

	public static final String[] TABLE_SQL_ADD_INDEXES = {
		"create index IX_43E8286A on LayoutRevision (head, plid)",
		"create index IX_E10AC39 on LayoutRevision (layoutSetBranchId, head, plid)",
		"create index IX_13984800 on LayoutRevision (layoutSetBranchId, layoutBranchId, plid)",
		"create index IX_4A84AF43 on LayoutRevision (layoutSetBranchId, parentLayoutRevisionId, plid)",
		"create index IX_70DA9ECB on LayoutRevision (layoutSetBranchId, plid, status)",
		"create index IX_7FFAE700 on LayoutRevision (layoutSetBranchId, status)",
		"create index IX_8EC3D2BC on LayoutRevision (plid, status)"
	};

}