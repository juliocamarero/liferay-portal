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

package com.liferay.portal.security.ldap;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TimeZoneUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.User;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.TestPropsValues;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.portlet.PortletPreferences;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Manuel de la Peña
 */
@ExecutionTestListeners(listeners = {
	MainServletExecutionTestListener.class,
	TransactionalExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class PortalLDAPImporterImplTest {

	@Before
	public void setUp() {
		try {
			_companyId = TestPropsValues.getCompanyId();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Transactional
	public void testImportLDAPUser() throws Exception {
		configLDAPServer(_companyId);

		PortalLDAPImporterUtil.importFromLDAP();

		List<User> users = UserLocalServiceUtil.getUsers(0, 10);

		completeUsersFields(users);

		// import again to check fields are not overriden

		PortalLDAPImporterUtil.importFromLDAP();

		List<User> importedUsers = UserLocalServiceUtil.getUsers(0, 10);

		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);

			Contact contact = user.getContact();

			User importedUser = importedUsers.get(i);

			Contact importedContact = importedUser.getContact();

			Assert.assertEquals(
				contact.getPrefixId(), importedContact.getPrefixId());
			Assert.assertEquals(
				user.getMiddleName(), importedUser.getMiddleName());
			Assert.assertEquals(
				contact.getSuffixId(), importedContact.getSuffixId());
			Assert.assertEquals(
				contact.getMale(), importedContact.getMale());
			Assert.assertEquals(
				user.getJobTitle(), importedUser.getJobTitle());
			Assert.assertEquals(
				contact.getAimSn(), importedContact.getAimSn());
			Assert.assertEquals(
				contact.getIcqSn(), importedContact.getIcqSn());
			Assert.assertEquals(
				contact.getJabberSn(), importedContact.getJabberSn());
			Assert.assertEquals(
				contact.getSkypeSn(), importedContact.getSkypeSn());
			Assert.assertEquals(
				contact.getMsnSn(), importedContact.getMsnSn());
			Assert.assertEquals(
				contact.getYmSn(), importedContact.getYmSn());
			Assert.assertEquals(
				contact.getFacebookSn(), importedContact.getFacebookSn());
			Assert.assertEquals(
				contact.getMySpaceSn(), importedContact.getMySpaceSn());
			Assert.assertEquals(
				contact.getTwitterSn(), importedContact.getTwitterSn());
			Assert.assertEquals(
				contact.getSmsSn(), importedContact.getSmsSn());
			Assert.assertEquals(
				user.getOpenId(), importedUser.getOpenId());
			Assert.assertEquals(
				user.getLanguageId(), importedUser.getLanguageId());
			Assert.assertEquals(
				user.getTimeZoneId(), importedUser.getTimeZoneId());
			Assert.assertEquals(
				user.getGreeting(), importedUser.getGreeting());
			Assert.assertEquals(
				user.getComments(), importedUser.getComments());
		}
	}

	protected UnicodeProperties addLDAPServer(
			long companyId, UnicodeProperties properties)
		throws Exception {

		PortletPreferences preferences = PrefsPropsUtil.getPreferences(
			companyId);

		String ldapServerIds = preferences.getValue(
			"ldap.server.ids", StringPool.BLANK);

		ldapServerIds = StringUtil.add(
			ldapServerIds, String.valueOf(_ldapServerId));

		properties.setProperty("ldap.server.ids", ldapServerIds);

		String postfix = LDAPSettingsUtil.getPropertyPostfix(_ldapServerId);

		// enable import

		properties.put(PropsKeys.LDAP_IMPORT_ENABLED, "true");

		properties.put("ldap.server.name" + postfix, "LDAP Server Name");
		properties.put(
			PropsKeys.LDAP_BASE_PROVIDER_URL + postfix, LDAP_BASE_PROVIDER_URL);
		properties.put(PropsKeys.LDAP_BASE_DN + postfix, _LDAP_BASE_DN);
		properties.put(
			PropsKeys.LDAP_SECURITY_PRINCIPAL + postfix,
			_LDAP_SECURITY_PRINCIPAL);
		properties.put(
			PropsKeys.LDAP_SECURITY_CREDENTIALS + postfix,
			_LDAP_SECURITY_CREDENTIALS);
		properties.put(
			PropsKeys.LDAP_AUTH_SEARCH_FILTER + postfix,
			_LDAP_AUTH_SEARCH_FILTER);
		properties.put(
			PropsKeys.LDAP_IMPORT_USER_SEARCH_FILTER + postfix,
			_LDAP_IMPORT_USER_SEARCH_FILTER);
		properties.put(
			PropsKeys.LDAP_IMPORT_GROUP_SEARCH_FILTER + postfix,
			_LDAP_IMPORT_GROUP_SEARCH_FILTER);
		properties.put(PropsKeys.LDAP_USERS_DN + postfix, _LDAP_USERS_DN);
		properties.put(
			PropsKeys.LDAP_USER_DEFAULT_OBJECT_CLASSES + postfix,
			_LDAP_USER_DEFAULT_OBJECT_CLASSES);
		properties.put(PropsKeys.LDAP_GROUPS_DN + postfix, _LDAP_GROUPS_DN);
		properties.put(
			PropsKeys.LDAP_GROUP_DEFAULT_OBJECT_CLASSES + postfix,
			_LDAP_GROUP_DEFAULT_OBJECT_CLASSES);

		properties.put(
			PropsKeys.LDAP_USER_MAPPINGS + postfix, _LDAP_USER_MAPPINGS);
		properties.put(PropsKeys.LDAP_USER_CUSTOM_MAPPINGS + postfix," ");
		properties.put(
			PropsKeys.LDAP_GROUP_MAPPINGS+ postfix, _LDAP_GROUP_MAPPINGS);
		properties.put(PropsKeys.LDAP_CONTACT_MAPPINGS + postfix," ");
		properties.put(PropsKeys.LDAP_CONTACT_CUSTOM_MAPPINGS + postfix," ");

		return properties;
	}

	protected void completeUsersFields(List<User> users) throws Exception {
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);

			Contact contact = user.getContact();

			contact.setPrefixId(1);

			if (Validator.isNull(user.getMiddleName())) {
				user.setMiddleName("portalMiddleName" + i);
			}

			contact.setSuffixId(1);
			contact.setMale(false);

			if (Validator.isNull(user.getJobTitle())) {
				user.setJobTitle("portalJobTitle" + i);
			}

			contact.setAimSn("portalAim" + i);
			contact.setIcqSn("portalIcq" + i);
			contact.setJabberSn("portalJabber" + i);
			contact.setSkypeSn("portalSkype" + i);
			contact.setMsnSn("portalMsn" + i);
			contact.setYmSn("portalYm" + i);

			contact.setFacebookSn("portalFacebook" + i);
			contact.setMySpaceSn("portalMySpace" + i);
			contact.setTwitterSn("portalTwitter" + i);

			contact.setSmsSn("portalSms" + i + "@liferay.com");

			user.setOpenId("http://liferay" + i + ".openid.com");

			Locale locale = LocaleUtil.getDefault();

			user.setLanguageId(LocaleUtil.toLanguageId(locale));

			TimeZone timeZone = TimeZoneUtil.getDefault();
			user.setTimeZoneId(timeZone.getID());
			user.setGreeting("hello portal" + i + "!");

			user.setComments("portal comments" + i);

			user.setModifiedDate(new Date());

			UserLocalServiceUtil.updateUser(user);
		}
	}

	protected void configLDAPServer(long companyId) throws Exception {
		_ldapServerId = CounterLocalServiceUtil.increment();

		UnicodeProperties properties = new UnicodeProperties();

		properties = addLDAPServer(companyId, properties);

		CompanyLocalServiceUtil.updatePreferences(companyId, properties);
	}

	private static final String _LDAP_AUTH_SEARCH_FILTER=
		"(mail=@email_address@)";

	private static final String _LDAP_BASE_DN = "CN=users,DC=mydomain,DC=com";

	private static final String LDAP_BASE_PROVIDER_URL =
		"ldap://192.168.1.218:389";

	private static final String _LDAP_GROUP_DEFAULT_OBJECT_CLASSES =
		"top,groupOfUniqueNames";

	private static final String _LDAP_GROUP_MAPPINGS =
		"groupName=cn\\ndescription=description\\nuser=uniqueMember";

	private static final String _LDAP_GROUPS_DN = "dc=example,dc=com";

	private static final String _LDAP_IMPORT_GROUP_SEARCH_FILTER =
		"(objectClass=groupOfUniqueNames)";

	private static final String _LDAP_IMPORT_USER_SEARCH_FILTER =
		"(objectClass=person)";

	private static final String _LDAP_SECURITY_CREDENTIALS = "password";

	private static final String _LDAP_SECURITY_PRINCIPAL =
		"mydomain\\administrator";

	private static final String _LDAP_USER_DEFAULT_OBJECT_CLASSES =
		"top,person,inetOrgPerson,organizationalPerson";

	private static final String _LDAP_USER_MAPPINGS =
		"uuid=uuid\nscreenName=cn\npassword=userPassword\n" +
		"emailAddress=mail\nfirstName=givenName\nlastName=sn\n" +
		"jobTitle=title\ngroup=groupMembership";

	private static final String _LDAP_USERS_DN = "dc=example,dc=com";

	private static long _ldapServerId;

	private long _companyId;

}