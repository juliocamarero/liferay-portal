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

package com.liferay.portal.setup;

import com.liferay.portal.util.PropsValues;

public class SetupDatabaseInputs {
	
	public SetupDatabaseInputs(String database, String driver, String password, 
		String username) {
		
		_database = database;
		_driver = driver;
		_password = password;
		_username = username;
	}
	
	public String get_database() {
		return _database;
	}
	
	public void set_database(String database) {
		_database = database;
	}
	
	public String get_driver() {
		return _driver;
	}
	
	public void set_driver(String driver) {
		_driver = driver;
	}
	
	public String get_password() {
		return _password;
	}
	
	public void set_password(String password) {
		_password = password;
	}
	
	public String get_username() {
		return _username;
	}
	
	public void set_username(String username) {
		_username = username;
	}
	
	public String toProperties()
	{
		String basicPortalExt = "";
		
		//database properties
		
		String driverClassName = "jdbc.default.driverClassName=";
		String url = "jdbc.default.url=";
		
		if ("DB2".equalsIgnoreCase(this.get_driver())) {
			driverClassName += "com.ibm.db2.jcc.DB2Driver";
			url += "jdbc:db2://localhost:50000/" + 
				this.get_database() + 
				":deferPrepares=false;fullyMaterializeInputStreams=true;" +
				"fullyMaterializeLobData=true;progresssiveLocators=2;" +
				"progressiveStreaming=2;";
		}
		else if ("Derby".equalsIgnoreCase(this.get_driver())) {
			driverClassName += "org.apache.derby.jdbc.EmbeddedDriver";
			url += "jdbc:derby:" + this.get_database();
		}
		else if ("Hypersonic".equalsIgnoreCase(this.get_driver())) {
			driverClassName += "org.hsqldb.jdbcDriver";
			url += "jdbc:hsqldb:" + PropsValues.LIFERAY_HOME +
				"/data/hsql/" + this.get_database();
			
			_username = "sa";
			_password = "";
		}
		else if ("Ingres".equalsIgnoreCase(this.get_driver())) {
			driverClassName += "com.ingres.jdbc.IngresDriver";
			url += "jdbc:ingres://localhost:II7/" + this.get_database();
		}
		else if ("MySQL".equalsIgnoreCase(this.get_driver())) {
			driverClassName += "com.mysql.jdbc.Driver";
			url += "jdbc:mysql://localhost/" + this.get_database() + 
				"?useUnicode=true&characterEncoding=UTF-8" +
				"&useFastDateParsing=false";
		}
		else if ("Oracle".equalsIgnoreCase(this.get_driver())) {
			driverClassName += "oracle.jdbc.this.get_driver().OracleDriver";
			url += "jdbc:oracle:thin:@localhost:1521:xe";
		}
		else if ("P6Spy".equalsIgnoreCase(this.get_driver())) {
			driverClassName += "com.p6spy.engine.spy.P6SpyDriver";
			url += "jdbc:mysql://localhost/" + this.get_database() + 
				"?useUnicode=true&characterEncoding=UTF-8" +
				"&useFastDateParsing=false";
		}
		else if ("PostgreSQL".equalsIgnoreCase(this.get_driver())) {
			driverClassName += "org.postgresql.Driver";
			url += "jdbc:postgresql://localhost:5432/" + this.get_database();
		}
		else if ("SQLServer".equalsIgnoreCase(this.get_driver())) {
			driverClassName += "net.sourceforge.jtds.jdbc.Driver";
			url += "jdbc:jtds:sqlserver://localhost/" + this.get_database();
		}
		else if ("Sybase".equalsIgnoreCase(this.get_driver())) {
			driverClassName += "net.sourceforge.jtds.jdbc.Driver";
			url += "jdbc:jtds:sybase://localhost:5000/" + this.get_database();
		}
		
		basicPortalExt += driverClassName + "\n";
		basicPortalExt += url + "\n";
		basicPortalExt += "jdbc.default.username=" + this.get_username() + "\n";
		basicPortalExt += "jdbc.default.password=" + this.get_password() + "\n";
	    
		
		return basicPortalExt;
	}
	
	private String _database;
	private String _driver;
	private String _password;
	private String _username;

}