<#assign finderColsList = finder.getColumns()>

<#list finderColsList as finderCol>
	<#assign finderColConjunction = "">

	<#if finderCol_has_next>
		<#assign finderColConjunction = " AND ">
	<#elseif finder.where?? && validator.isNotNull(finder.getWhere())>
		<#assign finderColConjunction = " AND " + finder.where>
	</#if>

	<#if !finderCol.isPrimitiveType()>
		private static final String _FINDER_COLUMN_${finder.name?upper_case}_${finderCol.name?upper_case}_1 =

		<#if finderCol.comparator == "=">
			"${entity.alias}.${finderCol.DBName} IS NULL${finderColConjunction}"
		<#elseif finderCol.comparator == "<>" || finderCol.comparator = "!=">
			"${entity.alias}.${finderCol.DBName} IS NOT NULL${finderColConjunction}"
		<#else>
			"${entity.alias}.${finderCol.DBName} ${finderCol.comparator} NULL${finderColConjunction}"
		</#if>

		;
	</#if>

	<#if finderCol.type == "String" && !finderCol.isCaseSensitive()>
		<#assign finderColExpression = "lower(" + entity.alias + "." + finderCol.DBName + ") " + finderCol.comparator + " ?">
	<#else>
		<#assign finderColExpression = entity.alias + "." + finderCol.DBName + " " + finderCol.comparator + " ?">
	</#if>

	private static final String _FINDER_COLUMN_${finder.name?upper_case}_${finderCol.name?upper_case}_2 = "${finderColExpression}${finderColConjunction}";

	<#if finderCol.type == "String">
		<#assign finderColExpression = entity.alias + "." + finderCol.DBName + " " + finderCol.comparator + " ''">

		private static final String _FINDER_COLUMN_${finder.name?upper_case}_${finderCol.name?upper_case}_3 = "(${entity.alias}.${finderCol.DBName} IS NULL OR ${finderColExpression})${finderColConjunction}";
	</#if>

	<#if finder.hasArrayableOperator()>
		<#if !finderCol.isPrimitiveType()>
			private static final String _FINDER_COLUMN_${finder.name?upper_case}_${finderCol.name?upper_case}_4 = "(" + removeConjunction(_FINDER_COLUMN_${finder.name?upper_case}_${finderCol.name?upper_case}_1) + ")";
		</#if>

		private static final String _FINDER_COLUMN_${finder.name?upper_case}_${finderCol.name?upper_case}_5 = "(" + removeConjunction(_FINDER_COLUMN_${finder.name?upper_case}_${finderCol.name?upper_case}_2) + ")";

		<#if finderCol.type == "String">
			private static final String _FINDER_COLUMN_${finder.name?upper_case}_${finderCol.name?upper_case}_6 = "(" + removeConjunction(_FINDER_COLUMN_${finder.name?upper_case}_${finderCol.name?upper_case}_3) + ")";
		</#if>
	</#if>
</#list>