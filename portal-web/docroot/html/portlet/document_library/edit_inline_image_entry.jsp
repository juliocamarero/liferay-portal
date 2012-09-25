<%--
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
--%>

<%@page import="com.liferay.portlet.documentlibrary.action.EditInlineImageEntryAction"%>
<%@page import="com.liferay.portlet.documentlibrary.util.filter.*"%>
<%@ include file="/html/portlet/document_library/init.jsp" %>

<%
String strutsAction = ParamUtil.getString(request, "struts_action");
		 
String cmd = ParamUtil.getString(request, Constants.CMD, Constants.EDIT);

String tabs2 = ParamUtil.getString(request, "tabs2", "version-history");

String redirect = ParamUtil.getString(request, "redirect");

String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");

String uploadProgressId = "dlFileEntryUploadProgress";

FileEntry fileEntry = (FileEntry)request.getAttribute(WebKeys.DOCUMENT_LIBRARY_FILE_ENTRY);

long fileEntryId = BeanParamUtil.getLong(fileEntry, request, "fileEntryId");

long repositoryId = BeanParamUtil.getLong(fileEntry, request, "repositoryId");

if (repositoryId <= 0) {

	// add_asset.jspf only passes in groupId

	repositoryId = BeanParamUtil.getLong(fileEntry, request, "groupId");
}

long folderId = BeanParamUtil.getLong(fileEntry, request, "folderId");

Folder folder = null;

if (fileEntry != null) {
	folder = fileEntry.getFolder();
}

if ((folder == null) && (folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID)) {
	try {
		folder = DLAppServiceUtil.getFolder(folderId);
	}
	catch (NoSuchFolderException nsfe) {
		folderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
	}
}

FileVersion fileVersion = null;

long fileEntryTypeId = ParamUtil.getLong(request, "fileEntryTypeId", -1);

if (fileEntry != null) {
	fileVersion = fileEntry.getLatestFileVersion();

	if ((fileEntryTypeId == -1) && (fileVersion.getModel() instanceof DLFileVersion)) {
		DLFileVersion dlFileVersion = (DLFileVersion)fileVersion.getModel();

		fileEntryTypeId = dlFileVersion.getFileEntryTypeId();
	}
}

DLFileEntryType dlFileEntryType = null;

if (fileEntryTypeId > 0) {
	dlFileEntryType = DLFileEntryTypeLocalServiceUtil.getFileEntryType(fileEntryTypeId);
}

long assetClassPK = 0;

if ((fileVersion != null) && !fileVersion.isApproved() && Validator.isNotNull(fileVersion.getVersion()) && !fileVersion.getVersion().equals(DLFileEntryConstants.VERSION_DEFAULT)) {
	assetClassPK = fileVersion.getFileVersionId();
}
else if (fileEntry != null) {
	assetClassPK = fileEntry.getFileEntryId();
}

boolean checkedOut = false;
boolean hasLock = false;
Lock lock = null;

if (fileEntry != null) {
	checkedOut = fileEntry.isCheckedOut();
	hasLock = fileEntry.hasLock();
	lock = fileEntry.getLock();
}

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", strutsAction);
portletURL.setParameter("tabs2", tabs2);
portletURL.setParameter("redirect", redirect);
portletURL.setParameter("fileEntryId", String.valueOf(fileEntryId));
%>

<c:if test="<%= Validator.isNull(referringPortletResource) %>">
	<liferay-util:include page="/html/portlet/document_library/top_links.jsp" />
</c:if>

<c:if test="<%= checkedOut %>">
	<c:choose>
		<c:when test="<%= hasLock %>">
			<div class="portlet-msg-success">
				<c:choose>
					<c:when test="<%= lock.isNeverExpires() %>">
						<liferay-ui:message key="you-now-have-an-indefinite-lock-on-this-document" />
					</c:when>
					<c:otherwise>

						<%
						String lockExpirationTime = LanguageUtil.getTimeDescription(pageContext, DLFileEntryConstants.LOCK_EXPIRATION_TIME).toLowerCase();
						%>

						<%= LanguageUtil.format(pageContext, "you-now-have-a-lock-on-this-document", lockExpirationTime, false) %>
					</c:otherwise>
				</c:choose>
			</div>
		</c:when>
		<c:otherwise>
			<div class="portlet-msg-error">
				<%= LanguageUtil.format(pageContext, "you-cannot-modify-this-document-because-it-was-checked-out-by-x-on-x", new Object[] {HtmlUtil.escape(PortalUtil.getUserName(lock.getUserId(), String.valueOf(lock.getUserId()))), dateFormatDateTime.format(lock.getCreateDate())}, false) %>
			</div>
		</c:otherwise>
	</c:choose>
