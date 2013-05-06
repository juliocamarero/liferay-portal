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

package com.liferay.portlet.social.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivitySet;
import com.liferay.portlet.social.service.base.SocialActivitySetLocalServiceBaseImpl;
import com.liferay.portlet.social.util.comparator.SocialActivitySetModifiedDateComparator;

import java.util.List;

/**
 * @author Matthew Kong
 */
public class SocialActivitySetLocalServiceImpl
	extends SocialActivitySetLocalServiceBaseImpl {

	public SocialActivitySet addActivitySet(long activityId)
		throws PortalException, SystemException {

		// Activity set

		SocialActivity activity = socialActivityPersistence.findByPrimaryKey(
			activityId);

		long activitySetId = counterLocalService.increment();

		SocialActivitySet activitySet = socialActivitySetPersistence.create(
			activitySetId);

		activitySet.setGroupId(activity.getGroupId());
		activitySet.setCompanyId(activity.getCompanyId());
		activitySet.setUserId(activity.getUserId());
		activitySet.setCreateDate(activity.getCreateDate());
		activitySet.setModifiedDate(activity.getCreateDate());
		activitySet.setClassName(activity.getClassName());
		activitySet.setClassPK(activity.getClassPK());
		activitySet.setType(activity.getType());
		activitySet.setActivityCount(1);

		socialActivitySetPersistence.update(activitySet);

		// Activity

		activity.setActivitySetId(activitySetId);

		socialActivityPersistence.update(activity);

		return activitySet;
	}

	public void decrementActivityCount(long activitySetId)
		throws PortalException, SystemException {

		if (activitySetId == 0) {
			return;
		}

		SocialActivitySet activitySet =
			socialActivitySetPersistence.findByPrimaryKey(activitySetId);

		if (activitySet.getActivityCount() == 1) {
			socialActivitySetPersistence.remove(activitySetId);

			return;
		}

		activitySet.setActivityCount(activitySet.getActivityCount() - 1);

		socialActivitySetPersistence.update(activitySet);
	}

	public void decrementActivityCount(long classNameId, long classPK)
		throws PortalException, SystemException {

		List<SocialActivity> activities = socialActivityPersistence.findByC_C(
			classNameId, classPK);

		for (SocialActivity activity : activities) {
			decrementActivityCount(activity.getActivitySetId());
		}
	}

	public SocialActivitySet getClassActivitySet(
			long classNameId, long classPK, int type)
		throws SystemException {

		return socialActivitySetPersistence.fetchByC_C_T_First(
			classNameId, classPK, type,
			new SocialActivitySetModifiedDateComparator());
	}

	public SocialActivitySet getClassActivitySet(
			long userId, long classNameId, long classPK, int type)
		throws SystemException {

		return socialActivitySetPersistence.fetchByU_C_C_T_First(
			userId, classNameId, classPK, type,
			new SocialActivitySetModifiedDateComparator());
	}

	public List<SocialActivitySet> getGroupActivitySets(
			long groupId, int start, int end)
		throws SystemException {

		return socialActivitySetPersistence.findByGroupId(
			groupId, start, end, new SocialActivitySetModifiedDateComparator());
	}

	public int getGroupActivitySetsCount(long groupId) throws SystemException {
		return socialActivitySetPersistence.countByGroupId(groupId);
	}

	public List<SocialActivitySet> getRelationActivitySets(
			long userId, int start, int end)
		throws SystemException {

		return socialActivitySetFinder.findByRelation(userId, start, end);
	}

	public List<SocialActivitySet> getRelationActivitySets(
			long userId, int type, int start, int end)
		throws SystemException {

		return socialActivitySetFinder.findByRelationType(
			userId, type, start, end);
	}

	public int getRelationActivitySetsCount(long userId)
		throws SystemException {

		return socialActivitySetFinder.countByRelation(userId);
	}

	public int getRelationActivitySetsCount(long userId, int type)
		throws SystemException {

		return socialActivitySetFinder.countByRelationType(userId, type);
	}

	public SocialActivitySet getUserActivitySet(
			long groupId, long userId, int type)
		throws SystemException {

		return socialActivitySetPersistence.fetchByG_U_T_First(
			groupId, userId, type,
			new SocialActivitySetModifiedDateComparator());
	}

	public List<SocialActivitySet> getUserActivitySets(
			long userId, int start, int end)
		throws SystemException {

		return socialActivitySetFinder.findByUser(userId, start, end);
	}

	public int getUserActivitySetsCount(long userId) throws SystemException {
		return socialActivitySetFinder.countByUser(userId);
	}

	public List<SocialActivitySet> getUserGroupsActivitySets(
			long userId, int start, int end)
		throws SystemException {

		return socialActivitySetFinder.findByUserGroups(userId, start, end);
	}

	public int getUserGroupsActivitySetsCount(long userId)
		throws SystemException {

		return socialActivitySetFinder.countByUserGroups(userId);
	}

	public void incrementActivityCount(long activitySetId, long activityId)
		throws PortalException, SystemException {

		// Activity set

		SocialActivitySet activitySet =
			socialActivitySetPersistence.findByPrimaryKey(activitySetId);

		SocialActivity activity = socialActivityPersistence.findByPrimaryKey(
			activityId);

		activitySet.setModifiedDate(activity.getCreateDate());

		activitySet.setActivityCount(activitySet.getActivityCount() + 1);

		socialActivitySetPersistence.update(activitySet);

		// Activity

		activity.setActivitySetId(activitySetId);

		socialActivityPersistence.update(activity);
	}

}