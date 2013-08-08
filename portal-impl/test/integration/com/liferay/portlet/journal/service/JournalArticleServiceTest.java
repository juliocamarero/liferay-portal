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

package com.liferay.portlet.journal.service;

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalFolderConstants;
import com.liferay.portlet.journal.util.JournalTestUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Juan Fernández
 */
@ExecutionTestListeners(
	listeners = {
		EnvironmentExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class JournalArticleServiceTest {

	@Before
	public void setUp() throws Exception {
		FinderCacheUtil.clearCache();

		_group = GroupTestUtil.addGroup();

		_addArticle();
	}

	@After
	public void tearDown() throws Exception {
		GroupLocalServiceUtil.deleteGroup(_group);

		JournalArticleLocalServiceUtil.deleteArticle(
			_group.getGroupId(), _article.getArticleId(), new ServiceContext());
	}

	@Test
	public void testAddArticle() throws Exception {
		Assert.assertTrue(_article.isApproved());
		Assert.assertEquals(1.0, _article.getVersion(), 0);
	}

	@Test
	public void testArticleHasAprovedVersion() throws Exception {
		testFetchLatestArticle(false, WorkflowConstants.STATUS_EXPIRED);
	}

	@Test
	public void testFetchLatestArticleByApprovedStatusWhenArticleIsExpired()
		throws Exception {

		_updateArticle();

		_expireArticle();

		_fetchLatestArticleByStatus(WorkflowConstants.STATUS_APPROVED);

		Assert.assertNull(_latestArticle);
	}

	@Test
	public void testFetchLatestArticleByApprovedStatusWithApprovedVersion()
		throws Exception {

		_updateArticle();

		_fetchLatestArticleByStatus(WorkflowConstants.STATUS_APPROVED);

		Assert.assertTrue(_latestArticle.isApproved());
		Assert.assertTrue(1.1 == _latestArticle.getVersion());
	}

	@Test
	public void testFetchLatestArticleByDraftStatusWhenNoDraftVersion()
		throws Exception {

		_updateArticle();

		_fetchLatestArticleByStatus(WorkflowConstants.STATUS_DRAFT);

		Assert.assertNull(_latestArticle);
	}

	@Test
	public void testFetchLatestArticleExpiredWithStatusAny() throws Exception {
		testFetchLatestArticle(true, WorkflowConstants.STATUS_ANY);
	}

	@Test
	public void testFetchLatestArticleExpiredWithStatusApproved()
		throws Exception {

		testFetchLatestArticle(true, WorkflowConstants.STATUS_APPROVED);
	}

	@Test
	public void testFetchLatestArticleExpiredWithStatusExpired()
		throws Exception {

		testFetchLatestArticle(true, WorkflowConstants.STATUS_EXPIRED);
	}

	@Test
	public void testFetchLatestArticleNotExpiredWithStatusAny()
		throws Exception {

		testFetchLatestArticle(false, WorkflowConstants.STATUS_ANY);
	}

	@Test
	public void testFetchLatestArticleNotExpiredWithStatusApproved()
		throws Exception {

		testFetchLatestArticle(false, WorkflowConstants.STATUS_APPROVED);
	}

	@Test
	public void testFetchLatestArticleNotExpiredWithStatusExpired()
		throws Exception {

		testFetchLatestArticle(false, WorkflowConstants.STATUS_EXPIRED);
	}

	@Test
	public void testUpdateArticle() throws Exception {
		_updateArticle();

		Assert.assertTrue(_article.isApproved());
		Assert.assertEquals(1.1, _article.getVersion(), 0);
	}

	protected void testFetchLatestArticle(
			boolean expireLatestVersion, int status)
		throws Exception {

		_updateArticle();

		if (expireLatestVersion) {
			_expireArticleLatestVersion();

			Assert.assertTrue(_article.isExpired());
			Assert.assertEquals(1.1, _article.getVersion(), 0);
		}

		_fetchLatestArticle(status);

		if (expireLatestVersion) {
			if (status == WorkflowConstants.STATUS_APPROVED) {
				Assert.assertNotNull(_latestArticle);
				Assert.assertTrue(_latestArticle.isApproved());
				Assert.assertEquals(
					"Version 1",
					_latestArticle.getTitle(LocaleUtil.getDefault()));
				Assert.assertEquals(1.0, _latestArticle.getVersion(), 0);
			}
			else if ((status == WorkflowConstants.STATUS_ANY) ||
					 (status == WorkflowConstants.STATUS_EXPIRED)) {

				Assert.assertNotNull(_latestArticle);
				Assert.assertTrue(_latestArticle.isExpired());
				Assert.assertEquals(
					"Version 2",
					_latestArticle.getTitle(LocaleUtil.getDefault()));
				Assert.assertEquals(1.1, _latestArticle.getVersion(), 0);
			}
		}
		else {
			if ((status == WorkflowConstants.STATUS_ANY) ||
				(status == WorkflowConstants.STATUS_APPROVED)) {

				Assert.assertNotNull(_latestArticle);
				Assert.assertTrue(_latestArticle.isApproved());
				Assert.assertEquals(
					"Version 2",
					_latestArticle.getTitle(LocaleUtil.getDefault()));
				Assert.assertEquals(1.1, _latestArticle.getVersion(), 0);
			}
			else {
				Assert.assertNull(_latestArticle);
			}
		}
	}

	private void  _addArticle() throws Exception {
		_article = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, "Version 1",
			"This is a test article.");
	}

	private void _expireArticle() throws Exception {
		JournalArticleLocalServiceUtil.expireArticle(
			_article.getUserId(), _article.getGroupId(),
			_article.getArticleId(), null,
			ServiceTestUtil.getServiceContext(_group.getGroupId())
		);
	}

	private void _expireArticleLatestVersion() throws Exception {
		_article = JournalArticleLocalServiceUtil.expireArticle(
			_article.getUserId(), _article.getGroupId(),
			_article.getArticleId(), 1.1, null,
			ServiceTestUtil.getServiceContext(_group.getGroupId()));
	}

	private void _fetchLatestArticle(int status) throws Exception {
		_latestArticle = JournalArticleLocalServiceUtil.fetchLatestArticle(
			_article.getResourcePrimKey(), status,
			status == WorkflowConstants.STATUS_APPROVED);
	}

	private void _fetchLatestArticleByStatus(int status) throws Exception {
		_latestArticle = JournalArticleLocalServiceUtil.fetchLatestArticle(
			_group.getGroupId(), _article.getArticleId(), status);
	}

	private void _updateArticle() throws Exception {
		_article = JournalTestUtil.updateArticle(
			_article, "Version 2", _article.getContent());
	}

	private JournalArticle _article;
	private Group _group;
	private JournalArticle _latestArticle;

}