</c:if>

<%
boolean localizeTitle = true;
String headerTitle = LanguageUtil.get(pageContext, "new-document");

if (fileVersion != null) {
	headerTitle = fileVersion.getTitle();
	localizeTitle= false;
}
else if (dlFileEntryType != null) {
	headerTitle = LanguageUtil.format(pageContext, "new-x", new Object[] {dlFileEntryType.getName()});
}

String editinlineVersionFile = (String)request.getAttribute(EditInlineImageEntryAction.EDITINLINE_VERSION_FILE);
String editinlineMaxVersionFile = (String)request.getAttribute(EditInlineImageEntryAction.EDITINLINE_MAX_VERSION_FILE);

//String temporalFileEditInline = (String)request.getAttribute(EditInlineImageEntryAction.TEMPORAL_FILE_INLINE);

// Se recuperan los nombres de los ficheros para hacer y deshacer
//String fileNames = (String)request.getAttribute(EditInlineImageEntryAction.FILE_NAMES);
//String[] arrFileNames = fileNames.split(EditInlineImageEntryAction.FILE_NAMES_SEPARATOR);
%>

<portlet:actionURL var="editFileEntryURL">
	<portlet:param name="struts_action" value="/document_library/edit_inline_image_entry" />
	<portlet:param name="uploader" value="classic" />
</portlet:actionURL>

