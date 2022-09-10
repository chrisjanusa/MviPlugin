package com.janusa.mvi.mvi.helpers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.util.ThrowableRunnable
import com.janusa.mvi.mvi.base.BasePathService
import org.jetbrains.kotlin.idea.KotlinIconProviderService
import javax.swing.Icon


fun isDirectory(event: AnActionEvent): Boolean {
    val virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE) ?: return false
    return virtualFile.isDirectory && virtualFile.parent.parent.path.contains("main") && !virtualFile.path.contains("res")
}

fun createDirectory(event: AnActionEvent, name: String) {
    val parentDirectory = event.getData(PlatformDataKeys.PSI_ELEMENT) as? PsiDirectory ?: return
    if (parentDirectory.findSubdirectory(name) == null) {
        WriteAction.run(ThrowableRunnable { parentDirectory.createSubdirectory(name) })
    }
}

fun createSubDirectory(parentDirectory: PsiDirectory, name: String) {
    if (parentDirectory.findSubdirectory(name) == null) {
        WriteAction.run(ThrowableRunnable { parentDirectory.createSubdirectory(name) })
    }
}

fun getDirectory(event: AnActionEvent, name: String): PsiDirectory? {
    val parentDirectory = event.getData(PlatformDataKeys.PSI_ELEMENT) as? PsiDirectory ?: return null
    return parentDirectory.findSubdirectory(name)
}

fun getDirectoryFromParent(parentDirectory: PsiDirectory, name: String): PsiDirectory? {
    return parentDirectory.findSubdirectory(name)
}

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

object KotlinFileType : FileType {
    override fun getName() = "Kotlin"

    override fun getDescription() = "Kotlin"

    override fun getDefaultExtension() = ".kt"

    override fun getIcon(): Icon = KotlinIconProviderService.getInstance().fileIcon

    override fun isBinary() = false
}

open class FileTemplate(val title: String, val content: String)

fun PsiDirectory.getPackage() = virtualFile.getPackage()

fun VirtualFile.getPackage(): String {
    return path.pathToPackage()
}

fun String.pathToPackage(): String {
    return this.substringAfter("main/").substringAfter("/").replace('/', '.')
}

fun PsiElement.getFileName() = this.containingFile?.name

fun PsiElement.getFileText() = this.containingFile?.text

private fun getBasePath(event: AnActionEvent): String? {
    val project = event.getData(PlatformDataKeys.PROJECT) ?: return null
    return BasePathService.getService(project).basePath
}

fun getBasePackage(event: AnActionEvent): String {
    return getBasePath(event)?.pathToPackage() ?: ""
}

fun saveBasePath(event: AnActionEvent, path: String) {
    val project = event.getData(PlatformDataKeys.PROJECT) ?: return
    BasePathService.getService(project).basePath = path
}


fun getBase(event: AnActionEvent): String? {
    val virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE) ?: return null
    val basePathStored = getBasePath(event)
    if (!basePathStored.isNullOrBlank()) {
        return basePathStored
    }
    if (virtualFile.parent.name == "base") {
        return virtualFile.parent.path
    }
    return null
}

fun getBaseDirectory(event: AnActionEvent): PsiDirectory? {
    val basePath = getBase(event) ?: return null
    val virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE) ?: return null
    val virtualBaseDir = virtualFile.fileSystem.findFileByPath(basePath) ?: return null
    val project = event.getData(PlatformDataKeys.PROJECT) ?: return null
    return PsiManager.getInstance(project).findDirectory(virtualBaseDir)
}