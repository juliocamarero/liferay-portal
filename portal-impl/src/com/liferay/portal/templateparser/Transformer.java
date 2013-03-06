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

package com.liferay.portal.templateparser;

import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.mobile.device.Device;
import com.liferay.portal.kernel.mobile.device.UnknownDevice;
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateContextType;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.templateparser.TemplateNode;
import com.liferay.portal.kernel.templateparser.TransformException;
import com.liferay.portal.kernel.templateparser.TransformerListener;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.xsl.XSLTemplateResource;
import com.liferay.portal.xsl.XSLURIResolver;
import com.liferay.portlet.journal.util.JournalXSLURIResolver;
import com.liferay.portlet.portletdisplaytemplate.util.PortletDisplayTemplateConstants;
import com.liferay.taglib.util.VelocityTaglib;
import com.liferay.util.PwdGenerator;

import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 * @author Wesley Gong
 * @author Angelo Jefferson
 * @author Hugo Huijser
 * @author Marcellus Tavares
 * @author Juan Fernández
 */
public class Transformer {

	public Transformer(
		String transformerListenerPropertyKey, String errorTemplatePropertyKey,
		TemplateContextType defaultTemplateContextType) {

		_transformerListenerClassNames = SetUtil.fromArray(
			PropsUtil.getArray(transformerListenerPropertyKey));

		Set<String> langTypes = TemplateManagerUtil.getSupportedLanguageTypes(
			errorTemplatePropertyKey);

		for (String langType : langTypes) {
			String errorTemplateId = PropsUtil.get(
				errorTemplatePropertyKey, new Filter(langType));

			if (Validator.isNotNull(errorTemplateId)) {
				_errorTemplateIds.put(langType, errorTemplateId);
			}
		}

		_defaultTemplateContextType = defaultTemplateContextType;
	}

	public String transform(
			ThemeDisplay themeDisplay, Map<String, Object> contextObjects,
			String script, String langType)
		throws Exception {

		if (Validator.isNull(langType)) {
			return null;
		}

		long companyId = 0;
		long companyGroupId = 0;
		long groupId = 0;

		if (themeDisplay != null) {
			companyId = themeDisplay.getCompanyId();
			companyGroupId = themeDisplay.getCompanyGroupId();
			groupId = themeDisplay.getScopeGroupId();
		}

		String templateId = String.valueOf(contextObjects.get("template_id"));

		templateId = getTemplateId(
			templateId, companyId, companyGroupId, groupId);

		Template template = getTemplate(templateId, script, langType);

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		boolean load = false;

		try {
			if (contextObjects != null) {
				for (String key : contextObjects.keySet()) {
					template.put(key, contextObjects.get(key));
				}
			}

			template.put("company", getCompany(themeDisplay, companyId));
			template.put("companyId", companyId);
			template.put("device", getDevice(themeDisplay));
			template.put("groupId", groupId);
			template.put(
				"permissionChecker",
				PermissionThreadLocal.getPermissionChecker());
			template.put(
				"randomNamespace",
				PwdGenerator.getPassword(PwdGenerator.KEY3, 4) +
					StringPool.UNDERLINE);
			template.put(
				"templatesPath", TemplateConstants.TEMPLATES_SEPARATOR);

			load = mergeTemplate(template, unsyncStringWriter);
		}
		catch (Exception e) {
			throw new TransformException("Unhandled exception", e);
		}

		if (!load) {
			throw new TransformException(
				"Unable to dynamically load transform script");
		}

		return unsyncStringWriter.toString();
	}

	public String transform(
			ThemeDisplay themeDisplay, Map<String, String> tokens,
			String viewMode, String languageId, String xml, String script,
			String langType)
		throws Exception {

		// Setup listeners

		if (_log.isDebugEnabled()) {
			_log.debug("Language " + languageId);
		}

		if (Validator.isNull(viewMode)) {
			viewMode = Constants.VIEW;
		}

		if (_logTokens.isDebugEnabled()) {
			String tokensString = PropertiesUtil.list(tokens);

			_logTokens.debug(tokensString);
		}

		if (_logTransformBefore.isDebugEnabled()) {
			_logTransformBefore.debug(xml);
		}

		List<TransformerListener> transformerListeners =
			new ArrayList<TransformerListener>();

		for (String transformerListenersClassName :
				_transformerListenerClassNames) {

			TransformerListener transformerListener = null;

			try {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Instantiate listener " +
							transformerListenersClassName);
				}

				ClassLoader classLoader =
					PortalClassLoaderUtil.getClassLoader();

				transformerListener =
					(TransformerListener)InstanceFactory.newInstance(
						classLoader, transformerListenersClassName);

				transformerListeners.add(transformerListener);
			}
			catch (Exception e) {
				_log.error(e, e);
			}

