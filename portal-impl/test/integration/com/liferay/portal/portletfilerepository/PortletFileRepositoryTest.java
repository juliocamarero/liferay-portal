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

package com.liferay.portal.portletfilerepository;

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eudaldo Alonso
 */
@ExecutionTestListeners(
	listeners = {
		EnvironmentExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class PortletFileRepositoryTest {

	@Test
	@Transactional
	public void testBasic() throws Exception {
		Group group = ServiceTestUtil.addGroup();

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext();

		serviceContext.setScopeGroupId(group.getGroupId());

		String portletId = ServiceTestUtil.randomString();

		long repositoryId = PortletFileRepositoryUtil.getPortletRepository(
			group.getGroupId(), portletId, serviceContext);

		long folderId = PortletFileRepositoryUtil.getFolder(
			TestPropsValues.getUserId(), repositoryId,
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			ServiceTestUtil.randomString(), serviceContext);

		int initialFileEntriesCount =
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				group.getGroupId(), folderId);

		InputStream inputStream = new UnsyncByteArrayInputStream(
			_TEST_CONTENT.getBytes());

		String fileName = ServiceTestUtil.randomString() + ".txt";

		FileEntry fileEntry = PortletFileRepositoryUtil.addPortletFileEntry(
			group.getGroupId(), TestPropsValues.getUserId(), portletId,
			folderId, inputStream, fileName);

		Assert.assertEquals(
			initialFileEntriesCount + 1,
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				group.getGroupId(), folderId,
				WorkflowConstants.STATUS_APPROVED));

		PortletFileRepositoryUtil.movePortletFileEntryToTrash(
			TestPropsValues.getUserId(), fileEntry.getFileEntryId());

		Assert.assertEquals(
			initialFileEntriesCount,
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				group.getGroupId(), folderId,
				WorkflowConstants.STATUS_APPROVED));

		PortletFileRepositoryUtil.movePortletFileEntryFromTrash(
			TestPropsValues.getUserId(), fileEntry.getFileEntryId());

		Assert.assertEquals(
			initialFileEntriesCount + 1,
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				group.getGroupId(), folderId,
				WorkflowConstants.STATUS_APPROVED));

		PortletFileRepositoryUtil.deletePortletFileEntry(
			group.getGroupId(), folderId, fileName);

		Assert.assertEquals(
			initialFileEntriesCount,
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				group.getGroupId(), folderId,
				WorkflowConstants.STATUS_APPROVED));
	}

	private static final String _TEST_CONTENT =
		"LIFERAY\nEnterprise. Open Source. For Life.";

}