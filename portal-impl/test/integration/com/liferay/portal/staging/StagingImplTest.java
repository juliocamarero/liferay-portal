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

package com.liferay.portal.staging;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.staging.StagingUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.Sync;
import com.liferay.portal.test.SynchronousDestinationExecutionTestListener;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portal.util.LayoutTestUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.util.JournalTestUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.hamcrest.Matchers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.powermock.api.mockito.PowerMockito;

/**
 * @author Julio Camarero
 */
@ExecutionTestListeners(listeners = {
	MainServletExecutionTestListener.class,
	SynchronousDestinationExecutionTestListener.class,
	TransactionalExecutionTestListener.class
})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Sync
@Transactional
public class StagingImplTest extends PowerMockito {

	@Test
	public void testLocalStagingCategories() throws Exception {
		enableLocalStaging(false, true);
	}

	@Test
	public void testLocalStagingJournal() throws Exception {
		enableLocalStaging(true, false);
	}

	@Test
	public void testLocalStagingNaming() throws Exception {
		enableLocalStaging(false, false);

		String localStagingMessage = LanguageUtil.get(
			LocaleThreadLocal.getDefaultLocale(), "staging");

		Assert.assertThat(
			_stagingGroup.getName(),
			Matchers.containsString(localStagingMessage));
	}

	@Test
	public void testRemoteStagingNaming() throws Exception {
		StagingImpl stagingImpl = new StagingImpl() {

			@Override
			protected void validate(
					String remoteAddress, int remotePort,
					String remotePathContext, boolean secureConnection,
					long remoteGroupId)
				throws Exception {

				return;
			}

			@Override
			protected void updateStagedPortlets(
					String remoteURL, long remoteGroupId,
					UnicodeProperties typeSettingsProperties)
				throws Exception {

				return;
			}
		};

		StagingImpl spiedStagingImpl = spy(stagingImpl);

		doCallRealMethod().when(
			spiedStagingImpl, "validate", Mockito.anyString(), Mockito.anyInt(),
			Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyLong());

		doNothing().when(
			spiedStagingImpl, "disableRemoteStaging",
			new Object[] {Mockito.anyString(), Mockito.anyLong()});
		doNothing().when(
			spiedStagingImpl, "enableRemoteStaging",
			new Object[] {Mockito.anyString(), Mockito.anyLong()});

		StagingUtil stagingUtil = (StagingUtil)PortalBeanLocatorUtil.locate(
			StagingUtil.class.getName());

		stagingUtil.setStaging(spiedStagingImpl);

		Group group = GroupTestUtil.addGroup();

		String remoteAddress = ServiceTestUtil.randomString();
		int remotePort = ServiceTestUtil.nextInt();
		String remotePathContext = ServiceTestUtil.randomString();
		boolean secureConnection = ServiceTestUtil.randomBoolean();
		long remoteGroupId = ServiceTestUtil.randomLong();

		StagingUtil.enableRemoteStaging(
			TestPropsValues.getUserId(), group, group, false, false,
			remoteAddress, remotePort, remotePathContext, secureConnection,
			remoteGroupId, new ServiceContext());

		String remoteStagingMessage = LanguageUtil.get(
			LocaleThreadLocal.getDefaultLocale(), "remote-staging");

		Assert.assertThat(
			group.getName(), Matchers.containsString(remoteStagingMessage));

		StagingUtil.disableStaging(group, new ServiceContext());

		Assert.assertThat(
			group.getName(),
			Matchers.not(Matchers.containsString(remoteStagingMessage)));
	}

	protected AssetCategory addAssetCategory(
			long groupId, String title, String description)
		throws Exception {

		Map<Locale, String> titleMap = new HashMap<Locale, String>();
		Map<Locale, String> descriptionMap = new HashMap<Locale, String>();

		for (Locale locale : _locales) {
			titleMap.put(locale, title.concat(LocaleUtil.toLanguageId(locale)));
			descriptionMap.put(
				locale, description.concat(LocaleUtil.toLanguageId(locale)));
		}

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			groupId);

		AssetVocabulary assetVocabulary =
			AssetVocabularyLocalServiceUtil.addVocabulary(
				TestPropsValues.getUserId(), "TestVocabulary", titleMap,
				descriptionMap, null, serviceContext);

