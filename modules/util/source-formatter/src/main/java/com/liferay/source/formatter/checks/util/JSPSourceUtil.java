/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.source.formatter.checks.util;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.source.formatter.util.FileUtil;

import java.io.File;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hugo Huijser
 */
public class JSPSourceUtil {

	public static List<String> addIncludedAndReferencedFileNames(
		List<String> fileNames, Set<String> checkedFileNames,
		Map<String, String> contentsMap) {

		Set<String> includedAndReferencedFileNames = new HashSet<>();

		for (String fileName : fileNames) {
			if (!checkedFileNames.add(fileName)) {
				continue;
			}

			fileName = StringUtil.replace(
				fileName, CharPool.BACK_SLASH, CharPool.SLASH);

			includedAndReferencedFileNames.addAll(
				getJSPIncludeFileNames(fileName, fileNames, contentsMap));
			includedAndReferencedFileNames.addAll(
				getJSPReferenceFileNames(fileName, fileNames, contentsMap));
		}

		if (includedAndReferencedFileNames.isEmpty()) {
			return fileNames;
		}

		for (String fileName : includedAndReferencedFileNames) {
			fileName = StringUtil.replace(
				fileName, CharPool.SLASH, CharPool.BACK_SLASH);

			if (!fileNames.contains(fileName)) {
				fileNames.add(fileName);
			}
		}

		return addIncludedAndReferencedFileNames(
			fileNames, checkedFileNames, contentsMap);
	}

	public static String buildFullPathIncludeFileName(
		String fileName, String includeFileName,
		Map<String, String> contentsMap) {

		String path = fileName;

		while (true) {
			int y = path.lastIndexOf(CharPool.SLASH);

			if (y == -1) {
				return StringPool.BLANK;
			}

			String fullPathIncludeFileName =
				path.substring(0, y) + includeFileName;

			if (contentsMap.containsKey(fullPathIncludeFileName) &&
				!fullPathIncludeFileName.equals(fileName)) {

				return fullPathIncludeFileName;
			}

			path = path.substring(0, y);
		}
	}

	public static String compressImportsOrTaglibs(
		String fileName, String content, String attributePrefix) {

		int x = content.indexOf(attributePrefix);

		int y = content.lastIndexOf(attributePrefix);

		y = content.indexOf("%>", y);

		if ((x == -1) || (y == -1) || (x > y)) {
			return content;
		}

		String importsOrTaglibs = content.substring(x, y);

		importsOrTaglibs = StringUtil.replace(
			importsOrTaglibs, new String[] {"%>\r\n<%@ ", "%>\n<%@ "},
			new String[] {"%><%@\r\n", "%><%@\n"});

		return content.substring(0, x) + importsOrTaglibs +
			content.substring(y);
	}

	public static Map<String, String> getContentsMap(List<String> fileNames)
		throws Exception {

		Map<String, String> contentsMap = new ConcurrentHashMap<>();

		if (ListUtil.isEmpty(fileNames)) {
			return contentsMap;
		}

		for (String fileName : fileNames) {
			fileName = StringUtil.replace(
				fileName, CharPool.BACK_SLASH, CharPool.SLASH);

			File file = new File(fileName);

			String content = FileUtil.read(file);

			if (content == null) {
				continue;
			}

			Matcher matcher = _includeFilePattern.matcher(content);

			while (matcher.find()) {
				content = StringUtil.replaceFirst(
					content, matcher.group(),
					"@ include file=\"" + matcher.group(1) + "\"",
					matcher.start());
			}

			contentsMap.put(fileName, content);
		}

		return contentsMap;
	}

