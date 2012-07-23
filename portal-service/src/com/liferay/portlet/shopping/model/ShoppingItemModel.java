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

package com.liferay.portlet.shopping.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.GroupedModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;
import java.util.Locale;

/**
 * The base model interface for the ShoppingItem service. Represents a row in the &quot;ShoppingItem&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.shopping.model.impl.ShoppingItemModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.shopping.model.impl.ShoppingItemImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ShoppingItem
 * @see com.liferay.portlet.shopping.model.impl.ShoppingItemImpl
 * @see com.liferay.portlet.shopping.model.impl.ShoppingItemModelImpl
 * @generated
 */
public interface ShoppingItemModel extends BaseModel<ShoppingItem>, GroupedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a shopping item model instance should use the {@link ShoppingItem} interface instead.
	 */

	/**
	 * Returns the primary key of this shopping item.
	 *
	 * @return the primary key of this shopping item
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this shopping item.
	 *
	 * @param primaryKey the primary key of this shopping item
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the item ID of this shopping item.
	 *
	 * @return the item ID of this shopping item
	 */
	public long getItemId();

	/**
	 * Sets the item ID of this shopping item.
	 *
	 * @param itemId the item ID of this shopping item
	 */
	public void setItemId(long itemId);

	/**
	 * Returns the group ID of this shopping item.
	 *
	 * @return the group ID of this shopping item
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this shopping item.
	 *
	 * @param groupId the group ID of this shopping item
	 */
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this shopping item.
	 *
	 * @return the company ID of this shopping item
	 */
	public long getCompanyId();

	/**
	 * Sets the company ID of this shopping item.
	 *
	 * @param companyId the company ID of this shopping item
	 */
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this shopping item.
	 *
	 * @return the user ID of this shopping item
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this shopping item.
	 *
	 * @param userId the user ID of this shopping item
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this shopping item.
	 *
	 * @return the user uuid of this shopping item
	 * @throws SystemException if a system exception occurred
	 */
	public String getUserUuid() throws SystemException;

	/**
	 * Sets the user uuid of this shopping item.
	 *
	 * @param userUuid the user uuid of this shopping item
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this shopping item.
	 *
	 * @return the user name of this shopping item
	 */
	@AutoEscape
	public String getUserName();

	/**
	 * Sets the user name of this shopping item.
	 *
	 * @param userName the user name of this shopping item
	 */
	public void setUserName(String userName);

	/**
	 * Returns the create date of this shopping item.
	 *
	 * @return the create date of this shopping item
	 */
	public Date getCreateDate();

	/**
	 * Sets the create date of this shopping item.
	 *
	 * @param createDate the create date of this shopping item
	 */
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this shopping item.
	 *
	 * @return the modified date of this shopping item
	 */
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this shopping item.
	 *
	 * @param modifiedDate the modified date of this shopping item
	 */
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the category ID of this shopping item.
	 *
	 * @return the category ID of this shopping item
	 */
	public long getCategoryId();

	/**
	 * Sets the category ID of this shopping item.
	 *
	 * @param categoryId the category ID of this shopping item
	 */
	public void setCategoryId(long categoryId);

	/**
	 * Returns the sku of this shopping item.
	 *
	 * @return the sku of this shopping item
	 */
	@AutoEscape
	public String getSku();

	/**
	 * Sets the sku of this shopping item.
	 *
	 * @param sku the sku of this shopping item
	 */
	public void setSku(String sku);

	/**
	 * Returns the name of this shopping item.
	 *
	 * @return the name of this shopping item
	 */
	@AutoEscape
	public String getName();

	/**
	 * Sets the name of this shopping item.
	 *
	 * @param name the name of this shopping item
	 */
	public void setName(String name);

	/**
	 * Returns the description of this shopping item.
	 *
	 * @return the description of this shopping item
	 */
	@AutoEscape
	public String getDescription();

	/**
	 * Sets the description of this shopping item.
	 *
	 * @param description the description of this shopping item
	 */
	public void setDescription(String description);

	/**
	 * Returns the properties of this shopping item.
	 *
	 * @return the properties of this shopping item
	 */
	@AutoEscape
	public String getProperties();

	/**
	 * Sets the properties of this shopping item.
	 *
	 * @param properties the properties of this shopping item
	 */
	public void setProperties(String properties);

	/**
	 * Returns the fields of this shopping item.
	 *
	 * @return the fields of this shopping item
	 */
	public boolean getFields();

	/**
	 * Returns <code>true</code> if this shopping item is fields.
	 *
	 * @return <code>true</code> if this shopping item is fields; <code>false</code> otherwise
	 */
	public boolean isFields();

	/**
	 * Sets whether this shopping item is fields.
	 *
	 * @param fields the fields of this shopping item
	 */
	public void setFields(boolean fields);

	/**
	 * Returns the fields quantities of this shopping item.
	 *
	 * @return the fields quantities of this shopping item
	 */
	@AutoEscape
	public String getFieldsQuantities();

	/**
	 * Sets the fields quantities of this shopping item.
	 *
	 * @param fieldsQuantities the fields quantities of this shopping item
	 */
	public void setFieldsQuantities(String fieldsQuantities);

	/**
	 * Returns the min quantity of this shopping item.
	 *
	 * @return the min quantity of this shopping item
	 */
	public int getMinQuantity();

	/**
	 * Sets the min quantity of this shopping item.
	 *
	 * @param minQuantity the min quantity of this shopping item
	 */
	public void setMinQuantity(int minQuantity);

	/**
	 * Returns the max quantity of this shopping item.
	 *
	 * @return the max quantity of this shopping item
	 */
	public int getMaxQuantity();

	/**
	 * Sets the max quantity of this shopping item.
	 *
	 * @param maxQuantity the max quantity of this shopping item
	 */
	public void setMaxQuantity(int maxQuantity);

	/**
	 * Returns the price of this shopping item.
	 *
	 * @return the price of this shopping item
	 */
	public double getPrice();

	/**
	 * Sets the price of this shopping item.
	 *
	 * @param price the price of this shopping item
	 */
	public void setPrice(double price);

	/**
	 * Returns the discount of this shopping item.
	 *
	 * @return the discount of this shopping item
	 */
	public double getDiscount();

	/**
	 * Sets the discount of this shopping item.
	 *
	 * @param discount the discount of this shopping item
	 */
	public void setDiscount(double discount);

	/**
	 * Returns the taxable of this shopping item.
	 *
	 * @return the taxable of this shopping item
	 */
	public boolean getTaxable();

	/**
	 * Returns <code>true</code> if this shopping item is taxable.
	 *
	 * @return <code>true</code> if this shopping item is taxable; <code>false</code> otherwise
	 */
	public boolean isTaxable();

	/**
	 * Sets whether this shopping item is taxable.
	 *
	 * @param taxable the taxable of this shopping item
	 */
	public void setTaxable(boolean taxable);

	/**
	 * Returns the shipping of this shopping item.
	 *
	 * @return the shipping of this shopping item
	 */
	public double getShipping();

	/**
	 * Sets the shipping of this shopping item.
	 *
	 * @param shipping the shipping of this shopping item
	 */
	public void setShipping(double shipping);

	/**
	 * Returns the use shipping formula of this shopping item.
	 *
	 * @return the use shipping formula of this shopping item
	 */
	public boolean getUseShippingFormula();

	/**
	 * Returns <code>true</code> if this shopping item is use shipping formula.
	 *
	 * @return <code>true</code> if this shopping item is use shipping formula; <code>false</code> otherwise
	 */
	public boolean isUseShippingFormula();

	/**
	 * Sets whether this shopping item is use shipping formula.
	 *
	 * @param useShippingFormula the use shipping formula of this shopping item
	 */
	public void setUseShippingFormula(boolean useShippingFormula);

	/**
	 * Returns the requires shipping of this shopping item.
	 *
	 * @return the requires shipping of this shopping item
	 */
	public boolean getRequiresShipping();

	/**
	 * Returns <code>true</code> if this shopping item is requires shipping.
	 *
	 * @return <code>true</code> if this shopping item is requires shipping; <code>false</code> otherwise
	 */
	public boolean isRequiresShipping();

	/**
	 * Sets whether this shopping item is requires shipping.
	 *
	 * @param requiresShipping the requires shipping of this shopping item
	 */
	public void setRequiresShipping(boolean requiresShipping);

	/**
	 * Returns the stock quantity of this shopping item.
	 *
	 * @return the stock quantity of this shopping item
	 */
	public int getStockQuantity();

	/**
	 * Sets the stock quantity of this shopping item.
	 *
	 * @param stockQuantity the stock quantity of this shopping item
	 */
	public void setStockQuantity(int stockQuantity);

	/**
	 * Returns the featured of this shopping item.
	 *
	 * @return the featured of this shopping item
	 */
	public boolean getFeatured();

	/**
	 * Returns <code>true</code> if this shopping item is featured.
	 *
	 * @return <code>true</code> if this shopping item is featured; <code>false</code> otherwise
	 */
	public boolean isFeatured();

	/**
	 * Sets whether this shopping item is featured.
	 *
	 * @param featured the featured of this shopping item
	 */
	public void setFeatured(boolean featured);

	/**
	 * Returns the sale of this shopping item.
	 *
	 * @return the sale of this shopping item
	 */
	public boolean getSale();

	/**
	 * Returns <code>true</code> if this shopping item is sale.
	 *
	 * @return <code>true</code> if this shopping item is sale; <code>false</code> otherwise
	 */
	public boolean isSale();

	/**
	 * Sets whether this shopping item is sale.
	 *
	 * @param sale the sale of this shopping item
	 */
	public void setSale(boolean sale);

	/**
	 * Returns the small image of this shopping item.
	 *
	 * @return the small image of this shopping item
	 */
	public boolean getSmallImage();

	/**
	 * Returns <code>true</code> if this shopping item is small image.
	 *
	 * @return <code>true</code> if this shopping item is small image; <code>false</code> otherwise
	 */
	public boolean isSmallImage();

	/**
	 * Sets whether this shopping item is small image.
	 *
	 * @param smallImage the small image of this shopping item
	 */
	public void setSmallImage(boolean smallImage);

	/**
	 * Returns the small image ID of this shopping item.
	 *
	 * @return the small image ID of this shopping item
	 */
	public long getSmallImageId();

	/**
	 * Sets the small image ID of this shopping item.
	 *
	 * @param smallImageId the small image ID of this shopping item
	 */
	public void setSmallImageId(long smallImageId);

	/**
	 * Returns the small image u r l of this shopping item.
	 *
	 * @return the small image u r l of this shopping item
	 */
	@AutoEscape
	public String getSmallImageURL();

	/**
	 * Sets the small image u r l of this shopping item.
	 *
	 * @param smallImageURL the small image u r l of this shopping item
	 */
	public void setSmallImageURL(String smallImageURL);

	/**
	 * Returns the medium image of this shopping item.
	 *
	 * @return the medium image of this shopping item
	 */
	public boolean getMediumImage();

	/**
	 * Returns <code>true</code> if this shopping item is medium image.
	 *
	 * @return <code>true</code> if this shopping item is medium image; <code>false</code> otherwise
	 */
	public boolean isMediumImage();

	/**
	 * Sets whether this shopping item is medium image.
	 *
	 * @param mediumImage the medium image of this shopping item
	 */
	public void setMediumImage(boolean mediumImage);

	/**
	 * Returns the medium image ID of this shopping item.
	 *
	 * @return the medium image ID of this shopping item
	 */
	public long getMediumImageId();

	/**
	 * Sets the medium image ID of this shopping item.
	 *
	 * @param mediumImageId the medium image ID of this shopping item
	 */
	public void setMediumImageId(long mediumImageId);

	/**
	 * Returns the medium image u r l of this shopping item.
	 *
	 * @return the medium image u r l of this shopping item
	 */
	@AutoEscape
	public String getMediumImageURL();

	/**
	 * Sets the medium image u r l of this shopping item.
	 *
	 * @param mediumImageURL the medium image u r l of this shopping item
	 */
	public void setMediumImageURL(String mediumImageURL);

	/**
	 * Returns the large image of this shopping item.
	 *
	 * @return the large image of this shopping item
	 */
	public boolean getLargeImage();

	/**
	 * Returns <code>true</code> if this shopping item is large image.
	 *
	 * @return <code>true</code> if this shopping item is large image; <code>false</code> otherwise
	 */
	public boolean isLargeImage();

	/**
	 * Sets whether this shopping item is large image.
	 *
	 * @param largeImage the large image of this shopping item
	 */
	public void setLargeImage(boolean largeImage);

	/**
	 * Returns the large image ID of this shopping item.
	 *
	 * @return the large image ID of this shopping item
	 */
	public long getLargeImageId();

	/**
	 * Sets the large image ID of this shopping item.
	 *
	 * @param largeImageId the large image ID of this shopping item
	 */
	public void setLargeImageId(long largeImageId);

	/**
	 * Returns the large image u r l of this shopping item.
	 *
	 * @return the large image u r l of this shopping item
	 */
	@AutoEscape
	public String getLargeImageURL();

	/**
	 * Sets the large image u r l of this shopping item.
	 *
	 * @param largeImageURL the large image u r l of this shopping item
	 */
	public void setLargeImageURL(String largeImageURL);

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

	public int compareTo(ShoppingItem shoppingItem);

	public int hashCode();

	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale);

	public CacheModel<ShoppingItem> toCacheModel();

	public ShoppingItem toEscapedModel();

	public String toString();

	public String toXmlString();
}