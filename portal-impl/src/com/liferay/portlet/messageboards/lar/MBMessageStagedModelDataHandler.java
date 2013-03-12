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

import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.portal.kernel.lar.StagedModelPathUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.messageboards.model.MBCategory;
import com.liferay.portlet.messageboards.model.MBCategoryConstants;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadLocalServiceUtil;
import com.liferay.portlet.messageboards.service.persistence.MBMessageUtil;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Kocsis
 */
public class MBMessageStagedModelDataHandler
	extends BaseStagedModelDataHandler<MBMessage> {

	@Override
	public String getClassName() {
		return MBMessage.class.getName();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Element[] elements,
			MBMessage message)
		throws Exception {

		Element messagesElement = elements[0];
		Element categoriesElement = elements[1];

		if ((message.getStatus() != WorkflowConstants.STATUS_APPROVED) ||
			(message.getCategoryId() ==
				MBCategoryConstants.DISCUSSION_CATEGORY_ID)) {

			return;
		}

		// Category

		StagedModelDataHandlerUtil.exportStagedModel(
			portletDataContext, categoriesElement, message.getCategory());

		// Message

		Element messageElement = messagesElement.addElement("message");

		message.setPriority(message.getPriority());

		MBThread thread = message.getThread();

		messageElement.addAttribute(
			"question", String.valueOf(thread.isQuestion()));

		boolean hasAttachmentsFileEntries =
			message.getAttachmentsFileEntriesCount() > 0;

		messageElement.addAttribute(
			"hasAttachmentsFileEntries",
			String.valueOf(hasAttachmentsFileEntries));

		// Attachments

		if (portletDataContext.getBooleanParameter(
				MBPortletDataHandler.NAMESPACE, "attachments") &&
			hasAttachmentsFileEntries) {

			for (FileEntry fileEntry : message.getAttachmentsFileEntries()) {
				String name = fileEntry.getTitle();
				String binPath = StagedModelPathUtil.getPath(message, name);

				Element attachmentElement = messageElement.addElement(
					"attachment");

				attachmentElement.addAttribute("name", name);
				attachmentElement.addAttribute("bin-path", binPath);

				portletDataContext.addZipEntry(
					binPath, fileEntry.getContentStream());
			}

			message.setAttachmentsFolderId(message.getAttachmentsFolderId());
		}

		portletDataContext.addClassedModel(
			messageElement, StagedModelPathUtil.getPath(message), message,
			MBPortletDataHandler.NAMESPACE);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, Element element, String path,
			MBMessage message)
		throws Exception {

		long userId = portletDataContext.getUserId(message.getUserUuid());
		String userName = message.getUserName();

		Map<Long, Long> categoryIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				MBCategory.class);

		long parentCategoryId = MapUtil.getLong(
			categoryIds, message.getCategoryId(), message.getCategoryId());

		Map<Long, Long> threadIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				MBThread.class);

		long threadId = MapUtil.getLong(threadIds, message.getThreadId(), 0);

		Map<Long, Long> messageIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				MBMessage.class);

		long parentMessageId = MapUtil.getLong(
			messageIds, message.getParentMessageId(),
			message.getParentMessageId());

		List<String> existingFiles = new ArrayList<String>();

		List<ObjectValuePair<String, InputStream>> inputStreamOVPs =
			getAttachments(portletDataContext, element, message);

		try {
			ServiceContext serviceContext =
				portletDataContext.createServiceContext(
					element, message, MBPortletDataHandler.NAMESPACE);

			if (message.getStatus() != WorkflowConstants.STATUS_APPROVED) {
				serviceContext.setWorkflowAction(
					WorkflowConstants.ACTION_SAVE_DRAFT);
			}

			// Category

			parentCategoryId = getCategoryId(
				portletDataContext, message, element, categoryIds,
				parentCategoryId);

			// Message & Attachment & Thread

			MBMessage importedMessage = null;

			if (portletDataContext.isDataStrategyMirror()) {
				MBMessage existingMessage = MBMessageUtil.fetchByUUID_G(
					message.getUuid(), portletDataContext.getScopeGroupId());

				if (existingMessage == null) {
					serviceContext.setUuid(message.getUuid());

					importedMessage = MBMessageLocalServiceUtil.addMessage(
						userId, userName, portletDataContext.getScopeGroupId(),
						parentCategoryId, threadId, parentMessageId,
						message.getSubject(), message.getBody(),
						message.getFormat(), inputStreamOVPs,
						message.getAnonymous(), message.getPriority(),
						message.getAllowPingbacks(), serviceContext);
				}
				else {
					importedMessage = MBMessageLocalServiceUtil.updateMessage(
						userId, existingMessage.getMessageId(),
						message.getSubject(), message.getBody(),
						inputStreamOVPs, existingFiles, message.getPriority(),
						message.getAllowPingbacks(), serviceContext);
				}
			}
			else {
				importedMessage = MBMessageLocalServiceUtil.addMessage(
					userId, userName, portletDataContext.getScopeGroupId(),
					parentCategoryId, threadId, parentMessageId,
					message.getSubject(), message.getBody(),
					message.getFormat(), inputStreamOVPs,
					message.getAnonymous(), message.getPriority(),
					message.getAllowPingbacks(), serviceContext);
			}

			importedMessage.setAnswer(message.getAnswer());

			if (importedMessage.isRoot()) {
				MBThreadLocalServiceUtil.updateQuestion(
					importedMessage.getThreadId(),
					GetterUtil.getBoolean(element.attributeValue("question")));
			}

			threadIds.put(message.getThreadId(), importedMessage.getThreadId());

			portletDataContext.importClassedModel(
				message, importedMessage, MBPortletDataHandler.NAMESPACE);
		}
		finally {
			for (ObjectValuePair<String, InputStream> inputStreamOVP :
					inputStreamOVPs) {

				InputStream inputStream = inputStreamOVP.getValue();

				StreamUtil.cleanUp(inputStream);
			}
		}
	}

	protected List<ObjectValuePair<String, InputStream>> getAttachments(
		PortletDataContext portletDataContext, Element messageElement,
		MBMessage message) {

		boolean hasAttachmentsFileEntries = GetterUtil.getBoolean(
			messageElement.attributeValue("hasAttachmentsFileEntries"));

		if (!hasAttachmentsFileEntries &&
			portletDataContext.getBooleanParameter(
				MBPortletDataHandler.NAMESPACE, "attachments")) {

			return Collections.emptyList();
		}

		List<ObjectValuePair<String, InputStream>> inputStreamOVPs =
			new ArrayList<ObjectValuePair<String, InputStream>>();

		List<Element> attachmentElements = messageElement.elements(
			"attachment");

		for (Element attachmentElement : attachmentElements) {
			String name = attachmentElement.attributeValue("name");
			String binPath = attachmentElement.attributeValue("bin-path");

			InputStream inputStream =
				portletDataContext.getZipEntryAsInputStream(binPath);

			if (inputStream == null) {
				continue;
			}

			ObjectValuePair<String, InputStream> inputStreamOVP =
				new ObjectValuePair<String, InputStream>(name, inputStream);

			inputStreamOVPs.add(inputStreamOVP);
		}

		if (inputStreamOVPs.isEmpty()) {
			_log.error(
				"Could not find attachments for message " +
					message.getMessageId());
		}

		return inputStreamOVPs;
	}

	protected long getCategoryId(
			PortletDataContext portletDataContext, MBMessage message,
			Element categoryElement, Map<Long, Long> categoryPKs,
			long categoryId)
		throws Exception {

		if ((categoryId != MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID) &&
			(categoryId != MBCategoryConstants.DISCUSSION_CATEGORY_ID) &&
			(categoryId == message.getCategoryId())) {

			String path = StagedModelPathUtil.getPath(
				portletDataContext, MBCategory.class.getName(), categoryId);

			MBCategory category =
				(MBCategory)portletDataContext.getZipEntryAsObject(path);

			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, categoryElement, path, category);

			categoryId = MapUtil.getLong(
				categoryPKs, message.getCategoryId(), message.getCategoryId());
		}

		return categoryId;
	}

	private static Log _log = LogFactoryUtil.getLog(
		MBMessageStagedModelDataHandler.class);

}