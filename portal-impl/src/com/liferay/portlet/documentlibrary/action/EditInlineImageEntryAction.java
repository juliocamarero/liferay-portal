package com.liferay.portlet.documentlibrary.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.liferay.portal.DuplicateLockException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.servlet.ServletResponseConstants;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.asset.AssetCategoryException;
import com.liferay.portlet.asset.AssetTagException;
import com.liferay.portlet.assetpublisher.util.AssetPublisherUtil;
import com.liferay.portlet.documentlibrary.DuplicateFileException;
import com.liferay.portlet.documentlibrary.DuplicateFolderNameException;
import com.liferay.portlet.documentlibrary.FileExtensionException;
import com.liferay.portlet.documentlibrary.FileMimeTypeException;
import com.liferay.portlet.documentlibrary.FileNameException;
import com.liferay.portlet.documentlibrary.FileSizeException;
import com.liferay.portlet.documentlibrary.InvalidFileVersionException;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.NoSuchFileVersionException;
import com.liferay.portlet.documentlibrary.NoSuchFolderException;
import com.liferay.portlet.documentlibrary.SourceFileNameException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.store.Store;
import com.liferay.portlet.documentlibrary.util.DLUtil;
import com.liferay.portlet.documentlibrary.util.EditInlineUtil;
import com.liferay.portlet.dynamicdatamapping.StorageFieldRequiredException;

/**
 * @author Jose Felix Iñigo Rodriguez
 */
public class EditInlineImageEntryAction extends PortletAction {
	private final static String CREATE_TEMPORAL_FILE = "createTemporalFile";
	public final static String EDITINLINE_VERSION_FILE = "editInlineVersionFile";
	public final static String EDITINLINE_MAX_VERSION_FILE = "editInlineMaxVersionFile";
	
