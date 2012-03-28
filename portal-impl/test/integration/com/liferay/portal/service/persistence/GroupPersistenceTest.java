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

package com.liferay.portal.service.persistence;

import com.liferay.portal.NoSuchGroupException;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.impl.GroupModelImpl;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.util.PropsValues;

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
public class GroupPersistenceTest {
	@Before
	public void setUp() throws Exception {
		_persistence = (GroupPersistence)PortalBeanLocatorUtil.locate(GroupPersistence.class.getName());
	}

	@Test
	public void testCreate() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Group group = _persistence.create(pk);

		Assert.assertNotNull(group);

		Assert.assertEquals(group.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		Group newGroup = addGroup();

		_persistence.remove(newGroup);

		Group existingGroup = _persistence.fetchByPrimaryKey(newGroup.getPrimaryKey());

		Assert.assertNull(existingGroup);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addGroup();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Group newGroup = _persistence.create(pk);

		newGroup.setCompanyId(ServiceTestUtil.nextLong());

		newGroup.setCreatorUserId(ServiceTestUtil.nextLong());

		newGroup.setClassNameId(ServiceTestUtil.nextLong());

		newGroup.setClassPK(ServiceTestUtil.nextLong());

		newGroup.setParentGroupId(ServiceTestUtil.nextLong());

		newGroup.setLiveGroupId(ServiceTestUtil.nextLong());

		newGroup.setName(ServiceTestUtil.randomString());

		newGroup.setDescription(ServiceTestUtil.randomString());

		newGroup.setType(ServiceTestUtil.nextInt());

		newGroup.setTypeSettings(ServiceTestUtil.randomString());

		newGroup.setFriendlyURL(ServiceTestUtil.randomString());

		newGroup.setSite(ServiceTestUtil.randomBoolean());

		newGroup.setActive(ServiceTestUtil.randomBoolean());

		_persistence.update(newGroup, false);

		Group existingGroup = _persistence.findByPrimaryKey(newGroup.getPrimaryKey());

		Assert.assertEquals(existingGroup.getGroupId(), newGroup.getGroupId());
		Assert.assertEquals(existingGroup.getCompanyId(),
			newGroup.getCompanyId());
		Assert.assertEquals(existingGroup.getCreatorUserId(),
			newGroup.getCreatorUserId());
		Assert.assertEquals(existingGroup.getClassNameId(),
			newGroup.getClassNameId());
		Assert.assertEquals(existingGroup.getClassPK(), newGroup.getClassPK());
		Assert.assertEquals(existingGroup.getParentGroupId(),
			newGroup.getParentGroupId());
		Assert.assertEquals(existingGroup.getLiveGroupId(),
			newGroup.getLiveGroupId());
		Assert.assertEquals(existingGroup.getName(), newGroup.getName());
		Assert.assertEquals(existingGroup.getDescription(),
			newGroup.getDescription());
		Assert.assertEquals(existingGroup.getType(), newGroup.getType());
		Assert.assertEquals(existingGroup.getTypeSettings(),
			newGroup.getTypeSettings());
		Assert.assertEquals(existingGroup.getFriendlyURL(),
			newGroup.getFriendlyURL());
		Assert.assertEquals(existingGroup.getSite(), newGroup.getSite());
		Assert.assertEquals(existingGroup.getActive(), newGroup.getActive());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		Group newGroup = addGroup();

		Group existingGroup = _persistence.findByPrimaryKey(newGroup.getPrimaryKey());

		Assert.assertEquals(existingGroup, newGroup);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchGroupException");
		}
		catch (NoSuchGroupException nsee) {
		}
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		Group newGroup = addGroup();

		Group existingGroup = _persistence.fetchByPrimaryKey(newGroup.getPrimaryKey());

