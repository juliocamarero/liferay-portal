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

package com.liferay.portal.layout;

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.CompanyUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalCallbackAwareExecutionTestListener;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.util.JournalTestUtil;
import com.liferay.portlet.sites.util.SitesUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;

/**
 * @author Eduardo Garcia
 */
@PrepareForTest({PortletLocalServiceUtil.class})

@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		TransactionalCallbackAwareExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class PortletExportImportTest extends BaseLayoutSetPrototypeTestCase {

	@Before
	public void setUp() throws Exception {

		// Create site template

		FinderCacheUtil.clearCache();

		LayoutSetPrototype layoutSetPrototype =
			ServiceTestUtil.addLayoutSetPrototype(
				ServiceTestUtil.randomString());

		_layoutSetPrototypeGroup = layoutSetPrototype.getGroup();

		_layoutSetPrototypeLayout = ServiceTestUtil.addLayout(
			_layoutSetPrototypeGroup.getGroupId(),
			ServiceTestUtil.randomString(), true);

		updateLayoutTemplateId(_layoutSetPrototypeLayout, "1_column");

		_layoutSetPrototypeJournalArticle = JournalTestUtil.addArticle(
			_layoutSetPrototypeGroup.getGroupId(), 0, "Test Article",
			"Test Content");

		_layoutSetPrototypeJournalContentPortletId =
			addJournalContentPortletToLayout(
				TestPropsValues.getUserId(), _layoutSetPrototypeLayout,
				_layoutSetPrototypeJournalArticle, "column-1");

		// Create site from site template

		_group = ServiceTestUtil.addGroup();

		SitesUtil.updateLayoutSetPrototypesLinks(
			_group, layoutSetPrototype.getLayoutSetPrototypeId(), 0, true,
			true);

		propagateChanges(_group);
	}

	@Test
	public void testExportImportPortletData() throws Exception {

		// Check data after site creation

		String content = _layoutSetPrototypeJournalArticle.getContent();

		JournalArticle journalArticle =
			JournalArticleLocalServiceUtil.getArticleByUrlTitle(
				_group.getGroupId(),
				_layoutSetPrototypeJournalArticle.getUrlTitle());

		Assert.assertEquals(content, journalArticle.getContent());

		// Update site template data

		JournalTestUtil.updateArticle(
			_layoutSetPrototypeJournalArticle, "New Test Content");

		// Check data after layout reset

		Layout layout = LayoutLocalServiceUtil.getFriendlyURLLayout(
			_group.getGroupId(), false,
			_layoutSetPrototypeLayout.getFriendlyURL());

		SitesUtil.resetPrototype(layout);

		Assert.assertEquals(content, journalArticle.getContent());
	}

	@Test
	public void testExportImportPortletPreferences() throws Exception {

		// Check preferences after site creation

		JournalArticle journalArticle =
			JournalArticleLocalServiceUtil.getArticleByUrlTitle(
				_group.getGroupId(),
				_layoutSetPrototypeJournalArticle.getUrlTitle());

		Layout layout = LayoutLocalServiceUtil.getFriendlyURLLayout(
			_group.getGroupId(), false,
			_layoutSetPrototypeLayout.getFriendlyURL());

		javax.portlet.PortletPreferences jxPreferences =
			ServiceTestUtil.getPortletPreferences(
				layout.getCompanyId(), layout.getPlid(),
				_layoutSetPrototypeJournalContentPortletId);

		Assert.assertEquals(
			journalArticle.getArticleId(),
			jxPreferences.getValue("articleId", StringPool.BLANK));

		Assert.assertEquals(
			String.valueOf(journalArticle.getGroupId()),
			jxPreferences.getValue("groupId", StringPool.BLANK));

		Assert.assertEquals(
			String.valueOf(true), jxPreferences.getValue(
				"showAvailableLocales", StringPool.BLANK));

		// Update site template preferences

		javax.portlet.PortletPreferences layoutSetprototypeJxPreferences =
			ServiceTestUtil.getPortletPreferences(
				_layoutSetPrototypeLayout.getCompanyId(),
				_layoutSetPrototypeLayout.getPlid(),
				_layoutSetPrototypeJournalContentPortletId);

		layoutSetprototypeJxPreferences.setValue(
			"showAvailableLocales", String.valueOf(false));

		ServiceTestUtil.updatePortletPreferences(
			_layoutSetPrototypeLayout.getPlid(),
			_layoutSetPrototypeJournalContentPortletId,
			layoutSetprototypeJxPreferences);

		// Check preferences after layout reset

		SitesUtil.resetPrototype(layout);

		jxPreferences = ServiceTestUtil.getPortletPreferences(
			_group.getCompanyId(), layout.getPlid(),
			_layoutSetPrototypeJournalContentPortletId);

		Assert.assertEquals(
			journalArticle.getArticleId(),
			jxPreferences.getValue("articleId", StringPool.BLANK));
		Assert.assertEquals(
			String.valueOf(journalArticle.getGroupId()),
			jxPreferences.getValue("groupId", StringPool.BLANK));
		Assert.assertEquals(
			Boolean.FALSE.toString(),
			jxPreferences.getValue("showAvailableLocales", StringPool.BLANK));

		// Update journal content portlet with a new globally scoped journal
		// article

		Company company = CompanyUtil.fetchByPrimaryKey(
			_layoutSetPrototypeLayout.getCompanyId());

		Group companyGroup = company.getGroup();

		JournalArticle globalScopeJournalArticle = JournalTestUtil.addArticle(
			companyGroup.getGroupId(), 0, "Global Article", "Global Content");

		layoutSetprototypeJxPreferences.setValue(
			"articleId", globalScopeJournalArticle.getArticleId());
		layoutSetprototypeJxPreferences.setValue(
			"groupId", Long.toString(companyGroup.getGroupId()));
		layoutSetprototypeJxPreferences.setValue(
			"lfrScopeLayoutUuid", StringPool.BLANK);
		layoutSetprototypeJxPreferences.setValue("lfrScopeType", "company");

		ServiceTestUtil.updatePortletPreferences(
			_layoutSetPrototypeLayout.getPlid(),
			_layoutSetPrototypeJournalContentPortletId,
			layoutSetprototypeJxPreferences);

		jxPreferences = ServiceTestUtil.getPortletPreferences(
			_group.getCompanyId(), layout.getPlid(),
			_layoutSetPrototypeJournalContentPortletId);

		// Check preferences when journal article is from the global scope

		Assert.assertEquals(
			globalScopeJournalArticle.getArticleId(),
			jxPreferences.getValue("articleId", StringPool.BLANK));
		Assert.assertEquals(
			String.valueOf(companyGroup.getGroupId()),
			jxPreferences.getValue("groupId", StringPool.BLANK));
		Assert.assertEquals(
			StringPool.BLANK,
			jxPreferences.getValue("lfrScopeLayoutUuid", StringPool.BLANK));
		Assert.assertEquals(
			"company",
			jxPreferences.getValue("lfrScopeType", StringPool.BLANK));
	}

	protected String addJournalContentPortletToLayout(
			long userId, Layout layout, JournalArticle journalArticle,
			String columnId)
		throws Exception {

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet) layout.getLayoutType();

		String journalPortletId = layoutTypePortlet.addPortletId(
			userId, PortletKeys.JOURNAL_CONTENT, columnId, -1);

		LayoutLocalServiceUtil.updateLayout(
			layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(),
			layout.getTypeSettings());

		javax.portlet.PortletPreferences prefs =
			ServiceTestUtil.getPortletPreferences(
				layout.getCompanyId(), layout.getPlid(), journalPortletId);

		prefs.setValue("articleId", journalArticle.getArticleId());
		prefs.setValue("groupId", String.valueOf(journalArticle.getGroupId()));
		prefs.setValue("showAvailableLocales", Boolean.TRUE.toString());

		ServiceTestUtil.updatePortletPreferences(
			layout.getPlid(), journalPortletId, prefs);

		return journalPortletId;
	}

	private Group _group;
	private Group _layoutSetPrototypeGroup;
	private JournalArticle _layoutSetPrototypeJournalArticle;
	private String _layoutSetPrototypeJournalContentPortletId;
	private Layout _layoutSetPrototypeLayout;

}