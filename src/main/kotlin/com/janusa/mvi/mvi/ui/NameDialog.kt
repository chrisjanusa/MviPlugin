package com.janusa.mvi.mvi.ui

import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class NameDialog(
    title: String,
    private val prompt: String,
    private val hint: String,
    private val result: NameHolder,
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
        result: NameHolder,
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
    }
}

data class NameHolder(
    var name: String = ""
)