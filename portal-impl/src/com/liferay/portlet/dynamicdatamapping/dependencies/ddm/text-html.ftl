<#include "../init.ftl">

<#assign skipEditorLoading = paramUtil.getBoolean(request, "p_p_isolated")>

<@aui["field-wrapper"] data=data helpMessage=escape(fieldStructure.tip) label=escape(label) required=required>
	<@liferay_ui["input-editor"] initMethod="${namespacedFieldName}InitEditor" name="${namespacedFieldName}Editor" onBlurMethod="${namespacedFieldName}OnBlurEditor" skipEditorLoading=skipEditorLoading value="${fieldValue}" />

//  TODO: Add required attribute to liferay-ui:input-editor
	<#if required>
			<@aui.validator name="required" />
		</#if>
	</@>

	${fieldStructure.children}
</@>

<@aui.script>
	Liferay.provide(
		window,
		'${portletNamespace}${namespacedFieldName}OnBlurEditor',
		function() {
			var A = AUI();

			var field = A.one('#${portletNamespace}${namespacedFieldName}');

			field.val(window['${portletNamespace}${namespacedFieldName}Editor'].getHTML());

			var form = field.get('form');

			if (form) {
				var formName = form.get('name');

				var formValidator = Liferay.Form.get(formName).formValidator;

				if (formValidator) {
					formValidator.validateField(field);
				}
			}
		},
		['liferay-form']
	);
</@>

<@aui.script use="aui-base">
	var field = A.one('#${portletNamespace}${namespacedFieldName}');

	var form = field.get('form');

	if (form) {
		form.on(
			'submit',
			function(event) {
				field.val(window['${portletNamespace}${namespacedFieldName}Editor'].getHTML());
			}
		);
	}
</@>