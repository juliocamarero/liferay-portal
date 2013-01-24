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

package com.liferay.portal.kernel.search;

import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.journal.model.JournalFolder;

/**
 * @author Eduardo Garcia
 */
public class FolderSearcher extends FacetedSearcher {

	public static Indexer getInstance() {
		return new FolderSearcher();
	}

	public String[] getClassNames() {
		return new String[] {
			DLFolder.class.getName(), BookmarksFolder.class.getName(),
			JournalFolder.class.getName()};
	}

	@Override
	protected BooleanQuery createFullQuery(
			BooleanQuery contextQuery, SearchContext searchContext)
		throws Exception {

		BooleanQuery searchQuery = BooleanQueryFactoryUtil.create(
			searchContext);

		String keywords = searchContext.getKeywords();

		if (Validator.isNotNull(keywords)) {
			addSearchLocalizedTerm(
				searchQuery, searchContext, Field.ASSET_CATEGORY_TITLES, false);

			searchQuery.addExactTerm(Field.ASSET_TAG_NAMES, keywords);
			searchQuery.addTerms(Field.KEYWORDS, keywords);
		}

		for (String entryClassName : getClassNames()) {
			Indexer indexer = IndexerRegistryUtil.getIndexer(entryClassName);

			if (indexer == null) {
				continue;
			}

			String searchEngineId = searchContext.getSearchEngineId();

			if (!searchEngineId.equals(indexer.getSearchEngineId())) {
				continue;
			}

			if (Validator.isNotNull(keywords)) {
				addSearchExpandoKeywords(
					searchQuery, searchContext, keywords, entryClassName);
			}

			indexer.postProcessSearchQuery(searchQuery, searchContext);

			for (IndexerPostProcessor indexerPostProcessor :
					indexer.getIndexerPostProcessors()) {

				indexerPostProcessor.postProcessSearchQuery(
					searchQuery, searchContext);
			}
		}

		BooleanQuery fullQuery = BooleanQueryFactoryUtil.create(searchContext);

		fullQuery.add(contextQuery, BooleanClauseOccur.MUST);

		if (searchQuery.hasClauses()) {
			fullQuery.add(searchQuery, BooleanClauseOccur.MUST);
		}

		BooleanClause[] booleanClauses = searchContext.getBooleanClauses();

		if (booleanClauses != null) {
			for (BooleanClause booleanClause : booleanClauses) {
				fullQuery.add(
					booleanClause.getQuery(),
					booleanClause.getBooleanClauseOccur());
			}
		}

		for (String entryClassName : getClassNames()) {
			Indexer indexer = IndexerRegistryUtil.getIndexer(entryClassName);

			if (indexer == null) {
				continue;
			}

			String searchEngineId = searchContext.getSearchEngineId();

			if (!searchEngineId.equals(indexer.getSearchEngineId())) {
				continue;
			}

			for (IndexerPostProcessor indexerPostProcessor :
					indexer.getIndexerPostProcessors()) {

				indexerPostProcessor.postProcessFullQuery(
					fullQuery, searchContext);
			}
		}

		long[] folderIds = searchContext.getFolderIds();

		BooleanQuery entryClassNameQuery = BooleanQueryFactoryUtil.create(
			searchContext);

		for (String entryClassName : getClassNames()) {
			entryClassNameQuery.addTerm(Field.ENTRY_CLASS_NAME, entryClassName);
		}

		contextQuery.add(entryClassNameQuery, BooleanClauseOccur.MUST);

		BooleanQuery entryClassPKQuery = BooleanQueryFactoryUtil.create(
			searchContext);

		for (long folderId : folderIds) {
			entryClassPKQuery.addTerm(Field.ENTRY_CLASS_PK, folderId);
		}

		contextQuery.add(entryClassPKQuery, BooleanClauseOccur.MUST);

		fullQuery.add(contextQuery, BooleanClauseOccur.MUST);

		return fullQuery;
	}

	protected boolean isFilterSearch(SearchContext searchContext) {
		for (String entryClassName : getClassNames()) {
			Indexer indexer = IndexerRegistryUtil.getIndexer(entryClassName);

			if (indexer == null) {
				continue;
			}

			if (indexer.isFilterSearch()) {
				return true;
			}
		}

		return super.isFilterSearch();
	}

}