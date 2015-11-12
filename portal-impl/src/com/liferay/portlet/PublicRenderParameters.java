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

package com.liferay.portlet;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Julio Camarero
 */
public class PublicRenderParameters extends HashMap<String, String[]> {

	public PublicRenderParameters(
		Map<String, String[]> map1, Map<String, String[]> map2) {

		super(map1);

		_map2 = map2;
	}

	@Override
	public void clear() {
		super.clear();

		if (_map2 != null) {
			_map2.clear();
		}
	}

	@Override
	public String[] put(String key, String[] value) {
		if (_map2 != null) {
			_map2.put(key, value);
		}

		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends String[]> m) {
		super.putAll(m);

		if (_map2 != null) {
			_map2.putAll(m);
		}
	}

	@Override
	public String[] remove(Object key) {
		if (_map2 != null) {
			_map2.remove(key);
		}

		return super.remove(key);
	}

	private Map<String, String[]> _map2;

}