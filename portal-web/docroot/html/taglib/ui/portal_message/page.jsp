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

<%@ include file="/html/taglib/init.jsp" %>

<%
String alternativeLayoutFriendlyURL = GetterUtil.getString(SessionMessages.get(request, "portletMessageAlternativeLayoutFriendlyUrl"));

String message = GetterUtil.getString(SessionMessages.get(request, "portalMessageMessage"));
String cssClass = GetterUtil.getString(SessionMessages.get(request, "portalMessageCssClass"), "alert-info");
boolean useAnimation = GetterUtil.getBoolean(SessionMessages.get(request, "portalMessageAnimation"), true);
int timeout = GetterUtil.getInteger(SessionMessages.get(request, "portalMessageTimeout"), 10000);
%>

<c:if test="<%= Validator.isNotNull(alternativeLayoutFriendlyURL) || Validator.isNotNull(message) %>">
	<div class="helper-hidden alert <%= cssClass %>" id="portalMessageContainer">
		<c:choose>
			<c:when test="<%= Validator.isNotNull(alternativeLayoutFriendlyURL) %>">
				<liferay-util:buffer var="redirectedLink">
					<aui:a href="<%= PortalUtil.getCurrentCompleteURL(request) %>">
						<%= PortalUtil.getCurrentCompleteURL(request) %>
					</aui:a>
				</liferay-util:buffer>

				<p class="redirected-to-message">
					<liferay-ui:message arguments="<%= redirectedLink %>" key="you-have-been-redirected-to-the-following-url" />
				</p>

				<liferay-util:buffer var="originalLink">
					<aui:a href="<%= alternativeLayoutFriendlyURL %>">
						<liferay-ui:message key="link" />
					</aui:a>
				</liferay-util:buffer>

				<p class="original-url">
					<liferay-ui:message arguments="<%= originalLink %>" key="click-in-the-following-link-if-you-want-to-dismiss-this-redirect-and-access-the-original-url" />
				</p>
			</c:when>
			<c:otherwise>
				<liferay-ui:message key="<%= message %>" />
			</c:otherwise>
		</c:choose>
	</div>

	<aui:script use="liferay-notice">
		var portalMessageContainer = A.one('#portalMessageContainer')

		var banner = new Liferay.Notice(
			{
				animationConfig:
					{
						duration: 2,
						top: '0px'
					},
				closeText: false,
				content: portalMessageContainer.html(),
				noticeClass: 'hide taglib-portal-message <%= cssClass %>',
				timeout: <%= timeout %>,
				toggleText: false,
				useAnimation: <%= useAnimation %>
			}
		);

		banner.show();
	</aui:script>
</c:if>