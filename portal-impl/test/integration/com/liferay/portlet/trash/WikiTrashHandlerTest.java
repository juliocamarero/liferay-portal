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

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.portal.kernel.trash.TrashHandlerRegistryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.trash.model.TrashEntry;
import com.liferay.portlet.trash.model.TrashEntryList;
import com.liferay.portlet.trash.service.TrashEntryLocalServiceUtil;
import com.liferay.portlet.trash.service.TrashEntryServiceUtil;
import com.liferay.portlet.wiki.model.WikiNode;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.service.WikiNodeLocalServiceUtil;
import com.liferay.portlet.wiki.service.WikiNodeServiceUtil;
import com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil;
import com.liferay.portlet.wiki.service.WikiPageServiceUtil;

import org.junit.Assert;
import org.junit.Before;
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
public class WikiTrashHandlerTest {

	@Before
	public void setUp() {
		FinderCacheUtil.clearCache();
	}

	@Test
	@Transactional
	public void testTrashAndDelete() throws Exception {
		testWikiPageTrash(true);
		testWikiNodeTrash(true);
	}

	@Test
	@Transactional
	public void testTrashAndRestoreApproved() throws Exception {
		testWikiPageTrash(false);
		testWikiNodeTrash(false);
	}

	protected WikiNode addWikiNode(ServiceContext serviceContext)
		throws Exception {

		serviceContext.setWorkflowAction(WorkflowConstants.ACTION_SAVE_DRAFT);

		String name = "Wiki Node Test";
		String description = "Description";

		WikiNode wikiNode = WikiNodeLocalServiceUtil.addNode(
			getUserId(), name, description, serviceContext);

		return wikiNode;
	}

	protected WikiPage addWikiPage(long nodeId, ServiceContext serviceContext)
		throws Exception {

		serviceContext.setWorkflowAction(WorkflowConstants.ACTION_SAVE_DRAFT);

		String title = "Wiki Page Test";
		String content = "Content";
		String sumary = "Sumary";

		WikiPage wikiPage = WikiPageLocalServiceUtil.addPage(
			getUserId(), nodeId, title, content, sumary, true, serviceContext);

		WikiPageLocalServiceUtil.updateStatus(
			getUserId(), wikiPage.getResourcePrimKey(),
			WorkflowConstants.STATUS_APPROVED, serviceContext);

		return wikiPage;
	}

	protected AssetEntry fetchAssetEntry(long blogsEntryId) throws Exception {
		return AssetEntryLocalServiceUtil.fetchEntry(
			WikiPage.class.getName(), blogsEntryId);
	}

	protected int getTrashEntriesCount(long groupId) throws Exception {
		TrashEntryList trashEntryList = TrashEntryServiceUtil.getEntries(
			groupId);

		return trashEntryList.getCount();
	}

	protected long getUserId() {
		return GetterUtil.getLong(PrincipalThreadLocal.getName());
	}

	protected int getWikiPagesNotInTrashCount(long nodeId) throws Exception {
		return WikiPageLocalServiceUtil.getPagesCount(nodeId);
	}

	protected boolean isAssetEntryVisible(long wikiPageId) throws Exception {
		AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(
			WikiPage.class.getName(), wikiPageId);

		return assetEntry.isVisible();
	}

	protected int searchTrashEntriesCount(
		String keywords, ServiceContext serviceContext)
		throws Exception {

		Thread.sleep(1000 * TestPropsValues.JUNIT_DELAY_FACTOR);

		Hits results = TrashEntryServiceUtil.search(
			serviceContext.getCompanyId(), serviceContext.getScopeGroupId(),
			serviceContext.getUserId(), keywords, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);

		return results.getLength();
	}

	protected int searchWikiPagesCount() throws Exception {
		Thread.sleep(1000 * TestPropsValues.JUNIT_DELAY_FACTOR);

		Indexer indexer = IndexerRegistryUtil.getIndexer(WikiPage.class);

		SearchContext searchContext = ServiceTestUtil.getSearchContext();

		Hits results = indexer.search(searchContext);

		return results.getLength();
	}

