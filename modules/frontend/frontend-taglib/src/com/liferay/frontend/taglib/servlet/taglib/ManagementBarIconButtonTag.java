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

package com.liferay.frontend.taglib.servlet.taglib;

import com.liferay.frontend.taglib.servlet.ServletContextUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * @author Eudaldo Alonso
 */
public class ManagementBarIconButtonTag extends IncludeTag {

	@Override
	public int doStartTag() throws JspException {
		setAttributeNamespace(_ATTRIBUTE_NAMESPACE);

		return super.doStartTag();
	}

	public void setCssClass(String cssClass) {
		_cssClass = cssClass;
	}

	public void setHref(String href) {
		_href = href;
	}

	public void setIconCssClass(String iconCssClass) {
		_iconCssClass = iconCssClass;
	}

	public void setId(String id) {
		_id = id;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		servletContext = ServletContextUtil.getServletContext();
	}

	@Override
	protected void cleanUp() {
		_cssClass = StringPool.BLANK;
		_href = StringPool.BLANK;
		_iconCssClass = StringPool.BLANK;
		_id = StringPool.BLANK;
	}

	protected String getHref() {
		if (Validator.isNotNull(_href)) {
			return _href;
		}

		return "javascript:;";
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected boolean isCleanUpSetAttributes() {
		return _CLEAN_UP_SET_ATTRIBUTES;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-frontend:management-bar-icon-button:cssClass", _cssClass);
		request.setAttribute(
			"liferay-frontend:management-bar-icon-button:href", getHref());
		request.setAttribute(
			"liferay-frontend:management-bar-icon-button:iconCssClass",
			_iconCssClass);
		request.setAttribute(
			"liferay-frontend:management-bar-icon-button:id", _id);
	}

	private static final String _ATTRIBUTE_NAMESPACE =
		"liferay-frontend:management-bar-icon-button:";

	private static final boolean _CLEAN_UP_SET_ATTRIBUTES = true;

	private static final String _PAGE = "/management_bar_icon_button/page.jsp";

	private String _cssClass = StringPool.BLANK;
	private String _href = StringPool.BLANK;
	private String _iconCssClass = StringPool.BLANK;
	private String _id = StringPool.BLANK;

}