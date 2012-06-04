/*
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.portalsettings.lar;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.BasePortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataException;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.PortletConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.PortalPreferencesLocalServiceUtil;
import com.liferay.portal.util.PortletKeys;

import javax.portlet.PortletPreferences;

/**
 * @author Kamesh Sampath
 */
public class PortalSettingsDataHandlerImpl extends BasePortletDataHandler {

	@Override
	public PortletDataHandlerControl[] getExportControls() {
		return new PortletDataHandlerControl[] {
			exportCompanyDetails, exportCompanyPreferences, exportLogo
		};
	}

	@Override
	public PortletDataHandlerControl[] getImportControls() {
		return new PortletDataHandlerControl[] {
			exportCompanyDetails, exportCompanyPreferences, exportLogo
		};
	}

	@Override
	public PortletPreferences doDeleteData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws PortletDataException {

		long companyId = portletDataContext.getCompanyId();

		long ownerId = companyId;
		int ownerType = PortletKeys.PREFS_OWNER_TYPE_COMPANY;

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Deleting existing preferences for compnay:" + companyId);
		}

		try {
			PortalPreferencesLocalServiceUtil.updatePreferences(
				ownerId, ownerType, PortletConstants.DEFAULT_PREFERENCES);
		}
		catch (SystemException e) {
			_log.error(
				"Error deleting preferences for company: " + companyId, e);
		}

