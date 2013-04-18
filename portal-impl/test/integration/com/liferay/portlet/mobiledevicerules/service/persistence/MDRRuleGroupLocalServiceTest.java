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

package com.liferay.portlet.mobiledevicerules.service.persistence;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalCallbackAwareExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portal.util.LayoutTestUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.mobiledevicerules.model.MDRRuleGroup;
import com.liferay.portlet.mobiledevicerules.service.MDRRuleGroupLocalServiceUtil;
import com.liferay.portlet.mobiledevicerules.util.MDRTestUtil;

import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Manuel de la Peña
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		TransactionalCallbackAwareExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class MDRRuleGroupLocalServiceTest {

	@Before
	public void setUp() throws Exception {
		Company company = CompanyLocalServiceUtil.getCompany(
			TestPropsValues.getCompanyId());

		_globalGroup = company.getGroup();

		_globalRuleGroup = MDRTestUtil.addRuleGroup(_globalGroup.getGroupId());
	}

	@Test
	public void testSelectGlobalMDRRulesNotPresent() throws Exception {
		testSelectableMDRRuleGroups(false, false);
	}

	@Test
	public void testSelectGlobalMDRRulesNotPresent_deprecatedAPI()
		throws Exception {

		testSelectableMDRRuleGroups(false, true);
	}

	@Test
	public void testSelectGlobalMDRRulesPresent() throws Exception {
		testSelectableMDRRuleGroups(true, false);
	}

	@Test
	public void testSelectGlobalMDRRulesPresent_deprecatedAPI()
		throws Exception {

		testSelectableMDRRuleGroups(true, true);
	}

	protected void testSelectableMDRRuleGroups(
			boolean includeGlobalGroup, boolean deprecated)
		throws Exception {

		Group group = GroupTestUtil.addGroup();

		Layout layout = LayoutTestUtil.addLayout(
			group.getGroupId(), ServiceTestUtil.randomString());

		List<MDRRuleGroup> ruleGroups = null;

		if (deprecated) {
			ruleGroups = MDRRuleGroupLocalServiceUtil.searchByKeywords(
				layout.getGroupId(), StringPool.BLANK, false, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS);
		}
		else {
			LinkedHashMap<String, Object> params = null;

			if (includeGlobalGroup) {
				params = new LinkedHashMap<String, Object>();

				params.put("includeGlobalScope", true);
			}

			ruleGroups = MDRRuleGroupLocalServiceUtil.searchByKeywords(
				layout.getGroupId(), StringPool.BLANK, params, false,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		}

		if (includeGlobalGroup || deprecated) {
			Assert.assertTrue(ruleGroups.contains(_globalRuleGroup));
		}
		else {
			Assert.assertFalse(ruleGroups.contains(_globalRuleGroup));
		}
	}

	private Group _globalGroup;
	private MDRRuleGroup _globalRuleGroup;

}