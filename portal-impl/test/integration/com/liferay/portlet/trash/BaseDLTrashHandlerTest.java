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

package com.liferay.portlet.trash;

import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.BaseDLAppTestCase;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.trash.service.TrashEntryServiceUtil;

/**
 * @author Alexander Chow
 * @author Zsolt Berentey
 */
public class BaseDLTrashHandlerTest extends BaseDLAppTestCase {

	protected AssetEntry fetchAssetEntry(String className, long classPK)
		throws Exception {

		return AssetEntryLocalServiceUtil.fetchEntry(className, classPK);
	}

	protected int getNotInTrashCount() throws Exception {
		return DLAppServiceUtil.getFoldersAndFileEntriesAndFileShortcutsCount(
			folder.getRepositoryId(), folder.getFolderId(),
			WorkflowConstants.STATUS_ANY, false);
	}

	protected int getTrashEntriesCount() throws Exception {
		Object[] result = TrashEntryServiceUtil.getEntries(folder.getGroupId());

		return (Integer)result[1];
	}

	protected boolean isAssetEntryVisible(String className, long classPK)
		throws Exception {

		AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(
			className, classPK);

		return assetEntry.isVisible();
	}

}