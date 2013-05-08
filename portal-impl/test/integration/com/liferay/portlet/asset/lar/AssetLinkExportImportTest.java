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

package com.liferay.portlet.asset.lar;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.lar.BasePortletExportImportTestCase;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalCallbackAwareExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portal.util.LayoutTestUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetLink;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetLinkLocalServiceUtil;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.blogs.util.BlogsTestUtil;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.service.BookmarksEntryLocalServiceUtil;
import com.liferay.portlet.bookmarks.util.BookmarksTestUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleResource;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.persistence.JournalArticleResourceUtil;
import com.liferay.portlet.journal.util.JournalTestUtil;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Víctor Miranda
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		TransactionalCallbackAwareExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class AssetLinkExportImportTest extends BasePortletExportImportTestCase {

	@Test
	public void testExportImportBlogWithLinks() throws Exception {
		long groupId = group.getGroupId();

		BlogsEntry blogsEntry = BlogsTestUtil.addEntry(
			TestPropsValues.getUserId(), groupId,
			ServiceTestUtil.randomString(), true);

		BlogsEntry relatedBlogsEntry1 = BlogsTestUtil.addEntry(
			TestPropsValues.getUserId(), groupId,
			ServiceTestUtil.randomString(), true);

		BlogsEntry relatedBlogsEntry2 = BlogsTestUtil.addEntry(
			TestPropsValues.getUserId(), groupId,
			ServiceTestUtil.randomString(), true);

		addAssetLink(
			groupId, blogsEntry.getUuid(), relatedBlogsEntry1.getUuid(), 1);

		addAssetLink(
			groupId, blogsEntry.getUuid(), relatedBlogsEntry2.getUuid(), 2);

		doExportImportPortlet(PortletKeys.BLOGS);

		BlogsEntry importedBlogsEntry =
			BlogsEntryLocalServiceUtil.getBlogsEntryByUuidAndGroupId(
				blogsEntry.getUuid(), importedGroup.getGroupId());

		Assert.assertNotNull(importedBlogsEntry);

		validateImportedLinks(blogsEntry.getUuid());
	}

	@Test
	public void testExportImportBookmarksWithLinks() throws Exception {
		long groupId = group.getGroupId();

		BookmarksEntry bookmarksEntry = BookmarksTestUtil.addEntry(
			groupId, true);

		BookmarksEntry relatedBookmarkEntry1 = BookmarksTestUtil.addEntry(
			groupId, true);

		BookmarksEntry relatedBookmarkEntry2 = BookmarksTestUtil.addEntry(
			groupId, true);

		addAssetLink(
			groupId, bookmarksEntry.getUuid(), relatedBookmarkEntry1.getUuid(),
			1);

		addAssetLink(
			groupId, bookmarksEntry.getUuid(), relatedBookmarkEntry2.getUuid(),
			2);

		doExportImportPortlet(PortletKeys.BOOKMARKS);

		BookmarksEntry importedBookmarksEntry =
			BookmarksEntryLocalServiceUtil.getBookmarksEntryByUuidAndGroupId(
				bookmarksEntry.getUuid(), importedGroup.getGroupId());

		Assert.assertNotNull(importedBookmarksEntry);

		validateImportedLinks(bookmarksEntry.getUuid());
	}

	@Test
	public void testExportImportJournalWithLinks() throws Exception {
		long groupId = group.getGroupId();

		JournalArticle article = JournalTestUtil.addArticle(
			groupId, ServiceTestUtil.randomString(),
			ServiceTestUtil.randomString());

		JournalArticle relatedArticle1 = JournalTestUtil.addArticle(
			groupId, ServiceTestUtil.randomString(),
			ServiceTestUtil.randomString());

		JournalArticle relatedArticle2 = JournalTestUtil.addArticle(
			groupId, ServiceTestUtil.randomString(),
			ServiceTestUtil.randomString());

		addAssetLink(
			groupId, article.getArticleResourceUuid(),
			relatedArticle1.getArticleResourceUuid(), 1);

		addAssetLink(
			groupId, article.getArticleResourceUuid(),
			relatedArticle2.getArticleResourceUuid(), 2);

		doExportImportPortlet(PortletKeys.JOURNAL);

		JournalArticleResource importedArticleResource =
			JournalArticleResourceUtil.fetchByUUID_G(
				article.getArticleResourceUuid(), importedGroup.getGroupId());

		JournalArticle importedArticle =
			JournalArticleLocalServiceUtil.getLatestArticle(
				importedArticleResource.getResourcePrimKey(),
				article.getStatus(), false);

		Assert.assertNotNull(importedArticle);

		validateImportedLinks(article.getArticleResourceUuid());
	}

	protected AssetLink addAssetLink(
			long groupId, String sourceEntryUUID, String targetEntryUUID,
			int weight)
		throws PortalException, SystemException {

		AssetEntry originAssetEntry = AssetEntryLocalServiceUtil.getEntry(
			groupId, sourceEntryUUID);

		AssetEntry targetAssetEntry = AssetEntryLocalServiceUtil.getEntry(
			groupId, targetEntryUUID);

		return AssetLinkLocalServiceUtil.addLink(
			TestPropsValues.getUserId(), originAssetEntry.getEntryId(),
			targetAssetEntry.getEntryId(), 0, weight);
	}

	protected void doExportImportPortlet(String portletId) throws Exception {
		larFile = LayoutLocalServiceUtil.exportPortletInfoAsFile(
			layout.getPlid(), layout.getGroupId(), portletId, getParameterMap(),
			null, null);

		importedGroup = GroupTestUtil.addGroup();

		importedLayout = LayoutTestUtil.addLayout(
			importedGroup.getGroupId(), ServiceTestUtil.randomString());

		LayoutLocalServiceUtil.importPortletInfo(
			TestPropsValues.getUserId(), importedLayout.getPlid(),
			importedGroup.getGroupId(), portletId, getParameterMap(), larFile);

		importedLayout = LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
			layout.getUuid(), importedGroup.getGroupId(),
			layout.isPrivateLayout());
	}

	protected Map<String, String[]> getParameterMap() {
		Map<String, String[]> parameterMap =
			new LinkedHashMap<String, String[]>();

		parameterMap.put(
			PortletDataHandlerKeys.DATA_STRATEGY,
			new String[] {
				PortletDataHandlerKeys.DATA_STRATEGY_MIRROR_OVERWRITE});
		parameterMap.put(
			PortletDataHandlerKeys.IGNORE_LAST_PUBLISH_DATE,
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_DATA,
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_DATA_ALL,
			new String[] {Boolean.TRUE.toString()});

		return parameterMap;
	}

	protected void validateImportedLinks(String uuid)
		throws PortalException, SystemException {

		AssetEntry originalAssetEntry = AssetEntryLocalServiceUtil.getEntry(
			group.getGroupId(), uuid);

		List<AssetLink> originalAssetLinks = AssetLinkLocalServiceUtil.getLinks(
			originalAssetEntry.getEntryId());

		AssetEntry importedAssetEntry =  AssetEntryLocalServiceUtil.getEntry(
			importedGroup.getGroupId(), uuid);

		List<AssetLink> importedAssetLinks = AssetLinkLocalServiceUtil.getLinks(
			importedAssetEntry.getEntryId());

		Assert.assertEquals(
			originalAssetLinks.size(), importedAssetLinks.size());

		for (AssetLink originalLink : originalAssetLinks) {
			AssetEntry sourceAssetEntry = AssetEntryLocalServiceUtil.getEntry(
				originalLink.getEntryId1());

			AssetEntry targetAssetEntry = AssetEntryLocalServiceUtil.getEntry(
				originalLink.getEntryId2());

			Iterator<AssetLink> iterator = importedAssetLinks.iterator();

			while (iterator.hasNext()) {
				AssetLink importedLink = iterator.next();

				AssetEntry importedLinkSourceAssetEntry =
					AssetEntryLocalServiceUtil.getEntry(
						importedLink.getEntryId1());

				AssetEntry importedLinkTargetAssetEntry =
					AssetEntryLocalServiceUtil.getEntry(
						importedLink.getEntryId2());

				if (!sourceAssetEntry.getClassUuid().equals(
						importedLinkSourceAssetEntry.getClassUuid())) {

					continue;
				}

				if (!targetAssetEntry.getClassUuid().equals(
						importedLinkTargetAssetEntry.getClassUuid())) {

					continue;
				}

				iterator.remove();

				break;
			}
		}

		Assert.assertEquals(0, importedAssetLinks.size());
	}

}