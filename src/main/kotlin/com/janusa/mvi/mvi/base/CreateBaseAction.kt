package com.janusa.mvi.mvi.base

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.janusa.mvi.mvi.helpers.*
import java.io.FileNotFoundException

class CreateBaseAction : AnAction("Create _Base") {
    override fun actionPerformed(event: AnActionEvent) {
        createDirectory(event, "base")
        val directory = getDirectory(event, "base") ?: throw FileNotFoundException("Could not create base package")
        createFile(event, directory, MVIScreenTemplate)
        createFile(event, directory, BaseActionTemplate)
        createFile(event, directory, BaseUpdaterTemplate)
        createFile(event, directory, BaseEventTemplate)
        createFile(event, directory, MVIViewModelTemplate)
        val buildGradle = getGradle(event) ?: throw FileNotFoundException("build.gradle not found")
        val buildFileManager = GradleFileManager(buildGradle)
        addNavigationDeps(buildFileManager, event)
        buildFileManager.writeToGradle()
        syncProject(event)
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = isDirectory(event) && !featureIsObtainable(event)
    }
}