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

package com.liferay.portal.security.permission;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.RoleServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portal.util.UserTestUtil;

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
public class PermissionCheckerTest {

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testIsReviewerWithCompanyAdminUser() throws Exception {
		User user = TestPropsValues.getUser();

		Assert.assertTrue(
			_getPermissionChecker(user).isReviewer(
				user.getCompanyId(), _group.getGroupId()));
	}

	@Test
	public void testIsReviewerWithGroupAdminUser() throws Exception {
		User user = UserTestUtil.addGroupAdminUser(_group);

		Assert.assertTrue(
			_getPermissionChecker(user).isReviewer(
				user.getCompanyId(), _group.getGroupId()));
	}

	@Test
	public void testIsReviewerWithRegularUser() throws Exception {
		User user = UserTestUtil.addUser();

		Assert.assertFalse(
			_getPermissionChecker(user).isReviewer(
				user.getCompanyId(), _group.getGroupId()));
	}

	@Test
	public void testIsReviewerWithReviewerUser() throws Exception {
		User user = UserTestUtil.addUser();

		Role reviewerRole = addReviewerRole();

		UserLocalServiceUtil.setRoleUsers(
			reviewerRole.getRoleId(), new long[] {user.getUserId()});

		Assert.assertTrue(
			_getPermissionChecker(user).isReviewer(
				user.getCompanyId(), _group.getGroupId()));
	}

	@Test
	public void testIsReviewerWithSiteReviewer() throws Exception {
		_group = GroupLocalServiceUtil.getGroup(
			TestPropsValues.getCompanyId(), GroupConstants.GUEST);

		_group.setSite(true);

		_group = GroupLocalServiceUtil.updateGroup(_group);

		addSiteReviewerRole();

		User user = UserTestUtil.addGroupUser(
			_group, RoleConstants.SITE_CONTENT_REVIEWER);

		Assert.assertTrue(
			_getPermissionChecker(user).isReviewer(
				user.getCompanyId(), _group.getGroupId()));
	}

	protected Role addReviewerRole() throws Exception {
		Role portalReviewerRole = RoleLocalServiceUtil.fetchRole(
			_group.getCompanyId(), RoleConstants.PORTAL_CONTENT_REVIEWER);

		if (portalReviewerRole != null) {
			return portalReviewerRole;
		}

		return RoleServiceUtil.addRole(
			null, 0, RoleConstants.PORTAL_CONTENT_REVIEWER,
			ServiceTestUtil.randomLocaleStringMap(),
			ServiceTestUtil.randomLocaleStringMap(), RoleConstants.TYPE_REGULAR,
			ServiceTestUtil.randomString(),
			ServiceTestUtil.getServiceContext());
	}

	protected void addSiteReviewerRole() throws Exception {
		Role siteReviewerRole = RoleLocalServiceUtil.fetchRole(
			_group.getCompanyId(), RoleConstants.SITE_CONTENT_REVIEWER);

		if (siteReviewerRole != null) {
			return;
		}

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext();

		serviceContext.setCompanyId(TestPropsValues.getCompanyId());

		RoleServiceUtil.addRole(
			null, 0, RoleConstants.SITE_CONTENT_REVIEWER,
			ServiceTestUtil.randomLocaleStringMap(),
			ServiceTestUtil.randomLocaleStringMap(), RoleConstants.TYPE_SITE,
			ServiceTestUtil.randomString(), serviceContext);
	}

	private PermissionChecker _getPermissionChecker(User user)
		throws Exception {

		return PermissionCheckerFactoryUtil.create(user);
	}

	private Group _group;

}