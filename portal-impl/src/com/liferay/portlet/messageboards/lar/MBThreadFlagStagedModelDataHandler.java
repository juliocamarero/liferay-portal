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
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.model.MBThreadFlag;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadFlagLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadLocalServiceUtil;
import com.liferay.portlet.messageboards.service.persistence.MBMessageUtil;
import com.liferay.portlet.messageboards.service.persistence.MBThreadUtil;

import java.util.Map;

/**
 * @author Daniel Kocsis
 */
public class MBThreadFlagStagedModelDataHandler
	extends BaseStagedModelDataHandler<MBThreadFlag> {

	@Override
	public String getClassName() {
		return MBThreadFlag.class.getName();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Element[] elements,
			MBThreadFlag threadFlag)
		throws Exception {

		String path = getThreadFlagPath(portletDataContext, threadFlag);

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		Element threadFlagElement = threadFlagsElement.addElement(
			"thread-flag");

		MBThread thread = MBThreadLocalServiceUtil.getThread(
			threadFlag.getThreadId());

		MBMessage rootMessage = MBMessageLocalServiceUtil.getMessage(
			thread.getRootMessageId());

		threadFlagElement.addAttribute(
			"root-message-uuid", rootMessage.getUuid());

		portletDataContext.addClassedModel(
			threadFlagElement, path, threadFlag, NAMESPACE);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, Element element, String path,
			MBThreadFlag threadFlag)
		throws Exception {

		long userId = portletDataContext.getUserId(threadFlag.getUserUuid());

		Map<Long, Long> messageIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				MBMessage.class);

		long threadId = MapUtil.getLong(
			messageIds, threadFlag.getThreadId(), threadFlag.getThreadId());

		MBThread thread = MBThreadUtil.fetchByPrimaryKey(threadId);

		if (thread == null) {
			String rootMessageUuid = threadFlagElement.attributeValue(
				"root-message-uuid");

			MBMessage rootMessage = MBMessageUtil.fetchByUUID_G(
				rootMessageUuid, portletDataContext.getScopeGroupId());

			if (rootMessage != null) {
				thread = rootMessage.getThread();
			}
		}

		if (thread == null) {
			return;
		}

		ServiceContext serviceContext =
			portletDataContext.createServiceContext(
				threadFlagElement, threadFlag, NAMESPACE);

		MBThreadFlagLocalServiceUtil.addThreadFlag(
			userId, thread, serviceContext);
	}

}