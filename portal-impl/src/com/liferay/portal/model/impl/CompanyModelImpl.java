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

package com.liferay.portal.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.CompanyModel;
import com.liferay.portal.model.CompanySoap;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The base model implementation for the Company service. Represents a row in the &quot;Company&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portal.model.CompanyModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link CompanyImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CompanyImpl
 * @see com.liferay.portal.model.Company
 * @see com.liferay.portal.model.CompanyModel
 * @generated
 */
@JSON(strict = true)
public class CompanyModelImpl extends BaseModelImpl<Company>
	implements CompanyModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a company model instance should use the {@link com.liferay.portal.model.Company} interface instead.
	 */
	public static final String TABLE_NAME = "Company";
	public static final Object[][] TABLE_COLUMNS = {
			{ "companyId", Types.BIGINT },
			{ "accountId", Types.BIGINT },
			{ "webId", Types.VARCHAR },
			{ "key_", Types.CLOB },
			{ "mx", Types.VARCHAR },
			{ "homeURL", Types.VARCHAR },
			{ "logoId", Types.BIGINT },
			{ "system", Types.BOOLEAN },
			{ "maxUsers", Types.INTEGER },
			{ "active_", Types.BOOLEAN }
		};
	public static final String TABLE_SQL_CREATE = "create table Company (companyId LONG not null primary key,accountId LONG,webId VARCHAR(75) null,key_ TEXT null,mx VARCHAR(75) null,homeURL STRING null,logoId LONG,system BOOLEAN,maxUsers INTEGER,active_ BOOLEAN)";
	public static final String TABLE_SQL_DROP = "drop table Company";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portal.model.Company"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portal.model.Company"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.model.Company"),
			true);
	public static long LOGOID_COLUMN_BITMASK = 1L;
	public static long MX_COLUMN_BITMASK = 2L;
	public static long SYSTEM_COLUMN_BITMASK = 4L;
	public static long WEBID_COLUMN_BITMASK = 8L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static Company toModel(CompanySoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		Company model = new CompanyImpl();

		model.setCompanyId(soapModel.getCompanyId());
		model.setAccountId(soapModel.getAccountId());
		model.setWebId(soapModel.getWebId());
		model.setKey(soapModel.getKey());
		model.setMx(soapModel.getMx());
		model.setHomeURL(soapModel.getHomeURL());
		model.setLogoId(soapModel.getLogoId());
		model.setSystem(soapModel.getSystem());
		model.setMaxUsers(soapModel.getMaxUsers());
		model.setActive(soapModel.getActive());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<Company> toModels(CompanySoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<Company> models = new ArrayList<Company>(soapModels.length);

		for (CompanySoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portal.model.Company"));

	public CompanyModelImpl() {
	}

	public long getPrimaryKey() {
		return _companyId;
	}

	public void setPrimaryKey(long primaryKey) {
		setCompanyId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return new Long(_companyId);
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return Company.class;
	}

	public String getModelClassName() {
		return Company.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("companyId", getCompanyId());
		attributes.put("accountId", getAccountId());
		attributes.put("webId", getWebId());
		attributes.put("key", getKey());
		attributes.put("mx", getMx());
		attributes.put("homeURL", getHomeURL());
		attributes.put("logoId", getLogoId());
		attributes.put("system", getSystem());
		attributes.put("maxUsers", getMaxUsers());
		attributes.put("active", getActive());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long accountId = (Long)attributes.get("accountId");

		if (accountId != null) {
			setAccountId(accountId);
		}

		String webId = (String)attributes.get("webId");

		if (webId != null) {
			setWebId(webId);
		}

		String key = (String)attributes.get("key");

		if (key != null) {
			setKey(key);
		}

		String mx = (String)attributes.get("mx");

		if (mx != null) {
			setMx(mx);
		}

		String homeURL = (String)attributes.get("homeURL");

		if (homeURL != null) {
			setHomeURL(homeURL);
		}

		Long logoId = (Long)attributes.get("logoId");

		if (logoId != null) {
			setLogoId(logoId);
		}

		Boolean system = (Boolean)attributes.get("system");

		if (system != null) {
			setSystem(system);
		}

		Integer maxUsers = (Integer)attributes.get("maxUsers");

		if (maxUsers != null) {
			setMaxUsers(maxUsers);
		}

		Boolean active = (Boolean)attributes.get("active");

		if (active != null) {
			setActive(active);
		}
	}

	@JSON
	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	@JSON
	public long getAccountId() {
		return _accountId;
	}

	public void setAccountId(long accountId) {
		_accountId = accountId;
	}

	@JSON
	public String getWebId() {
		if (_webId == null) {
			return StringPool.BLANK;
		}
		else {
			return _webId;
		}
	}

	public void setWebId(String webId) {
		_columnBitmask |= WEBID_COLUMN_BITMASK;

		if (_originalWebId == null) {
			_originalWebId = _webId;
		}

		_webId = webId;
	}

	public String getOriginalWebId() {
		return GetterUtil.getString(_originalWebId);
	}

	@JSON
	public String getKey() {
		if (_key == null) {
			return StringPool.BLANK;
		}
		else {
			return _key;
		}
	}

	public void setKey(String key) {
		_key = key;
	}

	@JSON
	public String getMx() {
		if (_mx == null) {
			return StringPool.BLANK;
		}
		else {
			return _mx;
		}
	}

	public void setMx(String mx) {
		_columnBitmask |= MX_COLUMN_BITMASK;

		if (_originalMx == null) {
			_originalMx = _mx;
		}

		_mx = mx;
	}

	public String getOriginalMx() {
		return GetterUtil.getString(_originalMx);
	}

	@JSON
	public String getHomeURL() {
		if (_homeURL == null) {
			return StringPool.BLANK;
		}
		else {
			return _homeURL;
		}
	}

	public void setHomeURL(String homeURL) {
		_homeURL = homeURL;
	}

	@JSON
	public long getLogoId() {
		return _logoId;
	}

	public void setLogoId(long logoId) {
		_columnBitmask |= LOGOID_COLUMN_BITMASK;

		if (!_setOriginalLogoId) {
			_setOriginalLogoId = true;

			_originalLogoId = _logoId;
		}

		_logoId = logoId;
	}

	public long getOriginalLogoId() {
		return _originalLogoId;
	}

	@JSON
	public boolean getSystem() {
		return _system;
	}

	public boolean isSystem() {
		return _system;
	}

	public void setSystem(boolean system) {
		_columnBitmask |= SYSTEM_COLUMN_BITMASK;

		if (!_setOriginalSystem) {
			_setOriginalSystem = true;

			_originalSystem = _system;
		}

		_system = system;
	}

	public boolean getOriginalSystem() {
		return _originalSystem;
	}

	@JSON
	public int getMaxUsers() {
		return _maxUsers;
	}

	public void setMaxUsers(int maxUsers) {
		_maxUsers = maxUsers;
	}

	@JSON
	public boolean getActive() {
		return _active;
	}

	public boolean isActive() {
		return _active;
	}

	public void setActive(boolean active) {
		_active = active;
	}

	public java.security.Key getKeyObj() {
		return null;
	}

	public void setKeyObj(java.security.Key keyObj) {
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public Company toEscapedModel() {
		if (_escapedModelProxy == null) {
			_escapedModelProxy = (Company)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelProxyInterfaces,
					new AutoEscapeBeanHandler(this));
		}

		return _escapedModelProxy;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			Company.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public Object clone() {
		CompanyImpl companyImpl = new CompanyImpl();

		companyImpl.setCompanyId(getCompanyId());
		companyImpl.setAccountId(getAccountId());
		companyImpl.setWebId(getWebId());
		companyImpl.setKey(getKey());
		companyImpl.setMx(getMx());
		companyImpl.setHomeURL(getHomeURL());
		companyImpl.setLogoId(getLogoId());
		companyImpl.setSystem(getSystem());
		companyImpl.setMaxUsers(getMaxUsers());
		companyImpl.setActive(getActive());

		companyImpl.resetOriginalValues();

		return companyImpl;
	}

	public int compareTo(Company company) {
		long primaryKey = company.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		Company company = null;

		try {
			company = (Company)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = company.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale) {
	}

	@Override
	public void resetOriginalValues() {
		CompanyModelImpl companyModelImpl = this;

		companyModelImpl._originalWebId = companyModelImpl._webId;

		companyModelImpl._originalMx = companyModelImpl._mx;

		companyModelImpl._originalLogoId = companyModelImpl._logoId;

		companyModelImpl._setOriginalLogoId = false;

		companyModelImpl._originalSystem = companyModelImpl._system;

		companyModelImpl._setOriginalSystem = false;

		companyModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<Company> toCacheModel() {
		CompanyCacheModel companyCacheModel = new CompanyCacheModel();

		companyCacheModel.companyId = getCompanyId();

		companyCacheModel.accountId = getAccountId();

		companyCacheModel.webId = getWebId();

		String webId = companyCacheModel.webId;

		if ((webId != null) && (webId.length() == 0)) {
			companyCacheModel.webId = null;
		}

		companyCacheModel.key = getKey();

		String key = companyCacheModel.key;

		if ((key != null) && (key.length() == 0)) {
			companyCacheModel.key = null;
		}

		companyCacheModel.mx = getMx();

		String mx = companyCacheModel.mx;

		if ((mx != null) && (mx.length() == 0)) {
			companyCacheModel.mx = null;
		}

		companyCacheModel.homeURL = getHomeURL();

		String homeURL = companyCacheModel.homeURL;

		if ((homeURL != null) && (homeURL.length() == 0)) {
			companyCacheModel.homeURL = null;
		}

		companyCacheModel.logoId = getLogoId();

		companyCacheModel.system = getSystem();

		companyCacheModel.maxUsers = getMaxUsers();

		companyCacheModel.active = getActive();

		companyCacheModel._keyObj = getKeyObj();

		return companyCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(21);

		sb.append("{companyId=");
		sb.append(getCompanyId());
		sb.append(", accountId=");
		sb.append(getAccountId());
		sb.append(", webId=");
		sb.append(getWebId());
		sb.append(", key=");
		sb.append(getKey());
		sb.append(", mx=");
		sb.append(getMx());
		sb.append(", homeURL=");
		sb.append(getHomeURL());
		sb.append(", logoId=");
		sb.append(getLogoId());
		sb.append(", system=");
		sb.append(getSystem());
		sb.append(", maxUsers=");
		sb.append(getMaxUsers());
		sb.append(", active=");
		sb.append(getActive());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(34);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portal.model.Company");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>accountId</column-name><column-value><![CDATA[");
		sb.append(getAccountId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>webId</column-name><column-value><![CDATA[");
		sb.append(getWebId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>key</column-name><column-value><![CDATA[");
		sb.append(getKey());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>mx</column-name><column-value><![CDATA[");
		sb.append(getMx());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>homeURL</column-name><column-value><![CDATA[");
		sb.append(getHomeURL());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>logoId</column-name><column-value><![CDATA[");
		sb.append(getLogoId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>system</column-name><column-value><![CDATA[");
		sb.append(getSystem());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>maxUsers</column-name><column-value><![CDATA[");
		sb.append(getMaxUsers());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>active</column-name><column-value><![CDATA[");
		sb.append(getActive());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = Company.class.getClassLoader();
	private static Class<?>[] _escapedModelProxyInterfaces = new Class[] {
			Company.class
		};
	private long _companyId;
	private long _accountId;
	private String _webId;
	private String _originalWebId;
	private String _key;
	private String _mx;
	private String _originalMx;
	private String _homeURL;
	private long _logoId;
	private long _originalLogoId;
	private boolean _setOriginalLogoId;
	private boolean _system;
	private boolean _originalSystem;
	private boolean _setOriginalSystem;
	private int _maxUsers;
	private boolean _active;
	private long _columnBitmask;
	private Company _escapedModelProxy;
}