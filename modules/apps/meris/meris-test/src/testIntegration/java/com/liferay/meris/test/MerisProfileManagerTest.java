/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.meris.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.meris.MerisProfile;
import com.liferay.meris.MerisProfileManager;
import com.liferay.meris.test.util.TestMerisProfile;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eduardo Garcia
 */
@RunWith(Arquillian.class)
public class MerisProfileManagerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() {
		_merisScopeId = RandomTestUtil.randomString();

		MerisProfile merisProfile = _addMerisProfile(
			_merisScopeId, new HashMap());

		_merisProfileId = merisProfile.getMerisProfileId();
	}

	@After
	public void tearDown() {
		_merisProfileManager.deleteMerisProfiles(_merisScopeId);
	}

	@Test
	public void testAddMerisProfile() {
		MerisProfile merisProfile = _createMerisProfile(_merisScopeId, null);

		_merisProfileManager.addMerisProfile(merisProfile);

		Assert.assertNotNull(
			"Meris segment was not added",
			_merisProfileManager.getMerisProfile(
				merisProfile.getMerisProfileId()));
	}

	@Test
	public void testDeleteMerisProfile() {
		MerisProfile merisProfile = _addMerisProfile(_merisScopeId, null);

		_merisProfileManager.deleteMerisProfile(
			merisProfile.getMerisProfileId());

		Assert.assertNull(
			"Meris segment was not deleted",
			_merisProfileManager.getMerisProfile(
				merisProfile.getMerisProfileId()));
	}

	@Test
	public void testGetMerisProfile() {
		Assert.assertNotNull(
			"Meris profile was not found",
			_merisProfileManager.getMerisProfile(_merisProfileId));
	}

	@Test
	public void testGetMerisProfiles() {
		Comparator<MerisProfile> merisProfileNameComparator =
			Comparator.comparing(p -> p.getMerisProfileId());

		List<MerisProfile> merisProfiles =
			_merisProfileManager.getMerisProfiles(
				_merisScopeId, 0, 1, merisProfileNameComparator);

		Assert.assertFalse(
			"No meris segments were found", merisProfiles.isEmpty());
	}

	private MerisProfile _addMerisProfile(
		String scopeId, Map<String, Object> attributes) {

		MerisProfile merisProfile = new TestMerisProfile(
			RandomTestUtil.randomString(), scopeId, attributes);

		return _merisProfileManager.addMerisProfile(merisProfile);
	}

	private MerisProfile _createMerisProfile(
		String scopeId, Map<String, Object> attributes) {

		return new TestMerisProfile(
			RandomTestUtil.randomString(), scopeId, attributes);
	}

	@Inject
	private static MerisProfileManager _merisProfileManager;

	private String _merisProfileId;
	private String _merisScopeId;

}