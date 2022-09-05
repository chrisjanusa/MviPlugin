package com.janusa.mvi.mvi.helpers

import com.intellij.openapi.actionSystem.AnActionEvent

fun featureIsObtainable(event: AnActionEvent): Boolean {
    return getFeature(event) != null
}

fun enableIfAnyEnabled(event: AnActionEvent) {
    event.presentation.isEnabledAndVisible = isDirectory(event) || featureIsObtainable(event)
}