<%--
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
--%>

<%@ include file="/html/portlet/portal_settings/init.jsp" %>

<%
Group liveGroup = (Group)request.getAttribute("site.liveGroup");

UnicodeProperties groupTypeSettings = null;

if (liveGroup != null) {
	groupTypeSettings = liveGroup.getTypeSettingsProperties();
}
else {
	groupTypeSettings = new UnicodeProperties();
}

int groupTrashEnabled = PropertiesParamUtil.getInteger(groupTypeSettings, request, "trashEnabled", TrashUtil.TRASH_ENABLED);

int trashEntriesMaxAge = PropertiesParamUtil.getInteger(groupTypeSettings, request, "trashEntriesMaxAge", PrefsPropsUtil.getInteger(company.getCompanyId(), PropsKeys.TRASH_ENTRIES_MAX_AGE));
%>

<aui:fieldset>
	<aui:input checked="<%= groupTrashEnabled == TrashUtil.TRASH_ENABLED %>" class="aui-field-label" label="enabled" name='<%= "settings--" + PropsKeys.TRASH_ENABLED + "--" %>' type="radio" value="<%= TrashUtil.TRASH_ENABLED %>" />

	<div class="recycle-bin-max-age-field">
		<aui:input disabled="<%= groupTrashEnabled != TrashUtil.TRASH_ENABLED %>" label="number-of-days-that-files-will-be-kept-in-the-recycle-bin" name="trashEntriesMaxAge" type="text" value="<%= trashEntriesMaxAge %>">
			<aui:validator name="min">1</aui:validator>
		</aui:input>
	</div>

	<aui:input checked="<%= groupTrashEnabled == TrashUtil.TRASH_DISABLED %>" class="aui-field-label" label="disabled" name='<%= "settings--" + PropsKeys.TRASH_ENABLED + "--" %>' type="radio" value="<%= TrashUtil.TRASH_DISABLED %>" />
</aui:fieldset>

<aui:script>
	Liferay.Util.disableSelectBoxes('<portlet:namespace />trashEntriesMaxAge', '<%= TrashUtil.TRASH_ENABLED %>', '<portlet:namespace />trashEnabled');
</aui:script>