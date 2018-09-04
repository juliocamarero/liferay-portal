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

import java.util.ArrayList;
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
		addSchema(_createCsdlSchema());
	}

	@Override
	public String getSingleEntityTypeName() {
		return _ENTITY_TYPE_NAME;
	}

	private CsdlEntityContainer _createCsdlEntityContainer() {
		CsdlEntityContainer csdlEntityContainer = new CsdlEntityContainer();

		csdlEntityContainer.setEntitySets(_createCsdlEntitySets());
		csdlEntityContainer.setName(_ENTITY_TYPE_NAME);

		return csdlEntityContainer;
	}

	private List<CsdlEntitySet> _createCsdlEntitySets() {
		CsdlEntitySet csdlEntitySet = new CsdlEntitySet();

		CsdlEntityType csdlEntityType = _createCsdlEntityType();

		csdlEntitySet.setName(csdlEntityType.getName());
		csdlEntitySet.setType(
			new FullQualifiedName(_NAMESPACE, csdlEntityType.getName()));

		return Collections.singletonList(csdlEntitySet);
	}

	private CsdlEntityType _createCsdlEntityType() {
		CsdlEntityType csdlEntityType = new CsdlEntityType();

		csdlEntityType.setName(_ENTITY_TYPE_NAME);

		List<CsdlProperty> csdlProperties = new ArrayList<>();

		CsdlProperty csdlProperty = _createCsdlProperty(
			"title", EdmPrimitiveTypeKind.String.getFullQualifiedName());

		csdlProperties.add(csdlProperty);

		csdlEntityType.setProperties(csdlProperties);

		return csdlEntityType;
	}

	private CsdlProperty _createCsdlProperty(
		String name, FullQualifiedName fullQualifiedName) {

		CsdlProperty csdlProperty = new CsdlProperty();

		csdlProperty.setName(name);
		csdlProperty.setType(fullQualifiedName);

		return csdlProperty;
	}

	private CsdlSchema _createCsdlSchema() {
		CsdlSchema csdlSchema = new CsdlSchema();

		csdlSchema.setEntityContainer(_createCsdlEntityContainer());
		csdlSchema.setEntityTypes(
			Collections.singletonList(_createCsdlEntityType()));
		csdlSchema.setNamespace(_NAMESPACE);

		return csdlSchema;
	}

	private static final String _ENTITY_TYPE_NAME = "StructuredContent";

	private static final String _NAMESPACE = "HypermediaRestApis";

}