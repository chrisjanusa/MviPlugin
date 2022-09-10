package com.janusa.mvi.mvi.screen

object EventSupportFile : SupportFile(
    "events",
    "Event",
    { name, _, _, basePackage ->
        EventTemplate(name, basePackage)
    }
)

object EventFeatureObtainer : SupportFeatureObtainer(EventSupportFile)

object EventPackageFeatureObtainer : SupportPackageFeatureObtainer(EventSupportFile)

class EventTemplate(name: String, basePackage: String) : SupportFileTemplate(
    name = name,
    content = (if (basePackage.isNotBlank()) "import $basePackage.BaseEvent\n" else "") +
            "import android.content.Context\n" +
            "import com.ramcosta.composedestinations.navigation.DestinationsNavigator\n" +
            "\n" +
            "class ${name}Event() : BaseEvent {\n" +
            "    override fun performEvent(context: Context, navigator: DestinationsNavigator) {\n" +
            "        TODO(\"Not yet implemented\")\n" +
            "    }\n" +
            "}",
    fileType = EventSupportFile
)