/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.expando;

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Roberto Díaz
 */
public abstract class BasePortletExpandoTestCase {

	public static final int SEND_DEFAULT_EXPANDO_VALUES = 1;

	public static final int SEND_EMPTY_EXPANDO_MAP = 0;

	public static final int SEND_NEW_EXPANDO_VALUES = 2;

	@Before
	public void setUp() throws Exception {
		FinderCacheUtil.clearCache();

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(TestPropsValues.getUser());

		PermissionThreadLocal.setPermissionChecker(permissionChecker);
	}

	@After
	public void tearDown() throws Exception {
		ExpandoTableLocalServiceUtil.deleteTables(
			TestPropsValues.getCompanyId(), getClassName());

		try {
			deleteModel();
		}
		catch (Exception e) {
		}

		baseModel = null;
	}

	@Test
	public void testAddCustomFieldToModel() throws Exception {
		int initialCount = ExpandoTableLocalServiceUtil.getExpandoTablesCount();

		addExpandoAttribute();

		Assert.assertEquals(
			initialCount + 1,
			ExpandoTableLocalServiceUtil.getExpandoTablesCount());
	}

	@Test
	public void testAddModelWithExpandoValue() throws Exception {
		addExpandoAttribute();

		baseModel = addModel();

		ExpandoBridge expandoBridge = baseModel.getExpandoBridge();

		String value = (String)expandoBridge.getAttribute("column-1");

		Assert.assertEquals("value-1", value);
	}

	@Test
	public void testDeleteAllValuesWhenDeletingOriginalModel()
		throws Exception {

		int initialValuesCount =
			ExpandoValueLocalServiceUtil.getExpandoValuesCount();

		addExpandoAttribute();

		baseModel = addModel();

		updateBaseModel(SEND_NEW_EXPANDO_VALUES);

		updateBaseModel(SEND_DEFAULT_EXPANDO_VALUES);

		Assert.assertEquals(
			(initialValuesCount + getExpectedValuesCountAdded()),
			ExpandoValueLocalServiceUtil.getExpandoValuesCount());

		deleteModel();

		Assert.assertEquals(
			initialValuesCount,
			ExpandoValueLocalServiceUtil.getExpandoValuesCount());
	}

	@Test
	public void testDeleteSingleValueWhenDeletingSingleModel()
		throws Exception {

		int initialValuesCount =
			ExpandoValueLocalServiceUtil.getExpandoValuesCount();

		addExpandoAttribute();

		baseModel = addModel();

		updateBaseModel(SEND_NEW_EXPANDO_VALUES);

		ClassedModel modelVersion = getModelVersion();

		updateBaseModel(SEND_DEFAULT_EXPANDO_VALUES);

		Assert.assertEquals(
			initialValuesCount + 3,
			ExpandoValueLocalServiceUtil.getExpandoValuesCount());

		deleteModelVersion(modelVersion);

		Assert.assertEquals(
			initialValuesCount + 2,
			ExpandoValueLocalServiceUtil.getExpandoValuesCount());
	}

	@Test
	public void testEqualValuesWhenUpdatingModelWithEmptyExpandoMap()
		throws Exception {

		addExpandoAttribute();

		baseModel = addModel();

		ExpandoBridge expandoBridge = baseModel.getExpandoBridge();

		String expectedValue = (String)expandoBridge.getAttribute("column-1");

		baseModel = updateBaseModel(SEND_EMPTY_EXPANDO_MAP);

		expandoBridge = baseModel.getExpandoBridge();

		String value = (String)expandoBridge.getAttribute("column-1");

		Assert.assertEquals(expectedValue, value);
	}

	@Test
	public void testIsDifferentValueWhenUpdatingModelWithNewExpandoValue()
		throws Exception {

		addExpandoAttribute();

		baseModel = addModel();

		ExpandoBridge expandoBridge = baseModel.getExpandoBridge();

		String expectedValue = (String)expandoBridge.getAttribute("column-1");

		updateBaseModel(SEND_NEW_EXPANDO_VALUES);

		baseModel = getModelVersion();

		expandoBridge = baseModel.getExpandoBridge();

		String value = (String)expandoBridge.getAttribute("column-1");

		Assert.assertNotEquals(expectedValue, value);
	}

	protected void addExpandoAttribute() throws Exception {
		String modelResource = getClassName();

		ExpandoBridge expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(
			TestPropsValues.getCompanyId(), modelResource, 0);

		expandoBridge.addAttribute("column-1", ExpandoColumnConstants.STRING);
	}

	protected abstract ClassedModel addModel() throws Exception;

	protected abstract void deleteModel()
		throws Exception;

	protected abstract void deleteModelVersion(ClassedModel modelVersion)
		throws Exception;

	protected abstract String getClassName();

	protected Map<String, Serializable> getExpandoMap(int action) {

		Map<String, Serializable> expandoMap =
			new HashMap<String, Serializable>();

		if (action == SEND_DEFAULT_EXPANDO_VALUES) {
			expandoMap.put("column-1", "value-1");
		}
		else if (action == SEND_NEW_EXPANDO_VALUES) {
			expandoMap.put("column-1", "value-2");
		}

		return expandoMap;
	}

	protected abstract int getExpectedValuesCountAdded();

	protected abstract ClassedModel getModelVersion() throws Exception;

	protected abstract ClassedModel updateBaseModel(int action)
		throws Exception;

	protected ClassedModel baseModel;

}