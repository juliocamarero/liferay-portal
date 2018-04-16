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

package com.liferay.portal.osgi.web.wab.reference.support.internal;

import aQute.bnd.osgi.Constants;
import aQute.bnd.osgi.Jar;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.UnsecureSAXReaderUtil;
import com.liferay.portal.kernel.xml.XPath;
import com.liferay.portal.osgi.web.wab.generator.WabGenerator;

import java.io.File;
import java.io.IOException;

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.url.AbstractURLStreamHandlerService;
import org.osgi.service.url.URLConstants;
import org.osgi.service.url.URLStreamHandlerService;

/**
 * @author Gregory Amerson
 */
@Component(
	immediate = true,
	property = URLConstants.URL_HANDLER_PROTOCOL + "=webbundledir",
	service = URLStreamHandlerService.class
)
public class WabDirURLStreamHandlerService
	extends AbstractURLStreamHandlerService {

	@Override
	public URLConnection openConnection(URL url) {
		try {
			Map<String, String[]> parameters = new HashMap<>();

			URI uri = new URI(url.getPath());

			File warDir = new File(uri);

			String bundleSymbolicName = _http.getParameter(
				url.toExternalForm(), Constants.BUNDLE_SYMBOLICNAME);

			if (bundleSymbolicName.equals(StringPool.BLANK)) {
				bundleSymbolicName = _getNameFromDirectory(warDir);
			}

			if (bundleSymbolicName.equals(StringPool.BLANK)) {
				bundleSymbolicName = _getNameFromXMLFile(warDir);
			}

			parameters.put(
				Constants.BUNDLE_SYMBOLICNAME,
				new String[] {bundleSymbolicName});

			String contextName = _http.getParameter(
				url.toExternalForm(), "Web-ContextPath");

			if (contextName.equals(StringPool.BLANK)) {
				contextName = _getNameFromDirectory(warDir);
			}

			if (contextName.equals(StringPool.BLANK)) {
				contextName = _getNameFromXMLFile(warDir);
			}

			if (contextName.equals(StringPool.BLANK)) {
				throw new IllegalArgumentException(
					"Unable to determine context name from " + url);
			}

			if (!contextName.startsWith(StringPool.SLASH)) {
				contextName = StringPool.SLASH.concat(contextName);
			}

			parameters.put("Web-ContextPath", new String[] {contextName});

			File generatedJarFile = _wabGenerator.generate(
				_classLoader, warDir, parameters);

			try (Jar generatedJar = new Jar(generatedJarFile)) {
				generatedJar.expand(warDir);
			}

			uri = warDir.toURI();

			WabDirHandler wabDirHandler = new WabDirHandler(
				uri.toASCIIString());

			return wabDirHandler.openConnection(url);
		}
		catch (Exception e) {
			_log.error("Unable to open connection", e);
		}

		return null;
	}

	@Reference(unbind = "-")
	public void setWabGenerator(WabGenerator wabGenerator) {
		_wabGenerator = wabGenerator;
	}

	@Activate
	public void start(BundleContext bundleContext) {
		Bundle bundle = bundleContext.getBundle(0);

		Class<?> clazz = bundle.getClass();

		_classLoader = clazz.getClassLoader();
	}

	private String _getNameFromDirectory(File warDir) {
		Matcher matcher = _pattern.matcher(warDir.getAbsolutePath());

		if (matcher.matches()) {
			return matcher.group(1);
		}

		return StringPool.BLANK;
	}

	private String _getNameFromXMLFile(File warDir) throws IOException {
		File lookAndFeelXmlFile = new File(
			warDir, "WEB-INF/liferay-look-and-feel.xml");

		Document document = _readDocument(lookAndFeelXmlFile);

		Element rootElement = document.getRootElement();

		XPath xPath = SAXReaderUtil.createXPath("//theme/@id", null, null);

		List<Node> nodes = xPath.selectNodes(rootElement);

		String themeId = null;

		if ((nodes != null) && !nodes.isEmpty()) {
			Node themeNode = nodes.get(0);

			themeId = themeNode.getText();
		}

		if (themeId != null) {
			return themeId + "-theme";
		}

		return StringPool.BLANK;
	}

	private Document _readDocument(File file) throws IOException {
		String content = FileUtil.read(file);

		try {
			return UnsecureSAXReaderUtil.read(content);
		}
		catch (DocumentException de) {
			throw new IOException(de);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		WabDirURLStreamHandlerService.class);

	private static final Pattern _pattern = Pattern.compile(
		".*\\/(.*-(T|t)heme)\\/?.*");

	private ClassLoader _classLoader;

	@Reference
	private Http _http;

	private WabGenerator _wabGenerator;

}