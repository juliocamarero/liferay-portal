/**
 * Copyright (c) 2000-2008 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.social.service.base;

import com.liferay.counter.service.CounterLocalService;
import com.liferay.counter.service.CounterLocalServiceFactory;
import com.liferay.counter.service.CounterService;
import com.liferay.counter.service.CounterServiceFactory;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.DynamicQueryInitializer;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserLocalServiceFactory;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.UserServiceFactory;
import com.liferay.portal.service.persistence.UserFinder;
import com.liferay.portal.service.persistence.UserFinderUtil;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.service.persistence.UserUtil;

import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.service.SocialActivityInterpreterLocalService;
import com.liferay.portlet.social.service.SocialActivityInterpreterLocalServiceFactory;
import com.liferay.portlet.social.service.SocialActivityLocalService;
import com.liferay.portlet.social.service.SocialRelationLocalService;
import com.liferay.portlet.social.service.SocialRelationLocalServiceFactory;
import com.liferay.portlet.social.service.SocialRelationService;
import com.liferay.portlet.social.service.SocialRelationServiceFactory;
import com.liferay.portlet.social.service.persistence.SocialActivityFinder;
import com.liferay.portlet.social.service.persistence.SocialActivityFinderUtil;
import com.liferay.portlet.social.service.persistence.SocialActivityPersistence;
import com.liferay.portlet.social.service.persistence.SocialActivityUtil;
import com.liferay.portlet.social.service.persistence.SocialRelationFinder;
import com.liferay.portlet.social.service.persistence.SocialRelationFinderUtil;
import com.liferay.portlet.social.service.persistence.SocialRelationPersistence;
import com.liferay.portlet.social.service.persistence.SocialRelationUtil;

import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * <a href="SocialActivityLocalServiceBaseImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public abstract class SocialActivityLocalServiceBaseImpl
	implements SocialActivityLocalService, InitializingBean {
	public SocialActivity addSocialActivity(SocialActivity socialActivity)
		throws SystemException {
		socialActivity.setNew(true);

		return socialActivityPersistence.update(socialActivity, false);
	}

	public void deleteSocialActivity(long activityId)
		throws PortalException, SystemException {
		socialActivityPersistence.remove(activityId);
	}

	public void deleteSocialActivity(SocialActivity socialActivity)
		throws PortalException, SystemException {
		socialActivityPersistence.remove(socialActivity);
	}

	public List<SocialActivity> dynamicQuery(
		DynamicQueryInitializer queryInitializer) throws SystemException {
		return socialActivityPersistence.findWithDynamicQuery(queryInitializer);
	}

	public List<SocialActivity> dynamicQuery(
		DynamicQueryInitializer queryInitializer, int begin, int end)
		throws SystemException {
		return socialActivityPersistence.findWithDynamicQuery(queryInitializer,
			begin, end);
	}

	public SocialActivity updateSocialActivity(SocialActivity socialActivity)
		throws SystemException {
		socialActivity.setNew(false);

		return socialActivityPersistence.update(socialActivity, true);
	}

	public SocialActivityPersistence getSocialActivityPersistence() {
		return socialActivityPersistence;
	}

	public void setSocialActivityPersistence(
		SocialActivityPersistence socialActivityPersistence) {
		this.socialActivityPersistence = socialActivityPersistence;
	}

	public SocialActivityFinder getSocialActivityFinder() {
		return socialActivityFinder;
	}

	public void setSocialActivityFinder(
		SocialActivityFinder socialActivityFinder) {
		this.socialActivityFinder = socialActivityFinder;
	}

	public SocialActivityInterpreterLocalService getSocialActivityInterpreterLocalService() {
		return socialActivityInterpreterLocalService;
	}

	public void setSocialActivityInterpreterLocalService(
		SocialActivityInterpreterLocalService socialActivityInterpreterLocalService) {
		this.socialActivityInterpreterLocalService = socialActivityInterpreterLocalService;
	}

	public SocialRelationLocalService getSocialRelationLocalService() {
		return socialRelationLocalService;
	}

	public void setSocialRelationLocalService(
		SocialRelationLocalService socialRelationLocalService) {
		this.socialRelationLocalService = socialRelationLocalService;
	}

	public SocialRelationService getSocialRelationService() {
		return socialRelationService;
	}

	public void setSocialRelationService(
		SocialRelationService socialRelationService) {
		this.socialRelationService = socialRelationService;
	}

	public SocialRelationPersistence getSocialRelationPersistence() {
		return socialRelationPersistence;
	}

	public void setSocialRelationPersistence(
		SocialRelationPersistence socialRelationPersistence) {
		this.socialRelationPersistence = socialRelationPersistence;
	}

	public SocialRelationFinder getSocialRelationFinder() {
		return socialRelationFinder;
	}

	public void setSocialRelationFinder(
		SocialRelationFinder socialRelationFinder) {
		this.socialRelationFinder = socialRelationFinder;
	}

	public CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	public void setCounterLocalService(CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	public CounterService getCounterService() {
		return counterService;
	}

	public void setCounterService(CounterService counterService) {
		this.counterService = counterService;
	}

	public UserLocalService getUserLocalService() {
		return userLocalService;
	}

	public void setUserLocalService(UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public UserFinder getUserFinder() {
		return userFinder;
	}

	public void setUserFinder(UserFinder userFinder) {
		this.userFinder = userFinder;
	}

	public void afterPropertiesSet() {
		if (socialActivityPersistence == null) {
			socialActivityPersistence = SocialActivityUtil.getPersistence();
		}

		if (socialActivityFinder == null) {
			socialActivityFinder = SocialActivityFinderUtil.getFinder();
		}

		if (socialActivityInterpreterLocalService == null) {
			socialActivityInterpreterLocalService = SocialActivityInterpreterLocalServiceFactory.getImpl();
		}

		if (socialRelationLocalService == null) {
			socialRelationLocalService = SocialRelationLocalServiceFactory.getImpl();
		}

		if (socialRelationService == null) {
			socialRelationService = SocialRelationServiceFactory.getImpl();
		}

		if (socialRelationPersistence == null) {
			socialRelationPersistence = SocialRelationUtil.getPersistence();
		}

		if (socialRelationFinder == null) {
			socialRelationFinder = SocialRelationFinderUtil.getFinder();
		}

		if (counterLocalService == null) {
			counterLocalService = CounterLocalServiceFactory.getImpl();
		}

		if (counterService == null) {
			counterService = CounterServiceFactory.getImpl();
		}

		if (userLocalService == null) {
			userLocalService = UserLocalServiceFactory.getImpl();
		}

		if (userService == null) {
			userService = UserServiceFactory.getImpl();
		}

		if (userPersistence == null) {
			userPersistence = UserUtil.getPersistence();
		}

		if (userFinder == null) {
			userFinder = UserFinderUtil.getFinder();
		}
	}

	protected SocialActivityPersistence socialActivityPersistence;
	protected SocialActivityFinder socialActivityFinder;
	protected SocialActivityInterpreterLocalService socialActivityInterpreterLocalService;
	protected SocialRelationLocalService socialRelationLocalService;
	protected SocialRelationService socialRelationService;
	protected SocialRelationPersistence socialRelationPersistence;
	protected SocialRelationFinder socialRelationFinder;
	protected CounterLocalService counterLocalService;
	protected CounterService counterService;
	protected UserLocalService userLocalService;
	protected UserService userService;
	protected UserPersistence userPersistence;
	protected UserFinder userFinder;
}