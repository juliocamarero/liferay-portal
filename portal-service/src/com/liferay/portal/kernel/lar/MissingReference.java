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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.xml.Element;

/**
 * @author Zsolt Berentey
 */
public class MissingReference {

	public MissingReference(Element element) {
		className = element.attributeValue("class-name");
		referrerClassName = element.attributeValue("referrer");
		name = element.attributeValue("name");
		referrerName = element.attributeValue("referrer-name");
	}

	public String getClassName() {
		return className;
	}

	public String getName() {
		return name;
	}

	public String getReferrerClassName() {
		return referrerClassName;
	}

	public String getReferrerName() {
		return referrerName;
	}

	private String className;
	private String name = StringPool.BLANK;
	private String referrerClassName = StringPool.BLANK;
	private String referrerName = StringPool.BLANK;

}