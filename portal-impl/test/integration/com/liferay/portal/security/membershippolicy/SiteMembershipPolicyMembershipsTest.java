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

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroupRole;
import com.liferay.portal.security.membershippolicy.util.MembershipPolicyTestUtil;
import com.liferay.portal.service.GroupServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.UserServiceUtil;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Roberto Díaz
 */
public class SiteMembershipPolicyMembershipsTest
	extends BaseSiteMembershipPolicyTestCase {

	@Test
	public void testCountGroupUsersWhenAddForbiddenSitesToUser()
		throws Exception {

		addUsers();
		addForbiddenSites();

		int expectedUsersInFirstForbiddenGroupCount =
			UserLocalServiceUtil.getGroupUsersCount(getForbiddenSiteIds()[0]);

		int expectedUsersInSecondForbiddenGroupCount =
			UserLocalServiceUtil.getGroupUsersCount(getForbiddenSiteIds()[1]);

		User user = UserLocalServiceUtil.getUser(getUserIds()[0]);

		try {
			MembershipPolicyTestUtil.updateUser(
				user, null, null, getForbiddenSiteIds(), null,
				Collections.<UserGroupRole>emptyList());
		}
		catch (Exception e) {
			if (!(e instanceof MembershipPolicyException)) {
				Assert.fail("Exception Throwing :" + e.getClass().getName());
			}
		}

		int currentUsersInFirstForbiddenGroupCount =
			UserLocalServiceUtil.getGroupUsersCount(getForbiddenSiteIds()[0]);

		int currentUsersInSecondForbiddenGroupCount =
			UserLocalServiceUtil.getGroupUsersCount(getForbiddenSiteIds()[1]);

		Assert.assertEquals(
			expectedUsersInFirstForbiddenGroupCount,
			currentUsersInFirstForbiddenGroupCount);

		Assert.assertEquals(
			expectedUsersInSecondForbiddenGroupCount,
			currentUsersInSecondForbiddenGroupCount);
	}

	@Test
	public void testCountGroupUsersWhenAddRequiredSitesToUser()
		throws Exception {

		addUsers();
		addRequiredSites();

		int expectedUsersInFirstRequiredGroupCount =
			UserLocalServiceUtil.getGroupUsersCount(
				getRequiredSiteIds()[0]) + addedUsers(1);

		int expectedUsersInSecondRequiredGroupCount =
			UserLocalServiceUtil.getGroupUsersCount(
				getRequiredSiteIds()[1]) + addedUsers(1);

		User user = UserLocalServiceUtil.getUser(getUserIds()[0]);

		MembershipPolicyTestUtil.updateUser(
			user, null, null, getRequiredSiteIds(), null,
			Collections.<UserGroupRole>emptyList());

		int currentUsersInFirstRequiredGroupCount =
			UserLocalServiceUtil.getGroupUsersCount(getRequiredSiteIds()[0]);

		int currentUsersInSecondRequiredGroupCount =
			UserLocalServiceUtil.getGroupUsersCount(getRequiredSiteIds()[1]);

		Assert.assertEquals(
			expectedUsersInFirstRequiredGroupCount,
			currentUsersInFirstRequiredGroupCount);

		Assert.assertEquals(
			expectedUsersInSecondRequiredGroupCount,
			currentUsersInSecondRequiredGroupCount);
	}

	@Test
	public void testCountGroupUsersWhenAddUsersToRequiredSite()
		throws Exception {

		addUsers();
		addRequiredSites();

		int expectedUserGroupCountInRequiredSite =
			UserLocalServiceUtil.getGroupUsersCount(
				getRequiredSiteIds()[0]) + addedUsers(2);

		try {
			UserServiceUtil.addGroupUsers(
				getRequiredSiteIds()[0], getUserIds(), _getServiceContext());
		}
		catch (Exception e) {
			if (e instanceof MembershipPolicyException) {
				Assert.fail("MembershipPolicyException was throw");
			}
			else {
				Assert.fail(
					"Unexpected Exception throw: " + e.getClass().getName());
			}
		}

		int currentUserGroupCountInRequiredSite =
			UserLocalServiceUtil.getGroupUsersCount(getRequiredSiteIds()[0]);

		Assert.assertEquals(
			expectedUserGroupCountInRequiredSite,
			currentUserGroupCountInRequiredSite);
		Assert.assertTrue(getPropagateMembershipMethodFlag());
	}

	@Test
	public void testCountGroupUsersWhenAddUserToRequiredSite()
		throws Exception {

		addUsers();
		addRequiredSites();

		int expectedGroupUsersInRequiredSite =
			UserLocalServiceUtil.getGroupUsersCount(
				getRequiredSiteIds()[0]) + addedUsers(1);

		UserServiceUtil.addGroupUsers(
			getRequiredSiteIds()[0], new long[]{getUserIds()[0]},
			_getServiceContext());

		int currentGroupUsersInRequiredSite =
			UserLocalServiceUtil.getGroupUsersCount(getRequiredSiteIds()[0]);

		Assert.assertEquals(
			expectedGroupUsersInRequiredSite, currentGroupUsersInRequiredSite);
		Assert.assertTrue(getPropagateMembershipMethodFlag());
	}

	@Test(expected = MembershipPolicyException.class)
	public void testExceptionThrowWhenAddForbiddenSitesToUser()
		throws Exception {

		addUsers();
		addForbiddenSites();

		User user = UserLocalServiceUtil.getUser(getUserIds()[0]);

		MembershipPolicyTestUtil.updateUser(
			user, null, null, getForbiddenSiteIds(), null,
			Collections.<UserGroupRole>emptyList());
	}

	@Test(expected = MembershipPolicyException.class)
	public void testExceptionThrowWhenAddForbiddenSiteToUser()
		throws Exception {

		addUsers();
		addForbiddenSites();

		User user = UserLocalServiceUtil.getUser(getUserIds()[0]);

		MembershipPolicyTestUtil.updateUser(
			user, null, null, getForbiddenSiteIds(), null,
			Collections.<UserGroupRole>emptyList());
	}

	@Test(expected = MembershipPolicyException.class)
	public void testExceptionThrowWhenAddUsersToForbiddenSite()
		throws Exception {

		addUsers();
		addForbiddenSites();

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext();

		UserServiceUtil.addGroupUsers(
			getForbiddenSiteIds()[0], getUserIds(), serviceContext);
	}

	@Test(expected = MembershipPolicyException.class)
	public void testExceptionThrowWhenAddUserToForbiddenSite()
		throws Exception {

		addUsers();
		addForbiddenSites();

		UserServiceUtil.addGroupUsers(
			getForbiddenSiteIds()[0], new long[]{getUserIds()[0]},
			_getServiceContext());
	}

	@Test(expected = MembershipPolicyException.class)
	public void testExceptionThrowWhenCreatingUserWithForbiddenSites()
		throws Exception {

		addForbiddenSites();

		MembershipPolicyTestUtil.addUser(
			null, null, getForbiddenSiteIds(), null);
	}

	@Test(expected = MembershipPolicyException.class)
	public void testExceptionThrowWhenRemoveUsersFromRequiredSite()
		throws Exception {

		addRequiredSites();

		User user = MembershipPolicyTestUtil.addUser(
			null, null, getRequiredSiteIds(), null);

		UserServiceUtil.unsetGroupUsers(
			getRequiredSiteIds()[0], new long[]{user.getUserId()},
			_getServiceContext());
	}

	@Test
	public void testLaunchPropagationWhenAddUsersToRequiredSite()
		throws Exception {

		addUsers();
		addRequiredSites();

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext();

		UserServiceUtil.addGroupUsers(
			getRequiredSiteIds()[0], getUserIds(), serviceContext);

		Assert.assertTrue(getPropagateMembershipMethodFlag());
	}

	@Test
	public void testLaunchPropagationWhenCreateUserWithRequiredSites()
		throws Exception {

		addRequiredSites();

		MembershipPolicyTestUtil.addUser(
			null, null, getRequiredSiteIds(), null);

		Assert.assertTrue(getPropagateMembershipMethodFlag());
	}

	@Test
	public void testLaunchVerificationWhenAddGroup() throws Exception {
		MembershipPolicyTestUtil.addGroup();

		Assert.assertTrue(getVerifyMethodFlag());
	}

	@Test
	public void testSuccessfulUnsetGroupUsersWhenRemoveUserFromStandardSite()
		throws Exception {

		addStandardSites();

		User user = MembershipPolicyTestUtil.addUser(
			null, null, getStandardSiteIds(), null);

		int expectedUserGroupCount =
			UserLocalServiceUtil.getGroupUsersCount(
				getStandardSiteIds()[0]) - 1;

		UserServiceUtil.unsetGroupUsers(
			getStandardSiteIds()[0], new long[]{user.getUserId()},
			_getServiceContext());

		int currentUserGroupCount = UserLocalServiceUtil.getGroupUsersCount(
			getStandardSiteIds()[0]);

		Assert.assertEquals(expectedUserGroupCount, currentUserGroupCount);
		Assert.assertTrue(getPropagateMembershipMethodFlag());
	}

	@Test
	public void testUpdateGroupLaunchVerification() throws Exception {
		Group group = MembershipPolicyTestUtil.addGroup();

		GroupServiceUtil.updateGroup(
			group.getGroupId(), group.getParentGroupId(), "TestSiteNewName",
			group.getDescription(), group.getType(), group.getFriendlyURL(),
			group.isActive(), _getServiceContext());

		Assert.assertTrue(getVerifyMethodFlag());
	}

	@Test
	public void testUpdateGroupTypeSettingsLaunchVerification()
		throws Exception {

		Group group = MembershipPolicyTestUtil.addGroup();

		String typeSettings = ServiceTestUtil.randomString(50);
		GroupServiceUtil.updateGroup(group.getGroupId(), typeSettings);

		Assert.assertTrue(getVerifyMethodFlag());
	}

	@Test
	public void testUserGrouCountWhenUpdateSitesFromUser() throws Exception {
		addUsers();
		addStandardSites();
		addRequiredSites();

		User user = UserLocalServiceUtil.getUser(getUserIds()[0]);

		Assert.assertEquals(currentGroups(1), user.getGroups().size());

		long[] userGroupIds = ArrayUtil.append(
			getStandardSiteIds(), getRequiredSiteIds(), user.getGroupIds());

		MembershipPolicyTestUtil.updateUser(
			user, null, null, userGroupIds, null,
			Collections.<UserGroupRole>emptyList());

		Assert.assertEquals(currentGroups(5), user.getGroups().size());

		MembershipPolicyTestUtil.updateUser(
			user, null, null, getRequiredSiteIds(), null,
			Collections.<UserGroupRole>emptyList());

		Assert.assertEquals(currentGroups(2), user.getGroups().size());
	}

	@Test
	public void testUserGroupCountWhenAddRequiredSiteOnUserUpdate()
		throws Exception {

		addUsers();
		addRequiredSites();

		int expectedUserGroupCount =
			UserLocalServiceUtil.getGroupUsersCount(
				getRequiredSiteIds()[0]) + addedUsers(1);

		User user = UserLocalServiceUtil.getUser(getUserIds()[0]);

		MembershipPolicyTestUtil.updateUser(
			user, null, null, new long[] {getRequiredSiteIds()[0]}, null,
			Collections.<UserGroupRole>emptyList());

		int currentUserGroupCount = UserLocalServiceUtil.getGroupUsersCount(
			getRequiredSiteIds()[0]);

		Assert.assertEquals(expectedUserGroupCount, currentUserGroupCount);
		Assert.assertTrue(getPropagateMembershipMethodFlag());
	}

	@Test
	public void testUserGroupCountWhenWhenDeleteRequiredSitesInUserUpdate()
		throws Exception {

		addUsers();
		addStandardSites();
		addRequiredSites();

		User user = UserLocalServiceUtil.getUser(getUserIds()[0]);

		Assert.assertEquals(currentGroups(1), user.getGroups().size());

		long[] userGroupIds = ArrayUtil.append(
			getStandardSiteIds(), getRequiredSiteIds(), user.getGroupIds());

		MembershipPolicyTestUtil.updateUser(
			user, null, null, userGroupIds, null,
			Collections.<UserGroupRole>emptyList());

		Assert.assertEquals(currentGroups(5), user.getGroups().size());

		MembershipPolicyTestUtil.updateUser(
			user, null, null, getStandardSiteIds(), null,
			Collections.<UserGroupRole>emptyList());

		Assert.assertEquals(currentGroups(4), user.getGroups().size());
	}

}