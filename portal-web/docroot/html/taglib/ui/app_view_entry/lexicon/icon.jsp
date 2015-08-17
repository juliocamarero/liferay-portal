<%--
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
--%>

<%@ include file="/html/taglib/ui/app_view_entry/init.jsp" %>

<div class="col-lg-4 display-<%= displayStyle %> entry-display-style <%= showCheckbox ? "selectable" : StringPool.BLANK %> <%= cssClass %>" <%= AUIUtil.buildData(data) %> data-draggable="<%= showCheckbox ? Boolean.TRUE.toString() : Boolean.FALSE.toString() %>" data-folder="<%= folder ? Boolean.TRUE.toString() : Boolean.FALSE.toString() %>" <%= folder ? "data-folder-id=\"" + rowCheckerId + "\"" : StringPool.BLANK %> data-title="<%= HtmlUtil.escapeAttribute(shortTitle) %>">
	<div class="checkbox checkbox-default toggle-card-dm">
		<aui:input cssClass="entry-selector" id="<%= rowCheckerId %>" label="" name="<%= RowChecker.ROW_IDS %>" title='<%= LanguageUtil.format(request, "select-x", HtmlUtil.escape(shortTitle)) %>' type="checkbox" value="<%= rowCheckerId %>" wrappedField="<%= true %>" />
		<div class="card card-dm toggle-card-container">
			<div class="aspect-ratio">
				<c:choose>
					<c:when test="<%= Validator.isNotNull(url) %>">
						<a href="<%= url %>" title="<%= linkTitle %>">
							<img alt="" src="<%= HtmlUtil.escapeAttribute(thumbnailSrc) %>" style="<%= thumbnailStyle %>" />
						</a>
					</c:when>
					<c:otherwise>
						<img alt="" src="<%= HtmlUtil.escapeAttribute(thumbnailSrc) %>" style="<%= thumbnailStyle %>" />
					</c:otherwise>
				</c:choose>

				<liferay-ui:user-display
					showLink="<%= false %>"
					showUserDetails="<%= false %>"
					showUserName="<%= false %>"
					userIconCssClass="sticker sticker-bottom"
					userId="<%= authorId %>"
					view="lexicon"
				/>
			</div>
			<div class="card-footer">
				<div class="card-dm-more-options">
					<liferay-util:include page="<%= actionJsp %>" servletContext="<%= actionJspServletContext %>" />
				</div>

				<div class="card-dm-details">
					<div class="card-dm-text-small">
						<liferay-ui:message arguments="<%= new String[] {LanguageUtil.getTimeDescription(locale, System.currentTimeMillis() - modifiedDate.getTime(), true), HtmlUtil.escape(author)} %>" key="x-ago-by-x" translateArguments="<%= false %>" />
					</div>

					<c:choose>
						<c:when test="<%= Validator.isNotNull(url) %>">
							<a href="<%= url %>" title="<%= linkTitle %>">
								<div class="card-dm-text-large"><%= HtmlUtil.escape(shortTitle) %></div>
							</a>
						</c:when>
						<c:otherwise>
							<div class="card-dm-text-large"><%= HtmlUtil.escape(shortTitle) %></div>
						</c:otherwise>
					</c:choose>

					<div class="card-dm-text-small">
						<aui:workflow-status showIcon="<%= false %>" showLabel="<%= false %>" status="<%= status %>" view="lexicon" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>