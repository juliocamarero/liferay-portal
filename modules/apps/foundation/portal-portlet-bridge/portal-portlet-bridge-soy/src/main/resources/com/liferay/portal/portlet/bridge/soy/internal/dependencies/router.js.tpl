<script>
	var modules = $MODULES;

	require.apply(
		window,
		modules.concat(
			[
				'frontend-js-spa-web/liferay/router/SoyPortletRouter.es',
				function() {
					var SoyPortletRouter = arguments[arguments.length - 1].default;

					var modulesExports = Array.prototype.splice.call(arguments, 0, arguments.length - 1);

					var mvcRenderCommandNames = $MVC_RENDER_COMMAND_NAMES;

					var routes = modulesExports.map(
						function(moduleExports, index) {
							var controller = moduleExports.default;

							var mvcRenderCommandName = mvcRenderCommandNames[index];

							if (modules[index].indexOf('.es') === -1) {
								controller = moduleExports[mvcRenderCommandName.split('/').pop()];
							}

							return {
								controller: controller,
								mvcRenderCommandName: mvcRenderCommandName
							};
						}
					);

					window.portletRouter = new SoyPortletRouter(
						{
							context: $CONTEXT,
							currentMVCRenderCommandName: '$CURRENT_MVC_RENDER_COMMAND_NAME',
							defaultRoute: routes.find(
								function(route) {
									return route.mvcRenderCommandName === '$DEFAULT_MVC_COMMAND_NAME';
								}
							),
							element: '#$ELEMENT_ID',
							friendlyURLRoutes: $FRIENDLY_URL_ROUTES,
							friendlyURLMapping: '$FRIENDLY_URL_MAPPING',
							portletId: '$PORTLET_ID',
							portletNamespace: '$PORTLET_NAMESPACE',
							portletWrapper: '#$PORTLET_WRAPPER_ID',
							routes: routes
						}
					);
				},
				function(e) {
					console.log(e);
				}
			]
		)
	);
</script>