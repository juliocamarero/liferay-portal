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

package com.liferay.journal.settings.internal;

import com.liferay.journal.settings.JournalGroupServiceSettingsOverride;
import com.liferay.portal.kernel.settings.TypedSettings;
import com.liferay.portal.kernel.util.LocalizationUtil;

/**
 * @author Juergen Kappler
 */
public class JournalGroupServiceSettingsOverrideImpl
	implements JournalGroupServiceSettingsOverride {

	public JournalGroupServiceSettingsOverrideImpl(
		TypedSettings typedSettings) {

		_typedSettings = typedSettings;
	}

	@Override
	public String emailArticleAddedBodyXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap("emailArticleAddedBody"),
			"emailArticleAddedBody");
	}

	@Override
	public String emailArticleAddedSubjectXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap("emailArticleAddedSubject"),
			"emailArticleAddedSubject");
	}

	@Override
	public String emailArticleApprovalDeniedBodyXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap(
				"emailArticleApprovalDeniedBody"),
			"emailArticleApprovalDeniedBody");
	}

	@Override
	public String emailArticleApprovalDeniedSubjectXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap(
				"emailArticleApprovalDeniedSubject"),
			"emailArticleApprovalDeniedSubject");
	}

	@Override
	public String emailArticleApprovalGrantedBodyXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap(
				"emailArticleApprovalGrantedBody"),
			"emailArticleApprovalGrantedBody");
	}

	@Override
	public String emailArticleApprovalGrantedSubjectXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap(
				"emailArticleApprovalGrantedSubject"),
			"emailArticleApprovalGrantedSubject");
	}

	@Override
	public String emailArticleApprovalRequestedBodyXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap(
				"emailArticleApprovalRequestedBody"),
			"emailArticleApprovalRequestedBody");
	}

	@Override
	public String emailArticleApprovalRequestedSubjectXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap(
				"emailArticleApprovalRequestedSubject"),
			"emailArticleApprovalRequestedSubject");
	}

	@Override
	public String emailArticleMovedFromFolderBodyXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap(
				"emailArticleMovedFromFolderBody"),
			"emailArticleMovedFromFolderBody");
	}

	@Override
	public String emailArticleMovedFromFolderSubjectXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap(
				"emailArticleMovedFromFolderSubject"),
			"emailArticleMovedFromFolderSubject");
	}

	@Override
	public String emailArticleMovedToFolderBodyXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap(
				"emailArticleMovedToFolderBody"),
			"emailArticleMovedToFolderBody");
	}

	@Override
	public String emailArticleMovedToFolderSubjectXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap(
				"emailArticleMovedToFolderSubject"),
			"emailArticleMovedToFolderSubject");
	}

	@Override
	public String emailArticleReviewBodyXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap("emailArticleReviewBody"),
			"emailArticleReviewBody");
	}

	@Override
	public String emailArticleReviewSubjectXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap("emailArticleReviewSubject"),
			"emailArticleReviewSubject");
	}

	@Override
	public String emailArticleUpdatedBodyXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap("emailArticleUpdatedBody"),
			"emailArticleUpdatedBody");
	}

	@Override
	public String emailArticleUpdatedSubjectXml() {
		return LocalizationUtil.getXml(
			_typedSettings.getLocalizedValuesMap("emailArticleUpdatedSubject"),
			"emailArticleUpdatedSubject");
	}

	private final TypedSettings _typedSettings;

}