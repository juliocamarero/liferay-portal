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

package com.liferay.portal.security.membershippolicy;

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Roberto Díaz
 */
@ExecutionTestListeners(
	listeners = {
		EnvironmentExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class SiteMembershipPolicyBaseImplTest
	extends BaseSiteMembershipPolicyTestCase {

	@Before
	public void setUp() throws Exception {
		FinderCacheUtil.clearCache();

		setGroup(GroupTestUtil.addGroup());
	}

	@After
	public void tearDown() throws Exception {
		GroupLocalServiceUtil.deleteGroup(getGroup());
	}

	@Test
	public void testIsMembershipAllowedReturnFalseInCheckException()
		throws Exception {

		addUsers();
		addForbiddenSites();

		Assert.assertFalse(
			SiteMembershipPolicyUtil.isMembershipAllowed(
				getUserIds()[0], getForbiddenSiteIds()[0]));
	}

	@Test
	public void testIsMembershipAllowedReturnTrueInCheckPass()
		throws Exception {

		addUsers();
		addStandardSites();

		Assert.assertTrue(
			SiteMembershipPolicyUtil.isMembershipAllowed(
				getUserIds()[0], getStandardSiteIds()[0]));
	}

	@Test
	public void testIsMembershipRequiredReturnFalseInCheckPass()
		throws Exception {

		addUsers();
		addStandardSites();

		Assert.assertFalse(
			SiteMembershipPolicyUtil.isMembershipRequired(
				getUserIds()[0], getStandardSiteIds()[0]));
	}

	@Test
	public void testIsMembershipRequiredReturnTrueInCheckException()
		throws Exception {

		addUsers();
		addRequiredSites();

		Assert.assertTrue(
			SiteMembershipPolicyUtil.isMembershipRequired(
				getUserIds()[0], getRequiredSiteIds()[0]));
	}

	@Test
	public void testIsRoleAllowedReturnFalseInCheckException()
		throws Exception {

		addUsers();
		addStandardSites();
		addForbiddenRoles();

		Assert.assertFalse(
			SiteMembershipPolicyUtil.isRoleAllowed(
				getUserIds()[0], getForbiddenSiteIds()[0],
				getForbiddenRoleIds()[0]));
	}

	@Test
	public void testIsRoleAllowedReturnTrueInCheckPass() throws Exception {
		addUsers();
		addStandardSites();
		addStandardRoles();

		Assert.assertTrue(
			SiteMembershipPolicyUtil.isRoleAllowed(
				getUserIds()[0], getStandardSiteIds()[0],
				getStandardRoleIds()[0]));
	}

	@Test
	public void testIsRoleRequiredReturnFalseInCheckPass() throws Exception {
		addUsers();
		addStandardSites();
		addStandardRoles();

		Assert.assertFalse(
			SiteMembershipPolicyUtil.isRoleRequired(
				getUserIds()[0], getStandardSiteIds()[0],
				getStandardRoleIds()[0]));
	}

	@Test
	public void testIsRoleRequiredReturnTrueInCheckException()
		throws Exception {

		addUsers();
		addStandardSites();
		addRequiredRoles();

		Assert.assertTrue(
			SiteMembershipPolicyUtil.isRoleRequired(
				getUserIds()[0], getRequiredSiteIds()[0],
				getRequiredRoleIds()[0]));
	}

}