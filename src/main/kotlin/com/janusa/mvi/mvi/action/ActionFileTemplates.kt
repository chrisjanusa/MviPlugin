package com.janusa.mvi.mvi.action

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.childrenOfType
import com.janusa.mvi.mvi.helpers.FeatureObtainer
import com.janusa.mvi.mvi.helpers.FileTemplate

const val actionFileSuffix = "Action"

object ActionFeatureObtainer : FeatureObtainer {

    override fun sameFileType(psiElement: PsiElement) =
        (psiElement as? PsiFile)?.name?.contains(actionFileSuffix) == true

    override fun getFeature(psiElement: PsiElement) =
        (psiElement as? PsiFile)?.text?.substringAfter("<")?.substringBefore("State")

}

class ActionTemplate(name: String, feature: String, featurePackage: String) : FileTemplate(
    title = "$name$actionFileSuffix",
    content = "import $featurePackage.${feature}State\n" +
            "\n" +
            "class ${name}Action() : BaseAction<${feature}State> {\n" +
            "    override suspend fun performAction(\n" +
            "        currentState: ${feature}State, \n" +
            "        sendUpdate: (BaseUpdater<${feature}State>) -> Unit, \n" +
            "        sendEvent: (BaseEvent) -> Unit\n" +
            "    ) {\n" +
            "        TODO(\"Not yet implemented\")\n" +
            "    }\n" +
            "}"
)

object ActionPackageFeatureObtainer : FeatureObtainer {

    override fun sameFileType(psiElement: PsiElement) = (psiElement as? PsiDirectory)?.name == "actions"

    override fun getFeature(psiElement: PsiElement): String? {
        val parentDirectory = (psiElement as? PsiDirectory)?.parent
        val featureScreen = parentDirectory?.childrenOfType<PsiFile>()?.first { it.name.contains("Screen") }
        return featureScreen?.name?.substringBefore("Screen")
    }

}