		return AssetCategoryLocalServiceUtil.addCategory(
			TestPropsValues.getUserId(), 0, titleMap, descriptionMap,
			assetVocabulary.getVocabularyId(), new String[0], serviceContext);
	}

	protected void enableLocalStaging(
			boolean stageJournal, boolean stageCategories)
		throws Exception {

		_liveGroup = GroupTestUtil.addGroup();

		LayoutTestUtil.addLayout(_liveGroup.getGroupId(), "Page1");
		LayoutTestUtil.addLayout(_liveGroup.getGroupId(), "Page2");

		int initialPagesCount = LayoutLocalServiceUtil.getLayoutsCount(
			_liveGroup, false);

		// Create content

		AssetCategory assetCategory = addAssetCategory(
			_liveGroup.getGroupId(), "Title", "content");
		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_liveGroup.getGroupId(), "Title", "content");

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			_liveGroup.getGroupId());

		Map<String, String[]> parameters = StagingUtil.getStagingParameters();

		parameters.put(
			PortletDataHandlerKeys.CATEGORIES,
			new String[] {String.valueOf(stageCategories)});
		parameters.put(
			PortletDataHandlerKeys.PORTLET_CONFIGURATION + "_" +
				PortletKeys.JOURNAL,
			new String[] {String.valueOf(stageJournal)});
		parameters.put(
			PortletDataHandlerKeys.PORTLET_CONFIGURATION_ALL,
			new String[] {Boolean.FALSE.toString()});
		parameters.put(
			PortletDataHandlerKeys.PORTLET_DATA + "_" + PortletKeys.JOURNAL,
			new String[] {String.valueOf(stageJournal)});
		parameters.put(
			PortletDataHandlerKeys.PORTLET_DATA_ALL,
			new String[] {Boolean.FALSE.toString()});
		parameters.put(
			PortletDataHandlerKeys.PORTLET_SETUP + "_" + PortletKeys.JOURNAL,
			new String[] {String.valueOf(stageJournal)});

		serviceContext.setAttribute(
			StagingUtil.getStagedPortletId(PortletDataHandlerKeys.CATEGORIES),
			stageCategories);
		serviceContext.setAttribute(
			StagingUtil.getStagedPortletId(PortletKeys.JOURNAL), stageJournal);

		for (String parameterName : parameters.keySet()) {
			serviceContext.setAttribute(
				parameterName, parameters.get(parameterName)[0]);
		}

		// Enable staging

		StagingUtil.enableLocalStaging(
			TestPropsValues.getUserId(), _liveGroup, _liveGroup, false, false,
			serviceContext);

		_stagingGroup = _liveGroup.getStagingGroup();

		Assert.assertNotNull(_stagingGroup);

		Assert.assertEquals(
			initialPagesCount,
			LayoutLocalServiceUtil.getLayoutsCount(_stagingGroup, false));

		// Update content in staging

		AssetCategory stagingAssetCategory =
			AssetCategoryLocalServiceUtil.getCategory(
				assetCategory.getUuid(), _stagingGroup.getGroupId());

		stagingAssetCategory = updateAssetCategory(
			stagingAssetCategory, "new name");

		JournalArticle stagingJournalArticle =
			JournalArticleLocalServiceUtil.getArticleByUrlTitle(
				_stagingGroup.getGroupId(), journalArticle.getUrlTitle());

		stagingJournalArticle = JournalTestUtil.updateArticle(
			stagingJournalArticle, "Title2",
			stagingJournalArticle.getContent());

		// Publish to live

		StagingUtil.publishLayouts(
			TestPropsValues.getUserId(), _stagingGroup.getGroupId(),
			_liveGroup.getGroupId(), false, parameters, null, null);

		// Retrieve content from live after publishing

		assetCategory = AssetCategoryLocalServiceUtil.getCategory(
			assetCategory.getUuid(), _liveGroup.getGroupId());
		journalArticle = JournalArticleLocalServiceUtil.getArticle(
			_liveGroup.getGroupId(), journalArticle.getArticleId());

		if (stageCategories) {
			for (Locale locale : _locales) {
				Assert.assertEquals(
					assetCategory.getTitle(locale),
					stagingAssetCategory.getTitle(locale));
			}
		}
		else {
			for (Locale locale : _locales) {
				Assert.assertFalse(
					assetCategory.getTitle(locale).equals(
						stagingAssetCategory.getTitle(locale)));
			}
		}

		if (stageJournal) {
			for (Locale locale : _locales) {
				Assert.assertEquals(
					journalArticle.getTitle(locale),
					stagingJournalArticle.getTitle(locale));
			}
		}
		else {
			for (Locale locale : _locales) {
				Assert.assertFalse(
					journalArticle.getTitle(locale).equals(
						stagingJournalArticle.getTitle(locale)));
			}
		}
	}

	protected AssetCategory updateAssetCategory(
			AssetCategory category, String name)
		throws Exception {

		Map<Locale, String> titleMap = new HashMap<Locale, String>();

		for (Locale locale : _locales) {
			titleMap.put(locale, name.concat(LocaleUtil.toLanguageId(locale)));
		}

		return AssetCategoryLocalServiceUtil.updateCategory(
			TestPropsValues.getUserId(), category.getCategoryId(),
			category.getParentCategoryId(), titleMap,
			category.getDescriptionMap(), category.getVocabularyId(), null,
			ServiceTestUtil.getServiceContext());
	}

	private static Locale[] _locales = {
		new Locale("en", "US"), new Locale("es", "ES"),
		new Locale("de", "DE")
	};

	private Group _liveGroup;
	private Group _stagingGroup;

}