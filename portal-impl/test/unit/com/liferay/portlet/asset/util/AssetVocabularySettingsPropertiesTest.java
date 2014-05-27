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

package com.liferay.portlet.asset.util;

import com.liferay.portlet.asset.model.AssetCategoryConstants;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author José Manuel Navarro
 */
public class AssetVocabularySettingsPropertiesTest {

	@Test
	public void testHasAssetRendererFactoryAll() {
		_settingProperties = getSettingProperties(
			true, AssetCategoryConstants.ALL_CLASS_NAME_IDS, true);

		Assert.assertTrue(_settingProperties.hasAssetRendererFactory(1));

		Assert.assertTrue(_settingProperties.hasAssetRendererFactory(2));
	}

	@Test
	public void testHasAssetRendererFactoryClassName() {
		_settingProperties = getSettingProperties(true, 1, true);

		Assert.assertTrue(_settingProperties.hasAssetRendererFactory(1));

		Assert.assertFalse(_settingProperties.hasAssetRendererFactory(2));
	}

	@Test
	public void testIsMultiValuedFalse() {
		_settingProperties = getSettingProperties(false, 1, true);

		Assert.assertFalse(_settingProperties.isMultiValued());
	}

	@Test
	public void testIsMultiValuedTrue() {
		_settingProperties = getSettingProperties(true, 1, true);

		Assert.assertTrue(_settingProperties.isMultiValued());
	}

	@Test
	public void testIsRequiredAllIsNotRequired() {
		_settingProperties = getSettingProperties(
			true, AssetCategoryConstants.ALL_CLASS_NAME_IDS, false);

		Assert.assertFalse(
			_settingProperties.isAssetRendererFactoryRequired(1));

		Assert.assertFalse(
			_settingProperties.isAssetRendererFactoryRequired(2));
	}

	@Test
	public void testIsRequiredAllIsRequired() {
		_settingProperties = getSettingProperties(
			true, AssetCategoryConstants.ALL_CLASS_NAME_IDS, true);

		Assert.assertTrue(_settingProperties.isAssetRendererFactoryRequired(1));

		Assert.assertTrue(_settingProperties.isAssetRendererFactoryRequired(2));
	}

	@Test
	public void testIsRequiredClassNameIsNotRequired() {
		_settingProperties = getSettingProperties(true, 1, false);

		Assert.assertFalse(
			_settingProperties.isAssetRendererFactoryRequired(1));

		Assert.assertFalse(
			_settingProperties.isAssetRendererFactoryRequired(2));
	}

	@Test
	public void testIsRequiredClassNameIsRequired() {
		_settingProperties = getSettingProperties(true, 1, true);

		Assert.assertTrue(_settingProperties.isAssetRendererFactoryRequired(1));

		Assert.assertFalse(
			_settingProperties.isAssetRendererFactoryRequired(2));
	}

	protected AssetVocabularySettingsProperties getSettingProperties(
		boolean multiValued, long className, boolean isRequired) {

		long[] classNames = new long[]{className};
		boolean[] required = new boolean[]{isRequired};

		return getSettingProperties(multiValued, classNames, required);
	}

	protected AssetVocabularySettingsProperties getSettingProperties(
		boolean multiValued, long[] classNames, boolean[] required) {

		AssetVocabularySettingsProperties settingsProperties =
			new AssetVocabularySettingsProperties();

		settingsProperties.setMultiValued(multiValued);

		settingsProperties.addAssetRendererFactories(classNames, required);

		return settingsProperties;
	}

	private AssetVocabularySettingsProperties _settingProperties;

}