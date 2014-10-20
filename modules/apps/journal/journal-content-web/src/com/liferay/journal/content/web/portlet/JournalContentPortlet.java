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
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletRequestModel;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.search.DisplayInformationProvider;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.DocumentConversionUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PrefsParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.util.DLUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleDisplay;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		"com.liferay.portlet.ddm-display=com.liferay.journal.content.web.portlet.action.JournalContentDDMDisplay",
		"com.liferay.portlet.display-category=category.cms",
		"com.liferay.portlet.icon=/icons/journal_content.png",
		"com.liferay.portlet.instanceable=true",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.friendly-url-mapping=journal_content",
		"com.liferay.portlet.friendly-url-routes=com/liferay/journal/content/web/portlet/route/journal-content-friendly-url-routes.xml",
		"com.liferay.portlet.layout-cacheable=true",
		"com.liferay.portlet.portlet-data-handler-class=com.liferay.portlet.journal.lar.JournalContentPortletDataHandler",
		"com.liferay.portlet.portlet-layout-listener-class=com.liferay.journal.content.web.JournalContentPortletLayoutListener",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.scopeable=true",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.struts-path=journal_content",
		"com.liferay.portlet.social-activity-interpreter-class=com.liferay.portlet.journal.social.JournalArticleActivityInterpreter",
		"com.liferay.portlet.social-activity-interpreter-class=com.liferay.portlet.journal.social.JournalFolderActivityInterpreter",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=Web Content Display",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.config-template=/configuration.jsp",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=com_liferay_journal_content_web_portlet_JournalContentPortlet",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=guest,power-user,user",
		"javax.portlet.supports.mime-type=text/html",
		"javax.portlet.supports.mime-type=application/vnd.wap.xhtml+xml"
	},
	service = {
		Portlet.class,
		DisplayInformationProvider.class
	}
)
public class JournalContentPortlet
	extends MVCPortlet
	implements DisplayInformationProvider {

	@Override
	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		PortletPreferences portletPreferences = renderRequest.getPreferences();

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long articleGroupId = ParamUtil.getLong(
			renderRequest, "articleGroupId");

		if (articleGroupId <= 0) {
			articleGroupId = GetterUtil.getLong(
				portletPreferences.getValue(
					"groupId", String.valueOf(themeDisplay.getScopeGroupId())));
		}

		String articleId = PrefsParamUtil.getString(
			portletPreferences, renderRequest, "articleId");
		String ddmTemplateKey = PrefsParamUtil.getString(
			portletPreferences, renderRequest, "ddmTemplateKey");

		JournalArticle article = null;
		JournalArticleDisplay articleDisplay = null;

		if ((articleGroupId > 0) && Validator.isNotNull(articleId)) {
			String viewMode = ParamUtil.getString(renderRequest, "viewMode");
			String languageId = LanguageUtil.getLanguageId(renderRequest);
			int page = ParamUtil.getInteger(renderRequest, "page", 1);

			article = JournalArticleLocalServiceUtil.fetchLatestArticle(
				articleGroupId, articleId, WorkflowConstants.STATUS_APPROVED);

			try {
				if (article == null) {
					article = JournalArticleLocalServiceUtil.getLatestArticle(
						articleGroupId, articleId,
						WorkflowConstants.STATUS_ANY);
				}

				double version = article.getVersion();

				articleDisplay = JournalContentUtil.getDisplay(
					articleGroupId, articleId, version, ddmTemplateKey,
					viewMode, languageId, page,
					new PortletRequestModel(renderRequest, renderResponse),
					themeDisplay);
			}
			catch (Exception e) {
				renderRequest.removeAttribute(WebKeys.JOURNAL_ARTICLE);

				articleDisplay = JournalContentUtil.getDisplay(
					articleGroupId, articleId, ddmTemplateKey, viewMode,
					languageId, page,
					new PortletRequestModel(renderRequest, renderResponse),
					themeDisplay);
			}
		}

		if (article != null) {
			renderRequest.setAttribute(WebKeys.JOURNAL_ARTICLE, article);
		}

		if (articleDisplay != null) {
			renderRequest.setAttribute(
				WebKeys.JOURNAL_ARTICLE_DISPLAY, articleDisplay);
		}
		else {
			renderRequest.removeAttribute(WebKeys.JOURNAL_ARTICLE_DISPLAY);
		}

		super.doView(renderRequest, renderResponse);
	}

	public void exportArticle(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		try {
			long groupId = ParamUtil.getLong(actionRequest, "groupId");
			String articleId = ParamUtil.getString(actionRequest, "articleId");

			String targetExtension = ParamUtil.getString(
				actionRequest, "targetExtension");

			PortletPreferences portletPreferences =
				actionRequest.getPreferences();

			String[] allowedExtensions = StringUtil.split(
				portletPreferences.getValue("extensions", null));

			String languageId = LanguageUtil.getLanguageId(actionRequest);
			PortletRequestModel portletRequestModel = new PortletRequestModel(
				actionRequest, actionResponse);
			ThemeDisplay themeDisplay =
				(ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			HttpServletRequest request = PortalUtil.getHttpServletRequest(
				actionRequest);
			HttpServletResponse response = PortalUtil.getHttpServletResponse(
				actionResponse);

			getFile(
				groupId, articleId, targetExtension, allowedExtensions,
				languageId, portletRequestModel, themeDisplay, request,
				response);
		}
		catch (Exception e) {
			PortalUtil.sendError(e, actionRequest, actionResponse);
		}
	}

	protected void getFile(
			long groupId, String articleId, String targetExtension,
			String[] allowedExtensions, String languageId,
			PortletRequestModel portletRequestModel, ThemeDisplay themeDisplay,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		try {
			JournalArticleDisplay articleDisplay =
				JournalContentUtil.getDisplay(
					groupId, articleId, null, "export", languageId, 1,
					portletRequestModel, themeDisplay);

			int pages = articleDisplay.getNumberOfPages();

			StringBundler sb = new StringBundler(pages + 12);

			sb.append("<html>");

			sb.append("<head>");
			sb.append("<meta content=\"");
			sb.append(ContentTypes.TEXT_HTML_UTF8);
			sb.append("\" http-equiv=\"content-type\" />");
			sb.append("<base href=\"");
			sb.append(themeDisplay.getPortalURL());
			sb.append("\" />");
			sb.append("</head>");

			sb.append("<body>");

			sb.append(articleDisplay.getContent());

			for (int i = 2; i <= pages; i++) {
				articleDisplay = JournalContentUtil.getDisplay(
					groupId, articleId, "export", languageId, i, themeDisplay);

				sb.append(articleDisplay.getContent());
			}

			sb.append("</body>");
			sb.append("</html>");

			InputStream is = new UnsyncByteArrayInputStream(
				sb.toString().getBytes(StringPool.UTF8));

			String title = articleDisplay.getTitle();
			String sourceExtension = "html";

			String fileName = title.concat(StringPool.PERIOD).concat(
				sourceExtension);

			if (Validator.isNotNull(targetExtension) &&
				ArrayUtil.contains(allowedExtensions, targetExtension)) {

				String id = DLUtil.getTempFileId(
					articleDisplay.getId(),
					String.valueOf(articleDisplay.getVersion()), languageId);

				File convertedFile = DocumentConversionUtil.convert(
					id, is, sourceExtension, targetExtension);

				if (convertedFile != null) {
					fileName = title.concat(StringPool.PERIOD).concat(
						targetExtension);

					is = new FileInputStream(convertedFile);
				}
			}

			String contentType = MimeTypesUtil.getContentType(fileName);

			ServletResponseUtil.sendFile(
				request, response, fileName, is, contentType);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	@Reference(unbind = "-")
	protected void setInvitationUpgrade(
		JournalContentUpgrade journalContentUpgrade) {
	}

	private static Log _log = LogFactoryUtil.getLog(
		JournalContentPortlet.class);

	@Override
	public String getClassName() {
		return JournalArticle.class.getName();
	}

	@Override
	public String getClassPK(PortletPreferences portletPreferences) {
		return portletPreferences.getValue("articleId", StringPool.BLANK);
	}

}