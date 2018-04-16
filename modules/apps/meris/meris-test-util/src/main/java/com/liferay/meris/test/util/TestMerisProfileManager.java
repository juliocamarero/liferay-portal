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

import com.liferay.meris.MerisProfileManager;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eduardo Garcia
 */
@Component(immediate = true, service = MerisProfileManager.class)
public class TestMerisProfileManager
	implements MerisProfileManager<TestMerisProfile> {

	@Override
	public TestMerisProfile addMerisProfile(TestMerisProfile testMerisProfile) {
		_merisProfiles.put(
			testMerisProfile.getMerisProfileId(), testMerisProfile);

		return testMerisProfile;
	}

	@Override
	public TestMerisProfile deleteMerisProfile(String merisProfileId) {
		TestMerisProfile testMerisProfile = getMerisProfile(merisProfileId);

		_merisProfiles.remove(testMerisProfile.getMerisProfileId());

		return testMerisProfile;
	}

	@Override
	public void deleteMerisProfiles(String scopeId) {
		Collection<TestMerisProfile> testMerisProfiles =
			_merisProfiles.values();

		testMerisProfiles.removeIf(
			merisProfile -> scopeId.equals(merisProfile.getScopeId()));
	}

	@Override
	public TestMerisProfile getMerisProfile(String merisProfileId) {
		return _merisProfiles.get(merisProfileId);
	}

	@Override
	public List<TestMerisProfile> getMerisProfiles(
		String scopeId, int start, int end,
		Comparator<TestMerisProfile> comparator) {

		Collection<TestMerisProfile> values = _merisProfiles.values();

		Stream<TestMerisProfile> stream = values.stream();

		return stream.filter(
			demoMerisProfile -> scopeId.equals(demoMerisProfile.getScopeId())
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
	public TestMerisProfile updateMerisProfile(
		TestMerisProfile testMerisProfile) {

		_merisProfiles.put(
			testMerisProfile.getMerisProfileId(), testMerisProfile);

		return testMerisProfile;
	}

	private final Map<String, TestMerisProfile> _merisProfiles =
		new ConcurrentHashMap<>();

}