<aui:form action="<%= editFileEntryURL %>" cssClass="lfr-dynamic-form" enctype="multipart/form-data" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveFileEntry(false);" %>'>
	<aui:input name="<%= Constants.CMD %>" type="hidden" />
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	<aui:input name="referringPortletResource" type="hidden" value="<%= referringPortletResource %>" />
	<aui:input name="uploadProgressId" type="hidden" value="<%= uploadProgressId %>" />
	<aui:input name="repositoryId" type="hidden" value="<%= repositoryId %>" />
	<aui:input name="folderId" type="hidden" value="<%= folderId %>" />
	<aui:input name="fileEntryId" type="hidden" value="<%= fileEntryId %>" />
	<aui:input name="workflowAction" type="hidden" value="<%= WorkflowConstants.ACTION_PUBLISH %>" />
	
	<aui:input name="<%=EditInlineImageEntryAction.EDITINLINE_VERSION_FILE %>" id="<%=EditInlineImageEntryAction.EDITINLINE_VERSION_FILE %>" type="hidden" value="<%= editinlineVersionFile %>" />
	<aui:input name="<%=EditInlineImageEntryAction.EDITINLINE_MAX_VERSION_FILE %>" id="<%=EditInlineImageEntryAction.EDITINLINE_MAX_VERSION_FILE %>" type="hidden" value="<%= editinlineMaxVersionFile %>" />
	
	<aui:input name="<%=EditInlineImageEntryAction.IMAGE_WIDTH %>" type="hidden" value="" />
	<aui:input name="<%=EditInlineImageEntryAction.IMAGE_HEIGHT %>" type="hidden" value="" />

	<liferay-ui:error exception="<%= DuplicateFileException.class %>" message="please-enter-a-unique-document-name" />
	<liferay-ui:error exception="<%= DuplicateFolderNameException.class %>" message="please-enter-a-unique-document-name" />

	<liferay-ui:error exception="<%= FileExtensionException.class %>">
		<liferay-ui:message key="document-names-must-end-with-one-of-the-following-extensions" /> <%= StringUtil.merge(PrefsPropsUtil.getStringArray(PropsKeys.DL_FILE_EXTENSIONS, StringPool.COMMA), StringPool.COMMA_AND_SPACE) %>.
	</liferay-ui:error>

	<liferay-ui:error exception="<%= FileMimeTypeException.class %>">
		<liferay-ui:message key="media-files-must-be-one-of-the-following-formats" /> <%= StringUtil.merge(DLUtil.getMediaGalleryMimeTypes(preferences, renderRequest), StringPool.COMMA_AND_SPACE) %>.
	</liferay-ui:error>

	<liferay-ui:error exception="<%= FileNameException.class %>" message="please-enter-a-file-with-a-valid-file-name" />
	<liferay-ui:error exception="<%= NoSuchFolderException.class %>" message="please-enter-a-valid-folder" />

	<liferay-ui:error exception="<%= SourceFileNameException.class %>">
		<liferay-ui:message key="the-source-file-does-not-have-the-same-extension-as-the-original-file" />
	</liferay-ui:error>

	<%
	long fileMaxSize = PrefsPropsUtil.getLong(PropsKeys.DL_FILE_MAX_SIZE);

	if (fileMaxSize == 0) {
		fileMaxSize = PrefsPropsUtil.getLong(PropsKeys.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE);
	}

	fileMaxSize /= 1024;
	%>

	<liferay-ui:error exception="<%= FileSizeException.class %>">
		<liferay-ui:message arguments="<%= fileMaxSize %>" key="please-enter-a-file-with-a-valid-file-size-no-larger-than-x" />
	</liferay-ui:error>

	<liferay-ui:asset-categories-error />

	<liferay-ui:asset-tags-error />

	<aui:model-context bean="<%= fileVersion %>" model="<%= DLFileVersion.class %>" />

	<c:if test="<%= fileVersion != null %>">
		<aui:workflow-status model="<%= DLFileEntry.class %>" status="<%= fileVersion.getStatus() %>" version="<%= fileVersion.getVersion() %>" />
	</c:if>

	<aui:fieldset>
		<aui:field-wrapper>
			<c:if test="<%= fileMaxSize != 0 %>">
				<div class="portlet-msg-info">
					<%= LanguageUtil.format(pageContext, "upload-documents-no-larger-than-x-k", String.valueOf(fileMaxSize), false) %>
				</div>
			</c:if>
		</aui:field-wrapper>
		
		<aui:button-row>
			<%@ include file="/html/portlet/document_library/filters/edit_inline_image_blur_effect.jspf" %>
			<%@ include file="/html/portlet/document_library/filters/edit_inline_image_gaussian_effect.jspf" %>
			<%@ include file="/html/portlet/document_library/filters/edit_inline_image_greasse_scale_effect.jspf" %>
			
			<%@ include file="/html/portlet/document_library/filters/edit_inline_image_crop.jspf" %>
			
			<%@ include file="/html/portlet/document_library/filters/edit_inline_image_rotate_effect.jspf" %>
			
			<aui:button disabled="false" name="undoButton" id="deshacerButton" onClick='<%= renderResponse.getNamespace() + "undo();" %>' value="deshacer" />
			<aui:button disabled="false" name="redoButton" id="rehacerButton" onClick='<%= renderResponse.getNamespace() + "redo();" %>' value="rehacer" />
		</aui:button-row>
		<div class="lfr-preview-file lfr-preview-image" id="<portlet:namespace />previewFile">
			<div class="lfr-preview-file-content lfr-preview-image-content" id="<portlet:namespace />previewFileContent">
				<div class="lfr-preview-file-image-current-column">
					<div class="lfr-preview-file-image-container">
						<%
							String previewQueryString = "&imagePreview=1";
							String previewFileURL = DLUtil.getPreviewURL(fileEntry, fileVersion, themeDisplay, previewQueryString, "/editinline/");
							previewFileURL += "&" + EditInlineImageEntryAction.EDITINLINE_VERSION_FILE + "=" + editinlineVersionFile;
						%>
						<img class="lfr-preview-file-image-current" id='<%= renderResponse.getNamespace() + "imgEditInline" %>' src="<%= previewFileURL %>" />
					</div>
				</div>
			</div>
		</div>
		
		<c:if test="<%= fileEntry == null %>">
			<aui:field-wrapper label="permissions">
				<liferay-ui:input-permissions
					modelName="<%= DLFileEntryConstants.getClassName() %>"
				/>
			</aui:field-wrapper>
		</c:if>

		<%
		boolean approved = false;
		boolean draft = false;
		boolean pending = false;

		if (fileVersion != null) {
			approved = fileVersion.isApproved();
			draft = fileVersion.isDraft();
			pending = fileVersion.isPending();
		}
		%>

		<c:if test="<%= approved %>">
			<div class="portlet-msg-info">
				<liferay-ui:message key="a-new-version-will-be-created-automatically-if-this-content-is-modified" />
			</div>
		</c:if>

		<c:if test="<%= pending %>">
			<div class="portlet-msg-info">
				<liferay-ui:message key="there-is-a-publication-workflow-in-process" />
			</div>
		</c:if>

		<aui:button-row>

			<aui:button disabled="false" name="updateButton" onClick='<%= renderResponse.getNamespace() + "updateImageFileEntry();" %>' value="publish" />

			<aui:button href="<%= redirect %>" type="cancel" />
		</aui:button-row>
	</aui:fieldset>
