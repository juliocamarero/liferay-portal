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

package com.liferay.portlet.mobiledevicerules.lar;

import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portlet.mobiledevicerules.model.MDRRuleGroupInstance;

/**
 * @author Mate Thurzo
 */
public class MDRRuleGroupInstanceStagedModelDataHandler
	extends BaseStagedModelDataHandler<MDRRuleGroupInstance> {

	@Override
	public String getClassName() {
		return MDRRuleGroupInstance.class.getName();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Element[] elements,
			MDRRuleGroupInstance ruleGroupInstance)
		throws Exception {

		return;
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			Element ruleGroupInstanceElement, String path,
			MDRRuleGroupInstance ruleGroupInstance)
		throws Exception {

		return;
	}

}