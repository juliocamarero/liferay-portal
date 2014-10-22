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

package com.liferay.journal.content.web.portlet;

import com.liferay.journal.content.web.portlet.upgrade.JournalContentUpgrade;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
*/
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.css-class-wrapper=portlet-journal-content",
		"com.liferay.portlet.configuration-action-class=com.liferay.journal.content.web.portlet.action.ConfigurationActionImpl",
		"com.liferay.portlet.custom-attributes-display=com.liferay.journal.content.web.JournalArticleCustomAttributesDisplay",
		"com.liferay.portlet.custom-attributes-display=com.liferay.journal.content.web.JournalFolderCustomAttributesDisplay",
		"com.liferay.portlet.ddm-display=com.liferay.portlet.journal.ddm.JournalContentDDMDisplay",
		"com.liferay.portlet.display-category=category.cms",
		"com.liferay.portlet.icon=/icons/journal_content.png",
		"com.liferay.portlet.instanceable=true",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.friendly-url-mapping=journal_content",
		"com.liferay.portlet.friendly-url-routes=com/liferay/journal/content/web/portlet/route/journal-content-friendly-url-routes.xml",
		"com.liferay.portlet.layout-cacheable=true",
		"com.liferay.portlet.portlet-data-handler-class=com.liferay.journal.content.web.lar.JournalContentPortletDataHandler",
		"com.liferay.portlet.portlet-layout-listener-class=com.liferay.portlet.journal.content.web.JournalContentPortletLayoutListener",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.scopeable=true",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.struts-path=journal_content",
		"com.liferay.portlet.social-activity-interpreter-class=com.liferay.journal.content.web.social.JournalArticleActivityInterpreter",
		"com.liferay.portlet.social-activity-interpreter-class=com.liferay.journal.content.web.social.JournalFolderActivityInterpreter",
		"com.liferay.portlet.use-default-template=false",
		"javax.portlet.display-name=Web Content Display",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.config-template=/configuration.jsp",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=guest,power-user,user",
		"javax.portlet.supports.mime-type=text/html",
		"javax.portlet.supports.mime-type=application/vnd.wap.xhtml+xml"
	},
	service = Portlet.class
)
public class JournalContentPortlet extends MVCPortlet {

	@Reference(unbind = "-")
	protected void setInvitationUpgrade(
		JournalContentUpgrade journalContentUpgrade) {
	}

}