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

package com.liferay.portlet.expando.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.model.AttachedModel;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import java.io.Serializable;

/**
 * The base model interface for the ExpandoValue service. Represents a row in the &quot;ExpandoValue&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.expando.model.impl.ExpandoValueModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.expando.model.impl.ExpandoValueImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ExpandoValue
 * @see com.liferay.portlet.expando.model.impl.ExpandoValueImpl
 * @see com.liferay.portlet.expando.model.impl.ExpandoValueModelImpl
 * @generated
 */
public interface ExpandoValueModel extends AttachedModel, BaseModel<ExpandoValue> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a expando value model instance should use the {@link ExpandoValue} interface instead.
	 */

	/**
	 * Returns the primary key of this expando value.
	 *
	 * @return the primary key of this expando value
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this expando value.
	 *
	 * @param primaryKey the primary key of this expando value
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the value ID of this expando value.
	 *
	 * @return the value ID of this expando value
	 */
	public long getValueId();

	/**
	 * Sets the value ID of this expando value.
	 *
	 * @param valueId the value ID of this expando value
	 */
	public void setValueId(long valueId);

	/**
	 * Returns the company ID of this expando value.
	 *
	 * @return the company ID of this expando value
	 */
	public long getCompanyId();

	/**
	 * Sets the company ID of this expando value.
	 *
	 * @param companyId the company ID of this expando value
	 */
	public void setCompanyId(long companyId);

	/**
	 * Returns the table ID of this expando value.
	 *
	 * @return the table ID of this expando value
	 */
	public long getTableId();

	/**
	 * Sets the table ID of this expando value.
	 *
	 * @param tableId the table ID of this expando value
	 */
	public void setTableId(long tableId);

	/**
	 * Returns the column ID of this expando value.
	 *
	 * @return the column ID of this expando value
	 */
	public long getColumnId();

	/**
	 * Sets the column ID of this expando value.
	 *
	 * @param columnId the column ID of this expando value
	 */
	public void setColumnId(long columnId);

	/**
	 * Returns the row ID of this expando value.
	 *
	 * @return the row ID of this expando value
	 */
	public long getRowId();

	/**
	 * Sets the row ID of this expando value.
	 *
	 * @param rowId the row ID of this expando value
	 */
	public void setRowId(long rowId);

	/**
	 * Returns the fully qualified class name of this expando value.
	 *
	 * @return the fully qualified class name of this expando value
	 */
	public String getClassName();

	/**
	 * Returns the class name ID of this expando value.
	 *
	 * @return the class name ID of this expando value
	 */
	public long getClassNameId();

	/**
	 * Sets the class name ID of this expando value.
	 *
	 * @param classNameId the class name ID of this expando value
	 */
	public void setClassNameId(long classNameId);

	/**
	 * Returns the class p k of this expando value.
	 *
	 * @return the class p k of this expando value
	 */
	public long getClassPK();

	/**
	 * Sets the class p k of this expando value.
	 *
	 * @param classPK the class p k of this expando value
	 */
	public void setClassPK(long classPK);

	/**
	 * Returns the data of this expando value.
	 *
	 * @return the data of this expando value
	 */
	@AutoEscape
	public String getData();

	/**
	 * Sets the data of this expando value.
	 *
	 * @param data the data of this expando value
	 */
	public void setData(String data);

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

	public int compareTo(ExpandoValue expandoValue);

	public int hashCode();

	public CacheModel<ExpandoValue> toCacheModel();

	public ExpandoValue toEscapedModel();

	public String toString();

	public String toXmlString();
}