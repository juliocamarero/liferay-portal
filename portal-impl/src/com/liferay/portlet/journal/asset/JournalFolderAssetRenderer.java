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

package com.liferay.portlet.journal.asset;

import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.trash.TrashRenderer;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.asset.model.BaseAssetRenderer;
import com.liferay.portlet.journal.model.JournalFolder;
import com.liferay.portlet.journal.service.JournalFolderServiceUtil;
import com.liferay.portlet.trash.util.TrashUtil;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

/**
 * @author Alexander Chow
 */
public class JournalFolderAssetRenderer
	extends BaseAssetRenderer implements TrashRenderer {

	public static final String TYPE = "folder";

	public JournalFolderAssetRenderer(JournalFolder folder) {
		_folder = folder;
	}

	public String getClassName() {
		return JournalFolder.class.getName();
	}

	public long getClassPK() {
		return _folder.getFolderId();
	}

	public long getGroupId() {
		return _folder.getGroupId();
	}

	@Override
	public String getIconPath(ThemeDisplay themeDisplay) {
		try {
			if (JournalFolderServiceUtil.getFoldersAndArticlesCount(
					_folder.getGroupId(), _folder.getFolderId(),
					WorkflowConstants.STATUS_APPROVED) > 0) {

				return themeDisplay.getPathThemeImages() +
					"/common/folder_full_document.png";
			}
		}
		catch (Exception e) {
		}

		return themeDisplay.getPathThemeImages() + "/common/folder_empty.png";
	}

	public String getPortletId() {
		AssetRendererFactory assetRendererFactory = getAssetRendererFactory();

		return assetRendererFactory.getPortletId();
	}

	public String getSummary(Locale locale) {
		return HtmlUtil.stripHtml(_folder.getDescription());
	}

	public String getTitle(Locale locale) {
		return TrashUtil.getOriginalTitle(_folder.getName());
	}

	public String getType() {
		return TYPE;
	}

	@Override
	public PortletURL getURLEdit(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
			getControlPanelPlid(liferayPortletRequest), PortletKeys.JOURNAL,
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter("struts_action", "/journal/edit_folder");
		portletURL.setParameter(
			"folderId", String.valueOf(_folder.getFolderId()));

		return portletURL;
	}

	@Override
	public PortletURL getURLView(
			LiferayPortletResponse liferayPortletResponse,
			WindowState windowState)
		throws Exception {

		PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
			PortletKeys.JOURNAL, PortletRequest.RENDER_PHASE);

		portletURL.setParameter("struts_action", "/journal/view");
		portletURL.setParameter(
			"folderId", String.valueOf(_folder.getFolderId()));
		portletURL.setWindowState(windowState);

		return portletURL;
	}

	@Override
	public String getURLViewInContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		String noSuchEntryRedirect) {

		return getURLViewInContext(
			liferayPortletRequest, noSuchEntryRedirect, "/journal/find_folder",
			"folderId", _folder.getFolderId());
	}

	public long getUserId() {
		return _folder.getUserId();
	}

	public String getUserName() {
		return _folder.getUserName();
	}

	public String getUuid() {
		return _folder.getUuid();
	}

	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse,
			String template)
		throws Exception {

		if (template.equals(TEMPLATE_FULL_CONTENT)) {
			renderRequest.setAttribute(WebKeys.JOURNAL_FOLDER, _folder);

			return "/html/portlet/journal/asset/folder_" + template + ".jsp";
		}
		else {
			return null;
		}
	}

	private JournalFolder _folder;

}