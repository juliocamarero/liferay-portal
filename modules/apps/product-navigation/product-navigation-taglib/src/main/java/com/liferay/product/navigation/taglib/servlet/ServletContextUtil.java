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

package com.liferay.product.navigation.taglib.servlet;

import com.liferay.product.navigation.control.menu.util.ProductNavigationControlMenuCategoryRegistry;
import com.liferay.product.navigation.control.menu.util.ProductNavigationControlMenuEntryRegistry;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Julio Camarero
 */
@Component(immediate = true)
public class ServletContextUtil {

	public static final String getContextPath() {
		ServletContext servletContext = _instance._getServletContext();

		return servletContext.getContextPath();
	}

	public static final ProductNavigationControlMenuCategoryRegistry
		getControlMenuCategoryRegistry() {

		return _instance._getControlMenuCategoryRegistry();
	}

	public static final ProductNavigationControlMenuEntryRegistry
		getControlMenuEntryRegistry() {

		return _instance._getControlMenuEntryRegistry();
	}

	public static final ServletContext getServletContext() {
		return _instance._getServletContext();
	}

	@Activate
	protected void activate() {
		_instance = this;
	}

	@Deactivate
	protected void deactivate() {
		_instance = null;
	}

	@Reference(unbind = "-")
	protected void setControlMenuCategoryRegistry(
		ProductNavigationControlMenuCategoryRegistry
			controlMenuCategoryRegistry) {

		_controlMenuCategoryRegistry = controlMenuCategoryRegistry;
	}

	@Reference(unbind = "-")
	protected void setControlMenuEntryRegistry(
		ProductNavigationControlMenuEntryRegistry controlMenuEntryRegistry) {

		_controlMenuEntryRegistry = controlMenuEntryRegistry;
	}

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.product.navigation.taglib)",
		unbind = "-"
	)
	protected void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	private ProductNavigationControlMenuCategoryRegistry
		_getControlMenuCategoryRegistry() {

		return _controlMenuCategoryRegistry;
	}

	private ProductNavigationControlMenuEntryRegistry
		_getControlMenuEntryRegistry() {

		return _controlMenuEntryRegistry;
	}

	private ServletContext _getServletContext() {
		return _servletContext;
	}

	private static ServletContextUtil _instance;

	private ProductNavigationControlMenuCategoryRegistry
		_controlMenuCategoryRegistry;
	private ProductNavigationControlMenuEntryRegistry _controlMenuEntryRegistry;
	private ServletContext _servletContext;

}