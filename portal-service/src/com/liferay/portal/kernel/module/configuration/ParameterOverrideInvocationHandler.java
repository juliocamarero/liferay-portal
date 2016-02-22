/*
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

package com.liferay.portal.kernel.module.configuration;

import com.liferay.portal.kernel.settings.LocalizedValuesMap;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Iván Zaera
 */
public class ParameterOverrideInvocationHandler<S>
	implements InvocationHandler {

	public ParameterOverrideInvocationHandler(
		Class<S> clazz, Object configurationInstance,
		Map<String, String[]> parameterMap) {

		_configurationInstance = configurationInstance;
		_parameterMap = parameterMap;
	}

	public S createProxy() {
		Class clazz = _configurationInstance.getClass();

		return (S)ProxyUtil.newProxyInstance(
			clazz.getClassLoader(), new Class[] {clazz}, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
		throws InvocationTargetException {

		Object result = null;

		try {
			result = _invokeMap(method);
		} catch (Exception e) {
		}

		if (result != null) {
			return result;
		}

		try {
			return _invokeConfigurationInstance(method, args);
		} catch (Exception e) {
			return null;
		}
	}

	private Object _invokeConfigurationInstance(Method method, Object[] args)
		throws IllegalAccessException, InvocationTargetException,
			   NoSuchMethodException {

		Class<?> clazz = _configurationInstance.getClass();

		method = clazz.getMethod(method.getName(), method.getParameterTypes());

		return method.invoke(_configurationInstance, args);
	}

	private Object _invokeMap(Method method)
		throws NoSuchMethodException, IllegalAccessException,
			   InvocationTargetException, InstantiationException {

		Class<?> returnType = method.getReturnType();

		if (returnType.equals(boolean.class)) {
			return Boolean.valueOf(_getMapValue(method.getName()));
		}
		else if (returnType.equals(double.class)) {
			return Double.valueOf(_getMapValue(method.getName()));
		}
		else if (returnType.equals(float.class)) {
			return Float.valueOf(_getMapValue(method.getName()));
		}
		else if (returnType.equals(int.class)) {
			return Integer.valueOf(_getMapValue(method.getName()));
		}
		else if (returnType.equals(LocalizedValuesMap.class)) {
			return _getMapValue(method.getName());
		}
		else if (returnType.equals(long.class)) {
			return Long.valueOf(_getMapValue(method.getName()));
		}
		else if (returnType.equals(String.class)) {
			return _getMapValue(method.getName());
		}
		else if (returnType.equals(String[].class)) {
			return _parameterMap.get(method.getName());
		}
		else if (returnType.isEnum()) {
			Method valueOfMethod = returnType.getDeclaredMethod(
				"valueOf", String.class);

			return valueOfMethod.invoke(
				returnType, _parameterMap.get(method.getName()));
		}

		Constructor<?> constructor = returnType.getConstructor(String.class);

		return constructor.newInstance(
			_parameterMap.get(method.getName()));
	}

	private String _getMapValue(String name) {
		String[] values = _parameterMap.get(name);

		if (values == null) {
			return null;
		}

		return values[0];
	}

	private final Object _configurationInstance;
	private final Map<String, String[]> _parameterMap;

}