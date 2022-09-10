package com.janusa.mvi.mvi.helpers

fun String.capitalize() = replaceFirstChar { it.uppercase() }
fun String.decapitalize() = replaceFirstChar { it.lowercase() }