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

package com.liferay.portlet.layoutsadmin.lar;

import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.model.Layout;

/**
 * @author Mate Thurzo
 */
public class LayoutStagedModelDataHandler
	extends BaseStagedModelDataHandler<Layout> {

	public static final String[] CLASS_NAMES =
		new String[] {Layout.class.getName()};

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Layout layout)
		throws Exception {

		String path = ExportImportPathUtil.getLayoutPath(
			portletDataContext, layout.getLayoutId()) + "/layout.xml";

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		LayoutRevision layoutRevision = null;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		boolean exportLAR = ParamUtil.getBoolean(serviceContext, "exportLAR");

		if (!exportLAR && LayoutStagingUtil.isBranchingLayout(layout) &&
			!layout.isTypeURL()) {

			long layoutSetBranchId = ParamUtil.getLong(
				serviceContext, "layoutSetBranchId");

			if (layoutSetBranchId <= 0) {
				return;
			}

			layoutRevision = LayoutRevisionUtil.fetchByL_H_P(
				layoutSetBranchId, true, layout.getPlid());

			if (layoutRevision == null) {
				return;
			}

			LayoutStagingHandler layoutStagingHandler =
				LayoutStagingUtil.getLayoutStagingHandler(layout);

			layoutStagingHandler.setLayoutRevision(layoutRevision);
		}

		Element layoutElement = layoutsElement.addElement("layout");

		if (layoutRevision != null) {
			layoutElement.addAttribute(
				"layout-revision-id",
				String.valueOf(layoutRevision.getLayoutRevisionId()));
			layoutElement.addAttribute(
				"layout-branch-id",
				String.valueOf(layoutRevision.getLayoutBranchId()));
			layoutElement.addAttribute(
				"layout-branch-name",
				String.valueOf(layoutRevision.getLayoutBranch().getName()));
		}

		layoutElement.addAttribute("layout-uuid", layout.getUuid());
		layoutElement.addAttribute(
			"layout-id", String.valueOf(layout.getLayoutId()));

		long parentLayoutId = layout.getParentLayoutId();

		if (parentLayoutId != LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {
			Layout parentLayout = LayoutLocalServiceUtil.getLayout(
				layout.getGroupId(), layout.isPrivateLayout(), parentLayoutId);

			if (parentLayout != null) {
				layoutElement.addAttribute(
					"parent-layout-uuid", parentLayout.getUuid());
			}
		}

		String layoutPrototypeUuid = layout.getLayoutPrototypeUuid();

		if (Validator.isNotNull(layoutPrototypeUuid)) {
			LayoutPrototype layoutPrototype =
				LayoutPrototypeLocalServiceUtil.
					getLayoutPrototypeByUuidAndCompanyId(
						layoutPrototypeUuid, portletDataContext.getCompanyId());

			layoutElement.addAttribute(
				"layout-prototype-uuid", layoutPrototypeUuid);
			layoutElement.addAttribute(
				"layout-prototype-name",
				layoutPrototype.getName(LocaleUtil.getDefault()));
		}

		boolean deleteLayout = MapUtil.getBoolean(
			portletDataContext.getParameterMap(), "delete_" + layout.getPlid());

		if (deleteLayout) {
			layoutElement.addAttribute("delete", String.valueOf(true));

			return;
		}

		portletDataContext.setPlid(layout.getPlid());

		if (layout.isIconImage()) {
			Image image = ImageLocalServiceUtil.getImage(
				layout.getIconImageId());

			if (image != null) {
				String iconPath = getLayoutIconPath(
					portletDataContext, layout, image);

				layoutElement.addElement("icon-image-path").addText(iconPath);

				portletDataContext.addZipEntry(iconPath, image.getTextObj());
			}
		}

		_portletExporter.exportPortletData(
			portletDataContext, layoutConfigurationPortlet, layout, null,
			layoutElement);

		// Layout permissions

		if (exportPermissions) {
			_permissionExporter.exportLayoutPermissions(
				portletDataContext, layoutCache,
				portletDataContext.getCompanyId(),
				portletDataContext.getScopeGroupId(), layout, layoutElement);
		}

		if (layout.isTypeArticle()) {
			exportJournalArticle(portletDataContext, layout, layoutElement);
		}

		if (layout.isTypeLinkToLayout()) {
			UnicodeProperties typeSettingsProperties =
				layout.getTypeSettingsProperties();

			long linkToLayoutId = GetterUtil.getLong(
				typeSettingsProperties.getProperty(
					"linkToLayoutId", StringPool.BLANK));

			if (linkToLayoutId > 0) {
				try {
					Layout linkedToLayout = LayoutLocalServiceUtil.getLayout(
						portletDataContext.getScopeGroupId(),
						layout.isPrivateLayout(), linkToLayoutId);

					exportLayout(
						portletDataContext, layoutConfigurationPortlet,
						layoutCache, portlets, portletIds, exportPermissions,
						linkedToLayout, layoutsElement);
				}
				catch (NoSuchLayoutException nsle) {
				}
			}
		}
		else if (layout.isSupportsEmbeddedPortlets()) {

			// Only portlet type layouts support page scoping

			if (layout.isTypePortlet()) {
				for (Portlet portlet : portlets) {
					if (portlet.isScopeable() && layout.hasScopeGroup()) {
						String key = PortletPermissionUtil.getPrimaryKey(
							layout.getPlid(), portlet.getPortletId());

						portletIds.put(
							key,
							new Object[] {
								portlet.getPortletId(), layout.getPlid(),
								layout.getScopeGroup().getGroupId(),
								StringPool.BLANK, layout.getUuid()
						});
					}
				}
			}

			LayoutTypePortlet layoutTypePortlet =
				(LayoutTypePortlet)layout.getLayoutType();

			// The getAllPortlets method returns all effective portlets for any
			// layout type, including embedded portlets, or in the case of panel
			// type layout, selected portlets

			for (Portlet portlet : layoutTypePortlet.getAllPortlets()) {
				String portletId = portlet.getPortletId();

				javax.portlet.PortletPreferences jxPreferences =
					PortletPreferencesFactoryUtil.getLayoutPortletSetup(
						layout, portletId);

				String scopeType = GetterUtil.getString(
					jxPreferences.getValue("lfrScopeType", null));
				String scopeLayoutUuid = GetterUtil.getString(
					jxPreferences.getValue("lfrScopeLayoutUuid", null));

				long scopeGroupId = portletDataContext.getScopeGroupId();

				if (Validator.isNotNull(scopeType)) {
					Group scopeGroup = null;

					if (scopeType.equals("company")) {
						scopeGroup = GroupLocalServiceUtil.getCompanyGroup(
							layout.getCompanyId());
					}
					else if (scopeType.equals("layout")) {
						Layout scopeLayout = null;

						scopeLayout =
							LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
								scopeLayoutUuid,
								portletDataContext.getGroupId(),
								portletDataContext.isPrivateLayout());

						if (scopeLayout == null) {
							continue;
						}

						scopeGroup = scopeLayout.getScopeGroup();
					}
					else {
						throw new IllegalArgumentException(
							"Scope type " + scopeType + " is invalid");
					}

					if (scopeGroup != null) {
						scopeGroupId = scopeGroup.getGroupId();
					}
				}

				String key = PortletPermissionUtil.getPrimaryKey(
					layout.getPlid(), portletId);

				portletIds.put(
					key,
					new Object[] {
						portletId, layout.getPlid(), scopeGroupId, scopeType,
						scopeLayoutUuid
					}
				);
			}
		}

		fixTypeSettings(layout);

		layoutElement.addAttribute("path", path);

		portletDataContext.addExpando(layoutElement, path, layout);

		portletDataContext.addZipEntry(path, layout);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, Layout layout)
		throws Exception {

		long groupId = portletDataContext.getGroupId();

		String layoutUuid = GetterUtil.getString(
			layoutElement.attributeValue("layout-uuid"));

		long layoutId = GetterUtil.getInteger(
			layoutElement.attributeValue("layout-id"));

		long oldLayoutId = layoutId;

		boolean deleteLayout = GetterUtil.getBoolean(
			layoutElement.attributeValue("delete"));

		if (deleteLayout) {
			Layout layout = LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
				layoutUuid, groupId, privateLayout);

			if (layout != null) {
				newLayoutsMap.put(oldLayoutId, layout);

				ServiceContext serviceContext =
					ServiceContextThreadLocal.getServiceContext();

				LayoutLocalServiceUtil.deleteLayout(
					layout, false, serviceContext);
			}

			return;
		}

		String path = layoutElement.attributeValue("path");

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		Layout layout = (Layout)portletDataContext.getZipEntryAsObject(path);

		Layout existingLayout = null;
		Layout importedLayout = null;

		String friendlyURL = layout.getFriendlyURL();

		if (layoutsImportMode.equals(
				PortletDataHandlerKeys.LAYOUTS_IMPORT_MODE_ADD_AS_NEW)) {

			layoutId = LayoutLocalServiceUtil.getNextLayoutId(
				groupId, privateLayout);
			friendlyURL = StringPool.SLASH + layoutId;
		}
		else if (layoutsImportMode.equals(
					PortletDataHandlerKeys.
						LAYOUTS_IMPORT_MODE_MERGE_BY_LAYOUT_NAME)) {

			Locale locale = LocaleUtil.getDefault();

			String localizedName = layout.getName(locale);

			for (Layout curLayout : previousLayouts) {
				if (localizedName.equals(curLayout.getName(locale)) ||
					friendlyURL.equals(curLayout.getFriendlyURL())) {

					existingLayout = curLayout;

					break;
				}
			}

			if (existingLayout == null) {
				layoutId = LayoutLocalServiceUtil.getNextLayoutId(
					groupId, privateLayout);
			}
		}
		else if (layoutsImportMode.equals(
					PortletDataHandlerKeys.
						LAYOUTS_IMPORT_MODE_CREATED_FROM_PROTOTYPE)) {

			existingLayout = LayoutUtil.fetchByG_P_SPLU(
				groupId, privateLayout, layout.getUuid());

			if (SitesUtil.isLayoutModifiedSinceLastMerge(existingLayout)) {
				newLayoutsMap.put(oldLayoutId, existingLayout);

				return;
			}
		}
		else {

			// The default behaviour of import mode is
			// PortletDataHandlerKeys.LAYOUTS_IMPORT_MODE_MERGE_BY_LAYOUT_UUID

			existingLayout = LayoutUtil.fetchByUUID_G_P(
				layout.getUuid(), groupId, privateLayout);

			if (existingLayout == null) {
				existingLayout = LayoutUtil.fetchByG_P_F(
					groupId, privateLayout, friendlyURL);
			}

			if (existingLayout == null) {
				layoutId = LayoutLocalServiceUtil.getNextLayoutId(
					groupId, privateLayout);
			}
		}

		if (_log.isDebugEnabled()) {
			if (existingLayout == null) {
				_log.debug(
					"Layout with {groupId=" + groupId + ",privateLayout=" +
						privateLayout + ",layoutId=" + layoutId +
							"} does not exist");
			}
			else {
				_log.debug(
					"Layout with {groupId=" + groupId + ",privateLayout=" +
						privateLayout + ",layoutId=" + layoutId + "} exists");
			}
		}

		if (existingLayout == null) {
			long plid = CounterLocalServiceUtil.increment();

			importedLayout = LayoutUtil.create(plid);

			if (layoutsImportMode.equals(
					PortletDataHandlerKeys.
							LAYOUTS_IMPORT_MODE_CREATED_FROM_PROTOTYPE)) {

				importedLayout.setSourcePrototypeLayoutUuid(layout.getUuid());

				layoutId = LayoutLocalServiceUtil.getNextLayoutId(
					groupId, privateLayout);
			}
			else {
				importedLayout.setCreateDate(layout.getCreateDate());
				importedLayout.setModifiedDate(layout.getModifiedDate());
				importedLayout.setLayoutPrototypeUuid(
					layout.getLayoutPrototypeUuid());
				importedLayout.setLayoutPrototypeLinkEnabled(
					layout.isLayoutPrototypeLinkEnabled());
				importedLayout.setSourcePrototypeLayoutUuid(
					layout.getSourcePrototypeLayoutUuid());
			}

			importedLayout.setUuid(layout.getUuid());
			importedLayout.setGroupId(groupId);
			importedLayout.setPrivateLayout(privateLayout);
			importedLayout.setLayoutId(layoutId);

			// Resources

			boolean addGroupPermissions = true;

			Group group = importedLayout.getGroup();

			if (privateLayout && group.isUser()) {
				addGroupPermissions = false;
			}

			boolean addGuestPermissions = false;

			if (!privateLayout || layout.isTypeControlPanel()) {
				addGuestPermissions = true;
			}

			ResourceLocalServiceUtil.addResources(
				user.getCompanyId(), groupId, user.getUserId(),
				Layout.class.getName(), importedLayout.getPlid(), false,
				addGroupPermissions, addGuestPermissions);

			LayoutSet layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(
				groupId, privateLayout);

			importedLayout.setLayoutSet(layoutSet);
		}
		else {
			importedLayout = existingLayout;
		}

		newLayoutsMap.put(oldLayoutId, importedLayout);

		long parentLayoutId = layout.getParentLayoutId();

		Node parentLayoutNode = rootElement.selectSingleNode(
			"./layouts/layout[@layout-id='" + parentLayoutId + "']");

		String parentLayoutUuid = GetterUtil.getString(
			layoutElement.attributeValue("parent-layout-uuid"));

		if ((parentLayoutId != LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) &&
			(parentLayoutNode != null)) {

			importLayout(
				portletDataContext, user, layoutCache, previousLayouts,
				newLayouts, newLayoutsMap, portletsMergeMode, themeId,
				colorSchemeId, layoutsImportMode, privateLayout,
				importPermissions, importPublicLayoutPermissions,
				importThemeSettings, rootElement, (Element)parentLayoutNode);

			Layout parentLayout = newLayoutsMap.get(parentLayoutId);

			parentLayoutId = parentLayout.getLayoutId();
		}
		else if (Validator.isNotNull(parentLayoutUuid)) {
			Layout parentLayout =
				LayoutLocalServiceUtil.getLayoutByUuidAndGroupId(
					parentLayoutUuid, groupId, privateLayout);

			parentLayoutId = parentLayout.getLayoutId();
		}

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Importing layout with layout id " + layoutId +
					" and parent layout id " + parentLayoutId);
		}

		importedLayout.setCompanyId(user.getCompanyId());
		importedLayout.setParentLayoutId(parentLayoutId);
		importedLayout.setName(layout.getName());
		importedLayout.setTitle(layout.getTitle());
		importedLayout.setDescription(layout.getDescription());
		importedLayout.setKeywords(layout.getKeywords());
		importedLayout.setRobots(layout.getRobots());
		importedLayout.setType(layout.getType());

		if (layout.isTypeArticle()) {
			importJournalArticle(portletDataContext, layout, layoutElement);

			updateTypeSettings(importedLayout, layout);
		}
		else if (layout.isTypePortlet() &&
				 Validator.isNotNull(layout.getTypeSettings()) &&
				 !portletsMergeMode.equals(
					PortletDataHandlerKeys.PORTLETS_MERGE_MODE_REPLACE)) {

			mergePortlets(
				importedLayout, layout.getTypeSettings(), portletsMergeMode);
		}
		else if (layout.isTypeLinkToLayout()) {
			UnicodeProperties typeSettingsProperties =
				layout.getTypeSettingsProperties();

			long linkToLayoutId = GetterUtil.getLong(
				typeSettingsProperties.getProperty(
					"linkToLayoutId", StringPool.BLANK));

			if (linkToLayoutId > 0) {
				Node linkedLayoutNode = rootElement.selectSingleNode(
					"./layouts/layout[@layout-id='" + linkToLayoutId + "']");

				if (linkedLayoutNode != null) {
					importLayout(
						portletDataContext, user, layoutCache, previousLayouts,
						newLayouts, newLayoutsMap, portletsMergeMode, themeId,
						colorSchemeId, layoutsImportMode, privateLayout,
						importPermissions, importPublicLayoutPermissions,
						importThemeSettings, rootElement,
						(Element)linkedLayoutNode);

					Layout linkedLayout = newLayoutsMap.get(linkToLayoutId);

					typeSettingsProperties.setProperty(
						"privateLayout",
						String.valueOf(linkedLayout.isPrivateLayout()));
					typeSettingsProperties.setProperty(
						"linkToLayoutId",
						String.valueOf(linkedLayout.getLayoutId()));
				}
				else {
					if (_log.isWarnEnabled()) {
						StringBundler sb = new StringBundler();

						sb.append("Unable to link layout with friendly URL ");
						sb.append(layout.getFriendlyURL());
						sb.append(" and layout id ");
						sb.append(layout.getLayoutId());
						sb.append(" to layout with layout id ");
						sb.append(linkToLayoutId);

						_log.warn(sb.toString());
					}
				}
			}

			updateTypeSettings(importedLayout, layout);
		}
		else {
			updateTypeSettings(importedLayout, layout);
		}

		importedLayout.setHidden(layout.isHidden());
		importedLayout.setFriendlyURL(friendlyURL);

		if (importThemeSettings) {
			importedLayout.setThemeId(layout.getThemeId());
			importedLayout.setColorSchemeId(layout.getColorSchemeId());
		}
		else {
			importedLayout.setThemeId(StringPool.BLANK);
			importedLayout.setColorSchemeId(StringPool.BLANK);
		}

		importedLayout.setWapThemeId(layout.getWapThemeId());
		importedLayout.setWapColorSchemeId(layout.getWapColorSchemeId());
		importedLayout.setCss(layout.getCss());
		importedLayout.setPriority(layout.getPriority());
		importedLayout.setLayoutPrototypeUuid(layout.getLayoutPrototypeUuid());
		importedLayout.setLayoutPrototypeLinkEnabled(
			layout.isLayoutPrototypeLinkEnabled());

		StagingUtil.updateLastImportSettings(
			layoutElement, importedLayout, portletDataContext);

		fixTypeSettings(importedLayout);

		importedLayout.setIconImage(false);

		if (layout.isIconImage()) {
			String iconImagePath = layoutElement.elementText("icon-image-path");

			byte[] iconBytes = portletDataContext.getZipEntryAsByteArray(
				iconImagePath);

			if ((iconBytes != null) && (iconBytes.length > 0)) {
				importedLayout.setIconImage(true);

				if (importedLayout.getIconImageId() == 0) {
					long iconImageId = CounterLocalServiceUtil.increment();

					importedLayout.setIconImageId(iconImageId);
				}

				ImageLocalServiceUtil.updateImage(
					importedLayout.getIconImageId(), iconBytes);
			}
		}
		else {
			ImageLocalServiceUtil.deleteImage(importedLayout.getIconImageId());
		}

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			layoutElement, importedLayout, null);

		importedLayout.setExpandoBridgeAttributes(serviceContext);

		LayoutUtil.update(importedLayout);

		portletDataContext.setPlid(importedLayout.getPlid());
		portletDataContext.setOldPlid(layout.getPlid());

		newLayouts.add(importedLayout);

		// Layout permissions

		if (importPermissions) {
			_permissionImporter.importLayoutPermissions(
				layoutCache, portletDataContext.getCompanyId(), groupId,
				user.getUserId(), importedLayout, layoutElement, rootElement);
		}

		if (importPublicLayoutPermissions) {
			String resourceName = Layout.class.getName();
			String resourcePrimKey = String.valueOf(importedLayout.getPlid());

			Role guestRole = RoleLocalServiceUtil.getRole(
				importedLayout.getCompanyId(), RoleConstants.GUEST);

			ResourcePermissionLocalServiceUtil.setResourcePermissions(
				importedLayout.getCompanyId(), resourceName,
				ResourceConstants.SCOPE_INDIVIDUAL, resourcePrimKey,
				guestRole.getRoleId(), new String[] {ActionKeys.VIEW});
		}

		_portletImporter.importPortletData(
			portletDataContext, PortletKeys.DOCKBAR, null, layoutElement);
	}

	protected void exportJournalArticle(
			PortletDataContext portletDataContext, Layout layout,
			Element layoutElement)
		throws Exception {

		UnicodeProperties typeSettingsProperties =
			layout.getTypeSettingsProperties();

		String articleId = typeSettingsProperties.getProperty(
			"article-id", StringPool.BLANK);

		long articleGroupId = layout.getGroupId();

		if (Validator.isNull(articleId)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"No article id found in typeSettings of layout " +
						layout.getPlid());
			}
		}

		JournalArticle article = null;

		try {
			article = JournalArticleLocalServiceUtil.getLatestArticle(
				articleGroupId, articleId, WorkflowConstants.STATUS_APPROVED);
		}
		catch (NoSuchArticleException nsae) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"No approved article found with group id " +
						articleGroupId + " and article id " + articleId);
			}
		}

		if (article == null) {
			return;
		}

		portletDataContext.setExportDataRootElement(layoutElement.getParent());

		StagedModelDataHandlerUtil.exportStagedModel(
			portletDataContext, article);

		portletDataContext.addReferenceElement(layoutElement, article);
	}

	protected void fixImportedLayoutTypeSettings(Layout layout)
		throws Exception {

		if (!layout.isTypeURL()) {
			return;
		}

		UnicodeProperties typeSettings = layout.getTypeSettingsProperties();

		String url = GetterUtil.getString(typeSettings.getProperty("url"));

		String friendlyURLPrivateGroupPath =
			PropsValues.LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING;
		String friendlyURLPrivateUserPath =
			PropsValues.LAYOUT_FRIENDLY_URL_PRIVATE_USER_SERVLET_MAPPING;
		String friendlyURLPublicPath =
			PropsValues.LAYOUT_FRIENDLY_URL_PUBLIC_SERVLET_MAPPING;

		if (!url.startsWith(friendlyURLPrivateGroupPath) &&
			!url.startsWith(friendlyURLPrivateUserPath) &&
			!url.startsWith(friendlyURLPublicPath)) {

			return;
		}

		int x = url.indexOf(CharPool.SLASH, 1);

		if (x == -1) {
			return;
		}

		int y = url.indexOf(CharPool.SLASH, x + 1);

		if (y == -1) {
			return;
		}

		String friendlyURL = url.substring(x, y);

		if (!friendlyURL.equals(LayoutExporter.SAME_GROUP_FRIENDLY_URL)) {
			return;
		}

		Group group = layout.getGroup();

		typeSettings.setProperty(
			"url",
			url.substring(0, x) + group.getFriendlyURL() + url.substring(y));
	}

	protected void fixTypeSettings(Layout layout) throws Exception {
		if (!layout.isTypeURL()) {
			return;
		}

		UnicodeProperties typeSettings = layout.getTypeSettingsProperties();

		String url = GetterUtil.getString(typeSettings.getProperty("url"));

		String friendlyURLPrivateGroupPath =
			PropsValues.LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING;
		String friendlyURLPrivateUserPath =
			PropsValues.LAYOUT_FRIENDLY_URL_PRIVATE_USER_SERVLET_MAPPING;
		String friendlyURLPublicPath =
			PropsValues.LAYOUT_FRIENDLY_URL_PUBLIC_SERVLET_MAPPING;

		if (!url.startsWith(friendlyURLPrivateGroupPath) &&
			!url.startsWith(friendlyURLPrivateUserPath) &&
			!url.startsWith(friendlyURLPublicPath)) {

			return;
		}

		int x = url.indexOf(CharPool.SLASH, 1);

		if (x == -1) {
			return;
		}

		int y = url.indexOf(CharPool.SLASH, x + 1);

		if (y == -1) {
			return;
		}

		String friendlyURL = url.substring(x, y);
		String groupFriendlyURL = layout.getGroup().getFriendlyURL();

		if (!friendlyURL.equals(groupFriendlyURL)) {
			return;
		}

		typeSettings.setProperty(
			"url",
			url.substring(0, x) + SAME_GROUP_FRIENDLY_URL + url.substring(y));
	}

	protected void importJournalArticle(
			PortletDataContext portletDataContext, Layout layout,
			Element layoutElement)
		throws Exception {

		UnicodeProperties typeSettingsProperties =
			layout.getTypeSettingsProperties();

		String articleId = typeSettingsProperties.getProperty(
			"article-id", StringPool.BLANK);

		if (Validator.isNull(articleId)) {
			return;
		}

		List<Element> referenceDataElements =
			portletDataContext.getReferenceDataElements(
				layoutElement, JournalArticle.class);

		if (!referenceDataElements.isEmpty()) {
			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, referenceDataElements.get(0));
		}

		Map<String, String> articleIds =
			(Map<String, String>)portletDataContext.getNewPrimaryKeysMap(
				JournalArticle.class + ".articleId");

		articleId = MapUtil.getString(articleIds, articleId, articleId);

		typeSettingsProperties.setProperty("article-id", articleId);

		JournalContentSearchLocalServiceUtil.updateContentSearch(
			portletDataContext.getScopeGroupId(), layout.isPrivateLayout(),
			layout.getLayoutId(), StringPool.BLANK, articleId, true);
	}

	protected void updateTypeSettings(Layout importedLayout, Layout layout)
		throws PortalException, SystemException {

		LayoutTypePortlet importedLayoutType =
			(LayoutTypePortlet)importedLayout.getLayoutType();

		List<String> importedPortletIds = importedLayoutType.getPortletIds();

		LayoutTypePortlet layoutType =
			(LayoutTypePortlet)layout.getLayoutType();

		importedPortletIds.removeAll(layoutType.getPortletIds());

		if (!importedPortletIds.isEmpty()) {
			PortletLocalServiceUtil.deletePortlets(
				importedLayout.getCompanyId(),
				importedPortletIds.toArray(
					new String[importedPortletIds.size()]),
				importedLayout.getPlid());
		}

		importedLayout.setTypeSettings(layout.getTypeSettings());
	}

}