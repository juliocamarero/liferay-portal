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

package com.liferay.portlet.journal.service;

import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.util.test.DDMStructureTestUtil;
import com.liferay.portlet.dynamicdatamapping.util.test.DDMTemplateTestUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleConstants;
import com.liferay.portlet.journal.model.JournalFolderConstants;
import com.liferay.portlet.journal.util.test.JournalTestUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Juan Fernández
 */
@Sync
public class JournalArticleExpirationTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE,
			SynchronousDestinationTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testExpiredArticle() throws Exception {
		testExpireArticle(true, _WHEN_PAST);
	}

	@Test
	public void testExpiredFutureArticle() throws Exception {
		testExpireArticle(true, _WHEN_FUTURE);
	}

	@Test
	public void testExpiredFutureLatestVersion() throws Exception {
		testExpireArticle(true, _WHEN_FUTURE);
	}

	@Test
	public void testExpiredFuturePreviousVersion() throws Exception {
		testExpireArticle(true, _WHEN_FUTURE);
	}

	@Test
	public void testExpiredLatestVersion() throws Exception {
		testExpireArticle(true, _WHEN_PAST);
	}

	@Test
	public void testExpiredPreviousVersion() throws Exception {
		testExpireArticle(true, _WHEN_PAST);
	}

	@Test
	public void testExpireDraft() throws Exception {
		testExpireArticle(false, _WHEN_PAST);
	}

	@Test
	public void testExpireFutureDraft() throws Exception {
		testExpireArticle(false, _WHEN_FUTURE);
	}

	protected JournalArticle addArticle(
			long groupId, Date expirationDate, int when, boolean approved)
		throws Exception {

		Map<Locale, String> titleMap = new HashMap<>();

		titleMap.put(LocaleUtil.getDefault(), RandomTestUtil.randomString());

		Map<Locale, String> descriptionMap = new HashMap<>();

		descriptionMap.put(
			LocaleUtil.getDefault(), RandomTestUtil.randomString());

		String content = DDMStructureTestUtil.getSampleStructuredContent();

		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			groupId, JournalArticle.class.getName());

		DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
			groupId, ddmStructure.getStructureId());

		Calendar displayDateCalendar = getCalendar(new Date(), when);

		Calendar expirationDateCalendar = getCalendar(expirationDate, when);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(groupId);

		if (approved) {
			serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);
		}
		else {
			serviceContext.setWorkflowAction(
				WorkflowConstants.ACTION_SAVE_DRAFT);
		}

		return JournalArticleLocalServiceUtil.addArticle(
			TestPropsValues.getUserId(), groupId,
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASSNAME_ID_DEFAULT, 0, StringPool.BLANK,
			true, JournalArticleConstants.VERSION_DEFAULT, titleMap,
			descriptionMap, content, ddmStructure.getStructureKey(),
			ddmTemplate.getTemplateKey(), null,
			displayDateCalendar.get(Calendar.MONTH),
			displayDateCalendar.get(Calendar.DAY_OF_MONTH),
			displayDateCalendar.get(Calendar.YEAR),
			displayDateCalendar.get(Calendar.HOUR_OF_DAY),
			displayDateCalendar.get(Calendar.MINUTE),
			expirationDateCalendar.get(Calendar.MONTH),
			expirationDateCalendar.get(Calendar.DAY_OF_MONTH),
			expirationDateCalendar.get(Calendar.YEAR),
			expirationDateCalendar.get(Calendar.HOUR_OF_DAY),
			expirationDateCalendar.get(Calendar.MINUTE), true, 0, 0, 0, 0, 0,
			true, true, false, null, null, null, null, serviceContext);
	}

	protected Calendar getCalendar(Date date, int when) {
		Calendar calendar = new GregorianCalendar();

		calendar.setTime(new Date(date.getTime() + Time.MINUTE * when * 5));

		return calendar;
	}

	protected void testExpireArticle(boolean approved, int when)
		throws Exception {

		int initialSearchArticlesCount = JournalTestUtil.getSearchArticlesCount(
			_group.getCompanyId(), _group.getGroupId());

		Date now = new Date();

		// Add Approved Article

		JournalArticle article = addArticle(
			_group.getGroupId(), now, when, true);

		// Add a version of the article

		article = updateArticle(article, now, when, approved);

		// Simulate automatic expiration

		JournalArticleLocalServiceUtil.checkArticles();

		article = JournalArticleLocalServiceUtil.getArticle(article.getId());

		AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(
			JournalArticle.class.getName(), article.getResourcePrimKey());

		if (when == _WHEN_FUTURE) {
			Assert.assertFalse(article.isApproved());
			Assert.assertFalse(assetEntry.isVisible());

			if (approved) {
				Assert.assertTrue(article.isScheduled());
			}
			else {
				Assert.assertTrue(article.isDraft());
			}
		}
		else {
			Assert.assertFalse(article.isExpired());
			Assert.assertEquals(approved, article.isApproved());
			Assert.assertEquals(approved, assetEntry.isVisible());

			if (approved) {
				Assert.assertEquals(
					initialSearchArticlesCount + 1,
					JournalTestUtil.getSearchArticlesCount(
						_group.getCompanyId(), _group.getGroupId()));
			}
			else {
				Assert.assertEquals(
					initialSearchArticlesCount,
					JournalTestUtil.getSearchArticlesCount(
						_group.getCompanyId(), _group.getGroupId()));
			}
		}
	}

	protected JournalArticle updateArticle(
			JournalArticle article, Date expirationDate, int when,
			boolean approved)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(article.getGroupId());

		if (approved) {
			serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);
		}
		else {
			serviceContext.setWorkflowAction(
				WorkflowConstants.ACTION_SAVE_DRAFT);
		}

		Calendar displayDateCalendar = getCalendar(
			article.getDisplayDate(), when);

		Calendar expirationDateCalendar = getCalendar(expirationDate, when);

		return JournalArticleLocalServiceUtil.updateArticle(
			TestPropsValues.getUserId(), article.getGroupId(),
			article.getFolderId(), article.getArticleId(), article.getVersion(),
			article.getTitleMap(), article.getDescriptionMap(),
			article.getContent(), article.getDDMStructureKey(),
			article.getDDMTemplateKey(), article.getLayoutUuid(),
			displayDateCalendar.get(Calendar.MONTH),
			displayDateCalendar.get(Calendar.DAY_OF_MONTH),
			displayDateCalendar.get(Calendar.YEAR),
			displayDateCalendar.get(Calendar.HOUR_OF_DAY),
			displayDateCalendar.get(Calendar.MINUTE),
			expirationDateCalendar.get(Calendar.MONTH),
			expirationDateCalendar.get(Calendar.DAY_OF_MONTH),
			expirationDateCalendar.get(Calendar.YEAR),
			expirationDateCalendar.get(Calendar.HOUR_OF_DAY),
			expirationDateCalendar.get(Calendar.MINUTE), true, 0, 0, 0, 0, 0,
			true, article.getIndexable(), article.isSmallImage(),
			article.getSmallImageURL(), null, null, null, serviceContext);
	}

	private static final int _WHEN_FUTURE = 1;

	private static final int _WHEN_PAST = -1;

	@DeleteAfterTestRun
	private Group _group;

}