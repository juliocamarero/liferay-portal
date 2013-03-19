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

import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroupRole;
import com.liferay.portal.security.membershippolicy.util.MembershipPolicyTestUtil;
import com.liferay.portal.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.service.UserGroupRoleServiceUtil;
import com.liferay.portal.service.persistence.UserGroupRolePK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Roberto Díaz
 */
public class SiteMembershipPolicyRolesTest
	extends BaseSiteMembershipPolicyTestCase {

	@Test(expected = MembershipPolicyException.class)
	public void testExceptionThrowingWhenAddUsersToForbiddenSiteRole()
		throws Exception {

		addUsers();
		addForbiddenRoles();

		Group group = getGroup();

		UserGroupRoleServiceUtil.addUserGroupRoles(
			getUserIds(), group.getGroupId(), getForbiddenRoleIds()[0]);
	}

	@Test(expected = MembershipPolicyException.class)
	public void testExceptionThrowingWhenAddUserToForbiddenSiteRoles()
		throws Exception {

		addUsers();
		addForbiddenRoles();

		Group group = getGroup();

		UserGroupRoleServiceUtil.addUserGroupRoles(
			getUserIds()[0], group.getGroupId(), getForbiddenRoleIds());

	}

	@Test(expected = MembershipPolicyException.class)
	public void testExceptionThrowingWhenDeleteUserFromRequiredSiteRoles()
		throws Exception {

		addUsers();
		addRequiredRoles();

		Group group = getGroup();

		UserGroupRoleServiceUtil.deleteUserGroupRoles(
			getUserIds()[0], group.getGroupId(), getRequiredRoleIds());
	}

	@Test(expected = MembershipPolicyException.class)
	public void testExceptionThrowingWhenDeleteUsersFromRequiredSiteRole()
		throws Exception {

		addUsers();
		addRequiredRoles();

		Group group = getGroup();

		UserGroupRoleServiceUtil.deleteUserGroupRoles(
			getUserIds(), group.getGroupId(), getRequiredRoleIds()[0]);
	}

	@Test
	public void testExceptionThrowWhenDeleteUsersFromStandardSiteRole()
		throws Exception {

		addUsers();
		addStandardRoles();

		Group group = getGroup();

		UserGroupRoleServiceUtil.deleteUserGroupRoles(
			getUserIds(), group.getGroupId(), getStandardRoleIds()[0]);

		Assert.assertTrue(getPropagateRolesMethodFlag());
	}

	@Test(expected = MembershipPolicyException.class)
	public void testExceptionThrowWhenSetForbiddenSiteRoleToUser()
		throws Exception {

		addUsers();
		addForbiddenRoles();

		Group group = getGroup();

		UserGroupRolePK userGroupRolePK = new UserGroupRolePK(
			getUserIds()[0], group.getGroupId(), getForbiddenRoleIds()[0]);

		List<UserGroupRole> userGroupRoleList = new ArrayList<UserGroupRole>();

		UserGroupRole userGroupRole =
			UserGroupRoleLocalServiceUtil.createUserGroupRole(userGroupRolePK);

		userGroupRoleList.add(userGroupRole);

		MembershipPolicyTestUtil.updateUser(
			getUser(), null, null, null, null, userGroupRoleList);
	}

	@Test
	public void testPropagationLaunchWhenAddUsersToStandardSiteRole()
		throws Exception {

		addUsers();
		addStandardRoles();

		Group group = getGroup();

		UserGroupRoleServiceUtil.addUserGroupRoles(
			getUserIds(), group.getGroupId(), getStandardRoleIds()[0]);

		Assert.assertTrue(getPropagateRolesMethodFlag());
	}

	@Test
	public void testPropagationLaunchWhenAddUserToStandardSiteRoles()
		throws Exception {

		addUsers();
		addStandardRoles();

		Group group = getGroup();

		UserGroupRoleServiceUtil.addUserGroupRoles(
			getUserIds()[0], group.getGroupId(), getStandardRoleIds());

		Assert.assertTrue(getPropagateRolesMethodFlag());
	}

	@Test
	public void testPropagationLaunchWhenDeletingUserFromStandardSiteRoles()
		throws Exception {

		addUsers();
		addStandardRoles();

		Group group = getGroup();

		UserGroupRoleServiceUtil.deleteUserGroupRoles(
			getUserIds()[0], group.getGroupId(), getStandardRoleIds());

		Assert.assertTrue(getPropagateRolesMethodFlag());
	}

	@Test
	public void testPropagationLaunchWhenSetStandardSiteRoleToUser()
		throws Exception {

		addUsers();
		addStandardRoles();

		Group group = getGroup();

		UserGroupRolePK userGroupRolePK = new UserGroupRolePK(
			getUserIds()[0], group.getGroupId(), getStandardRoleIds()[0]);

		List<UserGroupRole> userGroupRoleList = new ArrayList<UserGroupRole>();

		UserGroupRole userGroupRole =
			UserGroupRoleLocalServiceUtil.createUserGroupRole(userGroupRolePK);

		userGroupRoleList.add(userGroupRole);

		MembershipPolicyTestUtil.updateUser(
			getUser(), null, null, null, null, userGroupRoleList);

		Assert.assertTrue(getPropagateRolesMethodFlag());
	}

	@Test
	public void testPropagationLaunchWhenUnsetStandardSiteRoleFromUser()
		throws Exception {

		addUsers();
		addStandardRoles();

		Group group = getGroup();

		UserGroupRoleServiceUtil.addUserGroupRoles(
			getUser().getUserId(), group.getGroupId(), getStandardRoleIds());

		MembershipPolicyTestUtil.updateUser(
			getUser(), null, null, null, null,
			Collections.<UserGroupRole>emptyList());

		Assert.assertTrue(getPropagateRolesMethodFlag());
	}

	@Test
	public void testUserRoleCountWhenUnsetRequiredSiteRoleFromUser()
		throws Exception {

		addUsers();
		addRequiredRoles();

		Group group = getGroup();

		UserGroupRoleServiceUtil.addUserGroupRoles(
			getUserIds()[0], group.getGroupId(), getRequiredRoleIds());

		User user = getUser();

		List<UserGroupRole> expectedUserGroupRoles =
			UserGroupRoleLocalServiceUtil.getUserGroupRoles(user.getUserId());

		int expectedUserGroupRolesCount = expectedUserGroupRoles.size();

		List<UserGroupRole> emptyNonAbstractList =
			new ArrayList<UserGroupRole>();

		MembershipPolicyTestUtil.updateUser(
			getUser(), null, null, null, null, emptyNonAbstractList);

		List<UserGroupRole> currentUserGroupRoles =
			UserGroupRoleLocalServiceUtil.getUserGroupRoles(user.getUserId());

		int currentUserGroupRolesCount = currentUserGroupRoles.size();

		Assert.assertEquals(expectedUserGroupRoles, currentUserGroupRoles);
	}

}