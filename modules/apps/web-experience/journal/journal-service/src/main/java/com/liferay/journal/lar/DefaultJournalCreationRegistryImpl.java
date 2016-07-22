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

package com.liferay.journal.lar;

import com.liferay.journal.configuration.JournalGroupServiceConfiguration;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Pavel Savinov
 */
@Component(immediate = true, service = JournalCreationStrategyRegistry.class)
public class DefaultJournalCreationRegistryImpl
	implements JournalCreationStrategyRegistry {

	public JournalCreationStrategy getJournalCreationStrategy(long groupId) {
		try {
			JournalGroupServiceConfiguration journalGroupServiceConfiguration =
				ConfigurationProviderUtil.getGroupConfiguration(
					JournalGroupServiceConfiguration.class, groupId);

			JournalCreationStrategy journalCreationStrategy =
				_journalCreationStrategies.get(
					journalGroupServiceConfiguration.larCreationStrategy());

			if ((journalCreationStrategy != null) && _log.isDebugEnabled()) {
				Class<?> clazz = journalCreationStrategy.getClass();

				_log.debug("Return " + clazz.getName());
			}

			return journalCreationStrategy;
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return null;
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		unbind = "unregisterJournalCreationStrategy"
	)
	public void registerJournalCreationStrategy(
		JournalCreationStrategy journalCreationStrategy) {

		Class<?> clazz = journalCreationStrategy.getClass();

		_journalCreationStrategies.put(
			clazz.getName(), journalCreationStrategy);
	}

	public void unregisterJournalCreationStrategy(
		JournalCreationStrategy journalCreationStrategy) {

		Class<?> clazz = journalCreationStrategy.getClass();

		_journalCreationStrategies.remove(
			clazz.getName(), journalCreationStrategy);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DefaultJournalCreationRegistryImpl.class);

	private final Map<String, JournalCreationStrategy>
		_journalCreationStrategies = new ConcurrentHashMap<>();

}