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

package com.liferay.portal.layout;

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.model.LayoutTypePortletConstants;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalCallbackAwareExecutionTestListener;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.sites.util.SitesUtil;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;

/**
 * @author Julio Camarero
 * @author László Csontos
 */
@PrepareForTest({PortletLocalServiceUtil.class})

@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		TransactionalCallbackAwareExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class LayoutSetPrototypeTest extends BaseLayoutSetPrototypeTestCase {

	@Before
	public void setUp() throws Exception {
		FinderCacheUtil.clearCache();
		ServiceContextThreadLocal.pushServiceContext(
			ServiceTestUtil.getServiceContext());
	}

	@Test
	public void testLSPLinkDisabled() throws Exception {
		runLayoutSetPrototype(false, false, false, false, false);
	}

	@Test
	public void testLSPLinkDisabledWithPageAddition() throws Exception {
		runLayoutSetPrototype(false, false, true, false, false);
	}

	@Test
	public void testLSPLinkDisabledWithPageDeletion() throws Exception {
		runLayoutSetPrototype(false, false, true, true, false);
	}

	@Test
	public void testLSPLinkEnabled() throws Exception {
		runLayoutSetPrototype(true, false, false, false, false);
	}

	@Test
	public void testLSPLinkEnabledwithPageAddition() throws Exception {
		runLayoutSetPrototype(true, false, true, false, false);
	}

	@Test
	public void testLSPLinkEnabledwithPageAdditionFromLPLinkDisabled()
		throws Exception {

		runLayoutSetPrototype(true, false, true, false, true);
	}

	@Test
	public void testLSPLinkEnabledwithPageAdditionFromLPLinkEnabled()
		throws Exception {

		runLayoutSetPrototype(true, true, true, false, true);
	}

	@Test
	public void testLSPLinkEnabledwithPageDeletion() throws Exception {
		runLayoutSetPrototype(true, false, true, true, false);
	}

	@Test
	public void testLSPLinkEnabledwithPageDeletionFromLP() throws Exception {
		runLayoutSetPrototype(true, false, true, true, true);
	}

	@Test
	public void testMergeLayoutPrototypeLayout() throws Exception {

		// Add site template

		LayoutSetPrototype layoutSetPrototype =
			ServiceTestUtil.addLayoutSetPrototype(
				ServiceTestUtil.randomString());

		// Add page template

		LayoutPrototype layoutPrototype = ServiceTestUtil.addLayoutPrototype(
			ServiceTestUtil.randomString());

		Layout layoutPrototypeLayout = layoutPrototype.getLayout();

		// Add portlets to page template

		Assert.assertNotNull(
			ServiceTestUtil.addPortletToLayout(
				layoutPrototypeLayout, PortletKeys.JOURNAL_CONTENT,
				"column-1"));

		Assert.assertNotNull(
			ServiceTestUtil.addPortletToLayout(
				layoutPrototypeLayout, PortletKeys.WIKI_DISPLAY, "column-2"));

		updateLayoutTemplateId(layoutPrototypeLayout, "2_2_columns");

		Group layoutSetPrototypeGroup = layoutSetPrototype.getGroup();

		ServiceTestUtil.addLayout(
			layoutSetPrototypeGroup.getGroupId(),
			ServiceTestUtil.randomString(), layoutPrototype, true);

		// Add site based on site template

		Group group = ServiceTestUtil.addGroup(
			GroupConstants.DEFAULT_PARENT_GROUP_ID,
			ServiceTestUtil.randomString(),
			layoutSetPrototype.getLayoutSetPrototypeId());

		SitesUtil.updateLayoutSetPrototypesLinks(
			group, layoutSetPrototype.getLayoutSetPrototypeId(), 0L, true,
			false);

		propagateChanges(group);

		List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(
			group.getGroupId(), false);

		Layout layout = layouts.get(0);

		SitesUtil.applyLayoutPrototype(layoutPrototype, layout, true);

		// Change layout of page template

		updateLayoutTemplateId(layoutPrototypeLayout, "1_column");

		propagateChanges(layout);

		// Compare if settings were successfully propagated

		UnicodeProperties layoutProperties = layout.getTypeSettingsProperties();

		int mergeFailCount = GetterUtil.getInteger(
			layoutProperties.get(SitesUtil.MERGE_FAIL_COUNT));

		Assert.assertEquals(0, mergeFailCount);

		layoutProperties.remove(SitesUtil.LAST_MERGE_TIME);
		layoutProperties.remove(SitesUtil.MERGE_FAIL_COUNT);

		UnicodeProperties layoutPrototypeLayoutProperties =
			layoutPrototypeLayout.getTypeSettingsProperties();

		mergeFailCount = GetterUtil.getInteger(
			layoutPrototypeLayoutProperties.get(SitesUtil.MERGE_FAIL_COUNT));

		Assert.assertEquals(0, mergeFailCount);

		layoutPrototypeLayoutProperties.remove(SitesUtil.MERGE_FAIL_COUNT);

		Assert.assertEquals(layoutPrototypeLayoutProperties, layoutProperties);
	}

	protected void runLayoutSetPrototype(
		boolean layoutSetLinkEnabled, boolean layoutLinkEnabled,
		boolean addPage, boolean deletePage, boolean useLayoutPrototype)
		throws Exception {

		LayoutSetPrototype layoutSetPrototype =
			ServiceTestUtil.addLayoutSetPrototype(
				ServiceTestUtil.randomString());

		Group layoutSetPrototypeGroup = layoutSetPrototype.getGroup();

		int layoutSetPrototypeLayoutsCount =
			LayoutLocalServiceUtil.getLayoutsCount(
				layoutSetPrototypeGroup, true);

		ServiceTestUtil.addLayout(
			layoutSetPrototypeGroup.getGroupId(),
			ServiceTestUtil.randomString(), true);
		ServiceTestUtil.addLayout(
			layoutSetPrototypeGroup.getGroupId(),
			ServiceTestUtil.randomString(), true);

		Group group = ServiceTestUtil.addGroup();

		SitesUtil.updateLayoutSetPrototypesLinks(
			group, layoutSetPrototype.getLayoutSetPrototypeId(), 0,
			layoutSetLinkEnabled, false);

		propagateChanges(group);

		int groupLayoutsCount = LayoutLocalServiceUtil.getLayoutsCount(
			group, false);

		Assert.assertEquals(
			groupLayoutsCount, layoutSetPrototypeLayoutsCount + 2);

		if (addPage) {
			Layout layout = null;

			if (useLayoutPrototype) {
				LayoutPrototype layoutPrototype =
					ServiceTestUtil.addLayoutPrototype(
						ServiceTestUtil.randomString());

				Layout layoutPrototypeLayout = layoutPrototype.getLayout();

				updateLayoutTemplateId(layoutPrototypeLayout, "2_2_columns");

				layout = ServiceTestUtil.addLayout(
					group.getGroupId(), ServiceTestUtil.randomString(),
					layoutPrototype, layoutLinkEnabled);

				if (layoutLinkEnabled) {
					propagateChanges(layout);
				}

				updateLayoutTemplateId(layoutPrototypeLayout, "1_column");

				if (layoutLinkEnabled) {
					Assert.assertEquals(
						"2_2_columns",
						layout.getTypeSettingsProperty(
							LayoutTypePortletConstants.LAYOUT_TEMPLATE_ID));

					propagateChanges(layout);
				}
			}
			else {
				layout = ServiceTestUtil.addLayout(
					layoutSetPrototypeGroup.getGroupId(),
					ServiceTestUtil.randomString(), true);
			}

			if (!useLayoutPrototype) {
				groupLayoutsCount = LayoutLocalServiceUtil.getLayoutsCount(
					group, false);

				Assert.assertEquals(
					groupLayoutsCount, layoutSetPrototypeLayoutsCount + 2);
			}

			propagateChanges(group);

			groupLayoutsCount = LayoutLocalServiceUtil.getLayoutsCount(
				group, false);

			if (layoutSetLinkEnabled) {
				Assert.assertEquals(
					groupLayoutsCount, layoutSetPrototypeLayoutsCount + 3);

				if (useLayoutPrototype) {
					if (layoutLinkEnabled) {
						Assert.assertEquals(
							"1_column",
							layout.getTypeSettingsProperty(
								LayoutTypePortletConstants.LAYOUT_TEMPLATE_ID));
					}
					else {
						Assert.assertEquals(
							"2_2_columns",
							layout.getTypeSettingsProperty(
								LayoutTypePortletConstants.LAYOUT_TEMPLATE_ID));
					}
				}
			}

			if (deletePage) {
				LayoutLocalServiceUtil.deleteLayout(
					layout.getPlid(), ServiceTestUtil.getServiceContext());

				groupLayoutsCount = LayoutLocalServiceUtil.getLayoutsCount(
					group, false);

				if (layoutSetLinkEnabled) {
					if (!useLayoutPrototype) {
						Assert.assertEquals(
							groupLayoutsCount,
							layoutSetPrototypeLayoutsCount + 3);

						propagateChanges(group);
					}

					groupLayoutsCount = LayoutLocalServiceUtil.getLayoutsCount(
						group, false);
				}

				Assert.assertEquals(
					groupLayoutsCount, layoutSetPrototypeLayoutsCount + 2);
			}
		}
	}

}