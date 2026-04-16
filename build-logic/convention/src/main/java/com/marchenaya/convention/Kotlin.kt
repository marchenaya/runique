package com.marchenaya.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private const val PROJECT_COMPILE_SDK = "projectCompileSdkVersion"
private const val PROJECT_MIN_SDK = "projectMinSdkVersion"
private const val CORE_LIBRARY_DESUGARING = "coreLibraryDesugaring"
private const val DESUGAR_JDK_LIBS = "desugar.jdk.libs"

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension
) {
    commonExtension.apply {
        compileSdk = libs.findVersion(PROJECT_COMPILE_SDK).get().toString().toInt()

        defaultConfig.minSdk = libs.findVersion(PROJECT_MIN_SDK).get().toString().toInt()

        compileOptions.apply {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    configureKotlin()

    dependencies {
        CORE_LIBRARY_DESUGARING(libs.findLibrary(DESUGAR_JDK_LIBS).get())
    }
}

private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}