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

package com.liferay.portlet.documentlibrary.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the DLSync service. Represents a row in the &quot;DLSync&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.documentlibrary.model.impl.DLSyncModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.documentlibrary.model.impl.DLSyncImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DLSync
 * @see com.liferay.portlet.documentlibrary.model.impl.DLSyncImpl
 * @see com.liferay.portlet.documentlibrary.model.impl.DLSyncModelImpl
 * @generated
 */
public interface DLSyncModel extends BaseModel<DLSync> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a d l sync model instance should use the {@link DLSync} interface instead.
	 */

	/**
	 * Returns the primary key of this d l sync.
	 *
	 * @return the primary key of this d l sync
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this d l sync.
	 *
	 * @param primaryKey the primary key of this d l sync
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the sync ID of this d l sync.
	 *
	 * @return the sync ID of this d l sync
	 */
	public long getSyncId();

	/**
	 * Sets the sync ID of this d l sync.
	 *
	 * @param syncId the sync ID of this d l sync
	 */
	public void setSyncId(long syncId);

	/**
	 * Returns the company ID of this d l sync.
	 *
	 * @return the company ID of this d l sync
	 */
	public long getCompanyId();

	/**
	 * Sets the company ID of this d l sync.
	 *
	 * @param companyId the company ID of this d l sync
	 */
	public void setCompanyId(long companyId);

	/**
	 * Returns the create date of this d l sync.
	 *
	 * @return the create date of this d l sync
	 */
	public Date getCreateDate();

	/**
	 * Sets the create date of this d l sync.
	 *
	 * @param createDate the create date of this d l sync
	 */
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this d l sync.
	 *
	 * @return the modified date of this d l sync
	 */
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this d l sync.
	 *
	 * @param modifiedDate the modified date of this d l sync
	 */
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the file ID of this d l sync.
	 *
	 * @return the file ID of this d l sync
	 */
	public long getFileId();

	/**
	 * Sets the file ID of this d l sync.
	 *
	 * @param fileId the file ID of this d l sync
	 */
	public void setFileId(long fileId);

	/**
	 * Returns the repository ID of this d l sync.
	 *
	 * @return the repository ID of this d l sync
	 */
	public long getRepositoryId();

	/**
	 * Sets the repository ID of this d l sync.
	 *
	 * @param repositoryId the repository ID of this d l sync
	 */
	public void setRepositoryId(long repositoryId);

	/**
	 * Returns the parent folder ID of this d l sync.
	 *
	 * @return the parent folder ID of this d l sync
	 */
	public long getParentFolderId();

	/**
	 * Sets the parent folder ID of this d l sync.
	 *
	 * @param parentFolderId the parent folder ID of this d l sync
	 */
	public void setParentFolderId(long parentFolderId);

	/**
	 * Returns the event of this d l sync.
	 *
	 * @return the event of this d l sync
	 */
	@AutoEscape
	public String getEvent();

	/**
	 * Sets the event of this d l sync.
	 *
	 * @param event the event of this d l sync
	 */
	public void setEvent(String event);

	/**
	 * Returns the type of this d l sync.
	 *
	 * @return the type of this d l sync
	 */
	@AutoEscape
	public String getType();

	/**
	 * Sets the type of this d l sync.
	 *
	 * @param type the type of this d l sync
	 */
	public void setType(String type);

	public boolean isNew();

	public void setNew(boolean n);

	public boolean isCachedModel();

	public void setCachedModel(boolean cachedModel);

	public boolean isEscapedModel();

	public Serializable getPrimaryKeyObj();

	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	public ExpandoBridge getExpandoBridge();

	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	public Object clone();

	public int compareTo(DLSync dlSync);

	public int hashCode();

	public CacheModel<DLSync> toCacheModel();

	public DLSync toEscapedModel();

	public String toString();

	public String toXmlString();
}