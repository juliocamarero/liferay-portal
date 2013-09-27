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
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.RoleServiceUtil;
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

		_reviewerRole = RoleLocalServiceUtil.fetchRole(
			_group.getCompanyId(), RoleConstants.PORTAL_CONTENT_REVIEWER);

		if (_reviewerRole == null) {
			_reviewerRole = addReviewerRole();
		}
	}

	@Test
	public void testIsReviewerWithCompanyAdminUser() throws Exception {
		User user = TestPropsValues.getUser();

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		Assert.assertTrue(
			permissionChecker.isReviewer(user.getCompanyId(),
			_group.getGroupId()));
	}

	@Test
	public void testIsReviewerWithGroupAdminUser() throws Exception {
		User user = UserTestUtil.addGroupAdminUser(_group);

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		Assert.assertTrue(
			permissionChecker.isReviewer(user.getCompanyId(),
			_group.getGroupId()));
	}

	@Test
	public void testIsReviewerWithRegularUser() throws Exception {
		User user = UserTestUtil.addUser();

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		Assert.assertFalse(
			permissionChecker.isReviewer(user.getCompanyId(),
			_group.getGroupId()));
	}

	@Test
	public void testIsReviewerWithReviewerUser() throws Exception {
		User user = UserTestUtil.addUser();

		UserLocalServiceUtil.setRoleUsers(
			_reviewerRole.getRoleId(), new long[] {user.getUserId()});

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		Assert.assertTrue(
			permissionChecker.isReviewer(user.getCompanyId(),
			_group.getGroupId()));
	}

	protected Role addReviewerRole() throws Exception {
		return RoleServiceUtil.addRole(
			null, 0, RoleConstants.PORTAL_CONTENT_REVIEWER,
			ServiceTestUtil.randomLocaleStringMap(),
			ServiceTestUtil.randomLocaleStringMap(), RoleConstants.TYPE_REGULAR,
			ServiceTestUtil.randomString(),
			ServiceTestUtil.getServiceContext());
	}

	private Group _group;
	private Role _reviewerRole;

}