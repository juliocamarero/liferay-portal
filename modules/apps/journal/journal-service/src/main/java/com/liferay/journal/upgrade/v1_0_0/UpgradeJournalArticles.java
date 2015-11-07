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

package com.liferay.journal.upgrade.v1_0_0;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.PortletConstants;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.service.LayoutLocalService;
import com.liferay.portal.upgrade.util.UpgradePortletId;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.PortletPreferencesImpl;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.service.AssetCategoryLocalService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
public class UpgradeJournalArticles extends UpgradePortletId {

	protected long getCategoryId(long companyId, String type) throws Exception {
		List<AssetCategory> assetCategories = _assetCategoryLocalService.search(
			companyId, type, new String[0], QueryUtil.ALL_POS,
			QueryUtil.ALL_POS);

		if (!assetCategories.isEmpty()) {
			AssetCategory assetCategory = assetCategories.get(0);

			return assetCategory.getCategoryId();
		}

		return 0;
	}

	protected String getNewPreferences(long plid, String preferences)
		throws Exception {

		PortletPreferences oldPortletPreferences =
			PortletPreferencesFactoryUtil.fromDefaultXML(preferences);

		String ddmStructureKey = oldPortletPreferences.getValue(
			"ddmStructureKey", StringPool.BLANK);
		long groupId = GetterUtil.getLong(
			oldPortletPreferences.getValue("groupId", StringPool.BLANK));
		String orderByCol = oldPortletPreferences.getValue(
			"orderByCol", StringPool.BLANK);
		String orderByType = oldPortletPreferences.getValue(
			"orderByType", StringPool.BLANK);
		int pageDelta = GetterUtil.getInteger(
			oldPortletPreferences.getValue("pageDelta", StringPool.BLANK));
		String pageUrl = oldPortletPreferences.getValue(
			"pageUrl", StringPool.BLANK);
		String type = oldPortletPreferences.getValue("type", StringPool.BLANK);

		PortletPreferences newPortletPreferences = new PortletPreferencesImpl();

		newPortletPreferences.setValue(
			"anyAssetType",
			String.valueOf(
				PortalUtil.getClassNameId(JournalArticle.class.getName())));

		Layout layout = _layoutLocalService.getLayout(plid);

		long structureId = getStructureId(
			layout.getCompanyId(), layout.getGroupId(), ddmStructureKey);

		if (structureId > 0) {
			newPortletPreferences.setValue(
				"anyClassTypeJournalArticleAssetRendererFactory",
				String.valueOf(structureId));
		}

		String assetLinkBehavior = "showFullContent";

		if (pageUrl.equals("viewInContext")) {
			assetLinkBehavior = "viewInPortlet";
		}

		newPortletPreferences.setValue("assetLinkBehavior", assetLinkBehavior);

		if (structureId > 0) {
			newPortletPreferences.setValue(
				"classTypeIds", String.valueOf(structureId));
		}

		newPortletPreferences.setValue("delta", String.valueOf(pageDelta));
		newPortletPreferences.setValue("displayStyle", "table");
		newPortletPreferences.setValue("metadataFields", "publish-date,author");
		newPortletPreferences.setValue("orderByColumn1", orderByCol);
		newPortletPreferences.setValue("orderByType1", orderByType);
		newPortletPreferences.setValue("paginationType", "none");

		long categoryId = getCategoryId(layout.getCompanyId(), type);

		if (categoryId > 0) {
			newPortletPreferences.setValue(
				"queryAndOperator0", Boolean.TRUE.toString());
			newPortletPreferences.setValue(
				"queryContains0", Boolean.TRUE.toString());
			newPortletPreferences.setValue("queryName0", "assetCategories");
			newPortletPreferences.setValue(
				"queryValues0", String.valueOf(categoryId));
		}

		newPortletPreferences.setValue(
			"showAddContentButton", Boolean.FALSE.toString());

		String groupName = String.valueOf(groupId);

		if (groupId == layout.getGroupId()) {
			groupName = "default";
		}

		newPortletPreferences.setValue("scopeIds", "Group_" + groupName);

		return PortletPreferencesFactoryUtil.toXML(newPortletPreferences);
	}

	@Override
	protected String[][] getRenamePortletIdsArray() {
		return new String[][] {
			new String[] {
				_PORTLET_ID_JOURNAL_CONTENT_LIST, _PORTLET_ID_ASSET_PUBLISHER
			}
		};
	}

