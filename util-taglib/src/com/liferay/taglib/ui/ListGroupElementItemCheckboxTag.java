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

import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Carlos Lancha
 * @generated
 */
public class ListGroupElementItemCheckboxTag extends IncludeTag {

	@Override
	public int doStartTag() {
		return EVAL_BODY_INCLUDE;
	}

	public void setCssClass(String cssClass) {
		_cssClass = cssClass;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setValue(String value) {
		_value = value;
	}

	public void setView(String view) {
		_view = view;
	}

	@Override
	protected void cleanUp() {
		_cssClass = null;
		_id = null;
		_name = null;
		_title = null;
		_value = null;
		_view = null;
	}

	@Override
	protected String getPage() {
		if (Validator.isNotNull(_view)) {
			return "/html/taglib/ui/list_group_element_item_checkbox/" + _view + "/page.jsp";
		}

		return "/html/taglib/ui/list_group_element_item_checkbox/page.jsp";
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-ui:list-group-element-item-checkbox:cssClass", _cssClass);
		request.setAttribute(
			"liferay-ui:list-group-element-item-checkbox:id", _id);
		request.setAttribute(
			"liferay-ui:list-group-element-item-checkbox:name", _name);
		request.setAttribute(
			"liferay-ui:list-group-element-item-checkbox:title", _title);
		request.setAttribute(
			"liferay-ui:list-group-element-item-checkbox:value", _value);
		request.setAttribute(
			"liferay-ui:list-group-element-item-checkbox:view", _view);
	}

	private java.lang.String _cssClass = null;
	private java.lang.String _id = null;
	private java.lang.String _name = null;
	private java.lang.String _title = null;
	private java.lang.String _value = null;
	private java.lang.String _view = null;
}