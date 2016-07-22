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

package com.liferay.journal.util.impl;

import com.liferay.journal.configuration.JournalServiceConfiguration;
import com.liferay.journal.exception.FolderNameException;
import com.liferay.journal.util.JournalValidator;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;

/**
 * @author David Zhang
 */
@Component(immediate = true, service = JournalValidator.class)
public final class JournalValidatorImpl implements JournalValidator {

	@Override
	public boolean isValidName(String name) {
		if (Validator.isNull(name)) {
			return false;
		}

		String[] charactersBlacklist = {};

		try {
			long companyId = CompanyThreadLocal.getCompanyId();

			JournalServiceConfiguration journalServiceConfiguration =
				ConfigurationProviderUtil.getCompanyConfiguration(
					JournalServiceConfiguration.class, companyId);

			charactersBlacklist =
				journalServiceConfiguration.charactersblacklist();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		for (String blacklistChar : charactersBlacklist) {
			if (name.contains(blacklistChar)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void validateFolderName(String folderName)
		throws FolderNameException {

		if (!isValidName(folderName)) {
			throw new FolderNameException(folderName);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JournalValidatorImpl.class);

}