package com.janusa.mvi.mvi.screen

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.janusa.mvi.mvi.helpers.*
import com.janusa.mvi.mvi.ui.NameDialog
import com.janusa.mvi.mvi.ui.NameHolder
import java.io.FileNotFoundException

open class CreateSupportFileAction(private val fileType: SupportFile) : AnAction("Create _${fileType.suffix}") {
    override fun actionPerformed(event: AnActionEvent) {
        val feature = getFeature(event)
            ?: throw FileNotFoundException("Could not calculate feature name - Please try again basing off an MVI Screen implementation")
        val featureDirectory = getFeatureDirectory(event, feature)
            ?: throw FileNotFoundException("Could not find the feature package with name equal to lowercase feature name")
        createSubDirectory(featureDirectory, fileType.packageName)
        val directory = getDirectoryFromParent(featureDirectory, fileType.packageName)
            ?: throw FileNotFoundException("Could not create ${fileType.packageName} package")
        val nameHolder = NameHolder()
        val dialog = NameDialog(
            "Create New ${fileType.suffix}",
            "Enter the name of the ${fileType.suffix}",
            "${fileType.suffix} will be appended to the end automatically",
            nameHolder,
            listOf(fileType.suffix)
        )
        dialog.showAndGet()
        createFile(
            event,
            directory,
            fileType.getTemplate(nameHolder.name, feature, directory.getFeatureDirectoryPath(feature))
        )
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = featureIsObtainable(event)
    }
}