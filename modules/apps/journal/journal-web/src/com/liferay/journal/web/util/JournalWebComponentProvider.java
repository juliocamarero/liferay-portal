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

package com.liferay.journal.web.util;

import com.liferay.journal.configuration.JournalGroupServiceConfiguration;
import com.liferay.portal.kernel.settings.SettingsFactory;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Juergen Kappler
 */
@Component(immediate = true)
public class JournalWebComponentProvider {

	public static JournalWebComponentProvider getJournalWebComponentProvider() {
		return _journalWebComponentProvider;
	}

	public JournalGroupServiceConfiguration
		getJournalGroupServiceConfiguration() {

		return _journalGroupServiceConfiguration;
	}

	public SettingsFactory getSettingsFactory() {
		return _settingsFactory;
	}

	@Activate
	protected void activate() {
		_journalWebComponentProvider = this;
	}

	@Deactivate
	protected void deactivate() {
		_journalWebComponentProvider = null;
	}

	@Reference
	protected void setJournalGroupServiceConfiguration(
		JournalGroupServiceConfiguration journalGroupServiceConfiguration) {

		_journalGroupServiceConfiguration = journalGroupServiceConfiguration;
	}

	@Reference(unbind = "-")
	protected void setSettingsFactory(SettingsFactory settingsFactory) {
		_settingsFactory = settingsFactory;
	}

	protected void unsetJournalGroupServiceConfiguration(
		JournalGroupServiceConfiguration journalGroupServiceConfiguration) {

		_journalGroupServiceConfiguration = null;
	}

	private static JournalWebComponentProvider _journalWebComponentProvider;

	private JournalGroupServiceConfiguration _journalGroupServiceConfiguration;
	private SettingsFactory _settingsFactory;

}