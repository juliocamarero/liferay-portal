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

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Pavel Savinov
 */
@ExtendedObjectClassDefinition(
	category = "web-experience",
	scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.journal.configuration.JournalServiceConfiguration",
	localization = "content/Language",
	name = "journal.service.configuration.name"
)
public interface JournalServiceConfiguration {

	@Meta.AD(
		deflt = "&|\\'|@|\\\\|]|}|:|=|>|/|<|[|{|%|||+|#|`|?|\\\"|;|*|~",
		required = false
	)
	public String[] charactersblacklist();

	@Meta.AD(
		deflt = "ftl=com/liferay/journal/dependencies/error.ftl|vm=com/liferay/journal/dependencies/error.vm|xsl=com/liferay/journal/dependencies/error.xsl",
		required = false
	)
	public String[] errorTemplates();

	@Meta.AD(
		deflt = "com.liferay.journal.transformer.TokensTransformerListener|com.liferay.journal.transformer.ContentTransformerListener|com.liferay.journal.transformer.LocaleTransformerListener|com.liferay.journal.transformer.RegexTransformerListener|com.liferay.journal.transformer.ViewCounterTransformerListener",
		required = false
	)
	public String[] transformerListeners();

	@Meta.AD(deflt = "86400000", required = false)
	public long checkInterval();

	@Meta.AD(deflt = "", required = false)
	public String[] customTokenNames();

	@Meta.AD(deflt = "", required = false)
	public String[] customTokenValues();

	@Meta.AD(deflt = "true", required = false)
	public boolean articleCommentsEnabled();

	@Meta.AD(deflt = "true", required = false)
	public boolean databaseContentKeywordSearchEnabled();

	@Meta.AD(deflt = "true", required = false)
	public boolean expireAllArticleVersionsEnabled();

	@Meta.AD(deflt = "false", required = false)
	public boolean articleViewPermissionsCheckEnabled();

	@Meta.AD(deflt = "true", required = false)
	public boolean indexAllArticleVersionsEnabled();

	@Meta.AD(deflt = "true", required = false)
	public boolean folderIconCheckCountEnabled();

	@Meta.AD(deflt = "true", required = false)
	public boolean publishToLiveByDefaultEnabled();

	@Meta.AD(deflt = "true", required = false)
	public boolean versionHistoryByDefaultEnabled();

	@Meta.AD(deflt = "false", required = false)
	public boolean syncContentSearchOnStartup();

	@Meta.AD(deflt = "@page_break@", required = false)
	public String journalArticlePageBreakToken();

	@Meta.AD(deflt = "json", required = false)
	public String journalArticleStorageType();

}