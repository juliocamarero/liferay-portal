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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.structured.content.apio.architect.filter.Filter;

import java.util.Locale;
import java.util.Optional;

import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.core.Encoder;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Component that converts an OData {@link Filter} into a Liferay Search {@link
 * Query}.
 *
 * @author Julio Camarero
 * @review
 */
@Component(immediate = true, service = ODataQueryMapper.class)
public class ODataQueryMapper {

	public Query map(Filter filter, Locale locale) {
		if ((filter == null) || !filter.hasValue()) {
			return null;
		}

		Optional<String> optionalValue = filter.getValue();

		String filterValue = optionalValue.get();

		if (Validator.isNull(filterValue)) {
			return null;
		}

		UriInfo uriInfo = _getUriInfo(filterValue);

		FilterOption filterOption = uriInfo.getFilterOption();

		Expression expression = filterOption.getExpression();

		try {
			return (Query)expression.accept(new ODataExpressionVisitor(locale));
		}
		catch (ODataException ode) {
			throw new RuntimeException(ode);
		}
	}

	private String _encodeODataQuery(String oDataQuery) {
		return Encoder.encode(oDataQuery);
	}

	private UriInfo _getUriInfo(String filter) {
		String odataQueryFilterEncoded = "$filter=" + _encodeODataQuery(filter);

		try {
			return _oDataParser.parse(odataQueryFilterEncoded);
		}
		catch (ODataException ode) {
			String errorMessage = String.format(
				"Invalid query computed from filter and apply values. %n " +
					"OData query with encoded values: %s %n Filter: %n%s %n " +
						"Message (ODataException): %s",
				odataQueryFilterEncoded, filter, ode.getMessage());

			if (_log.isWarnEnabled()) {
				_log.warn(errorMessage, ode);
			}

			throw new RuntimeException(errorMessage, ode);
		}
		catch (Exception e) {
			String errorMessage = String.format(
				"Unexpected error parsing query formed by filter and apply " +
					"values. %n OData query with encoded values: %s %n " +
						"Filter: %s %n Message: %s",
				odataQueryFilterEncoded, filter, e.getMessage());

			if (_log.isWarnEnabled()) {
				_log.warn(errorMessage, e);
			}

			throw new RuntimeException(errorMessage, e);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ODataQueryMapper.class);

	@Reference
	private ODataParser _oDataParser;

}