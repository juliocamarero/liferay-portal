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

package com.liferay.portalweb.portal.permissions.imagegallery.portlet;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * @author Brian Wing Shun Chan
 */
public class AddUserPortletTest extends BaseTestCase {
	public void testAddUserPortlet() throws Exception {
		int label = 1;

		while (label >= 1) {
			switch (label) {
			case 1:
				selenium.open("/web/guest/home/");

				for (int second = 0;; second++) {
					if (second >= 90) {
						fail("timeout");
					}

					try {
						if (selenium.isElementPresent("link=Control Panel")) {
							break;
						}
					}
					catch (Exception e) {
					}

					Thread.sleep(1000);
				}

				selenium.clickAt("link=Control Panel",
					RuntimeVariables.replace("Control Panel"));
				selenium.waitForPageToLoad("30000");
				selenium.clickAt("link=Users and Organizations",
					RuntimeVariables.replace("Users and Organizations"));
				selenium.waitForPageToLoad("30000");
				selenium.clickAt("link=Add", RuntimeVariables.replace("Add"));

				for (int second = 0;; second++) {
					if (second >= 90) {
						fail("timeout");
					}

					try {
						if (selenium.isVisible(
									"//div[@class='lfr-component lfr-menu-list']/ul/li[1]/a")) {
							break;
						}
					}
					catch (Exception e) {
					}

					Thread.sleep(1000);
				}

				assertEquals(RuntimeVariables.replace("User"),
					selenium.getText(
						"//div[@class='lfr-component lfr-menu-list']/ul/li[1]/a"));
				selenium.click(RuntimeVariables.replace(
						"//div[@class='lfr-component lfr-menu-list']/ul/li[1]/a"));
				selenium.waitForPageToLoad("30000");
				selenium.select("//select[@id='_125_prefixId']",
					RuntimeVariables.replace("label=Mr."));
				selenium.type("//input[@id='_125_screenName']",
					RuntimeVariables.replace("Portlet"));
				selenium.type("//input[@id='_125_emailAddress']",
					RuntimeVariables.replace("Portlet@liferay.com"));
				selenium.type("//input[@id='_125_firstName']",
					RuntimeVariables.replace("Portlet"));
				selenium.type("//input[@id='_125_lastName']",
					RuntimeVariables.replace("Liferay"));
				selenium.select("//select[@id='_125_birthdayMonth']",
					RuntimeVariables.replace("label=April"));
				selenium.select("//select[@id='_125_birthdayDay']",
					RuntimeVariables.replace("label=10"));
				selenium.select("//select[@id='_125_birthdayYear']",
					RuntimeVariables.replace("label=1986"));
				selenium.select("//select[@id='_125_male']",
					RuntimeVariables.replace("label=Male"));
				selenium.clickAt("//input[@value='Save']",
					RuntimeVariables.replace("Save"));
				selenium.waitForPageToLoad("30000");
				assertTrue(selenium.isPartialText(
						"//a[@id='_125_passwordLink']", "Password"));
				selenium.clickAt("//a[@id='_125_passwordLink']",
					RuntimeVariables.replace("Password"));

				for (int second = 0;; second++) {
					if (second >= 90) {
						fail("timeout");
					}

					try {
						if (selenium.isVisible("//input[@id='_125_password1']")) {
							break;
						}
					}
					catch (Exception e) {
					}

					Thread.sleep(1000);
				}

				selenium.type("//input[@id='_125_password1']",
					RuntimeVariables.replace("password"));
				selenium.type("//input[@id='_125_password2']",
					RuntimeVariables.replace("password"));
				selenium.clickAt("//input[@value='Save']",
					RuntimeVariables.replace("Save"));
				selenium.waitForPageToLoad("30000");
				assertEquals(RuntimeVariables.replace(
						"Your request completed successfully."),
					selenium.getText("//div[@class='portlet-msg-success']"));
				selenium.open("/web/guest/home/");

				for (int second = 0;; second++) {
					if (second >= 90) {
						fail("timeout");
					}

					try {
						if (selenium.isVisible("link=Sign Out")) {
							break;
						}
					}
					catch (Exception e) {
					}

					Thread.sleep(1000);
				}

				selenium.clickAt("link=Sign Out",
					RuntimeVariables.replace("Sign Out"));
				selenium.waitForPageToLoad("30000");
				selenium.open("/web/guest/home/");

				for (int second = 0;; second++) {
					if (second >= 90) {
						fail("timeout");
					}

					try {
						if (selenium.isVisible("//input[@id='_58_login']")) {
							break;
						}
					}
					catch (Exception e) {
					}

					Thread.sleep(1000);
				}

				selenium.type("//input[@id='_58_login']",
					RuntimeVariables.replace("portlet@liferay.com"));
				selenium.type("//input[@id='_58_password']",
					RuntimeVariables.replace("password"));

				boolean rememberMeCheckboxChecked1 = selenium.isChecked(
						"_58_rememberMeCheckbox");

				if (rememberMeCheckboxChecked1) {
					label = 2;

					continue;
				}

				selenium.clickAt("//input[@id='_58_rememberMeCheckbox']",
					RuntimeVariables.replace("Remember Me Checkbox"));

			case 2:
				selenium.clickAt("//input[@value='Sign In']",
					RuntimeVariables.replace("Sign In"));
				selenium.waitForPageToLoad("30000");

				boolean iAgreeVisible1 = selenium.isElementPresent(
						"//span/input");

				if (!iAgreeVisible1) {
					label = 3;

					continue;
				}

				selenium.clickAt("//input[@value='I Agree']",
					RuntimeVariables.replace("I Agree"));
				selenium.waitForPageToLoad("30000");

			case 3:

				boolean newPasswordVisible1 = selenium.isElementPresent(
						"//span/input");

				if (!newPasswordVisible1) {
					label = 4;

					continue;
				}

				selenium.type("password1", RuntimeVariables.replace("test"));
				selenium.type("password2", RuntimeVariables.replace("test"));
				selenium.clickAt("//input[@value='Save']",
					RuntimeVariables.replace("Save"));
				selenium.waitForPageToLoad("30000");

			case 4:

				boolean passwordReminderVisible1 = selenium.isElementPresent(
						"reminderQueryAnswer");

				if (!passwordReminderVisible1) {
					label = 5;

					continue;
				}

				assertEquals(RuntimeVariables.replace(
						"Please choose a reminder query."),
					selenium.getText("//form/div[1]"));
				selenium.type("reminderQueryAnswer",
					RuntimeVariables.replace("test"));
				selenium.clickAt("//input[@value='Save']",
					RuntimeVariables.replace("Save"));
				selenium.waitForPageToLoad("30000");

			case 5:
				selenium.open("/web/guest/home/");

				for (int second = 0;; second++) {
					if (second >= 90) {
						fail("timeout");
					}

					try {
						if (selenium.isVisible("link=Sign Out")) {
							break;
						}
					}
					catch (Exception e) {
					}

					Thread.sleep(1000);
				}

				selenium.clickAt("link=Sign Out",
					RuntimeVariables.replace("Sign Out"));
				selenium.waitForPageToLoad("30000");
				selenium.open("/web/guest/home/");

				for (int second = 0;; second++) {
					if (second >= 90) {
						fail("timeout");
					}

					try {
						if (selenium.isVisible("//input[@id='_58_login']")) {
							break;
						}
					}
					catch (Exception e) {
					}

					Thread.sleep(1000);
				}

				selenium.type("//input[@id='_58_login']",
					RuntimeVariables.replace("test@liferay.com"));
				selenium.type("//input[@id='_58_password']",
					RuntimeVariables.replace("test"));

				boolean rememberMeCheckboxChecked2 = selenium.isChecked(
						"_58_rememberMeCheckbox");

				if (rememberMeCheckboxChecked2) {
					label = 6;

					continue;
				}

				selenium.clickAt("//input[@id='_58_rememberMeCheckbox']",
					RuntimeVariables.replace("Remember Me Checkbox"));

			case 6:
				selenium.clickAt("//input[@value='Sign In']",
					RuntimeVariables.replace("Sign In"));
				selenium.waitForPageToLoad("30000");

			case 100:
				label = -1;
			}
		}
	}
}