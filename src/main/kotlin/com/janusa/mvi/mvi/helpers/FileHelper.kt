package com.janusa.mvi.mvi.helpers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.util.ThrowableRunnable
import com.janusa.mvi.mvi.screen.stateFileSuffix
import org.jetbrains.kotlin.idea.KotlinIconProviderService
import javax.swing.Icon

fun createFile(event: AnActionEvent, directory: PsiDirectory, template: FileTemplate) {
    if (directory.findFile("${template.title}.kt") != null) return
    val project = event.getData(PlatformDataKeys.PROJECT) ?: return
    val packageName = "package ${directory.getPackage()}"
    val fileContent = "$packageName\n\n${template.content}"
    WriteAction.run(ThrowableRunnable {
        val file =
            PsiFileFactory.getInstance(project).createFileFromText("${template.title}.kt", KotlinFileType, fileContent)
        directory.add(file)
    })
}

open class FileTemplate(val title: String, val content: String)

fun PsiElement.getFileName() = this.containingFile?.name

fun PsiElement.getFileText() = this.containingFile?.text

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

object KotlinFileType : FileType {
    override fun getName() = "Kotlin"

    override fun getDescription() = "Kotlin"

    override fun getDefaultExtension() = ".kt"

    override fun getIcon(): Icon = KotlinIconProviderService.getInstance().fileIcon

    override fun isBinary() = false
}
