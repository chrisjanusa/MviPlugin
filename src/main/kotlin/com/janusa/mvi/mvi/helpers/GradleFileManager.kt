package com.janusa.mvi.mvi.helpers

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Document

class GradleFileManager(private val buildGradle: Document) {
    private var currentText = buildGradle.text

    fun addDependency(configuration: String, repository: String, version: String) {
        addDependency("\t$configuration '$repository:$version'") { line -> line.contains(repository) }
    }

    fun addDependencyComment(comment: String) {
        addDependency("\n\t// $comment") { line -> line.contains(comment) }
    }

    fun addDependency(text: String, skipInsertIfTrue: (String) -> Boolean = { false }) {
        val documentText =
            currentText.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sb = StringBuilder()
        var counter = 0
        var canSearch = false
        for (i in documentText.indices) {
            val line = documentText[i]
            if (canSearch) {
                if (line.contains("{")) {
                    counter += 1
                }
                if (skipInsertIfTrue(line)) {
                    canSearch = false
                }
                if (line.contains("}")) {
                    if (counter > 0) {
                        counter -= 1
                    } else {
                        canSearch = false
                        sb.append(text).append("\n")
                    }
                }
            }
            if (line.contains(DEPENDENCIES)) {
                val tempLine = line.replace(DEPENDENCIES, "")
                if (tempLine.trim().equals("{", true)) {
                    canSearch = true
                }
            }
            sb.append(line).append("\n")
        }
        currentText = sb.toString()
    }

    fun addPlugin(id: String, version: String) {
        val plugin = if (version.isNotBlank()) {
            "\tid '$id' version '$version'"
        } else {
            "\tid '$id'"
        }
        addPluginElement(plugin) { line -> line.contains(id) }
    }

    fun addPluginComment(comment: String) {
        addPluginElement("\n\t// $comment") { line -> line.contains(comment) }
    }

    private fun addPluginElement(text: String, skipInsertIfTrue: (String) -> Boolean = { false }) {
        val documentText =
            currentText.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sb = StringBuilder()
        var counter = 0
        var canSearch = false
        for (i in documentText.indices) {
            val line = documentText[i]
            if (canSearch) {
                if (line.contains("{")) {
                    counter += 1
                }
                if (skipInsertIfTrue(line)) {
                    canSearch = false
                }
                if (line.contains("}")) {
                    if (counter > 0) {
                        counter -= 1
                    } else {
                        canSearch = false
                        sb.append(text).append("\n")
                    }
                }
            }
            if (line.contains(PLUGINS)) {
                val tempLine = line.replace(PLUGINS, "")
                if (tempLine.trim().equals("{", true)) {
                    canSearch = true
                }
            }
            sb.append(line).append("\n")
        }
        currentText = sb.toString()
    }

    fun addAndroid(text: String, keyTerm: String) {
        val documentText =
            currentText.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sb = StringBuilder()
        var counter = 0
        var canSearch = false
        for (i in documentText.indices) {
            val line = documentText[i]
            if (canSearch) {
                if (line.contains("{")) {
                    counter += 1
                }
                if (line.contains(keyTerm)) {
                    canSearch = false
                }
                if (line.contains("}")) {
                    if (counter > 0) {
                        counter -= 1
                    } else {
                        canSearch = false
                        sb.append(text).append("\n")
                    }
                }
            }
            if (line.contains(ANDROID)) {
                val tempLine = line.replace(ANDROID, "")
                if (tempLine.trim().equals("{", true)) {
                    canSearch = true
                }
            }
            sb.append(line).append("\n")
        }
        currentText = sb.toString()
    }

    fun writeToGradle() {
        val application = ApplicationManager.getApplication()
        application.invokeLater {
            application.runWriteAction { buildGradle.setText(currentText) }
        }
    }
}