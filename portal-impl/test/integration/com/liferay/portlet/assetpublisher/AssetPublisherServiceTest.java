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

package com.liferay.portlet.assetpublisher;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;
import com.liferay.portlet.asset.service.persistence.AssetEntryQueryTestUtil;
import com.liferay.portlet.assetpublisher.util.AssetPublisherUtil;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.util.BlogsTestUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.util.DLAppTestUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.util.JournalTestUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Roberto Díaz
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class AssetPublisherServiceTest {

	@Before
	public void setUp() throws Exception {
		_groupId = TestPropsValues.getGroupId();
		_userId = TestPropsValues.getUserId();
	}

	@Test
	@Transactional
	public void testGetFilteredCategoryLabelsAndEntries() throws Exception {
		_addVocabulary();

		_addArticles();

		long[] categoryIds = new long[4];

		Iterator categoriesIterator = _categories.entrySet().iterator();

		while (categoriesIterator.hasNext()) {
			Map.Entry entry = (Map.Entry)categoriesIterator.next();

			AssetCategory category = (AssetCategory)entry.getValue();

			categoryIds = ArrayUtil.append(
				categoryIds, category.getCategoryId());
		}

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_groupId, JournalArticle.class.getName(), null, null, null,
				categoryIds);

		List<AssetEntry> assetEntries = AssetEntryLocalServiceUtil.getEntries(
			assetEntryQuery);

		Map<String, List<AssetEntry>> filteredLabelsAndEntries =
			AssetPublisherUtil.getFilteredCategoryLabelsAndEntries(
				assetEntries);

		Assert.assertEquals(5, filteredLabelsAndEntries.size());

		Iterator flaeIterator = filteredLabelsAndEntries.entrySet().iterator();

		while (flaeIterator.hasNext()) {
			Map.Entry entry = (Map.Entry)flaeIterator.next();

			String label = (String)entry.getKey();

			List<AssetEntry> entries = (List)entry.getValue();

			if (label.equals("Athletic")) {
				Assert.fail();
			}
			else if (label.equals("Barcelona")) {
				Assert.assertEquals(1, entries.size());
			}
			else if (label.equals("RealMadrid")) {
				Assert.assertEquals(2, entries.size());
			}
			else if (label.equals("Sevilla")) {
				Assert.assertEquals(1, entries.size());
			}
			else if (label.equals("Sporting")) {
				Assert.assertEquals(2, entries.size());
			}
			else if (label.equals("Barcelona, RealMadrid, Sevilla, Sporting")) {
				Assert.assertEquals(1, entries.size());
			}
			else {
				Assert.fail();
			}
		}
	}

	@Test
	@Transactional
	public void testGetFilteredClassLabelsAndEntries() throws Exception {
		AssetEntryQuery initialBlogsEntryAssetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_groupId, new String[]{BlogsEntry.class.getName()});

		AssetEntryQuery initialFileEntryAssetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_groupId, new String[]{DLFileEntry.class.getName()});

		List<AssetEntry> initialAssetBlogEntries =
			AssetEntryLocalServiceUtil.getEntries(
				initialBlogsEntryAssetEntryQuery);

		List<AssetEntry> initialAssetFileEntries =
			AssetEntryLocalServiceUtil.getEntries(
				initialFileEntryAssetEntryQuery);

		_addBlogEntries();

		_addFileEntries();

		String[] classNames =
			{BlogsEntry.class.getName(), DLFileEntry.class.getName()};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(_groupId, classNames);

		List<AssetEntry> assetEntries = AssetEntryLocalServiceUtil.getEntries(
			assetEntryQuery);

		long blogsEntryClassNameId = PortalUtil.getClassNameId(
			BlogsEntry.class);
		long fileEntryClassNameId = PortalUtil.getClassNameId(
			DLFileEntry.class);

		long[] classNameIds = {blogsEntryClassNameId, fileEntryClassNameId};

		Map<String, List<AssetEntry>> filteredLabelsAndEntries =
			AssetPublisherUtil.getFilteredClassLabelAndEntries(
				assetEntries, classNameIds, LocaleUtil.getDefault());

		Assert.assertEquals(2, filteredLabelsAndEntries.size());

		Iterator flaeIterator = filteredLabelsAndEntries.entrySet().iterator();

		while (flaeIterator.hasNext()) {
			Map.Entry entry = (Map.Entry)flaeIterator.next();

			String label = (String)entry.getKey();

			List<AssetEntry> entries = (List)entry.getValue();

			if (label.equals("Blogs Entry")) {
				Assert.assertEquals(
					initialAssetBlogEntries.size() + 2, entries.size());
			}
			else if (label.equals("Document")) {
				Assert.assertEquals(
					initialAssetFileEntries.size() + 1, entries.size());
			}
			else {
				Assert.fail();
			}
		}
	}

	private void _addArticles() throws Exception {
		AssetCategory categoryBarcelona = _categories.get("Barcelona");
		AssetCategory categoryRealMadrid = _categories.get("RealMadrid");
		AssetCategory categorySevilla = _categories.get("Sevilla");
		AssetCategory categorySporting = _categories.get("Sporting");

		long categoryBarcelonaId = categoryBarcelona.getCategoryId();
		long categoryRealMadridId = categoryRealMadrid.getCategoryId();
		long categorySevillaId = categorySevilla.getCategoryId();
		long categorySportingId = categorySporting.getCategoryId();

		_addCategorzedArticles(new long[]{categoryBarcelonaId}, 1);
		_addCategorzedArticles(new long[]{categoryRealMadridId}, 2);
		_addCategorzedArticles(new long[]{categorySevillaId}, 1);
		_addCategorzedArticles(new long[]{categorySportingId}, 2);
		_addCategorzedArticles(
			new long[] {
				categoryBarcelonaId, categoryRealMadridId, categorySevillaId,
				categorySportingId},
			1);
	}

	private void _addBlogEntries() throws Exception {
		for (int i = 0; i < 2; i++) {
			BlogsTestUtil.addEntry(
				_userId, _groupId, ServiceTestUtil.randomString(), true);
		}
	}

	private Map<String, AssetCategory> _addCategorires(long vocabularyId)
		throws Exception {

		Map<String, AssetCategory> categories =
			new HashMap<String, AssetCategory>(4);

		for (String categoryName : _categoryNames) {
			AssetCategory category =
				AssetCategoryLocalServiceUtil.addCategory(
					TestPropsValues.getUserId(), categoryName, vocabularyId,
					ServiceTestUtil.getServiceContext());

			categories.put(categoryName, category);
		}

		return categories;
	}

	private void _addCategorzedArticles(long[] categoryIds, int number)
		throws Exception {

		for (int i = 0; i < number; i++) {
			JournalArticle article = JournalTestUtil.addArticle(
				_groupId, ServiceTestUtil.randomString(),
				ServiceTestUtil.randomString(100));

			JournalArticleLocalServiceUtil.updateAsset(
				_userId, article, categoryIds, null, null);
		}
	}

	private void _addFileEntries() throws Exception {
		for (int i = 0; i < 1; i++) {
			DLAppTestUtil.addFileEntry(
				_groupId, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				ServiceTestUtil.randomString());
		}
	}

	private void _addVocabulary() throws Exception {
		AssetVocabulary vocabulary =
			AssetVocabularyLocalServiceUtil.addVocabulary(
				TestPropsValues.getUserId(), "Spanish_Football_Teams",
				ServiceTestUtil.getServiceContext(_groupId));

		_categories = _addCategorires(vocabulary.getVocabularyId());
	}

	private Map<String, AssetCategory> _categories;
	private String[] _categoryNames =
		{"Athletic", "Barcelona", "RealMadrid", "Sevilla", "Sporting",};
	private long _groupId;
	private long _userId;

}