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

package com.liferay.portal.service;

import aQute.bnd.annotation.ProviderType;

/**
 * Provides a wrapper for {@link LayoutTemplateLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see LayoutTemplateLocalService
 * @generated
 */
@ProviderType
public class LayoutTemplateLocalServiceWrapper
	implements LayoutTemplateLocalService,
		ServiceWrapper<LayoutTemplateLocalService> {
	public LayoutTemplateLocalServiceWrapper(
		LayoutTemplateLocalService layoutTemplateLocalService) {
		_layoutTemplateLocalService = layoutTemplateLocalService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _layoutTemplateLocalService.getBeanIdentifier();
	}

	@Override
	public java.lang.String getContent(java.lang.String layoutTemplateId,
		boolean standard, java.lang.String themeId) {
		return _layoutTemplateLocalService.getContent(layoutTemplateId,
			standard, themeId);
	}

	@Override
	public com.liferay.portal.model.LayoutTemplate getLayoutTemplate(
		java.lang.String layoutTemplateId, boolean standard,
		java.lang.String themeId) {
		return _layoutTemplateLocalService.getLayoutTemplate(layoutTemplateId,
			standard, themeId);
	}

	@Override
	public java.util.List<com.liferay.portal.model.LayoutTemplate> getLayoutTemplates() {
		return _layoutTemplateLocalService.getLayoutTemplates();
	}

	@Override
	public java.util.List<com.liferay.portal.model.LayoutTemplate> getLayoutTemplates(
		java.lang.String themeId) {
		return _layoutTemplateLocalService.getLayoutTemplates(themeId);
	}

	@Override
	public java.lang.String getWapContent(java.lang.String layoutTemplateId,
		boolean standard, java.lang.String themeId) {
		return _layoutTemplateLocalService.getWapContent(layoutTemplateId,
			standard, themeId);
	}

	@Override
	public java.util.List<com.liferay.portal.model.LayoutTemplate> init(
		javax.servlet.ServletContext servletContext, java.lang.String[] xmls,
		com.liferay.portal.kernel.plugin.PluginPackage pluginPackage) {
		return _layoutTemplateLocalService.init(servletContext, xmls,
			pluginPackage);
	}

	@Override
	public java.util.List<com.liferay.portal.model.LayoutTemplate> init(
		java.lang.String servletContextName,
		javax.servlet.ServletContext servletContext, java.lang.String[] xmls,
		com.liferay.portal.kernel.plugin.PluginPackage pluginPackage) {
		return _layoutTemplateLocalService.init(servletContextName,
			servletContext, xmls, pluginPackage);
	}

	@Override
	public void readLayoutTemplate(java.lang.String servletContextName,
		javax.servlet.ServletContext servletContext,
		java.util.Set<com.liferay.portal.model.LayoutTemplate> layoutTemplates,
		com.liferay.portal.kernel.xml.Element element, boolean standard,
		java.lang.String themeId,
		com.liferay.portal.kernel.plugin.PluginPackage pluginPackage) {
		_layoutTemplateLocalService.readLayoutTemplate(servletContextName,
			servletContext, layoutTemplates, element, standard, themeId,
			pluginPackage);
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_layoutTemplateLocalService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public void uninstallLayoutTemplate(java.lang.String layoutTemplateId,
		boolean standard) {
		_layoutTemplateLocalService.uninstallLayoutTemplate(layoutTemplateId,
			standard);
	}

	@Override
	public void uninstallLayoutTemplates(java.lang.String themeId) {
		_layoutTemplateLocalService.uninstallLayoutTemplates(themeId);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	@Deprecated
	public LayoutTemplateLocalService getWrappedLayoutTemplateLocalService() {
		return _layoutTemplateLocalService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	@Deprecated
	public void setWrappedLayoutTemplateLocalService(
		LayoutTemplateLocalService layoutTemplateLocalService) {
		_layoutTemplateLocalService = layoutTemplateLocalService;
	}

	@Override
	public LayoutTemplateLocalService getWrappedService() {
		return _layoutTemplateLocalService;
	}

	@Override
	public void setWrappedService(
		LayoutTemplateLocalService layoutTemplateLocalService) {
		_layoutTemplateLocalService = layoutTemplateLocalService;
	}

	private LayoutTemplateLocalService _layoutTemplateLocalService;
}