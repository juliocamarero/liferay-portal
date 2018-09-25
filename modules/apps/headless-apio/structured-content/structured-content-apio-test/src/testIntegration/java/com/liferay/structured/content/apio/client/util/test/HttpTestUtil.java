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

package com.liferay.structured.content.apio.client.util.test;

import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author Ruben Pulido
 */
public class HttpTestUtil {

	public static String getResponseFromURLWithBasicAuth(String url)
		throws IOException {

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		HttpClient httpClient = httpClientBuilder.build();

		HttpGet request = new HttpGet(url);

		String encodedAuthString = Base64.encode(
			"test@liferay.com:test".getBytes(StandardCharsets.ISO_8859_1));

		String authorizationHeader = "Basic " + encodedAuthString;

		request.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);

		request.setHeader(HttpHeaders.ACCEPT, "application/hal+json");

		HttpResponse httpResponse = httpClient.execute(request);

		HttpEntity httpEntity = httpResponse.getEntity();

		InputStream inputStream = httpEntity.getContent();

		return StringUtil.read(inputStream);
	}

}