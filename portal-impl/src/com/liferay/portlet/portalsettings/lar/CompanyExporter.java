/**
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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.Account;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.EmailAddress;
import com.liferay.portal.model.Image;
import com.liferay.portal.model.Phone;
import com.liferay.portal.model.PortletConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.model.Website;
import com.liferay.portal.service.AddressServiceUtil;
import com.liferay.portal.service.EmailAddressServiceUtil;
import com.liferay.portal.service.ImageLocalServiceUtil;
import com.liferay.portal.service.PhoneServiceUtil;
import com.liferay.portal.service.PortalPreferencesLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.WebsiteServiceUtil;
import com.liferay.portal.util.PortletKeys;

import javax.portlet.PortletPreferences;
import java.util.List;

/**
 * @author kamesh
 *
 */
public class CompanyExporter extends PortalSettingsDataHandlerImpl {

	public String exportCompany(
			PortletDataContext portletDataContext, Company company,
			boolean exportCompanyDetails, boolean exportLogo,
			boolean exportPreferences)
		throws Exception {

		Document document = SAXReaderUtil.createDocument();

		Element rootElement = document.addElement("portal-settings");

		Element companyElement = rootElement.addElement("company");

		String path = getCompanyPath(portletDataContext, company);

		portletDataContext.addClassedModel(
			companyElement, path, company, _NAMESPACE);

		// Company details include account, addresess, email, websites etc.,

		if (exportCompanyDetails) {
			_exportAccount(portletDataContext, company, companyElement);

			_exportCompanyDefaultUser(portletDataContext, company,
					companyElement);
		}

		if (exportLogo) {
			_exportLogo(portletDataContext, company, companyElement);
		}

		if (exportPreferences) {
			_exportPreferences(portletDataContext, company, companyElement);
		}

		return document.formattedString();
	}

	private void _exportAddresses(
			PortletDataContext portletDataContext,
			Element companyAccountElement, long accountId)
		throws PortalException, SystemException {

		if (_log.isDebugEnabled()) {
			_log.debug("Exporting Company Addresses for classPK:" + accountId);
		}

		List<Address> addresses = AddressServiceUtil.getAddresses(
			Account.class.getName(), accountId);

		if (!addresses.isEmpty()) {
			Element addressesElement = companyAccountElement.addElement(
				"addresses");

			for (Address address : addresses) {
				Element addressElement = addressesElement.addElement("address");

				String addressPath = getAddressPath(
					portletDataContext, accountId, address.getAddressId());

				portletDataContext.addClassedModel(
					addressElement, addressPath, address, _NAMESPACE);
			}
		}
	}

	private void _exportAccount(
			PortletDataContext portletDataContext, Company company,
			Element companyElement)
		throws PortalException, SystemException {

		Account account = company.getAccount();

		String accountPath = getCompanyAccountPath(portletDataContext, company);

		Element companyAccountElement = companyElement.addElement("acount");

		portletDataContext.addClassedModel(companyAccountElement, accountPath,
			account, _NAMESPACE);

		long accountId = account.getAccountId();

		_exportAddresses(portletDataContext, companyAccountElement, accountId);

		_exportEmailAddresses(
			portletDataContext, companyAccountElement, accountId);

		_exportPhones(portletDataContext, companyAccountElement, accountId);

		_exportWebsites(portletDataContext, companyAccountElement, accountId);
	}

	private void _exportCompanyDefaultUser(
			PortletDataContext portletDataContext, Company company,
			Element companyElement)
		throws PortalException, SystemException {

		Element userElement = companyElement.addElement("default-user");

		User defaultUser = UserLocalServiceUtil.getDefaultUser(
			company.getCompanyId());

		String defaultUserPath = getDefaultUserPath(
			portletDataContext, defaultUser);

		portletDataContext.addClassedModel(userElement, defaultUserPath,
			defaultUser, _NAMESPACE);
	}

