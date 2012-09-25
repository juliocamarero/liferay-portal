package com.liferay.portlet.documentlibrary.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portlet.documentlibrary.NoSuchFileException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.documentlibrary.util.filter.EditInlineFilterFactory;

public class EditInlineUtil {
	public final static String TEMP_FOLDER_EDITINLINE = "editinline";
	
	private static Log _log = LogFactoryUtil.getLog(EditInlineUtil.class);
	
	public static File createInitialTemporalFile(DLFileEntry fileEntry) throws PortalException, SystemException, IOException {
		
		InputStream inputStream = null;
		File fTmp = null;
		try {
			inputStream = fileEntry.getContentStream();
			
			String fileName = fileEntry.getTitle();
			String tempFolderName = TEMP_FOLDER_EDITINLINE;
			
			long groupId = fileEntry.getGroupId();
			long folderId = fileEntry.getFolderId();
			
			// Se borra el directorio de anteriores ediciones, si existe
			deleteTemporalFolder(groupId, folderId, fileName, tempFolderName);

			// Se crea el nuevo fichero temporal
			DLAppServiceUtil.addTempFileEntry(groupId, folderId,
					fileName, tempFolderName, fileEntry.getContentStream());
			
			_log.debug("Creado nuevo fichero temporal para " + fileName);
		} finally {
			if (inputStream != null) {
				StreamUtil.cleanUp(inputStream);
			}
		}
		
		return fTmp;
	}
	
	/**
	 * Borra el directorio temporal, si existe, para las ediciones de una foto editada anteriormente
	 * @param groupId
	 * @param folderId
	 * @param fileName
	 * @param tempFolderName
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void deleteTemporalFolder(long groupId, long folderId,
			String fileName, String tempFolderName) throws PortalException, SystemException {
		try {
			DLAppServiceUtil.deleteTempFileEntry(groupId, folderId,
					fileName, tempFolderName);
		} catch (PortalException e) {
			if(!(e instanceof NoSuchFileException)) {
				throw e;
			}
		}
	}
	
	public static void deleteTemporalFolder(long groupId, long folderId,
			String fileName) throws PortalException, SystemException {
		deleteTemporalFolder(groupId, folderId, fileName, TEMP_FOLDER_EDITINLINE);
	}

	/**
	 * Borra la versiones superiores a editinlineVersionFile del fichero temporal hasta la version
	 * máxima indicada por editinlineMaxVersionFile
	 * @param fileEntry
	 * @param editinlineVersionFile
	 * @param editinlineMaxVersionFile
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void deleteTempVersionesSuperiores(DLFileEntry fileEntry,
			String editinlineVersionFile, String editinlineMaxVersionFile) throws PortalException, SystemException {
		
		String fileName = fileEntry.getTitle();
		String tempFolderName = TEMP_FOLDER_EDITINLINE;
		
		long groupId = fileEntry.getGroupId();
		long folderId = fileEntry.getFolderId();
		
		float flVersionFile = Float.parseFloat(editinlineVersionFile);
		flVersionFile++;
		float flMaxVersionFile = Float.parseFloat(editinlineMaxVersionFile);
		
		while(flVersionFile <= flMaxVersionFile) {
			// Se crea el nuevo fichero temporal
			DLAppServiceUtil.deleteTempFileEntry(groupId, folderId, fileName,
					tempFolderName, Float.toString(flVersionFile));
			flVersionFile++;
		}
	}
	
	public final static String applyFilter(String strFilter, ActionRequest actionRequest,
			ActionResponse actionResponse) throws PortletException {
		EditInlineFilter filter = EditInlineFilterFactory.getFilter(strFilter);
		return filter.process(actionRequest, actionResponse);
	}
}
