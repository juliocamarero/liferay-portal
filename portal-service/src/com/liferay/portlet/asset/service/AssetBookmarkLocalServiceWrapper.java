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

package com.liferay.portlet.asset.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link AssetBookmarkLocalService}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       AssetBookmarkLocalService
 * @generated
 */
public class AssetBookmarkLocalServiceWrapper
	implements AssetBookmarkLocalService,
		ServiceWrapper<AssetBookmarkLocalService> {
	public AssetBookmarkLocalServiceWrapper(
		AssetBookmarkLocalService assetBookmarkLocalService) {
		_assetBookmarkLocalService = assetBookmarkLocalService;
	}

	/**
	* Adds the asset bookmark to the database. Also notifies the appropriate model listeners.
	*
	* @param assetBookmark the asset bookmark
	* @return the asset bookmark that was added
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.asset.model.AssetBookmark addAssetBookmark(
		com.liferay.portlet.asset.model.AssetBookmark assetBookmark)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.addAssetBookmark(assetBookmark);
	}

	/**
	* Creates a new asset bookmark with the primary key. Does not add the asset bookmark to the database.
	*
	* @param bookmarkId the primary key for the new asset bookmark
	* @return the new asset bookmark
	*/
	public com.liferay.portlet.asset.model.AssetBookmark createAssetBookmark(
		long bookmarkId) {
		return _assetBookmarkLocalService.createAssetBookmark(bookmarkId);
	}

	/**
	* Deletes the asset bookmark with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param bookmarkId the primary key of the asset bookmark
	* @return the asset bookmark that was removed
	* @throws PortalException if a asset bookmark with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.asset.model.AssetBookmark deleteAssetBookmark(
		long bookmarkId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.deleteAssetBookmark(bookmarkId);
	}

	/**
	* Deletes the asset bookmark from the database. Also notifies the appropriate model listeners.
	*
	* @param assetBookmark the asset bookmark
	* @return the asset bookmark that was removed
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.asset.model.AssetBookmark deleteAssetBookmark(
		com.liferay.portlet.asset.model.AssetBookmark assetBookmark)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.deleteAssetBookmark(assetBookmark);
	}

	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _assetBookmarkLocalService.dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetBookmarkModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) throws com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetBookmarkModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.dynamicQuery(dynamicQuery, start,
			end, orderByComparator);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows that match the dynamic query
	* @throws SystemException if a system exception occurred
	*/
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.dynamicQueryCount(dynamicQuery);
	}

	public com.liferay.portlet.asset.model.AssetBookmark fetchAssetBookmark(
		long bookmarkId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.fetchAssetBookmark(bookmarkId);
	}

	/**
	* Returns the asset bookmark with the primary key.
	*
	* @param bookmarkId the primary key of the asset bookmark
	* @return the asset bookmark
	* @throws PortalException if a asset bookmark with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.asset.model.AssetBookmark getAssetBookmark(
		long bookmarkId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.getAssetBookmark(bookmarkId);
	}

	public com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	* Returns a range of all the asset bookmarks.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetBookmarkModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of asset bookmarks
	* @param end the upper bound of the range of asset bookmarks (not inclusive)
	* @return the range of asset bookmarks
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portlet.asset.model.AssetBookmark> getAssetBookmarks(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.getAssetBookmarks(start, end);
	}

	/**
	* Returns the number of asset bookmarks.
	*
	* @return the number of asset bookmarks
	* @throws SystemException if a system exception occurred
	*/
	public int getAssetBookmarksCount()
		throws com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.getAssetBookmarksCount();
	}

	/**
	* Updates the asset bookmark in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param assetBookmark the asset bookmark
	* @return the asset bookmark that was updated
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.asset.model.AssetBookmark updateAssetBookmark(
		com.liferay.portlet.asset.model.AssetBookmark assetBookmark)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.updateAssetBookmark(assetBookmark);
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public java.lang.String getBeanIdentifier() {
		return _assetBookmarkLocalService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_assetBookmarkLocalService.setBeanIdentifier(beanIdentifier);
	}

	public com.liferay.portlet.asset.model.AssetBookmark addAssetBookmark(
		long groupId, long userId, java.lang.String className, long classPK)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.addAssetBookmark(groupId, userId,
			className, classPK);
	}

	public void deleteAssetBookmark(long userId, long classPK)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		_assetBookmarkLocalService.deleteAssetBookmark(userId, classPK);
	}

	public boolean isFavorite(long userId, java.lang.String className,
		long classPK)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _assetBookmarkLocalService.isFavorite(userId, className, classPK);
	}

	public void updateAsset(long groupId, long userId,
		com.liferay.portlet.asset.model.AssetBookmark assetBookmark,
		long[] assetCategoryIds)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		_assetBookmarkLocalService.updateAsset(groupId, userId, assetBookmark,
			assetCategoryIds);
	}

	/**
	 * @deprecated Renamed to {@link #getWrappedService}
	 */
	public AssetBookmarkLocalService getWrappedAssetBookmarkLocalService() {
		return _assetBookmarkLocalService;
	}

	/**
	 * @deprecated Renamed to {@link #setWrappedService}
	 */
	public void setWrappedAssetBookmarkLocalService(
		AssetBookmarkLocalService assetBookmarkLocalService) {
		_assetBookmarkLocalService = assetBookmarkLocalService;
	}

	public AssetBookmarkLocalService getWrappedService() {
		return _assetBookmarkLocalService;
	}

	public void setWrappedService(
		AssetBookmarkLocalService assetBookmarkLocalService) {
		_assetBookmarkLocalService = assetBookmarkLocalService;
	}

	private AssetBookmarkLocalService _assetBookmarkLocalService;
}