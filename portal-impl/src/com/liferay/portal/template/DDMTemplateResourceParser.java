/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.template;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.DDMTemplateResource;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.dynamicdatamapping.NoSuchTemplateException;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateServiceUtil;

/**
 * @author Tina Tian
 * @author Juan Fernández
 *
 */
public class DDMTemplateResourceParser implements TemplateResourceParser {

	public TemplateResource getTemplateResource(String templateId)
		throws TemplateException {

		int pos = templateId.indexOf(
			TemplateConstants.TEMPLATES_SEPARATOR + StringPool.SLASH);

		if (pos == -1) {
			return null;
		}

		try {
			int x = templateId.indexOf(CharPool.SLASH, pos);

			long ddmTemplateId = Long.valueOf(templateId.substring(x + 1));

			if (_log.isDebugEnabled()) {
				_log.debug("Loading ddmTemplateId=" + ddmTemplateId);
			}

			DDMTemplate ddmTemplate = DDMTemplateServiceUtil.getTemplate(
				ddmTemplateId);

			return new DDMTemplateResource(
				ddmTemplate.getTemplateKey(), ddmTemplate);
		}
		catch (NoSuchTemplateException nste) {
			return null;
		}
		catch (Exception e) {
			throw new TemplateException(
				"Unable to find template " + templateId, e);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		DDMTemplateResourceParser.class);

}