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

package com.liferay.structured.content.apio.internal.odata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.server.core.SchemaBasedEdmProvider;

/**
 * Provides the entity data model from the Indexed Entity (JournalArticle).
 *
 * @author Julio Camarero
 * @review
 */
public class StructuredContentEdmProvider extends SchemaBasedEdmProvider {

	public StructuredContentEdmProvider() {
		addSchema(_buildSchema());
	}

	private CsdlEntityContainer _buildEntityContainer() {
		CsdlEntityContainer csdlEntityContainer = new CsdlEntityContainer();

		CsdlEntityType entityType = _buildEntityType();

		FullQualifiedName entityTypeFqn = new FullQualifiedName(
			_NAMESPACE, entityType.getName());

		CsdlEntitySet csdlEntitySet = new CsdlEntitySet();

		csdlEntitySet.setName(entityType.getName());
		csdlEntitySet.setType(entityTypeFqn);

		csdlEntityContainer.setName(ODataConstants.ENTITY_NAME);
		csdlEntityContainer.setEntitySets(Arrays.asList(csdlEntitySet));

		return csdlEntityContainer;
	}

	private CsdlEntityType _buildEntityType() {
		CsdlProperty title = _buildProperty(
			"title", EdmPrimitiveTypeKind.String.getFullQualifiedName());

		CsdlEntityType csdlEntityType = new CsdlEntityType();

		csdlEntityType.setName(ODataConstants.ENTITY_NAME);
		csdlEntityType.setProperties(Arrays.asList(title));

		return csdlEntityType;
	}

	private CsdlProperty _buildProperty(String name, FullQualifiedName type) {
		CsdlProperty csdlProperty = new CsdlProperty();

		csdlProperty.setName(name);

		csdlProperty.setType(type);

		return csdlProperty;
	}

	private CsdlSchema _buildSchema() {

		// create Schema

		CsdlSchema schema = new CsdlSchema();

		schema.setNamespace(_NAMESPACE);

		// add EntityTypes

		List<CsdlEntityType> entityTypes = new ArrayList<>();

		entityTypes.add(_buildEntityType());

		schema.setEntityTypes(entityTypes);

		// add EntityContainer

		schema.setEntityContainer(_buildEntityContainer());

		return schema;
	}

	private static final String _NAMESPACE = "HeadlessApio";

}