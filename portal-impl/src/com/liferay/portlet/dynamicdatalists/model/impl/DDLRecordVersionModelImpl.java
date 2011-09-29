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

package com.liferay.portlet.dynamicdatalists.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.dynamicdatalists.model.DDLRecordVersion;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordVersionModel;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.Date;

/**
 * The base model implementation for the DDLRecordVersion service. Represents a row in the &quot;DDLRecordVersion&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portlet.dynamicdatalists.model.DDLRecordVersionModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link DDLRecordVersionImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DDLRecordVersionImpl
 * @see com.liferay.portlet.dynamicdatalists.model.DDLRecordVersion
 * @see com.liferay.portlet.dynamicdatalists.model.DDLRecordVersionModel
 * @generated
 */
public class DDLRecordVersionModelImpl extends BaseModelImpl<DDLRecordVersion>
	implements DDLRecordVersionModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a d d l record version model instance should use the {@link com.liferay.portlet.dynamicdatalists.model.DDLRecordVersion} interface instead.
	 */
	public static final String TABLE_NAME = "DDLRecordVersion";
	public static final Object[][] TABLE_COLUMNS = {
			{ "recordVersionId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "DDMStorageId", Types.BIGINT },
			{ "recordSetId", Types.BIGINT },
			{ "recordId", Types.BIGINT },
			{ "version", Types.VARCHAR },
			{ "displayIndex", Types.INTEGER },
			{ "status", Types.INTEGER },
			{ "statusByUserId", Types.BIGINT },
			{ "statusByUserName", Types.VARCHAR },
			{ "statusDate", Types.TIMESTAMP }
		};
	public static final String TABLE_SQL_CREATE = "create table DDLRecordVersion (recordVersionId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,DDMStorageId LONG,recordSetId LONG,recordId LONG,version VARCHAR(75) null,displayIndex INTEGER,status INTEGER,statusByUserId LONG,statusByUserName VARCHAR(75) null,statusDate DATE null)";
	public static final String TABLE_SQL_DROP = "drop table DDLRecordVersion";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portlet.dynamicdatalists.model.DDLRecordVersion"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portlet.dynamicdatalists.model.DDLRecordVersion"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portlet.dynamicdatalists.model.DDLRecordVersion"),
			true);
	public static long RECORDID_COLUMN_BITMASK = 1L;
	public static long STATUS_COLUMN_BITMASK = 2L;
	public static long VERSION_COLUMN_BITMASK = 4L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portlet.dynamicdatalists.model.DDLRecordVersion"));

	public DDLRecordVersionModelImpl() {
	}

	public long getPrimaryKey() {
		return _recordVersionId;
	}

	public void setPrimaryKey(long primaryKey) {
		setRecordVersionId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return new Long(_recordVersionId);
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return DDLRecordVersion.class;
	}

	public String getModelClassName() {
		return DDLRecordVersion.class.getName();
	}

	public long getRecordVersionId() {
		return _recordVersionId;
	}

	public void setRecordVersionId(long recordVersionId) {
		_recordVersionId = recordVersionId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
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
		_userId = userId;
	}

	public String getUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
	}

	public void setUserUuid(String userUuid) {
		_userUuid = userUuid;
	}

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

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public long getDDMStorageId() {
		return _DDMStorageId;
	}

	public void setDDMStorageId(long DDMStorageId) {
		_DDMStorageId = DDMStorageId;
	}

	public long getRecordSetId() {
		return _recordSetId;
	}

	public void setRecordSetId(long recordSetId) {
		_recordSetId = recordSetId;
	}

	public long getRecordId() {
		return _recordId;
	}

	public void setRecordId(long recordId) {
		_columnBitmask |= RECORDID_COLUMN_BITMASK;

		if (!_setOriginalRecordId) {
			_setOriginalRecordId = true;

			_originalRecordId = _recordId;
		}

		_recordId = recordId;
	}

	public long getOriginalRecordId() {
		return _originalRecordId;
	}

	public String getVersion() {
		if (_version == null) {
			return StringPool.BLANK;
		}
		else {
			return _version;
		}
	}

	public void setVersion(String version) {
		_columnBitmask |= VERSION_COLUMN_BITMASK;

		if (_originalVersion == null) {
			_originalVersion = _version;
		}

		_version = version;
	}

	public String getOriginalVersion() {
		return GetterUtil.getString(_originalVersion);
	}

	public int getDisplayIndex() {
		return _displayIndex;
	}

	public void setDisplayIndex(int displayIndex) {
		_displayIndex = displayIndex;
	}

	public int getStatus() {
		return _status;
	}

	public void setStatus(int status) {
		_columnBitmask |= STATUS_COLUMN_BITMASK;

		if (!_setOriginalStatus) {
			_setOriginalStatus = true;

			_originalStatus = _status;
		}

		_status = status;
	}

	public int getOriginalStatus() {
		return _originalStatus;
	}

	public long getStatusByUserId() {
		return _statusByUserId;
	}

	public void setStatusByUserId(long statusByUserId) {
		_statusByUserId = statusByUserId;
	}

	public String getStatusByUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getStatusByUserId(), "uuid",
			_statusByUserUuid);
	}

	public void setStatusByUserUuid(String statusByUserUuid) {
		_statusByUserUuid = statusByUserUuid;
	}

	public String getStatusByUserName() {
		if (_statusByUserName == null) {
			return StringPool.BLANK;
		}
		else {
			return _statusByUserName;
		}
	}

	public void setStatusByUserName(String statusByUserName) {
		_statusByUserName = statusByUserName;
	}

	public Date getStatusDate() {
		return _statusDate;
	}

	public void setStatusDate(Date statusDate) {
		_statusDate = statusDate;
	}

	/**
	 * @deprecated {@link #isApproved}
	 */
	public boolean getApproved() {
		return isApproved();
	}

	public boolean isApproved() {
		if (getStatus() == WorkflowConstants.STATUS_APPROVED) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isDraft() {
		if (getStatus() == WorkflowConstants.STATUS_DRAFT) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isExpired() {
		if (getStatus() == WorkflowConstants.STATUS_EXPIRED) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isPending() {
		if (getStatus() == WorkflowConstants.STATUS_PENDING) {
			return true;
		}
		else {
			return false;
		}
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public DDLRecordVersion toEscapedModel() {
		if (_escapedModelProxy == null) {
			_escapedModelProxy = (DDLRecordVersion)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelProxyInterfaces,
					new AutoEscapeBeanHandler(this));
		}

		return _escapedModelProxy;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		if (_expandoBridge == null) {
			_expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
					DDLRecordVersion.class.getName(), getPrimaryKey());
		}

		return _expandoBridge;
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		getExpandoBridge().setAttributes(serviceContext);
	}

	@Override
	public Object clone() {
		DDLRecordVersionImpl ddlRecordVersionImpl = new DDLRecordVersionImpl();

		ddlRecordVersionImpl.setRecordVersionId(getRecordVersionId());
		ddlRecordVersionImpl.setGroupId(getGroupId());
		ddlRecordVersionImpl.setCompanyId(getCompanyId());
		ddlRecordVersionImpl.setUserId(getUserId());
		ddlRecordVersionImpl.setUserName(getUserName());
		ddlRecordVersionImpl.setCreateDate(getCreateDate());
		ddlRecordVersionImpl.setDDMStorageId(getDDMStorageId());
		ddlRecordVersionImpl.setRecordSetId(getRecordSetId());
		ddlRecordVersionImpl.setRecordId(getRecordId());
		ddlRecordVersionImpl.setVersion(getVersion());
		ddlRecordVersionImpl.setDisplayIndex(getDisplayIndex());
		ddlRecordVersionImpl.setStatus(getStatus());
		ddlRecordVersionImpl.setStatusByUserId(getStatusByUserId());
		ddlRecordVersionImpl.setStatusByUserName(getStatusByUserName());
		ddlRecordVersionImpl.setStatusDate(getStatusDate());

		ddlRecordVersionImpl.resetOriginalValues();

		return ddlRecordVersionImpl;
	}

	public int compareTo(DDLRecordVersion ddlRecordVersion) {
		long primaryKey = ddlRecordVersion.getPrimaryKey();

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

		DDLRecordVersion ddlRecordVersion = null;

		try {
			ddlRecordVersion = (DDLRecordVersion)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = ddlRecordVersion.getPrimaryKey();

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
		DDLRecordVersionModelImpl ddlRecordVersionModelImpl = this;

		ddlRecordVersionModelImpl._originalRecordId = ddlRecordVersionModelImpl._recordId;

		ddlRecordVersionModelImpl._setOriginalRecordId = false;

		ddlRecordVersionModelImpl._originalVersion = ddlRecordVersionModelImpl._version;

		ddlRecordVersionModelImpl._originalStatus = ddlRecordVersionModelImpl._status;

		ddlRecordVersionModelImpl._setOriginalStatus = false;

		ddlRecordVersionModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<DDLRecordVersion> toCacheModel() {
		DDLRecordVersionCacheModel ddlRecordVersionCacheModel = new DDLRecordVersionCacheModel();

		ddlRecordVersionCacheModel.recordVersionId = getRecordVersionId();

		ddlRecordVersionCacheModel.groupId = getGroupId();

		ddlRecordVersionCacheModel.companyId = getCompanyId();

		ddlRecordVersionCacheModel.userId = getUserId();

		ddlRecordVersionCacheModel.userName = getUserName();

		String userName = ddlRecordVersionCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			ddlRecordVersionCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			ddlRecordVersionCacheModel.createDate = createDate.getTime();
		}
		else {
			ddlRecordVersionCacheModel.createDate = Long.MIN_VALUE;
		}

		ddlRecordVersionCacheModel.DDMStorageId = getDDMStorageId();

		ddlRecordVersionCacheModel.recordSetId = getRecordSetId();

		ddlRecordVersionCacheModel.recordId = getRecordId();

		ddlRecordVersionCacheModel.version = getVersion();

		String version = ddlRecordVersionCacheModel.version;

		if ((version != null) && (version.length() == 0)) {
			ddlRecordVersionCacheModel.version = null;
		}

		ddlRecordVersionCacheModel.displayIndex = getDisplayIndex();

		ddlRecordVersionCacheModel.status = getStatus();

		ddlRecordVersionCacheModel.statusByUserId = getStatusByUserId();

		ddlRecordVersionCacheModel.statusByUserName = getStatusByUserName();

		String statusByUserName = ddlRecordVersionCacheModel.statusByUserName;

		if ((statusByUserName != null) && (statusByUserName.length() == 0)) {
			ddlRecordVersionCacheModel.statusByUserName = null;
		}

		Date statusDate = getStatusDate();

		if (statusDate != null) {
			ddlRecordVersionCacheModel.statusDate = statusDate.getTime();
		}
		else {
			ddlRecordVersionCacheModel.statusDate = Long.MIN_VALUE;
		}

		return ddlRecordVersionCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(31);

		sb.append("{recordVersionId=");
		sb.append(getRecordVersionId());
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
		sb.append(", DDMStorageId=");
		sb.append(getDDMStorageId());
		sb.append(", recordSetId=");
		sb.append(getRecordSetId());
		sb.append(", recordId=");
		sb.append(getRecordId());
		sb.append(", version=");
		sb.append(getVersion());
		sb.append(", displayIndex=");
		sb.append(getDisplayIndex());
		sb.append(", status=");
		sb.append(getStatus());
		sb.append(", statusByUserId=");
		sb.append(getStatusByUserId());
		sb.append(", statusByUserName=");
		sb.append(getStatusByUserName());
		sb.append(", statusDate=");
		sb.append(getStatusDate());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(49);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portlet.dynamicdatalists.model.DDLRecordVersion");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>recordVersionId</column-name><column-value><![CDATA[");
		sb.append(getRecordVersionId());
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
			"<column><column-name>DDMStorageId</column-name><column-value><![CDATA[");
		sb.append(getDDMStorageId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>recordSetId</column-name><column-value><![CDATA[");
		sb.append(getRecordSetId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>recordId</column-name><column-value><![CDATA[");
		sb.append(getRecordId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>version</column-name><column-value><![CDATA[");
		sb.append(getVersion());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>displayIndex</column-name><column-value><![CDATA[");
		sb.append(getDisplayIndex());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>status</column-name><column-value><![CDATA[");
		sb.append(getStatus());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>statusByUserId</column-name><column-value><![CDATA[");
		sb.append(getStatusByUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>statusByUserName</column-name><column-value><![CDATA[");
		sb.append(getStatusByUserName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>statusDate</column-name><column-value><![CDATA[");
		sb.append(getStatusDate());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = DDLRecordVersion.class.getClassLoader();
	private static Class<?>[] _escapedModelProxyInterfaces = new Class[] {
			DDLRecordVersion.class
		};
	private long _recordVersionId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userUuid;
	private String _userName;
	private Date _createDate;
	private long _DDMStorageId;
	private long _recordSetId;
	private long _recordId;
	private long _originalRecordId;
	private boolean _setOriginalRecordId;
	private String _version;
	private String _originalVersion;
	private int _displayIndex;
	private int _status;
	private int _originalStatus;
	private boolean _setOriginalStatus;
	private long _statusByUserId;
	private String _statusByUserUuid;
	private String _statusByUserName;
	private Date _statusDate;
	private transient ExpandoBridge _expandoBridge;
	private long _columnBitmask;
	private DDLRecordVersion _escapedModelProxy;
}