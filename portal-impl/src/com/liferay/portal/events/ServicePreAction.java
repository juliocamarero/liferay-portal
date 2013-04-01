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

package com.liferay.portal.events;

import com.liferay.portal.LayoutPermissionException;
import com.liferay.portal.NoSuchGroupException;
import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.image.ImageToolUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.servlet.BrowserSnifferUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ColorSchemeFactoryUtil;
import com.liferay.portal.kernel.util.CookieKeys;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.kernel.util.SessionParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Image;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.LayoutTypePortletConstants;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.Theme;
import com.liferay.portal.model.User;
import com.liferay.portal.model.impl.VirtualLayout;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ImageLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.ThemeLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.permission.GroupPermissionUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.theme.ThemeDisplayFactory;
import com.liferay.portal.util.LayoutClone;
import com.liferay.portal.util.LayoutCloneFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletCategoryKeys;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;
import com.liferay.portal.webserver.WebServerServletTokenUtil;
import com.liferay.portlet.PortalPreferences;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.PortletURLImpl;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.journal.NoSuchArticleException;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleServiceUtil;
import com.liferay.portlet.sites.util.SitesUtil;

import java.io.File;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.StopWatch;
import org.apache.struts.Globals;

/**
 * @author Brian Wing Shun Chan
 * @author Felix Ventero
 * @author Jorge Ferrer
 */
public class ServicePreAction extends Action {

	public ServicePreAction() {
		initImportLARFiles();
	}

	public Locale initLocale(
			HttpServletRequest request, HttpServletResponse response, User user)
		throws Exception {

		if (user == null) {
			try {
				user = initUser(request);
			}
			catch (NoSuchUserException nsue) {
				return null;
			}
		}

		HttpSession session = request.getSession();

		Locale locale = (Locale)session.getAttribute(Globals.LOCALE_KEY);

		String doAsUserLanguageId = ParamUtil.getString(
			request, "doAsUserLanguageId");

		if (Validator.isNotNull(doAsUserLanguageId)) {
			locale = LocaleUtil.fromLanguageId(doAsUserLanguageId);
		}

		String i18nLanguageId = (String)request.getAttribute(
			WebKeys.I18N_LANGUAGE_ID);

		if (Validator.isNotNull(i18nLanguageId)) {
			locale = LocaleUtil.fromLanguageId(i18nLanguageId);
		}
		else if (locale == null) {
			if (!user.isDefaultUser()) {
				locale = user.getLocale();
			}
			else {

				// User previously set their preferred language

				String languageId = CookieKeys.getCookie(
					request, CookieKeys.GUEST_LANGUAGE_ID, false);

				if (Validator.isNotNull(languageId)) {
					locale = LocaleUtil.fromLanguageId(languageId);
				}

				// Get locale from the request

				if ((locale == null) && PropsValues.LOCALE_DEFAULT_REQUEST) {
					Enumeration<Locale> locales = request.getLocales();

					while (locales.hasMoreElements()) {
						Locale requestLocale = locales.nextElement();

						if (Validator.isNull(requestLocale.getCountry())) {

							// Locales must contain a country code

							requestLocale = LanguageUtil.getLocale(
								requestLocale.getLanguage());
						}

						if (LanguageUtil.isAvailableLocale(requestLocale)) {
							locale = requestLocale;

							break;
						}
					}
				}

				// Get locale from the default user

				if (locale == null) {
					locale = user.getLocale();
				}

				if (Validator.isNull(locale.getCountry())) {

					// Locales must contain a country code

					locale = LanguageUtil.getLocale(locale.getLanguage());
				}

				if (!LanguageUtil.isAvailableLocale(locale)) {
					locale = user.getLocale();
				}
			}

			session.setAttribute(Globals.LOCALE_KEY, locale);

			LanguageUtil.updateCookie(request, response, locale);
		}

		return locale;
	}

