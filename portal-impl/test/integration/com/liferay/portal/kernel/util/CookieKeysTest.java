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

import com.liferay.portal.CookieNotSupportedException;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;

import javax.servlet.http.Cookie;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Manuel de la Peña
 */
@ExecutionTestListeners(listeners = {MainServletExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class CookieKeysTest {

	@Before
	public void setUp() {
		_request = new MockHttpServletRequest();

		_request.setCookies(new Cookie[] {});

		_response = new MockHttpServletResponse();
	}

	@Test
	public void testAddCookie() {
		String value = CookieKeys.getCookie(
			_request, CookieTestUtil.getCookieName());

		Assert.assertNull(value);

		int initialCookieCount = _request.getCookies().length;

		Cookie cookie = CookieTestUtil.createCookie();

		CookieKeys.addCookie(_request, _response, cookie);

		_request.setCookies(cookie);

		Assert.assertEquals(
			initialCookieCount + 1, _request.getCookies().length);
	}

	@Test
	public void testCookieNotSupported() {
		try {
			CookieKeys.validateSupportCookie(_request);

			Assert.fail("Cookies are supported");
		}
		catch (CookieNotSupportedException e) {}
	}

	@Test
	public void testGetCookie() {
		Cookie cookie = CookieTestUtil.createCookie();

		CookieKeys.addCookie(_request, _response, cookie);

		_request.setCookies(cookie);

		String value = CookieKeys.getCookie(
			_request, CookieTestUtil.getCookieName());

		Assert.assertEquals(CookieTestUtil.getCookieName(), value);
	}

	@Test
	public void testGetCookieFromEmptyCookies() {
		String value = CookieKeys.getCookie(
			_request, CookieTestUtil.getCookieName());

		Assert.assertNull(value);
	}

	@Test
	public void testValidateSupportCookie() throws CookieNotSupportedException {
		Cookie cookie = CookieTestUtil.createCookie(CookieKeys.COOKIE_SUPPORT);

		_request.setCookies(cookie);

		CookieKeys.validateSupportCookie(_request);
	}

	private MockHttpServletRequest _request;

	private MockHttpServletResponse _response;

}