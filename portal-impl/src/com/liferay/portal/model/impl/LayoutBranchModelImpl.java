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
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.LayoutBranch;
import com.liferay.portal.model.LayoutBranchModel;
import com.liferay.portal.model.LayoutBranchSoap;
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
 * The base model implementation for the LayoutBranch service. Represents a row in the &quot;LayoutBranch&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portal.model.LayoutBranchModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link LayoutBranchImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see LayoutBranchImpl
 * @see com.liferay.portal.model.LayoutBranch
 * @see com.liferay.portal.model.LayoutBranchModel
 * @generated
 */
@JSON(strict = true)
public class LayoutBranchModelImpl extends BaseModelImpl<LayoutBranch>
	implements LayoutBranchModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a layout branch model instance should use the {@link com.liferay.portal.model.LayoutBranch} interface instead.
	 */
	public static final String TABLE_NAME = "LayoutBranch";
	public static final Object[][] TABLE_COLUMNS = {
			{ "LayoutBranchId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "layoutSetBranchId", Types.BIGINT },
			{ "plid", Types.BIGINT },
			{ "name", Types.VARCHAR },
			{ "description", Types.VARCHAR },
			{ "master", Types.BOOLEAN }
		};
	public static final String TABLE_SQL_CREATE = "create table LayoutBranch (LayoutBranchId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,layoutSetBranchId LONG,plid LONG,name VARCHAR(75) null,description STRING null,master BOOLEAN)";
	public static final String TABLE_SQL_DROP = "drop table LayoutBranch";
	public static final String ORDER_BY_JPQL = " ORDER BY layoutBranch.LayoutBranchId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY LayoutBranch.LayoutBranchId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portal.model.LayoutBranch"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portal.model.LayoutBranch"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.model.LayoutBranch"),
			true);
	public static long LAYOUTSETBRANCHID_COLUMN_BITMASK = 1L;
	public static long MASTER_COLUMN_BITMASK = 2L;
	public static long NAME_COLUMN_BITMASK = 4L;
	public static long PLID_COLUMN_BITMASK = 8L;
	public static long LAYOUTBRANCHID_COLUMN_BITMASK = 16L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static LayoutBranch toModel(LayoutBranchSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		LayoutBranch model = new LayoutBranchImpl();

		model.setLayoutBranchId(soapModel.getLayoutBranchId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setLayoutSetBranchId(soapModel.getLayoutSetBranchId());
		model.setPlid(soapModel.getPlid());
		model.setName(soapModel.getName());
		model.setDescription(soapModel.getDescription());
		model.setMaster(soapModel.getMaster());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<LayoutBranch> toModels(LayoutBranchSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<LayoutBranch> models = new ArrayList<LayoutBranch>(soapModels.length);

		for (LayoutBranchSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portal.model.LayoutBranch"));

	public LayoutBranchModelImpl() {
	}

	public long getPrimaryKey() {
		return _LayoutBranchId;
	}

	public void setPrimaryKey(long primaryKey) {
		setLayoutBranchId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return _LayoutBranchId;
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return LayoutBranch.class;
	}

	public String getModelClassName() {
		return LayoutBranch.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("LayoutBranchId", getLayoutBranchId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("layoutSetBranchId", getLayoutSetBranchId());
		attributes.put("plid", getPlid());
		attributes.put("name", getName());
		attributes.put("description", getDescription());
		attributes.put("master", getMaster());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long LayoutBranchId = (Long)attributes.get("LayoutBranchId");

		if (LayoutBranchId != null) {
			setLayoutBranchId(LayoutBranchId);
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

		Long layoutSetBranchId = (Long)attributes.get("layoutSetBranchId");

		if (layoutSetBranchId != null) {
			setLayoutSetBranchId(layoutSetBranchId);
		}

		Long plid = (Long)attributes.get("plid");

		if (plid != null) {
			setPlid(plid);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		Boolean master = (Boolean)attributes.get("master");

		if (master != null) {
			setMaster(master);
		}
	}

	@JSON
	public long getLayoutBranchId() {
		return _LayoutBranchId;
	}

	public void setLayoutBranchId(long LayoutBranchId) {
		_LayoutBranchId = LayoutBranchId;
	}

	@JSON
	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	@JSON
	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
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
	public long getLayoutSetBranchId() {
		return _layoutSetBranchId;
	}

	public void setLayoutSetBranchId(long layoutSetBranchId) {
		_columnBitmask |= LAYOUTSETBRANCHID_COLUMN_BITMASK;

		if (!_setOriginalLayoutSetBranchId) {
			_setOriginalLayoutSetBranchId = true;

			_originalLayoutSetBranchId = _layoutSetBranchId;
		}

		_layoutSetBranchId = layoutSetBranchId;
	}

	public long getOriginalLayoutSetBranchId() {
		return _originalLayoutSetBranchId;
	}

	@JSON
	public long getPlid() {
		return _plid;
	}

	public void setPlid(long plid) {
		_columnBitmask |= PLID_COLUMN_BITMASK;

		if (!_setOriginalPlid) {
			_setOriginalPlid = true;

			_originalPlid = _plid;
		}

		_plid = plid;
	}

	public long getOriginalPlid() {
		return _originalPlid;
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
	public String getDescription() {
		if (_description == null) {
			return StringPool.BLANK;
		}
		else {
			return _description;
		}
	}

	public void setDescription(String description) {
		_description = description;
	}

	@JSON
	public boolean getMaster() {
		return _master;
	}

	public boolean isMaster() {
		return _master;
	}

	public void setMaster(boolean master) {
		_columnBitmask |= MASTER_COLUMN_BITMASK;

		if (!_setOriginalMaster) {
			_setOriginalMaster = true;

			_originalMaster = _master;
		}

		_master = master;
	}

	public boolean getOriginalMaster() {
		return _originalMaster;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			LayoutBranch.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public LayoutBranch toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (LayoutBranch)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		LayoutBranchImpl layoutBranchImpl = new LayoutBranchImpl();

		layoutBranchImpl.setLayoutBranchId(getLayoutBranchId());
		layoutBranchImpl.setGroupId(getGroupId());
		layoutBranchImpl.setCompanyId(getCompanyId());
		layoutBranchImpl.setUserId(getUserId());
		layoutBranchImpl.setUserName(getUserName());
		layoutBranchImpl.setLayoutSetBranchId(getLayoutSetBranchId());
		layoutBranchImpl.setPlid(getPlid());
		layoutBranchImpl.setName(getName());
		layoutBranchImpl.setDescription(getDescription());
		layoutBranchImpl.setMaster(getMaster());

		layoutBranchImpl.resetOriginalValues();

		return layoutBranchImpl;
	}

	public int compareTo(LayoutBranch layoutBranch) {
		long primaryKey = layoutBranch.getPrimaryKey();

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

		LayoutBranch layoutBranch = null;

		try {
			layoutBranch = (LayoutBranch)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = layoutBranch.getPrimaryKey();

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
		LayoutBranchModelImpl layoutBranchModelImpl = this;

		layoutBranchModelImpl._originalLayoutSetBranchId = layoutBranchModelImpl._layoutSetBranchId;

		layoutBranchModelImpl._setOriginalLayoutSetBranchId = false;

		layoutBranchModelImpl._originalPlid = layoutBranchModelImpl._plid;

		layoutBranchModelImpl._setOriginalPlid = false;

		layoutBranchModelImpl._originalName = layoutBranchModelImpl._name;

		layoutBranchModelImpl._originalMaster = layoutBranchModelImpl._master;

		layoutBranchModelImpl._setOriginalMaster = false;

		layoutBranchModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<LayoutBranch> toCacheModel() {
		LayoutBranchCacheModel layoutBranchCacheModel = new LayoutBranchCacheModel();

		layoutBranchCacheModel.LayoutBranchId = getLayoutBranchId();

		layoutBranchCacheModel.groupId = getGroupId();

		layoutBranchCacheModel.companyId = getCompanyId();

		layoutBranchCacheModel.userId = getUserId();

		layoutBranchCacheModel.userName = getUserName();

		String userName = layoutBranchCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			layoutBranchCacheModel.userName = null;
		}

		layoutBranchCacheModel.layoutSetBranchId = getLayoutSetBranchId();

		layoutBranchCacheModel.plid = getPlid();

		layoutBranchCacheModel.name = getName();

		String name = layoutBranchCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			layoutBranchCacheModel.name = null;
		}

		layoutBranchCacheModel.description = getDescription();

		String description = layoutBranchCacheModel.description;

		if ((description != null) && (description.length() == 0)) {
			layoutBranchCacheModel.description = null;
		}

		layoutBranchCacheModel.master = getMaster();

		return layoutBranchCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(21);

		sb.append("{LayoutBranchId=");
		sb.append(getLayoutBranchId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", layoutSetBranchId=");
		sb.append(getLayoutSetBranchId());
		sb.append(", plid=");
		sb.append(getPlid());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", description=");
		sb.append(getDescription());
		sb.append(", master=");
		sb.append(getMaster());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(34);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portal.model.LayoutBranch");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>LayoutBranchId</column-name><column-value><![CDATA[");
		sb.append(getLayoutBranchId());
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
			"<column><column-name>layoutSetBranchId</column-name><column-value><![CDATA[");
		sb.append(getLayoutSetBranchId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>plid</column-name><column-value><![CDATA[");
		sb.append(getPlid());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>name</column-name><column-value><![CDATA[");
		sb.append(getName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>description</column-name><column-value><![CDATA[");
		sb.append(getDescription());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>master</column-name><column-value><![CDATA[");
		sb.append(getMaster());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = LayoutBranch.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			LayoutBranch.class
		};
	private long _LayoutBranchId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userUuid;
	private String _userName;
	private long _layoutSetBranchId;
	private long _originalLayoutSetBranchId;
	private boolean _setOriginalLayoutSetBranchId;
	private long _plid;
	private long _originalPlid;
	private boolean _setOriginalPlid;
	private String _name;
	private String _originalName;
	private String _description;
	private boolean _master;
	private boolean _originalMaster;
	private boolean _setOriginalMaster;
	private long _columnBitmask;
	private LayoutBranch _escapedModel;
}