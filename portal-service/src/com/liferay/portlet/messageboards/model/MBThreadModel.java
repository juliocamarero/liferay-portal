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

package com.liferay.portlet.messageboards.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.WorkflowedModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;
import java.util.Locale;

/**
 * The base model interface for the MBThread service. Represents a row in the &quot;MBThread&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.messageboards.model.impl.MBThreadModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.messageboards.model.impl.MBThreadImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see MBThread
 * @see com.liferay.portlet.messageboards.model.impl.MBThreadImpl
 * @see com.liferay.portlet.messageboards.model.impl.MBThreadModelImpl
 * @generated
 */
public interface MBThreadModel extends BaseModel<MBThread>, WorkflowedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a message boards thread model instance should use the {@link MBThread} interface instead.
	 */

	/**
	 * Returns the primary key of this message boards thread.
	 *
	 * @return the primary key of this message boards thread
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this message boards thread.
	 *
	 * @param primaryKey the primary key of this message boards thread
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the thread ID of this message boards thread.
	 *
	 * @return the thread ID of this message boards thread
	 */
	public long getThreadId();

	/**
	 * Sets the thread ID of this message boards thread.
	 *
	 * @param threadId the thread ID of this message boards thread
	 */
	public void setThreadId(long threadId);

	/**
	 * Returns the group ID of this message boards thread.
	 *
	 * @return the group ID of this message boards thread
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this message boards thread.
	 *
	 * @param groupId the group ID of this message boards thread
	 */
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this message boards thread.
	 *
	 * @return the company ID of this message boards thread
	 */
	public long getCompanyId();

	/**
	 * Sets the company ID of this message boards thread.
	 *
	 * @param companyId the company ID of this message boards thread
	 */
	public void setCompanyId(long companyId);

	/**
	 * Returns the category ID of this message boards thread.
	 *
	 * @return the category ID of this message boards thread
	 */
	public long getCategoryId();

	/**
	 * Sets the category ID of this message boards thread.
	 *
	 * @param categoryId the category ID of this message boards thread
	 */
	public void setCategoryId(long categoryId);

	/**
	 * Returns the root message ID of this message boards thread.
	 *
	 * @return the root message ID of this message boards thread
	 */
	public long getRootMessageId();

	/**
	 * Sets the root message ID of this message boards thread.
	 *
	 * @param rootMessageId the root message ID of this message boards thread
	 */
	public void setRootMessageId(long rootMessageId);

	/**
	 * Returns the root message user ID of this message boards thread.
	 *
	 * @return the root message user ID of this message boards thread
	 */
	public long getRootMessageUserId();

	/**
	 * Sets the root message user ID of this message boards thread.
	 *
	 * @param rootMessageUserId the root message user ID of this message boards thread
	 */
	public void setRootMessageUserId(long rootMessageUserId);

	/**
	 * Returns the root message user uuid of this message boards thread.
	 *
	 * @return the root message user uuid of this message boards thread
	 * @throws SystemException if a system exception occurred
	 */
	public String getRootMessageUserUuid() throws SystemException;

	/**
	 * Sets the root message user uuid of this message boards thread.
	 *
	 * @param rootMessageUserUuid the root message user uuid of this message boards thread
	 */
	public void setRootMessageUserUuid(String rootMessageUserUuid);

	/**
	 * Returns the message count of this message boards thread.
	 *
	 * @return the message count of this message boards thread
	 */
	public int getMessageCount();

	/**
	 * Sets the message count of this message boards thread.
	 *
	 * @param messageCount the message count of this message boards thread
	 */
	public void setMessageCount(int messageCount);

	/**
	 * Returns the view count of this message boards thread.
	 *
	 * @return the view count of this message boards thread
	 */
	public int getViewCount();

	/**
	 * Sets the view count of this message boards thread.
	 *
	 * @param viewCount the view count of this message boards thread
	 */
	public void setViewCount(int viewCount);

	/**
	 * Returns the last post by user ID of this message boards thread.
	 *
	 * @return the last post by user ID of this message boards thread
	 */
	public long getLastPostByUserId();

	/**
	 * Sets the last post by user ID of this message boards thread.
	 *
	 * @param lastPostByUserId the last post by user ID of this message boards thread
	 */
	public void setLastPostByUserId(long lastPostByUserId);

	/**
	 * Returns the last post by user uuid of this message boards thread.
	 *
	 * @return the last post by user uuid of this message boards thread
	 * @throws SystemException if a system exception occurred
	 */
	public String getLastPostByUserUuid() throws SystemException;

	/**
	 * Sets the last post by user uuid of this message boards thread.
	 *
	 * @param lastPostByUserUuid the last post by user uuid of this message boards thread
	 */
	public void setLastPostByUserUuid(String lastPostByUserUuid);

	/**
	 * Returns the last post date of this message boards thread.
	 *
	 * @return the last post date of this message boards thread
	 */
	public Date getLastPostDate();

	/**
	 * Sets the last post date of this message boards thread.
	 *
	 * @param lastPostDate the last post date of this message boards thread
	 */
	public void setLastPostDate(Date lastPostDate);

	/**
	 * Returns the priority of this message boards thread.
	 *
	 * @return the priority of this message boards thread
	 */
	public double getPriority();

	/**
	 * Sets the priority of this message boards thread.
	 *
	 * @param priority the priority of this message boards thread
	 */
	public void setPriority(double priority);

	/**
	 * Returns the question of this message boards thread.
	 *
	 * @return the question of this message boards thread
	 */
	public boolean getQuestion();

	/**
	 * Returns <code>true</code> if this message boards thread is question.
	 *
	 * @return <code>true</code> if this message boards thread is question; <code>false</code> otherwise
	 */
	public boolean isQuestion();

	/**
	 * Sets whether this message boards thread is question.
	 *
	 * @param question the question of this message boards thread
	 */
	public void setQuestion(boolean question);

	/**
	 * Returns the status of this message boards thread.
	 *
	 * @return the status of this message boards thread
	 */
	public int getStatus();

	/**
	 * Sets the status of this message boards thread.
	 *
	 * @param status the status of this message boards thread
	 */
	public void setStatus(int status);

	/**
	 * Returns the status by user ID of this message boards thread.
	 *
	 * @return the status by user ID of this message boards thread
	 */
	public long getStatusByUserId();

	/**
	 * Sets the status by user ID of this message boards thread.
	 *
	 * @param statusByUserId the status by user ID of this message boards thread
	 */
	public void setStatusByUserId(long statusByUserId);

	/**
	 * Returns the status by user uuid of this message boards thread.
	 *
	 * @return the status by user uuid of this message boards thread
	 * @throws SystemException if a system exception occurred
	 */
	public String getStatusByUserUuid() throws SystemException;

	/**
	 * Sets the status by user uuid of this message boards thread.
	 *
	 * @param statusByUserUuid the status by user uuid of this message boards thread
	 */
	public void setStatusByUserUuid(String statusByUserUuid);

	/**
	 * Returns the status by user name of this message boards thread.
	 *
	 * @return the status by user name of this message boards thread
	 */
	@AutoEscape
	public String getStatusByUserName();

	/**
	 * Sets the status by user name of this message boards thread.
	 *
	 * @param statusByUserName the status by user name of this message boards thread
	 */
	public void setStatusByUserName(String statusByUserName);

	/**
	 * Returns the status date of this message boards thread.
	 *
	 * @return the status date of this message boards thread
	 */
	public Date getStatusDate();

	/**
	 * Sets the status date of this message boards thread.
	 *
	 * @param statusDate the status date of this message boards thread
	 */
	public void setStatusDate(Date statusDate);

	/**
	 * @deprecated Renamed to {@link #isApproved()}
	 */
	public boolean getApproved();

	/**
	 * Returns <code>true</code> if this message boards thread is approved.
	 *
	 * @return <code>true</code> if this message boards thread is approved; <code>false</code> otherwise
	 */
	public boolean isApproved();

	/**
	 * Returns <code>true</code> if this message boards thread is denied.
	 *
	 * @return <code>true</code> if this message boards thread is denied; <code>false</code> otherwise
	 */
	public boolean isDenied();

	/**
	 * Returns <code>true</code> if this message boards thread is a draft.
	 *
	 * @return <code>true</code> if this message boards thread is a draft; <code>false</code> otherwise
	 */
	public boolean isDraft();

	/**
	 * Returns <code>true</code> if this message boards thread is expired.
	 *
	 * @return <code>true</code> if this message boards thread is expired; <code>false</code> otherwise
	 */
	public boolean isExpired();

	/**
	 * Returns <code>true</code> if this message boards thread is inactive.
	 *
	 * @return <code>true</code> if this message boards thread is inactive; <code>false</code> otherwise
	 */
	public boolean isInactive();

	/**
	 * Returns <code>true</code> if this message boards thread is incomplete.
	 *
	 * @return <code>true</code> if this message boards thread is incomplete; <code>false</code> otherwise
	 */
	public boolean isIncomplete();

	/**
	 * Returns <code>true</code> if this message boards thread is in the Recycle Bin.
	 *
	 * @return <code>true</code> if this message boards thread is in the Recycle Bin; <code>false</code> otherwise
	 */
	public boolean isInTrash();

	/**
	 * Returns <code>true</code> if this message boards thread is pending.
	 *
	 * @return <code>true</code> if this message boards thread is pending; <code>false</code> otherwise
	 */
	public boolean isPending();

	/**
	 * Returns <code>true</code> if this message boards thread is scheduled.
	 *
	 * @return <code>true</code> if this message boards thread is scheduled; <code>false</code> otherwise
	 */
	public boolean isScheduled();

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

	public int compareTo(MBThread mbThread);

	public int hashCode();

	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale);

	public CacheModel<MBThread> toCacheModel();

	public MBThread toEscapedModel();

	public String toString();

	public String toXmlString();
}