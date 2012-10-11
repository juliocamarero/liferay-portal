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

package com.liferay.portal.upgrade.v6_2_0;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.repository.liferayrepository.LiferayRepository;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.documentlibrary.NoSuchDirectoryException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.store.DLStoreUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.Date;

/**
 * @author Eudaldo Alonso
 */
public class UpgradeWikiAttachments extends UpgradeProcess {

	protected long addDLFileEntry(
			long groupId, long companyId, long userId, String userName,
			Timestamp createDate, long repositoryId, long folderId, String name,
			String extension, String mimeType, String title, long size)
		throws Exception {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			long fileEntryId = increment();

			con = DataAccess.getUpgradeOptimizedConnection();

			StringBundler sb = new StringBundler(9);

			sb.append("insert into DLFileEntry (uuid_, fileEntryId, groupId, ");
			sb.append("companyId, userId, userName, versionUserId, ");
			sb.append("versionUserName, createDate, modifiedDate, ");
			sb.append("repositoryId, folderId, name, extension, mimeType, ");
			sb.append("title, description, extraSettings, fileEntryTypeId, ");
			sb.append("version, size_, readCount, smallImageId, ");
			sb.append("largeImageId, custom1ImageId, custom2ImageId) values (");
			sb.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
			sb.append("?, ?, ?, ?, ?, ?, ?, ?, ?)");

			String sql = sb.toString();

			ps = con.prepareStatement(sql);

			ps.setString(1, PortalUUIDUtil.generate());
			ps.setLong(2, fileEntryId);
			ps.setLong(3, groupId);
			ps.setLong(4, companyId);
			ps.setLong(5, userId);
			ps.setString(6, userName);
			ps.setLong(7, userId);
			ps.setString(8, userName);
			ps.setTimestamp(9, createDate);
			ps.setTimestamp(10, createDate);
			ps.setLong(11, repositoryId);
			ps.setLong(12, folderId);
			ps.setString(13, name);
			ps.setString(14, extension);
			ps.setString(15, mimeType);
			ps.setString(16, title);
			ps.setString(17, StringPool.BLANK);
			ps.setString(18, StringPool.BLANK);
			ps.setLong(19, 0);
			ps.setString(20, "1.0");
			ps.setLong(21, size);
			ps.setInt(22, 0);
			ps.setLong(23, 0);
			ps.setLong(24, 0);
			ps.setLong(25, 0);
			ps.setLong(26, 0);

			ps.executeUpdate();

			return fileEntryId;
		}
		finally {
			DataAccess.cleanUp(con, ps);
		}
	}

	protected void addDLFileVersion(
			long fileVersionId, long groupId, long companyId, long userId,
			String userName, Timestamp createDate, long repositoryId,
			long folderId, long fileEntryId, String extension, String mimeType,
			String title, long size)
		throws Exception {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			StringBundler sb = new StringBundler(8);

			sb.append("insert into DLFileVersion (fileVersionId, groupId, ");
			sb.append("companyId, userId, userName, createDate, ");
			sb.append("modifiedDate, repositoryId, folderId, fileEntryId, ");
			sb.append("extension, mimeType, title, description, changeLog, ");
			sb.append("extraSettings, fileEntryTypeId, version, size_, ");
			sb.append("status, statusByUserId, statusByUserName, statusDate) ");
			sb.append("values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
			sb.append("?, ?, ?, ?, ?, ?, ?, ?)");

			String sql = sb.toString();

			ps = con.prepareStatement(sql);

			ps.setLong(1, fileVersionId);
			ps.setLong(2, groupId);
			ps.setLong(3, companyId);
			ps.setLong(4, userId);
			ps.setString(5, userName);
			ps.setTimestamp(6, createDate);
			ps.setTimestamp(7, createDate);
			ps.setLong(8, repositoryId);
			ps.setLong(9, folderId);
			ps.setLong(10, fileEntryId);
			ps.setString(11, extension);
			ps.setString(12, mimeType);
			ps.setString(13, title);
			ps.setString(14, StringPool.BLANK);
			ps.setString(15, StringPool.BLANK);
			ps.setString(16, StringPool.BLANK);
			ps.setLong(17, 0);
			ps.setString(18, "1.0");
			ps.setLong(19, size);
			ps.setInt(20, 0);
			ps.setLong(21, userId);
			ps.setString(22, userName);
			ps.setTimestamp(23, createDate);

			ps.executeUpdate();
		}
		finally {
			DataAccess.cleanUp(con, ps);
		}
	}

	protected long addDLFolder(
			long groupId, long companyId, long userId, String userName,
			Timestamp createDate, long repositoryId, long parentFolderId,
			String name, boolean hidden)
		throws Exception {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			long folderId = increment();

			con = DataAccess.getUpgradeOptimizedConnection();

			StringBundler sb = new StringBundler(8);

			sb.append("insert into DLFolder (uuid_, folderId, groupId, ");
			sb.append("companyId, userId, userName, createDate, ");
			sb.append("modifiedDate, repositoryId, mountPoint, ");
			sb.append("parentFolderId, name, description, lastPostDate, ");
			sb.append("defaultFileEntryTypeId, hidden_, ");
			sb.append("overrideFileEntryTypes, status, statusByUserId, ");
			sb.append("statusByUserName, statusDate) values (?, ?, ?, ?, ?, ");
			sb.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			String sql = sb.toString();

			ps = con.prepareStatement(sql);

			ps.setString(1, PortalUUIDUtil.generate());
			ps.setLong(2, folderId);
			ps.setLong(3, groupId);
			ps.setLong(4, companyId);
			ps.setLong(5, userId);
			ps.setString(6, userName);
			ps.setTimestamp(7, createDate);
			ps.setTimestamp(8, createDate);
			ps.setLong(9, repositoryId);
			ps.setBoolean(10, false);
			ps.setLong(11, parentFolderId);
			ps.setString(12, name);
			ps.setString(13, StringPool.BLANK);
			ps.setTimestamp(14, createDate);
			ps.setLong(15, 0);
			ps.setBoolean(16, hidden);
			ps.setBoolean(17, false);
			ps.setLong(18, 0);
			ps.setLong(19, userId);
			ps.setString(20, userName);
			ps.setTimestamp(21, createDate);

			ps.executeUpdate();

			return folderId;
		}
		finally {
			DataAccess.cleanUp(con, ps);
		}
	}

	protected long addRepository(
			long groupId, long companyId, long userId, String userName,
			Timestamp createDate, long classNameId, String name,
			String portletId)
		throws Exception {

		long repositoryId = increment();

		long folderId = addDLFolder(
			groupId, companyId, userId, userName, createDate, repositoryId,
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, name, true);

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			StringBundler sb = new StringBundler(8);

			sb.append("insert into Repository (uuid_, repositoryId, groupId, ");
			sb.append("companyId, userId, userName, createDate, ");
			sb.append("modifiedDate, classNameId, name, description, ");
			sb.append("portletId, typeSettings, dlFolderId) values (?, ?, ?, ");
			sb.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			ps = con.prepareStatement(sb.toString());

			ps.setString(1, PortalUUIDUtil.generate());
			ps.setLong(2, repositoryId);
			ps.setLong(3, groupId);
			ps.setLong(4, companyId);
			ps.setLong(5, userId);
			ps.setString(6, userName);
			ps.setTimestamp(7, createDate);
			ps.setTimestamp(8, createDate);
			ps.setLong(9, classNameId);
			ps.setString(10, name);
			ps.setString(11, StringPool.BLANK);
			ps.setString(12, portletId);
			ps.setString(13, StringPool.BLANK);
			ps.setLong(14, folderId);

			ps.executeUpdate();

			return repositoryId;
		}
		finally {
			DataAccess.cleanUp(con, ps);
		}
	}

	@Override
	protected void doUpgrade() throws Exception {
		updatePageAttachments();
	}

	protected long getFolder(
			long groupId, long companyId, long userId, String userName,
			Timestamp createDate, long repositoryId, long parentFolderId,
			String name, boolean hidden)
		throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"select folderId from DLFolder where repositoryId = ? and " +
					"parentFolderId = ? and name = ?");

			ps.setLong(1, repositoryId);
			ps.setLong(2, parentFolderId);
			ps.setString(3, name);

			rs = ps.executeQuery();

			while (rs.next()) {
				int folderId = rs.getInt(1);

				return folderId;
			}
		}
		finally {
			DataAccess.cleanUp(con, ps);
		}

		return addDLFolder(
			groupId, companyId, userId, userName, createDate, repositoryId,
			parentFolderId, name, hidden);
	}

	protected long getRepository(
			long groupId, long companyId, long userId, String userName,
			Timestamp createDate, long classNameId, String name,
			String portletId)
		throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"select repositoryId from Repository where groupId = ? and " +
					"name = ? and portletId = ?");

			ps.setLong(1, groupId);
			ps.setString(2, name);
			ps.setString(3, portletId);

			rs = ps.executeQuery();

			while (rs.next()) {
				int repositoryId = rs.getInt(1);

				return repositoryId;
			}
		}
		finally {
			DataAccess.cleanUp(con, ps);
		}

		return addRepository(
			groupId, companyId, userId, userName, createDate, classNameId, name,
			portletId);
	}

	protected void updatePageAttachment(
			long companyId, long groupId, long resourcePrimKey, long nodeId,
			long userId, String userName)
		throws Exception {

		String dirName = "wiki/" + resourcePrimKey;

		String[] attachments = null;

		try {
			attachments = DLStoreUtil.getFileNames(
				companyId, CompanyConstants.SYSTEM, dirName);
		}
		catch (NoSuchDirectoryException nsde) {
		}

		if ((attachments == null) || (attachments.length == 0)) {
			return;
		}

		Date date = new Date();

		Timestamp createDate = new Timestamp(date.getTime());

		long classNameId = PortalUtil.getClassNameId(
			LiferayRepository.class.getName());

		String portletId = PortletKeys.WIKI;

		long repositoryId = getRepository(
			groupId, companyId, userId, userName, createDate, classNameId,
			portletId, portletId);

		long repositoryFolderId = getFolder(
			groupId, companyId, userId, userName, createDate, repositoryId,
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, portletId, false);

		long nodeFolderId = getFolder(
			groupId, companyId, userId, userName, createDate, repositoryId,
			repositoryFolderId, String.valueOf(nodeId), false);

		long pageFolderId = getFolder(
			groupId, companyId, userId, userName, createDate, repositoryId,
			nodeFolderId, String.valueOf(resourcePrimKey), false);

		for (String attachment : attachments) {
			String name = String.valueOf(
				increment(DLFileEntry.class.getName()));

			String title = FileUtil.getShortFileName(attachment);

			long size = DLStoreUtil.getFileSize(
				companyId, CompanyConstants.SYSTEM, attachment);

			String extension = FileUtil.getExtension(title);

			String mimeType = MimeTypesUtil.getContentType(extension);

			long fileEntryId = addDLFileEntry(
				groupId, companyId, userId, userName, createDate, repositoryId,
				pageFolderId, name, extension, mimeType, title, size);

			addDLFileVersion(
				increment(), groupId, companyId, userId, userName, createDate,
				repositoryId, pageFolderId, fileEntryId, extension, mimeType,
				title, size);

			byte[] bytes = DLStoreUtil.getFileAsBytes(
				companyId, CompanyConstants.SYSTEM, attachment);

			DLStoreUtil.addFile(companyId, pageFolderId, name, false, bytes);

			try {
				DLStoreUtil.deleteFile(
					companyId, CompanyConstants.SYSTEM, attachment);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Can't not delete the attachment " + attachment, e);
				}
			}
		}

		try {
			DLStoreUtil.deleteDirectory(
				companyId, CompanyConstants.SYSTEM, dirName);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn("Can't delete the directory " + dirName, e);
			}
		}
	}

	protected void updatePageAttachments() throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"select resourcePrimKey, nodeId, companyId, groupId, userId, " +
					"userName from WikiPage group by resourcePrimKey");

			rs = ps.executeQuery();

			while (rs.next()) {
				long resourcePrimKey = rs.getLong("resourcePrimKey");
				long nodeId = rs.getLong("nodeId");
				long companyId = rs.getLong("companyId");
				long groupId = rs.getLong("groupId");
				long userId = rs.getLong("userId");
				String userName = rs.getString("userName");

				updatePageAttachment(
					companyId, groupId, resourcePrimKey, nodeId, userId,
					userName);
			}
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		UpgradeWikiAttachments.class);

}