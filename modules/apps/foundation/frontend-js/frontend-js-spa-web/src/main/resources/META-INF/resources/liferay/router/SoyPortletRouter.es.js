import Component from 'metal-component/src/Component';
import Router from 'metal-router/src/Router';
import Uri from 'metal-uri/src/Uri';
import utils from 'senna/src/utils/utils';
import RequestScreen from 'senna/src/screen/RequestScreen';
import IncrementalDomRenderer from 'metal-incremental-dom/src/IncrementalDomRenderer';

class SoyPortletRouter {
	constructor(config) {
		this.context = config.context;
		this.currentMVCRenderCommandName = config.currentMVCRenderCommandName;
		this.defaultRoute = config.defaultRoute;
		this.element = config.element;
		this.friendlyURLRoutes = config.friendlyURLRoutes;
		this.friendlyURLMapping = config.friendlyURLMapping;
		this.portletId = config.portletId;
		this.portletNamespace = config.portletNamespace;
		this.portletWrapper = config.portletWrapper;
		this.routes = config.routes;

		this.createActionRoute();
		this.createRoutes();
		this.createFriendlyURLRoutes();
		this.createDefaultRoute();

		Router.router().dispatch();

		Liferay.once(
			'beforeScreenFlip',
			() => {
				Router.routerInstance.dispose();

				Router.routerInstance = null;
				Router.activeRouter = null;
			}
		);
	}

	getActiveComponent() {
		return Router.getActiveComponent();
	}

	getActiveState() {
		return Router.activeState;
	}

	getRouter() {
		return Router.router();
	}

	createActionRoute() {
		var instance = this;

		var defaultRoute = this.defaultRoute;

		this.createRoute(
			{
				controller: defaultRoute.controller
			},
			{
				path: (url) => {
					var uri = new Uri(url);

					var portletIdParam = uri.getParameterValue('p_p_id');
					var lifecycleParam = uri.getParameterValue('p_p_lifecycle');

					return lifecycleParam === '1' && portletIdParam === this.portletId;
				}
			}
		);
	}

	createDefaultRoute() {
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
	}

	createFriendlyURLRoutes() {
		this.friendlyURLRoutes.forEach(
			(friendlyURLRoute) => {
				let implicitParameters = friendlyURLRoute.implicitParameters;

				let route = this.routes.find(
					(route) => {
						return route.mvcRenderCommandName === implicitParameters.mvcRenderCommandName;
					}
				);

				this.createRoute(
					route,
					{
						path: (url) => {
							var uri = new Uri(url);

							var pathname = uri.getPathname();

							var currentPath = pathname.substring(pathname.indexOf('/-/') + 2);

							var mappedPath = '/' + this.friendlyURLMapping + friendlyURLRoute.pattern;

							return currentPath === mappedPath;
						}
					}
				)
			}
		);
	}

	isFriendlyURLRoute(url) {
		var friendlyURLRoute = this.friendlyURLRoutes.find(
			(friendlyURLRoute) => {
				var uri = new Uri(url);

				var pathname = uri.getPathname();

				var currentPath = pathname.substring(pathname.indexOf('/-/') + 2);

				var mappedPath = '/' + this.friendlyURLMapping + friendlyURLRoute.pattern;

				return currentPath === mappedPath;
			}
		);

		return !!friendlyURLRoute;
	}

	createRoute(route, extendConfig) {
		var instance = this;

		var config = Object.assign(
			{
				component: route.controller,
				element: this.element,
				fetch: true,
				fetchUrl: this.getFetchUrl.bind(this),
				path: this.matchPath.bind(this, route.mvcRenderCommandName),
				portletRoutes: this.routes
			},
			extendConfig
		);

		if (config.path(utils.getCurrentBrowserPath())) {
			config.data = this.context;
			config.fetch = false;
		}

		class DefaultScreen extends Router.defaultScreen {
			beforeUpdateHistoryPath() {
				var redirect = RequestScreen.prototype.beforeUpdateHistoryPath.apply(this, arguments);

				var uri = new Uri(redirect);

				uri.removeParameter('p_p_lifecycle');
				uri.removeParameter(instance.portletNamespace + 'pjax');

				return uri.toString();
			}
		}

		Router.defaultScreen = DefaultScreen;

		var router = new Router(config, this.portletWrapper);

		Liferay.once('beforeScreenFlip', () => router.dispose());
	}

	createRoutes() {
		this.routes.forEach((route) => this.createRoute(route));
	}

	getFetchUrl(url) {
		var uri = new Uri(url);

		uri.setParameterValue('p_p_lifecycle', '2');
		uri.setParameterValue(this.portletNamespace + 'pjax', true);

		return uri.toString();
	}

	matchPath(mvcRenderCommandName, url) {
		var uri = new Uri(url);

		var mvcRenderCommandNameParam = uri.getParameterValue(this.portletNamespace + 'mvcRenderCommandName');

		var portletIdParam = uri.getParameterValue('p_p_id');

		return mvcRenderCommandNameParam === mvcRenderCommandName && portletIdParam === this.portletId;
	}

	_isDefaultPath(url) {
		var uri = new Uri(url);

		if (uri.hasParameter(this.portletNamespace + 'mvcRenderCommandName')) {
			return false;
		}

		if (this.isFriendlyURLRoute(url)) {
			return false;
		}

		var currentURI = new Uri(Liferay.currentURL);

		if (uri.getPathname() === currentURI.getPathname()) {
			return true;
		}

		if (uri.getPathname() === themeDisplay.getLayoutRelativeURL()) {
			return true;
		}

		return false;
	}
}

Router.router().on('endNavigate', (event) => {
	if (event.error) {
		if (event.error.requestError || event.error.invalidStatus) {
			window.location.href = event.path;
		}
	}
});

export default SoyPortletRouter;