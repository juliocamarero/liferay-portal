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

package com.liferay.portal.kernel.bean;

import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.util.HtmlImpl;

import java.io.Serializable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class AutoEscapeBeanHandlerTest {

	@Before
	public void setUp() throws Exception {
		new HtmlUtil().setHtml(new HtmlImpl());

		_model = new AutoEscapeFooImpl(UNESCAPED_TEXT);
	}

	@Test
	public void testToEscapedModel() {
		AutoEscapeFoo escapedModel = toEscapedModel();

		Assert.assertEquals(ESCAPED_TEXT, escapedModel.getAttribute());
	}

	protected AutoEscapeFoo toEscapedModel() {
		return (AutoEscapeFoo) ProxyUtil.newProxyInstance(
			_classLoader, _escapedModelInterfaces,
			new AutoEscapeBeanHandler(_model));
	}

	private static final String ESCAPED_TEXT = "old mc&#039;donald";

	private static final String UNESCAPED_TEXT = "old mc'donald";

	private static ClassLoader _classLoader =
		AutoEscapeFoo.class.getClassLoader();

	private static Class<?>[] _escapedModelInterfaces =
		new Class[] { AutoEscapeFoo.class };

	private AutoEscapeFoo _model;

	private interface AutoEscapeFoo extends Serializable {

		@AutoEscape
		public String getAttribute();
	}

	private class AutoEscapeFooImpl implements AutoEscapeFoo {

		public AutoEscapeFooImpl(String value) {
			_attribute = value;
		}

		public String getAttribute() {
			return _attribute;
		}

		private String _attribute;
	}

}