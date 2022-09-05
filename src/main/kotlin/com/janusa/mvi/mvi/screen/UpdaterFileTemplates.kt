package com.janusa.mvi.mvi.screen

object UpdaterSupportFile : SupportFile(
    "updaters",
    "Updater",
    { name, feature, featurePackage ->
        UpdaterTemplate(name, feature, featurePackage)
    }
)

object UpdaterFeatureObtainer : SupportFeatureObtainer(UpdaterSupportFile)

class UpdaterTemplate(name: String, feature: String, featurePackage: String) : SupportFileTemplate(
    name = name,
    content = "import $featurePackage.${feature}State\n" +
            "\n" +
            "class ${name}Updater() : BaseUpdater<${feature}State> {\n" +
            "    override fun performUpdate(prevState: ${feature}State): ${feature}State {\n" +
            "        return prevState.copy()\n" +
            "    }\n" +
            "}",
    fileType = UpdaterSupportFile
)

object UpdaterPackageFeatureObtainer : SupportPackageFeatureObtainer(UpdaterSupportFile)