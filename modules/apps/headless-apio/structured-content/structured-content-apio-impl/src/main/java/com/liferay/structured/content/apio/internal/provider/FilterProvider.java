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

package com.liferay.structured.content.apio.internal.provider;

import com.liferay.apio.architect.provider.Provider;
import com.liferay.structured.content.apio.architect.filter.Filter;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Cristina González
 */
@Component
public class FilterProvider implements Provider<Filter> {

	@Override
	public Filter createContext(HttpServletRequest httpServletRequest) {
		return new Filter(httpServletRequest.getParameter("filter"));
	}

}