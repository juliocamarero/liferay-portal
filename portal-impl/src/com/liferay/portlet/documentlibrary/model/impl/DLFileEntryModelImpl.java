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

package com.liferay.portlet.documentlibrary.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.portal.kernel.trash.TrashHandlerRegistryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ContainerModel;
import com.liferay.portal.model.TrashedModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFileEntryModel;
import com.liferay.portlet.documentlibrary.model.DLFileEntrySoap;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;
import com.liferay.portlet.trash.model.TrashEntry;
import com.liferay.portlet.trash.service.TrashEntryLocalServiceUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the DLFileEntry service. Represents a row in the &quot;DLFileEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portlet.documentlibrary.model.DLFileEntryModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link DLFileEntryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DLFileEntryImpl
 * @see com.liferay.portlet.documentlibrary.model.DLFileEntry
 * @see com.liferay.portlet.documentlibrary.model.DLFileEntryModel
 * @generated
 */
@JSON(strict = true)
public class DLFileEntryModelImpl extends BaseModelImpl<DLFileEntry>
	implements DLFileEntryModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a document library file entry model instance should use the {@link com.liferay.portlet.documentlibrary.model.DLFileEntry} interface instead.
	 */
	public static final String TABLE_NAME = "DLFileEntry";
	public static final Object[][] TABLE_COLUMNS = {
			{ "uuid_", Types.VARCHAR },
			{ "fileEntryId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "classNameId", Types.BIGINT },
			{ "classPK", Types.BIGINT },
			{ "repositoryId", Types.BIGINT },
			{ "folderId", Types.BIGINT },
			{ "name", Types.VARCHAR },
			{ "extension", Types.VARCHAR },
			{ "mimeType", Types.VARCHAR },
			{ "title", Types.VARCHAR },
			{ "description", Types.VARCHAR },
			{ "extraSettings", Types.CLOB },
			{ "fileEntryTypeId", Types.BIGINT },
			{ "version", Types.VARCHAR },
			{ "size_", Types.BIGINT },
			{ "readCount", Types.INTEGER },
			{ "smallImageId", Types.BIGINT },
			{ "largeImageId", Types.BIGINT },
			{ "custom1ImageId", Types.BIGINT },
			{ "custom2ImageId", Types.BIGINT },
			{ "manualCheckInRequired", Types.BOOLEAN }
		};
	public static final String TABLE_SQL_CREATE = "create table DLFileEntry (uuid_ VARCHAR(75) null,fileEntryId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,classNameId LONG,classPK LONG,repositoryId LONG,folderId LONG,name VARCHAR(255) null,extension VARCHAR(75) null,mimeType VARCHAR(75) null,title VARCHAR(255) null,description STRING null,extraSettings TEXT null,fileEntryTypeId LONG,version VARCHAR(75) null,size_ LONG,readCount INTEGER,smallImageId LONG,largeImageId LONG,custom1ImageId LONG,custom2ImageId LONG,manualCheckInRequired BOOLEAN)";
	public static final String TABLE_SQL_DROP = "drop table DLFileEntry";
	public static final String ORDER_BY_JPQL = " ORDER BY dlFileEntry.folderId ASC, dlFileEntry.name ASC";
	public static final String ORDER_BY_SQL = " ORDER BY DLFileEntry.folderId ASC, DLFileEntry.name ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portlet.documentlibrary.model.DLFileEntry"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portlet.documentlibrary.model.DLFileEntry"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portlet.documentlibrary.model.DLFileEntry"),
			true);
	public static long COMPANYID_COLUMN_BITMASK = 1L;
	public static long FILEENTRYTYPEID_COLUMN_BITMASK = 2L;
	public static long FOLDERID_COLUMN_BITMASK = 4L;
	public static long GROUPID_COLUMN_BITMASK = 8L;
	public static long MIMETYPE_COLUMN_BITMASK = 16L;
	public static long NAME_COLUMN_BITMASK = 32L;
	public static long TITLE_COLUMN_BITMASK = 64L;
	public static long USERID_COLUMN_BITMASK = 128L;
	public static long UUID_COLUMN_BITMASK = 256L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static DLFileEntry toModel(DLFileEntrySoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		DLFileEntry model = new DLFileEntryImpl();

		model.setUuid(soapModel.getUuid());
		model.setFileEntryId(soapModel.getFileEntryId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setClassNameId(soapModel.getClassNameId());
		model.setClassPK(soapModel.getClassPK());
		model.setRepositoryId(soapModel.getRepositoryId());
		model.setFolderId(soapModel.getFolderId());
		model.setName(soapModel.getName());
		model.setExtension(soapModel.getExtension());
		model.setMimeType(soapModel.getMimeType());
		model.setTitle(soapModel.getTitle());
		model.setDescription(soapModel.getDescription());
		model.setExtraSettings(soapModel.getExtraSettings());
		model.setFileEntryTypeId(soapModel.getFileEntryTypeId());
		model.setVersion(soapModel.getVersion());
		model.setSize(soapModel.getSize());
		model.setReadCount(soapModel.getReadCount());
		model.setSmallImageId(soapModel.getSmallImageId());
		model.setLargeImageId(soapModel.getLargeImageId());
		model.setCustom1ImageId(soapModel.getCustom1ImageId());
		model.setCustom2ImageId(soapModel.getCustom2ImageId());
		model.setManualCheckInRequired(soapModel.getManualCheckInRequired());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<DLFileEntry> toModels(DLFileEntrySoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<DLFileEntry> models = new ArrayList<DLFileEntry>(soapModels.length);

		for (DLFileEntrySoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portlet.documentlibrary.model.DLFileEntry"));

	public DLFileEntryModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _fileEntryId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setFileEntryId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _fileEntryId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return DLFileEntry.class;
	}

	@Override
	public String getModelClassName() {
		return DLFileEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("fileEntryId", getFileEntryId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("classNameId", getClassNameId());
		attributes.put("classPK", getClassPK());
		attributes.put("repositoryId", getRepositoryId());
		attributes.put("folderId", getFolderId());
		attributes.put("name", getName());
		attributes.put("extension", getExtension());
		attributes.put("mimeType", getMimeType());
		attributes.put("title", getTitle());
		attributes.put("description", getDescription());
		attributes.put("extraSettings", getExtraSettings());
		attributes.put("fileEntryTypeId", getFileEntryTypeId());
		attributes.put("version", getVersion());
		attributes.put("size", getSize());
		attributes.put("readCount", getReadCount());
		attributes.put("smallImageId", getSmallImageId());
		attributes.put("largeImageId", getLargeImageId());
		attributes.put("custom1ImageId", getCustom1ImageId());
		attributes.put("custom2ImageId", getCustom2ImageId());
		attributes.put("manualCheckInRequired", getManualCheckInRequired());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long fileEntryId = (Long)attributes.get("fileEntryId");

		if (fileEntryId != null) {
			setFileEntryId(fileEntryId);
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

		Long repositoryId = (Long)attributes.get("repositoryId");

		if (repositoryId != null) {
			setRepositoryId(repositoryId);
		}

		Long folderId = (Long)attributes.get("folderId");

		if (folderId != null) {
			setFolderId(folderId);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String extension = (String)attributes.get("extension");

		if (extension != null) {
			setExtension(extension);
		}

		String mimeType = (String)attributes.get("mimeType");

		if (mimeType != null) {
			setMimeType(mimeType);
		}

		String title = (String)attributes.get("title");

		if (title != null) {
			setTitle(title);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		String extraSettings = (String)attributes.get("extraSettings");

		if (extraSettings != null) {
			setExtraSettings(extraSettings);
		}

		Long fileEntryTypeId = (Long)attributes.get("fileEntryTypeId");

		if (fileEntryTypeId != null) {
			setFileEntryTypeId(fileEntryTypeId);
		}

		String version = (String)attributes.get("version");

		if (version != null) {
			setVersion(version);
		}

		Long size = (Long)attributes.get("size");

		if (size != null) {
			setSize(size);
		}

		Integer readCount = (Integer)attributes.get("readCount");

		if (readCount != null) {
			setReadCount(readCount);
		}

		Long smallImageId = (Long)attributes.get("smallImageId");

		if (smallImageId != null) {
			setSmallImageId(smallImageId);
		}

		Long largeImageId = (Long)attributes.get("largeImageId");

		if (largeImageId != null) {
			setLargeImageId(largeImageId);
		}

		Long custom1ImageId = (Long)attributes.get("custom1ImageId");

		if (custom1ImageId != null) {
			setCustom1ImageId(custom1ImageId);
		}

		Long custom2ImageId = (Long)attributes.get("custom2ImageId");

		if (custom2ImageId != null) {
			setCustom2ImageId(custom2ImageId);
		}

		Boolean manualCheckInRequired = (Boolean)attributes.get(
				"manualCheckInRequired");

		if (manualCheckInRequired != null) {
			setManualCheckInRequired(manualCheckInRequired);
		}
	}

	@JSON
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

	@JSON
	@Override
	public long getFileEntryId() {
		return _fileEntryId;
	}

	@Override
	public void setFileEntryId(long fileEntryId) {
		_fileEntryId = fileEntryId;
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
	public String getUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
	}

	@Override
	public void setUserUuid(String userUuid) {
		_userUuid = userUuid;
	}

	public long getOriginalUserId() {
		return _originalUserId;
	}

	@JSON
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

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@JSON
	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
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
		_classNameId = classNameId;
	}

	@JSON
	@Override
	public long getClassPK() {
		return _classPK;
	}

	@Override
	public void setClassPK(long classPK) {
		_classPK = classPK;
	}

	@JSON
	@Override
	public long getRepositoryId() {
		return _repositoryId;
	}

	@Override
	public void setRepositoryId(long repositoryId) {
		_repositoryId = repositoryId;
	}

	@JSON
	@Override
	public long getFolderId() {
		return _folderId;
	}

	@Override
	public void setFolderId(long folderId) {
		_columnBitmask = -1L;

		if (!_setOriginalFolderId) {
			_setOriginalFolderId = true;

			_originalFolderId = _folderId;
		}

		_folderId = folderId;
	}

	public long getOriginalFolderId() {
		return _originalFolderId;
	}

	@JSON
	@Override
	public String getName() {
		if (_name == null) {
			return StringPool.BLANK;
		}
		else {
			return _name;
		}
	}

	@Override
	public void setName(String name) {
		_columnBitmask = -1L;

		if (_originalName == null) {
			_originalName = _name;
		}

		_name = name;
	}

	public String getOriginalName() {
		return GetterUtil.getString(_originalName);
	}

	@JSON
	@Override
	public String getExtension() {
		if (_extension == null) {
			return StringPool.BLANK;
		}
		else {
			return _extension;
		}
	}

	@Override
	public void setExtension(String extension) {
		_extension = extension;
	}

	@JSON
	@Override
	public String getMimeType() {
		if (_mimeType == null) {
			return StringPool.BLANK;
		}
		else {
			return _mimeType;
		}
	}

	@Override
	public void setMimeType(String mimeType) {
		_columnBitmask |= MIMETYPE_COLUMN_BITMASK;

		if (_originalMimeType == null) {
			_originalMimeType = _mimeType;
		}

		_mimeType = mimeType;
	}

	public String getOriginalMimeType() {
		return GetterUtil.getString(_originalMimeType);
	}

	@JSON
	@Override
	public String getTitle() {
		if (_title == null) {
			return StringPool.BLANK;
		}
		else {
			return _title;
		}
	}

	@Override
	public void setTitle(String title) {
		_columnBitmask |= TITLE_COLUMN_BITMASK;

		if (_originalTitle == null) {
			_originalTitle = _title;
		}

		_title = title;
	}

	public String getOriginalTitle() {
		return GetterUtil.getString(_originalTitle);
	}

	@JSON
	@Override
	public String getDescription() {
		if (_description == null) {
			return StringPool.BLANK;
		}
		else {
			return _description;
		}
	}

	@Override
	public void setDescription(String description) {
		_description = description;
	}

	@JSON
	@Override
	public String getExtraSettings() {
		if (_extraSettings == null) {
			return StringPool.BLANK;
		}
		else {
			return _extraSettings;
		}
	}

	@Override
	public void setExtraSettings(String extraSettings) {
		_extraSettings = extraSettings;
	}

	@JSON
	@Override
	public long getFileEntryTypeId() {
		return _fileEntryTypeId;
	}

	@Override
	public void setFileEntryTypeId(long fileEntryTypeId) {
		_columnBitmask |= FILEENTRYTYPEID_COLUMN_BITMASK;

		if (!_setOriginalFileEntryTypeId) {
			_setOriginalFileEntryTypeId = true;

			_originalFileEntryTypeId = _fileEntryTypeId;
		}

		_fileEntryTypeId = fileEntryTypeId;
	}

	public long getOriginalFileEntryTypeId() {
		return _originalFileEntryTypeId;
	}

	@JSON
	@Override
	public String getVersion() {
		if (_version == null) {
			return StringPool.BLANK;
		}
		else {
			return _version;
		}
	}

	@Override
	public void setVersion(String version) {
		_version = version;
	}

	@JSON
	@Override
	public long getSize() {
		return _size;
	}

	@Override
	public void setSize(long size) {
		_size = size;
	}

	@JSON
	@Override
	public int getReadCount() {
		return _readCount;
	}

	@Override
	public void setReadCount(int readCount) {
		_readCount = readCount;
	}

	@JSON
	@Override
	public long getSmallImageId() {
		return _smallImageId;
	}

	@Override
	public void setSmallImageId(long smallImageId) {
		_smallImageId = smallImageId;
	}

	@JSON
	@Override
	public long getLargeImageId() {
		return _largeImageId;
	}

	@Override
	public void setLargeImageId(long largeImageId) {
		_largeImageId = largeImageId;
	}

	@JSON
	@Override
	public long getCustom1ImageId() {
		return _custom1ImageId;
	}

	@Override
	public void setCustom1ImageId(long custom1ImageId) {
		_custom1ImageId = custom1ImageId;
	}

	@JSON
	@Override
	public long getCustom2ImageId() {
		return _custom2ImageId;
	}

	@Override
	public void setCustom2ImageId(long custom2ImageId) {
		_custom2ImageId = custom2ImageId;
	}

	@JSON
	@Override
	public boolean getManualCheckInRequired() {
		return _manualCheckInRequired;
	}

	@Override
	public boolean isManualCheckInRequired() {
		return _manualCheckInRequired;
	}

	@Override
	public void setManualCheckInRequired(boolean manualCheckInRequired) {
		_manualCheckInRequired = manualCheckInRequired;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(PortalUtil.getClassNameId(
				DLFileEntry.class.getName()), getClassNameId());
	}

	@Override
	public int getStatus() {
		return 0;
	}

	@Override
	public TrashedModel getTrashContainer() {
		TrashHandler trashHandler = getTrashHandler();

		if ((trashHandler == null) ||
				Validator.isNull(trashHandler.getContainerModelClassName())) {
			return null;
		}

		try {
			ContainerModel containerModel = trashHandler.getParentContainerModel(getResourcePrimKey());

			if ((containerModel == null) ||
					!(containerModel instanceof TrashedModel)) {
				return null;
			}

			TrashedModel trashModel = (TrashedModel)containerModel;

			if (trashModel.isInTrash()) {
				return trashModel;
			}

			return trashModel.getTrashContainer();
		}
		catch (Exception e) {
		}

		return null;
	}

	@Override
	public TrashEntry getTrashEntry() throws PortalException, SystemException {
		if (!isInTrash() && !isInTrashContainer()) {
			return null;
		}

		TrashEntry trashEntry = TrashEntryLocalServiceUtil.fetchEntry(getModelClassName(),
				getPrimaryKey());

		if (trashEntry != null) {
			return trashEntry;
		}

		TrashedModel trashedModel = getTrashContainer();

		if (trashedModel != null) {
			return trashedModel.getTrashEntry();
		}

		return null;
	}

	@Override
	public TrashHandler getTrashHandler() {
		return TrashHandlerRegistryUtil.getTrashHandler(getModelClassName());
	}

	@Override
	public boolean isInTrash() {
		if (getStatus() == WorkflowConstants.STATUS_IN_TRASH) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isInTrashContainer() {
		if (getTrashContainer() != null) {
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
			DLFileEntry.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public DLFileEntry toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (DLFileEntry)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		DLFileEntryImpl dlFileEntryImpl = new DLFileEntryImpl();

		dlFileEntryImpl.setUuid(getUuid());
		dlFileEntryImpl.setFileEntryId(getFileEntryId());
		dlFileEntryImpl.setGroupId(getGroupId());
		dlFileEntryImpl.setCompanyId(getCompanyId());
		dlFileEntryImpl.setUserId(getUserId());
		dlFileEntryImpl.setUserName(getUserName());
		dlFileEntryImpl.setCreateDate(getCreateDate());
		dlFileEntryImpl.setModifiedDate(getModifiedDate());
		dlFileEntryImpl.setClassNameId(getClassNameId());
		dlFileEntryImpl.setClassPK(getClassPK());
		dlFileEntryImpl.setRepositoryId(getRepositoryId());
		dlFileEntryImpl.setFolderId(getFolderId());
		dlFileEntryImpl.setName(getName());
		dlFileEntryImpl.setExtension(getExtension());
		dlFileEntryImpl.setMimeType(getMimeType());
		dlFileEntryImpl.setTitle(getTitle());
		dlFileEntryImpl.setDescription(getDescription());
		dlFileEntryImpl.setExtraSettings(getExtraSettings());
		dlFileEntryImpl.setFileEntryTypeId(getFileEntryTypeId());
		dlFileEntryImpl.setVersion(getVersion());
		dlFileEntryImpl.setSize(getSize());
		dlFileEntryImpl.setReadCount(getReadCount());
		dlFileEntryImpl.setSmallImageId(getSmallImageId());
		dlFileEntryImpl.setLargeImageId(getLargeImageId());
		dlFileEntryImpl.setCustom1ImageId(getCustom1ImageId());
		dlFileEntryImpl.setCustom2ImageId(getCustom2ImageId());
		dlFileEntryImpl.setManualCheckInRequired(getManualCheckInRequired());

		dlFileEntryImpl.resetOriginalValues();

		return dlFileEntryImpl;
	}

	@Override
	public int compareTo(DLFileEntry dlFileEntry) {
		int value = 0;

		if (getFolderId() < dlFileEntry.getFolderId()) {
			value = -1;
		}
		else if (getFolderId() > dlFileEntry.getFolderId()) {
			value = 1;
		}
		else {
			value = 0;
		}

		if (value != 0) {
			return value;
		}

		value = getName().compareTo(dlFileEntry.getName());

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

		if (!(obj instanceof DLFileEntry)) {
			return false;
		}

		DLFileEntry dlFileEntry = (DLFileEntry)obj;

		long primaryKey = dlFileEntry.getPrimaryKey();

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
		DLFileEntryModelImpl dlFileEntryModelImpl = this;

		dlFileEntryModelImpl._originalUuid = dlFileEntryModelImpl._uuid;

		dlFileEntryModelImpl._originalGroupId = dlFileEntryModelImpl._groupId;

		dlFileEntryModelImpl._setOriginalGroupId = false;

		dlFileEntryModelImpl._originalCompanyId = dlFileEntryModelImpl._companyId;

		dlFileEntryModelImpl._setOriginalCompanyId = false;

		dlFileEntryModelImpl._originalUserId = dlFileEntryModelImpl._userId;

		dlFileEntryModelImpl._setOriginalUserId = false;

		dlFileEntryModelImpl._originalFolderId = dlFileEntryModelImpl._folderId;

		dlFileEntryModelImpl._setOriginalFolderId = false;

		dlFileEntryModelImpl._originalName = dlFileEntryModelImpl._name;

		dlFileEntryModelImpl._originalMimeType = dlFileEntryModelImpl._mimeType;

		dlFileEntryModelImpl._originalTitle = dlFileEntryModelImpl._title;

		dlFileEntryModelImpl._originalFileEntryTypeId = dlFileEntryModelImpl._fileEntryTypeId;

		dlFileEntryModelImpl._setOriginalFileEntryTypeId = false;

		dlFileEntryModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<DLFileEntry> toCacheModel() {
		DLFileEntryCacheModel dlFileEntryCacheModel = new DLFileEntryCacheModel();

		dlFileEntryCacheModel.uuid = getUuid();

		String uuid = dlFileEntryCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			dlFileEntryCacheModel.uuid = null;
		}

		dlFileEntryCacheModel.fileEntryId = getFileEntryId();

		dlFileEntryCacheModel.groupId = getGroupId();

		dlFileEntryCacheModel.companyId = getCompanyId();

		dlFileEntryCacheModel.userId = getUserId();

		dlFileEntryCacheModel.userName = getUserName();

		String userName = dlFileEntryCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			dlFileEntryCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			dlFileEntryCacheModel.createDate = createDate.getTime();
		}
		else {
			dlFileEntryCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			dlFileEntryCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			dlFileEntryCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		dlFileEntryCacheModel.classNameId = getClassNameId();

		dlFileEntryCacheModel.classPK = getClassPK();

		dlFileEntryCacheModel.repositoryId = getRepositoryId();

		dlFileEntryCacheModel.folderId = getFolderId();

		dlFileEntryCacheModel.name = getName();

		String name = dlFileEntryCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			dlFileEntryCacheModel.name = null;
		}

		dlFileEntryCacheModel.extension = getExtension();

		String extension = dlFileEntryCacheModel.extension;

		if ((extension != null) && (extension.length() == 0)) {
			dlFileEntryCacheModel.extension = null;
		}

		dlFileEntryCacheModel.mimeType = getMimeType();

		String mimeType = dlFileEntryCacheModel.mimeType;

		if ((mimeType != null) && (mimeType.length() == 0)) {
			dlFileEntryCacheModel.mimeType = null;
		}

		dlFileEntryCacheModel.title = getTitle();

		String title = dlFileEntryCacheModel.title;

		if ((title != null) && (title.length() == 0)) {
			dlFileEntryCacheModel.title = null;
		}

		dlFileEntryCacheModel.description = getDescription();

		String description = dlFileEntryCacheModel.description;

		if ((description != null) && (description.length() == 0)) {
			dlFileEntryCacheModel.description = null;
		}

		dlFileEntryCacheModel.extraSettings = getExtraSettings();

		String extraSettings = dlFileEntryCacheModel.extraSettings;

		if ((extraSettings != null) && (extraSettings.length() == 0)) {
			dlFileEntryCacheModel.extraSettings = null;
		}

		dlFileEntryCacheModel.fileEntryTypeId = getFileEntryTypeId();

		dlFileEntryCacheModel.version = getVersion();

		String version = dlFileEntryCacheModel.version;

		if ((version != null) && (version.length() == 0)) {
			dlFileEntryCacheModel.version = null;
		}

		dlFileEntryCacheModel.size = getSize();

		dlFileEntryCacheModel.readCount = getReadCount();

		dlFileEntryCacheModel.smallImageId = getSmallImageId();

		dlFileEntryCacheModel.largeImageId = getLargeImageId();

		dlFileEntryCacheModel.custom1ImageId = getCustom1ImageId();

		dlFileEntryCacheModel.custom2ImageId = getCustom2ImageId();

		dlFileEntryCacheModel.manualCheckInRequired = getManualCheckInRequired();

		return dlFileEntryCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(55);

		sb.append("{uuid=");
		sb.append(getUuid());
		sb.append(", fileEntryId=");
		sb.append(getFileEntryId());
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
		sb.append(", repositoryId=");
		sb.append(getRepositoryId());
		sb.append(", folderId=");
		sb.append(getFolderId());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", extension=");
		sb.append(getExtension());
		sb.append(", mimeType=");
		sb.append(getMimeType());
		sb.append(", title=");
		sb.append(getTitle());
		sb.append(", description=");
		sb.append(getDescription());
		sb.append(", extraSettings=");
		sb.append(getExtraSettings());
		sb.append(", fileEntryTypeId=");
		sb.append(getFileEntryTypeId());
		sb.append(", version=");
		sb.append(getVersion());
		sb.append(", size=");
		sb.append(getSize());
		sb.append(", readCount=");
		sb.append(getReadCount());
		sb.append(", smallImageId=");
		sb.append(getSmallImageId());
		sb.append(", largeImageId=");
		sb.append(getLargeImageId());
		sb.append(", custom1ImageId=");
		sb.append(getCustom1ImageId());
		sb.append(", custom2ImageId=");
		sb.append(getCustom2ImageId());
		sb.append(", manualCheckInRequired=");
		sb.append(getManualCheckInRequired());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(85);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portlet.documentlibrary.model.DLFileEntry");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>uuid</column-name><column-value><![CDATA[");
		sb.append(getUuid());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>fileEntryId</column-name><column-value><![CDATA[");
		sb.append(getFileEntryId());
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
			"<column><column-name>repositoryId</column-name><column-value><![CDATA[");
		sb.append(getRepositoryId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>folderId</column-name><column-value><![CDATA[");
		sb.append(getFolderId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>name</column-name><column-value><![CDATA[");
		sb.append(getName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>extension</column-name><column-value><![CDATA[");
		sb.append(getExtension());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>mimeType</column-name><column-value><![CDATA[");
		sb.append(getMimeType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>title</column-name><column-value><![CDATA[");
		sb.append(getTitle());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>description</column-name><column-value><![CDATA[");
		sb.append(getDescription());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>extraSettings</column-name><column-value><![CDATA[");
		sb.append(getExtraSettings());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>fileEntryTypeId</column-name><column-value><![CDATA[");
		sb.append(getFileEntryTypeId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>version</column-name><column-value><![CDATA[");
		sb.append(getVersion());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>size</column-name><column-value><![CDATA[");
		sb.append(getSize());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>readCount</column-name><column-value><![CDATA[");
		sb.append(getReadCount());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>smallImageId</column-name><column-value><![CDATA[");
		sb.append(getSmallImageId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>largeImageId</column-name><column-value><![CDATA[");
		sb.append(getLargeImageId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>custom1ImageId</column-name><column-value><![CDATA[");
		sb.append(getCustom1ImageId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>custom2ImageId</column-name><column-value><![CDATA[");
		sb.append(getCustom2ImageId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>manualCheckInRequired</column-name><column-value><![CDATA[");
		sb.append(getManualCheckInRequired());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = DLFileEntry.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			DLFileEntry.class
		};
	private String _uuid;
	private String _originalUuid;
	private long _fileEntryId;
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
	private long _classPK;
	private long _repositoryId;
	private long _folderId;
	private long _originalFolderId;
	private boolean _setOriginalFolderId;
	private String _name;
	private String _originalName;
	private String _extension;
	private String _mimeType;
	private String _originalMimeType;
	private String _title;
	private String _originalTitle;
	private String _description;
	private String _extraSettings;
	private long _fileEntryTypeId;
	private long _originalFileEntryTypeId;
	private boolean _setOriginalFileEntryTypeId;
	private String _version;
	private long _size;
	private int _readCount;
	private long _smallImageId;
	private long _largeImageId;
	private long _custom1ImageId;
	private long _custom2ImageId;
	private boolean _manualCheckInRequired;
	private long _columnBitmask;
	private DLFileEntry _escapedModel;
}