		return null;
	}

	@Override
	public String doExportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws PortletDataException {

		long companyId = portletDataContext.getCompanyId();

		if (_log.isDebugEnabled()) {
			_log.debug("Exporting compnay:" + companyId);
		}

		try {
			Company company = CompanyLocalServiceUtil.getCompany(companyId);

			boolean exportCompanyDetails =
				portletDataContext.getBooleanParameter(
					_NAMESPACE, "company-details");

			boolean exportLogo = portletDataContext.getBooleanParameter(
				_NAMESPACE, "logo");

			boolean exportPreferences = portletDataContext.getBooleanParameter(
				_NAMESPACE, "company-preferences");

			return _companyExporter.exportCompany(
				portletDataContext, company, exportCompanyDetails, exportLogo,
				exportPreferences);
		}
		catch (Exception e) {
			_log.error(e);
		}

		return StringPool.BLANK;
	}

	@Override
	public PortletPreferences doImportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, String data)
			throws PortletDataException {

		if (_log.isDebugEnabled()) {
			_log.debug("Import Portal Settings data");
		}

		Document document = null;

		try {
			document = SAXReaderUtil.read(data);

			Element rootElement = document.getRootElement();

			Element companyElement = rootElement.element("company");

			boolean importCompanyDetails =
				portletDataContext.getBooleanParameter(
					_NAMESPACE, "company-details");

			boolean importLogo = portletDataContext.getBooleanParameter(
				_NAMESPACE, "logo");

			boolean importPreferences =
				portletDataContext.getBooleanParameter(
					_NAMESPACE, "company-preferences");

			_companyImporter.importCompany(
				portletDataContext, companyElement, importCompanyDetails,
				importLogo, importPreferences);
		}
		catch (Exception e) {
			_log.error("Error occured while importing data", e);
		}

		return null;
	}

	protected String getAddressPath(
			PortletDataContext portletDataContext, long accountId,
			long addressId) {

		StringBundler sb = new StringBundler(7);

		sb.append(
			portletDataContext.getPortletPath(PortletKeys.PORTAL_SETTINGS));
		sb.append(PATH_COMPANY);
		sb.append("Address_");
		sb.append(accountId);
		sb.append("_");
		sb.append(addressId);
		sb.append(".xml");

		return sb.toString();
	}

	protected String getCompanyPath(
		PortletDataContext portletDataContext, Company company) {

		StringBundler sb = new StringBundler(4);

		sb.append(
			portletDataContext.getPortletPath(PortletKeys.PORTAL_SETTINGS));
		sb.append(PATH_COMPANY);
		sb.append(company.getCompanyId());
		sb.append(".xml");

		return sb.toString();
	}

	protected String getPreferencesPath(
		PortletDataContext portletDataContext, Company company) {

		StringBundler sb = new StringBundler(3);

		sb.append(
			portletDataContext.getPortletPath(PortletKeys.PORTAL_SETTINGS));
		sb.append(PATH_COMPANY);
		sb.append("preferences.xml");

		return sb.toString();
	}

	protected String getCompanyAccountPath(
			PortletDataContext portletDataContext, Company company) {

		StringBundler sb = new StringBundler(3);

		sb.append(
			portletDataContext.getPortletPath(PortletKeys.PORTAL_SETTINGS));
		sb.append(PATH_COMPANY);
		sb.append("account.xml");

		return sb.toString();
	}

	protected String getCompanyLogoPath(
		PortletDataContext portletDataContext, Company company) {

		StringBundler sb = new StringBundler(3);

		sb.append(
			portletDataContext.getPortletPath(PortletKeys.PORTAL_SETTINGS));
		sb.append(PATH_COMPANY);
		sb.append("logo/");

		return sb.toString();
	}

	protected String getEmailAddressPath(
			PortletDataContext portletDataContext, long accountId,
			long emailAddressId) {

		StringBundler sb = new StringBundler(7);

		sb.append(portletDataContext
				.getPortletPath(PortletKeys.PORTAL_SETTINGS));
		sb.append(PATH_COMPANY);
		sb.append("Email_Address_");
		sb.append(accountId);
		sb.append("_");
		sb.append(emailAddressId);
		sb.append(".xml");

		return sb.toString();
	}

	protected String getPhonePath(
			PortletDataContext portletDataContext, long accountId,
			long phoneId) {

		StringBundler sb = new StringBundler(7);

		sb.append(portletDataContext
				.getPortletPath(PortletKeys.PORTAL_SETTINGS));
		sb.append(PATH_COMPANY);
		sb.append("Phone_");
		sb.append(accountId);
		sb.append("_");
		sb.append(phoneId);
		sb.append(".xml");

		return sb.toString();
	}

	protected String getDefaultUserPath(
		PortletDataContext portletDataContext, User defaultUser) {

		StringBundler sb = new StringBundler(3);

		sb.append(
			portletDataContext.getPortletPath(PortletKeys.PORTAL_SETTINGS));
		sb.append(PATH_COMPANY);
		sb.append("default_user.xml");

		return sb.toString();
	}

	protected String getWebsitePath(
		PortletDataContext portletDataContext, long accountId, long websiteId) {

		StringBundler sb = new StringBundler(7);

		sb.append(portletDataContext
			.getPortletPath(PortletKeys.PORTAL_SETTINGS));
		sb.append(PATH_COMPANY);
		sb.append("Website_");
		sb.append(accountId);
		sb.append("_");
		sb.append(websiteId);
		sb.append(".xml");

		return sb.toString();
	}

	public static String getNamespace () {
		return _NAMESPACE;
	}

	private static final String PATH_COMPANY = "/company/";

	protected static final String _NAMESPACE = "portal-settings";

	public static final PortletDataHandlerBoolean exportCompanyDetails =
		new PortletDataHandlerBoolean(_NAMESPACE, "company-details", true);

	public static final PortletDataHandlerBoolean exportCompanyPreferences =
		new PortletDataHandlerBoolean(_NAMESPACE, "company-preferences", true);

	public static final PortletDataHandlerBoolean exportLogo =
		new PortletDataHandlerBoolean(_NAMESPACE, "logo", true);

	private static Log _log = LogFactoryUtil.getLog(
		PortalSettingsDataHandlerImpl.class);

	private final CompanyExporter _companyExporter = new CompanyExporter();

	private final CompanyImporter _companyImporter = new CompanyImporter();


}