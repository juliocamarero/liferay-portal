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

package com.liferay.portlet.dynamicdatalists.lar;

import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.StagedModelPathUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetLocalServiceUtil;
import com.liferay.portlet.dynamicdatalists.service.persistence.DDLRecordSetUtil;
import com.liferay.portlet.dynamicdatamapping.lar.DDMPortletDataHandler;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class DDLRecordSetStagedModelDataHandler
	extends BaseStagedModelDataHandler<DDLRecordSet> {

	@Override
	public String getClassName() {
		return DDLRecordSet.class.getName();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Element[] elements,
			DDLRecordSet recordSet)
		throws Exception {

		Element recordSetsElement = elements[0];
		Element ddmStructuresElement = elements[1];
		Element ddmTemplatesElement = elements[2];

		Element recordSetElement = recordSetsElement.addElement("record-set");

		String path = StagedModelPathUtil.getPath(recordSet);

		portletDataContext.addClassedModel(
			recordSetElement, path, recordSet, DDLPortletDataHandler.NAMESPACE);

		DDMStructure ddmStructure = recordSet.getDDMStructure();

		DDMPortletDataHandler.exportStructure(
			portletDataContext, ddmStructuresElement, ddmStructure);

		List<DDMTemplate> ddmTemplates = ddmStructure.getTemplates();

		for (DDMTemplate ddmTemplate : ddmTemplates) {
			DDMPortletDataHandler.exportTemplate(
				portletDataContext, ddmTemplatesElement, ddmTemplate);
		}
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, String path,
			DDLRecordSet stagedModel)
		throws Exception {

		DDLRecordSet recordSet =
			(DDLRecordSet)portletDataContext.getZipEntryAsObject(path);

		long userId = portletDataContext.getUserId(recordSet.getUserUuid());

		Map<Long, Long> structureIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DDMStructure.class);

		long structureId = MapUtil.getLong(
			structureIds, recordSet.getDDMStructureId(),
			recordSet.getDDMStructureId());

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			path, recordSet, DDLPortletDataHandler.NAMESPACE);

		DDLRecordSet importedRecordSet = null;

		if (portletDataContext.isDataStrategyMirror()) {
			DDLRecordSet existingRecordSet = DDLRecordSetUtil.fetchByUUID_G(
				recordSet.getUuid(), portletDataContext.getScopeGroupId());

			if (existingRecordSet == null) {
				serviceContext.setUuid(recordSet.getUuid());

				importedRecordSet = DDLRecordSetLocalServiceUtil.addRecordSet(
					userId, portletDataContext.getScopeGroupId(), structureId,
					recordSet.getRecordSetKey(), recordSet.getNameMap(),
					recordSet.getDescriptionMap(),
					recordSet.getMinDisplayRows(), recordSet.getScope(),
					serviceContext);
			}
			else {
				importedRecordSet =
					DDLRecordSetLocalServiceUtil.updateRecordSet(
						existingRecordSet.getRecordSetId(), structureId,
						recordSet.getNameMap(), recordSet.getDescriptionMap(),
						recordSet.getMinDisplayRows(), serviceContext);
			}
		}
		else {
			importedRecordSet = DDLRecordSetLocalServiceUtil.addRecordSet(
				userId, portletDataContext.getScopeGroupId(), structureId,
				recordSet.getRecordSetKey(), recordSet.getNameMap(),
				recordSet.getDescriptionMap(), recordSet.getMinDisplayRows(),
				recordSet.getScope(), serviceContext);
		}

		portletDataContext.importClassedModel(
			recordSet, importedRecordSet, DDLPortletDataHandler.NAMESPACE);
	}

}