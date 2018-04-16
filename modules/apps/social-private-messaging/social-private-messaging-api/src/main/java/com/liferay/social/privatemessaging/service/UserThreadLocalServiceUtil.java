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

package com.liferay.social.privatemessaging.service;

import aQute.bnd.annotation.ProviderType;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for UserThread. This utility wraps
 * {@link com.liferay.social.privatemessaging.service.impl.UserThreadLocalServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see UserThreadLocalService
 * @see com.liferay.social.privatemessaging.service.base.UserThreadLocalServiceBaseImpl
 * @see com.liferay.social.privatemessaging.service.impl.UserThreadLocalServiceImpl
 * @generated
 */
@ProviderType
public class UserThreadLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.social.privatemessaging.service.impl.UserThreadLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.message.boards.model.MBMessage addPrivateMessage(
		long userId, long mbThreadId, java.lang.String to,
		java.lang.String subject, java.lang.String body,
		java.util.List<com.liferay.portal.kernel.util.ObjectValuePair<java.lang.String, java.io.InputStream>> inputStreamOVPs,
		com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addPrivateMessage(userId, mbThreadId, to, subject, body,
			inputStreamOVPs, themeDisplay);
	}

	public static com.liferay.message.boards.model.MBMessage addPrivateMessageBranch(
		long userId, long parentMBMessageId, java.lang.String body,
		java.util.List<com.liferay.portal.kernel.util.ObjectValuePair<java.lang.String, java.io.InputStream>> inputStreamOVPs,
		com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addPrivateMessageBranch(userId, parentMBMessageId, body,
			inputStreamOVPs, themeDisplay);
	}

	public static void addUserThread(long userId, long mbThreadId,
		long topMBMessageId, boolean read, boolean deleted)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService()
			.addUserThread(userId, mbThreadId, topMBMessageId, read, deleted);
	}

	/**
	* Adds the user thread to the database. Also notifies the appropriate model listeners.
	*
	* @param userThread the user thread
	* @return the user thread that was added
	*/
	public static com.liferay.social.privatemessaging.model.UserThread addUserThread(
		com.liferay.social.privatemessaging.model.UserThread userThread) {
		return getService().addUserThread(userThread);
	}

	/**
	* Creates a new user thread with the primary key. Does not add the user thread to the database.
	*
	* @param userThreadId the primary key for the new user thread
	* @return the new user thread
	*/
	public static com.liferay.social.privatemessaging.model.UserThread createUserThread(
		long userThreadId) {
		return getService().createUserThread(userThreadId);
	}

	/**
	* @throws PortalException
	*/
	public static com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
		com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().deletePersistedModel(persistedModel);
	}

	public static void deleteUser(long userId)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService().deleteUser(userId);
	}

	/**
	* Deletes the user thread with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param userThreadId the primary key of the user thread
	* @return the user thread that was removed
	* @throws PortalException if a user thread with the primary key could not be found
	*/
	public static com.liferay.social.privatemessaging.model.UserThread deleteUserThread(
		long userThreadId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().deleteUserThread(userThreadId);
	}

	public static void deleteUserThread(long userId, long mbThreadId)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService().deleteUserThread(userId, mbThreadId);
	}

	/**
	* Deletes the user thread from the database. Also notifies the appropriate model listeners.
	*
	* @param userThread the user thread
	* @return the user thread that was removed
	*/
	public static com.liferay.social.privatemessaging.model.UserThread deleteUserThread(
		com.liferay.social.privatemessaging.model.UserThread userThread) {
		return getService().deleteUserThread(userThread);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	*/
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.social.privatemessaging.model.impl.UserThreadModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	*/
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {
		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.social.privatemessaging.model.impl.UserThreadModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	*/
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {
		return getService()
				   .dynamicQuery(dynamicQuery, start, end, orderByComparator);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows matching the dynamic query
	*/
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows matching the dynamic query
	*/
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {
		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static com.liferay.social.privatemessaging.model.UserThread fetchUserThread(
		long userThreadId) {
		return getService().fetchUserThread(userThreadId);
	}

	public static com.liferay.social.privatemessaging.model.UserThread fetchUserThread(
		long userId, long mbThreadId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().fetchUserThread(userId, mbThreadId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return getService().getActionableDynamicQuery();
	}

	public static com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery getIndexableActionableDynamicQuery() {
		return getService().getIndexableActionableDynamicQuery();
	}

	public static java.util.List<com.liferay.social.privatemessaging.model.UserThread> getMBThreadUserThreads(
		long mbThreadId) {
		return getService().getMBThreadUserThreads(mbThreadId);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static java.lang.String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	* Returns the user thread with the primary key.
	*
	* @param userThreadId the primary key of the user thread
	* @return the user thread
	* @throws PortalException if a user thread with the primary key could not be found
	*/
	public static com.liferay.social.privatemessaging.model.UserThread getUserThread(
		long userThreadId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getUserThread(userThreadId);
	}

	public static com.liferay.social.privatemessaging.model.UserThread getUserThread(
		long userId, long mbThreadId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getUserThread(userId, mbThreadId);
	}

	/**
	* Returns a range of all the user threads.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.social.privatemessaging.model.impl.UserThreadModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of user threads
	* @param end the upper bound of the range of user threads (not inclusive)
	* @return the range of user threads
	*/
	public static java.util.List<com.liferay.social.privatemessaging.model.UserThread> getUserThreads(
		int start, int end) {
		return getService().getUserThreads(start, end);
	}

	/**
	* Returns the number of user threads.
	*
	* @return the number of user threads
	*/
	public static int getUserThreadsCount() {
		return getService().getUserThreadsCount();
	}

	public static int getUserUserThreadCount(long userId, boolean deleted) {
		return getService().getUserUserThreadCount(userId, deleted);
	}

	public static int getUserUserThreadCount(long userId, boolean read,
		boolean deleted) {
		return getService().getUserUserThreadCount(userId, read, deleted);
	}

	public static java.util.List<com.liferay.social.privatemessaging.model.UserThread> getUserUserThreads(
		long userId, boolean deleted) {
		return getService().getUserUserThreads(userId, deleted);
	}

	public static java.util.List<com.liferay.social.privatemessaging.model.UserThread> getUserUserThreads(
		long userId, boolean read, boolean deleted) {
		return getService().getUserUserThreads(userId, read, deleted);
	}

	public static java.util.List<com.liferay.social.privatemessaging.model.UserThread> getUserUserThreads(
		long userId, boolean deleted, int start, int end) {
		return getService().getUserUserThreads(userId, deleted, start, end);
	}

	public static void markUserThreadAsRead(long userId, long mbThreadId)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService().markUserThreadAsRead(userId, mbThreadId);
	}

	public static void markUserThreadAsUnread(long userId, long mbThreadId)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService().markUserThreadAsUnread(userId, mbThreadId);
	}

	public static void updateUserName(com.liferay.portal.kernel.model.User user) {
		getService().updateUserName(user);
	}

	/**
	* Updates the user thread in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param userThread the user thread
	* @return the user thread that was updated
	*/
	public static com.liferay.social.privatemessaging.model.UserThread updateUserThread(
		com.liferay.social.privatemessaging.model.UserThread userThread) {
		return getService().updateUserThread(userThread);
	}

	public static UserThreadLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<UserThreadLocalService, UserThreadLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(UserThreadLocalService.class);

		ServiceTracker<UserThreadLocalService, UserThreadLocalService> serviceTracker =
			new ServiceTracker<UserThreadLocalService, UserThreadLocalService>(bundle.getBundleContext(),
				UserThreadLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
}