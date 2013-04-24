/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portalweb.plugins.gospelforasia;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * @author Brian Wing Shun Chan
 */
public class AddPortletTest extends BaseTestCase {
	public void testAddPortlet() throws Exception {
		int label = 1;

		while (label >= 1) {
			switch (label) {
			case 1:
				selenium.selectWindow("null");
				selenium.selectFrame("relative=top");
				selenium.open("/web/guest/home/");
				selenium.clickAt("link=Gospel For Asia Test Page",
					RuntimeVariables.replace("Gospel For Asia Test Page"));
				selenium.waitForPageToLoad("30000");
				selenium.clickAt("//div[@id='dockbar']",
					RuntimeVariables.replace("Dockbar"));
				selenium.waitForElementPresent(
					"//script[contains(@src,'/aui/aui-editable/aui-editable-min.js')]");
				assertEquals(RuntimeVariables.replace("Add"),
					selenium.getText("//li[@id='_145_addContent']/a/span"));
				selenium.mouseOver("//li[@id='_145_addContent']/a/span");
				selenium.waitForVisible(
					"//li[contains(@class,'add-application')]/a");
				assertEquals(RuntimeVariables.replace(
						"Content and Applications"),
					selenium.getText(
						"//li[contains(@class,'add-application')]/a"));
				selenium.clickAt("//li[contains(@class,'add-application')]/a",
					RuntimeVariables.replace("Content and Applications"));
				selenium.waitForElementPresent(
					"//script[contains(@src,'/aui/aui-live-search/aui-live-search-min.js')]");

				boolean searchApplicationsNotVisible = selenium.isVisible(
						"//input[@id='layout_configuration_content']");

				if (searchApplicationsNotVisible) {
					label = 2;

					continue;
				}

				assertEquals(RuntimeVariables.replace("Applications"),
					selenium.getText(
						"//div[@id='portal_add_panel']/ul/li/span/a[contains(.,'Applications')]"));
				selenium.clickAt("//div[@id='portal_add_panel']/ul/li/span/a[contains(.,'Applications')]",
					RuntimeVariables.replace("Applications"));

			case 2:
				selenium.waitForVisible(
					"//input[@id='layout_configuration_content']");
				selenium.sendKeys("//input[@id='layout_configuration_content']",
					RuntimeVariables.replace("g"));
				selenium.waitForVisible("//li[@title='Gospel for Asia']/p/a");
				selenium.clickAt("//li[@title='Gospel for Asia']/p/a",
					RuntimeVariables.replace("Add"));
				selenium.waitForVisible("//section");
				assertTrue(selenium.isVisible("//section"));

			case 100:
				label = -1;
			}
		}
	}
}