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

package com.liferay.portlet.bookmarks.service;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.test.DeleteAfterTestRun;
import com.liferay.portal.test.listeners.MainServletExecutionTestListener;
import com.liferay.portal.test.runners.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.util.test.GroupTestUtil;
import com.liferay.portal.util.test.ServiceContextTestUtil;
import com.liferay.portal.util.test.TestPropsValues;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.model.BookmarksFolderConstants;
import com.liferay.portlet.bookmarks.util.test.BookmarksTestUtil;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.testng.Assert;

/**
 * @author Shinn Lok
 */
@ExecutionTestListeners(listeners = {MainServletExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class BookmarksEntryLocalServiceTreeTest {

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testBookmarksEntryTreePathWhenMovingSubfolderWithEntry()
		throws Exception {

		BookmarksFolder folder = BookmarksTestUtil.addFolder(
			_group.getGroupId(),
			BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID, "Folder 1");

		BookmarksFolder subFolder = BookmarksTestUtil.addFolder(
			_group.getGroupId(), folder.getFolderId(), "Folder 1.1");

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		BookmarksEntry entry = BookmarksTestUtil.addEntry(
			subFolder.getFolderId(), true, serviceContext);

		BookmarksFolderLocalServiceUtil.moveFolder(
			subFolder.getFolderId(),
			BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		BookmarksEntry bookmarksEntry =
			BookmarksEntryLocalServiceUtil.getBookmarksEntry(
				entry.getEntryId());

		Assert.assertEquals(
			bookmarksEntry.buildTreePath(), bookmarksEntry.getTreePath());
	}

	@Test
	public void testBookmarksFolderTreePathWhenMovingFolderWithSubfolder()
		throws Exception {

		List<BookmarksFolder> bookmarksFolders =
			new ArrayList<BookmarksFolder>();

		BookmarksFolder folder = BookmarksTestUtil.addFolder(
			_group.getGroupId(),
			BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID, "Folder 1");

		bookmarksFolders.add(folder);

		BookmarksFolder subFolder = BookmarksTestUtil.addFolder(
			_group.getGroupId(), folder.getFolderId(), "Folder 1.1");

		bookmarksFolders.add(subFolder);

		BookmarksFolder subsubFolder = BookmarksTestUtil.addFolder(
			_group.getGroupId(), subFolder.getFolderId(), "Folder 1.1.1");

		bookmarksFolders.add(subsubFolder);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		BookmarksFolderLocalServiceUtil.moveFolder(
			subFolder.getFolderId(),
			BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		for (BookmarksFolder curbookmarksFolder : bookmarksFolders) {
			BookmarksFolder bookmarksFolder =
				BookmarksFolderLocalServiceUtil.getBookmarksFolder(
					curbookmarksFolder.getFolderId());

			Assert.assertEquals(
				bookmarksFolder.buildTreePath(), bookmarksFolder.getTreePath());
		}
	}

	@Test
	public void testRebuildTree() throws Exception {
		List<BookmarksEntry> entries = createTree();

		for (BookmarksEntry entry : entries) {
			entry.setTreePath(null);

			BookmarksEntryLocalServiceUtil.updateBookmarksEntry(entry);
		}

		BookmarksEntryLocalServiceUtil.rebuildTree(
			TestPropsValues.getCompanyId());

		for (BookmarksEntry entry : entries) {
			entry = BookmarksEntryLocalServiceUtil.getEntry(entry.getEntryId());

			Assert.assertEquals(entry.buildTreePath(), entry.getTreePath());
		}
	}

	protected List<BookmarksEntry> createTree() throws Exception {
		List<BookmarksEntry> entries = new ArrayList<BookmarksEntry>();

		BookmarksEntry entryA = BookmarksTestUtil.addEntry(
			_group.getGroupId(), true);

		entries.add(entryA);

		BookmarksFolder folder = BookmarksTestUtil.addFolder(
			_group.getGroupId(), "Folder A");

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		BookmarksEntry entryAA = BookmarksTestUtil.addEntry(
			folder.getFolderId(), true, serviceContext);

		entries.add(entryAA);

		return entries;
	}

	@DeleteAfterTestRun
	private Group _group;

}