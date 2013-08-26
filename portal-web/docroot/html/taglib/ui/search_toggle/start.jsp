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

<%@ include file="/html/taglib/ui/search_toggle/init.jsp" %>

<div class="taglib-search-toggle">
	<div class="form-search">
		<liferay-ui:input-search buttonLabel="<%= buttonLabel %>" displayTerms="<%= displayTerms %>" id="<%= id + DisplayTerms.KEYWORDS %>" />

		<button class="btn-link" id="<%= id %>toggleAdvanced" type="button">
			<i class="icon-cog"></i>
		</button>
	</div>
	<div class="popover taglib-search-toggle-advanced" id="<%= id %>advanced">
		<input id="<portlet:namespace /><%= id + displayTerms.ADVANCED_SEARCH %>" name="<portlet:namespace /><%= displayTerms.ADVANCED_SEARCH %>" type="hidden" value="false" />

		<div id="<portlet:namespace /><%= id %>advancedContent">
			<div id="<portlet:namespace /><%= id %>advancedBodyNode">
				<liferay-util:buffer var="andOperator">
					<aui:select cssClass="inline-control" inlineField="<%= true %>" label="" name="<%= displayTerms.AND_OPERATOR %>">
						<aui:option label="all" selected="<%= displayTerms.isAndOperator() %>" value="1" />
						<aui:option label="any" selected="<%= !displayTerms.isAndOperator() %>" value="0" />
					</aui:select>
				</liferay-util:buffer>

				<liferay-ui:message arguments="<%= andOperator %>" key="match-x-of-the-following-fields" />