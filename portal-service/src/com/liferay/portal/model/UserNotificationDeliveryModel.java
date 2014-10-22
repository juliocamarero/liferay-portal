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
 * The base model interface for the UserNotificationDelivery service. Represents a row in the &quot;UserNotificationDelivery&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portal.model.impl.UserNotificationDeliveryModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portal.model.impl.UserNotificationDeliveryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see UserNotificationDelivery
 * @see com.liferay.portal.model.impl.UserNotificationDeliveryImpl
 * @see com.liferay.portal.model.impl.UserNotificationDeliveryModelImpl
 * @generated
 */
@ProviderType
public interface UserNotificationDeliveryModel extends BaseModel<UserNotificationDelivery>,
	MVCCModel, TypedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a user notification delivery model instance should use the {@link UserNotificationDelivery} interface instead.
	 */

	/**
	 * Returns the primary key of this user notification delivery.
	 *
	 * @return the primary key of this user notification delivery
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this user notification delivery.
	 *
	 * @param primaryKey the primary key of this user notification delivery
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this user notification delivery.
	 *
	 * @return the mvcc version of this user notification delivery
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this user notification delivery.
	 *
	 * @param mvccVersion the mvcc version of this user notification delivery
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the user notification delivery ID of this user notification delivery.
	 *
	 * @return the user notification delivery ID of this user notification delivery
	 */
	public long getUserNotificationDeliveryId();

	/**
	 * Sets the user notification delivery ID of this user notification delivery.
	 *
	 * @param userNotificationDeliveryId the user notification delivery ID of this user notification delivery
	 */
	public void setUserNotificationDeliveryId(long userNotificationDeliveryId);

	/**
	 * Returns the company ID of this user notification delivery.
	 *
	 * @return the company ID of this user notification delivery
	 */
	public long getCompanyId();

	/**
	 * Sets the company ID of this user notification delivery.
	 *
	 * @param companyId the company ID of this user notification delivery
	 */
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this user notification delivery.
	 *
	 * @return the user ID of this user notification delivery
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this user notification delivery.
	 *
	 * @param userId the user ID of this user notification delivery
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this user notification delivery.
	 *
	 * @return the user uuid of this user notification delivery
	 */
	public String getUserUuid();

	/**
	 * Sets the user uuid of this user notification delivery.
	 *
	 * @param userUuid the user uuid of this user notification delivery
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the portlet ID of this user notification delivery.
	 *
	 * @return the portlet ID of this user notification delivery
	 */
	@AutoEscape
	public String getPortletId();

	/**
	 * Sets the portlet ID of this user notification delivery.
	 *
	 * @param portletId the portlet ID of this user notification delivery
	 */
	public void setPortletId(String portletId);

	/**
	 * Returns the fully qualified class name of this user notification delivery.
	 *
	 * @return the fully qualified class name of this user notification delivery
	 */
	@Override
	public String getClassName();

	public void setClassName(String className);

	/**
	 * Returns the class name ID of this user notification delivery.
	 *
	 * @return the class name ID of this user notification delivery
	 */
	@Override
	public long getClassNameId();

	/**
	 * Sets the class name ID of this user notification delivery.
	 *
	 * @param classNameId the class name ID of this user notification delivery
	 */
	@Override
	public void setClassNameId(long classNameId);

	/**
	 * Returns the notification type of this user notification delivery.
	 *
	 * @return the notification type of this user notification delivery
	 */
	public int getNotificationType();

	/**
	 * Sets the notification type of this user notification delivery.
	 *
	 * @param notificationType the notification type of this user notification delivery
	 */
	public void setNotificationType(int notificationType);

	/**
	 * Returns the delivery type of this user notification delivery.
	 *
	 * @return the delivery type of this user notification delivery
	 */
	public int getDeliveryType();

	/**
	 * Sets the delivery type of this user notification delivery.
	 *
	 * @param deliveryType the delivery type of this user notification delivery
	 */
	public void setDeliveryType(int deliveryType);

	/**
	 * Returns the deliver of this user notification delivery.
	 *
	 * @return the deliver of this user notification delivery
	 */
	public boolean getDeliver();

	/**
	 * Returns <code>true</code> if this user notification delivery is deliver.
	 *
	 * @return <code>true</code> if this user notification delivery is deliver; <code>false</code> otherwise
	 */
	public boolean isDeliver();

	/**
	 * Sets whether this user notification delivery is deliver.
	 *
	 * @param deliver the deliver of this user notification delivery
	 */
	public void setDeliver(boolean deliver);

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
		com.liferay.portal.model.UserNotificationDelivery userNotificationDelivery);

	@Override
	public int hashCode();

	@Override
	public CacheModel<com.liferay.portal.model.UserNotificationDelivery> toCacheModel();

	@Override
	public com.liferay.portal.model.UserNotificationDelivery toEscapedModel();

	@Override
	public com.liferay.portal.model.UserNotificationDelivery toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}