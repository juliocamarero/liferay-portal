package com.liferay.portlet.journal.action;

import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.XPath;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortletURLImpl;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleDisplay;
import com.liferay.portlet.journal.model.JournalFeed;
import com.liferay.portlet.journal.model.JournalFeedConstants;
import com.liferay.portlet.journal.service.JournalContentSearchLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalFeedLocalServiceUtil;
import com.liferay.portlet.journal.util.JournalRSSUtil;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;
import com.liferay.portlet.rss.DefaultRSSRenderer;
import com.liferay.util.RSSUtil;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndLink;


public class JournalRSSRenderer extends DefaultRSSRenderer {
	
	ThemeDisplay themeDisplay;
	private ResourceRequest request;
	private ResourceResponse response;
	
	public JournalRSSRenderer(ResourceRequest request, ResourceResponse response) {
		super(PortalUtil.getHttpServletRequest(request));
		this.request = request;
		this.response = response;
		themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Override
	public void populateFeedEntries(List<? super SyndEntry> syndEntries) {
	
		JournalFeed feed;
		try {
			feed = getJournalFeed();
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn("Could not obtain journal feed", e);
			}
			return;
		}
	
		String languageId = LanguageUtil.getLanguageId(request);
	
		long plid = PortalUtil.getPlidFromFriendlyURL(
			themeDisplay.getCompanyId(), feed.getTargetLayoutFriendlyUrl());
	
		Layout layout = themeDisplay.getLayout();
	
		if (plid > 0) {
			try {
				layout = LayoutLocalServiceUtil.getLayout(plid);
			}
			catch (NoSuchLayoutException nsle) {
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn("Could not obtain layout with plid " + plid, e);
				}
			}
		}
		
		List<JournalArticle> articles;
		try {
			articles = JournalRSSUtil.getArticles(feed);
		}
		catch (SystemException e) {
			if (_log.isWarnEnabled()) {
				_log.warn("Could not obtain articles for feed " + feed, e);
			}
			return;
		}
	
		if (_log.isDebugEnabled()) {
			_log.debug("Syndicating " + articles.size() + " articles");
		}
	
		for (JournalArticle article : articles) {
			SyndEntry syndEntry = new SyndEntryImpl();
	
			String author = PortalUtil.getUserName(article);
	
			syndEntry.setAuthor(author);
	
			SyndContent syndContent = new SyndContentImpl();
	
			syndContent.setType(RSSUtil.ENTRY_TYPE_DEFAULT);
	
			String value = article.getDescription(languageId);
	
			try {
				value = processContent(
					feed, article, languageId, themeDisplay, syndEntry,
					syndContent);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(e, e);
				}
			}
	
			syndContent.setValue(value);
	
			syndEntry.setDescription(syndContent);
	
