package com.marchenaya.convention

import com.marchenaya.convention.Constants.ANDROID_TEST_IMPLEMENTATION
import com.marchenaya.convention.Constants.IMPLEMENTATION
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

private const val CORE_PRESENTATION_UI = ":core:presentation:ui"
private const val CORE_PRESENTATION_DESIGN_SYSTEM = ":core:presentation:designsystem"
private const val KOIN_COMPOSE = "koin.compose"
private const val COMPOSE = "compose"
private const val COMPOSE_DEBUG = "compose.debug"
private const val DEBUG_IMPLEMENTATION = "debugImplementation"
private const val ANDROIDX_COMPOSE_UI_TEST_JUNIT_4 = "androidx.compose.ui.test.junit4"

fun DependencyHandlerScope.addUiLayerDependencies(project: Project) {
    IMPLEMENTATION(project(CORE_PRESENTATION_UI))
    IMPLEMENTATION(project(CORE_PRESENTATION_DESIGN_SYSTEM))

    IMPLEMENTATION(project.libs.findBundle(KOIN_COMPOSE).get())
    IMPLEMENTATION(project.libs.findBundle(COMPOSE).get())
    DEBUG_IMPLEMENTATION(project.libs.findBundle(COMPOSE_DEBUG).get())
    ANDROID_TEST_IMPLEMENTATION(project.libs.findLibrary(ANDROIDX_COMPOSE_UI_TEST_JUNIT_4).get())
}