	protected void testWikiNodeTrash(boolean delete) throws Exception {

		Group group = ServiceTestUtil.addGroup(
			"WikiPageTrashHandlerTest#testGroup");

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext();

		serviceContext.setScopeGroupId(group.getGroupId());

		WikiNode wikiNode = addWikiNode(serviceContext);

		long nodeId = wikiNode.getNodeId();

		int initialWikiPagesCount = getWikiPagesNotInTrashCount(nodeId);
		int initialWikiPagesSearchCount = searchWikiPagesCount();
		int initialTrashEntriesSearchCount = searchTrashEntriesCount(
			"Title", serviceContext);

		WikiPage wikiPage = addWikiPage(nodeId, serviceContext);

		int oldStatus = wikiPage.getStatus();

		WikiNodeServiceUtil.moveEntryToTrash(wikiNode.getNodeId());

		TrashEntry trashEntry = TrashEntryLocalServiceUtil.getEntry(
			WikiNode.class.getName(), wikiNode.getNodeId());

		Assert.assertEquals(wikiNode.getNodeId(), trashEntry.getClassPK());
		Assert.assertEquals(
			WorkflowConstants.STATUS_IN_TRASH, wikiNode.getStatus());
		Assert.assertEquals(
			initialWikiPagesCount, getWikiPagesNotInTrashCount(nodeId));
		Assert.assertFalse(isAssetEntryVisible(wikiPage.getResourcePrimKey()));
		Assert.assertEquals(
			initialWikiPagesSearchCount, searchWikiPagesCount());

		TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(
			wikiNode.getModelClassName());

		if (delete) {
			trashHandler.deleteTrashEntry(wikiNode.getNodeId());

			Assert.assertEquals(
				initialWikiPagesCount, getWikiPagesNotInTrashCount(nodeId));
			Assert.assertEquals(
				initialWikiPagesSearchCount, searchWikiPagesCount());
			Assert.assertEquals(
				initialTrashEntriesSearchCount,
				searchTrashEntriesCount("Title", serviceContext));
		}
		else {
			trashHandler.restoreTrashEntry(wikiNode.getNodeId());

			wikiNode = WikiNodeServiceUtil.getNode(wikiNode.getNodeId());

			Assert.assertEquals(oldStatus, wikiNode.getStatus());
			Assert.assertEquals(
				initialWikiPagesCount + 1, getWikiPagesNotInTrashCount(nodeId));

			Assert.assertTrue(isAssetEntryVisible(
				wikiPage.getResourcePrimKey()));
			Assert.assertEquals(
				initialWikiPagesSearchCount + 1, searchWikiPagesCount());

			Assert.assertEquals(
				initialTrashEntriesSearchCount,
				searchTrashEntriesCount("Title", serviceContext));

			trashHandler.deleteTrashEntry(wikiNode.getNodeId());
		}
	}

	protected void testWikiPageTrash(boolean delete) throws Exception {
		Group group = ServiceTestUtil.addGroup(
			"WikiPageTrashHandlerTest#testGroup");

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext();

		serviceContext.setScopeGroupId(group.getGroupId());

		WikiNode wikiNode = addWikiNode(serviceContext);

		long nodeId = wikiNode.getNodeId();

		int initialWikiPagesCount = getWikiPagesNotInTrashCount(nodeId);
		int initialTrashEntriesCount = getTrashEntriesCount(group.getGroupId());
		int initialWikiPagesSearchCount = searchWikiPagesCount();
		int initialTrashEntriesSearchCount = searchTrashEntriesCount(
			"Title", serviceContext);

		WikiPage wikiPage = addWikiPage(nodeId, serviceContext);

		int oldStatus = wikiPage.getStatus();

		Assert.assertEquals(
			initialWikiPagesCount + 1, getWikiPagesNotInTrashCount(nodeId));
		Assert.assertEquals(
			initialTrashEntriesCount, getTrashEntriesCount(group.getGroupId()));

		Assert.assertTrue(isAssetEntryVisible(wikiPage.getResourcePrimKey()));

		Assert.assertEquals(
			initialTrashEntriesSearchCount,
			searchTrashEntriesCount("Title", serviceContext));

		WikiPageServiceUtil.moveEntryToTrash(
			wikiPage.getNodeId(), wikiPage.getTitle());

		TrashEntry trashEntry = TrashEntryLocalServiceUtil.getEntry(
			WikiPage.class.getName(), wikiPage.getPageId());

		Assert.assertEquals(wikiPage.getPageId(), trashEntry.getClassPK());
		Assert.assertEquals(
			WorkflowConstants.STATUS_IN_TRASH, wikiPage.getStatus());
		Assert.assertEquals(
			initialWikiPagesCount, getWikiPagesNotInTrashCount(nodeId));
		Assert.assertEquals(
			initialTrashEntriesCount + 1, getTrashEntriesCount(nodeId));
		Assert.assertFalse(isAssetEntryVisible(wikiPage.getResourcePrimKey()));
		Assert.assertEquals(
			initialWikiPagesSearchCount, searchWikiPagesCount());
		Assert.assertEquals(
			initialTrashEntriesSearchCount + 1,
			searchTrashEntriesCount("Title", serviceContext));

		TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(
			wikiPage.getModelClassName());

		if (delete) {
			trashHandler.deleteTrashEntry(wikiPage.getPageId());

			Assert.assertEquals(
				initialWikiPagesCount, getWikiPagesNotInTrashCount(nodeId));
			Assert.assertNull(fetchAssetEntry(wikiPage.getPageId()));
			Assert.assertEquals(
				initialWikiPagesSearchCount, searchWikiPagesCount());
			Assert.assertEquals(
				initialTrashEntriesSearchCount,
				searchTrashEntriesCount("Title", serviceContext));
		}
		else {
			trashHandler.restoreTrashEntry(wikiPage.getPageId());

			wikiPage = WikiPageServiceUtil.getPage(
				wikiPage.getNodeId(), wikiPage.getTitle());

			Assert.assertEquals(oldStatus, wikiPage.getStatus());
			Assert.assertEquals(
				initialWikiPagesCount + 1, getWikiPagesNotInTrashCount(nodeId));

			Assert.assertTrue(isAssetEntryVisible(
				wikiPage.getResourcePrimKey()));
			Assert.assertEquals(
				initialWikiPagesSearchCount + 1, searchWikiPagesCount());

			Assert.assertEquals(
				initialTrashEntriesSearchCount,
				searchTrashEntriesCount("Title", serviceContext));

			trashHandler.deleteTrashEntry(wikiPage.getPageId());
		}
	}

}