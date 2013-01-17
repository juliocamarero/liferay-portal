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

package com.liferay.portlet.journal.lar;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.lar.PortletImporter;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalCallbackAwareExecutionTestListener;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleResource;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalServiceUtil;
import com.liferay.portlet.journal.util.JournalTestUtil;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Juan Fernández
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		TransactionalCallbackAwareExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class JournalExportImportTest {

	@After
	public void tearDown() throws Exception {
		if ((_larFile != null) && _larFile.exists()) {
			FileUtil.delete(_larFile);
		}
	}

	@Test
	public void testExportImportBasicJournalContent() throws Exception {
		exportImportJournalArticle(false);
	}

	@Test
	public void testExportImportStructuredJournalContent() throws Exception {
		exportImportJournalArticle(true);
	}

	protected void exportImportJournalArticle(boolean structuredContent)
		throws Exception {

		// Add a site and a layout

		Group group = ServiceTestUtil.addGroup();

		Layout layout = ServiceTestUtil.addLayout(
			group.getGroupId(), ServiceTestUtil.randomString());

		// Add a Journal Article and DDM Structure and Template

		JournalArticle article = null;
		DDMStructure ddmStructure = null;
		DDMTemplate ddmTemplate = null;

		if (structuredContent) {
			ddmStructure = JournalTestUtil.addDDMStructure(group.getGroupId());

			ddmTemplate = JournalTestUtil.addDDMTemplate(
				group.getGroupId(), ddmStructure.getStructureId());

			String content = JournalTestUtil.getSampleStructuredContent();

			article = JournalTestUtil.addArticle(
				group.getGroupId(), content, ddmStructure.getStructureKey(),
				ddmTemplate.getTemplateKey());
		}
		else {
			article = JournalTestUtil.addArticle(
				group.getGroupId(), ServiceTestUtil.randomString(),
				ServiceTestUtil.randomString());
		}

		String exportedResourceUuid = article.getArticleResourceUuid();

		// Export Portlet Content

		Map<String, String[]> parameterMap = getExportParameterMap(
			group.getGroupId(), layout.getPlid());

		_larFile = LayoutLocalServiceUtil.exportPortletInfoAsFile(
			layout.getPlid(), group.getGroupId(), PortletKeys.JOURNAL,
			parameterMap, null, null);

		// Remove the site

		GroupLocalServiceUtil.deleteGroup(group.getGroupId());

		// Add another site and layout

		group = ServiceTestUtil.addGroup();

		layout = ServiceTestUtil.addLayout(
			group.getGroupId(), ServiceTestUtil.randomString());

		int initialArticlesCount =
			JournalArticleLocalServiceUtil.getArticlesCount(group.getGroupId());

		// Import Portlet Content

		PortletImporter portletImporter = new PortletImporter();

		parameterMap = getImportParameterMap(
			group.getGroupId(), layout.getPlid());

		portletImporter.importPortletInfo(
			TestPropsValues.getUserId(), layout.getPlid(), group.getGroupId(),
			PortletKeys.JOURNAL, parameterMap, _larFile);

		// Verify there is just one new article in the new site

		int articlesCount = JournalArticleLocalServiceUtil.getArticlesCount(
			group.getGroupId());

		Assert.assertEquals(initialArticlesCount + 1, articlesCount);

		// Verify the same article exists in the new site

		JournalArticleResource importedJournalArticleResource =
			JournalArticleResourceLocalServiceUtil.fetchArticleResource(
				exportedResourceUuid, group.getGroupId());

		Assert.assertNotNull(importedJournalArticleResource);

		if (structuredContent) {

			// Verify the structure and template exist in the new site

			DDMStructure importedDDMStructure =
				DDMStructureLocalServiceUtil.fetchStructure(
					ddmStructure.getUuid(), group.getGroupId());

			Assert.assertNotNull(importedDDMStructure);

			DDMTemplate importedDDMTemplate =
				DDMTemplateLocalServiceUtil.fetchTemplate(
					ddmTemplate.getUuid(), group.getGroupId());

			Assert.assertNotNull(importedDDMTemplate);

			// Check Relationships

			Assert.assertEquals(
				article.getStructureId(),
				importedDDMStructure.getStructureKey());

			Assert.assertEquals(
				article.getTemplateId(), importedDDMTemplate.getTemplateKey());

			Assert.assertEquals(
				importedDDMTemplate.getClassPK(),
				importedDDMStructure.getStructureId());
		}
	}

	protected Map<String, String[]> getExportParameterMap(
		long groupId, long plid)
		throws Exception {

		Map<String, String[]> parameterMap = new HashMap<String, String[]>();

		parameterMap.put(
			"_journal_commentsCheckbox",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put("etag", new String[]{"0"});
		parameterMap.put(
			"_journal_web-content", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_embedded-assets", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"PORTLET_DATA_15Checkbox", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_embedded-assetsCheckbox",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_images", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"PORTLET_DATA_15", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_categories", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_ratings", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"PORTLET_METADATA_ALL", new String[]{Boolean.TRUE.toString()});
		parameterMap.put("strip", new String[]{"0"});
		parameterMap.put("cmd", new String[]{"export"});
		parameterMap.put("plid", new String[]{String.valueOf(plid)});
		parameterMap.put(
			"struts_action",
			new String[]{"/portlet_configuration/export_import"});
		parameterMap.put("range", new String[]{"fromLastPublishDate"});
		parameterMap.put(
			"_journal_tags", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_ratingsCheckbox", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"permissinsAssignedToRolesCheckbox",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_version-historyCheckbox",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_version-history", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_ddmStructures-ddmTemplates-and-feeds",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_categoriesCheckbox",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_imagesCheckbox", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_comments", new String[]{Boolean.TRUE.toString()});
		parameterMap.put("doAsGroupId", new String[]{String.valueOf(groupId)});
		parameterMap.put(
			"_journal_tagsCheckbox", new String[]{Boolean.TRUE.toString()});
		parameterMap.put("tabs1", new String[]{"export_import"});
		parameterMap.put("tabs2", new String[]{"export"});
		parameterMap.put(
			"PERMISSIONS", new String[]{Boolean.FALSE.toString()});
		parameterMap.put("portletResource", new String[]{PortletKeys.JOURNAL});
		parameterMap.put(
			"_journal_web-contentCheckbox",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"permissinsAssignedToRoles",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put("groupId", new String[]{String.valueOf(groupId)});
		parameterMap.put("CATEGORIES", new String[]{Boolean.FALSE.toString()});
		parameterMap.put(
			"PORTLET_DATA_CONTROL_DEFAULT",
			new String[]{Boolean.FALSE.toString()});

		return parameterMap;
	}

	protected Map<String, String[]> getImportParameterMap(
		long groupId, long plid)
		throws Exception {

		Map<String, String[]> parameterMap = new HashMap<String, String[]>();

		parameterMap.put(
			"_journal_commentsCheckbox",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"PERMISSIONSCheckbox", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_web-content", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_tags", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"DELETE_PORTLET_DATA", new String[]{Boolean.FALSE.toString()});
		parameterMap.put("USER_ID_STRATEGY", new String[]{"CURRENT_USER_ID"});
		parameterMap.put(
			"_journal_ratingsCheckbox", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"permissinsAssignedToRolesCheckbox",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_ddmStructures-ddmTemplates-and-feeds",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_categoriesCheckbox",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_imagesCheckbox", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_comments", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_images", new String[]{Boolean.TRUE.toString()});
		parameterMap.put("doAsGroupId", new String[]{String.valueOf(groupId)});
		parameterMap.put(
			"DATA_STRATEGY", new String[]{"DATA_STRATEGY_MIRROR"});
		parameterMap.put(
			"_journal_tagsCheckbox", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"PORTLET_DATACheckbox", new String[]{Boolean.TRUE.toString()});
		parameterMap.put("tabs1", new String[]{"export_import"});
		parameterMap.put("PERMISSIONS", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_categories", new String[]{Boolean.TRUE.toString()});
		parameterMap.put("tabs2", new String[]{"import"});
		parameterMap.put("portletResource", new String[]{PortletKeys.JOURNAL});
		parameterMap.put(
			"_journal_ratings", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_web-contentCheckbox",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"PORTLET_METADATA_ALL", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"permissinsAssignedToRoles",
			new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"PORTLET_DATA", new String[]{Boolean.TRUE.toString()});
		parameterMap.put("groupId", new String[]{String.valueOf(groupId)});
		parameterMap.put("cmd", new String[]{"import"});
		parameterMap.put("plid", new String[]{String.valueOf(plid)});
		parameterMap.put("CATEGORIES", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"struts_action",
			new String[]{"/portlet_configuration/export_import"});
		parameterMap.put(
			"PORTLET_DATA_CONTROL_DEFAULT",
			new String[]{Boolean.FALSE.toString()});
		parameterMap.put(
			"CATEGORIESCheckbox", new String[]{Boolean.TRUE.toString()});

		return parameterMap;
	}

	private File _larFile;

}