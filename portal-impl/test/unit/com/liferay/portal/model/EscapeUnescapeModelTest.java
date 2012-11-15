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

package com.liferay.portal.model;

import com.liferay.portal.kernel.test.TestCase;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.model.impl.UserImpl;
import com.liferay.portal.util.HtmlImpl;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Zsolt Berentey
 */
public class EscapeUnescapeModelTest extends TestCase{

	@Before
	public void setUp(){
		new HtmlUtil().setHtml(new HtmlImpl());

		_user = new UserImpl();
		_user.setScreenName(_UNESCAPED_TEXT);
	}

	@Test
	public void testEscape() {
		_user = _user.toEscapedModel();

		assertEquals(_ESCAPED_TEXT, _user.getScreenName());
	}

	@Test
	public void testUnescape() {
		_user = _user.toEscapedModel();

		_user = _user.toUnescapedModel();

		assertEquals(_UNESCAPED_TEXT, _user.getScreenName());
	}

	private static final String _ESCAPED_TEXT = "Old Mc&#039;Donald";
	private static final String _UNESCAPED_TEXT = "Old Mc'Donald";

	private User _user;

}