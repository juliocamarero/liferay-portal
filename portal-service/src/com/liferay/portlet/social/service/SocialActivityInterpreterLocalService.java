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
 * <a href="SocialActivityInterpreterLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is
 * <code>com.liferay.portlet.social.service.impl.SocialActivityInterpreterLocalServiceImpl</code>.
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
 * @see com.liferay.portlet.social.service.SocialActivityInterpreterLocalServiceFactory
 * @see com.liferay.portlet.social.service.SocialActivityInterpreterLocalServiceUtil
 *
 */
public interface SocialActivityInterpreterLocalService {
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

	public void afterPropertiesSet();

	public void addActivityInterpreter(
		com.liferay.portlet.social.model.SocialActivityInterpreter activityInterpreter);

	public void deleteActivityInterpreter(
		com.liferay.portlet.social.model.SocialActivityInterpreter activityInterpreter);

	public com.liferay.portlet.social.model.SocialActivityFeedEntry interpret(
		com.liferay.portlet.social.model.SocialActivity activityTracker,
		com.liferay.portal.theme.ThemeDisplay themeDisplay);
}