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

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.xml.Attribute;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.xml.SAXReaderImpl;

import java.io.InputStream;

import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Manuel de la Peña
 */
@PrepareForTest( {
	LanguageUtil.class, LocaleUtil.class, LocalizationUtil.class,
	SAXReaderUtil.class})
@RunWith(PowerMockRunner.class)
public class LocalizationImplUnitTest extends PowerMockito {

	@After
	public void tearDown() {
		verifyStatic();
	}

	@Test
	public void testFixContentDefaultLocale() {
		spy(SAXReaderUtil.class);

		when(
			SAXReaderUtil.getSAXReader()
		).thenReturn(
			_saxReader
		);

		spy(LocalizationUtil.class);

		when(
			LocalizationUtil.getLocalization()
		).thenReturn(
			_localization
		);

		try {
			String xml = readText(_STRUCTURE_FILE_NAME);

			Document document = SAXReaderUtil.read(xml);

			List<Node> structureNodes = document.selectNodes("//structure");

			for (Node structureNode : structureNodes) {
				String structureXML = structureNode.asXML();

				Document structureDocument = SAXReaderUtil.read(structureXML);

				Element rootElement =
					(Element)structureDocument.selectSingleNode(
						"/structure/root");

				Attribute defaultLocale = rootElement.attribute(
					"default-locale");

				Locale contentDefaultLocale = LocaleUtil.fromLanguageId(
					defaultLocale.getValue());

				Locale availableDefaultLocale = LocaleUtil.fromLanguageId(
					"es_ES");

				String rootXML = rootElement.asXML();

				structureXML = LocalizationUtil.fixContentDefaultLocale(
					rootXML, contentDefaultLocale, availableDefaultLocale);

				StringBundler sb = new StringBundler();

				sb.append("<meta-data locale=\"");
				sb.append(LocaleUtil.toLanguageId(availableDefaultLocale));
				sb.append("\"");

				Assert.assertTrue((structureXML.indexOf(sb.toString()) != -1));
			}
		}
		catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetDefaultImportLocaleUseCase1() {
		verifyDefaultImportLocale("es_ES", "es_ES,en_US,de_DE", "es_ES", true);
	}

	@Test
	public void testGetDefaultImportLocaleUseCase2() {
		verifyDefaultImportLocale("en_US", "bg_BG,en_US,de_DE", "en_US", true);
	}

	@Test
	public void testGetDefaultImportLocaleUseCase3() {
		verifyDefaultImportLocale("bg_BG", "bg_BG,en_US,de_DE", "en_US", true);
	}

	@Test
	public void testGetDefaultImportLocaleUseCase4() {
		verifyDefaultImportLocale("bg_BG", "bg_BG,fr_FR", "bg_BG", true);
	}

	@Test
	public void testGetDefaultImportLocaleWrongUseCase1() {
		verifyDefaultImportLocale("es_ES", "es_ES,en_US,de_DE", "en_US", false);
	}

	protected Locale[] getContentAvailableLocales(String locales) {
		String[] localeIds = StringUtil.split(locales);

		Locale[] array = new Locale[localeIds.length];

		for (int i = 0; i < localeIds.length; i++) {
			array[i] = new Locale(localeIds[i]);
		}

		return array;
	}

	protected String readText(String fileName) throws Exception {
		Class<?> clazz = getClass();

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/" + fileName);

		return StringUtil.read(inputStream);
	}

	protected void verifyDefaultImportLocale(
		String defaultContentLocale, String portalAvailableLocales,
		String expectedLocale, boolean checkEquals) {

		mockStatic(LocaleUtil.class);

		when(
			LocaleUtil.getDefault()
		).thenReturn(
			new Locale(defaultContentLocale)
		);

		mockStatic(LanguageUtil.class);

		Locale[] portalLocales = getContentAvailableLocales(
			portalAvailableLocales);

		when(
			LanguageUtil.getAvailableLocales()
		).thenReturn(
			portalLocales
		);

		spy(LocalizationUtil.class);

		when(
			LocalizationUtil.getLocalization()
		).thenReturn(
			_localization
		);

		Locale defaultImportLocale =
			LocalizationUtil.getDefaultImportLocale(
				_DEFAULT_CLASS_NAME, _DEFAULT_PRIMARY_KEY,
				_contentDefaultLocale, _contentAvailableLocales);

		String actualLocale = defaultImportLocale.toString();

		if (checkEquals) {
			Assert.assertEquals(
				expectedLocale.toLowerCase(), actualLocale.toLowerCase());
		}
		else {
			Assert.assertNotSame(
				expectedLocale.toLowerCase(), actualLocale.toLowerCase());
		}
	}

	private static final String _DEFAULT_CLASS_NAME =
		"com.liferay.portal.className";

	private static final long _DEFAULT_PRIMARY_KEY = 1L;

	private static final String _STRUCTURE_FILE_NAME =
		"dynamic-data-mapping-structures.xml";

	private Locale[] _contentAvailableLocales = getContentAvailableLocales(
		"es_ES,en_US,de_DE");

	private Locale _contentDefaultLocale = LocaleUtil.fromLanguageId("es_ES");

	private LocalizationImpl _localization = new LocalizationImpl();

	private SAXReaderImpl _saxReader = new SAXReaderImpl();

}