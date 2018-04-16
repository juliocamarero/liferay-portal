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

import java.util.Locale;
import java.util.Map;

/**
 * @author Eduardo Garcia
 */
public class TestMerisRule implements MerisRule, Comparable<TestMerisRule> {

	public TestMerisRule(
		String merisRuleId, String merisRuleTypeId, Map<Locale, String> nameMap,
		Map<Locale, String> descriptionMap,
		Map<String, Object> merisRuleTypeSettings) {

		_merisRuleId = merisRuleId;
		_merisRuleTypeId = merisRuleTypeId;
		_nameMap = nameMap;
		_descriptionMap = descriptionMap;
		_merisRuleTypeSettings = merisRuleTypeSettings;
	}

	@Override
	public int compareTo(TestMerisRule testMerisRule) {
		String merisRuleId = getMerisRuleId();

		return merisRuleId.compareTo(testMerisRule.getMerisRuleId());
	}

	@Override
	public String getDescription(Locale locale) {
		return _descriptionMap.get(locale);
	}

	@Override
	public String getMerisRuleId() {
		return _merisRuleId;
	}

	@Override
	public String getMerisRuleTypeId() {
		return _merisRuleTypeId;
	}

	@Override
	public Object getMerisRuleTypeSetting(String key) {
		return _merisRuleTypeSettings.get(key);
	}

	@Override
	public Map<String, Object> getMerisRuleTypeSettings() {
		return _merisRuleTypeSettings;
	}

	@Override
	public String getName(Locale locale) {
		return _nameMap.get(locale);
	}

	private final Map<Locale, String> _descriptionMap;
	private final String _merisRuleId;
	private final String _merisRuleTypeId;
	private final Map<String, Object> _merisRuleTypeSettings;
	private final Map<Locale, String> _nameMap;

}