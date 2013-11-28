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

package com.liferay.portal.tools;

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.io.unsync.UnsyncPrintWriter;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.util.UnsyncPrintWriterPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ModelHintsConstants;
import com.liferay.portal.scripting.ruby.RubyExecutor;
import com.liferay.portal.servlet.filters.aggregate.AggregateFilter;
import com.liferay.portal.servlet.filters.aggregate.FileAggregateContext;
import com.liferay.portal.util.FastDateFormatFactoryImpl;
import com.liferay.portal.util.FileImpl;
import com.liferay.portal.util.PortalImpl;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsImpl;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.DirectoryScanner;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 * @author Eduardo Lundgren
 */
public class SassToCssBuilder {

	public static File getCacheFile(String fileName) {
		return getCacheFile(fileName, StringPool.BLANK);
	}

	public static File getCacheFile(String fileName, String suffix) {
		return new File(getCacheFileName(fileName, suffix));
	}

	public static String getCacheFileName(String fileName, String suffix) {
		String cacheFileName = StringUtil.replace(
			fileName, StringPool.BACK_SLASH, StringPool.SLASH);

		int pos1 = cacheFileName.lastIndexOf(StringPool.SLASH);
		int pos2 = cacheFileName.lastIndexOf(StringPool.PERIOD);

		return cacheFileName.substring(0, pos1 + 1) + ".sass-cache/" +
			cacheFileName.substring(pos1 + 1, pos2) + suffix +
				cacheFileName.substring(pos2);
	}

