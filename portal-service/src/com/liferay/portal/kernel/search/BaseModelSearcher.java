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

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * László Csontos
 */
public abstract class BaseModelSearcher<T extends BaseModel<T>>
	extends BaseSearcher {

	public BaseModelSearcher(String[] classNames) {
		this(classNames, null);
	}

	public BaseModelSearcher(String[] classNames, String[] selectedFieldNames) {
		_selectedFieldNames = selectedFieldNames;
	}

	public List<T> processHits(Hits hits) {
		Document[] documents = hits.getDocs();

		List<T> models = new ArrayList<T>(hits.getLength());

		for (Document document : documents) {
			try {
				T model = getModel(document);

				if (model != null) {
					models.add(model);
				}
				else {
					long companyId = GetterUtil.getLong(
						document.get(Field.COMPANY_ID));

					delete(companyId, document.get(Field.UID));
				}
			}
			catch (Exception e) {
				_log.error(e);
			}
		}

		return models;
	}

	public BaseModelSearchResult<T> searchModel(SearchContext searchContext)
		throws SearchException {

		if (!ArrayUtil.isEmpty(_selectedFieldNames)) {
			QueryConfig queryConfig = searchContext.getQueryConfig();

			queryConfig.setSelectedFieldNames(_selectedFieldNames);
		}

		Hits hits = search(searchContext);

		List<T> models = processHits(hits);

		return new BaseModelSearchResult<T>(models, models.size());
	}

	protected abstract T getModel(Document document) throws SystemException;

	protected String[] getSelectedFieldNames() {
		return _selectedFieldNames;
	}

	private static Log _log = LogFactoryUtil.getLog(BaseModelSearcher.class);

	private String[] _selectedFieldNames;

}