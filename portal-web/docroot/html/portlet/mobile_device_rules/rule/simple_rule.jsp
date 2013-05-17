
<%--
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
--%>

<%@ include file="/html/portlet/mobile_device_rules/init.jsp" %>

<%
MDRRule rule = (MDRRule)request.getAttribute(WebKeys.MOBILE_DEVICE_RULES_RULE);

Set<String> operatingSystems = Collections.emptySet();
int deviceType = 0;

if (rule != null) {
	UnicodeProperties typeSettingsProperties = rule.getTypeSettingsProperties();

	operatingSystems = SetUtil.fromArray(StringUtil.split(typeSettingsProperties.get("os")));

	String tabletString = GetterUtil.getString(typeSettingsProperties.get("tablet"));

	if (tabletString.equals("tablets")) {
		deviceType = 1;
	}
	else if (tabletString.equals("other-devices")) {
		deviceType = 2;
	}
}
%>

<aui:select multiple="<%= true %>" name="os">
	<aui:option label="any-os" selected="<%= operatingSystems.isEmpty() %>" value="" />

	<%
	Set<VersionableName> knownOperationSystems = DeviceDetectionUtil.getKnownOperatingSystems();

	for (VersionableName knownOperationSystem : knownOperationSystems) {
	%>

		<aui:option label="<%= knownOperationSystem.getName() %>" selected="<%= operatingSystems.contains(knownOperationSystem.getName()) %>" />

	<%
	}
	%>

</aui:select>

<aui:select label="device-type" name="tablet">
	<aui:option label="any" selected="<%= deviceType == 0 %>" value="" />
	<aui:option label="tablets" selected="<%= deviceType == 1 %>" />
	<aui:option label="other-devices" selected="<%= deviceType == 2 %>" />
</aui:select>