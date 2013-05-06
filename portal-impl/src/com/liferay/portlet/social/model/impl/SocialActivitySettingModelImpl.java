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

package com.liferay.portlet.social.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;
import com.liferay.portlet.social.model.SocialActivitySetting;
import com.liferay.portlet.social.model.SocialActivitySettingModel;
import com.liferay.portlet.social.model.SocialActivitySettingSoap;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the SocialActivitySetting service. Represents a row in the &quot;SocialActivitySetting&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portlet.social.model.SocialActivitySettingModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link SocialActivitySettingImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SocialActivitySettingImpl
 * @see com.liferay.portlet.social.model.SocialActivitySetting
 * @see com.liferay.portlet.social.model.SocialActivitySettingModel
 * @generated
 */
@JSON(strict = true)
public class SocialActivitySettingModelImpl extends BaseModelImpl<SocialActivitySetting>
	implements SocialActivitySettingModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a social activity setting model instance should use the {@link com.liferay.portlet.social.model.SocialActivitySetting} interface instead.
	 */
	public static final String TABLE_NAME = "SocialActivitySetting";
	public static final Object[][] TABLE_COLUMNS = {
			{ "activitySettingId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "classNameId", Types.BIGINT },
			{ "activityType", Types.INTEGER },
			{ "name", Types.VARCHAR },
			{ "value", Types.VARCHAR }
		};
	public static final String TABLE_SQL_CREATE = "create table SocialActivitySetting (activitySettingId LONG not null primary key,groupId LONG,companyId LONG,classNameId LONG,activityType INTEGER,name VARCHAR(75) null,value VARCHAR(1024) null)";
	public static final String TABLE_SQL_DROP = "drop table SocialActivitySetting";
	public static final String ORDER_BY_JPQL = " ORDER BY socialActivitySetting.activitySettingId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY SocialActivitySetting.activitySettingId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portlet.social.model.SocialActivitySetting"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portlet.social.model.SocialActivitySetting"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portlet.social.model.SocialActivitySetting"),
			true);
	public static long ACTIVITYTYPE_COLUMN_BITMASK = 1L;
	public static long CLASSNAMEID_COLUMN_BITMASK = 2L;
	public static long GROUPID_COLUMN_BITMASK = 4L;
	public static long NAME_COLUMN_BITMASK = 8L;
	public static long ACTIVITYSETTINGID_COLUMN_BITMASK = 16L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static SocialActivitySetting toModel(
		SocialActivitySettingSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		SocialActivitySetting model = new SocialActivitySettingImpl();

		model.setActivitySettingId(soapModel.getActivitySettingId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setClassNameId(soapModel.getClassNameId());
		model.setActivityType(soapModel.getActivityType());
		model.setName(soapModel.getName());
		model.setValue(soapModel.getValue());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<SocialActivitySetting> toModels(
		SocialActivitySettingSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<SocialActivitySetting> models = new ArrayList<SocialActivitySetting>(soapModels.length);

		for (SocialActivitySettingSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portlet.social.model.SocialActivitySetting"));

	public SocialActivitySettingModelImpl() {
	}

	public long getPrimaryKey() {
		return _activitySettingId;
	}

	public void setPrimaryKey(long primaryKey) {
		setActivitySettingId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return _activitySettingId;
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return SocialActivitySetting.class;
	}

	public String getModelClassName() {
		return SocialActivitySetting.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("activitySettingId", getActivitySettingId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("classNameId", getClassNameId());
		attributes.put("activityType", getActivityType());
		attributes.put("name", getName());
		attributes.put("value", getValue());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long activitySettingId = (Long)attributes.get("activitySettingId");

		if (activitySettingId != null) {
			setActivitySettingId(activitySettingId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long classNameId = (Long)attributes.get("classNameId");

		if (classNameId != null) {
			setClassNameId(classNameId);
		}

		Integer activityType = (Integer)attributes.get("activityType");

		if (activityType != null) {
			setActivityType(activityType);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String value = (String)attributes.get("value");

		if (value != null) {
			setValue(value);
		}
	}

	@JSON
	public long getActivitySettingId() {
		return _activitySettingId;
	}

	public void setActivitySettingId(long activitySettingId) {
		_activitySettingId = activitySettingId;
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
	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public String getClassName() {
		if (getClassNameId() <= 0) {
			return StringPool.BLANK;
		}

		return PortalUtil.getClassName(getClassNameId());
	}

	public void setClassName(String className) {
		long classNameId = 0;

		if (Validator.isNotNull(className)) {
			classNameId = PortalUtil.getClassNameId(className);
		}

		setClassNameId(classNameId);
	}

	@JSON
	public long getClassNameId() {
		return _classNameId;
	}

	public void setClassNameId(long classNameId) {
		_columnBitmask |= CLASSNAMEID_COLUMN_BITMASK;

		if (!_setOriginalClassNameId) {
			_setOriginalClassNameId = true;

			_originalClassNameId = _classNameId;
		}

		_classNameId = classNameId;
	}

	public long getOriginalClassNameId() {
		return _originalClassNameId;
	}

	@JSON
	public int getActivityType() {
		return _activityType;
	}

	public void setActivityType(int activityType) {
		_columnBitmask |= ACTIVITYTYPE_COLUMN_BITMASK;

		if (!_setOriginalActivityType) {
			_setOriginalActivityType = true;

			_originalActivityType = _activityType;
		}

		_activityType = activityType;
	}

	public int getOriginalActivityType() {
		return _originalActivityType;
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
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			SocialActivitySetting.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public SocialActivitySetting toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (SocialActivitySetting)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		SocialActivitySettingImpl socialActivitySettingImpl = new SocialActivitySettingImpl();

		socialActivitySettingImpl.setActivitySettingId(getActivitySettingId());
		socialActivitySettingImpl.setGroupId(getGroupId());
		socialActivitySettingImpl.setCompanyId(getCompanyId());
		socialActivitySettingImpl.setClassNameId(getClassNameId());
		socialActivitySettingImpl.setActivityType(getActivityType());
		socialActivitySettingImpl.setName(getName());
		socialActivitySettingImpl.setValue(getValue());

		socialActivitySettingImpl.resetOriginalValues();

		return socialActivitySettingImpl;
	}

	public int compareTo(SocialActivitySetting socialActivitySetting) {
		long primaryKey = socialActivitySetting.getPrimaryKey();

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
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SocialActivitySetting)) {
			return false;
		}

		SocialActivitySetting socialActivitySetting = (SocialActivitySetting)obj;

		long primaryKey = socialActivitySetting.getPrimaryKey();

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
		SocialActivitySettingModelImpl socialActivitySettingModelImpl = this;

		socialActivitySettingModelImpl._originalGroupId = socialActivitySettingModelImpl._groupId;

		socialActivitySettingModelImpl._setOriginalGroupId = false;

		socialActivitySettingModelImpl._originalClassNameId = socialActivitySettingModelImpl._classNameId;

		socialActivitySettingModelImpl._setOriginalClassNameId = false;

		socialActivitySettingModelImpl._originalActivityType = socialActivitySettingModelImpl._activityType;

		socialActivitySettingModelImpl._setOriginalActivityType = false;

		socialActivitySettingModelImpl._originalName = socialActivitySettingModelImpl._name;

		socialActivitySettingModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<SocialActivitySetting> toCacheModel() {
		SocialActivitySettingCacheModel socialActivitySettingCacheModel = new SocialActivitySettingCacheModel();

		socialActivitySettingCacheModel.activitySettingId = getActivitySettingId();

		socialActivitySettingCacheModel.groupId = getGroupId();

		socialActivitySettingCacheModel.companyId = getCompanyId();

		socialActivitySettingCacheModel.classNameId = getClassNameId();

		socialActivitySettingCacheModel.activityType = getActivityType();

		socialActivitySettingCacheModel.name = getName();

		String name = socialActivitySettingCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			socialActivitySettingCacheModel.name = null;
		}

		socialActivitySettingCacheModel.value = getValue();

		String value = socialActivitySettingCacheModel.value;

		if ((value != null) && (value.length() == 0)) {
			socialActivitySettingCacheModel.value = null;
		}

		return socialActivitySettingCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(15);

		sb.append("{activitySettingId=");
		sb.append(getActivitySettingId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", classNameId=");
		sb.append(getClassNameId());
		sb.append(", activityType=");
		sb.append(getActivityType());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", value=");
		sb.append(getValue());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(25);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portlet.social.model.SocialActivitySetting");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>activitySettingId</column-name><column-value><![CDATA[");
		sb.append(getActivitySettingId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>groupId</column-name><column-value><![CDATA[");
		sb.append(getGroupId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>classNameId</column-name><column-value><![CDATA[");
		sb.append(getClassNameId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>activityType</column-name><column-value><![CDATA[");
		sb.append(getActivityType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>name</column-name><column-value><![CDATA[");
		sb.append(getName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>value</column-name><column-value><![CDATA[");
		sb.append(getValue());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = SocialActivitySetting.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			SocialActivitySetting.class
		};
	private long _activitySettingId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _classNameId;
	private long _originalClassNameId;
	private boolean _setOriginalClassNameId;
	private int _activityType;
	private int _originalActivityType;
	private boolean _setOriginalActivityType;
	private String _name;
	private String _originalName;
	private String _value;
	private long _columnBitmask;
	private SocialActivitySetting _escapedModel;
}