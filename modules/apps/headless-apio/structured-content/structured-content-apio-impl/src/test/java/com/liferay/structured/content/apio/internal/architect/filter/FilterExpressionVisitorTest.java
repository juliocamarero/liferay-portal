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

import com.liferay.structured.content.apio.architect.filter.expression.BinaryExpression;
import com.liferay.structured.content.apio.architect.filter.expression.LiteralExpression;
import com.liferay.structured.content.apio.internal.architect.filter.expression.LiteralExpressionImpl;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Ruben Pulido
 */
public class FilterExpressionVisitorTest {

	@BeforeClass
	public static void setUpClass() {
		_filterExpressionVisitor = new FilterExpressionVisitor();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testVisitBinaryExpressionOperatorWithEqualOperator() {
		String propertyName = "title";
		String propertyValue = "title1";

		Map<String, Object> result =
			(Map<String, Object>)_filterExpressionVisitor.
				visitBinaryExpressionOperation(
					BinaryExpression.Operation.EQ, propertyName, propertyValue);

		Assert.assertEquals(result.toString(), 1, result.size());
		Assert.assertEquals(propertyValue, result.get(propertyName));
	}

	@Test
	public void testVisitLiteralExpressionStringMultipleDoubleSingleQuotes() {
		LiteralExpression literal = new LiteralExpressionImpl(
			"'L''Oreal and L''Oreal'", LiteralExpression.Type.STRING);

		String result = (String)_filterExpressionVisitor.visitLiteralExpression(
			literal);

		Assert.assertEquals("L'Oreal and L'Oreal", result);
	}

	@Test
	public void testVisitLiteralExpressionStringOneSingleQuotes() {
		LiteralExpression literal = new LiteralExpressionImpl(
			"'L'Oreal'", LiteralExpression.Type.STRING);

		String result = (String)_filterExpressionVisitor.visitLiteralExpression(
			literal);

		Assert.assertEquals("L'Oreal", result);
	}

	@Test
	public void testVisitLiteralExpressionStringTwoSingleQuotes() {
		LiteralExpression literal = new LiteralExpressionImpl(
			"'L''Oreal'", LiteralExpression.Type.STRING);

		String result = (String)_filterExpressionVisitor.visitLiteralExpression(
			literal);

		Assert.assertEquals("L'Oreal", result);
	}

	@Test
	public void testVisitLiteralExpressionStringWithLeadingAndTrailingSingleQuotes() {
		LiteralExpression literal = new LiteralExpressionImpl(
			"'LOreal'", LiteralExpression.Type.STRING);

		String result = (String)_filterExpressionVisitor.visitLiteralExpression(
			literal);

		Assert.assertEquals("LOreal", result);
	}

	private static FilterExpressionVisitor _filterExpressionVisitor;

}