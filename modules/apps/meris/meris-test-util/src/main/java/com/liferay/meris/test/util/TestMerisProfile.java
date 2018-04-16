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

import java.util.Map;

/**
 * @author Eduardo Garcia
 */
public class TestMerisProfile
	implements MerisProfile, Comparable<TestMerisProfile> {

	public TestMerisProfile(
		String profileId, String scopeId, Map<String, Object> attributes) {

		_merisProfileId = profileId;
		_scopeId = scopeId;
		_attributes = attributes;
	}

	@Override
	public int compareTo(TestMerisProfile assetCategoryMerisProfile) {
		String merisProfileId = getMerisProfileId();

		return merisProfileId.compareTo(
			assetCategoryMerisProfile.getMerisProfileId());
	}

	@Override
	public Object getAttribute(String key) {
		return _attributes.get(key);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return _attributes;
	}

	@Override
	public String getMerisProfileId() {
		return _merisProfileId;
	}

	@Override
	public String getScopeId() {
		return _scopeId;
	}

	private final Map<String, Object> _attributes;
	private final String _merisProfileId;
	private final String _scopeId;

}