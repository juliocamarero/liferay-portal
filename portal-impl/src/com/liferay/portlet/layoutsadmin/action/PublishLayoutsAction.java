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

package com.liferay.portlet.layoutsadmin.action;

import com.liferay.portal.DuplicateLockException;
import com.liferay.portal.ImageTypeException;
import com.liferay.portal.LayoutFriendlyURLException;
import com.liferay.portal.LayoutNameException;
import com.liferay.portal.LayoutParentLayoutIdException;
import com.liferay.portal.LayoutPrototypeException;
import com.liferay.portal.LayoutSetVirtualHostException;
import com.liferay.portal.LayoutTypeException;
import com.liferay.portal.NoSuchGroupException;
import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.RemoteExportException;
import com.liferay.portal.RemoteOptionsException;
import com.liferay.portal.RequiredLayoutException;
import com.liferay.portal.SitemapChangeFrequencyException;
import com.liferay.portal.SitemapIncludeException;
import com.liferay.portal.SitemapPagePriorityException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.staging.StagingUtil;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.permission.GroupPermissionUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.service.permission.LayoutPrototypePermissionUtil;
import com.liferay.portal.service.permission.LayoutSetPrototypePermissionUtil;
import com.liferay.portal.service.permission.UserPermissionUtil;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.sites.action.ActionUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Alexander Chow
 * @author Raymond Augé
 * @author Levente Hudák
 */
public class PublishLayoutsAction extends PortletAction {

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

		String redirect = ParamUtil.getString(actionRequest, "redirect");
		String closeRedirect = ParamUtil.getString(
			actionRequest, "closeRedirect");

		try {
			if (cmd.equals("copy_from_live")) {
				StagingUtil.copyFromLive(actionRequest);
			}
			else if (cmd.equals("publish_to_live")) {
				StagingUtil.publishToLive(actionRequest);
			}
			else if (cmd.equals("publish_to_remote")) {
				StagingUtil.publishToRemote(actionRequest);
			}
			else if (cmd.equals("schedule_copy_from_live")) {
				StagingUtil.scheduleCopyFromLive(actionRequest);
			}
			else if (cmd.equals("schedule_publish_to_live")) {
				StagingUtil.schedulePublishToLive(actionRequest);
			}
			else if (cmd.equals("schedule_publish_to_remote")) {
				StagingUtil.schedulePublishToRemote(actionRequest);
			}
			else if (cmd.equals("unschedule_copy_from_live")) {
				StagingUtil.unscheduleCopyFromLive(actionRequest);
			}
			else if (cmd.equals("unschedule_publish_to_live")) {
				StagingUtil.unschedulePublishToLive(actionRequest);
			}
			else if (cmd.equals("unschedule_publish_to_remote")) {
				StagingUtil.unschedulePublishToRemote(actionRequest);
			}

			sendRedirect(
				portletConfig, actionRequest, actionResponse, redirect,
				closeRedirect);
		}
		catch (Exception e) {
			if (e instanceof NoSuchLayoutException ||
				e instanceof PrincipalException) {

				SessionErrors.add(actionRequest, e.getClass());

				setForward(actionRequest, "portlet.layouts_admin.error");
			}
			else if (e instanceof ImageTypeException ||
					 e instanceof LayoutFriendlyURLException ||
					 e instanceof LayoutNameException ||
					 e instanceof LayoutParentLayoutIdException ||
					 e instanceof LayoutSetVirtualHostException ||
					 e instanceof LayoutTypeException ||
					 e instanceof RequiredLayoutException ||
					 e instanceof SitemapChangeFrequencyException ||
					 e instanceof SitemapIncludeException ||
					 e instanceof SitemapPagePriorityException ||
					 e instanceof UploadException) {

				if (e instanceof LayoutFriendlyURLException) {
					SessionErrors.add(
						actionRequest,
						LayoutFriendlyURLException.class.getName(), e);
				}
				else {
					SessionErrors.add(actionRequest, e.getClass(), e);
				}

				sendRedirect(
					portletConfig, actionRequest, actionResponse, redirect,
					closeRedirect);
			}
			else if (e instanceof DuplicateLockException ||
					 e instanceof LayoutPrototypeException ||
					 e instanceof RemoteExportException ||
					 e instanceof RemoteOptionsException ||
					 e instanceof SystemException) {

				SessionErrors.add(actionRequest, e.getClass(), e);

				redirect = ParamUtil.getString(actionRequest, "pagesRedirect");

				sendRedirect(
					portletConfig, actionRequest, actionResponse, redirect,
					closeRedirect);
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
			ActionUtil.getGroup(renderRequest);
		}
		catch (Exception e) {
			if (e instanceof NoSuchGroupException ||
				e instanceof PrincipalException) {

				SessionErrors.add(renderRequest, e.getClass());

				return mapping.findForward("portlet.layouts_admin.error");
			}
			else {
				throw e;
			}
		}

		return mapping.findForward(
			getForward(renderRequest, "portlet.layouts_admin.publish_layouts"));
	}

