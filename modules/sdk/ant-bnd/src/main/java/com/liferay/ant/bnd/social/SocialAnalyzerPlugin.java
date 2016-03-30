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

package com.liferay.ant.bnd.social;

import aQute.bnd.osgi.Analyzer;
import aQute.bnd.osgi.Descriptors.PackageRef;
import aQute.bnd.osgi.Jar;
import aQute.bnd.osgi.Packages;
import aQute.bnd.osgi.Resource;
import aQute.bnd.service.AnalyzerPlugin;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Andrea Di Giorgi
 */
public class SocialAnalyzerPlugin implements AnalyzerPlugin {

	@Override
	public boolean analyzeJar(Analyzer analyzer) throws Exception {
		Jar jar = analyzer.getJar();

		Resource resource = jar.getResource(
			"META-INF/social/liferay-social.xml");

		if (resource == null) {
			return false;
		}

		Packages packages = analyzer.getReferred();

		Document document = readXMLResource(resource);

		NodeList nodeList = document.getElementsByTagName("activity-type");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			String className = getActivityTypeClassName(node.getTextContent());

			if (className == null) {
				continue;
			}

			String packageName = getPackageName(className);

			PackageRef packageRef = analyzer.getPackageRef(packageName);

			packages.put(packageRef);
		}

		return false;
	}

	protected String getActivityTypeClassName(String activityType) {
		if (!activityType.startsWith("${")) {
			return null;
		}

		int index = activityType.lastIndexOf('.');

		if (index == -1) {
			return null;
		}

		return activityType.substring(2, index);
	}

	protected String getPackageName(String className) {
		int index = className.lastIndexOf('.');

		return className.substring(0, index);
	}

	protected Document readXMLResource(Resource resource) throws Exception {
		InputStream inputStream = resource.openInputStream();

		try {
			DocumentBuilder documentBuilder =
				_documentBuilderFactory.newDocumentBuilder();

			return documentBuilder.parse(inputStream);
		}
		finally {
			inputStream.close();
		}
	}

	private static final DocumentBuilderFactory _documentBuilderFactory =
		DocumentBuilderFactory.newInstance();

}