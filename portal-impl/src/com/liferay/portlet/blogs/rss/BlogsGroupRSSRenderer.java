package com.liferay.portlet.blogs.rss;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.blogs.model.BlogsEntry;


public class BlogsGroupRSSRenderer extends BlogsBaseRSSRenderer {

	private Group group;
	private long plid;
	private boolean isScopeGroup;

	public BlogsGroupRSSRenderer(
		Group group, List<BlogsEntry> blogsEntries, 
		HttpServletRequest request) {
		
		this(group, blogsEntries, request, false);
	}

	public BlogsGroupRSSRenderer(
		Group group, List<BlogsEntry> blogsEntries, 
		HttpServletRequest request, boolean isScopeGroup) {
		
		super(blogsEntries, request);
		this.group = group;
		this.isScopeGroup = isScopeGroup;
		this.plid = ParamUtil.getLong(getRequest(), "p_l_id");
	}
	
	@Override
	public String getFeedURL() {
		if (isScopeGroup) {
			return super.getFeedURL() + "p_l_id=" + plid;
		}
		
		try {
			return PortalUtil.getLayoutFullURL(getThemeDisplay()) +
					Portal.FRIENDLY_URL_SEPARATOR + "blogs/rss";
		}
		catch (Exception e) {
			return StringPool.BLANK;
		}
	}
	
	@Override
	protected String getEntryURL() {
		return getFeedURL();
	}

	@Override
	public String getRSSName() {

		try {
			return group.getDescriptiveName();
		}
		catch (Exception e) {
			return StringPool.BLANK;
		}
	}

}
