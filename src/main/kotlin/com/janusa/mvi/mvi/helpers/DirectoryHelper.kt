package com.janusa.mvi.mvi.helpers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import com.intellij.util.ThrowableRunnable

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

fun PsiDirectory.getPackage() = virtualFile.getPackage()

fun VirtualFile.getPackage(): String {
    return path.pathToPackage()
}

fun String.pathToPackage(): String {
    return this.substringAfter("main/").substringAfter("/").replace('/', '.')
}

fun getBaseDirectory(event: AnActionEvent): PsiDirectory? {
    val basePath = getBase(event) ?: return null
    val virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE) ?: return null
    val virtualBaseDir = virtualFile.fileSystem.findFileByPath(basePath) ?: return null
    val project = event.getData(PlatformDataKeys.PROJECT) ?: return null
    return PsiManager.getInstance(project).findDirectory(virtualBaseDir)
}