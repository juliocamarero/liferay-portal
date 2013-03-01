package com.liferay.taglib.util;

import com.liferay.portal.kernel.util.StringUtil;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class CustomAttributesTagUtil {
	// Note for the reviewer(s):
	// This method&class is extracted to be testable and reusable from jsp.
	// The interface (using Enumeration) is due to the form that Expando API
	// returns the attributes. This is designed for a change that is as 
	// unintrusive as possible.
	// Feel free to remove this comment after reviewing
	public static List<String> getUnignoredAttributes(Enumeration<String> attributes,
			String ignore) {
		String[] ignored = StringUtil.split(ignore);
		List<String> ignoredList = Arrays.asList(ignored);
		LinkedList<String>result = new LinkedList<String>();
		
		while(attributes.hasMoreElements()) {
			String currentAttribute = attributes.nextElement();
			if(ignoredList.contains(currentAttribute)) {
				continue;
			} else {
				result.add(currentAttribute);
			}
		}
		return result;
	}
}
