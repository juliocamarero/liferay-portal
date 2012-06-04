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

import com.liferay.portal.NoSuchAddressException;
import com.liferay.portal.NoSuchEmailAddressException;
import com.liferay.portal.NoSuchPhoneException;
import com.liferay.portal.NoSuchWebsiteException;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.webcache.WebCachePoolUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Account;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.EmailAddress;
import com.liferay.portal.model.Phone;
import com.liferay.portal.model.User;
import com.liferay.portal.model.Website;
import com.liferay.portal.service.AddressServiceUtil;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.CompanyServiceUtil;
import com.liferay.portal.service.EmailAddressServiceUtil;
import com.liferay.portal.service.PhoneServiceUtil;
import com.liferay.portal.service.PortalPreferencesLocalServiceUtil;
import com.liferay.portal.service.WebsiteServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.usersadmin.util.UsersAdminUtil;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kamesh
 *
 */
public class CompanyImporter extends PortalSettingsDataHandlerImpl {

	public void importCompany(
			PortletDataContext portletDataContext, Element companyElement,
			boolean importCompanyDetails, boolean importLogo,
			boolean importPreferences)
		throws Exception {

		if (companyElement != null) {
			String path = companyElement.attributeValue("path");

			Company importedCompany =
				(Company)portletDataContext.getZipEntryAsObject(path);

			if (importedCompany != null) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Current Company Id"
							+ portletDataContext.getCompanyId());
					_log.debug(
						"Imported Company Id" + importedCompany.getCompanyId());
				}

				// Update General, Indentification details

				if (importCompanyDetails) {
					_importCompanyDetails(
						portletDataContext, companyElement, importedCompany);
				}

				// Update Logo

				if (importLogo) {
					_importLogo(
						portletDataContext, companyElement,
						importedCompany.getCompanyId());
				}

				// Import portal preferences

