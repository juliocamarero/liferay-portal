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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Brian Wing Shun Chan
 * @author Minhchau Dang
 * @author Juan Fernández
 */
public interface Cookie {

	public void addCookie(
		HttpServletRequest request, HttpServletResponse response,
		javax.servlet.http.Cookie cookie);

	public void addCookie(
		HttpServletRequest request, HttpServletResponse response,
		javax.servlet.http.Cookie cookie, boolean secure);

	public void addSupportCookie(
		HttpServletRequest request, HttpServletResponse response);

	public String get(HttpServletRequest request, String name);

	public String get(
		HttpServletRequest request, String name, boolean toUpperCase);

	public String getCookie(HttpServletRequest request, String name);

	public String getCookie(
		HttpServletRequest request, String name, boolean toUpperCase);

	public String getDomain(HttpServletRequest request);

	public String getDomain(String host);

	public boolean hasSessionId(HttpServletRequest request);

	public boolean isEncodedCookie(String name);

	public void validateSupportCookie(HttpServletRequest request)
		throws CookieNotSupportedException;

}