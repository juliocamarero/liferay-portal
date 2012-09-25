package com.liferay.portlet.documentlibrary.util;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

public interface EditInlineFilter {
	String process(ActionRequest actionRequest,
			ActionResponse actionResponse) throws PortletException;
}
