<#assign liferay_ui = taglibLiferayHash["/WEB-INF/tld/liferay-ui.tld"] />

<#if entries?has_content>
	<#list entries as entry>
		<@liferay_ui["rss-feed"]
			entriesPerFeed=entriesPerFeed?number
			expandedEntriesPerFeed=expandedEntriesPerFeed?number
			last=entry_has_next
			rssFeed=entry
			showFeedDescription=getterUtil.getBoolean(showFeedDescription)
			showFeedImage=getterUtil.getBoolean(showFeedImage)
			showFeedItemAuthor=getterUtil.getBoolean(showFeedItemAuthor)
			showFeedPublishedDate=getterUtil.getBoolean(showFeedPublishedDate)
			showFeedTitle=getterUtil.getBoolean(showFeedTitle)
		/>
	</#list>
</#if>