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

package com.liferay.portlet.messageboards.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.User;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;
import com.liferay.portlet.messageboards.model.MBThreadFlag;
import com.liferay.portlet.messageboards.model.MBThreadFlagModel;

import java.io.Serializable;

import java.sql.Types;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the MBThreadFlag service. Represents a row in the &quot;MBThreadFlag&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portlet.messageboards.model.MBThreadFlagModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link MBThreadFlagImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see MBThreadFlagImpl
 * @see com.liferay.portlet.messageboards.model.MBThreadFlag
 * @see com.liferay.portlet.messageboards.model.MBThreadFlagModel
 * @generated
 */
@ProviderType
public class MBThreadFlagModelImpl extends BaseModelImpl<MBThreadFlag>
	implements MBThreadFlagModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a message boards thread flag model instance should use the {@link com.liferay.portlet.messageboards.model.MBThreadFlag} interface instead.
	 */
	public static final String TABLE_NAME = "MBThreadFlag";
	public static final Object[][] TABLE_COLUMNS = {
			{ "uuid_", Types.VARCHAR },
			{ "threadFlagId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "threadId", Types.BIGINT }
		};
	public static final String TABLE_SQL_CREATE = "create table MBThreadFlag (uuid_ VARCHAR(75) null,threadFlagId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,threadId LONG)";
	public static final String TABLE_SQL_DROP = "drop table MBThreadFlag";
	public static final String ORDER_BY_JPQL = " ORDER BY mbThreadFlag.threadFlagId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY MBThreadFlag.threadFlagId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portlet.messageboards.model.MBThreadFlag"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portlet.messageboards.model.MBThreadFlag"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portlet.messageboards.model.MBThreadFlag"),
			true);
	public static final long COMPANYID_COLUMN_BITMASK = 1L;
	public static final long GROUPID_COLUMN_BITMASK = 2L;
	public static final long THREADID_COLUMN_BITMASK = 4L;
	public static final long USERID_COLUMN_BITMASK = 8L;
	public static final long UUID_COLUMN_BITMASK = 16L;
	public static final long THREADFLAGID_COLUMN_BITMASK = 32L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portlet.messageboards.model.MBThreadFlag"));

	public MBThreadFlagModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _threadFlagId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setThreadFlagId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _threadFlagId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return MBThreadFlag.class;
	}

	@Override
	public String getModelClassName() {
		return MBThreadFlag.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("threadFlagId", getThreadFlagId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("threadId", getThreadId());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long threadFlagId = (Long)attributes.get("threadFlagId");

		if (threadFlagId != null) {
			setThreadFlagId(threadFlagId);
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

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long threadId = (Long)attributes.get("threadId");

		if (threadId != null) {
			setThreadId(threadId);
		}
	}

	@Override
	public String getUuid() {
		if (_uuid == null) {
			return StringPool.BLANK;
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		if (_originalUuid == null) {
			_originalUuid = _uuid;
		}

		_uuid = uuid;
	}

	public String getOriginalUuid() {
		return GetterUtil.getString(_originalUuid);
	}

	@Override
	public long getThreadFlagId() {
		return _threadFlagId;
	}

	@Override
	public void setThreadFlagId(long threadFlagId) {
		_threadFlagId = threadFlagId;
	}

	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
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

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
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

	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_columnBitmask |= USERID_COLUMN_BITMASK;

		if (!_setOriginalUserId) {
			_setOriginalUserId = true;

			_originalUserId = _userId;
		}

		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return StringPool.BLANK;
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	public long getOriginalUserId() {
		return _originalUserId;
	}

	@Override
	public String getUserName() {
		if (_userName == null) {
			return StringPool.BLANK;
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
	}

	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	@Override
	public long getThreadId() {
		return _threadId;
	}

	@Override
	public void setThreadId(long threadId) {
		_columnBitmask |= THREADID_COLUMN_BITMASK;

		if (!_setOriginalThreadId) {
			_setOriginalThreadId = true;

			_originalThreadId = _threadId;
		}

		_threadId = threadId;
	}

	public long getOriginalThreadId() {
		return _originalThreadId;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(PortalUtil.getClassNameId(
				MBThreadFlag.class.getName()));
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			MBThreadFlag.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public MBThreadFlag toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (MBThreadFlag)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		MBThreadFlagImpl mbThreadFlagImpl = new MBThreadFlagImpl();

		mbThreadFlagImpl.setUuid(getUuid());
		mbThreadFlagImpl.setThreadFlagId(getThreadFlagId());
		mbThreadFlagImpl.setGroupId(getGroupId());
		mbThreadFlagImpl.setCompanyId(getCompanyId());
		mbThreadFlagImpl.setUserId(getUserId());
		mbThreadFlagImpl.setUserName(getUserName());
		mbThreadFlagImpl.setCreateDate(getCreateDate());
		mbThreadFlagImpl.setModifiedDate(getModifiedDate());
		mbThreadFlagImpl.setThreadId(getThreadId());

		mbThreadFlagImpl.resetOriginalValues();

		return mbThreadFlagImpl;
	}

	@Override
	public int compareTo(MBThreadFlag mbThreadFlag) {
		long primaryKey = mbThreadFlag.getPrimaryKey();

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

		if (!(obj instanceof MBThreadFlag)) {
			return false;
		}

		MBThreadFlag mbThreadFlag = (MBThreadFlag)obj;

		long primaryKey = mbThreadFlag.getPrimaryKey();

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
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		MBThreadFlagModelImpl mbThreadFlagModelImpl = this;

		mbThreadFlagModelImpl._originalUuid = mbThreadFlagModelImpl._uuid;

		mbThreadFlagModelImpl._originalGroupId = mbThreadFlagModelImpl._groupId;

		mbThreadFlagModelImpl._setOriginalGroupId = false;

		mbThreadFlagModelImpl._originalCompanyId = mbThreadFlagModelImpl._companyId;

		mbThreadFlagModelImpl._setOriginalCompanyId = false;

		mbThreadFlagModelImpl._originalUserId = mbThreadFlagModelImpl._userId;

		mbThreadFlagModelImpl._setOriginalUserId = false;

		mbThreadFlagModelImpl._originalThreadId = mbThreadFlagModelImpl._threadId;

		mbThreadFlagModelImpl._setOriginalThreadId = false;

		mbThreadFlagModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<MBThreadFlag> toCacheModel() {
		MBThreadFlagCacheModel mbThreadFlagCacheModel = new MBThreadFlagCacheModel();

		mbThreadFlagCacheModel.uuid = getUuid();

		String uuid = mbThreadFlagCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			mbThreadFlagCacheModel.uuid = null;
		}

		mbThreadFlagCacheModel.threadFlagId = getThreadFlagId();

		mbThreadFlagCacheModel.groupId = getGroupId();

		mbThreadFlagCacheModel.companyId = getCompanyId();

		mbThreadFlagCacheModel.userId = getUserId();

		mbThreadFlagCacheModel.userName = getUserName();

		String userName = mbThreadFlagCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			mbThreadFlagCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			mbThreadFlagCacheModel.createDate = createDate.getTime();
		}
		else {
			mbThreadFlagCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			mbThreadFlagCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			mbThreadFlagCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		mbThreadFlagCacheModel.threadId = getThreadId();

		return mbThreadFlagCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(19);

		sb.append("{uuid=");
		sb.append(getUuid());
		sb.append(", threadFlagId=");
		sb.append(getThreadFlagId());
		sb.append(", groupId=");
		sb.append(getGroupId());
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
		sb.append(", threadId=");
		sb.append(getThreadId());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(31);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portlet.messageboards.model.MBThreadFlag");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>uuid</column-name><column-value><![CDATA[");
		sb.append(getUuid());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>threadFlagId</column-name><column-value><![CDATA[");
		sb.append(getThreadFlagId());
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
			"<column><column-name>threadId</column-name><column-value><![CDATA[");
		sb.append(getThreadId());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = MBThreadFlag.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			MBThreadFlag.class
		};
	private String _uuid;
	private String _originalUuid;
	private long _threadFlagId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private long _originalUserId;
	private boolean _setOriginalUserId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private long _threadId;
	private long _originalThreadId;
	private boolean _setOriginalThreadId;
	private long _columnBitmask;
	private MBThreadFlag _escapedModel;
}