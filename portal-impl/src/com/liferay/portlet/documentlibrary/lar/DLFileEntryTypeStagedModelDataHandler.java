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
import com.liferay.portlet.documentlibrary.model.DLFileEntryType;

/**
 *
 * @author Mate Thurzo
 */
public class DLFileEntryTypeStagedModelDataHandler
	extends BaseStagedModelDataHandler<DLFileEntryType> {

	@Override
	public String getClassName() {
		return DLFileEntryType.class.getName();
	}

	/**
	 * @see com.liferay.portal.lar.PortletImporter#getAssetCategoryName(String,
	 *      long, long, String, int)
	 * @see com.liferay.portal.lar.PortletImporter#getAssetVocabularyName(
	 *      String, long, String, int)
	 */
	protected static String getFileEntryTypeName(
			String uuid, long groupId, String name, int count)
		throws Exception {

		DLFileEntryType dlFileEntryType = DLFileEntryTypeUtil.fetchByG_N(
			groupId, name);

		if (dlFileEntryType == null) {
			return name;
		}

		if (Validator.isNotNull(uuid) &&
			uuid.equals(dlFileEntryType.getUuid())) {

			return name;
		}

		name = StringUtil.appendParentheticalSuffix(name, count);

		return getFileEntryTypeName(uuid, groupId, name, ++count);
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext,
			DLFileEntryType fileEntryType)
		throws Exception {

		String path = getFileEntryTypePath(portletDataContext, dlFileEntryType);

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		Element fileEntryTypeElement = fileEntryTypesElement.addElement(
			"file-entry-type");

		List<DDMStructure> ddmStructures = dlFileEntryType.getDDMStructures();

		String[] ddmStructureUuids = new String[ddmStructures.size()];

		for (int i = 0; i < ddmStructures.size(); i++) {
			DDMStructure ddmStructure = ddmStructures.get(i);

			ddmStructureUuids[i] = ddmStructure.getUuid();

			StagedModelDataHandlerUtil.exportStagedModel(
				portletDataContext, ddmStructure);
		}

		fileEntryTypeElement.addAttribute(
			"structureUuids", StringUtil.merge(ddmStructureUuids));

		portletDataContext.addClassedModel(
			fileEntryTypeElement, path, dlFileEntryType, NAMESPACE);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			DLFileEntryType fileEntryType)
		throws Exception {

		String path = fileEntryTypeElement.attributeValue("path");

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		DLFileEntryType dlFileEntryType =
			(DLFileEntryType)portletDataContext.getZipEntryAsObject(path);

		long userId = portletDataContext.getUserId(
			dlFileEntryType.getUserUuid());

		String name = getFileEntryTypeName(
			dlFileEntryType.getUuid(), portletDataContext.getScopeGroupId(),
			dlFileEntryType.getName(), 2);

		List<Element> structureElements = fileEntryTypeElement.elements(
			"structure");

		for (Element structureElement : structureElements) {
			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, structureElement);
		}

		Map<Long, Long> ddmStructureIdsMap =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DDMStructure.class);

		Collection<Long> ddmStructureIdsCollection =
			ddmStructureIdsMap.values();

		long[] ddmStructureIds = ArrayUtil.toArray(
			ddmStructureIdsCollection.toArray(
				new Long[ddmStructureIdsMap.size()]));

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			path, dlFileEntryType, NAMESPACE);

		DLFileEntryType importedDLFileEntryType = null;

		if (portletDataContext.isDataStrategyMirror()) {
			DLFileEntryType existingDLFileEntryType =
				DLFileEntryTypeUtil.fetchByUUID_G(
					dlFileEntryType.getUuid(),
					portletDataContext.getScopeGroupId());

			if (existingDLFileEntryType == null) {
				Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(
					portletDataContext.getCompanyId());

				existingDLFileEntryType = DLFileEntryTypeUtil.fetchByUUID_G(
					dlFileEntryType.getUuid(), companyGroup.getGroupId());
			}

			if (existingDLFileEntryType == null) {
				serviceContext.setUuid(dlFileEntryType.getUuid());

				importedDLFileEntryType =
					DLFileEntryTypeLocalServiceUtil.addFileEntryType(
						userId, portletDataContext.getScopeGroupId(), name,
						dlFileEntryType.getDescription(), ddmStructureIds,
						serviceContext);
			}
			else {
				if (!isFileEntryTypeGlobal(
						portletDataContext.getCompanyId(),
						existingDLFileEntryType)) {

					DLFileEntryTypeLocalServiceUtil.updateFileEntryType(
						userId, existingDLFileEntryType.getFileEntryTypeId(),
						name, dlFileEntryType.getDescription(), ddmStructureIds,
						serviceContext);
				}

				importedDLFileEntryType = existingDLFileEntryType;
			}
		}
		else {
			importedDLFileEntryType =
				DLFileEntryTypeLocalServiceUtil.addFileEntryType(
					userId, portletDataContext.getScopeGroupId(), name,
					dlFileEntryType.getDescription(), ddmStructureIds,
					serviceContext);
		}

		if (!isFileEntryTypeGlobal(
				portletDataContext.getCompanyId(), importedDLFileEntryType)) {

			portletDataContext.importClassedModel(
				dlFileEntryType, importedDLFileEntryType, NAMESPACE);

			String importedDLFileEntryDDMStructureKey =
				DLUtil.getDDMStructureKey(importedDLFileEntryType);

			List<DDMStructure> ddmStructures =
				importedDLFileEntryType.getDDMStructures();

			for (DDMStructure ddmStructure : ddmStructures) {
				String ddmStructureKey = ddmStructure.getStructureKey();

				if (!DLUtil.isAutoGeneratedDLFileEntryTypeDDMStructureKey(
						ddmStructureKey)) {

					continue;
				}

				if (ddmStructureKey.equals(
						importedDLFileEntryDDMStructureKey)) {

					continue;
				}

				ddmStructure.setStructureKey(
					importedDLFileEntryDDMStructureKey);

				DDMStructureLocalServiceUtil.updateDDMStructure(ddmStructure);
			}
		}
	}

}