<#assign aui = taglibLiferayHash["/WEB-INF/tld/liferay-aui.tld"] />
<#assign liferay_ui = taglibLiferayHash["/WEB-INF/tld/liferay-ui.tld"] />

<#if entries?has_content>
	<#assign languageId = localeUtil.toLanguageId(locale) />

	<@aui["form"]
		action=formAction
		method="post"
		name='${namespace + formName}'
		useNamespace=false
	>
		<@aui["select"]
			changesContext=true
			id='${namespace + name}'
			label=""
			name='${name}'
			onChange='${namespace + "changeLanguage();"}'
			title="language"
		>

			<#assign localeOriginalIndex = -1>

			<#list languageUtil.getAvailableLocales() as availableLocale>
			    <#if availableLocale == locale>
			        <#assign localeOriginalIndex = availableLocale_index>
			    </#if>
			</#list>

			<#list entries as entry>
				<#if displayCurrentLocale?matches("false") && (localeOriginalIndex == entry_index)>
					<@aui["option"]
						cssClass="taglib-language-option"
						label=locale.getDisplayLanguage(locale)
						lang=localeUtil.toW3cLanguageId(locale)
						selected=true
						disabled=true
						value=languageId
					/>
				<#else>
					<@aui["option"]
						cssClass="taglib-language-option"
						label=entry.getLongDisplayName()
						lang=entry.getW3cLanguageId()
						selected=entry.isSelected()
						value=entry.getLanguageId()
					/>
				</#if>
			</#list>
		</@>
	</@>

	<@aui["script"]>
		<#list entries as entry>
			document.${namespace + formName}.${name}.options[${entry_index}].style.backgroundImage = 'url(${themeDisplay.getPathThemeImages()}/language/${entry.getLanguageId()}.png)';
		</#list>

		function ${namespace}changeLanguage() {
			var languageId = AUI.$(document.${namespace + formName}.${name}).val();

			submitForm(document.${namespace + formName});
		}
	</@>
</#if>