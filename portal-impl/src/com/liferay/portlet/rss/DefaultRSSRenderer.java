package com.liferay.portlet.rss;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.RSSUtil;
import com.sun.syndication.feed.synd.SyndEntry;


public abstract class DefaultRSSRenderer implements RSSRenderer {

	private ThemeDisplay themeDisplay;
	private HttpServletRequest request;
	
	public DefaultRSSRenderer(HttpServletRequest request) {

		this.request = request;
		this.themeDisplay =
			(ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
	}

	@Override
	public String getAlternateURL() throws PortalException, SystemException {
		return PortalUtil.getLayoutFullURL(themeDisplay);
	}

	@Override
	public Date getPublicationDate() throws PortalException, SystemException {

		return new Date();
	}

	@Override
	public String getRSSFeedType() throws PortalException, SystemException {

		return RSSUtil.getFeedType(getRSSFormat(), 
			getRSSVersion());
	}
	
	protected double getRSSVersion() throws PortalException, SystemException {

		return ParamUtil.getDouble(
			request, "version", RSSUtil.VERSION_DEFAULT);
	}

	private String getRSSFormat() throws PortalException, SystemException {
		return ParamUtil.getString(
			request, "type", RSSUtil.FORMAT_DEFAULT);
	}
	
	@Override
	public String getRSSDescription() throws PortalException, SystemException {
		return getRSSName();
	}

	@Override
	public abstract String getFeedURL() throws PortalException, SystemException;


	@Override
	public abstract String getRSSName() throws PortalException, SystemException;
	
	@Override
	public abstract void populateFeedEntries(
			List<? super SyndEntry> syndEntries)  
		throws PortalException, SystemException;
}
