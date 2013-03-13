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
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroupRole;
import com.liferay.portal.security.membershippolicy.util.MembershipPolicyTestUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.service.UserGroupRoleServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.persistence.UserGroupRolePK;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
public class SiteMembershipPolicyRolesTest
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

	@Test(expected = MembershipPolicyException.class)
	@Transactional
	public void testExceptionThrowingWhenAddUsersToForbiddenSiteRole()
		throws Exception {

		addUsers();
		addForbiddenRoles();

		long groupId = getGroup().getGroupId();
		long roleId = getForbiddenRoleIds()[0];

		UserGroupRoleServiceUtil.addUserGroupRoles(
			getUserIds(), groupId, roleId);
	}

	@Test(expected = MembershipPolicyException.class)
	@Transactional
	public void testExceptionThrowingWhenAddUserToForbiddenSiteRoles()
		throws Exception {

		addUsers();
		addForbiddenRoles();

		long userId = getUserIds()[0];
		long groupId = getGroup().getGroupId();

		UserGroupRoleServiceUtil.addUserGroupRoles(
			userId, groupId, getForbiddenRoleIds());

	}

	@Test(expected = MembershipPolicyException.class)
	@Transactional
	public void testExceptionThrowingWhenDeleteUserFromRequiredSiteRoles()
		throws Exception {

		addUsers();
		addRequiredRoles();

		long userId = getUserIds()[0];
		long groupId = getGroup().getGroupId();

		UserGroupRoleServiceUtil.deleteUserGroupRoles(
			userId, groupId, getRequiredRoleIds());
	}

	@Test(expected = MembershipPolicyException.class)
	@Transactional
	public void testExceptionThrowingWhenDeleteUsersFromRequiredSiteRole()
		throws Exception {

		addUsers();
		addRequiredRoles();

		long groupId = getGroup().getGroupId();
		long roleId = getRequiredRoleIds()[0];

		UserGroupRoleServiceUtil.deleteUserGroupRoles(
			getUserIds(), groupId, roleId);
	}

	@Test
	@Transactional
	public void testExceptionThrowWhenDeleteUsersFromStandardSiteRole()
		throws Exception {

		addUsers();
		addStandardRoles();

		long groupId = getGroup().getGroupId();
		long roleId = getStandardRoleIds()[0];

		UserGroupRoleServiceUtil.deleteUserGroupRoles(
			getUserIds(), groupId, roleId);

		Assert.assertTrue(getPropagateRolesMethodFlag());
	}

	@Test(expected = MembershipPolicyException.class)
	@Transactional
	public void testExceptionThrowWhenSetForbiddenSiteRoleFToUser()
		throws Exception {

		addUsers();
		addForbiddenRoles();

		long userId = _getUser().getUserId();
		long groupId = getGroup().getGroupId();
		long roleId = getForbiddenRoleIds()[0];

		UserGroupRolePK userGroupRolePK = new UserGroupRolePK(
			userId, groupId, roleId);

		List<UserGroupRole> userGroupRoleList = new ArrayList<UserGroupRole>();

		UserGroupRole userGroupRole =
			UserGroupRoleLocalServiceUtil.createUserGroupRole(userGroupRolePK);

		userGroupRoleList.add(userGroupRole);

		MembershipPolicyTestUtil.updateUser(
			_getUser(), null, null, null, null, userGroupRoleList);
	}

	@Test(expected = MembershipPolicyException.class)
	@Transactional
	public void testExceptionThrowWhenUnsetRequiredSiteRoleFromUser()
		throws Exception {

		addUsers();
		addRequiredRoles();

		long groupId = getGroup().getGroupId();

		UserGroupRoleServiceUtil.addUserGroupRoles(
			_getUser().getUserId(), groupId, getRequiredRoleIds());

		MembershipPolicyTestUtil.updateUser(
			_getUser(), null, null, null, null,
			Collections.<UserGroupRole>emptyList());
	}

	@Test
	@Transactional
	public void testPropagationLaunchWhenAddUsersToStandardSiteRole()
		throws Exception {

		addUsers();
		addStandardRoles();

		long groupId = getGroup().getGroupId();
		long roleId = getStandardRoleIds()[0];

		UserGroupRoleServiceUtil.addUserGroupRoles(
			getUserIds(), groupId, roleId);

		Assert.assertTrue(getPropagateRolesMethodFlag());
	}

	@Test
	@Transactional
	public void testPropagationLaunchWhenAddUserToStandardSiteRoles()
		throws Exception {

		addUsers();
		addStandardRoles();

		long userId = getUserIds()[0];
		long groupId = getGroup().getGroupId();

		UserGroupRoleServiceUtil.addUserGroupRoles(
			userId, groupId, getStandardRoleIds());

		Assert.assertTrue(getPropagateRolesMethodFlag());
	}

	@Test
	@Transactional
	public void testPropagationLaunchWhenDeletingUserFromStandardSiteRoles()
		throws Exception {

		addUsers();
		addStandardRoles();

		long userId = getUserIds()[0];
		long groupId = getGroup().getGroupId();

		UserGroupRoleServiceUtil.deleteUserGroupRoles(
			userId, groupId, getStandardRoleIds());

		Assert.assertTrue(getPropagateRolesMethodFlag());
	}

	@Test
	@Transactional
	public void testPropagationLaunchWhenSetStandardSiteRoleToUser()
		throws Exception {

		addUsers();
		addStandardRoles();

		long userId = _getUser().getUserId();
		long groupId = getGroup().getGroupId();
		long roleId = getStandardRoleIds()[0];

		UserGroupRolePK userGroupRolePK = new UserGroupRolePK(
			userId, groupId, roleId);

		List<UserGroupRole> userGroupRoleList = new ArrayList<UserGroupRole>();

		UserGroupRole userGroupRole =
			UserGroupRoleLocalServiceUtil.createUserGroupRole(userGroupRolePK);

		userGroupRoleList.add(userGroupRole);

		MembershipPolicyTestUtil.updateUser(
			_getUser(), null, null, null, null, userGroupRoleList);

		Assert.assertTrue(getPropagateRolesMethodFlag());
	}

	@Test
	@Transactional
	public void testPropagationLaunchWhenUnsetStandardSiteRoleFromUser()
		throws Exception {

		addUsers();
		addStandardRoles();

		long groupId = getGroup().getGroupId();

		UserGroupRoleServiceUtil.addUserGroupRoles(
			_getUser().getUserId(), groupId, getStandardRoleIds());

		MembershipPolicyTestUtil.updateUser(
			_getUser(), null, null, null, null,
			Collections.<UserGroupRole>emptyList());

		Assert.assertTrue(getPropagateRolesMethodFlag());
	}

	private User _getUser() throws Exception {
		return UserLocalServiceUtil.getUser(getUserIds()[0]);
	}

}