			String link;
			try {
				link = getEntryURL(feed, article, layout, themeDisplay);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn("Could not generate entryURL for article" +
						article);
				}
				continue;
			}
	
			syndEntry.setLink(link);
	
			syndEntry.setPublishedDate(article.getDisplayDate());
			syndEntry.setTitle(article.getTitle(languageId));
			syndEntry.setUpdatedDate(article.getModifiedDate());
			syndEntry.setUri(link);
	
			syndEntries.add(syndEntry);
		}
		
	}

	protected String getEntryURL(
			JournalFeed feed, JournalArticle article, Layout layout,
			ThemeDisplay themeDisplay)
		throws Exception {
	
		List<Long> hitLayoutIds =
			JournalContentSearchLocalServiceUtil.getLayoutIds(
				layout.getGroupId(), layout.isPrivateLayout(),
				article.getArticleId());
	
		if (hitLayoutIds.size() > 0) {
			Long hitLayoutId = hitLayoutIds.get(0);
	
			Layout hitLayout = LayoutLocalServiceUtil.getLayout(
				layout.getGroupId(), layout.isPrivateLayout(),
				hitLayoutId.longValue());
	
			return PortalUtil.getLayoutFriendlyURL(hitLayout, themeDisplay);
		}
		else {
			long plid = PortalUtil.getPlidFromFriendlyURL(
				feed.getCompanyId(), feed.getTargetLayoutFriendlyUrl());
	
			String portletId = PortletKeys.JOURNAL_CONTENT;
	
			if (Validator.isNotNull(feed.getTargetPortletId())) {
				portletId = feed.getTargetPortletId();
			}
	
			PortletURL entryURL = new PortletURLImpl(
				request, portletId, plid, PortletRequest.RENDER_PHASE);
	
			entryURL.setParameter("struts_action", "/journal_content/view");
			entryURL.setParameter(
				"groupId", String.valueOf(article.getGroupId()));
			entryURL.setParameter("articleId", article.getArticleId());
	
			return entryURL.toString();
		}
	}

	@Override
	public String getRSSFeedType() {
		
		try {
			JournalFeed feed = getJournalFeed();
			return feed.getFeedFormat() + "_" + feed.getFeedVersion();
		}
		catch (Exception e) {
			_log.warn("Could not generate RSSFeedType", e);
			return StringPool.BLANK;
		}
	}

	@Override
	public String getRSSName() {
		try {
			return getJournalFeed().getName();
		}
		catch (Exception e) {
			_log.warn("Could not generate RSSName", e);
			return StringPool.BLANK;
		}
	}

	protected String processContent(
			JournalFeed feed, JournalArticle article, String languageId,
			ThemeDisplay themeDisplay, SyndEntry syndEntry,
			SyndContent syndContent)
		throws Exception {
	
		String content = article.getDescription(languageId);
	
		String contentField = feed.getContentField();
	
		if (contentField.equals(JournalFeedConstants.RENDERED_WEB_CONTENT)) {
			String rendererTemplateId = article.getTemplateId();
	
			if (Validator.isNotNull(feed.getRendererTemplateId())) {
				rendererTemplateId = feed.getRendererTemplateId();
			}
	
			JournalArticleDisplay articleDisplay =
				JournalContentUtil.getDisplay(
					feed.getGroupId(), article.getArticleId(),
					rendererTemplateId, null, languageId, themeDisplay, 1,
					_XML_REQUUEST);
	
			if (articleDisplay != null) {
				content = articleDisplay.getContent();
			}
		}
		else if (!contentField.equals(
					JournalFeedConstants.WEB_CONTENT_DESCRIPTION)) {
	
			Document document = SAXReaderUtil.read(
				article.getContentByLocale(languageId));
	
			contentField = HtmlUtil.escapeXPathAttribute(contentField);
	
			XPath xPathSelector = SAXReaderUtil.createXPath(
				"//dynamic-element[@name=" + contentField + "]");
	
			List<Node> results = xPathSelector.selectNodes(document);
	
			if (results.size() == 0) {
				return content;
			}
	
			Element element = (Element)results.get(0);
	
			String elType = element.attributeValue("type");
	
			if (elType.equals("document_library")) {
				String url = element.elementText("dynamic-content");
	
				url = processURL(feed, url, themeDisplay, syndEntry);
			}
			else if (elType.equals("image") || elType.equals("image_gallery")) {
				String url = element.elementText("dynamic-content");
	
				url = processURL(feed, url, themeDisplay, syndEntry);
	
				content =
					content + "<br /><br /><img alt='' src='" +
						themeDisplay.getURLPortal() + url + "' />";
			}
			else if (elType.equals("text_box")) {
				syndContent.setType("text");
	
				content = element.elementText("dynamic-content");
			}
			else {
				content = element.elementText("dynamic-content");
			}
		}
	
		return content;
	}

	protected String processURL(
		JournalFeed feed, String url, ThemeDisplay themeDisplay,
		SyndEntry syndEntry) {
	
		url = StringUtil.replace(
			url,
			new String[] {
				"@group_id@", "@image_path@", "@main_path@"
			},
			new String[] {
				String.valueOf(feed.getGroupId()), themeDisplay.getPathImage(),
				themeDisplay.getPathMain()
			}
		);
	
		List<SyndEnclosure> syndEnclosures = JournalRSSUtil.getDLEnclosures(
			themeDisplay.getURLPortal(), url);
	
		syndEnclosures.addAll(
			JournalRSSUtil.getIGEnclosures(themeDisplay.getURLPortal(), url));
	
		syndEntry.setEnclosures(syndEnclosures);
	
		List<SyndLink> syndLinks = JournalRSSUtil.getDLLinks(
			themeDisplay.getURLPortal(), url);
	
		syndLinks.addAll(
			JournalRSSUtil.getIGLinks(themeDisplay.getURLPortal(), url));
	
		syndEntry.setLinks(syndLinks);
	
		return url;
	}

	@Override
	public String getFeedURL() {
	
		ResourceURL feedURL = response.createResourceURL();
		
		JournalFeed feed;
		try {
			feed = getJournalFeed();
		}
		catch (Exception e) {
			return StringPool.BLANK;
		}
		
		feedURL.setCacheability(ResourceURL.FULL);
		feedURL.setParameter("struts_action", "/journal/rss");
		feedURL.setParameter("groupId", String.valueOf(feed.getGroupId()));
		feedURL.setParameter("feedId", String.valueOf(feed.getFeedId()));
	
		return feedURL.toString();
	}

	private JournalFeed getJournalFeed()
		throws PortalException, SystemException {
	
		JournalFeed feed = 
			(JournalFeed) request.getAttribute("rssJournalFeed");
		
		if (feed != null) {
			return feed;
		}
		
		long id = ParamUtil.getLong(request, "id");
	
		long groupId = ParamUtil.getLong(request, "groupId");
		String feedId = ParamUtil.getString(request, "feedId");
	
		if (id > 0) {
			feed = JournalFeedLocalServiceUtil.getFeed(id);
		}
		else {
			feed = JournalFeedLocalServiceUtil.getFeed(groupId, feedId);
		}
		
		request.setAttribute("rssJournalFeed", feed);
		return feed;
	}
	
	private static final String _XML_REQUUEST =
		"<request><parameters><parameter><name>rss</name><value>true</value>"
			+ "</parameter></parameters></request>";
	
	private static Log _log = LogFactoryUtil.getLog(JournalRSSRenderer.class);
}
