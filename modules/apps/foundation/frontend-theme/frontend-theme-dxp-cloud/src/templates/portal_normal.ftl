<!DOCTYPE html>

<#include init />

<html>

<head>
	<@liferay_util["include"] page=top_head_include />

	<link rel="stylesheet" href="//dashboard.wedeploy.com/build/vendor/westyle/build/fonts/icon-12/icon-12.css">
	<link rel="stylesheet" href="//dashboard.wedeploy.com/build/vendor/westyle/build/fonts/icon-16/icon-16.css">
	<link rel="stylesheet" href="//dashboard.wedeploy.com/build/dashboard.css">
</head>

<body>
	<@liferay_util["include"] page=body_top_include />

	<@liferay_portlet["runtime"]
		portletName="dxp_cloud_portlet"
	/>

	<@liferay_util["include"] page=body_bottom_include />

	<@liferay_util["include"] page=bottom_include />
</body>
</html>