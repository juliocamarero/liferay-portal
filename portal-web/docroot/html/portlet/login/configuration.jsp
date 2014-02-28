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

<%@ include file="/html/portlet/login/init.jsp" %>

<%
String tabs1 = ParamUtil.getString(request, "tabs1", "general");
String tabs2 = ParamUtil.getString(request, "tabs2", "general");

String emailFromName = ParamUtil.getString(request, "preferences--emailFromName--", LoginUtil.getEmailFromName(portletPreferences, company.getCompanyId()));
String emailFromAddress = ParamUtil.getString(request, "preferences--emailFromAddress--", LoginUtil.getEmailFromAddress(portletPreferences, company.getCompanyId()));

String emailParam = "emailPasswordSent";
String defaultEmailSubject = StringPool.BLANK;
String defaultEmailBody = StringPool.BLANK;

if (tabs2.equals("password-reset-notification")) {
	emailParam = "emailPasswordReset";
	defaultEmailSubject = ContentUtil.get(PropsValues.ADMIN_EMAIL_PASSWORD_RESET_SUBJECT);
	defaultEmailBody = ContentUtil.get(PropsValues.ADMIN_EMAIL_PASSWORD_RESET_BODY);
}
else if (tabs2.equals("password-changed-notification")) {
	defaultEmailSubject = ContentUtil.get(PropsValues.ADMIN_EMAIL_PASSWORD_SENT_SUBJECT);
	defaultEmailBody = ContentUtil.get(PropsValues.ADMIN_EMAIL_PASSWORD_SENT_BODY);
}

String currentLanguageId = LanguageUtil.getLanguageId(request);

String emailSubjectParam = emailParam + "Subject_" + currentLanguageId;
String emailBodyParam = emailParam + "Body_" + currentLanguageId;

String emailSubject = PrefsParamUtil.getString(portletPreferences, request, emailSubjectParam, defaultEmailSubject);
String emailBody = PrefsParamUtil.getString(portletPreferences, request, emailBodyParam, defaultEmailBody);
%>

<liferay-portlet:actionURL portletConfiguration="true" var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="true" var="configurationRenderURL">
	<portlet:param name="tabs1" value="<%= tabs1 %>" />
	<portlet:param name="tabs2" value="<%= tabs2 %>" />
</liferay-portlet:renderURL>

<aui:form action="<%= configurationActionURL %>" method="post" name="fm" onSubmit='<%= renderResponse.getNamespace() + "saveConfiguration();" %>'>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= configurationRenderURL %>" />
	<aui:input name="tabs1" type="hidden" value="<%= tabs1 %>" />
	<aui:input name="tabs2" type="hidden" value="<%= tabs2 %>" />

	<liferay-ui:tabs
		names="general,email-from,password-changed-notification,password-reset-notification"
		refresh="<%= false %>"
	>

		<liferay-ui:error key="emailFromAddress" message="please-enter-a-valid-email-address" />
		<liferay-ui:error key="emailFromName" message="please-enter-a-valid-name" />

		<liferay-ui:section>
			<aui:fieldset>
				<aui:select label="authentication-type" name="preferences--authType--" value="<%= authType %>">
					<aui:option label="default" value="" />
					<aui:option label="by-email-address" value="<%= CompanyConstants.AUTH_TYPE_EA %>" />
					<aui:option label="by-screen-name" value="<%= CompanyConstants.AUTH_TYPE_SN %>" />
					<aui:option label="by-user-id" value="<%= CompanyConstants.AUTH_TYPE_ID %>" />
				</aui:select>
			</aui:fieldset>
		</liferay-ui:section>

		<liferay-ui:section>
			<aui:fieldset>
				<aui:input cssClass="lfr-input-text-container" label="name" name="preferences--emailFromName--" value="<%= emailFromName %>" />

				<aui:input cssClass="lfr-input-text-container" label="address" name="preferences--emailFromAddress--" value="<%= emailFromAddress %>" />
			</aui:fieldset>
		</liferay-ui:section>

		<%@ include file="/html/portlet/login/email_notifications.jspf" %>
	</liferay-ui:tabs>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>

<aui:script>
	function <portlet:namespace />initEditor() {
		return "<%= UnicodeFormatter.toString(emailBody) %>";
	}

	function <portlet:namespace />saveConfiguration() {
		<c:if test='<%= tabs2.endsWith("-notification") %>'>
			document.<portlet:namespace />fm.<portlet:namespace /><%= emailBodyParam %>.value = window.<portlet:namespace />editor.getHTML();
		</c:if>

		submitForm(document.<portlet:namespace />fm);
	}

	function <portlet:namespace />updateLanguage() {
		document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = '';
		submitForm(document.<portlet:namespace />fm);
	}
</aui:script>

<%!
public static final String EDITOR_WYSIWYG_IMPL_KEY = "editor.wysiwyg.portal-web.docroot.html.portlet.login.configuration.jsp";
%>