/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetCategoryConstants;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergio González
 */
public class AssetEntryTestUtil {

	public static long addCategory(
			String title, long vocabularyId, ServiceContext serviceContext)
		throws Exception {

		Locale locale = LocaleUtil.getDefault();

		Map<Locale, String> categoryTitleMap = new HashMap<Locale, String>();

		categoryTitleMap.put(locale, title);

		Map<Locale, String> categoryDescriptionMap =
			new HashMap<Locale, String>();

		categoryDescriptionMap.put(locale, StringPool.BLANK);

		AssetCategory assetCategory = AssetCategoryLocalServiceUtil.addCategory(
			TestPropsValues.getUserId(),
			AssetCategoryConstants.DEFAULT_PARENT_CATEGORY_ID, categoryTitleMap,
			categoryDescriptionMap, vocabularyId, null, serviceContext);

		return assetCategory.getCategoryId();
	}

	public static long addVocabulary(
			String title, ServiceContext serviceContext)
		throws Exception {

		Locale locale = LocaleUtil.getDefault();

		Map<Locale, String> vocabularyTitleMap = new HashMap<Locale, String>();

		vocabularyTitleMap.put(locale, title);

		Map<Locale, String> vocabularyDescriptionMap =
			new HashMap<Locale, String>();

		vocabularyDescriptionMap.put(locale, StringPool.BLANK);

		AssetVocabulary assetVocabulary =
			AssetVocabularyLocalServiceUtil.addVocabulary(
				TestPropsValues.getUserId(), title, vocabularyTitleMap,
				vocabularyDescriptionMap, null, serviceContext);

		return  assetVocabulary.getVocabularyId();
	}

	public static AssetEntryQuery buildAssetEntryQueryCategories(
		AssetEntryQuery assetEntryQuery, long[] assetCategoryIds, boolean any,
		boolean not) {

		if (any && not) {
			assetEntryQuery.setNotAnyCategoryIds(assetCategoryIds);
		}
		else if (!any && not) {
			assetEntryQuery.setNotAllCategoryIds(assetCategoryIds);
		}
		else if (any && !not) {
			assetEntryQuery.setAnyCategoryIds(assetCategoryIds);
		}
		else {
			assetEntryQuery.setAllCategoryIds(assetCategoryIds);
		}

		return assetEntryQuery;
	}

	public static AssetEntryQuery buildAssetEntryQueryTags(
			AssetEntryQuery assetEntryQuery, long[] assetTagIds, boolean any,
		boolean not) {

		if (any && not) {
			assetEntryQuery.setNotAnyTagIds(assetTagIds);
		}
		else if (!any && not) {
			assetEntryQuery.setNotAllTagIds(assetTagIds);
		}
		else if (any && !not) {
			assetEntryQuery.setAnyTagIds(assetTagIds);
		}
		else {
			assetEntryQuery.setAllTagIds(assetTagIds);
		}

		return assetEntryQuery;
	}

}