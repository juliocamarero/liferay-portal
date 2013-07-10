package com.liferay.portlet.blogs.rss;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portlet.blogs.model.BlogsEntry;


public class CompanyRSSRenderer extends BaseRSSRenderer {

	
	private Company company;

	public CompanyRSSRenderer(
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
	protected String getEntryURL() {
		return super.getFeedURL();
	}

}
