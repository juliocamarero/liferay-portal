/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.documentlibrary.expando;

import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.Repository;
import com.liferay.portal.repository.liferayrepository.LiferayRepository;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.documentlibrary.util.DLAppTestUtil;
import com.liferay.portlet.documentlibrary.util.RepositoryTestUtil;
import com.liferay.portlet.expando.BasePortletExpandoTestCase;

import java.io.File;

import org.junit.runner.RunWith;

/**
 * @author Roberto Díaz
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class DLFileEntryExpandoTest extends BasePortletExpandoTestCase {

	@Override
	public int getExpectedValuesCountAdded() {
		return 3;
	}

	@Override
	protected ClassedModel addModel() throws Exception {
		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			TestPropsValues.getGroupId());

		long classNameId = PortalUtil.getClassNameId(LiferayRepository.class);

		Repository repository = RepositoryTestUtil.addRepository(
			TestPropsValues.getUserId(), classNameId, serviceContext, false);

		long repositoryId = repository.getRepositoryId();

		Folder folder = DLAppTestUtil.addFolder(
			TestPropsValues.getUserId(), repositoryId, serviceContext);

		long folderId = folder.getFolderId();

		File file = _getFile();

		serviceContext.setExpandoBridgeAttributes(
			getExpandoMap(SEND_DEFAULT_EXPANDO_VALUES));

		return DLAppServiceUtil.addFileEntry(
			repositoryId, folderId, file.getName(), StringPool.BLANK,
			file.getName(), StringPool.BLANK, StringPool.BLANK, file,
			serviceContext);
	}

	@Override
	protected void deleteModel() throws Exception {
		DLAppServiceUtil.deleteFileEntry((Long)baseModel.getPrimaryKeyObj());
	}

	@Override
	protected void deleteModelVersion(ClassedModel modelVersion)
		throws Exception {

		FileEntry fileEntry = (FileEntry)baseModel;
		FileVersion fileVersion = (FileVersion)modelVersion;

		DLAppServiceUtil.deleteFileVersion(
			fileEntry.getFileEntryId(), fileVersion.getVersion());
	}

	@Override
	protected String getClassName() {
		return DLFileEntry.class.getName();
	}

	@Override
	protected ClassedModel getModelVersion() throws Exception {
		return ((FileEntry)baseModel).getLatestFileVersion();
	}

	@Override
	protected ClassedModel updateBaseModel(int modifyExpandoValue)
		throws Exception {

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			TestPropsValues.getGroupId());

		serviceContext.setExpandoBridgeAttributes(
			getExpandoMap(modifyExpandoValue));

		FileEntry fileEntry = (FileEntry)baseModel;

		return DLAppTestUtil.updateFileEntry(
			TestPropsValues.getGroupId(), fileEntry.getFileEntryId(),
			fileEntry.getTitle(), fileEntry.getTitle(), serviceContext);
	}

	private File _getFile() throws Exception {
		byte[] fileBytes = FileUtil.getBytes(
			getClass().getResourceAsStream("dependencies/OSX_Test.docx"));

		File file = null;

		if ((fileBytes != null) && (fileBytes.length > 0)) {
			file = FileUtil.createTempFile(fileBytes);
		}

		return file;
	}

}