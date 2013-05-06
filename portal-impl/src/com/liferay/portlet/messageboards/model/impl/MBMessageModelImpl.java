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

package com.liferay.portlet.messageboards.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBMessageModel;
import com.liferay.portlet.messageboards.model.MBMessageSoap;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the MBMessage service. Represents a row in the &quot;MBMessage&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portlet.messageboards.model.MBMessageModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link MBMessageImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see MBMessageImpl
 * @see com.liferay.portlet.messageboards.model.MBMessage
 * @see com.liferay.portlet.messageboards.model.MBMessageModel
 * @generated
 */
@JSON(strict = true)
public class MBMessageModelImpl extends BaseModelImpl<MBMessage>
	implements MBMessageModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a message-boards message model instance should use the {@link com.liferay.portlet.messageboards.model.MBMessage} interface instead.
	 */
	public static final String TABLE_NAME = "MBMessage";
	public static final Object[][] TABLE_COLUMNS = {
			{ "uuid_", Types.VARCHAR },
			{ "messageId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "classNameId", Types.BIGINT },
			{ "classPK", Types.BIGINT },
			{ "categoryId", Types.BIGINT },
			{ "threadId", Types.BIGINT },
			{ "rootMessageId", Types.BIGINT },
			{ "parentMessageId", Types.BIGINT },
			{ "subject", Types.VARCHAR },
			{ "body", Types.CLOB },
			{ "format", Types.VARCHAR },
			{ "anonymous", Types.BOOLEAN },
			{ "priority", Types.DOUBLE },
			{ "allowPingbacks", Types.BOOLEAN },
			{ "answer", Types.BOOLEAN },
			{ "status", Types.INTEGER },
			{ "statusByUserId", Types.BIGINT },
			{ "statusByUserName", Types.VARCHAR },
			{ "statusDate", Types.TIMESTAMP }
		};
	public static final String TABLE_SQL_CREATE = "create table MBMessage (uuid_ VARCHAR(75) null,messageId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,classNameId LONG,classPK LONG,categoryId LONG,threadId LONG,rootMessageId LONG,parentMessageId LONG,subject VARCHAR(75) null,body TEXT null,format VARCHAR(75) null,anonymous BOOLEAN,priority DOUBLE,allowPingbacks BOOLEAN,answer BOOLEAN,status INTEGER,statusByUserId LONG,statusByUserName VARCHAR(75) null,statusDate DATE null)";
	public static final String TABLE_SQL_DROP = "drop table MBMessage";
	public static final String ORDER_BY_JPQL = " ORDER BY mbMessage.createDate ASC, mbMessage.messageId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY MBMessage.createDate ASC, MBMessage.messageId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portlet.messageboards.model.MBMessage"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portlet.messageboards.model.MBMessage"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portlet.messageboards.model.MBMessage"),
			true);
	public static long ANSWER_COLUMN_BITMASK = 1L;
	public static long CATEGORYID_COLUMN_BITMASK = 2L;
	public static long CLASSNAMEID_COLUMN_BITMASK = 4L;
	public static long CLASSPK_COLUMN_BITMASK = 8L;
	public static long COMPANYID_COLUMN_BITMASK = 16L;
	public static long GROUPID_COLUMN_BITMASK = 32L;
	public static long PARENTMESSAGEID_COLUMN_BITMASK = 64L;
	public static long STATUS_COLUMN_BITMASK = 128L;
	public static long THREADID_COLUMN_BITMASK = 256L;
	public static long USERID_COLUMN_BITMASK = 512L;
	public static long UUID_COLUMN_BITMASK = 1024L;
	public static long CREATEDATE_COLUMN_BITMASK = 2048L;
	public static long MESSAGEID_COLUMN_BITMASK = 4096L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static MBMessage toModel(MBMessageSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		MBMessage model = new MBMessageImpl();

		model.setUuid(soapModel.getUuid());
		model.setMessageId(soapModel.getMessageId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setClassNameId(soapModel.getClassNameId());
		model.setClassPK(soapModel.getClassPK());
		model.setCategoryId(soapModel.getCategoryId());
		model.setThreadId(soapModel.getThreadId());
		model.setRootMessageId(soapModel.getRootMessageId());
		model.setParentMessageId(soapModel.getParentMessageId());
		model.setSubject(soapModel.getSubject());
		model.setBody(soapModel.getBody());
		model.setFormat(soapModel.getFormat());
		model.setAnonymous(soapModel.getAnonymous());
		model.setPriority(soapModel.getPriority());
		model.setAllowPingbacks(soapModel.getAllowPingbacks());
		model.setAnswer(soapModel.getAnswer());
		model.setStatus(soapModel.getStatus());
		model.setStatusByUserId(soapModel.getStatusByUserId());
		model.setStatusByUserName(soapModel.getStatusByUserName());
		model.setStatusDate(soapModel.getStatusDate());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<MBMessage> toModels(MBMessageSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<MBMessage> models = new ArrayList<MBMessage>(soapModels.length);

		for (MBMessageSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portlet.messageboards.model.MBMessage"));

	public MBMessageModelImpl() {
	}

	public long getPrimaryKey() {
		return _messageId;
	}

	public void setPrimaryKey(long primaryKey) {
		setMessageId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return _messageId;
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return MBMessage.class;
	}

	public String getModelClassName() {
		return MBMessage.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("messageId", getMessageId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("classNameId", getClassNameId());
		attributes.put("classPK", getClassPK());
		attributes.put("categoryId", getCategoryId());
		attributes.put("threadId", getThreadId());
		attributes.put("rootMessageId", getRootMessageId());
		attributes.put("parentMessageId", getParentMessageId());
		attributes.put("subject", getSubject());
		attributes.put("body", getBody());
		attributes.put("format", getFormat());
		attributes.put("anonymous", getAnonymous());
		attributes.put("priority", getPriority());
		attributes.put("allowPingbacks", getAllowPingbacks());
		attributes.put("answer", getAnswer());
		attributes.put("status", getStatus());
		attributes.put("statusByUserId", getStatusByUserId());
		attributes.put("statusByUserName", getStatusByUserName());
		attributes.put("statusDate", getStatusDate());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long messageId = (Long)attributes.get("messageId");

		if (messageId != null) {
			setMessageId(messageId);
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

		Long classNameId = (Long)attributes.get("classNameId");

		if (classNameId != null) {
			setClassNameId(classNameId);
		}

		Long classPK = (Long)attributes.get("classPK");

		if (classPK != null) {
			setClassPK(classPK);
		}

		Long categoryId = (Long)attributes.get("categoryId");

		if (categoryId != null) {
			setCategoryId(categoryId);
		}

		Long threadId = (Long)attributes.get("threadId");

		if (threadId != null) {
			setThreadId(threadId);
		}

		Long rootMessageId = (Long)attributes.get("rootMessageId");

		if (rootMessageId != null) {
			setRootMessageId(rootMessageId);
		}

		Long parentMessageId = (Long)attributes.get("parentMessageId");

		if (parentMessageId != null) {
			setParentMessageId(parentMessageId);
		}

		String subject = (String)attributes.get("subject");

		if (subject != null) {
			setSubject(subject);
		}

		String body = (String)attributes.get("body");

		if (body != null) {
			setBody(body);
		}

		String format = (String)attributes.get("format");

		if (format != null) {
			setFormat(format);
		}

		Boolean anonymous = (Boolean)attributes.get("anonymous");

		if (anonymous != null) {
			setAnonymous(anonymous);
		}

		Double priority = (Double)attributes.get("priority");

		if (priority != null) {
			setPriority(priority);
		}

		Boolean allowPingbacks = (Boolean)attributes.get("allowPingbacks");

		if (allowPingbacks != null) {
			setAllowPingbacks(allowPingbacks);
		}

		Boolean answer = (Boolean)attributes.get("answer");

		if (answer != null) {
			setAnswer(answer);
		}

		Integer status = (Integer)attributes.get("status");

		if (status != null) {
			setStatus(status);
		}

		Long statusByUserId = (Long)attributes.get("statusByUserId");

		if (statusByUserId != null) {
			setStatusByUserId(statusByUserId);
		}

		String statusByUserName = (String)attributes.get("statusByUserName");

		if (statusByUserName != null) {
			setStatusByUserName(statusByUserName);
		}

		Date statusDate = (Date)attributes.get("statusDate");

		if (statusDate != null) {
			setStatusDate(statusDate);
		}
	}

	@JSON
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

	@JSON
	public long getMessageId() {
		return _messageId;
	}

	public void setMessageId(long messageId) {
		_columnBitmask = -1L;

		_messageId = messageId;
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
		_columnBitmask = -1L;

		_createDate = createDate;
	}

	@JSON
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
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
	public long getCategoryId() {
		return _categoryId;
	}

	public void setCategoryId(long categoryId) {
		_columnBitmask |= CATEGORYID_COLUMN_BITMASK;

		if (!_setOriginalCategoryId) {
			_setOriginalCategoryId = true;

			_originalCategoryId = _categoryId;
		}

		_categoryId = categoryId;
	}

	public long getOriginalCategoryId() {
		return _originalCategoryId;
	}

	@JSON
	public long getThreadId() {
		return _threadId;
	}

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

	@JSON
	public long getRootMessageId() {
		return _rootMessageId;
	}

	public void setRootMessageId(long rootMessageId) {
		_rootMessageId = rootMessageId;
	}

	@JSON
	public long getParentMessageId() {
		return _parentMessageId;
	}

	public void setParentMessageId(long parentMessageId) {
		_columnBitmask |= PARENTMESSAGEID_COLUMN_BITMASK;

		if (!_setOriginalParentMessageId) {
			_setOriginalParentMessageId = true;

			_originalParentMessageId = _parentMessageId;
		}

		_parentMessageId = parentMessageId;
	}

	public long getOriginalParentMessageId() {
		return _originalParentMessageId;
	}

	@JSON
	public String getSubject() {
		if (_subject == null) {
			return StringPool.BLANK;
		}
		else {
			return _subject;
		}
	}

	public void setSubject(String subject) {
		_subject = subject;
	}

	@JSON
	public String getBody() {
		if (_body == null) {
			return StringPool.BLANK;
		}
		else {
			return _body;
		}
	}

	public void setBody(String body) {
		_body = body;
	}

	@JSON
	public String getFormat() {
		if (_format == null) {
			return StringPool.BLANK;
		}
		else {
			return _format;
		}
	}

	public void setFormat(String format) {
		_format = format;
	}

	@JSON
	public boolean getAnonymous() {
		return _anonymous;
	}

	public boolean isAnonymous() {
		return _anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		_anonymous = anonymous;
	}

	@JSON
	public double getPriority() {
		return _priority;
	}

	public void setPriority(double priority) {
		_priority = priority;
	}

	@JSON
	public boolean getAllowPingbacks() {
		return _allowPingbacks;
	}

	public boolean isAllowPingbacks() {
		return _allowPingbacks;
	}

	public void setAllowPingbacks(boolean allowPingbacks) {
		_allowPingbacks = allowPingbacks;
	}

	@JSON
	public boolean getAnswer() {
		return _answer;
	}

	public boolean isAnswer() {
		return _answer;
	}

	public void setAnswer(boolean answer) {
		_columnBitmask |= ANSWER_COLUMN_BITMASK;

		if (!_setOriginalAnswer) {
			_setOriginalAnswer = true;

			_originalAnswer = _answer;
		}

		_answer = answer;
	}

	public boolean getOriginalAnswer() {
		return _originalAnswer;
	}

	@JSON
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

	@JSON
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

	@JSON
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

	@JSON
	public Date getStatusDate() {
		return _statusDate;
	}

	public void setStatusDate(Date statusDate) {
		_statusDate = statusDate;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #isApproved}
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

	public boolean isDenied() {
		if (getStatus() == WorkflowConstants.STATUS_DENIED) {
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

	public boolean isInactive() {
		if (getStatus() == WorkflowConstants.STATUS_INACTIVE) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isIncomplete() {
		if (getStatus() == WorkflowConstants.STATUS_INCOMPLETE) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isInTrash() {
		if (getStatus() == WorkflowConstants.STATUS_IN_TRASH) {
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

	public boolean isScheduled() {
		if (getStatus() == WorkflowConstants.STATUS_SCHEDULED) {
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
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			MBMessage.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public MBMessage toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (MBMessage)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		MBMessageImpl mbMessageImpl = new MBMessageImpl();

		mbMessageImpl.setUuid(getUuid());
		mbMessageImpl.setMessageId(getMessageId());
		mbMessageImpl.setGroupId(getGroupId());
		mbMessageImpl.setCompanyId(getCompanyId());
		mbMessageImpl.setUserId(getUserId());
		mbMessageImpl.setUserName(getUserName());
		mbMessageImpl.setCreateDate(getCreateDate());
		mbMessageImpl.setModifiedDate(getModifiedDate());
		mbMessageImpl.setClassNameId(getClassNameId());
		mbMessageImpl.setClassPK(getClassPK());
		mbMessageImpl.setCategoryId(getCategoryId());
		mbMessageImpl.setThreadId(getThreadId());
		mbMessageImpl.setRootMessageId(getRootMessageId());
		mbMessageImpl.setParentMessageId(getParentMessageId());
		mbMessageImpl.setSubject(getSubject());
		mbMessageImpl.setBody(getBody());
		mbMessageImpl.setFormat(getFormat());
		mbMessageImpl.setAnonymous(getAnonymous());
		mbMessageImpl.setPriority(getPriority());
		mbMessageImpl.setAllowPingbacks(getAllowPingbacks());
		mbMessageImpl.setAnswer(getAnswer());
		mbMessageImpl.setStatus(getStatus());
		mbMessageImpl.setStatusByUserId(getStatusByUserId());
		mbMessageImpl.setStatusByUserName(getStatusByUserName());
		mbMessageImpl.setStatusDate(getStatusDate());

		mbMessageImpl.resetOriginalValues();

		return mbMessageImpl;
	}

	public int compareTo(MBMessage mbMessage) {
		int value = 0;

		value = DateUtil.compareTo(getCreateDate(), mbMessage.getCreateDate());

		if (value != 0) {
			return value;
		}

		if (getMessageId() < mbMessage.getMessageId()) {
			value = -1;
		}
		else if (getMessageId() > mbMessage.getMessageId()) {
			value = 1;
		}
		else {
			value = 0;
		}

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

		if (!(obj instanceof MBMessage)) {
			return false;
		}

		MBMessage mbMessage = (MBMessage)obj;

		long primaryKey = mbMessage.getPrimaryKey();

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
		MBMessageModelImpl mbMessageModelImpl = this;

		mbMessageModelImpl._originalUuid = mbMessageModelImpl._uuid;

		mbMessageModelImpl._originalGroupId = mbMessageModelImpl._groupId;

		mbMessageModelImpl._setOriginalGroupId = false;

		mbMessageModelImpl._originalCompanyId = mbMessageModelImpl._companyId;

		mbMessageModelImpl._setOriginalCompanyId = false;

		mbMessageModelImpl._originalUserId = mbMessageModelImpl._userId;

		mbMessageModelImpl._setOriginalUserId = false;

		mbMessageModelImpl._originalClassNameId = mbMessageModelImpl._classNameId;

		mbMessageModelImpl._setOriginalClassNameId = false;

		mbMessageModelImpl._originalClassPK = mbMessageModelImpl._classPK;

		mbMessageModelImpl._setOriginalClassPK = false;

		mbMessageModelImpl._originalCategoryId = mbMessageModelImpl._categoryId;

		mbMessageModelImpl._setOriginalCategoryId = false;

		mbMessageModelImpl._originalThreadId = mbMessageModelImpl._threadId;

		mbMessageModelImpl._setOriginalThreadId = false;

		mbMessageModelImpl._originalParentMessageId = mbMessageModelImpl._parentMessageId;

		mbMessageModelImpl._setOriginalParentMessageId = false;

		mbMessageModelImpl._originalAnswer = mbMessageModelImpl._answer;

		mbMessageModelImpl._setOriginalAnswer = false;

		mbMessageModelImpl._originalStatus = mbMessageModelImpl._status;

		mbMessageModelImpl._setOriginalStatus = false;

		mbMessageModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<MBMessage> toCacheModel() {
		MBMessageCacheModel mbMessageCacheModel = new MBMessageCacheModel();

		mbMessageCacheModel.uuid = getUuid();

		String uuid = mbMessageCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			mbMessageCacheModel.uuid = null;
		}

		mbMessageCacheModel.messageId = getMessageId();

		mbMessageCacheModel.groupId = getGroupId();

		mbMessageCacheModel.companyId = getCompanyId();

		mbMessageCacheModel.userId = getUserId();

		mbMessageCacheModel.userName = getUserName();

		String userName = mbMessageCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			mbMessageCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			mbMessageCacheModel.createDate = createDate.getTime();
		}
		else {
			mbMessageCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			mbMessageCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			mbMessageCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		mbMessageCacheModel.classNameId = getClassNameId();

		mbMessageCacheModel.classPK = getClassPK();

		mbMessageCacheModel.categoryId = getCategoryId();

		mbMessageCacheModel.threadId = getThreadId();

		mbMessageCacheModel.rootMessageId = getRootMessageId();

		mbMessageCacheModel.parentMessageId = getParentMessageId();

		mbMessageCacheModel.subject = getSubject();

		String subject = mbMessageCacheModel.subject;

		if ((subject != null) && (subject.length() == 0)) {
			mbMessageCacheModel.subject = null;
		}

		mbMessageCacheModel.body = getBody();

		String body = mbMessageCacheModel.body;

		if ((body != null) && (body.length() == 0)) {
			mbMessageCacheModel.body = null;
		}

		mbMessageCacheModel.format = getFormat();

		String format = mbMessageCacheModel.format;

		if ((format != null) && (format.length() == 0)) {
			mbMessageCacheModel.format = null;
		}

		mbMessageCacheModel.anonymous = getAnonymous();

		mbMessageCacheModel.priority = getPriority();

		mbMessageCacheModel.allowPingbacks = getAllowPingbacks();

		mbMessageCacheModel.answer = getAnswer();

		mbMessageCacheModel.status = getStatus();

		mbMessageCacheModel.statusByUserId = getStatusByUserId();

		mbMessageCacheModel.statusByUserName = getStatusByUserName();

		String statusByUserName = mbMessageCacheModel.statusByUserName;

		if ((statusByUserName != null) && (statusByUserName.length() == 0)) {
			mbMessageCacheModel.statusByUserName = null;
		}

		Date statusDate = getStatusDate();

		if (statusDate != null) {
			mbMessageCacheModel.statusDate = statusDate.getTime();
		}
		else {
			mbMessageCacheModel.statusDate = Long.MIN_VALUE;
		}

		return mbMessageCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(51);

		sb.append("{uuid=");
		sb.append(getUuid());
		sb.append(", messageId=");
		sb.append(getMessageId());
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
		sb.append(", classNameId=");
		sb.append(getClassNameId());
		sb.append(", classPK=");
		sb.append(getClassPK());
		sb.append(", categoryId=");
		sb.append(getCategoryId());
		sb.append(", threadId=");
		sb.append(getThreadId());
		sb.append(", rootMessageId=");
		sb.append(getRootMessageId());
		sb.append(", parentMessageId=");
		sb.append(getParentMessageId());
		sb.append(", subject=");
		sb.append(getSubject());
		sb.append(", body=");
		sb.append(getBody());
		sb.append(", format=");
		sb.append(getFormat());
		sb.append(", anonymous=");
		sb.append(getAnonymous());
		sb.append(", priority=");
		sb.append(getPriority());
		sb.append(", allowPingbacks=");
		sb.append(getAllowPingbacks());
		sb.append(", answer=");
		sb.append(getAnswer());
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
		StringBundler sb = new StringBundler(79);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portlet.messageboards.model.MBMessage");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>uuid</column-name><column-value><![CDATA[");
		sb.append(getUuid());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>messageId</column-name><column-value><![CDATA[");
		sb.append(getMessageId());
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
			"<column><column-name>classNameId</column-name><column-value><![CDATA[");
		sb.append(getClassNameId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>classPK</column-name><column-value><![CDATA[");
		sb.append(getClassPK());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>categoryId</column-name><column-value><![CDATA[");
		sb.append(getCategoryId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>threadId</column-name><column-value><![CDATA[");
		sb.append(getThreadId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>rootMessageId</column-name><column-value><![CDATA[");
		sb.append(getRootMessageId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>parentMessageId</column-name><column-value><![CDATA[");
		sb.append(getParentMessageId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>subject</column-name><column-value><![CDATA[");
		sb.append(getSubject());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>body</column-name><column-value><![CDATA[");
		sb.append(getBody());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>format</column-name><column-value><![CDATA[");
		sb.append(getFormat());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>anonymous</column-name><column-value><![CDATA[");
		sb.append(getAnonymous());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>priority</column-name><column-value><![CDATA[");
		sb.append(getPriority());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>allowPingbacks</column-name><column-value><![CDATA[");
		sb.append(getAllowPingbacks());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>answer</column-name><column-value><![CDATA[");
		sb.append(getAnswer());
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

	private static ClassLoader _classLoader = MBMessage.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			MBMessage.class
		};
	private String _uuid;
	private String _originalUuid;
	private long _messageId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private String _userUuid;
	private long _originalUserId;
	private boolean _setOriginalUserId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private long _classNameId;
	private long _originalClassNameId;
	private boolean _setOriginalClassNameId;
	private long _classPK;
	private long _originalClassPK;
	private boolean _setOriginalClassPK;
	private long _categoryId;
	private long _originalCategoryId;
	private boolean _setOriginalCategoryId;
	private long _threadId;
	private long _originalThreadId;
	private boolean _setOriginalThreadId;
	private long _rootMessageId;
	private long _parentMessageId;
	private long _originalParentMessageId;
	private boolean _setOriginalParentMessageId;
	private String _subject;
	private String _body;
	private String _format;
	private boolean _anonymous;
	private double _priority;
	private boolean _allowPingbacks;
	private boolean _answer;
	private boolean _originalAnswer;
	private boolean _setOriginalAnswer;
	private int _status;
	private int _originalStatus;
	private boolean _setOriginalStatus;
	private long _statusByUserId;
	private String _statusByUserUuid;
	private String _statusByUserName;
	private Date _statusDate;
	private long _columnBitmask;
	private MBMessage _escapedModel;
}