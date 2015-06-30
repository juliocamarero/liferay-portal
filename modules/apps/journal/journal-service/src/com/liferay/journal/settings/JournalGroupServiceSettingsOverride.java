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

package com.liferay.journal.settings;

/**
 * @author Juergen Kappler
 */
public interface JournalGroupServiceSettingsOverride {

	public String emailArticleAddedBodyXml();

	public String emailArticleAddedSubjectXml();

	public String emailArticleApprovalDeniedBodyXml();

	public String emailArticleApprovalDeniedSubjectXml();

	public String emailArticleApprovalGrantedBodyXml();

	public String emailArticleApprovalGrantedSubjectXml();

	public String emailArticleApprovalRequestedBodyXml();

	public String emailArticleApprovalRequestedSubjectXml();

	public String emailArticleMovedFromFolderBodyXml();

	public String emailArticleMovedFromFolderSubjectXml();

	public String emailArticleMovedToFolderBodyXml();

	public String emailArticleMovedToFolderSubjectXml();

	public String emailArticleReviewBodyXml();

	public String emailArticleReviewSubjectXml();

	public String emailArticleUpdatedBodyXml();

	public String emailArticleUpdatedSubjectXml();

}