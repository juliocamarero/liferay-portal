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

package com.liferay.portal.model;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Locale;

/**
 * The base model interface for the BrowserTracker service. Represents a row in the &quot;BrowserTracker&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portal.model.impl.BrowserTrackerModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portal.model.impl.BrowserTrackerImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see BrowserTracker
 * @see com.liferay.portal.model.impl.BrowserTrackerImpl
 * @see com.liferay.portal.model.impl.BrowserTrackerModelImpl
 * @generated
 */
public interface BrowserTrackerModel extends BaseModel<BrowserTracker> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a browser tracker model instance should use the {@link BrowserTracker} interface instead.
	 */

	/**
	 * Returns the primary key of this browser tracker.
	 *
	 * @return the primary key of this browser tracker
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this browser tracker.
	 *
	 * @param primaryKey the primary key of this browser tracker
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the browser tracker ID of this browser tracker.
	 *
	 * @return the browser tracker ID of this browser tracker
	 */
	public long getBrowserTrackerId();

	/**
	 * Sets the browser tracker ID of this browser tracker.
	 *
	 * @param browserTrackerId the browser tracker ID of this browser tracker
	 */
	public void setBrowserTrackerId(long browserTrackerId);

	/**
	 * Returns the user ID of this browser tracker.
	 *
	 * @return the user ID of this browser tracker
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this browser tracker.
	 *
	 * @param userId the user ID of this browser tracker
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this browser tracker.
	 *
	 * @return the user uuid of this browser tracker
	 * @throws SystemException if a system exception occurred
	 */
	public String getUserUuid() throws SystemException;

	/**
	 * Sets the user uuid of this browser tracker.
	 *
	 * @param userUuid the user uuid of this browser tracker
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the browser key of this browser tracker.
	 *
	 * @return the browser key of this browser tracker
	 */
	public long getBrowserKey();

	/**
	 * Sets the browser key of this browser tracker.
	 *
	 * @param browserKey the browser key of this browser tracker
	 */
	public void setBrowserKey(long browserKey);

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

	public int compareTo(BrowserTracker browserTracker);

	public int hashCode();

	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale);

	public CacheModel<BrowserTracker> toCacheModel();

	public BrowserTracker toEscapedModel();

	public String toString();

	public String toXmlString();
}