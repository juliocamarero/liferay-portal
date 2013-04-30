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

package com.liferay.portlet.bookmarks.expando;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.util.BookmarksTestUtil;
import com.liferay.portlet.expando.BasePortletExpandoTestCase;

import org.junit.Ignore;
import org.junit.runner.RunWith;

/**
 * @author Roberto Díaz
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class BookmarksEntryExpandoTest extends BasePortletExpandoTestCase {

	@Override
	@Ignore("This test does not apply")
	public void testDeleteSingleValueWhenDeletingSingleModel()
		throws Exception {
	}

	@Override
	protected ClassedModel addModel() throws Exception {
		BookmarksFolder folder = BookmarksTestUtil.addFolder(
			TestPropsValues.getGroupId(), ServiceTestUtil.randomString());

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			TestPropsValues.getGroupId());

		serviceContext.setExpandoBridgeAttributes(
			getExpandoMap(SEND_DEFAULT_EXPANDO_VALUES));

		return BookmarksTestUtil.addEntry(
			folder.getFolderId(), true, serviceContext );
	}

	@Override
	protected void deleteModel() throws Exception {
		BookmarksTestUtil.deleteEntry((Long)baseModel.getPrimaryKeyObj());
	}

	@Override
	protected void deleteModelVersion(ClassedModel modelVersion) {
	}

	@Override
	protected String getClassName() {
		return BookmarksEntry.class.getName();
	}

	@Override
	protected int getExpectedValuesCountAdded() {
		return 1;
	}

	@Override
	protected ClassedModel getModelVersion() {
		return null;
	}

	@Override
	protected ClassedModel updateBaseModel(int action) throws Exception {
		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			TestPropsValues.getGroupId());

		serviceContext.setExpandoBridgeAttributes(getExpandoMap(action));

		BookmarksEntry entry = (BookmarksEntry)baseModel;

		return BookmarksTestUtil.updateEntry(entry, serviceContext);
	}

}