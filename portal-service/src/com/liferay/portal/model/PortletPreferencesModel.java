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

package com.liferay.portal.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

/**
 * The base model interface for the PortletPreferences service. Represents a row in the &quot;PortletPreferences&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portal.model.impl.PortletPreferencesModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portal.model.impl.PortletPreferencesImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see PortletPreferences
 * @see com.liferay.portal.model.impl.PortletPreferencesImpl
 * @see com.liferay.portal.model.impl.PortletPreferencesModelImpl
 * @generated
 */
@ProviderType
public interface PortletPreferencesModel extends BaseModel<PortletPreferences>,
	MVCCModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a portlet preferences model instance should use the {@link PortletPreferences} interface instead.
	 */

	/**
	 * Returns the primary key of this portlet preferences.
	 *
	 * @return the primary key of this portlet preferences
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this portlet preferences.
	 *
	 * @param primaryKey the primary key of this portlet preferences
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this portlet preferences.
	 *
	 * @return the mvcc version of this portlet preferences
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this portlet preferences.
	 *
	 * @param mvccVersion the mvcc version of this portlet preferences
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the portlet preferences ID of this portlet preferences.
	 *
	 * @return the portlet preferences ID of this portlet preferences
	 */
	public long getPortletPreferencesId();

	/**
	 * Sets the portlet preferences ID of this portlet preferences.
	 *
	 * @param portletPreferencesId the portlet preferences ID of this portlet preferences
	 */
	public void setPortletPreferencesId(long portletPreferencesId);

	/**
	 * Returns the owner ID of this portlet preferences.
	 *
	 * @return the owner ID of this portlet preferences
	 */
	public long getOwnerId();

	/**
	 * Sets the owner ID of this portlet preferences.
	 *
	 * @param ownerId the owner ID of this portlet preferences
	 */
	public void setOwnerId(long ownerId);

	/**
	 * Returns the owner type of this portlet preferences.
	 *
	 * @return the owner type of this portlet preferences
	 */
	public int getOwnerType();

	/**
	 * Sets the owner type of this portlet preferences.
	 *
	 * @param ownerType the owner type of this portlet preferences
	 */
	public void setOwnerType(int ownerType);

	/**
	 * Returns the plid of this portlet preferences.
	 *
	 * @return the plid of this portlet preferences
	 */
	public long getPlid();

	/**
	 * Sets the plid of this portlet preferences.
	 *
	 * @param plid the plid of this portlet preferences
	 */
	public void setPlid(long plid);

	/**
	 * Returns the portlet ID of this portlet preferences.
	 *
	 * @return the portlet ID of this portlet preferences
	 */
	@AutoEscape
	public String getPortletId();

	/**
	 * Sets the portlet ID of this portlet preferences.
	 *
	 * @param portletId the portlet ID of this portlet preferences
	 */
	public void setPortletId(String portletId);

	/**
	 * Returns the preferences of this portlet preferences.
	 *
	 * @return the preferences of this portlet preferences
	 */
	@AutoEscape
	public String getPreferences();

	/**
	 * Sets the preferences of this portlet preferences.
	 *
	 * @param preferences the preferences of this portlet preferences
	 */
	public void setPreferences(String preferences);

	@Override
	public boolean isNew();

	@Override
	public void setNew(boolean n);

	@Override
	public boolean isCachedModel();

	@Override
	public void setCachedModel(boolean cachedModel);

	@Override
	public boolean isEscapedModel();

	@Override
	public Serializable getPrimaryKeyObj();

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	@Override
	public ExpandoBridge getExpandoBridge();

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	@Override
	public Object clone();

	@Override
	public int compareTo(
		com.liferay.portal.model.PortletPreferences portletPreferences);

	@Override
	public int hashCode();

	@Override
	public CacheModel<com.liferay.portal.model.PortletPreferences> toCacheModel();

	@Override
	public com.liferay.portal.model.PortletPreferences toEscapedModel();

	@Override
	public com.liferay.portal.model.PortletPreferences toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}