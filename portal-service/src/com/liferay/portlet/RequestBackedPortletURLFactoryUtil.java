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

package com.liferay.portlet;

import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Adolfo Pérez
 * @author Roberto Díaz
 */
public class RequestBackedPortletURLFactoryUtil {

	public static RequestBackedPortletURLFactory create(
		HttpServletRequest request) {

		return new HttpServletRequestRequestBackedPortletURLFactory(request);
	}

	public static RequestBackedPortletURLFactory create(
		PortletRequest portletRequest) {

		PortletResponse portletResponse =
			(PortletResponse)portletRequest.getAttribute(
				JavaConstants.JAVAX_PORTLET_RESPONSE);

		if (portletResponse == null) {
			return create(PortalUtil.getHttpServletRequest(portletRequest));
		}

		return new LiferayPortletResponseRequestBackedPortletURLFactory(
			PortalUtil.getLiferayPortletResponse(portletResponse));
	}

	public static RequestBackedPortletURLFactory
		createControlPanelPortletURLFactory(
			HttpServletRequest request, Group group, long refererPlid) {

		return new RequestBackedControlPanelPortletURLFactory(
			request, group, refererPlid);
	}

	public static RequestBackedPortletURLFactory
		createControlPanelPortletURLFactory(
			HttpServletRequest request, long companyId, long refererPlid) {

		return new RequestBackedControlPanelPortletURLFactory(
			request, companyId, refererPlid);
	}

	public static RequestBackedPortletURLFactory
		createControlPanelPortletURLFactory(
			PortletRequest portletRequest, Group group, long refererPlid) {

		return new RequestBackedControlPanelPortletURLFactory(
			portletRequest, group, refererPlid);
	}

	public static RequestBackedPortletURLFactory
		createControlPanelPortletURLFactory(
			PortletRequest portletRequest, long companyId, long refererPlid) {

		return new RequestBackedControlPanelPortletURLFactory(
			portletRequest, companyId, refererPlid);
	}

	private static class HttpServletRequestRequestBackedPortletURLFactory
		implements RequestBackedPortletURLFactory {

		@Override
		public PortletURL createActionURL(String portletId) {
			String actionPhase = PortletRequest.ACTION_PHASE;

			return createPortletURL(portletId, actionPhase);
		}

		@Override
		public PortletURL createRenderURL(String portletId) {
			return createPortletURL(portletId, PortletRequest.RENDER_PHASE);
		}

		@Override
		public PortletURL createResourceURL(String portletId) {
			return createPortletURL(portletId, PortletRequest.RESOURCE_PHASE);
		}

		protected PortletURL createPortletURL(
			String portletId, String lifecycle) {

			ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
				WebKeys.THEME_DISPLAY);

			return PortletURLFactoryUtil.create(
				_request, portletId, themeDisplay.getPlid(), lifecycle);
		}

		private HttpServletRequestRequestBackedPortletURLFactory(
			HttpServletRequest request) {

			_request = request;
		}

		private final HttpServletRequest _request;

	}

	private static class LiferayPortletResponseRequestBackedPortletURLFactory
		implements RequestBackedPortletURLFactory {

		@Override
		public PortletURL createActionURL(String portletId) {
			return _liferayPortletResponse.createActionURL(portletId);
		}

		@Override
		public PortletURL createRenderURL(String portletId) {
			return _liferayPortletResponse.createRenderURL(portletId);
		}

		@Override
		public PortletURL createResourceURL(String portletId) {
			return _liferayPortletResponse.createResourceURL(portletId);
		}

		private LiferayPortletResponseRequestBackedPortletURLFactory(
			LiferayPortletResponse liferayPortletResponse) {

			_liferayPortletResponse = liferayPortletResponse;
		}

		private final LiferayPortletResponse _liferayPortletResponse;

	}

	private static class RequestBackedControlPanelPortletURLFactory
		implements RequestBackedPortletURLFactory {

		public RequestBackedControlPanelPortletURLFactory(
			HttpServletRequest request, Group group, long refererPlid) {

			this(group.getCompanyId(), group, refererPlid);

			_request = request;
		}

		public RequestBackedControlPanelPortletURLFactory(
			HttpServletRequest request, long companyId, long refererPlid) {

			this(companyId, null, refererPlid);

			_request = request;
		}

		public RequestBackedControlPanelPortletURLFactory(
			PortletRequest portletRequest, Group group, long refererPlid) {

			this(group.getCompanyId(), group, refererPlid);

			_portletRequest = portletRequest;
		}

		public RequestBackedControlPanelPortletURLFactory(
			PortletRequest portletRequest, long companyId, long refererPlid) {

			this(companyId, null, refererPlid);

			_portletRequest = portletRequest;
		}

		@Override
		public PortletURL createActionURL(String portletId) {
			return createControlPanelPortletURL(
				portletId, PortletRequest.ACTION_PHASE);
		}

		@Override
		public PortletURL createRenderURL(String portletId) {
			return createControlPanelPortletURL(
				portletId, PortletRequest.RENDER_PHASE);
		}

		@Override
		public PortletURL createResourceURL(String portletId) {
			return createControlPanelPortletURL(
				portletId, PortletRequest.RESOURCE_PHASE);
		}

		protected PortletURL createControlPanelPortletURL(
			String portletId, String lifecycle) {

			LiferayPortletURL liferayPortletURL = null;

			Layout controlPanelLayout = PortalUtil.getControlPanelLayout(
				_companyId, _group);

			if (_request != null) {
				liferayPortletURL = PortletURLFactoryUtil.create(
					_request, portletId, controlPanelLayout, lifecycle);
			}
			else {
				liferayPortletURL = PortletURLFactoryUtil.create(
					_portletRequest, portletId, controlPanelLayout, lifecycle);
			}

			liferayPortletURL.setLifecycle(lifecycle);

			if (_refererPlid > 0) {
				liferayPortletURL.setRefererPlid(_refererPlid);
			}

			try {
				liferayPortletURL.setWindowState(WindowState.MAXIMIZED);
			}
			catch (WindowStateException wse) {
			}

			return liferayPortletURL;
		}

		private RequestBackedControlPanelPortletURLFactory(
			long companyId, Group group, long refererPlid) {

			_companyId = companyId;
			_group = group;
			_refererPlid = refererPlid;
		}

		private final long _companyId;
		private final Group _group;
		private PortletRequest _portletRequest;
		private final long _refererPlid;
		private HttpServletRequest _request;

	}

}