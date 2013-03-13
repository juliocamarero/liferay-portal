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
import com.liferay.portal.security.membershippolicy.util.RoleTestUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portal.util.UserTestUtil;

/**
 * @author Roberto Díaz
 */

public abstract class BaseSiteMembershipPolicyTestCase {

	public static int addNewUser(int i) {
		return i;
	}

	public static int currentGroups(int i) {
		return i;
	}

	public static long[] getForbiddenRoleIds() {
		return _forbiddenRoleIds;
	}

	public static long[] getForbiddenSiteIds() {
		return _forbiddenSiteIds;
	}

	public static Group getGroup() {
		return _group;
	}

	public static boolean getPropagateMembershipMethodFlag() {
		return _propagateMembershipMethodFlag;
	}

	public static boolean getPropagateRolesMethodFlag() {
		return _propagateRolesMethodFlag;
	}

	public static long[] getRequiredRoleIds() {
		return _requiredRoleIds;
	}

	public static long[] getRequiredSiteIds() {
		return _requiredSiteIds;
	}

	public static long[] getStandardRoleIds() {
		return _standardRoleIds;
	}

	public static long[] getStandardSiteIds() {
		return _standardSiteIds;
	}

	public static long[] getUserIds() {
		return _userIds;
	}

	public static boolean getVerifyMethodFlag() {
		return _verifyMethodFlag;
	}

	public static void setGroup(Group group) {
		_group = group;
	}

	public static void setPropagateMembershipMethodFlag(
		boolean propagateMembershipMethodFlag) {

		_propagateMembershipMethodFlag = propagateMembershipMethodFlag;
	}

	public static void setPropagateRolesMethodFlag(
		boolean propagateRolesMethodFlag) {

		_propagateRolesMethodFlag = propagateRolesMethodFlag;
	}

	public static void setVerifyMethodFlag(boolean verifyMethodFlag) {
		_verifyMethodFlag = verifyMethodFlag;
	}

	protected ServiceContext _getServiceContext() throws Exception {
		return ServiceTestUtil.getServiceContext();
	}

	protected void addForbiddenRoles() throws Exception {
		_forbiddenRoleIds[0] = RoleTestUtil.addGroupRole(_group.getGroupId());
		_forbiddenRoleIds[1] = RoleTestUtil.addGroupRole(_group.getGroupId());
	}

	protected void addForbiddenSites() throws Exception {
		_forbiddenSiteIds[0] = GroupTestUtil.addGroup(
			"ForbiddenTestSite1" + ServiceTestUtil.randomString()).getGroupId();
		_forbiddenSiteIds[1] = GroupTestUtil.addGroup(
			"ForbiddenTestSite2" + ServiceTestUtil.randomString()).getGroupId();
	}

	protected void addRequiredRoles() throws Exception {
		_requiredRoleIds[0] = RoleTestUtil.addGroupRole(_group.getGroupId());
		_requiredRoleIds[1] = RoleTestUtil.addGroupRole(_group.getGroupId());
	}

	protected void addRequiredSites() throws Exception {
		_requiredSiteIds[0] = GroupTestUtil.addGroup(
			"RequiredTestSite1" + ServiceTestUtil.randomString()).getGroupId();
		_requiredSiteIds[1] = GroupTestUtil.addGroup(
			"RequiredTestSite2" + ServiceTestUtil.randomString()).getGroupId();
	}

	protected void addStandardRoles() throws Exception {
		_standardRoleIds[0] = RoleTestUtil.addGroupRole(_group.getGroupId());
		_standardRoleIds[1] = RoleTestUtil.addGroupRole(_group.getGroupId());
	}

	protected void addStandardSites() throws Exception {
		_standardSiteIds[0] = GroupTestUtil.addGroup(
			"StandardTestSite1" + ServiceTestUtil.randomString()).getGroupId();
		_standardSiteIds[1] = GroupTestUtil.addGroup(
			"StandardTestSite2" + ServiceTestUtil.randomString()).getGroupId();
	}

	protected void addUsers() throws Exception {
		_userIds[0] = UserTestUtil.addUser(
			"UserTest1" + ServiceTestUtil.randomString(),
			_group.getGroupId()).getUserId();
		_userIds[1] = UserTestUtil.addUser(
			"UserTest2" + ServiceTestUtil.randomString(),
			_group.getGroupId()).getUserId();
	}

	private static long[] _forbiddenRoleIds = new long[2];
	private static long[] _forbiddenSiteIds = new long[2];
	private static Group _group;
	private static boolean _propagateMembershipMethodFlag = false;
	private static boolean _propagateRolesMethodFlag = false;
	private static long[] _requiredRoleIds = new long[2];
	private static long[] _requiredSiteIds = new long[2];
	private static long[] _standardRoleIds = new long[2];
	private static long[] _standardSiteIds = new long[2];
	private static long[] _userIds = new long[2];
	private static boolean _verifyMethodFlag;

}