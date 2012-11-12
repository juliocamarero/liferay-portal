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

import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

/**
 * The base model interface for the AssetTagStats service. Represents a row in the &quot;AssetTagStats&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.asset.model.impl.AssetTagStatsModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.asset.model.impl.AssetTagStatsImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetTagStats
 * @see com.liferay.portlet.asset.model.impl.AssetTagStatsImpl
 * @see com.liferay.portlet.asset.model.impl.AssetTagStatsModelImpl
 * @generated
 */
public interface AssetTagStatsModel extends BaseModel<AssetTagStats> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a asset tag stats model instance should use the {@link AssetTagStats} interface instead.
	 */

	/**
	 * Returns the primary key of this asset tag stats.
	 *
	 * @return the primary key of this asset tag stats
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this asset tag stats.
	 *
	 * @param primaryKey the primary key of this asset tag stats
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the tag stats ID of this asset tag stats.
	 *
	 * @return the tag stats ID of this asset tag stats
	 */
	public long getTagStatsId();

	/**
	 * Sets the tag stats ID of this asset tag stats.
	 *
	 * @param tagStatsId the tag stats ID of this asset tag stats
	 */
	public void setTagStatsId(long tagStatsId);

	/**
	 * Returns the tag ID of this asset tag stats.
	 *
	 * @return the tag ID of this asset tag stats
	 */
	public long getTagId();

	/**
	 * Sets the tag ID of this asset tag stats.
	 *
	 * @param tagId the tag ID of this asset tag stats
	 */
	public void setTagId(long tagId);

	/**
	 * Returns the fully qualified class name of this asset tag stats.
	 *
	 * @return the fully qualified class name of this asset tag stats
	 */
	public String getClassName();

	public void setClassName(String className);

	/**
	 * Returns the class name ID of this asset tag stats.
	 *
	 * @return the class name ID of this asset tag stats
	 */
	public long getClassNameId();

	/**
	 * Sets the class name ID of this asset tag stats.
	 *
	 * @param classNameId the class name ID of this asset tag stats
	 */
	public void setClassNameId(long classNameId);

	/**
	 * Returns the asset count of this asset tag stats.
	 *
	 * @return the asset count of this asset tag stats
	 */
	public int getAssetCount();

	/**
	 * Sets the asset count of this asset tag stats.
	 *
	 * @param assetCount the asset count of this asset tag stats
	 */
	public void setAssetCount(int assetCount);

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

	public int compareTo(AssetTagStats assetTagStats);

	public int hashCode();

	public CacheModel<AssetTagStats> toCacheModel();

	public AssetTagStats toEscapedModel();

	public AssetTagStats toUnescapedModel();

	public String toString();

	public String toXmlString();
}