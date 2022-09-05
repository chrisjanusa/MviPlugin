package com.janusa.mvi.mvi.helpers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.janusa.mvi.mvi.action.ActionFeatureObtainer
import com.janusa.mvi.mvi.action.ActionPackageFeatureObtainer
import com.janusa.mvi.mvi.screen.ScreenFeatureObtainer
import com.janusa.mvi.mvi.screen.StateFeatureObtainer
import com.janusa.mvi.mvi.screen.ViewModelObtainer

val featureFileObtainers = listOf(
    ActionFeatureObtainer,
    StateFeatureObtainer,
    ScreenFeatureObtainer,
    ViewModelObtainer,
    ActionPackageFeatureObtainer
)

fun getFeature(event: AnActionEvent): String? {
    val psiFile = event.getData(PlatformDataKeys.PSI_ELEMENT) ?: return null
    featureFileObtainers.forEach {
        if (it.sameFileType(psiFile)) {
            val feature = it.getFeature(psiFile)
            if (feature != null) {
                return feature
            }
        }
    }
    return null
}

fun getFeatureDirectory(event: AnActionEvent, featureName: String): PsiDirectory? {
    var virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE)
    virtualFile = getFeatureDirectory(virtualFile, featureName)
    if (virtualFile?.isDirectory == true) {
        val project = event.getData(PlatformDataKeys.PROJECT) ?: return null
        return PsiManager.getInstance(project).findDirectory(virtualFile)
    } else {
        return null
    }
}

private fun getFeatureDirectory(virtualFile: VirtualFile?, featureName: String): VirtualFile? {
    var currFile = virtualFile
    while (currFile != null && currFile.name != featureName.lowercase()) {
        currFile = currFile.parent
    }
    return currFile
}

fun PsiDirectory.getFeatureDirectoryPath(featureName: String) =
    getFeatureDirectory(virtualFile, featureName)?.getPackage() ?: ""

interface FeatureObtainer {
    fun sameFileType(psiElement: PsiElement): Boolean

    fun getFeature(psiElement: PsiElement): String?
}