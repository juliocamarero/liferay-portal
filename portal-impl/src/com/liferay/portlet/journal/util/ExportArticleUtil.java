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

package com.liferay.portlet.journal.util;

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.portlet.PortletRequestModel;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.documentlibrary.util.DLUtil;
import com.liferay.portlet.documentlibrary.util.DocumentConversionUtil;
import com.liferay.portlet.journal.model.JournalArticleDisplay;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author Eudaldo Alonso
 */
public class ExportArticleUtil extends PortletAction {

	public static Tuple getFile(
			long groupId, String articleId, String targetExtension,
			String[] allowedExtensions, String languageId,
			PortletRequestModel portletRequestModel, ThemeDisplay themeDisplay)
		throws Exception {

		JournalArticleDisplay articleDisplay =
			JournalContentUtil.getDisplay(
				groupId, articleId, null, "export", languageId, 1,
				portletRequestModel, themeDisplay);

		int pages = articleDisplay.getNumberOfPages();

		StringBundler sb = new StringBundler(pages + 12);

		sb.append("<html>");

		sb.append("<head>");
		sb.append("<meta content=\"");
		sb.append(ContentTypes.TEXT_HTML_UTF8);
		sb.append("\" http-equiv=\"content-type\" />");
		sb.append("<base href=\"");
		sb.append(themeDisplay.getPortalURL());
		sb.append("\" />");
		sb.append("</head>");

		sb.append("<body>");

		sb.append(articleDisplay.getContent());

		for (int i = 2; i <= pages; i++) {
			articleDisplay = JournalContentUtil.getDisplay(
				groupId, articleId, "export", languageId, i, themeDisplay);

			sb.append(articleDisplay.getContent());
		}

		sb.append("</body>");
		sb.append("</html>");

		InputStream is = new UnsyncByteArrayInputStream(
			sb.toString().getBytes(StringPool.UTF8));

		String title = articleDisplay.getTitle();
		String sourceExtension = "html";

		String fileName = title.concat(StringPool.PERIOD).concat(
			sourceExtension);

		if (Validator.isNull(targetExtension) ||
			!ArrayUtil.contains(allowedExtensions, targetExtension)) {

			return new Tuple(fileName, is);
		}

		String id = DLUtil.getTempFileId(
			articleDisplay.getId(), String.valueOf(articleDisplay.getVersion()),
			languageId);

		File convertedFile = DocumentConversionUtil.convert(
			id, is, sourceExtension, targetExtension);

		if (convertedFile != null) {
			fileName = title.concat(StringPool.PERIOD).concat(targetExtension);

			is = new FileInputStream(convertedFile);
		}

		return new Tuple(fileName, is);
	}

}