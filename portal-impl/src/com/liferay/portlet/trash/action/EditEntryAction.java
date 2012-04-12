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

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.portal.kernel.trash.TrashHandlerRegistryUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.trash.model.TrashEntry;
import com.liferay.portlet.trash.service.TrashEntryLocalServiceUtil;

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
 */
public class EditEntryAction extends PortletAction {

	@Override
	public void processAction(
			ActionMapping mapping, ActionForm form, PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		String action = StringPool.BLANK;

		int affectedEntries[] = null;

		try {
			if (cmd.equals(Constants.DELETE)) {
				action = "Delete";

				affectedEntries = deleteTrashEntries(actionRequest);
			}
			else if (cmd.equals(Constants.RESTORE)) {
				action = "Restore";

				affectedEntries = restoreTrashEntries(actionRequest);
			}

			manageRecycleBinMessages(actionRequest, affectedEntries, action);

			actionRequest.setAttribute(
				WebKeys.RECYCLE_BIN_RESULTS, affectedEntries);
		}
		catch (Exception e) {
			SessionErrors.add(actionRequest, e.getClass().getName());

			return;
		}

		sendRedirect(actionRequest, actionResponse);
	}

	@Override
	public ActionForward render(
			ActionMapping mapping, ActionForm form, PortletConfig portletConfig,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception {

		return mapping.findForward(
			getForward(renderRequest, "portlet.trash.view"));
	}

	protected int[] deleteTrashEntries(ActionRequest actionRequest)
		throws Exception {

		long trashEntryClassPK = ParamUtil.getLong(actionRequest, "classPK");

		/*
		deletedEntriesCount[0]: successfully deleted
		deletedEntriesCount[1]: not restored (permissions...)
		*/

		int deletedEntriesCount[] = {0, 0};

		if (trashEntryClassPK > 0) {
			String trashEntryClassName = ParamUtil.getString(
				actionRequest, "className");

			if (deleteTrashEntry(trashEntryClassName, trashEntryClassPK)) {
				deletedEntriesCount[0]++;
			}
			else {
				deletedEntriesCount[1]++;
			}
		}
		else {
			long[] deleteTrashEntryIds = StringUtil.split(
				ParamUtil.getString(actionRequest, "affectedEntryIds"), 0L);

			for (int i = 0; i < deleteTrashEntryIds.length; i++) {
				if (deleteTrashEntry(deleteTrashEntryIds[i])) {
					deletedEntriesCount[0]++;
				}
				else {
					deletedEntriesCount[1]++;
				}
			}
		}

		return deletedEntriesCount;
	}

	protected boolean deleteTrashEntry(long trashEntryId) throws Exception {
		TrashEntry trashEntry = null;

		try {
			trashEntry = TrashEntryLocalServiceUtil.getTrashEntry(trashEntryId);

			TrashHandler trashHandler =
				TrashHandlerRegistryUtil.getTrashHandler(
					trashEntry.getClassName());

			trashHandler.deleteTrashEntry(trashEntry.getClassPK());
		}
		catch (Exception e) {
			return false;
		}

		return true;
	}

	protected boolean deleteTrashEntry(String className, long classPK)
		throws Exception {

		TrashEntry trashEntry = null;

		try {
			trashEntry = TrashEntryLocalServiceUtil.getEntry(
				className, classPK);

			TrashHandler trashHandler =
				TrashHandlerRegistryUtil.getTrashHandler(
					trashEntry.getClassName());

			trashHandler.deleteTrashEntry(trashEntry.getClassPK());
		}
		catch (Exception e) {
			return false;
		}

		return true;
	}

	protected void manageRecycleBinMessages(
		ActionRequest actionRequest, int[] affectedEntries, String action) {

		String key = StringPool.BLANK;

		int affectedEntriesCount = affectedEntries[0];
		int notAffectedEntriesCount = affectedEntries[1];

		if (affectedEntriesCount >= 1) {
			if (affectedEntriesCount == 1) {
				key = "success" + action + "Entry";
			}
			else {
				key = "success" + action + "Entries";
			}

			SessionMessages.add(actionRequest, key);
		}

		if (notAffectedEntriesCount > 0) {
			if (affectedEntriesCount == 1) {
				key = "errorEntry" + action + "Entry";
			}
			else {
				key = "error" + action + "Entries";
			}

			SessionErrors.add(actionRequest, key);
		}
	}

	protected int[] restoreTrashEntries(ActionRequest actionRequest)
		throws Exception {

		long trashEntryClassPK = ParamUtil.getLong(actionRequest, "classPK");

		/*
		restoredEntriesCount[0]: successfully restored
		restoredEntriesCount[1]: not restored (permissions...)
		*/

		int restoredEntriesCount[] = {0, 0};

		if (trashEntryClassPK > 0) {
			String trashEntryClassName = ParamUtil.getString(
				actionRequest, "className");

			if (restoreTrashEntry(trashEntryClassName, trashEntryClassPK)) {
				restoredEntriesCount[0]++;
			}
			else {
				restoredEntriesCount[1]++;
			}
		}
		else {
			long[] restoreTrashEntryIds = StringUtil.split(
				ParamUtil.getString(actionRequest, "affectedEntryIds"), 0L);

			for (int i = 0; i < restoreTrashEntryIds.length; i++) {
				if (restoreTrashEntry(restoreTrashEntryIds[i])) {
					restoredEntriesCount[0]++;
				}
				else {
					restoredEntriesCount[1]++;
				}
			}
		}

		return restoredEntriesCount;
	}

	protected boolean restoreTrashEntry(long trashEntryId) {
		TrashEntry trashEntry = null;

		try {
			trashEntry = TrashEntryLocalServiceUtil.getTrashEntry(trashEntryId);

			TrashHandler trashHandler =
				TrashHandlerRegistryUtil.getTrashHandler(
					trashEntry.getClassName());

			trashHandler.restoreTrashEntry(trashEntry.getClassPK());
		}
		catch (Exception e) {
			return false;
		}

		return true;
	}

	protected boolean restoreTrashEntry(String className, long classPK) {
		TrashEntry trashEntry = null;

		try {
			trashEntry = TrashEntryLocalServiceUtil.getEntry(
				className, classPK);

			TrashHandler trashHandler =
				TrashHandlerRegistryUtil.getTrashHandler(
					trashEntry.getClassName());

			trashHandler.restoreTrashEntry(trashEntry.getClassPK());
		}
		catch (Exception e) {
			return false;
		}

		return true;
	}

}