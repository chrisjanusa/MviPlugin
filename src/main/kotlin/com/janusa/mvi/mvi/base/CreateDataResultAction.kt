package com.janusa.mvi.mvi.base

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.janusa.mvi.mvi.helpers.*

class CreateDataResultAction : AnAction("Create _Data Result") {
    override fun actionPerformed(event: AnActionEvent) {
        val directory = getBaseDirectory(event) ?: run {
            sendPackageError(
                event,
                "base",
            )
            return
        }
        createFile(event, directory, DataResultTemplate)
    }

    override fun update(event: AnActionEvent) {
        // base accessible either stored or we are in the base package
        // DataResult does not exist in that package
        event.presentation.isEnabledAndVisible =
            baseIsObtainable(event) && !baseContainsFile(event, "${DataResultTemplate.title}.kt")
    }
}