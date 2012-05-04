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

package com.liferay.portlet.trash.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.DateUtil;
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
import com.liferay.portlet.trash.model.TrashEntry;
import com.liferay.portlet.trash.model.TrashEntryModel;
import com.liferay.portlet.trash.model.TrashEntrySoap;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the TrashEntry service. Represents a row in the &quot;TrashEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portlet.trash.model.TrashEntryModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link TrashEntryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TrashEntryImpl
 * @see com.liferay.portlet.trash.model.TrashEntry
 * @see com.liferay.portlet.trash.model.TrashEntryModel
 * @generated
 */
@JSON(strict = true)
public class TrashEntryModelImpl extends BaseModelImpl<TrashEntry>
	implements TrashEntryModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a trash entry model instance should use the {@link com.liferay.portlet.trash.model.TrashEntry} interface instead.
	 */
	public static final String TABLE_NAME = "TrashEntry";
	public static final Object[][] TABLE_COLUMNS = {
			{ "entryId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "createDate", Types.TIMESTAMP },
			{ "classNameId", Types.BIGINT },
			{ "classPK", Types.BIGINT },
			{ "typeSettings", Types.CLOB },
			{ "status", Types.INTEGER },
			{ "deletedByUserId", Types.BIGINT },
			{ "deletedByUserName", Types.VARCHAR }
		};
	public static final String TABLE_SQL_CREATE = "create table TrashEntry (entryId LONG not null primary key,groupId LONG,companyId LONG,createDate DATE null,classNameId LONG,classPK LONG,typeSettings TEXT null,status INTEGER,deletedByUserId LONG,deletedByUserName VARCHAR(75) null)";
	public static final String TABLE_SQL_DROP = "drop table TrashEntry";
	public static final String ORDER_BY_JPQL = " ORDER BY trashEntry.createDate DESC";
	public static final String ORDER_BY_SQL = " ORDER BY TrashEntry.createDate DESC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portlet.trash.model.TrashEntry"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portlet.trash.model.TrashEntry"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portlet.trash.model.TrashEntry"),
			true);
	public static long CLASSNAMEID_COLUMN_BITMASK = 1L;
	public static long CLASSPK_COLUMN_BITMASK = 2L;
	public static long COMPANYID_COLUMN_BITMASK = 4L;
	public static long GROUPID_COLUMN_BITMASK = 8L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static TrashEntry toModel(TrashEntrySoap soapModel) {
		TrashEntry model = new TrashEntryImpl();

		model.setEntryId(soapModel.getEntryId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setCreateDate(soapModel.getCreateDate());
		model.setClassNameId(soapModel.getClassNameId());
		model.setClassPK(soapModel.getClassPK());
		model.setTypeSettings(soapModel.getTypeSettings());
		model.setStatus(soapModel.getStatus());
		model.setDeletedByUserId(soapModel.getDeletedByUserId());
		model.setDeletedByUserName(soapModel.getDeletedByUserName());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<TrashEntry> toModels(TrashEntrySoap[] soapModels) {
		List<TrashEntry> models = new ArrayList<TrashEntry>(soapModels.length);

		for (TrashEntrySoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portlet.trash.model.TrashEntry"));

	public TrashEntryModelImpl() {
	}

	public long getPrimaryKey() {
		return _entryId;
	}

	public void setPrimaryKey(long primaryKey) {
		setEntryId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return new Long(_entryId);
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return TrashEntry.class;
	}

	public String getModelClassName() {
		return TrashEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("entryId", getEntryId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("createDate", getCreateDate());
		attributes.put("classNameId", getClassNameId());
		attributes.put("classPK", getClassPK());
		attributes.put("typeSettings", getTypeSettings());
		attributes.put("status", getStatus());
		attributes.put("deletedByUserId", getDeletedByUserId());
		attributes.put("deletedByUserName", getDeletedByUserName());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long entryId = (Long)attributes.get("entryId");

		if (entryId != null) {
			setEntryId(entryId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Long classNameId = (Long)attributes.get("classNameId");

		if (classNameId != null) {
			setClassNameId(classNameId);
		}

		Long classPK = (Long)attributes.get("classPK");

		if (classPK != null) {
			setClassPK(classPK);
		}

		String typeSettings = (String)attributes.get("typeSettings");

		if (typeSettings != null) {
			setTypeSettings(typeSettings);
		}

		Integer status = (Integer)attributes.get("status");

		if (status != null) {
			setStatus(status);
		}

		Long deletedByUserId = (Long)attributes.get("deletedByUserId");

		if (deletedByUserId != null) {
			setDeletedByUserId(deletedByUserId);
		}

		String deletedByUserName = (String)attributes.get("deletedByUserName");

		if (deletedByUserName != null) {
			setDeletedByUserName(deletedByUserName);
		}
	}

	@JSON
	public long getEntryId() {
		return _entryId;
	}

	public void setEntryId(long entryId) {
		_entryId = entryId;
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
	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_columnBitmask = -1L;

		_createDate = createDate;
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

	@JSON
	public String getTypeSettings() {
		if (_typeSettings == null) {
			return StringPool.BLANK;
		}
		else {
			return _typeSettings;
		}
	}

	public void setTypeSettings(String typeSettings) {
		_typeSettings = typeSettings;
	}

	@JSON
	public int getStatus() {
		return _status;
	}

	public void setStatus(int status) {
		_status = status;
	}

	@JSON
	public long getDeletedByUserId() {
		return _deletedByUserId;
	}

	public void setDeletedByUserId(long deletedByUserId) {
		_deletedByUserId = deletedByUserId;
	}

	public String getDeletedByUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getDeletedByUserId(), "uuid",
			_deletedByUserUuid);
	}

	public void setDeletedByUserUuid(String deletedByUserUuid) {
		_deletedByUserUuid = deletedByUserUuid;
	}

	@JSON
	public String getDeletedByUserName() {
		if (_deletedByUserName == null) {
			return StringPool.BLANK;
		}
		else {
			return _deletedByUserName;
		}
	}

	public void setDeletedByUserName(String deletedByUserName) {
		_deletedByUserName = deletedByUserName;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public TrashEntry toEscapedModel() {
		if (_escapedModelProxy == null) {
			_escapedModelProxy = (TrashEntry)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelProxyInterfaces,
					new AutoEscapeBeanHandler(this));
		}

		return _escapedModelProxy;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		if (_expandoBridge == null) {
			_expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
					TrashEntry.class.getName(), getPrimaryKey());
		}

		return _expandoBridge;
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		getExpandoBridge().setAttributes(serviceContext);
	}

	@Override
	public Object clone() {
		TrashEntryImpl trashEntryImpl = new TrashEntryImpl();

		trashEntryImpl.setEntryId(getEntryId());
		trashEntryImpl.setGroupId(getGroupId());
		trashEntryImpl.setCompanyId(getCompanyId());
		trashEntryImpl.setCreateDate(getCreateDate());
		trashEntryImpl.setClassNameId(getClassNameId());
		trashEntryImpl.setClassPK(getClassPK());
		trashEntryImpl.setTypeSettings(getTypeSettings());
		trashEntryImpl.setStatus(getStatus());
		trashEntryImpl.setDeletedByUserId(getDeletedByUserId());
		trashEntryImpl.setDeletedByUserName(getDeletedByUserName());

		trashEntryImpl.resetOriginalValues();

		return trashEntryImpl;
	}

	public int compareTo(TrashEntry trashEntry) {
		int value = 0;

		value = DateUtil.compareTo(getCreateDate(), trashEntry.getCreateDate());

		value = value * -1;

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

		TrashEntry trashEntry = null;

		try {
			trashEntry = (TrashEntry)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = trashEntry.getPrimaryKey();

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
		TrashEntryModelImpl trashEntryModelImpl = this;

		trashEntryModelImpl._originalGroupId = trashEntryModelImpl._groupId;

		trashEntryModelImpl._setOriginalGroupId = false;

		trashEntryModelImpl._originalCompanyId = trashEntryModelImpl._companyId;

		trashEntryModelImpl._setOriginalCompanyId = false;

		trashEntryModelImpl._originalClassNameId = trashEntryModelImpl._classNameId;

		trashEntryModelImpl._setOriginalClassNameId = false;

		trashEntryModelImpl._originalClassPK = trashEntryModelImpl._classPK;

		trashEntryModelImpl._setOriginalClassPK = false;

		trashEntryModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<TrashEntry> toCacheModel() {
		TrashEntryCacheModel trashEntryCacheModel = new TrashEntryCacheModel();

		trashEntryCacheModel.entryId = getEntryId();

		trashEntryCacheModel.groupId = getGroupId();

		trashEntryCacheModel.companyId = getCompanyId();

		Date createDate = getCreateDate();

		if (createDate != null) {
			trashEntryCacheModel.createDate = createDate.getTime();
		}
		else {
			trashEntryCacheModel.createDate = Long.MIN_VALUE;
		}

		trashEntryCacheModel.classNameId = getClassNameId();

		trashEntryCacheModel.classPK = getClassPK();

		trashEntryCacheModel.typeSettings = getTypeSettings();

		String typeSettings = trashEntryCacheModel.typeSettings;

		if ((typeSettings != null) && (typeSettings.length() == 0)) {
			trashEntryCacheModel.typeSettings = null;
		}

		trashEntryCacheModel.status = getStatus();

		trashEntryCacheModel.deletedByUserId = getDeletedByUserId();

		trashEntryCacheModel.deletedByUserName = getDeletedByUserName();

		String deletedByUserName = trashEntryCacheModel.deletedByUserName;

		if ((deletedByUserName != null) && (deletedByUserName.length() == 0)) {
			trashEntryCacheModel.deletedByUserName = null;
		}

		return trashEntryCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(21);

		sb.append("{entryId=");
		sb.append(getEntryId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", classNameId=");
		sb.append(getClassNameId());
		sb.append(", classPK=");
		sb.append(getClassPK());
		sb.append(", typeSettings=");
		sb.append(getTypeSettings());
		sb.append(", status=");
		sb.append(getStatus());
		sb.append(", deletedByUserId=");
		sb.append(getDeletedByUserId());
		sb.append(", deletedByUserName=");
		sb.append(getDeletedByUserName());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(34);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portlet.trash.model.TrashEntry");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>entryId</column-name><column-value><![CDATA[");
		sb.append(getEntryId());
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
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
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
			"<column><column-name>typeSettings</column-name><column-value><![CDATA[");
		sb.append(getTypeSettings());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>status</column-name><column-value><![CDATA[");
		sb.append(getStatus());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>deletedByUserId</column-name><column-value><![CDATA[");
		sb.append(getDeletedByUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>deletedByUserName</column-name><column-value><![CDATA[");
		sb.append(getDeletedByUserName());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = TrashEntry.class.getClassLoader();
	private static Class<?>[] _escapedModelProxyInterfaces = new Class[] {
			TrashEntry.class
		};
	private long _entryId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private Date _createDate;
	private long _classNameId;
	private long _originalClassNameId;
	private boolean _setOriginalClassNameId;
	private long _classPK;
	private long _originalClassPK;
	private boolean _setOriginalClassPK;
	private String _typeSettings;
	private int _status;
	private long _deletedByUserId;
	private String _deletedByUserUuid;
	private String _deletedByUserName;
	private transient ExpandoBridge _expandoBridge;
	private long _columnBitmask;
	private TrashEntry _escapedModelProxy;
}