	protected void checkPermission(
			PermissionChecker permissionChecker, Group group, Layout layout,
			long selPlid)
		throws PortalException, SystemException {

		if (selPlid > 0) {
			LayoutPermissionUtil.check(
				permissionChecker, layout, ActionKeys.VIEW);
		}
		else {
			GroupPermissionUtil.check(
				permissionChecker, group, ActionKeys.VIEW);
		}
	}

	protected void checkPermissions(PortletRequest portletRequest)
		throws Exception {

		Group group = getGroup(portletRequest);

		if (group == null) {
			throw new PrincipalException();
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		Layout layout = themeDisplay.getLayout();

		String cmd = ParamUtil.getString(portletRequest, Constants.CMD);

		long selPlid = ParamUtil.getLong(portletRequest, "selPlid");

		if (selPlid > 0) {
			layout = LayoutLocalServiceUtil.getLayout(selPlid);
		}

		if (cmd.equals(Constants.ADD)) {
			long parentPlid = ParamUtil.getLong(portletRequest, "parentPlid");

			if (parentPlid == LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {
				if (!GroupPermissionUtil.contains(
						permissionChecker, group.getGroupId(),
						ActionKeys.ADD_LAYOUT)) {

					throw new PrincipalException();
				}
			}
			else {
				layout = LayoutLocalServiceUtil.getLayout(parentPlid);

				if (!LayoutPermissionUtil.contains(
						permissionChecker, layout, ActionKeys.ADD_LAYOUT)) {

					throw new PrincipalException();
				}
			}
		}
		else if (cmd.equals(Constants.DELETE)) {
			if (!LayoutPermissionUtil.contains(
					permissionChecker, layout, ActionKeys.DELETE)) {

				throw new PrincipalException();
			}
		}
		else if (cmd.equals(Constants.UPDATE)) {
			if (group.isCompany()) {
				if (!permissionChecker.isCompanyAdmin()) {
					throw new PrincipalException();
				}
			}
			else if (group.isLayoutPrototype()) {
				LayoutPrototypePermissionUtil.check(
					permissionChecker, group.getClassPK(), ActionKeys.UPDATE);
			}
			else if (group.isLayoutSetPrototype()) {
				LayoutSetPrototypePermissionUtil.check(
					permissionChecker, group.getClassPK(), ActionKeys.UPDATE);
			}
			else if (group.isUser()) {
				long groupUserId = group.getClassPK();

				User groupUser = UserLocalServiceUtil.getUserById(groupUserId);

				long[] organizationIds = groupUser.getOrganizationIds();

				UserPermissionUtil.check(
					permissionChecker, groupUserId, organizationIds,
					ActionKeys.UPDATE);
			}
			else {
				checkPermission(permissionChecker, group, layout, selPlid);
			}
		}
		else if (cmd.equals("publish_to_live")) {
			boolean hasUpdateLayoutPermission = false;

			if (layout != null) {
				hasUpdateLayoutPermission = LayoutPermissionUtil.contains(
					permissionChecker, layout, ActionKeys.UPDATE);
			}

			if (group.isCompany() || group.isSite()) {
				boolean publishToLive = GroupPermissionUtil.contains(
					permissionChecker, group.getGroupId(),
					ActionKeys.PUBLISH_STAGING);

				if (!hasUpdateLayoutPermission && !publishToLive) {
					throw new PrincipalException();
				}
			}
			else {
				checkPermission(permissionChecker, group, layout, selPlid);
			}
		}
		else if (cmd.equals("reset_customized_view")) {
			if (!LayoutPermissionUtil.contains(
					permissionChecker, layout, ActionKeys.CUSTOMIZE)) {

				throw new PrincipalException();
			}
		}
		else {
			checkPermission(permissionChecker, group, layout, selPlid);
		}
	}

	protected Group getGroup(PortletRequest portletRequest) throws Exception {
		return ActionUtil.getGroup(portletRequest);
	}

}