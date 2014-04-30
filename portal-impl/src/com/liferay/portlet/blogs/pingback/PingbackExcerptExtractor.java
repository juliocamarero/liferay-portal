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

package com.liferay.portlet.blogs.pingback;

import java.io.IOException;

/**
 * @author André de Oliveira
 */
public interface PingbackExcerptExtractor {

	public abstract String getExcerpt() throws IOException;

	public abstract void setSourceUri(String sourceUri);

	public abstract void setTargetUri(String targetUri);

	public abstract void validateSource()
		throws InvalidSourceURIException, UnavailableSourceURIException;

	public static class InvalidSourceURIException extends RuntimeException {

		public InvalidSourceURIException() {
			super("Could not find target URI in source");
		}
	}

	public static class UnavailableSourceURIException extends RuntimeException {

		public UnavailableSourceURIException() {
			super("Error accessing source URI");
		}
	}

}