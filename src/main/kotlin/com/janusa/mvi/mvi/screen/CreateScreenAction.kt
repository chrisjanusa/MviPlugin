package com.janusa.mvi.mvi.screen

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.janusa.mvi.mvi.action.ActionTemplate
import com.janusa.mvi.mvi.helpers.*
import com.janusa.mvi.mvi.ui.NameQuestionDialog
import com.janusa.mvi.mvi.ui.ScreenPromptHolder
import java.io.FileNotFoundException

class CreateScreenAction : AnAction("Create _Screen") {
    override fun actionPerformed(event: AnActionEvent) {
        val screenPromptHolder = ScreenPromptHolder()
        val dialog = NameQuestionDialog(
            "Create New Screen",
            "Enter the Name of the Feature",
            "This will be prepended to several file names and be the name of the package",
            screenPromptHolder,
            listOf(screenFileSuffix, stateFileSuffix, viewModelFileSuffix),
        )
        dialog.showAndGet()
        val feature = screenPromptHolder.name
        createDirectory(event, feature.lowercase())
        val featureDirectory = getDirectory(event, feature.lowercase())
            ?: throw FileNotFoundException("Could not create actions package")
        createFile(
            event,
            featureDirectory,
            ScreenTemplate(feature, screenPromptHolder.isStartScreen, screenPromptHolder.initAction)
        )
        createFile(event, featureDirectory, StateTemplate(feature))
        createFile(event, featureDirectory, ViewModelTemplate(feature))
        if (screenPromptHolder.initAction) {
            createSubDirectory(featureDirectory, "actions")
            val actionDirectory = getDirectoryFromParent(featureDirectory, "actions")
                ?: throw FileNotFoundException("Could not create actions package")
            createFile(
                event,
                actionDirectory,
                ActionTemplate(
                    "Init${feature}",
                    feature,
                    featureDirectory.getPackage()
                )
            )
        }
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = isDirectory(event) && !featureIsObtainable(event)
    }
}