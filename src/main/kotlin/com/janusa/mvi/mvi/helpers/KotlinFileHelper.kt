package com.janusa.mvi.mvi.helpers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.impl.EditorImpl
import com.janusa.mvi.mvi.screen.stateFileSuffix

fun getStateValue(event: AnActionEvent): StateValue? {
    val value = event.getData(PlatformDataKeys.EDITOR) ?: return null
    val document = value.document
    if (!(value as EditorImpl).virtualFile.name.contains(stateFileSuffix)) return null
    val position = value.caretModel.currentCaret.visualPosition
    val valueLine = document.text.split("\n")[position.line].substringAfter("val ", "")
    val name = valueLine.substringBefore(":", "")
    val type = valueLine.substringAfter(": ", "").substringBefore(" ")
    if (name.isBlank() || type.isBlank()) return null
    return StateValue(name, type)
}

data class StateValue(
    val name: String,
    val type: String
)