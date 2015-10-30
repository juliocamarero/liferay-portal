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

package com.liferay.journal.web.lar.lifecycle;

import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.journal.util.impl.JournalContentUtil;
import com.liferay.portlet.exportimport.lar.PortletDataContext;
import com.liferay.portlet.exportimport.lifecycle.BaseExportImportLifecycleListener;
import com.liferay.portlet.exportimport.lifecycle.ExportImportLifecycleListener;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Mate Thurzo
 */
@Component(service = ExportImportLifecycleListener.class)
public class JournalCacheExportImportLifecycleListener
	extends BaseExportImportLifecycleListener {

	@Override
	public boolean isParallel() {
		return false;
	}

	protected void clearCache() {
		_journalContentUtil.clearCache();
	}

	@Override
	protected void onLayoutImportProcessFinished(
			PortletDataContext portletDataContext)
		throws Exception {

		clearCache();
	}

	@Override
	protected void onPortletImportProcessFinished(
			PortletDataContext portletDataContext)
		throws Exception {

		String rootPortletId = portletDataContext.getRootPortletId();

		if (!rootPortletId.equals(JournalPortletKeys.JOURNAL)) {
			return;
		}

		clearCache();
	}

	@Reference
	protected void setJournalContentUtil(
		JournalContentUtil journalContentUtil) {

		_journalContentUtil = journalContentUtil;
	}

	private JournalContentUtil _journalContentUtil;

}