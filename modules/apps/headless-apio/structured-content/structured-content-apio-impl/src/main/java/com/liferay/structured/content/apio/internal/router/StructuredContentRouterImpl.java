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

package com.liferay.structured.content.apio.internal.router;

import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalArticleConstants;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.structured.content.apio.architect.filter.Filter;
import com.liferay.structured.content.apio.architect.form.StructuredContentCreatorForm;
import com.liferay.structured.content.apio.architect.form.StructuredContentUpdaterForm;
import com.liferay.structured.content.apio.architect.model.JournalArticleWrapper;
import com.liferay.structured.content.apio.architect.router.StructuredContentRouter;
import com.liferay.structured.content.apio.internal.odata.ODataQueryMapper;

import java.io.Serializable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Implementation for API for the Structured Content Controller.
 *
 * @author Cristina González
 */
@Component(immediate = true)
public class StructuredContentRouterImpl implements StructuredContentRouter {

	@Override
	public JournalArticleWrapper addJournalArticle(
			long contentSpaceId,
			StructuredContentCreatorForm structuredContentCreatorForm,
			ThemeDisplay themeDisplay)
		throws PortalException {

		Locale locale = themeDisplay.getLocale();

		ServiceContext serviceContext =
			structuredContentCreatorForm.getServiceContext(contentSpaceId);

		JournalArticle journalArticle = _journalArticleService.addArticle(
			contentSpaceId, 0, 0, 0, null, true,
			structuredContentCreatorForm.getTitleMap(locale),
			structuredContentCreatorForm.getDescriptionMap(locale),
			structuredContentCreatorForm.getText(),
			structuredContentCreatorForm.getStructure(),
			structuredContentCreatorForm.getTemplate(), null,
			structuredContentCreatorForm.getDisplayDateMonth(),
			structuredContentCreatorForm.getDisplayDateDay(),
			structuredContentCreatorForm.getDisplayDateYear(),
			structuredContentCreatorForm.getDisplayDateHour(),
			structuredContentCreatorForm.getDisplayDateMinute(), 0, 0, 0, 0, 0,
			true, 0, 0, 0, 0, 0, true, true, null, serviceContext);

		return new JournalArticleWrapper(journalArticle, themeDisplay);
	}

	@Override
	public void deleteJournalArticle(long journalArticleId)
		throws PortalException {

		JournalArticle journalArticle = _journalArticleService.getArticle(
			journalArticleId);

		_journalArticleService.deleteArticle(
			journalArticle.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getArticleResourceUuid(), new ServiceContext());
	}

	@Override
	public PageItems<JournalArticleWrapper> getPageItems(
			Pagination pagination, long contentSpaceId, Filter filter,
			ThemeDisplay themeDisplay)
		throws PortalException {

		Indexer<JournalArticle> indexer = _indexerRegistry.nullSafeGetIndexer(
			JournalArticle.class);

		Hits hits = indexer.search(
			buildSearchContext(
				themeDisplay.getCompanyId(), contentSpaceId, filter,
				pagination.getStartPosition(), pagination.getEndPosition()));

		List<JournalArticleWrapper> journalArticleWrappers = Stream.of(
			hits.toList()
		).flatMap(
			List::stream
		).map(
			document -> convert(document, themeDisplay)
		).filter(
			journalArticleWrapper -> journalArticleWrapper != null
		).collect(
			Collectors.toList()
		);

		return new PageItems<>(journalArticleWrappers, hits.getLength());
	}

	@Override
	public JournalArticleWrapper updateJournalArticle(
			long journalArticleId,
			StructuredContentUpdaterForm structuredContentUpdaterForm,
			ThemeDisplay themeDisplay)
		throws PortalException {

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setScopeGroupId(structuredContentUpdaterForm.getGroup());

		JournalArticle journalArticle = _journalArticleService.updateArticle(
			structuredContentUpdaterForm.getUser(),
			structuredContentUpdaterForm.getGroup(), 0,
			String.valueOf(journalArticleId),
			structuredContentUpdaterForm.getVersion(),
			structuredContentUpdaterForm.getTitleMap(),
			structuredContentUpdaterForm.getDescriptionMap(),
			structuredContentUpdaterForm.getText(), null, serviceContext);

		return new JournalArticleWrapper(journalArticle, themeDisplay);
	}

	protected SearchContext buildSearchContext(
		long companyId, long groupId, Filter filter, int start, int end) {

		SearchContext searchContext = new SearchContext();

		Map<String, Serializable> attributes = new HashMap<>();

		attributes.put(
			Field.CLASS_NAME_ID, JournalArticleConstants.CLASSNAME_ID_DEFAULT);
		attributes.put(Field.STATUS, WorkflowConstants.STATUS_APPROVED);

		searchContext.setAttributes(attributes);

		searchContext.setCompanyId(companyId);
		searchContext.setEnd(end);
		searchContext.setGroupIds(new long[] {groupId});

		Query query = _oDataQueryMapper.map(filter, searchContext.getLocale());

		if (query != null) {
			BooleanClause booleanClause = BooleanClauseFactoryUtil.create(
				query, BooleanClauseOccur.MUST.getName());

			searchContext.setBooleanClauses(
				new BooleanClause[] {booleanClause});
		}

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);
		queryConfig.setSelectedFieldNames(
			Field.ARTICLE_ID, Field.SCOPE_GROUP_ID);

		searchContext.setStart(start);

		return searchContext;
	}

	protected JournalArticleWrapper convert(
		Document document, ThemeDisplay themeDisplay) {

		String articleId = document.get(Field.ARTICLE_ID);
		long groupId = GetterUtil.getLong(document.get(Field.SCOPE_GROUP_ID));

		try {
			JournalArticle journalArticle = _journalArticleService.fetchArticle(
				groupId, articleId);

			return new JournalArticleWrapper(journalArticle, themeDisplay);
		}
		catch (PortalException pe) {
			_log.error("Unable to obtain Journal Article", pe);
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		StructuredContentRouterImpl.class);

	@Reference
	private IndexerRegistry _indexerRegistry;

	@Reference
	private JournalArticleService _journalArticleService;

	@Reference
	private ODataQueryMapper _oDataQueryMapper;

}