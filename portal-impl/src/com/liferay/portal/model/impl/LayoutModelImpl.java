/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutModel;
import com.liferay.portal.model.LayoutSoap;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.lang.reflect.Proxy;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The base model implementation for the Layout service. Represents a row in the &quot;Layout&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portal.model.LayoutModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link LayoutImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see LayoutImpl
 * @see com.liferay.portal.model.Layout
 * @see com.liferay.portal.model.LayoutModel
 * @generated
 */
@JSON(strict = true)
public class LayoutModelImpl extends BaseModelImpl<Layout>
	implements LayoutModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a layout model instance should use the {@link com.liferay.portal.model.Layout} interface instead.
	 */
	public static final String TABLE_NAME = "Layout";
	public static final Object[][] TABLE_COLUMNS = {
			{ "uuid_", Types.VARCHAR },
			{ "plid", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "privateLayout", Types.BOOLEAN },
			{ "layoutId", Types.BIGINT },
			{ "parentLayoutId", Types.BIGINT },
			{ "name", Types.VARCHAR },
			{ "title", Types.VARCHAR },
			{ "description", Types.VARCHAR },
			{ "keywords", Types.VARCHAR },
			{ "robots", Types.VARCHAR },
			{ "type_", Types.VARCHAR },
			{ "typeSettings", Types.CLOB },
			{ "hidden_", Types.BOOLEAN },
			{ "friendlyURL", Types.VARCHAR },
			{ "iconImage", Types.BOOLEAN },
			{ "iconImageId", Types.BIGINT },
			{ "themeId", Types.VARCHAR },
			{ "colorSchemeId", Types.VARCHAR },
			{ "wapThemeId", Types.VARCHAR },
			{ "wapColorSchemeId", Types.VARCHAR },
			{ "css", Types.VARCHAR },
			{ "priority", Types.INTEGER },
			{ "layoutPrototypeUuid", Types.VARCHAR },
			{ "layoutPrototypeLinkEnabled", Types.BOOLEAN }
		};
	public static final String TABLE_SQL_CREATE = "create table Layout (uuid_ VARCHAR(75) null,plid LONG not null primary key,groupId LONG,companyId LONG,createDate DATE null,modifiedDate DATE null,privateLayout BOOLEAN,layoutId LONG,parentLayoutId LONG,name STRING null,title STRING null,description STRING null,keywords STRING null,robots STRING null,type_ VARCHAR(75) null,typeSettings TEXT null,hidden_ BOOLEAN,friendlyURL VARCHAR(255) null,iconImage BOOLEAN,iconImageId LONG,themeId VARCHAR(75) null,colorSchemeId VARCHAR(75) null,wapThemeId VARCHAR(75) null,wapColorSchemeId VARCHAR(75) null,css STRING null,priority INTEGER,layoutPrototypeUuid VARCHAR(75) null,layoutPrototypeLinkEnabled BOOLEAN)";
	public static final String TABLE_SQL_DROP = "drop table Layout";
	public static final String ORDER_BY_JPQL = " ORDER BY layout.parentLayoutId ASC, layout.priority ASC";
	public static final String ORDER_BY_SQL = " ORDER BY Layout.parentLayoutId ASC, Layout.priority ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portal.model.Layout"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portal.model.Layout"),
			true);

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static Layout toModel(LayoutSoap soapModel) {
		Layout model = new LayoutImpl();

		model.setUuid(soapModel.getUuid());
		model.setPlid(soapModel.getPlid());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setPrivateLayout(soapModel.getPrivateLayout());
		model.setLayoutId(soapModel.getLayoutId());
		model.setParentLayoutId(soapModel.getParentLayoutId());
		model.setName(soapModel.getName());
		model.setTitle(soapModel.getTitle());
		model.setDescription(soapModel.getDescription());
		model.setKeywords(soapModel.getKeywords());
		model.setRobots(soapModel.getRobots());
		model.setType(soapModel.getType());
		model.setTypeSettings(soapModel.getTypeSettings());
		model.setHidden(soapModel.getHidden());
		model.setFriendlyURL(soapModel.getFriendlyURL());
		model.setIconImage(soapModel.getIconImage());
		model.setIconImageId(soapModel.getIconImageId());
		model.setThemeId(soapModel.getThemeId());
		model.setColorSchemeId(soapModel.getColorSchemeId());
		model.setWapThemeId(soapModel.getWapThemeId());
		model.setWapColorSchemeId(soapModel.getWapColorSchemeId());
		model.setCss(soapModel.getCss());
		model.setPriority(soapModel.getPriority());
		model.setLayoutPrototypeUuid(soapModel.getLayoutPrototypeUuid());
		model.setLayoutPrototypeLinkEnabled(soapModel.getLayoutPrototypeLinkEnabled());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<Layout> toModels(LayoutSoap[] soapModels) {
		List<Layout> models = new ArrayList<Layout>(soapModels.length);

		for (LayoutSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public Class<?> getModelClass() {
		return Layout.class;
	}

	public String getModelClassName() {
		return Layout.class.getName();
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portal.model.Layout"));

	public LayoutModelImpl() {
	}

	public long getPrimaryKey() {
		return _plid;
	}

	public void setPrimaryKey(long primaryKey) {
		setPlid(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return new Long(_plid);
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@JSON
	public String getUuid() {
		if (_uuid == null) {
			return StringPool.BLANK;
		}
		else {
			return _uuid;
		}
	}

	public void setUuid(String uuid) {
		if (_originalUuid == null) {
			_originalUuid = _uuid;
		}

		_uuid = uuid;
	}

	public String getOriginalUuid() {
		return GetterUtil.getString(_originalUuid);
	}

	@JSON
	public long getPlid() {
		return _plid;
	}

	public void setPlid(long plid) {
		_plid = plid;
	}

	@JSON
	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		if (!_setOriginalGroupId) {
			_setOriginalGroupId = true;

			_originalGroupId = _groupId;
		}

		_groupId = groupId;
	}

	public long getOriginalGroupId() {
		return _originalGroupId;
	}

	@JSON
	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	@JSON
	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@JSON
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	@JSON
	public boolean getPrivateLayout() {
		return _privateLayout;
	}

	public boolean isPrivateLayout() {
		return _privateLayout;
	}

	public void setPrivateLayout(boolean privateLayout) {
		if (!_setOriginalPrivateLayout) {
			_setOriginalPrivateLayout = true;

			_originalPrivateLayout = _privateLayout;
		}

		_privateLayout = privateLayout;
	}

	public boolean getOriginalPrivateLayout() {
		return _originalPrivateLayout;
	}

	@JSON
	public long getLayoutId() {
		return _layoutId;
	}

	public void setLayoutId(long layoutId) {
		if (!_setOriginalLayoutId) {
			_setOriginalLayoutId = true;

			_originalLayoutId = _layoutId;
		}

		_layoutId = layoutId;
	}

	public long getOriginalLayoutId() {
		return _originalLayoutId;
	}

	@JSON
	public long getParentLayoutId() {
		return _parentLayoutId;
	}

	public void setParentLayoutId(long parentLayoutId) {
		_parentLayoutId = parentLayoutId;
	}

	@JSON
	public String getName() {
		if (_name == null) {
			return StringPool.BLANK;
		}
		else {
			return _name;
		}
	}

	public String getName(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getName(languageId);
	}

	public String getName(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getName(languageId, useDefault);
	}

	public String getName(String languageId) {
		String value = LocalizationUtil.getLocalization(getName(), languageId);

		if (isEscapedModel()) {
			return HtmlUtil.escape(value);
		}
		else {
			return value;
		}
	}

	public String getName(String languageId, boolean useDefault) {
		String value = LocalizationUtil.getLocalization(getName(), languageId,
				useDefault);

		if (isEscapedModel()) {
			return HtmlUtil.escape(value);
		}
		else {
			return value;
		}
	}

	public Map<Locale, String> getNameMap() {
		return LocalizationUtil.getLocalizationMap(getName());
	}

	public void setName(String name) {
		_name = name;
	}

	public void setName(String name, Locale locale) {
		setName(name, locale, LocaleUtil.getDefault());
	}

	public void setName(String name, Locale locale, Locale defaultLocale) {
		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(name)) {
			setName(LocalizationUtil.updateLocalization(getName(), "Name",
					name, languageId, defaultLanguageId));
		}
		else {
			setName(LocalizationUtil.removeLocalization(getName(), "Name",
					languageId));
		}
	}

	public void setNameMap(Map<Locale, String> nameMap) {
		setNameMap(nameMap, LocaleUtil.getDefault());
	}

	public void setNameMap(Map<Locale, String> nameMap, Locale defaultLocale) {
		if (nameMap == null) {
			return;
		}

		Locale[] locales = LanguageUtil.getAvailableLocales();

		for (Locale locale : locales) {
			String name = nameMap.get(locale);

			setName(name, locale, defaultLocale);
		}
	}

	@JSON
	public String getTitle() {
		if (_title == null) {
			return StringPool.BLANK;
		}
		else {
			return _title;
		}
	}

	public String getTitle(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getTitle(languageId);
	}

	public String getTitle(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getTitle(languageId, useDefault);
	}

	public String getTitle(String languageId) {
		String value = LocalizationUtil.getLocalization(getTitle(), languageId);

		if (isEscapedModel()) {
			return HtmlUtil.escape(value);
		}
		else {
			return value;
		}
	}

	public String getTitle(String languageId, boolean useDefault) {
		String value = LocalizationUtil.getLocalization(getTitle(), languageId,
				useDefault);

		if (isEscapedModel()) {
			return HtmlUtil.escape(value);
		}
		else {
			return value;
		}
	}

	public Map<Locale, String> getTitleMap() {
		return LocalizationUtil.getLocalizationMap(getTitle());
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setTitle(String title, Locale locale) {
		setTitle(title, locale, LocaleUtil.getDefault());
	}

	public void setTitle(String title, Locale locale, Locale defaultLocale) {
		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(title)) {
			setTitle(LocalizationUtil.updateLocalization(getTitle(), "Title",
					title, languageId, defaultLanguageId));
		}
		else {
			setTitle(LocalizationUtil.removeLocalization(getTitle(), "Title",
					languageId));
		}
	}

	public void setTitleMap(Map<Locale, String> titleMap) {
		setTitleMap(titleMap, LocaleUtil.getDefault());
	}

	public void setTitleMap(Map<Locale, String> titleMap, Locale defaultLocale) {
		if (titleMap == null) {
			return;
		}

		Locale[] locales = LanguageUtil.getAvailableLocales();

		for (Locale locale : locales) {
			String title = titleMap.get(locale);

			setTitle(title, locale, defaultLocale);
		}
	}

	@JSON
	public String getDescription() {
		if (_description == null) {
			return StringPool.BLANK;
		}
		else {
			return _description;
		}
	}

	public String getDescription(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getDescription(languageId);
	}

	public String getDescription(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getDescription(languageId, useDefault);
	}

	public String getDescription(String languageId) {
		String value = LocalizationUtil.getLocalization(getDescription(),
				languageId);

		if (isEscapedModel()) {
			return HtmlUtil.escape(value);
		}
		else {
			return value;
		}
	}

	public String getDescription(String languageId, boolean useDefault) {
		String value = LocalizationUtil.getLocalization(getDescription(),
				languageId, useDefault);

		if (isEscapedModel()) {
			return HtmlUtil.escape(value);
		}
		else {
			return value;
		}
	}

	public Map<Locale, String> getDescriptionMap() {
		return LocalizationUtil.getLocalizationMap(getDescription());
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setDescription(String description, Locale locale) {
		setDescription(description, locale, LocaleUtil.getDefault());
	}

	public void setDescription(String description, Locale locale,
		Locale defaultLocale) {
		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(description)) {
			setDescription(LocalizationUtil.updateLocalization(
					getDescription(), "Description", description, languageId,
					defaultLanguageId));
		}
		else {
			setDescription(LocalizationUtil.removeLocalization(
					getDescription(), "Description", languageId));
		}
	}

	public void setDescriptionMap(Map<Locale, String> descriptionMap) {
		setDescriptionMap(descriptionMap, LocaleUtil.getDefault());
	}

	public void setDescriptionMap(Map<Locale, String> descriptionMap,
		Locale defaultLocale) {
		if (descriptionMap == null) {
			return;
		}

		Locale[] locales = LanguageUtil.getAvailableLocales();

		for (Locale locale : locales) {
			String description = descriptionMap.get(locale);

			setDescription(description, locale, defaultLocale);
		}
	}

	@JSON
	public String getKeywords() {
		if (_keywords == null) {
			return StringPool.BLANK;
		}
		else {
			return _keywords;
		}
	}

	public String getKeywords(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getKeywords(languageId);
	}

	public String getKeywords(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getKeywords(languageId, useDefault);
	}

	public String getKeywords(String languageId) {
		String value = LocalizationUtil.getLocalization(getKeywords(),
				languageId);

		if (isEscapedModel()) {
			return HtmlUtil.escape(value);
		}
		else {
			return value;
		}
	}

	public String getKeywords(String languageId, boolean useDefault) {
		String value = LocalizationUtil.getLocalization(getKeywords(),
				languageId, useDefault);

		if (isEscapedModel()) {
			return HtmlUtil.escape(value);
		}
		else {
			return value;
		}
	}

	public Map<Locale, String> getKeywordsMap() {
		return LocalizationUtil.getLocalizationMap(getKeywords());
	}

	public void setKeywords(String keywords) {
		_keywords = keywords;
	}

	public void setKeywords(String keywords, Locale locale) {
		setKeywords(keywords, locale, LocaleUtil.getDefault());
	}

	public void setKeywords(String keywords, Locale locale, Locale defaultLocale) {
		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(keywords)) {
			setKeywords(LocalizationUtil.updateLocalization(getKeywords(),
					"Keywords", keywords, languageId, defaultLanguageId));
		}
		else {
			setKeywords(LocalizationUtil.removeLocalization(getKeywords(),
					"Keywords", languageId));
		}
	}

	public void setKeywordsMap(Map<Locale, String> keywordsMap) {
		setKeywordsMap(keywordsMap, LocaleUtil.getDefault());
	}

	public void setKeywordsMap(Map<Locale, String> keywordsMap,
		Locale defaultLocale) {
		if (keywordsMap == null) {
			return;
		}

		Locale[] locales = LanguageUtil.getAvailableLocales();

		for (Locale locale : locales) {
			String keywords = keywordsMap.get(locale);

			setKeywords(keywords, locale, defaultLocale);
		}
	}

	@JSON
	public String getRobots() {
		if (_robots == null) {
			return StringPool.BLANK;
		}
		else {
			return _robots;
		}
	}

	public String getRobots(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getRobots(languageId);
	}

	public String getRobots(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getRobots(languageId, useDefault);
	}

	public String getRobots(String languageId) {
		String value = LocalizationUtil.getLocalization(getRobots(), languageId);

		if (isEscapedModel()) {
			return HtmlUtil.escape(value);
		}
		else {
			return value;
		}
	}

	public String getRobots(String languageId, boolean useDefault) {
		String value = LocalizationUtil.getLocalization(getRobots(),
				languageId, useDefault);

		if (isEscapedModel()) {
			return HtmlUtil.escape(value);
		}
		else {
			return value;
		}
	}

	public Map<Locale, String> getRobotsMap() {
		return LocalizationUtil.getLocalizationMap(getRobots());
	}

	public void setRobots(String robots) {
		_robots = robots;
	}

	public void setRobots(String robots, Locale locale) {
		setRobots(robots, locale, LocaleUtil.getDefault());
	}

	public void setRobots(String robots, Locale locale, Locale defaultLocale) {
		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(robots)) {
			setRobots(LocalizationUtil.updateLocalization(getRobots(),
					"Robots", robots, languageId, defaultLanguageId));
		}
		else {
			setRobots(LocalizationUtil.removeLocalization(getRobots(),
					"Robots", languageId));
		}
	}

	public void setRobotsMap(Map<Locale, String> robotsMap) {
		setRobotsMap(robotsMap, LocaleUtil.getDefault());
	}

	public void setRobotsMap(Map<Locale, String> robotsMap, Locale defaultLocale) {
		if (robotsMap == null) {
			return;
		}

		Locale[] locales = LanguageUtil.getAvailableLocales();

		for (Locale locale : locales) {
			String robots = robotsMap.get(locale);

			setRobots(robots, locale, defaultLocale);
		}
	}

	@JSON
	public String getType() {
		if (_type == null) {
			return StringPool.BLANK;
		}
		else {
			return _type;
		}
	}

	public void setType(String type) {
		_type = type;
	}

	@JSON
	public String getTypeSettings() {
		if (_typeSettings == null) {
			return StringPool.BLANK;
		}
		else {
			return _typeSettings;
		}
	}

	public void setTypeSettings(String typeSettings) {
		_typeSettings = typeSettings;
	}

	@JSON
	public boolean getHidden() {
		return _hidden;
	}

	public boolean isHidden() {
		return _hidden;
	}

	public void setHidden(boolean hidden) {
		_hidden = hidden;
	}

	@JSON
	public String getFriendlyURL() {
		if (_friendlyURL == null) {
			return StringPool.BLANK;
		}
		else {
			return _friendlyURL;
		}
	}

	public void setFriendlyURL(String friendlyURL) {
		if (_originalFriendlyURL == null) {
			_originalFriendlyURL = _friendlyURL;
		}

		_friendlyURL = friendlyURL;
	}

	public String getOriginalFriendlyURL() {
		return GetterUtil.getString(_originalFriendlyURL);
	}

	@JSON
	public boolean getIconImage() {
		return _iconImage;
	}

	public boolean isIconImage() {
		return _iconImage;
	}

	public void setIconImage(boolean iconImage) {
		_iconImage = iconImage;
	}

	@JSON
	public long getIconImageId() {
		return _iconImageId;
	}

	public void setIconImageId(long iconImageId) {
		if (!_setOriginalIconImageId) {
			_setOriginalIconImageId = true;

			_originalIconImageId = _iconImageId;
		}

		_iconImageId = iconImageId;
	}

	public long getOriginalIconImageId() {
		return _originalIconImageId;
	}

	@JSON
	public String getThemeId() {
		if (_themeId == null) {
			return StringPool.BLANK;
		}
		else {
			return _themeId;
		}
	}

	public void setThemeId(String themeId) {
		_themeId = themeId;
	}

	@JSON
	public String getColorSchemeId() {
		if (_colorSchemeId == null) {
			return StringPool.BLANK;
		}
		else {
			return _colorSchemeId;
		}
	}

	public void setColorSchemeId(String colorSchemeId) {
		_colorSchemeId = colorSchemeId;
	}

	@JSON
	public String getWapThemeId() {
		if (_wapThemeId == null) {
			return StringPool.BLANK;
		}
		else {
			return _wapThemeId;
		}
	}

	public void setWapThemeId(String wapThemeId) {
		_wapThemeId = wapThemeId;
	}

	@JSON
	public String getWapColorSchemeId() {
		if (_wapColorSchemeId == null) {
			return StringPool.BLANK;
		}
		else {
			return _wapColorSchemeId;
		}
	}

	public void setWapColorSchemeId(String wapColorSchemeId) {
		_wapColorSchemeId = wapColorSchemeId;
	}

	@JSON
	public String getCss() {
		if (_css == null) {
			return StringPool.BLANK;
		}
		else {
			return _css;
		}
	}

	public void setCss(String css) {
		_css = css;
	}

	@JSON
	public int getPriority() {
		return _priority;
	}

	public void setPriority(int priority) {
		_priority = priority;
	}

	@JSON
	public String getLayoutPrototypeUuid() {
		if (_layoutPrototypeUuid == null) {
			return StringPool.BLANK;
		}
		else {
			return _layoutPrototypeUuid;
		}
	}

	public void setLayoutPrototypeUuid(String layoutPrototypeUuid) {
		_layoutPrototypeUuid = layoutPrototypeUuid;
	}

	@JSON
	public boolean getLayoutPrototypeLinkEnabled() {
		return _layoutPrototypeLinkEnabled;
	}

	public boolean isLayoutPrototypeLinkEnabled() {
		return _layoutPrototypeLinkEnabled;
	}

	public void setLayoutPrototypeLinkEnabled(
		boolean layoutPrototypeLinkEnabled) {
		_layoutPrototypeLinkEnabled = layoutPrototypeLinkEnabled;
	}

	@Override
	public Layout toEscapedModel() {
		if (isEscapedModel()) {
			return (Layout)this;
		}
		else {
			if (_escapedModelProxy == null) {
				_escapedModelProxy = (Layout)Proxy.newProxyInstance(_classLoader,
						_escapedModelProxyInterfaces,
						new AutoEscapeBeanHandler(this));
			}

			return _escapedModelProxy;
		}
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		if (_expandoBridge == null) {
			_expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
					Layout.class.getName(), getPrimaryKey());
		}

		return _expandoBridge;
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		getExpandoBridge().setAttributes(serviceContext);
	}

	@Override
	public Object clone() {
		LayoutImpl layoutImpl = new LayoutImpl();

		layoutImpl.setUuid(getUuid());
		layoutImpl.setPlid(getPlid());
		layoutImpl.setGroupId(getGroupId());
		layoutImpl.setCompanyId(getCompanyId());
		layoutImpl.setCreateDate(getCreateDate());
		layoutImpl.setModifiedDate(getModifiedDate());
		layoutImpl.setPrivateLayout(getPrivateLayout());
		layoutImpl.setLayoutId(getLayoutId());
		layoutImpl.setParentLayoutId(getParentLayoutId());
		layoutImpl.setName(getName());
		layoutImpl.setTitle(getTitle());
		layoutImpl.setDescription(getDescription());
		layoutImpl.setKeywords(getKeywords());
		layoutImpl.setRobots(getRobots());
		layoutImpl.setType(getType());
		layoutImpl.setTypeSettings(getTypeSettings());
		layoutImpl.setHidden(getHidden());
		layoutImpl.setFriendlyURL(getFriendlyURL());
		layoutImpl.setIconImage(getIconImage());
		layoutImpl.setIconImageId(getIconImageId());
		layoutImpl.setThemeId(getThemeId());
		layoutImpl.setColorSchemeId(getColorSchemeId());
		layoutImpl.setWapThemeId(getWapThemeId());
		layoutImpl.setWapColorSchemeId(getWapColorSchemeId());
		layoutImpl.setCss(getCss());
		layoutImpl.setPriority(getPriority());
		layoutImpl.setLayoutPrototypeUuid(getLayoutPrototypeUuid());
		layoutImpl.setLayoutPrototypeLinkEnabled(getLayoutPrototypeLinkEnabled());

		layoutImpl.resetOriginalValues();

		return layoutImpl;
	}

	public int compareTo(Layout layout) {
		int value = 0;

		if (getParentLayoutId() < layout.getParentLayoutId()) {
			value = -1;
		}
		else if (getParentLayoutId() > layout.getParentLayoutId()) {
			value = 1;
		}
		else {
			value = 0;
		}

		if (value != 0) {
			return value;
		}

		if (getPriority() < layout.getPriority()) {
			value = -1;
		}
		else if (getPriority() > layout.getPriority()) {
			value = 1;
		}
		else {
			value = 0;
		}

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		Layout layout = null;

		try {
			layout = (Layout)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = layout.getPrimaryKey();

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
	public void resetOriginalValues() {
		LayoutModelImpl layoutModelImpl = this;

		layoutModelImpl._originalUuid = layoutModelImpl._uuid;

		layoutModelImpl._originalGroupId = layoutModelImpl._groupId;

		layoutModelImpl._setOriginalGroupId = false;

		layoutModelImpl._originalPrivateLayout = layoutModelImpl._privateLayout;

		layoutModelImpl._setOriginalPrivateLayout = false;

		layoutModelImpl._originalLayoutId = layoutModelImpl._layoutId;

		layoutModelImpl._setOriginalLayoutId = false;

		layoutModelImpl._originalFriendlyURL = layoutModelImpl._friendlyURL;

		layoutModelImpl._originalIconImageId = layoutModelImpl._iconImageId;

		layoutModelImpl._setOriginalIconImageId = false;
	}

	@Override
	public CacheModel<Layout> toCacheModel() {
		LayoutCacheModel layoutCacheModel = new LayoutCacheModel();

		layoutCacheModel.uuid = getUuid();

		String uuid = layoutCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			layoutCacheModel.uuid = null;
		}

		layoutCacheModel.plid = getPlid();

		layoutCacheModel.groupId = getGroupId();

		layoutCacheModel.companyId = getCompanyId();

		Date createDate = getCreateDate();

		if (createDate != null) {
			layoutCacheModel.createDate = createDate.getTime();
		}
		else {
			layoutCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			layoutCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			layoutCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		layoutCacheModel.privateLayout = getPrivateLayout();

		layoutCacheModel.layoutId = getLayoutId();

		layoutCacheModel.parentLayoutId = getParentLayoutId();

		layoutCacheModel.name = getName();

		String name = layoutCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			layoutCacheModel.name = null;
		}

		layoutCacheModel.title = getTitle();

		String title = layoutCacheModel.title;

		if ((title != null) && (title.length() == 0)) {
			layoutCacheModel.title = null;
		}

		layoutCacheModel.description = getDescription();

		String description = layoutCacheModel.description;

		if ((description != null) && (description.length() == 0)) {
			layoutCacheModel.description = null;
		}

		layoutCacheModel.keywords = getKeywords();

		String keywords = layoutCacheModel.keywords;

		if ((keywords != null) && (keywords.length() == 0)) {
			layoutCacheModel.keywords = null;
		}

		layoutCacheModel.robots = getRobots();

		String robots = layoutCacheModel.robots;

		if ((robots != null) && (robots.length() == 0)) {
			layoutCacheModel.robots = null;
		}

		layoutCacheModel.type = getType();

		String type = layoutCacheModel.type;

		if ((type != null) && (type.length() == 0)) {
			layoutCacheModel.type = null;
		}

		layoutCacheModel.typeSettings = getTypeSettings();

		String typeSettings = layoutCacheModel.typeSettings;

		if ((typeSettings != null) && (typeSettings.length() == 0)) {
			layoutCacheModel.typeSettings = null;
		}

		layoutCacheModel.hidden = getHidden();

		layoutCacheModel.friendlyURL = getFriendlyURL();

		String friendlyURL = layoutCacheModel.friendlyURL;

		if ((friendlyURL != null) && (friendlyURL.length() == 0)) {
			layoutCacheModel.friendlyURL = null;
		}

		layoutCacheModel.iconImage = getIconImage();

		layoutCacheModel.iconImageId = getIconImageId();

		layoutCacheModel.themeId = getThemeId();

		String themeId = layoutCacheModel.themeId;

		if ((themeId != null) && (themeId.length() == 0)) {
			layoutCacheModel.themeId = null;
		}

		layoutCacheModel.colorSchemeId = getColorSchemeId();

		String colorSchemeId = layoutCacheModel.colorSchemeId;

		if ((colorSchemeId != null) && (colorSchemeId.length() == 0)) {
			layoutCacheModel.colorSchemeId = null;
		}

		layoutCacheModel.wapThemeId = getWapThemeId();

		String wapThemeId = layoutCacheModel.wapThemeId;

		if ((wapThemeId != null) && (wapThemeId.length() == 0)) {
			layoutCacheModel.wapThemeId = null;
		}

		layoutCacheModel.wapColorSchemeId = getWapColorSchemeId();

		String wapColorSchemeId = layoutCacheModel.wapColorSchemeId;

		if ((wapColorSchemeId != null) && (wapColorSchemeId.length() == 0)) {
			layoutCacheModel.wapColorSchemeId = null;
		}

		layoutCacheModel.css = getCss();

		String css = layoutCacheModel.css;

		if ((css != null) && (css.length() == 0)) {
			layoutCacheModel.css = null;
		}

		layoutCacheModel.priority = getPriority();

		layoutCacheModel.layoutPrototypeUuid = getLayoutPrototypeUuid();

		String layoutPrototypeUuid = layoutCacheModel.layoutPrototypeUuid;

		if ((layoutPrototypeUuid != null) &&
				(layoutPrototypeUuid.length() == 0)) {
			layoutCacheModel.layoutPrototypeUuid = null;
		}

		layoutCacheModel.layoutPrototypeLinkEnabled = getLayoutPrototypeLinkEnabled();

		return layoutCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(57);

		sb.append("{uuid=");
		sb.append(getUuid());
		sb.append(", plid=");
		sb.append(getPlid());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append(", privateLayout=");
		sb.append(getPrivateLayout());
		sb.append(", layoutId=");
		sb.append(getLayoutId());
		sb.append(", parentLayoutId=");
		sb.append(getParentLayoutId());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", title=");
		sb.append(getTitle());
		sb.append(", description=");
		sb.append(getDescription());
		sb.append(", keywords=");
		sb.append(getKeywords());
		sb.append(", robots=");
		sb.append(getRobots());
		sb.append(", type=");
		sb.append(getType());
		sb.append(", typeSettings=");
		sb.append(getTypeSettings());
		sb.append(", hidden=");
		sb.append(getHidden());
		sb.append(", friendlyURL=");
		sb.append(getFriendlyURL());
		sb.append(", iconImage=");
		sb.append(getIconImage());
		sb.append(", iconImageId=");
		sb.append(getIconImageId());
		sb.append(", themeId=");
		sb.append(getThemeId());
		sb.append(", colorSchemeId=");
		sb.append(getColorSchemeId());
		sb.append(", wapThemeId=");
		sb.append(getWapThemeId());
		sb.append(", wapColorSchemeId=");
		sb.append(getWapColorSchemeId());
		sb.append(", css=");
		sb.append(getCss());
		sb.append(", priority=");
		sb.append(getPriority());
		sb.append(", layoutPrototypeUuid=");
		sb.append(getLayoutPrototypeUuid());
		sb.append(", layoutPrototypeLinkEnabled=");
		sb.append(getLayoutPrototypeLinkEnabled());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(88);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portal.model.Layout");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>uuid</column-name><column-value><![CDATA[");
		sb.append(getUuid());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>plid</column-name><column-value><![CDATA[");
		sb.append(getPlid());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>groupId</column-name><column-value><![CDATA[");
		sb.append(getGroupId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
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
			"<column><column-name>privateLayout</column-name><column-value><![CDATA[");
		sb.append(getPrivateLayout());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>layoutId</column-name><column-value><![CDATA[");
		sb.append(getLayoutId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>parentLayoutId</column-name><column-value><![CDATA[");
		sb.append(getParentLayoutId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>name</column-name><column-value><![CDATA[");
		sb.append(getName());
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
			"<column><column-name>keywords</column-name><column-value><![CDATA[");
		sb.append(getKeywords());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>robots</column-name><column-value><![CDATA[");
		sb.append(getRobots());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>type</column-name><column-value><![CDATA[");
		sb.append(getType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>typeSettings</column-name><column-value><![CDATA[");
		sb.append(getTypeSettings());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>hidden</column-name><column-value><![CDATA[");
		sb.append(getHidden());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>friendlyURL</column-name><column-value><![CDATA[");
		sb.append(getFriendlyURL());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>iconImage</column-name><column-value><![CDATA[");
		sb.append(getIconImage());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>iconImageId</column-name><column-value><![CDATA[");
		sb.append(getIconImageId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>themeId</column-name><column-value><![CDATA[");
		sb.append(getThemeId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>colorSchemeId</column-name><column-value><![CDATA[");
		sb.append(getColorSchemeId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>wapThemeId</column-name><column-value><![CDATA[");
		sb.append(getWapThemeId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>wapColorSchemeId</column-name><column-value><![CDATA[");
		sb.append(getWapColorSchemeId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>css</column-name><column-value><![CDATA[");
		sb.append(getCss());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>priority</column-name><column-value><![CDATA[");
		sb.append(getPriority());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>layoutPrototypeUuid</column-name><column-value><![CDATA[");
		sb.append(getLayoutPrototypeUuid());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>layoutPrototypeLinkEnabled</column-name><column-value><![CDATA[");
		sb.append(getLayoutPrototypeLinkEnabled());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = Layout.class.getClassLoader();
	private static Class<?>[] _escapedModelProxyInterfaces = new Class[] {
			Layout.class
		};
	private String _uuid;
	private String _originalUuid;
	private long _plid;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _privateLayout;
	private boolean _originalPrivateLayout;
	private boolean _setOriginalPrivateLayout;
	private long _layoutId;
	private long _originalLayoutId;
	private boolean _setOriginalLayoutId;
	private long _parentLayoutId;
	private String _name;
	private String _title;
	private String _description;
	private String _keywords;
	private String _robots;
	private String _type;
	private String _typeSettings;
	private boolean _hidden;
	private String _friendlyURL;
	private String _originalFriendlyURL;
	private boolean _iconImage;
	private long _iconImageId;
	private long _originalIconImageId;
	private boolean _setOriginalIconImageId;
	private String _themeId;
	private String _colorSchemeId;
	private String _wapThemeId;
	private String _wapColorSchemeId;
	private String _css;
	private int _priority;
	private String _layoutPrototypeUuid;
	private boolean _layoutPrototypeLinkEnabled;
	private transient ExpandoBridge _expandoBridge;
	private Layout _escapedModelProxy;
}