package com.liferay.portlet.blogs.rss;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Organization;
import com.liferay.portlet.blogs.model.BlogsEntry;


public class OrganizationRSSRenderer extends BaseRSSRenderer {

	private Organization organization;

	public OrganizationRSSRenderer(
		Organization organization, List<BlogsEntry> blogsEntries, 
		HttpServletRequest request) {

		super(blogsEntries, request);
		this.organization = organization;
	}
	
	@Override
	public String getFeedURL() {
	
		return StringPool.BLANK;
	}
	
	@Override
	protected String getEntryURL() {
		return super.getFeedURL();
	}

	@Override
	public String getRSSName() {
		return organization.getName();
	}

}