	public final static String IMAGE_WIDTH = "widthImage";
	public final static String IMAGE_HEIGHT = "heightImage";
	public final static String CROP_ENABLED = "cropEnabled";
	public final static String CROP_X1 = "cropX1";
	public final static String CROP_X2 = "cropX2";
	public final static String CROP_Y1 = "cropY1";
	public final static String CROP_Y2 = "cropY2";
	public final static String CROP_H = "cropH";
	public final static String CROP_W = "cropW";	
	@Override
	public void processAction(ActionMapping mapping, ActionForm form,
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (Validator.isNull(cmd)) {
				UploadException uploadException = (UploadException) actionRequest
						.getAttribute(WebKeys.UPLOAD_EXCEPTION);

				if (uploadException != null) {
					if (uploadException.isExceededSizeLimit()) {
						throw new FileSizeException(uploadException.getCause());
					}

					throw new PortalException(uploadException.getCause());
				}
			} else if (actionIsFilter(cmd)) {
				applyFilter(cmd, actionRequest, actionResponse);
			} else if (cmd.equals("updateInline")) {
				updateInlineImageEntry(portletConfig, actionRequest, actionResponse);
				
				// Este deberia ser, junto con cancelar, el unico caso para volver atras
				sendRedirect(actionRequest, actionResponse);
			}
		} catch (Exception e) {
			if (e instanceof DuplicateLockException
					|| e instanceof InvalidFileVersionException
					|| e instanceof NoSuchFileEntryException
					|| e instanceof PrincipalException) {

				if (e instanceof DuplicateLockException) {
					DuplicateLockException dle = (DuplicateLockException) e;

					SessionErrors.add(actionRequest, dle.getClass(),
							dle.getLock());
				} else {
					SessionErrors.add(actionRequest, e.getClass());
				}

				setForward(actionRequest, "portlet.document_library.error");
			} else if (e instanceof DuplicateFileException
					|| e instanceof DuplicateFolderNameException
					|| e instanceof FileExtensionException
					|| e instanceof FileMimeTypeException
					|| e instanceof FileNameException
					|| e instanceof FileSizeException
					|| e instanceof NoSuchFolderException
					|| e instanceof SourceFileNameException
					|| e instanceof StorageFieldRequiredException) {

				if (!cmd.equals(Constants.ADD_MULTIPLE)
						&& !cmd.equals(Constants.ADD_TEMP)) {

					SessionErrors.add(actionRequest, e.getClass());

					return;
				}

				if (e instanceof DuplicateFileException) {
					HttpServletResponse response = PortalUtil
							.getHttpServletResponse(actionResponse);

					response.setStatus(ServletResponseConstants.SC_DUPLICATE_FILE_EXCEPTION);
				} else if (e instanceof FileExtensionException) {
					HttpServletResponse response = PortalUtil
							.getHttpServletResponse(actionResponse);

					response.setStatus(ServletResponseConstants.SC_FILE_EXTENSION_EXCEPTION);
				} else if (e instanceof FileNameException) {
					HttpServletResponse response = PortalUtil
							.getHttpServletResponse(actionResponse);

					response.setStatus(ServletResponseConstants.SC_FILE_NAME_EXCEPTION);
				} else if (e instanceof FileSizeException) {
					HttpServletResponse response = PortalUtil
							.getHttpServletResponse(actionResponse);

					response.setStatus(ServletResponseConstants.SC_FILE_SIZE_EXCEPTION);
				}

				SessionErrors.add(actionRequest, e.getClass());
			} else if (e instanceof AssetCategoryException
					|| e instanceof AssetTagException) {

				SessionErrors.add(actionRequest, e.getClass(), e);
			} else {
				throw e;
			}
		}

	}
	
	private boolean actionIsFilter(String action) {
		return action.equals("blur") ||
				action.equals("gaussian") ||
				action.equals("greassescale") ||
				action.equals("rotate") ||
				action.equals("crop");
	}

	@Override
	public ActionForward render(ActionMapping mapping, ActionForm form,
			PortletConfig portletConfig, RenderRequest renderRequest,
			RenderResponse renderResponse) throws Exception {

		try {
			String strCreateTMP = renderRequest
					.getParameter(CREATE_TEMPORAL_FILE);
			if (strCreateTMP != null && Boolean.parseBoolean(strCreateTMP)) {
				createTemporalFile(renderRequest, renderResponse);
			}
			
			ActionUtil.getFileEntry(renderRequest);
		} catch (Exception e) {
			if (e instanceof NoSuchFileEntryException
					|| e instanceof NoSuchFileVersionException
					|| e instanceof PrincipalException) {

				SessionErrors.add(renderRequest, e.getClass());

				return mapping.findForward("portlet.document_library.error");
			} else {
				throw e;
			}
		}

		String forward = "portlet.document_library.edit_inline_image_entry";

		return mapping.findForward(getForward(renderRequest, forward));
	}

	private void createTemporalFile(RenderRequest renderRequest,
			RenderResponse renderResponse) throws PortalException,
			SystemException, IOException {
		long fileEntryId = ParamUtil.getLong(renderRequest, "fileEntryId");
		DLFileEntry fileEntry = DLFileEntryLocalServiceUtil
				.getDLFileEntry(fileEntryId);
		
		EditInlineUtil.createInitialTemporalFile(fileEntry);
		// Se asigna la version inicial del fichero para la visualizacion
		renderRequest.setAttribute(EDITINLINE_VERSION_FILE, Store.VERSION_DEFAULT);
		renderRequest.setAttribute(EDITINLINE_MAX_VERSION_FILE, Store.VERSION_DEFAULT);
	}

	protected void applyFilter(String filter, ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {
		try {
			String newVersion = EditInlineUtil.applyFilter(filter, actionRequest, actionResponse);
			
			actionRequest.setAttribute(EDITINLINE_VERSION_FILE, newVersion);
			actionRequest.setAttribute(EDITINLINE_MAX_VERSION_FILE, newVersion);
			
		} catch (PortletException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Cambiar el metodo, es una copia del updateFileEntry
	 * @param portletConfig
	 * @param actionRequest
	 * @param actionResponse
	 * @throws Exception
	 */
	protected void updateInlineImageEntry(PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String editinlineVersionFile = actionRequest.getParameter(EDITINLINE_VERSION_FILE);
		if(Store.VERSION_DEFAULT.equals(editinlineVersionFile)) {
			return;
		}
		UploadPortletRequest uploadPortletRequest = PortalUtil
				.getUploadPortletRequest(actionRequest);

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
				.getAttribute(WebKeys.THEME_DISPLAY);

		String cmd = ParamUtil.getString(uploadPortletRequest, Constants.CMD);
		long fileEntryId = ParamUtil.getLong(uploadPortletRequest,
				"fileEntryId");
		long folderId = ParamUtil.getLong(uploadPortletRequest, "folderId");
		String sourceFileName = uploadPortletRequest.getFileName("file");
		String title = ParamUtil.getString(uploadPortletRequest, "title");
		String description = ParamUtil.getString(uploadPortletRequest,
				"description");
		String changeLog = ParamUtil.getString(uploadPortletRequest,
				"changeLog");
		boolean majorVersion = ParamUtil.getBoolean(uploadPortletRequest,
				"majorVersion");

		if (folderId > 0) {
			Folder folder = DLAppServiceUtil.getFolder(folderId);

			if (folder.getGroupId() != themeDisplay.getScopeGroupId()) {
				throw new NoSuchFolderException();
			}
		}

		InputStream inputStream = null;

		try {
			DLFileEntry fileEntryEditinline = DLFileEntryLocalServiceUtil
					.getDLFileEntry(fileEntryId);
			String fileName = fileEntryEditinline.getTitle();

			String contentType = MimeTypesUtil.getContentType(fileName);
			long size = 0;
			String portletName = portletConfig.getPortletName();

			if (portletName.equals(PortletKeys.MEDIA_GALLERY_DISPLAY)) {
				String portletResource = ParamUtil.getString(actionRequest,
						"portletResource");

				PortletPreferences portletPreferences = null;

				if (Validator.isNotNull(portletResource)) {
					PortletPreferencesFactoryUtil.getPortletSetup(
							actionRequest, portletResource);
				} else {
					portletPreferences = actionRequest.getPreferences();
				}

				String[] mimeTypes = DLUtil.getMediaGalleryMimeTypes(
						portletPreferences, actionRequest);

				if (Arrays.binarySearch(mimeTypes, contentType) < 0) {
					throw new FileMimeTypeException(contentType);
				}
			}
			
			inputStream = DLAppServiceUtil.getTempFileEntryAsStream(themeDisplay.getUserId(), fileName,
					EditInlineUtil.TEMP_FOLDER_EDITINLINE, editinlineVersionFile);

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
					DLFileEntry.class.getName(), uploadPortletRequest);

			FileEntry fileEntry = null;

			if (cmd.equals(Constants.UPDATE_AND_CHECKIN)) {
				// Update file entry and checkin
				fileEntry = DLAppServiceUtil.updateFileEntryAndCheckIn(
						fileEntryId, sourceFileName, contentType, title,
						description, changeLog, majorVersion, inputStream,
						size, serviceContext);
			} else {
				// Update file entry
				fileEntry = DLAppServiceUtil.updateFileEntry(fileEntryId,
						sourceFileName, contentType, title, description,
						changeLog, majorVersion, inputStream, size,
						serviceContext);
			}

			// Se borran los archivos temporales creados.
			EditInlineUtil.deleteTemporalFolder(fileEntry.getGroupId(), fileEntry.getFolderId(),
					fileEntry.getTitle());

			AssetPublisherUtil.addRecentFolderId(actionRequest,
					DLFileEntry.class.getName(), folderId);
		} finally {
			StreamUtil.cleanUp(inputStream);
		}
	}

}
