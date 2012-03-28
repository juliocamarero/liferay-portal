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

package com.liferay.portal.service;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.util.MethodCache;
import com.liferay.portal.kernel.util.ReferenceRegistry;

/**
 * The utility for the service component local service. This utility wraps {@link com.liferay.portal.service.impl.ServiceComponentLocalServiceImpl} and is the primary access point for service operations in application layer code running on the local server.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ServiceComponentLocalService
 * @see com.liferay.portal.service.base.ServiceComponentLocalServiceBaseImpl
 * @see com.liferay.portal.service.impl.ServiceComponentLocalServiceImpl
 * @generated
 */
public class ServiceComponentLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.portal.service.impl.ServiceComponentLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* Adds the service component to the database. Also notifies the appropriate model listeners.
	*
	* @param serviceComponent the service component
	* @return the service component that was added
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.ServiceComponent addServiceComponent(
		com.liferay.portal.model.ServiceComponent serviceComponent)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().addServiceComponent(serviceComponent);
	}

	/**
	* Creates a new service component with the primary key. Does not add the service component to the database.
	*
	* @param serviceComponentId the primary key for the new service component
	* @return the new service component
	*/
	public static com.liferay.portal.model.ServiceComponent createServiceComponent(
		long serviceComponentId) {
		return getService().createServiceComponent(serviceComponentId);
	}

	/**
	* Deletes the service component with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param serviceComponentId the primary key of the service component
	* @return the service component that was removed
	* @throws PortalException if a service component with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.ServiceComponent deleteServiceComponent(
		long serviceComponentId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().deleteServiceComponent(serviceComponentId);
	}

	/**
	* Deletes the service component from the database. Also notifies the appropriate model listeners.
	*
	* @param serviceComponent the service component
	* @return the service component that was removed
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.ServiceComponent deleteServiceComponent(
		com.liferay.portal.model.ServiceComponent serviceComponent)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().deleteServiceComponent(serviceComponent);
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public static java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQuery(dynamicQuery);
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
	public static java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQuery(dynamicQuery, start, end);
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
	public static java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService()
				   .dynamicQuery(dynamicQuery, start, end, orderByComparator);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows that match the dynamic query
	* @throws SystemException if a system exception occurred
	*/
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	public static com.liferay.portal.model.ServiceComponent fetchServiceComponent(
		long serviceComponentId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().fetchServiceComponent(serviceComponentId);
	}

	/**
	* Returns the service component with the primary key.
	*
	* @param serviceComponentId the primary key of the service component
	* @return the service component
	* @throws PortalException if a service component with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.ServiceComponent getServiceComponent(
		long serviceComponentId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getServiceComponent(serviceComponentId);
	}

	public static com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	* Returns a range of all the service components.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param start the lower bound of the range of service components
	* @param end the upper bound of the range of service components (not inclusive)
	* @return the range of service components
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.model.ServiceComponent> getServiceComponents(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getServiceComponents(start, end);
	}

	/**
	* Returns the number of service components.
	*
	* @return the number of service components
	* @throws SystemException if a system exception occurred
	*/
	public static int getServiceComponentsCount()
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getServiceComponentsCount();
	}

	/**
	* Updates the service component in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param serviceComponent the service component
	* @return the service component that was updated
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.ServiceComponent updateServiceComponent(
		com.liferay.portal.model.ServiceComponent serviceComponent)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().updateServiceComponent(serviceComponent);
	}

	/**
	* Updates the service component in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param serviceComponent the service component
	* @param merge whether to merge the service component with the current session. See {@link com.liferay.portal.service.persistence.BatchSession#update(com.liferay.portal.kernel.dao.orm.Session, com.liferay.portal.model.BaseModel, boolean)} for an explanation.
	* @return the service component that was updated
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.ServiceComponent updateServiceComponent(
		com.liferay.portal.model.ServiceComponent serviceComponent,
		boolean merge)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().updateServiceComponent(serviceComponent, merge);
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public static java.lang.String getBeanIdentifier() {
		return getService().getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public static void setBeanIdentifier(java.lang.String beanIdentifier) {
		getService().setBeanIdentifier(beanIdentifier);
	}

	public static void destroyServiceComponent(
		javax.servlet.ServletContext servletContext,
		java.lang.ClassLoader classLoader)
		throws com.liferay.portal.kernel.exception.SystemException {
		getService().destroyServiceComponent(servletContext, classLoader);
	}

	public static com.liferay.portal.model.ServiceComponent initServiceComponent(
		javax.servlet.ServletContext servletContext,
		java.lang.ClassLoader classLoader, java.lang.String buildNamespace,
		long buildNumber, long buildDate, boolean buildAutoUpgrade)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService()
				   .initServiceComponent(servletContext, classLoader,
			buildNamespace, buildNumber, buildDate, buildAutoUpgrade);
	}

	public static void upgradeDB(java.lang.ClassLoader classLoader,
		java.lang.String buildNamespace, long buildNumber,
		boolean buildAutoUpgrade,
		com.liferay.portal.model.ServiceComponent previousServiceComponent,
		java.lang.String tablesSQL, java.lang.String sequencesSQL,
		java.lang.String indexesSQL) throws java.lang.Exception {
		getService()
			.upgradeDB(classLoader, buildNamespace, buildNumber,
			buildAutoUpgrade, previousServiceComponent, tablesSQL,
			sequencesSQL, indexesSQL);
	}

	public static void verifyDB()
		throws com.liferay.portal.kernel.exception.SystemException {
		getService().verifyDB();
	}

	public static ServiceComponentLocalService getService() {
		if (_service == null) {
			_service = (ServiceComponentLocalService)PortalBeanLocatorUtil.locate(ServiceComponentLocalService.class.getName());

			ReferenceRegistry.registerReference(ServiceComponentLocalServiceUtil.class,
				"_service");
			MethodCache.remove(ServiceComponentLocalService.class);
		}

		return _service;
	}

	public void setService(ServiceComponentLocalService service) {
		MethodCache.remove(ServiceComponentLocalService.class);

		_service = service;

		ReferenceRegistry.registerReference(ServiceComponentLocalServiceUtil.class,
			"_service");
		MethodCache.remove(ServiceComponentLocalService.class);
	}

	private static ServiceComponentLocalService _service;
}