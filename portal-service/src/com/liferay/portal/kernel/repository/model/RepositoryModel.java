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

package com.liferay.portal.kernel.repository.model;

import java.io.Serializable;
import java.util.Map;

import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.GroupedModel;
import com.liferay.portal.model.StagedModel;

/**
 * @author Alexander Chow
 */
public interface RepositoryModel<T>
	extends StagedModel, ClassedModel, Serializable {

	public Map<String, Serializable> getAttributes();

	public Object getModel();

	public long getPrimaryKey();

	public boolean isEscapedModel();

	public T toEscapedModel();

	public T toUnescapedModel();

}