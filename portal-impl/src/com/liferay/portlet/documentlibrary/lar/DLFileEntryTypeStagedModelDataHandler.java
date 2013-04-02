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

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext,
			DLFileEntryType fileEntryType)
		throws Exception {
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			DLFileEntryType fileEntryType)
		throws Exception {
	}

}