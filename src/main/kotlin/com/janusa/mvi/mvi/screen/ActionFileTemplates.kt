package com.janusa.mvi.mvi.screen

object ActionSupportFile : SupportFile(
    "actions",
    "Action",
    { name, feature, featurePackage, basePackage ->
        ActionTemplate(name, feature, featurePackage, basePackage)
    }
)

object ActionFeatureObtainer : SupportFeatureObtainer(ActionSupportFile)

object ActionPackageFeatureObtainer : SupportPackageFeatureObtainer(ActionSupportFile)

class ActionTemplate(name: String, feature: String, featurePackage: String, basePackage: String) : SupportFileTemplate(
    name = name,
    content = (if (basePackage.isNotBlank()) "import $basePackage.BaseAction\n" +
            "import $basePackage.BaseEvent\n" +
            "import $basePackage.BaseUpdater\n" else "") +
            "import $featurePackage.${feature}State\n" +
            "\n" +
            "class ${name}Action() : BaseAction<${feature}State> {\n" +
            "    override suspend fun performAction(\n" +
            "        currentState: ${feature}State, \n" +
            "        sendUpdater: (BaseUpdater<${feature}State>) -> Unit, \n" +
            "        sendEvent: (BaseEvent) -> Unit\n" +
            "    ) {\n" +
            "        TODO(\"Not yet implemented\")\n" +
            "    }\n" +
            "}",
    fileType = ActionSupportFile
)