	public static void main(String[] args) {
		Map<String, String> arguments = ArgumentsUtil.parseArguments(args);

		List<String> dirNames = new ArrayList<String>();

		String dirName = arguments.get("sass.dir");

		if (Validator.isNotNull(dirName)) {
			dirNames.add(dirName);
		}
		else {
			for (int i = 0;; i++ ) {
				dirName = arguments.get("sass.dir." + i);

				if (Validator.isNotNull(dirName)) {
					dirNames.add(dirName);
				}
				else {
					break;
				}
			}
		}

		String docrootDirName = arguments.get("sass.docroot.dir");
		String portalCommonDirName = arguments.get("sass.portal.common.dir");

		try {
			new SassToCssBuilder(dirNames, docrootDirName, portalCommonDirName);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String parseStaticTokens(String content) {
		return StringUtil.replace(
			content,
			new String[] {
				"@model_hints_constants_text_display_height@",
				"@model_hints_constants_text_display_width@",
				"@model_hints_constants_textarea_display_height@",
				"@model_hints_constants_textarea_display_width@"
			},
			new String[] {
				ModelHintsConstants.TEXT_DISPLAY_HEIGHT,
				ModelHintsConstants.TEXT_DISPLAY_WIDTH,
				ModelHintsConstants.TEXTAREA_DISPLAY_HEIGHT,
				ModelHintsConstants.TEXTAREA_DISPLAY_WIDTH
			});
	}

	public SassToCssBuilder(
			List<String> dirNames, String docrootDirName,
			String portalCommonDirName)
		throws Exception {

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		_initUtil(classLoader);

		_jsScript = StringUtil.read(
			classLoader, "com/liferay/portal/servlet/filters/dynamiccss/r2.js");

		_rubyScript = StringUtil.read(
			classLoader,
			"com/liferay/portal/servlet/filters/dynamiccss/main.rb");

		_tempDir = SystemProperties.get(SystemProperties.TMP_DIR);

		for (String dirName : dirNames) {

			// Create a new Ruby executor as a workaround for a bug with Ruby
			// that breaks "ant build-css" when it parses too many CSS files

			_rubyExecutor = new RubyExecutor();

			_rubyExecutor.setExecuteInSeparateThread(false);

			_parseSassDirectory(dirName, docrootDirName, portalCommonDirName);
		}
	}

	private void _addSassCache(
			String docrootDirName, String portalCommonDirName, String fileName)
		throws Exception {

		String filePath = docrootDirName.concat(fileName);

		File file = new File(filePath);
		File cacheFile = getCacheFile(filePath);

		String parsedContent = _parseSassFile(
			docrootDirName, portalCommonDirName, fileName);

		FileUtil.write(cacheFile, parsedContent);

		cacheFile.setLastModified(file.lastModified());

		// Generate rtl cache

		File rtlCacheFile = getCacheFile(filePath, "_rtl");

		FileUtil.write(rtlCacheFile, _getRtlCss(fileName, parsedContent));

		rtlCacheFile.setLastModified(file.lastModified());
	}

	private String _getContent(String docrootDirName, String fileName)
		throws Exception {

		File file = new File(docrootDirName.concat(fileName));

		String content = FileUtil.read(file);

		content = AggregateFilter.aggregateCss(
			new FileAggregateContext(docrootDirName, fileName), content);

		return parseStaticTokens(content);
	}

	private String _getCssThemePath(String fileName) {
		int pos = fileName.lastIndexOf("/css/");

		return fileName.substring(0, pos + 4);
	}

	private String _getRtlCss(String fileName, String css) throws Exception {
		Context context = Context.enter();

		String rtlCss = css;

		try {
			ScriptableObject scope = context.initStandardObjects();

			context.evaluateString(scope, _jsScript, "script", 1, null);

			Function function = (Function)scope.get("r2", scope);

			Object result = function.call(
				context, scope, scope, new Object[] {css});

			rtlCss = (String)Context.jsToJava(result, String.class);
		}
		catch (Exception e) {
			System.out.println("Unable to parse " + fileName + " to rtl");

			e.printStackTrace();
		}
		finally {
			Context.exit();
		}

		return rtlCss;
	}

	private void _initUtil(ClassLoader classLoader) {
		FastDateFormatFactoryUtil fastDateFormatFactoryUtil =
			new FastDateFormatFactoryUtil();

		fastDateFormatFactoryUtil.setFastDateFormatFactory(
			new FastDateFormatFactoryImpl());

		FileUtil fileUtil = new FileUtil();

		fileUtil.setFile(new FileImpl());

		PortalClassLoaderUtil.setClassLoader(classLoader);

		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(new PortalImpl());

		PropsUtil.setProps(new PropsImpl());
	}

	private boolean _isModified(String dirName, String[] fileNames)
		throws Exception {

		for (String fileName : fileNames) {
			fileName = _normalizeFileName(dirName, fileName);

			File file = new File(fileName);
			File cacheFile = getCacheFile(fileName);

			if (file.lastModified() != cacheFile.lastModified()) {
				return true;
			}
		}

		return false;
	}

	private String _normalizeFileName(String dirName, String fileName) {
		return StringUtil.replace(
			dirName + StringPool.SLASH + fileName,
			new String[] {
				StringPool.BACK_SLASH, StringPool.DOUBLE_SLASH
			},
			new String[] {
				StringPool.SLASH, StringPool.SLASH
			}
		);
	}

	private void _parseSassDirectory(
			String dirName, String docrootDirName, String portalCommonDirName)
		throws Exception {

		DirectoryScanner directoryScanner = new DirectoryScanner();

		String basedir = docrootDirName.concat(dirName);

		directoryScanner.setBasedir(basedir);

		directoryScanner.setExcludes(
			new String[] {
				"**\\_diffs\\**", "**\\.sass-cache*\\**",
				"**\\.sass_cache_*\\**", "**\\_sass_cache_*\\**",
				"**\\_styled\\**", "**\\_unstyled\\**"
			});
		directoryScanner.setIncludes(new String[] {"**\\*.css"});

		directoryScanner.scan();

		String[] fileNames = directoryScanner.getIncludedFiles();

		if (!_isModified(basedir, fileNames)) {
			return;
		}

		for (String fileName : fileNames) {
			fileName = _normalizeFileName(dirName, fileName);

			try {
				long start = System.currentTimeMillis();

				_addSassCache(docrootDirName, portalCommonDirName, fileName);

				long end = System.currentTimeMillis();

				System.out.println(
					"Parsed " + docrootDirName + fileName + " in " +
						(end - start) + " ms");
			}
			catch (Exception e) {
				System.out.println("Unable to parse " + fileName);

				e.printStackTrace();
			}
		}
	}

	private String _parseSassFile(
			String docrootDirName, String portalCommonDirName, String fileName)
		throws Exception {

		String filePath = docrootDirName.concat(fileName);

		Map<String, Object> inputObjects = new HashMap<String, Object>();

		inputObjects.put("commonSassPath", portalCommonDirName);
		inputObjects.put("content", _getContent(docrootDirName, fileName));
		inputObjects.put("cssRealPath", filePath);
		inputObjects.put("cssThemePath", _getCssThemePath(filePath));
		inputObjects.put("sassCachePath", _tempDir);

		UnsyncByteArrayOutputStream unsyncByteArrayOutputStream =
			new UnsyncByteArrayOutputStream();

		UnsyncPrintWriter unsyncPrintWriter = UnsyncPrintWriterPool.borrow(
			unsyncByteArrayOutputStream);

		inputObjects.put("out", unsyncPrintWriter);

		_rubyExecutor.eval(null, inputObjects, null, _rubyScript);

		unsyncPrintWriter.flush();

		return unsyncByteArrayOutputStream.toString();
	}

	private String _jsScript;
	private RubyExecutor _rubyExecutor;
	private String _rubyScript;
	private String _tempDir;

}