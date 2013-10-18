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

package com.liferay.portal.kernel.search;

/**
 * @author Eudaldo Alonso
 */
public class SearchResultExtraInfo {

	public SearchResultExtraInfo(
		String key, String value, String src, boolean image) {

		_image = image;
		_key = key;
		_src = src;
		_value = value;
	}

	public String getKey() {
		return _key;
	}

	public String getSrc() {
		return _src;
	}

	public String getValue() {
		return _value;
	}

	public boolean isImage() {
		return _image;
	}

	public void setImage(boolean image) {
		_image = image;
	}

	public void setKey(String key) {
		_key = key;
	}

	public void setSrc(String src) {
		_src = src;
	}

	public void setValue(String value) {
		_value = value;
	}

	private boolean _image;
	private String _key;
	private String _src;
	private String _value;

}