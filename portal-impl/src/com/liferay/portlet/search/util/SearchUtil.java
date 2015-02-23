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

package com.liferay.portlet.search.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.OpenSearch;
import com.liferay.portal.kernel.search.OpenSearchRegistryUtil;
import com.liferay.portal.kernel.search.OpenSearchUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.GroupServiceUtil;
import com.liferay.util.xml.XMLFormatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.Action;

/**
 * @author Eudaldo Alonso
 */
public class SearchUtil extends Action {

	public static Tuple getElements(
		String xml, String className, int inactiveGroupsCount) {

		List<Element> result = new ArrayList();
		int total = 0;

		try {
			xml = XMLFormatter.stripInvalidChars(xml);

			Document document = SAXReaderUtil.read(xml);

			Element rootElement = document.getRootElement();

			List<Element> entries = rootElement.elements("entry");

			total = GetterUtil.getInteger(
				rootElement.elementText(
					OpenSearchUtil.getQName(
						"totalResults", OpenSearchUtil.OS_NAMESPACE)));

			for (Element element : entries) {
				try {

					// Group id

					long entryScopeGroupId = GetterUtil.getLong(
						element.elementText(
							OpenSearchUtil.getQName(
								"scopeGroupId",
								OpenSearchUtil.LIFERAY_NAMESPACE)));

					if (Validator.isNotNull(entryScopeGroupId) &&
						(inactiveGroupsCount > 0)) {

						Group entryGroup = GroupServiceUtil.getGroup(
							entryScopeGroupId);

						if (entryGroup.isLayout()) {
							entryGroup = GroupLocalServiceUtil.getGroup(
								entryGroup.getParentGroupId());
						}

						if (!entryGroup.isActive()) {
							total--;

							continue;
						}
					}

					result.add(element);
				}
				catch (Exception e) {
					_log.error(
						"Error retrieving individual search result of type " +
							className,
						e);

					total--;
				}
			}
		}
		catch (Exception e) {
			_log.error("Error displaying content of type " + className, e);
		}

		return new Tuple(result, total);
	}

	public static List<OpenSearch> getOpenSearchInstances(
		String primarySearch) {

		List<OpenSearch> openSearchInstances =
			OpenSearchRegistryUtil.getOpenSearchInstances();

		Iterator<OpenSearch> itr = openSearchInstances.iterator();

		while (itr.hasNext()) {
			OpenSearch openSearch = itr.next();

			if (!openSearch.isEnabled()) {
				itr.remove();
			}
		}

		if (Validator.isNotNull(primarySearch)) {
			for (int i = 0; i < openSearchInstances.size(); i++) {
				OpenSearch openSearch = openSearchInstances.get(i);

				if (primarySearch.equals(openSearch.getClassName())) {
					if (i != 0) {
						openSearchInstances.remove(i);

						openSearchInstances.add(0, openSearch);
					}

					break;
				}
			}
		}

		return openSearchInstances;
	}

	private static final Log _log = LogFactoryUtil.getLog(SearchUtil.class);

}