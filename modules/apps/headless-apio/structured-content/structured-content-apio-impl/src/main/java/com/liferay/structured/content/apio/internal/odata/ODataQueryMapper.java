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

package com.liferay.structured.content.apio.internal.odata;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.structured.content.apio.architect.filter.Filter;

import java.util.Locale;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;

/**
 * @author Julio Camarero
 */
@Component(immediate = true, service = ODataQueryMapper.class)
public class ODataQueryMapper {

	public Query map(Filter filter, Locale locale) {
		if ((filter == null) || !filter.hasValue()) {
			return null;
		}

		Optional<String> optionalValue = filter.getValue();

		String filterValue = optionalValue.get();

		String title = filterValue.substring(
			filterValue.indexOf(StringPool.APOSTROPHE),
			filterValue.lastIndexOf(StringPool.APOSTROPHE));

		System.out.println("title: " + title);

		String localizedFieldName = Field.getLocalizedName(locale, "title");

		try {
			BooleanQuery localizedQuery = new BooleanQueryImpl();

			return localizedQuery.addTerm(localizedFieldName, title, false);
		}
		catch (ParseException pe) {
			_log.error(pe, pe);

			return null; // TODO: Propagate Exception
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ODataQueryMapper.class);

}