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
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.structured.content.apio.architect.filter.expression.Binary.Operation;
import com.liferay.structured.content.apio.architect.filter.expression.ExpressionVisitor;
import com.liferay.structured.content.apio.architect.filter.expression.Literal;
import com.liferay.structured.content.apio.architect.filter.expression.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Visitor that visits the operations, literals and members of an oData query to
 * create a Filter Map.
 *
 * @author Julio Camarero
 * @review
 */
public class FilterExpressionVisitor implements ExpressionVisitor<Object> {

	@Override
	public Object visitBinaryOperator(
		Operation operator, Object left, Object right) {

		if (operator == Operation.EQ) {
			return _createFilterMap((String)left, right);
		}
		else {
			throw new InvalidFilterException(
				"Binary operator " + operator + " not implemented");
		}
	}

	@Override
	public Object visitLiteral(Literal literal) {
		if (Literal.Type.STRING.equals(literal.getType())) {
			return _processStringLiteral(literal.getText());
		}

		return literal.getText();
	}

	@Override
	public Object visitMember(Member member) {
		List<String> resourcePath = member.getResourcePath();

		return String.valueOf(resourcePath.get(0));
	}

	private Map<String, Object> _createFilterMap(String left, Object right) {
		Map<String, Object> filterMap = new HashMap<>();

		filterMap.put(left, right);

		return filterMap;
	}

	private Object _processStringLiteral(String literal) {
		String unquotedLiteral = StringUtil.unquote(literal);

		return StringUtil.replace(
			unquotedLiteral, StringPool.DOUBLE_APOSTROPHE,
			StringPool.APOSTROPHE);
	}

}