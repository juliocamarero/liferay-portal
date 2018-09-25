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

import com.liferay.structured.content.apio.client.util.test.HttpTestUtil;
import com.liferay.structured.content.apio.client.util.test.JSONTestUtil;

import java.io.IOException;

import java.net.URL;

import java.util.Optional;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Ruben Pulido
 */
@RunAsClient
@RunWith(Arquillian.class)
public class StructuredContentApioTest {

	@Test
	public void testContentSpaceExistsInRootEndpoint() throws Exception {
		String contentSpacesHref = _getContentSpacesHref();

		Assert.assertNotNull(
			"Content space URL should not be empty in root end point",
			contentSpacesHref);
	}

	@Test
	public void testLiferayContentSpaceStructuredContentsContainsSelfLink()
		throws Exception {

		String contentSpacesHref = _getContentSpacesHref();

		String structuredContentsHref = _getStructuredContentsHref(
			contentSpacesHref, "Liferay");

		String structuredContentsResponse =
			HttpTestUtil.getResponseFromURLWithBasicAuth(
				structuredContentsHref);

		Assert.assertNotNull(
			"Structured contents response should not be empty",
			structuredContentsResponse);

		JSONObject responseJSONObject = new JSONObject(
			structuredContentsResponse);

		String href = _getHref(responseJSONObject, "self");

		Assert.assertNotNull(
			"Structured contents self link href should not be empty", href);
	}

	@Test
	public void testStructuredContentsExistsInContentSpaceEndpoint()
		throws Exception {

		String contentSpacesHref = _getContentSpacesHref();

		String contentSpaceName = "Liferay";

		String structuredContentsHref = _getStructuredContentsHref(
			contentSpacesHref, contentSpaceName);

		Assert.assertNotNull(
			"Structured contents href should not be empty in content space " +
				"with name " + contentSpaceName,
			structuredContentsHref);
	}

	private String _getContentSpacesHref() throws Exception {
		URL apiURL = new URL(_url, "/o/api");

		String apiResponse = HttpTestUtil.getResponseFromURLWithBasicAuth(
			apiURL.toString());

		JSONObject responseJSONObject = new JSONObject(apiResponse);

		return _getHref(responseJSONObject, "content-space");
	}

	private String _getHref(JSONObject jsonObject, String linkName)
		throws JSONException {

		JSONObject linksJSONObject = (JSONObject)jsonObject.get("_links");

		JSONObject hrefJSONObject = (JSONObject)linksJSONObject.get(linkName);

		return (String)hrefJSONObject.get("href");
	}

	private String _getStructuredContentsHref(
			String contentSpaceHref, String contentSpaceName)
		throws IOException, JSONException {

		String contentSpaceResponse =
			HttpTestUtil.getResponseFromURLWithBasicAuth(contentSpaceHref);

		JSONObject contentSpaceResponseJSONObject = new JSONObject(
			contentSpaceResponse);

		JSONObject embeddedJSONObject =
			(JSONObject)contentSpaceResponseJSONObject.get("_embedded");

		JSONArray jsonArray = (JSONArray)embeddedJSONObject.get("ContentSpace");

		Assert.assertTrue(
			"JSON array length should be greater than 0",
			jsonArray.length() > 0);

		JSONObject expectedContentSpaceJSONObject = new JSONObject();

		expectedContentSpaceJSONObject.put("name", contentSpaceName);

		Optional<JSONObject> contentSpaceJSONObjectOptional =
			JSONTestUtil.getJsonObjectFromJsonArrayOptional(
				expectedContentSpaceJSONObject, jsonArray);

		JSONObject contentSpaceJSONObject =
			contentSpaceJSONObjectOptional.get();

		return _getHref(contentSpaceJSONObject, "structuredContents");
	}

	@ArquillianResource
	private URL _url;

}