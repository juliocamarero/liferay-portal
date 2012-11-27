/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.util;

import javax.servlet.http.Cookie;

/**
 * @author Manuel de la Peña
 */
public class CookieTestUtil {

	public static Cookie createCookie() {
		return createCookie(
			_COOKIE_NAME, _COOKIE_NAME, StringPool.BLANK, StringPool.BLANK, 0,
			StringPool.SLASH, false, 1);
	}

	public static Cookie createCookie(String name) {
		return createCookie(
			name, name, StringPool.BLANK, StringPool.BLANK, 0, StringPool.SLASH,
			false, 1);
	}

	public static Cookie createCookie(
		String name, String value, String comment, String domain, int maxAge,
		String path, boolean secure, int version) {

		Cookie cookie = new Cookie(name, value);

		cookie.setComment(comment);
		cookie.setDomain(domain);
		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		cookie.setSecure(secure);
		cookie.setVersion(version);

		return cookie;
	}

	public static String getCookieName() {
		return _COOKIE_NAME;
	}

	private static final String _COOKIE_NAME = "COOKIE_NAME";

}