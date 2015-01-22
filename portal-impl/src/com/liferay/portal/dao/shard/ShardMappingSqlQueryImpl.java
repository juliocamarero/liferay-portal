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

package com.liferay.portal.dao.shard;

import com.liferay.portal.dao.jdbc.spring.MappingSqlQueryImpl;
import com.liferay.portal.kernel.dao.jdbc.RowMapper;

/**
 * @author Alexander Chow
 */
public class ShardMappingSqlQueryImpl<T> extends MappingSqlQueryImpl<T> {

	public ShardMappingSqlQueryImpl(
		String sql, int[] types, RowMapper<T> rowMapper) {

		super(ShardDataSource.getInstance(), sql, types, rowMapper);
	}

}