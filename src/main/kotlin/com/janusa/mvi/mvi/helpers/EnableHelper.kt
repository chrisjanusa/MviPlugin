package com.janusa.mvi.mvi.helpers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys

fun featureIsObtainable(event: AnActionEvent): Boolean {
    return getFeature(event) != null
}

fun baseIsObtainable(event: AnActionEvent): Boolean {
    return getBase(event) != null
}

fun baseContainsFile(event: AnActionEvent, name: String): Boolean {
    val virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE) ?: return false
    val basePath = getBase(event)
    if (basePath.isNullOrBlank()) return false
    val basePackageFile = virtualFile.fileSystem.findFileByPath(basePath) ?: return false
    if (basePackageFile.findChild(name) == null) return false
    return true
}

fun isStateValue(event: AnActionEvent): Boolean {
    return getStateValue(event) != null
}

fun enableIfAnyEnabled(event: AnActionEvent) {
    event.presentation.isEnabledAndVisible = isDirectory(event) || featureIsObtainable(event)
}