<#assign aui = taglibLiferayHash["/WEB-INF/tld/aui.tld"] />
<#assign liferay_portlet = taglibLiferayHash["/WEB-INF/tld/liferay-portlet.tld"] />
<#assign liferay_ui = taglibLiferayHash["/WEB-INF/tld/liferay-ui.tld"] />

<#assign asset_category_service = serviceLocator.findService("com.liferay.portlet.asset.service.AssetCategoryService")>

<#if entries?has_content>
	<@aui.layout>
		<#list entries as entry>
			<@aui.column columnWith="25">

				<div class="results-header">
					<h3>
						${entry.getName()}
					</h3>
				</div>

				<#assign categories = entry.getCategories()>

				<@category_tree categories=categories />
			</@aui.column>
		</#list>
	</@aui.layout>
</#if>

<#macro category_tree categories>
	<#if categories?has_content>
		<ul class="categories">
			<#list categories as category>
				<#assign category_url = renderResponse.createRenderURL()>

				${category_url.setParameter("resetCur", "true")}
				${category_url.setParameter("categoryId", category.getCategoryId()?string)}

				<li>
					<a href="${category_url}">${category.getName()}</a>

					<#assign child_categories = asset_category_service.getChildCategories(category.getCategoryId())>

					<@category_tree categories=child_categories />
				</li>
			</#list>
		</ul>
	</#if>
</#macro>