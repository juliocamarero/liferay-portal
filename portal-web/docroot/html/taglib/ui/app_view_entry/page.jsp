<%--
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
--%>

<%@ include file="/html/taglib/init.jsp" %>

<%
String actionJsp = (String)request.getAttribute("liferay-ui:app-view-entry:actionJsp");
String assetCategoryClassName = GetterUtil.getString(request.getAttribute("liferay-ui:app-view-entry:assetCategoryClassName"));
long assetCategoryClassPK = GetterUtil.getLong(request.getAttribute("liferay-ui:app-view-entry:assetCategoryClassPK"));
String assetTagClassName = GetterUtil.getString(request.getAttribute("liferay-ui:app-view-entry:assetTagClassName"));
long assetTagClassPK = GetterUtil.getLong(request.getAttribute("liferay-ui:app-view-entry:assetTagClassPK"));
String author = GetterUtil.getString(request.getAttribute("liferay-ui:app-view-entry:author"));
Date createDate = GetterUtil.getDate(request.getAttribute("liferay-ui:app-view-entry:createDate"), DateFormatFactoryUtil.getDate(locale), null);
String cssClass = GetterUtil.getString((String)request.getAttribute("liferay-ui:app-view-entry:cssClass"));
Date expirationDate = GetterUtil.getDate(request.getAttribute("liferay-ui:app-view-entry:expirationDate"), DateFormatFactoryUtil.getDate(locale), null);
Map<String, Object> data = (Map<String, Object>)request.getAttribute("liferay-ui:app-view-entry:data");
String description = (String)request.getAttribute("liferay-ui:app-view-entry:description");
Date displayDate = GetterUtil.getDate(request.getAttribute("liferay-ui:app-view-entry:displayDate"), DateFormatFactoryUtil.getDate(locale), null);
String displayStyle = (String)request.getAttribute("liferay-ui:app-view-entry:displayStyle");
boolean folder = GetterUtil.getBoolean(request.getAttribute("liferay-ui:app-view-entry:folder"));
String latestApprovedVersion = GetterUtil.getString(request.getAttribute("liferay-ui:app-view-entry:latestApprovedVersion"));
String latestApprovedVersionAuthor = GetterUtil.getString(request.getAttribute("liferay-ui:app-view-entry:latestApprovedVersionAuthor"));
boolean locked = GetterUtil.getBoolean(request.getAttribute("liferay-ui:app-view-entry:locked"));
Date modifiedDate = GetterUtil.getDate(request.getAttribute("liferay-ui:app-view-entry:modifiedDate"), DateFormatFactoryUtil.getDate(locale), null);
Date reviewDate = GetterUtil.getDate(request.getAttribute("liferay-ui:app-view-entry:reviewDate"), DateFormatFactoryUtil.getDate(locale), null);
String rowCheckerId = (String)request.getAttribute("liferay-ui:app-view-entry:rowCheckerId");
String rowCheckerName = (String)request.getAttribute("liferay-ui:app-view-entry:rowCheckerName");
boolean shortcut = GetterUtil.getBoolean(request.getAttribute("liferay-ui:app-view-entry:shortcut"));
boolean showCheckbox = GetterUtil.getBoolean(request.getAttribute("liferay-ui:app-view-entry:showCheckbox"));
boolean showLinkTitle = GetterUtil.getBoolean(request.getAttribute("liferay-ui:app-view-entry:showLinkTitle"));
int status = GetterUtil.getInteger(request.getAttribute("liferay-ui:app-view-entry:status"), -1);
String thumbnailDivStyle = (String)request.getAttribute("liferay-ui:app-view-entry:thumbnailDivStyle");
String thumbnailSrc = (String)request.getAttribute("liferay-ui:app-view-entry:thumbnailSrc");
String thumbnailStyle = (String)request.getAttribute("liferay-ui:app-view-entry:thumbnailStyle");
String title = (String)request.getAttribute("liferay-ui:app-view-entry:title");
String url = (String)request.getAttribute("liferay-ui:app-view-entry:url");
String version = GetterUtil.getString(request.getAttribute("liferay-ui:app-view-entry:version"));

String shortTitle = StringUtil.shorten(title, 60);

String linkTitle = StringPool.BLANK;

if (showLinkTitle) {
	linkTitle = HtmlUtil.escapeAttribute(HtmlUtil.unescape(title) + " - " + HtmlUtil.unescape(description));
}
%>

