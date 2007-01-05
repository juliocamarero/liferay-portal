/**
 * Copyright (c) 2000-2006 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portal.service.impl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.DuplicateGroupException;
import com.liferay.portal.GroupFriendlyURLException;
import com.liferay.portal.GroupNameException;
import com.liferay.portal.NoSuchGroupException;
import com.liferay.portal.NoSuchRoleException;
import com.liferay.portal.PortalException;
import com.liferay.portal.RequiredGroupException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Resource;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.model.impl.GroupImpl;
import com.liferay.portal.model.impl.LayoutImpl;
import com.liferay.portal.model.impl.ResourceImpl;
import com.liferay.portal.model.impl.UserImpl;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.persistence.GroupFinder;
import com.liferay.portal.service.persistence.GroupUtil;
import com.liferay.portal.service.persistence.ResourceUtil;
import com.liferay.portal.service.persistence.RoleUtil;
import com.liferay.portal.service.persistence.UserUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.BookmarksFolderLocalServiceUtil;
import com.liferay.portlet.calendar.service.CalEventLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;
import com.liferay.portlet.imagegallery.service.IGFolderLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBCategoryLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBStatsUserLocalServiceUtil;
import com.liferay.portlet.polls.service.PollsQuestionLocalServiceUtil;
import com.liferay.portlet.shopping.service.ShoppingCartLocalServiceUtil;
import com.liferay.portlet.wiki.service.WikiNodeLocalServiceUtil;
import com.liferay.util.StringUtil;
import com.liferay.util.Validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <a href="GroupLocalServiceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class GroupLocalServiceImpl implements GroupLocalService {

	public Group addGroup(
			String userId, String className, String classPK, String name,
			String description, String type, String friendlyURL)
		throws PortalException, SystemException {

		// Group

		User user = UserUtil.findByPrimaryKey(userId);

		if (Validator.isNull(className) || Validator.isNull(classPK)) {
			validateName(null, user.getActualCompanyId(), name);
		}

		validateFriendlyURL(null, user.getActualCompanyId(), friendlyURL);

		String groupId = Long.toString(CounterLocalServiceUtil.increment(
			Group.class.getName()));

		if (Validator.isNotNull(className) && Validator.isNotNull(classPK)) {
			name = groupId;
		}

		Group group = GroupUtil.create(groupId);

		group.setCompanyId(user.getActualCompanyId());
		group.setClassName(className);
		group.setClassPK(classPK);
		group.setParentGroupId(GroupImpl.DEFAULT_PARENT_GROUP_ID);
		group.setName(name);
		group.setDescription(description);
		group.setType(type);
		group.setFriendlyURL(friendlyURL);

		GroupUtil.update(group);

		// Layout sets

		LayoutSetLocalServiceUtil.addLayoutSet(
			LayoutImpl.PRIVATE + groupId, group.getCompanyId());

		LayoutSetLocalServiceUtil.addLayoutSet(
			LayoutImpl.PUBLIC + groupId, group.getCompanyId());

		if (Validator.isNull(className) && Validator.isNull(classPK) &&
			!UserImpl.isDefaultUser(userId)) {

			// Resources

			ResourceLocalServiceUtil.addResources(
				group.getCompanyId(), null, userId, Group.class.getName(),
				group.getPrimaryKey().toString(), false, false, false);

			// User

			UserLocalServiceUtil.addGroupUsers(
				group.getGroupId(), new String[] {userId});
		}

		return group;
	}

	public void addRoleGroups(String roleId, String[] groupIds)
		throws PortalException, SystemException {

		RoleUtil.addGroups(roleId, groupIds);
	}

	public void checkSystemGroups(String companyId)
		throws PortalException, SystemException {

		String[] systemGroups = PortalUtil.getSystemGroups();

		for (int i = 0; i < systemGroups.length; i++) {
			Group group = null;

			try {
				group = GroupFinder.findByC_N(companyId, systemGroups[i]);
			}
			catch (NoSuchGroupException nsge) {
				group = addGroup(
					UserImpl.getDefaultUserId(companyId), null, null,
					systemGroups[i], null, null, null);
			}

			if (group.getName().equals(GroupImpl.GUEST)) {
				LayoutSet layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(
					LayoutImpl.PUBLIC + group.getGroupId());

				if (layoutSet.getPageCount() == 0) {
					addDefaultLayouts(group);
				}
			}
		}
	}

	public void deleteGroup(String groupId)
		throws PortalException, SystemException {

		Group group = GroupUtil.findByPrimaryKey(groupId);

		if (PortalUtil.isSystemGroup(group.getName())) {
			throw new RequiredGroupException();
		}

		// Layout sets

		LayoutSetLocalServiceUtil.deleteLayoutSet(LayoutImpl.PRIVATE + groupId);
		LayoutSetLocalServiceUtil.deleteLayoutSet(LayoutImpl.PUBLIC + groupId);

		// Role

		try {
			Role role = RoleLocalServiceUtil.getGroupRole(
				group.getCompanyId(), groupId);

			RoleLocalServiceUtil.deleteRole(role.getRoleId());
		}
		catch (NoSuchRoleException nsre) {
		}

		// Blogs

		BlogsEntryLocalServiceUtil.deleteEntries(groupId);

		// Bookmarks

		BookmarksFolderLocalServiceUtil.deleteFolders(groupId);

		// Calendar

		CalEventLocalServiceUtil.deleteEvents(groupId);

		// Document library

		DLFolderLocalServiceUtil.deleteFolders(groupId);

		// Image gallery

		IGFolderLocalServiceUtil.deleteFolders(groupId);

		// Journal

		JournalArticleLocalServiceUtil.deleteArticles(groupId);

		// Message boards

		MBCategoryLocalServiceUtil.deleteCategories(groupId);
		MBStatsUserLocalServiceUtil.deleteStatsUserByGroupId(groupId);

		// Polls

		PollsQuestionLocalServiceUtil.deleteQuestions(groupId);

		// Shopping

		ShoppingCartLocalServiceUtil.deleteGroupCarts(groupId);

		// Wiki

		WikiNodeLocalServiceUtil.deleteNodes(groupId);

		// Resources

		Iterator itr = ResourceUtil.findByC_T_S_P(
			group.getCompanyId(), ResourceImpl.TYPE_CLASS,
			ResourceImpl.SCOPE_GROUP, groupId).iterator();

		while (itr.hasNext()) {
			Resource resource = (Resource)itr.next();

			ResourceLocalServiceUtil.deleteResource(resource);
		}

		if (Validator.isNull(group.getClassName()) &&
			Validator.isNull(group.getClassPK())) {

			ResourceLocalServiceUtil.deleteResource(
				group.getCompanyId(), Group.class.getName(),
				ResourceImpl.TYPE_CLASS, ResourceImpl.SCOPE_INDIVIDUAL,
				group.getPrimaryKey().toString());
		}

		// Group

		GroupUtil.remove(groupId);
	}

	public Group getFriendlyURLGroup(String companyId, String friendlyURL)
		throws PortalException, SystemException {

		if (Validator.isNull(friendlyURL)) {
			throw new NoSuchGroupException();
		}

		return GroupUtil.findByC_F(companyId, friendlyURL);
	}

	public Group getGroup(String groupId)
		throws PortalException, SystemException {

		return GroupUtil.findByPrimaryKey(groupId);
	}

	public Group getGroup(String companyId, String name)
		throws PortalException, SystemException {

		return GroupFinder.findByC_N(companyId, name);
	}

	public Group getOrganizationGroup(String companyId, String organizationId)
		throws PortalException, SystemException {

		return GroupUtil.findByC_C_C(
			companyId, Organization.class.getName(), organizationId);
	}

	public List getOrganizationsGroups(List organizations)
		throws PortalException, SystemException {

		List organizationGroups = new ArrayList();

		for (int i = 0; i < organizations.size(); i++) {
			Organization organization = (Organization)organizations.get(i);

			Group group = organization.getGroup();

			organizationGroups.add(group);
		}

		return organizationGroups;
	}

	public List getRoleGroups(String roleId)
		throws PortalException, SystemException {

		return RoleUtil.getGroups(roleId);
	}

	public Group getUserGroup(String companyId, String userId)
		throws PortalException, SystemException {

		return GroupUtil.findByC_C_C(
			companyId, User.class.getName(), userId);
	}

	public Group getUserGroupGroup(String companyId, String userGroupId)
		throws PortalException, SystemException {

		return GroupUtil.findByC_C_C(
			companyId, UserGroup.class.getName(), userGroupId);
	}

	public List getUserGroupsGroups(List userGroups)
		throws PortalException, SystemException {

		List userGroupGroups = new ArrayList();

		for (int i = 0; i < userGroups.size(); i++) {
			UserGroup userGroup = (UserGroup)userGroups.get(i);

			Group group = userGroup.getGroup();

			userGroupGroups.add(group);
		}

		return userGroupGroups;
	}

	public boolean hasRoleGroup(String roleId, String groupId)
		throws PortalException, SystemException {

		return RoleUtil.containsGroup(roleId, groupId);
	}

	public boolean hasUserGroup(String userId, String groupId)
		throws SystemException {

		if (GroupFinder.countByG_U(groupId, userId) > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public List search(
			String companyId, String name, String description, Map params,
			int begin, int end)
		throws SystemException {

		return GroupFinder.findByC_N_D(
			companyId, name, description, params, begin, end);
	}

	public int searchCount(
			String companyId, String name, String description, Map params)
		throws SystemException {

		return GroupFinder.countByC_N_D(companyId, name, description, params);
	}

	public void setRoleGroups(String roleId, String[] groupIds)
		throws PortalException, SystemException {

		RoleUtil.setGroups(roleId, groupIds);
	}

	public void unsetRoleGroups(String roleId, String[] groupIds)
		throws PortalException, SystemException {

		RoleUtil.removeGroups(roleId, groupIds);
	}

	public Group updateGroup(
			String groupId, String name, String description, String type,
			String friendlyURL)
		throws PortalException, SystemException {

		Group group = GroupUtil.findByPrimaryKey(groupId);

		String className = group.getClassName();
		String classPK = group.getClassPK();

		if (Validator.isNull(className) || Validator.isNull(classPK)) {
			validateName(group.getGroupId(), group.getCompanyId(), name);
		}

		if (PortalUtil.isSystemGroup(group.getName()) &&
			!group.getName().equals(name)) {

			throw new RequiredGroupException();
		}

		validateFriendlyURL(
			group.getGroupId(), group.getCompanyId(), friendlyURL);

		group.setName(name);
		group.setDescription(description);
		group.setType(type);
		group.setFriendlyURL(friendlyURL);

		GroupUtil.update(group);

		return group;
	}

	protected void addDefaultLayouts(Group group)
		throws PortalException, SystemException {

		String userId = UserImpl.getDefaultUserId(group.getCompanyId());
		String name = PropsUtil.get(PropsUtil.DEFAULT_GUEST_LAYOUT_NAME);

		Layout layout = LayoutLocalServiceUtil.addLayout(
			group.getGroupId(), userId, false,
			LayoutImpl.DEFAULT_PARENT_LAYOUT_ID, name, StringPool.BLANK,
			LayoutImpl.TYPE_PORTLET, false, StringPool.BLANK);

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		String layoutTemplateId = PropsUtil.get(
			PropsUtil.DEFAULT_GUEST_LAYOUT_TEMPLATE_ID);

		layoutTypePortlet.setLayoutTemplateId(layoutTemplateId);

		for (int i = 0; i < 10; i++) {
			String columnId = "column-" + i;
			String portletIds = PropsUtil.get(
				PropsUtil.DEFAULT_GUEST_LAYOUT_COLUMN + i);

			layoutTypePortlet.addPortletIds(
				null, StringUtil.split(portletIds), columnId, false);
		}

		LayoutLocalServiceUtil.updateLayout(
			layout.getLayoutId(), layout.getOwnerId(),
			layout.getTypeSettings());
	}

	protected void validateFriendlyURL(
			String groupId, String companyId, String friendlyURL)
		throws PortalException, SystemException {

		if (Validator.isNotNull(friendlyURL)) {
			int exceptionType = LayoutImpl.validateFriendlyURL(friendlyURL);

			if (exceptionType != -1) {
				throw new GroupFriendlyURLException(exceptionType);
			}

			try {
				Group group = GroupUtil.findByC_F(companyId, friendlyURL);

				if ((groupId == null) || !group.getGroupId().equals(groupId)) {
					throw new GroupFriendlyURLException(
						GroupFriendlyURLException.DUPLICATE);
				}
			}
			catch (NoSuchGroupException nsge) {
			}
		}
	}

	protected void validateName(String groupId, String companyId, String name)
		throws PortalException, SystemException {

		if ((Validator.isNull(name)) || (Validator.isNumber(name)) ||
			(name.indexOf(StringPool.COMMA) != -1) ||
			(name.indexOf(StringPool.STAR) != -1)) {

			throw new GroupNameException();
		}

		try {
			Group group = GroupFinder.findByC_N(companyId, name);

			if ((groupId == null) || !group.getGroupId().equals(groupId)) {
				throw new DuplicateGroupException();
			}
		}
		catch (NoSuchGroupException nsge) {
		}
	}

}