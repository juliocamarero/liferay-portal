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

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseServiceImpl;
import com.liferay.portal.kernel.service.persistence.LayoutPersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import com.liferay.site.navigation.model.SiteNavigationMenu;
import com.liferay.site.navigation.service.SiteNavigationMenuService;
import com.liferay.site.navigation.service.persistence.SiteNavigationMenuItemPersistence;
import com.liferay.site.navigation.service.persistence.SiteNavigationMenuPersistence;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the site navigation menu remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.site.navigation.service.impl.SiteNavigationMenuServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.site.navigation.service.impl.SiteNavigationMenuServiceImpl
 * @see com.liferay.site.navigation.service.SiteNavigationMenuServiceUtil
 * @generated
 */
public abstract class SiteNavigationMenuServiceBaseImpl extends BaseServiceImpl
	implements SiteNavigationMenuService, IdentifiableOSGiService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.site.navigation.service.SiteNavigationMenuServiceUtil} to access the site navigation menu remote service.
	 */

	/**
	 * Returns the site navigation menu local service.
	 *
	 * @return the site navigation menu local service
	 */
	public com.liferay.site.navigation.service.SiteNavigationMenuLocalService getSiteNavigationMenuLocalService() {
		return siteNavigationMenuLocalService;
	}

	/**
	 * Sets the site navigation menu local service.
	 *
	 * @param siteNavigationMenuLocalService the site navigation menu local service
	 */
	public void setSiteNavigationMenuLocalService(
		com.liferay.site.navigation.service.SiteNavigationMenuLocalService siteNavigationMenuLocalService) {
		this.siteNavigationMenuLocalService = siteNavigationMenuLocalService;
	}

	/**
	 * Returns the site navigation menu remote service.
	 *
	 * @return the site navigation menu remote service
	 */
	public SiteNavigationMenuService getSiteNavigationMenuService() {
		return siteNavigationMenuService;
	}

	/**
	 * Sets the site navigation menu remote service.
	 *
	 * @param siteNavigationMenuService the site navigation menu remote service
	 */
	public void setSiteNavigationMenuService(
		SiteNavigationMenuService siteNavigationMenuService) {
		this.siteNavigationMenuService = siteNavigationMenuService;
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
	 * Returns the layout remote service.
	 *
	 * @return the layout remote service
	 */
	public com.liferay.portal.kernel.service.LayoutService getLayoutService() {
		return layoutService;
	}

	/**
	 * Sets the layout remote service.
	 *
	 * @param layoutService the layout remote service
	 */
	public void setLayoutService(
		com.liferay.portal.kernel.service.LayoutService layoutService) {
		this.layoutService = layoutService;
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
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public com.liferay.portal.kernel.service.UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(
		com.liferay.portal.kernel.service.UserService userService) {
		this.userService = userService;
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
	 * Returns the site navigation menu item remote service.
	 *
	 * @return the site navigation menu item remote service
	 */
	public com.liferay.site.navigation.service.SiteNavigationMenuItemService getSiteNavigationMenuItemService() {
		return siteNavigationMenuItemService;
	}

	/**
	 * Sets the site navigation menu item remote service.
	 *
	 * @param siteNavigationMenuItemService the site navigation menu item remote service
	 */
	public void setSiteNavigationMenuItemService(
		com.liferay.site.navigation.service.SiteNavigationMenuItemService siteNavigationMenuItemService) {
		this.siteNavigationMenuItemService = siteNavigationMenuItemService;
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
	}

	public void destroy() {
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return SiteNavigationMenuService.class.getName();
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

	@BeanReference(type = com.liferay.site.navigation.service.SiteNavigationMenuLocalService.class)
	protected com.liferay.site.navigation.service.SiteNavigationMenuLocalService siteNavigationMenuLocalService;
	@BeanReference(type = SiteNavigationMenuService.class)
	protected SiteNavigationMenuService siteNavigationMenuService;
	@BeanReference(type = SiteNavigationMenuPersistence.class)
	protected SiteNavigationMenuPersistence siteNavigationMenuPersistence;
	@ServiceReference(type = com.liferay.counter.kernel.service.CounterLocalService.class)
	protected com.liferay.counter.kernel.service.CounterLocalService counterLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.LayoutLocalService.class)
	protected com.liferay.portal.kernel.service.LayoutLocalService layoutLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.LayoutService.class)
	protected com.liferay.portal.kernel.service.LayoutService layoutService;
	@ServiceReference(type = LayoutPersistence.class)
	protected LayoutPersistence layoutPersistence;
	@ServiceReference(type = com.liferay.portal.kernel.service.ResourceLocalService.class)
	protected com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.UserLocalService.class)
	protected com.liferay.portal.kernel.service.UserLocalService userLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.UserService.class)
	protected com.liferay.portal.kernel.service.UserService userService;
	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	@BeanReference(type = com.liferay.site.navigation.service.SiteNavigationMenuItemLocalService.class)
	protected com.liferay.site.navigation.service.SiteNavigationMenuItemLocalService siteNavigationMenuItemLocalService;
	@BeanReference(type = com.liferay.site.navigation.service.SiteNavigationMenuItemService.class)
	protected com.liferay.site.navigation.service.SiteNavigationMenuItemService siteNavigationMenuItemService;
	@BeanReference(type = SiteNavigationMenuItemPersistence.class)
	protected SiteNavigationMenuItemPersistence siteNavigationMenuItemPersistence;
}