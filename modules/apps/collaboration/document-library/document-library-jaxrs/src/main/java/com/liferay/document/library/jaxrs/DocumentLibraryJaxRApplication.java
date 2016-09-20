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

package com.liferay.document.library.jaxrs;

import com.liferay.document.library.jaxrs.provider.CORSRequestFilter;
import com.liferay.document.library.jaxrs.provider.CompanyContextProvider;
import com.liferay.document.library.jaxrs.provider.OptionalBodyWriter;
import com.liferay.document.library.jaxrs.provider.OrderBySelectorContextProvider;
import com.liferay.document.library.jaxrs.provider.PageContainerMessageBodyWriter;
import com.liferay.document.library.jaxrs.provider.PaginationProvider;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.DefaultJaxrsScanner;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Sierra Andrés
 */
@ApplicationPath("/api")
@Component(immediate = true, service = Application.class)
public class DocumentLibraryJaxRApplication extends Application {

	@Activate
	protected void activate() {
		BeanConfig beanConfig = new BeanConfig();

		beanConfig.setTitle("Files");
		beanConfig.setDescription("Files with Document Library");
		beanConfig.setBasePath("/o/document-library/api");
		beanConfig.setResourcePackage("com.liferay.document.library.jaxrs");

		new SwaggerContextService()
			.withSwaggerConfig(beanConfig)
			.withScanner(new DefaultJaxrsScanner())
			.initConfig()
			.initScanner();
	}

	public Set<Object> getSingletons() {
		return Collections.<Object>singleton(_documentLibraryRootResource);
	}

	public Set<Class<?>> getClasses() {
		return new HashSet<>(
			Arrays.asList(
				ApiListingResource.class, SwaggerSerializers.class,
				CompanyContextProvider.class, CORSRequestFilter.class,
				OptionalBodyWriter.class, PaginationProvider.class,
				OrderBySelectorContextProvider.class,
				PageContainerMessageBodyWriter.class));
	}

	@Reference
	DocumentLibraryRootResource _documentLibraryRootResource;

}