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

package com.liferay.staging.bar.web.theme.contributor;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.template.TemplateContextContributor;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.staging.bar.web.product.navigation.control.menu.StagingProductNavigationControlMenuEntry;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(
	immediate = true,
	property = {"type=" + TemplateContextContributor.TYPE_THEME},
	service = TemplateContextContributor.class
)
public class StagingBarTemplateContextContributor
	implements TemplateContextContributor {

	@Override
	public void prepare(
		Map<String, Object> contextObjects, HttpServletRequest request) {

		try {
			if (_stagingProductNavigationControlMenuEntry.isShow(request)) {
				StringBuilder sb = new StringBuilder();

				sb.append(
					GetterUtil.getString(contextObjects.get("bodyCssClass")));
				sb.append(StringPool.SPACE);
				sb.append("has-staging-bar");

				contextObjects.put("bodyCssClass", sb.toString());
			}
		}
		catch (PortalException pe) {
			pe.printStackTrace();
		}
	}

	@Reference(unbind = "-")
	protected void setCustomizationSettingsProductNavigationControlMenuEntry(
		StagingProductNavigationControlMenuEntry
			stagingProductNavigationControlMenuEntry) {

		this._stagingProductNavigationControlMenuEntry =
			stagingProductNavigationControlMenuEntry;
	}

	private StagingProductNavigationControlMenuEntry
		_stagingProductNavigationControlMenuEntry;

}