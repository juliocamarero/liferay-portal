/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.mobiledevicerules.lar;

import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.mobile.device.rulegroup.action.impl.SiteRedirectActionHandler;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.mobiledevicerules.model.MDRAction;
import com.liferay.portlet.mobiledevicerules.service.MDRActionLocalServiceUtil;
import com.liferay.portlet.mobiledevicerules.service.persistence.MDRActionUtil;

/**
 * @author Mate Thurzo
 */
public class MDRActionStagedModelDataHandler
	extends BaseStagedModelDataHandler<MDRAction> {

	@Override
	public String getClassName() {
		return MDRAction.class.getName();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Element[] elements,
			MDRAction action)
		throws Exception {

		String path = getActionPath(portletDataContext, action);

		Element actionElement = actionsElement.addElement("action");

		String type = action.getType();

		if (type.equals(SiteRedirectActionHandler.class.getName())) {
			UnicodeProperties typeSettingsProperties =
				action.getTypeSettingsProperties();

			long targetPlid = GetterUtil.getLong(
				typeSettingsProperties.getProperty("plid"));

			try {
				Layout targetLayout = LayoutLocalServiceUtil.getLayout(
					targetPlid);

				actionElement.addAttribute(
					"layout-uuid", targetLayout.getUuid());
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to set the layout uuid of the target " +
							targetPlid +
								". Site redirect may not match after import.",
						e);
				}
			}
		}

		portletDataContext.addClassedModel(
			actionElement, path, action, NAMESPACE);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, Element element, String path,
			MDRAction action)
		throws Exception {

		long userId = portletDataContext.getUserId(action.getUserUuid());

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			actionElement, action, NAMESPACE);

		serviceContext.setUserId(userId);

		validateTargetLayoutPlid(actionElement, action);

		MDRAction importedAction = null;

		if (portletDataContext.isDataStrategyMirror()) {
			MDRAction existingAction = MDRActionUtil.fetchByUUID_G(
				action.getUuid(), portletDataContext.getScopeGroupId());

			if (existingAction == null) {
				serviceContext.setUuid(action.getUuid());

				importedAction = MDRActionLocalServiceUtil.addAction(
					ruleGroupInstance.getRuleGroupInstanceId(),
					action.getNameMap(), action.getDescriptionMap(),
					action.getType(), action.getTypeSettingsProperties(),
					serviceContext);
			}
			else {
				importedAction = MDRActionLocalServiceUtil.updateAction(
					existingAction.getActionId(), action.getNameMap(),
					action.getDescriptionMap(), action.getType(),
					action.getTypeSettingsProperties(), serviceContext);
			}
		}
		else {
			importedAction = MDRActionLocalServiceUtil.addAction(
				ruleGroupInstance.getRuleGroupInstanceId(), action.getNameMap(),
				action.getDescriptionMap(), action.getType(),
				action.getTypeSettingsProperties(), serviceContext);
		}

		portletDataContext.importClassedModel(
			action, importedAction, NAMESPACE);
	}

	protected void validateTargetLayoutPlid(
		Element actionElement, MDRAction action) {

		String type = action.getType();

		if (!type.equals(SiteRedirectActionHandler.class.getName())) {
			return;
		}

		String targetLayoutUuid = actionElement.attributeValue("layout-uuid");

		if (Validator.isNull(targetLayoutUuid)) {
			return;
		}

		UnicodeProperties typeSettingsProperties =
			action.getTypeSettingsProperties();

		long targetGroupId = GetterUtil.getLong(
			typeSettingsProperties.getProperty("groupId"));
		boolean privateLayout = GetterUtil.getBoolean(
			actionElement.attributeValue("private-layout"));

		try {
			Layout targetLayout =
				LayoutLocalServiceUtil.getLayoutByUuidAndGroupId(
					targetLayoutUuid, targetGroupId, privateLayout);

			typeSettingsProperties.setProperty(
				"plid", String.valueOf(targetLayout.getPlid()));
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to find target layout with uuid " +
						targetLayoutUuid + " in group " + targetGroupId +
							". Site redirect may not match target layout.",
					e);
			}
		}
	}

}