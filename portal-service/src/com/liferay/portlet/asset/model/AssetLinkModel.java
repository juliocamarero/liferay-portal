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
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;
import java.util.Locale;

/**
 * The base model interface for the AssetLink service. Represents a row in the &quot;AssetLink&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.asset.model.impl.AssetLinkModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.asset.model.impl.AssetLinkImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetLink
 * @see com.liferay.portlet.asset.model.impl.AssetLinkImpl
 * @see com.liferay.portlet.asset.model.impl.AssetLinkModelImpl
 * @generated
 */
public interface AssetLinkModel extends BaseModel<AssetLink> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a asset link model instance should use the {@link AssetLink} interface instead.
	 */

	/**
	 * Returns the primary key of this asset link.
	 *
	 * @return the primary key of this asset link
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this asset link.
	 *
	 * @param primaryKey the primary key of this asset link
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the link ID of this asset link.
	 *
	 * @return the link ID of this asset link
	 */
	public long getLinkId();

	/**
	 * Sets the link ID of this asset link.
	 *
	 * @param linkId the link ID of this asset link
	 */
	public void setLinkId(long linkId);

	/**
	 * Returns the company ID of this asset link.
	 *
	 * @return the company ID of this asset link
	 */
	public long getCompanyId();

	/**
	 * Sets the company ID of this asset link.
	 *
	 * @param companyId the company ID of this asset link
	 */
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this asset link.
	 *
	 * @return the user ID of this asset link
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this asset link.
	 *
	 * @param userId the user ID of this asset link
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this asset link.
	 *
	 * @return the user uuid of this asset link
	 * @throws SystemException if a system exception occurred
	 */
	public String getUserUuid() throws SystemException;

	/**
	 * Sets the user uuid of this asset link.
	 *
	 * @param userUuid the user uuid of this asset link
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this asset link.
	 *
	 * @return the user name of this asset link
	 */
	@AutoEscape
	public String getUserName();

	/**
	 * Sets the user name of this asset link.
	 *
	 * @param userName the user name of this asset link
	 */
	public void setUserName(String userName);

	/**
	 * Returns the create date of this asset link.
	 *
	 * @return the create date of this asset link
	 */
	public Date getCreateDate();

	/**
	 * Sets the create date of this asset link.
	 *
	 * @param createDate the create date of this asset link
	 */
	public void setCreateDate(Date createDate);

	/**
	 * Returns the entry id1 of this asset link.
	 *
	 * @return the entry id1 of this asset link
	 */
	public long getEntryId1();

	/**
	 * Sets the entry id1 of this asset link.
	 *
	 * @param entryId1 the entry id1 of this asset link
	 */
	public void setEntryId1(long entryId1);

	/**
	 * Returns the entry id2 of this asset link.
	 *
	 * @return the entry id2 of this asset link
	 */
	public long getEntryId2();

	/**
	 * Sets the entry id2 of this asset link.
	 *
	 * @param entryId2 the entry id2 of this asset link
	 */
	public void setEntryId2(long entryId2);

	/**
	 * Returns the type of this asset link.
	 *
	 * @return the type of this asset link
	 */
	public int getType();

	/**
	 * Sets the type of this asset link.
	 *
	 * @param type the type of this asset link
	 */
	public void setType(int type);

	/**
	 * Returns the weight of this asset link.
	 *
	 * @return the weight of this asset link
	 */
	public int getWeight();

	/**
	 * Sets the weight of this asset link.
	 *
	 * @param weight the weight of this asset link
	 */
	public void setWeight(int weight);

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

	public int compareTo(AssetLink assetLink);

	public int hashCode();

	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale);

	public CacheModel<AssetLink> toCacheModel();

	public AssetLink toEscapedModel();

	public String toString();

	public String toXmlString();
}