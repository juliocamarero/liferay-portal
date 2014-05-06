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

package com.liferay.portal.model.impl;

import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.Group;
import com.liferay.portal.util.PropsValues;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Sampsa Sohlman
 */
@PrepareForTest(PrefsPropsUtil.class)
@RunWith(PowerMockRunner.class)
public class GroupImplTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		mockStatic(PrefsPropsUtil.class);

		when(
			PrefsPropsUtil.getBoolean(_companyId, PropsKeys.TRASH_ENABLED)).
			thenReturn(true);

		when(
			PrefsPropsUtil.getInteger(
				_companyId, PropsKeys.TRASH_ENTRIES_MAX_AGE)).thenReturn(
					PropsValues.TRASH_ENTRIES_MAX_AGE);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testSetTypeSettingsCreate() {
		Group group = createGroup();

		UnicodeProperties unicodeProperties = createTypeSettingsProperties(
			"trashEntriesMaxAge", "1440");

		group.setTypeSettingsProperties(unicodeProperties);

		Assert.assertEquals(1440L, group.getTrashEntriesMaxAge());
	}

	@Test
	public void testSetTypeSettingsRemoveByUpdate() {
		Group group = createGroup();

		UnicodeProperties unicodeProperties = createTypeSettingsProperties(
			"trashEntriesMaxAge", "1440");

		group.setTypeSettingsProperties(unicodeProperties);

		unicodeProperties = new UnicodeProperties(false);

		group.setTypeSettingsProperties(unicodeProperties);

		Assert.assertEquals(-1L, group.getTrashEntriesMaxAge());
	}

	@Test
	public void testSetTypeSettingsRemoveByUpdateToDefault() {
		Group group = createGroup();

		UnicodeProperties unicodeProperties = createTypeSettingsProperties(
			"trashEntriesMaxAge", "1440");

		group.setTypeSettingsProperties(unicodeProperties);

		Assert.assertEquals(1440L, group.getTrashEntriesMaxAge());

		unicodeProperties = createTypeSettingsProperties(
			"trashEntriesMaxAge",
			String.valueOf(PropsValues.TRASH_ENTRIES_MAX_AGE));

		group.setTypeSettingsProperties(unicodeProperties);

		Assert.assertEquals(-1, group.getTrashEntriesMaxAge());
	}

	private Group createGroup() {
		Group group = new GroupImpl();
		group.setCompanyId(_companyId);
		return group;
	}

	private UnicodeProperties createTypeSettingsProperties(
		String key, String value) {

		UnicodeProperties unicodeProperties = new UnicodeProperties(true);
		unicodeProperties.setProperty(key, value);
		return unicodeProperties;
	}

	private long _companyId = 10L;

}