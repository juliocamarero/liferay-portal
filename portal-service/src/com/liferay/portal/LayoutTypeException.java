/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portal;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * @author Brian Wing Shun Chan
 */
public class LayoutTypeException extends PortalException {

	public static final int NOT_PARENTABLE = 1;

	public static final int FIRST_LAYOUT = 2;

	public LayoutTypeException(int type) {
		_type = type;
	}

	public LayoutTypeException(int type, String layoutType) {
		_type = type;
		_layoutType = layoutType;
	}

	public int getType() {
		return _type;
	}

	public String getlayoutType() {
		return _layoutType;
	}

	private int _type;

	private String _layoutType;

}