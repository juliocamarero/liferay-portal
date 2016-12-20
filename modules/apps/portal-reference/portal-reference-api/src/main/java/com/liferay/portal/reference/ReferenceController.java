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

package com.liferay.portal.reference;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.reference.graph.ReferenceEdge;
import com.liferay.portal.reference.graph.ReferenceVertex;

import java.lang.reflect.Method;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jodd.bean.BeanUtil;

import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedMultigraph;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Mate Thurzo
 */
@Component(immediate = true, service = ReferenceController.class)
public class ReferenceController {

	public Graph<ReferenceVertex, ReferenceEdge> getReferenceGraph() {
		return _referenceGraph;
	}

	public void printGraph() {
		_log.error("###" + _referenceSupplierServiceMap.size());

		for (ReferenceSupplierService supplierService :
				_referenceSupplierServiceMap.values()) {

			_log.error(
				"###" + supplierService.getProcessingClass().getSimpleName());
		}

		_referenceGraph.edgeSet().stream().forEach(System.out::println);

		_referenceGraph.vertexSet().stream().forEach(
			vertex ->
				_log.error(vertex.getType() + " ... " + vertex.getUuid()));
	}

	public void processReferences() {
		_referenceSupplierServiceMap.values().stream().
			map(ss -> (ReferenceSupplierService<ClassedModel>)ss).
			forEach(
				(supplierService) ->
					_getEntities(supplierService).stream().
						filter(Objects::nonNull).map(e -> (ClassedModel)e).
						forEach(
							(entity) ->
								_processEntityReferences(
									entity, supplierService)));
	}

	@org.osgi.service.component.annotations.Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		service = ReferenceSupplierService.class,
		unbind = "unsetReferenceSupplierService"
	)
	protected void setReferenceSupplierService(
		ReferenceSupplierService<?> referenceSupplierService) {

		_referenceSupplierServiceMap.put(
			referenceSupplierService.getProcessingClass(),
			referenceSupplierService);
	}

	protected void unsetReferenceSupplierService(
		ReferenceSupplierService<?> referenceSupplierService) {

		_referenceSupplierServiceMap.remove(
			referenceSupplierService.getProcessingClass());
	}

	private ReferenceEdge _addEdge(
		ReferenceVertex sourceVertex, ReferenceVertex targetVertex) {

		if (!_referenceGraph.containsEdge(sourceVertex, targetVertex)) {
			return _referenceGraph.addEdge(sourceVertex, targetVertex);
		}

		return _referenceGraph.getEdge(sourceVertex, targetVertex);
	}

	private ReferenceVertex _addVertex(Object object) {
		ReferenceVertex vertex = _toVertex(object);

		if (!_referenceGraph.containsVertex(vertex)) {
			_referenceGraph.addVertex(vertex);
		}

		return vertex;
	}

	private Method _getDynamicQueryMethod(BaseLocalService baseLocalService) {
		Class<?> clazz = baseLocalService.getClass();

		try {
			return clazz.getMethod("dynamicQuery", DynamicQuery.class);
		}
		catch (NoSuchMethodException nsme) {
			return null;
		}
	}

	private List<? extends ClassedModel> _getEntities(
		ReferenceSupplierService<?> supplierService) {

		DynamicQuery dynamicQuery = supplierService.getDynamicQuery();

		BaseLocalService baseLocalService = supplierService.getLocalService();

		Method dynamicQueryMethod = _getDynamicQueryMethod(baseLocalService);

		if (dynamicQueryMethod == null) {
			return Collections.emptyList();
		}

		try {
			Object object = dynamicQueryMethod.invoke(
				baseLocalService, dynamicQuery);

			if (object instanceof List) {
				List entities = (List)object;

				if (ListUtil.isEmpty(entities)) {
					return Collections.emptyList();
				}

				return entities;
			}

			return Collections.emptyList();
		}
		catch (Exception e) {
			return Collections.emptyList();
		}
	}

	private <T extends ClassedModel> void _processEntityReferences(
		T entity, ReferenceSupplierService<T> supplierService) {

		// add the entity to the graph

		ReferenceVertex vertex = _addVertex(entity);

		// process references and add them to the graph

		supplierService.getReferenceSuppliers().referenceStream().
			//filter(Reference::isInbound).
			flatMap((reference) -> reference.supply(entity).stream()).
			filter(Objects::nonNull).
			forEach((targetReference) -> {
				ReferenceVertex targetVertex = _addVertex(targetReference);

				_addEdge(vertex, targetVertex);

				ReferenceSupplierService<? extends ClassedModel>
					targetSupplierService = _referenceSupplierServiceMap.get(
						targetReference.getClass());

				_processEntityReferences(
					targetReference,
					(ReferenceSupplierService<ClassedModel>)
						targetSupplierService);
			});
	}

	private ReferenceVertex _toVertex(Object object) {
		ReferenceVertex vertex = new ReferenceVertex();

		String uuid = GetterUtil.getString(
			BeanUtil.getPropertySilently(object, "uuid"));
		String type = object.getClass().getSimpleName();

		vertex.setType(type);
		vertex.setUuid(uuid);

		return vertex;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ReferenceController.class);

	private final Graph<ReferenceVertex, ReferenceEdge> _referenceGraph =
		new DirectedMultigraph<ReferenceVertex, ReferenceEdge>(
			ReferenceEdge.class);
	private final
		Map<Class<?>, ReferenceSupplierService<? extends ClassedModel>>
			_referenceSupplierServiceMap = new HashMap<>();

}