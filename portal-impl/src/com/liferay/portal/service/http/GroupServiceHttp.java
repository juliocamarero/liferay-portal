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

package com.liferay.portal.service.http;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.security.auth.HttpPrincipal;
import com.liferay.portal.service.GroupServiceUtil;

/**
 * Provides the HTTP utility for the
 * {@link com.liferay.portal.service.GroupServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it requires an additional
 * {@link com.liferay.portal.security.auth.HttpPrincipal} parameter.
 *
 * <p>
 * The benefits of using the HTTP utility is that it is fast and allows for
 * tunneling without the cost of serializing to text. The drawback is that it
 * only works with Java.
 * </p>
 *
 * <p>
 * Set the property <b>tunnel.servlet.hosts.allowed</b> in portal.properties to
 * configure security.
 * </p>
 *
 * <p>
 * The HTTP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see GroupServiceSoap
 * @see com.liferay.portal.security.auth.HttpPrincipal
 * @see com.liferay.portal.service.GroupServiceUtil
 * @generated
 */
@ProviderType
public class GroupServiceHttp {
	public static com.liferay.portal.model.Group addGroup(
		HttpPrincipal httpPrincipal, long parentGroupId, long liveGroupId,
		java.lang.String name, java.lang.String description, int type,
		boolean manualMembership, int membershipRestriction,
		java.lang.String friendlyURL, boolean site, boolean active,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"addGroup", _addGroupParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					parentGroupId, liveGroupId, name, description, type,
					manualMembership, membershipRestriction, friendlyURL, site,
					active, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.portal.model.Group)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static void addRoleGroups(HttpPrincipal httpPrincipal, long roleId,
		long[] groupIds)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"addRoleGroups", _addRoleGroupsParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(methodKey, roleId,
					groupIds);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static void checkRemoteStagingGroup(HttpPrincipal httpPrincipal,
		long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"checkRemoteStagingGroup",
					_checkRemoteStagingGroupParameterTypes2);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static void deleteGroup(HttpPrincipal httpPrincipal, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"deleteGroup", _deleteGroupParameterTypes3);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static void disableStaging(HttpPrincipal httpPrincipal, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"disableStaging", _disableStagingParameterTypes4);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static void enableStaging(HttpPrincipal httpPrincipal, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"enableStaging", _enableStagingParameterTypes5);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.portal.model.Group getCompanyGroup(
		HttpPrincipal httpPrincipal, long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getCompanyGroup", _getCompanyGroupParameterTypes6);

			MethodHandler methodHandler = new MethodHandler(methodKey, companyId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.portal.model.Group)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.portal.model.Group getGroup(
		HttpPrincipal httpPrincipal, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getGroup", _getGroupParameterTypes7);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.portal.model.Group)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.portal.model.Group getGroup(
		HttpPrincipal httpPrincipal, long companyId, java.lang.String name)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getGroup", _getGroupParameterTypes8);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					companyId, name);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.portal.model.Group)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.model.Group> getGroups(
		HttpPrincipal httpPrincipal, long companyId, long parentGroupId,
		boolean site)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getGroups", _getGroupsParameterTypes9);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					companyId, parentGroupId, site);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.model.Group>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.model.Group> getManageableSiteGroups(
		HttpPrincipal httpPrincipal,
		java.util.Collection<com.liferay.portal.model.Portlet> portlets, int max)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getManageableSiteGroups",
					_getManageableSiteGroupsParameterTypes10);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					portlets, max);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.model.Group>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.model.Group> getOrganizationsGroups(
		HttpPrincipal httpPrincipal,
		java.util.List<com.liferay.portal.model.Organization> organizations)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getOrganizationsGroups",
					_getOrganizationsGroupsParameterTypes11);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					organizations);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.model.Group>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.portal.model.Group getUserGroup(
		HttpPrincipal httpPrincipal, long companyId, long userId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getUserGroup", _getUserGroupParameterTypes12);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					companyId, userId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.portal.model.Group)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.model.Group> getUserGroupsGroups(
		HttpPrincipal httpPrincipal,
		java.util.List<com.liferay.portal.model.UserGroup> userGroups)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getUserGroupsGroups", _getUserGroupsGroupsParameterTypes13);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					userGroups);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.model.Group>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.model.Group> getUserOrganizationsGroups(
		HttpPrincipal httpPrincipal, long userId, int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getUserOrganizationsGroups",
					_getUserOrganizationsGroupsParameterTypes14);

			MethodHandler methodHandler = new MethodHandler(methodKey, userId,
					start, end);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.model.Group>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.model.Group> getUserSitesGroups(
		HttpPrincipal httpPrincipal)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getUserSitesGroups", _getUserSitesGroupsParameterTypes15);

			MethodHandler methodHandler = new MethodHandler(methodKey);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.model.Group>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.model.Group> getUserSitesGroups(
		HttpPrincipal httpPrincipal, long userId,
		java.lang.String[] classNames, boolean includeControlPanel, int max)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getUserSitesGroups", _getUserSitesGroupsParameterTypes16);

			MethodHandler methodHandler = new MethodHandler(methodKey, userId,
					classNames, includeControlPanel, max);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.model.Group>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.model.Group> getUserSitesGroups(
		HttpPrincipal httpPrincipal, long userId,
		java.lang.String[] classNames, int max)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getUserSitesGroups", _getUserSitesGroupsParameterTypes17);

			MethodHandler methodHandler = new MethodHandler(methodKey, userId,
					classNames, max);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.model.Group>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.model.Group> getUserSitesGroups(
		HttpPrincipal httpPrincipal, java.lang.String[] classNames, int max)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getUserSitesGroups", _getUserSitesGroupsParameterTypes18);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					classNames, max);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.model.Group>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static int getUserSitesGroupsCount(HttpPrincipal httpPrincipal)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"getUserSitesGroupsCount",
					_getUserSitesGroupsCountParameterTypes19);

			MethodHandler methodHandler = new MethodHandler(methodKey);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return ((Integer)returnObj).intValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static boolean hasUserGroup(HttpPrincipal httpPrincipal,
		long userId, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"hasUserGroup", _hasUserGroupParameterTypes20);

			MethodHandler methodHandler = new MethodHandler(methodKey, userId,
					groupId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return ((Boolean)returnObj).booleanValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.model.Group> search(
		HttpPrincipal httpPrincipal, long companyId, long[] classNameIds,
		java.lang.String keywords,
		java.util.LinkedHashMap<java.lang.String, java.lang.Object> params,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.model.Group> obc)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"search", _searchParameterTypes21);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					companyId, classNameIds, keywords, params, start, end, obc);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.model.Group>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.model.Group> search(
		HttpPrincipal httpPrincipal, long companyId, long[] classNameIds,
		java.lang.String name, java.lang.String description,
		java.util.LinkedHashMap<java.lang.String, java.lang.Object> params,
		boolean andOperator, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.model.Group> obc)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"search", _searchParameterTypes22);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					companyId, classNameIds, name, description, params,
					andOperator, start, end, obc);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.model.Group>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.model.Group> search(
		HttpPrincipal httpPrincipal, long companyId, java.lang.String name,
		java.lang.String description, java.lang.String[] params, int start,
		int end) throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"search", _searchParameterTypes23);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					companyId, name, description, params, start, end);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.model.Group>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static int searchCount(HttpPrincipal httpPrincipal, long companyId,
		java.lang.String name, java.lang.String description,
		java.lang.String[] params) {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"searchCount", _searchCountParameterTypes24);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					companyId, name, description, params);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return ((Integer)returnObj).intValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static void setRoleGroups(HttpPrincipal httpPrincipal, long roleId,
		long[] groupIds)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"setRoleGroups", _setRoleGroupsParameterTypes25);

			MethodHandler methodHandler = new MethodHandler(methodKey, roleId,
					groupIds);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static void unsetRoleGroups(HttpPrincipal httpPrincipal,
		long roleId, long[] groupIds)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"unsetRoleGroups", _unsetRoleGroupsParameterTypes26);

			MethodHandler methodHandler = new MethodHandler(methodKey, roleId,
					groupIds);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.portal.model.Group updateFriendlyURL(
		HttpPrincipal httpPrincipal, long groupId, java.lang.String friendlyURL)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"updateFriendlyURL", _updateFriendlyURLParameterTypes27);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					friendlyURL);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.portal.model.Group)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.portal.model.Group updateGroup(
		HttpPrincipal httpPrincipal, long groupId, long parentGroupId,
		java.lang.String name, java.lang.String description, int type,
		boolean manualMembership, int membershipRestriction,
		java.lang.String friendlyURL, boolean active,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"updateGroup", _updateGroupParameterTypes28);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					parentGroupId, name, description, type, manualMembership,
					membershipRestriction, friendlyURL, active, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.portal.model.Group)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.portal.model.Group updateGroup(
		HttpPrincipal httpPrincipal, long groupId, java.lang.String typeSettings)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"updateGroup", _updateGroupParameterTypes29);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					typeSettings);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.portal.model.Group)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static void updateStagedPortlets(HttpPrincipal httpPrincipal,
		long groupId,
		java.util.Map<java.lang.String, java.lang.String> stagedPortletIds)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(GroupServiceUtil.class,
					"updateStagedPortlets",
					_updateStagedPortletsParameterTypes30);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					stagedPortletIds);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(GroupServiceHttp.class);
	private static final Class<?>[] _addGroupParameterTypes0 = new Class[] {
			long.class, long.class, java.lang.String.class,
			java.lang.String.class, int.class, boolean.class, int.class,
			java.lang.String.class, boolean.class, boolean.class,
			com.liferay.portal.service.ServiceContext.class
		};
	private static final Class<?>[] _addRoleGroupsParameterTypes1 = new Class[] {
			long.class, long[].class
		};
	private static final Class<?>[] _checkRemoteStagingGroupParameterTypes2 = new Class[] {
			long.class
		};
	private static final Class<?>[] _deleteGroupParameterTypes3 = new Class[] {
			long.class
		};
	private static final Class<?>[] _disableStagingParameterTypes4 = new Class[] {
			long.class
		};
	private static final Class<?>[] _enableStagingParameterTypes5 = new Class[] {
			long.class
		};
	private static final Class<?>[] _getCompanyGroupParameterTypes6 = new Class[] {
			long.class
		};
	private static final Class<?>[] _getGroupParameterTypes7 = new Class[] {
			long.class
		};
	private static final Class<?>[] _getGroupParameterTypes8 = new Class[] {
			long.class, java.lang.String.class
		};
	private static final Class<?>[] _getGroupsParameterTypes9 = new Class[] {
			long.class, long.class, boolean.class
		};
	private static final Class<?>[] _getManageableSiteGroupsParameterTypes10 = new Class[] {
			java.util.Collection.class, int.class
		};
	private static final Class<?>[] _getOrganizationsGroupsParameterTypes11 = new Class[] {
			java.util.List.class
		};
	private static final Class<?>[] _getUserGroupParameterTypes12 = new Class[] {
			long.class, long.class
		};
	private static final Class<?>[] _getUserGroupsGroupsParameterTypes13 = new Class[] {
			java.util.List.class
		};
	private static final Class<?>[] _getUserOrganizationsGroupsParameterTypes14 = new Class[] {
			long.class, int.class, int.class
		};
	private static final Class<?>[] _getUserSitesGroupsParameterTypes15 = new Class[] {
			
		};
	private static final Class<?>[] _getUserSitesGroupsParameterTypes16 = new Class[] {
			long.class, java.lang.String[].class, boolean.class, int.class
		};
	private static final Class<?>[] _getUserSitesGroupsParameterTypes17 = new Class[] {
			long.class, java.lang.String[].class, int.class
		};
	private static final Class<?>[] _getUserSitesGroupsParameterTypes18 = new Class[] {
			java.lang.String[].class, int.class
		};
	private static final Class<?>[] _getUserSitesGroupsCountParameterTypes19 = new Class[] {
			
		};
	private static final Class<?>[] _hasUserGroupParameterTypes20 = new Class[] {
			long.class, long.class
		};
	private static final Class<?>[] _searchParameterTypes21 = new Class[] {
			long.class, long[].class, java.lang.String.class,
			java.util.LinkedHashMap.class, int.class, int.class,
			com.liferay.portal.kernel.util.OrderByComparator.class
		};
	private static final Class<?>[] _searchParameterTypes22 = new Class[] {
			long.class, long[].class, java.lang.String.class,
			java.lang.String.class, java.util.LinkedHashMap.class, boolean.class,
			int.class, int.class,
			com.liferay.portal.kernel.util.OrderByComparator.class
		};
	private static final Class<?>[] _searchParameterTypes23 = new Class[] {
			long.class, java.lang.String.class, java.lang.String.class,
			java.lang.String[].class, int.class, int.class
		};
	private static final Class<?>[] _searchCountParameterTypes24 = new Class[] {
			long.class, java.lang.String.class, java.lang.String.class,
			java.lang.String[].class
		};
	private static final Class<?>[] _setRoleGroupsParameterTypes25 = new Class[] {
			long.class, long[].class
		};
	private static final Class<?>[] _unsetRoleGroupsParameterTypes26 = new Class[] {
			long.class, long[].class
		};
	private static final Class<?>[] _updateFriendlyURLParameterTypes27 = new Class[] {
			long.class, java.lang.String.class
		};
	private static final Class<?>[] _updateGroupParameterTypes28 = new Class[] {
			long.class, long.class, java.lang.String.class,
			java.lang.String.class, int.class, boolean.class, int.class,
			java.lang.String.class, boolean.class,
			com.liferay.portal.service.ServiceContext.class
		};
	private static final Class<?>[] _updateGroupParameterTypes29 = new Class[] {
			long.class, java.lang.String.class
		};
	private static final Class<?>[] _updateStagedPortletsParameterTypes30 = new Class[] {
			long.class, java.util.Map.class
		};
}