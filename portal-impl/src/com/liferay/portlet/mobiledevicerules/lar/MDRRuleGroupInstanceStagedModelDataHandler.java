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
import com.liferay.portal.kernel.lar.StagedModelPathUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.mobiledevicerules.model.MDRRuleGroup;
import com.liferay.portlet.mobiledevicerules.model.MDRRuleGroupInstance;
import com.liferay.portlet.mobiledevicerules.service.MDRRuleGroupInstanceLocalServiceUtil;
import com.liferay.portlet.mobiledevicerules.service.persistence.MDRRuleGroupInstanceUtil;
import com.liferay.portlet.mobiledevicerules.service.persistence.MDRRuleGroupUtil;

import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class MDRRuleGroupInstanceStagedModelDataHandler
	extends BaseStagedModelDataHandler<MDRRuleGroupInstance> {

	@Override
	public String getClassName() {
		return MDRRuleGroupInstance.class.getName();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Element[] elements,
			MDRRuleGroupInstance ruleGroupInstance)
		throws Exception {


		Element ruleGroupInstancesElement = elements[0];

		Element ruleGroupInstanceElement = ruleGroupInstancesElement.addElement(
			"rule-group-instance");

		MDRRuleGroup ruleGroup = ruleGroupInstance.getRuleGroup();

		String className = ruleGroupInstance.getClassName();

		if (className.equals(Layout.class.getName())) {
			Layout layout = LayoutLocalServiceUtil.getLayout(
				ruleGroupInstance.getClassPK());

			ruleGroupInstanceElement.addAttribute(
				"layout-uuid", layout.getUuid());
		}

		String ruleGroupUuid = ruleGroup.getUuid();

		ruleGroupInstanceElement.addAttribute("rule-group-uuid", ruleGroupUuid);

		portletDataContext.addClassedModel(
			ruleGroupInstanceElement,
			StagedModelPathUtil.getPath(ruleGroupInstance), ruleGroupInstance,
			MDRPortletDataHandler.NAMESPACE);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			Element ruleGroupInstanceElement, String path,
			MDRRuleGroupInstance ruleGroupInstance)
		throws Exception {

		long userId = portletDataContext.getUserId(
			ruleGroupInstance.getUserUuid());

		Map<Long, Long> ruleGroupIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				MDRRuleGroup.class);

		Long ruleGroupId = ruleGroupIds.get(ruleGroupInstance.getRuleGroupId());

		if (ruleGroupId == null) {
			try {
				String ruleGroupUuid = ruleGroupInstanceElement.attributeValue(
					"rule-group-uuid");

				MDRRuleGroup ruleGroup = MDRRuleGroupUtil.fetchByUUID_G(
					ruleGroupUuid, portletDataContext.getScopeGroupId());

				ruleGroupId = ruleGroup.getRuleGroupId();
			}
			catch (Exception e) {
				if (_log.isErrorEnabled()) {
					_log.warn(
						"Unable to import rule group instance " +
							ruleGroupInstance,
						e);
				}

				return;
			}
		}

		long classPK = 0;

		String layoutUuid = ruleGroupInstanceElement.attributeValue(
			"layout-uuid");

		try {
			if (Validator.isNotNull(layoutUuid)) {
				Layout layout =
					LayoutLocalServiceUtil.getLayoutByUuidAndGroupId(
						layoutUuid, portletDataContext.getScopeGroupId(),
						portletDataContext.isPrivateLayout());

				classPK = layout.getPrimaryKey();
			}
			else {
				LayoutSet layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(
					portletDataContext.getScopeGroupId(),
					portletDataContext.isPrivateLayout());

				classPK = layoutSet.getLayoutSetId();
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				StringBundler sb = new StringBundler(5);

				sb.append("Layout ");
				sb.append(layoutUuid);
				sb.append(" is missing for rule group instance ");
				sb.append(ruleGroupInstance.getRuleGroupInstanceId());
				sb.append(", skipping this rule group instance.");

				_log.warn(sb.toString());
			}

			return;
		}

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			ruleGroupInstanceElement, ruleGroupInstance,
			MDRPortletDataHandler.NAMESPACE);

		serviceContext.setUserId(userId);

		MDRRuleGroupInstance importedRuleGroupInstance = null;

		if (portletDataContext.isDataStrategyMirror()) {
			MDRRuleGroupInstance existingMDRRuleGroupInstance =
				MDRRuleGroupInstanceUtil.fetchByUUID_G(
					ruleGroupInstance.getUuid(),
					portletDataContext.getScopeGroupId());

			if (existingMDRRuleGroupInstance == null) {
				serviceContext.setUuid(ruleGroupInstance.getUuid());

				importedRuleGroupInstance =
					MDRRuleGroupInstanceLocalServiceUtil.addRuleGroupInstance(
						portletDataContext.getScopeGroupId(),
						ruleGroupInstance.getClassName(), classPK, ruleGroupId,
						ruleGroupInstance.getPriority(), serviceContext);
			}
			else {
				importedRuleGroupInstance =
					MDRRuleGroupInstanceLocalServiceUtil.
						updateRuleGroupInstance(
							existingMDRRuleGroupInstance.
								getRuleGroupInstanceId(),
							ruleGroupInstance.getPriority());
			}
		}
		else {
			importedRuleGroupInstance =
				MDRRuleGroupInstanceLocalServiceUtil.addRuleGroupInstance(
					portletDataContext.getScopeGroupId(),
					ruleGroupInstance.getClassName(), classPK, ruleGroupId,
					ruleGroupInstance.getPriority(), serviceContext);
		}

		portletDataContext.importClassedModel(
			ruleGroupInstance, importedRuleGroupInstance,
			MDRPortletDataHandler.NAMESPACE);
	}

	private static Log _log = LogFactoryUtil.getLog(
		MDRRuleGroupInstanceStagedModelDataHandler.class);

}