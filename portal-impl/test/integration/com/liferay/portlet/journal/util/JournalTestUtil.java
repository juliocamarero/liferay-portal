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

package com.liferay.portlet.journal.util;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplateConstants;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalFolder;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalFolderLocalServiceUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Juan Fernández
 * @author Marcellus Tavares
 */
public class JournalTestUtil {

	public static JournalArticle addArticle(
			long groupId, long folderId, String name, String content)
		throws Exception {

		Map<Locale, String> titleMap = new HashMap<Locale, String>();

		Locale englishLocale = new Locale("en", "US");

		titleMap.put(englishLocale, name);

		Map<Locale, String> descriptionMap = new HashMap<Locale, String>();

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext();

		StringBundler sb = new StringBundler();

		sb.append("<?xml version=\"1.0\"?><root available-locales=");
		sb.append("\"en_US\" default-locale=\"en_US\">");
		sb.append("<static-content language-id=\"en_US\"><![CDATA[<p>");
		sb.append(content);
		sb.append("</p>]]>");
		sb.append("</static-content></root>");

		return JournalArticleLocalServiceUtil.addArticle(
			TestPropsValues.getUserId(), groupId, folderId, 0, 0,
			StringPool.BLANK, true, 1, titleMap, descriptionMap, sb.toString(),
			"general", null, null, null, 1, 1, 1965, 0, 0, 0, 0, 0, 0, 0, true,
			0, 0, 0, 0, 0, true, false, false, null, null, null, null,
			serviceContext);
	}

	public static JournalArticle addArticle(
			String content, String structureId, String templateId)
		throws Exception {

		Map<Locale, String> titleMap = new HashMap<Locale, String>();

		titleMap.put(Locale.US, "Test Article");

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setScopeGroupId(TestPropsValues.getGroupId());

		return JournalArticleLocalServiceUtil.addArticle(
			TestPropsValues.getUserId(), TestPropsValues.getGroupId(), 0, 0, 0,
			StringPool.BLANK, true, 0, titleMap, null, content, "general",
			structureId, templateId, null, 1, 1, 1965, 0, 0, 0, 0, 0, 0, 0,
			true, 0, 0, 0, 0, 0, true, false, false, null, null, null, null,
			serviceContext);
	}

	public static DDMStructure addDDMStructure(String xsd) throws Exception {
		Map<Locale, String> nameMap = new HashMap<Locale, String>();

		nameMap.put(Locale.US, "Test Structure");

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		return DDMStructureLocalServiceUtil.addStructure(
			TestPropsValues.getUserId(), TestPropsValues.getGroupId(),
			PortalUtil.getClassNameId(JournalArticle.class.getName()), nameMap,
			null, xsd, serviceContext);
	}

	public static DDMTemplate addDDMTemplate(
			long structureId, String xsl, String lang)
		throws Exception {

		Map<Locale, String> nameMap = new HashMap<Locale, String>();

		nameMap.put(Locale.US, "Test Template");

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		return DDMTemplateLocalServiceUtil.addTemplate(
			TestPropsValues.getUserId(), TestPropsValues.getGroupId(),
			PortalUtil.getClassNameId(DDMStructure.class.getName()),
			structureId, nameMap, null,
			DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, null, lang, xsl,
			serviceContext);
	}

	public static JournalFolder addFolder(
			long groupId, long parentFolderId, String name)
		throws Exception {

		JournalFolder folder = JournalFolderLocalServiceUtil.fetchFolder(
			groupId, name);

		if (folder != null) {
			return folder;
		}

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext();

		return JournalFolderLocalServiceUtil.addFolder(
			TestPropsValues.getUserId(), groupId, parentFolderId, name,
			"This is a test folder.", serviceContext);
	}

}