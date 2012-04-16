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

package com.liferay.portlet.blogs.trash;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.trash.BaseTrashHandler;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryServiceUtil;

/**
 * Represents the trash handler for blogs entries entity.
 *
 * @author Zsolt Berentey
 */
public class BlogsEntryTrashHandler extends BaseTrashHandler {

	public static final String CLASS_NAME = BlogsEntry.class.getName();

	/**
	 * Deletes all blogs entries with the matching primary keys.
	 *
	 * @param classPKs the primary keys to be deleted
	 * @throws PortalException if the blogs entry could not be found
	 * @throws SystemException if a system exception is ocurred
	 */
	public void deleteTrashEntries(long[] classPKs)
		throws PortalException, SystemException {

		for (long classPK : classPKs) {
			BlogsEntryServiceUtil.deleteEntry(classPK);
		}
	}

	/**
	 * Returns the blogs entry entity's class name
	 *
	 * @return the blogs entry entity's class name
	 */
	public String getClassName() {
		return CLASS_NAME;
	}

	/**
	 * Restores all blogs entries with the matching primary keys.
	 *
	 * @param classPKs the primary key to be restored
	 * @throws PortalException if the blogs entry could not be found
	 * @throws SystemException if a system exception is ocurred
	 */
	public void restoreTrashEntries(long[] classPKs)
		throws PortalException, SystemException {

		for (long classPK : classPKs) {
			BlogsEntryServiceUtil.restoreEntryFromTrash(classPK);
		}
	}

}