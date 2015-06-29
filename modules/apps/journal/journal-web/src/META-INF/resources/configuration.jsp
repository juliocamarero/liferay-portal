<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
JournalWebRequestHelper journalWebRequestHelper = new JournalWebRequestHelper(request);

JournalGroupServiceSettings journalGroupServiceSettings = journalWebRequestHelper.getJournalGroupServiceSettings();

String emailFromName = ParamUtil.getString(request, "preferences--emailFromName--", journalGroupServiceSettings.emailFromName());
String emailFromAddress = ParamUtil.getString(request, "preferences--emailFromAddress--", journalGroupServiceSettings.emailFromAddress());
%>

<liferay-portlet:actionURL portletConfiguration="<%= true %>" var="configurationActionURL">
	<portlet:param name="serviceName" value="<%= JournalConstants.SERVICE_NAME %>" />
	<portlet:param name="settingsScope" value="group" />
</liferay-portlet:actionURL>

<liferay-portlet:renderURL portletConfiguration="<%= true %>" var="configurationRenderURL" />

<aui:form action="<%= configurationActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= configurationRenderURL %>" />

	<%
	String tabs1Names = "email-from,web-content-added-email,web-content-moved-from-folder-email,web-content-moved-to-folder-email,web-content-review-email,web-content-updated-email";

	if (WorkflowDefinitionLinkLocalServiceUtil.getWorkflowDefinitionLinksCount(themeDisplay.getCompanyId(), scopeGroupId, JournalFolder.class.getName()) > 0) {
		tabs1Names = tabs1Names.concat(",web-content-approval-denied-email,web-content-approval-granted-email,web-content-approval-requested-email");
	}
	%>

	<liferay-ui:tabs
		names="<%= tabs1Names %>"
		refresh="<%= false %>"
	>
		<liferay-ui:error key="emailFromAddress" message="please-enter-a-valid-email-address" />
		<liferay-ui:error key="emailFromName" message="please-enter-a-valid-name" />
		<liferay-ui:error key="emailArticleAddedBody" message="please-enter-a-valid-body" />
		<liferay-ui:error key="emailArticleAddedSubject" message="please-enter-a-valid-subject" />
		<liferay-ui:error key="emailArticleApprovalDeniedBody" message="please-enter-a-valid-body" />
		<liferay-ui:error key="emailArticleApprovalDeniedSubject" message="please-enter-a-valid-subject" />
		<liferay-ui:error key="emailArticleApprovalGrantedBody" message="please-enter-a-valid-body" />
		<liferay-ui:error key="emailArticleApprovalGrantedSubject" message="please-enter-a-valid-subject" />
		<liferay-ui:error key="emailArticleApprovalRequestedBody" message="please-enter-a-valid-body" />
		<liferay-ui:error key="emailArticleApprovalRequestedSubject" message="please-enter-a-valid-subject" />
		<liferay-ui:error key="emailArticleReviewBody" message="please-enter-a-valid-body" />
		<liferay-ui:error key="emailArticleReviewSubject" message="please-enter-a-valid-subject" />
		<liferay-ui:error key="emailArticleUpdatedBody" message="please-enter-a-valid-body" />
		<liferay-ui:error key="emailArticleUpdatedSubject" message="please-enter-a-valid-subject" />

		<liferay-ui:section>
			<aui:fieldset>
				<aui:input cssClass="lfr-input-text-container" label="name" name="preferences--emailFromName--" type="text" value="<%= emailFromName %>" />

				<aui:input cssClass="lfr-input-text-container" label="address" name="preferences--emailFromAddress--" type="text" value="<%= emailFromAddress %>" />
			</aui:fieldset>
		</liferay-ui:section>

		<%
		Map<String, String> emailDefinitionTerms = JournalUtil.getEmailDefinitionTerms(renderRequest, emailFromAddress, emailFromName);
		%>

		<liferay-ui:section>
			<liferay-ui:email-notification-settings
				emailBody="<%= journalGroupServiceSettings.emailArticleAddedBodyXml() %>"
				emailDefinitionTerms="<%= emailDefinitionTerms %>"
				emailEnabled="<%= journalGroupServiceSettings.emailArticleAddedEnabled() %>"
				emailParam="emailArticleAdded"
				emailSubject="<%= journalGroupServiceSettings.emailArticleAddedSubjectXml() %>"
			/>
		</liferay-ui:section>

		<liferay-ui:section>
			<liferay-ui:email-notification-settings
				emailBody="<%= journalGroupServiceSettings.emailArticleMovedFromFolderBodyXml() %>"
				emailDefinitionTerms="<%= emailDefinitionTerms %>"
				emailEnabled="<%= journalGroupServiceSettings.emailArticleMovedFromFolderEnabled() %>"
				emailParam="emailArticleMovedFromFolder"
				emailSubject="<%= journalGroupServiceSettings.emailArticleMovedFromFolderSubjectXml() %>"
			/>
		</liferay-ui:section>

		<liferay-ui:section>
			<liferay-ui:email-notification-settings
				emailBody="<%= journalGroupServiceSettings.emailArticleMovedToFolderBodyXml() %>"
				emailDefinitionTerms="<%= emailDefinitionTerms %>"
				emailEnabled="<%= journalGroupServiceSettings.emailArticleMovedToFolderEnabled() %>"
				emailParam="emailArticleMovedToFolder"
				emailSubject="<%= journalGroupServiceSettings.emailArticleMovedToFolderSubjectXml() %>"
			/>
		</liferay-ui:section>

		<liferay-ui:section>
			<liferay-ui:email-notification-settings
				emailBody="<%= journalGroupServiceSettings.emailArticleReviewBodyXml() %>"
				emailDefinitionTerms="<%= emailDefinitionTerms %>"
				emailEnabled="<%= journalGroupServiceSettings.emailArticleReviewEnabled() %>"
				emailParam="emailArticleReview"
				emailSubject="<%= journalGroupServiceSettings.emailArticleReviewSubjectXml() %>"
			/>
		</liferay-ui:section>

		<liferay-ui:section>
			<liferay-ui:email-notification-settings
				emailBody="<%= journalGroupServiceSettings.emailArticleUpdatedBodyXml() %>"
				emailDefinitionTerms="<%= emailDefinitionTerms %>"
				emailEnabled="<%= journalGroupServiceSettings.emailArticleUpdatedEnabled() %>"
				emailParam="emailArticleUpdated"
				emailSubject="<%= journalGroupServiceSettings.emailArticleUpdatedSubjectXml() %>"
			/>
		</liferay-ui:section>

		<c:if test="<%= WorkflowDefinitionLinkLocalServiceUtil.getWorkflowDefinitionLinksCount(themeDisplay.getCompanyId(), scopeGroupId, JournalFolder.class.getName()) > 0 %>">
			<liferay-ui:section>
				<liferay-ui:email-notification-settings
					emailBody="<%= journalGroupServiceSettings.emailArticleApprovalDeniedBodyXml() %>"
					emailDefinitionTerms="<%= emailDefinitionTerms %>"
					emailEnabled="<%= journalGroupServiceSettings.emailArticleApprovalDeniedEnabled() %>"
					emailParam="emailArticleApprovalDenied"
					emailSubject="<%= journalGroupServiceSettings.emailArticleApprovalDeniedSubjectXml() %>"
				/>
			</liferay-ui:section>

			<liferay-ui:section>
				<liferay-ui:email-notification-settings
					emailBody="<%= journalGroupServiceSettings.emailArticleApprovalGrantedBodyXml() %>"
					emailDefinitionTerms="<%= emailDefinitionTerms %>"
					emailEnabled="<%= journalGroupServiceSettings.emailArticleApprovalGrantedEnabled() %>"
					emailParam="emailArticleApprovalGranted"
					emailSubject="<%= journalGroupServiceSettings.emailArticleApprovalGrantedSubjectXml() %>"
					/>
			</liferay-ui:section>

			<liferay-ui:section>
				<liferay-ui:email-notification-settings
					emailBody="<%= journalGroupServiceSettings.emailArticleApprovalRequestedBodyXml() %>"
					emailDefinitionTerms="<%= emailDefinitionTerms %>"
					emailEnabled="<%= journalGroupServiceSettings.emailArticleApprovalRequestedEnabled() %>"
					emailParam="emailArticleApprovalRequested"
					emailSubject="<%= journalGroupServiceSettings.emailArticleApprovalRequestedSubjectXml() %>"
				/>
			</liferay-ui:section>
		</c:if>
	</liferay-ui:tabs>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>