package com.janusa.mvi.mvi.base

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.janusa.mvi.mvi.helpers.*

class CreateBaseAction : AnAction("Create _Base") {
    override fun actionPerformed(event: AnActionEvent) {
        createDirectory(event, "base")
        val directory = getDirectory(event, "base") ?: run {
            sendPackageError(
                event,
                "base",
            )
            return
        }
        createFile(event, directory, MVIScreenTemplate)
        createFile(event, directory, BaseActionTemplate)
        createFile(event, directory, BaseUpdaterTemplate)
        createFile(event, directory, BaseEventTemplate)
        createFile(event, directory, MVIViewModelTemplate)
        val dependencyHolder = UseDependencyHolder()
        val dialog = AddDependencyDialog(
            "Add Navigation Dependencies?",
            dependencyHolder,
        )
        dialog.showAndGet()
        if (dependencyHolder.addDependency) {
            val buildGradle = getGradle(event)
            if (buildGradle == null) {
                sendErrorNotification(
                    event,
                    NotificationGroupIds.GRADLE_ERROR,
                    "Error finding Module build.gradle file",
                    "Navigation Dependencies were not added automatically so make sure to add them manually"
                )
            } else {
                val buildFileManager = GradleFileManager(buildGradle)
                addNavigationDeps(buildFileManager, event)
                buildFileManager.writeToGradle()
                syncProject(event)
            }
        }
        saveBasePath(event, directory.virtualFile.path)
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = isDirectory(event) && !featureIsObtainable(event)
    }
}