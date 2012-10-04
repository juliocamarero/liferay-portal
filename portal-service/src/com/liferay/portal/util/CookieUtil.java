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

package com.liferay.portal.util;

import com.liferay.portal.CookieNotSupportedException;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Brian Wing Shun Chan
 * @author Minhchau Dang
 * @author Juan Fernández
 */
public class CookieUtil {

	public static final String COMPANY_ID = "COMPANY_ID";

	public static final String COOKIE_SUPPORT = "COOKIE_SUPPORT";

	public static final String GUEST_LANGUAGE_ID = "GUEST_LANGUAGE_ID";

	public static final String ID = "ID";

	public static final String JSESSIONID = "JSESSIONID";

	public static final String LOGIN = "LOGIN";

	public static final int MAX_AGE = 31536000;

	public static final String PASSWORD = "PASSWORD";

	public static final String REMEMBER_ME = "REMEMBER_ME";

	public static final String SCREEN_NAME = "SCREEN_NAME";

	public static final String USER_UUID = "USER_UUID";

	public static final int VERSION = 0;

	public static void addCookie(
		HttpServletRequest request, HttpServletResponse response,
		javax.servlet.http.Cookie cookie) {

		getCookie().addCookie(request, response, cookie);
	}

	public static void addCookie(
		HttpServletRequest request, HttpServletResponse response,
		javax.servlet.http.Cookie cookie, boolean secure) {

		getCookie().addCookie(request, response, cookie, secure);
	}

	public static void addSupportCookie(
		HttpServletRequest request, HttpServletResponse response) {

		getCookie().addSupportCookie(request, response);
	}

	public static String get(HttpServletRequest request, String name) {
		return getCookie().get(request, name);
	}

	public static String get(
		HttpServletRequest request, String name, boolean toUpperCase) {
		return getCookie().get(request, name, toUpperCase);
	}

	public static Cookie getCookie() {
		PortalRuntimePermission.checkGetBeanProperty(CookieUtil.class);

		return _cookie;
	}

	public static String getCookie(HttpServletRequest request, String name) {
		return getCookie().getCookie(request, name);
	}

	public static String getCookie(
		HttpServletRequest request, String name, boolean toUpperCase) {

		return getCookie().getCookie(request, name, toUpperCase);
	}

	public static String getDomain(HttpServletRequest request) {
		return getCookie().getDomain(request);
	}

	public static String getDomain(String host) {
		return getCookie().getDomain(host);
	}

	public static boolean hasSessionId(HttpServletRequest request) {
		return getCookie().hasSessionId(request);
	}

	public static boolean isEncodedCookie(String name) {
		return getCookie().isEncodedCookie(name);
	}

	public static void validateSupportCookie(HttpServletRequest request)
		throws CookieNotSupportedException {

		getCookie().validateSupportCookie(request);
	}

	public void setCookie(Cookie cookie) {
		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_cookie = cookie;
	}

	private static Cookie _cookie;

}