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

package com.liferay.portlet.stagingbar.action;

import com.liferay.portal.LayoutBranchNameException;
import com.liferay.portal.NoSuchGroupException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.LayoutBranchServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portlet.layoutsadmin.action.EditLayoutsAction;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Brian Wing Shun Chan
 * @author Julio Camarero
 */
public class EditLayoutBranchAction extends EditLayoutsAction {

	@Override
	public void processAction(
			ActionMapping mapping, ActionForm form, PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		try {
			checkPermissions(actionRequest);
		}
		catch (PrincipalException pe) {
			return;
		}

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				updateLayoutSetBranch(actionRequest);
			}
			else if (cmd.equals(Constants.DELETE)) {
				deleteLayoutBranch(actionRequest, portletConfig);
			}

			if (SessionErrors.isEmpty(actionRequest)) {
				SessionMessages.add(
					actionRequest,
					portletConfig.getPortletName() + ".doConfigure");
			}

			sendRedirect(actionRequest, actionResponse);
		}
		catch (Exception e) {
			if (e instanceof LayoutBranchNameException) {
				LayoutBranchNameException lbne = (LayoutBranchNameException)e;

				SessionErrors.add(
					actionRequest, e.getClass().getName() + lbne.getType());

				sendRedirect(actionRequest, actionResponse);
			}
			else if (e instanceof PrincipalException ||
				e instanceof SystemException) {

				SessionErrors.add(actionRequest, e.getClass().getName());

				setForward(actionRequest, "portlet.staging_bar.error");
			}
			else {
				throw e;
			}
		}
	}

	@Override
	public ActionForward render(
			ActionMapping mapping, ActionForm form, PortletConfig portletConfig,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception {

		try {
			checkPermissions(renderRequest);
		}
		catch (PrincipalException pe) {
			SessionErrors.add(
				renderRequest, PrincipalException.class.getName());

			return mapping.findForward("portlet.staging_bar.error");
		}

		try {
			getGroup(renderRequest);
		}
		catch (Exception e) {
			if (e instanceof NoSuchGroupException ||
				e instanceof PrincipalException) {

				SessionErrors.add(renderRequest, e.getClass().getName());

				return mapping.findForward("portlet.staging_bar.error");
			}
			else {
				throw e;
			}
		}

		return mapping.findForward(
			getForward(
				renderRequest, "portlet.staging_bar.edit_layout_branch"));
	}

	protected void deleteLayoutBranch(
			ActionRequest actionRequest, PortletConfig portletConfig)
		throws Exception {

		long layoutBranchId = ParamUtil.getLong(
			actionRequest, "layoutBranchId");

		long currentLayoutBranchId = ParamUtil.getLong(
			actionRequest, "currentLayoutBranchId");

		LayoutBranchServiceUtil.deleteLayoutBranch(layoutBranchId);

		if (layoutBranchId == currentLayoutBranchId) {
			SessionMessages.add(
				actionRequest,
				portletConfig.getPortletName() + ".notAjaxable");
		}
	}

	protected void updateLayoutSetBranch(ActionRequest actionRequest)
		throws Exception {

		long layoutBranchId = ParamUtil.getLong(
			actionRequest, "layoutBranchId");

		long layoutRevisionId = ParamUtil.getLong(
			actionRequest, "mergeLayoutRevisionId");
		String name = ParamUtil.getString(actionRequest, "name");
		String description = ParamUtil.getString(actionRequest, "description");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			actionRequest);

		if (layoutBranchId <= 0) {
			LayoutBranchServiceUtil.addLayoutBranch(
				layoutRevisionId, name, description, false, serviceContext);
		}
		else {
			LayoutBranchServiceUtil.updateLayoutBranch(
				layoutBranchId, name, description, serviceContext);
		}
	}

}