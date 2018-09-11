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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.server.core.SchemaBasedEdmProvider;

/**
 * <p>
 * Provider of the Common Schema Definition Language (CSDL) for an Entity Data
 * Model (EDM) used by a service.
 * </p>
 *
 * <p>
 * EDM is the underlying abstract data model used by OData services to
 * formalize the description of the resources it exposes. Meanwhile, CSDL
 * defines an XML-based representation of the entity model.
 *
 * @author David Arques
 * @review
 */
public abstract class BaseSingleEntitySchemaBasedEdmProvider
	extends SchemaBasedEdmProvider {

	public BaseSingleEntitySchemaBasedEdmProvider() {
		addSchema(
			_createCsdlSchema(
				"HypermediaRestApis", getName(),
				_createCsdlProperties(getEntityFieldTypesMap())));
	}

	/**
	 * Returns the properties of the entity field type used to create the EDM.
	 *
	 * @return the entity type properties
	 * @review
	 */
	public abstract Map<String, EntityFieldType> getEntityFieldTypesMap();

	/**
	 * Returns the name of the single entity type used to create the EDM.
	 *
	 * @return the entity type name
	 * @review
	 */
	public abstract String getName();

	public enum EntityFieldType {

		DATE, STRING

	}

	private CsdlEntityContainer _createCsdlEntityContainer(
		String namespace, String name) {

		CsdlEntityContainer csdlEntityContainer = new CsdlEntityContainer();

		csdlEntityContainer.setEntitySets(
			_createCsdlEntitySets(namespace, name));
		csdlEntityContainer.setName(name);

		return csdlEntityContainer;
	}

	private List<CsdlEntitySet> _createCsdlEntitySets(
		String namespace, String entityNameType) {

		CsdlEntitySet csdlEntitySet = new CsdlEntitySet();

		csdlEntitySet.setName(entityNameType);
		csdlEntitySet.setType(new FullQualifiedName(namespace, entityNameType));

		return Collections.singletonList(csdlEntitySet);
	}

	private CsdlEntityType _createCsdlEntityType(
		String name, List<CsdlProperty> csdlProperties) {

		CsdlEntityType csdlEntityType = new CsdlEntityType();

		csdlEntityType.setName(name);
		csdlEntityType.setProperties(csdlProperties);

		return csdlEntityType;
	}

	private List<CsdlProperty> _createCsdlProperties(
		Map<String, EntityFieldType> entityTypesMap) {

		Set<Map.Entry<String, EntityFieldType>> entries = entityTypesMap.entrySet();

		Stream<Map.Entry<String, EntityFieldType>> stream = entries.stream();

		return stream.map(
			entry -> _createCsdlProperty(entry.getKey(), entry.getValue())
		).collect(
			Collectors.toList()
		);
	}

	private CsdlProperty _createCsdlProperty(
		String name, EntityFieldType entityFieldType) {

		CsdlProperty csdlProperty = new CsdlProperty();

		csdlProperty.setName(name);

		FullQualifiedName fullQualifiedName = null;

		if (entityFieldType.equals(EntityFieldType.STRING)) {
			fullQualifiedName =
				EdmPrimitiveTypeKind.String.getFullQualifiedName();
		}
		else if (entityFieldType.equals(EntityFieldType.DATE)) {
			fullQualifiedName =
				EdmPrimitiveTypeKind.Date.getFullQualifiedName();
		}

		csdlProperty.setType(fullQualifiedName);

		return csdlProperty;
	}

	private CsdlSchema _createCsdlSchema(
		String namespace, String name, List<CsdlProperty> csdlProperties) {

		CsdlSchema csdlSchema = new CsdlSchema();

		csdlSchema.setEntityContainer(
			_createCsdlEntityContainer(namespace, name));
		csdlSchema.setEntityTypes(
			Collections.singletonList(
				_createCsdlEntityType(name, csdlProperties)));
		csdlSchema.setNamespace(namespace);

		return csdlSchema;
	}

}