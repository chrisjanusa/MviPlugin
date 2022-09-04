package com.janusa.mvi.mvi.helpers

import com.intellij.openapi.actionSystem.AnActionEvent

fun enableIfDirectory(event: AnActionEvent) {
    event.presentation.isEnabledAndVisible = isDirectory(event)
}

fun enableIfAnyEnabled(event: AnActionEvent) {
    event.presentation.isEnabledAndVisible = isDirectory(event)
}