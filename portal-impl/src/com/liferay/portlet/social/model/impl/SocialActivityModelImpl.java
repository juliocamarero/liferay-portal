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

package com.liferay.portlet.social.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import com.liferay.social.kernel.model.SocialActivity;
import com.liferay.social.kernel.model.SocialActivityModel;
import com.liferay.social.kernel.model.SocialActivitySoap;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the SocialActivity service. Represents a row in the &quot;SocialActivity&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link SocialActivityModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link SocialActivityImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SocialActivityImpl
 * @see SocialActivity
 * @see SocialActivityModel
 * @generated
 */
@JSON(strict = true)
@ProviderType
public class SocialActivityModelImpl extends BaseModelImpl<SocialActivity>
	implements SocialActivityModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a social activity model instance should use the {@link SocialActivity} interface instead.
	 */
	public static final String TABLE_NAME = "SocialActivity";
	public static final Object[][] TABLE_COLUMNS = {
			{ "activityId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "createDate", Types.BIGINT },
			{ "activitySetId", Types.BIGINT },
			{ "mirrorActivityId", Types.BIGINT },
			{ "classNameId", Types.BIGINT },
			{ "classPK", Types.BIGINT },
			{ "parentClassNameId", Types.BIGINT },
			{ "parentClassPK", Types.BIGINT },
			{ "type_", Types.INTEGER },
			{ "extraData", Types.CLOB },
			{ "receiverUserId", Types.BIGINT }
		};
	public static final Map<String, Integer> TABLE_COLUMNS_MAP = new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("activityId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("createDate", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("activitySetId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("mirrorActivityId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("classNameId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("classPK", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("parentClassNameId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("parentClassPK", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("type_", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("extraData", Types.CLOB);
		TABLE_COLUMNS_MAP.put("receiverUserId", Types.BIGINT);
	}

	public static final String TABLE_SQL_CREATE = "create table SocialActivity (activityId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,createDate LONG,activitySetId LONG,mirrorActivityId LONG,classNameId LONG,classPK LONG,parentClassNameId LONG,parentClassPK LONG,type_ INTEGER,extraData TEXT null,receiverUserId LONG)";
	public static final String TABLE_SQL_DROP = "drop table SocialActivity";
	public static final String ORDER_BY_JPQL = " ORDER BY socialActivity.createDate DESC";
	public static final String ORDER_BY_SQL = " ORDER BY SocialActivity.createDate DESC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.social.kernel.model.SocialActivity"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.social.kernel.model.SocialActivity"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.social.kernel.model.SocialActivity"),
			true);
	public static final long ACTIVITYSETID_COLUMN_BITMASK = 1L;
	public static final long CLASSNAMEID_COLUMN_BITMASK = 2L;
	public static final long CLASSPK_COLUMN_BITMASK = 4L;
	public static final long COMPANYID_COLUMN_BITMASK = 8L;
	public static final long CREATEDATE_COLUMN_BITMASK = 16L;
	public static final long GROUPID_COLUMN_BITMASK = 32L;
	public static final long MIRRORACTIVITYID_COLUMN_BITMASK = 64L;
	public static final long RECEIVERUSERID_COLUMN_BITMASK = 128L;
	public static final long TYPE_COLUMN_BITMASK = 256L;
	public static final long USERID_COLUMN_BITMASK = 512L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static SocialActivity toModel(SocialActivitySoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		SocialActivity model = new SocialActivityImpl();

		model.setActivityId(soapModel.getActivityId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setCreateDate(soapModel.getCreateDate());
		model.setActivitySetId(soapModel.getActivitySetId());
		model.setMirrorActivityId(soapModel.getMirrorActivityId());
		model.setClassNameId(soapModel.getClassNameId());
		model.setClassPK(soapModel.getClassPK());
		model.setParentClassNameId(soapModel.getParentClassNameId());
		model.setParentClassPK(soapModel.getParentClassPK());
		model.setType(soapModel.getType());
		model.setExtraData(soapModel.getExtraData());
		model.setReceiverUserId(soapModel.getReceiverUserId());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<SocialActivity> toModels(SocialActivitySoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<SocialActivity> models = new ArrayList<SocialActivity>(soapModels.length);

		for (SocialActivitySoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.social.kernel.model.SocialActivity"));

	public SocialActivityModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _activityId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setActivityId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _activityId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return SocialActivity.class;
	}

	@Override
	public String getModelClassName() {
		return SocialActivity.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("activityId", getActivityId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("createDate", getCreateDate());
		attributes.put("activitySetId", getActivitySetId());
		attributes.put("mirrorActivityId", getMirrorActivityId());
		attributes.put("classNameId", getClassNameId());
		attributes.put("classPK", getClassPK());
		attributes.put("parentClassNameId", getParentClassNameId());
		attributes.put("parentClassPK", getParentClassPK());
		attributes.put("type", getType());
		attributes.put("extraData", getExtraData());
		attributes.put("receiverUserId", getReceiverUserId());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long activityId = (Long)attributes.get("activityId");

		if (activityId != null) {
			setActivityId(activityId);
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

		Long createDate = (Long)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Long activitySetId = (Long)attributes.get("activitySetId");

		if (activitySetId != null) {
			setActivitySetId(activitySetId);
		}

		Long mirrorActivityId = (Long)attributes.get("mirrorActivityId");

		if (mirrorActivityId != null) {
			setMirrorActivityId(mirrorActivityId);
		}

		Long classNameId = (Long)attributes.get("classNameId");

		if (classNameId != null) {
			setClassNameId(classNameId);
		}

		Long classPK = (Long)attributes.get("classPK");

		if (classPK != null) {
			setClassPK(classPK);
		}

		Long parentClassNameId = (Long)attributes.get("parentClassNameId");

		if (parentClassNameId != null) {
			setParentClassNameId(parentClassNameId);
		}

		Long parentClassPK = (Long)attributes.get("parentClassPK");

		if (parentClassPK != null) {
			setParentClassPK(parentClassPK);
		}

		Integer type = (Integer)attributes.get("type");

		if (type != null) {
			setType(type);
		}

		String extraData = (String)attributes.get("extraData");

		if (extraData != null) {
			setExtraData(extraData);
		}

		Long receiverUserId = (Long)attributes.get("receiverUserId");

		if (receiverUserId != null) {
			setReceiverUserId(receiverUserId);
		}
	}

	@JSON
	@Override
	public long getActivityId() {
		return _activityId;
	}

	@Override
	public void setActivityId(long activityId) {
		_activityId = activityId;
	}

	@JSON
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

	@JSON
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

	@JSON
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

	@JSON
	@Override
	public long getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(long createDate) {
		_columnBitmask = -1L;

		if (!_setOriginalCreateDate) {
			_setOriginalCreateDate = true;

			_originalCreateDate = _createDate;
		}

		_createDate = createDate;
	}

	public long getOriginalCreateDate() {
		return _originalCreateDate;
	}

	@JSON
	@Override
	public long getActivitySetId() {
		return _activitySetId;
	}

	@Override
	public void setActivitySetId(long activitySetId) {
		_columnBitmask |= ACTIVITYSETID_COLUMN_BITMASK;

		if (!_setOriginalActivitySetId) {
			_setOriginalActivitySetId = true;

			_originalActivitySetId = _activitySetId;
		}

		_activitySetId = activitySetId;
	}

	public long getOriginalActivitySetId() {
		return _originalActivitySetId;
	}

	@JSON
	@Override
	public long getMirrorActivityId() {
		return _mirrorActivityId;
	}

	@Override
	public void setMirrorActivityId(long mirrorActivityId) {
		_columnBitmask |= MIRRORACTIVITYID_COLUMN_BITMASK;

		if (!_setOriginalMirrorActivityId) {
			_setOriginalMirrorActivityId = true;

			_originalMirrorActivityId = _mirrorActivityId;
		}

		_mirrorActivityId = mirrorActivityId;
	}

	public long getOriginalMirrorActivityId() {
		return _originalMirrorActivityId;
	}

	@Override
	public String getClassName() {
		if (getClassNameId() <= 0) {
			return StringPool.BLANK;
		}

		return PortalUtil.getClassName(getClassNameId());
	}

	@Override
	public void setClassName(String className) {
		long classNameId = 0;

		if (Validator.isNotNull(className)) {
			classNameId = PortalUtil.getClassNameId(className);
		}

		setClassNameId(classNameId);
	}

	@JSON
	@Override
	public long getClassNameId() {
		return _classNameId;
	}

	@Override
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
	@Override
	public long getClassPK() {
		return _classPK;
	}

	@Override
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
	@Override
	public long getParentClassNameId() {
		return _parentClassNameId;
	}

	@Override
	public void setParentClassNameId(long parentClassNameId) {
		_parentClassNameId = parentClassNameId;
	}

	@JSON
	@Override
	public long getParentClassPK() {
		return _parentClassPK;
	}

	@Override
	public void setParentClassPK(long parentClassPK) {
		_parentClassPK = parentClassPK;
	}

	@JSON
	@Override
	public int getType() {
		return _type;
	}

	@Override
	public void setType(int type) {
		_columnBitmask |= TYPE_COLUMN_BITMASK;

		if (!_setOriginalType) {
			_setOriginalType = true;

			_originalType = _type;
		}

		_type = type;
	}

	public int getOriginalType() {
		return _originalType;
	}

	@JSON
	@Override
	public String getExtraData() {
		if (_extraData == null) {
			return StringPool.BLANK;
		}
		else {
			return _extraData;
		}
	}

	@Override
	public void setExtraData(String extraData) {
		_extraData = extraData;
	}

	@JSON
	@Override
	public long getReceiverUserId() {
		return _receiverUserId;
	}

	@Override
	public void setReceiverUserId(long receiverUserId) {
		_columnBitmask |= RECEIVERUSERID_COLUMN_BITMASK;

		if (!_setOriginalReceiverUserId) {
			_setOriginalReceiverUserId = true;

			_originalReceiverUserId = _receiverUserId;
		}

		_receiverUserId = receiverUserId;
	}

	@Override
	public String getReceiverUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getReceiverUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return StringPool.BLANK;
		}
	}

	@Override
	public void setReceiverUserUuid(String receiverUserUuid) {
	}

	public long getOriginalReceiverUserId() {
		return _originalReceiverUserId;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			SocialActivity.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public SocialActivity toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (SocialActivity)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		SocialActivityImpl socialActivityImpl = new SocialActivityImpl();

		socialActivityImpl.setActivityId(getActivityId());
		socialActivityImpl.setGroupId(getGroupId());
		socialActivityImpl.setCompanyId(getCompanyId());
		socialActivityImpl.setUserId(getUserId());
		socialActivityImpl.setCreateDate(getCreateDate());
		socialActivityImpl.setActivitySetId(getActivitySetId());
		socialActivityImpl.setMirrorActivityId(getMirrorActivityId());
		socialActivityImpl.setClassNameId(getClassNameId());
		socialActivityImpl.setClassPK(getClassPK());
		socialActivityImpl.setParentClassNameId(getParentClassNameId());
		socialActivityImpl.setParentClassPK(getParentClassPK());
		socialActivityImpl.setType(getType());
		socialActivityImpl.setExtraData(getExtraData());
		socialActivityImpl.setReceiverUserId(getReceiverUserId());

		socialActivityImpl.resetOriginalValues();

		return socialActivityImpl;
	}

	@Override
	public int compareTo(SocialActivity socialActivity) {
		int value = 0;

		if (getCreateDate() < socialActivity.getCreateDate()) {
			value = -1;
		}
		else if (getCreateDate() > socialActivity.getCreateDate()) {
			value = 1;
		}
		else {
			value = 0;
		}

		value = value * -1;

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SocialActivity)) {
			return false;
		}

		SocialActivity socialActivity = (SocialActivity)obj;

		long primaryKey = socialActivity.getPrimaryKey();

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
		SocialActivityModelImpl socialActivityModelImpl = this;

		socialActivityModelImpl._originalGroupId = socialActivityModelImpl._groupId;

		socialActivityModelImpl._setOriginalGroupId = false;

		socialActivityModelImpl._originalCompanyId = socialActivityModelImpl._companyId;

		socialActivityModelImpl._setOriginalCompanyId = false;

		socialActivityModelImpl._originalUserId = socialActivityModelImpl._userId;

		socialActivityModelImpl._setOriginalUserId = false;

		socialActivityModelImpl._originalCreateDate = socialActivityModelImpl._createDate;

		socialActivityModelImpl._setOriginalCreateDate = false;

		socialActivityModelImpl._originalActivitySetId = socialActivityModelImpl._activitySetId;

		socialActivityModelImpl._setOriginalActivitySetId = false;

		socialActivityModelImpl._originalMirrorActivityId = socialActivityModelImpl._mirrorActivityId;

		socialActivityModelImpl._setOriginalMirrorActivityId = false;

		socialActivityModelImpl._originalClassNameId = socialActivityModelImpl._classNameId;

		socialActivityModelImpl._setOriginalClassNameId = false;

		socialActivityModelImpl._originalClassPK = socialActivityModelImpl._classPK;

		socialActivityModelImpl._setOriginalClassPK = false;

		socialActivityModelImpl._originalType = socialActivityModelImpl._type;

		socialActivityModelImpl._setOriginalType = false;

		socialActivityModelImpl._originalReceiverUserId = socialActivityModelImpl._receiverUserId;

		socialActivityModelImpl._setOriginalReceiverUserId = false;

		socialActivityModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<SocialActivity> toCacheModel() {
		SocialActivityCacheModel socialActivityCacheModel = new SocialActivityCacheModel();

		socialActivityCacheModel.activityId = getActivityId();

		socialActivityCacheModel.groupId = getGroupId();

		socialActivityCacheModel.companyId = getCompanyId();

		socialActivityCacheModel.userId = getUserId();

		socialActivityCacheModel.createDate = getCreateDate();

		socialActivityCacheModel.activitySetId = getActivitySetId();

		socialActivityCacheModel.mirrorActivityId = getMirrorActivityId();

		socialActivityCacheModel.classNameId = getClassNameId();

		socialActivityCacheModel.classPK = getClassPK();

		socialActivityCacheModel.parentClassNameId = getParentClassNameId();

		socialActivityCacheModel.parentClassPK = getParentClassPK();

		socialActivityCacheModel.type = getType();

		socialActivityCacheModel.extraData = getExtraData();

		String extraData = socialActivityCacheModel.extraData;

		if ((extraData != null) && (extraData.length() == 0)) {
			socialActivityCacheModel.extraData = null;
		}

		socialActivityCacheModel.receiverUserId = getReceiverUserId();

		return socialActivityCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(29);

		sb.append("{activityId=");
		sb.append(getActivityId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", activitySetId=");
		sb.append(getActivitySetId());
		sb.append(", mirrorActivityId=");
		sb.append(getMirrorActivityId());
		sb.append(", classNameId=");
		sb.append(getClassNameId());
		sb.append(", classPK=");
		sb.append(getClassPK());
		sb.append(", parentClassNameId=");
		sb.append(getParentClassNameId());
		sb.append(", parentClassPK=");
		sb.append(getParentClassPK());
		sb.append(", type=");
		sb.append(getType());
		sb.append(", extraData=");
		sb.append(getExtraData());
		sb.append(", receiverUserId=");
		sb.append(getReceiverUserId());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(46);

		sb.append("<model><model-name>");
		sb.append("com.liferay.social.kernel.model.SocialActivity");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>activityId</column-name><column-value><![CDATA[");
		sb.append(getActivityId());
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
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>activitySetId</column-name><column-value><![CDATA[");
		sb.append(getActivitySetId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>mirrorActivityId</column-name><column-value><![CDATA[");
		sb.append(getMirrorActivityId());
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
			"<column><column-name>parentClassNameId</column-name><column-value><![CDATA[");
		sb.append(getParentClassNameId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>parentClassPK</column-name><column-value><![CDATA[");
		sb.append(getParentClassPK());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>type</column-name><column-value><![CDATA[");
		sb.append(getType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>extraData</column-name><column-value><![CDATA[");
		sb.append(getExtraData());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>receiverUserId</column-name><column-value><![CDATA[");
		sb.append(getReceiverUserId());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = SocialActivity.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			SocialActivity.class
		};
	private long _activityId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private long _originalUserId;
	private boolean _setOriginalUserId;
	private long _createDate;
	private long _originalCreateDate;
	private boolean _setOriginalCreateDate;
	private long _activitySetId;
	private long _originalActivitySetId;
	private boolean _setOriginalActivitySetId;
	private long _mirrorActivityId;
	private long _originalMirrorActivityId;
	private boolean _setOriginalMirrorActivityId;
	private long _classNameId;
	private long _originalClassNameId;
	private boolean _setOriginalClassNameId;
	private long _classPK;
	private long _originalClassPK;
	private boolean _setOriginalClassPK;
	private long _parentClassNameId;
	private long _parentClassPK;
	private int _type;
	private int _originalType;
	private boolean _setOriginalType;
	private String _extraData;
	private long _receiverUserId;
	private long _originalReceiverUserId;
	private boolean _setOriginalReceiverUserId;
	private long _columnBitmask;
	private SocialActivity _escapedModel;
}