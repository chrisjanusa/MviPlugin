package com.janusa.mvi.mvi.helpers

import com.intellij.openapi.actionSystem.AnActionEvent

fun addNavigationDeps(buildFileManager: GradleFileManager, event: AnActionEvent) {
    buildFileManager.addDependencyComment("Navigation")
    buildFileManager.addDependency("implementation", "androidx.navigation:navigation-compose", "2.5.2")
    buildFileManager.addDependency("implementation", "io.github.raamcosta.compose-destinations:core", "1.7.17-beta")
    buildFileManager.addDependency("ksp", "io.github.raamcosta.compose-destinations:ksp", "1.7.17-beta")
    buildFileManager.addAndroid(
        "\tapplicationVariants.all { variant ->\n" +
                "\t\tkotlin.sourceSets {\n" +
                "\t\t\tgetByName(variant.name) {\n" +
                "\t\t\t\tkotlin.srcDir(\"build/generated/ksp/\${variant.name}/kotlin\")\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}",
        "kotlin.srcDir(\"build/generated/ksp/\${variant.name}/kotlin\")"
    )
    val kotlinVersion = getKotlinVersion(event)
    if (kotlinVersion == null) {
        sendErrorNotification(
            event,
            NotificationGroupIds.GRADLE_ERROR,
            "Error finding kotlin version from project build.gradle file",
            "ksp plugin was not added automatically so make sure to add it manually"
        )
    } else {
        buildFileManager.addPluginComment("This must match the kotlin version")
        buildFileManager.addPlugin("com.google.devtools.ksp", "$kotlinVersion+")
    }
}