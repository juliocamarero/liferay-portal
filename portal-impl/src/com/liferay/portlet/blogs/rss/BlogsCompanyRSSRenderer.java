package com.liferay.portlet.blogs.rss;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portlet.blogs.model.BlogsEntry;


public class BlogsCompanyRSSRenderer extends BlogsBaseRSSRenderer {

	
	private Company company;

	public BlogsCompanyRSSRenderer(
		Company company, List<BlogsEntry> blogsEntries,
		HttpServletRequest request) {

		super(blogsEntries, request);
		this.company = company;
	}
	
	@Override
	public String getFeedURL() {
		return StringPool.BLANK;
	}
	
	@Override
	public String getRSSName() {

		try {
			return company.getName();
		}
		catch (Exception e) {
			return StringPool.BLANK;
		}
	}

	@Override
	protected String getEntryURL() throws PortalException, SystemException {
		return super.getFeedURL();
	}

}
