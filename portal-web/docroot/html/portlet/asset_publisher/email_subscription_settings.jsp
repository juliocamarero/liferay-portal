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

<%@ include file="/html/portlet/asset_publisher/init.jsp" %>

<%
String emailParam = (String)request.getAttribute("configuration.jsp-emailParam");
String editorParam = (String)request.getAttribute("configuration.jsp-editorParam");

String currentLanguageId = LanguageUtil.getLanguageId(request);

String emailFromName = ParamUtil.getString(request, "emailFromName", AssetPublisherUtil.getEmailFromName(preferences, company.getCompanyId()));
String emailFromAddress = ParamUtil.getString(request, "emailFromAddress", AssetPublisherUtil.getEmailFromAddress(preferences, company.getCompanyId()));

String defaultEmailSubject = ContentUtil.get(PropsUtil.get(PropsKeys.ASSET_PUBLISHER_EMAIL_ASSET_ENTRY_ADDED_SUBJECT));
String emailSubject = PrefsParamUtil.getString(preferences, request, emailParam + "Subject_" + currentLanguageId, defaultEmailSubject);

String defaultEmailBody = ContentUtil.get(PropsUtil.get(PropsKeys.ASSET_PUBLISHER_EMAIL_ASSET_ENTRY_ADDED_BODY));

String emailBody = PrefsParamUtil.getString(preferences, request, emailParam + "Body_" + currentLanguageId, defaultEmailBody);

String editorContent = emailBody;

boolean emailAssetEntryAddedEnabled = AssetPublisherUtil.getEmailAssetEntryAddedEnabled(preferences);
%>

<liferay-ui:error key="emailAssetEntryAddedBody" message="please-enter-a-valid-body" />
<liferay-ui:error key="emailAssetEntryAddedSubject" message="please-enter-a-valid-subject" />
<liferay-ui:error key="emailFromAddress" message="please-enter-a-valid-email-address" />
<liferay-ui:error key="emailFromName" message="please-enter-a-valid-name" />

<aui:fieldset>
	<aui:input id="enableEmailSubscription" label="enable-email-subscription" name="preferences--emailAssetEntryAddedEnabled--" type="checkbox" value="<%= emailAssetEntryAddedEnabled %>" />

	<div class='<%= emailAssetEntryAddedEnabled ? StringPool.BLANK : "aui-helper-hidden" %>' id="<portlet:namespace />emailSubscriptionSettings">
		<aui:input cssClass="lfr-input-text-container" label="name" name="preferences--emailFromName--" value="<%= emailFromName %>" />

		<aui:input cssClass="lfr-input-text-container" label="address" name="preferences--emailFromAddress--" value="<%= emailFromAddress %>" />

		<aui:select label="language" name="languageId" onChange='<%= renderResponse.getNamespace() + "updateLanguage(this);" %>'>

			<%
				Locale[] locales = LanguageUtil.getAvailableLocales();

				for (int i = 0; i < locales.length; i++) {
					String style = StringPool.BLANK;

					if (Validator.isNotNull(preferences.getValue(emailParam + "Subject_" + LocaleUtil.toLanguageId(locales[i]), StringPool.BLANK)) ||
						Validator.isNotNull(preferences.getValue(emailParam + "Body_" + LocaleUtil.toLanguageId(locales[i]), StringPool.BLANK))) {

						style = "font-weight: bold;";
					}
			%>

			<aui:option label="<%= locales[i].getDisplayName(locale) %>" selected="<%= currentLanguageId.equals(LocaleUtil.toLanguageId(locales[i])) %>" style="<%= style %>" value="<%= LocaleUtil.toLanguageId(locales[i]) %>" />

			<%
				}
			%>

		</aui:select>

		<aui:input cssClass="lfr-input-text-container" label="subject" name='<%= "preferences--" + emailParam + "Subject_" + currentLanguageId + "--" %>' value="<%= emailSubject %>" />

		<aui:field-wrapper label="body">
			<liferay-ui:input-editor editorImpl="<%= EDITOR_WYSIWYG_IMPL_KEY %>" />

			<aui:input name='<%= "preferences--" + editorParam + "--" %>' type="hidden" />
		</aui:field-wrapper>
	</div>
</aui:fieldset>

<div class="definition-of-terms">
	<h4><liferay-ui:message key="definition-of-terms" /></h4>

	<dl>
		<dt>
			[$COMPANY_ID$]
		</dt>
		<dd>
			<liferay-ui:message key="the-company-id-associated-with-the-asset" />
		</dd>
		<dt>
			[$COMPANY_MX$]
		</dt>
		<dd>
			<liferay-ui:message key="the-company-mx-associated-with-the-asset" />
		</dd>
		<dt>
			[$COMPANY_NAME$]
		</dt>
		<dd>
			<liferay-ui:message key="the-company-name-associated-with-the-asset" />
		</dd>
		<dt>
		<dt>
			[ASSET_ENTRIES_LIST]
		</dt>
		<dd>
			<liferay-ui:message key="the-list-of-assets" />
		</dd>
		<dt>
			[$FROM_ADDRESS$]
		</dt>
		<dd>
			<%= HtmlUtil.escape(emailFromAddress) %>
		</dd>
		<dt>
			[$FROM_NAME$]
		</dt>
		<dd>
			<%= HtmlUtil.escape(emailFromName) %>
		</dd>
		<dt>
			[$PORTAL_URL$]
		</dt>
		<dd>
			<%= company.getVirtualHostname() %>
		</dd>
		<dt>
			[$PORTLET_NAME$]
		</dt>
		<dd>
			<%= PortalUtil.getPortletTitle(renderResponse) %>
		</dd>
		<dt>
			[$SITE_NAME$]
		</dt>
		<dd>
			<liferay-ui:message key="the-site-name-associated-with-the-asset" />
		</dd>
		<dt>
			[$TO_ADDRESS$]
		</dt>
		<dd>
			<liferay-ui:message key="the-address-of-the-email-recipient" />
		</dd>
		<dt>
			[$TO_NAME$]
		</dt>
		<dd>
			<liferay-ui:message key="the-name-of-the-email-recipient" />
		</dd>
	</dl>
</div>

<aui:script>
	function <portlet:namespace />initEditor() {
		return "<%= UnicodeFormatter.toString(editorContent) %>";
	}

	function <portlet:namespace />updateLanguage() {
		document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = '<%= Constants.TRANSLATE %>';

		submitForm(document.<portlet:namespace />fm);
	}
</aui:script>

<aui:script use="aui-base">
	var enableEmailSubscription = A.one('#<portlet:namespace />enableEmailSubscriptionCheckbox');

	enableEmailSubscription.on(
		'change',
		function(event) {
			var emailSubscriptionSettings = A.one('#<portlet:namespace />emailSubscriptionSettings');

			emailSubscriptionSettings.toggle();
		}
	);
</aui:script>

<%!
public static final String EDITOR_WYSIWYG_IMPL_KEY = "editor.wysiwyg.portal-web.docroot.html.portlet.asset_publisher.configuration.jsp";
%>