package com.janusa.mvi.mvi.screen

import com.janusa.mvi.mvi.helpers.FileTemplate
import com.janusa.mvi.mvi.helpers.capitalize

object UpdaterSupportFile : SupportFile(
    "updaters",
    "Updater",
    { name, feature, featurePackage, basePackage ->
        UpdaterTemplate(name, feature, featurePackage, basePackage)
    }
)

object UpdaterFeatureObtainer : SupportFeatureObtainer(UpdaterSupportFile)

class UpdaterTemplate(name: String, feature: String, featurePackage: String, basePackage: String) : SupportFileTemplate(
    name = name,
    content = (if (basePackage.isNotBlank()) "import $basePackage.BaseUpdater\n" else "") +
            "import $featurePackage.${feature}State\n" +
            "\n" +
            "class ${name}Updater() : BaseUpdater<${feature}State> {\n" +
            "    override fun performUpdate(prevState: ${feature}State): ${feature}State {\n" +
            "        return prevState.copy()\n" +
            "    }\n" +
            "}",
    fileType = UpdaterSupportFile
)

object UpdaterPackageFeatureObtainer : SupportPackageFeatureObtainer(UpdaterSupportFile)

class UpdaterFromStateTemplate(
    valueName: String,
    valueType: String,
    feature: String,
    featurePackage: String,
    basePackage: String
) : FileTemplate(
    title = "${valueName.capitalize()}${UpdaterSupportFile.suffix}",
    content = (if (basePackage.isNotBlank()) "import $basePackage.BaseUpdater\n" else "") +
            "import $featurePackage.${feature}State\n" +
            "\n" +
            "class ${valueName.capitalize()}Updater(private val new${valueName.capitalize()}: $valueType) : BaseUpdater<${feature}State> {\n" +
            "    override fun performUpdate(prevState: ${feature}State): ${feature}State {\n" +
            "        return prevState.copy($valueName = new${valueName.capitalize()})\n" +
            "    }\n" +
            "}",
)