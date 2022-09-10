package com.janusa.mvi.mvi.base

import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.bind
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class AddDependencyDialog(
    title: String,
    private val result: UseDependencyHolder,
) : DialogWrapper(true) {
    init {
        init()
        this.title = title
    }

    override fun createCenterPanel(): JComponent = createNameDialogPanel(result)

    private fun createNameDialogPanel(
        result: UseDependencyHolder,
    ): DialogPanel = panel {
        buttonsGroup("Attempt to Add Dependencies") {
            row {
                radioButton("Yes", true)
            }
            row {
                radioButton("No", false)
            }
        }.bind(result::addDependency)
        group("Dependencies to Be Added", indent = true) {
            row("implementation 'androidx.navigation:navigation-compose") {}
            row("implementation 'io.github.raamcosta.compose-destinations:core") {}
            row("ksp 'io.github.raamcosta.compose-destinations:ksp'") {}
            row("id 'com.google.devtools.ksp'") {}
        }
    }
}

data class UseDependencyHolder(
    var addDependency: Boolean = true
)