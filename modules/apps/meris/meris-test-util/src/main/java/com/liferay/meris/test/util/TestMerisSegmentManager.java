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

package com.liferay.meris.test.util;

import com.liferay.meris.MerisProfile;
import com.liferay.meris.MerisProfileManager;
import com.liferay.meris.MerisRule;
import com.liferay.meris.MerisRuleType;
import com.liferay.meris.MerisRuleTypeManager;
import com.liferay.meris.MerisSegmentManager;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eduardo Garcia
 */
@Component(immediate = true, service = MerisSegmentManager.class)
public class TestMerisSegmentManager
	implements MerisSegmentManager <TestMerisSegment, TestMerisProfile> {

	@Override
	public TestMerisSegment addMerisSegment(TestMerisSegment testMerisSegment) {
		_merisSegments.put(
			testMerisSegment.getMerisSegmentId(), testMerisSegment);

		return testMerisSegment;
	}

	@Override
	public TestMerisSegment deleteMerisSegment(String merisSegmentId) {
		TestMerisSegment testMerisSegment = getMerisSegment(merisSegmentId);

		_merisSegments.remove(testMerisSegment.getMerisSegmentId());

		return testMerisSegment;
	}

	@Override
	public void deleteMerisSegments(String scopeId) {
		Collection<TestMerisSegment> testMerisSegments =
			_merisSegments.values();

		testMerisSegments.removeIf(
			merisSegment -> scopeId.equals(merisSegment.getScopeId()));
	}

	@Override
	public List<TestMerisProfile> getMerisProfiles(
		String merisSegmentId, Map<String, Object> context, int start, int end,
		Comparator<TestMerisProfile> comparator) {

		TestMerisSegment testMerisSegment = getMerisSegment(merisSegmentId);

		if (testMerisSegment == null) {
			return Collections.emptyList();
		}

		List<TestMerisProfile> testMerisProfiles =
			_merisProfileManager.getMerisProfiles(
				testMerisSegment.getScopeId(), QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null);

		Stream<TestMerisProfile> stream = testMerisProfiles.stream();

		return stream.filter(
			merisProfile -> matches(
				merisProfile.getMerisProfileId(), merisSegmentId, context)
		).collect(
			Collectors.collectingAndThen(
				Collectors.toList(),
				list -> {
					list.sort(comparator);

					return ListUtil.subList(list, start, end);
				})
		);
	}

	@Override
	public TestMerisSegment getMerisSegment(String merisSegmentId) {
		return _merisSegments.get(merisSegmentId);
	}

	@Override
	public List<TestMerisSegment> getMerisSegments(
		String scopeId, int start, int end,
		Comparator<TestMerisSegment> comparator) {

		Collection<TestMerisSegment> values = _merisSegments.values();

		Stream<TestMerisSegment> stream = values.stream();

		return stream.filter(
			testMerisSegment -> scopeId.equals(testMerisSegment.getScopeId())
		).collect(
			Collectors.collectingAndThen(
				Collectors.toList(),
				list -> {
					list.sort(comparator);

					return ListUtil.subList(list, start, end);
				})
		);
	}

	@Override
	public List<TestMerisSegment> getMerisSegments(
		String scopeId, String merisProfileId, String merisSegmentId,
		Map<String, Object> context, int start, int end,
		Comparator<TestMerisSegment> comparator) {

		MerisProfile merisProfile = _merisProfileManager.getMerisProfile(
			merisProfileId);

		if (merisProfile == null) {
			return Collections.emptyList();
		}

		TestMerisSegment testMerisSegment = getMerisSegment(merisSegmentId);

		if (testMerisSegment == null) {
			return Collections.emptyList();
		}

		List<TestMerisSegment> testMerisSegments = getMerisSegments(
			scopeId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		Stream<TestMerisSegment> stream = testMerisSegments.stream();

		return stream.filter(
			merisSegment -> matches(
				merisProfileId, merisSegment.getMerisSegmentId(), context)
		).collect(
			Collectors.collectingAndThen(
				Collectors.toList(),
				list -> {
					list.sort(comparator);

					return ListUtil.subList(list, start, end);
				})
		);
	}

	@Override
	public boolean matches(
		String merisProfileId, String merisSegmentId,
		Map<String, Object> context) {

		MerisProfile merisProfile = _merisProfileManager.getMerisProfile(
			merisProfileId);

		if (merisProfile == null) {
			return false;
		}

		TestMerisSegment testMerisSegment = getMerisSegment(merisSegmentId);

		if (testMerisSegment == null) {
			return false;
		}

		List<MerisRule> merisRules = testMerisSegment.getMerisRules(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		Stream<MerisRule> stream = merisRules.stream();

		return stream.allMatch(
			merisRule -> {
				MerisRuleType merisRuleType =
					_merisRuleTypeManager.getMerisRuleType(
						merisRule.getMerisRuleTypeId());

				if (merisRuleType == null) {
					return false;
				}

				return merisRuleType.matches(
					context, merisRule.getMerisRuleTypeSettings());
			});
	}

	@Override
	public TestMerisSegment updateMerisSegment(
		TestMerisSegment testMerisSegment) {

		_merisSegments.put(
			testMerisSegment.getMerisSegmentId(), testMerisSegment);

		return testMerisSegment;
	}

	@Reference
	private MerisProfileManager _merisProfileManager;

	@Reference
	private MerisRuleTypeManager _merisRuleTypeManager;

	private final Map<String, TestMerisSegment> _merisSegments =
		new ConcurrentHashMap<>();

}