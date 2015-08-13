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
public class ListGroupElementItemIconTag extends IncludeTag {

	@Override
	public int doStartTag() {
		return EVAL_BODY_INCLUDE;
	}

	public void setCssClass(String cssClass) {
		_cssClass = cssClass;
	}

	public void setIconCssClass(String iconCssClass) {
		_iconCssClass = iconCssClass;
	}

	public void setIsImage(boolean isImage) {
		_isImage = isImage;
	}

	public void setIcon(String icon) {
		_icon = icon;
	}

	public void setView(String view) {
		_view = view;
	}

	@Override
	protected void cleanUp() {
		_cssClass = null;
		_iconCssClass = null;
		_isImage = false;
		_icon = null;
		_view = null;
	}

	@Override
	protected String getPage() {
		if (Validator.isNotNull(_view)) {
			return "/html/taglib/ui/list_group_element_item_icon/" + _view + "/page.jsp";
		}

		return "/html/taglib/ui/list_group_element_item_icon/page.jsp";
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-ui:list-group-element-item-icon:cssClass", _cssClass);
		request.setAttribute(
			"liferay-ui:list-group-element-item-icon:iconCssClass", _iconCssClass);
		request.setAttribute(
			"liferay-ui:list-group-element-item-icon:isImage", _isImage);
		request.setAttribute(
			"liferay-ui:list-group-element-item-icon:icon", _icon);
		request.setAttribute(
			"liferay-ui:list-group-element-item-icon:view", _view);
	}

	private String _cssClass = null;
	private String _iconCssClass = null;
	private boolean _isImage = false;
	private String _icon = null;
	private String _view = null;
}