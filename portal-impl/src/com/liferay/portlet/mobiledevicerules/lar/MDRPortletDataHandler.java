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

import com.liferay.portal.kernel.lar.BasePortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.mobile.device.rulegroup.action.impl.SiteRedirectActionHandler;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.mobiledevicerules.model.MDRAction;
import com.liferay.portlet.mobiledevicerules.model.MDRRule;
import com.liferay.portlet.mobiledevicerules.model.MDRRuleGroup;
import com.liferay.portlet.mobiledevicerules.model.MDRRuleGroupInstance;
import com.liferay.portlet.mobiledevicerules.service.MDRRuleGroupInstanceLocalServiceUtil;
import com.liferay.portlet.mobiledevicerules.service.MDRRuleGroupLocalServiceUtil;
import com.liferay.portlet.mobiledevicerules.service.persistence.MDRRuleGroupInstanceUtil;
import com.liferay.portlet.mobiledevicerules.service.persistence.MDRRuleGroupUtil;

import java.util.List;

import javax.portlet.PortletPreferences;

/**
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 */
public class MDRPortletDataHandler extends BasePortletDataHandler {

	public static final String NAMESPACE = "mobile_device_rules";

	public MDRPortletDataHandler() {
		setAlwaysExportable(true);
		setAlwaysStaged(true);
		setExportControls(
			new PortletDataHandlerBoolean(NAMESPACE, "rule-groups", true, true),
			new PortletDataHandlerBoolean(
				NAMESPACE, "rule-group-instances", true, true));
		setImportControls(new PortletDataHandlerControl[0]);
		setPublishToLiveByDefault(true);
	}

	@Override
	protected PortletPreferences doDeleteData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		if (portletDataContext.addPrimaryKey(
				MDRPortletDataHandler.class, "deleteData")) {

			return portletPreferences;
		}

		MDRRuleGroupInstanceLocalServiceUtil.deleteGroupRuleGroupInstances(
			portletDataContext.getScopeGroupId());

		MDRRuleGroupLocalServiceUtil.deleteRuleGroups(
			portletDataContext.getGroupId());

		return portletPreferences;
	}

	@Override
	protected String doExportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		portletDataContext.addPermissions(
			"com.liferay.portlet.mobiledevicerules",
			portletDataContext.getScopeGroupId());

		Element rootElement = addExportRootElement();

		Element ruleGroupsElement = rootElement.addElement("rule-groups");

		List<MDRRuleGroup> ruleGroups = MDRRuleGroupUtil.findByGroupId(
			portletDataContext.getGroupId());

		for (MDRRuleGroup ruleGroup : ruleGroups) {
			exportRuleGroup(portletDataContext, ruleGroupsElement, ruleGroup);
		}

		Element ruleGroupInstancesElement = rootElement.addElement(
			"rule-group-instances");

		List<MDRRuleGroupInstance> ruleGroupInstances =
			MDRRuleGroupInstanceUtil.findByGroupId(
				portletDataContext.getScopeGroupId());

		for (MDRRuleGroupInstance ruleGroupInstance : ruleGroupInstances) {
			exportRuleGroupInstance(
				portletDataContext, ruleGroupInstancesElement,
				ruleGroupInstance);
		}

		return rootElement.formattedString();
	}

	@Override
	protected PortletPreferences doImportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, String data)
		throws Exception {

		portletDataContext.importPermissions(
			"com.liferay.portlet.mobiledevicerules",
			portletDataContext.getSourceGroupId(),
			portletDataContext.getScopeGroupId());

		Document document = SAXReaderUtil.read(data);

		Element rootElement = document.getRootElement();

		Element ruleGroupsElement = rootElement.element("rule-groups");

		List<Element> ruleGroupElements = ruleGroupsElement.elements(
			"rule-group");

		for (Element ruleGroupElement : ruleGroupElements) {
			String path = ruleGroupElement.attributeValue("path");

			if (!portletDataContext.isPathNotProcessed(path)) {
				continue;
			}

			MDRRuleGroup ruleGroup =
				(MDRRuleGroup)portletDataContext.getZipEntryAsObject(path);

			importRuleGroup(portletDataContext, ruleGroupElement, ruleGroup);
		}

		Element ruleGroupInstancesElement = rootElement.element(
			"rule-group-instances");

		List<Element> ruleGroupInstanceElements =
			ruleGroupInstancesElement.elements("rule-group-instance");

		for (Element ruleGroupInstanceElement : ruleGroupInstanceElements) {
			String path = ruleGroupInstanceElement.attributeValue("path");

			if (!portletDataContext.isPathNotProcessed(path)) {
				continue;
			}

			MDRRuleGroupInstance ruleGroupInstance =
				(MDRRuleGroupInstance)portletDataContext.getZipEntryAsObject(
					path);

			importRuleGroupInstance(
				portletDataContext, ruleGroupInstanceElement,
				ruleGroupInstance);
		}

		return null;
	}

	protected String getActionPath(
		PortletDataContext portletDataContext, MDRAction action) {

		StringBundler sb = new StringBundler(4);

		sb.append(
			portletDataContext.getPortletPath(
				PortletKeys.MOBILE_DEVICE_SITE_ADMIN));
		sb.append("/actions/");
		sb.append(action.getActionId());
		sb.append(".xml");

		return sb.toString();
	}

	protected String getRuleGroupInstancePath(
		PortletDataContext portletDataContext,
		MDRRuleGroupInstance ruleGroupInstance) {

		StringBundler sb = new StringBundler(4);

		sb.append(
			portletDataContext.getPortletPath(
				PortletKeys.MOBILE_DEVICE_SITE_ADMIN));
		sb.append("/rule-group-instances/");
		sb.append(ruleGroupInstance.getRuleGroupInstanceId());
		sb.append(".xml");

		return sb.toString();
	}

	protected String getRuleGroupPath(
		PortletDataContext portletDataContext, MDRRuleGroup ruleGroup) {

		StringBundler sb = new StringBundler(4);

		sb.append(
			portletDataContext.getPortletPath(
				PortletKeys.MOBILE_DEVICE_SITE_ADMIN));
		sb.append("/rule-groups/");
		sb.append(ruleGroup.getRuleGroupId());
		sb.append(".xml");

		return sb.toString();
	}

	protected String getRulePath(
		PortletDataContext portletDataContext, MDRRule rule) {

		StringBundler sb = new StringBundler(4);

		sb.append(
			portletDataContext.getPortletPath(
				PortletKeys.MOBILE_DEVICE_SITE_ADMIN));
		sb.append("/rules/");
		sb.append(rule.getRuleId());
		sb.append(".xml");

		return sb.toString();
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

	private static Log _log = LogFactoryUtil.getLog(
		MDRPortletDataHandler.class);

}