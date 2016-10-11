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

package com.liferay.portal.portlet.bridge.soy.internal;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;
import com.liferay.portal.kernel.portlet.Route;
import com.liferay.portal.kernel.portlet.Router;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.InputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;

/**
 * @author Bruno Basto
 */
public class SoyPortletHelper {

	public SoyPortletHelper(Bundle bundle, String defaultMVCRenderCommandName)
		throws Exception {

		_bundle = bundle;
		_defaultMVCRenderCommandName = defaultMVCRenderCommandName;
		_moduleName = getModuleName();
		_jsonSerializer = JSONFactoryUtil.createJSONSerializer();
		_routerJavaScriptTPL = _getRouterJavaScriptTPL();
	}

	public String getRouterJavaScript(
		String currentMVCRenderCommandName, String elementId,
		Set<String> mvcRenderCommandNames, String portletId,
		String portletNamespace, String portletWrapperId,
		FriendlyURLMapper friendlyURLMapper, Template template) {

		Set<String> filteredMVCRenderCommandNames = new LinkedHashSet<>();

		Set<String> modules = new LinkedHashSet<>();

		for (String mvcRenderCommandName : mvcRenderCommandNames) {
			if (mvcRenderCommandName.equals(StringPool.SLASH)) {
				mvcRenderCommandName = _defaultMVCRenderCommandName;
			}

			if (filteredMVCRenderCommandNames.contains(mvcRenderCommandName)) {
				continue;
			}

			filteredMVCRenderCommandNames.add(mvcRenderCommandName);

			modules.add(_getModulePath(mvcRenderCommandName));
		}

		String modulesString = _jsonSerializer.serialize(modules);

		String mvcRenderCommandNamesString = _jsonSerializer.serialize(
			filteredMVCRenderCommandNames);

		template.remove("element");

		String contextString = _jsonSerializer.serializeDeep(template);

		Router router = friendlyURLMapper.getRouter();

		String friendlyURLMapping = friendlyURLMapper.getMapping();

		List<Map<String, Object>> friendlyURLRoutes = _getFriendlyURLRoutes(
			router.getRoutes());

		String friendlyURLRoutesString = _jsonSerializer.serializeDeep(
			friendlyURLRoutes);

		return StringUtil.replace(
			_routerJavaScriptTPL,
			new String[] {
				"$CURRENT_MVC_RENDER_COMMAND_NAME", "$DEFAULT_MVC_COMMAND_NAME",
				"$ELEMENT_ID", "$MVC_RENDER_COMMAND_NAMES", "$MODULES",
				"$PORTLET_ID", "$PORTLET_NAMESPACE", "$PORTLET_WRAPPER_ID",
				"$CONTEXT", "$FRIENDLY_URL_ROUTES", "$FRIENDLY_URL_MAPPING"
			},
			new String[] {
				currentMVCRenderCommandName, _defaultMVCRenderCommandName,
				elementId, mvcRenderCommandNamesString, modulesString,
				portletId, portletNamespace, portletWrapperId, contextString,
				friendlyURLRoutesString, friendlyURLMapping
			});
	}

	public String getTemplateNamespace(String path) {
		return path.concat(".render");
	}

	public String serializeTemplate(Template template) {
		return _jsonSerializer.serializeDeep(template);
	}

	protected String getControllerName(String mvcRenderCommandName) {
		String controllerName = _controllersMap.get(mvcRenderCommandName);

		if (controllerName != null) {
			return controllerName;
		}

		URL url = _bundle.getEntry(
			"/META-INF/resources/".concat(mvcRenderCommandName).concat(
				".es.js"));

		if (url != null) {
			controllerName = mvcRenderCommandName.concat(".es");
		}
		else {
			controllerName = mvcRenderCommandName.concat(".soy");
		}

		_controllersMap.put(mvcRenderCommandName, controllerName);

		return controllerName;
	}

	protected String getModuleName() throws Exception {
		URL url = _bundle.getEntry("package.json");

		if (url == null) {
			return null;
		}

		String json = StringUtil.read(url.openStream());

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(json);

		String moduleName = jsonObject.getString("name");

		if (Validator.isNull(moduleName)) {
			return null;
		}

		return moduleName;
	}

	private List<Map<String, Object>> _getFriendlyURLRoutes(
		List<Route> routes) {

		List<Map<String, Object>> routesMapping = new ArrayList<>();

		for (Route route : routes) {
			Map<String, Object> mapping = new HashMap<>();

			mapping.put("implicitParameters", route.getImplicitParameters());
			mapping.put("pattern", route.getPattern());

			routesMapping.add(mapping);
		}

		return routesMapping;
	}

	private String _getModulePath(String mvcRenderCommandName) {
		String controllerName = getControllerName(mvcRenderCommandName);

		return _moduleName.concat(StringPool.SLASH).concat(controllerName);
	}

	private String _getRouterJavaScriptTPL() throws Exception {
		Class<?> clazz = getClass();

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/router.js.tpl");

		return StringUtil.read(inputStream);
	}

	private final Bundle _bundle;
	private final Map<String, String> _controllersMap = new HashMap<>();
	private final String _defaultMVCRenderCommandName;
	private final JSONSerializer _jsonSerializer;
	private final String _moduleName;
	private final String _routerJavaScriptTPL;

}