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

import com.liferay.meris.MerisRule;
import com.liferay.meris.MerisSegment;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Eduardo Garcia
 */
public class TestMerisSegment
	implements MerisSegment, Comparable<TestMerisSegment> {

	public TestMerisSegment(
		String merisSegmentId, String scopeId, Map<Locale, String> nameMap,
		Map<Locale, String> descriptionMap, List<MerisRule> merisRules) {

		_merisSegmentId = merisSegmentId;
		_scopeId = scopeId;
		_nameMap = nameMap;
		_descriptionMap = descriptionMap;
		_merisRules = merisRules;
	}

	@Override
	public int compareTo(TestMerisSegment testMerisSegment) {
		String merisSegmentId = getMerisSegmentId();

		return merisSegmentId.compareTo(testMerisSegment.getMerisSegmentId());
	}

	@Override
	public String getDescription(Locale locale) {
		return _descriptionMap.get(locale);
	}

	@Override
	public List<MerisRule> getMerisRules(
		int start, int end, Comparator<MerisRule> comparator) {

		Stream<MerisRule> stream = _merisRules.stream();

		return stream.collect(
			Collectors.collectingAndThen(
				Collectors.toList(),
				list -> {
					list.sort(comparator);

					return ListUtil.subList(list, start, end);
				}));
	}

	@Override
	public String getMerisSegmentId() {
		return _merisSegmentId;
	}

	@Override
	public String getName(Locale locale) {
		return _nameMap.get(locale);
	}

	@Override
	public String getScopeId() {
		return _scopeId;
	}

	private final Map<Locale, String> _descriptionMap;
	private final List<MerisRule> _merisRules;
	private final String _merisSegmentId;
	private final Map<Locale, String> _nameMap;
	private final String _scopeId;

}