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

package com.liferay.hello.soy.web.internal.portlet.action;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=hello_soy_portlet",
		"mvc.command.name=/gallery/GalleryHome"
	},
	service = MVCRenderCommand.class
)
public class GalleryMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		Template template = (Template)renderRequest.getAttribute(
			WebKeys.TEMPLATE);

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		List<DLFileEntry> fileEntries = _dlFileEntryLocalService.getFileEntries(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		List<Map<String, Object>> fileEntriesInfo = new ArrayList<>();

		for (DLFileEntry dlFileEntry : fileEntries) {
			Map<String, Object> fileEntryInfo = new HashMap<>();

			FileEntry fileEntry = null;

			try {
				fileEntry = _dlAppLocalService.getFileEntry(
					dlFileEntry.getFileEntryId());

				String thumbnailSrc = DLUtil.getThumbnailSrc(
					fileEntry, themeDisplay);

				fileEntryInfo.put("fileEntryId", dlFileEntry.getFileEntryId());
				fileEntryInfo.put("name", dlFileEntry.getName());
				fileEntryInfo.put("src", thumbnailSrc);

				fileEntriesInfo.add(fileEntryInfo);
			}
			catch (Exception e) {
				continue;
			}
		}

		template.put("files", fileEntriesInfo);

		template.put("path", "Gallery");

		return "GalleryHome.render";
	}

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private DLFileEntryLocalService _dlFileEntryLocalService;

}