package com.liferay.portlet.rss;

import java.util.Date;
import java.util.List;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.sun.syndication.feed.synd.SyndEntry;


public interface RSSRenderer {
	
	public String getAlternateURL() throws PortalException, SystemException;
	public Date getPublicationDate() throws PortalException, SystemException;
	public String getRSSFeedType() throws PortalException, SystemException;
	public String getRSSDescription() throws PortalException, SystemException;
	public String getFeedURL() throws PortalException, SystemException;
	public String getRSSName() throws PortalException, SystemException;
	public void populateFeedEntries(List<? super SyndEntry> syndEntries)
		throws PortalException, SystemException;
	
}
