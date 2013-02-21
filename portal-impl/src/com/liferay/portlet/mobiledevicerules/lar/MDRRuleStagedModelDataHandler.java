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
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.mobiledevicerules.model.MDRRule;
import com.liferay.portlet.mobiledevicerules.service.MDRRuleLocalServiceUtil;
import com.liferay.portlet.mobiledevicerules.service.persistence.MDRRuleUtil;

/**
 * @author Mate Thurzo
 */
public class MDRRuleStagedModelDataHandler
	extends BaseStagedModelDataHandler<MDRRule> {

	@Override
	public String getClassName() {
		return MDRRule.class.getName();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Element[] elements,
			MDRRule rule)
		throws Exception {

		String path = getRulePath(portletDataContext, rule);

		Element ruleElement = rulesElement.addElement("rule");

		portletDataContext.addClassedModel(ruleElement, path, rule, NAMESPACE);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, Element element, String path,
			MDRRule rule)
		throws Exception {

		long userId = portletDataContext.getUserId(rule.getUserUuid());

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			ruleElement, rule, NAMESPACE);

		serviceContext.setUserId(userId);

		MDRRule importedRule = null;

		if (portletDataContext.isDataStrategyMirror()) {
			MDRRule existingRule = MDRRuleUtil.fetchByUUID_G(
				rule.getUuid(), portletDataContext.getScopeGroupId());

			if (existingRule == null) {
				serviceContext.setUuid(rule.getUuid());

				importedRule = MDRRuleLocalServiceUtil.addRule(
					ruleGroup.getRuleGroupId(), rule.getNameMap(),
					rule.getDescriptionMap(), rule.getType(),
					rule.getTypeSettingsProperties(), serviceContext);
			}
			else {
				importedRule = MDRRuleLocalServiceUtil.updateRule(
					existingRule.getRuleId(), rule.getNameMap(),
					rule.getDescriptionMap(), rule.getType(),
					rule.getTypeSettingsProperties(), serviceContext);
			}
		}
		else {
			importedRule = MDRRuleLocalServiceUtil.addRule(
				ruleGroup.getRuleGroupId(), rule.getNameMap(),
				rule.getDescriptionMap(), rule.getType(),
				rule.getTypeSettingsProperties(), serviceContext);
		}

		portletDataContext.importClassedModel(rule, importedRule, NAMESPACE);
	}

}