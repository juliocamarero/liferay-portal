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

package com.liferay.portal.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ResourceTypePermission;
import com.liferay.portal.model.ResourceTypePermissionModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

/**
 * The base model implementation for the ResourceTypePermission service. Represents a row in the &quot;ResourceTypePermission&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portal.model.ResourceTypePermissionModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link ResourceTypePermissionImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ResourceTypePermissionImpl
 * @see com.liferay.portal.model.ResourceTypePermission
 * @see com.liferay.portal.model.ResourceTypePermissionModel
 * @generated
 */
public class ResourceTypePermissionModelImpl extends BaseModelImpl<ResourceTypePermission>
	implements ResourceTypePermissionModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a resource type permission model instance should use the {@link com.liferay.portal.model.ResourceTypePermission} interface instead.
	 */
	public static final String TABLE_NAME = "ResourceTypePermission";
	public static final Object[][] TABLE_COLUMNS = {
			{ "resourceTypePermissionId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "name", Types.VARCHAR },
			{ "roleId", Types.BIGINT },
			{ "actionIds", Types.BIGINT }
		};
	public static final String TABLE_SQL_CREATE = "create table ResourceTypePermission (resourceTypePermissionId LONG not null primary key,companyId LONG,groupId LONG,name VARCHAR(75) null,roleId LONG,actionIds LONG)";
	public static final String TABLE_SQL_DROP = "drop table ResourceTypePermission";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portal.model.ResourceTypePermission"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portal.model.ResourceTypePermission"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.model.ResourceTypePermission"),
			true);
	public static long COMPANYID_COLUMN_BITMASK = 1L;
	public static long GROUPID_COLUMN_BITMASK = 2L;
	public static long NAME_COLUMN_BITMASK = 4L;
	public static long ROLEID_COLUMN_BITMASK = 8L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portal.model.ResourceTypePermission"));

	public ResourceTypePermissionModelImpl() {
	}

	public long getPrimaryKey() {
		return _resourceTypePermissionId;
	}

	public void setPrimaryKey(long primaryKey) {
		setResourceTypePermissionId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return new Long(_resourceTypePermissionId);
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return ResourceTypePermission.class;
	}

	public String getModelClassName() {
		return ResourceTypePermission.class.getName();
	}

	public long getResourceTypePermissionId() {
		return _resourceTypePermissionId;
	}

	public void setResourceTypePermissionId(long resourceTypePermissionId) {
		_resourceTypePermissionId = resourceTypePermissionId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_columnBitmask |= COMPANYID_COLUMN_BITMASK;

		if (!_setOriginalCompanyId) {
			_setOriginalCompanyId = true;

			_originalCompanyId = _companyId;
		}

		_companyId = companyId;
	}

	public long getOriginalCompanyId() {
		return _originalCompanyId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_columnBitmask |= GROUPID_COLUMN_BITMASK;

		if (!_setOriginalGroupId) {
			_setOriginalGroupId = true;

			_originalGroupId = _groupId;
		}

		_groupId = groupId;
	}

	public long getOriginalGroupId() {
		return _originalGroupId;
	}

	public String getName() {
		if (_name == null) {
			return StringPool.BLANK;
		}
		else {
			return _name;
		}
	}

	public void setName(String name) {
		_columnBitmask |= NAME_COLUMN_BITMASK;

		if (_originalName == null) {
			_originalName = _name;
		}

		_name = name;
	}

	public String getOriginalName() {
		return GetterUtil.getString(_originalName);
	}

	public long getRoleId() {
		return _roleId;
	}

	public void setRoleId(long roleId) {
		_columnBitmask |= ROLEID_COLUMN_BITMASK;

		if (!_setOriginalRoleId) {
			_setOriginalRoleId = true;

			_originalRoleId = _roleId;
		}

		_roleId = roleId;
	}

	public long getOriginalRoleId() {
		return _originalRoleId;
	}

	public long getActionIds() {
		return _actionIds;
	}

	public void setActionIds(long actionIds) {
		_actionIds = actionIds;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ResourceTypePermission toEscapedModel() {
		if (_escapedModelProxy == null) {
			_escapedModelProxy = (ResourceTypePermission)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelProxyInterfaces,
					new AutoEscapeBeanHandler(this));
		}

		return _escapedModelProxy;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		if (_expandoBridge == null) {
			_expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
					ResourceTypePermission.class.getName(), getPrimaryKey());
		}

		return _expandoBridge;
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		getExpandoBridge().setAttributes(serviceContext);
	}

	@Override
	public Object clone() {
		ResourceTypePermissionImpl resourceTypePermissionImpl = new ResourceTypePermissionImpl();

		resourceTypePermissionImpl.setResourceTypePermissionId(getResourceTypePermissionId());
		resourceTypePermissionImpl.setCompanyId(getCompanyId());
		resourceTypePermissionImpl.setGroupId(getGroupId());
		resourceTypePermissionImpl.setName(getName());
		resourceTypePermissionImpl.setRoleId(getRoleId());
		resourceTypePermissionImpl.setActionIds(getActionIds());

		resourceTypePermissionImpl.resetOriginalValues();

		return resourceTypePermissionImpl;
	}

	public int compareTo(ResourceTypePermission resourceTypePermission) {
		long primaryKey = resourceTypePermission.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		ResourceTypePermission resourceTypePermission = null;

		try {
			resourceTypePermission = (ResourceTypePermission)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = resourceTypePermission.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public void resetOriginalValues() {
		ResourceTypePermissionModelImpl resourceTypePermissionModelImpl = this;

		resourceTypePermissionModelImpl._originalCompanyId = resourceTypePermissionModelImpl._companyId;

		resourceTypePermissionModelImpl._setOriginalCompanyId = false;

		resourceTypePermissionModelImpl._originalGroupId = resourceTypePermissionModelImpl._groupId;

		resourceTypePermissionModelImpl._setOriginalGroupId = false;

		resourceTypePermissionModelImpl._originalName = resourceTypePermissionModelImpl._name;

		resourceTypePermissionModelImpl._originalRoleId = resourceTypePermissionModelImpl._roleId;

		resourceTypePermissionModelImpl._setOriginalRoleId = false;

		resourceTypePermissionModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<ResourceTypePermission> toCacheModel() {
		ResourceTypePermissionCacheModel resourceTypePermissionCacheModel = new ResourceTypePermissionCacheModel();

		resourceTypePermissionCacheModel.resourceTypePermissionId = getResourceTypePermissionId();

		resourceTypePermissionCacheModel.companyId = getCompanyId();

		resourceTypePermissionCacheModel.groupId = getGroupId();

		resourceTypePermissionCacheModel.name = getName();

		String name = resourceTypePermissionCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			resourceTypePermissionCacheModel.name = null;
		}

		resourceTypePermissionCacheModel.roleId = getRoleId();

		resourceTypePermissionCacheModel.actionIds = getActionIds();

		return resourceTypePermissionCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(13);

		sb.append("{resourceTypePermissionId=");
		sb.append(getResourceTypePermissionId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", roleId=");
		sb.append(getRoleId());
		sb.append(", actionIds=");
		sb.append(getActionIds());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(22);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portal.model.ResourceTypePermission");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>resourceTypePermissionId</column-name><column-value><![CDATA[");
		sb.append(getResourceTypePermissionId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>groupId</column-name><column-value><![CDATA[");
		sb.append(getGroupId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>name</column-name><column-value><![CDATA[");
		sb.append(getName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>roleId</column-name><column-value><![CDATA[");
		sb.append(getRoleId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>actionIds</column-name><column-value><![CDATA[");
		sb.append(getActionIds());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = ResourceTypePermission.class.getClassLoader();
	private static Class<?>[] _escapedModelProxyInterfaces = new Class[] {
			ResourceTypePermission.class
		};
	private long _resourceTypePermissionId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private String _name;
	private String _originalName;
	private long _roleId;
	private long _originalRoleId;
	private boolean _setOriginalRoleId;
	private long _actionIds;
	private transient ExpandoBridge _expandoBridge;
	private long _columnBitmask;
	private ResourceTypePermission _escapedModelProxy;
}