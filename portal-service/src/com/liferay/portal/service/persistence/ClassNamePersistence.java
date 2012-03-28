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

package com.liferay.portal.service.persistence;

import com.liferay.portal.model.ClassName;

/**
 * The persistence interface for the class name service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ClassNamePersistenceImpl
 * @see ClassNameUtil
 * @generated
 */
public interface ClassNamePersistence extends BasePersistence<ClassName> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link ClassNameUtil} to access the class name persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Caches the class name in the entity cache if it is enabled.
	*
	* @param className the class name
	*/
	public void cacheResult(com.liferay.portal.model.ClassName className);

	/**
	* Caches the class names in the entity cache if it is enabled.
	*
	* @param classNames the class names
	*/
	public void cacheResult(
		java.util.List<com.liferay.portal.model.ClassName> classNames);

	/**
	* Creates a new class name with the primary key. Does not add the class name to the database.
	*
	* @param classNameId the primary key for the new class name
	* @return the new class name
	*/
	public com.liferay.portal.model.ClassName create(long classNameId);

	/**
	* Removes the class name with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param classNameId the primary key of the class name
	* @return the class name that was removed
	* @throws com.liferay.portal.NoSuchClassNameException if a class name with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.ClassName remove(long classNameId)
		throws com.liferay.portal.NoSuchClassNameException,
			com.liferay.portal.kernel.exception.SystemException;

	public com.liferay.portal.model.ClassName updateImpl(
		com.liferay.portal.model.ClassName className, boolean merge)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the class name with the primary key or throws a {@link com.liferay.portal.NoSuchClassNameException} if it could not be found.
	*
	* @param classNameId the primary key of the class name
	* @return the class name
	* @throws com.liferay.portal.NoSuchClassNameException if a class name with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.ClassName findByPrimaryKey(long classNameId)
		throws com.liferay.portal.NoSuchClassNameException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the class name with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param classNameId the primary key of the class name
	* @return the class name, or <code>null</code> if a class name with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.ClassName fetchByPrimaryKey(
		long classNameId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the class name where value = &#63; or throws a {@link com.liferay.portal.NoSuchClassNameException} if it could not be found.
	*
	* @param value the value
	* @return the matching class name
	* @throws com.liferay.portal.NoSuchClassNameException if a matching class name could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.ClassName findByValue(
		java.lang.String value)
		throws com.liferay.portal.NoSuchClassNameException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the class name where value = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param value the value
	* @return the matching class name, or <code>null</code> if a matching class name could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.ClassName fetchByValue(
		java.lang.String value)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the class name where value = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param value the value
	* @param retrieveFromCache whether to use the finder cache
	* @return the matching class name, or <code>null</code> if a matching class name could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.ClassName fetchByValue(
		java.lang.String value, boolean retrieveFromCache)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns all the class names.
	*
	* @return the class names
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.ClassName> findAll()
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the class names.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param start the lower bound of the range of class names
	* @param end the upper bound of the range of class names (not inclusive)
	* @return the range of class names
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.ClassName> findAll(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the class names.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param start the lower bound of the range of class names
	* @param end the upper bound of the range of class names (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of class names
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.ClassName> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes the class name where value = &#63; from the database.
	*
	* @param value the value
	* @return the class name that was removed
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.ClassName removeByValue(
		java.lang.String value)
		throws com.liferay.portal.NoSuchClassNameException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the class names from the database.
	*
	* @throws SystemException if a system exception occurred
	*/
	public void removeAll()
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of class names where value = &#63;.
	*
	* @param value the value
	* @return the number of matching class names
	* @throws SystemException if a system exception occurred
	*/
	public int countByValue(java.lang.String value)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of class names.
	*
	* @return the number of class names
	* @throws SystemException if a system exception occurred
	*/
	public int countAll()
		throws com.liferay.portal.kernel.exception.SystemException;
}