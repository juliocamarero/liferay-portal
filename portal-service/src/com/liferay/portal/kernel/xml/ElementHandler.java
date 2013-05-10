/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.xml;

import java.util.HashSet;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * @author Zsolt Berentey
 */
public class ElementHandler implements ContentHandler {

	public ElementHandler(
		ElementProcessor elementProcessor, String[] triggers) {

		_elementProcessor = elementProcessor;

		for (String trigger : triggers) {
			_triggers.add(trigger);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
		throws SAXException {

		if (_currentElement != null) {
			_currentElement.setText(new String(ch, start, length));
		}
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void endElement(String uri, String localName, String qName)
		throws SAXException {

		if (_currentElement != null) {
			_elementProcessor.processElement(_currentElement);

			_currentElement = null;
		}
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
		throws SAXException {
	}

	@Override
	public void processingInstruction(String target, String data)
		throws SAXException {
	}

	@Override
	public void setDocumentLocator(Locator locator) {
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void startElement(
			String uri, String localName, String qName, Attributes attributes)
		throws SAXException {

		if (_currentElement != null) {
			_elementProcessor.processElement(_currentElement);

			_currentElement = null;
		}

		if (!_triggers.contains(localName)) {
			return;
		}

		Element element = SAXReaderUtil.createElement(qName);

		for (int i = 0; i < attributes.getLength(); i++) {
			element.addAttribute(
				attributes.getQName(i), attributes.getValue(i));
		}

		_currentElement = element;
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
		throws SAXException {
	}

	private Element _currentElement;
	private ElementProcessor _elementProcessor;
	private Set<String> _triggers = new HashSet<String>();

}