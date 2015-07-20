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

package com.liferay.taglib.ui;

import com.liferay.portal.kernel.util.Validator;
import com.liferay.taglib.util.PortalIncludeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Carlos Lancha
 */
public class ListGroupElementItemTag extends TagSupport {

	@Override
	public int doEndTag() throws JspException {
		try {
			PortalIncludeUtil.include(pageContext, getEndPage());

			return EVAL_PAGE;
		}
		catch (Exception e) {
			throw new JspException(e);
		}
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			HttpServletRequest request =
				(HttpServletRequest)pageContext.getRequest();

			setAttributes(request);

			PortalIncludeUtil.include(pageContext, getStartPage());

			return EVAL_BODY_INCLUDE;
		}
		catch (Exception e) {
			throw new JspException(e);
		}
	}

	public void setCssClass(String cssClass) {
		_cssClass = cssClass;
	}

	public void setType(String type) {
		_type = type;
	}

	public void setView(String view) {
		_view = view;
	}

	protected String getEndPage() {
		if (Validator.isNotNull(_view)) {
			return "/html/taglib/ui/list_group_element_item/" + _view + "/end.jsp";
		}

		return "/html/taglib/ui/list_group_element_item/end.jsp";
	}

	protected String getStartPage() {
		if (Validator.isNotNull(_view)) {
			return "/html/taglib/ui/list_group_element_item/" + _view + "/start.jsp";
		}

		return "/html/taglib/ui/list_group_element_item/start.jsp";
	}

	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-ui:list-group-element-item:cssClass", _cssClass);
		request.setAttribute("liferay-ui:list-group-element-item:type", _type);
	}

	private String _cssClass = "";
	private String _type = "field";
	private String _view;

}