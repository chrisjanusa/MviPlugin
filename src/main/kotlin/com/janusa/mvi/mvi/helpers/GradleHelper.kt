package com.janusa.mvi.mvi.helpers

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import javax.swing.SwingUtilities


const val DEPENDENCIES = "dependencies"
const val PLUGINS = "plugins"
const val ANDROID = "android"

fun getGradle(event: AnActionEvent): Document? {
    var virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE)
    while (virtualFile != null && virtualFile.name != "src") {
        virtualFile = virtualFile.parent
    }
    virtualFile ?: return null
    val gradleFile =
        virtualFile.parent?.findChild("build.gradle") ?: return null
    return FileDocumentManager.getInstance().getDocument(gradleFile)
}

private fun getProjectGradle(event: AnActionEvent): Document? {
    val basePath = event.getData(PlatformDataKeys.PROJECT)?.basePath
    if (basePath.isNullOrBlank()) {
        return null
    }
    val projectBaseDir = LocalFileSystem.getInstance().findFileByPath(basePath)
    val gradleFile = projectBaseDir?.findChild("build.gradle") ?: return null
    return FileDocumentManager.getInstance().getDocument(gradleFile)
}

fun getKotlinVersion(event: AnActionEvent): String? {
    val buildGradle = getProjectGradle(event)
    val documentText =
        buildGradle?.text?.split("\n".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
            ?: return null
    for (i in documentText.indices) {
        val line = documentText[i]
        if (line.contains("id 'org.jetbrains.kotlin.android' version '")) {
            return line.substringAfter("id 'org.jetbrains.kotlin.android' version '").substringBefore("'")
        }
    }
    return null
}

fun syncProject(actionEvent: AnActionEvent) {
    val androidSyncAction = getAction("Android.SyncProject")
    val refreshAllProjectAction = getAction("ExternalSystem.RefreshAllProjects")
    if (androidSyncAction != null && androidSyncAction !is EmptyAction) {
        androidSyncAction.actionPerformed(actionEvent)
    } else if (refreshAllProjectAction != null && refreshAllProjectAction !is EmptyAction) {
        refreshAllProjectAction.actionPerformed(actionEvent)
    } else {
        SwingUtilities.invokeLater {
            Messages.showInfoMessage(
                "Project sync failed.",
                "SYNC FAILED"
            )
        }
    }
}

private fun getAction(actionId: String): AnAction? {
    return ActionManager.getInstance().getAction(actionId)
}


