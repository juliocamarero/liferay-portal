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

package com.liferay.structured.content.apio.client.test;

import com.jayway.jsonpath.JsonPath;

import com.liferay.petra.json.web.service.client.JSONWebServiceClient;
import com.liferay.petra.json.web.service.client.internal.JSONWebServiceClientImpl;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.List;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Ruben Pulido
 */
@RunAsClient
@RunWith(Arquillian.class)
public class StructuredContentApioTest {

	@Before
	public void setUp() throws MalformedURLException {
		_rootEndpointURL = new URL(_url, "/o/api");

		_jsonWebServiceClient = new JSONWebServiceClientImpl();

		_jsonWebServiceClient.setHostName(_rootEndpointURL.getHost());
		_jsonWebServiceClient.setHostPort(_rootEndpointURL.getPort());
		_jsonWebServiceClient.setProtocol(_rootEndpointURL.getProtocol());

		_jsonWebServiceClient.setPassword("test");
		_jsonWebServiceClient.setLogin("test@liferay.com");
	}

	@Test
	public void testContentSpaceLinkExistsInRootEndpoint() throws Exception {
		String response = _jsonWebServiceClient.doGet(
			_rootEndpointURL.toExternalForm());

		Assert.assertNotNull(
			"Content space link should not be empty in root end point",
			JsonPath.read(response, "$._links.content-space.href"));
	}

	@Test
	public void testStructuredContentsExistsInContentSpaceEndpoint()
		throws Exception {

		String response = _jsonWebServiceClient.doGet(
			_rootEndpointURL.toExternalForm());

		String contentSpacesHref = JsonPath.read(
			response, "$._links.content-space.href");

		String contentSpacesResponse = _jsonWebServiceClient.doGet(
			contentSpacesHref);

		List<String> liferayStructuredContentsHrefs = JsonPath.read(
			contentSpacesResponse,
			"$._embedded.ContentSpace[?(@.name == 'Liferay')]._links." +
				"structuredContents.href");

		Assert.assertNotNull(
			"Structured contents link should not be empty in Liferay site",
			liferayStructuredContentsHrefs.get(0));
	}

	@Test
	public void testStructuredContentsMatchesSelfLink() throws Exception {
		String response = _jsonWebServiceClient.doGet(
			_rootEndpointURL.toExternalForm());

		String contentSpacesHref = JsonPath.read(
			response, "$._links.content-space.href");

		String contentSpacesResponse = _jsonWebServiceClient.doGet(
			contentSpacesHref);

		List<String> liferayStructuredContentsHrefs = JsonPath.read(
			contentSpacesResponse,
			"$._embedded.ContentSpace[?(@.name == 'Liferay')]._links." +
				"structuredContents.href");

		String liferayStructuredContentsResponse = _jsonWebServiceClient.doGet(
			liferayStructuredContentsHrefs.get(0));

		String liferayStructuredContentsSelfHref = JsonPath.read(
			liferayStructuredContentsResponse, "$._links.self.href");

		Assert.assertTrue(
			liferayStructuredContentsSelfHref.startsWith(
				liferayStructuredContentsHrefs.get(0)));
	}

	private JSONWebServiceClient _jsonWebServiceClient;
	private URL _rootEndpointURL;

	@ArquillianResource
	private URL _url;

}