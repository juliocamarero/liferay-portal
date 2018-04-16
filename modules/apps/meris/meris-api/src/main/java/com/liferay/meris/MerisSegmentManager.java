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

package com.liferay.meris;

import aQute.bnd.annotation.ProviderType;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author Eduardo Garcia
 */
@ProviderType
public interface MerisSegmentManager
	<S extends MerisSegment, P extends MerisProfile> {

	public S addMerisSegment(S segment);

	public S deleteMerisSegment(String merisSegmentId);

	public void deleteMerisSegments(String scopeId);

	public List<P> getMerisProfiles(
		String merisSegmentId, Map<String, Object> context, int start, int end,
		Comparator<P> comparator);

	public S getMerisSegment(String merisSegmentId);

	public List<S> getMerisSegments(
		String scopeId, int start, int end, Comparator<S> comparator);

	public List<S> getMerisSegments(
		String scopeId, String merisProfileId, String merisSegmentId,
		Map<String, Object> context, int start, int end,
		Comparator<S> comparator);

	public boolean matches(
		String merisProfileId, String merisSegmentId,
		Map<String, Object> context);

	public S updateMerisSegment(S segment);

}