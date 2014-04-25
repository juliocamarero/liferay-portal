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

package com.liferay.portlet.trash.service;

import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.portal.kernel.trash.TrashHandlerRegistryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.BackgroundTaskLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.StagingLocalServiceUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portal.util.LayoutTestUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.util.DLAppTestUtil;
import com.liferay.portlet.trash.model.TrashEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;

/**
 * @author Sampsa Sohlman
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class TrashEntryLocalServiceCheckEntriesTest  extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		_userId = TestPropsValues.getUserId();

		List<TrashEntry> list = TrashEntryLocalServiceUtil.getTrashEntries(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		if (list.size() > 0) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Cleaning TrashEntries due previous tests did not" +
					" clean up them properly");
			}

			clearTrashEntries(list);
		}
	}

	@After
	public void tearDown() throws Exception {
		clearTrashEntries(
			TrashEntryLocalServiceUtil.getTrashEntries(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS));

		for (Group group : _groups) {
			List<BackgroundTask> backgroundTasks =
				BackgroundTaskLocalServiceUtil.getBackgroundTasks(
					group.getGroupId(),
					BackgroundTaskConstants.STATUS_IN_PROGRESS);

			int counter = 0;

			while (!backgroundTasks.isEmpty() && (counter < 200)) {
				Thread.sleep(100);
				counter++;
				backgroundTasks =
					BackgroundTaskLocalServiceUtil.getBackgroundTasks(
						group.getGroupId(),
						BackgroundTaskConstants.STATUS_IN_PROGRESS);
			}

			GroupLocalServiceUtil.deleteGroup(group);
		}
	}

	@Test
	public void testGroupMaxAgeChanged() throws Exception {
		Group group = setTrashEntriesMaxAge(createGroups(1).get(0), 2);
		verifyCleanUpAfterTwoDays(group);
	}

	@Test
	public void testMultiGroupTrashCleanUp() throws Exception {
		List<Group> list = createGroups(10);

		int actual = 0;

		for (Group group : list) {
			group = setTrashEntriesMaxAge(createGroups(1).get(0), 2);
			actual = actual + createTrashEntriesForTwoDaysExpiryTest(group);
		}

		TrashEntryLocalServiceUtil.checkEntries();

		Assert.assertEquals(
			actual, TrashEntryLocalServiceUtil.getTrashEntriesCount());
	}

	@Test
	public void testOneGroup() throws Exception {
		Group group = createGroups(1).get(0);
		createFileEntriesAndMoveThemToTrash(group.getGroupId(), 5, 500);
		createFileEntriesAndMoveThemToTrash(group.getGroupId(), 5, 0);
		TrashEntryLocalServiceUtil.checkEntries();
		Assert.assertEquals(
			5, TrashEntryLocalServiceUtil.getTrashEntriesCount());
	}

	@Test
	public void testWithLayoutGroup() throws Exception {
		Group group = setTrashEntriesMaxAge(createGroups(1).get(0), 2);
		verifyCleanUpAfterTwoDays(createLayoutGroup(group.getGroupId()));
	}

	@Test
	public void testWithStaging() throws Exception {
		Group group = setTrashEntriesMaxAge(createGroups(1).get(0), 2);

		StagingLocalServiceUtil.enableLocalStaging(
			_userId, group, false, false, ServiceTestUtil.getServiceContext());

		group = GroupLocalServiceUtil.getGroup(group.getGroupId());

		verifyCleanUpAfterTwoDays(group.getStagingGroup());
	}

	protected void clearTrashEntries(List<TrashEntry> list) throws Exception {
		for (TrashEntry entry : list) {
			TrashHandler trashHandler =
				TrashHandlerRegistryUtil.getTrashHandler(entry.getClassName());
			trashHandler.deleteTrashEntry(entry.getClassPK());
		}
	}

	protected void createFileEntriesAndMoveThemToTrash(
			long groupId, int count, int reduceDays)
		throws Exception {

		createFileEntriesAndMoveThemToTrash(groupId, count, reduceDays, 0);
	}

	protected void createFileEntriesAndMoveThemToTrash(
			long groupId, int count, int reduceDays, int reduceMinutes)
		throws Exception {

		for (int i = 0; i < count; i++) {
			FileEntry fileEntry =
				DLAppTestUtil.addFileEntry(
					groupId, groupId,
					DLFolderConstants.DEFAULT_PARENT_FOLDER_ID);

			DLAppLocalServiceUtil.moveFileEntryToTrash(
				_userId, fileEntry.getFileEntryId());

			if (reduceDays > 0) {
				TrashEntry trashEntry =
					TrashEntryLocalServiceUtil.getEntry(
						DLFileEntry.class.getName(),
						fileEntry.getFileEntryId());

				DateTime dateTime = new DateTime(
					trashEntry.getCreateDate().getTime());

				if (reduceDays > 0) {
					dateTime = dateTime.plusDays(-reduceDays);
				}

				if (reduceMinutes > 0) {
					dateTime = dateTime.plusMinutes(-reduceMinutes);
				}

				trashEntry.setCreateDate(dateTime.toDate());

				TrashEntryLocalServiceUtil.updateTrashEntry(trashEntry);
			}
		}
	}

	protected List<Group> createGroups(int count) throws Exception {
		List<Group> list = new ArrayList<Group>();

		for (int i = 0; i < count; i++) {
			Group group = GroupTestUtil.addGroup();
			group = GroupLocalServiceUtil.getGroup(group.getGroupId());
			list.add(group);
		}

		_groups = list;
		return _groups;
	}

	protected Group createLayoutGroup(long groupId) throws Exception {
		Group group = GroupLocalServiceUtil.getGroup(groupId);

		Layout layout = LayoutTestUtil.addLayout(group);

		layout = LayoutLocalServiceUtil.getLayout(layout.getPlid());

		String name = String.valueOf(layout.getPlid());

		Group layoutGroup = GroupLocalServiceUtil.addGroup(
			_userId, GroupConstants.DEFAULT_PARENT_GROUP_ID,
			Layout.class.getName(), layout.getPlid(),
			GroupConstants.DEFAULT_LIVE_GROUP_ID, name, null, 0, true,
			GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, null, false, true,
			null);

		return GroupLocalServiceUtil.getGroup(layoutGroup.getGroupId());
	}

	protected int createTrashEntriesForTwoDaysExpiryTest(Group group)
		throws Exception {

		createFileEntriesAndMoveThemToTrash(group.getGroupId(), 5, 1);
		createFileEntriesAndMoveThemToTrash(group.getGroupId(), 5, 1, 1430);
		createFileEntriesAndMoveThemToTrash(group.getGroupId(), 5, 2, 1);
		createFileEntriesAndMoveThemToTrash(group.getGroupId(), 5, 3);

		return 10;
	}

	protected Group setTrashEntriesMaxAge(Group group, double days)
		throws Exception {

		UnicodeProperties typeSettingsProperties =
			group.getParentLiveGroupTypeSettingsProperties();

		int trashEntriesMaxAgeCompany = PrefsPropsUtil.getInteger(
			group.getCompanyId(), PropsKeys.TRASH_ENTRIES_MAX_AGE);

		if (days > 0) {
			days *= 1440;
		}
		else {
			days = GetterUtil.getInteger(
				typeSettingsProperties.getProperty("trashEntriesMaxAge"),
				trashEntriesMaxAgeCompany);
		}

		if (days != trashEntriesMaxAgeCompany) {
			typeSettingsProperties.setProperty(
				"trashEntriesMaxAge",
				String.valueOf(GetterUtil.getInteger(days)));
		}
		else {
			typeSettingsProperties.remove("trashEntriesMaxAge");
		}

		group.setTypeSettingsProperties(typeSettingsProperties);

		GroupLocalServiceUtil.updateGroup(group);
		return GroupLocalServiceUtil.getGroup(group.getGroupId());
	}

	protected void verifyCleanUpAfterTwoDays(Group group) throws Exception {

		int actual = createTrashEntriesForTwoDaysExpiryTest(group);

		TrashEntryLocalServiceUtil.checkEntries();

		Assert.assertEquals(
			actual, TrashEntryLocalServiceUtil.getTrashEntriesCount());
	}

	private static Log _log = LogFactoryUtil.getLog(
		TrashEntryLocalServiceCheckEntriesTest.class);

	private List<Group> _groups = Collections.emptyList();
	private long _userId = 0;

}