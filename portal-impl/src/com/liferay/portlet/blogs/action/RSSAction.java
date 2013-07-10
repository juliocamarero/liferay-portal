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

package com.liferay.portlet.blogs.action;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.rss.CompanyRSSRenderer;
import com.liferay.portlet.blogs.rss.GroupRSSRenderer;
import com.liferay.portlet.blogs.rss.OrganizationRSSRenderer;
import com.liferay.portlet.blogs.service.BlogsEntryServiceUtil;
import com.liferay.portlet.rss.RSSRenderer;
import com.liferay.portlet.rss.action.DefaultRSSAction;
import com.liferay.util.RSSUtil;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;

import edu.emory.mathcs.backport.java.util.Collections;

import java.util.Date;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 */
public class RSSAction extends DefaultRSSAction {

	@Override
	public RSSRenderer createRSSRenderer(HttpServletRequest request) 
		throws Exception {
		
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		long companyId = ParamUtil.getLong(request, "companyId");
		long groupId = ParamUtil.getLong(request, "groupId");
		long organizationId = ParamUtil.getLong(request, "organizationId");
		int status = WorkflowConstants.STATUS_APPROVED;
		int max = ParamUtil.getInteger(
			request, "max", SearchContainer.DEFAULT_DELTA);
		
		List<BlogsEntry> blogsEntries = Collections.emptyList();
		
		if (companyId > 0) {
			blogsEntries =
				BlogsEntryServiceUtil.getCompanyEntries(
					companyId, new Date(), status, max);
			return new CompanyRSSRenderer(
				CompanyLocalServiceUtil.getCompany(companyId), 
				blogsEntries, request);
			
		}
		else if (groupId > 0) {
			
			blogsEntries = BlogsEntryServiceUtil.getGroupEntries(
				groupId, new Date(), status, max);
			
			return new GroupRSSRenderer(
				GroupLocalServiceUtil.getGroup(groupId), blogsEntries, request);
		}
		
		else if (organizationId > 0) {
			blogsEntries = BlogsEntryServiceUtil.getOrganizationEntries(
				organizationId, new Date(), status, max);
			return new OrganizationRSSRenderer(
				OrganizationLocalServiceUtil.getOrganization(organizationId),
				blogsEntries, request);
		}
		else if (layout != null) {
			groupId = themeDisplay.getScopeGroupId();

			blogsEntries = BlogsEntryServiceUtil.getGroupEntries(
				groupId, new Date(), status, max);
			return new GroupRSSRenderer(
				GroupLocalServiceUtil.getGroup(groupId), blogsEntries, request, 
				true);
		}
		
		throw new UnsupportedOperationException();
	}
	
}