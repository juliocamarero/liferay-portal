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

package com.liferay.portlet.social.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
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
import com.liferay.portlet.social.model.SocialActivityLimit;
import com.liferay.portlet.social.model.SocialActivityLimitModel;

import java.io.Serializable;

import java.sql.Types;

import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the SocialActivityLimit service. Represents a row in the &quot;SocialActivityLimit&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portlet.social.model.SocialActivityLimitModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link SocialActivityLimitImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SocialActivityLimitImpl
 * @see com.liferay.portlet.social.model.SocialActivityLimit
 * @see com.liferay.portlet.social.model.SocialActivityLimitModel
 * @generated
 */
public class SocialActivityLimitModelImpl extends BaseModelImpl<SocialActivityLimit>
	implements SocialActivityLimitModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a social activity limit model instance should use the {@link com.liferay.portlet.social.model.SocialActivityLimit} interface instead.
	 */
	public static final String TABLE_NAME = "SocialActivityLimit";
	public static final Object[][] TABLE_COLUMNS = {
			{ "activityLimitId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "classNameId", Types.BIGINT },
			{ "classPK", Types.BIGINT },
			{ "activityType", Types.INTEGER },
			{ "activityCounterName", Types.VARCHAR },
			{ "value", Types.VARCHAR }
		};
	public static final String TABLE_SQL_CREATE = "create table SocialActivityLimit (activityLimitId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,classNameId LONG,classPK LONG,activityType INTEGER,activityCounterName VARCHAR(75) null,value VARCHAR(75) null)";
	public static final String TABLE_SQL_DROP = "drop table SocialActivityLimit";
	public static final String ORDER_BY_ENTITY_ALIAS = " ORDER BY socialActivityLimit.activityLimitId ASC";
	public static final String ORDER_BY_ENTITY_TABLE = " ORDER BY SocialActivityLimit.activityLimitId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portlet.social.model.SocialActivityLimit"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portlet.social.model.SocialActivityLimit"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portlet.social.model.SocialActivityLimit"),
			true);
	public static long ACTIVITYCOUNTERNAME_COLUMN_BITMASK = 1L;
	public static long ACTIVITYTYPE_COLUMN_BITMASK = 2L;
	public static long CLASSNAMEID_COLUMN_BITMASK = 4L;
	public static long CLASSPK_COLUMN_BITMASK = 8L;
	public static long GROUPID_COLUMN_BITMASK = 16L;
	public static long USERID_COLUMN_BITMASK = 32L;
	public static long ACTIVITYLIMITID_COLUMN_BITMASK = 64L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portlet.social.model.SocialActivityLimit"));

	public SocialActivityLimitModelImpl() {
	}

	public long getPrimaryKey() {
		return _activityLimitId;
	}

	public void setPrimaryKey(long primaryKey) {
		setActivityLimitId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return _activityLimitId;
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return SocialActivityLimit.class;
	}

	public String getModelClassName() {
		return SocialActivityLimit.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("activityLimitId", getActivityLimitId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("classNameId", getClassNameId());
		attributes.put("classPK", getClassPK());
		attributes.put("activityType", getActivityType());
		attributes.put("activityCounterName", getActivityCounterName());
		attributes.put("value", getValue());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long activityLimitId = (Long)attributes.get("activityLimitId");

		if (activityLimitId != null) {
			setActivityLimitId(activityLimitId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		Long classNameId = (Long)attributes.get("classNameId");

		if (classNameId != null) {
			setClassNameId(classNameId);
		}

		Long classPK = (Long)attributes.get("classPK");

		if (classPK != null) {
			setClassPK(classPK);
		}

		Integer activityType = (Integer)attributes.get("activityType");

		if (activityType != null) {
			setActivityType(activityType);
		}

		String activityCounterName = (String)attributes.get(
				"activityCounterName");

		if (activityCounterName != null) {
			setActivityCounterName(activityCounterName);
		}

		String value = (String)attributes.get("value");

		if (value != null) {
			setValue(value);
		}
	}

	public long getActivityLimitId() {
		return _activityLimitId;
	}

	public void setActivityLimitId(long activityLimitId) {
		_activityLimitId = activityLimitId;
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

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_columnBitmask |= USERID_COLUMN_BITMASK;

		if (!_setOriginalUserId) {
			_setOriginalUserId = true;

			_originalUserId = _userId;
		}

		_userId = userId;
	}

	public String getUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
	}

	public void setUserUuid(String userUuid) {
		_userUuid = userUuid;
	}

	public long getOriginalUserId() {
		return _originalUserId;
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

	public long getClassPK() {
		return _classPK;
	}

	public void setClassPK(long classPK) {
		_columnBitmask |= CLASSPK_COLUMN_BITMASK;

		if (!_setOriginalClassPK) {
			_setOriginalClassPK = true;

			_originalClassPK = _classPK;
		}

		_classPK = classPK;
	}

	public long getOriginalClassPK() {
		return _originalClassPK;
	}

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

	public String getActivityCounterName() {
		if (_activityCounterName == null) {
			return StringPool.BLANK;
		}
		else {
			return _activityCounterName;
		}
	}

	public void setActivityCounterName(String activityCounterName) {
		_columnBitmask |= ACTIVITYCOUNTERNAME_COLUMN_BITMASK;

		if (_originalActivityCounterName == null) {
			_originalActivityCounterName = _activityCounterName;
		}

		_activityCounterName = activityCounterName;
	}

	public String getOriginalActivityCounterName() {
		return GetterUtil.getString(_originalActivityCounterName);
	}

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
			SocialActivityLimit.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public SocialActivityLimit toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (SocialActivityLimit)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		SocialActivityLimitImpl socialActivityLimitImpl = new SocialActivityLimitImpl();

		socialActivityLimitImpl.setActivityLimitId(getActivityLimitId());
		socialActivityLimitImpl.setGroupId(getGroupId());
		socialActivityLimitImpl.setCompanyId(getCompanyId());
		socialActivityLimitImpl.setUserId(getUserId());
		socialActivityLimitImpl.setClassNameId(getClassNameId());
		socialActivityLimitImpl.setClassPK(getClassPK());
		socialActivityLimitImpl.setActivityType(getActivityType());
		socialActivityLimitImpl.setActivityCounterName(getActivityCounterName());
		socialActivityLimitImpl.setValue(getValue());

		socialActivityLimitImpl.resetOriginalValues();

		return socialActivityLimitImpl;
	}

	public int compareTo(SocialActivityLimit socialActivityLimit) {
		long primaryKey = socialActivityLimit.getPrimaryKey();

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

		SocialActivityLimit socialActivityLimit = null;

		try {
			socialActivityLimit = (SocialActivityLimit)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = socialActivityLimit.getPrimaryKey();

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
		SocialActivityLimitModelImpl socialActivityLimitModelImpl = this;

		socialActivityLimitModelImpl._originalGroupId = socialActivityLimitModelImpl._groupId;

		socialActivityLimitModelImpl._setOriginalGroupId = false;

		socialActivityLimitModelImpl._originalUserId = socialActivityLimitModelImpl._userId;

		socialActivityLimitModelImpl._setOriginalUserId = false;

		socialActivityLimitModelImpl._originalClassNameId = socialActivityLimitModelImpl._classNameId;

		socialActivityLimitModelImpl._setOriginalClassNameId = false;

		socialActivityLimitModelImpl._originalClassPK = socialActivityLimitModelImpl._classPK;

		socialActivityLimitModelImpl._setOriginalClassPK = false;

		socialActivityLimitModelImpl._originalActivityType = socialActivityLimitModelImpl._activityType;

		socialActivityLimitModelImpl._setOriginalActivityType = false;

		socialActivityLimitModelImpl._originalActivityCounterName = socialActivityLimitModelImpl._activityCounterName;

		socialActivityLimitModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<SocialActivityLimit> toCacheModel() {
		SocialActivityLimitCacheModel socialActivityLimitCacheModel = new SocialActivityLimitCacheModel();

		socialActivityLimitCacheModel.activityLimitId = getActivityLimitId();

		socialActivityLimitCacheModel.groupId = getGroupId();

		socialActivityLimitCacheModel.companyId = getCompanyId();

		socialActivityLimitCacheModel.userId = getUserId();

		socialActivityLimitCacheModel.classNameId = getClassNameId();

		socialActivityLimitCacheModel.classPK = getClassPK();

		socialActivityLimitCacheModel.activityType = getActivityType();

		socialActivityLimitCacheModel.activityCounterName = getActivityCounterName();

		String activityCounterName = socialActivityLimitCacheModel.activityCounterName;

		if ((activityCounterName != null) &&
				(activityCounterName.length() == 0)) {
			socialActivityLimitCacheModel.activityCounterName = null;
		}

		socialActivityLimitCacheModel.value = getValue();

		String value = socialActivityLimitCacheModel.value;

		if ((value != null) && (value.length() == 0)) {
			socialActivityLimitCacheModel.value = null;
		}

		return socialActivityLimitCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(19);

		sb.append("{activityLimitId=");
		sb.append(getActivityLimitId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", classNameId=");
		sb.append(getClassNameId());
		sb.append(", classPK=");
		sb.append(getClassPK());
		sb.append(", activityType=");
		sb.append(getActivityType());
		sb.append(", activityCounterName=");
		sb.append(getActivityCounterName());
		sb.append(", value=");
		sb.append(getValue());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(31);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portlet.social.model.SocialActivityLimit");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>activityLimitId</column-name><column-value><![CDATA[");
		sb.append(getActivityLimitId());
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
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>classNameId</column-name><column-value><![CDATA[");
		sb.append(getClassNameId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>classPK</column-name><column-value><![CDATA[");
		sb.append(getClassPK());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>activityType</column-name><column-value><![CDATA[");
		sb.append(getActivityType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>activityCounterName</column-name><column-value><![CDATA[");
		sb.append(getActivityCounterName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>value</column-name><column-value><![CDATA[");
		sb.append(getValue());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = SocialActivityLimit.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			SocialActivityLimit.class
		};
	private long _activityLimitId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _userId;
	private String _userUuid;
	private long _originalUserId;
	private boolean _setOriginalUserId;
	private long _classNameId;
	private long _originalClassNameId;
	private boolean _setOriginalClassNameId;
	private long _classPK;
	private long _originalClassPK;
	private boolean _setOriginalClassPK;
	private int _activityType;
	private int _originalActivityType;
	private boolean _setOriginalActivityType;
	private String _activityCounterName;
	private String _originalActivityCounterName;
	private String _value;
	private long _columnBitmask;
	private SocialActivityLimit _escapedModel;
}