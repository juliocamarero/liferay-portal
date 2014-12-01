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

import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackRegistryUtil;
import com.liferay.portal.security.exportimport.UserExporterUtil;
import com.liferay.portal.security.exportimport.UserImportTransactionThreadLocal;
import com.liferay.portal.security.exportimport.UserOperation;
import com.liferay.portal.service.UserGroupLocalServiceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcellus Tavares
 */
public class UserGroupModelListener extends BaseModelListener<UserGroup> {

	@Override
	public void onAfterAddAssociation(
			Object classPK, String associationClassName,
			Object associationClassPK)
		throws ModelListenerException {

		try {
			long userGroupId = ((Long)classPK).longValue();

			if (associationClassName.equals(User.class.getName())) {
				exportToLDAP(
					(Long)associationClassPK, (Long)userGroupId,
					UserOperation.ADD);
			}

			reindexUsers(userGroupId, associationClassName);
		}
		catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	@Override
	public void onAfterRemoveAssociation(
			Object classPK, String associationClassName,
			Object associationClassPK)
		throws ModelListenerException {

		try {
			long userGroupId = ((Long)classPK).longValue();

			if (associationClassName.equals(User.class.getName())) {
				exportToLDAP(
					(Long)associationClassPK, (Long)userGroupId,
					UserOperation.REMOVE);
			}

			reindexUsers(userGroupId, associationClassName);
		}
		catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	protected void exportToLDAP(
			long userId, long userGroupId, UserOperation userOperation)
		throws Exception {

		if (UserImportTransactionThreadLocal.isOriginatesFromImport()) {
			return;
		}

		UserExporterUtil.exportUser(userId, userGroupId, userOperation);
	}

	protected void reindexUsers(long userGroupId, String associationClassName) {
		if (!_TABLE_MAPPER_CLASSES.contains(associationClassName)) {
			return;
		}

		long[] userIds = UserGroupLocalServiceUtil.getUserPrimaryKeys(
			userGroupId);

		Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(
			User.class.getName());

		indexer.commitCallbackReindex(userIds);
	}

	private static final List<String> _TABLE_MAPPER_CLASSES =
		new ArrayList<String>();

	static {
		_TABLE_MAPPER_CLASSES.add(Group.class.getName());
		_TABLE_MAPPER_CLASSES.add(Team.class.getName());
	}

}