	public ThemeDisplay initThemeDisplay(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();

		// Company

		Company company = PortalUtil.getCompany(request);

		long companyId = company.getCompanyId();

		// CDN host

		String cdnHost = PortalUtil.getCDNHost(request);

		String dynamicResourcesCDNHost = StringPool.BLANK;

		boolean cdnDynamicResourceEnabled =
			PortalUtil.isCDNDynamicResourcesEnabled(request);

		if (cdnDynamicResourceEnabled) {
			dynamicResourcesCDNHost = cdnHost;
		}

		// Portal URL

		String portalURL = PortalUtil.getPortalURL(request);

		// Paths

		String contextPath = PortalUtil.getPathContext();
		String friendlyURLPrivateGroupPath =
			PortalUtil.getPathFriendlyURLPrivateGroup();
		String friendlyURLPrivateUserPath =
			PortalUtil.getPathFriendlyURLPrivateUser();
		String friendlyURLPublicPath = PortalUtil.getPathFriendlyURLPublic();
		String imagePath = dynamicResourcesCDNHost.concat(
			PortalUtil.getPathImage());
		String mainPath = PortalUtil.getPathMain();

		String i18nPath = (String)request.getAttribute(WebKeys.I18N_PATH);

		if (Validator.isNotNull(i18nPath)) {
			if (Validator.isNotNull(contextPath)) {
				String i18nContextPath = contextPath.concat(i18nPath);

				friendlyURLPrivateGroupPath = StringUtil.replaceFirst(
					friendlyURLPrivateGroupPath, contextPath, i18nContextPath);
				friendlyURLPrivateUserPath = StringUtil.replaceFirst(
					friendlyURLPrivateUserPath, contextPath, i18nContextPath);
				friendlyURLPublicPath = StringUtil.replaceFirst(
					friendlyURLPublicPath, contextPath, i18nContextPath);
				mainPath = StringUtil.replaceFirst(
					mainPath, contextPath, i18nContextPath);
			}
			else {
				friendlyURLPrivateGroupPath = i18nPath.concat(
					friendlyURLPrivateGroupPath);
				friendlyURLPrivateUserPath = i18nPath.concat(
					friendlyURLPrivateUserPath);
				friendlyURLPublicPath = i18nPath.concat(friendlyURLPublicPath);
				mainPath = i18nPath.concat(mainPath);
			}
		}

		// Company logo

		StringBundler sb = new StringBundler(5);

		sb.append(imagePath);
		sb.append("/company_logo?img_id=");
		sb.append(company.getLogoId());
		sb.append("&t=");
		sb.append(WebServerServletTokenUtil.getToken(company.getLogoId()));

		String companyLogo = sb.toString();

		int companyLogoHeight = 0;
		int companyLogoWidth = 0;

		Image companyLogoImage = null;

		if (company.getLogoId() > 0) {
			companyLogoImage = ImageLocalServiceUtil.getCompanyLogo(
				company.getLogoId());
		}
		else {
			companyLogoImage = ImageToolUtil.getDefaultCompanyLogo();
		}

		if (companyLogoImage != null) {
			companyLogoHeight = companyLogoImage.getHeight();
			companyLogoWidth = companyLogoImage.getWidth();
		}

		String realCompanyLogo = companyLogo;
		int realCompanyLogoHeight = companyLogoHeight;
		int realCompanyLogoWidth = companyLogoWidth;

		// User

		User user = null;

		try {
			user = initUser(request);
		}
		catch (NoSuchUserException nsue) {
			return null;
		}

		boolean signedIn = !user.isDefaultUser();

		if (PropsValues.BROWSER_CACHE_DISABLED ||
			(PropsValues.BROWSER_CACHE_SIGNED_IN_DISABLED && signedIn)) {

			response.setDateHeader(HttpHeaders.EXPIRES, 0);
			response.setHeader(
				HttpHeaders.CACHE_CONTROL,
				HttpHeaders.CACHE_CONTROL_NO_CACHE_VALUE);
			response.setHeader(
				HttpHeaders.PRAGMA, HttpHeaders.PRAGMA_NO_CACHE_VALUE);
		}

		User realUser = user;

		Long realUserId = (Long)session.getAttribute(WebKeys.USER_ID);

		if (realUserId != null) {
			if (user.getUserId() != realUserId.longValue()) {
				realUser = UserLocalServiceUtil.getUserById(
					realUserId.longValue());
			}
		}

		String doAsUserId = ParamUtil.getString(request, "doAsUserId");
		String doAsUserLanguageId = ParamUtil.getString(
			request, "doAsUserLanguageId");
		long doAsGroupId = ParamUtil.getLong(request, "doAsGroupId");

		long refererGroupId = ParamUtil.getLong(request, "refererGroupId");

		long refererPlid = ParamUtil.getLong(request, "refererPlid");

		if (LayoutLocalServiceUtil.fetchLayout(refererPlid) == null) {
			refererPlid = 0;
		}

		String controlPanelCategory = ParamUtil.getString(
			request, "controlPanelCategory");

		// Permission checker

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		PermissionThreadLocal.setPermissionChecker(permissionChecker);

		// Locale

		String i18nLanguageId = (String)request.getAttribute(
			WebKeys.I18N_LANGUAGE_ID);

		Locale locale = initLocale(request, response, user);

		// Cookie support

		try {

			// LEP-4069

			CookieKeys.validateSupportCookie(request);
		}
		catch (Exception e) {
			CookieKeys.addSupportCookie(request, response);
		}

		// Time zone

		TimeZone timeZone = user.getTimeZone();

		if (timeZone == null) {
			timeZone = company.getTimeZone();
		}

		// Layouts

		if (signedIn) {
			updateUserLayouts(user);
		}

		Layout layout = null;
		List<Layout> layouts = null;

		long plid = ParamUtil.getLong(request, "p_l_id");

		if (plid > 0) {
			layout = LayoutLocalServiceUtil.getLayout(plid);

			long sourceGroupId = ParamUtil.getLong(request, "p_v_l_s_g_id");

			if ((sourceGroupId > 0) && (sourceGroupId != layout.getGroupId())) {
				Group sourceGroup = GroupLocalServiceUtil.getGroup(
					sourceGroupId);

				if (layout.isPublicLayout() ||
					SitesUtil.isUserGroupLayoutSetViewable(
						permissionChecker, layout.getGroup())) {

					layout = new VirtualLayout(layout, sourceGroup);
				}
				else {
					layout = null;
				}
			}
		}
		else {
			long groupId = ParamUtil.getLong(request, "groupId");
			boolean privateLayout = ParamUtil.getBoolean(
				request, "privateLayout");
			long layoutId = ParamUtil.getLong(request, "layoutId");

			if ((groupId > 0) && (layoutId > 0)) {
				layout = LayoutLocalServiceUtil.getLayout(
					groupId, privateLayout, layoutId);
			}
		}

		String ppid = ParamUtil.getString(request, "p_p_id");

		Boolean redirectToDefaultLayout = (Boolean)request.getAttribute(
			WebKeys.REDIRECT_TO_DEFAULT_LAYOUT);

		if (redirectToDefaultLayout == null) {
			redirectToDefaultLayout = Boolean.FALSE;
		}

		if (layout != null) {
			Group group = layout.getGroup();

			if (!signedIn && PropsValues.AUTH_FORWARD_BY_REDIRECT) {
				request.setAttribute(WebKeys.REQUESTED_LAYOUT, layout);
			}

			if (Validator.isNull(controlPanelCategory) &&
				Validator.isNotNull(ppid) &&
				(LiferayWindowState.isPopUp(request) ||
				 LiferayWindowState.isExclusive(request))) {

				controlPanelCategory =
					_CONTROL_PANEL_CATEGORY_PORTLET_PREFIX + ppid;
			}

			boolean viewableGroup = LayoutPermissionUtil.contains(
				permissionChecker, layout, controlPanelCategory, true,
				ActionKeys.VIEW);
			boolean viewableStaging = GroupPermissionUtil.contains(
				permissionChecker, group.getGroupId(), ActionKeys.VIEW_STAGING);

			if (viewableStaging) {
				layouts = LayoutLocalServiceUtil.getLayouts(
					layout.getGroupId(), layout.isPrivateLayout(),
					LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);
			}
			else if (!viewableGroup && group.isStagingGroup()) {
				layout = null;
			}
			else if (!isLoginRequest(request) &&
					 (!viewableGroup ||
					  (!redirectToDefaultLayout &&
					   !LayoutPermissionUtil.contains(
						   permissionChecker, layout, false,
						   ActionKeys.VIEW)))) {

				if (user.isDefaultUser() &&
					PropsValues.AUTH_LOGIN_PROMPT_ENABLED) {

					throw new PrincipalException("User is not authenticated");
				}

				sb = new StringBundler(6);

				sb.append("User ");
				sb.append(user.getUserId());
				sb.append(" is not allowed to access the ");
				sb.append(layout.isPrivateLayout() ? "private": "public");
				sb.append(" pages of group ");
				sb.append(layout.getGroupId());

				if (_log.isWarnEnabled()) {
					_log.warn(sb.toString());
				}

				throw new NoSuchLayoutException(sb.toString());
			}
			else if (isLoginRequest(request) && !viewableGroup) {
				layout = null;
			}
			else if (group.isLayoutPrototype()) {
				layouts = new ArrayList<Layout>();
			}
			else {
				layouts = LayoutLocalServiceUtil.getLayouts(
					layout.getGroupId(), layout.isPrivateLayout(),
					LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

				if (!group.isControlPanel()) {
					doAsGroupId = 0;
				}
			}
		}

		List<Layout> unfilteredLayouts = layouts;

		if (layout == null) {
			Object[] defaultLayout = getDefaultLayout(request, user, signedIn);

			layout = (Layout)defaultLayout[0];
			layouts = (List<Layout>)defaultLayout[1];

			request.setAttribute(WebKeys.LAYOUT_DEFAULT, Boolean.TRUE);
		}

		Object[] viewableLayouts = getViewableLayouts(
			request, user, permissionChecker, layout, layouts);

		String layoutSetLogo = null;

		layout = (Layout)viewableLayouts[0];
		layouts = (List<Layout>)viewableLayouts[1];

		Group group = null;

		if (layout != null) {
			group = layout.getGroup();

			if (!group.isControlPanel()) {
				rememberVisitedGroupIds(request, group.getGroupId());
			}
		}

		LayoutTypePortlet layoutTypePortlet = null;

		layouts = mergeAdditionalLayouts(
			request, user, permissionChecker, layout, layouts);

		LayoutSet layoutSet = null;

		boolean hasCustomizeLayoutPermission = false;
		boolean hasUpdateLayoutPermission = false;

		boolean customizedView = SessionParamUtil.getBoolean(
			request, "customized_view", true);

		if (layout != null) {
			hasCustomizeLayoutPermission = LayoutPermissionUtil.contains(
				permissionChecker, layout, ActionKeys.CUSTOMIZE);
			hasUpdateLayoutPermission = LayoutPermissionUtil.contains(
				permissionChecker, layout, ActionKeys.UPDATE);

			layoutSet = layout.getLayoutSet();

			if (company.isSiteLogo()) {
				long logoId = 0;

				if (layoutSet.isLogo()) {
					logoId = layoutSet.getLogoId();

					if (logoId == 0) {
						logoId = layoutSet.getLiveLogoId();
					}
				}
				else {
					LayoutSet siblingLayoutSet =
						LayoutSetLocalServiceUtil.getLayoutSet(
							layout.getGroupId(), !layout.isPrivateLayout());

					if (siblingLayoutSet.isLogo()) {
						logoId = siblingLayoutSet.getLogoId();
					}
				}

				if (logoId > 0) {
					sb = new StringBundler(5);

					sb.append(imagePath);
					sb.append("/layout_set_logo?img_id=");
					sb.append(logoId);
					sb.append("&t=");
					sb.append(WebServerServletTokenUtil.getToken(logoId));

					layoutSetLogo = sb.toString();

					Image layoutSetLogoImage =
						ImageLocalServiceUtil.getCompanyLogo(logoId);

					companyLogo = layoutSetLogo;
					companyLogoHeight = layoutSetLogoImage.getHeight();
					companyLogoWidth = layoutSetLogoImage.getWidth();
				}
			}

			plid = layout.getPlid();

			// Updates to shared layouts are not reflected until the next time
			// the user logs in because group layouts are cached in the session

			layout = (Layout)layout.clone();

			layoutTypePortlet = (LayoutTypePortlet)layout.getLayoutType();

			boolean customizable = layoutTypePortlet.isCustomizable();

			if (!customizable ||
				(group.isLayoutPrototype() || group.isLayoutSetPrototype())) {

				customizedView = false;
			}

			layoutTypePortlet.setCustomizedView(customizedView);
			layoutTypePortlet.setUpdatePermission(hasUpdateLayoutPermission);

			if (signedIn && customizable && customizedView &&
				hasCustomizeLayoutPermission) {

				PortalPreferences portalPreferences =
					PortletPreferencesFactoryUtil.getPortalPreferences(
						companyId, user.getUserId(), true);

				layoutTypePortlet.setPortalPreferences(portalPreferences);
			}

			LayoutClone layoutClone = LayoutCloneFactory.getInstance();

			if (layoutClone != null) {
				String typeSettings = layoutClone.get(request, plid);

				if (typeSettings != null) {
					UnicodeProperties typeSettingsProperties =
						new UnicodeProperties(true);

					typeSettingsProperties.load(typeSettings);

					String stateMax = typeSettingsProperties.getProperty(
						LayoutTypePortletConstants.STATE_MAX);
					String stateMin = typeSettingsProperties.getProperty(
						LayoutTypePortletConstants.STATE_MIN);
					String modeAbout = typeSettingsProperties.getProperty(
						LayoutTypePortletConstants.MODE_ABOUT);
					String modeConfig = typeSettingsProperties.getProperty(
						LayoutTypePortletConstants.MODE_CONFIG);
					String modeEdit = typeSettingsProperties.getProperty(
						LayoutTypePortletConstants.MODE_EDIT);
					String modeEditDefaults =
						typeSettingsProperties.getProperty(
							LayoutTypePortletConstants.MODE_EDIT_DEFAULTS);
					String modeEditGuest = typeSettingsProperties.getProperty(
						LayoutTypePortletConstants.MODE_EDIT_GUEST);
					String modeHelp = typeSettingsProperties.getProperty(
						LayoutTypePortletConstants.MODE_HELP);
					String modePreview = typeSettingsProperties.getProperty(
						LayoutTypePortletConstants.MODE_PREVIEW);
					String modePrint = typeSettingsProperties.getProperty(
						LayoutTypePortletConstants.MODE_PRINT);

					layoutTypePortlet.setStateMax(stateMax);
					layoutTypePortlet.setStateMin(stateMin);
					layoutTypePortlet.setModeAbout(modeAbout);
					layoutTypePortlet.setModeConfig(modeConfig);
					layoutTypePortlet.setModeEdit(modeEdit);
					layoutTypePortlet.setModeEditDefaults(modeEditDefaults);
					layoutTypePortlet.setModeEditGuest(modeEditGuest);
					layoutTypePortlet.setModeHelp(modeHelp);
					layoutTypePortlet.setModePreview(modePreview);
					layoutTypePortlet.setModePrint(modePrint);
				}
			}

			request.setAttribute(WebKeys.LAYOUT, layout);
			request.setAttribute(WebKeys.LAYOUTS, layouts);
		}

		// Scope

		long scopeGroupId = PortalUtil.getScopeGroupId(request);

		if ((scopeGroupId <= 0) && (doAsGroupId > 0)) {
			scopeGroupId = doAsGroupId;
		}

		long siteGroupId = PortalUtil.getSiteGroupId(scopeGroupId);

		// Theme and color scheme

		Theme theme = null;
		ColorScheme colorScheme = null;

		boolean wapTheme = BrowserSnifferUtil.isWap(request);

		if ((layout != null) && group.isControlPanel()) {
			String themeId = PrefsPropsUtil.getString(
				companyId, PropsKeys.CONTROL_PANEL_LAYOUT_REGULAR_THEME_ID);
			String colorSchemeId =
				ColorSchemeFactoryUtil.getDefaultRegularColorSchemeId();

			theme = ThemeLocalServiceUtil.getTheme(
				companyId, themeId, wapTheme);
			colorScheme = ThemeLocalServiceUtil.getColorScheme(
				companyId, theme.getThemeId(), colorSchemeId, wapTheme);

			if (!wapTheme && theme.isWapTheme()) {
				theme = ThemeLocalServiceUtil.getTheme(
					companyId,
					PropsValues.CONTROL_PANEL_LAYOUT_REGULAR_THEME_ID, false);
				colorScheme = ThemeLocalServiceUtil.getColorScheme(
					companyId, theme.getThemeId(), colorSchemeId, false);
			}

			request.setAttribute(WebKeys.THEME, theme);
			request.setAttribute(WebKeys.COLOR_SCHEME, colorScheme);
		}

		boolean themeCssFastLoad = SessionParamUtil.getBoolean(
			request, "css_fast_load", PropsValues.THEME_CSS_FAST_LOAD);
		boolean themeImagesFastLoad = SessionParamUtil.getBoolean(
			request, "images_fast_load", PropsValues.THEME_IMAGES_FAST_LOAD);

		boolean themeJsBarebone = PropsValues.JAVASCRIPT_BAREBONE_ENABLED;

		if (themeJsBarebone) {
			if (signedIn) {
				themeJsBarebone = false;
			}
		}

		boolean themeJsFastLoad = SessionParamUtil.getBoolean(
			request, "js_fast_load", PropsValues.JAVASCRIPT_FAST_LOAD);

		String lifecycle = ParamUtil.getString(request, "p_p_lifecycle", "0");

		lifecycle = ParamUtil.getString(request, "p_t_lifecycle", lifecycle);

		boolean isolated = ParamUtil.getBoolean(request, "p_p_isolated");

		String facebookCanvasPageURL = (String)request.getAttribute(
			WebKeys.FACEBOOK_CANVAS_PAGE_URL);

		boolean widget = false;

		Boolean widgetObj = (Boolean)request.getAttribute(WebKeys.WIDGET);

		if (widgetObj != null) {
			widget = widgetObj.booleanValue();
		}

		// Theme display

		ThemeDisplay themeDisplay = ThemeDisplayFactory.create();

		themeDisplay.setRequest(request);

		// Set the CDN host, portal URL, and Facebook application ID first
		// because other methods (setLookAndFeel) depend on them being set

		themeDisplay.setCDNHost(cdnHost);
		themeDisplay.setCDNDynamicResourcesHost(dynamicResourcesCDNHost);
		themeDisplay.setPortalURL(portalURL);
		themeDisplay.setFacebookCanvasPageURL(facebookCanvasPageURL);
		themeDisplay.setWidget(widget);

		themeDisplay.setCompany(company);
		themeDisplay.setCompanyLogo(companyLogo);
		themeDisplay.setCompanyLogoHeight(companyLogoHeight);
		themeDisplay.setCompanyLogoWidth(companyLogoWidth);
		themeDisplay.setRealCompanyLogo(realCompanyLogo);
		themeDisplay.setRealCompanyLogoHeight(realCompanyLogoHeight);
		themeDisplay.setRealCompanyLogoWidth(realCompanyLogoWidth);
		themeDisplay.setUser(user);
		themeDisplay.setRealUser(realUser);
		themeDisplay.setDoAsUserId(doAsUserId);
		themeDisplay.setDoAsUserLanguageId(doAsUserLanguageId);
		themeDisplay.setDoAsGroupId(doAsGroupId);
		themeDisplay.setRefererGroupId(refererGroupId);
		themeDisplay.setRefererPlid(refererPlid);
		themeDisplay.setControlPanelCategory(controlPanelCategory);
		themeDisplay.setLayoutSet(layoutSet);
		themeDisplay.setLayoutSetLogo(layoutSetLogo);
		themeDisplay.setLayout(layout);
		themeDisplay.setLayouts(layouts);
		themeDisplay.setUnfilteredLayouts(unfilteredLayouts);
		themeDisplay.setPlid(plid);
		themeDisplay.setPpid(ppid);
		themeDisplay.setLayoutTypePortlet(layoutTypePortlet);
		themeDisplay.setScopeGroupId(scopeGroupId);
		themeDisplay.setSiteGroupId(siteGroupId);
		themeDisplay.setSignedIn(signedIn);
		themeDisplay.setPermissionChecker(permissionChecker);
		themeDisplay.setLocale(locale);
		themeDisplay.setLanguageId(LocaleUtil.toLanguageId(locale));
		themeDisplay.setI18nLanguageId(i18nLanguageId);
		themeDisplay.setI18nPath(i18nPath);
		themeDisplay.setTimeZone(timeZone);
		themeDisplay.setLookAndFeel(theme, colorScheme);
		themeDisplay.setThemeCssFastLoad(themeCssFastLoad);
		themeDisplay.setThemeImagesFastLoad(themeImagesFastLoad);
		themeDisplay.setThemeJsBarebone(themeJsBarebone);
		themeDisplay.setThemeJsFastLoad(themeJsFastLoad);
		themeDisplay.setServerName(request.getServerName());
		themeDisplay.setServerPort(request.getServerPort());
		themeDisplay.setSecure(request.isSecure());
		themeDisplay.setLifecycle(lifecycle);
		themeDisplay.setLifecycleAction(lifecycle.equals("1"));
		themeDisplay.setLifecycleEvent(lifecycle.equals("3"));
		themeDisplay.setLifecycleRender(lifecycle.equals("0"));
		themeDisplay.setLifecycleResource(lifecycle.equals("2"));
		themeDisplay.setStateExclusive(LiferayWindowState.isExclusive(request));
		themeDisplay.setStateMaximized(LiferayWindowState.isMaximized(request));
		themeDisplay.setStatePopUp(LiferayWindowState.isPopUp(request));
		themeDisplay.setIsolated(isolated);
		themeDisplay.setPathApplet(contextPath.concat("/applets"));
		themeDisplay.setPathCms(contextPath.concat("/cms"));
		themeDisplay.setPathContext(contextPath);
		themeDisplay.setPathFlash(contextPath.concat("/flash"));
		themeDisplay.setPathFriendlyURLPrivateGroup(
			friendlyURLPrivateGroupPath);
		themeDisplay.setPathFriendlyURLPrivateUser(friendlyURLPrivateUserPath);
		themeDisplay.setPathFriendlyURLPublic(friendlyURLPublicPath);
		themeDisplay.setPathImage(imagePath);
		themeDisplay.setPathJavaScript(contextPath.concat("/html/js"));
		themeDisplay.setPathMain(mainPath);
		themeDisplay.setPathSound(contextPath.concat("/html/sound"));

		// Icons

		themeDisplay.setShowAddContentIcon(false);
		themeDisplay.setShowControlPanelIcon(signedIn);
		themeDisplay.setShowHomeIcon(true);
		themeDisplay.setShowMyAccountIcon(signedIn);
		themeDisplay.setShowPageSettingsIcon(false);
		themeDisplay.setShowPortalIcon(true);
		themeDisplay.setShowSignInIcon(!signedIn);
		themeDisplay.setShowSignOutIcon(signedIn);

		boolean showSiteContentIcon = false;

		long controlPanelPlid = 0;

		if (signedIn && PropsValues.DOCKBAR_SHOW_SITE_CONTENT_ICON) {
			controlPanelPlid = PortalUtil.getControlPanelPlid(companyId);

			List<Portlet> siteContentPortlets =
				PortalUtil.getControlPanelPortlets(
					PortletCategoryKeys.CONTENT, themeDisplay);

			Portlet groupPagesPortlet = PortletLocalServiceUtil.getPortletById(
				PortletKeys.GROUP_PAGES);

			siteContentPortlets.remove(groupPagesPortlet);

			Portlet siteMembershipsAdminPortlet =
				PortletLocalServiceUtil.getPortletById(
					PortletKeys.SITE_MEMBERSHIPS_ADMIN);

			siteContentPortlets.remove(siteMembershipsAdminPortlet);

			Portlet siteSettingsPortlet =
				PortletLocalServiceUtil.getPortletById(
					PortletKeys.SITE_SETTINGS);

			siteContentPortlets.remove(siteSettingsPortlet);

			showSiteContentIcon =
				PortletPermissionUtil.hasControlPanelAccessPermission(
					permissionChecker, scopeGroupId, siteContentPortlets);
		}

		themeDisplay.setShowSiteContentIcon(showSiteContentIcon);

		themeDisplay.setShowStagingIcon(false);

		// Session

		if (PropsValues.SESSION_ENABLE_URL_WITH_SESSION_ID &&
			!CookieKeys.hasSessionId(request)) {

			themeDisplay.setAddSessionIdToURL(true);
			themeDisplay.setSessionId(session.getId());
		}

		// URLs

		String urlControlPanel = friendlyURLPrivateGroupPath.concat(
			GroupConstants.CONTROL_PANEL_FRIENDLY_URL);

		if (Validator.isNotNull(doAsUserId)) {
			urlControlPanel = HttpUtil.addParameter(
				urlControlPanel, "doAsUserId", doAsUserId);
		}

		if (scopeGroupId > 0) {
			urlControlPanel = HttpUtil.addParameter(
				urlControlPanel, "doAsGroupId", scopeGroupId);
		}

		if (refererGroupId > 0) {
			urlControlPanel = HttpUtil.addParameter(
				urlControlPanel, "refererGroupId", refererGroupId);
		}
		else if (scopeGroupId > 0) {
			Layout refererLayout = LayoutLocalServiceUtil.fetchLayout(plid);

			if (refererLayout != null) {
				Group refererLayoutGroup = refererLayout.getGroup();

				if (refererLayoutGroup.isUserGroup()) {
					urlControlPanel = HttpUtil.addParameter(
						urlControlPanel, "refererGroupId", scopeGroupId);
				}
			}
		}

		if (refererPlid > 0) {
			urlControlPanel = HttpUtil.addParameter(
				urlControlPanel, "refererPlid", refererPlid);
		}
		else if (plid > 0) {
			urlControlPanel = HttpUtil.addParameter(
				urlControlPanel, "refererPlid", plid);
		}

		if (themeDisplay.isAddSessionIdToURL()) {
			urlControlPanel = PortalUtil.getURLWithSessionId(
				urlControlPanel, session.getId());
		}

		themeDisplay.setURLControlPanel(urlControlPanel);

		String siteContentURL = urlControlPanel;

		siteContentURL = HttpUtil.addParameter(
			siteContentURL, "controlPanelCategory",
			PortletCategoryKeys.CONTENT);

		themeDisplay.setURLSiteContent(siteContentURL);

		String currentURL = PortalUtil.getCurrentURL(request);

		themeDisplay.setURLCurrent(currentURL);

		String urlHome = PortalUtil.getHomeURL(request);

		themeDisplay.setURLHome(urlHome);

		if (layout != null) {
			if (layout.isTypePortlet()) {
				boolean freeformLayout =
					layoutTypePortlet.getLayoutTemplateId().equals("freeform");

				themeDisplay.setFreeformLayout(freeformLayout);

				if (hasUpdateLayoutPermission) {
					themeDisplay.setShowAddContentIconPermission(true);

					if (!LiferayWindowState.isMaximized(request)) {
						themeDisplay.setShowAddContentIcon(true);
					}

					themeDisplay.setShowLayoutTemplatesIcon(true);

					if (!group.isUser()) {
						themeDisplay.setShowPageCustomizationIcon(true);
					}

					themeDisplay.setURLAddContent(
						"Liferay.LayoutConfiguration.toggle('".concat(
							PortletKeys.LAYOUT_CONFIGURATION).concat("');"));
				}

				if (hasCustomizeLayoutPermission && customizedView) {
					themeDisplay.setShowAddContentIconPermission(true);

					if (!LiferayWindowState.isMaximized(request)) {
						themeDisplay.setShowAddContentIcon(true);
					}

					themeDisplay.setURLAddContent(
						"Liferay.LayoutConfiguration.toggle('".concat(
							PortletKeys.LAYOUT_CONFIGURATION).concat("');"));
				}
			}

			if (hasUpdateLayoutPermission) {
				themeDisplay.setShowPageSettingsIcon(true);

				LiferayPortletURL pageSettingsURL = new PortletURLImpl(
					request, PortletKeys.LAYOUTS_ADMIN, controlPanelPlid,
					PortletRequest.RENDER_PHASE);

				pageSettingsURL.setDoAsGroupId(scopeGroupId);
				pageSettingsURL.setParameter(
					"struts_action", "/layouts_admin/edit_layouts");

				if (layout.isPrivateLayout()) {
					pageSettingsURL.setParameter("tabs1", "private-pages");
				}
				else {
					pageSettingsURL.setParameter("tabs1", "public-pages");
				}

				pageSettingsURL.setParameter(
					"groupId", String.valueOf(scopeGroupId));
				pageSettingsURL.setParameter("selPlid", String.valueOf(plid));
				pageSettingsURL.setPortletMode(PortletMode.VIEW);

				if (PropsValues.DOCKBAR_ADMINISTRATIVE_LINKS_SHOW_IN_POP_UP) {
					pageSettingsURL.setControlPanelCategory(
						_CONTROL_PANEL_CATEGORY_PORTLET_PREFIX +
							PortletKeys.LAYOUTS_ADMIN);
					pageSettingsURL.setParameter("closeRedirect", currentURL);
					pageSettingsURL.setWindowState(LiferayWindowState.POP_UP);
				}
				else {
					pageSettingsURL.setPlid(plid);
					pageSettingsURL.setParameter(
						"redirect", themeDisplay.getURLHome());
					pageSettingsURL.setWindowState(WindowState.MAXIMIZED);
				}

				themeDisplay.setURLPageSettings(pageSettingsURL);

				boolean site = group.isSite();

				if (!site && group.isStagingGroup()) {
					Group liveGroup = group.getLiveGroup();

					site = liveGroup.isSite();
				}

				if (site &&
					GroupPermissionUtil.contains(
						permissionChecker, scopeGroupId,
						ActionKeys.ASSIGN_MEMBERS)) {

					themeDisplay.setShowManageSiteMembershipsIcon(true);

					LiferayPortletURL manageSiteMembershipsURL =
						new PortletURLImpl(
							request, PortletKeys.SITE_MEMBERSHIPS_ADMIN,
							controlPanelPlid, PortletRequest.RENDER_PHASE);

					manageSiteMembershipsURL.setDoAsGroupId(scopeGroupId);
					manageSiteMembershipsURL.setParameter(
						"struts_action", "/sites_admin/edit_site_assignments");
					manageSiteMembershipsURL.setParameter(
						"groupId", String.valueOf(scopeGroupId));
					manageSiteMembershipsURL.setParameter(
						"selPlid", String.valueOf(plid));
					manageSiteMembershipsURL.setPortletMode(PortletMode.VIEW);

					if (PropsValues.
							DOCKBAR_ADMINISTRATIVE_LINKS_SHOW_IN_POP_UP) {

						manageSiteMembershipsURL.setControlPanelCategory(
							_CONTROL_PANEL_CATEGORY_PORTLET_PREFIX +
								PortletKeys.SITE_MEMBERSHIPS_ADMIN);
						manageSiteMembershipsURL.setWindowState(
							LiferayWindowState.POP_UP);
					}
					else {
						manageSiteMembershipsURL.setPlid(plid);
						manageSiteMembershipsURL.setParameter(
							"redirect", themeDisplay.getURLHome());
						manageSiteMembershipsURL.setParameter(
							"showBackURL", Boolean.FALSE.toString());
						manageSiteMembershipsURL.setWindowState(
							WindowState.MAXIMIZED);
					}

					themeDisplay.setURLManageSiteMemberships(
						manageSiteMembershipsURL);
				}
				else {
					themeDisplay.setShowManageSiteMembershipsIcon(false);
				}
			}

			boolean hasAddLayoutGroupPermission = GroupPermissionUtil.contains(
				permissionChecker, scopeGroupId, ActionKeys.ADD_LAYOUT);
			boolean hasAddLayoutLayoutPermission =
				LayoutPermissionUtil.contains(
					permissionChecker, layout, ActionKeys.ADD_LAYOUT);
			boolean hasManageLayoutsGroupPermission =
				GroupPermissionUtil.contains(
					permissionChecker, scopeGroupId, ActionKeys.MANAGE_LAYOUTS);
			boolean hasManageStagingPermission = GroupPermissionUtil.contains(
				permissionChecker, scopeGroupId, ActionKeys.MANAGE_STAGING);
			boolean hasPublishStagingPermission = GroupPermissionUtil.contains(
				permissionChecker, scopeGroupId, ActionKeys.PUBLISH_STAGING);
			boolean hasUpdateGroupPermission = GroupPermissionUtil.contains(
				permissionChecker, scopeGroupId, ActionKeys.UPDATE);
			boolean hasViewStagingPermission = GroupPermissionUtil.contains(
				permissionChecker, scopeGroupId, ActionKeys.VIEW_STAGING);

			if (!group.isControlPanel() && !group.isUser() &&
				!group.isUserGroup() && hasUpdateGroupPermission) {

				themeDisplay.setShowSiteSettingsIcon(true);

				LiferayPortletURL siteSettingsURL = new PortletURLImpl(
					request, PortletKeys.SITE_SETTINGS, controlPanelPlid,
					PortletRequest.RENDER_PHASE);

				siteSettingsURL.setDoAsGroupId(scopeGroupId);
				siteSettingsURL.setParameter(
					"struts_action", "/sites_admin/edit_site");
				siteSettingsURL.setParameter(
					"groupId", String.valueOf(scopeGroupId));
				siteSettingsURL.setParameter(
					"showBackURL", Boolean.FALSE.toString());
				siteSettingsURL.setPortletMode(PortletMode.VIEW);

				if (PropsValues.DOCKBAR_ADMINISTRATIVE_LINKS_SHOW_IN_POP_UP) {
					siteSettingsURL.setControlPanelCategory(
						_CONTROL_PANEL_CATEGORY_PORTLET_PREFIX +
							PortletKeys.SITE_SETTINGS);
					siteSettingsURL.setParameter("closeRedirect", currentURL);
					siteSettingsURL.setWindowState(LiferayWindowState.POP_UP);
				}
				else {
					siteSettingsURL.setPlid(plid);
					siteSettingsURL.setParameter(
						"redirect", themeDisplay.getURLHome());
					siteSettingsURL.setWindowState(
						LiferayWindowState.MAXIMIZED);
				}

				themeDisplay.setURLSiteSettings(siteSettingsURL);
			}

			if (!group.isLayoutPrototype() &&
				(hasAddLayoutGroupPermission || hasAddLayoutLayoutPermission ||
				 hasManageLayoutsGroupPermission || hasUpdateGroupPermission)) {

				themeDisplay.setShowSiteMapSettingsIcon(true);

				LiferayPortletURL siteMapSettingsURL = new PortletURLImpl(
					request, PortletKeys.LAYOUTS_ADMIN, controlPanelPlid,
					PortletRequest.RENDER_PHASE);

				siteMapSettingsURL.setDoAsGroupId(scopeGroupId);
				siteMapSettingsURL.setParameter(
					"struts_action", "/layouts_admin/edit_layouts");

				if (layout.isPrivateLayout()) {
					siteMapSettingsURL.setParameter("tabs1", "private-pages");
				}
				else {
					siteMapSettingsURL.setParameter("tabs1", "public-pages");
				}

				siteMapSettingsURL.setParameter(
					"groupId", String.valueOf(scopeGroupId));
				siteMapSettingsURL.setPortletMode(PortletMode.VIEW);

				if (PropsValues.DOCKBAR_ADMINISTRATIVE_LINKS_SHOW_IN_POP_UP) {
					siteMapSettingsURL.setControlPanelCategory(
						_CONTROL_PANEL_CATEGORY_PORTLET_PREFIX +
							PortletKeys.LAYOUTS_ADMIN);
					siteMapSettingsURL.setParameter(
						"closeRedirect", currentURL);
					siteMapSettingsURL.setWindowState(
						LiferayWindowState.POP_UP);
				}
				else {
					siteMapSettingsURL.setPlid(plid);
					siteMapSettingsURL.setParameter(
						"redirect", themeDisplay.getURLHome());
					siteMapSettingsURL.setWindowState(
						LiferayWindowState.MAXIMIZED);
				}

				themeDisplay.setURLSiteMapSettings(siteMapSettingsURL);
			}

			if (group.hasStagingGroup() && !group.isStagingGroup()) {
				themeDisplay.setShowAddContentIcon(false);
				themeDisplay.setShowLayoutTemplatesIcon(false);
				themeDisplay.setShowPageSettingsIcon(false);
				themeDisplay.setURLPublishToLive(null);
			}

			if (group.isControlPanel()) {
				themeDisplay.setShowPageSettingsIcon(false);
				themeDisplay.setURLPublishToLive(null);
			}

			// LEP-4987

			if (group.isStaged() || group.isStagingGroup()) {
				if (hasManageStagingPermission || hasPublishStagingPermission ||
					hasUpdateLayoutPermission || hasViewStagingPermission) {

					themeDisplay.setShowStagingIcon(true);
				}

				if (hasPublishStagingPermission) {
					PortletURL publishToLiveURL = new PortletURLImpl(
						request, PortletKeys.LAYOUTS_ADMIN, plid,
						PortletRequest.RENDER_PHASE);

					publishToLiveURL.setParameter(
						"struts_action", "/layouts_admin/publish_layouts");

					if (layout.isPrivateLayout()) {
						publishToLiveURL.setParameter("tabs1", "private-pages");
					}
					else {
						publishToLiveURL.setParameter("tabs1", "public-pages");
					}

					publishToLiveURL.setParameter("pagesRedirect", currentURL);
					publishToLiveURL.setParameter(
						"groupId", String.valueOf(scopeGroupId));
					publishToLiveURL.setParameter(
						"selPlid", String.valueOf(plid));
					publishToLiveURL.setPortletMode(PortletMode.VIEW);
					publishToLiveURL.setWindowState(
						LiferayWindowState.EXCLUSIVE);

					themeDisplay.setURLPublishToLive(publishToLiveURL);
				}
			}

			PortletURLImpl myAccountURL = new PortletURLImpl(
				request, PortletKeys.MY_ACCOUNT, controlPanelPlid,
				PortletRequest.RENDER_PHASE);

			if (scopeGroupId > 0) {
				myAccountURL.setDoAsGroupId(scopeGroupId);
			}

			myAccountURL.setParameter("struts_action", "/my_account/edit_user");
			myAccountURL.setPortletMode(PortletMode.VIEW);

			if (refererPlid > 0) {
				myAccountURL.setRefererPlid(refererPlid);
			}
			else {
				myAccountURL.setRefererPlid(plid);
			}

			myAccountURL.setWindowState(WindowState.MAXIMIZED);

			themeDisplay.setURLMyAccount(myAccountURL);
		}

		if (!user.isActive() ||
			(PrefsPropsUtil.getBoolean(
				companyId, PropsKeys.TERMS_OF_USE_REQUIRED) &&
			 !user.isAgreedToTermsOfUse())) {

			themeDisplay.setShowAddContentIcon(false);
			themeDisplay.setShowMyAccountIcon(false);
			themeDisplay.setShowPageSettingsIcon(false);
		}

		if (layout.isLayoutPrototypeLinkActive()) {
			themeDisplay.setShowPageCustomizationIcon(false);
		}

		if (group.isLayoutPrototype()) {
			themeDisplay.setShowControlPanelIcon(false);
			themeDisplay.setShowHomeIcon(false);
			themeDisplay.setShowManageSiteMembershipsIcon(false);
			themeDisplay.setShowMyAccountIcon(false);
			themeDisplay.setShowPageCustomizationIcon(false);
			themeDisplay.setShowPageSettingsIcon(true);
			themeDisplay.setShowPortalIcon(false);
			themeDisplay.setShowSignInIcon(false);
			themeDisplay.setShowSignOutIcon(false);
			themeDisplay.setShowSiteContentIcon(false);
			themeDisplay.setShowSiteSettingsIcon(false);
			themeDisplay.setShowStagingIcon(false);
		}

		if (group.isLayoutSetPrototype()) {
			themeDisplay.setShowPageCustomizationIcon(false);
			themeDisplay.setShowSiteSettingsIcon(false);
		}

		if (group.hasStagingGroup() && !group.isStagingGroup()) {
			themeDisplay.setShowLayoutTemplatesIcon(false);
			themeDisplay.setShowPageCustomizationIcon(false);
			themeDisplay.setShowPageSettingsIcon(false);
			themeDisplay.setShowSiteContentIcon(false);
			themeDisplay.setShowSiteMapSettingsIcon(false);
			themeDisplay.setShowSiteSettingsIcon(false);
		}

		themeDisplay.setURLPortal(portalURL.concat(contextPath));

		String urlSignIn = mainPath.concat(_PATH_PORTAL_LOGIN);

		if (layout != null) {
			urlSignIn = HttpUtil.addParameter(
				urlSignIn, "p_l_id", layout.getPlid());
		}

		themeDisplay.setURLSignIn(urlSignIn);

		themeDisplay.setURLSignOut(mainPath.concat(_PATH_PORTAL_LOGOUT));

		PortletURL updateManagerURL = new PortletURLImpl(
			request, PortletKeys.UPDATE_MANAGER, plid,
			PortletRequest.RENDER_PHASE);

		updateManagerURL.setParameter("struts_action", "/update_manager/view");
		updateManagerURL.setPortletMode(PortletMode.VIEW);
		updateManagerURL.setWindowState(WindowState.MAXIMIZED);

		themeDisplay.setURLUpdateManager(updateManagerURL);

		return themeDisplay;
	}

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
		throws ActionException {

		StopWatch stopWatch = null;

		if (_log.isDebugEnabled()) {
			stopWatch = new StopWatch();

			stopWatch.start();
		}

		try {
			servicePre(request, response);
		}
		catch (Exception e) {
			throw new ActionException(e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Running takes " + stopWatch.getTime() + " ms");
		}
	}

	protected void addDefaultLayoutsByLAR(
			long userId, long groupId, boolean privateLayout, File larFile)
		throws PortalException, SystemException {

		Map<String, String[]> parameterMap = new HashMap<String, String[]>();

		parameterMap.put(
			PortletDataHandlerKeys.PERMISSIONS,
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_DATA,
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_DATA_CONTROL_DEFAULT,
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_SETUP,
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.THEME_REFERENCE,
			new String[] {Boolean.TRUE.toString()});

		LayoutLocalServiceUtil.importLayouts(
			userId, groupId, privateLayout, parameterMap, larFile);
	}

	protected void addDefaultUserPrivateLayoutByProperties(
			long userId, long groupId)
		throws PortalException, SystemException {

		String friendlyURL = getFriendlyURL(
			PropsValues.DEFAULT_USER_PRIVATE_LAYOUT_FRIENDLY_URL);

		ServiceContext serviceContext = new ServiceContext();

		Layout layout = LayoutLocalServiceUtil.addLayout(
			userId, groupId, true, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			PropsValues.DEFAULT_USER_PRIVATE_LAYOUT_NAME, StringPool.BLANK,
			StringPool.BLANK, LayoutConstants.TYPE_PORTLET, false, friendlyURL,
			serviceContext);

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		layoutTypePortlet.setLayoutTemplateId(
			0, PropsValues.DEFAULT_USER_PRIVATE_LAYOUT_TEMPLATE_ID, false);

		for (int i = 0; i < 10; i++) {
			String columnId = "column-" + i;
			String portletIds = PropsUtil.get(
				PropsKeys.DEFAULT_USER_PRIVATE_LAYOUT_COLUMN + i);

			String[] portletIdsArray = StringUtil.split(portletIds);

			layoutTypePortlet.addPortletIds(
				0, portletIdsArray, columnId, false);
		}

		LayoutLocalServiceUtil.updateLayout(
			layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(),
			layout.getTypeSettings());

		boolean updateLayoutSet = false;

		LayoutSet layoutSet = layout.getLayoutSet();

		if (Validator.isNotNull(
				PropsValues.DEFAULT_USER_PRIVATE_LAYOUT_REGULAR_THEME_ID)) {

			layoutSet.setThemeId(
				PropsValues.DEFAULT_USER_PRIVATE_LAYOUT_REGULAR_THEME_ID);

			updateLayoutSet = true;
		}

		if (Validator.isNotNull(
				PropsValues.
					DEFAULT_USER_PRIVATE_LAYOUT_REGULAR_COLOR_SCHEME_ID)) {

			layoutSet.setColorSchemeId(
				PropsValues.
					DEFAULT_USER_PRIVATE_LAYOUT_REGULAR_COLOR_SCHEME_ID);

			updateLayoutSet = true;
		}

		if (Validator.isNotNull(
				PropsValues.DEFAULT_USER_PRIVATE_LAYOUT_WAP_THEME_ID)) {

			layoutSet.setWapThemeId(
				PropsValues.DEFAULT_USER_PRIVATE_LAYOUT_WAP_THEME_ID);

			updateLayoutSet = true;
		}

		if (Validator.isNotNull(
				PropsValues.DEFAULT_USER_PRIVATE_LAYOUT_WAP_COLOR_SCHEME_ID)) {

			layoutSet.setWapColorSchemeId(
				PropsValues.DEFAULT_USER_PRIVATE_LAYOUT_WAP_COLOR_SCHEME_ID);

			updateLayoutSet = true;
		}

		if (updateLayoutSet) {
			LayoutSetLocalServiceUtil.updateLayoutSet(layoutSet);
		}
	}

	protected void addDefaultUserPrivateLayouts(User user)
		throws PortalException, SystemException {

		Group userGroup = user.getGroup();

		if (privateLARFile != null) {
			addDefaultLayoutsByLAR(
				user.getUserId(), userGroup.getGroupId(), true, privateLARFile);
		}
		else {
			addDefaultUserPrivateLayoutByProperties(
				user.getUserId(), userGroup.getGroupId());
		}
	}

	protected void addDefaultUserPublicLayoutByProperties(
			long userId, long groupId)
		throws PortalException, SystemException {

		String friendlyURL = getFriendlyURL(
			PropsValues.DEFAULT_USER_PUBLIC_LAYOUT_FRIENDLY_URL);

		ServiceContext serviceContext = new ServiceContext();

		Layout layout = LayoutLocalServiceUtil.addLayout(
			userId, groupId, false, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			PropsValues.DEFAULT_USER_PUBLIC_LAYOUT_NAME, StringPool.BLANK,
			StringPool.BLANK, LayoutConstants.TYPE_PORTLET, false, friendlyURL,
			serviceContext);

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		layoutTypePortlet.setLayoutTemplateId(
			0, PropsValues.DEFAULT_USER_PUBLIC_LAYOUT_TEMPLATE_ID, false);

		for (int i = 0; i < 10; i++) {
			String columnId = "column-" + i;
			String portletIds = PropsUtil.get(
				PropsKeys.DEFAULT_USER_PUBLIC_LAYOUT_COLUMN + i);

			String[] portletIdsArray = StringUtil.split(portletIds);

			layoutTypePortlet.addPortletIds(
				0, portletIdsArray, columnId, false);
		}

		LayoutLocalServiceUtil.updateLayout(
			layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(),
			layout.getTypeSettings());

		boolean updateLayoutSet = false;

		LayoutSet layoutSet = layout.getLayoutSet();

		if (Validator.isNotNull(
				PropsValues.DEFAULT_USER_PUBLIC_LAYOUT_REGULAR_THEME_ID)) {

			layoutSet.setThemeId(
				PropsValues.DEFAULT_USER_PUBLIC_LAYOUT_REGULAR_THEME_ID);

			updateLayoutSet = true;
		}

		if (Validator.isNotNull(
				PropsValues.
					DEFAULT_USER_PUBLIC_LAYOUT_REGULAR_COLOR_SCHEME_ID)) {

			layoutSet.setColorSchemeId(
				PropsValues.DEFAULT_USER_PUBLIC_LAYOUT_REGULAR_COLOR_SCHEME_ID);

			updateLayoutSet = true;
		}

		if (Validator.isNotNull(
				PropsValues.DEFAULT_USER_PUBLIC_LAYOUT_WAP_THEME_ID)) {

			layoutSet.setWapThemeId(
				PropsValues.DEFAULT_USER_PUBLIC_LAYOUT_WAP_THEME_ID);

			updateLayoutSet = true;
		}

		if (Validator.isNotNull(
				PropsValues.DEFAULT_USER_PUBLIC_LAYOUT_WAP_COLOR_SCHEME_ID)) {

			layoutSet.setWapColorSchemeId(
				PropsValues.DEFAULT_USER_PUBLIC_LAYOUT_WAP_COLOR_SCHEME_ID);

			updateLayoutSet = true;
		}

		if (updateLayoutSet) {
			LayoutSetLocalServiceUtil.updateLayoutSet(layoutSet);
		}
	}

	protected void addDefaultUserPublicLayouts(User user)
		throws PortalException, SystemException {

		Group userGroup = user.getGroup();

		if (publicLARFile != null) {
			addDefaultLayoutsByLAR(
				user.getUserId(), userGroup.getGroupId(), false, publicLARFile);
		}
		else {
			addDefaultUserPublicLayoutByProperties(
				user.getUserId(), userGroup.getGroupId());
		}
	}

	protected void deleteDefaultUserPrivateLayouts(User user)
		throws PortalException, SystemException {

		Group userGroup = user.getGroup();

		ServiceContext serviceContext = new ServiceContext();

		LayoutLocalServiceUtil.deleteLayouts(
			userGroup.getGroupId(), true, serviceContext);
	}

	protected void deleteDefaultUserPublicLayouts(User user)
		throws PortalException, SystemException {

		Group userGroup = user.getGroup();

		ServiceContext serviceContext = new ServiceContext();

		LayoutLocalServiceUtil.deleteLayouts(
			userGroup.getGroupId(), false, serviceContext);
	}

	protected Object[] getDefaultLayout(
			HttpServletRequest request, User user, boolean signedIn)
		throws PortalException, SystemException {

		Layout layout = null;
		List<Layout> layouts = null;

		if (signedIn) {

			// Check the user's personal layouts

			Group userGroup = user.getGroup();

			layouts = LayoutLocalServiceUtil.getLayouts(
				userGroup.getGroupId(), true,
				LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

			if (layouts.size() == 0) {
				layouts = LayoutLocalServiceUtil.getLayouts(
					userGroup.getGroupId(), false,
					LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);
			}

			if (layouts.size() > 0) {
				layout = layouts.get(0);
			}

			// Check the user's sites

			if (layout == null) {
				LinkedHashMap<String, Object> groupParams =
					new LinkedHashMap<String, Object>();

				groupParams.put("usersGroups", new Long(user.getUserId()));

				List<Group> groups = GroupLocalServiceUtil.search(
					user.getCompanyId(), null, null, groupParams,
					QueryUtil.ALL_POS, QueryUtil.ALL_POS);

				for (Group group : groups) {
					layouts = LayoutLocalServiceUtil.getLayouts(
						group.getGroupId(), true,
						LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

					if (layouts.size() == 0) {
						layouts = LayoutLocalServiceUtil.getLayouts(
							group.getGroupId(), false,
							LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);
					}

					if (layouts.size() > 0) {
						layout = layouts.get(0);

						break;
					}
				}
			}
		}
		else {

			// Check the virtual host

			LayoutSet layoutSet = (LayoutSet)request.getAttribute(
				WebKeys.VIRTUAL_HOST_LAYOUT_SET);

			if (layoutSet != null) {
				layouts = LayoutLocalServiceUtil.getLayouts(
					layoutSet.getGroupId(), layoutSet.isPrivateLayout(),
					LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

				if (layouts.size() > 0) {
					layout = layouts.get(0);
				}
			}
		}

		if ((layout == null) || layout.isPrivateLayout()) {

			// Check the Guest site

			Group guestGroup = GroupLocalServiceUtil.getGroup(
				user.getCompanyId(), GroupConstants.GUEST);

			layouts = LayoutLocalServiceUtil.getLayouts(
				guestGroup.getGroupId(), false,
				LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

			if (layouts.size() > 0) {
				layout = layouts.get(0);
			}
		}

		return new Object[] {layout, layouts};
	}

	protected String getFriendlyURL(String friendlyURL) {
		friendlyURL = GetterUtil.getString(friendlyURL);

		return FriendlyURLNormalizerUtil.normalize(friendlyURL);
	}

	protected Object[] getViewableLayouts(
			HttpServletRequest request, User user,
			PermissionChecker permissionChecker, Layout layout,
			List<Layout> layouts)
		throws PortalException, SystemException {

		if ((layouts == null) || layouts.isEmpty()) {
			return new Object[] {layout, layouts};
		}

		Group group = layout.getGroup();

		boolean hasViewLayoutPermission = false;
		boolean hasViewStagingPermission =
			(group.isStagingGroup() || group.isStagedRemotely()) &&
			 GroupPermissionUtil.contains(
				 permissionChecker, group.getGroupId(),
				 ActionKeys.VIEW_STAGING);

		if (LayoutPermissionUtil.contains(
				permissionChecker, layout, false, ActionKeys.VIEW) ||
			hasViewStagingPermission) {

			hasViewLayoutPermission = true;
		}

		List<Layout> accessibleLayouts = new ArrayList<Layout>();

		for (int i = 0; i < layouts.size(); i++) {
			Layout curLayout = layouts.get(i);

			if (!curLayout.isHidden() &&
				(LayoutPermissionUtil.contains(
					permissionChecker, curLayout, false, ActionKeys.VIEW) ||
				 hasViewStagingPermission)) {

				if (accessibleLayouts.isEmpty() && !hasViewLayoutPermission) {
					layout = curLayout;
				}

				accessibleLayouts.add(curLayout);
			}
		}

		if (accessibleLayouts.isEmpty()) {
			layouts = null;

			if (!hasViewLayoutPermission) {
				SessionErrors.add(
					request, LayoutPermissionException.class.getName());
			}
		}
		else {
			layouts = accessibleLayouts;
		}

		return new Object[] {layout, layouts};
	}

	protected Boolean hasPowerUserRole(User user) throws Exception {
		return RoleLocalServiceUtil.hasUserRole(
			user.getUserId(), user.getCompanyId(), RoleConstants.POWER_USER,
			true);
	}

	protected void initImportLARFiles() {
		String privateLARFileName =
			PropsValues.DEFAULT_USER_PRIVATE_LAYOUTS_LAR;

		if (_log.isDebugEnabled()) {
			_log.debug("Reading private LAR file " + privateLARFileName);
		}

		if (Validator.isNotNull(privateLARFileName)) {
			privateLARFile = new File(privateLARFileName);

			if (!privateLARFile.exists()) {
				_log.error(
					"Private LAR file " + privateLARFile + " does not exist");

				privateLARFile = null;
			}
			else {
				if (_log.isDebugEnabled()) {
					_log.debug("Using private LAR file " + privateLARFileName);
				}
			}
		}

		String publicLARFileName = PropsValues.DEFAULT_USER_PUBLIC_LAYOUTS_LAR;

		if (_log.isDebugEnabled()) {
			_log.debug("Reading public LAR file " + publicLARFileName);
		}

		if (Validator.isNotNull(publicLARFileName)) {
			publicLARFile = new File(publicLARFileName);

			if (!publicLARFile.exists()) {
				_log.error(
					"Public LAR file " + publicLARFile + " does not exist");

				publicLARFile = null;
			}
			else {
				if (_log.isDebugEnabled()) {
					_log.debug("Using public LAR file " + publicLARFileName);
				}
			}
		}
	}

	protected User initUser(HttpServletRequest request) throws Exception {
		try {
			User user = PortalUtil.getUser(request);

			if (user != null) {
				return user;
			}
		}
		catch (NoSuchUserException nsue) {
			if (_log.isWarnEnabled()) {
				_log.warn(nsue.getMessage());
			}

			long userId = PortalUtil.getUserId(request);

			if (userId > 0) {
				HttpSession session = request.getSession();

				session.invalidate();
			}

			throw nsue;
		}

		Company company = PortalUtil.getCompany(request);

		return company.getDefaultUser();
	}

	protected boolean isLoginRequest(HttpServletRequest request) {
		String requestURI = request.getRequestURI();

		String mainPath = PortalUtil.getPathMain();

		if (requestURI.startsWith(mainPath.concat(_PATH_PORTAL_LOGIN))) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @deprecated As of 6.1.0
	 */
	protected boolean isViewableCommunity(
			User user, long groupId, boolean privateLayout,
			PermissionChecker permissionChecker)
		throws PortalException, SystemException {

		return LayoutPermissionUtil.contains(
			permissionChecker, groupId, privateLayout, 0, ActionKeys.VIEW);
	}

	/**
	 * @deprecated As of 6.1.0
	 */
	protected boolean isViewableGroup(
			User user, long groupId, boolean privateLayout, long layoutId,
			String controlPanelCategory, PermissionChecker permissionChecker)
		throws PortalException, SystemException {

		return LayoutPermissionUtil.contains(
			permissionChecker, groupId, privateLayout, layoutId,
			controlPanelCategory, ActionKeys.VIEW);
	}

	protected List<Layout> mergeAdditionalLayouts(
			HttpServletRequest request, User user,
			PermissionChecker permissionChecker, Layout layout,
			List<Layout> layouts)
		throws PortalException, SystemException {

		if ((layout == null) || layout.isPrivateLayout()) {
			return layouts;
		}

		long layoutGroupId = layout.getGroupId();

		Group guestGroup = GroupLocalServiceUtil.getGroup(
			user.getCompanyId(), GroupConstants.GUEST);

		if (layoutGroupId != guestGroup.getGroupId()) {
			Group layoutGroup = GroupLocalServiceUtil.getGroup(layoutGroupId);

			UnicodeProperties typeSettingsProperties =
				layoutGroup.getTypeSettingsProperties();

			boolean mergeGuestPublicPages = GetterUtil.getBoolean(
				typeSettingsProperties.getProperty("mergeGuestPublicPages"));

			if (!mergeGuestPublicPages) {
				return layouts;
			}

			List<Layout> guestLayouts = LayoutLocalServiceUtil.getLayouts(
				guestGroup.getGroupId(), false,
				LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

			Object[] viewableLayouts = getViewableLayouts(
				request, user, permissionChecker, layout, guestLayouts);

			guestLayouts = (List<Layout>)viewableLayouts[1];

			if (layouts == null) {
				return guestLayouts;
			}

			layouts.addAll(0, guestLayouts);
		}
		else {
			HttpSession session = request.getSession();

			Long previousGroupId = (Long)session.getAttribute(
				WebKeys.VISITED_GROUP_ID_PREVIOUS);

			if ((previousGroupId != null) &&
				(previousGroupId.longValue() != layoutGroupId)) {

				Group previousGroup = null;

				try {
					previousGroup = GroupLocalServiceUtil.getGroup(
						previousGroupId.longValue());
				}
				catch (NoSuchGroupException nsge) {
					if (_log.isWarnEnabled()) {
						_log.warn(nsge);
					}

					return layouts;
				}

				UnicodeProperties typeSettingsProperties =
					previousGroup.getTypeSettingsProperties();

				boolean mergeGuestPublicPages = GetterUtil.getBoolean(
					typeSettingsProperties.getProperty(
						"mergeGuestPublicPages"));

				if (!mergeGuestPublicPages) {
					return layouts;
				}

				List<Layout> previousLayouts =
					LayoutLocalServiceUtil.getLayouts(
						previousGroupId.longValue(), false,
						LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

				Object[] viewableLayouts = getViewableLayouts(
					request, user, permissionChecker, layout, previousLayouts);

				previousLayouts = (List<Layout>)viewableLayouts[1];

				if (previousLayouts != null) {
					layouts.addAll(previousLayouts);
				}
			}
		}

		return layouts;
	}

	protected void rememberVisitedGroupIds(
		HttpServletRequest request, long currentGroupId) {

		String requestURI = GetterUtil.getString(request.getRequestURI());

		if (!requestURI.endsWith(_PATH_PORTAL_LAYOUT)) {
			return;
		}

		HttpSession session = request.getSession();

		Long recentGroupId = (Long)session.getAttribute(
			WebKeys.VISITED_GROUP_ID_RECENT);

		Long previousGroupId = (Long)session.getAttribute(
			WebKeys.VISITED_GROUP_ID_PREVIOUS);

		if (recentGroupId == null) {
			recentGroupId = new Long(currentGroupId);

			session.setAttribute(
				WebKeys.VISITED_GROUP_ID_RECENT, recentGroupId);
		}
		else if (recentGroupId.longValue() != currentGroupId) {
			previousGroupId = new Long(recentGroupId.longValue());

			recentGroupId = new Long(currentGroupId);

			session.setAttribute(
				WebKeys.VISITED_GROUP_ID_RECENT, recentGroupId);

			session.setAttribute(
				WebKeys.VISITED_GROUP_ID_PREVIOUS, previousGroupId);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Current group id " + currentGroupId);
			_log.debug("Recent group id " + recentGroupId);
			_log.debug("Previous group id " + previousGroupId);
		}
	}

	protected void servicePre(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		ThemeDisplay themeDisplay = initThemeDisplay(request, response);

		if (themeDisplay == null) {
			return;
		}

		request.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);

		// Service context

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			request);

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		// Ajaxable render

		if (PropsValues.LAYOUT_AJAX_RENDER_ENABLE) {
			boolean portletAjaxRender = ParamUtil.getBoolean(
				request, "p_p_ajax", true);

			request.setAttribute(
				WebKeys.PORTLET_AJAX_RENDER, portletAjaxRender);
		}

		// Parallel render

		if (PropsValues.LAYOUT_PARALLEL_RENDER_ENABLE &&
			ServerDetector.isTomcat()) {

			boolean portletParallelRender = ParamUtil.getBoolean(
				request, "p_p_parallel", true);

			request.setAttribute(
				WebKeys.PORTLET_PARALLEL_RENDER, portletParallelRender);
		}

		// Main Journal article

		long mainJournalArticleId = ParamUtil.getLong(request, "p_j_a_id");

		if (mainJournalArticleId > 0) {
			try {
				JournalArticle mainJournalArticle =
					JournalArticleServiceUtil.getArticle(mainJournalArticleId);

				AssetEntry layoutAssetEntry =
					AssetEntryLocalServiceUtil.getEntry(
						JournalArticle.class.getName(),
						mainJournalArticle.getResourcePrimKey());

				request.setAttribute(
					WebKeys.LAYOUT_ASSET_ENTRY, layoutAssetEntry);
			}
			catch (NoSuchArticleException nsae) {
				if (_log.isWarnEnabled()) {
					_log.warn(nsae.getMessage());
				}
			}
		}
	}

	protected void updateUserLayouts(User user) throws Exception {
		Boolean hasPowerUserRole = null;

		// Private layouts

		boolean addDefaultUserPrivateLayouts = false;

		if (PropsValues.LAYOUT_USER_PRIVATE_LAYOUTS_ENABLED &&
			PropsValues.LAYOUT_USER_PRIVATE_LAYOUTS_AUTO_CREATE) {

			addDefaultUserPrivateLayouts = true;

			if (PropsValues.LAYOUT_USER_PRIVATE_LAYOUTS_POWER_USER_REQUIRED) {
				if (hasPowerUserRole == null) {
					hasPowerUserRole = hasPowerUserRole(user);
				}

				if (!hasPowerUserRole.booleanValue()) {
					addDefaultUserPrivateLayouts = false;
				}
			}
		}

		Boolean hasPrivateLayouts = null;

		if (addDefaultUserPrivateLayouts) {
			hasPrivateLayouts = LayoutLocalServiceUtil.hasLayouts(
				user, true, false);

			if (!hasPrivateLayouts) {
				addDefaultUserPrivateLayouts(user);
			}
		}

		boolean deleteDefaultUserPrivateLayouts = false;

		if (!PropsValues.LAYOUT_USER_PRIVATE_LAYOUTS_ENABLED) {
			deleteDefaultUserPrivateLayouts = true;
		}
		else if (PropsValues.LAYOUT_USER_PRIVATE_LAYOUTS_POWER_USER_REQUIRED) {
			if (hasPowerUserRole == null) {
				hasPowerUserRole = hasPowerUserRole(user);
			}

			if (!hasPowerUserRole.booleanValue()) {
				deleteDefaultUserPrivateLayouts = true;
			}
		}

		if (deleteDefaultUserPrivateLayouts) {
			if (hasPrivateLayouts == null) {
				hasPrivateLayouts = LayoutLocalServiceUtil.hasLayouts(
					user, true, false);
			}

			if (hasPrivateLayouts) {
				deleteDefaultUserPrivateLayouts(user);
			}
		}

		// Public pages

		boolean addDefaultUserPublicLayouts = false;

		if (PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_ENABLED &&
			PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_AUTO_CREATE) {

			addDefaultUserPublicLayouts = true;

			if (PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_POWER_USER_REQUIRED) {
				if (hasPowerUserRole == null) {
					hasPowerUserRole = hasPowerUserRole(user);
				}

				if (!hasPowerUserRole.booleanValue()) {
					addDefaultUserPublicLayouts = false;
				}
			}
		}

		Boolean hasPublicLayouts = null;

		if (addDefaultUserPublicLayouts) {
			hasPublicLayouts = LayoutLocalServiceUtil.hasLayouts(
				user, false, false);

			if (!hasPublicLayouts) {
				addDefaultUserPublicLayouts(user);
			}
		}

		boolean deleteDefaultUserPublicLayouts = false;

		if (!PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_ENABLED) {
			deleteDefaultUserPublicLayouts = true;
		}
		else if (PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_POWER_USER_REQUIRED) {
			if (hasPowerUserRole == null) {
				hasPowerUserRole = hasPowerUserRole(user);
			}

			if (!hasPowerUserRole.booleanValue()) {
				deleteDefaultUserPublicLayouts = true;
			}
		}

		if (deleteDefaultUserPublicLayouts) {
			if (hasPublicLayouts == null) {
				hasPublicLayouts = LayoutLocalServiceUtil.hasLayouts(
					user, false, false);
			}

			if (hasPublicLayouts) {
				deleteDefaultUserPublicLayouts(user);
			}
		}
	}

	protected File privateLARFile;
	protected File publicLARFile;

	private static final String _CONTROL_PANEL_CATEGORY_PORTLET_PREFIX =
		"portlet_";

	private static final String _PATH_PORTAL_LAYOUT = "/portal/layout";
	private static final String _PATH_PORTAL_LOGIN = "/portal/login";
	private static final String _PATH_PORTAL_LOGOUT = "/portal/logout";

	private static Log _log = LogFactoryUtil.getLog(ServicePreAction.class);

}