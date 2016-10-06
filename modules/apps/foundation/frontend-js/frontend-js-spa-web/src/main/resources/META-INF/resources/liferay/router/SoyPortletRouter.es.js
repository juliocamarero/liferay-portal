'use strict';

import Router from 'metal-router/src/Router';
import Uri from 'metal-uri/src/Uri';
import Component from 'metal-component/src/Component';


class SoyPortletRouter {
	constructor(config) {
		this.context = config.context;
		this.currentMVCRenderCommandName = config.currentMVCRenderCommandName;
		this.defaultRoute = config.defaultRoute;
		this.element = config.element;
		this.portletId = config.portletId;
		this.portletNamespace = config.portletNamespace;
		this.portletWrapper = config.portletWrapper;
		this.routes = config.routes;

		this.addedCommands = {};

		this.createRoutes();
	}

	createRoute(route, extendConfig) {
		var config = {
			component: route.controller,
			fetch: true,
			fetchUrl: this.getFetchUrl.bind(this),
			path: this.matchPath.bind(this, route.mvcRenderCommandName)
		};

		if (route.mvcRenderCommandName === this.currentMVCRenderCommandName) {
			if (!this.addedCommands[route.mvcRenderCommandName]) {
				config.element = this.element;
				config.data = this.context;
				config.fetch = false;

				this.addedCommands[route.mvcRenderCommandName] = true;
			}
		}

		new Router(Object.assign(config, extendConfig), this.portletWrapper);
	}

	createRoutes() {
		var wrapperNode = document.querySelector(this.portletWrapper);

		this.createRoute(
			{
				controller: this.defaultRoute.controller,
				mvcRenderCommandName: this.defaultRoute.mvcRenderCommandName
			},
			{
				fetch: false,
				data: this.context,
				path: (url) => {
					var uri = new Uri(url);

					if (!uri.hasParameter(this.portletNamespace + 'mvcRenderCommandName')) {
						if (this.currentMVCRenderCommandName === this.defaultRoute.mvcRenderCommandName) {
							return true;
						}
					}

					return false;
				}
			}
		);

		this.routes.forEach((route) => this.createRoute(route));

		Router.router().dispatch();
	}

	getFetchUrl(url) {
		var uri = new Uri(url);

		uri.setParameterValue('p_p_lifecycle', '2');

		return uri.toString();
	}

	matchPath(mvcRenderCommandName, url) {
		var uri = new Uri(url);

		var mvcRenderCommandNameParam = uri.getParameterValue(this.portletNamespace + 'mvcRenderCommandName');

		var portletIdParam = uri.getParameterValue('p_p_id');

		return mvcRenderCommandNameParam === mvcRenderCommandName && portletIdParam === this.portletId;
	}
}

export default SoyPortletRouter;