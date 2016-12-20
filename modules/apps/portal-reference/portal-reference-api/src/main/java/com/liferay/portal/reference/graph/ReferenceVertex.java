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

package com.liferay.portal.reference.graph;

/**
 * @author Mate Thurzo
 */
public class ReferenceVertex {

	public ReferenceVertex() {
	}

	public ReferenceVertex(Object object) {
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ReferenceVertex)) {
			return false;
		}

		ReferenceVertex vertex = (ReferenceVertex)obj;

		if (vertex.getUuid().equals(getUuid()) &&
			vertex.getType().equals(getType())) {

			return true;
		}

		return false;
	}

	public String getType() {
		return _type;
	}

	public String getUuid() {
		return _uuid;
	}

	public void setType(String type) {
		_type = type;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	private String _type;
	private String _uuid;

}