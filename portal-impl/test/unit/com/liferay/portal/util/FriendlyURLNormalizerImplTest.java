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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Julio Camarero
 */
@RunWith(PowerMockRunner.class)
public class FriendlyURLNormalizerImplTest extends PowerMockito {

	@Test
	public void testSimpleWord() {
		Assert.assertEquals("word", _friendlyURLNormalizerImpl.normalize("word"));
	}

	@Test
	public void testWordWithBlanks() {
		Assert.assertEquals(
			"word-with-blanks",
			_friendlyURLNormalizerImpl.normalize("word with blanks"));
	}

	@Test
	public void testWordWithDoubleBlanks() {
		Assert.assertEquals(
			"word-with-double-blanks",
			_friendlyURLNormalizerImpl.normalize("word with   double  blanks"));
	}

	@Test
	public void testWordWithSpecialCharacters() {
		Assert.assertEquals(
			"word-with-special-characters",
			_friendlyURLNormalizerImpl.normalize("word&: =()with !@special# %+characters"));
	}

	private FriendlyURLNormalizerImpl _friendlyURLNormalizerImpl =
		new FriendlyURLNormalizerImpl();

}