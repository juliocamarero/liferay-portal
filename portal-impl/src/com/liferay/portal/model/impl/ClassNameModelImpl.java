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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ClassName;
import com.liferay.portal.model.ClassNameModel;
import com.liferay.portal.model.ClassNameSoap;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the ClassName service. Represents a row in the &quot;ClassName_&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portal.model.ClassNameModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link ClassNameImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ClassNameImpl
 * @see com.liferay.portal.model.ClassName
 * @see com.liferay.portal.model.ClassNameModel
 * @generated
 */
@JSON(strict = true)
public class ClassNameModelImpl extends BaseModelImpl<ClassName>
	implements ClassNameModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a class name model instance should use the {@link com.liferay.portal.model.ClassName} interface instead.
	 */
	public static final String TABLE_NAME = "ClassName_";
	public static final Object[][] TABLE_COLUMNS = {
			{ "classNameId", Types.BIGINT },
			{ "value", Types.VARCHAR }
		};
	public static final String TABLE_SQL_CREATE = "create table ClassName_ (classNameId LONG not null primary key,value VARCHAR(200) null)";
	public static final String TABLE_SQL_DROP = "drop table ClassName_";
	public static final String ORDER_BY_JPQL = " ORDER BY className.classNameId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY ClassName_.classNameId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portal.model.ClassName"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portal.model.ClassName"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.model.ClassName"),
			true);
	public static long VALUE_COLUMN_BITMASK = 1L;
	public static long CLASSNAMEID_COLUMN_BITMASK = 2L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static ClassName toModel(ClassNameSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		ClassName model = new ClassNameImpl();

		model.setClassNameId(soapModel.getClassNameId());
		model.setValue(soapModel.getValue());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<ClassName> toModels(ClassNameSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<ClassName> models = new ArrayList<ClassName>(soapModels.length);

		for (ClassNameSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portal.model.ClassName"));

	public ClassNameModelImpl() {
	}

	public long getPrimaryKey() {
		return _classNameId;
	}

	public void setPrimaryKey(long primaryKey) {
		setClassNameId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return _classNameId;
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return ClassName.class;
	}

	public String getModelClassName() {
		return ClassName.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("classNameId", getClassNameId());
		attributes.put("value", getValue());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long classNameId = (Long)attributes.get("classNameId");

		if (classNameId != null) {
			setClassNameId(classNameId);
		}

		String value = (String)attributes.get("value");

		if (value != null) {
			setValue(value);
		}
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
		_classNameId = classNameId;
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
		_columnBitmask |= VALUE_COLUMN_BITMASK;

		if (_originalValue == null) {
			_originalValue = _value;
		}

		_value = value;
	}

	public String getOriginalValue() {
		return GetterUtil.getString(_originalValue);
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(0,
			ClassName.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public ClassName toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (ClassName)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		ClassNameImpl classNameImpl = new ClassNameImpl();

		classNameImpl.setClassNameId(getClassNameId());
		classNameImpl.setValue(getValue());

		classNameImpl.resetOriginalValues();

		return classNameImpl;
	}

	public int compareTo(ClassName className) {
		long primaryKey = className.getPrimaryKey();

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

		ClassName className = null;

		try {
			className = (ClassName)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = className.getPrimaryKey();

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
		ClassNameModelImpl classNameModelImpl = this;

		classNameModelImpl._originalValue = classNameModelImpl._value;

		classNameModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<ClassName> toCacheModel() {
		ClassNameCacheModel classNameCacheModel = new ClassNameCacheModel();

		classNameCacheModel.classNameId = getClassNameId();

		classNameCacheModel.value = getValue();

		String value = classNameCacheModel.value;

		if ((value != null) && (value.length() == 0)) {
			classNameCacheModel.value = null;
		}

		return classNameCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(5);

		sb.append("{classNameId=");
		sb.append(getClassNameId());
		sb.append(", value=");
		sb.append(getValue());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(10);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portal.model.ClassName");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>classNameId</column-name><column-value><![CDATA[");
		sb.append(getClassNameId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>value</column-name><column-value><![CDATA[");
		sb.append(getValue());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = ClassName.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			ClassName.class
		};
	private long _classNameId;
	private String _value;
	private String _originalValue;
	private long _columnBitmask;
	private ClassName _escapedModel;
}