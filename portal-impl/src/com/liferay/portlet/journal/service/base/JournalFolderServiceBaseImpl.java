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

package com.liferay.portlet.journal.service.base;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.BaseServiceImpl;
import com.liferay.portal.service.persistence.GroupFinder;
import com.liferay.portal.service.persistence.GroupPersistence;
import com.liferay.portal.service.persistence.UserFinder;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.service.persistence.WorkflowInstanceLinkPersistence;

import com.liferay.portlet.asset.service.persistence.AssetEntryFinder;
import com.liferay.portlet.asset.service.persistence.AssetEntryPersistence;
import com.liferay.portlet.asset.service.persistence.AssetLinkPersistence;
import com.liferay.portlet.expando.service.persistence.ExpandoValuePersistence;
import com.liferay.portlet.journal.model.JournalFolder;
import com.liferay.portlet.journal.service.JournalFolderService;
import com.liferay.portlet.journal.service.persistence.JournalArticleFinder;
import com.liferay.portlet.journal.service.persistence.JournalArticleImagePersistence;
import com.liferay.portlet.journal.service.persistence.JournalArticlePersistence;
import com.liferay.portlet.journal.service.persistence.JournalArticleResourcePersistence;
import com.liferay.portlet.journal.service.persistence.JournalContentSearchPersistence;
import com.liferay.portlet.journal.service.persistence.JournalFeedFinder;
import com.liferay.portlet.journal.service.persistence.JournalFeedPersistence;
import com.liferay.portlet.journal.service.persistence.JournalFolderFinder;
import com.liferay.portlet.journal.service.persistence.JournalFolderPersistence;
import com.liferay.portlet.social.service.persistence.SocialActivityCounterFinder;
import com.liferay.portlet.social.service.persistence.SocialActivityCounterPersistence;
import com.liferay.portlet.social.service.persistence.SocialActivityFinder;
import com.liferay.portlet.social.service.persistence.SocialActivityPersistence;
import com.liferay.portlet.trash.service.persistence.TrashEntryPersistence;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the journal folder remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portlet.journal.service.impl.JournalFolderServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portlet.journal.service.impl.JournalFolderServiceImpl
 * @see com.liferay.portlet.journal.service.JournalFolderServiceUtil
 * @generated
 */
