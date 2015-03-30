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

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.SelectableEntry;
import com.liferay.portal.util.PortalUtil;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

public class ElementSelectorTag extends IncludeTag {

	public void setElements(List<SelectableEntry> elements) {
		_elements = elements;
	}

	public void setHiddenInput(String hiddenInput) {
		_hiddenInput = hiddenInput;
	}

	public void setId(String id) {
		_id = id;
	}

	@Override
	protected void cleanUp() {
		_elements = null;
		_hiddenInput = "";
		_id = null;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		String id = _id;

		if (Validator.isNull(id)) {
			id = PortalUtil.generateRandomKey(
				request,
				"taglib_ui_element_selector_page") + StringPool.UNDERLINE;
		}

		request.setAttribute("liferay-ui:element-selector:elements", _elements);
		request.setAttribute("liferay-ui:element-selector:hiddenInput", _hiddenInput);
		request.setAttribute("liferay-ui:element-selector:id", id);
	}

	private static final String _PAGE =
		"/html/taglib/ui/element_selector/page.jsp";

	private List<SelectableEntry> _elements;
	private String _hiddenInput;
	private String _id;
}