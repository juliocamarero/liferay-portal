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

import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.lar.BaseStagedModelDataHandlerTestCase;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portlet.documentlibrary.model.DLFileEntryType;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryTypeLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;
import com.liferay.portlet.documentlibrary.util.DLAppTestUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.util.DDMStructureTestUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.runner.RunWith;

/**
 * @author Mate Thurzo
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class FileEntryStagedModelDataHandlerTest
	extends BaseStagedModelDataHandlerTestCase {

	@Override
	protected Map<String, List<StagedModel>> addDependentStagedModelsMap(
			Group group)
		throws Exception {

		Map<String, List<StagedModel>> dependentStagedModelsMap =
			new HashMap<String, List<StagedModel>>();

		Folder folder = DLAppTestUtil.addFolder(
			group.getGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			ServiceTestUtil.randomString());

		addDependentStagedModel(dependentStagedModelsMap, Folder.class, folder);

		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			group.getGroupId(), DLFileEntryType.class.getName());

		addDependentStagedModel(
			dependentStagedModelsMap, DDMStructure.class, ddmStructure);

		DLFileEntryType dlFileEntryType = DLAppTestUtil.addDLFileEntryType(
			group.getGroupId(), ddmStructure.getStructureId());

		addDependentStagedModel(
			dependentStagedModelsMap, DLFileEntryType.class, dlFileEntryType);

		return dependentStagedModelsMap;
		return dependentStagedModelsMap;
	}

	@Override
	protected StagedModel addStagedModel(
			Group group,
			Map<String, List<StagedModel>> dependentStagedModelsMap)
		throws Exception {

		List<StagedModel> folderDependentStagedModels =
			dependentStagedModelsMap.get(Folder.class.getSimpleName());

		Folder folder = (Folder)folderDependentStagedModels.get(0);

		List<StagedModel> fileEntryTypeDependentStagedModels =
			dependentStagedModelsMap.get(DLFileEntryType.class.getSimpleName());

		DLFileEntryType dlFileEntryType =
			(DLFileEntryType)fileEntryTypeDependentStagedModels.get(0);

		return DLAppTestUtil.addFileEntry(
			group.getGroupId(), folder.getFolderId(),
			ServiceTestUtil.randomString(),
			dlFileEntryType.getFileEntryTypeId());
	}

	@Override
	protected StagedModel getStagedModel(String uuid, Group group) {
		try {
			return DLAppLocalServiceUtil.getFileEntryByUuidAndGroupId(
				uuid, group.getGroupId());
		}
		catch (Exception e) {
			return null;
		}
	}

	@Override
	protected Class<? extends StagedModel> getStagedModelClass() {
		return FileEntry.class;
	}

	@Override
	protected String getStagedModelPath(long groupId, StagedModel stagedModel) {
		if (stagedModel instanceof FileEntry) {
			FileEntry fileEntry = (FileEntry)stagedModel;

			return ExportImportPathUtil.getModelPath(
				groupId, FileEntry.class.getName(), fileEntry.getFileEntryId());
		}
		else if (stagedModel instanceof Folder) {
			Folder folder = (Folder)stagedModel;

			return ExportImportPathUtil.getModelPath(
				groupId, Folder.class.getName(), folder.getFolderId());
		}

		return super.getStagedModelPath(groupId, stagedModel);
	}

	@Override
	protected void validateImport(
			Map<String, List<StagedModel>> dependentStagedModelsMap,
			Group group)
		throws Exception {

		List<StagedModel> foldersDependentStagedModels =
			dependentStagedModelsMap.get(Folder.class.getSimpleName());

		Assert.assertEquals(1, foldersDependentStagedModels.size());

		Folder folder = (Folder)foldersDependentStagedModels.get(0);

		DLFolderLocalServiceUtil.getDLFolderByUuidAndGroupId(
			folder.getUuid(), group.getGroupId());

		List<StagedModel> ddmStructureDependentStagedModels =
			dependentStagedModelsMap.get(DDMStructure.class.getSimpleName());

		Assert.assertEquals(1, ddmStructureDependentStagedModels.size());

		DDMStructure structure =
			(DDMStructure)ddmStructureDependentStagedModels.get(0);

		DDMStructureLocalServiceUtil.getDDMStructureByUuidAndGroupId(
			structure.getUuid(), group.getGroupId());

		List<StagedModel> dlFileEntryTypesDependentStagedModels =
			dependentStagedModelsMap.get(DLFileEntryType.class.getSimpleName());

		Assert.assertEquals(1, dlFileEntryTypesDependentStagedModels.size());

		DLFileEntryType dlFileEntryType =
			(DLFileEntryType)dlFileEntryTypesDependentStagedModels.get(0);

		DLFileEntryTypeLocalServiceUtil.getDLFileEntryTypeByUuidAndGroupId(
			dlFileEntryType.getUuid(), group.getGroupId());
	}

}