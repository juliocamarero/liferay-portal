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

package com.liferay.portal.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

/**
 * The base model interface for the LayoutSet service. Represents a row in the &quot;LayoutSet&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portal.model.impl.LayoutSetModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portal.model.impl.LayoutSetImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see LayoutSet
 * @see com.liferay.portal.model.impl.LayoutSetImpl
 * @see com.liferay.portal.model.impl.LayoutSetModelImpl
 * @generated
 */
public interface LayoutSetModel extends BaseModel<LayoutSet> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a layout set model instance should use the {@link LayoutSet} interface instead.
	 */

	/**
	 * Returns the primary key of this layout set.
	 *
	 * @return the primary key of this layout set
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this layout set.
	 *
	 * @param primaryKey the primary key of this layout set
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the layout set ID of this layout set.
	 *
	 * @return the layout set ID of this layout set
	 */
	public long getLayoutSetId();

	/**
	 * Sets the layout set ID of this layout set.
	 *
	 * @param layoutSetId the layout set ID of this layout set
	 */
	public void setLayoutSetId(long layoutSetId);

	/**
	 * Returns the group ID of this layout set.
	 *
	 * @return the group ID of this layout set
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this layout set.
	 *
	 * @param groupId the group ID of this layout set
	 */
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this layout set.
	 *
	 * @return the company ID of this layout set
	 */
	public long getCompanyId();

	/**
	 * Sets the company ID of this layout set.
	 *
	 * @param companyId the company ID of this layout set
	 */
	public void setCompanyId(long companyId);

	/**
	 * Returns the private layout of this layout set.
	 *
	 * @return the private layout of this layout set
	 */
	public boolean getPrivateLayout();

	/**
	 * Returns <code>true</code> if this layout set is private layout.
	 *
	 * @return <code>true</code> if this layout set is private layout; <code>false</code> otherwise
	 */
	public boolean isPrivateLayout();

	/**
	 * Sets whether this layout set is private layout.
	 *
	 * @param privateLayout the private layout of this layout set
	 */
	public void setPrivateLayout(boolean privateLayout);

	/**
	 * Returns the logo of this layout set.
	 *
	 * @return the logo of this layout set
	 */
	public boolean getLogo();

	/**
	 * Returns <code>true</code> if this layout set is logo.
	 *
	 * @return <code>true</code> if this layout set is logo; <code>false</code> otherwise
	 */
	public boolean isLogo();

	/**
	 * Sets whether this layout set is logo.
	 *
	 * @param logo the logo of this layout set
	 */
	public void setLogo(boolean logo);

	/**
	 * Returns the logo ID of this layout set.
	 *
	 * @return the logo ID of this layout set
	 */
	public long getLogoId();

	/**
	 * Sets the logo ID of this layout set.
	 *
	 * @param logoId the logo ID of this layout set
	 */
	public void setLogoId(long logoId);

	/**
	 * Returns the theme ID of this layout set.
	 *
	 * @return the theme ID of this layout set
	 */
	@AutoEscape
	public String getThemeId();

	/**
	 * Sets the theme ID of this layout set.
	 *
	 * @param themeId the theme ID of this layout set
	 */
	public void setThemeId(String themeId);

	/**
	 * Returns the color scheme ID of this layout set.
	 *
	 * @return the color scheme ID of this layout set
	 */
	@AutoEscape
	public String getColorSchemeId();

	/**
	 * Sets the color scheme ID of this layout set.
	 *
	 * @param colorSchemeId the color scheme ID of this layout set
	 */
	public void setColorSchemeId(String colorSchemeId);

	/**
	 * Returns the wap theme ID of this layout set.
	 *
	 * @return the wap theme ID of this layout set
	 */
	@AutoEscape
	public String getWapThemeId();

	/**
	 * Sets the wap theme ID of this layout set.
	 *
	 * @param wapThemeId the wap theme ID of this layout set
	 */
	public void setWapThemeId(String wapThemeId);

	/**
	 * Returns the wap color scheme ID of this layout set.
	 *
	 * @return the wap color scheme ID of this layout set
	 */
	@AutoEscape
	public String getWapColorSchemeId();

	/**
	 * Sets the wap color scheme ID of this layout set.
	 *
	 * @param wapColorSchemeId the wap color scheme ID of this layout set
	 */
	public void setWapColorSchemeId(String wapColorSchemeId);

	/**
	 * Returns the css of this layout set.
	 *
	 * @return the css of this layout set
	 */
	@AutoEscape
	public String getCss();

	/**
	 * Sets the css of this layout set.
	 *
	 * @param css the css of this layout set
	 */
	public void setCss(String css);

	/**
	 * Returns the page count of this layout set.
	 *
	 * @return the page count of this layout set
	 */
	public int getPageCount();

	/**
	 * Sets the page count of this layout set.
	 *
	 * @param pageCount the page count of this layout set
	 */
	public void setPageCount(int pageCount);

	/**
	 * Returns the settings of this layout set.
	 *
	 * @return the settings of this layout set
	 */
	@AutoEscape
	public String getSettings();

	/**
	 * Sets the settings of this layout set.
	 *
	 * @param settings the settings of this layout set
	 */
	public void setSettings(String settings);

	/**
	 * Returns the layout set prototype uuid of this layout set.
	 *
	 * @return the layout set prototype uuid of this layout set
	 */
	@AutoEscape
	public String getLayoutSetPrototypeUuid();

	/**
	 * Sets the layout set prototype uuid of this layout set.
	 *
	 * @param layoutSetPrototypeUuid the layout set prototype uuid of this layout set
	 */
	public void setLayoutSetPrototypeUuid(String layoutSetPrototypeUuid);

	/**
	 * Returns the layout set prototype link enabled of this layout set.
	 *
	 * @return the layout set prototype link enabled of this layout set
	 */
	public boolean getLayoutSetPrototypeLinkEnabled();

	/**
	 * Returns <code>true</code> if this layout set is layout set prototype link enabled.
	 *
	 * @return <code>true</code> if this layout set is layout set prototype link enabled; <code>false</code> otherwise
	 */
	public boolean isLayoutSetPrototypeLinkEnabled();

	/**
	 * Sets whether this layout set is layout set prototype link enabled.
	 *
	 * @param layoutSetPrototypeLinkEnabled the layout set prototype link enabled of this layout set
	 */
	public void setLayoutSetPrototypeLinkEnabled(
		boolean layoutSetPrototypeLinkEnabled);

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

	public int compareTo(LayoutSet layoutSet);

	public int hashCode();

	public CacheModel<LayoutSet> toCacheModel();

	public LayoutSet toEscapedModel();

	public String toString();

	public String toXmlString();
}