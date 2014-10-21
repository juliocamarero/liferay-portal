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

package com.liferay.portlet.journalcontent.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.journal.NoSuchArticleException;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleResource;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalServiceUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;

/**
 * @author Brian Wing Shun Chan
 * @author Douglas Wong
 * @author Raymond Augé
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

	@Override
	public void processAction(
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		String articleId = getArticleId(actionRequest);

		setPreference(actionRequest, "articleId", articleId);

		long articleGroupId = getArticleGroupId(actionRequest);

		setPreference(
			actionRequest, "articleGroupId", String.valueOf(articleGroupId));

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

	protected long getArticleGroupId(PortletRequest portletRequest) {
		long assetEntryId = GetterUtil.getLong(
			getParameter(portletRequest, "assetEntryId"));

		AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(
			assetEntryId);

		return assetEntry.getGroupId();
	}

	protected String getArticleId(PortletRequest portletRequest)
		throws PortalException {

		long assetEntryId = GetterUtil.getLong(
			getParameter(portletRequest, "assetEntryId"));

		AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(
			assetEntryId);

		JournalArticle article = null;

		try {
			article = JournalArticleLocalServiceUtil.getArticle(
				assetEntry.getClassPK());
		}
		catch (NoSuchArticleException nsae1) {
			JournalArticleResource articleResource =
				JournalArticleResourceLocalServiceUtil.getArticleResource(
					assetEntry.getClassPK());

			article = JournalArticleLocalServiceUtil.getLatestArticle(
				articleResource.getGroupId(), articleResource.getArticleId(),
				WorkflowConstants.STATUS_APPROVED);
		}

		return StringUtil.toUpperCase(article.getArticleId());
	}

}