package com.liferay.portlet.activities.action;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.rss.DefaultRSSRenderer;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityFeedEntry;
import com.liferay.portlet.social.service.SocialActivityInterpreterLocalServiceUtil;
import com.liferay.portlet.social.service.SocialActivityLocalServiceUtil;
import com.liferay.util.RSSUtil;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;


public class ActivitiesRSSRenderer extends DefaultRSSRenderer {
	
	
	
	private PortletRequest portletRequest;
	private ThemeDisplay themeDisplay;

	public ActivitiesRSSRenderer(
		PortletRequest request, PortletResponse response) {
		super(PortalUtil.getHttpServletRequest(request));
		portletRequest = request;
		themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}
	
	@Override
	public String getFeedURL() {
		try {
			return PortalUtil.getLayoutFullURL(themeDisplay) +
						Portal.FRIENDLY_URL_SEPARATOR + "activities/rss";
		}
		catch (Exception e) {
			return StringPool.BLANK;
		}
	}
	
	@Override
	public String getRSSName() {
		return ParamUtil.getString(portletRequest, "feedTitle");
	}

	@Override
	public void populateFeedEntries(List<? super SyndEntry> syndEntries) {
	
		String displayStyle = ParamUtil.getString(
			portletRequest, "displayStyle", RSSUtil.DISPLAY_STYLE_DEFAULT);

		int max = ParamUtil.getInteger(
			portletRequest, "max", SearchContainer.DEFAULT_DELTA);

		List<SocialActivity> activities;
		ServiceContext serviceContext;
		try {
			activities = getActivities(portletRequest, max);

			serviceContext = ServiceContextFactory.getInstance(
				portletRequest);
		}
		catch (Exception e) {
			return;
		}

		
		for (SocialActivity activity : activities) {
			SocialActivityFeedEntry activityFeedEntry =
				SocialActivityInterpreterLocalServiceUtil.interpret(
					StringPool.BLANK, activity, serviceContext);

			if (activityFeedEntry == null) {
				continue;
			}

			SyndEntry syndEntry = new SyndEntryImpl();

			SyndContent syndContent = new SyndContentImpl();

			syndContent.setType(RSSUtil.ENTRY_TYPE_DEFAULT);

			String value = null;

			if (displayStyle.equals(RSSUtil.DISPLAY_STYLE_TITLE)) {
				value = StringPool.BLANK;
			}
			else {
				value = activityFeedEntry.getBody();
			}

			syndContent.setValue(value);

			syndEntry.setDescription(syndContent);

			if (Validator.isNotNull(activityFeedEntry.getLink())) {
				syndEntry.setLink(activityFeedEntry.getLink());
			}

			syndEntry.setPublishedDate(new Date(activity.getCreateDate()));
			syndEntry.setTitle(
				HtmlUtil.extractText(activityFeedEntry.getTitle()));
			syndEntry.setUri(syndEntry.getLink());

			syndEntries.add(syndEntry);
		}

	}
	
	protected List<SocialActivity> getActivities(
		PortletRequest portletRequest, int max)
		throws Exception {

		Group group =
			GroupLocalServiceUtil.getGroup(themeDisplay.getScopeGroupId());

		int start = 0;

		if (group.isOrganization()) {
			return SocialActivityLocalServiceUtil.getOrganizationActivities(
				group.getOrganizationId(), start, max);
		}
		else if (group.isRegularSite()) {
			return SocialActivityLocalServiceUtil.getGroupActivities(
				group.getGroupId(), start, max);
		}
		else if (group.isUser()) {
			return SocialActivityLocalServiceUtil.getUserActivities(
				group.getClassPK(), start, max);
		}

		return Collections.emptyList();
}
	
	

}
