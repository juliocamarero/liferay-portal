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

package com.liferay.portal.verify;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portlet.wiki.model.WikiNode;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.model.WikiPageConstants;
import com.liferay.portlet.wiki.service.WikiNodeLocalServiceUtil;
import com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil;
import com.liferay.portlet.wiki.service.persistence.WikiPageUtil;

import java.util.Date;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
public class VerifyWiki extends VerifyProcess {

	@Override
	protected void doVerify() throws Exception {
		verifyCreateDate();
		verifyNoAssetPages();
	}

	protected void verifyCreateDate() throws Exception {
		List<WikiNode> nodes = WikiNodeLocalServiceUtil.getWikiNodes(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (WikiNode node : nodes) {
			List<WikiPage> firstPages = WikiPageUtil.findByN_V(
				node.getNodeId(), WikiPageConstants.VERSION_DEFAULT);

			for (WikiPage firstPage : firstPages) {
				Date createDate = firstPage.getCreateDate();

				List<WikiPage> pageVersions = WikiPageLocalServiceUtil.getPages(
					firstPage.getNodeId(), firstPage.getTitle(),
					QueryUtil.ALL_POS, QueryUtil.ALL_POS);

				for (WikiPage pageVersion : pageVersions) {
					Date date = pageVersion.getCreateDate();

					if (!createDate.equals(date)) {
						pageVersion.setCreateDate(createDate);

						WikiPageLocalServiceUtil.updateWikiPage(pageVersion);
					}
				}
			}
		}
	}

	protected void verifyNoAssetPages() throws Exception {
		List<WikiPage> pages = WikiPageLocalServiceUtil.getNoAssetPages();

		if (_log.isDebugEnabled()) {
			_log.debug("Processing " + pages.size() + " pages with no asset");
		}

		for (WikiPage page : pages) {
			try {
				WikiPageLocalServiceUtil.updateAsset(
					page.getUserId(), page, null, null, null);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to update asset for page " + page.getPageId() +
							": " + e.getMessage());
				}
			}
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Assets verified for pages");
		}
	}

	private static Log _log = LogFactoryUtil.getLog(VerifyWiki.class);

}