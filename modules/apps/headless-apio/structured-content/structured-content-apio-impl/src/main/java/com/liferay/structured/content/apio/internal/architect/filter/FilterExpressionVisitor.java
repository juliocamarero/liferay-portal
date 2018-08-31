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

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.structured.content.apio.architect.filter.expression.BinaryExpression.Operation;
import com.liferay.structured.content.apio.architect.filter.expression.ExpressionVisitor;
import com.liferay.structured.content.apio.architect.filter.expression.LiteralExpression;
import com.liferay.structured.content.apio.architect.filter.expression.MemberExpression;

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
	public Object visitBinaryExpressionOperation(
		Operation operation, Object left, Object right) {

		if (operation == Operation.EQ) {
			return _createFilterMap((String)left, right);
		}
		else {
			throw new InvalidFilterException(
				"BinaryExpression operator " + operation + " not implemented");
		}
	}

	@Override
	public Object visitLiteralExpression(LiteralExpression literalExpression) {
		if (LiteralExpression.Type.STRING.equals(literalExpression.getType())) {
			return _processStringLiteralExpression(literalExpression.getText());
		}

		return literalExpression.getText();
	}

	@Override
	public Object visitMemberExpression(MemberExpression memberExpression) {
		List<String> resourcePath = memberExpression.getResourcePath();

		return String.valueOf(resourcePath.get(0));
	}

	private static String _removeLeadingAndTrailingSingleQuotes(String s) {
		if (Validator.isNull(s) || (s.length() == 1)) {
			return s;
		}

		if ((s.charAt(0) == CharPool.APOSTROPHE) &&
			(s.charAt(s.length() - 1) == CharPool.APOSTROPHE)) {

			return s.substring(1, s.length() - 1);
		}

		return s;
	}

	private Map<String, Object> _createFilterMap(String left, Object right) {
		Map<String, Object> filterMap = new HashMap<>();

		filterMap.put(left, right);

		return filterMap;
	}

	private Object _processStringLiteralExpression(String literal) {
		String unquotedLiteral = _removeLeadingAndTrailingSingleQuotes(literal);

		return StringUtil.replace(
			unquotedLiteral, StringPool.DOUBLE_APOSTROPHE,
			StringPool.APOSTROPHE);
	}

}