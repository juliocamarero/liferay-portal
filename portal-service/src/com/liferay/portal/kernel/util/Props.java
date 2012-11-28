/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.configuration.Filter;

import java.util.Properties;

/**
 * @author Brian Wing Shun Chan
 */
public interface Props {

	public boolean contains(String key);

	public String get(String key);

	public String get(String key, Filter filter);

	public String[] getArray(String key);

	public String[] getArray(String key, Filter filter);

	public Properties getProperties();

	public Properties getProperties(String prefix, boolean removePrefix);

	public String[] getStringArray(String name, String delimiter);

}