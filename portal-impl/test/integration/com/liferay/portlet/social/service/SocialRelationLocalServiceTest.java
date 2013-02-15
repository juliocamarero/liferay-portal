/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.social.service;

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portal.util.comparator.UserScreenNameComparator;
import com.liferay.portlet.social.model.SocialRelation;
import com.liferay.portlet.social.model.SocialRelationConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Brian Wing Shun Chan
 */
@ExecutionTestListeners(listeners = {EnvironmentExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class SocialRelationLocalServiceTest {

	@Before
	public void setUp() throws Exception {
		FinderCacheUtil.clearCache();

		_userIdsMap = new HashMap<String, long[]>();

		_relationIds = new ArrayList<Long>();

		for (String screenNamePrefix : _PREFIX) {
			long[] userIds = new long[_USERS_PREFIX_COUNT];

			for (int i = 1; i <= userIds.length; i++) {
				User user = ServiceTestUtil.addUser(
					screenNamePrefix + i, false, null);

				userIds[i - 1] = user.getUserId();
			}

			_userIdsMap.put(screenNamePrefix, userIds);
		}

		addRelationsWithBiType();

		addRelationsWithUniType();
	}

	@After
	public void tearDown() throws Exception {
		for (String screenNamePrefix : _PREFIX) {
			long[] userIds = _userIdsMap.get(screenNamePrefix);

			for (int i = 1; i <= userIds.length; i++) {
				UserLocalServiceUtil.deleteUser(userIds[i - 1]);
			}
		}

		for (Long relationId : _relationIds) {
			SocialRelation relation =
				SocialRelationLocalServiceUtil.getRelation(relationId);

			SocialRelationLocalServiceUtil.deleteRelation(relation);
		}
	}

	@Test
	public void testGetMutualRelations() throws Exception {
		User dlc1User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc1");

		User dlc2User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc2");

		// Do dlc1 and dlc2 have 4 mutual relations?

		List<User> users = UserLocalServiceUtil.getSocialUsers(
			dlc1User.getUserId(), dlc2User.getUserId(), QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, new UserScreenNameComparator(true));

		Assert.assertEquals(4, users.size());
	}

	@Test
	public void testGetMutualRelationsByBiType() throws Exception {
		User dlc1User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc1");

		User dlc2User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc2");

		// Do dlc1 and dlc2 have 3 mutual friends?

		List<User> users = UserLocalServiceUtil.getSocialUsers(
			dlc1User.getUserId(), dlc2User.getUserId(),
			SocialRelationConstants.TYPE_BI_FRIEND, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, new UserScreenNameComparator(true));

		Assert.assertEquals(3, users.size());

		Assert.assertEquals("dlc3", users.get(0).getScreenName());
		Assert.assertEquals("dlc4", users.get(1).getScreenName());
		Assert.assertEquals("dlc5", users.get(2).getScreenName());
	}

	@Test
	public void testGetMutualRelationsByUniType() throws Exception {
		User fra3User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra3");

		User fra4User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra4");

		User fra5User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra5");

		// Are fra3 and fra4 both children of fra1 and fra2?

		List<User> users = UserLocalServiceUtil.getSocialUsers(
			fra3User.getUserId(), fra4User.getUserId(),
			SocialRelationConstants.TYPE_UNI_CHILD, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, new UserScreenNameComparator(true));

		Assert.assertEquals(2, users.size());

		Assert.assertEquals("fra1", users.get(0).getScreenName());
		Assert.assertEquals("fra2", users.get(1).getScreenName());

		// Are fra3 and fra5 both children of fra1?

		users = UserLocalServiceUtil.getSocialUsers(
			fra3User.getUserId(), fra5User.getUserId(),
			SocialRelationConstants.TYPE_UNI_CHILD, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, new UserScreenNameComparator(true));

		Assert.assertEquals(1, users.size());

		Assert.assertEquals("fra1", users.get(0).getScreenName());
	}

	@Test
	public void testGetRelations() throws Exception {
		User dlc1User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc1");

		// Does dlc1 have 8 relations?

		List<User> users = UserLocalServiceUtil.getSocialUsers(
			dlc1User.getUserId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			new UserScreenNameComparator(true));

		Assert.assertEquals(8, users.size());

		Assert.assertEquals("dlc2", users.get(0).getScreenName());
		Assert.assertEquals("dlc3", users.get(1).getScreenName());
		Assert.assertEquals("dlc4", users.get(2).getScreenName());
		Assert.assertEquals("dlc5", users.get(3).getScreenName());
		Assert.assertEquals("dlc6", users.get(4).getScreenName());
		Assert.assertEquals("dlc7", users.get(5).getScreenName());
		Assert.assertEquals("dlc8", users.get(6).getScreenName());
		Assert.assertEquals("dlc9", users.get(7).getScreenName());
	}

	@Test
	public void testGetRelationsByBiType() throws Exception {
		User dlc1User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc1");

		User dlc2User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc2");

		User dlc3User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc3");

		// Does dlc1 have 7 friends?

		List<User> users = UserLocalServiceUtil.getSocialUsers(
			dlc1User.getUserId(), SocialRelationConstants.TYPE_BI_FRIEND,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			new UserScreenNameComparator(true));

		Assert.assertEquals(7, users.size());

		Assert.assertEquals("dlc2", users.get(0).getScreenName());
		Assert.assertEquals("dlc3", users.get(1).getScreenName());
		Assert.assertEquals("dlc4", users.get(2).getScreenName());
		Assert.assertEquals("dlc5", users.get(3).getScreenName());
		Assert.assertEquals("dlc6", users.get(4).getScreenName());
		Assert.assertEquals("dlc7", users.get(5).getScreenName());
		Assert.assertEquals("dlc8", users.get(6).getScreenName());

		// Is dlc1 a coworker of dlc9?

		users = UserLocalServiceUtil.getSocialUsers(
			dlc1User.getUserId(), SocialRelationConstants.TYPE_BI_COWORKER,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			new UserScreenNameComparator(true));

		Assert.assertEquals(1, users.size());

		Assert.assertEquals("dlc9", users.get(0).getScreenName());

		// Is dlc1 romantically involved with dlc2?

		users = UserLocalServiceUtil.getSocialUsers(
			dlc1User.getUserId(),
			SocialRelationConstants.TYPE_BI_ROMANTIC_PARTNER, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, new UserScreenNameComparator(true));

		Assert.assertEquals(1, users.size());

		Assert.assertEquals("dlc2", users.get(0).getScreenName());

		// Is dlc2 romantically involved with dlc1?

		users = UserLocalServiceUtil.getSocialUsers(
			dlc2User.getUserId(),
			SocialRelationConstants.TYPE_BI_ROMANTIC_PARTNER, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, new UserScreenNameComparator(true));

		Assert.assertEquals(1, users.size());

		Assert.assertEquals("dlc1", users.get(0).getScreenName());

		// Is dlc3 romantically involved with anyone?

		users = UserLocalServiceUtil.getSocialUsers(
			dlc3User.getUserId(),
			SocialRelationConstants.TYPE_BI_ROMANTIC_PARTNER, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, new UserScreenNameComparator(true));

		Assert.assertEquals(0, users.size());
	}

	@Test
	public void testGetRelationsByUniType() throws Exception {
		User fra1User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra1");

		User fra2User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra2");

		User fra3User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra3");

		User fra6User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra6");

		// Is fra1 a parent to 8 children?

		List<User> users = UserLocalServiceUtil.getSocialUsers(
			fra1User.getUserId(), SocialRelationConstants.TYPE_UNI_PARENT,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			new UserScreenNameComparator(true));

		Assert.assertEquals(8, users.size());

		Assert.assertEquals("fra2", users.get(0).getScreenName());
		Assert.assertEquals("fra3", users.get(1).getScreenName());
		Assert.assertEquals("fra4", users.get(2).getScreenName());
		Assert.assertEquals("fra5", users.get(3).getScreenName());
		Assert.assertEquals("fra6", users.get(4).getScreenName());
		Assert.assertEquals("fra7", users.get(5).getScreenName());
		Assert.assertEquals("fra8", users.get(6).getScreenName());
		Assert.assertEquals("fra9", users.get(7).getScreenName());

		// Is fra2 a parent of anyone?

		users = UserLocalServiceUtil.getSocialUsers(
			fra2User.getUserId(), SocialRelationConstants.TYPE_UNI_PARENT,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			new UserScreenNameComparator(true));

		Assert.assertEquals(0, users.size());

		// Is fra3 a child of anyone?

		users = UserLocalServiceUtil.getSocialUsers(
			fra3User.getUserId(), SocialRelationConstants.TYPE_UNI_CHILD,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			new UserScreenNameComparator(true));

		Assert.assertEquals(2, users.size());

		Assert.assertEquals("fra1", users.get(0).getScreenName());
		Assert.assertEquals("fra2", users.get(1).getScreenName());

		// Is fra6 a child of fra1?

		users = UserLocalServiceUtil.getSocialUsers(
			fra6User.getUserId(), SocialRelationConstants.TYPE_UNI_CHILD,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			new UserScreenNameComparator(true));

		Assert.assertEquals(0, users.size());
	}

	protected void addRelation(long userId1, long userId2, int relationType)
		throws Exception {

		SocialRelation relation = SocialRelationLocalServiceUtil.addRelation(
			userId1, userId2, relationType);

		_relationIds.add(relation.getRelationId());
	}

	protected void addRelationsWithBiType() throws Exception {
		User dlc1User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc1");

		User dlc2User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc2");

		User dlc3User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc3");

		User dlc4User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc4");

		User dlc5User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc5");

		User dlc6User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc6");

		User dlc7User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc7");

		User dlc8User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc8");

		User dlc9User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "dlc9");

		// Friend

		addRelation(
			dlc1User.getUserId(), dlc2User.getUserId(),
			SocialRelationConstants.TYPE_BI_FRIEND);

		addRelation(
			dlc1User.getUserId(), dlc3User.getUserId(),
			SocialRelationConstants.TYPE_BI_FRIEND);

		addRelation(
			dlc1User.getUserId(), dlc4User.getUserId(),
			SocialRelationConstants.TYPE_BI_FRIEND);

		addRelation(
			dlc1User.getUserId(), dlc5User.getUserId(),
			SocialRelationConstants.TYPE_BI_FRIEND);

		addRelation(
			dlc1User.getUserId(), dlc6User.getUserId(),
			SocialRelationConstants.TYPE_BI_FRIEND);

		addRelation(
			dlc1User.getUserId(), dlc7User.getUserId(),
			SocialRelationConstants.TYPE_BI_FRIEND);

		addRelation(
			dlc1User.getUserId(), dlc8User.getUserId(),
			SocialRelationConstants.TYPE_BI_FRIEND);

		// Friend

		addRelation(
			dlc2User.getUserId(), dlc3User.getUserId(),
			SocialRelationConstants.TYPE_BI_FRIEND);

		addRelation(
			dlc2User.getUserId(), dlc4User.getUserId(),
			SocialRelationConstants.TYPE_BI_FRIEND);

		addRelation(
			dlc2User.getUserId(), dlc5User.getUserId(),
			SocialRelationConstants.TYPE_BI_FRIEND);

		// Coworker

		addRelation(
			dlc1User.getUserId(), dlc9User.getUserId(),
			SocialRelationConstants.TYPE_BI_COWORKER);

		addRelation(
			dlc2User.getUserId(), dlc9User.getUserId(),
			SocialRelationConstants.TYPE_BI_COWORKER);

		// Romantic partner

		addRelation(
			dlc1User.getUserId(), dlc2User.getUserId(),
			SocialRelationConstants.TYPE_BI_ROMANTIC_PARTNER);
	}

	protected void addRelationsWithUniType() throws Exception {
		User fra1User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra1");

		User fra2User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra2");

		User fra3User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra3");

		User fra4User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra4");

		User fra5User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra5");

		User fra6User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra6");

		User fra7User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra7");

		User fra8User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra8");

		User fra9User = UserLocalServiceUtil.getUserByScreenName(
			TestPropsValues.getCompanyId(), "fra9");

		// Parent

		addRelation(
			fra1User.getUserId(), fra2User.getUserId(),
			SocialRelationConstants.TYPE_UNI_PARENT);

		addRelation(
			fra1User.getUserId(), fra3User.getUserId(),
			SocialRelationConstants.TYPE_UNI_PARENT);

		addRelation(
			fra1User.getUserId(), fra4User.getUserId(),
			SocialRelationConstants.TYPE_UNI_PARENT);

		addRelation(
			fra1User.getUserId(), fra5User.getUserId(),
			SocialRelationConstants.TYPE_UNI_PARENT);

		addRelation(
			fra1User.getUserId(), fra6User.getUserId(),
			SocialRelationConstants.TYPE_UNI_PARENT);

		addRelation(
			fra1User.getUserId(), fra7User.getUserId(),
			SocialRelationConstants.TYPE_UNI_PARENT);

		addRelation(
			fra1User.getUserId(), fra8User.getUserId(),
			SocialRelationConstants.TYPE_UNI_PARENT);

		addRelation(
			fra1User.getUserId(), fra9User.getUserId(),
			SocialRelationConstants.TYPE_UNI_PARENT);

		// Child

		addRelation(
			fra3User.getUserId(), fra1User.getUserId(),
			SocialRelationConstants.TYPE_UNI_CHILD);

		addRelation(
			fra3User.getUserId(), fra2User.getUserId(),
			SocialRelationConstants.TYPE_UNI_CHILD);

		addRelation(
			fra4User.getUserId(), fra1User.getUserId(),
			SocialRelationConstants.TYPE_UNI_CHILD);

		addRelation(
			fra4User.getUserId(), fra2User.getUserId(),
			SocialRelationConstants.TYPE_UNI_CHILD);

		addRelation(
			fra5User.getUserId(), fra1User.getUserId(),
			SocialRelationConstants.TYPE_UNI_CHILD);
	}

	private static final String[] _PREFIX = new String[] {"dlc", "fra"};
	private static final int _USERS_PREFIX_COUNT = 9;

	private List<Long> _relationIds;
	private Map<String, long[]> _userIdsMap;

}