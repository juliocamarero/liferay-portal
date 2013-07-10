package com.liferay.portlet.rss;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
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
	public String getAlternateURL() {
		
		try {
			return PortalUtil.getLayoutFullURL(themeDisplay);
		}
		catch (Exception e) {
			return StringPool.BLANK;
		}
	}

	@Override
	public Date getPublicationDate() {

		return new Date();
	}

	@Override
	public String getRSSFeedType() {

		return RSSUtil.getFeedType(getRSSFormat(), 
			getRSSVersion());
	}
	
	protected double getRSSVersion() {

		return ParamUtil.getDouble(
			request, "version", RSSUtil.VERSION_DEFAULT);
	}

	private String getRSSFormat() {
		return ParamUtil.getString(
			request, "type", RSSUtil.FORMAT_DEFAULT);
	}
	
	@Override
	public String getRSSDescription() {
		return getRSSName();
	}

	@Override
	public abstract String getFeedURL();


	@Override
	public abstract String getRSSName();
	
	@Override
	public abstract void populateFeedEntries(
		List<? super SyndEntry> syndEntries);
}
