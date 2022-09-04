package com.janusa.mvi.mvi.helpers

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import org.apache.commons.lang.StringUtils
import java.io.FileNotFoundException
import javax.swing.SwingUtilities


const val DEPENDENCIES = "dependencies"
const val PLUGINS = "plugins"
const val ANDROID = "android"

@Throws(FileNotFoundException::class)
fun getGradle(event: AnActionEvent): Document? {
    var virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE)
    while (virtualFile != null && virtualFile.name != "src") {
        virtualFile = virtualFile.parent
    }
    virtualFile ?: throw FileNotFoundException("File not found.")
    val gradleFile =
        virtualFile.parent?.findChild("build.gradle") ?: throw FileNotFoundException("build.gradle not found")
    return FileDocumentManager.getInstance().getDocument(gradleFile)
}

@Throws(FileNotFoundException::class)
private fun getProjectGradle(event: AnActionEvent): Document? {
    val basePath =
        event.getData(PlatformDataKeys.PROJECT)?.basePath ?: throw FileNotFoundException("Project base path not found.")
    if (StringUtils.isEmpty(basePath)) {
        throw FileNotFoundException("Project base path not found.")
    }
    val projectBaseDir = LocalFileSystem.getInstance().findFileByPath(basePath)
    val gradleFile = projectBaseDir?.findChild("build.gradle") ?: throw FileNotFoundException("build.gradle not found")
    return FileDocumentManager.getInstance().getDocument(gradleFile)
}

fun getKotlinVersion(event: AnActionEvent): String {
    val buildGradle = getProjectGradle(event)
    val documentText =
        buildGradle?.text?.split("\n".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
            ?: throw FileNotFoundException("Kotlin version not found in build.gradle")
    for (i in documentText.indices) {
        val line = documentText[i]
        if (line.contains("id 'org.jetbrains.kotlin.android' version '")) {
            return line.substringAfter("id 'org.jetbrains.kotlin.android' version '").substringBefore("'")
        }
    }
    throw FileNotFoundException("Kotlin version not found in build.gradle")
}

//fun addDependency(configuration: String, repository: String, version: String, buildGradle: Document?) {
//    val documentText =
//        buildGradle?.text?.split("\n".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray() ?: return
//    val sb = StringBuilder()
//    var counter = 0
//    var canSearch = false
//    for (i in documentText.indices) {
//        val line = documentText[i]
//        if (canSearch) {
//            if (line.contains("{")) {
//                counter += 1
//            }
//            if (line.contains(repository)) {
//                canSearch = false
//            }
//            if (line.contains("}")) {
//                if (counter > 0) {
//                    counter -= 1
//                } else {
//                    canSearch = false
//                    sb.append("\t$configuration '$repository:$version'").append("\n")
//                }
//            }
//        }
//        if (line.contains(DEPENDENCIES)) {
//            val tempLine = line.replace(DEPENDENCIES, "")
//            if (tempLine.trim().equals("{", true)) {
//                canSearch = true
//            } else {
//                if (!tempLine.trim().isRequiredField()) {
//                    counter = -1
//                    canSearch = true
//                } else {
//                    if (tempLine.trim().contains("{")
//                        && !tempLine.trim().contains("//")
//                        && (tempLine.trim().contains(IMPLEMENTATION) || tempLine.trim().contains(COMPILE))
//                    ) {
//                        canSearch = true
//                    }
//                }
//            }
//        }
//        sb.append(line).append("\n")
//    }
//    writeToGradle(sb, buildGradle)
//}
//
//fun addPlugin(id: String, version: String, buildGradle: Document?) {
//    val documentText =
//        buildGradle?.text?.split("\n".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray() ?: return
//    val sb = StringBuilder()
//    var counter = 0
//    var canSearch = false
//    for (i in documentText.indices) {
//        val line = documentText[i]
//        if (canSearch) {
//            if (line.contains("{")) {
//                counter += 1
//            }
//            if (line.contains(id)) {
//                canSearch = false
//            }
//            if (line.contains("}")) {
//                if (counter > 0) {
//                    counter -= 1
//                } else {
//                    canSearch = false
//                    sb.append("\tid '$id'")
//                    if (version.isNotBlank()) {
//                        sb.append(" version '$version'").append("\n")
//                    }
//                    sb.append("\n")
//                }
//            }
//        }
//        if (line.contains(PLUGINS)) {
//            val tempLine = line.replace(PLUGINS, "")
//            if (tempLine.trim().equals("{", true)) {
//                canSearch = true
//            } else {
//                if (!tempLine.trim().isRequiredField()) {
//                    counter = -1
//                    canSearch = true
//                } else {
//                    if (tempLine.trim().contains("{")
//                        && !tempLine.trim().contains("//")
//                        && (tempLine.trim().contains(IMPLEMENTATION) || tempLine.trim().contains(COMPILE))
//                    ) {
//                        canSearch = true
//                    }
//                }
//            }
//        }
//        sb.append(line).append("\n")
//    }
//    writeToGradle(sb, buildGradle)
//}
//
//private fun writeToGradle(stringBuilder: StringBuilder, buildGradle: Document?) {
//    val application = ApplicationManager.getApplication()
//    application.invokeLater {
//        application.runWriteAction { buildGradle?.setText(stringBuilder) }
//    }
//}

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


