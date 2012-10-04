/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.trash.action;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.LiferayPortletConfig;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.portal.kernel.trash.TrashHandlerRegistryUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.trash.DuplicateEntryException;
import com.liferay.portlet.trash.TrashEntryConstants;
import com.liferay.portlet.trash.model.TrashEntry;
import com.liferay.portlet.trash.service.TrashEntryLocalServiceUtil;
import com.liferay.portlet.trash.service.TrashEntryServiceUtil;
import com.liferay.portlet.trash.util.TrashUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Manuel de la Peña
 * @author Zsolt Berentey
 */
public class EditEntryAction extends PortletAction {

	@Override
	public void processAction(
			ActionMapping mapping, ActionForm form, PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			TrashEntry[] entries = null;

			if (cmd.equals(Constants.DELETE)) {
				deleteEntries(actionRequest);
			}
			else if (cmd.equals(Constants.EMPTY_TRASH)) {
				emptyTrash(actionRequest);
			}
			else if (cmd.equals(Constants.MOVE)) {
				entries = moveEntry(actionRequest);
			}
			else if (cmd.equals(Constants.RENAME)) {
				entries = restoreEntryRename(actionRequest);
			}
			else if (cmd.equals(Constants.RESTORE)) {
				entries = restoreEntries(actionRequest);
			}
			else if (cmd.equals(Constants.OVERRIDE)) {
				entries = restoreEntryOverwrite(actionRequest);
			}
			else if (cmd.equals(Constants.CHECK_ENTRY)) {
				checkEntry(actionRequest, actionResponse);

				return;
			}

			if (cmd.equals(Constants.RENAME) || cmd.equals(Constants.RESTORE) ||
				cmd.equals(Constants.OVERRIDE) || cmd.equals(Constants.MOVE)) {

				addRestoreData(
					(LiferayPortletConfig)portletConfig, actionRequest,
					entries);
			}

			sendRedirect(actionRequest, actionResponse);
		}
		catch (PrincipalException pe) {
			if (pe.getMessage() != null) {
				SessionErrors.add(actionRequest, pe.getMessage());
			}
			else {
				SessionErrors.add(actionRequest, pe.getClass());
			}
		}
		catch (Exception e) {
			SessionErrors.add(actionRequest, e.getClass());
		}
	}

	@Override
	public ActionForward render(
			ActionMapping mapping, ActionForm form, PortletConfig portletConfig,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception {

		return mapping.findForward(
			getForward(renderRequest, "portlet.trash.view"));
	}

	protected void addRestoreData(
			LiferayPortletConfig liferayPortletConfig,
			ActionRequest actionRequest, TrashEntry[] entries)
		throws Exception {

		if ((entries == null) || (entries.length <= 0)) {
			return;
		}

		List<String> restoreLinks = new ArrayList<String>();
		List<String> restoreMessages = new ArrayList<String>();

		for (TrashEntry entry : entries) {
			TrashHandler trashHandler =
				TrashHandlerRegistryUtil.getTrashHandler(entry.getClassName());

			String restoreLink = trashHandler.getRestoreLink(
				actionRequest, entry.getClassPK());

			String restoreMessage = trashHandler.getRestoreMessage(
				actionRequest, entry.getClassPK());

			if (Validator.isNull(restoreLink) ||
				Validator.isNull(restoreMessage)) {

				continue;
			}

			restoreLinks.add(restoreLink);
			restoreMessages.add(restoreMessage);
		}

		Map<String, List<String>> data = new HashMap<String, List<String>>();

		data.put("restoreLinks", restoreLinks);
		data.put("restoreMessages", restoreMessages);

		SessionMessages.add(
			actionRequest,
			liferayPortletConfig.getPortletId() +
				SessionMessages.KEY_SUFFIX_DELETE_SUCCESS_DATA, data);

		SessionMessages.add(
			actionRequest,
			liferayPortletConfig.getPortletId() +
				SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
	}

	protected void checkEntry(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long trashEntryId = ParamUtil.getLong(actionRequest, "trashEntryId");

		String newName = ParamUtil.getString(actionRequest, "newName");

		TrashEntry entry = TrashEntryLocalServiceUtil.getTrashEntry(
			trashEntryId);

		TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(
			entry.getClassName());

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		try {
			trashHandler.checkDuplicateTrashEntry(
				entry, TrashEntryConstants.DEFAULT_CONTAINER_ID, newName);

			jsonObject.put("success", true);
		}
		catch (DuplicateEntryException dee) {
			jsonObject.put("duplicateEntryId", dee.getDuplicateEntryId());
			jsonObject.put("oldName", dee.getOldName());
			jsonObject.put("success", false);
			jsonObject.put("trashEntryId", dee.getTrashEntryId());
		}

		writeJSON(actionRequest, actionResponse, jsonObject);
	}

	protected void deleteEntries(ActionRequest actionRequest) throws Exception {
		long trashEntryId = ParamUtil.getLong(actionRequest, "trashEntryId");

		if (trashEntryId > 0) {
			TrashEntryServiceUtil.deleteEntry(trashEntryId);
		}
		else {
			long[] deleteEntryIds = StringUtil.split(
				ParamUtil.getString(actionRequest, "deleteEntryIds"), 0L);

			TrashEntryServiceUtil.deleteEntries(deleteEntryIds);
		}
	}

	protected void emptyTrash(ActionRequest actionRequest) throws Exception {
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		TrashEntryServiceUtil.deleteEntries(themeDisplay.getScopeGroupId());
	}

	protected TrashEntry[] moveEntry(ActionRequest actionRequest)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long containerModelId = ParamUtil.getLong(
			actionRequest, "containerModelId");
		long trashEntryId = ParamUtil.getLong(actionRequest, "trashEntryId");

		TrashEntry entry = TrashEntryLocalServiceUtil.getTrashEntry(
			trashEntryId);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			entry.getClassName(), actionRequest);

		TrashEntryServiceUtil.moveEntry(
			themeDisplay.getScopeGroupId(), trashEntryId, containerModelId,
			serviceContext);

		return new TrashEntry[] {entry};
	}

	protected TrashEntry[] restoreEntries(ActionRequest actionRequest)
		throws Exception {

		long trashEntryId = ParamUtil.getLong(actionRequest, "trashEntryId");

		TrashEntry[] entries = null;

		long[] restoreEntryIds = null;

		if (trashEntryId > 0) {
			restoreEntryIds = new long[] {trashEntryId};
		}
		else {
			restoreEntryIds = StringUtil.split(
				ParamUtil.getString(actionRequest, "restoreEntryIds"), 0L);
		}

		entries = new TrashEntry[restoreEntryIds.length];

		for (int i = 0; i < restoreEntryIds.length; i++) {
			entries[i] = TrashEntryServiceUtil.restoreEntry(
				restoreEntryIds[i], 0, null);
		}

		return entries;
	}

	protected TrashEntry[] restoreEntryOverwrite(ActionRequest actionRequest)
		throws Exception {

		long trashEntryId = ParamUtil.getLong(actionRequest, "trashEntryId");

		long duplicateEntryId = ParamUtil.getLong(
			actionRequest, "duplicateEntryId");

		TrashEntry entry = TrashEntryServiceUtil.restoreEntry(
			trashEntryId, duplicateEntryId, null);

		return new TrashEntry[] {entry};
	}

	protected TrashEntry[] restoreEntryRename(ActionRequest actionRequest)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long trashEntryId = ParamUtil.getLong(actionRequest, "trashEntryId");

		String newName = ParamUtil.getString(actionRequest, "newName");

		if (Validator.isNull(newName)) {
			String oldName = ParamUtil.getString(actionRequest, "oldName");

			newName = TrashUtil.getNewName(themeDisplay, oldName);
		}

		TrashEntry entry = TrashEntryServiceUtil.restoreEntry(
			trashEntryId, 0, newName);

		return new TrashEntry[] {entry};
	}

}