package com.liferay.portlet.assetpublisher.action;

import java.util.List;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.assetpublisher.util.AssetPublisherUtil;
import com.liferay.portlet.rss.DefaultRSSRenderer;
import com.liferay.util.RSSUtil;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;


public class AssetRSSRenderer extends DefaultRSSRenderer {

	private PortletRequest portletRequest;
	private PortletResponse portletResponse;
	private PortletPreferences portletPreferences;
	private ThemeDisplay themeDisplay;

	public AssetRSSRenderer(
		PortletRequest portletRequest, PortletResponse portletResponse) {
		super(PortalUtil.getHttpServletRequest(portletRequest));
		this.portletRequest = portletRequest;
		this.portletResponse = portletResponse;
		this.portletPreferences = portletRequest.getPreferences();
		this.themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
		
	}

	protected List<AssetEntry> getAssetEntries()
	throws Exception {
	
		int rssDelta = GetterUtil.getInteger(
			portletPreferences.getValue("rssDelta", "20"));
	
		return AssetPublisherUtil.getAssetEntries(
			portletPreferences, themeDisplay.getLayout(),
			themeDisplay.getScopeGroupId(), rssDelta, true);
	}
	
	@Override
	public void populateFeedEntries(
			List<? super SyndEntry> syndEntries) {
		
		String assetLinkBehavior = portletPreferences.getValue(
			"assetLinkBehavior", "showFullContent");
		String rssDisplayStyle = portletPreferences.getValue(
			"rssDisplayStyle", RSSUtil.DISPLAY_STYLE_ABSTRACT);
		
		List<AssetEntry> assetEntries;
		try {
			assetEntries = getAssetEntries();
		}
		catch (Exception e) {
			return;
		}
		
		for (AssetEntry assetEntry : assetEntries) {
			SyndEntry syndEntry = new SyndEntryImpl();

			String author = PortalUtil.getUserName(assetEntry);

			syndEntry.setAuthor(author);

			SyndContent syndContent = new SyndContentImpl();

			syndContent.setType(RSSUtil.ENTRY_TYPE_DEFAULT);

			String value = null;

			String languageId = LanguageUtil.getLanguageId(portletRequest);

			if (rssDisplayStyle.equals(RSSUtil.DISPLAY_STYLE_TITLE)) {
				value = StringPool.BLANK;
			}
			else {
				value = assetEntry.getSummary(languageId, true);
			}

			syndContent.setValue(value);

			syndEntry.setDescription(syndContent);

			String link;
			try {
				link = getEntryURL(assetLinkBehavior, assetEntry);
			}
			catch (Exception e) {
				continue;
			}

			syndEntry.setLink(link);

			syndEntry.setPublishedDate(assetEntry.getPublishDate());
			syndEntry.setTitle(assetEntry.getTitle(languageId, true));
			syndEntry.setUpdatedDate(assetEntry.getModifiedDate());
			syndEntry.setUri(syndEntry.getLink());

			syndEntries.add(syndEntry);
		}
	}

	@Override
	public String getFeedURL() {
	
		try {
			String feedURL = getAssetPublisherURL();
			return feedURL.concat("rss");
		}
		catch (Exception e) {
			return StringPool.BLANK;
		}
	}

	protected String getAssetPublisherURL()
		throws Exception {
	
		Layout layout = themeDisplay.getLayout();
	
		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();
	
		StringBundler sb = new StringBundler(7);
	
		String layoutFriendlyURL = GetterUtil.getString(
			PortalUtil.getLayoutFriendlyURL(layout, themeDisplay));
	
		if (!layoutFriendlyURL.startsWith(Http.HTTP_WITH_SLASH) &&
			!layoutFriendlyURL.startsWith(Http.HTTPS_WITH_SLASH)) {
	
			sb.append(themeDisplay.getPortalURL());
		}
	
		sb.append(layoutFriendlyURL);
		sb.append(Portal.FRIENDLY_URL_SEPARATOR);
		sb.append("asset_publisher/");
		sb.append(portletDisplay.getInstanceId());
		sb.append(StringPool.SLASH);
	
		return sb.toString();
	}

	private String getEntryURL(
			String linkBehavior, AssetEntry assetEntry)
		throws Exception {
	
		if (linkBehavior.equals("viewInPortlet")) {
			return getEntryURLViewInContext(assetEntry);
		}
		else {
			return getEntryURLAssetPublisher(assetEntry);
		}
	}

	protected String getEntryURLAssetPublisher(AssetEntry assetEntry)
		throws Exception {
	
		AssetRendererFactory assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				assetEntry.getClassName());
	
		StringBundler sb = new StringBundler(4);
	
		sb.append(getAssetPublisherURL());
		sb.append(assetRendererFactory.getType());
		sb.append("/id/");
		sb.append(assetEntry.getEntryId());
	
		return sb.toString();
	}

	protected String getEntryURLViewInContext(AssetEntry assetEntry)
		throws Exception {
	
		AssetRendererFactory assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				assetEntry.getClassName());
	
		AssetRenderer assetRenderer = assetRendererFactory.getAssetRenderer(
			assetEntry.getClassPK());
	
		String viewInContextURL = assetRenderer.getURLViewInContext(
			(LiferayPortletRequest)portletRequest,
			(LiferayPortletResponse)portletResponse, null);
	
		if (Validator.isNotNull(viewInContextURL) &&
			!viewInContextURL.startsWith(Http.HTTP_WITH_SLASH) &&
			!viewInContextURL.startsWith(Http.HTTPS_WITH_SLASH)) {
	
			viewInContextURL = themeDisplay.getPortalURL() + viewInContextURL;
		}
	
		return viewInContextURL;
	}

	public String getRRSFeedType() {
	
		return portletRequest.getPreferences().getValue(
			"rssFeedType", RSSUtil.FEED_TYPE_DEFAULT);
	}

	@Override
	public String getRSSName() {
	
		return RSSUtil.getFeedTypeFormat(getRRSFeedType());
	}
}
