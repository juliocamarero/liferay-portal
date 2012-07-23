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

package com.liferay.portlet.wiki.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Locale;

/**
 * The base model interface for the WikiPageResource service. Represents a row in the &quot;WikiPageResource&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.wiki.model.impl.WikiPageResourceModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.wiki.model.impl.WikiPageResourceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see WikiPageResource
 * @see com.liferay.portlet.wiki.model.impl.WikiPageResourceImpl
 * @see com.liferay.portlet.wiki.model.impl.WikiPageResourceModelImpl
 * @generated
 */
public interface WikiPageResourceModel extends BaseModel<WikiPageResource> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a wiki page resource model instance should use the {@link WikiPageResource} interface instead.
	 */

	/**
	 * Returns the primary key of this wiki page resource.
	 *
	 * @return the primary key of this wiki page resource
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this wiki page resource.
	 *
	 * @param primaryKey the primary key of this wiki page resource
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this wiki page resource.
	 *
	 * @return the uuid of this wiki page resource
	 */
	@AutoEscape
	public String getUuid();

	/**
	 * Sets the uuid of this wiki page resource.
	 *
	 * @param uuid the uuid of this wiki page resource
	 */
	public void setUuid(String uuid);

	/**
	 * Returns the resource prim key of this wiki page resource.
	 *
	 * @return the resource prim key of this wiki page resource
	 */
	public long getResourcePrimKey();

	/**
	 * Sets the resource prim key of this wiki page resource.
	 *
	 * @param resourcePrimKey the resource prim key of this wiki page resource
	 */
	public void setResourcePrimKey(long resourcePrimKey);

	/**
	 * Returns the node ID of this wiki page resource.
	 *
	 * @return the node ID of this wiki page resource
	 */
	public long getNodeId();

	/**
	 * Sets the node ID of this wiki page resource.
	 *
	 * @param nodeId the node ID of this wiki page resource
	 */
	public void setNodeId(long nodeId);

	/**
	 * Returns the title of this wiki page resource.
	 *
	 * @return the title of this wiki page resource
	 */
	@AutoEscape
	public String getTitle();

	/**
	 * Sets the title of this wiki page resource.
	 *
	 * @param title the title of this wiki page resource
	 */
	public void setTitle(String title);

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

	public int compareTo(WikiPageResource wikiPageResource);

	public int hashCode();

	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale);

	public CacheModel<WikiPageResource> toCacheModel();

	public WikiPageResource toEscapedModel();

	public String toString();

	public String toXmlString();
}