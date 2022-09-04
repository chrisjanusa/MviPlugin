package com.janusa.mvi.mvi.groups

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.janusa.mvi.mvi.helpers.enableIfAnyEnabled


class NewMVIGroup : DefaultActionGroup() {
    override fun update(event: AnActionEvent) {
        enableIfAnyEnabled(event)
    }
}
