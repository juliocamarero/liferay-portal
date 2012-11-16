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

package com.liferay.portlet.asset.service.impl;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.asset.NoSuchFavoriteAssetException;
import com.liferay.portlet.asset.model.FavoriteAsset;
import com.liferay.portlet.asset.service.base.FavoriteAssetServiceBaseImpl;

/**
 * The implementation of the favorite asset remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.portlet.asset.service.FavoriteAssetService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Juan Fernández
 * @see com.liferay.portlet.asset.service.base.FavoriteAssetServiceBaseImpl
 * @see com.liferay.portlet.asset.service.FavoriteAssetServiceUtil
 */
public class FavoriteAssetServiceImpl extends FavoriteAssetServiceBaseImpl {

	public FavoriteAsset addFavoriteAsset(
			long userId, String className, long classPK)
		throws SystemException {

		return favoriteAssetLocalService.addFavoriteAsset(
			userId, className, classPK);
	}

	public void deleteFavoriteAsset(long userId, long classPK)
		throws NoSuchFavoriteAssetException, SystemException {

		favoriteAssetLocalService.deleteFavoriteAsset(userId, classPK);
	}

	public boolean isFavorite(long userId, String className, long classPK)
		throws SystemException {

		return favoriteAssetLocalService.isFavorite(userId, className, classPK);
	}

}