<#include "../init.ftl">

<#assign layoutLocalService = serviceLocator.findService("com.liferay.portal.service.LayoutLocalService")>
<#assign layoutService = serviceLocator.findService("com.liferay.portal.service.LayoutService")>

<#macro getLayoutOption
	groupId
	parentLayoutId
	privateLayout
	selected
	layout
	level = 0
>
	<#assign curLayoutJSON = escapeAttribute("{ \"layoutId\": ${layout.getLayoutId()}, \"groupId\": ${groupId}, \"privateLayout\": ${privateLayout?string} }")>

	<@aui.option selected=selected useModelValue=false value=curLayoutJSON>
		<#list 0..level as i>
			&ndash;&nbsp;
		</#list>

		${escape(layout.getName(requestedLocale))}
	</@>
</#macro>

<#macro getLayoutsOptions
	groupId
	parentLayoutId
	privateLayout
	selectedPlid
	level = 0
>
	<#assign layouts = layoutService.getLayouts(groupId, privateLayout, parentLayoutId)>

	<#if (layouts?size > 0)>
		<#if (level == 0)>
			<optgroup label="<#if (privateLayout)>${languageUtil.get(requestedLocale, "private-pages")}<#else>${languageUtil.get(requestedLocale, "public-pages")}</#if>">
		</#if>

		<#list layouts as curLayout>
			<@getLayoutOption
				groupId = groupId
				parentLayoutId = parentLayoutId
				privateLayout = privateLayout
				selected = (selectedPlid == curLayout.getPlid())
				layout = curLayout
				level = level
			/>

			<@getLayoutsOptions
				groupId = scopeGroupId
				level = level + 1
				parentLayoutId = curLayout.getLayoutId()
				privateLayout = privateLayout
				selectedPlid = selectedPlid
			/>
		</#list>

		<#if (level == 0)>
			</optgroup>
		</#if>
	</#if>
</#macro>

<@aui["field-wrapper"] data=data>
	<#assign selectedPlid = 0>

	<#assign fieldRawValue = paramUtil.getString(request, "${namespacedFieldName}", fieldRawValue)>

	<#if (fieldRawValue?? && fieldRawValue != "")>
		<#assign fieldLayoutJSONObject = jsonFactoryUtil.createJSONObject(fieldRawValue)>

		<#if (fieldLayoutJSONObject.getLong("groupId") > 0)>
			<#assign selectedLayoutGroupId = fieldLayoutJSONObject.getLong("groupId")>
		<#else>
			<#assign selectedLayoutGroupId = scopeGroupId>
		</#if>

		<#assign selectedLayout = layoutLocalService.fetchLayout(selectedLayoutGroupId, fieldLayoutJSONObject.getBoolean("privateLayout"), fieldLayoutJSONObject.getLong("layoutId"))!"">

		<#if (selectedLayout??)>
			<#assign selectedPlid = selectedLayout.getPlid()>
		</#if>
	</#if>

	<@aui.select helpMessage=escape(fieldStructure.tip) name=namespacedFieldName label=escape(label) required=required>
		<#if (selectedLayout?? && !layoutPermission.contains(permissionChecker, selectedLayout, "VIEW"))>
			<optgroup label="${languageUtil.get(requestedLocale, "current")}">
				<@getLayoutOption
					groupId = scopeGroupId
					parentLayoutId = 0
					privateLayout = selectedLayout.isPrivateLayout()
					selected = true
					layout = selectedLayout
					level = 0
				/>
			</optgroup>
		</#if>

		<@getLayoutsOptions
			groupId = scopeGroupId
			parentLayoutId = 0
			privateLayout = false
			selectedPlid = selectedPlid
		/>

		<@getLayoutsOptions
			groupId = scopeGroupId
			parentLayoutId = 0
			privateLayout = true
			selectedPlid = selectedPlid
		/>
	</@aui.select>

	${fieldStructure.children}
</@>