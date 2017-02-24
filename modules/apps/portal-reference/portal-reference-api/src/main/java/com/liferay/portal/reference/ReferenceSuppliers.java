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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Mate Thurzo
 */
public class ReferenceSuppliers<T extends ClassedModel> {

	public static ReferenceSuppliers create() {
		return new ReferenceSuppliers();
	}

	public <U extends ClassedModel> ReferenceSuppliers<T> with(
		ReferenceSupplier<T, U> referenceSupplier, Direction direction) {

		_add(referenceSupplier, direction);

		return this;
	}

	public ReferenceSuppliers<T> with(
		MultiReferenceSupplier<T> multiReferenceSupplier, Direction direction) {

		_add(multiReferenceSupplier, direction);

		return this;
	}

	public ReferenceSuppliers<T> withBidirectional(
		MultiReferenceSupplier<T> multiReferenceSupplier) {

		_add(multiReferenceSupplier, Direction.BOTH);

		return this;
	}

	public <U extends ClassedModel> ReferenceSuppliers<T> withBidirectional(
		ReferenceSupplier<T, U> referenceSupplier) {

		_add(referenceSupplier, Direction.BOTH);

		return this;
	}

	public Stream<Reference<T, ?>> referenceStream() {
		return _references.stream();
	}

	public ReferenceSuppliers<T> withInbound(
		MultiReferenceSupplier<T> multiReferenceSupplier) {

		_add(multiReferenceSupplier, Direction.INBOUND);

		return this;
	}

	public <U extends ClassedModel> ReferenceSuppliers<T> withInbound(
		ReferenceSupplier<T, U> referenceSupplier) {

		_add(referenceSupplier, Direction.INBOUND);

		return this;
	}

	public ReferenceSuppliers<T> withOutbound(
		MultiReferenceSupplier<T> multiReferenceSupplier) {

		_add(multiReferenceSupplier, Direction.OUTBOUND);

		return this;
	}

	public <U extends ClassedModel> ReferenceSuppliers withOutbound(
		ReferenceSupplier<T, U> referenceSupplier) {

		_add(referenceSupplier, Direction.OUTBOUND);

		return this;
	}

	private ReferenceSuppliers() {
		_references = new HashSet<>();
	}

	private void _add(
		MultiReferenceSupplier<T> multiReferenceSupplier, Direction direction) {

		Reference<T, ?> reference = new Reference<>(
			multiReferenceSupplier, direction);

		_references.add(reference);
	}

	private <U extends ClassedModel> void _add(
		ReferenceSupplier<T, U> referenceSupplier, Direction direction) {

		Reference<T, U> reference = new Reference<>(
			referenceSupplier, direction);

		_references.add(reference);
	}

	private final Set<Reference<T, ?>> _references;

}