	private void _exportEmailAddresses(
			PortletDataContext portletDataContext,
			Element companyAccountElement, long accountId)
		throws PortalException, SystemException {

		if (_log.isDebugEnabled()) {
			_log.debug("Exporting Company Email Addresses for account: "
				+ accountId);
		}

		List<EmailAddress> emailAddresses =
			EmailAddressServiceUtil.getEmailAddresses(
				Account.class.getName(), accountId);

		if (!emailAddresses.isEmpty()) {
			Element emailAddressesElement = companyAccountElement.addElement(
				"emailAddresses");

			for (EmailAddress emailAddress : emailAddresses) {
				Element emailAddressElement = emailAddressesElement.addElement(
					"emailAddress");

				String emailAddressPath = getEmailAddressPath(
					portletDataContext, accountId,
					emailAddress.getEmailAddressId());

				portletDataContext.addClassedModel(
					emailAddressElement, emailAddressPath, emailAddress,
					_NAMESPACE);
			}
		}
	}

	private void _exportLogo(
			PortletDataContext portletDataContext, Company company,
			Element companyElement)
		throws SystemException {

		if (_log.isDebugEnabled()) {
			_log.debug("Exporting Logo for company: " + company.getCompanyId());
		}

		Image image = ImageLocalServiceUtil.getCompanyLogo(company.getLogoId());

		if (image != null) {
			String logoPath = getCompanyLogoPath(
				portletDataContext, company);

			companyElement.addAttribute("logo-path", logoPath);

			portletDataContext.addZipEntry(logoPath, image.getTextObj());
		}
	}

	private void _exportPhones(
			PortletDataContext portletDataContext,
			Element companyAccountElement, long accountId)
		throws PortalException, SystemException {

		if (_log.isDebugEnabled()) {
			_log.debug("Exporting Company Phones for account: " + accountId);
		}

		List<Phone> phones = PhoneServiceUtil.getPhones(
			Account.class.getName(), accountId);

		if (!phones.isEmpty()) {
			Element phonesElement = companyAccountElement.addElement(
				"phones");

			for (Phone phone : phones) {
				Element phoneElement = phonesElement.addElement("phone");

				String phonePath = getPhonePath(portletDataContext, accountId,
					phone.getPhoneId());

				portletDataContext.addClassedModel(
					phoneElement, phonePath, phone, _NAMESPACE);
			}
		}
	}

	private void _exportPreferences(
			PortletDataContext portletDataContext, Company company,
			Element companyElement)
		throws Exception {

		long ownerId = company.getCompanyId();
		int onwerType = PortletKeys.PREFS_OWNER_TYPE_COMPANY;

		PortletPreferences companyPreferences =
			PortalPreferencesLocalServiceUtil.getPreferences(
				company.getCompanyId(), ownerId, onwerType);

		String preferencesXML = companyPreferences.toString();

		if (Validator.isNull(preferencesXML)) {
			preferencesXML = PortletConstants.DEFAULT_PREFERENCES;
		}

		Document document = SAXReaderUtil.read(preferencesXML);

		String path = getPreferencesPath(portletDataContext, company);

		Element portletPreferencesElement = companyElement.addElement(
			"portlet-preferences");

		portletPreferencesElement.addAttribute("path", path);

		if (portletDataContext.isPathNotProcessed(path)) {
			portletDataContext.addZipEntry(
				path, document.formattedString(StringPool.TAB, false, false));
		}
	}

	private void _exportWebsites(
			PortletDataContext portletDataContext,
			Element companyAccountElement, long accountId)
		throws PortalException, SystemException {

		if (_log.isDebugEnabled()) {
			_log.debug("Exporting Company Websites for account: " + accountId);
		}

		List<Website> websites = WebsiteServiceUtil.getWebsites(
			Account.class.getName(), accountId);

		if (!websites.isEmpty()) {
			Element websitesElement = companyAccountElement.addElement(
				"websites");

			for (Website website : websites) {
				Element websiteElement = websitesElement.addElement("website");

				String websitePath = getWebsitePath(
					portletDataContext, accountId, website.getWebsiteId());

				portletDataContext.addClassedModel(
					websiteElement, websitePath, website, _NAMESPACE);
			}
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		CompanyExporter.class);

}