			// Modify XML

			if (_logXmlBeforeListener.isDebugEnabled()) {
				_logXmlBeforeListener.debug(xml);
			}

			if (transformerListener != null) {
				xml = transformerListener.onXml(xml, languageId, tokens);

				if (_logXmlAfterListener.isDebugEnabled()) {
					_logXmlAfterListener.debug(xml);
				}
			}

			// Modify script

			if (_logScriptBeforeListener.isDebugEnabled()) {
				_logScriptBeforeListener.debug(script);
			}

			if (transformerListener != null) {
				script = transformerListener.onScript(
					script, xml, languageId, tokens);

				if (_logScriptAfterListener.isDebugEnabled()) {
					_logScriptAfterListener.debug(script);
				}
			}
		}

		// Transform

		String output = null;

		if (Validator.isNull(langType)) {
			output = LocalizationUtil.getLocalization(xml, languageId);
		}
		else {
			long companyId = 0;
			long companyGroupId = 0;
			long groupId = 0;

			if (themeDisplay != null) {
				companyId = themeDisplay.getCompanyId();
				companyGroupId = themeDisplay.getCompanyGroupId();
				groupId = themeDisplay.getScopeGroupId();
			}
			else if (tokens != null) {
				companyId = GetterUtil.getLong(tokens.get("company_id"));
				companyGroupId = GetterUtil.getLong(
					tokens.get("company_group_id"));
				groupId = GetterUtil.getLong(tokens.get("group_id"));
			}

			String templateId = tokens.get("template_id");

			templateId = getTemplateId(
				templateId, companyId, companyGroupId, groupId);

			Template template = getTemplate(
				templateId, tokens, languageId, xml, script, langType);

			UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

			boolean load = false;

			try {
				if (Validator.isNotNull(xml)) {
					Document document = SAXReaderUtil.read(xml);

					Element rootElement = document.getRootElement();

					List<TemplateNode> templateNodes = getTemplateNodes(
						themeDisplay, rootElement);

					if (templateNodes != null) {
						for (TemplateNode templateNode : templateNodes) {
							template.put(templateNode.getName(), templateNode);
						}
					}

					Element requestElement = rootElement.element("request");

					template.put(
						"request", insertRequestVariables(requestElement));

					template.put("xmlRequest", requestElement.asXML());
				}

				template.put("company", getCompany(themeDisplay, companyId));
				template.put("companyId", companyId);
				template.put("device", getDevice(themeDisplay));
				template.put("groupId", groupId);

				Locale locale = LocaleUtil.fromLanguageId(languageId);

				template.put("locale", locale);

				template.put(
					"permissionChecker",
					PermissionThreadLocal.getPermissionChecker());
				template.put(
					"randomNamespace",
					PwdGenerator.getPassword(PwdGenerator.KEY3, 4) +
						StringPool.UNDERLINE);
				template.put(
					"templatesPath", TemplateConstants.TEMPLATES_SEPARATOR);
				template.put("viewMode", viewMode);

				load = mergeTemplate(template, unsyncStringWriter);
			}
			catch (Exception e) {
				if (e instanceof DocumentException) {
					throw new TransformException(
						"Unable to read XML document", e);
				}
				else if (e instanceof IOException) {
					throw new TransformException("Error reading template", e);
				}
				else if (e instanceof TransformException) {
					throw (TransformException)e;
				}
				else {
					throw new TransformException("Unhandled exception", e);
				}
			}

			if (!load) {
				throw new TransformException(
					"Unable to dynamically load transform script");
			}

			output = unsyncStringWriter.toString();
		}

		// Postprocess output

		for (TransformerListener transformerListener : transformerListeners) {

			// Modify output

			if (_logOutputBeforeListener.isDebugEnabled()) {
				_logOutputBeforeListener.debug(output);
			}

			output = transformerListener.onOutput(output, languageId, tokens);

			if (_logOutputAfterListener.isDebugEnabled()) {
				_logOutputAfterListener.debug(output);
			}
		}

		if (_logTransfromAfter.isDebugEnabled()) {
			_logTransfromAfter.debug(output);
		}

		return output;
	}

	protected Company getCompany(ThemeDisplay themeDisplay, long companyId)
		throws Exception {

		if (themeDisplay != null) {
			return themeDisplay.getCompany();
		}

		return CompanyLocalServiceUtil.getCompany(companyId);
	}

	protected Device getDevice(ThemeDisplay themeDisplay) {
		if (themeDisplay != null) {
			return themeDisplay.getDevice();
		}

		return UnknownDevice.getInstance();
	}

	protected TemplateResource getErrorTemplateResource(String langType) {
		try {
			Class<?> clazz = getClass();

			ClassLoader classLoader = clazz.getClassLoader();

			String errorTemplateId = _errorTemplateIds.get(langType);

			URL url = classLoader.getResource(errorTemplateId);

			return new URLTemplateResource(errorTemplateId, url);
		}
		catch (Exception e) {
		}

		return null;
	}

	protected Template getTemplate(
			String templateId, Map<String, String> tokens, String languageId,
			String xml, String script, String langType)
		throws Exception {

		TemplateResource templateResource = null;

		if (langType.equals(TemplateConstants.LANG_TYPE_XSL)) {
			XSLURIResolver xslURIResolver = new JournalXSLURIResolver(
				tokens, languageId);

			templateResource = new XSLTemplateResource(
				templateId, script, xslURIResolver, xml);
		}
		else {
			templateResource = new StringTemplateResource(templateId, script);
		}

		TemplateResource errorTemplateResource = getErrorTemplateResource(
			langType);

		TemplateContextType templateContextType = getTemplateContextType(
			langType);

		return TemplateManagerUtil.getTemplate(
			langType, templateResource, errorTemplateResource,
			templateContextType);
	}

	protected Template getTemplate(
			String templateId, String script, String langType)
		throws Exception {

		TemplateResource templateResource = new StringTemplateResource(
			templateId, script);

		TemplateResource errorTemplateResource = getErrorTemplateResource(
			langType);

		TemplateContextType templateContextType = getTemplateContextType(
			langType);

		return TemplateManagerUtil.getTemplate(
			langType, templateResource, errorTemplateResource,
			templateContextType);
	}

	protected TemplateContextType getTemplateContextType(String langType) {
		if (langType.equals(TemplateConstants.LANG_TYPE_XSL)) {
			return TemplateContextType.EMPTY;
		}

		return _defaultTemplateContextType;
	}

	protected String getTemplateId(
		String templateId, long companyId, long companyGroupId, long groupId) {

		StringBundler sb = new StringBundler(5);

		sb.append(companyId);
		sb.append(StringPool.POUND);

		if (companyGroupId > 0) {
			sb.append(companyGroupId);
		}
		else {
			sb.append(groupId);
		}

		sb.append(StringPool.POUND);
		sb.append(templateId);

		return sb.toString();
	}

	protected List<TemplateNode> getTemplateNodes(
			ThemeDisplay themeDisplay, Element element)
		throws Exception {

		List<TemplateNode> templateNodes = new ArrayList<TemplateNode>();

		Map<String, TemplateNode> prototypeTemplateNodes =
			new HashMap<String, TemplateNode>();

		List<Element> dynamicElementElements = element.elements(
			"dynamic-element");

		for (Element dynamicElementElement : dynamicElementElements) {
			Element dynamicContentElement = dynamicElementElement.element(
				"dynamic-content");

			String data = StringPool.BLANK;

			if (dynamicContentElement != null) {
				data = dynamicContentElement.getText();
			}

			String name = dynamicElementElement.attributeValue(
				"name", StringPool.BLANK);

			if (name.length() == 0) {
				throw new TransformException(
					"Element missing \"name\" attribute");
			}

			String type = dynamicElementElement.attributeValue(
				"type", StringPool.BLANK);

			TemplateNode templateNode = new TemplateNode(
				themeDisplay, name, stripCDATA(data), type);

			if (dynamicElementElement.element("dynamic-element") != null) {
				templateNode.appendChildren(
					getTemplateNodes(themeDisplay, dynamicElementElement));
			}
			else if ((dynamicContentElement != null) &&
					 (dynamicContentElement.element("option") != null)) {

				List<Element> optionElements = dynamicContentElement.elements(
					"option");

				for (Element optionElement : optionElements) {
					templateNode.appendOption(
						stripCDATA(optionElement.getText()));
				}
			}

			TemplateNode prototypeTemplateNode = prototypeTemplateNodes.get(
				name);

			if (prototypeTemplateNode == null) {
				prototypeTemplateNode = templateNode;

				prototypeTemplateNodes.put(name, prototypeTemplateNode);

				templateNodes.add(templateNode);
			}

			prototypeTemplateNode.appendSibling(templateNode);
		}

		return templateNodes;
	}

	protected Map<String, Object> insertRequestVariables(Element element) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (element == null) {
			return map;
		}

		for (Element childElement : element.elements()) {
			String name = childElement.getName();

			if (name.equals("attribute")) {
				Element nameElement = childElement.element("name");
				Element valueElement = childElement.element("value");

				map.put(nameElement.getText(), valueElement.getText());
			}
			else if (name.equals("parameter")) {
				Element nameElement = childElement.element("name");

				List<Element> valueElements = childElement.elements("value");

				if (valueElements.size() == 1) {
					Element valueElement = valueElements.get(0);

					map.put(nameElement.getText(), valueElement.getText());
				}
				else {
					List<String> values = new ArrayList<String>();

					for (Element valueElement : valueElements) {
						values.add(valueElement.getText());
					}

					map.put(nameElement.getText(), values);
				}
			}
			else {
				List<Element> elements = childElement.elements();

				if (!elements.isEmpty()) {
					map.put(name, insertRequestVariables(childElement));
				}
				else {
					map.put(name, childElement.getText());
				}
			}
		}

		return map;
	}

	protected boolean mergeTemplate(
			Template template, UnsyncStringWriter unsyncStringWriter)
		throws Exception {

		VelocityTaglib velocityTaglib = (VelocityTaglib)template.get(
			PortletDisplayTemplateConstants.TAGLIB_LIFERAY);

		if (velocityTaglib != null) {
			velocityTaglib.setTemplate(template);
		}

		return template.processTemplate(unsyncStringWriter);
	}

	protected String stripCDATA(String s) {
		if (s.startsWith(StringPool.CDATA_OPEN) &&
			s.endsWith(StringPool.CDATA_CLOSE)) {

			s = s.substring(
				StringPool.CDATA_OPEN.length(),
				s.length() - StringPool.CDATA_CLOSE.length());
		}

		return s;
	}

	private static Log _log = LogFactoryUtil.getLog(Transformer.class);

	private static Log _logOutputAfterListener = LogFactoryUtil.getLog(
		Transformer.class.getName() + ".OutputAfterListener");
	private static Log _logOutputBeforeListener = LogFactoryUtil.getLog(
		Transformer.class.getName() + ".OutputBeforeListener");
	private static Log _logScriptAfterListener = LogFactoryUtil.getLog(
		Transformer.class.getName() + ".ScriptAfterListener");
	private static Log _logScriptBeforeListener = LogFactoryUtil.getLog(
		Transformer.class.getName() + ".ScriptBeforeListener");
	private static Log _logTokens = LogFactoryUtil.getLog(
		Transformer.class.getName() + ".Tokens");
	private static Log _logTransformBefore = LogFactoryUtil.getLog(
		Transformer.class.getName() + ".TransformBefore");
	private static Log _logTransfromAfter = LogFactoryUtil.getLog(
		Transformer.class.getName() + ".TransformAfter");
	private static Log _logXmlAfterListener = LogFactoryUtil.getLog(
		Transformer.class.getName() + ".XmlAfterListener");
	private static Log _logXmlBeforeListener = LogFactoryUtil.getLog(
		Transformer.class.getName() + ".XmlBeforeListener");

	private TemplateContextType _defaultTemplateContextType;
	private Map<String, String> _errorTemplateIds =
		new HashMap<String, String>();
	private Set<String> _transformerListenerClassNames;

}