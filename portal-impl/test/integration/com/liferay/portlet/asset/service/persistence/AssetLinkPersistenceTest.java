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

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.util.PropsValues;

import com.liferay.portlet.asset.NoSuchLinkException;
import com.liferay.portlet.asset.model.AssetLink;
import com.liferay.portlet.asset.model.impl.AssetLinkModelImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
@ExecutionTestListeners(listeners =  {
	PersistenceExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class AssetLinkPersistenceTest {
	@Before
	public void setUp() throws Exception {
		_persistence = (AssetLinkPersistence)PortalBeanLocatorUtil.locate(AssetLinkPersistence.class.getName());
	}

	@Test
	public void testCreate() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetLink assetLink = _persistence.create(pk);

		Assert.assertNotNull(assetLink);

		Assert.assertEquals(assetLink.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		AssetLink newAssetLink = addAssetLink();

		_persistence.remove(newAssetLink);

		AssetLink existingAssetLink = _persistence.fetchByPrimaryKey(newAssetLink.getPrimaryKey());

		Assert.assertNull(existingAssetLink);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addAssetLink();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetLink newAssetLink = _persistence.create(pk);

		newAssetLink.setCompanyId(ServiceTestUtil.nextLong());

		newAssetLink.setUserId(ServiceTestUtil.nextLong());

		newAssetLink.setUserName(ServiceTestUtil.randomString());

		newAssetLink.setCreateDate(ServiceTestUtil.nextDate());

		newAssetLink.setEntryId1(ServiceTestUtil.nextLong());

		newAssetLink.setEntryId2(ServiceTestUtil.nextLong());

		newAssetLink.setType(ServiceTestUtil.nextInt());

		newAssetLink.setWeight(ServiceTestUtil.nextInt());

		_persistence.update(newAssetLink, false);

		AssetLink existingAssetLink = _persistence.findByPrimaryKey(newAssetLink.getPrimaryKey());

		Assert.assertEquals(existingAssetLink.getLinkId(),
			newAssetLink.getLinkId());
		Assert.assertEquals(existingAssetLink.getCompanyId(),
			newAssetLink.getCompanyId());
		Assert.assertEquals(existingAssetLink.getUserId(),
			newAssetLink.getUserId());
		Assert.assertEquals(existingAssetLink.getUserName(),
			newAssetLink.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingAssetLink.getCreateDate()),
			Time.getShortTimestamp(newAssetLink.getCreateDate()));
		Assert.assertEquals(existingAssetLink.getEntryId1(),
			newAssetLink.getEntryId1());
		Assert.assertEquals(existingAssetLink.getEntryId2(),
			newAssetLink.getEntryId2());
		Assert.assertEquals(existingAssetLink.getType(), newAssetLink.getType());
		Assert.assertEquals(existingAssetLink.getWeight(),
			newAssetLink.getWeight());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		AssetLink newAssetLink = addAssetLink();

		AssetLink existingAssetLink = _persistence.findByPrimaryKey(newAssetLink.getPrimaryKey());

		Assert.assertEquals(existingAssetLink, newAssetLink);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchLinkException");
		}
		catch (NoSuchLinkException nsee) {
		}
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		AssetLink newAssetLink = addAssetLink();

		AssetLink existingAssetLink = _persistence.fetchByPrimaryKey(newAssetLink.getPrimaryKey());

		Assert.assertEquals(existingAssetLink, newAssetLink);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetLink missingAssetLink = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingAssetLink);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		AssetLink newAssetLink = addAssetLink();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetLink.class,
				AssetLink.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("linkId",
				newAssetLink.getLinkId()));

		List<AssetLink> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		AssetLink existingAssetLink = result.get(0);

		Assert.assertEquals(existingAssetLink, newAssetLink);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetLink.class,
				AssetLink.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("linkId",
				ServiceTestUtil.nextLong()));

		List<AssetLink> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		AssetLink newAssetLink = addAssetLink();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetLink.class,
				AssetLink.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("linkId"));

		Object newLinkId = newAssetLink.getLinkId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("linkId",
				new Object[] { newLinkId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingLinkId = result.get(0);

		Assert.assertEquals(existingLinkId, newLinkId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetLink.class,
				AssetLink.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("linkId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("linkId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		AssetLink newAssetLink = addAssetLink();

		_persistence.clearCache();

		AssetLinkModelImpl existingAssetLinkModelImpl = (AssetLinkModelImpl)_persistence.findByPrimaryKey(newAssetLink.getPrimaryKey());

		Assert.assertEquals(existingAssetLinkModelImpl.getEntryId1(),
			existingAssetLinkModelImpl.getOriginalEntryId1());
		Assert.assertEquals(existingAssetLinkModelImpl.getEntryId2(),
			existingAssetLinkModelImpl.getOriginalEntryId2());
		Assert.assertEquals(existingAssetLinkModelImpl.getType(),
			existingAssetLinkModelImpl.getOriginalType());
	}

	protected AssetLink addAssetLink() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetLink assetLink = _persistence.create(pk);

		assetLink.setCompanyId(ServiceTestUtil.nextLong());

		assetLink.setUserId(ServiceTestUtil.nextLong());

		assetLink.setUserName(ServiceTestUtil.randomString());

		assetLink.setCreateDate(ServiceTestUtil.nextDate());

		assetLink.setEntryId1(ServiceTestUtil.nextLong());

		assetLink.setEntryId2(ServiceTestUtil.nextLong());

		assetLink.setType(ServiceTestUtil.nextInt());

		assetLink.setWeight(ServiceTestUtil.nextInt());

		_persistence.update(assetLink, false);

		return assetLink;
	}

	private AssetLinkPersistence _persistence;
}