				if (importPreferences) {
					_importPreferences(
						portletDataContext, companyElement,
						importedCompany.getCompanyId());

					clearCaches();
				}

			}
		}
	}

	protected void clearCaches() {
		if (_log.isDebugEnabled()) {
			_log.debug("Clearing the DB cache");
		}

		CacheRegistryUtil.clear();

		if (_log.isDebugEnabled()) {
			_log.debug("Clearing the clustered VM cache");
		}

		MultiVMPoolUtil.clear();

		if (_log.isDebugEnabled()) {
			_log.debug("Clearing the Single VM cache");
		}

		WebCachePoolUtil.clear();
	}

	private List<Address> _importAddress(
			PortletDataContext portletDataContext, Company company,
			Element accountElement)
		throws PortalException, SystemException {

		if (_log.isDebugEnabled()) {
			_log.debug("Importing Company Addresses");
		}

		List<Address> addresses = new ArrayList<Address>();

		Element addressesElement = accountElement.element("addressses");

		if (addressesElement != null) {
			List<Element> listOfAddress = addressesElement.elements("address");

			for (Element element : listOfAddress) {
				String path = element.attributeValue("path");

				Address address =
					(Address)portletDataContext.getZipEntryAsObject(path);

				if (address != null) {
					try {
						AddressServiceUtil.getAddress(address.getAddressId());
					}
					catch (NoSuchAddressException e) {
						_log.debug(
							"Importing address: " + address.getAddressId());

						address = AddressServiceUtil.addAddress(
							address.getClassName(), address.getClassPK(),
							address.getStreet1(), address.getStreet2(),
							address.getStreet3(), address.getCity(),
							address.getZip(), address.getRegionId(),
							address.getCountryId(), address.getTypeId(),
							address.isMailing(), address.isPrimary());
					}
					finally {
						addresses.add(address);
					}
				}
			}

			UsersAdminUtil.updateAddresses(
				Account.class.getName(), company.getAccountId(), addresses);

			if (_log.isDebugEnabled()) {
				_log.debug("Successfully imported " + addresses.size()
					+ " addresses");
			}
		}

		return addresses;
	}

	private void _importCompanyDetails(
			PortletDataContext portletDataContext, Element companyElement,
			Company company)
			throws PortalException, SystemException {

		long companyId = company.getCompanyId();

		Element userElement = companyElement.element("default-user");

		if (userElement != null) {
			String path = userElement.attributeValue("path");

			User defaultUser =
				(User)portletDataContext.getZipEntryAsObject(path);

			if (defaultUser != null) {
				if (_log.isDebugEnabled()) {
					_log.debug("Importing default user :"
							+ defaultUser.getUserId());
				}

				String languageId = defaultUser.getLanguageId();
				String timeZoneId = defaultUser.getTimeZoneId();

				CompanyLocalServiceUtil.updateDisplay(
					companyId, languageId, timeZoneId);
			}
		}

		// update the company account

		Account account = null;

		Element accountElement = companyElement.element("account");

		if (accountElement != null) {
			String accountPath = accountElement.attributeValue("path");

			if (accountPath != null) {
				account = (Account)portletDataContext.getZipEntryAsObject(
					accountPath);
			}
		}

		if (account != null) {
			if (_log.isDebugEnabled()) {
				_log.debug("Importing Company Account details ");
			}

			CompanyServiceUtil.updateCompany(
				companyId, company.getVirtualHostname(), company.getMx(),
				company.getHomeURL(), company.getName(), account.getLegalName(),
				account.getLegalId(), account.getLegalType(),
				account.getSicCode(), account.getTickerSymbol(),
				account.getIndustry(), account.getType(), account.getSize());

			if (_log.isDebugEnabled()) {
				_log.debug("Importing Company Identification section details ");
			}

			_importAddress(portletDataContext, company, accountElement);

			_importEmailAddress(portletDataContext, company, accountElement);

			_importPhones(portletDataContext, company, accountElement);

			_importWebsites(portletDataContext, company, accountElement);
		}

		PortalUtil.resetCDNHosts();
	}

	private List<EmailAddress> _importEmailAddress(
			PortletDataContext portletDataContext, Company company,
			Element accountElement)
			throws PortalException, SystemException {

		if (_log.isDebugEnabled()) {
			_log.debug("Importing Company Email Addresses");
		}

		List<EmailAddress> emailAddresses = new ArrayList<EmailAddress>();

		Element emailAddressesElement = accountElement.element("email-address");

		if (emailAddressesElement != null) {
			List<Element> listOfEmailAddress = emailAddressesElement.elements(
				"email-address");

			for (Element element : listOfEmailAddress) {
				String path = element.attributeValue("path");

				EmailAddress emailAddress =
					(EmailAddress) portletDataContext.getZipEntryAsObject(path);

				if (emailAddress != null) {
					try {
						EmailAddressServiceUtil.getEmailAddress(
							emailAddress.getEmailAddressId());
					}
					catch (NoSuchEmailAddressException e) {
						_log.debug(
							"Importing email address: "
								+ emailAddress.getEmailAddressId());

						emailAddress = EmailAddressServiceUtil.addEmailAddress(
							emailAddress.getClassName(),
							emailAddress.getClassPK(),
							emailAddress.getAddress(), emailAddress.getTypeId(),
							emailAddress.isPrimary());
					}
					finally {
						emailAddresses.add(emailAddress);
					}
				}
			}

			UsersAdminUtil.updateEmailAddresses(
				Account.class.getName(), company.getAccountId(),
				emailAddresses);

			if (_log.isDebugEnabled()) {
				_log.debug("Successfully imported " + emailAddresses.size()
					+ " email addresses");
			}
		}

		return emailAddresses;
	}

	private List<Phone> _importPhones(
			PortletDataContext portletDataContext, Company company,
			Element accountElement)
			throws PortalException, SystemException {

		if (_log.isDebugEnabled()) {
			_log.debug("Importing Company Phones");
		}

		List<Phone> phones = new ArrayList<Phone>();

		Element phonesElement = accountElement.element("phones");

		if (phonesElement != null) {
			List<Element> listOfPhones = phonesElement.elements("phone");

			for (Element element : listOfPhones) {
				String path = element.attributeValue("path");

				Phone phone =
					(Phone)portletDataContext.getZipEntryAsObject(path);

				if (phone != null) {
					try {
						PhoneServiceUtil.getPhone(phone.getPhoneId());

					}
					catch (NoSuchPhoneException e) {
						_log.debug("Importing phone: " + phone.getPhoneId());

						phone = PhoneServiceUtil.addPhone(
							phone.getClassName(), phone.getClassPK(),
							phone.getNumber(), phone.getExtension(),
							phone.getTypeId(), phone.isPrimary());

					}
					finally {
						phones.add(phone);
					}
				}
			}

			UsersAdminUtil.updatePhones(
				Account.class.getName(), company.getAccountId(), phones);

			if (_log.isDebugEnabled()) {
				_log.debug("Successfully imported " + phones.size()
					+ " phones");
			}
		}

		return phones;
	}

	private void _importLogo(
			PortletDataContext portletDataContext, Element companyElement,
			long companyId)
		throws PortalException, SystemException, IOException {

		String logoPath = companyElement.attributeValue("logo-path");

		byte[] logoBytes = portletDataContext.getZipEntryAsByteArray(
			logoPath);

		if ((logoBytes != null) && (logoBytes.length > 0)) {
			File logo = FileUtil.createTempFile(logoBytes);

			CompanyLocalServiceUtil.updateLogo(companyId, logo);
		}
		else {
			CompanyLocalServiceUtil.updateLogo(companyId, (File)null);
		}
	}

	private void _importPreferences(
			PortletDataContext portletDataContext,
			Element companyElement, long companyId)
		throws IOException, SystemException {

		int onwerType = PortletKeys.PREFS_OWNER_TYPE_COMPANY;

		Element portletPreferencesElement = companyElement.element(
			"portlet-preferences");

		String path = portletPreferencesElement.attributeValue("path");

		String xml = portletDataContext.getZipEntryAsString(path);

		if (_log.isDebugEnabled()) {
			_log.debug("Importing company preferences");
		}

		PortalPreferencesLocalServiceUtil.updatePreferences(
			companyId, onwerType, xml);
	}

	private List<Website> _importWebsites(
			PortletDataContext portletDataContext, Company company,
			Element accountElement)
		throws PortalException, SystemException {

		if (_log.isDebugEnabled()) {
			_log.debug("Importing Company Websites");
		}

		List<Website> websites = new ArrayList<Website>();

		Element websitesElement = accountElement.element("websites");

		if (websitesElement != null) {
			List<Element> listOfWebsites = websitesElement.elements(
					"website");

			for (Element element : listOfWebsites) {
				String path = element.attributeValue("path");

				Website website =
					(Website) portletDataContext.getZipEntryAsObject(path);

				if (website != null) {
					long websiteId = website.getWebsiteId();

					try {
						WebsiteServiceUtil.getWebsite(websiteId);

					}
					catch (NoSuchWebsiteException e) {
						_log.debug("No website exist with PK:" + websiteId
							+ " creating new ...");

						String className = website.getClassName();
						long classPK = website.getClassPK();
						String url = website.getUrl();
						int typeId = website.getTypeId();
						boolean primary = website.isPrimary();

						website = WebsiteServiceUtil.addWebsite(
							className, classPK, url, typeId, primary);

					}
					finally {
						websites.add(website);
					}
				}
			}

			UsersAdminUtil.updateWebsites(
				Account.class.getName(), company.getAccountId(), websites);

			if (_log.isDebugEnabled()) {
				_log.debug("Successfully imported " + websites.size()
					+ " web sites");
			}
		}

		return websites;
	}

	private static Log _log = LogFactoryUtil.getLog(
		CompanyImporter.class);

}