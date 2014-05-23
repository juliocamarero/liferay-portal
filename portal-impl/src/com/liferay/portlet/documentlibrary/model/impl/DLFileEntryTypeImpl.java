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

package com.liferay.portlet.documentlibrary.model.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PredicateFilter;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntryType;
import com.liferay.portlet.documentlibrary.model.DLFileEntryTypeConstants;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;

import java.util.List;
import java.util.Locale;

/**
 * @author Alexander Chow
 * @author Mate Thurzo
 */
public class DLFileEntryTypeImpl extends DLFileEntryTypeBaseImpl {

	public DLFileEntryTypeImpl() {
	}

	@Override
	public List<DDMStructure> getDDMStructures() throws SystemException {
		return DDMStructureLocalServiceUtil.getDLFileEntryTypeStructures(
			getFileEntryTypeId());
	}

	@Override
	public String getName(Locale locale) {
		String name = super.getName(locale);

		if (getFileEntryTypeId() ==
				DLFileEntryTypeConstants.FILE_ENTRY_TYPE_ID_BASIC_DOCUMENT) {

			name = LanguageUtil.get(locale, name);
		}

		return name;
	}

	@Override
	public String getUnambiguousName(
			List<DLFileEntryType> fileEntryTypes, long groupId,
			final Locale locale)
		throws PortalException, SystemException {

		if (getGroupId() == groupId ) {
			return getName(locale);
		}

		boolean hasAmbiguousFileEntryTypes = ListUtil.exists(
			fileEntryTypes, new PredicateFilter<DLFileEntryType>() {

			@Override
			public boolean filter(DLFileEntryType curFileEntryType) {
				String curFileEntryTypeName = curFileEntryType.getName(locale);

				if (curFileEntryTypeName.equals(getName(locale)) &&
					(curFileEntryType.getFileEntryTypeId()
						!= getFileEntryTypeId())) {

					return true;
				}

				return false;
			}
		});

		if (hasAmbiguousFileEntryTypes) {
			Group structureGroup = GroupLocalServiceUtil.getGroup(getGroupId());

			StringBundler sb = new StringBundler(5);

			sb.append(getName(locale));
			sb.append(StringPool.SPACE);
			sb.append(StringPool.OPEN_PARENTHESIS);
			sb.append(structureGroup.getDescriptiveName(locale));
			sb.append(StringPool.CLOSE_PARENTHESIS);

			return sb.toString();
		}

		return getName(locale);
	}

	@Override
	public boolean isExportable() {
		if (getFileEntryTypeId() ==
				DLFileEntryTypeConstants.FILE_ENTRY_TYPE_ID_BASIC_DOCUMENT) {

			return false;
		}

		return true;
	}

}