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

package com.liferay.portal.reference;

import com.liferay.portal.kernel.model.ClassedModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * This class holds a direction and lambda expression to be able to determine
 * the references for a specific entity.
 *
 * @author Mate Thurzo
 */
public class Reference<T extends ClassedModel, U extends ClassedModel> {

	public Reference(
		ReferenceSupplier<T, U> referenceSupplier, Direction direction) {

		_referenceSupplier = referenceSupplier;
		_direction = direction;
	}

	public Reference(
		MultiReferenceSupplier<T> multiReferenceSupplier, Direction direction) {

		_multiReferenceSupplier = multiReferenceSupplier;
		_direction = direction;
	}

	public boolean isInbound() {
		if (_direction != Direction.INBOUND) {
			return false;
		}

		return true;
	}

	public boolean isOutbound() {
		if (_direction != Direction.OUTBOUND) {
			return false;
		}

		return true;
	}

	public boolean isBidirectional() {
		if (_direction != Direction.BOTH) {
			return false;
		}

		return true;
	}

	public Collection<? extends ClassedModel> supply(T entity) {
		List others = new ArrayList<>();

		if (_referenceSupplier != null) {
			Optional other = _referenceSupplier.supply(entity);

			other.ifPresent(o -> others.add(o));

			return others;
		}

		if (_multiReferenceSupplier != null) {
			return _multiReferenceSupplier.supply(entity);
		}

		return Collections.emptyList();
	}

	private Direction _direction;
	private MultiReferenceSupplier<T> _multiReferenceSupplier;
	private U _other;
	private ReferenceSupplier<T, U> _referenceSupplier;

}