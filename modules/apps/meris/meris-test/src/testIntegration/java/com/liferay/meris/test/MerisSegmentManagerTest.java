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
import com.liferay.meris.MerisRule;
import com.liferay.meris.MerisSegment;
import com.liferay.meris.MerisSegmentManager;
import com.liferay.meris.test.util.TestMerisAgeRuleType;
import com.liferay.meris.test.util.TestMerisProfile;
import com.liferay.meris.test.util.TestMerisRule;
import com.liferay.meris.test.util.TestMerisSegment;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
public class MerisSegmentManagerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() {
		_merisScopeId = RandomTestUtil.randomString();

		Map<String, Object> merisRuleTypeSettings = new HashMap();

		merisRuleTypeSettings.put("ageMax", 40);
		merisRuleTypeSettings.put("ageMin", 30);

		MerisRule merisRule = _createMerisRule(
			TestMerisAgeRuleType.ID, merisRuleTypeSettings);

		MerisSegment merisSegment = _addMerisSegment(
			_merisScopeId, Collections.singletonList(merisRule));

		_merisSegmentId = merisSegment.getMerisSegmentId();

		Map<String, Object> attributes = new HashMap();

		attributes.put("age", 20);

		MerisProfile merisProfile = _addMerisProfile(_merisScopeId, attributes);

		_merisProfileId = merisProfile.getMerisProfileId();
	}

	@After
	public void tearDown() {
		_merisSegmentManager.deleteMerisSegments(_merisScopeId);
	}

	@Test
	public void testAddMerisSegment() {
		MerisSegment merisSegment = _createMerisSegment(_merisScopeId, null);

		_merisSegmentManager.addMerisSegment(merisSegment);

		Assert.assertNotNull(
			"Meris segment was not added",
			_merisSegmentManager.getMerisSegment(
				merisSegment.getMerisSegmentId()));
	}

	@Test
	public void testDeleteMerisSegment() {
		MerisSegment merisSegment = _addMerisSegment(_merisScopeId, null);

		_merisSegmentManager.deleteMerisSegment(
			merisSegment.getMerisSegmentId());

		Assert.assertNull(
			"Meris segment was not deleted",
			_merisSegmentManager.getMerisSegment(
				merisSegment.getMerisSegmentId()));
	}

	@Test
	public void testGetMerisProfileMerisSegments() throws Exception {
		Comparator<MerisSegment> merisSegmentNameComparator =
			Comparator.comparing(s -> s.getName(Locale.getDefault()));

		List<MerisSegment> merisSegments =
			_merisSegmentManager.getMerisSegments(
				_merisScopeId, _merisProfileId, _merisSegmentId,
				new HashMap<>(), 0, 1, merisSegmentNameComparator);

		Assert.assertFalse(
			"Meris profile does not contain a meris segment",
			merisSegments.isEmpty());
	}

	@Test
	public void testGetMerisProfiles() throws Exception {
		List merisProfiles = _merisSegmentManager.getMerisProfiles(
			_merisSegmentId, new HashMap<>(), QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);

		Assert.assertFalse(
			"Meris segment does not contain a meris profile",
			merisProfiles.isEmpty());
	}

	@Test
	public void testGetMerisSegment() {
		MerisSegment merisSegment = _merisSegmentManager.getMerisSegment(
			_merisSegmentId);

		Assert.assertNotNull("Meris segment was not found", merisSegment);

		Comparator<MerisRule> merisRuleNameComparator = Comparator.comparing(
			s -> s.getName(Locale.getDefault()));

		List<MerisRule> merisRules = merisSegment.getMerisRules(
			0, 1, merisRuleNameComparator);

		Assert.assertFalse(
			"Meris segment does not contain a meris rule",
			merisRules.isEmpty());
	}

	@Test
	public void testGetMerisSegments() {
		Comparator<MerisSegment> merisSegmentNameComparator =
			Comparator.comparing(s -> s.getName(Locale.getDefault()));

		List<MerisSegment> merisSegments =
			_merisSegmentManager.getMerisSegments(
				_merisScopeId, 0, 1, merisSegmentNameComparator);

		Assert.assertFalse(
			"No meris segments were found", merisSegments.isEmpty());
	}

	@Test
	public void testMatches() {
		Assert.assertTrue(
			"Meris profile does not match the meris segment",
			_merisSegmentManager.matches(
				_merisProfileId, _merisSegmentId, new HashMap<>()));
	}

	@Test
	public void testUpdateMerisSegment() {
		MerisSegment merisSegment = _addMerisSegment(_merisScopeId, null);

		MerisSegment updatedMerisSegment = new TestMerisSegment(
			merisSegment.getMerisSegmentId(), merisSegment.getScopeId(),
			RandomTestUtil.randomLocaleStringMap(),
			RandomTestUtil.randomLocaleStringMap(), null);

		_merisSegmentManager.updateMerisSegment(updatedMerisSegment);

		merisSegment = _merisSegmentManager.getMerisSegment(
			merisSegment.getMerisSegmentId());

		Assert.assertEquals(
			"Meris segment was not updated",
			updatedMerisSegment.getName(Locale.getDefault()),
			merisSegment.getName(Locale.getDefault()));
	}

	private MerisProfile _addMerisProfile(
		String scopeId, Map<String, Object> attributes) {

		MerisProfile merisProfile = new TestMerisProfile(
			RandomTestUtil.randomString(), scopeId, attributes);

		return _merisProfileManager.addMerisProfile(merisProfile);
	}

	private MerisSegment _addMerisSegment(
		String scopeId, List<MerisRule> merisRules) {

		MerisSegment merisSegment = _createMerisSegment(scopeId, merisRules);

		return _merisSegmentManager.addMerisSegment(merisSegment);
	}

	private MerisRule _createMerisRule(
		String merisRuleTypeId, Map<String, Object> merisRuleTypeSettings) {

		return new TestMerisRule(
			RandomTestUtil.randomString(), merisRuleTypeId,
			RandomTestUtil.randomLocaleStringMap(),
			RandomTestUtil.randomLocaleStringMap(), merisRuleTypeSettings);
	}

	private MerisSegment _createMerisSegment(
		String scopeId, List<MerisRule> merisRules) {

		return new TestMerisSegment(
			RandomTestUtil.randomString(), scopeId,
			RandomTestUtil.randomLocaleStringMap(),
			RandomTestUtil.randomLocaleStringMap(), merisRules);
	}

	@Inject
	private static MerisProfileManager _merisProfileManager;

	@Inject
	private static MerisSegmentManager _merisSegmentManager;

	private String _merisProfileId;
	private String _merisScopeId;
	private String _merisSegmentId;

}