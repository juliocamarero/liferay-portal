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

package com.liferay.structured.content.apio.internal.architect.filter;

import java.util.Collections;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;

/**
 * Provides the entity data model from the Indexed Entity (JournalArticle).
 *
 * @author Julio Camarero
 * @review
 */
public class StructuredContentSingleEntitySchemaBasedEdmProvider
	extends BaseSingleEntitySchemaBasedEdmProvider {

	public StructuredContentSingleEntitySchemaBasedEdmProvider() {
		addSchema(
			_createCsdlSchema("HypermediaRestApis", getSingleEntityTypeName()));
	}

	@Override
	public String getSingleEntityTypeName() {
		return "StructuredContent";
	}

	private CsdlEntityContainer _createCsdlEntityContainer(
		String namespace, String entityTypeName) {

		CsdlEntityContainer csdlEntityContainer = new CsdlEntityContainer();

		csdlEntityContainer.setEntitySets(
			_createCsdlEntitySets(namespace, entityTypeName));
		csdlEntityContainer.setName(entityTypeName);

		return csdlEntityContainer;
	}

	private List<CsdlEntitySet> _createCsdlEntitySets(
		String namespace, String entityNameType) {

		CsdlEntitySet csdlEntitySet = new CsdlEntitySet();

		csdlEntitySet.setName(entityNameType);
		csdlEntitySet.setType(new FullQualifiedName(namespace, entityNameType));

		return Collections.singletonList(csdlEntitySet);
	}

	private CsdlEntityType _createCsdlEntityType(String entityTypeName) {
		CsdlEntityType csdlEntityType = new CsdlEntityType();

		csdlEntityType.setName(entityTypeName);

		csdlEntityType.setProperties(
			Collections.singletonList(
				_createCsdlProperty(
					"title",
					EdmPrimitiveTypeKind.String.getFullQualifiedName())));

		return csdlEntityType;
	}

	private CsdlProperty _createCsdlProperty(
		String name, FullQualifiedName fullQualifiedName) {

		CsdlProperty csdlProperty = new CsdlProperty();

		csdlProperty.setName(name);
		csdlProperty.setType(fullQualifiedName);

		return csdlProperty;
	}

	private CsdlSchema _createCsdlSchema(
		String namespace, String entityTypeNames) {

		CsdlSchema csdlSchema = new CsdlSchema();

		csdlSchema.setNamespace(namespace);

		csdlSchema.setEntityTypes(
			Collections.singletonList(_createCsdlEntityType(entityTypeNames)));

		csdlSchema.setEntityContainer(
			_createCsdlEntityContainer(namespace, entityTypeNames));

		return csdlSchema;
	}

}