	protected long getStructureId(
			long companyId, long groupId, String ddmStructureKey)
		throws Exception {

		DDMStructure ddmStructure = _ddmStructureLocalService.fetchStructure(
			groupId, PortalUtil.getClassNameId(JournalArticle.class.getName()),
			ddmStructureKey);

		if (ddmStructure == null) {
			Group companyGroup = _groupLocalService.getCompanyGroup(companyId);

			_ddmStructureLocalService.fetchStructure(
				companyGroup.getGroupId(),
				PortalUtil.getClassNameId(JournalArticle.class.getName()),
				ddmStructureKey);
		}

		if (ddmStructure != null) {
			return ddmStructure.getStructureId();
		}

		return 0;
	}

	@Reference(unbind = "-")
	protected void setAssetCategoryLocalService(
		AssetCategoryLocalService assetCategoryLocalService) {

		_assetCategoryLocalService = assetCategoryLocalService;
	}

	@Reference(unbind = "-")
	protected void setDDMStructureLocalService(
		DDMStructureLocalService ddmStructureLocalService) {

		_ddmStructureLocalService = ddmStructureLocalService;
	}

	@Reference(unbind = "-")
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	@Reference(unbind = "-")
	protected void setLayoutLocalService(
		LayoutLocalService layoutLocalService) {

		_layoutLocalService = layoutLocalService;
	}

	@Override
	protected void updateInstanceablePortletPreferences(
			String oldRootPortletId, String newRootPortletId)
		throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			StringBundler sb = new StringBundler(9);

			sb.append("select portletPreferencesId, plid, portletId, ");
			sb.append("preferences from PortletPreferences where portletId ");
			sb.append("= '");
			sb.append(oldRootPortletId);
			sb.append("' OR portletId like '");
			sb.append(oldRootPortletId);
			sb.append("_INSTANCE_%' OR portletId like '");
			sb.append(oldRootPortletId);
			sb.append("_USER_%_INSTANCE_%'");

			ps = con.prepareStatement(sb.toString());

			rs = ps.executeQuery();

			while (rs.next()) {
				long portletPreferencesId = rs.getLong("portletPreferencesId");
				long plid = rs.getLong("plid");
				String portletId = rs.getString("portletId");
				String preferences = rs.getString("preferences");

				if (preferences.equals("<portlet-preferences />")) {
					continue;
				}

				String newPreferences = getNewPreferences(plid, preferences);

				long userId = PortletConstants.getUserId(portletId);
				String instanceId = PortletConstants.getInstanceId(portletId);

				String newPortletId = PortletConstants.assemblePortletId(
					_PORTLET_ID_ASSET_PUBLISHER, userId, instanceId);

				updatePortletPreference(
					portletPreferencesId, newPortletId, newPreferences);
			}
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	@Override
	protected void updatePortlet(
			String oldRootPortletId, String newRootPortletId)
		throws Exception {

		try {
			updateResourcePermission(oldRootPortletId, newRootPortletId, true);

			updateInstanceablePortletPreferences(
				oldRootPortletId, newRootPortletId);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}
	}

	protected void updatePortletPreference(
			long portletPreferencesId, String newPortletId,
			String newPreferences)
		throws Exception {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"update PortletPreferences set preferences = ?, " +
					"portletId = ? where portletPreferencesId = " +
						portletPreferencesId);

			ps.setString(1, newPreferences);
			ps.setString(2, newPortletId);

			ps.executeUpdate();
		}
		catch (SQLException sqle) {
			if (_log.isWarnEnabled()) {
				_log.warn(sqle, sqle);
			}
		}
		finally {
			DataAccess.cleanUp(con, ps);
		}
	}

	private static final String _PORTLET_ID_ASSET_PUBLISHER =
		"com_liferay_asset_publisher_web_AssetPublisherPortlet";

	private static final String _PORTLET_ID_JOURNAL_CONTENT_LIST = "62";

	private static final Log _log = LogFactoryUtil.getLog(
		UpgradeJournalArticles.class);

	private AssetCategoryLocalService _assetCategoryLocalService;
	private DDMStructureLocalService _ddmStructureLocalService;
	private GroupLocalService _groupLocalService;
	private LayoutLocalService _layoutLocalService;

}