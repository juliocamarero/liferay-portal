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

package com.liferay.portal.kernel.safe;

import com.liferay.portal.kernel.safe.model.Item;

import java.util.List;

/**
 * Specifies the Portal Safe interface.
 *
 * <p>
 * Implementations should use the {@link
 * com.liferay.portal.kernel.safe.serializer.ItemSerializerRegistryUtil} to
 * convert an {@link com.liferay.portal.kernel.safe.model.Item} into a {@link
 * com.liferay.portal.kernel.safe.serializer.SerializedItem} and {@link
 * com.liferay.portal.kernel.safe.storage.Storage}. Then, the item can be loaded
 * or saved as a serialized item into underlying safe storage.
 * </p>
 *
 * @author Tomas Polesovsky
 */
public interface PortalSafe {

	/**
	 * Returns names of all items in the safe associated with the company ID and
	 * the group ID.
	 *
	 * @param  companyId the company ID from which items are loaded
	 * @param  groupId the group ID inside the company from which items are
	 *         loaded
	 * @return names of all items in the safe associated with the company ID and
	 *         the group ID. These can be later used for loading or removing
	 *         items from the safe.
	 * @throws PortalSafeException if a PortalSafeException occurred or may
	 *         throw a {@link
	 *         com.liferay.portal.kernel.safe.storage.StorageException} if there
	 *         was a problem with loading the item from the underlying storage
	 */
	public List<String> listItemsNames(long companyId, long groupId)
		throws PortalSafeException;

	/**
	 * Returns a loaded item associated with the company ID, the group ID, and
	 * the item's name.
	 *
	 * <p>
	 * Returning a loaded item may throw the following exceptions:
	 * </p>
	 *
	 * <ul>
	 * <li>
	 * {@link
	 * com.liferay.portal.kernel.safe.model.NoSuchItemException} if there is no
	 * item found with the name
	 * </li>
	 * <li>
	 * {@link
	 * com.liferay.portal.kernel.safe.serializer.NoSuchItemSerializerException}
	 * if there is no serializer registered that is able to read the item
	 * </li>
	 * <li>
	 * {@link com.liferay.portal.kernel.safe.storage.StorageException} if
	 * there is a problem with loading the item from the underlying storage
	 * </li>
	 * </ul>
	 *
	 * @param  companyId the company ID from which the item is loaded
	 * @param  groupId the group ID inside the company from which the item is
	 *         loaded
	 * @param  name the name of the item to be loaded
	 * @return a loaded item associated with the company ID, group ID, and item
	 *         name
	 * @throws PortalSafeException if a PortalSafeException error occurred
	 */
	public Item loadItem(long companyId, long groupId, String name)
		throws PortalSafeException;

	/**
	 * Removes the item from the safe. The item is associated with the company
	 * ID, the group ID, and the item's name.
	 *
	 * <p>
	 * Removing an item from the safe may throw the following exceptions:
	 * </p>
	 *
	 * <ul>
	 * <li>
	 * {@link
	 * com.liferay.portal.kernel.safe.model.NoSuchItemException} if there is no
	 * item found with the specified name
	 * </li>
	 * <li>
	 * {@link
	 * com.liferay.portal.kernel.safe.storage.StorageException} if there is a
	 * problem with accessing the item from the underlying storage
	 * </li>
	 * </ul>
	 *
	 * @param  companyId the company ID from which the item is removed
	 * @param  groupId the group ID inside the company from which the item is
	 *         removed
	 * @param  name the name of the item to be removed
	 * @throws PortalSafeException if a PortalSafeException error occurred
	 *         during the item's removal from the safe
	 */
	public void removeItem(long companyId, long groupId, String name)
		throws PortalSafeException;

	/**
	 * Saves the item to the safe. The item is associated with the company ID,
	 * the group ID, and the item's name.
	 *
	 * <p>
	 * Saving an item to the safe may throw the following exceptions:
	 * </p>
	 *
	 * <ul>
	 * <li>
	 * {@link
	 * com.liferay.portal.kernel.safe.serializer.NoSuchItemSerializerException}
	 * if there is no serializer registered that is able to convert the item
	 * into its binary form
	 * </li>
	 * <li>
	 * {@link
	 * com.liferay.portal.kernel.safe.storage.StorageException} if there is a
	 * problem with saving the item into the underlying storage
	 * </li>
	 * </ul>
	 *
	 * @param  companyId the company ID from which the item is saved
	 * @param  groupId the group ID inside the company from which the item is
	 *         saved
	 * @param  item the item to be saved
	 * @throws PortalSafeException if a PortalSafeException error occurred
	 */
	public void saveItem(long companyId, long groupId, Item item)
		throws PortalSafeException;

}