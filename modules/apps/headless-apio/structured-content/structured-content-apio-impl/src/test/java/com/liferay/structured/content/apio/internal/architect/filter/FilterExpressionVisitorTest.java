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

import com.liferay.structured.content.apio.architect.filter.expression.Binary;
import com.liferay.structured.content.apio.architect.filter.expression.Literal;
import com.liferay.structured.content.apio.internal.architect.filter.expression.LiteralImpl;

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
	public void testVisitBinaryOperatorWithEqualOperator() {
		String propertyName = "title";
		String propertyValue = "title1";

		Map<String, Object> result =
			(Map<String, Object>)_filterExpressionVisitor.visitBinaryOperator(
				Binary.Operation.EQ, propertyName, propertyValue);

		Assert.assertEquals(result.toString(), 1, result.size());
		Assert.assertEquals(propertyValue, result.get(propertyName));
	}

	@Test
	public void testVisitLiteralStringMultipleDoubleSingleQuotes() {
		Literal literal = new LiteralImpl(
			"'L''Oreal and L''Oreal'", Literal.Type.STRING);

		String result = (String)_filterExpressionVisitor.visitLiteral(literal);

		Assert.assertEquals("L'Oreal and L'Oreal", result);
	}

	@Test
	public void testVisitLiteralStringOneSingleQuotes() {
		Literal literal = new LiteralImpl("'L'Oreal'", Literal.Type.STRING);

		String result = (String)_filterExpressionVisitor.visitLiteral(literal);

		Assert.assertEquals("L'Oreal", result);
	}

	@Test
	public void testVisitLiteralStringTwoSingleQuotes() {
		Literal literal = new LiteralImpl("'L''Oreal'", Literal.Type.STRING);

		String result = (String)_filterExpressionVisitor.visitLiteral(literal);

		Assert.assertEquals("L'Oreal", result);
	}

	@Test
	public void testVisitLiteralStringWithLeadingAndTrailingSingleQuotes() {
		Literal literal = new LiteralImpl("'LOreal'", Literal.Type.STRING);

		String result = (String)_filterExpressionVisitor.visitLiteral(literal);

		Assert.assertEquals("LOreal", result);
	}

	private static FilterExpressionVisitor _filterExpressionVisitor;

}