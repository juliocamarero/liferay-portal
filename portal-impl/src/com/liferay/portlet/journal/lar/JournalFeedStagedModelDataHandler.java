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

package com.liferay.portlet.journal.lar;

import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.journal.FeedTargetLayoutFriendlyUrlException;
import com.liferay.portlet.journal.model.JournalFeed;
import com.liferay.portlet.journal.service.JournalFeedLocalServiceUtil;
import com.liferay.portlet.journal.service.persistence.JournalFeedUtil;

import java.util.Map;

/**
 * @author Daniel Kocsis
 */
public class JournalFeedStagedModelDataHandler
	extends BaseStagedModelDataHandler<JournalFeed> {

	@Override
	public String getClassName() {
		return JournalFeed.class.getName();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, JournalFeed feed)
		throws Exception {

		if (!portletDataContext.isWithinDateRange(feed.getModifiedDate())) {
			return;
		}

		String path = getFeedPath(portletDataContext, feed);

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		feed = (JournalFeed)feed.clone();

		Element feedElement = feedsElement.addElement("feed");

		Group group = GroupLocalServiceUtil.getGroup(
			portletDataContext.getScopeGroupId());

		String newGroupFriendlyURL = group.getFriendlyURL().substring(1);

		String[] friendlyUrlParts = StringUtil.split(
			feed.getTargetLayoutFriendlyUrl(), '/');

		String oldGroupFriendlyURL = friendlyUrlParts[2];

		if (newGroupFriendlyURL.equals(oldGroupFriendlyURL)) {
			String targetLayoutFriendlyUrl = StringUtil.replaceFirst(
				feed.getTargetLayoutFriendlyUrl(),
				StringPool.SLASH + newGroupFriendlyURL + StringPool.SLASH,
				"/@data_handler_group_friendly_url@/");

			feed.setTargetLayoutFriendlyUrl(targetLayoutFriendlyUrl);
		}

		portletDataContext.addClassedModel(feedElement, path, feed, NAMESPACE);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, JournalFeed feed)
		throws Exception {

		String path = feedElement.attributeValue("path");

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		JournalFeed feed = (JournalFeed)portletDataContext.getZipEntryAsObject(
			path);

		long userId = portletDataContext.getUserId(feed.getUserUuid());

		JournalCreationStrategy creationStrategy =
			JournalCreationStrategyFactory.getInstance();

		long authorId = creationStrategy.getAuthorUserId(
			portletDataContext, feed);

		if (authorId != JournalCreationStrategy.USE_DEFAULT_USER_ID_STRATEGY) {
			userId = authorId;
		}

		Group group = GroupLocalServiceUtil.getGroup(
			portletDataContext.getScopeGroupId());

		String newGroupFriendlyURL = group.getFriendlyURL().substring(1);

		String[] friendlyUrlParts = StringUtil.split(
			feed.getTargetLayoutFriendlyUrl(), '/');

		String oldGroupFriendlyURL = friendlyUrlParts[2];

		if (oldGroupFriendlyURL.equals("@data_handler_group_friendly_url@")) {
			feed.setTargetLayoutFriendlyUrl(
				StringUtil.replace(
					feed.getTargetLayoutFriendlyUrl(),
					"@data_handler_group_friendly_url@", newGroupFriendlyURL));
		}

		String feedId = feed.getFeedId();
		boolean autoFeedId = false;

		if (Validator.isNumber(feedId) ||
			(JournalFeedUtil.fetchByG_F(
				portletDataContext.getScopeGroupId(), feedId) != null)) {

			autoFeedId = true;
		}

		Map<String, String> ddmStructureKeys =
			(Map<String, String>)portletDataContext.getNewPrimaryKeysMap(
				DDMStructure.class + ".structureKey");

		String parentDDMStructureKey = MapUtil.getString(
			ddmStructureKeys, feed.getStructureId(), feed.getStructureId());

		Map<String, String> ddmTemplateKeys =
			(Map<String, String>)portletDataContext.getNewPrimaryKeysMap(
				DDMTemplate.class + ".templateKey");

		String parentDDMTemplateKey = MapUtil.getString(
			ddmTemplateKeys, feed.getTemplateId(), feed.getTemplateId());
		String parentRenderDDMTemplateKey = MapUtil.getString(
			ddmTemplateKeys, feed.getRendererTemplateId(),
			feed.getRendererTemplateId());

		boolean addGroupPermissions = creationStrategy.addGroupPermissions(
			portletDataContext, feed);
		boolean addGuestPermissions = creationStrategy.addGuestPermissions(
			portletDataContext, feed);

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			feedElement, feed, NAMESPACE);

		serviceContext.setAddGroupPermissions(addGroupPermissions);
		serviceContext.setAddGuestPermissions(addGuestPermissions);

		JournalFeed importedFeed = null;

		try {
			if (portletDataContext.isDataStrategyMirror()) {
				JournalFeed existingFeed = JournalFeedUtil.fetchByUUID_G(
					feed.getUuid(), portletDataContext.getScopeGroupId());

				if (existingFeed == null) {
					serviceContext.setUuid(feed.getUuid());

					importedFeed = JournalFeedLocalServiceUtil.addFeed(
						userId, portletDataContext.getScopeGroupId(), feedId,
						autoFeedId, feed.getName(), feed.getDescription(),
						feed.getType(), parentDDMStructureKey,
						parentDDMTemplateKey, parentRenderDDMTemplateKey,
						feed.getDelta(), feed.getOrderByCol(),
						feed.getOrderByType(),
						feed.getTargetLayoutFriendlyUrl(),
						feed.getTargetPortletId(), feed.getContentField(),
						feed.getFeedFormat(), feed.getFeedVersion(),
						serviceContext);
				}
				else {
					importedFeed = JournalFeedLocalServiceUtil.updateFeed(
						existingFeed.getGroupId(), existingFeed.getFeedId(),
						feed.getName(), feed.getDescription(), feed.getType(),
						parentDDMStructureKey, parentDDMTemplateKey,
						parentRenderDDMTemplateKey, feed.getDelta(),
						feed.getOrderByCol(), feed.getOrderByType(),
						feed.getTargetLayoutFriendlyUrl(),
						feed.getTargetPortletId(), feed.getContentField(),
						feed.getFeedFormat(), feed.getFeedVersion(),
						serviceContext);
				}
			}
			else {
				importedFeed = JournalFeedLocalServiceUtil.addFeed(
					userId, portletDataContext.getScopeGroupId(), feedId,
					autoFeedId, feed.getName(), feed.getDescription(),
					feed.getType(), parentDDMStructureKey, parentDDMTemplateKey,
					parentRenderDDMTemplateKey, feed.getDelta(),
					feed.getOrderByCol(), feed.getOrderByType(),
					feed.getTargetLayoutFriendlyUrl(),
					feed.getTargetPortletId(), feed.getContentField(),
					feed.getFeedFormat(), feed.getFeedVersion(),
					serviceContext);
			}

			portletDataContext.importClassedModel(
				feed, importedFeed, NAMESPACE);

			if (!feedId.equals(importedFeed.getFeedId())) {
				if (_log.isWarnEnabled()) {
					StringBundler sb = new StringBundler(5);

					sb.append("A feed with the ID ");
					sb.append(feedId);
					sb.append(" already exists. The new generated ID is ");
					sb.append(importedFeed.getFeedId());
					sb.append(".");

					_log.warn(sb.toString());
				}
			}
		}
		catch (FeedTargetLayoutFriendlyUrlException ftlfurle) {
			if (_log.isWarnEnabled()) {
				StringBundler sb = new StringBundler(6);

				sb.append("A feed with the ID ");
				sb.append(feedId);
				sb.append(" cannot be imported because layout with friendly ");
				sb.append("URL ");
				sb.append(feed.getTargetLayoutFriendlyUrl());
				sb.append(" does not exist");

				_log.warn(sb.toString());
			}
		}
	}

}