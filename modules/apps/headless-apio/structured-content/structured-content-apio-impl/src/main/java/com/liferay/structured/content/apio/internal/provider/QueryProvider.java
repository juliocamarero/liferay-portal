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
import com.liferay.structured.content.apio.architect.query.Query;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Cristina González
 */
@Component
public class QueryProvider implements Provider<Query> {

	@Override
	public Query createContext(HttpServletRequest httpServletRequest) {
		return () -> Optional.ofNullable(
			httpServletRequest.getParameter("filter")
		).filter(
			string -> string.startsWith("title=")
		).map(
			string -> string.substring(6)
		);

		//&filter=(name eq 'Jon')
	}

}