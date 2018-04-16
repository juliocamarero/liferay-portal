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

package com.liferay.frontend.taglib.clay.servlet.taglib.util;

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 */
public class NavigationItemList extends ArrayList<NavigationItem> {

	public NavigationItemList() {
		_request = null;
	}

	public NavigationItemList(HttpServletRequest request) {
		_request = request;
	}

	public void add(Consumer<NavigationItem> consumer) {
		NavigationItem navigationItem = new NavigationItem(_request);

		consumer.accept(navigationItem);

		add(navigationItem);
	}

	private final HttpServletRequest _request;

}