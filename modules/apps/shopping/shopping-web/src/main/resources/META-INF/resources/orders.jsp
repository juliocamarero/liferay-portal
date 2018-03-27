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
PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("mvcRenderCommandName", "/shopping/view");
portletURL.setParameter("tabs1", "order");
%>

<liferay-util:include page="/tabs1.jsp" servletContext="<%= application %>" />

<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" />
	<aui:input name="deleteOrderIds" type="hidden" />

	<%
	OrderSearch searchContainer = new OrderSearch(renderRequest, portletURL);

	List headerNames = searchContainer.getHeaderNames();

	headerNames.add(StringPool.BLANK);

	searchContainer.setRowChecker(new RowChecker(renderResponse));
	%>

	<liferay-ui:search-form
		page="/order_search.jsp"
		searchContainer="<%= searchContainer %>"
		servletContext="<%= application %>"
	/>

	<%
	OrderSearchTerms searchTerms = (OrderSearchTerms)searchContainer.getSearchTerms();

	long searchUserId = user.getUserId();

	if (ShoppingPermission.contains(permissionChecker, scopeGroupId, ActionKeys.MANAGE_ORDERS)) {
		searchUserId = 0;
	}

	int total = ShoppingOrderLocalServiceUtil.searchCount(scopeGroupId, company.getCompanyId(), searchUserId, searchTerms.getNumber(), searchTerms.getFirstName(), searchTerms.getLastName(), searchTerms.getEmailAddress(), searchTerms.getFirstName(), searchTerms.getLastName(), searchTerms.getEmailAddress(), searchTerms.getStatus(), searchTerms.isAndOperator());

	searchContainer.setTotal(total);

	List results = ShoppingOrderLocalServiceUtil.search(scopeGroupId, company.getCompanyId(), searchUserId, searchTerms.getNumber(), searchTerms.getFirstName(), searchTerms.getLastName(), searchTerms.getEmailAddress(), searchTerms.getFirstName(), searchTerms.getLastName(), searchTerms.getEmailAddress(), searchTerms.getStatus(), searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd());

	searchContainer.setResults(results);
	%>

	<c:if test="<%= !results.isEmpty() %>">
		<div class="separator"><!-- --></div>

		<aui:button-row>
			<aui:button disabled="<%= true %>" name="delete" onClick='<%= renderResponse.getNamespace() + "deleteOrders();" %>' value="delete" />
		</aui:button-row>
	</c:if>

	<%
	List resultRows = searchContainer.getResultRows();

	for (int i = 0; i < results.size(); i++) {
		ShoppingOrder order = (ShoppingOrder)results.get(i);

		order = order.toEscapedModel();

		ResultRow row = new ResultRow(order, order.getOrderId(), i);

		PortletURL rowURL = renderResponse.createRenderURL();

		rowURL.setParameter("mvcRenderCommandName", "/shopping/edit_order");
		rowURL.setParameter("redirect", currentURL);
		rowURL.setParameter("orderId", String.valueOf(order.getOrderId()));

		// Number

		row.addText(order.getNumber(), rowURL);

		// Date

		row.addDate(order.getCreateDate(), rowURL);

		// Status

		row.addText(ShoppingUtil.getPpPaymentStatus(order, request), rowURL);

		// Customer

		row.addText(HtmlUtil.escape(PortalUtil.getUserName(order.getUserId(), order.getBillingFirstName() + StringPool.SPACE + order.getBillingLastName())));

		// Action

		row.addJSP("/order_action.jsp", "entry-action", application, request, response);

		// Add result row

		resultRows.add(row);
	}
	%>

	<liferay-ui:search-iterator
		searchContainer="<%= searchContainer %>"
	/>
</aui:form>

<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />deleteOrders',
		function() {
			if (confirm('<%= UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-delete-the-selected-orders") %>')) {
				document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = '<%= Constants.DELETE %>';
				document.<portlet:namespace />fm.<portlet:namespace />deleteOrderIds.value = Liferay.Util.listCheckedExcept(document.<portlet:namespace />fm, '<portlet:namespace />allRowIds');

				submitForm(document.<portlet:namespace />fm, '<portlet:actionURL name="/shopping/edit_order"><portlet:param name="mvcActionCommand" value="/shopping/edit_order" /><portlet:param name="redirect" value="<%= currentURL %>" /></portlet:actionURL>');
			}
		},
		['liferay-util-list-fields']
	);
</aui:script>