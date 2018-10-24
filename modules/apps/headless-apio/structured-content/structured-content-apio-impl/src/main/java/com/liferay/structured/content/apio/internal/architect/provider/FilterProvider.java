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

package com.liferay.structured.content.apio.internal.architect.provider;

import com.liferay.apio.architect.provider.Provider;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.odata.filter.Filter;
import com.liferay.portal.odata.filter.FilterParser;
import com.liferay.portal.odata.filter.InvalidFilterException;
import com.liferay.structured.content.apio.internal.architect.filter.StructuredContentEntityModel;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Cristina González
 */
@Component(service = Provider.class)
public class FilterProvider implements Provider<Filter> {

	@Override
	public Filter createContext(HttpServletRequest httpServletRequest) {
		String filterString = httpServletRequest.getParameter("filter");

		if (Validator.isNull(filterString)) {
			return Filter.emptyFilter();
		}

		try {
			return new Filter(_filterParser.parse(filterString));
		}
		catch (Exception e) {
			throw new InvalidFilterException(
				String.format(
					"Invalid query computed from filter '%s': %s", filterString,
					e.getMessage()),
				e);
		}
	}

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(entity.model.name=" + StructuredContentEntityModel.NAME + ")",
		unbind = "unbind"
	)
	public void setFilterParser(FilterParser filterParser) {
		if (_log.isInfoEnabled()) {
			_log.info("Binding " + filterParser);
		}

		_filterParser = filterParser;
	}

	public void unbind(FilterParser filterParser) {
		if (_log.isInfoEnabled()) {
			_log.info("Unbinding " + filterParser);
		}

		_filterParser = null;
	}

	private static final Log _log = LogFactoryUtil.getLog(FilterProvider.class);

	private FilterParser _filterParser;

}