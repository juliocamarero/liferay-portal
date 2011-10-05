/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.database;

import com.liferay.portal.kernel.dao.jdbc.DataSourceFactoryUtil;
import com.liferay.portal.util.BaseTestCase;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * @author Miguel Pastor
 */
public class TestDatabaseConnectivity extends BaseTestCase {

	public void testOKMysqlConnection() {
		assertTrue(
			checkConnection(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost/lportal?useUnicode=true", "liferay",
				"liferay"));
	}

	public void testBadMySQLDriver() {
		assertFalse(
			checkConnection(
				"com.mysql.jdbc.Dri",
				"jdbc:mysql://localhost/lportal?useUnicode=true", "liferay",
				"liferay"));
	}

	public void testBadMySQLURL() {
		assertFalse(
			checkConnection(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://nonexistig/lportal?useUnicode=true", "liferay",
				"liferay"));
	}

	public void testBadMySQLUsername() {
		assertFalse(
			checkConnection(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost/lportal?useUnicode=true", "foo",
				"liferay"));
	}

	public void testBadMySQLPassword() {
		assertFalse(
			checkConnection(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost/lportal?useUnicode=true", "liferay",
				"foo"));
	}

	protected DataSource getDatasource(
			String driverClassName, String url, String userName,
			String password)
		throws Exception {

		return DataSourceFactoryUtil.initDataSource(
			driverClassName, url, userName, password);
	}

	protected boolean checkConnection(
		String driverClassName, String url, String userName, String password) {

		try {
			Class.forName(driverClassName);
		}
		catch (ClassNotFoundException e) {
			return false;
		}

		DataSource dataSource = null;
		Connection connection = null;

		try {
			dataSource = getDatasource(
				driverClassName, url, userName, password);

			connection = dataSource.getConnection();
		}
		catch (SQLException e) {
			return false;
		}
		catch (Exception e) {
			return false;
		}

		return true;
	}

}