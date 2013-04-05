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
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.model.Repository;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.service.persistence.RepositoryUtil;

/**
 * @author Mate Thurzo
 */
public class FolderStagedModelDataHandler
	extends BaseStagedModelDataHandler<Folder> {

	@Override
	public void exportStagedModel(
			PortletDataContext portletDataContext, Folder folder)
		throws PortletDataException {

		try {
			doExportStagedModel(portletDataContext, folder);
		}
		catch (Exception e) {
			throw new PortletDataException(e);
		}
	}

	@Override
	public String getClassName() {
		return Folder.class.getName();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Folder folder)
		throws Exception {

		if (folder.isMountPoint()) {
			Repository repository = RepositoryUtil.findByPrimaryKey(
				folder.getRepositoryId());

			StagedModelDataHandlerUtil.exportStagedModel(
				portletDataContext, repository);

			return;
		}
		else if (!folder.isDefaultRepository()) {

			// No need to export non-Liferay repository items since they would
			// be exported as part of repository export

			return;
		}

		Object folderModel = folder.getModel();

		if (!(folderModel instanceof StagedModel)) {
			return;
		}

		StagedModelDataHandlerUtil.exportStagedModel(
			portletDataContext, (StagedModel)folderModel);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, Folder stagedModel)
		throws Exception {

		return;
	}

}