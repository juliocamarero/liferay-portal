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

package com.liferay.journal.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;

import com.liferay.journal.model.JournalArticleLocalization;
import com.liferay.journal.model.JournalArticleLocalizationModel;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Serializable;

import java.sql.Types;

import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the JournalArticleLocalization service. Represents a row in the &quot;JournalArticleLocalization&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link JournalArticleLocalizationModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link JournalArticleLocalizationImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see JournalArticleLocalizationImpl
 * @see JournalArticleLocalization
 * @see JournalArticleLocalizationModel
 * @generated
 */
@ProviderType
public class JournalArticleLocalizationModelImpl extends BaseModelImpl<JournalArticleLocalization>
	implements JournalArticleLocalizationModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a journal article localization model instance should use the {@link JournalArticleLocalization} interface instead.
	 */
	public static final String TABLE_NAME = "JournalArticleLocalization";
	public static final Object[][] TABLE_COLUMNS = {
			{ "articleLocalizationId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "articlePK", Types.BIGINT },
			{ "title", Types.VARCHAR },
			{ "description", Types.VARCHAR },
			{ "languageId", Types.VARCHAR }
		};
	public static final Map<String, Integer> TABLE_COLUMNS_MAP = new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("articleLocalizationId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("articlePK", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("title", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("description", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("languageId", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE = "create table JournalArticleLocalization (articleLocalizationId LONG not null primary key,companyId LONG,articlePK LONG,title VARCHAR(400) null,description STRING null,languageId VARCHAR(75) null)";
	public static final String TABLE_SQL_DROP = "drop table JournalArticleLocalization";
	public static final String ORDER_BY_JPQL = " ORDER BY journalArticleLocalization.articleLocalizationId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY JournalArticleLocalization.articleLocalizationId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.journal.service.util.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.journal.model.JournalArticleLocalization"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.journal.service.util.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.journal.model.JournalArticleLocalization"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.journal.service.util.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.journal.model.JournalArticleLocalization"),
			true);
	public static final long ARTICLEPK_COLUMN_BITMASK = 1L;
	public static final long LANGUAGEID_COLUMN_BITMASK = 2L;
	public static final long ARTICLELOCALIZATIONID_COLUMN_BITMASK = 4L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.journal.service.util.ServiceProps.get(
				"lock.expiration.time.com.liferay.journal.model.JournalArticleLocalization"));

	public JournalArticleLocalizationModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _articleLocalizationId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setArticleLocalizationId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _articleLocalizationId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return JournalArticleLocalization.class;
	}

	@Override
	public String getModelClassName() {
		return JournalArticleLocalization.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("articleLocalizationId", getArticleLocalizationId());
		attributes.put("companyId", getCompanyId());
		attributes.put("articlePK", getArticlePK());
		attributes.put("title", getTitle());
		attributes.put("description", getDescription());
		attributes.put("languageId", getLanguageId());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long articleLocalizationId = (Long)attributes.get(
				"articleLocalizationId");

		if (articleLocalizationId != null) {
			setArticleLocalizationId(articleLocalizationId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long articlePK = (Long)attributes.get("articlePK");

		if (articlePK != null) {
			setArticlePK(articlePK);
		}

		String title = (String)attributes.get("title");

		if (title != null) {
			setTitle(title);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		String languageId = (String)attributes.get("languageId");

		if (languageId != null) {
			setLanguageId(languageId);
		}
	}

	@Override
	public long getArticleLocalizationId() {
		return _articleLocalizationId;
	}

	@Override
	public void setArticleLocalizationId(long articleLocalizationId) {
		_articleLocalizationId = articleLocalizationId;
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
	public long getArticlePK() {
		return _articlePK;
	}

	@Override
	public void setArticlePK(long articlePK) {
		_columnBitmask |= ARTICLEPK_COLUMN_BITMASK;

		if (!_setOriginalArticlePK) {
			_setOriginalArticlePK = true;

			_originalArticlePK = _articlePK;
		}

		_articlePK = articlePK;
	}

	public long getOriginalArticlePK() {
		return _originalArticlePK;
	}

	@Override
	public String getTitle() {
		if (_title == null) {
			return "";
		}
		else {
			return _title;
		}
	}

	@Override
	public void setTitle(String title) {
		_title = title;
	}

	@Override
	public String getDescription() {
		if (_description == null) {
			return "";
		}
		else {
			return _description;
		}
	}

	@Override
	public void setDescription(String description) {
		_description = description;
	}

	@Override
	public String getLanguageId() {
		if (_languageId == null) {
			return "";
		}
		else {
			return _languageId;
		}
	}

	@Override
	public void setLanguageId(String languageId) {
		_columnBitmask |= LANGUAGEID_COLUMN_BITMASK;

		if (_originalLanguageId == null) {
			_originalLanguageId = _languageId;
		}

		_languageId = languageId;
	}

	public String getOriginalLanguageId() {
		return GetterUtil.getString(_originalLanguageId);
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			JournalArticleLocalization.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public JournalArticleLocalization toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (JournalArticleLocalization)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		JournalArticleLocalizationImpl journalArticleLocalizationImpl = new JournalArticleLocalizationImpl();

		journalArticleLocalizationImpl.setArticleLocalizationId(getArticleLocalizationId());
		journalArticleLocalizationImpl.setCompanyId(getCompanyId());
		journalArticleLocalizationImpl.setArticlePK(getArticlePK());
		journalArticleLocalizationImpl.setTitle(getTitle());
		journalArticleLocalizationImpl.setDescription(getDescription());
		journalArticleLocalizationImpl.setLanguageId(getLanguageId());

		journalArticleLocalizationImpl.resetOriginalValues();

		return journalArticleLocalizationImpl;
	}

	@Override
	public int compareTo(JournalArticleLocalization journalArticleLocalization) {
		long primaryKey = journalArticleLocalization.getPrimaryKey();

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

		if (!(obj instanceof JournalArticleLocalization)) {
			return false;
		}

		JournalArticleLocalization journalArticleLocalization = (JournalArticleLocalization)obj;

		long primaryKey = journalArticleLocalization.getPrimaryKey();

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
		JournalArticleLocalizationModelImpl journalArticleLocalizationModelImpl = this;

		journalArticleLocalizationModelImpl._originalArticlePK = journalArticleLocalizationModelImpl._articlePK;

		journalArticleLocalizationModelImpl._setOriginalArticlePK = false;

		journalArticleLocalizationModelImpl._originalLanguageId = journalArticleLocalizationModelImpl._languageId;

		journalArticleLocalizationModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<JournalArticleLocalization> toCacheModel() {
		JournalArticleLocalizationCacheModel journalArticleLocalizationCacheModel =
			new JournalArticleLocalizationCacheModel();

		journalArticleLocalizationCacheModel.articleLocalizationId = getArticleLocalizationId();

		journalArticleLocalizationCacheModel.companyId = getCompanyId();

		journalArticleLocalizationCacheModel.articlePK = getArticlePK();

		journalArticleLocalizationCacheModel.title = getTitle();

		String title = journalArticleLocalizationCacheModel.title;

		if ((title != null) && (title.length() == 0)) {
			journalArticleLocalizationCacheModel.title = null;
		}

		journalArticleLocalizationCacheModel.description = getDescription();

		String description = journalArticleLocalizationCacheModel.description;

		if ((description != null) && (description.length() == 0)) {
			journalArticleLocalizationCacheModel.description = null;
		}

		journalArticleLocalizationCacheModel.languageId = getLanguageId();

		String languageId = journalArticleLocalizationCacheModel.languageId;

		if ((languageId != null) && (languageId.length() == 0)) {
			journalArticleLocalizationCacheModel.languageId = null;
		}

		return journalArticleLocalizationCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(13);

		sb.append("{articleLocalizationId=");
		sb.append(getArticleLocalizationId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", articlePK=");
		sb.append(getArticlePK());
		sb.append(", title=");
		sb.append(getTitle());
		sb.append(", description=");
		sb.append(getDescription());
		sb.append(", languageId=");
		sb.append(getLanguageId());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(22);

		sb.append("<model><model-name>");
		sb.append("com.liferay.journal.model.JournalArticleLocalization");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>articleLocalizationId</column-name><column-value><![CDATA[");
		sb.append(getArticleLocalizationId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>articlePK</column-name><column-value><![CDATA[");
		sb.append(getArticlePK());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>title</column-name><column-value><![CDATA[");
		sb.append(getTitle());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>description</column-name><column-value><![CDATA[");
		sb.append(getDescription());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>languageId</column-name><column-value><![CDATA[");
		sb.append(getLanguageId());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = JournalArticleLocalization.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			JournalArticleLocalization.class
		};
	private long _articleLocalizationId;
	private long _companyId;
	private long _articlePK;
	private long _originalArticlePK;
	private boolean _setOriginalArticlePK;
	private String _title;
	private String _description;
	private String _languageId;
	private String _originalLanguageId;
	private long _columnBitmask;
	private JournalArticleLocalization _escapedModel;
}