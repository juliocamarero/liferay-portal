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

package unit.com.liferay.taglib.util;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.taglib.util.CustomAttributesTagUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Olaf Kock
 */
public class CustomAttributesTagUtilTest extends CustomAttributesTagUtil {

	@Before
	public void setup() {
		_attributes = new ArrayList<String>();

		_attributes.add("aaa");
		_attributes.add("bbb");
		_attributes.add("ccc");
	}

	@Test
	public void testIgnoreAllAttributes() {
		Assert.assertEquals(
			StringPool.BLANK,
			StringUtil.merge(filterAttributes(_attributes, "aaa,bbb,ccc")));
		Assert.assertEquals(
			StringPool.BLANK,
			StringUtil.merge(filterAttributes(_attributes, "bbb,ccc,aaa")));
		Assert.assertEquals(
			StringPool.BLANK,
			StringUtil.merge(filterAttributes(_attributes, "ccc,bbb,aaa")));
	}

	@Test
	public void testIgnoreAttributes() {
		Assert.assertEquals(
			"ccc", StringUtil.merge(filterAttributes(_attributes, "aaa,bbb")));
		Assert.assertEquals(
			"bbb", StringUtil.merge(filterAttributes(_attributes, "aaa,ccc")));
		Assert.assertEquals(
			"aaa", StringUtil.merge(filterAttributes(_attributes, "bbb,ccc")));
		Assert.assertEquals(
			"aaa", StringUtil.merge(filterAttributes(_attributes, "ccc,bbb")));
		Assert.assertEquals(
			"bbb", StringUtil.merge(filterAttributes(_attributes, "ccc,aaa")));
		Assert.assertEquals(
			"aaa,bbb",
			StringUtil.merge(filterAttributes(_attributes, "ccc,ccc,ccc")));
	}

	@Test
	public void testIgnoreNoAttributes() {
		Assert.assertEquals(
			"aaa,bbb,ccc",
			StringUtil.merge(filterAttributes(_attributes, StringPool.BLANK)));
		Assert.assertEquals(
			"aaa,bbb,ccc",
			StringUtil.merge(filterAttributes(_attributes, null)));
		Assert.assertEquals(
			"aaa,bbb,ccc",
			StringUtil.merge(filterAttributes(_attributes, StringPool.BLANK)));
		Assert.assertEquals(
			"aaa,bbb,ccc",
			StringUtil.merge(filterAttributes(_attributes, StringPool.SPACE)));
	}

	@Test
	public void testIgnoreNonexistingAttributes() {
		Assert.assertEquals(
			"aaa,bbb,ccc",
			StringUtil.merge(filterAttributes(_attributes, "some-attribute")));
		Assert.assertEquals(
			"aaa,bbb,ccc",
			StringUtil.merge(
				filterAttributes(
					_attributes,
					"some-attribute,some-other-attribute,another-attribute")));
		Assert.assertEquals(
			"aaa,bbb,ccc",
			StringUtil.merge(
				filterAttributes(
					_attributes,
					"attribute-one,attribute-two,nonexisting-attribute")));
	}

	@Test
	public void testIgnoreOneAttribute() {
		Assert.assertEquals(
			"bbb,ccc", StringUtil.merge(filterAttributes(_attributes, "aaa")));
		Assert.assertEquals(
			"aaa,ccc", StringUtil.merge(filterAttributes(_attributes, "bbb")));
		Assert.assertEquals(
			"aaa,bbb", StringUtil.merge(filterAttributes(_attributes, "ccc")));
	}

	@Test
	public void testNoAttributesAvailable() {
		Assert.assertEquals(
			StringPool.BLANK,
			StringUtil.merge(
				filterAttributes(
					Collections.<String>emptyList(), "some-attribute")));
		Assert.assertEquals(
			StringPool.BLANK,
			StringUtil.merge(
				filterAttributes(
					Collections.<String>emptyList(),
					"some-attribute,some-other-attribute,another-attribute")));
		Assert.assertEquals(
			StringPool.BLANK,
			StringUtil.merge(
				filterAttributes(
					Collections.<String>emptyList(), StringPool.BLANK)));
		Assert.assertEquals(
			StringPool.BLANK,
			StringUtil.merge(
				filterAttributes(Collections.<String>emptyList(), null)));
	}

	private List<String> _attributes;

}