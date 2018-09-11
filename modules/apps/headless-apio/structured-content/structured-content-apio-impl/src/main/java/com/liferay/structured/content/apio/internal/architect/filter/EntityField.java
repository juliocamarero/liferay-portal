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
	 * @param entityFieldName - the name of the EntityField
	 * @param entityFieldType - the {@link EntityField}
	 * @review
	 */
	public EntityField(
		String entityFieldName, EntityFieldType entityFieldType) {

		_entityFieldName = entityFieldName;
		_entityFieldType = entityFieldType;
	}

	/**
	 * Returns the name of the <code>EntityField</code>
	 *
	 * @return the name of the <code>EntityField</code>
	 * @review
	 */
	public String getEntityFieldName() {
		return _entityFieldName;
	}

	/**
	 * Returns the {@link EntityFieldType} of the <code>EntityField</code>
	 *
	 * @return the {@link EntityFieldType}
	 * @review
	 */
	public EntityFieldType getEntityFieldType() {
		return _entityFieldType;
	}

	public enum EntityFieldType {

		DATE, STRING

	}

	private final String _entityFieldName;
	private final EntityFieldType _entityFieldType;

}