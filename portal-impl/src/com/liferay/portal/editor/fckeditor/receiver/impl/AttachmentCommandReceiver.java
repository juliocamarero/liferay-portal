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

package com.liferay.portal.editor.fckeditor.receiver.impl;

import com.liferay.portal.editor.fckeditor.command.CommandArgument;
import com.liferay.portal.editor.fckeditor.exception.FCKException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.documentlibrary.DuplicateDirectoryException;
import com.liferay.portlet.documentlibrary.store.DLStoreUtil;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author Julio Camarero
 */
public class AttachmentCommandReceiver extends BaseCommandReceiver {

	protected String createFolder(CommandArgument commandArgument) {
		return "0";
	}

	protected String fileUpload(
		CommandArgument commandArgument, String fileName, File file,
		String extension) {

		try {
			HttpServletRequest request = commandArgument.getHttpServletRequest();

			long resourcePK = ParamUtil.getLong(request, "wikiPageResourcePrimKey");

			WikiPage page = WikiPageLocalServiceUtil.getPage(resourcePK);

			String portletId = CompanyConstants.SYSTEM_STRING;

			long groupId = GroupConstants.DEFAULT_PARENT_GROUP_ID;

			long repositoryId = CompanyConstants.SYSTEM;

			String dirName = page.getAttachmentsDir();

			try {
				DLStoreUtil.addDirectory(
					page.getCompanyId(), repositoryId, dirName);
			}
			catch (DuplicateDirectoryException dde) {
			}

			DLStoreUtil.addFile(
				page.getCompanyId(), portletId, groupId,
				repositoryId, dirName + "/" + fileName, new ServiceContext(),
				file);
		}
		catch (Exception e) {
			throw new FCKException(e);
		}

		return "0";
	}

	protected void getFolders(
		CommandArgument commandArgument, Document document, Node rootNode) {
	}

	protected void getFoldersAndFiles(
		CommandArgument commandArgument, Document document, Node rootNode) {

		try {
			_getFiles(commandArgument, document, rootNode);
		}
		catch (Exception e) {
			throw new FCKException(e);
		}
	}

	protected boolean isStagedData(Group group) {
		return group.isStagedPortlet(PortletKeys.WIKI);
	}

	private void _getFiles(
			CommandArgument commandArgument, Document document, Node rootNode)
		throws Exception {

		Element filesElement = document.createElement("Files");

		rootNode.appendChild(filesElement);

		HttpServletRequest request = commandArgument.getHttpServletRequest();

		long resourcePK = ParamUtil.getLong(request, "wikiPageResourcePrimKey");

		WikiPage page = WikiPageLocalServiceUtil.getPage(resourcePK);

		long repositoryId = CompanyConstants.SYSTEM;

		String dirName = page.getAttachmentsDir();

		String[] fileNames = DLStoreUtil.getFileNames(
			page.getCompanyId(), CompanyConstants.SYSTEM, dirName);

		for (String fileName : fileNames) {
			byte[] fileEntry = DLStoreUtil.getFile(
				page.getCompanyId(), CompanyConstants.SYSTEM, fileName);

			String[] parts = StringUtil.split(fileName, StringPool.SLASH);
			fileName = parts[3];

			Element fileElement = document.createElement("File");

			filesElement.appendChild(fileElement);

			fileElement.setAttribute("name", fileName);
			fileElement.setAttribute("desc", fileName);
			fileElement.setAttribute("size", getSize(fileEntry.length));

			/*StringBundler url = new StringBundler(7);

			ThemeDisplay themeDisplay = commandArgument.getThemeDisplay();

			url.append(themeDisplay.getPathMain());
			url.append("/wiki/get_page_attachment?title=");
			url.append(HttpUtil.encodeURL(page.getTitle()));
			url.append("&nodeId=");
			url.append(page.getNodeId());
			url.append("&fileName=");
			url.append(fileName);*/

			fileElement.setAttribute("url", fileName);
		}
	}

}