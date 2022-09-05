package com.janusa.mvi.mvi.helpers

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

enum class NotificationGroupIds(val groupId: String) {
    GRADLE_ERROR("Gradle Error"),
    PACKAGE_CREATION_ERROR("Package Creation Error"),
    FILE_PARSING_ERROR("File Parsing Error"),
    FEATURE_ERROR("Feature Error")
}

fun sendErrorNotification(event: AnActionEvent, groupId: NotificationGroupIds, title: String, content: String) {
    val project = event.getRequiredData(CommonDataKeys.PROJECT)
    val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup(groupId.groupId)
    notificationGroup.createNotification(title, content, NotificationType.ERROR)
        .notify(project)
}

enum class MissingFeatureAttribute(val type: String) {
    PACKAGE("package"),
    NAME("name")
}

fun sendFeatureError(event: AnActionEvent, missingFeatureAttribute: MissingFeatureAttribute, fileType: String) {
    sendErrorNotification(
        event,
        NotificationGroupIds.FEATURE_ERROR,
        "Error calculating Feature/Screen ${missingFeatureAttribute.type}",
        "The $fileType file was not created. ${if (missingFeatureAttribute == MissingFeatureAttribute.PACKAGE) "Expecting feature package with name equal to lowercase feature name. " else ""}Make sure you are within a Screen/Feature package and try again."
    )
}

fun sendPackageError(event: AnActionEvent, packageName: String) {
    sendErrorNotification(
        event,
        NotificationGroupIds.PACKAGE_CREATION_ERROR,
        "Error creating $packageName package",
        "The file for $packageName was not created. Try creating the package yourself and then running this command again."
    )
}