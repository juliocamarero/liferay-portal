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

import java.util.Locale;
import java.util.function.Function;

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
	 * @param  name - the name of the EntityField
	 * @param  type - the {@link Type}
	 * @param  filterableAndSortableFieldNameFunction - the {@link Function} to
	 *         convert the entity field name to a filterable/sortable field name
	 *         given a locale
	 * @review
	 */
	public EntityField(
		String name, Type type,
		Function<Locale, String> filterableAndSortableFieldNameFunction) {

		this(
			name, type, filterableAndSortableFieldNameFunction,
			filterableAndSortableFieldNameFunction);
	}

	/**
	 * Creates a new <code>EntityField</code>
	 *
	 * @param  name - the name of the EntityField
	 * @param  type - the {@link Type}
	 * @param  sortableFieldNameFunction - the {@link Function} to convert the
	 *         entity field name to a sortable field name given a locale
	 * @param  filterableFieldNameFunction - the {@link Function} to convert the
	 *         entity field name to a filterable field name given a locale
	 * @review
	 */
	public EntityField(
		String name, Type type,
		Function<Locale, String> sortableFieldNameFunction,
		Function<Locale, String> filterableFieldNameFunction) {

		if (Validator.isNull(name)) {
			throw new IllegalArgumentException("Name is null");
		}

		if (type == null) {
			throw new IllegalArgumentException("Type is null");
		}

		if (sortableFieldNameFunction == null) {
			throw new IllegalArgumentException(
				"Sortable field name function is null");
		}

		if (filterableFieldNameFunction == null) {
			throw new IllegalArgumentException(
				"Filterable field name function is null");
		}

		_name = name;
		_type = type;
		_sortableNameFunction = sortableFieldNameFunction;
		_filterableFieldNameFunction = filterableFieldNameFunction;
	}

	/**
	 * Returns the filterable name of the <code>EntityField</code>
	 *
	 * @param  locale
	 * @return the filterable name of the <code>EntityField</code>
	 * @review
	 */
	public String getFilterableName(Locale locale) {
		return _filterableFieldNameFunction.apply(locale);
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
	 * Returns the sortable name of the <code>EntityField</code>
	 *
	 * @param  locale
	 * @return the sortable name of the <code>EntityField</code>
	 * @review
	 */
	public String getSortableName(Locale locale) {
		return _sortableNameFunction.apply(locale);
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

	private final Function<Locale, String> _filterableFieldNameFunction;
	private final String _name;
	private final Function<Locale, String> _sortableNameFunction;
	private final Type _type;

}