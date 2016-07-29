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
		description = "specifcy-characters-that-are-not-allowed-in-journal-folder-names",
		required = false
	)
	public String[] charactersblacklist();

	@Meta.AD(
		deflt = "<div class=\"journal-template-error\">\n\t<span class=\"alert alert-error\">\n\t\t<@liferay.language key=\"an-error-occurred-while-processing-the-template\" />\n\t</span>\n\n\t<pre>${exception?html}</pre>\n\n\t<div class=\"scroll-pane\">\n\t\t<div class=\"inner-scroll-pane\">\n\t\t\t<#assign lines = stringUtil.split(script, \"\\n\") />\n\n\t\t\t<#list lines as curLine>\n\t\t\t\t<#assign css = ''>\n\n\t\t\t\t<#if line?exists && line == (curLine_index + 1)>\n\t\t\t\t\t<#assign css = ' class=\"error-line\"' />\n\t\t\t\t</#if>\n\n\t\t\t\t<pre${css}><span>${curLine_index + 1}</span>${curLine?html}&nbsp;</pre>\n\t\t\t</#list>\n\t\t</div>\n\t</div>\n</div>",
		required = false
	)
	public String errorTemplateFTL();

	@Meta.AD(
		deflt = "#set ($companyId = $getterUtil.getLong($companyId))\n\n<div class=\"journal-template-error\">\n\t<span class=\"alert alert-error\">\n\t\t#language (\"an-error-occurred-while-processing-the-template\")\n\t</span>\n\n\t<pre>$htmlUtil.escape($exception)</pre>\n\n\t<div class=\"scroll-pane\">\n\t\t<div class=\"inner-scroll-pane\">\n\t\t\t#set ($lines = $script.split(\"\\n\"))\n\n\t\t\t#foreach ($curLine in $lines)\n\t\t\t\t#set ($css = '')\n\n\t\t\t\t#if ($line == $velocityCount)\n\t\t\t\t\t#set ($css = ' class=\"error-line\"')\n\t\t\t\t#end\n\n\t\t\t\t<pre${css}><span>${velocityCount}</span>${htmlUtil.escape($curLine)}&nbsp;</pre>\n\t\t\t#end\n\t\t</div>\n\t</div>\n</div>",
		required = false
	)
	public String errorTemplateVM();

	@Meta.AD(
		deflt = "<?xml version=\"1.0\"?>\n\n<xsl:stylesheet\n\txmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"\n\txmlns:languageUtil=\"xalan://com.liferay.portal.kernel.language.LanguageUtil\"\n\txmlns:str=\"http://exslt.org/strings\"\n\txmlns:xalan=\"http://xml.apache.org/xalan\"\n\texclude-result-prefixes=\"xalan\"\n\textension-element-prefixes=\"languageUtil str xalan\">\n\n\t<xsl:output method=\"html\" omit-xml-declaration=\"yes\" />\n\n\t<xsl:param name=\"companyId\" />\n\t<xsl:param name=\"exception\" />\n\t<xsl:param name=\"line\" />\n\t<xsl:param name=\"locale\" />\n\t<xsl:param name=\"script\" />\n\n\t<xsl:template match=\"/\">\n\t\t<div class=\"journal-template-error\">\n\t\t\t<span class=\"alert alert-error\">\n\t\t\t\t<xsl:value-of select=\"languageUtil:get($locale, 'an-error-occurred-while-processing-the-template')\" />\n\t\t\t</span>\n\n\t\t\t<pre>\n\t\t\t\t<xsl:value-of select=\"$exception\" />\n\t\t\t</pre>\n\n\t\t\t<br/>\n\n\t\t\t<div class=\"scroll-pane\">\n\t\t\t\t<div class=\"inner-scroll-pane\">\n\t\t\t\t\t<xsl:for-each select=\"str:split($script, '&#xa;')\">\n\t\t\t\t\t\t<pre>\n\t\t\t\t\t\t\t<xsl:if test=\"$line = position()\">\n\t\t\t\t\t\t\t\t<xsl:attribute name=\"class\">\n\t\t\t\t\t\t\t\t\t<xsl:text>error-line</xsl:text>\n\t\t\t\t\t\t\t\t</xsl:attribute>\n\t\t\t\t\t\t\t</xsl:if>\n\t\t\t\t\t\t\t<span>\n\t\t\t\t\t\t\t\t<xsl:value-of select=\"position()\" />\n\t\t\t\t\t\t\t</span><xsl:value-of select=\".\" />\n\t\t\t\t\t\t\t<xsl:text>&#160;</xsl:text>\n\t\t\t\t\t\t</pre>\n\t\t\t\t\t</xsl:for-each>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t</div>\n\t</xsl:template>\n</xsl:stylesheet>",
		required = false
	)
	public String errorTemplateXSL();

	@Meta.AD(deflt = "86400000", required = false)
	public long checkInterval();

	@Meta.AD(
		deflt = "", description = "journal-article-custom-token-names",
		required = false
	)
	public String[] customTokenNames();

	@Meta.AD(
		deflt = "", description = "journal-article-custom-token-values",
		required = false
	)
	public String[] customTokenValues();

	@Meta.AD(
		deflt = "true", description = "journal-article-comments",
		required = false
	)
	public boolean articleCommentsEnabled();

	@Meta.AD(
		deflt = "true",
		description = "journal-article-database-search-content-keywords",
		required = false
	)
	public boolean databaseContentKeywordSearchEnabled();

	@Meta.AD(
		deflt = "true", description = "journal-article-expire-all-versions",
		required = false
	)
	public boolean expireAllArticleVersionsEnabled();

	@Meta.AD(
		deflt = "false", description = "journal-article-view-permission-check",
		required = false
	)
	public boolean articleViewPermissionsCheckEnabled();

	@Meta.AD(
		deflt = "true", description = "journal-article-index-all-versions",
		required = false
	)
	public boolean indexAllArticleVersionsEnabled();

	@Meta.AD(
		deflt = "true", description = "journal-folder-icon-check-count",
		required = false
	)
	public boolean folderIconCheckCountEnabled();

	@Meta.AD(
		deflt = "true", description = "publish-to-live-by-default",
		required = false
	)
	public boolean publishToLiveByDefaultEnabled();

	@Meta.AD(
		deflt = "true", description = "publish-version-history-by-default",
		required = false
	)
	public boolean versionHistoryByDefaultEnabled();

	@Meta.AD(
		deflt = "false", description = "sync-content-search-on-startup",
		required = false
	)
	public boolean syncContentSearchOnStartup();

	@Meta.AD(
		deflt = "@page_break@",
		description = "journal-article-token-page-break", required = false
	)
	public String journalArticlePageBreakToken();

	@Meta.AD(
		deflt = "json", description = "journal-article-storage-type",
		required = false
	)
	public String journalArticleStorageType();

}