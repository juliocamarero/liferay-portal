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

package com.liferay.structured.content.apio.internal.architect.resource;

import com.liferay.structured.content.apio.architect.filter.Filter;
import com.liferay.structured.content.apio.architect.filter.expression.BinaryExpression;
import com.liferay.structured.content.apio.architect.filter.expression.LiteralExpression;
import com.liferay.structured.content.apio.internal.architect.filter.expression.BinaryExpressionImpl;
import com.liferay.structured.content.apio.internal.architect.filter.expression.LiteralExpressionImpl;
import com.liferay.structured.content.apio.internal.architect.filter.expression.MemberExpressionImpl;

import java.util.Collections;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author David Arques
 */
public class StructuredContentNestedCollectionResourceTest {

	@Test
	public void testGetFilterMapWithExistingProperty() {
		Filter filter = new Filter(
			new BinaryExpressionImpl(
				new MemberExpressionImpl(Collections.singletonList("title")),
				BinaryExpression.Operation.EQ,
				new LiteralExpressionImpl(
					"Title Value", LiteralExpression.Type.STRING)));

		Map<String, Object> filterMap = _resource.getFilterMap(filter);

		Assert.assertEquals("Title Value", filterMap.get("title"));
	}

	@Test
	public void testGetFilterMapWithNonexistingProperty() {
		Filter filter = new Filter(
			new BinaryExpressionImpl(
				new MemberExpressionImpl(Collections.singletonList("title")),
				BinaryExpression.Operation.EQ,
				new LiteralExpressionImpl(
					"Title Value", LiteralExpression.Type.STRING)));

		Map<String, Object> filterMap = _resource.getFilterMap(filter);

		Assert.assertNull(filterMap.get("nonexistingProperty"));
	}

	private static final StructuredContentNestedCollectionResource _resource =
		new StructuredContentNestedCollectionResource();

}