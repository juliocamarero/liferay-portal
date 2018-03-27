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

import com.liferay.frontend.taglib.servlet.taglib.util.AddMenuKeys;
import com.liferay.taglib.util.IncludeTag;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ambrin Chaudhary
 */
public class AddMenuItemTag extends IncludeTag {

	@Override
	public int doStartTag() {
		return EVAL_BODY_INCLUDE;
	}

	public void setAnchorData(Map<String, Object> anchorData) {
		_anchorData = anchorData;
	}

	public void setCssClass(String cssClass) {
		_cssClass = cssClass;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setType(AddMenuKeys.AddMenuType addMenuType) {
		_addMenuType = addMenuType;
	}

	public void setUrl(String url) {
		_url = url;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_cssClass = null;
		_id = null;
		_title = null;
		_addMenuType = AddMenuKeys.AddMenuType.DEFAULT;
		_url = null;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		List<AddMenuItem> addMenuItems =
			(List<AddMenuItem>)request.getAttribute(
				"liferay-frontend:add-menu:addMenuItems");

		if (addMenuItems != null) {
			AddMenuItem addMenuItem = new AddMenuItem(
				_anchorData, _cssClass, _id, _title, _addMenuType, _url);

			addMenuItems.add(addMenuItem);
		}
	}

	private AddMenuKeys.AddMenuType _addMenuType =
		AddMenuKeys.AddMenuType.DEFAULT;
	private Map<String, Object> _anchorData;
	private String _cssClass;
	private String _id;
	private String _title;
	private String _url;

}