</aui:form>

<liferay-ui:upload-progress
	id="<%= uploadProgressId %>"
	message="uploading"
	redirect="<%= redirect %>"
/>

<link rel="stylesheet" href="/html/css/jquery/jquery.Jcrop.min.css" type="text/css" />

<aui:script>
	function <portlet:namespace />saveFileEntry(draft) {
		<%= HtmlUtil.escape(uploadProgressId) %>.startProgress();

		if (draft) {
			document.<portlet:namespace />fm.<portlet:namespace />workflowAction.value = "<%= WorkflowConstants.ACTION_SAVE_DRAFT %>";
		}

		document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = "<%= (fileEntry == null) ? Constants.ADD : Constants.UPDATE %>";
		submitForm(document.<portlet:namespace />fm);
	}
	
	<%-- INICIO FELIX --%>
	var jsVersionFile = '<%=(int)Float.parseFloat(editinlineVersionFile) %>';
	var jsMaxVersionFile = '<%=(int)Float.parseFloat(editinlineMaxVersionFile) %>';
	
	function <portlet:namespace />undo() {
		if(jsVersionFile > 1) {
			var srcImg = $('#<portlet:namespace />imgEditInline').attr("src");
			
			var strOldTmp = '<%=EditInlineImageEntryAction.EDITINLINE_VERSION_FILE %>=' + jsVersionFile + '.0';
			jsVersionFile--;
			var strNewTmp = '<%=EditInlineImageEntryAction.EDITINLINE_VERSION_FILE %>=' + jsVersionFile + '.0';
			
			$('#<portlet:namespace /><%=EditInlineImageEntryAction.EDITINLINE_VERSION_FILE %>').val(jsVersionFile + '.0');

			var newSrc = srcImg.replace(strOldTmp, strNewTmp);
			$('.lfr-preview-file-image-current').attr("src", newSrc);
		}
	}
	
	function <portlet:namespace />redo() {
		if(jsVersionFile < jsMaxVersionFile) {
			var srcImg = $('#<portlet:namespace />imgEditInline').attr("src");

			var strOldTmp = '<%=EditInlineImageEntryAction.EDITINLINE_VERSION_FILE %>=' + jsVersionFile + '.0';
			jsVersionFile++;
			var strNewTmp = '<%=EditInlineImageEntryAction.EDITINLINE_VERSION_FILE %>=' + jsVersionFile + '.0';
			
			$('#<portlet:namespace /><%=EditInlineImageEntryAction.EDITINLINE_VERSION_FILE %>').val(jsVersionFile + '.0');

			var newSrc = srcImg.replace(strOldTmp, strNewTmp);
			$('.lfr-preview-file-image-current').attr("src", newSrc);
		}
	}
	
	function <portlet:namespace />updateImageFileEntry() {
		<%= HtmlUtil.escape(uploadProgressId) %>.startProgress();

		document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = "updateInline";
		submitForm(document.<portlet:namespace />fm);
	}
	<%-- FIN FELIX --%>

	<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) %>">
		Liferay.Util.focusFormField(document.<portlet:namespace />fm.<portlet:namespace />file);
	</c:if>
</aui:script>

<%
if (fileEntry != null) {
	DLUtil.addPortletBreadcrumbEntries(fileEntry, request, renderResponse);

	PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, "edit"), currentURL);
}
else {
	DLUtil.addPortletBreadcrumbEntries(folderId, request, renderResponse);

	PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, "add-file-entry"), currentURL);
}
%>

<%!
private static Log _log = LogFactoryUtil.getLog("portal-web.docroot.html.portlet.document_library.edit_file_entry_jsp");
%>
