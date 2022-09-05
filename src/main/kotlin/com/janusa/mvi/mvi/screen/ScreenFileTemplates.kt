package com.janusa.mvi.mvi.screen

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.janusa.mvi.mvi.helpers.FeatureObtainer
import com.janusa.mvi.mvi.helpers.FileTemplate
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly

const val screenFileSuffix = "Screen"

object ScreenFeatureObtainer : CoreFeatureObtainer(screenFileSuffix)

class ScreenTemplate(name: String, firstScreen: Boolean, initAction: Boolean) : FileTemplate(
    title = "$name$screenFileSuffix",
    content = "import androidx.compose.runtime.Composable\n" +
            "import com.ramcosta.composedestinations.annotation.Destination\n" +
            "import com.ramcosta.composedestinations.navigation.DestinationsNavigator\n" +
            "\n" +
            "@Destination${if (firstScreen) "(start = true)" else ""}\n" +
            "@Composable\n" +
            "fun ${name}Screen(navigator: DestinationsNavigator) {\n" +
            "\tval viewModel = ${name.decapitalizeAsciiOnly()}ViewModel()\n" +
            "\tMVIScreen(navigator = navigator, viewModel = viewModel${if (initAction) ", Init${name}Action()" else ""}) { ${name}ScreenContent(it, viewModel.performAction) }\n" +
            "}\n" +
            "\n" +
            "@Composable\n" +
            "fun ${name}ScreenContent(state: ${name}State, performAction: (BaseAction<${name}State>) -> Unit) {\n" +
            "\n" +
            "}"
)

const val stateFileSuffix = "State"

object StateFeatureObtainer : CoreFeatureObtainer(stateFileSuffix)

class StateTemplate(name: String) : FileTemplate(
    title = "$name$stateFileSuffix",
    content = "data class ${name}State(\n" +
            "\tval replaceThis: String = \"\",\n" +
            ")"
)

const val viewModelFileSuffix = "ViewModel"

object ViewModelObtainer : CoreFeatureObtainer(viewModelFileSuffix)

class ViewModelTemplate(name: String) : FileTemplate(
    title = "$name$viewModelFileSuffix",
    content = "import android.os.Bundle\n" +
            "import androidx.compose.runtime.Composable\n" +
            "import androidx.compose.ui.platform.LocalSavedStateRegistryOwner\n" +
            "import androidx.lifecycle.AbstractSavedStateViewModelFactory\n" +
            "import androidx.lifecycle.SavedStateHandle\n" +
            "import androidx.lifecycle.ViewModel\n" +
            "import androidx.lifecycle.ViewModelStoreOwner\n" +
            "import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner\n" +
            "import androidx.navigation.NavBackStackEntry\n" +
            "import androidx.savedstate.SavedStateRegistryOwner\n" +
            "\n" +
            "class ${name}ViewModel : MVIViewModel<${name}State>(${name}State())\n" +
            "\n" +
            "@Composable fun ${name.decapitalizeAsciiOnly()}ViewModel(\n" +
            "    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {\n" +
            "        \"No ViewModelStoreOwner was provided via LocalViewModelStoreOwner\"\n" +
            "    },\n" +
            "    savedStateRegistryOwner: SavedStateRegistryOwner = LocalSavedStateRegistryOwner.current\n" +
            "): ${name}ViewModel {\n" +
            "    return androidx.lifecycle.viewmodel.compose.viewModel(\n" +
            "        viewModelStoreOwner = viewModelStoreOwner,\n" +
            "        factory = ${name}ViewModelFactory(\n" +
            "            owner = savedStateRegistryOwner,\n" +
            "            defaultArgs = (savedStateRegistryOwner as? NavBackStackEntry)?.arguments\n" +
            "        )\n" +
            "    )\n" +
            "}\n" +
            "\n" +
            "class ${name}ViewModelFactory(\n" +
            "    owner: SavedStateRegistryOwner,\n" +
            "    defaultArgs: Bundle?,\n" +
            ") : AbstractSavedStateViewModelFactory(\n" +
            "    owner,\n" +
            "    defaultArgs\n" +
            ") {\n" +
            "\n" +
            "    @Suppress(\"UNCHECKED_CAST\")\n" +
            "    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {\n" +
            "        return when (modelClass) {\n" +
            "            ${name}ViewModel::class.java -> ${name}ViewModel()\n" +
            "\n" +
            "            else -> throw RuntimeException(\"Unknown view model \$modelClass\")\n" +
            "        } as T\n" +
            "    }\n" +
            "}"
)

open class CoreFeatureObtainer(private val suffix: String) : FeatureObtainer {

    override fun sameFileType(psiElement: PsiElement) = (psiElement as? PsiFile)?.name?.contains(suffix) == true

    override fun getFeature(psiElement: PsiElement) = (psiElement as? PsiFile)?.name?.substringBefore(suffix)

}