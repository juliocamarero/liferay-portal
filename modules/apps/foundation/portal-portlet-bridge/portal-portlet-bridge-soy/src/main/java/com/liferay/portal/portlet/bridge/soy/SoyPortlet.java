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

package com.liferay.portal.portlet.bridge.soy;

import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.PortletPreferencesIds;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;
import com.liferay.portal.kernel.portlet.InvokerPortlet;
import com.liferay.portal.kernel.portlet.PortletConfigFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletInstanceFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCCommandCache;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UnsyncPrintWriterPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.portlet.bridge.soy.internal.SoyPortletHelper;
import com.liferay.portal.template.soy.utils.SoyTemplateResourcesCollector;
import com.liferay.portlet.RenderRequestFactory;
import com.liferay.portlet.RenderRequestImpl;
import com.liferay.portlet.RenderResponseFactory;

import java.io.IOException;
import java.io.Writer;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.portlet.MimeResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Miroslav Ligas
 * @author Bruno Basto
 */
public class SoyPortlet extends MVCPortlet {

	@Override
	public void init() throws PortletException {
		super.init();

		propagateRequestParameters = GetterUtil.getBoolean(
			getInitParameter("propagate-request-parameters"), true);

		_bundle = FrameworkUtil.getBundle(this.getClass());

		try {
			_soyPortletHelper = new SoyPortletHelper(_bundle, viewTemplate);

			template = _getTemplate();
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		_render(renderRequest, renderResponse, false);
	}

	@Override
	public void serveResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException, PortletException {

		HttpServletResponse response = PortalUtil.getHttpServletResponse(
			resourceResponse);

		String portletId = PortalUtil.getPortletId(resourceRequest);

		Portlet portlet = _getPortlet(portletId);

		try {
			RenderRequestImpl renderRequestImpl = _createRenderRequest(
				resourceRequest, portlet);

			RenderResponse renderResponse = _createRenderResponse(
				renderRequestImpl, resourceResponse, portlet);

			_render(renderRequestImpl, renderResponse, true);

			String path = getPath(resourceRequest, resourceResponse);

			MVCRenderCommand mvcRenderCommand = getMVCRenderCommand(path);

			mvcRenderCommand.render(renderRequestImpl, renderResponse);

			response.setContentType(ContentTypes.APPLICATION_JSON);

			template.remove("id");

			ServletResponseUtil.write(
				response, _soyPortletHelper.serializeTemplate(template));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class MVCRenderCommandDescriptor {

		public MVCRenderCommandDescriptor(
			MVCRenderCommand mvcRenderCommand, Map<String, Object> properties) {

			_mvcRenderCommand = mvcRenderCommand;
			_properties = properties;
		}

		public MVCRenderCommand getImageEditorCapability() {
			return _mvcRenderCommand;
		}

		public Map<String, Object> getProperties() {
			return _properties;
		}

		private final MVCRenderCommand _mvcRenderCommand;
		private final Map<String, Object> _properties;

	}

	protected Set<String> getJavaScriptRequiredModules(String path) {
		return Collections.emptySet();
	}

	protected MVCRenderCommand getMVCRenderCommand(
		String mvcRenderCommandName) {

		MVCCommandCache mvcRenderCommandCache = getMVCRenderCommandCache();

		return (MVCRenderCommand)mvcRenderCommandCache.getMVCCommand(
			mvcRenderCommandName);
	}

	protected Set<String> getMVCRenderCommandNames() {
		MVCCommandCache mvcRenderCommandCache = getMVCRenderCommandCache();

		return mvcRenderCommandCache.getMVCCommandNames();
	}

	@Override
	protected String getPath(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		return ParamUtil.getString(
			portletRequest, "mvcRenderCommandName", viewTemplate);
	}

	protected String getPortletWrapperId(String portletNamespace) {
		StringBundler sb = new StringBundler(3);

		sb.append(portletNamespace);
		sb.append(StringPool.UNDERLINE);
		sb.append("SoyWrapper");

		return sb.toString();
	}

	@Override
	protected void include(
			String namespace, PortletRequest portletRequest,
			PortletResponse portletResponse, String lifecycle)
		throws IOException, PortletException {

		try {
			String portletNamespace = portletResponse.getNamespace();

			Writer writer = _getWriter(portletResponse);

			_writeTemplate(portletNamespace, writer);

			_writeJavaScript(
				portletRequest, portletResponse, portletNamespace, writer);
		}
		catch (Exception e) {
			throw new PortletException(e);
		}

		if (clearRequestParameters) {
			if (lifecycle.equals(PortletRequest.RENDER_PHASE)) {
				portletResponse.setProperty("clear-request-parameters", "true");
			}
		}
	}

	protected void populateJavaScriptTemplateContext(
		Template template, String portletNamespace) {

		String portletComponentId = _getPortletComponentId(portletNamespace);

		template.put(
			"element", "#" + getPortletWrapperId(portletNamespace) + " > div");
		template.put("id", portletComponentId);
	}

	protected void propagateRequestParameters(PortletRequest portletRequest) {
		Map<String, String[]> parametersMap = portletRequest.getParameterMap();

		for (Map.Entry<String, String[]> entry : parametersMap.entrySet()) {
			String parameterName = entry.getKey();
			String[] parameterValues = entry.getValue();

			if (parameterValues.length == 1) {
				template.put(parameterName, parameterValues[0]);
			}
			else if (parameterValues.length > 1) {
				template.put(parameterName, parameterValues);
			}
		}
	}

	protected boolean propagateRequestParameters;
	protected Template template;

	private RenderRequestImpl _createRenderRequest(
			ResourceRequest resourceRequest, Portlet portlet)
		throws Exception {

		HttpServletRequest request = PortalUtil.getHttpServletRequest(
			resourceRequest);

		ServletContext servletContext = (ServletContext)request.getAttribute(
			WebKeys.CTX);

		InvokerPortlet invokerPortlet = PortletInstanceFactoryUtil.create(
			portlet, servletContext);

		PortletPreferencesIds portletPreferencesIds =
			PortletPreferencesFactoryUtil.getPortletPreferencesIds(
				request, portlet.getPortletId());

		PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.getStrictPreferences(
				portletPreferencesIds);

		PortletConfig portletConfig = PortletConfigFactoryUtil.create(
			portlet, servletContext);

		PortletContext portletContext = portletConfig.getPortletContext();

		return RenderRequestFactory.create(
			request, portlet, invokerPortlet, portletContext,
			resourceRequest.getWindowState(), resourceRequest.getPortletMode(),
			portletPreferences);
	}

	private RenderResponse _createRenderResponse(
			RenderRequestImpl renderRequestImpl,
			ResourceResponse resourceResponse, Portlet portlet)
		throws Exception {

		HttpServletResponse response = PortalUtil.getHttpServletResponse(
			resourceResponse);

		return RenderResponseFactory.create(
			renderRequestImpl, response, portlet.getPortletId(),
			portlet.getCompanyId());
	}

	private FriendlyURLMapper _getFriendlyURLMapper(String portletId) {
		Portlet portlet = _getPortlet(portletId);

		return portlet.getFriendlyURLMapperInstance();
	}

	private Portlet _getPortlet(String portletId) {
		return PortletLocalServiceUtil.getPortletById(portletId);
	}

	private String _getPortletComponentId(String portletNamespace) {
		return portletNamespace.concat("PortletComponent");
	}

	private Template _getTemplate() throws TemplateException {
		SoyTemplateResourcesCollector soyTemplateResourcesCollector =
			new SoyTemplateResourcesCollector(_bundle, templatePath);

		return TemplateManagerUtil.getTemplate(
			TemplateConstants.LANG_TYPE_SOY,
			soyTemplateResourcesCollector.getTemplateResources(), false);
	}

	private Writer _getWriter(PortletResponse portletResponse)
		throws IOException {

		Writer writer = null;

		if (portletResponse instanceof MimeResponse) {
			MimeResponse mimeResponse = (MimeResponse)portletResponse;

			writer = UnsyncPrintWriterPool.borrow(mimeResponse.getWriter());
		}
		else {
			writer = new UnsyncStringWriter();
		}

		return writer;
	}

	private void _prepareTemplate(
		PortletRequest portletRequest, PortletResponse portletResponse,
		String portletNamespace) {

		String path = getPath(portletRequest, portletResponse);

		if (propagateRequestParameters) {
			propagateRequestParameters(portletRequest);
		}

		template.put(
			TemplateConstants.NAMESPACE,
			_soyPortletHelper.getTemplateNamespace(path));

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		template.prepare(httpServletRequest);

		populateJavaScriptTemplateContext(template, portletNamespace);
	}

	private void _render(
			RenderRequest renderRequest, RenderResponse renderResponse,
			boolean resource)
		throws IOException, PortletException {

		renderRequest.setAttribute(WebKeys.TEMPLATE, template);

		String portletNamespace = renderResponse.getNamespace();

		_prepareTemplate(renderRequest, renderResponse, portletNamespace);

		if (!resource) {
			super.render(renderRequest, renderResponse);
		}
	}

	private void _writeJavaScript(
			PortletRequest portletRequest, PortletResponse portletResponse,
			String portletNamespace, Writer writer)
		throws IOException {

		String portletId = PortalUtil.getPortletId(portletRequest);

		String path = getPath(portletRequest, portletResponse);

		Set<String> mvcRenderCommandNames = getMVCRenderCommandNames();

		String portletWrapperId = getPortletWrapperId(portletNamespace);

		String portletComponentId = _getPortletComponentId(portletNamespace);

		String portletJavaScript = _soyPortletHelper.getRouterJavaScript(
			path, portletComponentId, mvcRenderCommandNames, portletId,
			portletNamespace, portletWrapperId, template);

		writer.write(portletJavaScript);
	}

	private void _writeTemplate(String portletNamespace, Writer writer)
		throws Exception {

		writer.write("<div id=\"");
		writer.write(getPortletWrapperId(portletNamespace));
		writer.write("\">");

		template.processTemplate(writer);

		writer.write("</div>");
	}

	private Bundle _bundle;
	private SoyPortletHelper _soyPortletHelper;

}