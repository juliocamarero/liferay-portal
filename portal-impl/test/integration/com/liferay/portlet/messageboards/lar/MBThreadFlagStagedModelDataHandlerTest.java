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

package com.liferay.portlet.messageboards.lar;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.lar.BaseStagedModelDataHandlerTestCase;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portlet.messageboards.model.MBCategory;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBThreadFlag;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadFlagLocalServiceUtil;
import com.liferay.portlet.messageboards.util.MBTestUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.runner.RunWith;

/**
 * @author Daniel Kocsis
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class MBThreadFlagStagedModelDataHandlerTest
	extends BaseStagedModelDataHandlerTestCase {

	@Override
	protected Map<String, List<StagedModel>> addDependentStagedModelsMap(
			Group group)
		throws Exception {

		Map<String, List<StagedModel>> dependentStagedModelsMap =
			new HashMap<String, List<StagedModel>>();

		MBMessage message = MBTestUtil.addMessage(group.getGroupId());

		addDependentStagedModel(
			dependentStagedModelsMap, MBMessage.class, message);

		return dependentStagedModelsMap;
	}

	@Override
	protected StagedModel addStagedModel(
			Group group, Map<String,
			List<StagedModel>> dependentStagedModelsMap)
		throws Exception {

		List<StagedModel> dependentStagedModels = dependentStagedModelsMap.get(
			MBMessage.class.getName());

		MBMessage message = (MBMessage)dependentStagedModels.get(0);

		return MBTestUtil.addThreadFlag(
			group.getGroupId(), message.getThread());
	}

	@Override
	protected Element[] getDependentStagedModelsElements(
		Map<String, List<StagedModel>> dependentStagedModelsMap) {

		return new Element[] {
			SAXReaderUtil.createElement(MBCategory.class.getName()),
			SAXReaderUtil.createElement(MBMessage.class.getName()),
			SAXReaderUtil.createElement(MBThreadFlag.class.getName())
		};
	}

	@Override
	protected String getElementName() {
		return "thread-flag";
	}

	@Override
	protected StagedModel getStagedModel(String uuid, Group group) {
		return null;
	}

	@Override
	protected String getStagedModelClassName() {
		return MBThreadFlag.class.getName();
	}

	@Override
	protected void validateImport(
			Map<String, List<StagedModel>> dependentStagedModelsMap,
			Group group)
		throws Exception {

		List<StagedModel> dependentStagedModels = dependentStagedModelsMap.get(
			MBMessage.class.getName());

		Assert.assertEquals(1, dependentStagedModels.size());

		MBMessage message = (MBMessage)dependentStagedModels.get(0);

		MBMessageLocalServiceUtil.getMBMessageByUuidAndGroupId(
			message.getUuid(), group.getGroupId());
	}

	@Override
	protected void validateImport(
			StagedModel stagedModel,
			Map<String, List<StagedModel>> dependentStagedModelsMap,
			Group group)
		throws Exception {

		validateImport(dependentStagedModelsMap, group);

		MBThreadFlag threadFlag = (MBThreadFlag)stagedModel;

		List<StagedModel> dependentStagedModels = dependentStagedModelsMap.get(
			MBMessage.class.getName());

		MBMessage message = (MBMessage)dependentStagedModels.get(0);

		Assert.assertTrue(
			MBThreadFlagLocalServiceUtil.hasThreadFlag(
				threadFlag.getUserId(), message.getThread()));
	}

}