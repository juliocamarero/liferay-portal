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

package com.liferay.portal.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Account;
import com.liferay.portal.model.AccountModel;
import com.liferay.portal.model.AccountSoap;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the Account service. Represents a row in the &quot;Account_&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link AccountModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link AccountImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AccountImpl
 * @see Account
 * @see AccountModel
 * @generated
 */
@JSON(strict = true)
@ProviderType
public class AccountModelImpl extends BaseModelImpl<Account>
	implements AccountModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a account model instance should use the {@link Account} interface instead.
	 */
	public static final String TABLE_NAME = "Account_";
	public static final Object[][] TABLE_COLUMNS = {
			{ "mvccVersion", Types.BIGINT },
			{ "accountId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "parentAccountId", Types.BIGINT },
			{ "name", Types.VARCHAR },
			{ "legalName", Types.VARCHAR },
			{ "legalId", Types.VARCHAR },
			{ "legalType", Types.VARCHAR },
			{ "sicCode", Types.VARCHAR },
			{ "tickerSymbol", Types.VARCHAR },
			{ "industry", Types.VARCHAR },
			{ "type_", Types.VARCHAR },
			{ "size_", Types.VARCHAR }
		};
	public static final String TABLE_SQL_CREATE = "create table Account_ (mvccVersion LONG default 0,accountId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,parentAccountId LONG,name VARCHAR(75) null,legalName VARCHAR(75) null,legalId VARCHAR(75) null,legalType VARCHAR(75) null,sicCode VARCHAR(75) null,tickerSymbol VARCHAR(75) null,industry VARCHAR(75) null,type_ VARCHAR(75) null,size_ VARCHAR(75) null)";
	public static final String TABLE_SQL_DROP = "drop table Account_";
	public static final String ORDER_BY_JPQL = " ORDER BY account.accountId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY Account_.accountId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portal.model.Account"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portal.model.Account"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = false;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static Account toModel(AccountSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		Account model = new AccountImpl();

		model.setMvccVersion(soapModel.getMvccVersion());
		model.setAccountId(soapModel.getAccountId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setParentAccountId(soapModel.getParentAccountId());
		model.setName(soapModel.getName());
		model.setLegalName(soapModel.getLegalName());
		model.setLegalId(soapModel.getLegalId());
		model.setLegalType(soapModel.getLegalType());
		model.setSicCode(soapModel.getSicCode());
		model.setTickerSymbol(soapModel.getTickerSymbol());
		model.setIndustry(soapModel.getIndustry());
		model.setType(soapModel.getType());
		model.setSize(soapModel.getSize());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<Account> toModels(AccountSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<Account> models = new ArrayList<Account>(soapModels.length);

		for (AccountSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portal.model.Account"));

	public AccountModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _accountId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setAccountId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _accountId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return Account.class;
	}

	@Override
	public String getModelClassName() {
		return Account.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("accountId", getAccountId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("parentAccountId", getParentAccountId());
		attributes.put("name", getName());
		attributes.put("legalName", getLegalName());
		attributes.put("legalId", getLegalId());
		attributes.put("legalType", getLegalType());
		attributes.put("sicCode", getSicCode());
		attributes.put("tickerSymbol", getTickerSymbol());
		attributes.put("industry", getIndustry());
		attributes.put("type", getType());
		attributes.put("size", getSize());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		Long accountId = (Long)attributes.get("accountId");

		if (accountId != null) {
			setAccountId(accountId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long parentAccountId = (Long)attributes.get("parentAccountId");

		if (parentAccountId != null) {
			setParentAccountId(parentAccountId);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String legalName = (String)attributes.get("legalName");

		if (legalName != null) {
			setLegalName(legalName);
		}

		String legalId = (String)attributes.get("legalId");

		if (legalId != null) {
			setLegalId(legalId);
		}

		String legalType = (String)attributes.get("legalType");

		if (legalType != null) {
			setLegalType(legalType);
		}

		String sicCode = (String)attributes.get("sicCode");

		if (sicCode != null) {
			setSicCode(sicCode);
		}

		String tickerSymbol = (String)attributes.get("tickerSymbol");

		if (tickerSymbol != null) {
			setTickerSymbol(tickerSymbol);
		}

		String industry = (String)attributes.get("industry");

		if (industry != null) {
			setIndustry(industry);
		}

		String type = (String)attributes.get("type");

		if (type != null) {
			setType(type);
		}

		String size = (String)attributes.get("size");

		if (size != null) {
			setSize(size);
		}
	}

	@JSON
	@Override
	public long getMvccVersion() {
		return _mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	@JSON
	@Override
	public long getAccountId() {
		return _accountId;
	}

	@Override
	public void setAccountId(long accountId) {
		_accountId = accountId;
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	@JSON
	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return StringPool.BLANK;
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@JSON
	@Override
	public String getUserName() {
		if (_userName == null) {
			return StringPool.BLANK;
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@JSON
	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		_modifiedDate = modifiedDate;
	}

	@JSON
	@Override
	public long getParentAccountId() {
		return _parentAccountId;
	}

	@Override
	public void setParentAccountId(long parentAccountId) {
		_parentAccountId = parentAccountId;
	}

	@JSON
	@Override
	public String getName() {
		if (_name == null) {
			return StringPool.BLANK;
		}
		else {
			return _name;
		}
	}

	@Override
	public void setName(String name) {
		_name = name;
	}

	@JSON
	@Override
	public String getLegalName() {
		if (_legalName == null) {
			return StringPool.BLANK;
		}
		else {
			return _legalName;
		}
	}

	@Override
	public void setLegalName(String legalName) {
		_legalName = legalName;
	}

	@JSON
	@Override
	public String getLegalId() {
		if (_legalId == null) {
			return StringPool.BLANK;
		}
		else {
			return _legalId;
		}
	}

	@Override
	public void setLegalId(String legalId) {
		_legalId = legalId;
	}

	@JSON
	@Override
	public String getLegalType() {
		if (_legalType == null) {
			return StringPool.BLANK;
		}
		else {
			return _legalType;
		}
	}

	@Override
	public void setLegalType(String legalType) {
		_legalType = legalType;
	}

	@JSON
	@Override
	public String getSicCode() {
		if (_sicCode == null) {
			return StringPool.BLANK;
		}
		else {
			return _sicCode;
		}
	}

	@Override
	public void setSicCode(String sicCode) {
		_sicCode = sicCode;
	}

	@JSON
	@Override
	public String getTickerSymbol() {
		if (_tickerSymbol == null) {
			return StringPool.BLANK;
		}
		else {
			return _tickerSymbol;
		}
	}

	@Override
	public void setTickerSymbol(String tickerSymbol) {
		_tickerSymbol = tickerSymbol;
	}

	@JSON
	@Override
	public String getIndustry() {
		if (_industry == null) {
			return StringPool.BLANK;
		}
		else {
			return _industry;
		}
	}

	@Override
	public void setIndustry(String industry) {
		_industry = industry;
	}

	@JSON
	@Override
	public String getType() {
		if (_type == null) {
			return StringPool.BLANK;
		}
		else {
			return _type;
		}
	}

	@Override
	public void setType(String type) {
		_type = type;
	}

	@JSON
	@Override
	public String getSize() {
		if (_size == null) {
			return StringPool.BLANK;
		}
		else {
			return _size;
		}
	}

	@Override
	public void setSize(String size) {
		_size = size;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			Account.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public Account toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (Account)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		AccountImpl accountImpl = new AccountImpl();

		accountImpl.setMvccVersion(getMvccVersion());
		accountImpl.setAccountId(getAccountId());
		accountImpl.setCompanyId(getCompanyId());
		accountImpl.setUserId(getUserId());
		accountImpl.setUserName(getUserName());
		accountImpl.setCreateDate(getCreateDate());
		accountImpl.setModifiedDate(getModifiedDate());
		accountImpl.setParentAccountId(getParentAccountId());
		accountImpl.setName(getName());
		accountImpl.setLegalName(getLegalName());
		accountImpl.setLegalId(getLegalId());
		accountImpl.setLegalType(getLegalType());
		accountImpl.setSicCode(getSicCode());
		accountImpl.setTickerSymbol(getTickerSymbol());
		accountImpl.setIndustry(getIndustry());
		accountImpl.setType(getType());
		accountImpl.setSize(getSize());

		accountImpl.resetOriginalValues();

		return accountImpl;
	}

	@Override
	public int compareTo(Account account) {
		long primaryKey = account.getPrimaryKey();

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
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Account)) {
			return false;
		}

		Account account = (Account)obj;

		long primaryKey = account.getPrimaryKey();

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

	@Override
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		AccountModelImpl accountModelImpl = this;

		accountModelImpl._setModifiedDate = false;
	}

	@Override
	public CacheModel<Account> toCacheModel() {
		AccountCacheModel accountCacheModel = new AccountCacheModel();

		accountCacheModel.mvccVersion = getMvccVersion();

		accountCacheModel.accountId = getAccountId();

		accountCacheModel.companyId = getCompanyId();

		accountCacheModel.userId = getUserId();

		accountCacheModel.userName = getUserName();

		String userName = accountCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			accountCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			accountCacheModel.createDate = createDate.getTime();
		}
		else {
			accountCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			accountCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			accountCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		accountCacheModel.parentAccountId = getParentAccountId();

		accountCacheModel.name = getName();

		String name = accountCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			accountCacheModel.name = null;
		}

		accountCacheModel.legalName = getLegalName();

		String legalName = accountCacheModel.legalName;

		if ((legalName != null) && (legalName.length() == 0)) {
			accountCacheModel.legalName = null;
		}

		accountCacheModel.legalId = getLegalId();

		String legalId = accountCacheModel.legalId;

		if ((legalId != null) && (legalId.length() == 0)) {
			accountCacheModel.legalId = null;
		}

		accountCacheModel.legalType = getLegalType();

		String legalType = accountCacheModel.legalType;

		if ((legalType != null) && (legalType.length() == 0)) {
			accountCacheModel.legalType = null;
		}

		accountCacheModel.sicCode = getSicCode();

		String sicCode = accountCacheModel.sicCode;

		if ((sicCode != null) && (sicCode.length() == 0)) {
			accountCacheModel.sicCode = null;
		}

		accountCacheModel.tickerSymbol = getTickerSymbol();

		String tickerSymbol = accountCacheModel.tickerSymbol;

		if ((tickerSymbol != null) && (tickerSymbol.length() == 0)) {
			accountCacheModel.tickerSymbol = null;
		}

		accountCacheModel.industry = getIndustry();

		String industry = accountCacheModel.industry;

		if ((industry != null) && (industry.length() == 0)) {
			accountCacheModel.industry = null;
		}

		accountCacheModel.type = getType();

		String type = accountCacheModel.type;

		if ((type != null) && (type.length() == 0)) {
			accountCacheModel.type = null;
		}

		accountCacheModel.size = getSize();

		String size = accountCacheModel.size;

		if ((size != null) && (size.length() == 0)) {
			accountCacheModel.size = null;
		}

		return accountCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(35);

		sb.append("{mvccVersion=");
		sb.append(getMvccVersion());
		sb.append(", accountId=");
		sb.append(getAccountId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append(", parentAccountId=");
		sb.append(getParentAccountId());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", legalName=");
		sb.append(getLegalName());
		sb.append(", legalId=");
		sb.append(getLegalId());
		sb.append(", legalType=");
		sb.append(getLegalType());
		sb.append(", sicCode=");
		sb.append(getSicCode());
		sb.append(", tickerSymbol=");
		sb.append(getTickerSymbol());
		sb.append(", industry=");
		sb.append(getIndustry());
		sb.append(", type=");
		sb.append(getType());
		sb.append(", size=");
		sb.append(getSize());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(55);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portal.model.Account");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>mvccVersion</column-name><column-value><![CDATA[");
		sb.append(getMvccVersion());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>accountId</column-name><column-value><![CDATA[");
		sb.append(getAccountId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userName</column-name><column-value><![CDATA[");
		sb.append(getUserName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
		sb.append(getModifiedDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>parentAccountId</column-name><column-value><![CDATA[");
		sb.append(getParentAccountId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>name</column-name><column-value><![CDATA[");
		sb.append(getName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>legalName</column-name><column-value><![CDATA[");
		sb.append(getLegalName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>legalId</column-name><column-value><![CDATA[");
		sb.append(getLegalId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>legalType</column-name><column-value><![CDATA[");
		sb.append(getLegalType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>sicCode</column-name><column-value><![CDATA[");
		sb.append(getSicCode());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>tickerSymbol</column-name><column-value><![CDATA[");
		sb.append(getTickerSymbol());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>industry</column-name><column-value><![CDATA[");
		sb.append(getIndustry());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>type</column-name><column-value><![CDATA[");
		sb.append(getType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>size</column-name><column-value><![CDATA[");
		sb.append(getSize());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = Account.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			Account.class
		};
	private long _mvccVersion;
	private long _accountId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private long _parentAccountId;
	private String _name;
	private String _legalName;
	private String _legalId;
	private String _legalType;
	private String _sicCode;
	private String _tickerSymbol;
	private String _industry;
	private String _type;
	private String _size;
	private Account _escapedModel;
}