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

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.lar.UserIdStrategy;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.lar.BasePortletExportImportTestCase;
import com.liferay.portal.lar.PortletImporter;
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
import com.liferay.portlet.blogs.util.BlogsTestUtil;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.util.BookmarksTestUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.util.JournalTestUtil;

import java.util.ArrayList;
import java.util.HashMap;
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

		BlogsEntry mainBlogEntry = BlogsTestUtil.addEntry(
			TestPropsValues.getUserId(), group.getGroupId(),
			ServiceTestUtil.randomString(), true);

		String mainBlogEntryUuid = mainBlogEntry.getUuid();

		AssetEntry mainAssetEntry = AssetEntryLocalServiceUtil.getEntry(
			group.getGroupId(), mainBlogEntryUuid);

		BlogsEntry relatedBlogEntry1 = BlogsTestUtil.addEntry(
			TestPropsValues.getUserId(), group.getGroupId(),
			ServiceTestUtil.randomString(), true);

		BlogsEntry relatedBlogEntry2 = BlogsTestUtil.addEntry(
			TestPropsValues.getUserId(), group.getGroupId(),
			ServiceTestUtil.randomString(), true);

		List<AssetLink> assetLinks = new ArrayList<AssetLink>();

		AssetLink assetLink1 = createAssetLink(
			groupId, mainBlogEntryUuid, relatedBlogEntry1.getUuid(), 1);

		assetLinks.add(assetLink1);

		AssetLink assetLink2 = createAssetLink(
			groupId, mainBlogEntryUuid, relatedBlogEntry2.getUuid(), 2);

		assetLinks.add(assetLink2);

		long numberOfLinksOfAssetEntry =
			AssetLinkLocalServiceUtil.getDirectLinks(
				mainAssetEntry.getEntryId()).size();

		doExport(PortletKeys.BLOGS);

		doImport(PortletKeys.BLOGS);

		AssetEntry importMainAssetEntry = AssetEntryLocalServiceUtil.getEntry(
			importedGroup.getGroupId(), mainBlogEntryUuid);

		List<AssetLink> importedLinks =
			AssetLinkLocalServiceUtil.getDirectLinks(
				importMainAssetEntry.getEntryId());

		long numberOfLinksAfterImport = importedLinks.size();

		Assert.assertEquals(
			numberOfLinksAfterImport, numberOfLinksOfAssetEntry);

		_checkImportedLinks(assetLinks, importedGroup.getGroupId());

	}

	@Test
	public void testExportImportBookmarksWithLinks() throws Exception {

		long groupId = group.getGroupId();

		BookmarksEntry mainBookmarkEntry = BookmarksTestUtil.addEntry(
			groupId, true);

		String mainBookmarkEntryUuid = mainBookmarkEntry.getUuid();

		AssetEntry mainAssetEntry = AssetEntryLocalServiceUtil.getEntry(
			group.getGroupId(), mainBookmarkEntryUuid);

		BookmarksEntry relatedBookmarkEntry1 = BookmarksTestUtil.addEntry(
			groupId, true);

		BookmarksEntry relatedBookmarkEntry2 = BookmarksTestUtil.addEntry(
			groupId, true);

		List<AssetLink> assetLinks = new ArrayList<AssetLink>();

		AssetLink assetLink1 = createAssetLink(
			groupId, mainBookmarkEntryUuid, relatedBookmarkEntry1.getUuid(), 1);

		assetLinks.add(assetLink1);

		AssetLink assetLink2 = createAssetLink(
			groupId, mainBookmarkEntryUuid, relatedBookmarkEntry2.getUuid(), 2);

		assetLinks.add(assetLink2);

		long numberOfLinksBeforeImport =
			AssetLinkLocalServiceUtil.getDirectLinks(
				mainAssetEntry.getEntryId()).size();

		doExport(PortletKeys.BOOKMARKS);

		doImport(PortletKeys.BOOKMARKS);

		AssetEntry importMainAssetEntry = AssetEntryLocalServiceUtil.getEntry(
			importedGroup.getGroupId(), mainBookmarkEntryUuid);

		List<AssetLink> importedLinks =
				AssetLinkLocalServiceUtil.getDirectLinks(
					importMainAssetEntry.getEntryId());

		long numberOfLinksAfterImport = importedLinks.size();

		Assert.assertEquals(
			numberOfLinksAfterImport, numberOfLinksBeforeImport);

		_checkImportedLinks(assetLinks, importedGroup.getGroupId());
	}

	@Test
	public void testExportImportJournalWithLinks() throws Exception {

		long groupId = group.getGroupId();

		JournalArticle mainJournalEntry = JournalTestUtil.addArticle(
			group.getGroupId(),
			"mainJournalEntryTest"+ServiceTestUtil.randomString(),
			ServiceTestUtil.randomString());

		String mainJournalEntryUuid = mainJournalEntry.getArticleResourceUuid();

		AssetEntry mainAssetEntry = AssetEntryLocalServiceUtil.getEntry(
			group.getGroupId(), mainJournalEntryUuid);

		JournalArticle relatedArticleEntry1 = JournalTestUtil.addArticle(
			group.getGroupId(), ServiceTestUtil.randomString(),
			ServiceTestUtil.randomString());

		JournalArticle relatedArticleEntry2 = JournalTestUtil.addArticle(
			group.getGroupId(), ServiceTestUtil.randomString(),
			ServiceTestUtil.randomString());

		List<AssetLink> assetLinks = new ArrayList<AssetLink>();

		AssetLink assetLink1 = createAssetLink(
			groupId, mainJournalEntryUuid,
			relatedArticleEntry1.getArticleResourceUuid(), 1);

		assetLinks.add(assetLink1);

		AssetLink assetLink2 = createAssetLink(
			groupId, mainJournalEntryUuid,
			relatedArticleEntry2.getArticleResourceUuid(), 2);

		assetLinks.add(assetLink2);

		List<AssetLink> exportAssetLinks = AssetLinkLocalServiceUtil.getLinks(
			mainAssetEntry.getEntryId());

		doExport(PortletKeys.JOURNAL);

		doImport(PortletKeys.JOURNAL);

		AssetEntry mainAssetEntryImported = AssetEntryLocalServiceUtil.getEntry(
			importedGroup.getGroupId(), mainJournalEntryUuid);

		List<AssetLink> importAssetLinks = AssetLinkLocalServiceUtil.getLinks(
			mainAssetEntryImported.getEntryId());

		Assert.assertEquals(exportAssetLinks.size(), importAssetLinks.size());

		_checkImportedLinks(assetLinks, importedGroup.getGroupId());
	}

	protected Map<String, String[]> getBaseParameterMap() throws Exception {
		Map<String, String[]> parameterMap = new HashMap<String, String[]>();

		parameterMap.put(
			PortletDataHandlerKeys.CATEGORIES,
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_DATA_ALL,
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.PERMISSIONS,
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_DATA_CONTROL_DEFAULT,
			new String[] {Boolean.FALSE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_METADATA_ALL,
			new String[] {Boolean.TRUE.toString()});

		return parameterMap;
	}

	protected Map<String, String[]> getExportParameterMap(String portlet)
		throws Exception {

		Map<String, String[]> parameterMap = getBaseParameterMap();

		parameterMap.put(Constants.CMD, new String[] {Constants.EXPORT});
		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_DATA + StringPool.UNDERLINE +
				portlet,
			new String[] {Boolean.TRUE.toString()});

		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_METADATA_ALL,
			new String[] {Boolean.TRUE.toString()});

		parameterMap.put(
			"_journal_embedded-assets", new String[] {Boolean.TRUE.toString()});

		parameterMap.put(
			"_journal_web-content", new String[] {Boolean.TRUE.toString()});

		parameterMap.put("range", new String[] {"fromLastPublishDate"});

		return parameterMap;
	}

	protected Map<String, String[]> getImportParameterMap(
			long plid, String portletResource)
		throws Exception {

		Map<String, String[]> parameterMap = getBaseParameterMap();

		parameterMap.put(Constants.CMD, new String[] {Constants.IMPORT});
		parameterMap.put(
			PortletDataHandlerKeys.DATA_STRATEGY,
			new String[] {PortletDataHandlerKeys.DATA_STRATEGY_MIRROR});
		parameterMap.put(
			PortletDataHandlerKeys.DELETE_PORTLET_DATA,
			new String[] {Boolean.FALSE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_DATA,
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.USER_ID_STRATEGY,
			new String[] {UserIdStrategy.CURRENT_USER_ID});

		parameterMap.put("plid", new String[] {String.valueOf(plid)});

		parameterMap.put("portletResource", new String[] {portletResource});

		parameterMap.put(
			"_journal_embedded-assets", new String[] {Boolean.TRUE.toString()});

		parameterMap.put(
			"_journal_web-content", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_version-history", new String[] {Boolean.TRUE.toString()});

		parameterMap.put("range", new String[] {"fromLastPublishDate"});

		return parameterMap;
	}

	private void _checkImportedLinks(
			List<AssetLink> assetLinks, long importedGroupId)
		throws PortalException, SystemException {

		for (AssetLink originLink : assetLinks) {
			AssetEntry sourceAssetEntry = AssetEntryLocalServiceUtil.getEntry(
				originLink.getEntryId1());

			AssetEntry targetAssetEntry = AssetEntryLocalServiceUtil.getEntry(
				originLink.getEntryId2());

			AssetLink importedLink = AssetLinkLocalServiceUtil.getLink(
				importedGroupId, sourceAssetEntry.getClassUuid(),
				targetAssetEntry.getClassUuid(), originLink.getType());

			Assert.assertEquals(originLink.getType(), importedLink.getType());
			Assert.assertEquals(
				originLink.getWeight(), importedLink.getWeight());
		}
	}

	private AssetLink createAssetLink(
			long groupId, String originEntryUUID, String targetEntryUUID,
			int weight) throws PortalException, SystemException {

		AssetEntry originAssetEntry = AssetEntryLocalServiceUtil.getEntry(
			groupId, originEntryUUID);

		AssetEntry targetAssetEntry = AssetEntryLocalServiceUtil.getEntry(
			groupId, targetEntryUUID);

		return AssetLinkLocalServiceUtil.addLink(
			TestPropsValues.getUserId(), originAssetEntry.getEntryId(),
			targetAssetEntry.getEntryId(), 0, weight);
	}

	private void doExport(String portlet) throws Exception {
		Map<String, String[]> parameterMap = getExportParameterMap(portlet);

		larFile = LayoutLocalServiceUtil.exportPortletInfoAsFile(
			layout.getPlid(), group.getGroupId(), portlet, parameterMap, null,
			null);
	}

	private void doImport(String portlet) throws Exception {
		importedGroup = GroupTestUtil.addGroup();

		importedLayout = LayoutTestUtil.addLayout(
			importedGroup.getGroupId(), ServiceTestUtil.randomString());

		Map<String, String[]> parameterMap = getImportParameterMap(
			importedLayout.getPlid(), portlet);

		LayoutLocalServiceUtil.importPortletInfo(
			TestPropsValues.getUserId(), importedLayout.getPlid(),
			importedGroup.getGroupId(), portlet, parameterMap, larFile);
	}

}