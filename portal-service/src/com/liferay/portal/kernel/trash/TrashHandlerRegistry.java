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

package com.liferay.portal.kernel.trash;

import java.util.List;

/**
 * Represents the interface for registering handlers for those entities that
 * can be moved to Trash.
 *
 * <p>
 * The entities that can be registered are:
 * </p>
 *
 * <ul>
 * <li>
 * @link com.liferay.portlet.blogs.trash.BlogsEntryTrashHandler
 * </li>
 * </ul>
 *
 * @author Alexander Chow
 */
public interface TrashHandlerRegistry {

	/**
	 * Returns the TrashHandler object associated with the
	 * <code>className</code> parameter.
	 *
	 * @param className class name of the TrashHandler.
	 * @return The trashHandler object associated with the className.
	 */
	public TrashHandler getTrashHandler(String className);

	/**
	 * Returns a List with all trashHandlers.
	 *
	 * @return The trashHandler object associated.
	 */
	public List<TrashHandler> getTrashHandlers();

	/**
	 * Registers the trashHandler passed as parameter.
	 *
	 * @param trashHandler the TrashHandler to register.
	 */
	public void register(TrashHandler trashHandler);

	/**
	 * Unregisters the trashHandler passed as parameter.
	 *
	 * @param trashHandler the TrashHandler to unregister.
	 */
	public void unregister(TrashHandler trashHandler);

}