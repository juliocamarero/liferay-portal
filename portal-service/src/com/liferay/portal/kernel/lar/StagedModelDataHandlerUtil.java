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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.StagedModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 */
public class StagedModelDataHandlerUtil {

	public static <T extends StagedModel> void exportStagedModel(
			PortletDataContext portletDataContext, T stagedModel)
		throws PortletDataException {

		StagedModelDataHandler<T> stagedModelDataHandler =
			_getStagedModelDataHandler(stagedModel);

		stagedModelDataHandler.exportStagedModel(
			portletDataContext, stagedModel);
	}

	public static void importStagedModel(
			PortletDataContext portletDataContext, Element element)
		throws PortletDataException {

		String path = element.attributeValue("path");

		StagedModel stagedModel =
			(StagedModel)portletDataContext.getZipEntryAsObject(element, path);

		importStagedModel(portletDataContext, stagedModel);
	}

	public static <T extends StagedModel> void importStagedModel(
			PortletDataContext portletDataContext, T stagedModel)
		throws PortletDataException {

		StagedModelDataHandler<T> stagedModelDataHandler =
			_getStagedModelDataHandler(stagedModel);

		stagedModelDataHandler.importStagedModel(
			portletDataContext, stagedModel);
	}

	private static <T extends StagedModel> StagedModelDataHandler<T>
		_getStagedModelDataHandler(T stagedModel) {

		StagedModelDataHandler<T> stagedModelDataHandler = null;

		stagedModelDataHandler = _checkStagedModelDataHandlerOverride(
			stagedModel);

		if (stagedModelDataHandler != null) {
			return stagedModelDataHandler;
		}

		BaseModel<?> baseModel = (BaseModel<?>)stagedModel;

		stagedModelDataHandler =
			(StagedModelDataHandler<T>)
				StagedModelDataHandlerRegistryUtil.getStagedModelDataHandler(
					baseModel.getModelClassName());

		return stagedModelDataHandler;
	}

	private static <T extends StagedModel> StagedModelDataHandler<T>
		_checkStagedModelDataHandlerOverride(T stagedModel) {

		String stagedModelDataHandlerOverride =
			PropsUtil.get(
				"staged.model.data.handler.override." +
					stagedModel.getClass().getName());

		if (Validator.isNull(stagedModelDataHandlerOverride)) {
			return null;
		}

		return (StagedModelDataHandler<T>)
			StagedModelDataHandlerRegistryUtil.getStagedModelDataHandler(
				stagedModelDataHandlerOverride);
	}

}