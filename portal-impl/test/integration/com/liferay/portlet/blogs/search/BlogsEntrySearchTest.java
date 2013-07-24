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

package com.liferay.portlet.blogs.search;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.search.BaseSearchTestCase;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.Sync;
import com.liferay.portal.test.SynchronousDestinationExecutionTestListener;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.util.BlogsTestUtil;

import org.junit.Assert;
import org.junit.runner.RunWith;

/**
 * @author Eudaldo Alonso
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		SynchronousDestinationExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Sync
public class BlogsEntrySearchTest extends BaseSearchTestCase {

	@Override
	public void testParentBaseModelUserPermissions() throws Exception {
		Assert.assertTrue("This test does not apply", true);
	}

	@Override
	public void testSearchAttachments() throws Exception {
		Assert.assertTrue("This test does not apply", true);
	}

	@Override
	public void testSearchByDDMStructureField() throws Exception {
		Assert.assertTrue("This test does not apply", true);
	}

	@Override
	public void testSearchExpireAllVersions() throws Exception {
		Assert.assertTrue("This test does not apply", true);
	}

	@Override
	public void testSearchExpireLatestVersion() throws Exception {
		Assert.assertTrue("This test does not apply", true);
	}

	@Override
	public void testSearchMyEntries() throws Exception {
		Assert.assertTrue("This test does not apply", true);
	}

	@Override
	public void testSearchRecentEntries() throws Exception {
		Assert.assertTrue("This test does not apply", true);
	}

	@Override
	public void testSearchStatus() throws Exception {
		Assert.assertTrue("This test does not apply", true);
	}

	@Override
	public void testSearchVersions() throws Exception {
		Assert.assertTrue("This test does not apply", true);
	}

	@Override
	public void testSearchWithinDDMStructure() throws Exception {
		Assert.assertTrue("This test does not apply", true);
	}

	@Override
	protected BaseModel<?> addBaseModelWithWorkflow(
			BaseModel<?> parentBaseModel, boolean approved, String keywords,
			ServiceContext serviceContext)
		throws Exception {

		return BlogsTestUtil.addEntry(
			TestPropsValues.getUserId(), keywords, approved, serviceContext);
	}

	@Override
	protected Class<?> getBaseModelClass() {
		return BlogsEntry.class;
	}

	@Override
	protected String getSearchKeywords() {
		return "Title";
	}

}