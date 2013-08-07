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

package com.liferay.portal.search;

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.Sync;
import com.liferay.portal.test.SynchronousDestinationExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.util.DDMStructureTestUtil;
import com.liferay.portlet.dynamicdatamapping.util.DDMTemplateTestUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalFolder;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.util.JournalArticleIndexer;
import com.liferay.portlet.journal.util.JournalTestUtil;

import java.util.Locale;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eudaldo Alonso
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		SynchronousDestinationExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Sync
public class JournalIndexerTest {

	@Before
	public void setUp() throws Exception {
		FinderCacheUtil.clearCache();

		group = GroupTestUtil.addGroup();
	}

	@After
	public void tearDown() throws Exception {
		GroupLocalServiceUtil.deleteGroup(group);
	}

	@Test
	public void testAddArticle() throws Exception {
		Assert.assertTrue(false);
	}

	public void testCopyArticle() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testDeleteArticle() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testDeleteArticles() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testExpireArticle() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testMoveArticle() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testMoveArticleFromTrash() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testMoveArticleToTrash() throws Exception {
		Assert.assertTrue(false);
	}

	public void testRemoveArticleLocale() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testRestoreArticleFromTrash() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testUpdateArticle() throws Exception {
		Assert.assertTrue(false);
	}

	public void testUpdateArticleTranslation() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testUpdateContentWithoutStructure() throws Exception {
		SearchContext searchContext = ServiceTestUtil.getSearchContext(
			group.getGroupId());

		JournalFolder folder = JournalTestUtil.addFolder(
			group.getGroupId(), ServiceTestUtil.randomString());

		int initialBaseModelsSearchCount = searchCount(
			group.getGroupId(), searchContext);

		String content = "Liferay Architectural Approach";

		JournalArticle article = JournalTestUtil.addArticleWithWorkflow(
			group.getGroupId(), folder.getFolderId(), "title", content, true);

		searchContext.setKeywords("Architectural");

		Assert.assertEquals(
			initialBaseModelsSearchCount + 1,
			searchCount(group.getGroupId(), searchContext));

		content = JournalTestUtil.createLocalizedContent(
			"Architectural Approach", Locale.getDefault());

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), article.getArticleId(), article.getVersion(),
			content);

		Assert.assertEquals(
			initialBaseModelsSearchCount + 1,
			searchCount(group.getGroupId(), searchContext));

		searchContext.setKeywords("Liferay");

		Assert.assertEquals(
			initialBaseModelsSearchCount,
			searchCount(group.getGroupId(), searchContext));
	}

	@Test
	public void testUpdateContentWithStructure() throws Exception {
		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			group.getGroupId());

		SearchContext searchContext = ServiceTestUtil.getSearchContext(
			group.getGroupId());

		JournalFolder folder = JournalTestUtil.addFolder(
			group.getGroupId(), ServiceTestUtil.randomString());

		int initialBaseModelsSearchCount = searchCount(
			group.getGroupId(), searchContext);

		JournalArticle article = addJournalWithDDMStructure(
			folder.getFolderId(), "Liferay Architectural Approach",
			serviceContext);

		searchContext.setKeywords("Architectural");

		Assert.assertEquals(
			initialBaseModelsSearchCount + 1,
			searchCount(group.getGroupId(), searchContext));

		String content = DDMStructureTestUtil.getSampleStructuredContent(
			"name", "Architectural Approach");

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), article.getArticleId(), article.getVersion(),
			content);

		Assert.assertEquals(
			initialBaseModelsSearchCount + 1,
			searchCount(group.getGroupId(), searchContext));

		searchContext.setKeywords("Liferay");

		Assert.assertEquals(
			initialBaseModelsSearchCount,
			searchCount(group.getGroupId(), searchContext));
	}

	@Test
	public void testUpdateStatus() throws Exception {
		Assert.assertTrue(false);
	}

	protected JournalArticle addJournalWithDDMStructure(
			long folderId, String keywords, ServiceContext serviceContext)
		throws Exception {

		String xsd = DDMStructureTestUtil.getSampleStructureXSD("name");

		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			serviceContext.getScopeGroupId(), JournalArticle.class.getName(),
			xsd);

		DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
			serviceContext.getScopeGroupId(), ddmStructure.getStructureId());

		String content = DDMStructureTestUtil.getSampleStructuredContent(
			"name", keywords);

		return JournalTestUtil.addArticleWithXMLContent(
			folderId, content, ddmStructure.getStructureKey(),
			ddmTemplate.getTemplateKey(), serviceContext);
	}

	protected int searchCount(long groupId, SearchContext searchContext)
		throws Exception {

		Indexer indexer = IndexerRegistryUtil.getIndexer(
			JournalArticleIndexer.class);

		searchContext.setAttribute("status", WorkflowConstants.STATUS_APPROVED);
		searchContext.setGroupIds(new long[] {groupId});

		Hits results = indexer.search(searchContext);

		return results.getLength();
	}

	protected Group group;

}