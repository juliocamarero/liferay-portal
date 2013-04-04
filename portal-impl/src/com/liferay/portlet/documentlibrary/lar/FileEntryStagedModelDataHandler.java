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
import com.liferay.portal.kernel.lar.StagedModelPathUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Repository;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.service.persistence.RepositoryUtil;

import java.util.List;

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

		Element fileEntryGroupElement =
			portletDataContext.getExportDataGroupElement(FileEntry.class);

		Element fileEntryElement = fileEntryGroupElement.addElement(
			"staged-model");

		String fileEntryPath = StagedModelPathUtil.getPath(
			fileEntry.getGroupId(), FileEntry.class.getName(),
			fileEntry.getFileEntryId());

		fileEntryElement.addAttribute("path", fileEntryPath);

		if (!fileEntry.isDefaultRepository()) {
			Repository repository = RepositoryUtil.findByPrimaryKey(
				fileEntry.getRepositoryId());

			StagedModelDataHandlerUtil.exportStagedModel(
				portletDataContext, repository);

			portletDataContext.addReferenceElement(
				fileEntryElement, repository);

			portletDataContext.addClassedModel(
				fileEntryElement, fileEntryPath, fileEntry,
				DLPortletDataHandler.NAMESPACE);

			return;
		}

		Object fileEntryModel = fileEntry.getModel();

		if (!(fileEntryModel instanceof StagedModel)) {
			return;
		}

		StagedModelDataHandlerUtil.exportStagedModel(
			portletDataContext, (StagedModel)fileEntryModel);

		portletDataContext.addReferenceElement(
			fileEntryElement, (StagedModel)fileEntryModel);

		portletDataContext.addClassedModel(
			fileEntryElement, fileEntryPath, fileEntry,
			DLPortletDataHandler.NAMESPACE);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, FileEntry fileEntry)
		throws Exception {

		String path = StagedModelPathUtil.getPath(
			fileEntry.getGroupId(), Folder.class.getName(),
			fileEntry.getFolderId());

		Element fileEntryElement =
			portletDataContext.getImportDataStagedModelElement(
				FileEntry.class.getSimpleName(), "path", path);

		Element folderReferencesElement = fileEntryElement.element(
			"references");

		List<Element> refElements = folderReferencesElement.elements();

		for (Element refElement : refElements) {
			String className = refElement.attributeValue("class-name");
			String classPk = refElement.attributeValue("class-pk");

			String refPath = StagedModelPathUtil.getPath(
				portletDataContext, className, Long.valueOf(classPk));

			StagedModel referencedStagedModel =
				(StagedModel)portletDataContext.getZipEntryAsObject(refPath);

			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, referencedStagedModel);
		}
	}

}