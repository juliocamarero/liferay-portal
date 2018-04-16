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

package com.liferay.configuration.admin.web.internal.portlet.action;

import com.liferay.configuration.admin.constants.ConfigurationAdminPortletKeys;
import com.liferay.configuration.admin.web.internal.constants.ConfigurationAdminWebKeys;
import com.liferay.configuration.admin.web.internal.display.ConfigurationCategoryMenuDisplay;
import com.liferay.configuration.admin.web.internal.display.ConfigurationEntry;
import com.liferay.configuration.admin.web.internal.display.ConfigurationModelConfigurationEntry;
import com.liferay.configuration.admin.web.internal.model.ConfigurationModel;
import com.liferay.configuration.admin.web.internal.util.ConfigurationModelRetriever;
import com.liferay.configuration.admin.web.internal.util.DDMFormRendererHelper;
import com.liferay.configuration.admin.web.internal.util.ResourceBundleLoaderProvider;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderer;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.cm.Configuration;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Jorge Ferrer
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ConfigurationAdminPortletKeys.SYSTEM_SETTINGS,
		"mvc.command.name=/edit_configuration",
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = MVCRenderCommand.class
)
public class EditConfigurationMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		String factoryPid = ParamUtil.getString(renderRequest, "factoryPid");

		String pid = ParamUtil.getString(renderRequest, "pid", factoryPid);

		MVCRenderCommand customRenderCommand = _renderCommands.get(pid);

		if (customRenderCommand != null) {
			return customRenderCommand.render(renderRequest, renderResponse);
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Map<String, ConfigurationModel> configurationModels =
			_configurationModelRetriever.getConfigurationModels(
				themeDisplay.getLanguageId());

		ConfigurationModel configurationModel = configurationModels.get(pid);

		if ((configurationModel == null) && Validator.isNotNull(factoryPid)) {
			configurationModel = configurationModels.get(factoryPid);
		}

		if ((configurationModel != null) &&
			!configurationModel.isCompanyFactory()) {

			Configuration configuration =
				_configurationModelRetriever.getConfiguration(pid);

			configurationModel = new ConfigurationModel(
				configurationModel.getExtendedObjectClassDefinition(),
				configuration, configurationModel.getBundleSymbolicName(),
				configurationModel.getBundleLocation(),
				configurationModel.isFactory());
		}

		if (configurationModel != null) {
			ConfigurationCategoryMenuDisplay configurationCategoryMenuDisplay =
				_configurationModelRetriever.
					getConfigurationCategoryMenuDisplay(
						configurationModel.getCategory(),
						themeDisplay.getLanguageId());

			renderRequest.setAttribute(
				ConfigurationAdminWebKeys.CONFIGURATION_CATEGORY_MENU_DISPLAY,
				configurationCategoryMenuDisplay);

			ConfigurationEntry configurationEntry =
				new ConfigurationModelConfigurationEntry(
					configurationModel, _portal.getLocale(renderRequest),
					_resourceBundleLoaderProvider);

			renderRequest.setAttribute(
				ConfigurationAdminWebKeys.CONFIGURATION_ENTRY,
				configurationEntry);

			renderRequest.setAttribute(
				ConfigurationAdminWebKeys.CONFIGURATION_MODEL,
				configurationModel);

			DDMFormRendererHelper ddmFormRendererHelper =
				new DDMFormRendererHelper(
					renderRequest, renderResponse, configurationModel,
					_ddmFormRenderer, _resourceBundleLoaderProvider);

			renderRequest.setAttribute(
				ConfigurationAdminWebKeys.CONFIGURATION_MODEL_FORM_HTML,
				ddmFormRendererHelper.getDDMFormHTML());

			renderRequest.setAttribute(
				ConfigurationAdminWebKeys.RESOURCE_BUNDLE_LOADER_PROVIDER,
				_resourceBundleLoaderProvider);

			return "/edit_configuration.jsp";
		}

		SessionErrors.add(renderRequest, "entryInvalid");

		return "/error.jsp";
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(&(javax.portlet.name=" + ConfigurationAdminPortletKeys.SYSTEM_SETTINGS + ")(mvc.command.name=/edit_configuration)(configurationPid=*))",
		unbind = "removeRenderCommand"
	)
	protected void addRenderCommand(
		MVCRenderCommand mvcRenderCommand, Map<String, Object> properties) {

		_renderCommands.put(
			(String)properties.get("configurationPid"), mvcRenderCommand);
	}

	protected void removeRenderCommand(
		MVCRenderCommand mvcRenderCommand, Map<String, Object> properties) {

		_renderCommands.remove(properties.get("configurationPid"));
	}

	@Reference
	private ConfigurationModelRetriever _configurationModelRetriever;

	@Reference
	private DDMFormRenderer _ddmFormRenderer;

	@Reference
	private Portal _portal;

	private final Map<String, MVCRenderCommand> _renderCommands =
		new HashMap<>();

	@Reference
	private ResourceBundleLoaderProvider _resourceBundleLoaderProvider;

}