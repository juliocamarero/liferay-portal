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

package com.liferay.portlet.portletdisplaytemplate.util;

import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.PipingServletResponse;
import com.liferay.portal.kernel.staging.StagingConstants;
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateContextType;
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.dynamicdatamapping.NoSuchTemplateException;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.taglib.util.VelocityTaglib;

import java.io.Writer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * @author Eduardo Garcia
 * @author Juan Fernández
 * @author Brian Wing Shun Chan
 */
public class PortletDisplayTemplateImpl implements PortletDisplayTemplate {

	public DDMTemplate fetchDDMTemplate(long groupId, String displayStyle) {
		try {
			Group group = GroupLocalServiceUtil.getGroup(groupId);

			Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(
				group.getCompanyId());

			if (!displayStyle.startsWith("ddmTemplate_")) {
				return null;
			}

			String uuid = displayStyle.substring(12);

			if (Validator.isNull(uuid)) {
				return null;
			}

			try {
				return
					DDMTemplateLocalServiceUtil.getDDMTemplateByUuidAndGroupId(
						uuid, groupId);
			}
			catch (NoSuchTemplateException nste) {
			}

			try {
				return
					DDMTemplateLocalServiceUtil.getDDMTemplateByUuidAndGroupId(
						uuid, companyGroup.getGroupId());
			}
			catch (NoSuchTemplateException nste) {
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		return null;
	}

	public long getDDMTemplateGroupId(ThemeDisplay themeDisplay) {
		try {
			Group scopeGroup = themeDisplay.getScopeGroup();

			if (scopeGroup.hasStagingGroup()) {
				Group stagingGroup = GroupLocalServiceUtil.getStagingGroup(
					scopeGroup.getGroupId());

				if (GetterUtil.getBoolean(
						scopeGroup.getTypeSettingsProperty(
							StagingConstants.STAGED_PORTLET +
								PortletKeys.PORTLET_DISPLAY_TEMPLATES))) {

					return stagingGroup.getGroupId();
				}
			}
			else if (scopeGroup.getLiveGroupId() > 0) {
				Group liveGroup = scopeGroup.getLiveGroup();

				if (!GetterUtil.getBoolean(
						liveGroup.getTypeSettingsProperty(
							StagingConstants.STAGED_PORTLET +
								PortletKeys.PORTLET_DISPLAY_TEMPLATES))) {

					return liveGroup.getGroupId();
				}
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		return themeDisplay.getScopeGroupId();
	}

	public long getPortletDisplayTemplateDDMTemplateId(
		ThemeDisplay themeDisplay, String displayStyle) {

		long portletDisplayDDMTemplateId = 0;

		long portletDisplayDDMTemplateGroupId = getDDMTemplateGroupId(
			themeDisplay);

		if (displayStyle.startsWith("ddmTemplate_")) {
			DDMTemplate portletDisplayDDMTemplate = fetchDDMTemplate(
				portletDisplayDDMTemplateGroupId, displayStyle);

			if (portletDisplayDDMTemplate != null) {
				portletDisplayDDMTemplateId =
					portletDisplayDDMTemplate.getTemplateId();
			}
		}

		return portletDisplayDDMTemplateId;
	}

	public String renderDDMTemplate(
			PageContext pageContext, long ddmTemplateId, List<?> entries)
		throws Exception {

		Map<String, Object> contextObjects = new HashMap<String, Object>();

		return renderDDMTemplate(
			pageContext, ddmTemplateId, entries, contextObjects, true);
	}

	public String renderDDMTemplate(
			PageContext pageContext, long ddmTemplateId, List<?> entries,
			Map<String, Object> contextObjects)
		throws Exception {

		return renderDDMTemplate(
			pageContext, ddmTemplateId, entries, contextObjects, true);
	}

	/**
	 * @see com.liferay.taglib.util.ThemeUtil#includeVM
	 */
	public String renderDDMTemplate(
			PageContext pageContext, long ddmTemplateId, List<?> entries,
			Map<String, Object> contextObjects, boolean write)
		throws Exception {

		HttpServletRequest request =
			(HttpServletRequest)pageContext.getRequest();

		DDMTemplate ddmTemplate = DDMTemplateLocalServiceUtil.getTemplate(
			ddmTemplateId);

		TemplateResource templateResource = new StringTemplateResource(
			ddmTemplate.getTemplateKey(), ddmTemplate.getScript());

		Template template = TemplateManagerUtil.getTemplate(
			TemplateManager.VELOCITY, templateResource,
			TemplateContextType.STANDARD);

		// Velocity variables

		template.prepare(request);

		// Page context

		template.put("pageContext", pageContext);

		// Tag libraries

		HttpServletResponse response =
			(HttpServletResponse)pageContext.getResponse();

		ServletContext servletContext =
			request.getSession().getServletContext();

		Writer writer = null;

		if (write) {
			writer = pageContext.getOut();
		}
		else {
			writer = new UnsyncStringWriter();
		}

		VelocityTaglib velocityTaglib = new VelocityTaglib(
			servletContext, request,
			new PipingServletResponse(response, writer), pageContext);

		template.put(PortletDisplayTemplateConstants.THEME, velocityTaglib);
		template.put(
			PortletDisplayTemplateConstants.VELOCITY_TAGLIB, velocityTaglib);
		template.put(PortletDisplayTemplateConstants.WRITER, writer);

		// Display template data

		template.put(
			PortletDisplayTemplateConstants.DDM_TEMPLATE_ID, ddmTemplateId);
		template.put(PortletDisplayTemplateConstants.ENTRIES, entries);

		if (entries.size() == 1) {
			template.put(
				PortletDisplayTemplateConstants.ENTRY, entries.get(0));
		}

		// Other data

		if (contextObjects != null) {

			contextObjects.putAll(_getPortletPreferences(request));

			for (String key : contextObjects.keySet()) {
				template.put(key, contextObjects.get(key));
			}
		}

		// Merge templates

		template.processTemplate(writer);

		if (write) {
			return StringPool.BLANK;
		}
		else {
			return ((UnsyncStringWriter)writer).toString();
		}
	}

	private static Map<String, Object> _getPortletPreferences(
		HttpServletRequest request) {

		PortletRequest portletRequest =
			(PortletRequest)request.getAttribute(
				JavaConstants.JAVAX_PORTLET_REQUEST);

		Map<String, Object> contextObjects = new HashMap<String, Object>();

		PortletPreferences portletPreferences = portletRequest.getPreferences();

		Map<String, String[]> map = portletPreferences.getMap();

		for (Map.Entry<String, String[]> entry : map.entrySet()) {
			String[] values = entry.getValue();

			if ((values == null) || (values.length == 0)) {
				continue;
			}

			String value = values[0];

			if (value == null) {
				continue;
			}

			contextObjects.put(entry.getKey(), value);
		}

		return contextObjects;
	}

	private static Log _log = LogFactoryUtil.getLog(
		PortletDisplayTemplateImpl.class);

}