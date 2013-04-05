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
import com.liferay.portal.kernel.lar.PortletDataException;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.model.Repository;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.service.persistence.RepositoryUtil;

/**
 * @author Mate Thurzo
 */
public class FileEntryStagedModelDataHandler
	extends BaseStagedModelDataHandler<FileEntry> {

	@Override
	public void exportStagedModel(
			PortletDataContext portletDataContext, FileEntry fileEntry)
		throws PortletDataException {

		try {
			doExportStagedModel(portletDataContext, fileEntry);
		}
		catch (Exception e) {
			throw new PortletDataException(e);
		}
	}

	@Override
	public String getClassName() {
		return FileEntry.class.getName();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, FileEntry fileEntry)
		throws Exception {

		if (!fileEntry.isDefaultRepository()) {
			Repository repository = RepositoryUtil.findByPrimaryKey(
				fileEntry.getRepositoryId());

			StagedModelDataHandlerUtil.exportStagedModel(
				portletDataContext, repository);

			return;
		}

		Object fileEntryModel = fileEntry.getModel();

		if (!(fileEntryModel instanceof StagedModel)) {
			return;
		}

		StagedModelDataHandlerUtil.exportStagedModel(
			portletDataContext, (StagedModel)fileEntryModel);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, FileEntry stagedModel)
		throws Exception {

		return;
	}

}