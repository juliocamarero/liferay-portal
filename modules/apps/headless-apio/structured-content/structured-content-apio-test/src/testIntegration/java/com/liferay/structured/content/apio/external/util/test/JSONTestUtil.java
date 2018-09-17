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

package com.liferay.structured.content.apio.external.util.test;

import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

/**
 * @author Ruben Pulido
 */
public class JSONTestUtil {

	public static Optional<JSONObject> getJsonObjectFromJsonArrayOptional(
			JSONObject jsonObject, JSONArray jsonArray)
		throws JSONException {

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject actualJSONObject = (JSONObject)jsonArray.get(i);

			JSONCompareResult jsonCompareResult = JSONCompare.compareJSON(
				jsonObject, actualJSONObject, JSONCompareMode.LENIENT);

			if (!jsonCompareResult.failed()) {
				return Optional.of(actualJSONObject);
			}
		}

		return Optional.empty();
	}

}