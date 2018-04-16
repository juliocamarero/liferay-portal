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

import com.liferay.meris.MerisRuleType;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.util.MapUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eduardo Garcia
 */
@Component
public class TestMerisAgeRuleType
	implements MerisRuleType, Comparable<TestMerisAgeRuleType> {

	public static final String ID = TestMerisAgeRuleType.class.getName();

	@Override
	public int compareTo(TestMerisAgeRuleType assetCategoryMerisRule) {
		String merisRuleId = getMerisRuleTypeId();

		return merisRuleId.compareTo(
			assetCategoryMerisRule.getMerisRuleTypeId());
	}

	@Override
	public String getDescription(Locale locale) {
		return ResourceActionsUtil.getModelResource(
			locale, getMerisRuleTypeId() + ".description");
	}

	@Override
	public Object getMerisRuleTypeDefaultSetting(String key) {
		return null;
	}

	@Override
	public Map<String, Object> getMerisRuleTypeDefaultSettings() {
		Map<String, Object> merisRuleTypeDefaultSetting = new HashMap<>();

		merisRuleTypeDefaultSetting.put("assetCategoryId", 0);

		return Collections.unmodifiableMap(merisRuleTypeDefaultSetting);
	}

	@Override
	public String getMerisRuleTypeId() {
		return ID;
	}

	@Override
	public String getName(Locale locale) {
		return ResourceActionsUtil.getModelResource(
			locale, getMerisRuleTypeId());
	}

	@Override
	public boolean matches(
		Map<String, Object> context, Map<String, Object> merisRuleSettings) {

		int contextAge = MapUtil.getInteger(context, "age");

		int contextAgeMax = MapUtil.getInteger(context, "ageMax");
		int contextAgeMin = MapUtil.getInteger(context, "ageMin");

		if ((contextAge <= contextAgeMax) && (contextAge >= contextAgeMin)) {
			return true;
		}

		return false;
	}

	private static final Map<String, Object> _merisRuleTypeDefaultSettings;

	static {
		_merisRuleTypeDefaultSettings = new HashMap();

		_merisRuleTypeDefaultSettings.put("ageMax", 0);
		_merisRuleTypeDefaultSettings.put("ageMin", 100);
	}

}