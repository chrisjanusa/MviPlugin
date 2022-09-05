package com.janusa.mvi.mvi.screen

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.janusa.mvi.mvi.helpers.*

class CreateUpdaterFromStateAction : AnAction("Create _${UpdaterSupportFile.suffix}") {
    override fun actionPerformed(event: AnActionEvent) {
        val feature = getFeatureFromStateEditor(event) ?: run {
            sendFeatureError(
                event,
                MissingFeatureAttribute.NAME,
                UpdaterSupportFile.suffix
            )
            return
        }
        val featureDirectory = getFeatureDirectory(event, feature) ?: run {
            sendFeatureError(
                event,
                MissingFeatureAttribute.PACKAGE,
                UpdaterSupportFile.suffix
            )
            return
        }
        createSubDirectory(featureDirectory, UpdaterSupportFile.packageName)
        val directory = getDirectoryFromParent(featureDirectory, UpdaterSupportFile.packageName) ?: run {
            sendPackageError(event, UpdaterSupportFile.packageName)
            return
        }
        val stateValue = getStateValue(event) ?: run {
            sendErrorNotification(
                event,
                NotificationGroupIds.FILE_PARSING_ERROR,
                "Error parsing val from state",
                "The file for ${UpdaterSupportFile.packageName} was not created. Try creating the package yourself and then running this command again."
            )
            return
        }
        createFile(
            event,
            directory,
            UpdaterFromStateTemplate(
                stateValue.name,
                stateValue.type,
                feature,
                directory.getFeatureDirectoryPath(feature),
                getBasePackage(event)
            )
        )
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = isStateValue(event)
    }
}