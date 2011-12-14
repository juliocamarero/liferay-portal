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

package com.liferay.portal.tools.jspc.resin;

import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.StackTraceUtil;
import com.liferay.portal.util.FileImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;

/**
 * @author Brian Wing Shun Chan
 */
public class BatchJspCompiler {

	public static void main(String[] args) {
		if (args.length == 2) {
			new BatchJspCompiler(args[0], args[1]);
		}
		else {
			throw new IllegalArgumentException();
		}
	}

	public BatchJspCompiler(String appDir, String classDir) {
		try {
			_appDir = appDir;
			_classDir = classDir;

			DirectoryScanner ds = new DirectoryScanner();

			ds.setBasedir(appDir);
			ds.setIncludes(new String[] {"**\\*.jsp"});

			ds.scan();

			String[] fileNames = ds.getIncludedFiles();

			Arrays.sort(fileNames);

			_compile(fileNames);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void _compile(String[] fileNames) throws Exception {
		if (fileNames.length == 0) {
			return;
		}

		List<String> arguments = new ArrayList<String>();

		arguments.add("-app-dir");
		arguments.add(_appDir);
		arguments.add("-class-dir");
		arguments.add(_classDir);
		arguments.addAll(Arrays.asList(fileNames));

		MethodKey methodKey = new MethodKey(
			"com.caucho.jsp.JspCompiler", "main", String[].class);

		MethodHandler methodHandler = new MethodHandler(
			methodKey, (Object)arguments.toArray(new String[arguments.size()]));

		try {
			methodHandler.invoke(false);
		}
		catch (Exception e) {
			_fileUtil.write(
				_appDir + "/../jspc_error", StackTraceUtil.getStackTrace(e));
		}
	}

	private static FileImpl _fileUtil = FileImpl.getInstance();

	private String _appDir;
	private String _classDir;

}