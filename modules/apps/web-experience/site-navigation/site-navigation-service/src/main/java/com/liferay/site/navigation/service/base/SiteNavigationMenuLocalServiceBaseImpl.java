/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.site.navigation.service.base;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.service.persistence.LayoutPersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import com.liferay.site.navigation.model.SiteNavigationMenu;
import com.liferay.site.navigation.service.SiteNavigationMenuLocalService;
import com.liferay.site.navigation.service.persistence.SiteNavigationMenuItemPersistence;
import com.liferay.site.navigation.service.persistence.SiteNavigationMenuPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the site navigation menu local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.site.navigation.service.impl.SiteNavigationMenuLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.site.navigation.service.impl.SiteNavigationMenuLocalServiceImpl
 * @see com.liferay.site.navigation.service.SiteNavigationMenuLocalServiceUtil
 * @generated
 */
@ProviderType
public abstract class SiteNavigationMenuLocalServiceBaseImpl
	extends BaseLocalServiceImpl implements SiteNavigationMenuLocalService,
		IdentifiableOSGiService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.site.navigation.service.SiteNavigationMenuLocalServiceUtil} to access the site navigation menu local service.
	 */

	/**
	 * Adds the site navigation menu to the database. Also notifies the appropriate model listeners.
	 *
	 * @param siteNavigationMenu the site navigation menu
	 * @return the site navigation menu that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SiteNavigationMenu addSiteNavigationMenu(
		SiteNavigationMenu siteNavigationMenu) {
		siteNavigationMenu.setNew(true);

		return siteNavigationMenuPersistence.update(siteNavigationMenu);
	}

	/**
	 * Creates a new site navigation menu with the primary key. Does not add the site navigation menu to the database.
	 *
	 * @param siteNavigationMenuId the primary key for the new site navigation menu
	 * @return the new site navigation menu
	 */
	@Override
	public SiteNavigationMenu createSiteNavigationMenu(
		long siteNavigationMenuId) {
		return siteNavigationMenuPersistence.create(siteNavigationMenuId);
	}

	/**
	 * Deletes the site navigation menu with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param siteNavigationMenuId the primary key of the site navigation menu
	 * @return the site navigation menu that was removed
	 * @throws PortalException if a site navigation menu with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SiteNavigationMenu deleteSiteNavigationMenu(
		long siteNavigationMenuId) throws PortalException {
		return siteNavigationMenuPersistence.remove(siteNavigationMenuId);
	}

	/**
	 * Deletes the site navigation menu from the database. Also notifies the appropriate model listeners.
	 *
	 * @param siteNavigationMenu the site navigation menu
	 * @return the site navigation menu that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SiteNavigationMenu deleteSiteNavigationMenu(
		SiteNavigationMenu siteNavigationMenu) throws PortalException {
		return siteNavigationMenuPersistence.remove(siteNavigationMenu);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(SiteNavigationMenu.class,
			clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return siteNavigationMenuPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.site.navigation.model.impl.SiteNavigationMenuModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end) {
		return siteNavigationMenuPersistence.findWithDynamicQuery(dynamicQuery,
			start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.site.navigation.model.impl.SiteNavigationMenuModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end, OrderByComparator<T> orderByComparator) {
		return siteNavigationMenuPersistence.findWithDynamicQuery(dynamicQuery,
			start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return siteNavigationMenuPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery,
		Projection projection) {
		return siteNavigationMenuPersistence.countWithDynamicQuery(dynamicQuery,
			projection);
	}

	@Override
	public SiteNavigationMenu fetchSiteNavigationMenu(long siteNavigationMenuId) {
		return siteNavigationMenuPersistence.fetchByPrimaryKey(siteNavigationMenuId);
	}

	/**
	 * Returns the site navigation menu with the primary key.
	 *
	 * @param siteNavigationMenuId the primary key of the site navigation menu
	 * @return the site navigation menu
	 * @throws PortalException if a site navigation menu with the primary key could not be found
	 */
	@Override
	public SiteNavigationMenu getSiteNavigationMenu(long siteNavigationMenuId)
		throws PortalException {
		return siteNavigationMenuPersistence.findByPrimaryKey(siteNavigationMenuId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery = new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(siteNavigationMenuLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(SiteNavigationMenu.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("siteNavigationMenuId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery() {
		IndexableActionableDynamicQuery indexableActionableDynamicQuery = new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(siteNavigationMenuLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(SiteNavigationMenu.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"siteNavigationMenuId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {
		actionableDynamicQuery.setBaseLocalService(siteNavigationMenuLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(SiteNavigationMenu.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("siteNavigationMenuId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {
		return siteNavigationMenuLocalService.deleteSiteNavigationMenu((SiteNavigationMenu)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {
		return siteNavigationMenuPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the site navigation menus.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.site.navigation.model.impl.SiteNavigationMenuModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of site navigation menus
	 * @param end the upper bound of the range of site navigation menus (not inclusive)
	 * @return the range of site navigation menus
	 */
	@Override
	public List<SiteNavigationMenu> getSiteNavigationMenus(int start, int end) {
		return siteNavigationMenuPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of site navigation menus.
	 *
	 * @return the number of site navigation menus
	 */
	@Override
	public int getSiteNavigationMenusCount() {
		return siteNavigationMenuPersistence.countAll();
	}

	/**
	 * Updates the site navigation menu in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param siteNavigationMenu the site navigation menu
	 * @return the site navigation menu that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SiteNavigationMenu updateSiteNavigationMenu(
		SiteNavigationMenu siteNavigationMenu) {
		return siteNavigationMenuPersistence.update(siteNavigationMenu);
	}

	/**
	 * Returns the site navigation menu local service.
	 *
	 * @return the site navigation menu local service
	 */
	public SiteNavigationMenuLocalService getSiteNavigationMenuLocalService() {
		return siteNavigationMenuLocalService;
	}

	/**
	 * Sets the site navigation menu local service.
	 *
	 * @param siteNavigationMenuLocalService the site navigation menu local service
	 */
	public void setSiteNavigationMenuLocalService(
		SiteNavigationMenuLocalService siteNavigationMenuLocalService) {
		this.siteNavigationMenuLocalService = siteNavigationMenuLocalService;
	}

	/**
	 * Returns the site navigation menu persistence.
	 *
	 * @return the site navigation menu persistence
	 */
	public SiteNavigationMenuPersistence getSiteNavigationMenuPersistence() {
		return siteNavigationMenuPersistence;
	}

	/**
	 * Sets the site navigation menu persistence.
	 *
	 * @param siteNavigationMenuPersistence the site navigation menu persistence
	 */
	public void setSiteNavigationMenuPersistence(
		SiteNavigationMenuPersistence siteNavigationMenuPersistence) {
		this.siteNavigationMenuPersistence = siteNavigationMenuPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the layout local service.
	 *
	 * @return the layout local service
	 */
	public com.liferay.portal.kernel.service.LayoutLocalService getLayoutLocalService() {
		return layoutLocalService;
	}

	/**
	 * Sets the layout local service.
	 *
	 * @param layoutLocalService the layout local service
	 */
	public void setLayoutLocalService(
		com.liferay.portal.kernel.service.LayoutLocalService layoutLocalService) {
		this.layoutLocalService = layoutLocalService;
	}

	/**
	 * Returns the layout persistence.
	 *
	 * @return the layout persistence
	 */
	public LayoutPersistence getLayoutPersistence() {
		return layoutPersistence;
	}

	/**
	 * Sets the layout persistence.
	 *
	 * @param layoutPersistence the layout persistence
	 */
	public void setLayoutPersistence(LayoutPersistence layoutPersistence) {
		this.layoutPersistence = layoutPersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	/**
	 * Returns the site navigation menu item local service.
	 *
	 * @return the site navigation menu item local service
	 */
	public com.liferay.site.navigation.service.SiteNavigationMenuItemLocalService getSiteNavigationMenuItemLocalService() {
		return siteNavigationMenuItemLocalService;
	}

	/**
	 * Sets the site navigation menu item local service.
	 *
	 * @param siteNavigationMenuItemLocalService the site navigation menu item local service
	 */
	public void setSiteNavigationMenuItemLocalService(
		com.liferay.site.navigation.service.SiteNavigationMenuItemLocalService siteNavigationMenuItemLocalService) {
		this.siteNavigationMenuItemLocalService = siteNavigationMenuItemLocalService;
	}

	/**
	 * Returns the site navigation menu item persistence.
	 *
	 * @return the site navigation menu item persistence
	 */
	public SiteNavigationMenuItemPersistence getSiteNavigationMenuItemPersistence() {
		return siteNavigationMenuItemPersistence;
	}

	/**
	 * Sets the site navigation menu item persistence.
	 *
	 * @param siteNavigationMenuItemPersistence the site navigation menu item persistence
	 */
	public void setSiteNavigationMenuItemPersistence(
		SiteNavigationMenuItemPersistence siteNavigationMenuItemPersistence) {
		this.siteNavigationMenuItemPersistence = siteNavigationMenuItemPersistence;
	}

	public void afterPropertiesSet() {
		persistedModelLocalServiceRegistry.register("com.liferay.site.navigation.model.SiteNavigationMenu",
			siteNavigationMenuLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.site.navigation.model.SiteNavigationMenu");
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return SiteNavigationMenuLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return SiteNavigationMenu.class;
	}

	protected String getModelClassName() {
		return SiteNavigationMenu.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = siteNavigationMenuPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = SiteNavigationMenuLocalService.class)
	protected SiteNavigationMenuLocalService siteNavigationMenuLocalService;
	@BeanReference(type = SiteNavigationMenuPersistence.class)
	protected SiteNavigationMenuPersistence siteNavigationMenuPersistence;
	@ServiceReference(type = com.liferay.counter.kernel.service.CounterLocalService.class)
	protected com.liferay.counter.kernel.service.CounterLocalService counterLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.LayoutLocalService.class)
	protected com.liferay.portal.kernel.service.LayoutLocalService layoutLocalService;
	@ServiceReference(type = LayoutPersistence.class)
	protected LayoutPersistence layoutPersistence;
	@ServiceReference(type = com.liferay.portal.kernel.service.ResourceLocalService.class)
	protected com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.UserLocalService.class)
	protected com.liferay.portal.kernel.service.UserLocalService userLocalService;
	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	@BeanReference(type = com.liferay.site.navigation.service.SiteNavigationMenuItemLocalService.class)
	protected com.liferay.site.navigation.service.SiteNavigationMenuItemLocalService siteNavigationMenuItemLocalService;
	@BeanReference(type = SiteNavigationMenuItemPersistence.class)
	protected SiteNavigationMenuItemPersistence siteNavigationMenuItemPersistence;
	@ServiceReference(type = PersistedModelLocalServiceRegistry.class)
	protected PersistedModelLocalServiceRegistry persistedModelLocalServiceRegistry;
}