package com.janusa.mvi.mvi.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.janusa.mvi.mvi.helpers.*
import com.janusa.mvi.mvi.ui.NameDialog
import com.janusa.mvi.mvi.ui.NameHolder
import java.io.FileNotFoundException

class CreateActionFromFeatureAction : AnAction("Create _Action") {
    override fun actionPerformed(event: AnActionEvent) {
        val feature = getFeature(event)
            ?: throw FileNotFoundException("Could not calculate feature name - Please try again basing off an MVI Screen implementation")
        val featureDirectory = getFeatureDirectory(event, feature)
            ?: throw FileNotFoundException("Could not find the feature package with name equal to lowercase feature name")
        createSubDirectory(featureDirectory, "actions")
        val directory = getDirectoryFromParent(featureDirectory, "actions")
            ?: throw FileNotFoundException("Could not create actions package")
        val nameHolder = NameHolder()
        val dialog = NameDialog(
            "Create New Action",
            "Enter the Name of the Action",
            "Action will be appended to the end automatically",
            nameHolder,
            listOf(actionFileSuffix)
        )
        dialog.showAndGet()
        createFile(
            event,
            directory,
            ActionTemplate(nameHolder.name, feature, directory.getFeatureDirectoryPath(feature))
        )
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = featureIsObtainable(event)
    }
}