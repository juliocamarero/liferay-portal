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

package com.liferay.portal.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ResourceBlock;
import com.liferay.portal.model.ResourceBlockModel;
import com.liferay.portal.model.ResourceBlockSoap;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the ResourceBlock service. Represents a row in the &quot;ResourceBlock&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portal.model.ResourceBlockModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link ResourceBlockImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ResourceBlockImpl
 * @see com.liferay.portal.model.ResourceBlock
 * @see com.liferay.portal.model.ResourceBlockModel
 * @generated
 */
@JSON(strict = true)
public class ResourceBlockModelImpl extends BaseModelImpl<ResourceBlock>
	implements ResourceBlockModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a resource block model instance should use the {@link com.liferay.portal.model.ResourceBlock} interface instead.
	 */
	public static final String TABLE_NAME = "ResourceBlock";
	public static final Object[][] TABLE_COLUMNS = {
			{ "resourceBlockId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "name", Types.VARCHAR },
			{ "permissionsHash", Types.VARCHAR },
			{ "referenceCount", Types.BIGINT }
		};
	public static final String TABLE_SQL_CREATE = "create table ResourceBlock (resourceBlockId LONG not null primary key,companyId LONG,groupId LONG,name VARCHAR(75) null,permissionsHash VARCHAR(75) null,referenceCount LONG)";
	public static final String TABLE_SQL_DROP = "drop table ResourceBlock";
	public static final String ORDER_BY_JPQL = " ORDER BY resourceBlock.resourceBlockId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY ResourceBlock.resourceBlockId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portal.model.ResourceBlock"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portal.model.ResourceBlock"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.model.ResourceBlock"),
			true);
	public static long COMPANYID_COLUMN_BITMASK = 1L;
	public static long GROUPID_COLUMN_BITMASK = 2L;
	public static long NAME_COLUMN_BITMASK = 4L;
	public static long PERMISSIONSHASH_COLUMN_BITMASK = 8L;
	public static long RESOURCEBLOCKID_COLUMN_BITMASK = 16L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static ResourceBlock toModel(ResourceBlockSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		ResourceBlock model = new ResourceBlockImpl();

		model.setResourceBlockId(soapModel.getResourceBlockId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setGroupId(soapModel.getGroupId());
		model.setName(soapModel.getName());
		model.setPermissionsHash(soapModel.getPermissionsHash());
		model.setReferenceCount(soapModel.getReferenceCount());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<ResourceBlock> toModels(ResourceBlockSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<ResourceBlock> models = new ArrayList<ResourceBlock>(soapModels.length);

		for (ResourceBlockSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portal.model.ResourceBlock"));

	public ResourceBlockModelImpl() {
	}

	public long getPrimaryKey() {
		return _resourceBlockId;
	}

	public void setPrimaryKey(long primaryKey) {
		setResourceBlockId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return _resourceBlockId;
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return ResourceBlock.class;
	}

	public String getModelClassName() {
		return ResourceBlock.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("resourceBlockId", getResourceBlockId());
		attributes.put("companyId", getCompanyId());
		attributes.put("groupId", getGroupId());
		attributes.put("name", getName());
		attributes.put("permissionsHash", getPermissionsHash());
		attributes.put("referenceCount", getReferenceCount());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long resourceBlockId = (Long)attributes.get("resourceBlockId");

		if (resourceBlockId != null) {
			setResourceBlockId(resourceBlockId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String permissionsHash = (String)attributes.get("permissionsHash");

		if (permissionsHash != null) {
			setPermissionsHash(permissionsHash);
		}

		Long referenceCount = (Long)attributes.get("referenceCount");

		if (referenceCount != null) {
			setReferenceCount(referenceCount);
		}
	}

	@JSON
	public long getResourceBlockId() {
		return _resourceBlockId;
	}

	public void setResourceBlockId(long resourceBlockId) {
		_resourceBlockId = resourceBlockId;
	}

	@JSON
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

	@JSON
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

	@JSON
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

	@JSON
	public String getPermissionsHash() {
		if (_permissionsHash == null) {
			return StringPool.BLANK;
		}
		else {
			return _permissionsHash;
		}
	}

	public void setPermissionsHash(String permissionsHash) {
		_columnBitmask |= PERMISSIONSHASH_COLUMN_BITMASK;

		if (_originalPermissionsHash == null) {
			_originalPermissionsHash = _permissionsHash;
		}

		_permissionsHash = permissionsHash;
	}

	public String getOriginalPermissionsHash() {
		return GetterUtil.getString(_originalPermissionsHash);
	}

	@JSON
	public long getReferenceCount() {
		return _referenceCount;
	}

	public void setReferenceCount(long referenceCount) {
		_referenceCount = referenceCount;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			ResourceBlock.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public ResourceBlock toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (ResourceBlock)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		ResourceBlockImpl resourceBlockImpl = new ResourceBlockImpl();

		resourceBlockImpl.setResourceBlockId(getResourceBlockId());
		resourceBlockImpl.setCompanyId(getCompanyId());
		resourceBlockImpl.setGroupId(getGroupId());
		resourceBlockImpl.setName(getName());
		resourceBlockImpl.setPermissionsHash(getPermissionsHash());
		resourceBlockImpl.setReferenceCount(getReferenceCount());

		resourceBlockImpl.resetOriginalValues();

		return resourceBlockImpl;
	}

	public int compareTo(ResourceBlock resourceBlock) {
		long primaryKey = resourceBlock.getPrimaryKey();

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

		ResourceBlock resourceBlock = null;

		try {
			resourceBlock = (ResourceBlock)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = resourceBlock.getPrimaryKey();

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
		ResourceBlockModelImpl resourceBlockModelImpl = this;

		resourceBlockModelImpl._originalCompanyId = resourceBlockModelImpl._companyId;

		resourceBlockModelImpl._setOriginalCompanyId = false;

		resourceBlockModelImpl._originalGroupId = resourceBlockModelImpl._groupId;

		resourceBlockModelImpl._setOriginalGroupId = false;

		resourceBlockModelImpl._originalName = resourceBlockModelImpl._name;

		resourceBlockModelImpl._originalPermissionsHash = resourceBlockModelImpl._permissionsHash;

		resourceBlockModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<ResourceBlock> toCacheModel() {
		ResourceBlockCacheModel resourceBlockCacheModel = new ResourceBlockCacheModel();

		resourceBlockCacheModel.resourceBlockId = getResourceBlockId();

		resourceBlockCacheModel.companyId = getCompanyId();

		resourceBlockCacheModel.groupId = getGroupId();

		resourceBlockCacheModel.name = getName();

		String name = resourceBlockCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			resourceBlockCacheModel.name = null;
		}

		resourceBlockCacheModel.permissionsHash = getPermissionsHash();

		String permissionsHash = resourceBlockCacheModel.permissionsHash;

		if ((permissionsHash != null) && (permissionsHash.length() == 0)) {
			resourceBlockCacheModel.permissionsHash = null;
		}

		resourceBlockCacheModel.referenceCount = getReferenceCount();

		return resourceBlockCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(13);

		sb.append("{resourceBlockId=");
		sb.append(getResourceBlockId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", permissionsHash=");
		sb.append(getPermissionsHash());
		sb.append(", referenceCount=");
		sb.append(getReferenceCount());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(22);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portal.model.ResourceBlock");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>resourceBlockId</column-name><column-value><![CDATA[");
		sb.append(getResourceBlockId());
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
			"<column><column-name>permissionsHash</column-name><column-value><![CDATA[");
		sb.append(getPermissionsHash());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>referenceCount</column-name><column-value><![CDATA[");
		sb.append(getReferenceCount());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = ResourceBlock.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			ResourceBlock.class
		};
	private long _resourceBlockId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private String _name;
	private String _originalName;
	private String _permissionsHash;
	private String _originalPermissionsHash;
	private long _referenceCount;
	private long _columnBitmask;
	private ResourceBlock _escapedModel;
}