package com.liferay.portlet.wiki.action;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.rss.DefaultRSSRenderer;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.util.WikiUtil;
import com.liferay.util.RSSUtil;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;


public class WikiRSSRenderer extends DefaultRSSRenderer {
	
	private ThemeDisplay themeDisplay;
	private long nodeId;
	private HttpServletRequest request;
	private List<WikiPage> pages;
	private boolean diff;
	
	public WikiRSSRenderer(
		HttpServletRequest request, List<WikiPage> pagesToExport, 
		boolean diff) {
		
		super(request);
		this.pages = pagesToExport;
		this.diff = diff;
		themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);
		this.request = request;
		nodeId = ParamUtil.getLong(request, "nodeId");
	}

	@Override
	public String getFeedURL() {
		String layoutFullURL;
		try {
			layoutFullURL = PortalUtil.getLayoutFullURL(
				themeDisplay.getScopeGroupId(), PortletKeys.WIKI);
		}
		catch (Exception e) {
			_log.warn("Could not get URL for Layout", e);
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(layoutFullURL);
		sb.append(Portal.FRIENDLY_URL_SEPARATOR);
		sb.append("wiki/");
		sb.append(nodeId);

		return sb.toString();
	}

	@Override
	public String getRSSName() {
		return ParamUtil.getString(request, "title");
	}

	@Override
	public void populateFeedEntries(List<? super SyndEntry> syndEntries) {
		WikiPage latestPage = null;

		StringBundler sb = new StringBundler(6);
		
		String entryURL = getFeedURL() + StringPool.SLASH + getRSSName();
		
		Locale locale = themeDisplay.getLocale();
		
		String attachmentURLPrefix = WikiUtil.getAttachmentURLPrefix(
			themeDisplay.getPathMain(), themeDisplay.getPlid(), nodeId,
			getRSSName());
		
		String displayStyle = ParamUtil.getString(
			request, "displayStyle", RSSUtil.DISPLAY_STYLE_DEFAULT);
		
		for (WikiPage page : pages) {
			SyndEntry syndEntry = new SyndEntryImpl();

			String author = PortalUtil.getUserName(page);

			syndEntry.setAuthor(author);

			SyndContent syndContent = new SyndContentImpl();

			syndContent.setType(RSSUtil.ENTRY_TYPE_DEFAULT);

			sb.setIndex(0);

			sb.append(entryURL);

			if (entryURL.endsWith(StringPool.SLASH)) {
				sb.append(HttpUtil.encodeURL(page.getTitle()));
			}

			if (diff) {
				if ((latestPage != null) || (pages.size() == 1)) {
					sb.append(StringPool.QUESTION);
					sb.append(PortalUtil.getPortletNamespace(PortletKeys.WIKI));
					sb.append("version=");
					sb.append(page.getVersion());

					String value = null;

					try {
						if (latestPage == null) {
							value =
								WikiUtil.convert(
									page, null, null, attachmentURLPrefix);
						}
						else {
							value =
								WikiUtil.diffHtml(
									latestPage, page, null, null,
									attachmentURLPrefix);
						}
					}
					catch (Exception e) {
						if (_log.isWarnEnabled()) {
							_log.warn("Could not generate content", e);
						}
						continue;
					}

					syndContent.setValue(value);

					syndEntry.setDescription(syndContent);

					syndEntries.add(syndEntry);
				}
			}
			else {
				String value = null;

				if (displayStyle.equals(RSSUtil.DISPLAY_STYLE_ABSTRACT)) {
					value = StringUtil.shorten(
						HtmlUtil.extractText(page.getContent()),
						PropsValues.WIKI_RSS_ABSTRACT_LENGTH, StringPool.BLANK);
				}
				else if (displayStyle.equals(RSSUtil.DISPLAY_STYLE_TITLE)) {
					value = StringPool.BLANK;
				}
				else {
					try {
						value = WikiUtil.convert(
							page, null, null, attachmentURLPrefix);
					}
					catch (Exception e) {
						_log.warn("", e);
					}
				}

				syndContent.setValue(value);

				syndEntry.setDescription(syndContent);

				syndEntries.add(syndEntry);
			}

			syndEntry.setLink(sb.toString());
			syndEntry.setPublishedDate(page.getCreateDate());

			String title =
				page.getTitle() + StringPool.SPACE + page.getVersion();

			if (page.isMinorEdit()) {
				title +=
					StringPool.SPACE + StringPool.OPEN_PARENTHESIS +
						LanguageUtil.get(locale, "minor-edit") +
							StringPool.CLOSE_PARENTHESIS;
			}

			syndEntry.setTitle(title);

			syndEntry.setUpdatedDate(page.getModifiedDate());
			syndEntry.setUri(sb.toString());

			latestPage = page;
		}
	}
	
	private static Log _log = LogFactoryUtil.getLog(WikiRSSRenderer.class); 
}
