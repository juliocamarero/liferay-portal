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

package com.liferay.portlet.messageboards.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link MBDiscussionLocalService}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       MBDiscussionLocalService
 * @generated
 */
public class MBDiscussionLocalServiceWrapper implements MBDiscussionLocalService,
	ServiceWrapper<MBDiscussionLocalService> {
	public MBDiscussionLocalServiceWrapper(
		MBDiscussionLocalService mbDiscussionLocalService) {
		_mbDiscussionLocalService = mbDiscussionLocalService;
	}

	/**
	* Adds the message boards discussion to the database. Also notifies the appropriate model listeners.
	*
	* @param mbDiscussion the message boards discussion
	* @return the message boards discussion that was added
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.messageboards.model.MBDiscussion addMBDiscussion(
		com.liferay.portlet.messageboards.model.MBDiscussion mbDiscussion)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.addMBDiscussion(mbDiscussion);
	}

	/**
	* Creates a new message boards discussion with the primary key. Does not add the message boards discussion to the database.
	*
	* @param discussionId the primary key for the new message boards discussion
	* @return the new message boards discussion
	*/
	public com.liferay.portlet.messageboards.model.MBDiscussion createMBDiscussion(
		long discussionId) {
		return _mbDiscussionLocalService.createMBDiscussion(discussionId);
	}

	/**
	* Deletes the message boards discussion with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param discussionId the primary key of the message boards discussion
	* @return the message boards discussion that was removed
	* @throws PortalException if a message boards discussion with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.messageboards.model.MBDiscussion deleteMBDiscussion(
		long discussionId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.deleteMBDiscussion(discussionId);
	}

	/**
	* Deletes the message boards discussion from the database. Also notifies the appropriate model listeners.
	*
	* @param mbDiscussion the message boards discussion
	* @return the message boards discussion that was removed
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.messageboards.model.MBDiscussion deleteMBDiscussion(
		com.liferay.portlet.messageboards.model.MBDiscussion mbDiscussion)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.deleteMBDiscussion(mbDiscussion);
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) throws com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.dynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows that match the dynamic query
	* @throws SystemException if a system exception occurred
	*/
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.dynamicQueryCount(dynamicQuery);
	}

	public com.liferay.portlet.messageboards.model.MBDiscussion fetchMBDiscussion(
		long discussionId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.fetchMBDiscussion(discussionId);
	}

	/**
	* Returns the message boards discussion with the primary key.
	*
	* @param discussionId the primary key of the message boards discussion
	* @return the message boards discussion
	* @throws PortalException if a message boards discussion with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.messageboards.model.MBDiscussion getMBDiscussion(
		long discussionId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.getMBDiscussion(discussionId);
	}

	public com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	* Returns a range of all the message boards discussions.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param start the lower bound of the range of message boards discussions
	* @param end the upper bound of the range of message boards discussions (not inclusive)
	* @return the range of message boards discussions
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portlet.messageboards.model.MBDiscussion> getMBDiscussions(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.getMBDiscussions(start, end);
	}

	/**
	* Returns the number of message boards discussions.
	*
	* @return the number of message boards discussions
	* @throws SystemException if a system exception occurred
	*/
	public int getMBDiscussionsCount()
		throws com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.getMBDiscussionsCount();
	}

	/**
	* Updates the message boards discussion in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param mbDiscussion the message boards discussion
	* @return the message boards discussion that was updated
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.messageboards.model.MBDiscussion updateMBDiscussion(
		com.liferay.portlet.messageboards.model.MBDiscussion mbDiscussion)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.updateMBDiscussion(mbDiscussion);
	}

	/**
	* Updates the message boards discussion in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param mbDiscussion the message boards discussion
	* @param merge whether to merge the message boards discussion with the current session. See {@link com.liferay.portal.service.persistence.BatchSession#update(com.liferay.portal.kernel.dao.orm.Session, com.liferay.portal.model.BaseModel, boolean)} for an explanation.
	* @return the message boards discussion that was updated
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.messageboards.model.MBDiscussion updateMBDiscussion(
		com.liferay.portlet.messageboards.model.MBDiscussion mbDiscussion,
		boolean merge)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.updateMBDiscussion(mbDiscussion, merge);
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public java.lang.String getBeanIdentifier() {
		return _mbDiscussionLocalService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_mbDiscussionLocalService.setBeanIdentifier(beanIdentifier);
	}

	public com.liferay.portlet.messageboards.model.MBDiscussion addDiscussion(
		long classNameId, long classPK, long threadId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.addDiscussion(classNameId, classPK,
			threadId);
	}

	public com.liferay.portlet.messageboards.model.MBDiscussion getDiscussion(
		long discussionId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.getDiscussion(discussionId);
	}

	public com.liferay.portlet.messageboards.model.MBDiscussion getDiscussion(
		java.lang.String className, long classPK)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.getDiscussion(className, classPK);
	}

	public com.liferay.portlet.messageboards.model.MBDiscussion getThreadDiscussion(
		long threadId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _mbDiscussionLocalService.getThreadDiscussion(threadId);
	}

	/**
	 * @deprecated Renamed to {@link #getWrappedService}
	 */
	public MBDiscussionLocalService getWrappedMBDiscussionLocalService() {
		return _mbDiscussionLocalService;
	}

	/**
	 * @deprecated Renamed to {@link #setWrappedService}
	 */
	public void setWrappedMBDiscussionLocalService(
		MBDiscussionLocalService mbDiscussionLocalService) {
		_mbDiscussionLocalService = mbDiscussionLocalService;
	}

	public MBDiscussionLocalService getWrappedService() {
		return _mbDiscussionLocalService;
	}

	public void setWrappedService(
		MBDiscussionLocalService mbDiscussionLocalService) {
		_mbDiscussionLocalService = mbDiscussionLocalService;
	}

	private MBDiscussionLocalService _mbDiscussionLocalService;
}