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

package com.liferay.portlet.documentlibrary.lar;

import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portlet.documentlibrary.model.DLFileRank;

/**
 * @author Mate Thurzo
 */
public class DLFileRankStagedModelDataHandler
	extends BaseStagedModelDataHandler<DLFileRank> {

	@Override
	public String getClassName() {
		return DLFileRank.class.getName();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, DLFileRank fileRank)
		throws Exception {

		String path = getFileRankPath(portletDataContext, fileRank);

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		Element fileRankElement = fileRanksElement.addElement("file-rank");

		FileEntry fileEntry = FileEntryUtil.fetchByPrimaryKey(
			fileRank.getFileEntryId());

		String fileEntryUuid = fileEntry.getUuid();

		fileRankElement.addAttribute("file-entry-uuid", fileEntryUuid);

		portletDataContext.addClassedModel(
			fileRankElement, path, fileRank, NAMESPACE);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, DLFileRank fileRank)
		throws Exception {

		long userId = portletDataContext.getUserId(fileRank.getUserUuid());

		long groupId = portletDataContext.getScopeGroupId();

		FileEntry fileEntry = FileEntryUtil.fetchByUUID_R(
			fileEntryUuid, groupId);

		if (fileEntry == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to retrieve file " + fileEntryUuid +
						" to import file rank");
			}

			return;
		}

		long fileEntryId = fileEntry.getFileEntryId();

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setCreateDate(fileRank.getCreateDate());

		DLAppLocalServiceUtil.updateFileRank(
			portletDataContext.getScopeGroupId(),
			portletDataContext.getCompanyId(), userId, fileEntryId,
			serviceContext);
	}

}