	public static Set<String> getJSPIncludeFileNames(
		String fileName, Collection<String> fileNames,
		Map<String, String> contentsMap) {

		Set<String> includeFileNames = new HashSet<>();

		String content = contentsMap.get(fileName);

		if (Validator.isNull(content)) {
			return includeFileNames;
		}

		for (int x = -1;;) {
			x = content.indexOf("<%@ include file=", x + 1);

			if (x == -1) {
				break;
			}

			Matcher matcher = _javaCodeTagPattern.matcher(content);

			if (matcher.find() && (matcher.start() == x)) {
				continue;
			}

			x = content.indexOf(CharPool.QUOTE, x);

			if (x == -1) {
				break;
			}

			int y = content.indexOf(CharPool.QUOTE, x + 1);

			if (y == -1) {
				break;
			}

			String includeFileName = content.substring(x + 1, y);

			if (!includeFileName.startsWith(StringPool.SLASH)) {
				includeFileName = StringPool.SLASH + includeFileName;
			}

			matcher = _jspIncludeFilePattern.matcher(includeFileName);

			if (!matcher.find()) {
				throw new RuntimeException(
					"Invalid include " + includeFileName);
			}

			String extension = matcher.group(1);

			if (extension.equals("svg")) {
				continue;
			}

			includeFileName = buildFullPathIncludeFileName(
				fileName, includeFileName, contentsMap);

			if ((includeFileName.endsWith("jsp") ||
				 includeFileName.endsWith("jspf") ||
				 includeFileName.endsWith("tag")) &&
				!fileNames.contains(includeFileName)) {

				includeFileNames.add(includeFileName);
			}
		}

		return includeFileNames;
	}

	public static Set<String> getJSPReferenceFileNames(
		String fileName, Collection<String> fileNames,
		Map<String, String> contentsMap) {

		Set<String> referenceFileNames = new HashSet<>();

		if (!fileName.endsWith("init.jsp") && !fileName.endsWith("init.jspf") &&
			!fileName.endsWith("init.tag") &&
			!fileName.contains("init-ext.jsp")) {

			return referenceFileNames;
		}

		for (Map.Entry<String, String> entry : contentsMap.entrySet()) {
			String referenceFileName = entry.getKey();

			if (fileNames.contains(referenceFileName)) {
				continue;
			}

			String sharedPath = fileName.substring(
				0, StringUtil.startsWithWeight(referenceFileName, fileName));

			if (Validator.isNull(sharedPath) ||
				!sharedPath.contains(StringPool.SLASH)) {

				continue;
			}

			if (!sharedPath.endsWith(StringPool.SLASH)) {
				sharedPath = sharedPath.substring(
					0, sharedPath.lastIndexOf(CharPool.SLASH) + 1);
			}

			String content = null;

			for (int x = -1;;) {
				x = sharedPath.indexOf(CharPool.SLASH, x + 1);

				if (x == -1) {
					break;
				}

				if (content == null) {
					content = entry.getValue();
				}

				if (content.contains(
						"<%@ include file=\"" + fileName.substring(x)) ||
					content.contains(
						"<%@ include file=\"" + fileName.substring(x + 1))) {

					referenceFileNames.add(referenceFileName);

					break;
				}
			}
		}

		return referenceFileNames;
	}

	public static boolean isJavaSource(String content, int pos) {
		return isJavaSource(content, pos, false);
	}

	public static boolean isJavaSource(
		String content, int pos, boolean checkInsideTags) {

		String s = content.substring(pos);

		Matcher matcher = _javaEndTagPattern.matcher(s);

		if (matcher.find()) {
			s = s.substring(0, matcher.start());

			matcher = _javaStartTagPattern.matcher(s);

			if (!matcher.find()) {
				return true;
			}
		}

		if (!checkInsideTags) {
			return false;
		}

		int x = content.indexOf(CharPool.NEW_LINE, pos);

		if (x == -1) {
			return false;
		}

		s = content.substring(pos, x);

		int y = s.indexOf("%>");

		if (y == -1) {
			return false;
		}

		s = s.substring(0, y);

		if (!s.contains("<%")) {
			return true;
		}

		return false;
	}

	private static final Pattern _includeFilePattern = Pattern.compile(
		"\\s*@\\s*include\\s*file=['\"](.*)['\"]");
	private static final Pattern _javaCodeTagPattern = Pattern.compile(
		"<%\\s*[@=\n]");
	private static final Pattern _javaEndTagPattern = Pattern.compile(
		"[\n\t]%>(\n|\\Z)");
	private static final Pattern _javaStartTagPattern = Pattern.compile(
		"[\n\t]<%\\!?\n");
	private static final Pattern _jspIncludeFilePattern = Pattern.compile(
		"/.*\\.(jsp[f]?|svg|tag)");

}