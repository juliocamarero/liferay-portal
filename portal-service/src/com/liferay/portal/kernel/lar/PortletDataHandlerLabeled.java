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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Locale;

/**
 * @author Raymond Augé
 */
public class PortletDataHandlerLabeled extends PortletDataHandlerBoolean {

	public PortletDataHandlerLabeled(
		String controlName, String controlLabel, boolean defaultState,
		boolean disabled) {

		super(StringPool.BLANK, controlName, defaultState, disabled);

		_controlLabel = controlLabel;
	}

	public String getControlLabel() {
		return _controlLabel;
	}

	@Override
	public String getHelpMessage(Locale locale, String action) {
		return LanguageUtil.get(
			locale, action + "-" + _controlLabel + "-help", StringPool.BLANK);
	}

	private String _controlLabel;

}