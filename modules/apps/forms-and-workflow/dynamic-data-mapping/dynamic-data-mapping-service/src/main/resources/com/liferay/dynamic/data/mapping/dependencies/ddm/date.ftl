<#include "../init.ftl">

<#assign
	DATE = staticUtil["java.util.Calendar"].DATE
	MONTH = staticUtil["java.util.Calendar"].MONTH
	YEAR = staticUtil["java.util.Calendar"].YEAR
/>

<#if (hasFieldValue)>
	<#assign
		dateValue = fieldRawValue?date["yyyy-MM-dd"]

		fieldValue = calendarFactory.getCalendar(requestedLocale)

		void = fieldValue.setTimeInMillis(dateValue?long)
	/>

	<#assign
		day = fieldValue.get(DATE)
		month = fieldValue.get(MONTH)
		year = fieldValue.get(YEAR)
	/>
<#elseif validator.isNotNull(predefinedValue)>
	<#assign
		predefinedDate = dateUtil.parseDate(predefinedValue, requestedLocale)

		fieldValue = calendarFactory.getCalendar(predefinedDate?long)
	/>

	<#assign
		day = fieldValue.get(DATE)
		month = fieldValue.get(MONTH)
		year = fieldValue.get(YEAR)
	/>
<#else>
	<#assign
		day = 0
		month = -1
		year = 0
	/>
</#if>

<#assign
	dayValue = paramUtil.getInteger(request, "${namespacedFieldName}Day", day)
	monthValue = paramUtil.getInteger(request, "${namespacedFieldName}Month", month)
	yearValue = paramUtil.getInteger(request, "${namespacedFieldName}Year", year)
/>

<@liferay_aui["field-wrapper"] cssClass="form-builder-field" data=data helpMessage=escape(fieldStructure.tip) label=escape(label) name=namespacedFieldName>
	<div class="form-group">
		<@liferay_ui["input-date"]
			cssClass=cssClass
			dayParam="${namespacedFieldName}Day"
			dayValue=dayValue
			disabled=false
			monthParam="${namespacedFieldName}Month"
			monthValue=monthValue
			name="${namespacedFieldName}"
			nullable=true
			required=required
			yearParam="${namespacedFieldName}Year"
			yearValue=yearValue
		/>
	</div>

	${fieldStructure.children}
</@>