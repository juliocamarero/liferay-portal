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

package com.liferay.portlet.asset.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.asset.model.AssetTagProperty;
import com.liferay.portlet.asset.model.AssetTagPropertyModel;
import com.liferay.portlet.asset.model.AssetTagPropertySoap;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The base model implementation for the AssetTagProperty service. Represents a row in the &quot;AssetTagProperty&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portlet.asset.model.AssetTagPropertyModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link AssetTagPropertyImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetTagPropertyImpl
 * @see com.liferay.portlet.asset.model.AssetTagProperty
 * @see com.liferay.portlet.asset.model.AssetTagPropertyModel
 * @generated
 */
@JSON(strict = true)
public class AssetTagPropertyModelImpl extends BaseModelImpl<AssetTagProperty>
	implements AssetTagPropertyModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a asset tag property model instance should use the {@link com.liferay.portlet.asset.model.AssetTagProperty} interface instead.
	 */
	public static final String TABLE_NAME = "AssetTagProperty";
	public static final Object[][] TABLE_COLUMNS = {
			{ "tagPropertyId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "tagId", Types.BIGINT },
			{ "key_", Types.VARCHAR },
			{ "value", Types.VARCHAR }
		};
	public static final String TABLE_SQL_CREATE = "create table AssetTagProperty (tagPropertyId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,tagId LONG,key_ VARCHAR(75) null,value VARCHAR(255) null)";
	public static final String TABLE_SQL_DROP = "drop table AssetTagProperty";
	public static final String ORDER_BY_JPQL = " ORDER BY assetTagProperty.key ASC";
	public static final String ORDER_BY_SQL = " ORDER BY AssetTagProperty.key_ ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portlet.asset.model.AssetTagProperty"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portlet.asset.model.AssetTagProperty"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portlet.asset.model.AssetTagProperty"),
			true);
	public static long COMPANYID_COLUMN_BITMASK = 1L;
	public static long KEY_COLUMN_BITMASK = 2L;
	public static long TAGID_COLUMN_BITMASK = 4L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static AssetTagProperty toModel(AssetTagPropertySoap soapModel) {
		AssetTagProperty model = new AssetTagPropertyImpl();

		model.setTagPropertyId(soapModel.getTagPropertyId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setTagId(soapModel.getTagId());
		model.setKey(soapModel.getKey());
		model.setValue(soapModel.getValue());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<AssetTagProperty> toModels(
		AssetTagPropertySoap[] soapModels) {
		List<AssetTagProperty> models = new ArrayList<AssetTagProperty>(soapModels.length);

		for (AssetTagPropertySoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portlet.asset.model.AssetTagProperty"));

	public AssetTagPropertyModelImpl() {
	}

	public long getPrimaryKey() {
		return _tagPropertyId;
	}

	public void setPrimaryKey(long primaryKey) {
		setTagPropertyId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return new Long(_tagPropertyId);
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return AssetTagProperty.class;
	}

	public String getModelClassName() {
		return AssetTagProperty.class.getName();
	}

	@JSON
	public long getTagPropertyId() {
		return _tagPropertyId;
	}

	public void setTagPropertyId(long tagPropertyId) {
		_tagPropertyId = tagPropertyId;
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
	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
	}

	public void setUserUuid(String userUuid) {
		_userUuid = userUuid;
	}

	@JSON
	public String getUserName() {
		if (_userName == null) {
			return StringPool.BLANK;
		}
		else {
			return _userName;
		}
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	@JSON
	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@JSON
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	@JSON
	public long getTagId() {
		return _tagId;
	}

	public void setTagId(long tagId) {
		_columnBitmask |= TAGID_COLUMN_BITMASK;

		if (!_setOriginalTagId) {
			_setOriginalTagId = true;

			_originalTagId = _tagId;
		}

		_tagId = tagId;
	}

	public long getOriginalTagId() {
		return _originalTagId;
	}

	@JSON
	public String getKey() {
		if (_key == null) {
			return StringPool.BLANK;
		}
		else {
			return _key;
		}
	}

	public void setKey(String key) {
		_columnBitmask |= KEY_COLUMN_BITMASK;

		if (_originalKey == null) {
			_originalKey = _key;
		}

		_key = key;
	}

	public String getOriginalKey() {
		return GetterUtil.getString(_originalKey);
	}

	@JSON
	public String getValue() {
		if (_value == null) {
			return StringPool.BLANK;
		}
		else {
			return _value;
		}
	}

	public void setValue(String value) {
		_value = value;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public AssetTagProperty toEscapedModel() {
		if (_escapedModelProxy == null) {
			_escapedModelProxy = (AssetTagProperty)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelProxyInterfaces,
					new AutoEscapeBeanHandler(this));
		}

		return _escapedModelProxy;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		if (_expandoBridge == null) {
			_expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
					AssetTagProperty.class.getName(), getPrimaryKey());
		}

		return _expandoBridge;
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		getExpandoBridge().setAttributes(serviceContext);
	}

	@Override
	public Object clone() {
		AssetTagPropertyImpl assetTagPropertyImpl = new AssetTagPropertyImpl();

		assetTagPropertyImpl.setTagPropertyId(getTagPropertyId());
		assetTagPropertyImpl.setCompanyId(getCompanyId());
		assetTagPropertyImpl.setUserId(getUserId());
		assetTagPropertyImpl.setUserName(getUserName());
		assetTagPropertyImpl.setCreateDate(getCreateDate());
		assetTagPropertyImpl.setModifiedDate(getModifiedDate());
		assetTagPropertyImpl.setTagId(getTagId());
		assetTagPropertyImpl.setKey(getKey());
		assetTagPropertyImpl.setValue(getValue());

		assetTagPropertyImpl.resetOriginalValues();

		return assetTagPropertyImpl;
	}

	public int compareTo(AssetTagProperty assetTagProperty) {
		int value = 0;

		value = getKey().compareTo(assetTagProperty.getKey());

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		AssetTagProperty assetTagProperty = null;

		try {
			assetTagProperty = (AssetTagProperty)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = assetTagProperty.getPrimaryKey();

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
		AssetTagPropertyModelImpl assetTagPropertyModelImpl = this;

		assetTagPropertyModelImpl._originalCompanyId = assetTagPropertyModelImpl._companyId;

		assetTagPropertyModelImpl._setOriginalCompanyId = false;

		assetTagPropertyModelImpl._originalTagId = assetTagPropertyModelImpl._tagId;

		assetTagPropertyModelImpl._setOriginalTagId = false;

		assetTagPropertyModelImpl._originalKey = assetTagPropertyModelImpl._key;

		assetTagPropertyModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<AssetTagProperty> toCacheModel() {
		AssetTagPropertyCacheModel assetTagPropertyCacheModel = new AssetTagPropertyCacheModel();

		assetTagPropertyCacheModel.tagPropertyId = getTagPropertyId();

		assetTagPropertyCacheModel.companyId = getCompanyId();

		assetTagPropertyCacheModel.userId = getUserId();

		assetTagPropertyCacheModel.userName = getUserName();

		String userName = assetTagPropertyCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			assetTagPropertyCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			assetTagPropertyCacheModel.createDate = createDate.getTime();
		}
		else {
			assetTagPropertyCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			assetTagPropertyCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			assetTagPropertyCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		assetTagPropertyCacheModel.tagId = getTagId();

		assetTagPropertyCacheModel.key = getKey();

		String key = assetTagPropertyCacheModel.key;

		if ((key != null) && (key.length() == 0)) {
			assetTagPropertyCacheModel.key = null;
		}

		assetTagPropertyCacheModel.value = getValue();

		String value = assetTagPropertyCacheModel.value;

		if ((value != null) && (value.length() == 0)) {
			assetTagPropertyCacheModel.value = null;
		}

		return assetTagPropertyCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(19);

		sb.append("{tagPropertyId=");
		sb.append(getTagPropertyId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append(", tagId=");
		sb.append(getTagId());
		sb.append(", key=");
		sb.append(getKey());
		sb.append(", value=");
		sb.append(getValue());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(31);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portlet.asset.model.AssetTagProperty");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>tagPropertyId</column-name><column-value><![CDATA[");
		sb.append(getTagPropertyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userName</column-name><column-value><![CDATA[");
		sb.append(getUserName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
		sb.append(getModifiedDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>tagId</column-name><column-value><![CDATA[");
		sb.append(getTagId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>key</column-name><column-value><![CDATA[");
		sb.append(getKey());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>value</column-name><column-value><![CDATA[");
		sb.append(getValue());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = AssetTagProperty.class.getClassLoader();
	private static Class<?>[] _escapedModelProxyInterfaces = new Class[] {
			AssetTagProperty.class
		};
	private long _tagPropertyId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private String _userUuid;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private long _tagId;
	private long _originalTagId;
	private boolean _setOriginalTagId;
	private String _key;
	private String _originalKey;
	private String _value;
	private transient ExpandoBridge _expandoBridge;
	private long _columnBitmask;
	private AssetTagProperty _escapedModelProxy;
}