package com.liferay.portlet.rss;

import java.util.Date;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;


public interface RSSRenderer {
	
	public String getAlternateURL();
	public Date getPublicationDate();
	public String getRSSFeedType();
	public String getRSSDescription();
	public String getFeedURL();
	public String getRSSName();
	public void populateFeedEntries(List<? super SyndEntry> syndEntries);
	
}
