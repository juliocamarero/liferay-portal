'use strict';

import Router from 'metal-router/src/Router';
import Uri from 'metal-uri/src/Uri';
import utils from 'senna/src/utils/utils';

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

		this.createRoutes();
	}

	createRoute(route, extendConfig) {
		var config = Object.assign(
			{
				component: route.controller,
				fetch: true,
				fetchUrl: this.getFetchUrl.bind(this),
				path: this.matchPath.bind(this, route.mvcRenderCommandName)
			},
			extendConfig
		);

		if (config.path(utils.getCurrentBrowserPath())) {
			config.data = this.context;
			config.element = this.element;
			config.fetch = false;
		}

		new Router(config, this.portletWrapper);
	}

	_isDefaultPath(url) {
		var uri = new Uri(url);

		var hasMVCCommandNameParam = uri.hasParameter(this.portletNamespace + 'mvcRenderCommandName');

		return !hasMVCCommandNameParam && this.currentMVCRenderCommandName === this.defaultRoute.mvcRenderCommandName;
	}

	createRoutes() {
		var defaultRoute = this.defaultRoute;

		this.createRoute(
			{
				controller: defaultRoute.controller,
				mvcRenderCommandName: defaultRoute.mvcRenderCommandName
			},
			{
				path: this._isDefaultPath.bind(this)
			}
		);

		this.routes.forEach((route => this.createRoute(route)));

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