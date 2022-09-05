package com.janusa.mvi.mvi.screen

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.childrenOfType
import com.janusa.mvi.mvi.helpers.FeatureObtainer
import com.janusa.mvi.mvi.helpers.FileTemplate

sealed class SupportFile(
    val packageName: String,
    val suffix: String,
    val getTemplate: (String, String, String) -> FileTemplate
)

open class SupportFileTemplate(name: String, content: String, fileType: SupportFile) : FileTemplate(
    title = "$name${fileType.suffix}",
    content = content
)

open class SupportFeatureObtainer(private val fileType: SupportFile) : FeatureObtainer {

    override fun sameFileType(psiElement: PsiElement) =
        (psiElement as? PsiFile)?.name?.contains(fileType.suffix) == true

    override fun getFeature(psiElement: PsiElement) =
        (psiElement as? PsiFile)?.text?.substringAfter("Base${fileType.suffix}<")?.substringBefore("State")

}

open class SupportPackageFeatureObtainer(private val fileType: SupportFile) : FeatureObtainer {

    override fun sameFileType(psiElement: PsiElement) = (psiElement as? PsiDirectory)?.name == fileType.packageName

    override fun getFeature(psiElement: PsiElement): String? {
        val parentDirectory = (psiElement as? PsiDirectory)?.parent
        val featureScreen = parentDirectory?.childrenOfType<PsiFile>()?.first { it.name.contains("Screen") }
        return featureScreen?.name?.substringBefore("Screen")
    }

}