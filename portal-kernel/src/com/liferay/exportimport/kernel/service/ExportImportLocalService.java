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

package com.liferay.exportimport.kernel.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.exportimport.kernel.lar.MissingReferences;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Transactional;

import java.io.File;
import java.io.InputStream;

import java.util.Map;

/**
 * Provides the local service interface for ExportImport. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Brian Wing Shun Chan
 * @see ExportImportLocalServiceUtil
 * @see com.liferay.portlet.exportimport.service.base.ExportImportLocalServiceBaseImpl
 * @see com.liferay.portlet.exportimport.service.impl.ExportImportLocalServiceImpl
 * @generated
 */
@ProviderType
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
	PortalException.class, SystemException.class})
public interface ExportImportLocalService extends BaseLocalService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link ExportImportLocalServiceUtil} to access the export import local service. Add custom service methods to {@link com.liferay.portlet.exportimport.service.impl.ExportImportLocalServiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public File exportLayoutsAsFile(
		ExportImportConfiguration exportImportConfiguration)
		throws PortalException;

	/**
	* @deprecated As of 7.0.0
	*/
	@java.lang.Deprecated
	public File exportLayoutsAsFile(long userId, long groupId,
		boolean privateLayout,
		Map<java.lang.String, java.lang.String[]> parameterMap)
		throws PortalException;

	public long exportLayoutsAsFileInBackground(long userId,
		ExportImportConfiguration exportImportConfiguration)
		throws PortalException;

	public long exportLayoutsAsFileInBackground(long userId,
		long exportImportConfigurationId) throws PortalException;

	public File exportPortletInfoAsFile(
		ExportImportConfiguration exportImportConfiguration)
		throws PortalException;

	public long exportPortletInfoAsFileInBackground(long userId,
		ExportImportConfiguration exportImportConfiguration)
		throws PortalException;

	public long exportPortletInfoAsFileInBackground(long userId,
		long exportImportConfigurationId) throws PortalException;

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public java.lang.String getOSGiServiceIdentifier();

	public void importLayouts(
		ExportImportConfiguration exportImportConfiguration, File file)
		throws PortalException;

	public void importLayouts(
		ExportImportConfiguration exportImportConfiguration,
		InputStream inputStream) throws PortalException;

	/**
	* @deprecated As of 7.0.0
	*/
	@java.lang.Deprecated
	public void importLayouts(long userId, long groupId, boolean privateLayout,
		Map<java.lang.String, java.lang.String[]> parameterMap, File file)
		throws PortalException;

	public void importLayoutsDataDeletions(
		ExportImportConfiguration exportImportConfiguration, File file)
		throws PortalException;

	public long importLayoutsInBackground(long userId,
		ExportImportConfiguration exportImportConfiguration, File file)
		throws PortalException;

	public long importLayoutsInBackground(long userId,
		ExportImportConfiguration exportImportConfiguration,
		InputStream inputStream) throws PortalException;

	public long importLayoutsInBackground(long userId,
		long exportImportConfigurationId, File file) throws PortalException;

	public long importLayoutsInBackground(long userId,
		long exportImportConfigurationId, InputStream inputStream)
		throws PortalException;

	public void importPortletDataDeletions(
		ExportImportConfiguration exportImportConfiguration, File file)
		throws PortalException;

	public void importPortletInfo(
		ExportImportConfiguration exportImportConfiguration, File file)
		throws PortalException;

	public void importPortletInfo(
		ExportImportConfiguration exportImportConfiguration,
		InputStream inputStream) throws PortalException;

	public long importPortletInfoInBackground(long userId,
		ExportImportConfiguration exportImportConfiguration, File file)
		throws PortalException;

	public long importPortletInfoInBackground(long userId,
		ExportImportConfiguration exportImportConfiguration,
		InputStream inputStream) throws PortalException;

	public long importPortletInfoInBackground(long userId,
		long exportImportConfigurationId, File file) throws PortalException;

	public long importPortletInfoInBackground(long userId,
		long exportImportConfigurationId, InputStream inputStream)
		throws PortalException;

	public MissingReferences validateImportLayoutsFile(
		ExportImportConfiguration exportImportConfiguration, File file)
		throws PortalException;

	public MissingReferences validateImportLayoutsFile(
		ExportImportConfiguration exportImportConfiguration,
		InputStream inputStream) throws PortalException;

	public MissingReferences validateImportPortletInfo(
		ExportImportConfiguration exportImportConfiguration, File file)
		throws PortalException;

	public MissingReferences validateImportPortletInfo(
		ExportImportConfiguration exportImportConfiguration,
		InputStream inputStream) throws PortalException;
}