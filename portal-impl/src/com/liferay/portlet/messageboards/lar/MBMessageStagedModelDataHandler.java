/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.messageboards.model.MBCategory;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.model.MBThreadFlag;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadLocalServiceUtil;
import com.liferay.portlet.messageboards.service.persistence.MBMessageUtil;
import com.liferay.portlet.messageboards.service.persistence.MBThreadFlagUtil;

import java.io.InputStream;

import java.util.ArrayList;
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

		if (!portletDataContext.isWithinDateRange(message.getModifiedDate())) {
			return;
		}

		if (message.getStatus() != WorkflowConstants.STATUS_APPROVED) {
			return;
		}

		exportParentCategory(
			portletDataContext, categoriesElement, message.getCategoryId());

		String path = getMessagePath(portletDataContext, message);

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

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

		if (portletDataContext.getBooleanParameter(NAMESPACE, "attachments") &&
			hasAttachmentsFileEntries) {

			for (FileEntry fileEntry : message.getAttachmentsFileEntries()) {
				String name = fileEntry.getTitle();
				String binPath = getMessageAttachementBinPath(
					portletDataContext, message, name);

				Element attachmentElement = messageElement.addElement(
					"attachment");

				attachmentElement.addAttribute("name", name);
				attachmentElement.addAttribute("bin-path", binPath);

				portletDataContext.addZipEntry(
					binPath, fileEntry.getContentStream());
			}

			message.setAttachmentsFolderId(message.getAttachmentsFolderId());
		}

		if (portletDataContext.getBooleanParameter(NAMESPACE, "thread-flags")) {
			List<MBThreadFlag> threadFlags = MBThreadFlagUtil.findByThreadId(
				message.getThreadId());

			for (MBThreadFlag threadFlag : threadFlags) {
				exportThreadFlag(
					portletDataContext, threadFlagsElement, threadFlag);
			}
		}

		portletDataContext.addClassedModel(
			messageElement, path, message, NAMESPACE);
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

		long categoryId = MapUtil.getLong(
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
			getAttachments(portletDataContext, messageElement, message);

		try {
			ServiceContext serviceContext =
				portletDataContext.createServiceContext(
					messageElement, message, NAMESPACE);

			if (message.getStatus() != WorkflowConstants.STATUS_APPROVED) {
				serviceContext.setWorkflowAction(
					WorkflowConstants.ACTION_SAVE_DRAFT);
			}

			categoryId = getCategoryId(
				portletDataContext, message, categoryIds, categoryId);

			MBMessage importedMessage = null;

			if (portletDataContext.isDataStrategyMirror()) {
				MBMessage existingMessage = MBMessageUtil.fetchByUUID_G(
					message.getUuid(), portletDataContext.getScopeGroupId());

				if (existingMessage == null) {
					serviceContext.setUuid(message.getUuid());

					importedMessage = MBMessageLocalServiceUtil.addMessage(
						userId, userName, portletDataContext.getScopeGroupId(),
						categoryId, threadId, parentMessageId,
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
					categoryId, threadId, parentMessageId, message.getSubject(),
					message.getBody(), message.getFormat(), inputStreamOVPs,
					message.getAnonymous(), message.getPriority(),
					message.getAllowPingbacks(), serviceContext);
			}

			importedMessage.setAnswer(message.getAnswer());

			if (importedMessage.isRoot()) {
				MBThreadLocalServiceUtil.updateQuestion(
					importedMessage.getThreadId(),
					GetterUtil.getBoolean(
						messageElement.attributeValue("question")));
			}

			threadIds.put(message.getThreadId(), importedMessage.getThreadId());

			portletDataContext.importClassedModel(
				message, importedMessage, NAMESPACE);
		}
		finally {
			for (ObjectValuePair<String, InputStream> inputStreamOVP :
					inputStreamOVPs) {

				InputStream inputStream = inputStreamOVP.getValue();

				StreamUtil.cleanUp(inputStream);
			}
		}
	}

}