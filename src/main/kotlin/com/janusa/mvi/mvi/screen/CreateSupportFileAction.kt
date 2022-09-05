package com.janusa.mvi.mvi.screen

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.janusa.mvi.mvi.helpers.*
import com.janusa.mvi.mvi.ui.NameDialog
import com.janusa.mvi.mvi.ui.NameHolder

open class CreateSupportFileAction(private val fileType: SupportFile) : AnAction("Create _${fileType.suffix}") {
    override fun actionPerformed(event: AnActionEvent) {
        val feature = getFeature(event) ?: run {
            sendFeatureError(
                event,
                MissingFeatureAttribute.NAME,
                fileType.suffix
            )
            return
        }
        val featureDirectory = getFeatureDirectory(event, feature) ?: run {
            sendFeatureError(
                event,
                MissingFeatureAttribute.PACKAGE,
                fileType.suffix
            )
            return
        }
        createSubDirectory(featureDirectory, fileType.packageName)
        val directory = getDirectoryFromParent(featureDirectory, fileType.packageName) ?: run {
            sendPackageError(
                event,
                fileType.packageName
            )
            return
        }
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
            fileType.getTemplate(
                nameHolder.name,
                feature,
                directory.getFeatureDirectoryPath(feature),
                getBasePackage(event)
            )
        )
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = featureIsObtainable(event)
    }
}