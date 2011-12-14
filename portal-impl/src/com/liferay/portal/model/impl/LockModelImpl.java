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
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.Lock;
import com.liferay.portal.model.LockModel;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.Date;

/**
 * The base model implementation for the Lock service. Represents a row in the &quot;Lock_&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portal.model.LockModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link LockImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see LockImpl
 * @see com.liferay.portal.model.Lock
 * @see com.liferay.portal.model.LockModel
 * @generated
 */
public class LockModelImpl extends BaseModelImpl<Lock> implements LockModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a lock model instance should use the {@link com.liferay.portal.model.Lock} interface instead.
	 */
	public static final String TABLE_NAME = "Lock_";
	public static final Object[][] TABLE_COLUMNS = {
			{ "uuid_", Types.VARCHAR },
			{ "lockId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "className", Types.VARCHAR },
			{ "key_", Types.VARCHAR },
			{ "owner", Types.VARCHAR },
			{ "inheritable", Types.BOOLEAN },
			{ "expirationDate", Types.TIMESTAMP }
		};
	public static final String TABLE_SQL_CREATE = "create table Lock_ (uuid_ VARCHAR(75) null,lockId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,className VARCHAR(75) null,key_ VARCHAR(200) null,owner VARCHAR(255) null,inheritable BOOLEAN,expirationDate DATE null)";
	public static final String TABLE_SQL_DROP = "drop table Lock_";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portal.model.Lock"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portal.model.Lock"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.model.Lock"),
			true);
	public static long CLASSNAME_COLUMN_BITMASK = 1L;
	public static long EXPIRATIONDATE_COLUMN_BITMASK = 2L;
	public static long KEY_COLUMN_BITMASK = 4L;
	public static long OWNER_COLUMN_BITMASK = 8L;
	public static long UUID_COLUMN_BITMASK = 16L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portal.model.Lock"));

	public LockModelImpl() {
	}

	public long getPrimaryKey() {
		return _lockId;
	}

	public void setPrimaryKey(long primaryKey) {
		setLockId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return new Long(_lockId);
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return Lock.class;
	}

	public String getModelClassName() {
		return Lock.class.getName();
	}

	public String getUuid() {
		if (_uuid == null) {
			return StringPool.BLANK;
		}
		else {
			return _uuid;
		}
	}

	public void setUuid(String uuid) {
		if (_originalUuid == null) {
			_originalUuid = _uuid;
		}

		_uuid = uuid;
	}

	public String getOriginalUuid() {
		return GetterUtil.getString(_originalUuid);
	}

	public long getLockId() {
		return _lockId;
	}

	public void setLockId(long lockId) {
		_lockId = lockId;
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

	public String getClassName() {
		if (_className == null) {
			return StringPool.BLANK;
		}
		else {
			return _className;
		}
	}

	public void setClassName(String className) {
		_columnBitmask |= CLASSNAME_COLUMN_BITMASK;

		if (_originalClassName == null) {
			_originalClassName = _className;
		}

		_className = className;
	}

	public String getOriginalClassName() {
		return GetterUtil.getString(_originalClassName);
	}

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

	public String getOwner() {
		if (_owner == null) {
			return StringPool.BLANK;
		}
		else {
			return _owner;
		}
	}

	public void setOwner(String owner) {
		_columnBitmask |= OWNER_COLUMN_BITMASK;

		if (_originalOwner == null) {
			_originalOwner = _owner;
		}

		_owner = owner;
	}

	public String getOriginalOwner() {
		return GetterUtil.getString(_originalOwner);
	}

	public boolean getInheritable() {
		return _inheritable;
	}

	public boolean isInheritable() {
		return _inheritable;
	}

	public void setInheritable(boolean inheritable) {
		_inheritable = inheritable;
	}

	public Date getExpirationDate() {
		return _expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		_columnBitmask |= EXPIRATIONDATE_COLUMN_BITMASK;

		if (_originalExpirationDate == null) {
			_originalExpirationDate = _expirationDate;
		}

		_expirationDate = expirationDate;
	}

	public Date getOriginalExpirationDate() {
		return _originalExpirationDate;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public Lock toEscapedModel() {
		if (_escapedModelProxy == null) {
			_escapedModelProxy = (Lock)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelProxyInterfaces,
					new AutoEscapeBeanHandler(this));
		}

		return _escapedModelProxy;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		if (_expandoBridge == null) {
			_expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
					Lock.class.getName(), getPrimaryKey());
		}

		return _expandoBridge;
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		getExpandoBridge().setAttributes(serviceContext);
	}

	@Override
	public Object clone() {
		LockImpl lockImpl = new LockImpl();

		lockImpl.setUuid(getUuid());
		lockImpl.setLockId(getLockId());
		lockImpl.setCompanyId(getCompanyId());
		lockImpl.setUserId(getUserId());
		lockImpl.setUserName(getUserName());
		lockImpl.setCreateDate(getCreateDate());
		lockImpl.setClassName(getClassName());
		lockImpl.setKey(getKey());
		lockImpl.setOwner(getOwner());
		lockImpl.setInheritable(getInheritable());
		lockImpl.setExpirationDate(getExpirationDate());

		lockImpl.resetOriginalValues();

		return lockImpl;
	}

	public int compareTo(Lock lock) {
		long primaryKey = lock.getPrimaryKey();

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

		Lock lock = null;

		try {
			lock = (Lock)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = lock.getPrimaryKey();

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
		LockModelImpl lockModelImpl = this;

		lockModelImpl._originalUuid = lockModelImpl._uuid;

		lockModelImpl._originalClassName = lockModelImpl._className;

		lockModelImpl._originalKey = lockModelImpl._key;

		lockModelImpl._originalOwner = lockModelImpl._owner;

		lockModelImpl._originalExpirationDate = lockModelImpl._expirationDate;

		lockModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<Lock> toCacheModel() {
		LockCacheModel lockCacheModel = new LockCacheModel();

		lockCacheModel.uuid = getUuid();

		String uuid = lockCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			lockCacheModel.uuid = null;
		}

		lockCacheModel.lockId = getLockId();

		lockCacheModel.companyId = getCompanyId();

		lockCacheModel.userId = getUserId();

		lockCacheModel.userName = getUserName();

		String userName = lockCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			lockCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			lockCacheModel.createDate = createDate.getTime();
		}
		else {
			lockCacheModel.createDate = Long.MIN_VALUE;
		}

		lockCacheModel.className = getClassName();

		String className = lockCacheModel.className;

		if ((className != null) && (className.length() == 0)) {
			lockCacheModel.className = null;
		}

		lockCacheModel.key = getKey();

		String key = lockCacheModel.key;

		if ((key != null) && (key.length() == 0)) {
			lockCacheModel.key = null;
		}

		lockCacheModel.owner = getOwner();

		String owner = lockCacheModel.owner;

		if ((owner != null) && (owner.length() == 0)) {
			lockCacheModel.owner = null;
		}

		lockCacheModel.inheritable = getInheritable();

		Date expirationDate = getExpirationDate();

		if (expirationDate != null) {
			lockCacheModel.expirationDate = expirationDate.getTime();
		}
		else {
			lockCacheModel.expirationDate = Long.MIN_VALUE;
		}

		return lockCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(23);

		sb.append("{uuid=");
		sb.append(getUuid());
		sb.append(", lockId=");
		sb.append(getLockId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", className=");
		sb.append(getClassName());
		sb.append(", key=");
		sb.append(getKey());
		sb.append(", owner=");
		sb.append(getOwner());
		sb.append(", inheritable=");
		sb.append(getInheritable());
		sb.append(", expirationDate=");
		sb.append(getExpirationDate());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(37);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portal.model.Lock");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>uuid</column-name><column-value><![CDATA[");
		sb.append(getUuid());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>lockId</column-name><column-value><![CDATA[");
		sb.append(getLockId());
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
			"<column><column-name>className</column-name><column-value><![CDATA[");
		sb.append(getClassName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>key</column-name><column-value><![CDATA[");
		sb.append(getKey());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>owner</column-name><column-value><![CDATA[");
		sb.append(getOwner());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>inheritable</column-name><column-value><![CDATA[");
		sb.append(getInheritable());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>expirationDate</column-name><column-value><![CDATA[");
		sb.append(getExpirationDate());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = Lock.class.getClassLoader();
	private static Class<?>[] _escapedModelProxyInterfaces = new Class[] {
			Lock.class
		};
	private String _uuid;
	private String _originalUuid;
	private long _lockId;
	private long _companyId;
	private long _userId;
	private String _userUuid;
	private String _userName;
	private Date _createDate;
	private String _className;
	private String _originalClassName;
	private String _key;
	private String _originalKey;
	private String _owner;
	private String _originalOwner;
	private boolean _inheritable;
	private Date _expirationDate;
	private Date _originalExpirationDate;
	private transient ExpandoBridge _expandoBridge;
	private long _columnBitmask;
	private Lock _escapedModelProxy;
}