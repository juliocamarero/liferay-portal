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

/**
 * @author André de Oliveira
 */
public interface PingbackExcerptExtractor {

	public String getExcerpt() throws UnavailableSourceURIException;

	public void setSourceURI(String sourceURI);

	public void setTargetURI(String targetURI);

	public void validateSource()
		throws InvalidSourceURIException, UnavailableSourceURIException;

	public static class InvalidSourceURIException extends RuntimeException {

		public InvalidSourceURIException() {
			super("Could not find target URI in source");
		}
	}

	public static class UnavailableSourceURIException extends RuntimeException {

		public UnavailableSourceURIException(Throwable cause) {
			super("Error accessing source URI", cause);
		}
	}

}