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

package com.liferay.site.item.selector.web.display.context;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.GroupServiceUtil;
import com.liferay.portal.service.permission.GroupPermissionUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.usersadmin.search.GroupSearch;
import com.liferay.portlet.usersadmin.search.GroupSearchTerms;
import com.liferay.site.item.selector.criterion.SiteItemSelectorCriterion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.portlet.PortletRequest;

import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Julio Camarero
 */
public class AllSitesItemSelectorViewDisplayContext
	extends BaseSitesItemSelectorViewDisplayContext {

	public AllSitesItemSelectorViewDisplayContext(
		HttpServletRequest request,
		SiteItemSelectorCriterion siteItemSelectorCriterion,
		String itemSelectedEventName, PortletURL portletURL) {

		super(
			request, siteItemSelectorCriterion, itemSelectedEventName,
			portletURL);

		_portletRequest = getPortletRequest();
	}

	@Override
	public GroupSearch getGroupSearch() throws PortalException {
		ThemeDisplay themeDisplay = (ThemeDisplay)_portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		GroupSearch groupSearch = new GroupSearch(
			_portletRequest, portletURL);

		GroupSearchTerms searchTerms =
			(GroupSearchTerms)groupSearch.getSearchTerms();

		long parentGroupId = getParentGroupId();

		Company company = themeDisplay.getCompany();

		List results = null;

		if (!searchTerms.hasSearchTerms() && isFilterManageableGroups() &&
			(parentGroupId <= 0)) {

			int total = getAllGroups().size();

			groupSearch.setTotal(total);

			results = ListUtil.subList(
				getAllGroups(), groupSearch.getStart(), groupSearch.getEnd());
		}
		else if (searchTerms.hasSearchTerms()) {
			int total = GroupLocalServiceUtil.searchCount(
				company.getCompanyId(), _classNameIds,
				searchTerms.getKeywords(),
				getGroupParams(themeDisplay, searchTerms, parentGroupId));

			groupSearch.setTotal(total);

			results = GroupLocalServiceUtil.search(
				company.getCompanyId(), _classNameIds,
				searchTerms.getKeywords(),
				getGroupParams(themeDisplay, searchTerms, parentGroupId),
				groupSearch.getStart(), groupSearch.getEnd(),
				groupSearch.getOrderByComparator());
		}
		else {
			int total = GroupLocalServiceUtil.searchCount(
				company.getCompanyId(), _classNameIds, getGroupId(),
				searchTerms.getKeywords(),
				getGroupParams(themeDisplay, searchTerms, parentGroupId));

			groupSearch.setTotal(total);

			results = GroupLocalServiceUtil.search(
				company.getCompanyId(), _classNameIds, getGroupId(),
				searchTerms.getKeywords(),
				getGroupParams(themeDisplay, searchTerms, parentGroupId),
				groupSearch.getStart(), groupSearch.getEnd(),
				groupSearch.getOrderByComparator());
		}

		groupSearch.setResults(results);

		return groupSearch;
	}

	protected List<Group> getAllGroups() throws PortalException {
		List<Group> groups = new ArrayList<>();

		ThemeDisplay themeDisplay = (ThemeDisplay)_portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (isFilterManageableGroups()) {
			User user = themeDisplay.getUser();

			groups = user.getSiteGroups(true);
		}

		if (getGroupId() != GroupConstants.DEFAULT_PARENT_GROUP_ID) {
			groups.clear();

			groups.add(GroupLocalServiceUtil.getGroup(getGroupId()));
		}

		return groups;
	}

	protected Group getGroup() throws PortalException {
		long groupId = getGroupId();

		if (groupId > 0) {
			_group = GroupServiceUtil.getGroup(groupId);
		}

		return _group;
	}

	protected long getGroupId() {
		if (_groupId <= 0) {
			_groupId = ParamUtil.getLong(
				_portletRequest, "groupId",
				GroupConstants.DEFAULT_PARENT_GROUP_ID);
		}

		return _groupId;
	}

	protected LinkedHashMap<String, Object> getGroupParams(
			ThemeDisplay themeDisplay, GroupSearchTerms searchTerms,
			long parentGroupId)
		throws PortalException {

		LinkedHashMap<String, Object> groupParams = new LinkedHashMap<>();

		groupParams.put("site", Boolean.TRUE);

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		User user = themeDisplay.getUser();

		if (searchTerms.hasSearchTerms()) {
			if (isFilterManageableGroups()) {
				groupParams.put("groupsTree", getAllGroups());
			}
			else if (parentGroupId > 0) {
				List<Group> groupsTree = new ArrayList<>();

				Group parentGroup = GroupLocalServiceUtil.getGroup(
					parentGroupId);

				groupsTree.add(parentGroup);

				groupParams.put("groupsTree", groupsTree);
			}

			if (!permissionChecker.isCompanyAdmin() &&
				!GroupPermissionUtil.contains(
					permissionChecker, ActionKeys.VIEW)) {

				groupParams.put("usersGroups", Long.valueOf(user.getUserId()));
			}
		}

		return groupParams;
	}

	protected long getParentGroupId() throws PortalException {
		Group group = getGroup();

		if (group != null) {
			return group.getGroupId();
		}

		if (isFilterManageableGroups()) {
			return GroupConstants.ANY_PARENT_GROUP_ID;
		}

		return GroupConstants.DEFAULT_PARENT_GROUP_ID;
	}

	protected boolean isFilterManageableGroups() {
		ThemeDisplay themeDisplay = (ThemeDisplay)_portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		if (permissionChecker.isCompanyAdmin()) {
			return false;
		}

		if (GroupPermissionUtil.contains(permissionChecker, ActionKeys.VIEW)) {
			return false;
		}

		return true;
	}

	private static final long[] _classNameIds = new long[] {
		PortalUtil.getClassNameId(Company.class),
		PortalUtil.getClassNameId(Group.class),
		PortalUtil.getClassNameId(Organization.class)
	};

	private Group _group;
	private long _groupId;
	private final PortletRequest _portletRequest;

}