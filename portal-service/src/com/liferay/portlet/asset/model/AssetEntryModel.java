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

package com.liferay.portlet.asset.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.AttachedModel;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.GroupedModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * The base model interface for the AssetEntry service. Represents a row in the &quot;AssetEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.asset.model.impl.AssetEntryModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.asset.model.impl.AssetEntryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetEntry
 * @see com.liferay.portlet.asset.model.impl.AssetEntryImpl
 * @see com.liferay.portlet.asset.model.impl.AssetEntryModelImpl
 * @generated
 */
public interface AssetEntryModel extends AttachedModel, BaseModel<AssetEntry>,
	GroupedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a asset entry model instance should use the {@link AssetEntry} interface instead.
	 */

	/**
	 * Returns the primary key of this asset entry.
	 *
	 * @return the primary key of this asset entry
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this asset entry.
	 *
	 * @param primaryKey the primary key of this asset entry
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the entry ID of this asset entry.
	 *
	 * @return the entry ID of this asset entry
	 */
	public long getEntryId();

	/**
	 * Sets the entry ID of this asset entry.
	 *
	 * @param entryId the entry ID of this asset entry
	 */
	public void setEntryId(long entryId);

	/**
	 * Returns the group ID of this asset entry.
	 *
	 * @return the group ID of this asset entry
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this asset entry.
	 *
	 * @param groupId the group ID of this asset entry
	 */
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this asset entry.
	 *
	 * @return the company ID of this asset entry
	 */
	public long getCompanyId();

	/**
	 * Sets the company ID of this asset entry.
	 *
	 * @param companyId the company ID of this asset entry
	 */
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this asset entry.
	 *
	 * @return the user ID of this asset entry
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this asset entry.
	 *
	 * @param userId the user ID of this asset entry
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this asset entry.
	 *
	 * @return the user uuid of this asset entry
	 * @throws SystemException if a system exception occurred
	 */
	public String getUserUuid() throws SystemException;

	/**
	 * Sets the user uuid of this asset entry.
	 *
	 * @param userUuid the user uuid of this asset entry
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this asset entry.
	 *
	 * @return the user name of this asset entry
	 */
	@AutoEscape
	public String getUserName();

	/**
	 * Sets the user name of this asset entry.
	 *
	 * @param userName the user name of this asset entry
	 */
	public void setUserName(String userName);

	/**
	 * Returns the create date of this asset entry.
	 *
	 * @return the create date of this asset entry
	 */
	public Date getCreateDate();

	/**
	 * Sets the create date of this asset entry.
	 *
	 * @param createDate the create date of this asset entry
	 */
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this asset entry.
	 *
	 * @return the modified date of this asset entry
	 */
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this asset entry.
	 *
	 * @param modifiedDate the modified date of this asset entry
	 */
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the fully qualified class name of this asset entry.
	 *
	 * @return the fully qualified class name of this asset entry
	 */
	public String getClassName();

	/**
	 * Returns the class name ID of this asset entry.
	 *
	 * @return the class name ID of this asset entry
	 */
	public long getClassNameId();

	/**
	 * Sets the class name ID of this asset entry.
	 *
	 * @param classNameId the class name ID of this asset entry
	 */
	public void setClassNameId(long classNameId);

	/**
	 * Returns the class p k of this asset entry.
	 *
	 * @return the class p k of this asset entry
	 */
	public long getClassPK();

	/**
	 * Sets the class p k of this asset entry.
	 *
	 * @param classPK the class p k of this asset entry
	 */
	public void setClassPK(long classPK);

	/**
	 * Returns the class uuid of this asset entry.
	 *
	 * @return the class uuid of this asset entry
	 */
	@AutoEscape
	public String getClassUuid();

	/**
	 * Sets the class uuid of this asset entry.
	 *
	 * @param classUuid the class uuid of this asset entry
	 */
	public void setClassUuid(String classUuid);

	/**
	 * Returns the class type ID of this asset entry.
	 *
	 * @return the class type ID of this asset entry
	 */
	public long getClassTypeId();

	/**
	 * Sets the class type ID of this asset entry.
	 *
	 * @param classTypeId the class type ID of this asset entry
	 */
	public void setClassTypeId(long classTypeId);

	/**
	 * Returns the visible of this asset entry.
	 *
	 * @return the visible of this asset entry
	 */
	public boolean getVisible();

	/**
	 * Returns <code>true</code> if this asset entry is visible.
	 *
	 * @return <code>true</code> if this asset entry is visible; <code>false</code> otherwise
	 */
	public boolean isVisible();

	/**
	 * Sets whether this asset entry is visible.
	 *
	 * @param visible the visible of this asset entry
	 */
	public void setVisible(boolean visible);

	/**
	 * Returns the start date of this asset entry.
	 *
	 * @return the start date of this asset entry
	 */
	public Date getStartDate();

	/**
	 * Sets the start date of this asset entry.
	 *
	 * @param startDate the start date of this asset entry
	 */
	public void setStartDate(Date startDate);

	/**
	 * Returns the end date of this asset entry.
	 *
	 * @return the end date of this asset entry
	 */
	public Date getEndDate();

	/**
	 * Sets the end date of this asset entry.
	 *
	 * @param endDate the end date of this asset entry
	 */
	public void setEndDate(Date endDate);

	/**
	 * Returns the publish date of this asset entry.
	 *
	 * @return the publish date of this asset entry
	 */
	public Date getPublishDate();

	/**
	 * Sets the publish date of this asset entry.
	 *
	 * @param publishDate the publish date of this asset entry
	 */
	public void setPublishDate(Date publishDate);

	/**
	 * Returns the expiration date of this asset entry.
	 *
	 * @return the expiration date of this asset entry
	 */
	public Date getExpirationDate();

	/**
	 * Sets the expiration date of this asset entry.
	 *
	 * @param expirationDate the expiration date of this asset entry
	 */
	public void setExpirationDate(Date expirationDate);

	/**
	 * Returns the mime type of this asset entry.
	 *
	 * @return the mime type of this asset entry
	 */
	@AutoEscape
	public String getMimeType();

	/**
	 * Sets the mime type of this asset entry.
	 *
	 * @param mimeType the mime type of this asset entry
	 */
	public void setMimeType(String mimeType);

	/**
	 * Returns the title of this asset entry.
	 *
	 * @return the title of this asset entry
	 */
	public String getTitle();

	/**
	 * Returns the localized title of this asset entry in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param locale the locale of the language
	 * @return the localized title of this asset entry
	 */
	@AutoEscape
	public String getTitle(Locale locale);

	/**
	 * Returns the localized title of this asset entry in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param locale the local of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized title of this asset entry. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	 */
	@AutoEscape
	public String getTitle(Locale locale, boolean useDefault);

	/**
	 * Returns the localized title of this asset entry in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @return the localized title of this asset entry
	 */
	@AutoEscape
	public String getTitle(String languageId);

	/**
	 * Returns the localized title of this asset entry in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized title of this asset entry
	 */
	@AutoEscape
	public String getTitle(String languageId, boolean useDefault);

	/**
	 * Returns a map of the locales and localized titles of this asset entry.
	 *
	 * @return the locales and localized titles of this asset entry
	 */
	public Map<Locale, String> getTitleMap();

	/**
	 * Sets the title of this asset entry.
	 *
	 * @param title the title of this asset entry
	 */
	public void setTitle(String title);

	/**
	 * Sets the localized title of this asset entry in the language.
	 *
	 * @param title the localized title of this asset entry
	 * @param locale the locale of the language
	 */
	public void setTitle(String title, Locale locale);

	/**
	 * Sets the localized title of this asset entry in the language, and sets the default locale.
	 *
	 * @param title the localized title of this asset entry
	 * @param locale the locale of the language
	 * @param defaultLocale the default locale
	 */
	public void setTitle(String title, Locale locale, Locale defaultLocale);

	/**
	 * Sets the localized titles of this asset entry from the map of locales and localized titles.
	 *
	 * @param titleMap the locales and localized titles of this asset entry
	 */
	public void setTitleMap(Map<Locale, String> titleMap);

	/**
	 * Sets the localized titles of this asset entry from the map of locales and localized titles, and sets the default locale.
	 *
	 * @param titleMap the locales and localized titles of this asset entry
	 * @param defaultLocale the default locale
	 */
	public void setTitleMap(Map<Locale, String> titleMap, Locale defaultLocale);

	/**
	 * Returns the description of this asset entry.
	 *
	 * @return the description of this asset entry
	 */
	@AutoEscape
	public String getDescription();

	/**
	 * Sets the description of this asset entry.
	 *
	 * @param description the description of this asset entry
	 */
	public void setDescription(String description);

	/**
	 * Returns the summary of this asset entry.
	 *
	 * @return the summary of this asset entry
	 */
	@AutoEscape
	public String getSummary();

	/**
	 * Sets the summary of this asset entry.
	 *
	 * @param summary the summary of this asset entry
	 */
	public void setSummary(String summary);

	/**
	 * Returns the url of this asset entry.
	 *
	 * @return the url of this asset entry
	 */
	@AutoEscape
	public String getUrl();

	/**
	 * Sets the url of this asset entry.
	 *
	 * @param url the url of this asset entry
	 */
	public void setUrl(String url);

	/**
	 * Returns the layout uuid of this asset entry.
	 *
	 * @return the layout uuid of this asset entry
	 */
	@AutoEscape
	public String getLayoutUuid();

	/**
	 * Sets the layout uuid of this asset entry.
	 *
	 * @param layoutUuid the layout uuid of this asset entry
	 */
	public void setLayoutUuid(String layoutUuid);

	/**
	 * Returns the height of this asset entry.
	 *
	 * @return the height of this asset entry
	 */
	public int getHeight();

	/**
	 * Sets the height of this asset entry.
	 *
	 * @param height the height of this asset entry
	 */
	public void setHeight(int height);

	/**
	 * Returns the width of this asset entry.
	 *
	 * @return the width of this asset entry
	 */
	public int getWidth();

	/**
	 * Sets the width of this asset entry.
	 *
	 * @param width the width of this asset entry
	 */
	public void setWidth(int width);

	/**
	 * Returns the priority of this asset entry.
	 *
	 * @return the priority of this asset entry
	 */
	public double getPriority();

	/**
	 * Sets the priority of this asset entry.
	 *
	 * @param priority the priority of this asset entry
	 */
	public void setPriority(double priority);

	/**
	 * Returns the view count of this asset entry.
	 *
	 * @return the view count of this asset entry
	 */
	public int getViewCount();

	/**
	 * Sets the view count of this asset entry.
	 *
	 * @param viewCount the view count of this asset entry
	 */
	public void setViewCount(int viewCount);

	public boolean isNew();

	public void setNew(boolean n);

	public boolean isCachedModel();

	public void setCachedModel(boolean cachedModel);

	public boolean isEscapedModel();

	public Serializable getPrimaryKeyObj();

	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	public ExpandoBridge getExpandoBridge();

	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	public Object clone();

	public int compareTo(AssetEntry assetEntry);

	public int hashCode();

	public CacheModel<AssetEntry> toCacheModel();

	public AssetEntry toEscapedModel();

	public String toString();

	public String toXmlString();
}