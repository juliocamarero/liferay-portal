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

package com.liferay.portalweb.socialofficesite.forums.mbthread.postnewmbcategorythreadmessagesite;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * @author Brian Wing Shun Chan
 */
public class ViewMBCategoryThreadMessageMyPostsSiteTest extends BaseTestCase {
	public void testViewMBCategoryThreadMessageMyPostsSite()
		throws Exception {
		int label = 1;

		while (label >= 1) {
			switch (label) {
			case 1:
				selenium.selectWindow("null");
				selenium.selectFrame("relative=top");
				selenium.open("/user/joebloggs/so/dashboard");
				assertEquals(RuntimeVariables.replace("Sites"),
					selenium.getText("//div[@id='so-sidebar']/h3"));
				assertTrue(selenium.isVisible("//input[@class='search-input']"));
				selenium.type("//input[@class='search-input']",
					RuntimeVariables.replace("Open"));
				Thread.sleep(1000);
				assertEquals(RuntimeVariables.replace("Open Site Name"),
					selenium.getText(
						"//li[contains(@class, 'social-office-enabled')]/span[2]/a"));
				selenium.clickAt("//li[contains(@class, 'social-office-enabled')]/span[2]/a",
					RuntimeVariables.replace("Open Site Name"));
				selenium.waitForPageToLoad("30000");
				assertEquals(RuntimeVariables.replace("Forums"),
					selenium.getText("//nav/ul/li[contains(.,'Forums')]/a/span"));
				selenium.clickAt("//nav/ul/li[contains(.,'Forums')]/a/span",
					RuntimeVariables.replace("Forums"));
				selenium.waitForPageToLoad("30000");
				selenium.clickAt("link=My Posts",
					RuntimeVariables.replace("My Posts"));
				selenium.waitForPageToLoad("30000");

				boolean portletTitleVisible = selenium.isVisible(
						"xPath=(//span[@class='portlet-title-default'])[contains(.,'Breadcrumb')]");

				if (portletTitleVisible) {
					label = 2;

					continue;
				}

				selenium.clickAt("//li[@id='_145_toggleControls']",
					RuntimeVariables.replace("Edit Controls"));

			case 2:
				assertEquals(RuntimeVariables.replace("Breadcrumb"),
					selenium.getText(
						"xPath=(//span[@class='portlet-title-default'])[contains(.,'Breadcrumb')]"));
				assertEquals(RuntimeVariables.replace("Message Boards"),
					selenium.getText(
						"xPath=(//span[@class='portlet-title-default'])[contains(.,'Message Boards')]"));
				assertEquals(RuntimeVariables.replace("Liferay"),
					selenium.getText(
						"xPath=(//ul[contains(@class,'breadcrumbs')]/li)[1]"));
				assertEquals(RuntimeVariables.replace("Open Site Name"),
					selenium.getText(
						"xPath=(//ul[contains(@class,'breadcrumbs')]/li)[2]"));
				assertEquals(RuntimeVariables.replace("Forums"),
					selenium.getText(
						"xPath=(//ul[contains(@class,'breadcrumbs')]/li)[3]"));
				assertEquals(RuntimeVariables.replace("My Posts"),
					selenium.getText(
						"xPath=(//ul[contains(@class,'breadcrumbs')]/li)[4]"));
				assertEquals(RuntimeVariables.replace(
						"MB Category Thread Message Subject"),
					selenium.getText("//td[1]/a"));
				assertEquals(RuntimeVariables.replace("Joe Bloggs"),
					selenium.getText("//td[2]/a"));
				assertEquals(RuntimeVariables.replace("1"),
					selenium.getText("//td[3]/a"));
				assertTrue(selenium.isVisible("//td[4]/a"));
				assertTrue(selenium.isPartialText("//td[5]/a", "By: Joe Bloggs"));
				selenium.clickAt("//td[1]/a",
					RuntimeVariables.replace(
						"MB Category Thread Message Subject"));
				selenium.waitForPageToLoad("30000");
				assertEquals(RuntimeVariables.replace("Breadcrumb"),
					selenium.getText(
						"xPath=(//span[@class='portlet-title-default'])[contains(.,'Breadcrumb')]"));
				assertEquals(RuntimeVariables.replace("Message Boards"),
					selenium.getText(
						"xPath=(//span[@class='portlet-title-default'])[contains(.,'Message Boards')]"));
				assertEquals(RuntimeVariables.replace("Liferay"),
					selenium.getText(
						"xPath=(//ul[contains(@class,'breadcrumbs')]/li)[1]"));
				assertEquals(RuntimeVariables.replace("Open Site Name"),
					selenium.getText(
						"xPath=(//ul[contains(@class,'breadcrumbs')]/li)[2]"));
				assertEquals(RuntimeVariables.replace("Forums"),
					selenium.getText(
						"xPath=(//ul[contains(@class,'breadcrumbs')]/li)[3]"));
				assertEquals(RuntimeVariables.replace("MB Category Name"),
					selenium.getText(
						"xPath=(//ul[contains(@class,'breadcrumbs')]/li)[4]"));
				assertEquals(RuntimeVariables.replace(
						"MB Category Thread Message Subject"),
					selenium.getText(
						"xPath=(//ul[contains(@class,'breadcrumbs')]/li)[5]"));
				assertEquals(RuntimeVariables.replace(
						"MB Category Thread Message Subject"),
					selenium.getText("//h1[@class='header-title']"));
				assertEquals(RuntimeVariables.replace(
						"\u00ab Back to MB Category Name"),
					selenium.getText("//a[@id='_19_TabsBack']"));
				assertEquals(RuntimeVariables.replace(
						"Threads [ Previous | Next ]"),
					selenium.getText("//div[@class='thread-navigation']"));
				assertEquals(RuntimeVariables.replace("Joe Bloggs"),
					selenium.getText("//span[@class='user-name']"));
				assertEquals(RuntimeVariables.replace(
						"MB Category Thread Message Subject"),
					selenium.getText("//div[@class='subject']/a"));
				assertEquals(RuntimeVariables.replace(
						"MB Category Thread Message Body"),
					selenium.getText("//div[@class='thread-body']"));
				assertEquals(RuntimeVariables.replace("0 (0 Votes)"),
					selenium.getText(
						"//div[contains(@id,'ratingThumbContent')]/div"));

			case 100:
				label = -1;
			}
		}
	}
}