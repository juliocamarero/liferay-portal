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

		// Add site and layout

		Layout layoutFrom = addGroupAndLayout();

		// Add a basic content

		JournalArticle exportedJournalArticle = JournalTestUtil.addArticle(
			layoutFrom.getGroupId(), "Test Article", "My test content");

		String exportedResourceUuid =
			exportedJournalArticle.getArticleResourceUuid();

		// Export content

		Map<String, String[]> parameterMap = getExportParameterMap(layoutFrom);

		String portletId = PortletKeys.JOURNAL;

		_larFile = LayoutLocalServiceUtil.exportPortletInfoAsFile(
			layoutFrom.getPlid(), layoutFrom.getGroupId(), portletId,
			parameterMap, null, null);

		// Remove the first site and all its data

		GroupLocalServiceUtil.deleteGroup(layoutFrom.getGroupId());

		// Add another site and layout

		Layout layoutTo = addGroupAndLayout();

		// Import the contents from the original site

		PortletImporter portletImporter = new PortletImporter();

		parameterMap = getImportParameterMap(layoutTo);

		portletImporter.importPortletInfo(
			TestPropsValues.getUserId(), layoutTo.getPlid(),
			layoutTo.getGroupId(), portletId, parameterMap, _larFile);

		// Verify there's a new article in the new site

		int articlesCount = JournalArticleLocalServiceUtil.getArticlesCount(
			layoutTo.getGroupId());

		Assert.assertTrue(articlesCount > 0);

		// Verify the same article exists in the new site

		JournalArticleResource importedJournalArticleResource =
			JournalArticleResourceLocalServiceUtil.fetchArticleResource(
				exportedResourceUuid, layoutTo.getGroupId());

		Assert.assertNotNull(importedJournalArticleResource);
	}

	@Test
	public void testExportImportStructuredJournalContent() throws Exception {

		// Add site and layout

		Layout layoutFrom = addGroupAndLayout();

		long plid = layoutFrom.getPlid();

		// Add a structure

		DDMStructure exportedDDMStructure = JournalTestUtil.addDDMStructure(
			layoutFrom.getGroupId());

		// Add a template

		DDMTemplate exportedDDMTemplate = JournalTestUtil.addDDMTemplate(
			layoutFrom.getGroupId(), exportedDDMStructure.getStructureId());

		// Add a content

		String content = JournalTestUtil.getSampleStructuredContent();

		JournalArticle exportedJournalArticle = JournalTestUtil.addArticle(
			layoutFrom.getGroupId(), content,
			exportedDDMStructure.getStructureKey(),
			exportedDDMTemplate.getTemplateKey());

		String exportedResourceUuid =
			exportedJournalArticle.getArticleResourceUuid();

		// Export content

		Map<String, String[]> parameterMap = getExportParameterMap(layoutFrom);

		String portletId = PortletKeys.JOURNAL;

		_larFile = LayoutLocalServiceUtil.exportPortletInfoAsFile(
			layoutFrom.getPlid(), layoutFrom.getGroupId(), portletId,
			parameterMap, null, null);

		// Remove the first site and all its data

		GroupLocalServiceUtil.deleteGroup(layoutFrom.getGroupId());

		// Add another site and layout

		Layout layoutTo = addGroupAndLayout();

		// Import the contents from the original site

		PortletImporter portletImporter = new PortletImporter();

		parameterMap = getImportParameterMap(layoutTo);

		portletImporter.importPortletInfo(
			TestPropsValues.getUserId(), layoutTo.getPlid(),
			layoutTo.getGroupId(), portletId, parameterMap, _larFile);

		// Verify there's a new article in the new site

		int articlesCount = JournalArticleLocalServiceUtil.getArticlesCount(
			layoutTo.getGroupId());

		Assert.assertTrue(articlesCount > 0);

		// Verify the same article exists in the new site

		JournalArticleResource importedJournalArticleResource =
			JournalArticleResourceLocalServiceUtil.fetchArticleResource(
				exportedResourceUuid, layoutTo.getGroupId());

		Assert.assertNotNull(importedJournalArticleResource);

		JournalArticle importedJournalArticle =
			JournalArticleLocalServiceUtil.getArticle(
				layoutTo.getGroupId(),
				importedJournalArticleResource.getArticleId());

		Assert.assertNotNull(importedJournalArticle);

		// Verify the structure exists in the new site

		DDMStructure importedDDMStructure =
			DDMStructureLocalServiceUtil.fetchStructure(
				exportedDDMStructure.getUuid(), layoutTo.getGroupId());

		Assert.assertNotNull(importedDDMStructure);

		// Verify the template exists in the new site

		DDMTemplate importedDDMTemplate =
			DDMTemplateLocalServiceUtil.fetchTemplate(
				exportedDDMTemplate.getUuid(), layoutTo.getGroupId());

		Assert.assertNotNull(importedDDMTemplate);

		// Check structure - template relationship

		Assert.assertEquals(
			importedDDMTemplate.getClassPK(),
			importedDDMStructure.getStructureId());

		// Check article - structure relationship

		Assert.assertEquals(
			importedJournalArticle.getStructureId(),
			importedDDMStructure.getStructureKey());

		// Check article - template relationship

		Assert.assertEquals(
			importedJournalArticle.getTemplateId(),
			importedDDMTemplate.getTemplateKey());
	}

	protected Layout addGroupAndLayout() throws Exception {
		Group group = ServiceTestUtil.addGroup();

		return ServiceTestUtil.addLayout(group.getGroupId(), "Site  layout");
	}

	protected Map<String, String[]> getExportParameterMap(Layout layout)
		throws Exception {

		Map<String, String[]> parameterMap = new HashMap<String, String[]>();

		parameterMap.put(
			"_journal_commentsCheckbox",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put("etag", new String[] {"0"});
		parameterMap.put(
			"_journal_web-content", new String[]{Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_embedded-assets", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"PORTLET_DATA_15Checkbox", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_embedded-assetsCheckbox",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_images", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"PORTLET_DATA_15", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_categories", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_ratings", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"PORTLET_METADATA_ALL", new String[] {Boolean.TRUE.toString()});
		parameterMap.put("strip", new String[] {"0"});
		parameterMap.put("cmd", new String[] {"export"});
		parameterMap.put(
			"plid", new String[] {String.valueOf(layout.getPlid())});
		parameterMap.put(
			"struts_action",
			new String[] {"/portlet_configuration/export_import"});
		parameterMap.put("range", new String[] {"fromLastPublishDate"});
		parameterMap.put(
			"_journal_tags", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_ratingsCheckbox", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"permissinsAssignedToRolesCheckbox",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_version-historyCheckbox",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_version-history", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_ddmStructures-ddmTemplates-and-feeds",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_categoriesCheckbox",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_imagesCheckbox", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_comments", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"doAsGroupId",
			new String[]{String.valueOf(layout.getGroup().getGroupId())});
		parameterMap.put(
			"_journal_tagsCheckbox", new String[] {Boolean.TRUE.toString()});
		parameterMap.put("tabs1", new String[] {"export_import"});
		parameterMap.put("tabs2", new String[] {"export"});
		parameterMap.put(
			"PERMISSIONS", new String[] {Boolean.FALSE.toString()});
		parameterMap.put("portletResource", new String[] {PortletKeys.JOURNAL});
		parameterMap.put(
			"_journal_web-contentCheckbox",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"permissinsAssignedToRoles",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"groupId",
			new String[]{String.valueOf(layout.getGroup().getGroupId())});
		parameterMap.put("CATEGORIES", new String[] {Boolean.FALSE.toString()});
		parameterMap.put(
			"PORTLET_DATA_CONTROL_DEFAULT",
			new String[] {Boolean.FALSE.toString()});

		return parameterMap;
	}

	protected Map<String, String[]> getImportParameterMap(Layout layout)
		throws Exception {

		Map<String, String[]> parameterMap = new HashMap<String, String[]>();

		parameterMap.put(
			"_journal_commentsCheckbox",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"PERMISSIONSCheckbox", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_web-content", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_tags", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"DELETE_PORTLET_DATA", new String[] {Boolean.FALSE.toString()});
		parameterMap.put("USER_ID_STRATEGY", new String[] {"CURRENT_USER_ID"});
		parameterMap.put(
			"_journal_ratingsCheckbox", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"permissinsAssignedToRolesCheckbox",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_ddmStructures-ddmTemplates-and-feeds",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_categoriesCheckbox",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_imagesCheckbox", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_comments", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_images", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"doAsGroupId", new String[] {String.valueOf(layout.getGroupId())});
		parameterMap.put(
			"DATA_STRATEGY", new String[] {"DATA_STRATEGY_MIRROR"});
		parameterMap.put(
			"_journal_tagsCheckbox", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"PORTLET_DATACheckbox", new String[] {Boolean.TRUE.toString()});
		parameterMap.put("tabs1", new String[] {"export_import"});
		parameterMap.put("PERMISSIONS", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_categories", new String[] {Boolean.TRUE.toString()});
		parameterMap.put("tabs2", new String[] {"import"});
		parameterMap.put("portletResource", new String[] {PortletKeys.JOURNAL});
		parameterMap.put(
			"_journal_ratings", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"_journal_web-contentCheckbox",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"PORTLET_METADATA_ALL", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"permissinsAssignedToRoles",
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"PORTLET_DATA", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"groupId", new String[] {String.valueOf(layout.getGroupId())});
			parameterMap.put("cmd", new String[] {"import"});
		parameterMap.put(
			"plid", new String[] {String.valueOf(layout.getPlid())});
		parameterMap.put("CATEGORIES", new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			"struts_action",
			new String[] {"/portlet_configuration/export_import"});
		parameterMap.put(
			"PORTLET_DATA_CONTROL_DEFAULT",
			new String[] {Boolean.FALSE.toString()});
		parameterMap.put(
			"CATEGORIESCheckbox", new String[] {Boolean.TRUE.toString()});

		return parameterMap;
	}

	private File _larFile;

}