		Assert.assertEquals(existingGroup, newGroup);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Group missingGroup = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingGroup);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		Group newGroup = addGroup();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Group.class,
				Group.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("groupId",
				newGroup.getGroupId()));

		List<Group> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Group existingGroup = result.get(0);

		Assert.assertEquals(existingGroup, newGroup);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Group.class,
				Group.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("groupId",
				ServiceTestUtil.nextLong()));

		List<Group> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		Group newGroup = addGroup();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Group.class,
				Group.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("groupId"));

		Object newGroupId = newGroup.getGroupId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("groupId",
				new Object[] { newGroupId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingGroupId = result.get(0);

		Assert.assertEquals(existingGroupId, newGroupId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Group.class,
				Group.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("groupId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("groupId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		Group newGroup = addGroup();

		_persistence.clearCache();

		GroupModelImpl existingGroupModelImpl = (GroupModelImpl)_persistence.findByPrimaryKey(newGroup.getPrimaryKey());

		Assert.assertEquals(existingGroupModelImpl.getLiveGroupId(),
			existingGroupModelImpl.getOriginalLiveGroupId());

		Assert.assertEquals(existingGroupModelImpl.getCompanyId(),
			existingGroupModelImpl.getOriginalCompanyId());
		Assert.assertTrue(Validator.equals(existingGroupModelImpl.getName(),
				existingGroupModelImpl.getOriginalName()));

		Assert.assertEquals(existingGroupModelImpl.getCompanyId(),
			existingGroupModelImpl.getOriginalCompanyId());
		Assert.assertTrue(Validator.equals(
				existingGroupModelImpl.getFriendlyURL(),
				existingGroupModelImpl.getOriginalFriendlyURL()));

		Assert.assertEquals(existingGroupModelImpl.getCompanyId(),
			existingGroupModelImpl.getOriginalCompanyId());
		Assert.assertEquals(existingGroupModelImpl.getClassNameId(),
			existingGroupModelImpl.getOriginalClassNameId());
		Assert.assertEquals(existingGroupModelImpl.getClassPK(),
			existingGroupModelImpl.getOriginalClassPK());

		Assert.assertEquals(existingGroupModelImpl.getCompanyId(),
			existingGroupModelImpl.getOriginalCompanyId());
		Assert.assertEquals(existingGroupModelImpl.getLiveGroupId(),
			existingGroupModelImpl.getOriginalLiveGroupId());
		Assert.assertTrue(Validator.equals(existingGroupModelImpl.getName(),
				existingGroupModelImpl.getOriginalName()));

		Assert.assertEquals(existingGroupModelImpl.getCompanyId(),
			existingGroupModelImpl.getOriginalCompanyId());
		Assert.assertEquals(existingGroupModelImpl.getClassNameId(),
			existingGroupModelImpl.getOriginalClassNameId());
		Assert.assertEquals(existingGroupModelImpl.getLiveGroupId(),
			existingGroupModelImpl.getOriginalLiveGroupId());
		Assert.assertTrue(Validator.equals(existingGroupModelImpl.getName(),
				existingGroupModelImpl.getOriginalName()));
	}

	protected Group addGroup() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Group group = _persistence.create(pk);

		group.setCompanyId(ServiceTestUtil.nextLong());

		group.setCreatorUserId(ServiceTestUtil.nextLong());

		group.setClassNameId(ServiceTestUtil.nextLong());

		group.setClassPK(ServiceTestUtil.nextLong());

		group.setParentGroupId(ServiceTestUtil.nextLong());

		group.setLiveGroupId(ServiceTestUtil.nextLong());

		group.setName(ServiceTestUtil.randomString());

		group.setDescription(ServiceTestUtil.randomString());

		group.setType(ServiceTestUtil.nextInt());

		group.setTypeSettings(ServiceTestUtil.randomString());

		group.setFriendlyURL(ServiceTestUtil.randomString());

		group.setSite(ServiceTestUtil.randomBoolean());

		group.setActive(ServiceTestUtil.randomBoolean());

		_persistence.update(group, false);

		return group;
	}

	private GroupPersistence _persistence;
}