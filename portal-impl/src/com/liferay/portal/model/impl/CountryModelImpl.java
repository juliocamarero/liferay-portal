/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.Country;
import com.liferay.portal.model.CountryModel;
import com.liferay.portal.model.CountrySoap;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.List;

/**
 * The base model implementation for the Country service. Represents a row in the &quot;Country&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portal.model.CountryModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link CountryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CountryImpl
 * @see com.liferay.portal.model.Country
 * @see com.liferay.portal.model.CountryModel
 * @generated
 */
@JSON(strict = true)
public class CountryModelImpl extends BaseModelImpl<Country>
	implements CountryModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a country model instance should use the {@link com.liferay.portal.model.Country} interface instead.
	 */
	public static final String TABLE_NAME = "Country";
	public static final Object[][] TABLE_COLUMNS = {
			{ "countryId", Types.BIGINT },
			{ "name", Types.VARCHAR },
			{ "a2", Types.VARCHAR },
			{ "a3", Types.VARCHAR },
			{ "number_", Types.VARCHAR },
			{ "idd_", Types.VARCHAR },
			{ "zipRequired", Types.BOOLEAN },
			{ "active_", Types.BOOLEAN }
		};
	public static final String TABLE_SQL_CREATE = "create table Country (countryId LONG not null primary key,name VARCHAR(75) null,a2 VARCHAR(75) null,a3 VARCHAR(75) null,number_ VARCHAR(75) null,idd_ VARCHAR(75) null,zipRequired BOOLEAN,active_ BOOLEAN)";
	public static final String TABLE_SQL_DROP = "drop table Country";
	public static final String ORDER_BY_JPQL = " ORDER BY country.name ASC";
	public static final String ORDER_BY_SQL = " ORDER BY Country.name ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portal.model.Country"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portal.model.Country"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.model.Country"),
			true);
	public static long A2_COLUMN_BITMASK = 1L;
	public static long A3_COLUMN_BITMASK = 2L;
	public static long ACTIVE_COLUMN_BITMASK = 4L;
	public static long NAME_COLUMN_BITMASK = 8L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static Country toModel(CountrySoap soapModel) {
		Country model = new CountryImpl();

		model.setCountryId(soapModel.getCountryId());
		model.setName(soapModel.getName());
		model.setA2(soapModel.getA2());
		model.setA3(soapModel.getA3());
		model.setNumber(soapModel.getNumber());
		model.setIdd(soapModel.getIdd());
		model.setZipRequired(soapModel.getZipRequired());
		model.setActive(soapModel.getActive());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<Country> toModels(CountrySoap[] soapModels) {
		List<Country> models = new ArrayList<Country>(soapModels.length);

		for (CountrySoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portal.model.Country"));

	public CountryModelImpl() {
	}

	public long getPrimaryKey() {
		return _countryId;
	}

	public void setPrimaryKey(long primaryKey) {
		setCountryId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return new Long(_countryId);
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return Country.class;
	}

	public String getModelClassName() {
		return Country.class.getName();
	}

	@JSON
	public long getCountryId() {
		return _countryId;
	}

	public void setCountryId(long countryId) {
		_countryId = countryId;
	}

	@JSON
	public String getName() {
		if (_name == null) {
			return StringPool.BLANK;
		}
		else {
			return _name;
		}
	}

	public void setName(String name) {
		_columnBitmask |= NAME_COLUMN_BITMASK;

		if (_originalName == null) {
			_originalName = _name;
		}

		_name = name;
	}

	public String getOriginalName() {
		return GetterUtil.getString(_originalName);
	}

	@JSON
	public String getA2() {
		if (_a2 == null) {
			return StringPool.BLANK;
		}
		else {
			return _a2;
		}
	}

	public void setA2(String a2) {
		_columnBitmask |= A2_COLUMN_BITMASK;

		if (_originalA2 == null) {
			_originalA2 = _a2;
		}

		_a2 = a2;
	}

	public String getOriginalA2() {
		return GetterUtil.getString(_originalA2);
	}

	@JSON
	public String getA3() {
		if (_a3 == null) {
			return StringPool.BLANK;
		}
		else {
			return _a3;
		}
	}

	public void setA3(String a3) {
		_columnBitmask |= A3_COLUMN_BITMASK;

		if (_originalA3 == null) {
			_originalA3 = _a3;
		}

		_a3 = a3;
	}

	public String getOriginalA3() {
		return GetterUtil.getString(_originalA3);
	}

	@JSON
	public String getNumber() {
		if (_number == null) {
			return StringPool.BLANK;
		}
		else {
			return _number;
		}
	}

	public void setNumber(String number) {
		_number = number;
	}

	@JSON
	public String getIdd() {
		if (_idd == null) {
			return StringPool.BLANK;
		}
		else {
			return _idd;
		}
	}

	public void setIdd(String idd) {
		_idd = idd;
	}

	@JSON
	public boolean getZipRequired() {
		return _zipRequired;
	}

	public boolean isZipRequired() {
		return _zipRequired;
	}

	public void setZipRequired(boolean zipRequired) {
		_zipRequired = zipRequired;
	}

	@JSON
	public boolean getActive() {
		return _active;
	}

	public boolean isActive() {
		return _active;
	}

	public void setActive(boolean active) {
		_columnBitmask |= ACTIVE_COLUMN_BITMASK;

		if (!_setOriginalActive) {
			_setOriginalActive = true;

			_originalActive = _active;
		}

		_active = active;
	}

	public boolean getOriginalActive() {
		return _originalActive;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public Country toEscapedModel() {
		if (_escapedModelProxy == null) {
			_escapedModelProxy = (Country)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelProxyInterfaces,
					new AutoEscapeBeanHandler(this));
		}

		return _escapedModelProxy;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		if (_expandoBridge == null) {
			_expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(0,
					Country.class.getName(), getPrimaryKey());
		}

		return _expandoBridge;
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		getExpandoBridge().setAttributes(serviceContext);
	}

	@Override
	public Object clone() {
		CountryImpl countryImpl = new CountryImpl();

		countryImpl.setCountryId(getCountryId());
		countryImpl.setName(getName());
		countryImpl.setA2(getA2());
		countryImpl.setA3(getA3());
		countryImpl.setNumber(getNumber());
		countryImpl.setIdd(getIdd());
		countryImpl.setZipRequired(getZipRequired());
		countryImpl.setActive(getActive());

		countryImpl.resetOriginalValues();

		return countryImpl;
	}

	public int compareTo(Country country) {
		int value = 0;

		value = getName().compareTo(country.getName());

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		Country country = null;

		try {
			country = (Country)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = country.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public void resetOriginalValues() {
		CountryModelImpl countryModelImpl = this;

		countryModelImpl._originalName = countryModelImpl._name;

		countryModelImpl._originalA2 = countryModelImpl._a2;

		countryModelImpl._originalA3 = countryModelImpl._a3;

		countryModelImpl._originalActive = countryModelImpl._active;

		countryModelImpl._setOriginalActive = false;

		countryModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<Country> toCacheModel() {
		CountryCacheModel countryCacheModel = new CountryCacheModel();

		countryCacheModel.countryId = getCountryId();

		countryCacheModel.name = getName();

		String name = countryCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			countryCacheModel.name = null;
		}

		countryCacheModel.a2 = getA2();

		String a2 = countryCacheModel.a2;

		if ((a2 != null) && (a2.length() == 0)) {
			countryCacheModel.a2 = null;
		}

		countryCacheModel.a3 = getA3();

		String a3 = countryCacheModel.a3;

		if ((a3 != null) && (a3.length() == 0)) {
			countryCacheModel.a3 = null;
		}

		countryCacheModel.number = getNumber();

		String number = countryCacheModel.number;

		if ((number != null) && (number.length() == 0)) {
			countryCacheModel.number = null;
		}

		countryCacheModel.idd = getIdd();

		String idd = countryCacheModel.idd;

		if ((idd != null) && (idd.length() == 0)) {
			countryCacheModel.idd = null;
		}

		countryCacheModel.zipRequired = getZipRequired();

		countryCacheModel.active = getActive();

		return countryCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(17);

		sb.append("{countryId=");
		sb.append(getCountryId());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", a2=");
		sb.append(getA2());
		sb.append(", a3=");
		sb.append(getA3());
		sb.append(", number=");
		sb.append(getNumber());
		sb.append(", idd=");
		sb.append(getIdd());
		sb.append(", zipRequired=");
		sb.append(getZipRequired());
		sb.append(", active=");
		sb.append(getActive());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(28);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portal.model.Country");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>countryId</column-name><column-value><![CDATA[");
		sb.append(getCountryId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>name</column-name><column-value><![CDATA[");
		sb.append(getName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>a2</column-name><column-value><![CDATA[");
		sb.append(getA2());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>a3</column-name><column-value><![CDATA[");
		sb.append(getA3());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>number</column-name><column-value><![CDATA[");
		sb.append(getNumber());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>idd</column-name><column-value><![CDATA[");
		sb.append(getIdd());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>zipRequired</column-name><column-value><![CDATA[");
		sb.append(getZipRequired());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>active</column-name><column-value><![CDATA[");
		sb.append(getActive());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = Country.class.getClassLoader();
	private static Class<?>[] _escapedModelProxyInterfaces = new Class[] {
			Country.class
		};
	private long _countryId;
	private String _name;
	private String _originalName;
	private String _a2;
	private String _originalA2;
	private String _a3;
	private String _originalA3;
	private String _number;
	private String _idd;
	private boolean _zipRequired;
	private boolean _active;
	private boolean _originalActive;
	private boolean _setOriginalActive;
	private transient ExpandoBridge _expandoBridge;
	private long _columnBitmask;
	private Country _escapedModelProxy;
}