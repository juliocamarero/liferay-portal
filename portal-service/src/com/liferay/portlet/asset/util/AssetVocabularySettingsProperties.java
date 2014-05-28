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

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portlet.asset.model.AssetCategoryConstants;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>
 * This class is intended to handle setting properties for asset vocabulary
 * model instead of handling the properties using generic UnicodeProperties
 * class.
 * </p>
 *
 * @author José Manuel Navarro
 */
public class AssetVocabularySettingsProperties extends UnicodeProperties {

	public AssetVocabularySettingsProperties() {
		super(true);
	}

	public AssetVocabularySettingsProperties(String properties) {
		super(true);
		fastLoad(properties);
	}

	public long[] getAssetRendererFactoryClassNameIds() {
		String propertyValue = getProperty(KEY_SELECTED_CLASSNAMES);

		return StringUtil.split(propertyValue, 0L);
	}

	public long[] getRequiredAssetRendererFactoryClassNameIds() {
		String propertyValue = getProperty(KEY_REQUIRED_CLASSNAMES);

		return StringUtil.split(propertyValue, 0L);
	}

	public boolean hasAssetRendererFactory(long classNameId) {
		return isClassNameIdSpecified(
			classNameId, getAssetRendererFactoryClassNameIds());
	}

	public boolean isAssetRendererFactoryRequired(long classNameId) {
		return isClassNameIdSpecified(
			classNameId, getRequiredAssetRendererFactoryClassNameIds());
	}

	public boolean isMultiValued() {
		String propertyValue = getProperty(KEY_MULTIVALUED);

		return GetterUtil.getBoolean(propertyValue, true);
	}

	public void setAssetRendererFactories(
		long[] classNameIds, boolean[] areRequired) {

		Set<Long> selectedClassNameIds = new LinkedHashSet<Long>();
		Set<Long> requiredClassNameIds = new LinkedHashSet<Long>();

		for (int index : indexes) {
			long classNameId = ParamUtil.getLong(
				actionRequest, "classNameId" + index);

			boolean required = ParamUtil.getBoolean(
				actionRequest, "required" + index);

			if (classNameId == AssetCategoryConstants.ALL_CLASS_NAME_IDS) {
				selectedClassNameIds.clear();
				selectedClassNameIds.add(classNameId);

				if (required) {
					requiredClassNameIds.clear();
					requiredClassNameIds.add(classNameId);
				}

				break;
			}
			else {
				selectedClassNameIds.add(classNameId);

				if (required) {
					requiredClassNameIds.add(classNameId);
				}
			}
		}

		settingsProperties.setProperty(
			"selectedClassNameIds", StringUtil.merge(selectedClassNameIds));
		settingsProperties.setProperty(
			"requiredClassNameIds", StringUtil.merge(requiredClassNameIds));
	}

	public void setMultiValued(boolean multiValued) {
		setProperty(KEY_MULTIVALUED, String.valueOf(multiValued));
	}

	protected boolean isClassNameIdSpecified(
		long classNameId, long[] classNameIds) {

		if (classNameIds.length == 0) {
			return false;
		}

		if ((classNameIds[0] !=
				AssetCategoryConstants.ALL_CLASS_NAME_IDS) &&
			!ArrayUtil.contains(classNameIds, classNameId)) {

			return false;
		}

		return true;
	}

	private static final String KEY_MULTIVALUED = "multiValued";

	private static final String KEY_REQUIRED_CLASSNAMES =
		"requiredClassNameIds";

	private static final String KEY_SELECTED_CLASSNAMES =
		"selectedClassNameIds";

}