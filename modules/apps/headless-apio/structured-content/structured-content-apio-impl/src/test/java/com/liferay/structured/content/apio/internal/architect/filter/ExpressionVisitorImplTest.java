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

import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.QueryTerm;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.structured.content.apio.architect.filter.expression.BinaryExpression;
import com.liferay.structured.content.apio.architect.filter.expression.LiteralExpression;
import com.liferay.structured.content.apio.internal.architect.filter.expression.LiteralExpressionImpl;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Ruben Pulido
 */
public class ExpressionVisitorImplTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testVisitBinaryExpressionOperationWithEqualOperation() {
		Map<String, EntityField> entityFieldsMap =
			_baseSingleEntitySchemaBasedEdmProvider.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("title");

		String value = "title1";

		BooleanClause<Query> queryBooleanClause =
			_expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.EQ, entityField, value);

		Assert.assertEquals(
			BooleanClauseOccur.MUST,
			queryBooleanClause.getBooleanClauseOccur());

		TermQueryImpl termQuery = (TermQueryImpl)queryBooleanClause.getClause();

		QueryTerm queryTerm = termQuery.getQueryTerm();

		Assert.assertEquals(
			entityField.getEntityFieldName(), queryTerm.getField());
		Assert.assertEquals(value, queryTerm.getValue());
	}

	@Test
	public void testVisitLiteralExpressionWithDoubleSingleQuotes() {
		LiteralExpression literalExpression = new LiteralExpressionImpl(
			"'L''Oreal'", LiteralExpression.Type.STRING);

		Assert.assertEquals(
			"L'Oreal",
			_expressionVisitorImpl.visitLiteralExpression(literalExpression));
	}

	@Test
	public void testVisitLiteralExpressionWithMultipleDoubleSingleQuotes() {
		LiteralExpression literalExpression = new LiteralExpressionImpl(
			"'L''Oreal and L''Oreal'", LiteralExpression.Type.STRING);

		Assert.assertEquals(
			"L'Oreal and L'Oreal",
			_expressionVisitorImpl.visitLiteralExpression(literalExpression));
	}

	@Test
	public void testVisitLiteralExpressionWithOneSingleQuote() {
		LiteralExpression literalExpression = new LiteralExpressionImpl(
			"'L'Oreal'", LiteralExpression.Type.STRING);

		Assert.assertEquals(
			"L'Oreal",
			_expressionVisitorImpl.visitLiteralExpression(literalExpression));
	}

	@Test
	public void testVisitLiteralExpressionWithSurroundingSingleQuotes() {
		LiteralExpression literalExpression = new LiteralExpressionImpl(
			"'LOreal'", LiteralExpression.Type.STRING);

		Assert.assertEquals(
			"LOreal",
			_expressionVisitorImpl.visitLiteralExpression(literalExpression));
	}

	private static final BaseSingleEntitySchemaBasedEdmProvider
		_baseSingleEntitySchemaBasedEdmProvider =
			new BaseSingleEntitySchemaBasedEdmProvider() {

				@Override
				public Map<String, EntityField> getEntityFieldsMap() {
					return Stream.of(
						new EntityField(
							"title", EntityField.EntityFieldType.STRING,
							locale -> "title")
					).collect(
						Collectors.toMap(
							EntityField::getEntityFieldName,
							Function.identity())
					);
				}

				@Override
				public String getName() {
					return "SomeEntityName";
				}

			};

	private static final ExpressionVisitorImpl _expressionVisitorImpl =
		new ExpressionVisitorImpl(
			LocaleUtil.getDefault(), _baseSingleEntitySchemaBasedEdmProvider);

}