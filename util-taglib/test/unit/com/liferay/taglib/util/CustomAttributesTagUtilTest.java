package com.liferay.taglib.util;

import static org.junit.Assert.*;

import com.liferay.taglib.util.CustomAttributesTagUtil;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

public class CustomAttributesTagUtilTest extends CustomAttributesTagUtil {
	
	private Vector<String> attributes;

	public CustomAttributesTagUtilTest() {
		attributes = new Vector<String>();
		attributes.add("aaa");
		attributes.add("bbb");
		attributes.add("ccc");
	}
	
	public String concat(List<String> list) {
		Collections.sort(list);
		StringBuffer result = new StringBuffer();
		for (String string : list) {
			result.append(string);
			result.append(",");
		}
		if(result.length() > 1) {
			result.setLength(result.length()-1);
		}
		return result.toString();
	}
	
	@Test
	public void testForNoIgnoredValues() {
		assertEquals("aaa,bbb,ccc", concat(getUnignoredAttributes(attributes.elements(), "")));
		assertEquals("aaa,bbb,ccc", concat(getUnignoredAttributes(attributes.elements(), null)));
		assertEquals("aaa,bbb,ccc", concat(getUnignoredAttributes(attributes.elements(), ",")));
		assertEquals("aaa,bbb,ccc", concat(getUnignoredAttributes(attributes.elements(), " ")));
	}

	@Test
	public void testForIgnoringOneAttribute() {
		assertEquals("bbb,ccc", concat(getUnignoredAttributes(attributes.elements(), "aaa")));
		assertEquals("aaa,ccc", concat(getUnignoredAttributes(attributes.elements(), "bbb")));
		assertEquals("aaa,bbb", concat(getUnignoredAttributes(attributes.elements(), "ccc")));
	}
	
	@Test
	public void testForIgnoringSomeAttributes() {
		assertEquals("ccc", concat(getUnignoredAttributes(attributes.elements(), "aaa,bbb")));
		assertEquals("bbb", concat(getUnignoredAttributes(attributes.elements(), "aaa,ccc")));
		assertEquals("aaa", concat(getUnignoredAttributes(attributes.elements(), "bbb,ccc")));
		assertEquals("aaa", concat(getUnignoredAttributes(attributes.elements(), "ccc,bbb")));
		assertEquals("bbb", concat(getUnignoredAttributes(attributes.elements(), "ccc,aaa")));
		assertEquals("aaa,bbb", concat(getUnignoredAttributes(attributes.elements(), "ccc,ccc,ccc")));
	}
	
	@Test
	public void testForIgnoringAllElements() {
		assertEquals("", concat(getUnignoredAttributes(attributes.elements(), "aaa,bbb,ccc")));
		assertEquals("", concat(getUnignoredAttributes(attributes.elements(), "bbb,ccc,aaa")));
		assertEquals("", concat(getUnignoredAttributes(attributes.elements(), "ccc,bbb,aaa")));
	}
	
	@Test
	public void testForIgnoringNonexistingElements() {
		assertEquals("aaa,bbb,ccc", concat(getUnignoredAttributes(attributes.elements(), "some-attribute")));
		assertEquals("aaa,bbb,ccc", concat(getUnignoredAttributes(attributes.elements(), "some-attribute,some-other-attribute,yet-another-attribute")));
		assertEquals("aaa,bbb,ccc", concat(getUnignoredAttributes(attributes.elements(), "attribute-one,attribute-two,nonexisting-attribute")));
	}
	
	@Test
	public void testForNoAttributesAvailable() {
		Vector<String> empty = new Vector<String>();
		assertEquals("", concat(getUnignoredAttributes(empty.elements(), "some-attribute")));
		assertEquals("", concat(getUnignoredAttributes(empty.elements(), "some-attribute,some-other-attribute,yet-another-attribute")));
		assertEquals("", concat(getUnignoredAttributes(empty.elements(), "")));
		assertEquals("", concat(getUnignoredAttributes(empty.elements(), null)));
	}
}
