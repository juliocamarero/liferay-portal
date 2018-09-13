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

package com.liferay.structured.content.apio.internal.architect.filter;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.structured.content.apio.architect.entity.EntityField;
import com.liferay.structured.content.apio.architect.filter.expression.BinaryExpression;
import com.liferay.structured.content.apio.architect.filter.expression.ExpressionVisitor;
import com.liferay.structured.content.apio.architect.filter.expression.LiteralExpression;
import com.liferay.structured.content.apio.architect.filter.expression.MemberExpression;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * @author Julio Camarero
 */
public class ExpressionVisitorImpl implements ExpressionVisitor<Object> {

	public ExpressionVisitorImpl(
		Locale locale,
		StructuredContentSingleEntitySchemaBasedEdmProvider
			structuredContentSingleEntitySchemaBasedEdmProvider) {

		_locale = locale;
		_structuredContentSingleEntitySchemaBasedEdmProvider =
			structuredContentSingleEntitySchemaBasedEdmProvider;
	}

	@Override
	public BooleanClause<Query> visitBinaryExpressionOperation(
		BinaryExpression.Operation operation, Object left, Object right) {

		if (operation == BinaryExpression.Operation.EQ) {
			return _getBooleanClause((EntityField)left, right, _locale);
		}
		else {
			throw new UnsupportedOperationException(
				"Unsupported method visitBinaryExpressionOperation with " +
					"operation " + operation);
		}
	}

	@Override
	public Object visitLiteralExpression(LiteralExpression literalExpression) {
		if (Objects.equals(
				LiteralExpression.Type.STRING, literalExpression.getType())) {

			return _normalizeLiteral(literalExpression.getText());
		}

		return literalExpression.getText();
	}

	@Override
	public Object visitMemberExpression(MemberExpression memberExpression) {
		List<String> resourcePath = memberExpression.getResourcePath();

		Map<String, EntityField> entityFieldsMap =
			_structuredContentSingleEntitySchemaBasedEdmProvider.
				getEntityFieldsMap();

		return entityFieldsMap.get(resourcePath.get(0));
	}

	private BooleanClause<Query> _getBooleanClause(
		EntityField entityField, Object fieldValue, Locale locale) {

		TermQuery termQuery = new TermQueryImpl(
			entityField.getSortableName(locale), String.valueOf(fieldValue));

		return BooleanClauseFactoryUtil.create(
			termQuery, BooleanClauseOccur.MUST.getName());
	}

	private Object _normalizeLiteral(String literal) {
		literal = StringUtil.toLowerCase(literal);

		literal = StringUtil.unquote(literal);

		return StringUtil.replace(
			literal, StringPool.DOUBLE_APOSTROPHE, StringPool.APOSTROPHE);
	}

	private final Locale _locale;
	private final StructuredContentSingleEntitySchemaBasedEdmProvider
		_structuredContentSingleEntitySchemaBasedEdmProvider;

}