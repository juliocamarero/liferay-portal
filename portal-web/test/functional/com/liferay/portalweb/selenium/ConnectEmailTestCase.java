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

package com.liferay.portalweb.selenium;

import com.liferay.portalweb.util.TestPropsValues;

import org.junit.Test;

/**
 * @author Kwang Lee
 */
public class ConnectEmailTestCase extends BaseSeleniumTestCase {

	@Test
	public void testConnectEmail() throws Exception {
		selenium.connectToEmailAccount(
			TestPropsValues.EMAIL_ADDRESS_1, TestPropsValues.EMAIL_PASSWORD_1);
	}

	@Test
	public void testFailConnectEmail() throws Exception {
		try {
			selenium.connectToEmailAccount(
				"ThisIsNotCorrectEmailAddress",
				"ThisIsNotCorrectEmailPassword");
		}
		catch (Throwable t) {
			String message = t.getMessage();

			assertTrue(message.contains("Invalid credentials"));
		}
	}

}