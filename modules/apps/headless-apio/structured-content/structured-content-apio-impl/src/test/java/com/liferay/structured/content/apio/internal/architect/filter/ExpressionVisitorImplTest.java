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
import org.junit.Test;

/**
 * @author Ruben Pulido
 */
public class ExpressionVisitorImplTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testVisitBinaryExpressionOperatorWithEqualOperator() {
		String left = "title";
		String right = "title1";

		Map<String, Object> map =
			(Map<String, Object>)_expressionVisitorImpl.
				visitBinaryExpressionOperation(
					BinaryExpression.Operation.EQ, left, right);

		Assert.assertEquals(map.toString(), 1, map.size());
		Assert.assertEquals(right, map.get(left));
	}

	@Test
	public void testVisitLiteralExpressionStringMultipleDoubleSingleQuotes() {
		LiteralExpression literal = new LiteralExpressionImpl(
			"'L''Oreal and L''Oreal'", LiteralExpression.Type.STRING);

		Assert.assertEquals(
			"L'Oreal and L'Oreal",
			_expressionVisitorImpl.visitLiteralExpression(literal));
	}

	@Test
	public void testVisitLiteralExpressionStringOneSingleQuotes() {
		LiteralExpression literal = new LiteralExpressionImpl(
			"'L'Oreal'", LiteralExpression.Type.STRING);

		Assert.assertEquals(
			"L'Oreal", _expressionVisitorImpl.visitLiteralExpression(literal));
	}

	@Test
	public void testVisitLiteralExpressionStringTwoSingleQuotes() {
		LiteralExpression literal = new LiteralExpressionImpl(
			"'L''Oreal'", LiteralExpression.Type.STRING);

		Assert.assertEquals(
			"L'Oreal", _expressionVisitorImpl.visitLiteralExpression(literal));
	}

	@Test
	public void testVisitLiteralExpressionStringWithLeadingAndTrailingSingleQuotes() {
		LiteralExpression literal = new LiteralExpressionImpl(
			"'LOreal'", LiteralExpression.Type.STRING);

		Assert.assertEquals(
			"LOreal", _expressionVisitorImpl.visitLiteralExpression(literal));
	}

	private static final ExpressionVisitorImpl _expressionVisitorImpl =
		new ExpressionVisitorImpl();

}