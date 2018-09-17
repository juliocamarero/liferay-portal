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

package com.liferay.structured.content.apio.external.test;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.structured.content.apio.external.util.test.HttpTestUtil;
import com.liferay.structured.content.apio.external.util.test.JSONTestUtil;

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

import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 * @author Ruben Pulido
 */
@RunAsClient
@RunWith(Arquillian.class)
public class StructuredContentApioTest {

	@Test
	public void testContentSpaceExistsInRootEndpoint() throws Exception {
		String contentSpaceURLString = _getManagesContentSpacesURL();

		Assert.assertNotNull(
			"Content space URL should not be empty in root end point",
			contentSpaceURLString);
	}

	@Test
	public void testStructuredContentsEndpointIsRegistered() throws Exception {
		String contentSpaceURLString = _getManagesContentSpacesURL();

		String structuredContentsUrlString = _getManagesStructuredContentURL(
			contentSpaceURLString, "Liferay");

		String structuredContentsResponse =
			HttpTestUtil.getResponseFromURLWithBasicAuth(
				structuredContentsUrlString);

		Assert.assertNotNull(
			"Structured contents Response should not be empty",
			structuredContentsResponse);

		JSONObject structuredContentsResponseJSONObject = new JSONObject(
			structuredContentsResponse);

		Assert.assertNotNull(
			"Structured contents Response id should not be empty",
			structuredContentsResponseJSONObject.get("@id"));

		JSONObject managesStructuredContentJSONObject = _getManagesJSONObject(
			"StructuredContent");

		JSONObject manages =
			(JSONObject)structuredContentsResponseJSONObject.get("manages");

		JSONAssert.assertEquals(
			managesStructuredContentJSONObject, manages,
			JSONCompareMode.LENIENT);
	}

	@Test
	public void testStructuredContentsExistsInContentSpaceEndpoint()
		throws Exception {

		String contentSpaceURLString = _getManagesContentSpacesURL();

		String contentSpaceName = "Liferay";

		String structuredContentsUrlString = _getManagesStructuredContentURL(
			contentSpaceURLString, contentSpaceName);

		Assert.assertNotNull(
			"Structured contents URL should not be empty in content space " +
				"with name " + contentSpaceName,
			structuredContentsUrlString);
	}

	private String _getManagesContentSpacesURL() throws Exception {
		URL apiURL = new URL(_url, "/o/api");

		JSONObject contentSpaceExpectedJSONObject = new JSONObject();

		JSONObject managesJSONObject = _getManagesJSONObject("ContentSpace");

		contentSpaceExpectedJSONObject.put("manages", managesJSONObject);

		contentSpaceExpectedJSONObject.put(
			"@type", new JSONArray("[Collection]"));

		String apiResponseString = StringUtil.read(apiURL.openStream());

		JSONObject responseJSONObject = new JSONObject(apiResponseString);

		JSONArray collectionJSONArray = (JSONArray)responseJSONObject.get(
			"collection");

		Optional<JSONObject> jsonObjectOptional =
			JSONTestUtil.getJsonObjectFromJsonArrayOptional(
				contentSpaceExpectedJSONObject, collectionJSONArray);

		if (!jsonObjectOptional.isPresent()) {
			throw new Exception(
				"Expected JSON object " + contentSpaceExpectedJSONObject +
					" not found in response " + apiResponseString);
		}

		JSONObject jsonObject = jsonObjectOptional.get();

		return (String)jsonObject.get("@id");
	}

	private JSONObject _getManagesJSONObject(String s) throws JSONException {
		JSONObject managesJSONObject = new JSONObject();

		managesJSONObject.put("object", "schema:" + s);
		managesJSONObject.put("property", "rdf:type");

		return managesJSONObject;
	}

	private String _getManagesStructuredContentURL(
			String contentSpaceURLString, String contentSpaceName)
		throws IOException, JSONException {

		String contentSpaceResponse =
			HttpTestUtil.getResponseFromURLWithBasicAuth(contentSpaceURLString);

		JSONObject contentSpaceResponseJSONObject = new JSONObject(
			contentSpaceResponse);

		JSONArray memberJSONArray =
			(JSONArray)contentSpaceResponseJSONObject.get("member");

		Assert.assertTrue(
			"Member JSON array length should be greater than 0",
			memberJSONArray.length() > 0);

		JSONObject jsonObject1 = new JSONObject();

		jsonObject1.put("name", contentSpaceName);

		Optional<JSONObject> liferayContentSpaceJSONObjectOptional =
			JSONTestUtil.getJsonObjectFromJsonArrayOptional(
				jsonObject1, memberJSONArray);

		JSONObject liferayContentSpaceJSONObject =
			liferayContentSpaceJSONObjectOptional.get();

		String structuredContentsUrlString =
			liferayContentSpaceJSONObject.getString("structuredContents");

		Assert.assertNotNull(
			"Structured contents URL should be present",
			structuredContentsUrlString);

		return structuredContentsUrlString;
	}

	@ArquillianResource
	private URL _url;

}