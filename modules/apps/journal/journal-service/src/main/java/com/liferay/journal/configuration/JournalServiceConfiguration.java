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

package com.liferay.journal.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * @author Juergen Kappler
 */
@Meta.OCD(id = "com.liferay.journal.configuration.JournalServiceConfiguration")
public interface JournalServiceConfiguration {

	@Meta.AD(
		deflt = "&,\\',@,\\\\,],},:,=,>,/,<,[,{,%,|,+,#,`,?,\\\",;,*,~",
		required = false
	)
	public String charBlackList();

	@Meta.AD(required = false)
	public String emailFromAddress();

	@Meta.AD(required = false)
	public String emailFromName();

	@Meta.AD(deflt = "true", required = false)
	public boolean journalArticleCommentsEnabled();

	@Meta.AD(deflt = "true", required = false)
	public boolean journalArticleDatabaseKeywordSearchContent();

	@Meta.AD(deflt = "true", required = false)
	public boolean journalArticleExpireAllVersions();

	@Meta.AD(deflt = "true", required = false)
	public boolean journalArticlesIndexAllVersions();

	@Meta.AD(deflt = "json", required = false)
	public String journalArticleStorageType();

	@Meta.AD(deflt = "@page_break@", required = false)
	public String journalArticleTokenPageBreak();

	@Meta.AD(deflt = "true", required = false)
	public boolean journalArticleViewPermissionCheckEnabled();

	@Meta.AD(deflt = "true", required = false)
	public boolean journalFolderIconCheckCount();

	@Meta.AD(
		deflt = "com.liferay.journal.lar.JournalCreationStrategyImpl",
		required = false
	)
	public String larCreationStrategy();

	@Meta.AD(
		deflt = "com_liferay_journal_web_portlet_JournalPortlet",
		required = false
	)
	public String serviceConfiguratorPortletIds();

	@Meta.AD(deflt = "false", required = false)
	public boolean syncContentSearchOnStartup();

	@Meta.AD(required = false)
	public long termsOfUseJournalArticleGroupId();

	@Meta.AD(required = false)
	public long termsOfUseJournalArticleId();

}