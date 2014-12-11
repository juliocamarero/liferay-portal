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

package com.liferay.portal.model;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.model.impl.UserModelImpl;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.exportimport.UserExporterUtil;
import com.liferay.portal.security.exportimport.UserImportTransactionThreadLocal;
import com.liferay.portal.service.MembershipRequestLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

/**
 * @author Scott Lee
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 * @author Vilmos Papp
 */
public class UserModelListener extends UserCollectionReindexListener<User> {

	public UserModelListener() {
		super(
			SetUtil.fromArray(new String[] {
				Group.class.getName(), Organization.class.getName(),
				Role.class.getName(), Team.class.getName(),
				UserGroup.class.getName()
			}),
			true, false);
	}

	@Override
	public void onAfterAddAssociation(
			Object classPK, String associationClassName,
			Object associationClassPK)
		throws ModelListenerException {

		try {
			if (associationClassName.equals(Group.class.getName())) {
				updateMembershipRequestStatus(
					(Long)classPK, (Long)associationClassPK);
			}

			super.onAfterAddAssociation(
				classPK, associationClassName, associationClassPK);
		}
		catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	@Override
	public void onAfterCreate(User user) throws ModelListenerException {
		try {
			exportToLDAP(user);
		}
		catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	@Override
	public void onAfterUpdate(User user) throws ModelListenerException {
		try {
			exportToLDAP(user);
		}
		catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	@Override
	public void onBeforeUpdate(User user) {
		UserModelImpl userModelImpl = (UserModelImpl)user;

		UserImportTransactionThreadLocal.setOriginalEmailAddress(
			userModelImpl.getOriginalEmailAddress());
	}

	protected void exportToLDAP(User user) throws Exception {
		if (user.isDefaultUser() ||
			UserImportTransactionThreadLocal.isOriginatesFromImport()) {

			return;
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Map<String, Serializable> expandoBridgeAttributes = null;

		if (serviceContext != null) {
			expandoBridgeAttributes =
				serviceContext.getExpandoBridgeAttributes();
		}

		UserExporterUtil.exportUser(user, expandoBridgeAttributes);
	}

	@Override
	protected long[] getUserIds(Object classPK) {
		return new long[] { (Long)classPK };
	}

	protected void updateMembershipRequestStatus(long userId, long groupId)
		throws Exception {

		long principalUserId = GetterUtil.getLong(
			PrincipalThreadLocal.getName());

		User user = UserLocalServiceUtil.getUser(userId);

		List<MembershipRequest> membershipRequests =
			MembershipRequestLocalServiceUtil.getMembershipRequests(
				userId, groupId, MembershipRequestConstants.STATUS_PENDING);

		for (MembershipRequest membershipRequest : membershipRequests) {
			MembershipRequestLocalServiceUtil.updateStatus(
				principalUserId, membershipRequest.getMembershipRequestId(),
				LanguageUtil.get(
					user.getLocale(), "your-membership-has-been-approved"),
				MembershipRequestConstants.STATUS_APPROVED, false,
				new ServiceContext());
		}
	}

}