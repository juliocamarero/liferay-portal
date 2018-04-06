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

package com.liferay.mobile.device.rules.web.internal.upgrade;

import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Cristina González
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class MDRServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"com.liferay.mobile.device.rules.service", "0.0.1", "1.0.0",
			new DummyUpgradeStep());

		registry.register(
			"com.liferay.mobile.device.rules.service", "1.0.0", "1.0.1",
			new MDRActionUpgrade(
				"com.liferay.portal.mobile.device.rulegroup.action.impl",
				"com.liferay.mobile.device.rules.rule.group.action"),
			new MDRRuleUpgrade(
				"com.liferay.portal.mobile.device.rulegroup.rule.impl." +
					"SimpleRuleHandler",
				"com.liferay.mobile.device.rules.rule.group.rule." +
					"SimpleRuleHandler"));
	}

}