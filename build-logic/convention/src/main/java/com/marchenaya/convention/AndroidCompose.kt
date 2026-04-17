package com.marchenaya.convention

import com.android.build.api.dsl.CommonExtension
import com.marchenaya.convention.Constants.ANDROID_TEST_IMPLEMENTATION
import com.marchenaya.convention.Constants.IMPLEMENTATION
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

private const val ANDROIDX_COMPOSE_BOM = "androidx.compose.bom"
private const val API = "api"
private const val ANDROIDX_COMPOSE_UI_TOOLING_PREVIEW = "androidx.compose.ui.tooling.preview"

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension
) {
    commonExtension.buildFeatures.apply { compose = true }

    this@configureAndroidCompose.dependencies {
        val bom = libs.findLibrary(ANDROIDX_COMPOSE_BOM).get()
        API(platform(bom))
        IMPLEMENTATION(platform(bom))
        ANDROID_TEST_IMPLEMENTATION(platform(bom))
        IMPLEMENTATION(libs.findLibrary(ANDROIDX_COMPOSE_UI_TOOLING_PREVIEW).get())
    }
}