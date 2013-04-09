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
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Repository;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.service.persistence.RepositoryUtil;

import java.util.List;

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
	public void importStagedModel(
			PortletDataContext portletDataContext, Folder folder)
		throws PortletDataException {

		try {
			doImportStagedModel(portletDataContext, folder);
		}
		catch (Exception e) {
			throw new PortletDataException(e);
		}
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Folder folder)
		throws Exception {

		Element folderGroupElement =
			portletDataContext.getExportDataGroupElement(Folder.class);

		Element folderElement = folderGroupElement.addElement("staged-model");

		String folderPath = StagedModelPathUtil.getPath(
			folder.getGroupId(), Folder.class.getName(), folder.getFolderId());

		folderElement.addAttribute("path", folderPath);

		if (folder.isMountPoint()) {
			Repository repository = RepositoryUtil.findByPrimaryKey(
				folder.getRepositoryId());

			StagedModelDataHandlerUtil.exportStagedModel(
				portletDataContext, repository);

			portletDataContext.addReferenceElement(folderElement, repository);

			portletDataContext.addClassedModel(
				folderElement, folderPath, folder,
				DLPortletDataHandler.NAMESPACE);

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

		portletDataContext.addReferenceElement(
			folderElement, (StagedModel)folderModel);

		portletDataContext.addClassedModel(
			folderElement, folderPath, folder, DLPortletDataHandler.NAMESPACE);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, Folder folder)
		throws Exception {

		String path = StagedModelPathUtil.getPath(
			folder.getGroupId(), Folder.class.getName(), folder.getFolderId());

		Element folderElement =
			portletDataContext.getImportDataStagedModelElement(
				Folder.class.getSimpleName(), "path", path);

		Element folderReferencesElement = folderElement.element("references");

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