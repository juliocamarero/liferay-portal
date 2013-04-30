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

package com.liferay.portlet.asset;

import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portlet.asset.model.AssetRendererFactory;

import java.util.List;

/**
 * @author Bruno Farache
 * @author Marcellus Tavares
 */
public class AssetRendererFactoryRegistryUtil {

	public static List<AssetRendererFactory> getActivePortletsAssetRendererFactories(
		long companyId) {
		
		return getAssetRendererFactoryRegistry().getActivePortletsAssetRendererFactories(
			companyId);
	}
	
	public static AssetRendererFactory getActivePortletAssetRendererFactoryByClassName(
		long companyId, String className) {

		return getAssetRendererFactoryRegistry().getActivePortletAssetRendererFactoryByClassName(
			companyId, className);
	}
	
	public static long[] getActivePortletsClassNameIds(long companyId) {
		
		return getAssetRendererFactoryRegistry().getActivePortletsClassNameIds(
			companyId);
	}
	
	public static List<AssetRendererFactory> getAssetRendererFactories() {
		return getAssetRendererFactoryRegistry().getAssetRendererFactories();
	}

	public static AssetRendererFactory getAssetRendererFactoryByClassName(
		String className) {

		return getAssetRendererFactoryRegistry().
			getAssetRendererFactoryByClassName(className);
	}

	public static AssetRendererFactory getAssetRendererFactoryByType(
		String type) {

		return getAssetRendererFactoryRegistry().getAssetRendererFactoryByType(
			type);
	}

	public static AssetRendererFactoryRegistry
		getAssetRendererFactoryRegistry() {

		PortalRuntimePermission.checkGetBeanProperty(
			AssetRendererFactoryRegistryUtil.class);

		return _assetRendererFactoryRegistry;
	}

	public static long[] getClassNameIds() {
		return getAssetRendererFactoryRegistry().getClassNameIds();
	}

	public static void register(AssetRendererFactory assetRendererFactory) {
		getAssetRendererFactoryRegistry().register(assetRendererFactory);
	}

	public static void register(
		List<AssetRendererFactory> assetRendererFactories) {

		for (AssetRendererFactory assetRendererFactory :
				assetRendererFactories) {

			register(assetRendererFactory);
		}
	}

	public static void unregister(AssetRendererFactory assetRendererFactory) {
		getAssetRendererFactoryRegistry().unregister(assetRendererFactory);
	}

	public static void unregister(
		List<AssetRendererFactory> assetRendererFactories) {

		for (AssetRendererFactory assetRendererFactory :
				assetRendererFactories) {

			unregister(assetRendererFactory);
		}
	}

	public void setAssetRendererFactoryRegistry(
		AssetRendererFactoryRegistry assetRendererFactoryRegistry) {

		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_assetRendererFactoryRegistry = assetRendererFactoryRegistry;
	}

	private static AssetRendererFactoryRegistry _assetRendererFactoryRegistry;

}