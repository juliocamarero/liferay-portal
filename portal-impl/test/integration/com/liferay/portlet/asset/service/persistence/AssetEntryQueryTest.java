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

package com.liferay.portlet.asset.service.persistence;

import com.liferay.portal.kernel.cache.Lifecycle;
import com.liferay.portal.kernel.cache.ThreadLocalCache;
import com.liferay.portal.kernel.cache.ThreadLocalCacheManager;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.asset.service.AssetEntryServiceUtil;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.asset.service.impl.AssetEntryServiceImpl;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Sergio González
 */
@ExecutionTestListeners(
	listeners = {
		EnvironmentExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class AssetEntryQueryTest {

	@Test
	public void testAllAssetTags1() throws Exception {
		String[] assetTagNames = new String[] {"liferay"};

		testAssetTags(assetTagNames, false, false, 2);
	}

	@Test
	public void testAllAssetTags2() throws Exception {
		String[] assetTagNames = new String[] {"liferay", "architecture"};

		testAssetTags(assetTagNames, false, false, 2);
	}

	@Test
	public void testAllAssetTags3() throws Exception {
		String[] assetTagNames =
			new String[] {"liferay", "architecture", "services"};

		testAssetTags(assetTagNames, false, false, 1);
	}

	@Test
	public void testAllAssetTags4() throws Exception {
		String[] assetTagNames =
			new String[] {"liferay", "architecture", "services", "osgi"};

		testAssetTags(assetTagNames, false, false, 0);
	}

	@Test
	public void testAnyAssetTags1() throws Exception {
		String[] assetTagNames = new String[] {"liferay"};

		testAssetTags(assetTagNames, true, false, 2);
	}

	@Test
	public void testAnyAssetTags2() throws Exception {
		String[] assetTagNames = new String[] {"liferay", "architecture"};

		testAssetTags(assetTagNames, true, false, 2);
	}

	@Test
	public void testAnyAssetTags3() throws Exception {
		String[] assetTagNames =
			new String[] {"liferay", "architecture", "services"};

		testAssetTags(assetTagNames, true, false, 2);
	}

	@Test
	public void testAnyAssetTags4() throws Exception {
		String[] assetTagNames = new String[] {"modularity", "osgi"};

		testAssetTags(assetTagNames, true, false, 1);
	}

	@Test
	public void testNotAllAssetTags1() throws Exception {
		String[] assetTagNames = new String[] {"liferay"};

		testAssetTags(assetTagNames, false, true, 0);
	}

	@Test
	public void testNotAllAssetTags2() throws Exception {
		String[] assetTagNames = new String[] {"liferay", "architecture"};

		testAssetTags(assetTagNames, false, true, 0);
	}

	@Test
	public void testNotAllAssetTags3() throws Exception {
		String[] assetTagNames =
			new String[] {"liferay", "architecture", "services"};

		testAssetTags(assetTagNames, false, true, 1);
	}

	@Test
	public void testNotAllAssetTags4() throws Exception {
		String[] assetTagNames =
			new String[] {"liferay", "architecture", "services", "osgi"};

		testAssetTags(assetTagNames, false, true, 2);
	}

	@Test
	public void testNotAnyAssetTags1() throws Exception {
		String[] assetTagNames = new String[] {"liferay"};

		testAssetTags(assetTagNames, true, true, 0);
	}

	@Test
	public void testNotAnyAssetTags2() throws Exception {
		String[] assetTagNames = new String[] {"liferay", "architecture"};

		testAssetTags(assetTagNames, true, true, 0);
	}

	@Test
	public void testNotAnyAssetTags3() throws Exception {
		String[] assetTagNames =
			new String[] {"liferay", "architecture", "services"};

		testAssetTags(assetTagNames, true, true, 0);
	}

	@Test
	public void testNotAnyAssetTags4() throws Exception {
		String[] assetTagNames = new String[] {"modularity", "osgi"};

		testAssetTags(assetTagNames, true, true, 1);
	}

	private static AssetEntryQuery buildAssetEntryQueryTags(
		AssetEntryQuery assetEntryQuery, long[] assetTagIds, boolean any,
		boolean not) {

		if (any && not) {
			assetEntryQuery.setNotAnyTagIds(assetTagIds);
		}
		else if (!any && not) {
			assetEntryQuery.setNotAllTagIds(assetTagIds);
		}
		else if (any && !not) {
			assetEntryQuery.setAnyTagIds(assetTagIds);
		}
		else {
			assetEntryQuery.setAllTagIds(assetTagIds);
		}

		return assetEntryQuery;
	}

	private void testAssetTags(
			String[] assetTagNames, boolean any, boolean not,
			int expectedResults)
		throws Exception {

		// Clear the thread local cache which is populated in AssetPublisherUtil

		ThreadLocalCache<Object[]> threadLocalCache =
			ThreadLocalCacheManager.getThreadLocalCache(
				Lifecycle.REQUEST, AssetEntryServiceImpl.class.getName());

		threadLocalCache.removeAll();

		Group group = ServiceTestUtil.addGroup();

		long[] assetTagIds = AssetTagLocalServiceUtil.getTagIds(
			group.getGroupId(), assetTagNames);

		AssetEntryQuery assetEntryQuery = new AssetEntryQuery();

		assetEntryQuery = buildAssetEntryQueryTags(
			assetEntryQuery, assetTagIds, any, not);

		assetEntryQuery.setGroupIds(new long[] {group.getGroupId()});

		int initialEntries = AssetEntryServiceUtil.getEntriesCount(
			assetEntryQuery);

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			group.getGroupId());

		serviceContext.setAssetTagNames(_assetTagNames1);

		BlogsEntryLocalServiceUtil.addEntry(
			TestPropsValues.getUserId(), "Liferay Architectural Approach",
			StringPool.BLANK, "This is a blog entry for testing purposes", 1, 1,
			1965, 0, 0, true, true, null, false, null, null, null,
			serviceContext);

		serviceContext.setAssetTagNames(_assetTagNames2);

		BlogsEntryLocalServiceUtil.addEntry(
			TestPropsValues.getUserId(), "Modularity with OSGI",
			StringPool.BLANK, "This is a blog entry for testing purposes", 1, 1,
			1965, 0, 0, true, true, null, false, null, null, null,
			serviceContext);

		// Clear the thread local cache which is populated in AssetPublisherUtil

		threadLocalCache.removeAll();

		assetTagIds = AssetTagLocalServiceUtil.getTagIds(
			group.getGroupId(), assetTagNames);

		assetEntryQuery = buildAssetEntryQueryTags(
			assetEntryQuery, assetTagIds, any, not);

		int allTagsEntries = AssetEntryServiceUtil.getEntriesCount(
			assetEntryQuery);

		Assert.assertEquals(initialEntries + expectedResults, allTagsEntries);
	}

	private String[] _assetTagNames1 =
		new String[] {"liferay", "architecture", "services"};
	private String[] _assetTagNames2 =
		new String[] {"liferay", "architecture", "modularity", "osgi"};

}