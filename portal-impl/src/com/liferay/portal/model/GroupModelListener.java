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

package com.liferay.portal.model;

import com.liferay.portal.service.persistence.GroupUtil;

import java.util.Collections;
import java.util.Set;

/**
 * @author Andrew Betts
 */
public class GroupModelListener extends UserCollectionReindexListener<Group> {

	@Override
	protected Set<String> getTableMapperClasses() {
		return _TABLE_MAPPER_CLASSES;
	}

	@Override
	protected long[] getUserIds(Object classPK) {
		return GroupUtil.getUserGroupPrimaryKeys((Long)classPK);
	}

	@Override
	protected boolean isAssociationReindex() {
		return true;
	}

	private static final Set<String> _TABLE_MAPPER_CLASSES =
		Collections.singleton(Role.class.getName());

}