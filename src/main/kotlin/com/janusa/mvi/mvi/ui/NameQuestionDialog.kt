package com.janusa.mvi.mvi.ui

import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class NameQuestionDialog(
    title: String,
    private val prompt: String,
    private val hint: String,
    private val result: ScreenPromptHolder,
    private val filesSuffixes: List<String>
) : DialogWrapper(true) {
    init {
        init()
        this.title = title
    }

    override fun createCenterPanel(): JComponent = createNameDialogPanel(prompt, hint, result, filesSuffixes)

    private fun createNameDialogPanel(
        prompt: String,
        hint: String,
        result: ScreenPromptHolder,
        filesSuffixes: List<String>
    ): DialogPanel = panel {
        row(prompt) {
            textField().comment(hint).bindText(result::name)
        }
        group("Files to Be Generated", indent = true) {
            filesSuffixes.forEach {
                row("${result.name}$it.kt") {}
            }
        }
        row {
            checkBox("This is the first screen shown").bindSelected(result::isStartScreen)
        }
        row {
            checkBox("Auto create init action for screen").bindSelected(result::initAction)
        }
    }
}

data class ScreenPromptHolder(
    var name: String = "",
    var initAction: Boolean = false,
    var isStartScreen: Boolean = false
)