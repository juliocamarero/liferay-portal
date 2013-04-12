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
import com.liferay.portal.model.Repository;

/**
 * @author Mate Thurzo
 */
public class RepositoryStagedModelDataHandler
	extends BaseStagedModelDataHandler<Repository> {

	@Override
	public String getClassName() {
		return Repository.class.getName();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Repository repository)
		throws Exception {

		if (!portletDataContext.isWithinDateRange(
				repository.getModifiedDate())) {

			return;
		}

		String path = getRepositoryPath(portletDataContext, repository);

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		Element repositoryElement = repositoriesElement.addElement(
			"repository");

		Folder folder = DLAppLocalServiceUtil.getFolder(
			repository.getDlFolderId());

		if (folder.getModel() instanceof DLFolder) {
			DLFolder dlFolder = (DLFolder)folder.getModel();

			repositoryElement.addAttribute(
				"hidden", String.valueOf(dlFolder.isHidden()));
		}

		portletDataContext.addClassedModel(
			repositoryElement, path, repository, NAMESPACE);

		List<RepositoryEntry> repositoryEntries =
			RepositoryEntryUtil.findByRepositoryId(
				repository.getRepositoryId());

		for (RepositoryEntry repositoryEntry : repositoryEntries) {
			exportRepositoryEntry(
				portletDataContext, repositoryEntriesElement, repositoryEntry);
		}
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, Repository repository)
		throws Exception {

		String path = repositoryElement.attributeValue("path");

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		Repository repository =
			(Repository)portletDataContext.getZipEntryAsObject(
				repositoryElement, path);

		long userId = portletDataContext.getUserId(repository.getUserUuid());

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			repositoryElement, repository, NAMESPACE);

		Repository importedRepository = null;

		try {
			boolean hidden = GetterUtil.getBoolean(
				repositoryElement.attributeValue("hidden"));

			if (portletDataContext.isDataStrategyMirror()) {
				Repository existingRepository = RepositoryUtil.fetchByUUID_G(
					repository.getUuid(), portletDataContext.getScopeGroupId());

				if (existingRepository == null) {
					existingRepository =
						RepositoryLocalServiceUtil.fetchRepository(
							portletDataContext.getScopeGroupId(),
							repository.getName());
				}

				long classNameId = 0;

				if (existingRepository != null) {
					classNameId = existingRepository.getClassNameId();
				}

				if ((existingRepository == null) ||
					(classNameId !=
						PortalUtil.getClassNameId(LiferayRepository.class))) {

					serviceContext.setUuid(repository.getUuid());

					importedRepository =
						RepositoryLocalServiceUtil.addRepository(
							userId, portletDataContext.getScopeGroupId(),
							repository.getClassNameId(),
							DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
							repository.getName(), repository.getDescription(),
							repository.getPortletId(),
							repository.getTypeSettingsProperties(), hidden,
							serviceContext);
				}
				else {
					RepositoryLocalServiceUtil.updateRepository(
						existingRepository.getRepositoryId(),
						repository.getName(), repository.getDescription());

					importedRepository = existingRepository;
				}
			}
			else {
				importedRepository = RepositoryLocalServiceUtil.addRepository(
					userId, portletDataContext.getScopeGroupId(),
					repository.getClassNameId(),
					DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
					repository.getName(), repository.getDescription(),
					repository.getPortletId(),
					repository.getTypeSettingsProperties(), hidden,
					serviceContext);
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to connect to repository {name=" +
						repository.getName() + ", typeSettings=" +
						repository.getTypeSettingsProperties() + "}",
					e);
			}
		}

		portletDataContext.importClassedModel(
			repository, importedRepository, NAMESPACE);
	}

}