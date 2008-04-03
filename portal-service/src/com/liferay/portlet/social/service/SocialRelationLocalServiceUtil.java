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

package com.liferay.portlet.social.service;


/**
 * <a href="SocialRelationLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.liferay.portlet.social.service.SocialRelationLocalService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * <p>
 * <code>com.liferay.portlet.social.service.SocialRelationLocalServiceFactory</code>
 * is responsible for the lookup of the bean.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.social.service.SocialRelationLocalService
 * @see com.liferay.portlet.social.service.SocialRelationLocalServiceFactory
 *
 */
public class SocialRelationLocalServiceUtil {
	public static com.liferay.portlet.social.model.SocialRelation addSocialRelation(
		com.liferay.portlet.social.model.SocialRelation socialRelation)
		throws com.liferay.portal.SystemException {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		return socialRelationLocalService.addSocialRelation(socialRelation);
	}

	public static void deleteSocialRelation(long relationId)
		throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		socialRelationLocalService.deleteSocialRelation(relationId);
	}

	public static void deleteSocialRelation(
		com.liferay.portlet.social.model.SocialRelation socialRelation)
		throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		socialRelationLocalService.deleteSocialRelation(socialRelation);
	}

	public static java.util.List<com.liferay.portlet.social.model.SocialRelation> dynamicQuery(
		com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer)
		throws com.liferay.portal.SystemException {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		return socialRelationLocalService.dynamicQuery(queryInitializer);
	}

	public static java.util.List<com.liferay.portlet.social.model.SocialRelation> dynamicQuery(
		com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer,
		int begin, int end) throws com.liferay.portal.SystemException {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		return socialRelationLocalService.dynamicQuery(queryInitializer, begin,
			end);
	}

	public static com.liferay.portlet.social.model.SocialRelation updateSocialRelation(
		com.liferay.portlet.social.model.SocialRelation socialRelation)
		throws com.liferay.portal.SystemException {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		return socialRelationLocalService.updateSocialRelation(socialRelation);
	}

	public static com.liferay.portlet.social.service.persistence.SocialActivityPersistence getSocialActivityPersistence() {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		return socialRelationLocalService.getSocialActivityPersistence();
	}

	public static void setSocialActivityPersistence(
		com.liferay.portlet.social.service.persistence.SocialActivityPersistence socialActivityPersistence) {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		socialRelationLocalService.setSocialActivityPersistence(socialActivityPersistence);
	}

	public static com.liferay.portlet.social.service.persistence.SocialActivityFinder getSocialActivityFinder() {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		return socialRelationLocalService.getSocialActivityFinder();
	}

	public static void setSocialActivityFinder(
		com.liferay.portlet.social.service.persistence.SocialActivityFinder socialActivityFinder) {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		socialRelationLocalService.setSocialActivityFinder(socialActivityFinder);
	}

	public static com.liferay.portlet.social.service.persistence.SocialRelationPersistence getSocialRelationPersistence() {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		return socialRelationLocalService.getSocialRelationPersistence();
	}

	public static void setSocialRelationPersistence(
		com.liferay.portlet.social.service.persistence.SocialRelationPersistence socialRelationPersistence) {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		socialRelationLocalService.setSocialRelationPersistence(socialRelationPersistence);
	}

	public static com.liferay.portlet.social.service.persistence.SocialRelationFinder getSocialRelationFinder() {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		return socialRelationLocalService.getSocialRelationFinder();
	}

	public static void setSocialRelationFinder(
		com.liferay.portlet.social.service.persistence.SocialRelationFinder socialRelationFinder) {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		socialRelationLocalService.setSocialRelationFinder(socialRelationFinder);
	}

	public static com.liferay.portal.service.persistence.UserPersistence getUserPersistence() {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		return socialRelationLocalService.getUserPersistence();
	}

	public static void setUserPersistence(
		com.liferay.portal.service.persistence.UserPersistence userPersistence) {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		socialRelationLocalService.setUserPersistence(userPersistence);
	}

	public static com.liferay.portal.service.persistence.UserFinder getUserFinder() {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		return socialRelationLocalService.getUserFinder();
	}

	public static void setUserFinder(
		com.liferay.portal.service.persistence.UserFinder userFinder) {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		socialRelationLocalService.setUserFinder(userFinder);
	}

	public static void afterPropertiesSet() {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		socialRelationLocalService.afterPropertiesSet();
	}

	public static com.liferay.portlet.social.model.SocialRelation addRelation(
		long userId1, long userId2, int type)
		throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		return socialRelationLocalService.addRelation(userId1, userId2, type);
	}

	public static void deleteRelation(long relationId)
		throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		socialRelationLocalService.deleteRelation(relationId);
	}

	public static void deleteRelations(long userId)
		throws com.liferay.portal.SystemException {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		socialRelationLocalService.deleteRelations(userId);
	}

	public static java.util.List<com.liferay.portlet.social.model.SocialRelation> getRelations(
		long userId, int type, int begin, int end)
		throws com.liferay.portal.SystemException {
		SocialRelationLocalService socialRelationLocalService = SocialRelationLocalServiceFactory.getService();

		return socialRelationLocalService.getRelations(userId, type, begin, end);
	}
}