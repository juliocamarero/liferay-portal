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

package com.liferay.journal.web.portlet.configuration.icon;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.configuration.icon.BasePortletConfigurationIcon;
import com.liferay.portal.kernel.webdav.WebDAVUtil;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portlet.PortletURLFactoryUtil;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

/**
 * @author Eudaldo Alonso
 */
public class StructuresPortletConfigurationIcon
	extends BasePortletConfigurationIcon {

	public StructuresPortletConfigurationIcon(PortletRequest portletRequest) {
		super(portletRequest);
	}

	@Override
	public String getMessage() {
		return "structures";
	}

	@Override
	public String getURL() {
		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			portletDisplay.getId());

		PortletURL portletURL = PortletURLFactoryUtil.create(
			portletRequest,
			PortletProviderUtil.getPortletId(
				DDMStructure.class.getName(), PortletProvider.Action.VIEW),
			themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

		portletURL.setParameter("mvcPath", "/view.jsp");
		portletURL.setParameter(
			"groupId", String.valueOf(themeDisplay.getScopeGroupId()));
		portletURL.setParameter("redirect", themeDisplay.getURLCurrent());
		portletURL.setParameter(
			"refererPortletName", JournalPortletKeys.JOURNAL);
		portletURL.setParameter(
			"refererWebDAVToken", WebDAVUtil.getStorageToken(portlet));
		portletURL.setParameter("showAncestorScopes", Boolean.TRUE.toString());
		portletURL.setParameter("showManageTemplates", Boolean.TRUE.toString());

		/*
		ddmURL.setDoAsGroupId(
			config.doAsGroupId || themeDisplay.getScopeGroupId());

		ddmURL.setParameter("classNameId", config.classNameId);
		ddmURL.setParameter("classPK", config.classPK);
		ddmURL.setParameter("resourceClassNameId", config.resourceClassNameId);
		ddmURL.setParameter("eventName", config.eventName);
		ddmURL.setParameter("mode", config.mode);
		ddmURL.setParameter(
			"portletResourceNamespace", config.portletResourceNamespace);

		ddmURL.setParameter("scopeTitle", config.title);

		if ("showBackURL" in config) {
			ddmURL.setParameter("showBackURL", config.showBackURL);
		}

		if ("showHeader" in config) {
			ddmURL.setParameter("showHeader", config.showHeader);
		}

		if ("showToolbar" in config) {
			ddmURL.setParameter("showToolbar", config.showToolbar);
		}

		ddmURL.setParameter(
			"structureAvailableFields", config.structureAvailableFields);
		ddmURL.setParameter("templateId", config.templateId);

		ddmURL.setPortletId(Liferay.PortletKeys.DYNAMIC_DATA_MAPPING);
		*/

		return portletURL.toString();
	}

	@Override
	public boolean isShow() {
		User user = themeDisplay.getUser();

		if (user.isDefaultUser()) {
			return false;
		}

		return true;
	}

	@Override
	public boolean isToolTip() {
		return false;
	}

}