package com.janusa.mvi.mvi.screen

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.janusa.mvi.mvi.helpers.*
import com.janusa.mvi.mvi.screen.ui.NameQuestionDialog
import com.janusa.mvi.mvi.screen.ui.ScreenPromptHolder

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
        val featureDirectory = getDirectory(event, feature.lowercase()) ?: run {
            sendPackageError(event, feature.lowercase())
            return
        }
        val basePackage = getBasePackage(event)
        createFile(event, featureDirectory, StateTemplate(feature))
        createFile(event, featureDirectory, ViewModelTemplate(feature, basePackage))
        if (screenPromptHolder.initAction) {
            createSubDirectory(featureDirectory, ActionSupportFile.packageName)
            val actionDirectory = getDirectoryFromParent(featureDirectory, ActionSupportFile.packageName) ?: run {
                sendPackageError(event, ActionSupportFile.packageName)
                createFile(
                    event,
                    featureDirectory,
                    ScreenTemplate(
                        feature,
                        screenPromptHolder.isStartScreen,
                        screenPromptHolder.initAction,
                        basePackage
                    )
                )
                return
            }
            createFile(
                event,
                actionDirectory,
                ActionTemplate(
                    "Init${feature}",
                    feature,
                    featureDirectory.getPackage(),
                    basePackage
                )
            )
            createFile(
                event,
                featureDirectory,
                ScreenTemplate(
                    feature,
                    screenPromptHolder.isStartScreen,
                    screenPromptHolder.initAction,
                    basePackage,
                    actionDirectory.getPackage()
                )
            )
        } else {
            createFile(
                event,
                featureDirectory,
                ScreenTemplate(
                    feature,
                    screenPromptHolder.isStartScreen,
                    screenPromptHolder.initAction,
                    basePackage
                )
            )
        }
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = isDirectory(event) && !featureIsObtainable(event)
    }
}