public abstract class JournalFolderServiceBaseImpl extends BaseServiceImpl
	implements JournalFolderService, IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.portlet.journal.service.JournalFolderServiceUtil} to access the journal folder remote service.
	 */

	/**
	 * Returns the journal article local service.
	 *
	 * @return the journal article local service
	 */
	public com.liferay.portlet.journal.service.JournalArticleLocalService getJournalArticleLocalService() {
		return journalArticleLocalService;
	}

	/**
	 * Sets the journal article local service.
	 *
	 * @param journalArticleLocalService the journal article local service
	 */
	public void setJournalArticleLocalService(
		com.liferay.portlet.journal.service.JournalArticleLocalService journalArticleLocalService) {
		this.journalArticleLocalService = journalArticleLocalService;
	}

	/**
	 * Returns the journal article remote service.
	 *
	 * @return the journal article remote service
	 */
	public com.liferay.portlet.journal.service.JournalArticleService getJournalArticleService() {
		return journalArticleService;
	}

	/**
	 * Sets the journal article remote service.
	 *
	 * @param journalArticleService the journal article remote service
	 */
	public void setJournalArticleService(
		com.liferay.portlet.journal.service.JournalArticleService journalArticleService) {
		this.journalArticleService = journalArticleService;
	}

	/**
	 * Returns the journal article persistence.
	 *
	 * @return the journal article persistence
	 */
	public JournalArticlePersistence getJournalArticlePersistence() {
		return journalArticlePersistence;
	}

	/**
	 * Sets the journal article persistence.
	 *
	 * @param journalArticlePersistence the journal article persistence
	 */
	public void setJournalArticlePersistence(
		JournalArticlePersistence journalArticlePersistence) {
		this.journalArticlePersistence = journalArticlePersistence;
	}

	/**
	 * Returns the journal article finder.
	 *
	 * @return the journal article finder
	 */
	public JournalArticleFinder getJournalArticleFinder() {
		return journalArticleFinder;
	}

	/**
	 * Sets the journal article finder.
	 *
	 * @param journalArticleFinder the journal article finder
	 */
	public void setJournalArticleFinder(
		JournalArticleFinder journalArticleFinder) {
		this.journalArticleFinder = journalArticleFinder;
	}

	/**
	 * Returns the journal article image local service.
	 *
	 * @return the journal article image local service
	 */
	public com.liferay.portlet.journal.service.JournalArticleImageLocalService getJournalArticleImageLocalService() {
		return journalArticleImageLocalService;
	}

	/**
	 * Sets the journal article image local service.
	 *
	 * @param journalArticleImageLocalService the journal article image local service
	 */
	public void setJournalArticleImageLocalService(
		com.liferay.portlet.journal.service.JournalArticleImageLocalService journalArticleImageLocalService) {
		this.journalArticleImageLocalService = journalArticleImageLocalService;
	}

	/**
	 * Returns the journal article image persistence.
	 *
	 * @return the journal article image persistence
	 */
	public JournalArticleImagePersistence getJournalArticleImagePersistence() {
		return journalArticleImagePersistence;
	}

	/**
	 * Sets the journal article image persistence.
	 *
	 * @param journalArticleImagePersistence the journal article image persistence
	 */
	public void setJournalArticleImagePersistence(
		JournalArticleImagePersistence journalArticleImagePersistence) {
		this.journalArticleImagePersistence = journalArticleImagePersistence;
	}

	/**
	 * Returns the journal article resource local service.
	 *
	 * @return the journal article resource local service
	 */
	public com.liferay.portlet.journal.service.JournalArticleResourceLocalService getJournalArticleResourceLocalService() {
		return journalArticleResourceLocalService;
	}

	/**
	 * Sets the journal article resource local service.
	 *
	 * @param journalArticleResourceLocalService the journal article resource local service
	 */
	public void setJournalArticleResourceLocalService(
		com.liferay.portlet.journal.service.JournalArticleResourceLocalService journalArticleResourceLocalService) {
		this.journalArticleResourceLocalService = journalArticleResourceLocalService;
	}

	/**
	 * Returns the journal article resource persistence.
	 *
	 * @return the journal article resource persistence
	 */
	public JournalArticleResourcePersistence getJournalArticleResourcePersistence() {
		return journalArticleResourcePersistence;
	}

	/**
	 * Sets the journal article resource persistence.
	 *
	 * @param journalArticleResourcePersistence the journal article resource persistence
	 */
	public void setJournalArticleResourcePersistence(
		JournalArticleResourcePersistence journalArticleResourcePersistence) {
		this.journalArticleResourcePersistence = journalArticleResourcePersistence;
	}

	/**
	 * Returns the journal content search local service.
	 *
	 * @return the journal content search local service
	 */
	public com.liferay.portlet.journal.service.JournalContentSearchLocalService getJournalContentSearchLocalService() {
		return journalContentSearchLocalService;
	}

	/**
	 * Sets the journal content search local service.
	 *
	 * @param journalContentSearchLocalService the journal content search local service
	 */
	public void setJournalContentSearchLocalService(
		com.liferay.portlet.journal.service.JournalContentSearchLocalService journalContentSearchLocalService) {
		this.journalContentSearchLocalService = journalContentSearchLocalService;
	}

	/**
	 * Returns the journal content search persistence.
	 *
	 * @return the journal content search persistence
	 */
	public JournalContentSearchPersistence getJournalContentSearchPersistence() {
		return journalContentSearchPersistence;
	}

	/**
	 * Sets the journal content search persistence.
	 *
	 * @param journalContentSearchPersistence the journal content search persistence
	 */
	public void setJournalContentSearchPersistence(
		JournalContentSearchPersistence journalContentSearchPersistence) {
		this.journalContentSearchPersistence = journalContentSearchPersistence;
	}

	/**
	 * Returns the journal feed local service.
	 *
	 * @return the journal feed local service
	 */
	public com.liferay.portlet.journal.service.JournalFeedLocalService getJournalFeedLocalService() {
		return journalFeedLocalService;
	}

	/**
	 * Sets the journal feed local service.
	 *
	 * @param journalFeedLocalService the journal feed local service
	 */
	public void setJournalFeedLocalService(
		com.liferay.portlet.journal.service.JournalFeedLocalService journalFeedLocalService) {
		this.journalFeedLocalService = journalFeedLocalService;
	}

	/**
	 * Returns the journal feed remote service.
	 *
	 * @return the journal feed remote service
	 */
	public com.liferay.portlet.journal.service.JournalFeedService getJournalFeedService() {
		return journalFeedService;
	}

	/**
	 * Sets the journal feed remote service.
	 *
	 * @param journalFeedService the journal feed remote service
	 */
	public void setJournalFeedService(
		com.liferay.portlet.journal.service.JournalFeedService journalFeedService) {
		this.journalFeedService = journalFeedService;
	}

	/**
	 * Returns the journal feed persistence.
	 *
	 * @return the journal feed persistence
	 */
	public JournalFeedPersistence getJournalFeedPersistence() {
		return journalFeedPersistence;
	}

	/**
	 * Sets the journal feed persistence.
	 *
	 * @param journalFeedPersistence the journal feed persistence
	 */
	public void setJournalFeedPersistence(
		JournalFeedPersistence journalFeedPersistence) {
		this.journalFeedPersistence = journalFeedPersistence;
	}

	/**
	 * Returns the journal feed finder.
	 *
	 * @return the journal feed finder
	 */
	public JournalFeedFinder getJournalFeedFinder() {
		return journalFeedFinder;
	}

	/**
	 * Sets the journal feed finder.
	 *
	 * @param journalFeedFinder the journal feed finder
	 */
	public void setJournalFeedFinder(JournalFeedFinder journalFeedFinder) {
		this.journalFeedFinder = journalFeedFinder;
	}

	/**
	 * Returns the journal folder local service.
	 *
	 * @return the journal folder local service
	 */
	public com.liferay.portlet.journal.service.JournalFolderLocalService getJournalFolderLocalService() {
		return journalFolderLocalService;
	}

	/**
	 * Sets the journal folder local service.
	 *
	 * @param journalFolderLocalService the journal folder local service
	 */
	public void setJournalFolderLocalService(
		com.liferay.portlet.journal.service.JournalFolderLocalService journalFolderLocalService) {
		this.journalFolderLocalService = journalFolderLocalService;
	}

	/**
	 * Returns the journal folder remote service.
	 *
	 * @return the journal folder remote service
	 */
	public com.liferay.portlet.journal.service.JournalFolderService getJournalFolderService() {
		return journalFolderService;
	}

	/**
	 * Sets the journal folder remote service.
	 *
	 * @param journalFolderService the journal folder remote service
	 */
	public void setJournalFolderService(
		com.liferay.portlet.journal.service.JournalFolderService journalFolderService) {
		this.journalFolderService = journalFolderService;
	}

	/**
	 * Returns the journal folder persistence.
	 *
	 * @return the journal folder persistence
	 */
	public JournalFolderPersistence getJournalFolderPersistence() {
		return journalFolderPersistence;
	}

	/**
	 * Sets the journal folder persistence.
	 *
	 * @param journalFolderPersistence the journal folder persistence
	 */
	public void setJournalFolderPersistence(
		JournalFolderPersistence journalFolderPersistence) {
		this.journalFolderPersistence = journalFolderPersistence;
	}

	/**
	 * Returns the journal folder finder.
	 *
	 * @return the journal folder finder
	 */
	public JournalFolderFinder getJournalFolderFinder() {
		return journalFolderFinder;
	}

	/**
	 * Sets the journal folder finder.
	 *
	 * @param journalFolderFinder the journal folder finder
	 */
	public void setJournalFolderFinder(JournalFolderFinder journalFolderFinder) {
		this.journalFolderFinder = journalFolderFinder;
	}

	/**
	 * Returns the journal structure local service.
	 *
	 * @return the journal structure local service
	 */
	@SuppressWarnings("deprecation")
	public com.liferay.portlet.journal.service.JournalStructureLocalService getJournalStructureLocalService() {
		return journalStructureLocalService;
	}

	/**
	 * Sets the journal structure local service.
	 *
	 * @param journalStructureLocalService the journal structure local service
	 */
	@SuppressWarnings("deprecation")
	public void setJournalStructureLocalService(
		com.liferay.portlet.journal.service.JournalStructureLocalService journalStructureLocalService) {
		this.journalStructureLocalService = journalStructureLocalService;
	}

	/**
	 * Returns the journal structure remote service.
	 *
	 * @return the journal structure remote service
	 */
	@SuppressWarnings("deprecation")
	public com.liferay.portlet.journal.service.JournalStructureService getJournalStructureService() {
		return journalStructureService;
	}

	/**
	 * Sets the journal structure remote service.
	 *
	 * @param journalStructureService the journal structure remote service
	 */
	@SuppressWarnings("deprecation")
	public void setJournalStructureService(
		com.liferay.portlet.journal.service.JournalStructureService journalStructureService) {
		this.journalStructureService = journalStructureService;
	}

	/**
	 * Returns the journal template local service.
	 *
	 * @return the journal template local service
	 */
	@SuppressWarnings("deprecation")
	public com.liferay.portlet.journal.service.JournalTemplateLocalService getJournalTemplateLocalService() {
		return journalTemplateLocalService;
	}

	/**
	 * Sets the journal template local service.
	 *
	 * @param journalTemplateLocalService the journal template local service
	 */
	@SuppressWarnings("deprecation")
	public void setJournalTemplateLocalService(
		com.liferay.portlet.journal.service.JournalTemplateLocalService journalTemplateLocalService) {
		this.journalTemplateLocalService = journalTemplateLocalService;
	}

	/**
	 * Returns the journal template remote service.
	 *
	 * @return the journal template remote service
	 */
	@SuppressWarnings("deprecation")
	public com.liferay.portlet.journal.service.JournalTemplateService getJournalTemplateService() {
		return journalTemplateService;
	}

	/**
	 * Sets the journal template remote service.
	 *
	 * @param journalTemplateService the journal template remote service
	 */
	@SuppressWarnings("deprecation")
	public void setJournalTemplateService(
		com.liferay.portlet.journal.service.JournalTemplateService journalTemplateService) {
		this.journalTemplateService = journalTemplateService;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the group local service.
	 *
	 * @return the group local service
	 */
	public com.liferay.portal.service.GroupLocalService getGroupLocalService() {
		return groupLocalService;
	}

	/**
	 * Sets the group local service.
	 *
	 * @param groupLocalService the group local service
	 */
	public void setGroupLocalService(
		com.liferay.portal.service.GroupLocalService groupLocalService) {
		this.groupLocalService = groupLocalService;
	}

	/**
	 * Returns the group remote service.
	 *
	 * @return the group remote service
	 */
	public com.liferay.portal.service.GroupService getGroupService() {
		return groupService;
	}

	/**
	 * Sets the group remote service.
	 *
	 * @param groupService the group remote service
	 */
	public void setGroupService(
		com.liferay.portal.service.GroupService groupService) {
		this.groupService = groupService;
	}

	/**
	 * Returns the group persistence.
	 *
	 * @return the group persistence
	 */
	public GroupPersistence getGroupPersistence() {
		return groupPersistence;
	}

	/**
	 * Sets the group persistence.
	 *
	 * @param groupPersistence the group persistence
	 */
	public void setGroupPersistence(GroupPersistence groupPersistence) {
		this.groupPersistence = groupPersistence;
	}

	/**
	 * Returns the group finder.
	 *
	 * @return the group finder
	 */
	public GroupFinder getGroupFinder() {
		return groupFinder;
	}

	/**
	 * Sets the group finder.
	 *
	 * @param groupFinder the group finder
	 */
	public void setGroupFinder(GroupFinder groupFinder) {
		this.groupFinder = groupFinder;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public com.liferay.portal.service.UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(
		com.liferay.portal.service.UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	/**
	 * Returns the user finder.
	 *
	 * @return the user finder
	 */
	public UserFinder getUserFinder() {
		return userFinder;
	}

	/**
	 * Sets the user finder.
	 *
	 * @param userFinder the user finder
	 */
	public void setUserFinder(UserFinder userFinder) {
		this.userFinder = userFinder;
	}

	/**
	 * Returns the workflow instance link local service.
	 *
	 * @return the workflow instance link local service
	 */
	public com.liferay.portal.service.WorkflowInstanceLinkLocalService getWorkflowInstanceLinkLocalService() {
		return workflowInstanceLinkLocalService;
	}

	/**
	 * Sets the workflow instance link local service.
	 *
	 * @param workflowInstanceLinkLocalService the workflow instance link local service
	 */
	public void setWorkflowInstanceLinkLocalService(
		com.liferay.portal.service.WorkflowInstanceLinkLocalService workflowInstanceLinkLocalService) {
		this.workflowInstanceLinkLocalService = workflowInstanceLinkLocalService;
	}

	/**
	 * Returns the workflow instance link persistence.
	 *
	 * @return the workflow instance link persistence
	 */
	public WorkflowInstanceLinkPersistence getWorkflowInstanceLinkPersistence() {
		return workflowInstanceLinkPersistence;
	}

	/**
	 * Sets the workflow instance link persistence.
	 *
	 * @param workflowInstanceLinkPersistence the workflow instance link persistence
	 */
	public void setWorkflowInstanceLinkPersistence(
		WorkflowInstanceLinkPersistence workflowInstanceLinkPersistence) {
		this.workflowInstanceLinkPersistence = workflowInstanceLinkPersistence;
	}

	/**
	 * Returns the asset entry local service.
	 *
	 * @return the asset entry local service
	 */
	public com.liferay.portlet.asset.service.AssetEntryLocalService getAssetEntryLocalService() {
		return assetEntryLocalService;
	}

	/**
	 * Sets the asset entry local service.
	 *
	 * @param assetEntryLocalService the asset entry local service
	 */
	public void setAssetEntryLocalService(
		com.liferay.portlet.asset.service.AssetEntryLocalService assetEntryLocalService) {
		this.assetEntryLocalService = assetEntryLocalService;
	}

	/**
	 * Returns the asset entry remote service.
	 *
	 * @return the asset entry remote service
	 */
	public com.liferay.portlet.asset.service.AssetEntryService getAssetEntryService() {
		return assetEntryService;
	}

	/**
	 * Sets the asset entry remote service.
	 *
	 * @param assetEntryService the asset entry remote service
	 */
	public void setAssetEntryService(
		com.liferay.portlet.asset.service.AssetEntryService assetEntryService) {
		this.assetEntryService = assetEntryService;
	}

	/**
	 * Returns the asset entry persistence.
	 *
	 * @return the asset entry persistence
	 */
	public AssetEntryPersistence getAssetEntryPersistence() {
		return assetEntryPersistence;
	}

	/**
	 * Sets the asset entry persistence.
	 *
	 * @param assetEntryPersistence the asset entry persistence
	 */
	public void setAssetEntryPersistence(
		AssetEntryPersistence assetEntryPersistence) {
		this.assetEntryPersistence = assetEntryPersistence;
	}

	/**
	 * Returns the asset entry finder.
	 *
	 * @return the asset entry finder
	 */
	public AssetEntryFinder getAssetEntryFinder() {
		return assetEntryFinder;
	}

	/**
	 * Sets the asset entry finder.
	 *
	 * @param assetEntryFinder the asset entry finder
	 */
	public void setAssetEntryFinder(AssetEntryFinder assetEntryFinder) {
		this.assetEntryFinder = assetEntryFinder;
	}

	/**
	 * Returns the asset link local service.
	 *
	 * @return the asset link local service
	 */
	public com.liferay.portlet.asset.service.AssetLinkLocalService getAssetLinkLocalService() {
		return assetLinkLocalService;
	}

	/**
	 * Sets the asset link local service.
	 *
	 * @param assetLinkLocalService the asset link local service
	 */
	public void setAssetLinkLocalService(
		com.liferay.portlet.asset.service.AssetLinkLocalService assetLinkLocalService) {
		this.assetLinkLocalService = assetLinkLocalService;
	}

	/**
	 * Returns the asset link persistence.
	 *
	 * @return the asset link persistence
	 */
	public AssetLinkPersistence getAssetLinkPersistence() {
		return assetLinkPersistence;
	}

	/**
	 * Sets the asset link persistence.
	 *
	 * @param assetLinkPersistence the asset link persistence
	 */
	public void setAssetLinkPersistence(
		AssetLinkPersistence assetLinkPersistence) {
		this.assetLinkPersistence = assetLinkPersistence;
	}

	/**
	 * Returns the expando value local service.
	 *
	 * @return the expando value local service
	 */
	public com.liferay.portlet.expando.service.ExpandoValueLocalService getExpandoValueLocalService() {
		return expandoValueLocalService;
	}

	/**
	 * Sets the expando value local service.
	 *
	 * @param expandoValueLocalService the expando value local service
	 */
	public void setExpandoValueLocalService(
		com.liferay.portlet.expando.service.ExpandoValueLocalService expandoValueLocalService) {
		this.expandoValueLocalService = expandoValueLocalService;
	}

	/**
	 * Returns the expando value remote service.
	 *
	 * @return the expando value remote service
	 */
	public com.liferay.portlet.expando.service.ExpandoValueService getExpandoValueService() {
		return expandoValueService;
	}

	/**
	 * Sets the expando value remote service.
	 *
	 * @param expandoValueService the expando value remote service
	 */
	public void setExpandoValueService(
		com.liferay.portlet.expando.service.ExpandoValueService expandoValueService) {
		this.expandoValueService = expandoValueService;
	}

	/**
	 * Returns the expando value persistence.
	 *
	 * @return the expando value persistence
	 */
	public ExpandoValuePersistence getExpandoValuePersistence() {
		return expandoValuePersistence;
	}

	/**
	 * Sets the expando value persistence.
	 *
	 * @param expandoValuePersistence the expando value persistence
	 */
	public void setExpandoValuePersistence(
		ExpandoValuePersistence expandoValuePersistence) {
		this.expandoValuePersistence = expandoValuePersistence;
	}

	/**
	 * Returns the social activity local service.
	 *
	 * @return the social activity local service
	 */
	public com.liferay.portlet.social.service.SocialActivityLocalService getSocialActivityLocalService() {
		return socialActivityLocalService;
	}

	/**
	 * Sets the social activity local service.
	 *
	 * @param socialActivityLocalService the social activity local service
	 */
	public void setSocialActivityLocalService(
		com.liferay.portlet.social.service.SocialActivityLocalService socialActivityLocalService) {
		this.socialActivityLocalService = socialActivityLocalService;
	}

	/**
	 * Returns the social activity persistence.
	 *
	 * @return the social activity persistence
	 */
	public SocialActivityPersistence getSocialActivityPersistence() {
		return socialActivityPersistence;
	}

	/**
	 * Sets the social activity persistence.
	 *
	 * @param socialActivityPersistence the social activity persistence
	 */
	public void setSocialActivityPersistence(
		SocialActivityPersistence socialActivityPersistence) {
		this.socialActivityPersistence = socialActivityPersistence;
	}

	/**
	 * Returns the social activity finder.
	 *
	 * @return the social activity finder
	 */
	public SocialActivityFinder getSocialActivityFinder() {
		return socialActivityFinder;
	}

	/**
	 * Sets the social activity finder.
	 *
	 * @param socialActivityFinder the social activity finder
	 */
	public void setSocialActivityFinder(
		SocialActivityFinder socialActivityFinder) {
		this.socialActivityFinder = socialActivityFinder;
	}

	/**
	 * Returns the social activity counter local service.
	 *
	 * @return the social activity counter local service
	 */
	public com.liferay.portlet.social.service.SocialActivityCounterLocalService getSocialActivityCounterLocalService() {
		return socialActivityCounterLocalService;
	}

	/**
	 * Sets the social activity counter local service.
	 *
	 * @param socialActivityCounterLocalService the social activity counter local service
	 */
	public void setSocialActivityCounterLocalService(
		com.liferay.portlet.social.service.SocialActivityCounterLocalService socialActivityCounterLocalService) {
		this.socialActivityCounterLocalService = socialActivityCounterLocalService;
	}

	/**
	 * Returns the social activity counter persistence.
	 *
	 * @return the social activity counter persistence
	 */
	public SocialActivityCounterPersistence getSocialActivityCounterPersistence() {
		return socialActivityCounterPersistence;
	}

	/**
	 * Sets the social activity counter persistence.
	 *
	 * @param socialActivityCounterPersistence the social activity counter persistence
	 */
	public void setSocialActivityCounterPersistence(
		SocialActivityCounterPersistence socialActivityCounterPersistence) {
		this.socialActivityCounterPersistence = socialActivityCounterPersistence;
	}

	/**
	 * Returns the social activity counter finder.
	 *
	 * @return the social activity counter finder
	 */
	public SocialActivityCounterFinder getSocialActivityCounterFinder() {
		return socialActivityCounterFinder;
	}

	/**
	 * Sets the social activity counter finder.
	 *
	 * @param socialActivityCounterFinder the social activity counter finder
	 */
	public void setSocialActivityCounterFinder(
		SocialActivityCounterFinder socialActivityCounterFinder) {
		this.socialActivityCounterFinder = socialActivityCounterFinder;
	}

	/**
	 * Returns the trash entry local service.
	 *
	 * @return the trash entry local service
	 */
	public com.liferay.portlet.trash.service.TrashEntryLocalService getTrashEntryLocalService() {
		return trashEntryLocalService;
	}

	/**
	 * Sets the trash entry local service.
	 *
	 * @param trashEntryLocalService the trash entry local service
	 */
	public void setTrashEntryLocalService(
		com.liferay.portlet.trash.service.TrashEntryLocalService trashEntryLocalService) {
		this.trashEntryLocalService = trashEntryLocalService;
	}

	/**
	 * Returns the trash entry remote service.
	 *
	 * @return the trash entry remote service
	 */
	public com.liferay.portlet.trash.service.TrashEntryService getTrashEntryService() {
		return trashEntryService;
	}

	/**
	 * Sets the trash entry remote service.
	 *
	 * @param trashEntryService the trash entry remote service
	 */
	public void setTrashEntryService(
		com.liferay.portlet.trash.service.TrashEntryService trashEntryService) {
		this.trashEntryService = trashEntryService;
	}

	/**
	 * Returns the trash entry persistence.
	 *
	 * @return the trash entry persistence
	 */
	public TrashEntryPersistence getTrashEntryPersistence() {
		return trashEntryPersistence;
	}

	/**
	 * Sets the trash entry persistence.
	 *
	 * @param trashEntryPersistence the trash entry persistence
	 */
	public void setTrashEntryPersistence(
		TrashEntryPersistence trashEntryPersistence) {
		this.trashEntryPersistence = trashEntryPersistence;
	}

	public void afterPropertiesSet() {
	}

	public void destroy() {
	}

	/**
	 * Returns the Spring bean ID for this bean.
	 *
	 * @return the Spring bean ID for this bean
	 */
	@Override
	public String getBeanIdentifier() {
		return _beanIdentifier;
	}

	/**
	 * Sets the Spring bean ID for this bean.
	 *
	 * @param beanIdentifier the Spring bean ID for this bean
	 */
	@Override
	public void setBeanIdentifier(String beanIdentifier) {
		_beanIdentifier = beanIdentifier;
	}

	protected Class<?> getModelClass() {
		return JournalFolder.class;
	}

	protected String getModelClassName() {
		return JournalFolder.class.getName();
	}

	/**
	 * Performs an SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) throws SystemException {
		try {
			DataSource dataSource = journalFolderPersistence.getDataSource();

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql, new int[0]);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = com.liferay.portlet.journal.service.JournalArticleLocalService.class)
	protected com.liferay.portlet.journal.service.JournalArticleLocalService journalArticleLocalService;
	@BeanReference(type = com.liferay.portlet.journal.service.JournalArticleService.class)
	protected com.liferay.portlet.journal.service.JournalArticleService journalArticleService;
	@BeanReference(type = JournalArticlePersistence.class)
	protected JournalArticlePersistence journalArticlePersistence;
	@BeanReference(type = JournalArticleFinder.class)
	protected JournalArticleFinder journalArticleFinder;
	@BeanReference(type = com.liferay.portlet.journal.service.JournalArticleImageLocalService.class)
	protected com.liferay.portlet.journal.service.JournalArticleImageLocalService journalArticleImageLocalService;
	@BeanReference(type = JournalArticleImagePersistence.class)
	protected JournalArticleImagePersistence journalArticleImagePersistence;
	@BeanReference(type = com.liferay.portlet.journal.service.JournalArticleResourceLocalService.class)
	protected com.liferay.portlet.journal.service.JournalArticleResourceLocalService journalArticleResourceLocalService;
	@BeanReference(type = JournalArticleResourcePersistence.class)
	protected JournalArticleResourcePersistence journalArticleResourcePersistence;
	@BeanReference(type = com.liferay.portlet.journal.service.JournalContentSearchLocalService.class)
	protected com.liferay.portlet.journal.service.JournalContentSearchLocalService journalContentSearchLocalService;
	@BeanReference(type = JournalContentSearchPersistence.class)
	protected JournalContentSearchPersistence journalContentSearchPersistence;
	@BeanReference(type = com.liferay.portlet.journal.service.JournalFeedLocalService.class)
	protected com.liferay.portlet.journal.service.JournalFeedLocalService journalFeedLocalService;
	@BeanReference(type = com.liferay.portlet.journal.service.JournalFeedService.class)
	protected com.liferay.portlet.journal.service.JournalFeedService journalFeedService;
	@BeanReference(type = JournalFeedPersistence.class)
	protected JournalFeedPersistence journalFeedPersistence;
	@BeanReference(type = JournalFeedFinder.class)
	protected JournalFeedFinder journalFeedFinder;
	@BeanReference(type = com.liferay.portlet.journal.service.JournalFolderLocalService.class)
	protected com.liferay.portlet.journal.service.JournalFolderLocalService journalFolderLocalService;
	@BeanReference(type = com.liferay.portlet.journal.service.JournalFolderService.class)
	protected com.liferay.portlet.journal.service.JournalFolderService journalFolderService;
	@BeanReference(type = JournalFolderPersistence.class)
	protected JournalFolderPersistence journalFolderPersistence;
	@BeanReference(type = JournalFolderFinder.class)
	protected JournalFolderFinder journalFolderFinder;
	@BeanReference(type = com.liferay.portlet.journal.service.JournalStructureLocalService.class)
	@SuppressWarnings("deprecation")
	protected com.liferay.portlet.journal.service.JournalStructureLocalService journalStructureLocalService;
	@BeanReference(type = com.liferay.portlet.journal.service.JournalStructureService.class)
	@SuppressWarnings("deprecation")
	protected com.liferay.portlet.journal.service.JournalStructureService journalStructureService;
	@BeanReference(type = com.liferay.portlet.journal.service.JournalTemplateLocalService.class)
	@SuppressWarnings("deprecation")
	protected com.liferay.portlet.journal.service.JournalTemplateLocalService journalTemplateLocalService;
	@BeanReference(type = com.liferay.portlet.journal.service.JournalTemplateService.class)
	@SuppressWarnings("deprecation")
	protected com.liferay.portlet.journal.service.JournalTemplateService journalTemplateService;
	@BeanReference(type = com.liferay.counter.service.CounterLocalService.class)
	protected com.liferay.counter.service.CounterLocalService counterLocalService;
	@BeanReference(type = com.liferay.portal.service.GroupLocalService.class)
	protected com.liferay.portal.service.GroupLocalService groupLocalService;
	@BeanReference(type = com.liferay.portal.service.GroupService.class)
	protected com.liferay.portal.service.GroupService groupService;
	@BeanReference(type = GroupPersistence.class)
	protected GroupPersistence groupPersistence;
	@BeanReference(type = GroupFinder.class)
	protected GroupFinder groupFinder;
	@BeanReference(type = com.liferay.portal.service.ResourceLocalService.class)
	protected com.liferay.portal.service.ResourceLocalService resourceLocalService;
	@BeanReference(type = com.liferay.portal.service.UserLocalService.class)
	protected com.liferay.portal.service.UserLocalService userLocalService;
	@BeanReference(type = com.liferay.portal.service.UserService.class)
	protected com.liferay.portal.service.UserService userService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	@BeanReference(type = UserFinder.class)
	protected UserFinder userFinder;
	@BeanReference(type = com.liferay.portal.service.WorkflowInstanceLinkLocalService.class)
	protected com.liferay.portal.service.WorkflowInstanceLinkLocalService workflowInstanceLinkLocalService;
	@BeanReference(type = WorkflowInstanceLinkPersistence.class)
	protected WorkflowInstanceLinkPersistence workflowInstanceLinkPersistence;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetEntryLocalService.class)
	protected com.liferay.portlet.asset.service.AssetEntryLocalService assetEntryLocalService;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetEntryService.class)
	protected com.liferay.portlet.asset.service.AssetEntryService assetEntryService;
	@BeanReference(type = AssetEntryPersistence.class)
	protected AssetEntryPersistence assetEntryPersistence;
	@BeanReference(type = AssetEntryFinder.class)
	protected AssetEntryFinder assetEntryFinder;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetLinkLocalService.class)
	protected com.liferay.portlet.asset.service.AssetLinkLocalService assetLinkLocalService;
	@BeanReference(type = AssetLinkPersistence.class)
	protected AssetLinkPersistence assetLinkPersistence;
	@BeanReference(type = com.liferay.portlet.expando.service.ExpandoValueLocalService.class)
	protected com.liferay.portlet.expando.service.ExpandoValueLocalService expandoValueLocalService;
	@BeanReference(type = com.liferay.portlet.expando.service.ExpandoValueService.class)
	protected com.liferay.portlet.expando.service.ExpandoValueService expandoValueService;
	@BeanReference(type = ExpandoValuePersistence.class)
	protected ExpandoValuePersistence expandoValuePersistence;
	@BeanReference(type = com.liferay.portlet.social.service.SocialActivityLocalService.class)
	protected com.liferay.portlet.social.service.SocialActivityLocalService socialActivityLocalService;
	@BeanReference(type = SocialActivityPersistence.class)
	protected SocialActivityPersistence socialActivityPersistence;
	@BeanReference(type = SocialActivityFinder.class)
	protected SocialActivityFinder socialActivityFinder;
	@BeanReference(type = com.liferay.portlet.social.service.SocialActivityCounterLocalService.class)
	protected com.liferay.portlet.social.service.SocialActivityCounterLocalService socialActivityCounterLocalService;
	@BeanReference(type = SocialActivityCounterPersistence.class)
	protected SocialActivityCounterPersistence socialActivityCounterPersistence;
	@BeanReference(type = SocialActivityCounterFinder.class)
	protected SocialActivityCounterFinder socialActivityCounterFinder;
	@BeanReference(type = com.liferay.portlet.trash.service.TrashEntryLocalService.class)
	protected com.liferay.portlet.trash.service.TrashEntryLocalService trashEntryLocalService;
	@BeanReference(type = com.liferay.portlet.trash.service.TrashEntryService.class)
	protected com.liferay.portlet.trash.service.TrashEntryService trashEntryService;
	@BeanReference(type = TrashEntryPersistence.class)
	protected TrashEntryPersistence trashEntryPersistence;
	private String _beanIdentifier;
}