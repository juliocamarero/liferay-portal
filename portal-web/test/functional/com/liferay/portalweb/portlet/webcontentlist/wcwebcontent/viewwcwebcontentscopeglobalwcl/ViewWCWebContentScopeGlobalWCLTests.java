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

package com.liferay.portalweb.portlet.webcontentlist.wcwebcontent.viewwcwebcontentscopeglobalwcl;

import com.liferay.portalweb.portal.BaseTestSuite;
import com.liferay.portalweb.portal.controlpanel.webcontent.wcwebcontent.addwcwebcontent.AddWCWebContentTest;
import com.liferay.portalweb.portal.controlpanel.webcontent.wcwebcontent.addwcwebcontent.TearDownWCWebContentTest;
import com.liferay.portalweb.portal.controlpanel.webcontent.wcwebcontent.addwcwebcontentscopeglobal.AddWCWebContentScopeGlobalTest;
import com.liferay.portalweb.portal.controlpanel.webcontent.wcwebcontent.addwcwebcontentscopeglobal.TearDownWCWebContentScopeGlobalTest;
import com.liferay.portalweb.portal.controlpanel.webcontent.wcwebcontent.addwcwebcontentscopemysite.AddWCWebContentScopeMySiteTest;
import com.liferay.portalweb.portal.controlpanel.webcontent.wcwebcontent.addwcwebcontentscopemysite.TearDownWCWebContentScopeMySiteTest;
import com.liferay.portalweb.portal.util.TearDownPageTest;
import com.liferay.portalweb.portlet.webcontentlist.portlet.addportletwcl.AddPageWCLTest;
import com.liferay.portalweb.portlet.webcontentlist.portlet.addportletwcl.AddPortletWCLTest;
import com.liferay.portalweb.portlet.webcontentlist.portlet.configureportletwclfiltersitescopeglobal.ConfigurePortletWCLFilterSiteScopeGlobalTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Brian Wing Shun Chan
 */
public class ViewWCWebContentScopeGlobalWCLTests extends BaseTestSuite {
	public static Test suite() {
		TestSuite testSuite = new TestSuite();
		testSuite.addTestSuite(AddPageWCLTest.class);
		testSuite.addTestSuite(AddPortletWCLTest.class);
		testSuite.addTestSuite(AddWCWebContentScopeGlobalTest.class);
		testSuite.addTestSuite(AddWCWebContentTest.class);
		testSuite.addTestSuite(AddWCWebContentScopeMySiteTest.class);
		testSuite.addTestSuite(ConfigurePortletWCLFilterSiteScopeGlobalTest.class);
		testSuite.addTestSuite(ViewWCWebContentScopeGlobalWCLTest.class);
		testSuite.addTestSuite(TearDownWCWebContentScopeMySiteTest.class);
		testSuite.addTestSuite(TearDownWCWebContentTest.class);
		testSuite.addTestSuite(TearDownWCWebContentScopeGlobalTest.class);
		testSuite.addTestSuite(TearDownPageTest.class);

		return testSuite;
	}
}