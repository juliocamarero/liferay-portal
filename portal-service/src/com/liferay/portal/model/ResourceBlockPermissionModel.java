/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

/**
 * The base model interface for the ResourceBlockPermission service. Represents a row in the &quot;ResourceBlockPermission&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portal.model.impl.ResourceBlockPermissionModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portal.model.impl.ResourceBlockPermissionImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ResourceBlockPermission
 * @see com.liferay.portal.model.impl.ResourceBlockPermissionImpl
 * @see com.liferay.portal.model.impl.ResourceBlockPermissionModelImpl
 * @generated
 */
public interface ResourceBlockPermissionModel extends BaseModel<ResourceBlockPermission> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a resource block permission model instance should use the {@link ResourceBlockPermission} interface instead.
	 */

	/**
	 * Returns the primary key of this resource block permission.
	 *
	 * @return the primary key of this resource block permission
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this resource block permission.
	 *
	 * @param primaryKey the primary key of this resource block permission
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the resource block permission ID of this resource block permission.
	 *
	 * @return the resource block permission ID of this resource block permission
	 */
	public long getResourceBlockPermissionId();

	/**
	 * Sets the resource block permission ID of this resource block permission.
	 *
	 * @param resourceBlockPermissionId the resource block permission ID of this resource block permission
	 */
	public void setResourceBlockPermissionId(long resourceBlockPermissionId);

	/**
	 * Returns the resource block ID of this resource block permission.
	 *
	 * @return the resource block ID of this resource block permission
	 */
	public long getResourceBlockId();

	/**
	 * Sets the resource block ID of this resource block permission.
	 *
	 * @param resourceBlockId the resource block ID of this resource block permission
	 */
	public void setResourceBlockId(long resourceBlockId);

	/**
	 * Returns the role ID of this resource block permission.
	 *
	 * @return the role ID of this resource block permission
	 */
	public long getRoleId();

	/**
	 * Sets the role ID of this resource block permission.
	 *
	 * @param roleId the role ID of this resource block permission
	 */
	public void setRoleId(long roleId);

	/**
	 * Returns the action IDs of this resource block permission.
	 *
	 * @return the action IDs of this resource block permission
	 */
	public long getActionIds();

	/**
	 * Sets the action IDs of this resource block permission.
	 *
	 * @param actionIds the action IDs of this resource block permission
	 */
	public void setActionIds(long actionIds);

	public boolean isNew();

	public void setNew(boolean n);

	public boolean isCachedModel();

	public void setCachedModel(boolean cachedModel);

	public boolean isEscapedModel();

	public Serializable getPrimaryKeyObj();

	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	public ExpandoBridge getExpandoBridge();

	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	public Object clone();

	public int compareTo(ResourceBlockPermission resourceBlockPermission);

	public int hashCode();

	public CacheModel<ResourceBlockPermission> toCacheModel();

	public ResourceBlockPermission toEscapedModel();

	public ResourceBlockPermission toUnescapedModel();

	public String toString();

	public String toXmlString();
}