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

package com.liferay.portlet.messageboards.lar;

import com.liferay.portal.kernel.lar.BasePortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.messageboards.model.MBBan;
import com.liferay.portlet.messageboards.model.MBCategory;
import com.liferay.portlet.messageboards.model.MBCategoryConstants;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBThreadFlag;
import com.liferay.portlet.messageboards.service.MBBanLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBCategoryLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBStatsUserLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadLocalServiceUtil;
import com.liferay.portlet.messageboards.service.persistence.MBBanUtil;
import com.liferay.portlet.messageboards.service.persistence.MBCategoryUtil;
import com.liferay.portlet.messageboards.service.persistence.MBMessageUtil;

import java.util.List;

import javax.portlet.PortletPreferences;

/**
 * @author Bruno Farache
 * @author Raymond Augé
 */
public class MBPortletDataHandler extends BasePortletDataHandler {

	public static final String NAMESPACE = "message_board";

	public MBPortletDataHandler() {
		setAlwaysExportable(true);
		setExportControls(
			new PortletDataHandlerBoolean(
				NAMESPACE, "categories-and-messages", true, true),
			new PortletDataHandlerBoolean(NAMESPACE, "thread-flags"),
			new PortletDataHandlerBoolean(NAMESPACE, "user-bans"));
		setExportMetadataControls(
			new PortletDataHandlerBoolean(
				NAMESPACE, "message-board-messages", true,
				new PortletDataHandlerControl[] {
					new PortletDataHandlerBoolean(NAMESPACE, "attachments"),
					new PortletDataHandlerBoolean(NAMESPACE, "ratings"),
					new PortletDataHandlerBoolean(NAMESPACE, "tags")
				}));
		setPublishToLiveByDefault(
			PropsValues.MESSAGE_BOARDS_PUBLISH_TO_LIVE_BY_DEFAULT);
	}

	@Override
	protected PortletPreferences doDeleteData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		if (portletDataContext.addPrimaryKey(
				MBPortletDataHandler.class, "deleteData")) {

			return portletPreferences;
		}

		MBBanLocalServiceUtil.deleteBansByGroupId(
			portletDataContext.getScopeGroupId());

		MBCategoryLocalServiceUtil.deleteCategories(
			portletDataContext.getScopeGroupId());

		MBStatsUserLocalServiceUtil.deleteStatsUsersByGroupId(
			portletDataContext.getScopeGroupId());

		MBThreadLocalServiceUtil.deleteThreads(
			portletDataContext.getScopeGroupId(),
			MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID);

		return portletPreferences;
	}

	@Override
	protected String doExportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		portletDataContext.addPermissions(
			"com.liferay.portlet.messageboards",
			portletDataContext.getScopeGroupId());

		Element rootElement = addExportRootElement();

		rootElement.addAttribute(
			"group-id", String.valueOf(portletDataContext.getScopeGroupId()));

		Element categoriesElement = rootElement.addElement("categories");
		Element messagesElement = rootElement.addElement("messages");
		Element threadFlagsElement = rootElement.addElement("thread-flags");
		Element userBansElement = rootElement.addElement("user-bans");

		List<MBCategory> categories = MBCategoryUtil.findByGroupId(
			portletDataContext.getScopeGroupId());

		for (MBCategory category : categories) {
			exportCategory(
				portletDataContext, categoriesElement, messagesElement,
				threadFlagsElement, category);
		}

		List<MBMessage> messages = MBMessageUtil.findByG_C(
			portletDataContext.getScopeGroupId(),
			MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID);

		for (MBMessage message : messages) {
			exportMessage(
				portletDataContext, categoriesElement, messagesElement,
				threadFlagsElement, message);
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "user-bans")) {
			List<MBBan> bans = MBBanUtil.findByGroupId(
				portletDataContext.getScopeGroupId());

			for (MBBan ban : bans) {
				exportBan(portletDataContext, userBansElement, ban);
			}
		}

		return rootElement.formattedString();
	}

	@Override
	protected PortletPreferences doImportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, String data)
		throws Exception {

		portletDataContext.importPermissions(
			"com.liferay.portlet.messageboards",
			portletDataContext.getSourceGroupId(),
			portletDataContext.getScopeGroupId());

		Document document = SAXReaderUtil.read(data);

		Element rootElement = document.getRootElement();

		Element categoriesElement = rootElement.element("categories");

		for (Element categoryElement : categoriesElement.elements("category")) {
			String path = categoryElement.attributeValue("path");

			if (!portletDataContext.isPathNotProcessed(path)) {
				continue;
			}

			MBCategory category =
				(MBCategory)portletDataContext.getZipEntryAsObject(path);

			importCategory(portletDataContext, path, category);
		}

		Element messagesElement = rootElement.element("messages");

		for (Element messageElement : messagesElement.elements("message")) {
			String path = messageElement.attributeValue("path");

			if (!portletDataContext.isPathNotProcessed(path)) {
				continue;
			}

			MBMessage message =
				(MBMessage)portletDataContext.getZipEntryAsObject(path);

			importMessage(portletDataContext, messageElement, message);
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "thread-flags")) {
			Element threadFlagsElement = rootElement.element("thread-flags");

			for (Element threadFlagElement :
					threadFlagsElement.elements("thread-flag")) {

				String path = threadFlagElement.attributeValue("path");

				if (!portletDataContext.isPathNotProcessed(path)) {
					continue;
				}

				MBThreadFlag threadFlag =
					(MBThreadFlag)portletDataContext.getZipEntryAsObject(path);

				importThreadFlag(
					portletDataContext, threadFlagElement, threadFlag);
			}
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "user-bans")) {
			Element userBansElement = rootElement.element("user-bans");

			for (Element userBanElement :
					userBansElement.elements("user-ban")) {

				String path = userBanElement.attributeValue("path");

				if (!portletDataContext.isPathNotProcessed(path)) {
					continue;
				}

				MBBan ban = (MBBan)portletDataContext.getZipEntryAsObject(path);

				importBan(portletDataContext, userBanElement, ban);
			}
		}

		return null;
	}

	protected void exportParentCategory(
			PortletDataContext portletDataContext, Element categoriesElement,
			long categoryId)
		throws Exception {

		if (!portletDataContext.hasDateRange() ||
			(categoryId == MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID) ||
			(categoryId == MBCategoryConstants.DISCUSSION_CATEGORY_ID)) {

			return;
		}

		MBCategory category = MBCategoryUtil.findByPrimaryKey(categoryId);

		exportParentCategory(
			portletDataContext, categoriesElement,
			category.getParentCategoryId());

		String path = getCategoryPath(portletDataContext, category);

		if (portletDataContext.isPathNotProcessed(path)) {
			Element categoryElement = categoriesElement.addElement("category");

			portletDataContext.addClassedModel(
				categoryElement, path, category, NAMESPACE);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(MBPortletDataHandler.class);

}