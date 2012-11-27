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

package com.liferay.util;

import com.liferay.portal.kernel.util.CookieTestUtil;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Manuel de la Peña
 * @deprecated {@link com.liferay.portal.kernel.util.CookieKeysTest}
 */
public class CookieUtilTest {

	@Test
	public void testGetCookie() {
		MockHttpServletRequest request = new MockHttpServletRequest();

		request.setCookies(CookieTestUtil.createCookie());

		String value = CookieUtil.get(request, CookieTestUtil.getCookieName());

		Assert.assertEquals(CookieTestUtil.getCookieName(), value);
	}

	@Test
	public void testGetCookieFromEmptyCookies() {

		MockHttpServletRequest request = new MockHttpServletRequest();

		String value = CookieUtil.get(request, CookieTestUtil.getCookieName());

		Assert.assertNull(value);
	}

}