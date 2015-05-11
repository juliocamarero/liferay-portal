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

<html dir="<liferay-ui:message key="lang.dir" />">

<head>
	<meta content="no-cache" http-equiv="Cache-Control" />
	<meta content="no-cache" http-equiv="Pragma" />
	<meta content="0" http-equiv="Expires" />
</head>

<body onLoad="setTimeout('document.fm.submit()', 100);">

<form action="<%= HtmlUtil.escapeAttribute(iFrameDisplayContext.getSrc()) %>" method="<%= HtmlUtil.escapeAttribute(iFrameDisplayContext.getFormMethod()) %>" name="fm">

<%
List<String> hiddenVariablesList = ListUtil.toList(StringUtil.split(iFrameDisplayContext.getHiddenVariables(), CharPool.SEMICOLON));

hiddenVariablesList.addAll(iFrameDisplayContext.getIframeVariables());

for (KeyValuePair hiddenVariable : iFrameDisplayContext.getHiddenVariablesList()) {
%>

	<input name="<%= HtmlUtil.escapeAttribute(hiddenVariable.getKey()) %>" type="hidden" value="<%= HtmlUtil.escapeAttribute(hiddenVariable.getValue()) %>" />

<%
}
%>

<input name="<%= HtmlUtil.escapeAttribute(iFrameDisplayContext.getUserNameField()) %>" type="hidden" value="<%= HtmlUtil.escapeAttribute(iFrameDisplayContext.getUserName()) %>" />

<input name="<%= HtmlUtil.escapeAttribute(iFrameDisplayContext.getPasswordField()) %>" type="hidden" value="<%= HtmlUtil.escapeAttribute(iFrameDisplayContext.getPassword()) %>" />

</form>

</body>

</html>