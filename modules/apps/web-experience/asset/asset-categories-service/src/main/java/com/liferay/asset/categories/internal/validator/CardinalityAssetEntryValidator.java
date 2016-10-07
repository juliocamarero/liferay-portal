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

package com.liferay.asset.categories.internal.validator;

import com.liferay.asset.categories.validator.CardinalityAssetEntryValidatorHelper;
import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.exception.AssetCategoryException;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.asset.kernel.validator.AssetEntryValidator;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Juan Fernández
 */
@Component(
	immediate = true, property = {"model.class.name=*"},
	service = AssetEntryValidator.class
)
public class CardinalityAssetEntryValidator implements AssetEntryValidator {

	@Override
	public void validate(
			long groupId, String className, long classPK, long classTypePK,
			long[] categoryIds, String[] entryNames)
		throws PortalException {

		CardinalityAssetEntryValidatorHelper
		cardinalityAssetEntryValidatorHelper =
			_getCardinalityAssetEntryValidatorHelper(className);

		if ((cardinalityAssetEntryValidatorHelper != null) &&
			!cardinalityAssetEntryValidatorHelper.isValidable(
				groupId, className, classPK, classTypePK, categoryIds,
				entryNames)) {

			return;
		}

		validate(groupId, className, classTypePK, categoryIds, entryNames);
	}

	@Override
	public void validate(
			long groupId, String className, long classTypePK,
			long[] categoryIds, String[] entryNames)
		throws PortalException {

		List<AssetVocabulary> assetVocabularies =
			_assetVocabularyLocalService.getGroupVocabularies(groupId, false);

		Group group = _groupLocalService.getGroup(groupId);

		if (!group.isCompany()) {
			Group companyGroup = _groupLocalService.fetchCompanyGroup(
				group.getCompanyId());

			if (companyGroup != null) {
				assetVocabularies = ListUtil.copy(assetVocabularies);

				assetVocabularies.addAll(
					_assetVocabularyLocalService.getGroupVocabularies(
						companyGroup.getGroupId()));
			}
		}

		long classNameId = _classNameLocalService.getClassNameId(className);

		if (isCategorizable(classNameId)) {
			for (AssetVocabulary assetVocabulary : assetVocabularies) {
				validate(
					classNameId, classTypePK, categoryIds, assetVocabulary);
			}
		}
	}

	protected boolean isCategorizable(long classNameId) {
		String className = PortalUtil.getClassName(classNameId);

		AssetRendererFactory<?> assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				className);

		if ((assetRendererFactory == null) ||
			!assetRendererFactory.isCategorizable()) {

			return false;
		}

		return true;
	}

	@Reference(unbind = "-")
	protected void setAssetVocabularyLocalService(
		AssetVocabularyLocalService assetVocabularyLocalService) {

		_assetVocabularyLocalService = assetVocabularyLocalService;
	}

	@Reference(unbind = "-")
	protected void setClassNameLocalService(
		ClassNameLocalService classNameLocalService) {

		_classNameLocalService = classNameLocalService;
	}

	@Reference(unbind = "-")
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	protected void validate(
			long classNameId, long classTypePK, final long[] categoryIds,
			AssetVocabulary assetVocabulary)
		throws PortalException {

		if (!assetVocabulary.isAssociatedToClassNameIdAndClassTypePK(
				classNameId, classTypePK)) {

			return;
		}

		if (assetVocabulary.isMissingRequiredCategory(
				classNameId, classTypePK, categoryIds)) {

			throw new AssetCategoryException(
				assetVocabulary, AssetCategoryException.AT_LEAST_ONE_CATEGORY);
		}

		if (!assetVocabulary.isMultiValued() &&
			assetVocabulary.hasMoreThanOneCategorySelected(categoryIds)) {

			throw new AssetCategoryException(
				assetVocabulary, AssetCategoryException.TOO_MANY_CATEGORIES);
		}
	}

	private CardinalityAssetEntryValidatorHelper
		_getCardinalityAssetEntryValidatorHelper(String className) {

		if (Validator.isNotNull(className)) {
			CardinalityAssetEntryValidatorHelper
				cardinalityAssetEntryValidatorHelper =
					_serviceTrackerMap.getService(className);

			if (cardinalityAssetEntryValidatorHelper != null) {
				return cardinalityAssetEntryValidatorHelper;
			}
		}

		return null;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, CardinalityAssetEntryValidatorHelper.class,
			"model.class.name");
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	private AssetVocabularyLocalService _assetVocabularyLocalService;
	private ClassNameLocalService _classNameLocalService;
	private GroupLocalService _groupLocalService;

	private ServiceTrackerMap<String, CardinalityAssetEntryValidatorHelper>
	_serviceTrackerMap;

}