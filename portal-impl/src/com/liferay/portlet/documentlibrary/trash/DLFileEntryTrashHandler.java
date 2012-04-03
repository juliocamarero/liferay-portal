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

package com.liferay.portlet.documentlibrary.trash;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.trash.BaseTrashHandler;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFileVersion;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileVersionLocalServiceUtil;
import com.liferay.portlet.trash.model.TrashEntry;
import com.liferay.portlet.trash.service.TrashEntryLocalServiceUtil;

/**
 * @author Alexander Chow
 * @author Manuel de la Peña
 */
public class DLFileEntryTrashHandler extends BaseTrashHandler {

	public static final String CLASS_NAME = DLFileEntry.class.getName();

	@Transactional(
		propagation = Propagation.REQUIRES_NEW,
		rollbackFor = {PortalException.class, SystemException.class})
	public void deleteTrashEntries(long[] classPKs) {
		for (int i = 0; i < classPKs.length; i++) {
			try {
				TrashEntry trashEntry =
					TrashEntryLocalServiceUtil.getTrashEntry(classPKs[i]);

				TrashEntryLocalServiceUtil.deleteTrashEntry(
					trashEntry.getEntryId());

				DLFileEntry dlFileEntry =
					DLFileEntryLocalServiceUtil.getDLFileEntry(
						trashEntry.getClassPK());

				// delete the file entry

				DLAppServiceUtil.deleteFileEntry(dlFileEntry.getFileEntryId());
			}
			catch (PortalException e) {
				e.printStackTrace();
			}
			catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}

	public String getClassName() {
		return CLASS_NAME;
	}

	@Transactional(
		propagation = Propagation.REQUIRES_NEW,
		rollbackFor = {PortalException.class, SystemException.class})
	public void restoreTrashEntries(long[] classPKs) {
		for (int i = 0; i < classPKs.length; i++) {
			try {
				TrashEntry trashEntry =
					TrashEntryLocalServiceUtil.getTrashEntry(classPKs[i]);

				TrashEntryLocalServiceUtil.deleteTrashEntry(classPKs[i]);

				DLFileEntry dlFileEntry =
					DLFileEntryLocalServiceUtil.getDLFileEntry(
						trashEntry.getClassPK());

				// restore the file entry to prior status

				DLFileVersion dlFileVersion = dlFileEntry.getFileVersion();
				dlFileVersion.setStatus(trashEntry.getStatus());

				DLFileVersionLocalServiceUtil.updateDLFileVersion(
					dlFileVersion, true);
			}
			catch (PortalException e) {
				e.printStackTrace();
			}
			catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}

}