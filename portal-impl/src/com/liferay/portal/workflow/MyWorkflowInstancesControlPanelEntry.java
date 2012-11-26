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

package com.liferay.portal.workflow;

import com.liferay.portal.kernel.workflow.WorkflowInstanceManagerUtil;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * @author Miguel Pastor
 */
public class MyWorkflowInstancesControlPanelEntry
	extends WorkflowControlPanelEntry {

	@Override
	protected boolean hasPermissionImplicit(
			Portlet portlet, String category, ThemeDisplay themeDisplay)
		throws Exception {

		if (WorkflowInstanceManagerUtil.getWorkflowInstanceCount(
				themeDisplay.getCompanyId(), themeDisplay.getUserId(), null,
				null, null) > 0) {

			return true;
		}

		return false;
	}

}