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

package com.liferay.structured.content.apio.architect.entity;

import com.liferay.portal.kernel.util.Validator;

/**
 * Models a <code>EntityField</code>.
 *
 * @author Cristina González
 * @review
 */
public class EntityField {

	/**
	 * Creates a new <code>EntityField</code>
	 *
	 * @param name - the name of the EntityField
	 * @param type - the {@link Type}
	 */
	public EntityField(String name, Type type) {
		if (Validator.isNull(name)) {
			throw new IllegalArgumentException("Name is null");
		}

		if (type == null) {
			throw new IllegalArgumentException("Type is null");
		}

		_name = name;
		_type = type;
	}

	/**
	 * Returns the name of the <code>EntityField</code>
	 *
	 * @return the name of the <code>EntityField</code>
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Returns the {@link Type} of the <code>EntityField</code>
	 *
	 * @return the {@link Type}
	 */
	public Type getType() {
		return _type;
	}

	public enum Type {

		DATE, STRING

	}

	private final String _name;
	private final Type _type;

}