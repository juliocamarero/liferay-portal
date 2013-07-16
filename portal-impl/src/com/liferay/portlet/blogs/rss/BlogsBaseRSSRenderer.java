package com.liferay.portlet.blogs.rss;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.util.RSSUtil;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;

public abstract class BlogsBaseRSSRenderer implements 
	com.liferay.portlet.rss.RSSRenderer{

	private List<BlogsEntry> blogsEntries;
	private HttpServletRequest request;
	private String displayStyle;
	private ThemeDisplay themeDisplay;
	
	
	public ThemeDisplay getThemeDisplay() {
	
		return themeDisplay;
	}


	public HttpServletRequest getRequest() {
	
		return request;
	}

	
	public BlogsBaseRSSRenderer(
		List<BlogsEntry> blogsEntries,
		HttpServletRequest request) {

		this.blogsEntries = blogsEntries;
		this.request = request;
		this.themeDisplay =
			(ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		this.displayStyle = ParamUtil.getString(
			request, "displayStyle", RSSUtil.DISPLAY_STYLE_DEFAULT);;
	}

	@Override
	public String getAlternateURL() throws PortalException, SystemException {
		String feedURL = getFeedURL();
		if (feedURL.endsWith("/-/blogs/rss")) {
			return feedURL.substring(0, feedURL.length() - 12);
		}
		return PortalUtil.getLayoutFullURL(themeDisplay);
	}

	abstract protected String getEntryURL() 
		throws PortalException, SystemException;
	
	@Override
	public String getFeedURL() throws PortalException, SystemException {
		return themeDisplay.getPortalURL() + themeDisplay.getPathMain() +
		"/blogs/find_entry?";
	}

	@Override
	abstract public String getRSSName() throws PortalException, SystemException;

	@Override
	public void populateFeedEntries(List<? super SyndEntry> syndEntries) 
		throws PortalException, SystemException {

		for (BlogsEntry entry : blogsEntries) {
			SyndEntry syndEntry = new SyndEntryImpl();

			String author = PortalUtil.getUserName(entry);

			syndEntry.setAuthor(author);

			SyndContent syndContent = new SyndContentImpl();

			syndContent.setType(RSSUtil.ENTRY_TYPE_DEFAULT);

			String value = null;

			if (displayStyle.equals(RSSUtil.DISPLAY_STYLE_ABSTRACT)) {
				String summary = entry.getDescription();

				if (Validator.isNull(summary)) {
					summary = entry.getContent();
				}

				value = StringUtil.shorten(
					HtmlUtil.extractText(summary),
					PropsValues.BLOGS_RSS_ABSTRACT_LENGTH, StringPool.BLANK);
			}
			else if (displayStyle.equals(RSSUtil.DISPLAY_STYLE_TITLE)) {
				value = StringPool.BLANK;
			}
			else {
				value = StringUtil.replace(
					entry.getContent(),
					new String[] {
						"href=\"/", "src=\"/"
					},
					new String[] {
						"href=\"" + themeDisplay.getURLPortal() + "/",
						"src=\"" + themeDisplay.getURLPortal() + "/"
					});
			}

			syndContent.setValue(value);

			syndEntry.setDescription(syndContent);

			StringBundler sb = new StringBundler(4);
			
			String entryURL = getEntryURL();
			
			if (entryURL.endsWith("/blogs/rss")) {
				sb.append(entryURL.substring(0, entryURL.length() - 3));
				sb.append(entry.getUrlTitle());
			}
			else {
				sb.append(entryURL);

				if (!entryURL.endsWith(StringPool.QUESTION)) {
					sb.append(StringPool.AMPERSAND);
				}

				sb.append("entryId=");
				sb.append(entry.getEntryId());
			}

			String link = sb.toString();

			syndEntry.setLink(link);

			syndEntry.setPublishedDate(entry.getDisplayDate());
			syndEntry.setTitle(entry.getTitle());
			syndEntry.setUpdatedDate(entry.getModifiedDate());
			syndEntry.setUri(link);

			syndEntries.add(syndEntry);
		}
		
	}

	@Override
	public String getRSSFeedType() throws PortalException, SystemException {
		
		String type = ParamUtil.getString(
			getRequest(), "type", RSSUtil.FORMAT_DEFAULT);
		return RSSUtil.getFeedType(type, getRSSVersion());
	}
		
	
	public double getRSSVersion() throws PortalException, SystemException {
		return ParamUtil.getDouble(
			request, "version", RSSUtil.VERSION_DEFAULT);
	}

	@Override
	public Date getPublicationDate() throws PortalException, SystemException {
		return new Date();
	}

	@Override
	public String getRSSDescription() throws PortalException, SystemException {
		return getRSSName();
	}

}
