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

package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.BaseModelListener;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.model.PortletConstants;
import com.liferay.portal.model.ResourcePermission;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.LayoutSetPrototypeLocalServiceUtil;
import com.liferay.portal.service.persistence.LayoutSetPrototypeUtil;
import com.liferay.portlet.sites.util.Sites;

import java.util.Date;

/**
 * @author Eduardo Garcia
 */
public class LayoutSetPrototypeResourcePermissionListener
	extends BaseModelListener<ResourcePermission> {

	@Override
	public void onAfterCreate(ResourcePermission resourcePermission) {
		updateLayoutSetPrototype(resourcePermission, new Date());
	}

	@Override
	public void onAfterRemove(ResourcePermission resourcePermission) {
		updateLayoutSetPrototype(resourcePermission, new Date());
	}

	@Override
	public void onAfterUpdate(ResourcePermission resourcePermission) {
		updateLayoutSetPrototype(resourcePermission, new Date());
	}

	protected void updateLayoutSetPrototype(
		ResourcePermission resourcePermission, Date modifiedDate) {

		String name = resourcePermission.getName();
		String primKey = resourcePermission.getPrimKey();

		long plid = 0;

		int index = primKey.indexOf(PortletConstants.LAYOUT_SEPARATOR);

		if (index != -1) {
			plid = GetterUtil.getLong(primKey.substring(0, index));
		}
		else if (name.equals(Layout.class.getName())) {
			plid = GetterUtil.getLong(primKey);
		}

		if (plid <= 0) {
			return;
		}

		try {
			Layout layout = LayoutLocalServiceUtil.fetchLayout(plid);

			if (layout != null) {
				Group group = layout.getGroup();

				if (!group.isLayoutSetPrototype()) {
					return;
				}

				LayoutSetPrototype layoutSetPrototype =
					LayoutSetPrototypeLocalServiceUtil.getLayoutSetPrototype(
						group.getClassPK());

				layoutSetPrototype.setModifiedDate(modifiedDate);

				LayoutSetPrototypeUtil.update(layoutSetPrototype);

				LayoutSet layoutSet = layoutSetPrototype.getLayoutSet();

				layoutSet.setModifiedDate(modifiedDate);

				UnicodeProperties settingsProperties =
					layoutSet.getSettingsProperties();

				settingsProperties.remove(Sites.MERGE_FAIL_COUNT);

				LayoutSetLocalServiceUtil.updateLayoutSet(layoutSet);
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		LayoutSetPrototypeResourcePermissionListener.class);

}