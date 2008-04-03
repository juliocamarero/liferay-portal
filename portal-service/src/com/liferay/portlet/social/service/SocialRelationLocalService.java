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
 * <a href="SocialRelationLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is
 * <code>com.liferay.portlet.social.service.impl.SocialRelationLocalServiceImpl</code>.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.social.service.SocialRelationLocalServiceFactory
 * @see com.liferay.portlet.social.service.SocialRelationLocalServiceUtil
 *
 */
public interface SocialRelationLocalService {
	public com.liferay.portlet.social.model.SocialRelation addSocialRelation(
		com.liferay.portlet.social.model.SocialRelation socialRelation)
		throws com.liferay.portal.SystemException;

	public void deleteSocialRelation(long relationId)
		throws com.liferay.portal.SystemException,
			com.liferay.portal.PortalException;

	public void deleteSocialRelation(
		com.liferay.portlet.social.model.SocialRelation socialRelation)
		throws com.liferay.portal.SystemException,
			com.liferay.portal.PortalException;

	public java.util.List<com.liferay.portlet.social.model.SocialRelation> dynamicQuery(
		com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer)
		throws com.liferay.portal.SystemException;

	public java.util.List<com.liferay.portlet.social.model.SocialRelation> dynamicQuery(
		com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer,
		int begin, int end) throws com.liferay.portal.SystemException;

	public com.liferay.portlet.social.model.SocialRelation updateSocialRelation(
		com.liferay.portlet.social.model.SocialRelation socialRelation)
		throws com.liferay.portal.SystemException;

	public com.liferay.portlet.social.service.persistence.SocialActivityPersistence getSocialActivityPersistence();

	public void setSocialActivityPersistence(
		com.liferay.portlet.social.service.persistence.SocialActivityPersistence socialActivityPersistence);

	public com.liferay.portlet.social.service.persistence.SocialActivityFinder getSocialActivityFinder();

	public void setSocialActivityFinder(
		com.liferay.portlet.social.service.persistence.SocialActivityFinder socialActivityFinder);

	public com.liferay.portlet.social.service.persistence.SocialRelationPersistence getSocialRelationPersistence();

	public void setSocialRelationPersistence(
		com.liferay.portlet.social.service.persistence.SocialRelationPersistence socialRelationPersistence);

	public com.liferay.portlet.social.service.persistence.SocialRelationFinder getSocialRelationFinder();

	public void setSocialRelationFinder(
		com.liferay.portlet.social.service.persistence.SocialRelationFinder socialRelationFinder);

	public com.liferay.portal.service.persistence.UserPersistence getUserPersistence();

	public void setUserPersistence(
		com.liferay.portal.service.persistence.UserPersistence userPersistence);

	public com.liferay.portal.service.persistence.UserFinder getUserFinder();

	public void setUserFinder(
		com.liferay.portal.service.persistence.UserFinder userFinder);

	public void afterPropertiesSet();

	public com.liferay.portlet.social.model.SocialRelation addRelation(
		long userId1, long userId2, int type)
		throws com.liferay.portal.SystemException,
			com.liferay.portal.PortalException;

	public void deleteRelation(long relationId)
		throws com.liferay.portal.SystemException,
			com.liferay.portal.PortalException;

	public void deleteRelations(long userId)
		throws com.liferay.portal.SystemException;

	public java.util.List<com.liferay.portlet.social.model.SocialRelation> getRelations(
		long userId, int type, int begin, int end)
		throws com.liferay.portal.SystemException;
}