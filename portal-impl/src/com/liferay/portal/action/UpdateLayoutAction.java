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

package com.liferay.portal.action;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.servlet.BufferCacheServletResponse;
import com.liferay.portal.kernel.servlet.DynamicServletRequest;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.staging.StagingUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.InstancePool;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutRevision;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.LayoutRevisionLocalServiceUtil;
import com.liferay.portal.service.LayoutServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.servlet.NamespaceServletRequest;
import com.liferay.portal.struts.JSONAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.LayoutClone;
import com.liferay.portal.util.LayoutCloneFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletPreferences;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Brian Wing Shun Chan
 */
public class UpdateLayoutAction extends JSONAction {

	@Override
	public String getJSON(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		long userId = themeDisplay.getUserId();

		Layout layout = themeDisplay.getLayout();
		LayoutTypePortlet layoutTypePortlet =
			themeDisplay.getLayoutTypePortlet();

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		String cmd = ParamUtil.getString(request, Constants.CMD);

		String portletId = ParamUtil.getString(request, "p_p_id");

		boolean updateLayout = true;

		if (cmd.equals(Constants.ADD)) {
			String columnId = ParamUtil.getString(request, "p_p_col_id", null);
			int columnPos = ParamUtil.getInteger(request, "p_p_col_pos", -1);

			portletId = layoutTypePortlet.addPortletId(
				userId, portletId, columnId, columnPos);

			if (layoutTypePortlet.isCustomizable() &&
				layoutTypePortlet.isCustomizedView() &&
				!layoutTypePortlet.isColumnDisabled(columnId)) {

				updateLayout = false;
			}
		}
		else if (cmd.equals(Constants.DELETE)) {
			if (layoutTypePortlet.hasPortletId(portletId)) {
				layoutTypePortlet.removePortletId(userId, portletId);

				if (layoutTypePortlet.isCustomizable() &&
					layoutTypePortlet.isCustomizedView()) {

					updateLayout = false;
				}
			}
		}
		else if (cmd.equals("drag")) {
			if (LayoutPermissionUtil.contains(
					permissionChecker, layout, ActionKeys.UPDATE)) {

				String height = ParamUtil.getString(request, "height");
				String width = ParamUtil.getString(request, "width");
				String top = ParamUtil.getString(request, "top");
				String left = ParamUtil.getString(request, "left");

				PortletPreferences preferences =
					PortletPreferencesFactoryUtil.getLayoutPortletSetup(
						layout, portletId);

				StringBundler sb = new StringBundler(12);

				sb.append("height=");
				sb.append(height);
				sb.append("\n");
				sb.append("width=");
				sb.append(width);
				sb.append("\n");
				sb.append("top=");
				sb.append(top);
				sb.append("\n");
				sb.append("left=");
				sb.append(left);
				sb.append("\n");

				preferences.setValue("portlet-freeform-styles", sb.toString());

				preferences.store();
			}
		}
		else if (cmd.equals("minimize")) {
			boolean restore = ParamUtil.getBoolean(request, "p_p_restore");

			if (restore) {
				layoutTypePortlet.removeStateMinPortletId(portletId);
			}
			else {
				layoutTypePortlet.addStateMinPortletId(portletId);
			}

			updateLayout = false;
		}
		else if (cmd.equals("move")) {
			String columnId = ParamUtil.getString(request, "p_p_col_id");
			int columnPos = ParamUtil.getInteger(request, "p_p_col_pos");

			layoutTypePortlet.movePortletId(
				userId, portletId, columnId, columnPos);

			if (layoutTypePortlet.isCustomizable() &&
				layoutTypePortlet.isCustomizedView() &&
				!layoutTypePortlet.isColumnDisabled(columnId)) {

				updateLayout = false;
			}
		}
		else if (cmd.equals("redo_layout_revision")) {
			long layoutRevisionId = ParamUtil.getLong(
				request, "layoutRevisionId");
			long layoutSetBranchId = ParamUtil.getLong(
				request, "layoutSetBranchId");

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				request);

			LayoutRevisionLocalServiceUtil.updateStatus(
				userId, layoutRevisionId, WorkflowConstants.STATUS_DRAFT,
				serviceContext);

			StagingUtil.setRecentLayoutRevisionId(
				request, layoutSetBranchId, layout.getPlid(), layoutRevisionId);

			updateLayout = false;
		}
		else if (cmd.equals("select_layout_revision")) {
			long layoutRevisionId = ParamUtil.getLong(
				request, "layoutRevisionId");
			long layoutSetBranchId = ParamUtil.getLong(
				request, "layoutSetBranchId");

			StagingUtil.setRecentLayoutRevisionId(
				request, layoutSetBranchId, layout.getPlid(), layoutRevisionId);

			updateLayout = false;
		}
		else if (cmd.equals("toggle_customized_view")) {
			updateLayout = false;
		}
		else if (cmd.equals("update_type_settings")) {
			UnicodeProperties layoutTypeSettingsProperties =
				layout.getTypeSettingsProperties();

			UnicodeProperties formTypeSettingsProperties =
				PropertiesParamUtil.getProperties(
					request, "TypeSettingsProperties--");

			layoutTypeSettingsProperties.putAll(formTypeSettingsProperties);
		}
		else if (cmd.equals("undo_layout_revision")) {
			long layoutRevisionId = ParamUtil.getLong(
				request, "layoutRevisionId");
			long layoutSetBranchId = ParamUtil.getLong(
				request, "layoutSetBranchId");

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				request);

