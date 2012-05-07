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

package com.liferay.portlet.trash;

import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portlet.documentlibrary.model.DLFileEntryConstants;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.documentlibrary.util.DLIndexer;
import com.liferay.portlet.trash.service.TrashEntryServiceUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Alexander Chow
 */
@ExecutionTestListeners(listeners = {EnvironmentExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class DLFileEntryTrashHandlerTest extends BaseDLTrashHandlerTest {

	@Test
	public void testTrashAndDelete() throws Exception {
		testTrashFileEntry(true, false, false);
	}

	@Test
	public void testTrashAndDeleteVersioned() throws Exception {
		testTrashFileEntry(true, true, false);
	}

	@Test
	public void testTrashAndDeleteVersionedAndCheckedOut() throws Exception {
		testTrashFileEntry(true, true, true);
	}

	@Test
	public void testTrashAndRestore() throws Exception {
		testTrashFileEntry(false, false, false);
	}

	@Test
	public void testTrashAndRestoreVersioned() throws Exception {
		testTrashFileEntry(false, true, false);
	}

	@Test
	public void testTrashAndRestoreVersionedAndCheckedOut() throws Exception {
		testTrashFileEntry(false, true, true);
	}

	protected int searchFileEntriesCount() throws Exception {
		Indexer indexer = IndexerRegistryUtil.getIndexer(DLIndexer.class);

		SearchContext searchContext = ServiceTestUtil.getSearchContext();

		Hits results = indexer.search(searchContext);

		return results.getLength();
	}

	protected void testTrashFileEntry(
			boolean delete, boolean versioned, boolean leaveCheckedOut)
		throws Exception {

		int initialFileEntriesCount = getNotInTrashCount();
		int initialTrashEntriesCount = getTrashEntriesCount();
		int initialSearchFileEntriesCount = searchFileEntriesCount();

		FileEntry fileEntry = addFileEntry(false, "Test Basic.txt");

		long fileEntryId = fileEntry.getFileEntryId();

		if (versioned) {
			updateFileEntry(fileEntryId, null, "Test Basic 2.txt");
		}

		if (leaveCheckedOut) {
			DLAppServiceUtil.checkOutFileEntry(
				fileEntryId, new ServiceContext());
		}

		Assert.assertEquals(initialFileEntriesCount + 1, getNotInTrashCount());
		Assert.assertEquals(initialTrashEntriesCount, getTrashEntriesCount());
		Assert.assertTrue(
			isAssetEntryVisible(
				DLFileEntryConstants.getClassName(), fileEntryId));
		Assert.assertEquals(
			initialSearchFileEntriesCount + 1, searchFileEntriesCount());

		DLAppServiceUtil.moveFileEntryToTrash(fileEntryId);

		Assert.assertEquals(initialFileEntriesCount, getNotInTrashCount());
		Assert.assertEquals(
			initialTrashEntriesCount + 1, getTrashEntriesCount());
		Assert.assertFalse(
			isAssetEntryVisible(
				DLFileEntryConstants.getClassName(), fileEntryId));
		Assert.assertEquals(
			initialSearchFileEntriesCount, searchFileEntriesCount());

		if (delete) {
			TrashEntryServiceUtil.deleteEntries(folder.getGroupId());

			Assert.assertEquals(initialFileEntriesCount, getNotInTrashCount());
			Assert.assertNull(
				fetchAssetEntry(
					DLFileEntryConstants.getClassName(), fileEntryId));
			Assert.assertEquals(
				initialSearchFileEntriesCount, searchFileEntriesCount());
		}
		else {
			DLAppServiceUtil.restoreFileEntryFromTrash(fileEntryId);

			Assert.assertEquals(
				initialFileEntriesCount + 1, getNotInTrashCount());
			Assert.assertTrue(
				isAssetEntryVisible(
					DLFileEntryConstants.getClassName(), fileEntryId));
			Assert.assertEquals(
				initialSearchFileEntriesCount + 1, searchFileEntriesCount());
		}

		Assert.assertEquals(initialTrashEntriesCount, getTrashEntriesCount());
	}

}