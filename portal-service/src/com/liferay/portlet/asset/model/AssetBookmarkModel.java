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

package com.liferay.portlet.asset.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.AttachedModel;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

/**
 * The base model interface for the AssetBookmark service. Represents a row in the &quot;AssetBookmark&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.asset.model.impl.AssetBookmarkModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.asset.model.impl.AssetBookmarkImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetBookmark
 * @see com.liferay.portlet.asset.model.impl.AssetBookmarkImpl
 * @see com.liferay.portlet.asset.model.impl.AssetBookmarkModelImpl
 * @generated
 */
public interface AssetBookmarkModel extends AttachedModel,
	BaseModel<AssetBookmark> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a asset bookmark model instance should use the {@link AssetBookmark} interface instead.
	 */

	/**
	 * Returns the primary key of this asset bookmark.
	 *
	 * @return the primary key of this asset bookmark
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this asset bookmark.
	 *
	 * @param primaryKey the primary key of this asset bookmark
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this asset bookmark.
	 *
	 * @return the uuid of this asset bookmark
	 */
	@AutoEscape
	public String getUuid();

	/**
	 * Sets the uuid of this asset bookmark.
	 *
	 * @param uuid the uuid of this asset bookmark
	 */
	public void setUuid(String uuid);

	/**
	 * Returns the bookmark ID of this asset bookmark.
	 *
	 * @return the bookmark ID of this asset bookmark
	 */
	public long getBookmarkId();

	/**
	 * Sets the bookmark ID of this asset bookmark.
	 *
	 * @param bookmarkId the bookmark ID of this asset bookmark
	 */
	public void setBookmarkId(long bookmarkId);

	/**
	 * Returns the user ID of this asset bookmark.
	 *
	 * @return the user ID of this asset bookmark
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this asset bookmark.
	 *
	 * @param userId the user ID of this asset bookmark
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this asset bookmark.
	 *
	 * @return the user uuid of this asset bookmark
	 * @throws SystemException if a system exception occurred
	 */
	public String getUserUuid() throws SystemException;

	/**
	 * Sets the user uuid of this asset bookmark.
	 *
	 * @param userUuid the user uuid of this asset bookmark
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the fully qualified class name of this asset bookmark.
	 *
	 * @return the fully qualified class name of this asset bookmark
	 */
	public String getClassName();

	public void setClassName(String className);

	/**
	 * Returns the class name ID of this asset bookmark.
	 *
	 * @return the class name ID of this asset bookmark
	 */
	public long getClassNameId();

	/**
	 * Sets the class name ID of this asset bookmark.
	 *
	 * @param classNameId the class name ID of this asset bookmark
	 */
	public void setClassNameId(long classNameId);

	/**
	 * Returns the class p k of this asset bookmark.
	 *
	 * @return the class p k of this asset bookmark
	 */
	public long getClassPK();

	/**
	 * Sets the class p k of this asset bookmark.
	 *
	 * @param classPK the class p k of this asset bookmark
	 */
	public void setClassPK(long classPK);

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

	public int compareTo(AssetBookmark assetBookmark);

	public int hashCode();

	public CacheModel<AssetBookmark> toCacheModel();

	public AssetBookmark toEscapedModel();

	public AssetBookmark toUnescapedModel();

	public String toString();

	public String toXmlString();
}