			LayoutRevision layoutRevision =
				LayoutRevisionLocalServiceUtil.updateStatus(
					userId, layoutRevisionId, WorkflowConstants.STATUS_INACTIVE,
					serviceContext);

			StagingUtil.setRecentLayoutRevisionId(
				request, layoutSetBranchId, layout.getPlid(),
				layoutRevision.getParentLayoutRevisionId());

			updateLayout = false;
		}

		if (updateLayout) {

			// LEP-3648

			layoutTypePortlet.resetModes();
			layoutTypePortlet.resetStates();

			layout = LayoutServiceUtil.updateLayout(
				layout.getGroupId(), layout.isPrivateLayout(),
				layout.getLayoutId(), layout.getTypeSettings());
		}
		else {
			LayoutClone layoutClone = LayoutCloneFactory.getInstance();

			if (layoutClone != null) {
				layoutClone.update(
					request, layout.getPlid(), layout.getTypeSettings());
			}
		}

		if (cmd.equals(Constants.ADD) && (portletId != null)) {
			addPortlet(mapping, form, request, response, portletId);
		}

		return StringPool.BLANK;
	}

	protected void addPortlet(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response, String portletId)
		throws Exception {

		// Run the render portlet action to add a portlet without refreshing.

		Action renderPortletAction = (Action)InstancePool.get(
			RenderPortletAction.class.getName());

		// Pass in the portlet id because the portlet id may be the instance id.
		// Namespace the request if necessary. See LEP-4644.

		long companyId = PortalUtil.getCompanyId(request);

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			companyId, portletId);

		DynamicServletRequest dynamicRequest = null;

		if (portlet.isPrivateRequestAttributes()) {
			String portletNamespace = PortalUtil.getPortletNamespace(
				portlet.getPortletId());

			dynamicRequest = new NamespaceServletRequest(
				request, portletNamespace, portletNamespace);
		}
		else {
			dynamicRequest = new DynamicServletRequest(request);
		}

		dynamicRequest.setParameter("p_p_id", portletId);

		String dataType = ParamUtil.getString(request, "dataType");

		if (dataType.equals("json")) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			BufferCacheServletResponse bufferCacheServletResponse =
				new BufferCacheServletResponse(response);

			renderPortletAction.execute(
				mapping, form, dynamicRequest, bufferCacheServletResponse);

			String portletHTML = bufferCacheServletResponse.getString();

			portletHTML = portletHTML.trim();

			populatePortletJSONObject(
				request, portletHTML, portlet, jsonObject);

			response.setContentType(ContentTypes.TEXT_JAVASCRIPT);

			ServletResponseUtil.write(response, jsonObject.toString());
		}
		else {
			renderPortletAction.execute(
				mapping, form, dynamicRequest, response);
		}
	}

	protected String getRootPortletId(Portlet portlet) {

		// Workaround for portlet.getRootPortletId() because that does not
		// return the proper root portlet ID for OpenSocial and WSRP portlets

		Portlet rootPortlet = portlet.getRootPortlet();

		return rootPortlet.getPortletId();
	}

	protected void populatePortletJSONObject(
			HttpServletRequest request, String portletHTML, Portlet portlet,
			JSONObject jsonObject)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		LayoutTypePortlet layoutTypePortlet =
			themeDisplay.getLayoutTypePortlet();

		jsonObject.put("portletHTML", portletHTML);
		jsonObject.put("refresh", !portlet.isAjaxable());

		Set<String> footerCssSet = new LinkedHashSet<String>();
		Set<String> footerJavaScriptSet = new LinkedHashSet<String>();
		Set<String> headerCssSet = new LinkedHashSet<String>();
		Set<String> headerJavaScriptSet = new LinkedHashSet<String>();

		boolean portletOnLayout = false;

		String rootPortletId = getRootPortletId(portlet);
		String portletId = portlet.getPortletId();

		for (Portlet layoutPortlet : layoutTypePortlet.getAllPortlets()) {

			// Check to see if an instance of this portlet is already in the
			// layout, but ignore the portlet that was just added

			String layoutPortletRootPortletId = getRootPortletId(layoutPortlet);

			if (rootPortletId.equals(layoutPortletRootPortletId) &&
				!portletId.equals(layoutPortlet.getPortletId())) {

				portletOnLayout = true;

				break;
			}
		}

		if (!portletOnLayout && portlet.isAjaxable()) {
			List<String> footerPortalCsss =
				PortalUtil.getFooterPortalCssProcessed(portlet, request);

			for (String curFooterPortalCss : footerPortalCsss) {
				footerCssSet.add(curFooterPortalCss);
			}

			List<String> footerPortalJavaScripts =
				PortalUtil.getFooterPortalJavascriptProcessed(portlet, request);

			for (String curFooterPortalJavaScript : footerPortalJavaScripts) {
				footerJavaScriptSet.add(curFooterPortalJavaScript);
			}

			List<String> footerPortletCsss =
				PortalUtil.getFooterPortletCssProcessed(portlet, request);

			for (String curFooterPortletCss : footerPortletCsss) {
				footerCssSet.add(curFooterPortletCss);
			}

			List<String> footerPortletJavaScripts =
				PortalUtil.getFooterPortletJavascriptProcessed(
					portlet, request);

			for (String curFooterPortletJavaScript : footerPortletJavaScripts) {
				footerJavaScriptSet.add(curFooterPortletJavaScript);
			}

			List<String> headerPortalCsss =
				PortalUtil.getHeaderPortalCssProcessed(portlet, request);

			for (String curHeaderPortalCss : headerPortalCsss) {
				headerCssSet.add(curHeaderPortalCss);
			}

			List<String> headerPortalJavaScripts =
				PortalUtil.getHeaderPortalJavascriptProcessed(portlet, request);

			for (String curHeaderPortalJavaScript : headerPortalJavaScripts) {
				headerJavaScriptSet.add(curHeaderPortalJavaScript);
			}

			List<String> headerPortletCsss =
				PortalUtil.getHeaderPortletCssProcessed(portlet, request);

			for (String curHeaderPortletCss : headerPortletCsss) {
				headerCssSet.add(curHeaderPortletCss);
			}

			List<String> headerPortletJavaScripts =
				PortalUtil.getHeaderPortletJavascriptProcessed(
					portlet, request);

			for (String curHeaderPortletJavaScript : headerPortletJavaScripts) {
				headerJavaScriptSet.add(curHeaderPortletJavaScript);
			}
		}

		String footerCssPaths = JSONFactoryUtil.serialize(
			footerCssSet.toArray(new String[footerCssSet.size()]));

		jsonObject.put(
			"footerCssPaths", JSONFactoryUtil.createJSONArray(footerCssPaths));

		String footerJavaScriptPaths = JSONFactoryUtil.serialize(
			footerJavaScriptSet.toArray(
				new String[footerJavaScriptSet.size()]));

		jsonObject.put(
			"footerJavaScriptPaths",
			JSONFactoryUtil.createJSONArray(footerJavaScriptPaths));

		String headerCssPaths = JSONFactoryUtil.serialize(
			headerCssSet.toArray(new String[headerCssSet.size()]));

		jsonObject.put(
			"headerCssPaths", JSONFactoryUtil.createJSONArray(headerCssPaths));

		String headerJavaScriptPaths = JSONFactoryUtil.serialize(
			headerJavaScriptSet.toArray(
				new String[headerJavaScriptSet.size()]));

		jsonObject.put(
			"headerJavaScriptPaths",
			JSONFactoryUtil.createJSONArray(headerJavaScriptPaths));
	}

}