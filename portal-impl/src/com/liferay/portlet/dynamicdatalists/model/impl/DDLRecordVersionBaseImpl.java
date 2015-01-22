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

package com.liferay.portlet.dynamicdatalists.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portlet.dynamicdatalists.model.DDLRecordVersion;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordVersionLocalServiceUtil;

/**
 * The extended model base implementation for the DDLRecordVersion service. Represents a row in the &quot;DDLRecordVersion&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This class exists only as a container for the default extended model level methods generated by ServiceBuilder. Helper methods and all application logic should be put in {@link DDLRecordVersionImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DDLRecordVersionImpl
 * @see com.liferay.portlet.dynamicdatalists.model.DDLRecordVersion
 * @generated
 */
@ProviderType
public abstract class DDLRecordVersionBaseImpl extends DDLRecordVersionModelImpl
	implements DDLRecordVersion {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a d d l record version model instance should use the {@link DDLRecordVersion} interface instead.
	 */
	@Override
	public void persist() {
		if (this.isNew()) {
			DDLRecordVersionLocalServiceUtil.addDDLRecordVersion(this);
		}
		else {
			DDLRecordVersionLocalServiceUtil.updateDDLRecordVersion(this);
		}
	}
}