/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.parsers.bbcode;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.util.BBCodeTranslator;

/**
 * @author Iliyan Peychev
 * @author Miguel Pastor
 */
public class BBCodeUtil {

	public static String[][] EMOTICONS = null;

	public static String[] EMOTICONS_DESCRIPTIONS = null;

	public static String[] EMOTICONS_FILES = null;

	public static String[] EMOTICONS_SYMBOLS = null;

	public static String NEW_THREAD_URL = "${newThreadURL}";

	public static String getHTML(MBMessage message) {
		String body = message.getBody();

		return getHTML(body);
	}

	public static String getHTML(String bbcode) {
		try {
			bbcode = _bbCodeTranslator.parse(bbcode);
		}
		catch (Exception e) {
			_log.error(
				"Could not parse bbcode: " + bbcode + " " + e.getMessage());

			bbcode = HtmlUtil.escape(bbcode);
		}

		return bbcode;
	}

	public void setBBCodeTranslator(BBCodeTranslator bbCodeTranslator) {
		_bbCodeTranslator = bbCodeTranslator;
	}

	private static BBCodeTranslator _bbCodeTranslator;

	private static Log _log = LogFactoryUtil.getLog(BBCodeTranslator.class);

}