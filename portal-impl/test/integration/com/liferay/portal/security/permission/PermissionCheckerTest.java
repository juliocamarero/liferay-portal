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
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
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
import com.liferay.portal.util.OrganizationTestUtil;
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
	public void testIsCompanyAdminWithCompanyAdmin() throws Exception {
		Assert.assertTrue(
			getPermissionChecker(TestPropsValues.getUser()).isCompanyAdmin());
	}

	@Test
	public void testIsCompanyAdminWithRegularUser() throws Exception {
		User user = UserTestUtil.addUser();

		Assert.assertFalse(getPermissionChecker(user).isCompanyAdmin());
	}

	@Test
	public void testIsGroupAdminWithCompanyAdmin() throws Exception {
		Assert.assertTrue(
			getPermissionChecker(
				TestPropsValues.getUser()).isGroupAdmin(_group.getGroupId()));
	}

	@Test
	public void testIsGroupAdminWithGroupAdmin() throws Exception {
		User user = UserTestUtil.addGroupAdminUser(_group);

		Assert.assertTrue(
			getPermissionChecker(user).isGroupAdmin(_group.getGroupId()));
	}

	@Test
	public void testIsGroupAdminWithRegularUser() throws Exception {
		User user = UserTestUtil.addUser();

		Assert.assertFalse(
			getPermissionChecker(user).isGroupAdmin(_group.getGroupId()));
	}

	@Test
	public void testIsGroupMemberWithGroupMember() throws Exception {
		User user = UserTestUtil.addUser();

		UserLocalServiceUtil.addGroupUser(
			_group.getGroupId(), user.getUserId());

		Assert.assertTrue(
			getPermissionChecker(user).isGroupMember(_group.getGroupId()));
	}

	@Test
	public void testIsGroupMemberWithNonGroupMember() throws Exception {
		User user = UserTestUtil.addUser();

		Assert.assertFalse(
			getPermissionChecker(user).isGroupMember(_group.getGroupId()));
	}

	@Test
	public void testIsGroupOwnerWithCompanyAdmin() throws Exception {
		Assert.assertTrue(
			getPermissionChecker(
				TestPropsValues.getUser()).isGroupOwner(_group.getGroupId()));
	}

	@Test
	public void testIsGroupOwnerWithGroupAdmin() throws Exception {
		User user = UserTestUtil.addGroupAdminUser(_group);

		Assert.assertFalse(
			getPermissionChecker(user).isGroupOwner(_group.getGroupId()));
	}

	@Test
	public void testIsGroupOwnerWithOwnerUser() throws Exception {
		User user = UserTestUtil.addGroupOwnerUser(_group);

		Assert.assertTrue(
			getPermissionChecker(user).isGroupOwner(_group.getGroupId()));
	}

	@Test
	public void testIsGroupOwnerWithRegularUser() throws Exception {
		User user = UserTestUtil.addUser(
			_group.getGroupId(), LocaleUtil.getDefault());

		Assert.assertFalse(
			getPermissionChecker(user).isGroupOwner(_group.getGroupId()));
	}

	@Test
	public void testIsOmniAdminWithAdministratorRoleUser() throws Exception {
		User user = UserTestUtil.addOmniAdmin();

		Assert.assertFalse(getPermissionChecker(user).isOmniadmin());
	}

	@Test
	public void testIsOmniAdminWithCompanyAdmin() throws Exception {
		Assert.assertFalse(
			getPermissionChecker(TestPropsValues.getUser()).isOmniadmin());
	}

	@Test
	public void testIsOmniAdminWithGroupAdmin() throws Exception {
		User user = UserTestUtil.addGroupAdminUser(_group);

		Assert.assertFalse(getPermissionChecker(user).isOmniadmin());
	}

	@Test
	public void testIsOmniAdminWithRegularUser() throws Exception {
		User user = UserTestUtil.addUser();

		Assert.assertFalse(getPermissionChecker(user).isOmniadmin());
	}

	@Test
	public void testIsOrganizationAdminWithCompanyAdmin() throws Exception {
		Organization organization = OrganizationTestUtil.addOrganization();

		Assert.assertTrue(
			getPermissionChecker(TestPropsValues.getUser()).isOrganizationAdmin(
				organization.getOrganizationId()));
	}

	@Test
	public void testIsOrganizationAdminWithGroupAdmin() throws Exception {
		Organization organization = OrganizationTestUtil.addOrganization();

		User user = UserTestUtil.addGroupAdminUser(organization.getGroup());

		Assert.assertFalse(
			getPermissionChecker(user).isOrganizationAdmin(
				organization.getOrganizationId()));
	}

	@Test
	public void testIsOrganizationAdminWithOrganizationAdmin()
		throws Exception {

		Organization organization = OrganizationTestUtil.addOrganization();

		User user = UserTestUtil.addOrganizationAdminUser(organization);

		Assert.assertTrue(
			getPermissionChecker(user).isOrganizationAdmin(
				organization.getOrganizationId()));
	}

	@Test
	public void testIsOrganizationAdminWithRegularUser() throws Exception {
		Organization organization = OrganizationTestUtil.addOrganization();

		User user = UserTestUtil.addUser();

		Assert.assertFalse(
			getPermissionChecker(user).isOrganizationAdmin(
				organization.getOrganizationId()));
	}

	@Test
	public void testIsOrganizationOwnerWithCompanyAdmin() throws Exception {
		Organization organization = OrganizationTestUtil.addOrganization();

		Assert.assertTrue(
			getPermissionChecker(TestPropsValues.getUser()).isOrganizationOwner(
				organization.getOrganizationId()));
	}

	@Test
	public void testIsOrganizationOwnerWithGroupAdmin() throws Exception {
		Organization organization = OrganizationTestUtil.addOrganization();

		User user = UserTestUtil.addGroupAdminUser(organization.getGroup());

		Assert.assertFalse(
			getPermissionChecker(user).isOrganizationOwner(
				organization.getOrganizationId()));
	}

	@Test
	public void testIsOrganizationOwnerWithOrganizationAdmin()
		throws Exception {

		Organization organization = OrganizationTestUtil.addOrganization();

		User user = UserTestUtil.addOrganizationAdminUser(organization);

		Assert.assertFalse(
			getPermissionChecker(user).isOrganizationOwner(
				organization.getOrganizationId()));
	}

	@Test
	public void testIsOrganizationOwnerWithRegularUser() throws Exception {
		Organization organization = OrganizationTestUtil.addOrganization();

		User user = UserTestUtil.addUser();

		Assert.assertFalse(
			getPermissionChecker(user).isOrganizationOwner(
				organization.getOrganizationId()));
	}

	@Test
	public void testIsReviewerWithCompanyAdminUser() throws Exception {
		User user = TestPropsValues.getUser();

		Assert.assertTrue(
			getPermissionChecker(user).isReviewer(
				user.getCompanyId(), _group.getGroupId()));
	}

	@Test
	public void testIsReviewerWithGroupAdminUser() throws Exception {
		User user = UserTestUtil.addGroupAdminUser(_group);

		Assert.assertTrue(
			getPermissionChecker(user).isReviewer(user.getCompanyId(),
			_group.getGroupId()));
	}

	@Test
	public void testIsReviewerWithRegularUser() throws Exception {
		User user = UserTestUtil.addUser();

		Assert.assertFalse(
			getPermissionChecker(user).isReviewer(user.getCompanyId(),
			_group.getGroupId()));
	}

	@Test
	public void testIsReviewerWithReviewerUser() throws Exception {
		User user = UserTestUtil.addUser();

		Role reviewerRole = addReviewerRole();

		UserLocalServiceUtil.setRoleUsers(
			reviewerRole.getRoleId(), new long[] {user.getUserId()});

		Assert.assertTrue(
			getPermissionChecker(user).isReviewer(user.getCompanyId(),
			_group.getGroupId()));
	}

	protected Role addReviewerRole() throws Exception {
		Role reviewerRole = RoleLocalServiceUtil.fetchRole(
			_group.getCompanyId(), RoleConstants.PORTAL_CONTENT_REVIEWER);

		if (reviewerRole == null) {
			reviewerRole = RoleServiceUtil.addRole(
				null, 0, RoleConstants.PORTAL_CONTENT_REVIEWER,
				ServiceTestUtil.randomLocaleStringMap(),
				ServiceTestUtil.randomLocaleStringMap(),
				RoleConstants.TYPE_REGULAR, ServiceTestUtil.randomString(),
				ServiceTestUtil.getServiceContext());
		}

		return reviewerRole;
	}

	protected PermissionChecker getPermissionChecker(User user)
		throws Exception {

		return PermissionCheckerFactoryUtil.create(user);
	}

	private Group _group;

}