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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.petra.string.StringPool" %><%@
page import="com.liferay.portal.kernel.bean.BeanParamUtil" %><%@
page import="com.liferay.portal.kernel.dao.search.SearchContainer" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.model.Portlet" %><%@
page import="com.liferay.portal.kernel.model.PortletConstants" %><%@
page import="com.liferay.portal.kernel.portlet.PortletURLUtil" %><%@
page import="com.liferay.portal.kernel.service.PortletLocalServiceUtil" %><%@
page import="com.liferay.portal.kernel.servlet.ServletContextPool" %><%@
page import="com.liferay.portal.kernel.util.GetterUtil" %><%@
page import="com.liferay.portal.kernel.util.KeyValuePair" %><%@
page import="com.liferay.portal.kernel.util.KeyValuePairComparator" %><%@
page import="com.liferay.portal.kernel.util.ListUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.PortalUtil" %><%@
page import="com.liferay.portal.kernel.util.StringUtil" %><%@
page import="com.liferay.portal.kernel.util.UnicodeProperties" %><%@
page import="com.liferay.taglib.search.ResultRow" %><%@
page import="com.liferay.wsrp.constants.Constants" %><%@
page import="com.liferay.wsrp.exception.NoSuchConsumerPortletException" %><%@
page import="com.liferay.wsrp.exception.WSRPConsumerNameException" %><%@
page import="com.liferay.wsrp.exception.WSRPConsumerPortletHandleException" %><%@
page import="com.liferay.wsrp.exception.WSRPConsumerPortletNameException" %><%@
page import="com.liferay.wsrp.exception.WSRPConsumerWSDLException" %><%@
page import="com.liferay.wsrp.exception.WSRPProducerNameException" %><%@
page import="com.liferay.wsrp.model.WSRPConsumer" %><%@
page import="com.liferay.wsrp.model.WSRPConsumerPortlet" %><%@
page import="com.liferay.wsrp.model.WSRPProducer" %><%@
page import="com.liferay.wsrp.service.WSRPConsumerLocalServiceUtil" %><%@
page import="com.liferay.wsrp.service.WSRPConsumerPortletLocalServiceUtil" %><%@
page import="com.liferay.wsrp.service.WSRPProducerLocalServiceUtil" %><%@
page import="com.liferay.wsrp.util.WSRPConsumerManager" %><%@
page import="com.liferay.wsrp.util.WebKeys" %><%@
page import="com.liferay.wsrp.web.internal.util.WSRPConsumerManagerFactoryUtil" %>

<%@ page import="java.util.ArrayList" %><%@
page import="java.util.Arrays" %><%@
page import="java.util.Iterator" %><%@
page import="java.util.List" %><%@
page import="java.util.Objects" %>

<%@ page import="javax.portlet.PortletURL" %>

<%@ page import="oasis.names.tc.wsrp.v2.types.LocalizedString" %><%@
page import="oasis.names.tc.wsrp.v2.types.PortletDescription" %><%@
page import="oasis.names.tc.wsrp.v2.types.PropertyDescription" %><%@
page import="oasis.names.tc.wsrp.v2.types.ServiceDescription" %>

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
String currentURL = PortalUtil.getCurrentURL(request);
%>