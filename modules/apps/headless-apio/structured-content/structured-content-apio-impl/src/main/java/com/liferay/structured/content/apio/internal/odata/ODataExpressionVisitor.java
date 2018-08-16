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

import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;

import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.edm.EdmEnumType;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.core.edm.primitivetype.EdmString;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.queryoption.expression.BinaryOperatorKind;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import org.apache.olingo.server.api.uri.queryoption.expression.Literal;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;
import org.apache.olingo.server.api.uri.queryoption.expression.MethodKind;
import org.apache.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;

/**
 * Visitor that visit the operations, literals and members of an oData query to
 * create a Liferay Search {@link Query}
 *
 * @author Julio Camarero
 * @review
 */
public class ODataExpressionVisitor implements ExpressionVisitor<Object> {

	public ODataExpressionVisitor(Locale locale) {
		_locale = locale;
	}

	@Override
	public Object visitAlias(String aliasName) {
		throw new RuntimeException("visitAlias not implemented");
	}

	@Override
	public Object visitBinaryOperator(
		BinaryOperatorKind operator, Object left, Object right) {

		if (operator == BinaryOperatorKind.EQ) {
			return _eq((String)left, right);
		}
		else {
			throw new RuntimeException(
				"Binary operator " + operator + " not implemented");
		}
	}

	@Override
	public Object visitEnum(EdmEnumType type, List<String> enumValues) {
		throw new RuntimeException("visitEnum not implemented");
	}

	@Override
	public Object visitLambdaExpression(
		String lambdaFunction, String lambdaVariable, Expression expression) {

		throw new RuntimeException("visitLambdaExpression not implemented");
	}

	@Override
	public Object visitLambdaReference(String variableName) {
		throw new RuntimeException("visitLambdaReference not implemented");
	}

	@Override
	public Object visitLiteral(Literal literal) {
		String value = literal.getText();

		if (literal.getType() instanceof EdmString) {
			value = _removeQuotes(value);
		}

		return value;
	}

	@Override
	public Object visitMember(Member member) {
		UriInfoResource resourcePath = member.getResourcePath();

		List<UriResource> uriResourceParts = resourcePath.getUriResourceParts();

		return String.valueOf(uriResourceParts.get(0));
	}

	@Override
	public Object visitMethodCall(MethodKind method, List<Object> parameters) {
		throw new RuntimeException("visitMethodCall not implemented");
	}

	@Override
	public Object visitTypeLiteral(EdmType type) {
		throw new RuntimeException("visitTypeLiteral not implemented");
	}

	@Override
	public Object visitUnaryOperator(
		UnaryOperatorKind operator, Object operand) {

		throw new RuntimeException("visitUnaryOperator not implemented");
	}

	private Query _eq(String fieldName, Object getValue) {
		String valueString = getValue.toString();

		if (valueString.equals("null")) {
			throw new RuntimeException("eq null not implemented");
		}

		String localizedFieldName = Field.getLocalizedName(_locale, fieldName);

		try {
			BooleanQuery localizedQuery = new BooleanQueryImpl();

			return localizedQuery.addTerm(
				localizedFieldName, valueString, false);
		}
		catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}

	private String _removeQuotes(String input) {
		String value = input;

		if (value.startsWith("'") && value.endsWith("'")) {
			value = value.substring(1, value.length() - 1);
		}

		if (value.contains("''")) {
			value = value.replaceAll("''", "'");
		}

		return value;
	}

	private final Locale _locale;

}