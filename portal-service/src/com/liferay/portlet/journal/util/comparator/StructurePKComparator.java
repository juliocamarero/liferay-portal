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

package com.liferay.portlet.journal.util.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portlet.journal.model.JournalStructure;

/**
 * @author Vilmos Papp
 * @author Brian Wing Shun Chan
 */
public class StructurePKComparator extends OrderByComparator {

	public static final String ORDER_BY_ASC = "JournalStructure.id_ ASC";

	public static final String ORDER_BY_DESC = "JournalStructure.id_ DESC";

	public static final String[] ORDER_BY_FIELDS = {"id_"};

	public StructurePKComparator() {
		this(false);
	}

	public StructurePKComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		JournalStructure structure1 = (JournalStructure)obj1;
		JournalStructure structure2 = (JournalStructure)obj2;

		int value = 0;

		if (structure1.getId() < structure2.getId()) {
			value = -1;
		}
		else if (structure1.getId() > structure2.getId()) {
			value = 1;
		}

		if (_ascending) {
			return value;
		}
		else {
			return -value;
		}
	}

	@Override
	public String getOrderBy() {
		if (_ascending) {
			return ORDER_BY_ASC;
		}
		else {
			return ORDER_BY_DESC;
		}
	}

	@Override
	public String[] getOrderByFields() {
		return ORDER_BY_FIELDS;
	}

	@Override
	public boolean isAscending() {
		return _ascending;
	}

	private boolean _ascending;

}