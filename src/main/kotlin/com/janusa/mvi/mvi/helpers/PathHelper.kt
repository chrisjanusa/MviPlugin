package com.janusa.mvi.mvi.helpers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.janusa.mvi.mvi.base.BasePathService

private fun getBasePath(event: AnActionEvent): String? {
    val project = event.getData(PlatformDataKeys.PROJECT) ?: return null
    return BasePathService.getService(project).basePath
}

fun getBasePackage(event: AnActionEvent): String {
    return getBasePath(event)?.pathToPackage() ?: ""
}

fun saveBasePath(event: AnActionEvent, path: String) {
    val project = event.getData(PlatformDataKeys.PROJECT) ?: return
    BasePathService.getService(project).basePath = path
}


fun getBase(event: AnActionEvent): String? {
    val virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE) ?: return null
    val basePathStored = getBasePath(event)
    if (!basePathStored.isNullOrBlank()) {
        return basePathStored
    }
    if (virtualFile.parent.name == "base") {
        return virtualFile.parent.path
    }
    return null
}