<c:choose>
	<c:when test='<%= displayStyle.equals("icon") %>'>
		<div class="app-view-entry app-view-entry-taglib entry-display-style display-<%= displayStyle %> <%= showCheckbox ? "selectable" : StringPool.BLANK %> <%= cssClass %>" <%= AUIUtil.buildData(data) %> data-draggable="<%= showCheckbox ? Boolean.TRUE.toString() : Boolean.FALSE.toString() %>" data-title="<%= HtmlUtil.escapeAttribute(shortTitle) %>">
			<c:if test="<%= showCheckbox %>">
				<aui:input cssClass="overlay entry-selector" label="" name="<%= RowChecker.ROW_IDS + rowCheckerName %>" type="checkbox" value="<%= rowCheckerId %>" />
			</c:if>

			<%
			if (!folder) {
				request.removeAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
			}
			%>

			<liferay-util:include page="<%= actionJsp %>" />

			<c:choose>
				<c:when test="<%= Validator.isNull(url) %>">
					<span class="entry-link" data-folder="<%= folder ? Boolean.TRUE.toString() : Boolean.FALSE.toString() %>" <%= folder ? "data-folder-id=\"" + rowCheckerId + "\"" : StringPool.BLANK %> title="<%= linkTitle %>">
				</c:when>
				<c:otherwise>
					<a class="entry-link" data-folder="<%= folder ? Boolean.TRUE.toString() : Boolean.FALSE.toString() %>" <%= folder ? "data-folder-id=\"" + rowCheckerId + "\"" : StringPool.BLANK %> href="<%= url %>" title="<%= linkTitle %>">
				</c:otherwise>
			</c:choose>

			<div class="entry-thumbnail" style="<%= thumbnailDivStyle %>">
				<img alt="" border="no" class="img-polaroid" src="<%= thumbnailSrc %>" style="<%= thumbnailStyle %>" />

				<c:if test="<%= shortcut %>">
					<img alt="<liferay-ui:message key="shortcut" />" class="shortcut-icon img-polaroid" src="<%= themeDisplay.getPathThemeImages() %>/file_system/large/overlay_link.png" />
				</c:if>

				<c:if test="<%= locked %>">
					<img alt="<liferay-ui:message key="locked" />" class="locked-icon img-polaroid" src="<%= themeDisplay.getPathThemeImages() %>/file_system/large/overlay_lock.png" />
				</c:if>
			</div>

			<span class="entry-title">
				<span class="entry-title-text">
					<%= HtmlUtil.escape(shortTitle) %>

					<c:if test="<%= !folder && ((status == WorkflowConstants.STATUS_DRAFT) || (status == WorkflowConstants.STATUS_PENDING)) %>">

						<%
							String statusLabel = WorkflowConstants.toLabel(status);
						%>

						<span class="workflow-status-<%= statusLabel %>">
							(<liferay-ui:message key="<%= statusLabel %>" />)
						</span>
					 </c:if>
				</span>

				<span class="entry-result-icon"></span>
			</span>

			<c:choose>
				<c:when test="<%= Validator.isNull(url) %>">
					</span>
				</c:when>
				<c:otherwise>
					</a>
				</c:otherwise>
			</c:choose>
		</div>
	</c:when>
	<c:when test='<%= displayStyle.equals("descriptive") %>'>
		<div class="app-view-entry app-view-entry-taglib entry-display-style display-<%= displayStyle %> <%= showCheckbox ? "selectable" : StringPool.BLANK %> <%= cssClass %>" <%= AUIUtil.buildData(data) %> data-draggable="<%= showCheckbox ? Boolean.TRUE.toString() : Boolean.FALSE.toString() %>" data-title="<%= HtmlUtil.escapeAttribute(shortTitle) %>">
			<c:choose>
				<c:when test="<%= Validator.isNull(url) %>">
					<span class="entry-link" data-folder="<%= folder ? Boolean.TRUE.toString() : Boolean.FALSE.toString() %>" data-folder-id="<%= rowCheckerId %>" title="<%= linkTitle %>">
				</c:when>
				<c:otherwise>
					<a class="entry-link" data-folder="<%= folder ? Boolean.TRUE.toString() : Boolean.FALSE.toString() %>" data-folder-id="<%= rowCheckerId %>" href="<%= url %>" title="<%= linkTitle %>">
				</c:otherwise>
			</c:choose>

			<div class="entry-thumbnail" style="<%= thumbnailDivStyle %>">
				<img alt="" border="no" class="img-polaroid" src="<%= thumbnailSrc %>" style="<%= thumbnailStyle %>" />

				<c:if test="<%= shortcut %>">
					<img alt="<liferay-ui:message key="shortcut" />" class="shortcut-icon img-polaroid" src="<%= themeDisplay.getPathThemeImages() %>/file_system/large/overlay_link.png" />
				</c:if>

				<c:if test="<%= locked %>">
					<img alt="<liferay-ui:message key="locked" />" class="locked-icon img-polaroid" src="<%= themeDisplay.getPathThemeImages() %>/file_system/large/overlay_lock.png" />
				</c:if>
			</div>

			<div class="entry-metadata">
				<span class="entry-title">
					<span class="entry-title-text">
						<%= HtmlUtil.escape(title) %>
					</span>

					<span class="entry-result-icon"></span>
				</span>

				<span class="entry-description">
					<c:if test="<%= Validator.isNotNull(description) %>">
						<%= HtmlUtil.escape(description) %>
					</c:if>
				</span>
				<dl>
					<div class="entry-metadata-row" >
						<c:choose>
							<c:when test="<%= Validator.isNotNull(version) %>">
								<dt class="entry-version">
									<span class="entry-version-number">
										<liferay-ui:message key="version" />
									</span>
								</dt>
								<dd class="entry-version">
									<span class="entry-version-number">
										<%= String.valueOf(version) %>
									</span>
									<c:if test='<%= (status != -1) && (status == WorkflowConstants.STATUS_DRAFT || status == WorkflowConstants.STATUS_PENDING) %>' >
										<%
										String statusLabel = WorkflowConstants.toLabel(status);
										%>
										<span class="workflow-status-<%= statusLabel %>">
											(<liferay-ui:message key="<%= statusLabel %>" />)
										</span>
									</c:if>
								</dd>
							</c:when>

							<c:when test="<%= status != -1 %>">
								<dt class="entry-version">
									<span class="entry-version-number">
										<liferay-ui:message key="status" />
									</span>
								</dt>

								<%
								String statusLabel = WorkflowConstants.toLabel(status);
								%>

								<dd class="entry-version">
									<span class="workflow-status-<%= statusLabel %>">
										<liferay-ui:message key="<%= statusLabel %>" />
									</span>
								</dd>
							</c:when>
						</c:choose>
					</div>
					<c:if test="<%= (modifiedDate != null) && Validator.isNotNull(author) %>" >
						<div class="entry-metadata-row" >
							<c:choose>
								<c:when test="<%= (createDate != null) && modifiedDate.equals(createDate) %>">
									<dt class="entry-author">
										<liferay-ui:message key="created" />
									<dt>
								</c:when>
								<c:otherwise >
									<dt class="entry-author">
										<liferay-ui:message key="last-updated" />
									</dt>
								</c:otherwise>
							</c:choose>
							<dd class="entry-author">
								<liferay-ui:message arguments="<%= new String[] {LanguageUtil.getTimeDescription(locale, System.currentTimeMillis() - createDate.getTime(), true), author} %>" key="x-ago-by-x" />
							</dd>
						</div>
					</c:if>

					<%
					Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(locale, timeZone);
					%>
					<div class="entry-metadata-row" >
						<c:choose>
							<c:when test="<%= (displayDate != null) && (expirationDate != null) %>">
								<dt class="entry-schedule">
									<liferay-ui:message key="schedule" />
								</dt>
								<dd class="entry-schedule">
									<c:choose>
										<c:when test="<%= reviewDate != null %>">
											<liferay-ui:message arguments="<%= new String[] {HtmlUtil.escape(dateFormatDateTime.format(displayDate)), HtmlUtil.escape(dateFormatDateTime.format(expirationDate)), HtmlUtil.escape(dateFormatDateTime.format(reviewDate))} %>" key="x-to-x-review-date-x" />
										</c:when>
										<c:otherwise>
											<liferay-ui:message arguments="<%= new String[] {HtmlUtil.escape(dateFormatDateTime.format(displayDate)), HtmlUtil.escape(dateFormatDateTime.format(expirationDate))} %>" key="x-to-x" />
										</c:otherwise>
									</c:choose>
								</dd>
							</c:when>

							<c:when test="<%= (displayDate != null) && (expirationDate != null) %>">
								<dt class="entry-schedule">
									<liferay-ui:message key="display-date" />
								</dt>
								<dd class="entry-schedule">
									<c:choose>
										<c:when test="<%= reviewDate != null %>">
											<liferay-ui:message arguments="<%= new String[] {HtmlUtil.escape(dateFormatDateTime.format(displayDate)), HtmlUtil.escape(dateFormatDateTime.format(reviewDate))} %>" key="x-review-date-x" />
										</c:when>
										<c:otherwise>
											<liferay-ui:message key="<%= HtmlUtil.escape(dateFormatDateTime.format(displayDate)) %>" />
										</c:otherwise>
									</c:choose>
								</dd>
							</c:when>

							<c:when test="<%= (displayDate != null) && (expirationDate != null) %>">
								<dt class="entry-schedule">
									<liferay-ui:message key="expiration-date" />
								</dt>
								<dd class="entry-schedule">
								<c:choose>
									<c:when test="<%= reviewDate != null %>">
										<liferay-ui:message arguments="<%= new String[] {HtmlUtil.escape(dateFormatDateTime.format(expirationDate)), HtmlUtil.escape(dateFormatDateTime.format(reviewDate))} %>" key="x-review-date-x" />
									</c:when>
									<c:otherwise>
										<liferay-ui:message key="<%= HtmlUtil.escape(dateFormatDateTime.format(expirationDate)) %>" />
									</c:otherwise>
								</c:choose>
								</dd>
							</c:when>
						</c:choose>
					</div>
				</dl>

				<span class="entry-categories">
					<c:if test="<%= Validator.isNotNull(assetCategoryClassName) && (assetCategoryClassPK > 0) %>">
						<liferay-ui:asset-categories-summary
							className="<%= assetCategoryClassName %>"
							classPK="<%= assetCategoryClassPK %>"
						/>
					</c:if>
				</span>

				<span class="entry-tags">
					<c:if test="<%= Validator.isNotNull(assetTagClassName) && (assetTagClassPK > 0) %>">
						<liferay-ui:asset-tags-summary
							className="<%= assetTagClassName %>"
							classPK="<%= assetTagClassPK %>"
						/>
					</c:if>
				</span>
				<c:if test="<%= Validator.isNotNull(latestApprovedVersion) && (status == WorkflowConstants.STATUS_DRAFT || status == WorkflowConstants.STATUS_PENDING) %>">
					<dl class="entry-latest-approved-container">
						<div class="entry-metadata-row">
							<dt class="entry-latest-approved">
								<liferay-ui:message key="latest-aproved-version" />
							</dt>
							<dd class="entry-latest-approved">
								<liferay-ui:message arguments="<%= new String[] {latestApprovedVersion, latestApprovedVersionAuthor} %>" key="x-by-x" />
							</dd>
						</div>
					</dl>
				</c:if>
			</div>
			<c:choose>
				<c:when test="<%= Validator.isNull(url) %>">
					</span>
				</c:when>
				<c:otherwise>
					</a>
				</c:otherwise>
			</c:choose>

			<%
			if (!folder) {
				request.removeAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
			}
			%>

			<liferay-util:include page="<%= actionJsp %>" />

			<c:if test="<%= showCheckbox %>">
				<aui:input cssClass="overlay entry-selector" label="" name="<%= RowChecker.ROW_IDS + rowCheckerName %>" type="checkbox" value="<%= rowCheckerId %>" />
			</c:if>
		</div>
	</c:when>

	<c:when test='<%= displayStyle.equals("list") %>'>
		<div class="app-view-entry app-view-entry-taglib entry-display-style display-<%= displayStyle %> <%= locked ? "locked" : StringPool.BLANK %> <%= cssClass %>" <%= AUIUtil.buildData(data) %>>
			<liferay-ui:icon
				cssClass='<%= showCheckbox ? "app-view-entry app-view-entry-taglib entry-display-style selectable" : "app-view-entry app-view-entry-taglib entry-display-style" %>'
				data="<%= data %>"
				label="<%= true %>"
				linkCssClass="entry-link"
				localizeMessage="<%= false %>"
				message="<%= title %>"
				method="get"
				src="<%= thumbnailSrc %>"
				url="<%= url %>"
			/>

			<c:if test="<%= !folder && ((status == WorkflowConstants.STATUS_DRAFT) || (status == WorkflowConstants.STATUS_PENDING)) %>">

				<%
				String statusLabel = WorkflowConstants.toLabel(status);
				%>

				<span class="workflow-status-<%= statusLabel %>">
					(<liferay-ui:message key="<%= statusLabel %>" />)
				</span>
			</c:if>
		</div>
	</c:when>
</c:choose>