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

package com.liferay.portlet.usersadmin.lar;

import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;

import java.util.Calendar;

/**
 * @author Daniel Kocsis
 */
public class UserStagedModelDataHandler
	extends BaseStagedModelDataHandler<User> {

	public static final String[] CLASS_NAMES = {User.class.getName()};

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, User user)
		throws Exception {

		// Contact needs to be exported

		Element userElement = portletDataContext.getExportDataElement(user);

		portletDataContext.addClassedModel(
			userElement, ExportImportPathUtil.getModelPath(user), user,
			UsersAdminPortletDataHandler.NAMESPACE);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, User user)
		throws Exception {

		long userId = portletDataContext.getUserId(user.getUserUuid());

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			user, UsersAdminPortletDataHandler.NAMESPACE);

		// Import contact

		User existingUser =
			UserLocalServiceUtil.getUserByUuidAndCompanyId(
				user.getUuid(), portletDataContext.getCompanyId());

		User importedUser = null;

		Calendar birthdayCal = CalendarFactoryUtil.getCalendar();

		birthdayCal.setTime(user.getBirthday());

		int birthdayMonth = birthdayCal.get(Calendar.MONTH);
		int birthdayDay = birthdayCal.get(Calendar.DAY_OF_MONTH);
		int birthdayYear = birthdayCal.get(Calendar.YEAR);

		if (existingUser == null) {
			serviceContext.setUuid(user.getUuid());

			importedUser = UserLocalServiceUtil.addUser(
				userId, portletDataContext.getCompanyId(), false,
				user.getPasswordUnencrypted(), user.getPasswordUnencrypted(),
				false, user.getScreenName(), user.getEmailAddress(),
				user.getFacebookId(), user.getOpenId(), user.getLocale(),
				user.getFirstName(), user.getMiddleName(), user.getLastName(),
				0, 0, user.getMale(), birthdayMonth, birthdayDay, birthdayYear,
				user.getJobTitle(), user.getGroupIds(),
				user.getOrganizationIds(), user.getRoleIds(),
				user.getUserGroupIds(), true, serviceContext);
		}
		else {
			Contact contact = existingUser.getContact();

			importedUser = UserLocalServiceUtil.updateUser(
				userId, existingUser.getPasswordUnencrypted(),
				user.getPasswordUnencrypted(), user.getPasswordUnencrypted(),
				user.isPasswordReset(), user.getReminderQueryQuestion(),
				user.getReminderQueryAnswer(), user.getScreenName(),
				user.getEmailAddress(), user.getFacebookId(), user.getOpenId(),
				user.getLanguageId(), user.getTimeZoneId(), user.getGreeting(),
				user.getComments(), user.getFirstName(), user.getMiddleName(),
				user.getLastName(), contact.getPrefixId(),
				contact.getSuffixId(), user.getMale(), birthdayMonth,
				birthdayDay, birthdayYear, contact.getSmsSn(),
				contact.getAimSn(), contact.getFacebookSn(), contact.getIcqSn(),
				contact.getJabberSn(), contact.getMsnSn(),
				contact.getMySpaceSn(), contact.getSkypeSn(),
				contact.getTwitterSn(), contact.getYmSn(), user.getJobTitle(),
				user.getGroupIds(), user.getOrganizationIds(),
				user.getRoleIds(), null, user.getUserGroupIds(),
				serviceContext);
		}

		portletDataContext.importClassedModel(
			user, importedUser, UsersAdminPortletDataHandler.NAMESPACE);
	}

	@Override
	protected boolean validateMissingReference(String uuid, long groupId) {
		try {
			Group group = GroupLocalServiceUtil.getGroup(groupId);

			UserLocalServiceUtil.getUserByUuidAndCompanyId(
				uuid, group.getCompanyId());
		}
		catch (Exception e) {
			return false;
		}

		return true;
	}

}