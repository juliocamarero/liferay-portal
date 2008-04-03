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
 * <a href="SocialActivityInterpreterLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.liferay.portlet.social.service.SocialActivityInterpreterLocalService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * <p>
 * <code>com.liferay.portlet.social.service.SocialActivityInterpreterLocalServiceFactory</code>
 * is responsible for the lookup of the bean.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.social.service.SocialActivityInterpreterLocalService
 * @see com.liferay.portlet.social.service.SocialActivityInterpreterLocalServiceFactory
 *
 */
public class SocialActivityInterpreterLocalServiceUtil {
	public static com.liferay.portlet.social.service.persistence.SocialActivityPersistence getSocialActivityPersistence() {
		SocialActivityInterpreterLocalService socialActivityInterpreterLocalService =
			SocialActivityInterpreterLocalServiceFactory.getService();

		return socialActivityInterpreterLocalService.getSocialActivityPersistence();
	}

	public static void setSocialActivityPersistence(
		com.liferay.portlet.social.service.persistence.SocialActivityPersistence socialActivityPersistence) {
		SocialActivityInterpreterLocalService socialActivityInterpreterLocalService =
			SocialActivityInterpreterLocalServiceFactory.getService();

		socialActivityInterpreterLocalService.setSocialActivityPersistence(socialActivityPersistence);
	}

	public static com.liferay.portlet.social.service.persistence.SocialActivityFinder getSocialActivityFinder() {
		SocialActivityInterpreterLocalService socialActivityInterpreterLocalService =
			SocialActivityInterpreterLocalServiceFactory.getService();

		return socialActivityInterpreterLocalService.getSocialActivityFinder();
	}

	public static void setSocialActivityFinder(
		com.liferay.portlet.social.service.persistence.SocialActivityFinder socialActivityFinder) {
		SocialActivityInterpreterLocalService socialActivityInterpreterLocalService =
			SocialActivityInterpreterLocalServiceFactory.getService();

		socialActivityInterpreterLocalService.setSocialActivityFinder(socialActivityFinder);
	}

	public static com.liferay.portlet.social.service.persistence.SocialRelationPersistence getSocialRelationPersistence() {
		SocialActivityInterpreterLocalService socialActivityInterpreterLocalService =
			SocialActivityInterpreterLocalServiceFactory.getService();

		return socialActivityInterpreterLocalService.getSocialRelationPersistence();
	}

	public static void setSocialRelationPersistence(
		com.liferay.portlet.social.service.persistence.SocialRelationPersistence socialRelationPersistence) {
		SocialActivityInterpreterLocalService socialActivityInterpreterLocalService =
			SocialActivityInterpreterLocalServiceFactory.getService();

		socialActivityInterpreterLocalService.setSocialRelationPersistence(socialRelationPersistence);
	}

	public static com.liferay.portlet.social.service.persistence.SocialRelationFinder getSocialRelationFinder() {
		SocialActivityInterpreterLocalService socialActivityInterpreterLocalService =
			SocialActivityInterpreterLocalServiceFactory.getService();

		return socialActivityInterpreterLocalService.getSocialRelationFinder();
	}

	public static void setSocialRelationFinder(
		com.liferay.portlet.social.service.persistence.SocialRelationFinder socialRelationFinder) {
		SocialActivityInterpreterLocalService socialActivityInterpreterLocalService =
			SocialActivityInterpreterLocalServiceFactory.getService();

		socialActivityInterpreterLocalService.setSocialRelationFinder(socialRelationFinder);
	}

	public static void afterPropertiesSet() {
		SocialActivityInterpreterLocalService socialActivityInterpreterLocalService =
			SocialActivityInterpreterLocalServiceFactory.getService();

		socialActivityInterpreterLocalService.afterPropertiesSet();
	}

	public static void addActivityInterpreter(
		com.liferay.portlet.social.model.SocialActivityInterpreter activityInterpreter) {
		SocialActivityInterpreterLocalService socialActivityInterpreterLocalService =
			SocialActivityInterpreterLocalServiceFactory.getService();

		socialActivityInterpreterLocalService.addActivityInterpreter(activityInterpreter);
	}

	public static void deleteActivityInterpreter(
		com.liferay.portlet.social.model.SocialActivityInterpreter activityInterpreter) {
		SocialActivityInterpreterLocalService socialActivityInterpreterLocalService =
			SocialActivityInterpreterLocalServiceFactory.getService();

		socialActivityInterpreterLocalService.deleteActivityInterpreter(activityInterpreter);
	}

	public static com.liferay.portlet.social.model.SocialActivityFeedEntry interpret(
		com.liferay.portlet.social.model.SocialActivity activityTracker,
		com.liferay.portal.theme.ThemeDisplay themeDisplay) {
		SocialActivityInterpreterLocalService socialActivityInterpreterLocalService =
			SocialActivityInterpreterLocalServiceFactory.getService();

		return socialActivityInterpreterLocalService.interpret(activityTracker,
			themeDisplay);
	}
}