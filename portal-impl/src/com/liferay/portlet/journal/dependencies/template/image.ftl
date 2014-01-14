<#include "init.ftl">

<#assign variableAltName = name + ".getAlt()">

<img alt="${getVariableReferenceCode(variableAltName)}" src="${getVariableReferenceCode(variableName)}" />