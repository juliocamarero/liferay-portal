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

package com.liferay.journal.content.web.entries;

import com.liferay.portal.model.SelectableEntry;

public class RatingsSelectableEntry implements SelectableEntry {
	private boolean _selected = false;

	public RatingsSelectableEntry(boolean selected) {
		this._selected = selected;
	}

	public String getIcon() {
		return "star-half-full";
	}

	public String getKey() {
		return "enableRatings";
	}

	public String getLabel() {
		return "enable-ratings";
	}

	public boolean getSelected() {
		return this._selected;
	}

	public void setIcon(String icon) {

	}

	public void setKey(String key) {

	}

	public void setLabel(String label) {

	}

	public void setSelected(boolean selected) {

	}
}