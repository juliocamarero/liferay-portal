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

package com.liferay.portlet.messageboards.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.model.AttachedModel;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.StagedGroupedModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the MBDiscussion service. Represents a row in the &quot;MBDiscussion&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.messageboards.model.impl.MBDiscussionModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.messageboards.model.impl.MBDiscussionImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see MBDiscussion
 * @see com.liferay.portlet.messageboards.model.impl.MBDiscussionImpl
 * @see com.liferay.portlet.messageboards.model.impl.MBDiscussionModelImpl
 * @generated
 */
@ProviderType
public interface MBDiscussionModel extends AttachedModel, BaseModel<MBDiscussion>,
	StagedGroupedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a message boards discussion model instance should use the {@link MBDiscussion} interface instead.
	 */

	/**
	 * Returns the primary key of this message boards discussion.
	 *
	 * @return the primary key of this message boards discussion
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this message boards discussion.
	 *
	 * @param primaryKey the primary key of this message boards discussion
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this message boards discussion.
	 *
	 * @return the uuid of this message boards discussion
	 */
	@AutoEscape
	@Override
	public String getUuid();

	/**
	 * Sets the uuid of this message boards discussion.
	 *
	 * @param uuid the uuid of this message boards discussion
	 */
	@Override
	public void setUuid(String uuid);

	/**
	 * Returns the discussion ID of this message boards discussion.
	 *
	 * @return the discussion ID of this message boards discussion
	 */
	public long getDiscussionId();

	/**
	 * Sets the discussion ID of this message boards discussion.
	 *
	 * @param discussionId the discussion ID of this message boards discussion
	 */
	public void setDiscussionId(long discussionId);

	/**
	 * Returns the group ID of this message boards discussion.
	 *
	 * @return the group ID of this message boards discussion
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this message boards discussion.
	 *
	 * @param groupId the group ID of this message boards discussion
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this message boards discussion.
	 *
	 * @return the company ID of this message boards discussion
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this message boards discussion.
	 *
	 * @param companyId the company ID of this message boards discussion
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this message boards discussion.
	 *
	 * @return the user ID of this message boards discussion
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this message boards discussion.
	 *
	 * @param userId the user ID of this message boards discussion
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this message boards discussion.
	 *
	 * @return the user uuid of this message boards discussion
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this message boards discussion.
	 *
	 * @param userUuid the user uuid of this message boards discussion
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this message boards discussion.
	 *
	 * @return the user name of this message boards discussion
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this message boards discussion.
	 *
	 * @param userName the user name of this message boards discussion
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this message boards discussion.
	 *
	 * @return the create date of this message boards discussion
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this message boards discussion.
	 *
	 * @param createDate the create date of this message boards discussion
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this message boards discussion.
	 *
	 * @return the modified date of this message boards discussion
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this message boards discussion.
	 *
	 * @param modifiedDate the modified date of this message boards discussion
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the fully qualified class name of this message boards discussion.
	 *
	 * @return the fully qualified class name of this message boards discussion
	 */
	@Override
	public String getClassName();

	public void setClassName(String className);

	/**
	 * Returns the class name ID of this message boards discussion.
	 *
	 * @return the class name ID of this message boards discussion
	 */
	@Override
	public long getClassNameId();

	/**
	 * Sets the class name ID of this message boards discussion.
	 *
	 * @param classNameId the class name ID of this message boards discussion
	 */
	@Override
	public void setClassNameId(long classNameId);

	/**
	 * Returns the class p k of this message boards discussion.
	 *
	 * @return the class p k of this message boards discussion
	 */
	@Override
	public long getClassPK();

	/**
	 * Sets the class p k of this message boards discussion.
	 *
	 * @param classPK the class p k of this message boards discussion
	 */
	@Override
	public void setClassPK(long classPK);

	/**
	 * Returns the thread ID of this message boards discussion.
	 *
	 * @return the thread ID of this message boards discussion
	 */
	public long getThreadId();

	/**
	 * Sets the thread ID of this message boards discussion.
	 *
	 * @param threadId the thread ID of this message boards discussion
	 */
	public void setThreadId(long threadId);

	/**
	 * Returns the last publish date of this message boards discussion.
	 *
	 * @return the last publish date of this message boards discussion
	 */
	@Override
	public Date getLastPublishDate();

	/**
	 * Sets the last publish date of this message boards discussion.
	 *
	 * @param lastPublishDate the last publish date of this message boards discussion
	 */
	@Override
	public void setLastPublishDate(Date lastPublishDate);

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
		com.liferay.portlet.messageboards.model.MBDiscussion mbDiscussion);

	@Override
	public int hashCode();

	@Override
	public CacheModel<com.liferay.portlet.messageboards.model.MBDiscussion> toCacheModel();

	@Override
	public com.liferay.portlet.messageboards.model.MBDiscussion toEscapedModel();

	@Override
	public com.liferay.portlet.messageboards.model.MBDiscussion toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}