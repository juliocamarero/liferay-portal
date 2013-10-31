/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.util.Validator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link Image}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Image
 * @generated
 */
@ProviderType
public class ImageWrapper implements Image, ModelWrapper<Image> {
	public ImageWrapper(Image image) {
		_image = image;
	}

	@Override
	public Class<?> getModelClass() {
		return Image.class;
	}

	@Override
	public String getModelClassName() {
		return Image.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("imageId", getImageId());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("type", getType());
		attributes.put("height", getHeight());
		attributes.put("width", getWidth());
		attributes.put("size", getSize());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long imageId = (Long)attributes.get("imageId");

		if (imageId != null) {
			setImageId(imageId);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String type = (String)attributes.get("type");

		if (type != null) {
			setType(type);
		}

		Integer height = (Integer)attributes.get("height");

		if (height != null) {
			setHeight(height);
		}

		Integer width = (Integer)attributes.get("width");

		if (width != null) {
			setWidth(width);
		}

		Integer size = (Integer)attributes.get("size");

		if (size != null) {
			setSize(size);
		}
	}

	/**
	* Returns the primary key of this image.
	*
	* @return the primary key of this image
	*/
	@Override
	public long getPrimaryKey() {
		return _image.getPrimaryKey();
	}

	/**
	* Sets the primary key of this image.
	*
	* @param primaryKey the primary key of this image
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_image.setPrimaryKey(primaryKey);
	}

	/**
	* Returns the image ID of this image.
	*
	* @return the image ID of this image
	*/
	@Override
	public long getImageId() {
		return _image.getImageId();
	}

	/**
	* Sets the image ID of this image.
	*
	* @param imageId the image ID of this image
	*/
	@Override
	public void setImageId(long imageId) {
		_image.setImageId(imageId);
	}

	/**
	* Returns the modified date of this image.
	*
	* @return the modified date of this image
	*/
	@Override
	public java.util.Date getModifiedDate() {
		return _image.getModifiedDate();
	}

	/**
	* Sets the modified date of this image.
	*
	* @param modifiedDate the modified date of this image
	*/
	@Override
	public void setModifiedDate(java.util.Date modifiedDate) {
		_image.setModifiedDate(modifiedDate);
	}

	/**
	* Returns the type of this image.
	*
	* @return the type of this image
	*/
	@Override
	public java.lang.String getType() {
		return _image.getType();
	}

	/**
	* Sets the type of this image.
	*
	* @param type the type of this image
	*/
	@Override
	public void setType(java.lang.String type) {
		_image.setType(type);
	}

	/**
	* Returns the height of this image.
	*
	* @return the height of this image
	*/
	@Override
	public int getHeight() {
		return _image.getHeight();
	}

	/**
	* Sets the height of this image.
	*
	* @param height the height of this image
	*/
	@Override
	public void setHeight(int height) {
		_image.setHeight(height);
	}

	/**
	* Returns the width of this image.
	*
	* @return the width of this image
	*/
	@Override
	public int getWidth() {
		return _image.getWidth();
	}

	/**
	* Sets the width of this image.
	*
	* @param width the width of this image
	*/
	@Override
	public void setWidth(int width) {
		_image.setWidth(width);
	}

	/**
	* Returns the size of this image.
	*
	* @return the size of this image
	*/
	@Override
	public int getSize() {
		return _image.getSize();
	}

	/**
	* Sets the size of this image.
	*
	* @param size the size of this image
	*/
	@Override
	public void setSize(int size) {
		_image.setSize(size);
	}

	@Override
	public boolean isNew() {
		return _image.isNew();
	}

	@Override
	public void setNew(boolean n) {
		_image.setNew(n);
	}

	@Override
	public boolean isCachedModel() {
		return _image.isCachedModel();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_image.setCachedModel(cachedModel);
	}

	@Override
	public boolean isEscapedModel() {
		return _image.isEscapedModel();
	}

	@Override
	public java.io.Serializable getPrimaryKeyObj() {
		return _image.getPrimaryKeyObj();
	}

	@Override
	public void setPrimaryKeyObj(java.io.Serializable primaryKeyObj) {
		_image.setPrimaryKeyObj(primaryKeyObj);
	}

	@Override
	public com.liferay.portlet.expando.model.ExpandoBridge getExpandoBridge() {
		return _image.getExpandoBridge();
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.model.BaseModel<?> baseModel) {
		_image.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portlet.expando.model.ExpandoBridge expandoBridge) {
		_image.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.service.ServiceContext serviceContext) {
		_image.setExpandoBridgeAttributes(serviceContext);
	}

	@Override
	public java.lang.Object clone() {
		return new ImageWrapper((Image)_image.clone());
	}

	@Override
	public int compareTo(com.liferay.portal.model.Image image) {
		return _image.compareTo(image);
	}

	@Override
	public int hashCode() {
		return _image.hashCode();
	}

	@Override
	public com.liferay.portal.model.CacheModel<com.liferay.portal.model.Image> toCacheModel() {
		return _image.toCacheModel();
	}

	@Override
	public com.liferay.portal.model.Image toEscapedModel() {
		return new ImageWrapper(_image.toEscapedModel());
	}

	@Override
	public com.liferay.portal.model.Image toUnescapedModel() {
		return new ImageWrapper(_image.toUnescapedModel());
	}

	@Override
	public java.lang.String toString() {
		return _image.toString();
	}

	@Override
	public java.lang.String toXmlString() {
		return _image.toXmlString();
	}

	@Override
	public void persist()
		throws com.liferay.portal.kernel.exception.SystemException {
		_image.persist();
	}

	@Override
	public byte[] getTextObj() {
		return _image.getTextObj();
	}

	@Override
	public void setTextObj(byte[] textObj) {
		_image.setTextObj(textObj);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ImageWrapper)) {
			return false;
		}

		ImageWrapper imageWrapper = (ImageWrapper)obj;

		if (Validator.equals(_image, imageWrapper._image)) {
			return true;
		}

		return false;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedModel}
	 */
	public Image getWrappedImage() {
		return _image;
	}

	@Override
	public Image getWrappedModel() {
		return _image;
	}

	@Override
	public void resetOriginalValues() {
		_image.resetOriginalValues();
	}

	private Image _image;
}