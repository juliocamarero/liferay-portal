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

package com.liferay.oauth2.provider.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;

import com.liferay.oauth2.provider.model.OAuth2RefreshToken;
import com.liferay.oauth2.provider.model.OAuth2RefreshTokenModel;
import com.liferay.oauth2.provider.model.OAuth2RefreshTokenSoap;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the OAuth2RefreshToken service. Represents a row in the &quot;OAuth2RefreshToken&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link OAuth2RefreshTokenModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link OAuth2RefreshTokenImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2RefreshTokenImpl
 * @see OAuth2RefreshToken
 * @see OAuth2RefreshTokenModel
 * @generated
 */
@ProviderType
public class OAuth2RefreshTokenModelImpl extends BaseModelImpl<OAuth2RefreshToken>
	implements OAuth2RefreshTokenModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a o auth2 refresh token model instance should use the {@link OAuth2RefreshToken} interface instead.
	 */
	public static final String TABLE_NAME = "OAuth2RefreshToken";
	public static final Object[][] TABLE_COLUMNS = {
			{ "oAuth2RefreshTokenId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "oAuth2ApplicationId", Types.BIGINT },
			{ "expirationDate", Types.TIMESTAMP },
			{ "remoteIPInfo", Types.VARCHAR },
			{ "scopeAliases", Types.CLOB },
			{ "tokenContent", Types.CLOB }
		};
	public static final Map<String, Integer> TABLE_COLUMNS_MAP = new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("oAuth2RefreshTokenId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("oAuth2ApplicationId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("expirationDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("remoteIPInfo", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("scopeAliases", Types.CLOB);
		TABLE_COLUMNS_MAP.put("tokenContent", Types.CLOB);
	}

	public static final String TABLE_SQL_CREATE = "create table OAuth2RefreshToken (oAuth2RefreshTokenId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,oAuth2ApplicationId LONG,expirationDate DATE null,remoteIPInfo VARCHAR(75) null,scopeAliases TEXT null,tokenContent TEXT null)";
	public static final String TABLE_SQL_DROP = "drop table OAuth2RefreshToken";
	public static final String ORDER_BY_JPQL = " ORDER BY oAuth2RefreshToken.oAuth2RefreshTokenId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY OAuth2RefreshToken.oAuth2RefreshTokenId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.oauth2.provider.service.util.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.oauth2.provider.model.OAuth2RefreshToken"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.oauth2.provider.service.util.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.oauth2.provider.model.OAuth2RefreshToken"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.oauth2.provider.service.util.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.oauth2.provider.model.OAuth2RefreshToken"),
			true);
	public static final long OAUTH2APPLICATIONID_COLUMN_BITMASK = 1L;
	public static final long TOKENCONTENT_COLUMN_BITMASK = 2L;
	public static final long OAUTH2REFRESHTOKENID_COLUMN_BITMASK = 4L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static OAuth2RefreshToken toModel(OAuth2RefreshTokenSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		OAuth2RefreshToken model = new OAuth2RefreshTokenImpl();

		model.setOAuth2RefreshTokenId(soapModel.getOAuth2RefreshTokenId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setOAuth2ApplicationId(soapModel.getOAuth2ApplicationId());
		model.setExpirationDate(soapModel.getExpirationDate());
		model.setRemoteIPInfo(soapModel.getRemoteIPInfo());
		model.setScopeAliases(soapModel.getScopeAliases());
		model.setTokenContent(soapModel.getTokenContent());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<OAuth2RefreshToken> toModels(
		OAuth2RefreshTokenSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<OAuth2RefreshToken> models = new ArrayList<OAuth2RefreshToken>(soapModels.length);

		for (OAuth2RefreshTokenSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.oauth2.provider.service.util.ServiceProps.get(
				"lock.expiration.time.com.liferay.oauth2.provider.model.OAuth2RefreshToken"));

	public OAuth2RefreshTokenModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _oAuth2RefreshTokenId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setOAuth2RefreshTokenId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _oAuth2RefreshTokenId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return OAuth2RefreshToken.class;
	}

	@Override
	public String getModelClassName() {
		return OAuth2RefreshToken.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("oAuth2RefreshTokenId", getOAuth2RefreshTokenId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("oAuth2ApplicationId", getOAuth2ApplicationId());
		attributes.put("expirationDate", getExpirationDate());
		attributes.put("remoteIPInfo", getRemoteIPInfo());
		attributes.put("scopeAliases", getScopeAliases());
		attributes.put("tokenContent", getTokenContent());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long oAuth2RefreshTokenId = (Long)attributes.get("oAuth2RefreshTokenId");

		if (oAuth2RefreshTokenId != null) {
			setOAuth2RefreshTokenId(oAuth2RefreshTokenId);
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

		Long oAuth2ApplicationId = (Long)attributes.get("oAuth2ApplicationId");

		if (oAuth2ApplicationId != null) {
			setOAuth2ApplicationId(oAuth2ApplicationId);
		}

		Date expirationDate = (Date)attributes.get("expirationDate");

		if (expirationDate != null) {
			setExpirationDate(expirationDate);
		}

		String remoteIPInfo = (String)attributes.get("remoteIPInfo");

		if (remoteIPInfo != null) {
			setRemoteIPInfo(remoteIPInfo);
		}

		String scopeAliases = (String)attributes.get("scopeAliases");

		if (scopeAliases != null) {
			setScopeAliases(scopeAliases);
		}

		String tokenContent = (String)attributes.get("tokenContent");

		if (tokenContent != null) {
			setTokenContent(tokenContent);
		}
	}

	@Override
	public long getOAuth2RefreshTokenId() {
		return _oAuth2RefreshTokenId;
	}

	@Override
	public void setOAuth2RefreshTokenId(long oAuth2RefreshTokenId) {
		_oAuth2RefreshTokenId = oAuth2RefreshTokenId;
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

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
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
	}

	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@Override
	public long getOAuth2ApplicationId() {
		return _oAuth2ApplicationId;
	}

	@Override
	public void setOAuth2ApplicationId(long oAuth2ApplicationId) {
		_columnBitmask |= OAUTH2APPLICATIONID_COLUMN_BITMASK;

		if (!_setOriginalOAuth2ApplicationId) {
			_setOriginalOAuth2ApplicationId = true;

			_originalOAuth2ApplicationId = _oAuth2ApplicationId;
		}

		_oAuth2ApplicationId = oAuth2ApplicationId;
	}

	public long getOriginalOAuth2ApplicationId() {
		return _originalOAuth2ApplicationId;
	}

	@Override
	public Date getExpirationDate() {
		return _expirationDate;
	}

	@Override
	public void setExpirationDate(Date expirationDate) {
		_expirationDate = expirationDate;
	}

	@Override
	public String getRemoteIPInfo() {
		if (_remoteIPInfo == null) {
			return "";
		}
		else {
			return _remoteIPInfo;
		}
	}

	@Override
	public void setRemoteIPInfo(String remoteIPInfo) {
		_remoteIPInfo = remoteIPInfo;
	}

	@Override
	public String getScopeAliases() {
		if (_scopeAliases == null) {
			return "";
		}
		else {
			return _scopeAliases;
		}
	}

	@Override
	public void setScopeAliases(String scopeAliases) {
		_scopeAliases = scopeAliases;
	}

	@Override
	public String getTokenContent() {
		if (_tokenContent == null) {
			return "";
		}
		else {
			return _tokenContent;
		}
	}

	@Override
	public void setTokenContent(String tokenContent) {
		_columnBitmask |= TOKENCONTENT_COLUMN_BITMASK;

		if (_originalTokenContent == null) {
			_originalTokenContent = _tokenContent;
		}

		_tokenContent = tokenContent;
	}

	public String getOriginalTokenContent() {
		return GetterUtil.getString(_originalTokenContent);
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			OAuth2RefreshToken.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public OAuth2RefreshToken toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (OAuth2RefreshToken)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		OAuth2RefreshTokenImpl oAuth2RefreshTokenImpl = new OAuth2RefreshTokenImpl();

		oAuth2RefreshTokenImpl.setOAuth2RefreshTokenId(getOAuth2RefreshTokenId());
		oAuth2RefreshTokenImpl.setCompanyId(getCompanyId());
		oAuth2RefreshTokenImpl.setUserId(getUserId());
		oAuth2RefreshTokenImpl.setUserName(getUserName());
		oAuth2RefreshTokenImpl.setCreateDate(getCreateDate());
		oAuth2RefreshTokenImpl.setOAuth2ApplicationId(getOAuth2ApplicationId());
		oAuth2RefreshTokenImpl.setExpirationDate(getExpirationDate());
		oAuth2RefreshTokenImpl.setRemoteIPInfo(getRemoteIPInfo());
		oAuth2RefreshTokenImpl.setScopeAliases(getScopeAliases());
		oAuth2RefreshTokenImpl.setTokenContent(getTokenContent());

		oAuth2RefreshTokenImpl.resetOriginalValues();

		return oAuth2RefreshTokenImpl;
	}

	@Override
	public int compareTo(OAuth2RefreshToken oAuth2RefreshToken) {
		long primaryKey = oAuth2RefreshToken.getPrimaryKey();

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

		if (!(obj instanceof OAuth2RefreshToken)) {
			return false;
		}

		OAuth2RefreshToken oAuth2RefreshToken = (OAuth2RefreshToken)obj;

		long primaryKey = oAuth2RefreshToken.getPrimaryKey();

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
		OAuth2RefreshTokenModelImpl oAuth2RefreshTokenModelImpl = this;

		oAuth2RefreshTokenModelImpl._originalOAuth2ApplicationId = oAuth2RefreshTokenModelImpl._oAuth2ApplicationId;

		oAuth2RefreshTokenModelImpl._setOriginalOAuth2ApplicationId = false;

		oAuth2RefreshTokenModelImpl._originalTokenContent = oAuth2RefreshTokenModelImpl._tokenContent;

		oAuth2RefreshTokenModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<OAuth2RefreshToken> toCacheModel() {
		OAuth2RefreshTokenCacheModel oAuth2RefreshTokenCacheModel = new OAuth2RefreshTokenCacheModel();

		oAuth2RefreshTokenCacheModel.oAuth2RefreshTokenId = getOAuth2RefreshTokenId();

		oAuth2RefreshTokenCacheModel.companyId = getCompanyId();

		oAuth2RefreshTokenCacheModel.userId = getUserId();

		oAuth2RefreshTokenCacheModel.userName = getUserName();

		String userName = oAuth2RefreshTokenCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			oAuth2RefreshTokenCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			oAuth2RefreshTokenCacheModel.createDate = createDate.getTime();
		}
		else {
			oAuth2RefreshTokenCacheModel.createDate = Long.MIN_VALUE;
		}

		oAuth2RefreshTokenCacheModel.oAuth2ApplicationId = getOAuth2ApplicationId();

		Date expirationDate = getExpirationDate();

		if (expirationDate != null) {
			oAuth2RefreshTokenCacheModel.expirationDate = expirationDate.getTime();
		}
		else {
			oAuth2RefreshTokenCacheModel.expirationDate = Long.MIN_VALUE;
		}

		oAuth2RefreshTokenCacheModel.remoteIPInfo = getRemoteIPInfo();

		String remoteIPInfo = oAuth2RefreshTokenCacheModel.remoteIPInfo;

		if ((remoteIPInfo != null) && (remoteIPInfo.length() == 0)) {
			oAuth2RefreshTokenCacheModel.remoteIPInfo = null;
		}

		oAuth2RefreshTokenCacheModel.scopeAliases = getScopeAliases();

		String scopeAliases = oAuth2RefreshTokenCacheModel.scopeAliases;

		if ((scopeAliases != null) && (scopeAliases.length() == 0)) {
			oAuth2RefreshTokenCacheModel.scopeAliases = null;
		}

		oAuth2RefreshTokenCacheModel.tokenContent = getTokenContent();

		String tokenContent = oAuth2RefreshTokenCacheModel.tokenContent;

		if ((tokenContent != null) && (tokenContent.length() == 0)) {
			oAuth2RefreshTokenCacheModel.tokenContent = null;
		}

		return oAuth2RefreshTokenCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(21);

		sb.append("{oAuth2RefreshTokenId=");
		sb.append(getOAuth2RefreshTokenId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", oAuth2ApplicationId=");
		sb.append(getOAuth2ApplicationId());
		sb.append(", expirationDate=");
		sb.append(getExpirationDate());
		sb.append(", remoteIPInfo=");
		sb.append(getRemoteIPInfo());
		sb.append(", scopeAliases=");
		sb.append(getScopeAliases());
		sb.append(", tokenContent=");
		sb.append(getTokenContent());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(34);

		sb.append("<model><model-name>");
		sb.append("com.liferay.oauth2.provider.model.OAuth2RefreshToken");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>oAuth2RefreshTokenId</column-name><column-value><![CDATA[");
		sb.append(getOAuth2RefreshTokenId());
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
			"<column><column-name>oAuth2ApplicationId</column-name><column-value><![CDATA[");
		sb.append(getOAuth2ApplicationId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>expirationDate</column-name><column-value><![CDATA[");
		sb.append(getExpirationDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>remoteIPInfo</column-name><column-value><![CDATA[");
		sb.append(getRemoteIPInfo());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>scopeAliases</column-name><column-value><![CDATA[");
		sb.append(getScopeAliases());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>tokenContent</column-name><column-value><![CDATA[");
		sb.append(getTokenContent());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = OAuth2RefreshToken.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			OAuth2RefreshToken.class
		};
	private long _oAuth2RefreshTokenId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private long _oAuth2ApplicationId;
	private long _originalOAuth2ApplicationId;
	private boolean _setOriginalOAuth2ApplicationId;
	private Date _expirationDate;
	private String _remoteIPInfo;
	private String _scopeAliases;
	private String _tokenContent;
	private String _originalTokenContent;
	private long _columnBitmask;
	private OAuth2RefreshToken _escapedModel;
}